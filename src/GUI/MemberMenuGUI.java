package GUI;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import Client.Client;
import Common.GuiInterface;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logic.Main;
import logic.RegistrationController;

public class MemberMenuGUI implements Initializable,GuiInterface{

	@FXML
	private SplitPane mainSplitPane;

	@FXML
	private AnchorPane leftPane;

	@FXML
	private Label lblUser_name;

	@FXML
	private ImageView asd;
	
	@FXML
    private Button btnPersonalData;

    @FXML
    private Button btnSearchBook;

    @FXML
    private Button btnHistoryActivities;

    @FXML
    private Button btnExtend;

	@FXML
	private AnchorPane rightPane;
	
	public void clerEffect(Button btn) {
		btnExtend.setEffect(null);
		btnHistoryActivities.setEffect(null);
		btnPersonalData.setEffect(null);
		btnSearchBook.setEffect(null);
		btn.setEffect(setEffect());
	}

	public InnerShadow setEffect() {
		InnerShadow innerShadow = new InnerShadow();
		innerShadow.setChoke(0.32);
		innerShadow.setColor(Color.web("#2176ff"));
		return innerShadow;
	}
	
	@FXML
    void InboxMouse(MouseEvent event) throws IOException {
		Stage primaryStage = new Stage();
		primaryStage.initModality(Modality.APPLICATION_MODAL);
		FXMLLoader loader = new FXMLLoader();
		AnchorPane root = loader.load(getClass().getResource("/GUI/InBox.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.showAndWait();
    }

	public void Display() throws IOException {
		System.out.println("1");
		Stage primaryStage=new Stage();
		System.out.println("2");
		Parent root = FXMLLoader.load(getClass().getResource("/GUI/ReaderMenu.fxml"));
		System.out.println("3");
		Scene scene = new Scene(root);
		primaryStage.setTitle("Member Menu");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	public void init() throws IOException {
		Main.client.clientUI= this;
		lblUser_name.setText(Client.arrayUser.get(2)+" "+Client.arrayUser.get(3));
		leftPane.maxWidthProperty().bind(mainSplitPane.widthProperty().multiply(0.1855));
		leftPane.minWidthProperty().bind(mainSplitPane.widthProperty().multiply(0.1855));
		AnchorPane pane=FXMLLoader.load(getClass().getResource("/GUI/ReaderPersonalData.fxml"));
		clerEffect(btnPersonalData);
		rightPane.getChildren().setAll(pane);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {	
		try {
			init();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	public void Logout(ActionEvent event) throws IOException {
		OBLcontroller.memberStage.close();
		RegistrationController.logout(Client.arrayUser.get(0),Client.arrayUser.get(1));
		Client.arrayUser.clear();
		Main.primary.show();
	}

	
	@FXML
	void HistoryActivities(ActionEvent event) throws IOException {
		clerEffect(btnHistoryActivities);
		AnchorPane pane=FXMLLoader.load(getClass().getResource("/GUI/ActivitiesHistory.fxml"));
		rightPane.getChildren().setAll(pane);
	}

	@FXML
	void PersonalDataScreen(ActionEvent event) throws IOException {
		clerEffect(btnPersonalData);
		AnchorPane pane=FXMLLoader.load(getClass().getResource("/GUI/ReaderPersonalData.fxml"));
		rightPane.getChildren().setAll(pane);
	}

	@FXML
	void SearchScreen(ActionEvent event) throws IOException {
		clerEffect(btnSearchBook);
		AnchorPane pane=FXMLLoader.load(getClass().getResource("/GUI/SearchBook.fxml"));
		rightPane.getChildren().setAll(pane);
	}
	
    @FXML
    void extendLoanPeriodScreen(ActionEvent event) throws IOException{
    	clerEffect(btnExtend);
		AnchorPane pane=FXMLLoader.load(getClass().getResource("/GUI/MemberExtendLoanPeriod.fxml"));
		rightPane.getChildren().setAll(pane);
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
