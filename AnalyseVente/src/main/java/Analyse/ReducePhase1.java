package Analyse;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Cette classe permet de grouper un ou plusieurs fichiers dans un ou plusieurs dossiers selon une clé;
 * La clé est définie par le type de reduce global ou par magasin
 */

public class ReducePhase1 extends ReducerAbstract {

  public ReducePhase1 (int indexDate,
                       String tempName,
                       SortGlobalOrMg sortType,
                       File... fileToSplit) {
    super (indexDate, tempName, sortType, fileToSplit);
  }

  public ReducePhase1 (int indexDate,
                       String tempName,
                       SortGlobalOrMg sortType,
                       int numFields,
                       File... fileToSplit) {
    super (indexDate, tempName, sortType, numFields, fileToSplit);
  }

  /**
   * cette méthode permet d"écrire le résultat de ReduceCol dans un ou plusieurs fichiers
   */
  @Override
  public void TransFile () {
    HashMap <List <String>, float[]> aggMap = getReducedCol ();
    String fileName = fileToTrans[0].getName ();
    FileWriter filePart = null;
    BufferedWriter writer = null;
    try {
      filePart = new FileWriter (fileDirectory + "/" + fileName + "_" + sortType);
      writer = new BufferedWriter (filePart);
      Set <List <String>> tmpSet = aggMap.keySet ();
      for (List list : tmpSet) {
        float[] val = aggMap.get (list);
        String strWrite = null;
        switch (sortType) {
          case Mg: {
            strWrite = (String) list.get (0) + "|" + (String) list.get (1);
          }
          break;
          case global: {
            strWrite = (String) list.get (0);
          }
          break;
        }

        writer.write (strWrite + "|" + (int) val[0] + "|" + val[1]);
        writer.newLine ();
      }
      writer.close ();
    }
    catch (IOException e) {
      e.printStackTrace ();
    }
  }

  /**
   * cette méthode permet de lire à partir des fichiers et regrouper les éléments selon une clé qui dépend du sorType (global= tous les magasins ou par Mg: magasin
   * return: une Hashmap dont la clé est une liste de String
   * dans le cas de global la clé est le id du produit
   * dans le cas de Mg la clé est le magasin id + id du produit
   */
  public HashMap <List <String>, float[]> getReducedCol () {
    HashMap <List <String>, float[]> aggMaptmp = new HashMap <> ();
    TextRowDecoder decoder = new TextRowDecoder (numFields, delimiter);
    FileReader <byte[][]> reader = null;
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
        List <String> key = null;

        switch (sortType) {
          case Mg: {
            key = Arrays.asList (new String (element[0]), new String (element[1]));
          }
          break;
          case global: {
            key = Arrays.asList (new String (element[1 - (4 - numFields)]));
          }
          break;
        }
        float qtyPdTemp = Float.parseFloat (new String (element[2 - (4 - numFields)]));
        float prixPdTemp = Float.parseFloat (new String (element[3 - (4 - numFields)]));
        if ( aggMaptmp.containsKey (key) ) {
          float[] tmpValue = aggMaptmp.get (key);
          tmpValue[0] = tmpValue[0] + qtyPdTemp;
          tmpValue[1] = tmpValue[1] + prixPdTemp;
          aggMaptmp.put (key, tmpValue);

        }
        else {
          aggMaptmp.put (key, new float[] {qtyPdTemp, prixPdTemp});
        }
      }
    }
    return aggMaptmp;
  }
}

