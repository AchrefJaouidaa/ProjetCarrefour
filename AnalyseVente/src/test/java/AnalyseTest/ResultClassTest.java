package AnalyseTest;

import Analyse.ResultClass;
import Analyse.SortGlobalOrMg;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import static org.junit.Assert.assertEquals;

public class ResultClassTest {
  private String absPath = "/home/lansrod/Bureau/mavenproject/achrefTest/";

  @Test
  public void testcreateDailyResults () {
    ResultClass resultClassInstance = new ResultClass ("20170501", "20170502", absPath, SortGlobalOrMg.Mg);
    resultClassInstance.createDailyResults ();
    File file = new File (absPath + "Reslt/0/Mg/top_100_CA_20170501_13NTlLTc-0cql-U72c-fEY6-oMBZzymNgn7n");
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
  public void testcreatePeriodResults () {
    ResultClass resultClassInstance = new ResultClass ("20170501", "20170502", absPath, SortGlobalOrMg.Mg);
    resultClassInstance.createPeriodResults ();
    File file = new File (absPath + "Reslt/8/Mg/top_100_CA_all_13NTlLTc-0cql-U72c-fEY6-oMBZzymNgn7n");
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
    assertEquals ("1|106|7996.64,2|52|4756.96,", strBu.toString ());
  }
}
