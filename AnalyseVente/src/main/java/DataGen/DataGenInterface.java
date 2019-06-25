package DataGen;

import java.io.*;

/**
 * cette class est extended par MagasinDataGen et TransDataGen
 * max_bytes est le max taille du fichier
 */
public abstract class DataGenInterface {

  public int max_bytes;
  static int newLine_Bytes = getValue ();

  /**
   * permet de concatener plusieurs string
   * @param args sont les strings a concatener
   * @return
   */
  String concat (String... args) {
    StringBuilder line = new StringBuilder ();
    for (String arg : args) {
      line.append (arg);
      line.append ('|');
    }
    return line.toString ().substring (0, line.length () - 1);
  }

  static int getValue () {
    int tempVar = 0;
    try {
      tempVar = System.getProperty ("line.separator")
              .getBytes ("UTF-8").length;
    }
    catch (UnsupportedEncodingException e) {
      e.printStackTrace ();
    }
    return tempVar;
  }

  /**
   * permet d'ecrire les donnes generes dans un fichier d'un max_bytes (taille de fichier)
   * @param bw le writer dans le fichier
   */
  void writeToFile (BufferedWriter bw) {
    writeToFile (max_bytes, bw);
  }

  void writeToFile (int max_bytes, BufferedWriter bw) {
    int bytesWritten = 0;
    while ( existsMoreData () ) {
      try {
        String toWrite = getCurrentStringToWrite ();
        int bytesOfString = toWrite.getBytes ("UTF-8").length;
        if (bytesWritten + bytesOfString + newLine_Bytes > max_bytes) {
          continue;
        }
        else {
          bw.write (toWrite);
          bw.newLine ();
          bytesWritten += bytesOfString + newLine_Bytes;
        }
      }
      catch (IOException ie) {
        ie.printStackTrace ();
      }
    }
  }

  abstract Boolean existsMoreData ();

  abstract String getCurrentStringToWrite ();
}
