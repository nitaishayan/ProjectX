package GUI;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import Common.Book;
import Common.GuiInterface;
import Common.Librarian;
import Common.Member;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import logic.CommonController;
import logic.Main;
import logic.StatisticReportsController;


public class StatisticsReportsGUI_LateInReturn implements Initializable, GuiInterface{
	/**
	 * this class connect between the input in the GUI to the controller of Statistic Reports process.
	 * The class giving the functionality to choose the library statistics in any time.
	 */

	@FXML
	private AnchorPane PaneLateInReturn;
	
	@FXML
    private GridPane gridPane;

	@FXML
	private Label lbl_Average;

	@FXML
	private Label lbl_Median;

	@FXML
	private Label lbl_DecimalDistribution;

	@FXML
	private Label lbl_DecimalDistribution1;

	@FXML
	private TableView<Book> tableViewLates;

	@FXML
	private TableColumn<Book,String> bookIDCol;

	@FXML
	private TableColumn<Book,String> bookNameCol;

	@FXML
	private TableColumn<Book,String> authorsNameCol;

	@FXML
	private TableColumn<Book,String> editionNoCol;

	@FXML
	private TableColumn<Book,String> isWantedCol;

	@FXML
	private Button btn_Total;
	
	@FXML
    private Button btn_backScreen;

	

	//global variables
	public static final String TOTAL = "Total";
	public static final String SPECIFIC_BOOK = "Specific Book";
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
     	PaneLateInReturn.getChildren().setAll(pane);
    }
    
    /**
     * This method update labels and grid pane to the total statistics of the library.	
     * @param event - event cause from pressing on "Total" button.
     */
	@FXML
	void ReturnToTotal(ActionEvent event) {
		StatisticReportsController.ShowBooks(TOTAL,null);
		
	}
	
	/**
	 * this method show information pop-up on the screen with given message.
	 * @param string- the message that is shown in the pop-up.
	 */
	@Override
	public void showSuccess(String string) {
		// TODO Auto-generated method stub
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Success");
		alert.setHeaderText("An successful operation");
		alert.setContentText(string);
		alert.showAndWait();
	}

	/**
	* This method set the variable of the GuiInterface in the client to this.
	* Set properties to the specific columns of the table view.
	* Set the columns to the table view and add a click event when a row from the table view is pressed on.
	* The method give the user the functionality to press on some book and get his details.
	*/
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		Main.client.clientUI=this;

		tableViewLates.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); //resize the columns to the table view
		TableColumn<Book,String> bookIDCol = new TableColumn<Book,String>("Book ID");//attach the fxml column to the property of the column
		TableColumn<Book,String> bookNameCol = new TableColumn<Book,String>("Book Name");
		TableColumn<Book,String> authorsNameCol = new TableColumn<Book,String>("Author's Name");
		TableColumn<Book,String> editionNoCol = new TableColumn<Book,String>("Edition No.");
		TableColumn<Book,String> isWantedCol = new TableColumn<Book,String>("Is Wanted");

		bookIDCol.setSortType(TableColumn.SortType.DESCENDING);//set sort type of the column to DESCENDING
		bookNameCol.setSortType(TableColumn.SortType.DESCENDING);
		authorsNameCol.setSortType(TableColumn.SortType.DESCENDING);
		editionNoCol.setSortType(TableColumn.SortType.DESCENDING);
		isWantedCol.setSortType(TableColumn.SortType.DESCENDING);

		tableViewLates.getColumns().setAll(bookIDCol, bookNameCol,authorsNameCol,editionNoCol,isWantedCol);//attach the columns to the table view


		bookIDCol.setCellValueFactory(new PropertyValueFactory<Book,String>("bookID"));//set column property to the fxml column
		bookNameCol.setCellValueFactory(new PropertyValueFactory<Book,String>("bookName"));
		authorsNameCol.setCellValueFactory(new PropertyValueFactory<Book,String>("authorName"));
		editionNoCol.setCellValueFactory(new PropertyValueFactory<Book,String>("editionNumber"));
		isWantedCol.setCellValueFactory(new PropertyValueFactory<Book,String>("wanted"));

		StatisticReportsController.showBookTableView();

		///////// row listener - when select row
		tableViewLates.getSelectionModel().selectedIndexProperty().addListener(new RowSelectListener());
	}

	/**
	 * case "labels" - Get array list with the amount days book were late to return, the summary of days books were late to return and the maximum amount of time a book was late to return.
	 * In addittion, this case get the average, median and decimal distribution of the specific book or total library and update the appropiate labels and grid pane. 
	 * case "tableView" - This case puts inside the table view all the books that return from the search after initialize method is called.
	 * @param obj - ArrayList with the relevant data for create this window and maneuver through the different methods (all the information that needed).
	 */
	@Override
	public void display(Object obj) {

		ArrayList<String> list = (ArrayList<String>)obj;
		
		ArrayList<String> result = new ArrayList<String>();
		ArrayList<String> result2 = new ArrayList<String>();
		
		ArrayList<Book> BookListTemp = new ArrayList<>();
		
		Label label;
		
		int j=3;
		int numOfBooks = (list.size()-3)/5;
		

		
		int i=0;
		Book bookTemp=null;

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
			lbl_Average.setText(StatisticReportsController.average(sumOfDaysDelayed, numOfBookDelayed)+" Days");

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

			i=0;
			while(i<numOfBooks) {
				bookTemp = new Book(list.get(j),list.get(j+1),null,list.get(j+2),list.get(j+3),list.get(j+4),null,null,null,null,null,null);
				i++;
				j += 5;
				BookListTemp.add(bookTemp);
			}

			ObservableList<Book> bookList = FXCollections.observableArrayList(BookListTemp);//create ObservableList and attach to the book's created in getBooksStatistics function
			tableViewLates.setItems(bookList);//set ObservableList to the table view

			break;

		default:
			break;
		}
	}

	/**
	 * this method show fail information pop-up on the screen with given message.
	 * @param string- the message that is shown in the pop-up.
	 */
	@Override
	public void showFailed(String message) {
		// TODO Auto-generated method stub
		Platform.runLater(()->{
			Alert alert=new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText(message);
			alert.showAndWait();	
		});
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
			Book BookTemp = tableViewLates.getSelectionModel().getSelectedItem();//put in Book object the selected item in the table view
			StatisticReportsController.ShowBooks(SPECIFIC_BOOK,BookTemp);

		}
	}







}
