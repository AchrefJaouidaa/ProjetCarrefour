package Analyse;

public class ResultMain {
  public static void main (String[] args) {
    ResultClass resultAllMagasins = new ResultClass (args[0], args[1], args[2], SortGlobalOrMg.global);
    resultAllMagasins.createDailyResults ();
    resultAllMagasins.createPeriodResults ();

    ResultClass resultSingleMagasin = new ResultClass (args[0], args[1], args[2], SortGlobalOrMg.Mg);
    resultSingleMagasin.createDailyResults ();
    resultSingleMagasin.createPeriodResults ();
    resultSingleMagasin.destroy ();
  }
}
