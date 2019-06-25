package DataGen;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * cette methode retourne une instance d'un writer
 */
public class FileWriterInstance {
  public BufferedWriter getWriterInstance (String filePath) {
    BufferedWriter bw = null;
    try {
      FileOutputStream fos = new FileOutputStream (filePath);
      OutputStreamWriter osr = new OutputStreamWriter (fos, "UTF-8");
      bw = new BufferedWriter (osr);
    }
    catch (IOException e) {
      e.printStackTrace ();
    }
    return bw;
  }
}
