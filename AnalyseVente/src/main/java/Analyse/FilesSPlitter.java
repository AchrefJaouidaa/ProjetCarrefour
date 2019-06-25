package Analyse;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


/**
 * Cette classe permet de lire, diviser les fichiers transactions selon un maxFilesSize et ajouter le prix total de la  transaction
 * @param 'maxFilesize' est le mx taille des fichiers splitted en Byte
 */

public class FilesSPlitter extends TransFileInterface {
  private final static long maxFileSize = 5242880;
  ArrayList<File> listOfMg;

  public FilesSPlitter(File fileToTrans,
                       int indexDate,
                       ArrayList<File> listOfMg,
                       String tempName) {
    super(indexDate, tempName, fileToTrans);
    this.listOfMg = listOfMg;
  }

  /**
   * cette méthode prend en mémoire tous les fichiers magasins
   * @param 'Mgdata' est une hashmap de clé magasin,idproduit et la valeur le prix par produit
   */

  public HashMap<List<String>, String> loadMgData() {
    HashMap<List<String>, String> MgData = new HashMap<>();
    TextRowDecoder decoder = new TextRowDecoder(2, delimiter);
    FileReader<byte[][]> reader;
    for (File tempFile : listOfMg) {
      String tmpName = tempFile.getName();
      String MgName = tmpName.substring(15, tmpName.length() - 14);
      reader = FileReader.create(decoder, CHUNK_SIZE, tempFile);
      for (List<byte[][]> chunk : reader) {
        for (byte[][] element : chunk) {
          String elem1 = new String(element[0]);
          String elem2 = new String(element[1]);
          ArrayList<String> tempList = new ArrayList<>();
          tempList.add(MgName);
          tempList.add(elem1);
          MgData.put(tempList, elem2);
        }
      }
    }
    return MgData;
  }

  /**
   * cette méthode permet de parcourir les fichiers transactions en loadant à chaque fois un CHUNK_SIZE et grouper avec le resultat de loadMgData
   * @parm 'incr' est la taille de fichier en cours de creation
   * 'index' correspond au jour
   */

  public void TransFile() {
    long incr = 0;
    int index = 0;
    TextRowDecoder decoder = new TextRowDecoder(5, delimiter);
    String fileName = fileToTrans[0].getName();
    FileReader<byte[][]> reader = FileReader.create(decoder, CHUNK_SIZE, fileToTrans);
    FileWriter filePart = null;
    BufferedWriter writer = null;
    HashMap<List<String>, String> listToJoin = loadMgData();
    try {
      filePart = new FileWriter(fileDirectory + "/Part_" + index + "_" + fileName + ".data");
      writer = new BufferedWriter(filePart);
      for (List<byte[][]> chunk : reader) {
        if (incr <= maxFileSize) {
          incr = incr + CHUNK_SIZE;
        }
        else {
          writer.close();
          index = index + 1;
          incr = 0;
          filePart = new FileWriter(fileDirectory + "/Part_" + index + "_" + fileName + ".data");
          writer = new BufferedWriter(filePart);
        }
        for (byte[][] element : chunk) {
          String idMgTemp = new String(element[2]);
          String idPdTemp = new String(element[3]);
          String qtyPdTemp = new String(element[4]);
          String prixPdtemp = listToJoin.get(Arrays.asList(idMgTemp, idPdTemp));
          float priceTotaltemp = Float.parseFloat(qtyPdTemp) * Float.parseFloat(prixPdtemp);
          writer.write(idMgTemp + "|" + idPdTemp + "|" + qtyPdTemp + "|" + priceTotaltemp);

          writer.newLine();
        }
      }
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
