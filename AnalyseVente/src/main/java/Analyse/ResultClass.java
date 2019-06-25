package Analyse;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Cette classe permet d'avoir les résultats demandés en combinant toutes les autres classes
 * IL FAUT AFFINER LA PERFORMANCE DU CODE AVEC LES MAXFILESIZE (FILESPLITTER.CLASS) ET CHUNK_SIZE (TRANSFILEINTERFACE.CLASS)
 * les resultats sont dans le fichier Reslt crée dans le meme path (string) donné
 * les results daily sont numerotés de 0 (day1) jusqu'a le endDate donné et la periode doit etre au max 7 jours
 * les resultats dans le fichier 8 est par magasin pour toute la periode
 * les resultats dans le fichier 10 est pour touts les magasins pour toute la periode
 */

public class ResultClass {
  private String folderPath;
  private String startDate;
  private String endDate;
  private int numbDays;
  private SortGlobalOrMg sortType;
  int length1;
  int length2;
  private Thread thread1;
  private Thread thread2;


  public ResultClass (String startDate, String endDate, String folderPath, SortGlobalOrMg sortType) {
    this.startDate = startDate;
    this.endDate = endDate;
    this.folderPath = folderPath;
    this.sortType = sortType;

  }

  /**
   * cette méthode permet d'avoir le fichier temp1
   * elle prend tous les fichiers de transactions et les fichiers des magasins
   * elle divise les fichiers en fixant la max de stockage du fichier (dans l'interface TransFileInterface
   * elle permet d'ajouter à chaque fichier de transaction le prix total des ventes par transaction
   * param file
   */
  public void Initialize () {
    ListOfFiles listfilesInstance = ListOfFiles.getInstance (startDate, endDate, folderPath);
    ArrayList <File>[] mgList = listfilesInstance.getListMgFiles ();
    ArrayList <File> transList = listfilesInstance.getListTransFiles ();
    numbDays = transList.size ();
    length1 = numbDays / 2;
    length2 = numbDays - numbDays / 2;
    List <File> transListsub1 = transList.subList (0, numbDays / 2);
    List <File> transListsub2 = transList.subList (numbDays / 2, numbDays);
    File[] transFileArr1 = transListsub1.toArray (new File[transListsub1.size ()]);
    File[] transFileArr2 = transListsub2.toArray (new File[transListsub2.size ()]);
    ;

    thread1 = new Thread ("ach") {
      public void run () {

        for (int i = 0; i < transFileArr1.length; i++) {
          new FilesSPlitter (transFileArr1[i], i, mgList[i], "temp1").TransFile ();
        }
      }
    };
    thread2 = new Thread () {
      public void run () {
        for (int i = 0; i < transFileArr2.length; i++) {
          int j = i + transFileArr1.length;
          new FilesSPlitter (transFileArr2[i], j, mgList[j], "temp1").TransFile ();
        }
      }
    };
    thread1.start ();
    thread2.start ();
    try {
      thread1.join ();
      thread2.join ();
    }
    catch (InterruptedException e) {
      e.printStackTrace ();
    }
  }

  /**
   * cette méthode permet d'avoir le fichier temp2
   * elle prend tous les fichiers de transactions  fichier par fichier et les combine pour chaque magasin
   */

  public void createTemp2 () {
    Initialize ();
    thread1 = new Thread () {
      public void run () {
        for (int i = 0; i < length1; i++) {
          File[] listofFIles = new File (folderPath + "temp1/" + i + "/").listFiles ();
          for (File file : listofFIles) {
            new ReducePhase1 (i, "temp2", sortType, file).TransFile ();
          }

        }
      }
    };
    thread2 = new Thread () {
      public void run () {
        int j = 0;
        for (int i = 0; i < length2; i++) {
          j = i + length1;
          File[] listofFIles = new File (folderPath + "temp1/" + i + "/").listFiles ();
          for (File file : listofFIles) {
            new ReducePhase1 (j, "temp2", sortType, file).TransFile ();
          }

        }
      }
    };
    thread1.start ();
    thread2.start ();
    try {
      thread1.join ();
      thread2.join ();
    }
    catch (InterruptedException e) {
      e.printStackTrace ();
    }
  }

  /**
   * cette méthode permet d'avoir les fichier dans chque dossier de temp2
   * elle prend tous les fichiers de transactions  fichier et les combine pour chaque magasin
   */

  public void createTemp3 () {
    createTemp2 ();
    thread1 = new Thread () {
      public void run () {
        for (int i = 0; i < length1; i++) {
          File[] listofFIles = new File (folderPath + "temp2/" + i + "/").listFiles ();
          new ReducePhase1 (i, "temp3", sortType, listofFIles).TransFile ();
        }
      }
    };
    thread2 = new Thread () {
      public void run () {
        int j = 0;
        for (int i = 0; i < length2; i++) {
          j = i + length1;
          File[] listofFIles = new File (folderPath + "temp2/" + i + "/").listFiles ();
          new ReducePhase1 (j, "temp3", sortType, listofFIles).TransFile ();
        }
      }
    };
    thread1.start ();
    thread2.start ();
    try {
      thread1.join ();
      thread2.join ();
    }
    catch (InterruptedException e) {
      e.printStackTrace ();
    }
  }

  /**
   * cette méthode permet d'avoir les fichier dans chque dossier de temp2
   * elle prend tous les fichiers de transactions  fichier par fichier et les combine pour tous les magasins
   */

  public void createTempg () {
    Initialize ();
    thread1 = new Thread () {
      public void run () {
        for (int i = 0; i < length1; i++) {
          File[] listofFIles = new File (folderPath + "temp1/" + i + "/").listFiles ();
          for (File file : listofFIles) {
            new ReducePhase1 (i, "tempg", sortType, file).TransFile ();
          }
        }
      }
    };
    thread2 = new Thread () {
      public void run () {
        int j = 0;
        for (int i = 0; i < length2; i++) {
          j = i + length1;
          File[] listofFIles = new File (folderPath + "temp1/" + i + "/").listFiles ();
          for (File file : listofFIles) {
            new ReducePhase1 (j, "tempg", sortType, file).TransFile ();
          }

        }
      }
    };
    thread1.start ();
    thread2.start ();

    try {
      thread1.join ();
      thread2.join ();
    }
    catch (InterruptedException e) {
      e.printStackTrace ();
    }
  }

  /**
   * cette méthode permet d'avoir les fichier dans chque dossier de temp2
   * elle prend tous les fichiers de transactions  fichier et les combine pour tous les magasins
   */
  public void createTempl () {
    createTempg ();
    thread1 = new Thread () {
      public void run () {
        for (int i = 0; i < length1; i++) {
          File[] listofFIles = new File (folderPath + "tempg/" + i + "/").listFiles ();
          new ReducePhase1 (i, "templ", sortType, 3, listofFIles).TransFile ();
        }
      }
    };
    thread2 = new Thread () {
      public void run () {
        int j = 0;
        for (int i = 0; i < length2; i++) {
          j = i + length1;
          File[] listofFIles = new File (folderPath + "tempg/" + i + "/").listFiles ();
          new ReducePhase1 (j, "templ", sortType, 3, listofFIles).TransFile ();
        }
      }
    };
    thread1.start ();
    thread2.start ();
    try {
      thread1.join ();
      thread2.join ();
    }
    catch (InterruptedException e) {
      e.printStackTrace ();
    }

  }

  /**
   * cette méthode permet d'avoir le resultat daily CA et ventes soit par magasin soit sur tous les magasins
   */
  public void createDailyResults () {
    switch (sortType){
      case Mg:{
        createTemp3 ();
//to create the results per day per Mg
        thread1 = new Thread () {
          public void run () {
            for (int i = 0; i < length1; i++) {
              File[] listofFIles = new File (folderPath + "temp3/" + i + "/").listFiles ();
              new ReducePhase2 (i, "Reslt", sortType, SortCAOrVentes.VENTES, listofFIles).TransFile ();
              new ReducePhase2 (i, "Reslt", sortType, SortCAOrVentes.CA, listofFIles).TransFile ();
            }
          }
        };
        thread2 = new Thread () {
          public void run () {
            int j = 0;
            for (int i = 0; i < length2; i++) {
              j = i + length1;
              File[] listofFIles = new File (folderPath + "temp3/" + i + "/").listFiles ();
              new ReducePhase2 (j, "Reslt", sortType, SortCAOrVentes.VENTES, listofFIles).TransFile ();
              new ReducePhase2 (j, "Reslt", sortType, SortCAOrVentes.CA, listofFIles).TransFile ();
            }
          }
        };
        thread1.start ();
        thread2.start ();
        try {
          thread1.join ();
          thread2.join ();
        }
        catch (InterruptedException e) {
          e.printStackTrace ();
        }
      }
      break;
      case global:{
        createTempl ();
//to create the results per day per Mg
        thread1 = new Thread () {
          public void run () {
            for (int i = 0; i < length1; i++) {
              File[] listofFIles = new File (folderPath + "templ/" + i + "/").listFiles ();

              new ReducePhase2 (i, "Reslt", sortType, SortCAOrVentes.VENTES, 3, listofFIles).TransFile ();
              new ReducePhase2 (i, "Reslt", sortType, SortCAOrVentes.CA, 3, listofFIles).TransFile ();
            }
          }
        };
        thread2 = new Thread () {
          public void run () {
            int j = 0;
            for (int i = 0; i < length2; i++) {
              j = i + length1;
              File[] listofFIles = new File (folderPath + "templ/" + i + "/").listFiles ();
              new ReducePhase2 (j, "Reslt", sortType, SortCAOrVentes.VENTES, 3, listofFIles).TransFile ();
              new ReducePhase2 (j, "Reslt", sortType, SortCAOrVentes.CA, 3, listofFIles).TransFile ();
            }
          }
        };
        thread1.start ();
        thread2.start ();
        try {
          thread1.join ();
          thread2.join ();
        }
        catch (InterruptedException e) {
          e.printStackTrace ();
        }
      }
      break;
    }
  }


  /**
   * cette méthode permet d'avoir le resultat d'une periode de MAX 7 JOURS CA et ventes soit par magasin soit sur tous les magasins
   */

  public void createPeriodResults () {
    switch (sortType){
      case Mg:{
        createTemp3 ();
        File[] listofFIles = new File[numbDays];
        for (int i = 0; i < numbDays; i++) {
          listofFIles[i] = new File (folderPath + "temp3/" + i + "/").listFiles ()[0];
        }
        new ReducePhase1 (7, "Reslt", sortType, listofFIles).TransFile ();
        new ReducePhase2 (8, "Reslt", sortType, SortCAOrVentes.CA, new File (folderPath + "Reslt/" + 7 + "/").listFiles ()[0]).TransFile ();
        new ReducePhase2 (8, "Reslt", sortType, SortCAOrVentes.VENTES, new File (folderPath + "Reslt/" + 7 + "/").listFiles ()[0]).TransFile ();
      }
      break;
      case global:{
        createTempl ();
        File[] listofFIles = new File[numbDays];
        for (int i = 0; i < numbDays; i++) {
          listofFIles[i] = new File (folderPath + "templ/" + i + "/").listFiles ()[0];

        }
        new ReducePhase1 (9, "Reslt", sortType, 3, listofFIles).TransFile ();
        new ReducePhase2 (10, "Reslt", sortType, SortCAOrVentes.VENTES, 3, new File (folderPath + "Reslt/" + 9 + "/").listFiles ()[0]).TransFile ();
        new ReducePhase2 (10, "Reslt", sortType, SortCAOrVentes.CA, 3, new File (folderPath + "Reslt/" + 9 + "/").listFiles ()[0]).TransFile ();
      }
      break;
    }

  }

  /**
   * cette methode est utilisé pour supprimer le fichier
   * @param file
   */
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

  /**
   * cette methode permet d'effacer tous les fichiers temporaires
   */

  public void destroy () {
    deleteDirectory (new File (folderPath + "temp1/"));
    deleteDirectory (new File (folderPath + "temp2/"));
    deleteDirectory (new File (folderPath + "temp3/"));
    deleteDirectory (new File (folderPath + "tempg/"));
    deleteDirectory (new File (folderPath + "templ/"));
    deleteDirectory (new File (folderPath + "Reslt/7"));
    deleteDirectory (new File (folderPath + "Reslt/9"));
  }
}


