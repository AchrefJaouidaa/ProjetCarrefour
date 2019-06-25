package AnalyseTest;

import Analyse.FilesSPlitter;
import Analyse.ListOfFiles;
import Analyse.ReducePhase1;
import Analyse.SortGlobalOrMg;
import org.junit.Test;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ReducePhase1Test {

  private String absPath = "/home/lansrod/Bureau/mavenproject/achrefTest/";

  @Test
  public void testTransFile () {
    ListOfFiles listOfFiles = ListOfFiles.getInstance ("20170501", "20170503", absPath);
    ArrayList <File> listMgFiles = listOfFiles.getListMgFiles ()[0];
    FilesSPlitter fileSplitterInstance = new FilesSPlitter (new File (absPath + "transactions_20170501"), 0, listMgFiles, "temp1");
    fileSplitterInstance.TransFile ();
    ReducePhase1 abis = new ReducePhase1 (0, "temp2", SortGlobalOrMg.Mg, new File (absPath + "temp1/0/Part_0_transactions_20170501.data"));
    abis.TransFile ();
    File file = new File (absPath + "temp1/0/Part_0_transactions_20170501.data");
    StringBuilder strBu = new StringBuilder ();
    try {
      BufferedReader br = new BufferedReader (new FileReader (file));
      String st;
      while ( (st = br.readLine ()) != null ) {
        strBu.append (st);
        strBu.append (",");
      }
    }
    catch (Exception e) {
      e.printStackTrace ();
    }
    assertEquals ("13NTlLTc-0cql-U72c-fEY6-oMBZzymNgn7n|2|15|1372.2001,13NTlLTc-0cql-U72c-fEY6-oMBZzymNgn7n|1|4|301.76,juUANLJA-2jIP-3EMJ-qCNM-rUCdzOQQXjSg|1|15|689.25,13NTlLTc-0cql-U72c-fEY6-oMBZzymNgn7n|1|2|150.88,13NTlLTc-0cql-U72c-fEY6-oMBZzymNgn7n|2|1|91.48,13NTlLTc-0cql-U72c-fEY6-oMBZzymNgn7n|1|12|905.28,juUANLJA-2jIP-3EMJ-qCNM-rUCdzOQQXjSg|1|3|137.85,juUANLJA-2jIP-3EMJ-qCNM-rUCdzOQQXjSg|2|8|552.16,juUANLJA-2jIP-3EMJ-qCNM-rUCdzOQQXjSg|1|4|183.8,13NTlLTc-0cql-U72c-fEY6-oMBZzymNgn7n|2|10|914.80005,13NTlLTc-0cql-U72c-fEY6-oMBZzymNgn7n|1|15|1131.6001,juUANLJA-2jIP-3EMJ-qCNM-rUCdzOQQXjSg|2|7|483.13998,juUANLJA-2jIP-3EMJ-qCNM-rUCdzOQQXjSg|1|11|505.45,13NTlLTc-0cql-U72c-fEY6-oMBZzymNgn7n|1|1|75.44,13NTlLTc-0cql-U72c-fEY6-oMBZzymNgn7n|1|19|1433.3601,", strBu.toString ());
  }

  @Test
  public void testgetReducedCol () {
    ListOfFiles listOfFiles = ListOfFiles.getInstance ("20170501", "20170503", absPath);
    ArrayList <File> listMgFiles = listOfFiles.getListMgFiles ()[0];
    FilesSPlitter fileSplitterInstance = new FilesSPlitter (new File (absPath + "transactions_20170501"), 0, listMgFiles, "temp1");
    fileSplitterInstance.TransFile ();
    ReducePhase1 abis = new ReducePhase1 (0, "temp2", SortGlobalOrMg.Mg, new File (absPath + "temp1/0/Part_0_transactions_20170501.data"));
    HashMap <List <String>, float[]> aggMap = abis.getReducedCol ();
    float[] result = aggMap.get (Arrays.asList ("13NTlLTc-0cql-U72c-fEY6-oMBZzymNgn7n", "1"));
    float[] expected = {53F, 3998.32F};
    java.util.Arrays.equals (result, expected);
  }
}
