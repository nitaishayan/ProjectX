package GUI;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import Common.GuiInterface;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import logic.Main;

public class StatisticReportsGUI_Manager implements Initializable, GuiInterface{

    @FXML
    private AnchorPane StatisticsPane;
	
    @FXML
    private Button btnReaderCards_statistics;

    @FXML
    private Button btnLoans_statistics;

    @FXML
    private Button btnEmployeeRecords_statistics;

    @FXML
    private Button btnActivity_statistics;

    @FXML
    private Button btnLateOnReturn_statistics;
    
    
    @FXML
    void ActivityScreen(ActionEvent event) {

    }

    /**
	 * This method move the user to the StatisticsReports-EmployeeRecords screen.
	 * @param event - event from press on "Employee Records" button.
	 * @throws IOException
	 */
    @FXML
    void EmployeeRecordsScreen(ActionEvent event) throws IOException {
    	AnchorPane pane=FXMLLoader.load(getClass().getResource("/GUI/StatisticsReports-EmployeeRecords.fxml"));
    	StatisticsPane.getChildren().setAll(pane);
    }

    /**
	 * This method move the user to the StatisticsReports-LateInReturn screen.
	 * @param event - event from press on "Late On Return" button.
	 * @throws IOException
	 */
    @FXML
    void LateOnReturnScreen(ActionEvent event) throws IOException {
    	AnchorPane pane=FXMLLoader.load(getClass().getResource("/GUI/StatisticsReports-LateInReturn.fxml"));
    	StatisticsPane.getChildren().setAll(pane);
    }

    /**
   	 * This method move the user to the StatisticReports-Loans screen.
   	 * @param event - event from press on "Loans" button.
   	 * @throws IOException
   	 */
    @FXML
    void LoansScreen(ActionEvent event) throws IOException {
    	AnchorPane pane=FXMLLoader.load(getClass().getResource("/GUI/StatisticReports-Loans.fxml"));
    	StatisticsPane.getChildren().setAll(pane);
    }

    /**
   	 * This method move the user to the StatisticReports-ReaderCards screen.
   	 * @param event - event from press on "Reader Cards" button.
   	 * @throws IOException
   	 */
    @FXML
    void ReaderCardsScreen(ActionEvent event) throws IOException {
    	AnchorPane pane=FXMLLoader.load(getClass().getResource("/GUI/StatisticReports-ReaderCards.fxml"));
    	StatisticsPane.getChildren().setAll(pane);
    }

    /**
	* Not used method(must implement because the implementation of GuiInterface)
	*/
	@Override
	public void showSuccess(String string) {
		// TODO Auto-generated method stub
		
	}

	/**
	* Not used method(must implement because the implementation of GuiInterface)
	*/
	@Override
	public void display(Object obj) {
		// TODO Auto-generated method stub
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
	 * This method set the variable of the GuiInterface in the client to this.
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		Main.client.clientUI=this;
	}
	
}
