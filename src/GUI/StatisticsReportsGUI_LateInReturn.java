package GUI;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.w3c.dom.css.Counter;

import com.sun.corba.se.spi.orbutil.fsm.Guard.Result;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

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

    @FXML
    void backScreen(ActionEvent event) throws IOException {
     	AnchorPane pane=FXMLLoader.load(getClass().getResource("/GUI/StatisticReports.fxml"));
     	PaneLateInReturn.getChildren().setAll(pane);
    }

	//global variables
	public static final String TOTAL = "Total";
	public static final String SPECIFIC_BOOK = "Specific Book";
	ArrayList<Float> DelayedBooksDays = new ArrayList<Float>();

	int AmountDaysLate;
	float sumOfDaysDelayed, numOfBookDelayed;


	@FXML
	void ReturnToTotal(ActionEvent event) {
		StatisticReportsController.ShowBooks(TOTAL,null);
		
	}

	@Override
	public void showSuccess(String string) {
		// TODO Auto-generated method stub
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Success");
		alert.setHeaderText("An successful operation");
		alert.setContentText(string);
		alert.showAndWait();
	}

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

	@Override
	public void showFailed(String message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void freshStart() {
		// TODO Auto-generated method stub

	}


	///////// row select listener
	private class RowSelectListener implements ChangeListener {
		@Override
		public void changed(ObservableValue arg0, Object arg1, Object arg2) {
			Book BookTemp = tableViewLates.getSelectionModel().getSelectedItem();//put in Book object the selected item in the table view
			StatisticReportsController.ShowBooks(SPECIFIC_BOOK,BookTemp);

		}
	}







}