package GUI;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import logic.CommonController;
import logic.Main;

public class StatisticReportsGUI_EmployeeRecords implements GuiInterface,Initializable{

    @FXML
    private AnchorPane employeePane;
	
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

    @FXML
    void backScreen(ActionEvent event) throws IOException {
    	AnchorPane pane=FXMLLoader.load(getClass().getResource("/GUI/StatisticReports.fxml"));
    	employeePane.getChildren().setAll(pane);
    }

	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		Main.client.clientUI=this;
		
		
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

	@Override
	public void showSuccess(String string) {
		// TODO Auto-generated method stub
		
	}

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

	@Override
	public void showFailed(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void freshStart() {
		// TODO Auto-generated method stub
		
	}
	
	 private void getLibrarians() {
		 CommonController.ShowEmployeeRecords();
	  }
	
}
