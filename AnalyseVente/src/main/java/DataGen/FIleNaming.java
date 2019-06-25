package DataGen;

import java.util.ArrayList;

/**
 * cette classe permet de generer les noms des fichiers transactions et magasins
 */
public class FIleNaming {

  private int mgNumber;

  public FIleNaming (int mgNumber) {
    this.mgNumber = mgNumber;
  }

  private static ArrayList <String> mgNames = new ArrayList <> ();

  public static ArrayList <String> getListNames () {
    return mgNames;
  }

  /**cette methode permet de generer un string alphanumeric de taille n
   *
   * @param n length du string a generer
   * @return le random string generé
   */
  private static String getAlphaNumericString (int n) {
    String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
            + "0123456789" + "abcdefghijklmnopqrstuvxyz";
    StringBuilder sb = new StringBuilder (n);
    for (int i = 0; i < n; i++) {
      int index = (int) (AlphaNumericString.length () * Math.random ());
      sb.append (AlphaNumericString.charAt (index));
    }
    return sb.toString ();
  }

  /**
   * permet de generer le nom du magasin
   * @return
   */
  private static String MagasinId () {
    return getAlphaNumericString (8) + "-" + getAlphaNumericString (4) +
            "-" + getAlphaNumericString (4) + "-" + getAlphaNumericString (4) +
            "-" + getAlphaNumericString (12);
  }

  /**
   * permet de generer le nom du magasin avec MagasinId
   * @param path le path du fichier a generer
   * @param fileDate le date du fichier
   * @return le nom du magasin
   */
  public String generateMgName (String path, String fileDate) {
    String mgName = MagasinId ();
    while ( mgNames.contains (mgName) ) {
      mgName = MagasinId ();
    }
    mgNames.add (mgName);
    return path + "reference_prod-" + mgName + "_" + fileDate + ".data";
  }

  public String getMgName (String path, String fileDate, int incr) {
    return path + "reference_prod-" + mgNames.get (incr) + "_" + fileDate + ".data";
  }


  public String generateTransName (String path, int fileDate) {
    return path + "transactions_" + fileDate;
  }
}
