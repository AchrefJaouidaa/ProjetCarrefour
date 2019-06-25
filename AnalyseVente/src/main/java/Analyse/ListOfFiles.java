package Analyse;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Cette classe permet à partir d"un path ou tous les fichiers sont stockés d'avoir la liste des transactions files et des magasins
 * @param "listTransFiles' est la liste des fichiers transactions
 * "listMaFiles' est la liste des fichiers magasins
 */

public class ListOfFiles {
  private static ListOfFiles sSoleInstance;
  private ArrayList<File> listTransFiles = new ArrayList<> ();
  private ArrayList<File>[] listMaFiles = null;

  public ArrayList<File> getListTransFiles () {
    return listTransFiles;
  }

  public ArrayList<File>[] getListMgFiles () {
    return listMaFiles;
  }

  private ListOfFiles (String startDate,
                       String endDate,
                       String... folders) {
    int startDateInt = Integer.parseInt (startDate);
    int endDateInt = Integer.parseInt (endDate);
    listMaFiles = new ArrayList[endDateInt - startDateInt + 1];
    for (String flder : folders) {
      File folder = new File (flder);
      List<File> newList = Arrays.asList (folder.listFiles ());
      for (File file : newList) {
        if (file.getName ().startsWith ("tra")) {
          int i = 0;
          for (File filetmp : listTransFiles) {
            if (Integer.parseInt (filetmp.getName ().substring (13, 21)) < Integer.parseInt (file.getName ().substring (13, 21))) {
              i = i + 1;
            }
          }
          int fileInt = Integer.parseInt (file.getName ().substring (13, 21));
          if (fileInt - startDateInt <= endDateInt - startDateInt) {
            listTransFiles.add (i, file);
          }
        }
        else if (file.getName ().startsWith ("ref")) {
          String tmp1 = file.getName ();
          String tmp = tmp1.substring (tmp1.length () - 13, tmp1.length () - 5);
          int tmpInt = Integer.parseInt (tmp) - startDateInt;
          if (tmpInt <= endDateInt - startDateInt && listMaFiles[tmpInt] == null) {
            listMaFiles[tmpInt] = new ArrayList<> ();
          }
          if (tmpInt <= endDateInt - startDateInt) {
            listMaFiles[tmpInt].add (file);
          }
        }
      }
    }
  }

  public static ListOfFiles getInstance (String startDate, String endDate, String... folders) {
    if (sSoleInstance == null) { //if there is no instance available... create new one
      sSoleInstance = new ListOfFiles (startDate, endDate, folders);
    }

    return sSoleInstance;
  }
}
