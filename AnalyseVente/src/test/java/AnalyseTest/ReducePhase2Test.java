package AnalyseTest;

import Analyse.*;
import org.junit.Test;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class ReducePhase2Test {
  private String absPath = "/home/lansrod/Bureau/mavenproject/achrefTest/";

  @Test
  public void testTransFile () {
    ListOfFiles listOfFiles = ListOfFiles.getInstance ("20170501", "20170503", absPath);
    ArrayList <File> listMgFiles = listOfFiles.getListMgFiles ()[0];
    FilesSPlitter FileSplitterInstance = new FilesSPlitter (new File (absPath + "transactions_20170501"), 0, listMgFiles, "temp1");
    FileSplitterInstance.TransFile ();
    ReducePhase1 reducePhase1Instance = new ReducePhase1 (0, "temp2", SortGlobalOrMg.Mg, new File (absPath + "temp1/0/Part_0_transactions_20170501.data"));
    reducePhase1Instance.TransFile ();
    reducePhase1Instance = new ReducePhase1 (0, "temp3", SortGlobalOrMg.Mg, new File (absPath + "temp2/0/Part_0_transactions_20170501.data_Mg"));
    reducePhase1Instance.TransFile ();
    ReducePhase2 reducePhase2Instance = new ReducePhase2 (0, "Rslt", SortGlobalOrMg.Mg, SortCAOrVentes.CA, new File (absPath + "temp3/0/Part_0_transactions_20170501.data_Mg_Mg"));
    reducePhase2Instance.TransFile ();
    File file = new File (absPath + "Rslt/0/Mg/top_100_CA_20170501_13NTlLTc-0cql-U72c-fEY6-oMBZzymNgn7n");
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
    assertEquals ("1|53|3998.32,2|26|2378.48,", strBu.toString ());
  }

  @Test
  public void testgetReducedCol () {
    ListOfFiles listOfFiles = ListOfFiles.getInstance ("20170501", "20170503", absPath);
    ArrayList <File> listMgFiles = listOfFiles.getListMgFiles ()[0];
    FilesSPlitter FileSplitterInstance = new FilesSPlitter (new File (absPath + "transactions_20170501"), 0, listMgFiles, "temp1");
    FileSplitterInstance.TransFile ();
    ReducePhase1 reducePhase1Instance = new ReducePhase1 (0, "temp2", SortGlobalOrMg.Mg, new File (absPath + "temp1/0/Part_0_transactions_20170501.data"));
    reducePhase1Instance.TransFile ();
    reducePhase1Instance = new ReducePhase1 (0, "temp3", SortGlobalOrMg.Mg, new File (absPath + "temp2/0/Part_0_transactions_20170501.data_Mg"));
    reducePhase1Instance.TransFile ();
    ReducePhase2 reducePhase2Instance = new ReducePhase2 (0, "temp2", SortGlobalOrMg.Mg, SortCAOrVentes.CA, new File (absPath + "temp3/0/Part_0_transactions_20170501.data_Mg_Mg"));
    HashMap <String, ArrayList <float[]>> aggMap = reducePhase2Instance.getReducedCol ();
    float[] result = aggMap.get ("13NTlLTc-0cql-U72c-fEY6-oMBZzymNgn7n").get (0);
    float[] expected = {53F, 3998.32F};
    java.util.Arrays.equals (result, expected);
  }
}
