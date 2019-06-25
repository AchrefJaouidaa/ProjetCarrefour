package DataGen;

import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.io.BufferedWriter;
import java.io.IOException;
/**
 *  cette classe permet de generer les donnees magasins
 *  lineIndex est le nimbre de lignes ecrites
 * */
public class MagasinDataGen extends DataGenInterface {

  private int lineIndex = 0;
  /**
   *  cette methode sert a limiter le nombre d''output à ecrire dans le fichier magasin en nombre de produit
   * */
  public Boolean existsMoreData () {
    return lineIndex++ < 999;
  } //number of product -1

  /**
   * cette methode permet de fournir le string à ecrire dans le fichier magasin
   * @return
   */
  public String getCurrentStringToWrite () {
    double a = 0.1 + Math.random () * 100 * 100;
    float c = (float) Math.round (a) / 100;
    return concat ("" + lineIndex, "" + c);

  }

  /**
   * cette methode permet d'ecrire ligne par ligne dans le fichier magasin
   * @param MAX_BYTES max bytes à ecrie
   * @param bw une instance du writer
   */
  @Override
  void writeToFile (int MAX_BYTES, BufferedWriter bw) {
    int bytesWritten = 0;
    while ( existsMoreData () ) {
      try {
        String toWrite = getCurrentStringToWrite ();
        bw.write (toWrite);
        bw.newLine ();
      }
      catch (IOException ie) {
        ie.printStackTrace ();
      }
    }
  }
}
