package Analyse;


import com.google.common.io.Closeables;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * the FileReader implements Iterable. Each iteration will process next 4096 bytes of data and decode them into a list of objects using the Decoder. Notice that FileReader accept a list of files, which is nice since it enable traversal through the data without worrying about aggregation across files. By the way, 4096 byte chunks are probably a bit small for bigger files.
 *
 * @param <T> type de données à decoder
 */
public class FileReader <T> implements Iterable <List <T>> {
  private long CHUNK_SIZE;
  private final Decoder <T> decoder;
  private Iterator <File> files;

  private FileReader (Decoder <T> decoder,
                      long CHUNK_SIZE,
                      File... files) {
    this (decoder, CHUNK_SIZE, Arrays.asList (files));
  }

  private FileReader (Decoder <T> decoder,
                      long CHUNK_SIZE,
                      List <File> files) {
    this.files = files.iterator ();
    this.decoder = decoder;
    this.CHUNK_SIZE = CHUNK_SIZE;
  }

  public static <T> FileReader <T> create (Decoder <T> decoder,
                                           long CHUNK_SIZE,
                                           List <File> files) {
    return new FileReader <T> (decoder, CHUNK_SIZE, files);
  }

  public static <T> FileReader <T> create (Decoder <T> decoder,
                                           long CHUNK_SIZE,
                                           File... files) {
    return new FileReader <T> (decoder, CHUNK_SIZE, files);
  }

  /**
   * cette méthode permet d'itérer sur les fichiers , en lisant une taille de données fixeés par CHUNK_SIZE
   * @return est un Iterator
   */
  public Iterator <List <T>> iterator () {
    return new Iterator <List <T>> () {
      private List <T> entries;
      private long chunkPos = 0;
      private MappedByteBuffer buffer;
      private FileChannel channel;

      public boolean hasNext () {
        if ( buffer == null || !buffer.hasRemaining () ) {
          buffer = nextBuffer (chunkPos);
          if ( buffer == null ) {
            return false;
          }
        }
        T result = null;
        while ( (result = decoder.decode (buffer)) != null ) {
          if ( entries == null ) {
            entries = new ArrayList <T> ();
          }
          entries.add (result);
        }
        // set next MappedByteBuffer chunk
        chunkPos += buffer.position ();
        buffer = null;
        if ( entries != null ) {
          return true;
        }
        else {
          Closeables.closeQuietly (channel);
          return false;
        }
      }

      private MappedByteBuffer nextBuffer (long position) {
        try {
          if ( channel == null || channel.size () == position ) {
            if ( channel != null ) {
              Closeables.closeQuietly (channel);
              channel = null;
            }
            if ( files.hasNext () ) {
              File file = files.next ();
              channel = new RandomAccessFile (file, "r").getChannel ();
              chunkPos = 0;
              position = 0;
            }
            else {
              return null;
            }
          }
          long chunkSize = CHUNK_SIZE;
          if ( channel.size () - position < chunkSize ) {
            chunkSize = channel.size () - position;
          }
          return channel.map (FileChannel.MapMode.READ_ONLY, chunkPos, chunkSize);
        }
        catch (IOException e) {
          Closeables.closeQuietly (channel);
          throw new RuntimeException (e);
        }
      }

      public List <T> next () {
        List <T> res = entries;
        entries = null;
        return res;
      }

      public void remove () {
        throw new UnsupportedOperationException ();
      }
    };
  }
}
