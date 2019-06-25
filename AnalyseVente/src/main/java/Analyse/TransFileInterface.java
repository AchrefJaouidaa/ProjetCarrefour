package Analyse;

import java.io.File;

/**
 * cette classe est implémentée par FileSPlitter, ReducerAbstract
 * delimiter est le delimiter global de tous les fichiers
 * CUNK_SIZE est le max bytes en mémoire to load en lisant et en parcourant un fichier de n'importe quelle taille
 * fileTrans est le/les fichiers à transformer
 * fileDirectory est l'absolute directory du/ des fichiers
 */

public abstract class TransFileInterface {

  protected final static byte delimiter = 124;
  protected final static long CHUNK_SIZE = 4096;

  File[] fileToTrans;
  File fileDirectory;
  int indexDate;

  public TransFileInterface (int indexDate, String tempName, File... fileToTrans) {
    this.fileToTrans = fileToTrans;
    this.indexDate = indexDate;
    fileDirectory = new File (getAbsolPath (fileToTrans[0].toString ()) + tempName + "/" + indexDate + "/");
    if (!fileDirectory.exists ()){
      fileDirectory.mkdirs ();
    }
  }

  public String getAbsolPath (String path) {
    String[] pathArray = path.split ("/");
    int temp = pathArray[pathArray.length - 1].length ();
    return path.substring (0, path.length () - temp);
  }

  public abstract void TransFile ();

}