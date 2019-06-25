package Analyse;

import java.io.File;

/**
 * cette classe est implémentée par ReducePhase1 et ReducePhase 2
 * @param 'sortType est soit pour tous les magasins ou pour chaque magasin
 * numFields est le nombre de fields dans le fichier à traiter
 */
public abstract class ReducerAbstract extends TransFileInterface {
  protected SortGlobalOrMg sortType;
  protected int numFields;

  public ReducerAbstract (int indexDate,
                          String tempName,
                          SortGlobalOrMg sortType,
                          File... fileToSplit) {
    super (indexDate, tempName, fileToSplit);
    this.sortType = sortType;
    this.numFields = 4;
  }

  public ReducerAbstract (int indexDate,
                          String tempName, SortGlobalOrMg sortType, int numFields, File... fileToSplit) {
    super (indexDate, tempName, fileToSplit);
    this.sortType = sortType;
    this.numFields = numFields;
  }

  /**
   *
   * @param path est le path du fichier a traiter
   * @return retourne le path absolu ou le dossier initial
   */
  @Override
  public String getAbsolPath (String path) {
    String[] pathArray = path.split ("/");
    int temp = pathArray[pathArray.length - 1].length ();
    String AbsPath = null;
    if ( fileToTrans[0].listFiles () == null ) {
      AbsPath = path.substring (0, path.length () - temp - 8);
    }
    else {
      AbsPath = path.substring (0, path.length () - temp - 6);
    }
    return AbsPath;
  }
}
