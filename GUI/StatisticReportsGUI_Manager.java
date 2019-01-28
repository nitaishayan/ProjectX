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

    @FXML
    void EmployeeRecordsScreen(ActionEvent event) {

    }

    @FXML
    void LateOnReturnScreen(ActionEvent event) {

    }

    @FXML
    void LoansScreen(ActionEvent event) {

    }

    @FXML
    void ReaderCardsScreen(ActionEvent event) throws IOException {
    	AnchorPane pane=FXMLLoader.load(getClass().getResource("/GUI/StatisticReports-ReaderCards.fxml"));
    	StatisticsPane.getChildren().setAll(pane);
    }

	@Override
	public void showSuccess(String string) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void display(Object obj) {
		// TODO Auto-generated method stub
	}

	@Override
	public void showFailed(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void freshStart() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		Main.client.clientUI=this;
	}
	
}
