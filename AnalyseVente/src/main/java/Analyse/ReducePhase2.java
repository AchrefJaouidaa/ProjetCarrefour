package Analyse;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Cette classe permet de grouper un ou plusieurs fichiers dans un ou plusieurs dossiers selon une clé;
 * La clé est définie par le type de reduce global ou par magasin et le résultat souhaité par ventes ou par chiffre d'affaire
 * param:  sortCV  est le type de résultat souhaité par chiffre d"affaire ou par quantite de ventes
 */

public class ReducePhase2 extends ReducerAbstract {
  private SortCAOrVentes sortCV;

  public ReducePhase2 (int indexDate,
                       String tempName,
                       SortGlobalOrMg sortGMType,
                       SortCAOrVentes sortCV,
                       File... fileToSplit) {
    super (indexDate, tempName, sortGMType, fileToSplit);
    this.sortCV = sortCV;
  }

  public ReducePhase2 (int indexDate,
                       String tempName,
                       SortGlobalOrMg sortGMType,
                       SortCAOrVentes sortCV,
                       int numFields,
                       File... fileToSplit) {
    super (indexDate, tempName, sortGMType, numFields, fileToSplit);
    this.sortCV = sortCV;
  }

  /**
   * cette méthode permet d"écrire le résultat de ReduceCol dans un  fichier dans le cas de tous les magasins /ou plusieurs fichiers par magasin
   */
  @Override
  public void TransFile () {

    HashMap <String, ArrayList <float[]>> aggMap = getReducedCol ();
    String fileName = fileToTrans[0].getName ();
    FileWriter filePart = null;
    BufferedWriter writer = null;
    try {
      switch (sortType){
        case Mg:{
          Set <String> tmpSet = aggMap.keySet ();
          for (String str : tmpSet) {
            ArrayList <float[]> val = aggMap.get (str);
            File fileDirectoryNew = new File (fileDirectory + "/" + sortType + "/");
            if ( !fileDirectoryNew.exists () ) {
              fileDirectoryNew.mkdirs ();
            }
            String temppStr = fileName;
            if ( indexDate == 8 ) {
              temppStr = "all";
            }
            else {
              temppStr = fileName.substring (20, 28);
            }
            filePart = new FileWriter (fileDirectoryNew + "/" + "top_100_" + sortCV + "_" + temppStr + "_" + str);
            writer = new BufferedWriter (filePart);
            int numb_result=0;
            for (float[] tempfloat : val) {
              if(numb_result<100){
                writer.write ((int) tempfloat[0] + "|" + (int) tempfloat[1] + "|" + tempfloat[2]);
                writer.newLine ();
                numb_result= numb_result+1;
              }
              else {
                continue;
              }
            }
            writer.close ();
          }
        }
        break;
        case global:{
          ArrayList <float[]> val = aggMap.get ("ANYKEY");
          File fileDirectoryNew = new File (fileDirectory + "/" + sortType + "/");
          if ( !fileDirectoryNew.exists () ) {
            fileDirectoryNew.mkdirs ();
          }
          String tempStrr = fileName;
          if ( indexDate == 10 ) {
            tempStrr = "all";
          }
          else {
            tempStrr = fileName.substring (20, 28);
          }
          filePart = new FileWriter (fileDirectoryNew + "/" + "top_100_" + sortCV + "_" + tempStrr);
          writer = new BufferedWriter (filePart);
          int numb_result=0;
          for (float[] tempfloat : val) {
            if(numb_result<100) {
              writer.write ((int) tempfloat[0] + "|" + (int) tempfloat[1] + "|" + tempfloat[2]);
              writer.newLine ();
              numb_result = numb_result + 1;
            }
            else {
              continue;
            }
          }
          writer.close ();

        }
        break;
      }
    }
    catch (IOException e) {
      e.printStackTrace ();
    }
  }

  /**
   * cette méthode permet de lire à partir des fichiers et ordonner les éléments selon une clé qui dépend du sorType
   * @return: une Hashmap dont la clé est une liste de String
   * dans le cas de global la clé est aléatoire
   * dans le cas de Mg la clé est le magasin id
   */
  public HashMap <String, ArrayList <float[]>> getReducedCol () {
    HashMap <String, ArrayList <float[]>> aggMaptmp = new HashMap <> ();
    TextRowDecoder decoder = new TextRowDecoder (numFields, delimiter);
    FileReader <byte[][]> reader;
    if ( fileToTrans.length == 1 && fileToTrans[0].listFiles () == null ) {
      reader = FileReader.create (decoder, CHUNK_SIZE, fileToTrans[0]);
    }
    else if ( fileToTrans.length > 1 ) {
      reader = FileReader.create (decoder, CHUNK_SIZE, fileToTrans);
    }
    else {
      reader = FileReader.create (decoder, CHUNK_SIZE, fileToTrans[0].listFiles ());
    }

    for (List <byte[][]> chunk : reader) {
      for (byte[][] element : chunk) {

        String key = null;
        float idPdTemp = Float.parseFloat (new String (element[1 - (4 - numFields)]));
        float qtyPdTemp = Float.parseFloat (new String (element[2 - (4 - numFields)]));
        float prixPdTemp = Float.parseFloat (new String (element[3 - (4 - numFields)]));

        switch (sortType){
          case Mg:{
            key = new String (element[0]);
          }
          break;
          case global:{
            key = "ANYKEY";
          }
          break;
        }
        ArrayList <float[]> tempList = null;
        if ( aggMaptmp.containsKey (key) ) {
          int i = 0;
          tempList = aggMaptmp.get (key);
          switch (sortCV){
            case VENTES:{
              for (float[] tempArray : tempList) {
                if ( tempArray[1] > qtyPdTemp ) {
                  i = i + 1;
                }
                else break;
              }
            }
            break;
            case CA:{
              for (float[] tempArray : tempList) {
                if ( tempArray[2] > prixPdTemp ) {
                  i = i + 1;
                }
                else break;
              }
            }
            break;
          }
          if ( i > tempList.size () ) {
            tempList.add (new float[] {idPdTemp, qtyPdTemp, prixPdTemp});
          }
          else {
            tempList.add (i, new float[] {idPdTemp, qtyPdTemp, prixPdTemp});
          }
          aggMaptmp.put (key, tempList);

        }
        else {
          tempList = new ArrayList <> (Arrays.asList (new float[] {idPdTemp, qtyPdTemp, prixPdTemp}));
          aggMaptmp.put (key, tempList);
        }
      }
    }
    return aggMaptmp;
  }
}
