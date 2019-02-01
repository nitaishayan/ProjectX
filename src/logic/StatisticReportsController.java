package logic;

import java.text.DecimalFormat;
import java.util.ArrayList;


import Common.Book;

public class StatisticReportsController {
	
	private static DecimalFormat df2 = new DecimalFormat("#.##");
	
	/**
	 * This method sends request for the server to show books.
	 * @param option - define the option arrived - Total,Specific Book,Specific Loan Book.
	 * @param book - defines the specific book or null if it's Total option.
	 */
	public static void ShowBooks(String option,Book book) {
		ArrayList<String> BooksList = new ArrayList<>();
		BooksList.add("StatisticsBooks");
		BooksList.add(option);
		if (option.equals("Specific Book") || option.equals("Specific LoanBook")) {
			if(book == null) {
				BooksList.add("null");
				}
			else {
			BooksList.add(book.getBookID());
			}
		}
		else {
			BooksList.add("null");
		}
		System.out.println(BooksList.toString()+" inside statisticsController");
		Main.client.handleMessageFromClientUI(BooksList);
	}
	
	/**
	 * Calculates the decimal distribution's parts.
	 * @param maxDelayedDays - this is the maximum days delayed/loan of all book which are late in return/loaned.
	 * @return return an array list with the decimal distribution's parts.
	 */
	public static ArrayList<String> decimalDistributionString(float maxDelayedDays) {
		
		ArrayList<String> decimal = new ArrayList<String>();
		String DecimalDistribution="";
		float partsTemp = 0 , parts, Temp;
		parts = maxDelayedDays/10;
		
		for (int k = 0; k < 10; k++,partsTemp+=parts) {
			Temp = partsTemp+parts;
			decimal.add(df2.format(partsTemp)+" - "+df2.format(Temp));
		}
	return decimal;
	}
	
	/**
	 * This method order the days delayed/loan.
	 * @param DaysArray - an array list with all the days delayed/loan. 
	 * @param maxDelayedDays - this is the maximum days delayed/loan of all book which are late in return/loaned.
	 * @return return an array list with the decimal distribution's parts.
	 */
	public static ArrayList<String> decimalDistributionCalculation(ArrayList<Float> DaysArray,float maxDelayedDays) {
		System.out.println(DaysArray.toString()+" "+maxDelayedDays+" inside statsController + maxdaysdelayed");
		
		ArrayList<String> decimalCalc = new ArrayList<String>();
		ArrayList<Integer> partsArray = new ArrayList<Integer>();
		
		String resultString = "";
		double partsTemp = 0;
		double parts = maxDelayedDays/10;
		double Temp;
		int g=0,results;
		
		//initialize the arrayList to ZEROS
		for (int k = 0; k < 10; k++)
			partsArray.add(0);
		
		for (int k = 0; k < 10; k++,partsTemp+=parts) {
			partsTemp =Double.parseDouble(new DecimalFormat("##.#").format(partsTemp));
			Temp = partsTemp+parts;
			Temp =Double.parseDouble(new DecimalFormat("##.#").format(Temp));
			
			while((DaysArray.get(g)) <= Temp) {
				
				results = partsArray.get(k);
				results++;
				partsArray.set(k,results);
				g++;
				
				if(g == DaysArray.size())
					break;
			}
		}
		
		for (int k = 0; k < 10; k++) {
			decimalCalc.add(String.valueOf(partsArray.get(k)));
		}
		return decimalCalc;
	}

	/**
	 * This method calculates the median of the library/specific books which is/are delayed/loan.
	 * @param DelayedBooksDays - an array list with all the days delayed/loan. 
	 * @param AmountDaysLate - this is the amount of times a book was delayed/loan.
	 * @return return a string with the value of the median.
	 */
	public static String median(ArrayList<Float> DelayedBooksDays, int AmountDaysLate) {
		
		int index;
		float medianRes;
		
		if ((AmountDaysLate%2)==0) {//if pair
			index = AmountDaysLate/2;
			Float part1 = DelayedBooksDays.get(index-1);
			Float part2 = DelayedBooksDays.get(index);
			medianRes = (float)(part1+part2)/2;
		}
		else {//if un-pair
			if (AmountDaysLate == 1) {
				index = 0;
				medianRes = DelayedBooksDays.get(index);
			}
			else {
			index = (AmountDaysLate-1)/2;
			medianRes = DelayedBooksDays.get(index+1);
			}

		}
		
		return String.valueOf(medianRes);
	}

	/**
	 * This method calculates the average of the library/specific books which is/are delayed/loan.
	 * @param numOfDaysDelayed - this is the amount of days a book was delayed/loan.
	 * @param numOfBookDelayed - this is the amount of times book was delayed/loan.
	 * @return return a string with the value of the average.
	 */
	public static String average(double numOfDaysDelayed, double numOfBookDelayed) {
		Double result = numOfDaysDelayed/numOfBookDelayed;
		return String.valueOf(Double.parseDouble(new DecimalFormat("##.#").format(result)));
	}
	
	/**
	 * This method sends request for the server to show books for table view.
	 */
	public static void showBookTableView () {
		
		ArrayList<String> BooksList = new ArrayList<>();
		BooksList.add("showTableView");
		Main.client.handleMessageFromClientUI(BooksList);
		}

	/**
	 * This method sends request for the server to show books for the activity report.
	 * @param startDate - the start period date.
	 * @param endDate - the end period time.
	 */
	public static void getActivityReport(String startDate, String endDate) {
		ArrayList<String> data = new ArrayList<>();
		data.add("getActivityReport");
		data.add(startDate);
		data.add(endDate);
		Main.client.handleMessageFromClientUI(data);
	}
}
