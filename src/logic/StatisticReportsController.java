package logic;

import java.text.DecimalFormat;
import java.util.ArrayList;

import com.sun.org.apache.xpath.internal.operations.Bool;

import Common.Book;
import sun.security.action.GetBooleanAction;

public class StatisticReportsController {
	
	private static DecimalFormat df2 = new DecimalFormat("#.##");
	
	public static void ShowBooks(String option,Book book) {
		ArrayList<String> BooksList = new ArrayList<>();
		BooksList.add("StatisticsBooks");
		BooksList.add(option);
		if (option.equals("Specific Book")) {
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
	
	public static ArrayList<String> decimalDistributionString(float maxDelayedDays) {
		
		ArrayList<String> decimal = new ArrayList<String>();
		String DecimalDistribution="";
		float partsTemp = 0 , parts, Temp;
		parts = maxDelayedDays/10;
		
		for (int k = 0; k < 10; k++,partsTemp+=parts) {
			Temp = partsTemp+parts;
			//DecimalDistribution = DecimalDistribution + " " + df2.format(partsTemp) + " - " + df2.format(Temp) + " | ";
			///
			//
			decimal.add(df2.format(partsTemp)+" - "+df2.format(Temp));
		}
	System.out.println("inside decimal distribution string");
		////return DecimalDistribution;
	return decimal;
	}
	
	public static ArrayList<String> decimalDistributionCalculation(ArrayList<Float> DaysArray,float maxDelayedDays) {
		System.out.println(DaysArray.toString()+" "+maxDelayedDays+" inside statsController + maxdaysdelayed");
		
		ArrayList<String> decimalCalc = new ArrayList<String>();
		ArrayList<Integer> partsArray = new ArrayList<Integer>();
		
		String resultString = "";
		float partsTemp = 0;
		float parts = maxDelayedDays/10;
		float Temp;
		int g=0,results;
		
		//initialize the arrayList to ZEROS
		for (int k = 0; k < 10; k++)
			partsArray.add(0);
		
		for (int k = 0; k < 10; k++,partsTemp+=parts) {
			Temp = partsTemp+parts;
			
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
			//resultString = resultString + "             " + partsArray.get(k);
			decimalCalc.add(String.valueOf(partsArray.get(k)));
		}
		return decimalCalc;
		//return resultString;
	}

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

	public static String average(float numOfDaysDelayed, float numOfBookDelayed) {
		return String.valueOf(numOfDaysDelayed/numOfBookDelayed);
	}
	
	public static void showBookTableView () {
		
		ArrayList<String> BooksList = new ArrayList<>();
		BooksList.add("showTableView");
		Main.client.handleMessageFromClientUI(BooksList);
		}
	}
