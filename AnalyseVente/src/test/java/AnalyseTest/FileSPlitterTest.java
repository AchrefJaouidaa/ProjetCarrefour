package AnalyseTest;

import Analyse.FilesSPlitter;
import Analyse.ListOfFiles;
import org.junit.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class FileSPlitterTest {
  private String absPath = "/home/lansrod/Bureau/mavenproject/achrefTest/";

  @Test
  public void testloadMgData () {
    ListOfFiles listOfFiles = ListOfFiles.getInstance ("20170501", "20170503", absPath);
    ArrayList <File> listMgFiles = listOfFiles.getListMgFiles ()[0];
    File fileToSplit = listOfFiles.getListTransFiles ().get (0);
    FilesSPlitter fileSplitterInstance = new FilesSPlitter (new File (absPath + "transactions_20170501"), 0, listMgFiles, "temp1");
    HashMap <List <String>, String> result = fileSplitterInstance.loadMgData ();
    HashMap <List <String>, String> expected = new HashMap <> ();
    expected.put (Arrays.asList ("13NTlLTc-0cql-U72c-fEY6-oMBZzymNgn7n", "1"), "75.44");
    expected.put (Arrays.asList ("13NTlLTc-0cql-U72c-fEY6-oMBZzymNgn7n", "2"), "91.48");
    expected.put (Arrays.asList ("juUANLJA-2jIP-3EMJ-qCNM-rUCdzOQQXjSg", "1"), "45.95");
    expected.put (Arrays.asList ("juUANLJA-2jIP-3EMJ-qCNM-rUCdzOQQXjSg", "2"), "69.02");
    assertEquals (result, expected);
  }

  @Test
  public void testTransFile () {
    ListOfFiles listOfFiles = ListOfFiles.getInstance ("20170501", "20170503", absPath);
    ArrayList <File> listMgFiles = listOfFiles.getListMgFiles ()[0];
    File fileToSplit = listOfFiles.getListTransFiles ().get (0);
    FilesSPlitter fileSplitterInstance = new FilesSPlitter (new File (absPath + "transactions_20170501"), 0, listMgFiles, "temp1");
    fileSplitterInstance.TransFile ();
    File file = new File (absPath + "temp1/0/Part_0_transactions_20170501.data");
    StringBuilder strBu = new StringBuilder ();
    try {
      BufferedReader br = new BufferedReader (new FileReader (file));
      String st;
      int i = 1;
      int line = 1;
      while ( (st = br.readLine ()) != null && line < 2 ) {
        strBu.append (st);
        line++;
      }
    }
    catch (Exception e) {
      e.printStackTrace ();
    }
    assertEquals ("13NTlLTc-0cql-U72c-fEY6-oMBZzymNgn7n|2|15|1372.2001", strBu.toString ());
  }
}
