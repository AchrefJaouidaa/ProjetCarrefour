package DataGen;


import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * cette classe permet de creer les donnees de transactions
 * @param "fileName est le fichier des magasins
 * mgNumber est le nombre des magasins
 * str1 et str2 sont utilises pour creer les données de temps
 */
public class TransDataGen extends DataGenInterface {

  private FIleNaming fileName;
  private int mgNumber;
  private String transDate;
  private String str1;
  private String str2;

  public TransDataGen (FIleNaming fileName,
                       int mgNumber,
                       String transDate,
                       int max_bytes) {
    this.fileName = fileName;
    this.mgNumber = mgNumber;
    this.transDate = transDate;
    this.max_bytes = max_bytes;
    str1 = new StringBuilder (transDate).append (" 00:00:00").toString ();
    str2 = new StringBuilder (transDate).append (" 23:59:59").toString ();
  }
/** cette methode est utilisee pour creer le format du temps souhaite*/
  private String CreatedDateTime () {
    String text = null;
    try {
      Date startDate = new SimpleDateFormat ("yyyyMMdd hh:mm:ss").parse (str1);
      Date endDate = new SimpleDateFormat ("yyyyMMdd hh:mm:ss").parse (str2);
      long generatedDate = startDate.getTime () + (long) (Math.random () * (endDate.getTime () - startDate.getTime ()));
      Date date = new Date (generatedDate);
      SimpleDateFormat sdf = new SimpleDateFormat ("yyyyMMdd'T'HHmmss'+0100'");
      text = sdf.format (date);

    }
    catch (ParseException e) {
      e.printStackTrace ();
    }
    return text;
  }

  private String[] getMagasinsId (String folderPath) {
    File folder = new File (folderPath);
    File[] listOfFiles = folder.listFiles ();
    int filesNumber = listOfFiles.length;
    String[] fileNames = new String[filesNumber];
    for (int i = 0; i < filesNumber; i++) {
      fileNames[i] = listOfFiles[i].getName ().substring (15, 51);
    }
    return fileNames;
  }

  float maxLines = 15500 * ((float) max_bytes / (1024 * 1024));

  private int lineIndex = -1;

  public Boolean existsMoreData () {
    return lineIndex++ < 15500 * ((float) max_bytes / (1024 * 1024));
  } //15500 lines equivalent to 1 Mega

  private int incr = 1;

/**
 * cette methode est utlise pour generer la ligne à ecrire dans le fichier des transactions
 * */
  public String getCurrentStringToWrite () {
    Random r = new Random ();
    int numSameId = r.nextInt (5) + 1;
    StringBuilder str = new StringBuilder ();
    for (int i = 0; i < numSameId; i++) {
      int MgId = r.nextInt (mgNumber);
      String magasinID = fileName.getListNames ().get (MgId);
      String timeStrg = CreatedDateTime ();
      String pdtId = Integer.toString (r.nextInt (999) + 1);//number of product -1
      String pdQty = Integer.toString (r.nextInt (20));
      str.append (concat (Integer.toString (incr), timeStrg, magasinID, pdtId, pdQty));
      str.append ("\n");
    }
    incr = incr + 1;
    return str.toString ().substring (0, str.length () - 1);

  }
}

