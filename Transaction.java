
public class Transaction {
  private String payer;
  private int points;
  private int timeStamp;

  public Transaction(String payer, int points, int timeStamp) {
    this.payer = payer;
    this.points = points;
    this.timeStamp = timeStamp;
  }

  public String getPayer() {
    return this.payer;
  }

  public int getPoints() {
    return this.points;
  }

  public int getTimeStamp() {
    return this.timeStamp;
  }
}
