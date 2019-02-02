package GUI;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import Client.Client;
import Common.GuiInterface;
import Common.InBoxMessage;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import logic.CommonController;
import logic.Main;
import logic.SearchBookController;

public class InBoxGUI implements Initializable, GuiInterface{

    @FXML
    private TableView<InBoxMessage> table;

    @FXML
    private TableColumn<InBoxMessage, String> dateCol;

    @FXML
    private TableColumn<InBoxMessage, String> topicCol;

    @FXML
    private TableColumn<InBoxMessage, String> contentCol;

    @FXML
    private Label inBoxLabel;
    
    @FXML
    private TextArea txtArea;

	@Override
	public void showSuccess(String string) {
		// TODO Auto-generated method stub	
	}

	@Override
	public void display(Object obj) {
		
		ArrayList<String> 			arrayMessage 	= (ArrayList<String>)obj;

		if (arrayMessage.size() <= 2)
		{
			showFailed("You dont have messages currently");
			return;
		}
		
		ObservableList<InBoxMessage> list		     = FXCollections.observableArrayList();
		int 				 		 numberOfMessage = (arrayMessage.size()-1)/3;
		int							 i				 = 0;
		int							 j			 	 = 0;

		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		dateCol.setCellValueFactory(new PropertyValueFactory<InBoxMessage,String>("date"));
		topicCol.setCellValueFactory(new PropertyValueFactory<InBoxMessage,String>("topic"));
		contentCol.setCellValueFactory(new PropertyValueFactory<InBoxMessage,String>("content"));
		
		CommonController.setColumnWidth(topicCol, 150, 160, 170);
		CommonController.setColumnWidth(dateCol, 160, 170, 180);
		
		Platform.runLater(() -> {	
			table.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					if (table.getSelectionModel().getSelectedItem()==null)
						return;
					txtArea.clear();
					txtArea.setText(table.getSelectionModel().getSelectedItem().getContent());
				}
			});
		});
		while(i<numberOfMessage)
		{
			InBoxMessage newMessage = new InBoxMessage(arrayMessage.get(j+1), arrayMessage.get(j+2),arrayMessage.get(j+3));
			list.add(newMessage);
			i++;
			j+=3;
		}
		table.setItems(list);
	}

	@Override
	public void showFailed(String message) {
		Alert alert=new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText(message);
		alert.showAndWait();	
	}

	@Override
	public void freshStart() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Main.client.clientUI = this;
		CommonController.getMessage(Client.arrayUser.get(0));
	}

}
