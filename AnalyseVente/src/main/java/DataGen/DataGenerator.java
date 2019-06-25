package DataGen;

import java.io.BufferedWriter;
import java.io.File;

/**
 * cette classe permet de generer les donnes par magasins et des transactions pour une periode donnée
 * on peut changer la taille du fichier des transactions avec transFileSize qui doit etre saisi en octet
 */
public class DataGenerator {
  private String startDate;
  private String endDate;
  private int mgNumber;
  private int startDateInt;
  private int endDateInt;


  public DataGenerator (String startDate,
                  String endDate,
                  int mgNumber) {
    this.endDate = endDate;
    this.startDate = startDate;
    this.mgNumber = mgNumber;
    this.startDateInt = Integer.parseInt (startDate);
    this.endDateInt = Integer.parseInt (endDate);
  }

  /**
   * cette methode permet de generer les fichiers magasins dans une periode donnée
   */

  public void generateMgData (String path, FIleNaming fileName) {
    int totalDays = endDateInt - startDateInt;
    String fileDate = Integer.toString (startDateInt);
    for (int i = 0; i < mgNumber; i++) {
      FileWriterInstance file = new FileWriterInstance ();
      String mgFileName = fileName.generateMgName (path, fileDate);
      BufferedWriter bw = file.getWriterInstance (mgFileName);
      MagasinDataGen MagasinFile = new MagasinDataGen ();
      MagasinFile.writeToFile (bw);
      try {
        bw.flush ();
        bw.close ();
      }
      catch (Exception e) {
        e.printStackTrace ();
      }
    }
    for (int i = 0; i < mgNumber; i++) {
      for (int j = 1; j <= totalDays; j++) {
        fileDate = Integer.toString (startDateInt + j);
        FileWriterInstance file = new FileWriterInstance ();
        String mgFileName = fileName.getMgName (path, fileDate, i);
        BufferedWriter bw = file.getWriterInstance (mgFileName);
        MagasinDataGen MagasinFile = new MagasinDataGen ();
        MagasinFile.writeToFile (bw);
        try {
          bw.flush ();
          bw.close ();
        }
        catch (Exception e) {
          e.printStackTrace ();
        }
      }
    }
  }

  /**
   * cette methode permet de generer les fichiers transactions dans une periode donnée et pour une taille donnée
   */
  public void generateTransData (String path,
                                 FIleNaming fileName,
                                 int Max_Bytes) {
    int totalDays = endDateInt - startDateInt;
    for (int i = 0; i <= totalDays; i++) {
      FileWriterInstance file = new FileWriterInstance ();
      String transFileName = fileName.generateTransName (path, startDateInt + i);
      BufferedWriter bw = file.getWriterInstance (transFileName);
      int k = startDateInt + i;
      TransDataGen transFile = new TransDataGen (fileName, mgNumber, "" + k, Max_Bytes);
      transFile.writeToFile (bw);
      try {
        bw.flush ();
        bw.close ();
      }
      catch (Exception e) {
        e.printStackTrace ();
      }
    }
  }

  public static void main (String[] args) {

    String absPath = args[2];
    if (!new File (absPath).exists ()) {
      new File (absPath).mkdirs ();
    }
    int mgNumber = Integer.parseInt (args[3]);
    DataGenerator x = new DataGenerator (args[0], args[1], mgNumber);
    FIleNaming fileName = new FIleNaming (mgNumber);
    int transFileSize = 1024 * 1024 * Integer.parseInt (args[4]);
    x.generateMgData (absPath, fileName);
    x.generateTransData (absPath, fileName, transFileSize);

  }
}
