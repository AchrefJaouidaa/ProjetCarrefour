package DataGen;

import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

import java.io.File;

public class FileNamingTest {
  private String absPath = "/home/lansrod/Bureau/mavenproject/achrefTest/";

  @Test
  public void testgetMgName () {
    String fileDate = "20190623";
    String filename = new FIleNaming (2).generateMgName (absPath, fileDate);
    File file = new File (filename);
    assertTrue (file.getName ().startsWith ("reference_prod-"));
    assertEquals (file.getName ().substring (52, 65), fileDate + ".data");
  }
}
