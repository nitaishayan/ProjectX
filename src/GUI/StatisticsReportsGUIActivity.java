package GUI;

import java.net.URL;
import java.util.ResourceBundle;

import Common.GuiInterface;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import logic.StatisticReportsController;

public class StatisticsReportsGUIActivity implements GuiInterface,Initializable{
	private String pickDateStart;
	private String pickDateEnd;
    @FXML
    private Label monthLabel;

    @FXML
    private Label freezeLabel;

    @FXML
    private Label nonActiveLabel;

    @FXML
    private Label numOfCopies;

    @FXML
    private Label delayMembers;

    @FXML
    private Button back;

    @FXML
    private Label activeLabel;

	public String getPickDateEnd() {
		return pickDateEnd;
	}

	public void setPickDateEnd(String pickDateEnd) {
		this.pickDateEnd = pickDateEnd;
	}

	public String getPickDateStart() {
		return pickDateStart;
	}

	public void setPickDateStart(String pickDateStart) {
		this.pickDateStart = pickDateStart;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		//StatisticReportsGUI_Manager.setEndDate(null);
		//StatisticReportsGUI_Manager.setStartDate(null);
		StatisticReportsController.getActivityReport(StatisticReportsGUI_Manager.getStartDate(),StatisticReportsGUI_Manager.getEndDate());
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

}
