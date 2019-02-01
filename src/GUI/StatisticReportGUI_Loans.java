package GUI;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Common.Book;
import Common.GuiInterface;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import logic.Main;
import logic.StatisticReportsController;
import javafx.scene.layout.AnchorPane;

public class StatisticReportGUI_Loans implements Initializable, GuiInterface{
	/**
	 * this class connect between the input in the GUI to the controller of Statistic Reports (loans) process.
	 * The class giving the functionality to choose the library statistics in any time.
	 */

	
	@FXML
	private AnchorPane AnchorPane;

	@FXML
	private GridPane gridPane;

	@FXML
	private Label lbl_average;

	@FXML
	private Label lbl_Median;

	@FXML
	private TableView<Book> tableView;

	@FXML
	private TableColumn<Book,String> col_bookID;

	@FXML
	private TableColumn<Book,String> col_bookName;

	@FXML
	private TableColumn<Book,String> col_authorName;

	@FXML
	private TableColumn<Book,String> col_editionNumber;

	@FXML
	private TableColumn<Book,String> col_wanted;

	@FXML
	private Button btn_backScreen;
	
	//global variables
		public static final String TOTAL = "Total";
		public static final String SPECIFIC_LOANBOOK = "Specific LoanBook";
		ArrayList<Float> DelayedBooksDays = new ArrayList<Float>();

		int AmountDaysLate;
		float sumOfDaysDelayed, numOfBookDelayed;

	/**
	 * This method move the user back to the Main Statistic Reports screen.	
	 * @param event - event cause from pressing on "Back" button.
	 * @throws IOException
	 */
	@FXML
	void backScreen(ActionEvent event) throws IOException {
		AnchorPane pane=FXMLLoader.load(getClass().getResource("/GUI/StatisticReports.fxml"));
		AnchorPane.getChildren().setAll(pane);
	}

	
	/**
	 * Not used method(must implement because the implementation of GuiInterface)
	 */
	@Override
	public void showSuccess(String string) {
		// TODO Auto-generated method stub

	}
	
	/**
	 * This method set the variable of the GuiInterface in the client to this.
	 * Set properties to the specific columns of the table view.
	 * Set the columns to the table view and add a click event when a row from the table view is pressed on.
	 * The method give the user the functionality to press on some book and get the loan book statistics.
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		Main.client.clientUI=this;

		tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); //resize the columns to the table view
		TableColumn<Book,String> col_bookID = new TableColumn<Book,String>("Book ID");//attach the fxml column to the property of the column
		TableColumn<Book,String> col_bookName = new TableColumn<Book,String>("Book Name");
		TableColumn<Book,String> col_authorName = new TableColumn<Book,String>("Author's Name");
		TableColumn<Book,String> col_editionNumber = new TableColumn<Book,String>("Edition No.");
		TableColumn<Book,String> col_wanted = new TableColumn<Book,String>("Is Wanted");

		col_bookID.setSortType(TableColumn.SortType.DESCENDING);//set sort type of the column to DESCENDING
		col_bookName.setSortType(TableColumn.SortType.DESCENDING);
		col_authorName.setSortType(TableColumn.SortType.DESCENDING);
		col_editionNumber.setSortType(TableColumn.SortType.DESCENDING);
		col_wanted.setSortType(TableColumn.SortType.DESCENDING);

		tableView.getColumns().setAll(col_bookID, col_bookName,col_authorName,col_editionNumber,col_wanted);//attach the columns to the table view


		col_bookID.setCellValueFactory(new PropertyValueFactory<Book,String>("bookID"));//set column property to the fxml column
		col_bookName.setCellValueFactory(new PropertyValueFactory<Book,String>("bookName"));
		col_authorName.setCellValueFactory(new PropertyValueFactory<Book,String>("authorName"));
		col_editionNumber.setCellValueFactory(new PropertyValueFactory<Book,String>("editionNumber"));
		col_wanted.setCellValueFactory(new PropertyValueFactory<Book,String>("wanted"));

		StatisticReportsController.showBookTableView();

		///////// row listener - when select row
		tableView.getSelectionModel().selectedIndexProperty().addListener(new RowSelectListener());
	}

	/**
	 * case "labels" - Get array list with the amount days book were loaned, the summary of days books were loaned and the maximum amount of time a book was loaned.
	 * In addition, this case get the average, median and decimal distribution of the specific book or total library and update the appropiate labels and grid pane. 
	 * case "tableView" - This case puts inside the table view all the books that return from the search after initialize method is called.
	 * @param obj - ArrayList with the relevant data for create this window and maneuver through the different methods (all the information that needed).
	 */
	@Override
	public void display(Object obj) {
		// TODO Auto-generated method stub

		ArrayList<String> list = (ArrayList<String>)obj;
		ArrayList<Book> BookListTemp = new ArrayList<>();
		ArrayList<String> result = new ArrayList<String>();
		ArrayList<String> result2 = new ArrayList<String>();
		Book bookTemp=null;
		Label label;
		int numOfBooks = (list.size()-3)/5;
		int i=0;
		int j=3;

		switch (list.get(2)) {

		case "labels":

			AmountDaysLate = Integer.parseInt(list.get(4));//COUNT of delayed copies
			sumOfDaysDelayed = Float.parseFloat(list.get(3));//SUM of delayed days
			numOfBookDelayed = Integer.parseInt(list.get(4));
			float maxDelayedDays = Float.parseFloat(list.get(5));//get the MAX of the column DaysLateInReturn

			i=0;
			//initialize the arrayList(DelayedBooksDays) to values of delayed days
			DelayedBooksDays.clear();
			for (int k = 0; k < numOfBookDelayed; k++) {
				DelayedBooksDays.add(Float.parseFloat(list.get(k+6)));
			}

			//set text in label  - Average
			lbl_average.setText(StatisticReportsController.average(sumOfDaysDelayed, numOfBookDelayed)+" Days");

			//set text in label  - Median
			lbl_Median.setText(StatisticReportsController.median(DelayedBooksDays,AmountDaysLate)+" Days");

			//set text in gridPane  - DecimalDistributionString - String
			gridPane.getChildren().clear();
			result.addAll(StatisticReportsController.decimalDistributionString(maxDelayedDays));
			for (int r = 0; r < 10; r++) {
				label = new Label(result.get(r));
				gridPane.setRowIndex(label, 0);
				gridPane.setColumnIndex(label, r);

				gridPane.getChildren().add(label);
			}

			//set text in gridPane  - DecimalDistributionCalculation - Result String
			result2.addAll(StatisticReportsController.decimalDistributionCalculation(DelayedBooksDays, maxDelayedDays));
			for (int r = 0; r < 10; r++) {
				label = new Label(result2.get(r));
				gridPane.setRowIndex(label, 1);
				gridPane.setColumnIndex(label, r);

				gridPane.getChildren().add(label);
			}

			break;

		case "tableView":

			while(i<numOfBooks) {
				bookTemp = new Book(list.get(j),list.get(j+1),null,list.get(j+2),list.get(j+3),list.get(j+4),null,null,null,null,null,null);
				i++;
				j += 5;
				BookListTemp.add(bookTemp);
			}

			ObservableList<Book> bookList = FXCollections.observableArrayList(BookListTemp);//create ObservableList and attach to the book's created in getBooksStatistics function
			tableView.setItems(bookList);//set ObservableList to the table view

			break;

		default:
			break;
		}

	}

	/**
	 * Not used method(must implement because the implementation of GuiInterface)
	 */
	@Override
	public void showFailed(String message) {
		// TODO Auto-generated method stub

	}

	/**
	 * Not used method(must implement because the implementation of GuiInterface)
	 */
	@Override
	public void freshStart() {
		// TODO Auto-generated method stub

	}

	/**
	 * This private class implements ChangeListener overrides the changed method to fulfill the functionality of pressing a row
	 * in the table view and show the statistic report for this specific book chosen.
	 */
	///////// row select listener
	private class RowSelectListener implements ChangeListener {
		@Override
		public void changed(ObservableValue arg0, Object arg1, Object arg2) {
			Book BookTemp = tableView.getSelectionModel().getSelectedItem();//put in Book object the selected item in the table view
			StatisticReportsController.ShowBooks(SPECIFIC_LOANBOOK,BookTemp);

		}
	}

}
