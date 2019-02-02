package GUI;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import Common.GuiInterface;
import Common.Librarian;
import Common.Member;
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
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import logic.CommonController;
import logic.Main;

public class StatisticReportsGUI_EmployeeRecords implements GuiInterface,Initializable{
	/**
	 * this class connect between the input in the GUI to the controller of Statistic Reports (employee records) process.
	 * The class giving the functionality to choose the library statistics in any time.
	 */

	
    @FXML
    private AnchorPane employeePane;
    @FXML
    private AnchorPane leftPane;
    
    @FXML
    private SplitPane MainSplitPane;
	
	@FXML
    private TableView<Librarian> TableViewLibrarian;

	@FXML
    private TableColumn<Librarian,String> firstNameCol;

    @FXML
    private TableColumn<Librarian,String> lastNameCol;
    
    @FXML
    private TableColumn<Librarian,String> LibrarianIDCol;

    @FXML
    private Label LibrarianID;

    @FXML
    private Label FirstName;

    @FXML
    private Label LastName;

    @FXML
    private Label Email;

    @FXML
    private Label IsManager;
    
    @FXML
    private Button btn_backScreen;

    
    /**
	 * This method move the user back to the Main Statistic Reports screen.	
	 * @param event - event cause from pressing on "Back" button.
	 * @throws IOException
	 */
    @FXML
    void backScreen(ActionEvent event) throws IOException {
    	AnchorPane pane=FXMLLoader.load(getClass().getResource("/GUI/StatisticReports.fxml"));
    	employeePane.getChildren().setAll(pane);
    }

	/**
	 * This method set the variable of the GuiInterface in the client to this.
	 * Set properties to the specific columns of the table view.
	 * Set the columns to the table view and add a click event when a row from the table view is pressed on.
	 * The method give the user the functionality to press on some librarian and get her personal details.
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		Main.client.clientUI=this;
		leftPane.maxWidthProperty().bind(MainSplitPane.widthProperty().multiply(0.4947));
		leftPane.minWidthProperty().bind(MainSplitPane.widthProperty().multiply(0.4947));
		
		TableViewLibrarian.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); //resize the columns to the table view
		TableColumn<Librarian,String> firstNameCol = new TableColumn<Librarian,String>("First Name");//attach the fxml column to the property of the column
		TableColumn<Librarian,String> lastNameCol = new TableColumn<Librarian,String>("Last Name");
		TableColumn<Librarian,String> LibrarianIDCol = new TableColumn<Librarian,String>("Librarian ID");
		
		firstNameCol.setSortType(TableColumn.SortType.DESCENDING);//set sort type of the column to DESCENDING
		lastNameCol.setSortType(TableColumn.SortType.DESCENDING);
		LibrarianIDCol.setSortType(TableColumn.SortType.DESCENDING);
		
		TableViewLibrarian.getColumns().setAll(firstNameCol, lastNameCol,LibrarianIDCol);//attach the columns to the table view (TableViewLibrarian)
		
		
		getLibrarians();
		
		firstNameCol.setCellValueFactory(new PropertyValueFactory<Librarian,String>("firstName"));//set column property to the fxml column
		lastNameCol.setCellValueFactory(new PropertyValueFactory<Librarian,String>("lastName"));
		LibrarianIDCol.setCellValueFactory(new PropertyValueFactory<Librarian,String>("id"));
		
		///////// row listener - when select row
		TableViewLibrarian.getSelectionModel().selectedIndexProperty().addListener(new RowSelectListener());
	}
	
	/**
	 * This private class implements ChangeListener overrides the changed method to fulfill the functionality of pressing a row
	 * in the table view and show the librarian's details in the specific labels.
	 */
		 private class RowSelectListener implements ChangeListener {
				@Override
				public void changed(ObservableValue arg0, Object arg1, Object arg2) {
					// TODO Auto-generated method stub		
					Librarian LibrarianTemp = TableViewLibrarian.getSelectionModel().getSelectedItem();//put in Librarian object the selected item in the table view
					FirstName.setText(LibrarianTemp.getFirstName());
					LastName.setText(LibrarianTemp.getLastName());
					LibrarianID.setText(LibrarianTemp.getId());
					Email.setText(LibrarianTemp.getEmail());
					IsManager.setText(LibrarianTemp.getIsManager());
				}
	}

		/**
		* Not used method(must implement because the implementation of GuiInterface)
		*/
		@Override
		public void showSuccess(String string) {
			// TODO Auto-generated method stub
			
		}

		/**
		 *  This method puts inside the table view all the librarians that return from the search after initialize method is called.
		 * @param obj - ArrayList with the relevant data for create this window and maneuver through the different methods (all the information that needed).
		 */
	@Override
	public void display(Object obj) {
		// TODO Auto-generated method stub
		ArrayList<String> list = (ArrayList<String>)obj;
		int numOfLibrarian = (list.size()-1)/5;
		int i=0,j=1;
		 ArrayList<Librarian> LibrarianListTemp = new ArrayList<>();
		 Librarian librarianTemp=null;
		 while(i<numOfLibrarian) {
			 librarianTemp = new Librarian(list.get(j),null,list.get(j+3), null, list.get(j+1),list.get(j+2), null, list.get(j+4));//Librarian ID, null, email, null, first name, last name, null, is manager 
			 i++;
			 j += 5;
			 LibrarianListTemp.add(librarianTemp);
		 }
		 ObservableList<Librarian> librarianList = FXCollections.observableArrayList(LibrarianListTemp);//create ObservableList and attach to the person's created in getLibrarians function
		 TableViewLibrarian.setItems(librarianList);//set ObservableList to the table view (TableViewLibrarian)
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
	* Not used method(must implement because the implementation of GuiInterface)
	*/
	 private void getLibrarians() {
		 CommonController.ShowEmployeeRecords();
	  }
	
}
