import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class FetchTransactions {
  ArrayList<String> payerArray = new ArrayList<String>();
  ArrayList<Integer> pointsArray = new ArrayList<Integer>();
  ArrayList<Integer> timestampArray = new ArrayList<Integer>();


  public void loadTransactions(File file) throws FileNotFoundException {
    // keeps track of the current line
    String currentLine;
    Scanner scnr = null;

    try {
      // reads the file using the scanner and checks if the file is the right file
      scnr = new Scanner(file);

    } catch (FileNotFoundException f) {
      throw new FileNotFoundException("csv file cannot be loaded");
    }
    // checks if there is a next line in the file and loads the line if it passes the tests
    try {
      scnr.nextLine(); // skip first line on excel sheet
      while (scnr.hasNextLine()) {
        currentLine = scnr.nextLine();
        try {
          // split the current line in order to store the payer, points, and timestamp
          String[] transactionArray = currentLine.trim().split(",");
          if (transactionArray.length == 3) {
            // load the transaction

            payerArray.add(transactionArray[0]);
            pointsArray.add(Integer.parseInt(transactionArray[1]));
            String replaceDash = transactionArray[2].replace("-", "");
            replaceDash = replaceDash.substring(0, replaceDash.indexOf("T"));
            int replaceDashAsInt = Integer.parseInt(replaceDash);
            timestampArray.add(replaceDashAsInt);
          }
        } catch (IllegalArgumentException i1) {
          continue;
        }
      }
      // close the scanner in the finally block
    } finally {
      scnr.close();
    }
  }

  public void getOutput(int points) {
    int pointsPlaceholder = points;
    ArrayList<Integer> alreadyAddedList = new ArrayList<Integer>();
    // check that the same transaction isn't being found as the min transaction everytime
    while (pointsPlaceholder > 0) {
      int minDateIndex = -1;
      for (int i = 0; i < timestampArray.size(); i++) {
        if ((timestampArray.get(i) < timestampArray.get(minDateIndex))
            && alreadyAddedList.contains(timestampArray.get(i)) == false) {
          minDateIndex = i;
        }
      }
      if (minDateIndex == -1) {
        break;
      }
      if (pointsPlaceholder - pointsArray.get(minDateIndex) >= 0) {
        pointsPlaceholder = pointsPlaceholder - pointsArray.get(minDateIndex);
        pointsArray.set(minDateIndex, 0);
      } else {
        pointsArray.set(minDateIndex, pointsArray.get(minDateIndex) - pointsPlaceholder);
        pointsPlaceholder = 0;
      }
      alreadyAddedList.add(timestampArray.get(minDateIndex));
    }

  }

  public String printOutput() {
    ArrayList<String> duplicateList = new ArrayList<String>();
    for (int i = 0; i < payerArray.size(); i++) {
      if (!(duplicateList.contains(payerArray.get(i)))) {
        duplicateList.add(payerArray.get(i) + ": " + pointsArray.get(i));
      } else {
        for (int j = 0; j < duplicateList.size(); j++) {
          if (duplicateList.get(j).contains(payerArray.get(i))) {
            String[] arraySplit = duplicateList.get(j).trim().split(": ");
            int pointsToAddTo = Integer.parseInt(arraySplit[1]);
            pointsToAddTo = pointsToAddTo + pointsArray.get(i);
            duplicateList.set(j, payerArray.get(i) + ": " + pointsToAddTo);
          }
        }
      }
    }
    // display final output
    String output = "";
    for (int k = 0; k < duplicateList.size(); k++) {
      output += duplicateList.get(k) + "\n";
    }
    return output.trim();
  }

  public static void main(String[] args) throws FileNotFoundException {
    int amountOfPointsToSpend = 5000;
    String csvFileString = "csvfiles/transactions.csv";
    // int amountOfPointsToSpend = Integer.parseInt(args[0]);
    // String csvFileString = args[1];
    File csvFile = new File(csvFileString);
    FetchTransactions f1 = new FetchTransactions();
    f1.loadTransactions(csvFile);
    f1.getOutput(5000);
    System.out.println(f1.printOutput());


  }
}
