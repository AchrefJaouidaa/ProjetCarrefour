package AnalyseTest;

import Analyse.ListOfFiles;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class testListOfFiles {
  private String absPath = "/home/lansrod/Bureau/mavenproject/achrefTest/";

  @Test
  public void testgetListTransFiles () {
    ListOfFiles listOfFiles = ListOfFiles.getInstance ("20170501", "20170503", absPath);
    File file = listOfFiles.getListTransFiles ().get (0);
    assertEquals ("transactions_20170501", file.getName ());
  }

  @Test
  public void testgetListMgFiles () {

    ListOfFiles listOfFiles = ListOfFiles.getInstance ("20170501", "20170503", absPath);
    File file = listOfFiles.getListMgFiles ()[0].get (0);
    assertTrue (file.getName ().startsWith ("reference_prod-"));
  }
}
