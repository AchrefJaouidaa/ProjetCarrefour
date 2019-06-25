package DataGen;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.Assert.*;

public class DataGenTest {
  private String absPath = "/home/lansrod/Bureau/mavenproject/achrefTest1/";

  private void deleteDirectory (File file) {
    try {
      if (file.isDirectory ()) {
        File[] entries = file.listFiles ();
        if (entries != null) {
          for (File entry : entries) {
            deleteDirectory (entry);
          }
        }
      }
      if (!file.delete ()) {
        throw new IOException ("Failed to delete " + file);
      }
    }
    catch (IOException e) {
      e.printStackTrace ();
    }
  }

  @Test
  public void testgenerateMgData () {
    if (!new File (absPath).exists ()) {
      new File (absPath).mkdirs ();
    }
    else {
      deleteDirectory (new File (absPath));
      new File (absPath).mkdirs ();
    }
    int MgNumber = 2;
    DataGenerator dataGenInstance = new DataGenerator ("20170501", "20170502", MgNumber);
    FIleNaming fileName = new FIleNaming (MgNumber);
    dataGenInstance.generateMgData (absPath, fileName);
    File[] files = new File (absPath).listFiles ();
    assertEquals (files.length, 4);
    File file = files[0];
    StringBuilder strBu = new StringBuilder ();
    int line = 0;
    try {
      BufferedReader br = new BufferedReader (new FileReader (file));
      String st;
      while ( (st = br.readLine ()) != null ) {
        strBu.append (st);
        line++;
      }
    }
    catch (Exception e) {
      e.printStackTrace ();
    }
    assertEquals (line, 999);
  }

  @Test
  public void testgenerateTransData () {
    if (!new File (absPath).exists ()) {
      new File (absPath).mkdirs ();
    }
    else {
      deleteDirectory (new File (absPath));
      new File (absPath).mkdirs ();
    }
    int MgNumber = 2;
    DataGenerator dataGenInstance = new DataGenerator ("20170501", "20170502", MgNumber);
    FIleNaming fileName = new FIleNaming (MgNumber);
    File[] filesMg = new File (absPath).listFiles ();

    dataGenInstance.generateMgData (absPath, fileName);
    dataGenInstance.generateTransData (absPath, fileName, 1024 * 1024);
    File[] files = new File (absPath).listFiles ();
    assertEquals (files.length, 6);
  }
}
