package GUI;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import Common.GuiInterface;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
/**
 * Class that present the activities history based on the options that the member can enter
 * the options are - status history , loan history, delay history
 * when the member will press the button - a table view appeared with all the details 
 * @author nitay shayan
 *
 */
public class ActivitiesHistoryGUI implements Initializable,GuiInterface {

	@FXML
	private Button btnHistory;

	/**
	 * This method is launch a query in data base to get all the books 
	 * information by a specific member that is delayed or lost books in the library.
	 * @param event Event happend by click on the button
	 * @throws IOException
	 *  
	 */
	@FXML
	void delayHistory(ActionEvent event) throws IOException {
		Parent parent=FXMLLoader.load(getClass().getResource("/GUI/DelayandLostTableView.fxml"));
		Scene scene=new Scene(parent);
		Stage stage=new Stage();
		stage.setScene(scene);
		stage.setMaxHeight(555);
		stage.setMinHeight(555);
		stage.setMinWidth(767);
		stage.setMaxWidth(767);
		stage.show();
	}
	
	/**
	 *  Method that launch a query to find a loan history by a memberID inserted by the librarian and recieve the data from the data base
	 * eventually display the data in table view window
	 * 
	 * @param event press the button
	 * @throws IOException
	 */
	@FXML
	void loanHistory(ActionEvent event) throws IOException {
		//Load page of loan history
		Parent parent=FXMLLoader.load(getClass().getResource("/GUI/HistoryOfLoanTableView.fxml"));
		Scene scene=new Scene(parent);
		Stage stage=new Stage();
		stage.setScene(scene);
		stage.setMaxHeight(648);
		stage.setMinHeight(648);
		stage.setMinWidth(1107);
		stage.setMaxWidth(1107);
		stage.show();
	}

	/**
	 *  Method that launch a query to find status activity of a specific member in the data base.
	 * eventually display the data in table view window
	 * 
	 * @param  event press the button
	 * @throws IOException
	 */
	@FXML
	void statusHistory(ActionEvent event) throws IOException {
		Parent parent=FXMLLoader.load(getClass().getResource("/GUI/MemberStatusHistory.fxml"));
		Scene scene=new Scene(parent);
		Stage stage=new Stage();
		stage.setScene(scene);
		stage.setMaxHeight(522);
		stage.setMinHeight(522);
		stage.setMinWidth(818);
		stage.setMaxWidth(818);
		stage.show();
	}

	/**
	 * show Success message based on the String
	 */
	@Override
	public void showSuccess(String string) {
		Alert alert=new Alert(AlertType.INFORMATION);
		alert.setTitle("Success");
		alert.setHeaderText(string);
		alert.showAndWait();						
	}
	@Override
	public void display(Object obj) {
		// TODO Auto-generated method stub

	}
	/**
	 * show failed message based on the String
	 */
	@Override
	public void showFailed(String message) {
		// TODO Auto-generated method stub

	}
	@Override
	public void freshStart() {
		// TODO Auto-generated method stub

	}
	/**
	 * initialize method that launch page as he open from another screen
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}
}
