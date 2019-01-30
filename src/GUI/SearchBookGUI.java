package GUI;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import Client.Client;
import Common.BookPro;
import Common.GuiInterface;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logic.SearchBookController;
import logic.BookHandlerController;
import logic.Main;

public class SearchBookGUI implements Initializable, GuiInterface{

	@FXML
	private RadioButton radio_btn_book_name;

	@FXML
	private ToggleGroup choice;

	@FXML
	private TextField txtBook_Name;

	@FXML
	private RadioButton radio_btn_authors_name;

	@FXML
	private TextField txtAuthor_Name;

	@FXML
	private RadioButton radio_btn_book_theme;

	@FXML
	private TextField txtBook_Theme;

	@FXML
	private RadioButton radio_btn_free_text;

	@FXML
	private TextField txtFree_Text;

	@FXML
	private Button btnSearch;

	@FXML
	private Button btnBack;

	@FXML
	void onBackClick(ActionEvent event) throws IOException {
		OBLcontroller.searchForReader.close();
		Main.primary.show();
	}

	@FXML
	void onSearchClick(ActionEvent event) {
		String searchPick;
		if (choice.getSelectedToggle().equals(radio_btn_book_name))
		{
			searchPick = "Book Name";
			SearchBookController.searchBook(searchPick,txtBook_Name.getText());
		}
		else if (choice.getSelectedToggle().equals(radio_btn_authors_name))
		{
			searchPick = "Authors Name";
			SearchBookController.searchBook(searchPick,txtAuthor_Name.getText());
		}
		else if (choice.getSelectedToggle().equals(radio_btn_book_theme))
		{
			searchPick = "Book Theme";
			SearchBookController.searchBook(searchPick,txtBook_Theme.getText());
		}
		else if (choice.getSelectedToggle().equals(radio_btn_free_text))
		{
			searchPick = "Free text";
			SearchBookController.searchBook(searchPick, txtFree_Text.getText());	
		}
	}

	@FXML
	void enterPress(KeyEvent event) {
		if (event.getCode() == KeyCode.ENTER){
			String searchPick;
			if (choice.getSelectedToggle().equals(radio_btn_book_name))
			{
				searchPick = "Book Name";
				SearchBookController.searchBook(searchPick,txtBook_Name.getText());
			}
			else if (choice.getSelectedToggle().equals(radio_btn_authors_name))
			{
				searchPick = "Authors Name";
				SearchBookController.searchBook(searchPick,txtAuthor_Name.getText());
			}
			else if (choice.getSelectedToggle().equals(radio_btn_book_theme))
			{
				searchPick = "Book Theme";
				SearchBookController.searchBook(searchPick,txtBook_Theme.getText());
			}
			else if (choice.getSelectedToggle().equals(radio_btn_free_text))
			{
				searchPick = "Free text";
				SearchBookController.searchBook(searchPick, txtFree_Text.getText());	
			}
		}

	}

	@FXML
	void openAndCloseFields(ActionEvent event) 
	{

		if (choice.getSelectedToggle().equals(radio_btn_book_name))
		{
			freshStart();
			txtBook_Name.setDisable(false);
			txtAuthor_Name.setDisable(true);
			txtBook_Theme.setDisable(true);
			txtFree_Text.setDisable(true);
		}

		if(choice.getSelectedToggle().equals(radio_btn_authors_name))
		{
			freshStart();
			txtAuthor_Name.setDisable(false);
			txtBook_Name.setDisable(true);
			txtBook_Theme.setDisable(true);
			txtFree_Text.setDisable(true);
		}

		if(choice.getSelectedToggle().equals(radio_btn_book_theme))
		{
			freshStart();
			txtBook_Theme.setDisable(false);
			txtAuthor_Name.setDisable(true);
			txtBook_Name.setDisable(true);
			txtFree_Text.setDisable(true);
		}

		if(choice.getSelectedToggle().equals(radio_btn_free_text))
		{
			freshStart();
			txtFree_Text.setDisable(false);
			txtBook_Theme.setDisable(true);
			txtAuthor_Name.setDisable(true);
			txtBook_Name.setDisable(true);
		}
	}

	@Override
	public void showSuccess(String string) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Confirm");
		alert.setHeaderText(string);
		alert.showAndWait();	
	}

	@Override
	public void display(Object obj) 
	{
		ArrayList<String>    		 datalist 			 = 	(ArrayList<String>)obj;
		if ((datalist.get(0).equals("SearchBookDetailes")))
			displayBookDetails(datalist);
		else {
			int 				 		 numberOfBook  	 	 =  (datalist.size()-4)/8;
			int 			 			 i					 =	0;
			int							 j					 =  0;
			Label						  searchLab			 =  new Label("Search book result");
			Stage 				 		  primaryStage 		 =  new Stage();
			VBox 					 	  root				 =  new VBox(20);
			ObservableList<BookPro> 	  bookList 			 =  FXCollections.observableArrayList();
			TableView<BookPro> 			  table				 =  new TableView<>();
			TableColumn<BookPro, String>  bookIDCol		 	 =  new TableColumn<>("Book ID");
			TableColumn<BookPro, String>  bookNameCol		 =  new TableColumn<>("Book name");
			TableColumn<BookPro, String>  authorNameCol		 =  new TableColumn<>("Author name");
			TableColumn<BookPro, String>  bookGenreCol		 =  new TableColumn<>("Book genre");
			TableColumn<BookPro, String>  descriptionCol	 =  new TableColumn<>("Description");

			primaryStage.initModality(Modality.APPLICATION_MODAL);
			table.getColumns().addAll(bookIDCol,bookNameCol,authorNameCol,bookGenreCol,descriptionCol);
			table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
			bookIDCol.setCellValueFactory(cellData -> cellData.getValue().getBookID());
			bookNameCol.setCellValueFactory(cellData -> cellData.getValue().getBookName());
			authorNameCol.setCellValueFactory(cellData -> cellData.getValue().getAuthorName());
			bookGenreCol.setCellValueFactory(cellData-> cellData.getValue().getBookGenre());
			descriptionCol.setCellValueFactory(cellData -> cellData.getValue().getDescription());

			while(i<numberOfBook)
			{
				BookPro newBook = new BookPro(datalist.get(j+8),datalist.get(j+4), datalist.get(j+5),datalist.get(j+6),datalist.get(j+7));
				bookList.add(newBook);
				i++;
				j+=8;
			}

			Platform.runLater(() -> {	
				table.setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						if (Client.arrayUser.size() > 2)
						{
							SearchBookController.searchBookDetailes(table.getSelectionModel().getSelectedItem().getBookID().getValue(),table.getSelectionModel().getSelectedItem().getBookName().getValue(), table.getSelectionModel().getSelectedItem().getAuthorName().getValue());
						}
					}

				});
			});

			System.out.println(obj);
			table.setItems(bookList);
			root.getChildren().addAll(searchLab,table);
			searchLab.setFont(new Font(20));
			searchLab.setStyle("-fx-font-weight: bold");
			searchLab.setPrefWidth(180);
			searchLab.setPrefHeight(35);
			primaryStage.setTitle("Search book result");
			root.setAlignment(Pos.CENTER);
			root.setPrefWidth(800);
			root.setPrefHeight(400);
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.showAndWait();
		}
	}

	private void displayBookDetails(ArrayList<String> detailesData) {
		Stage 	   	 primaryStage   = new Stage();
		VBox 	 	 mainVbox       = new VBox(20);
		Label 		 ans  			= new Label();
		Label		 detailes		= new Label();
		Scene 		 scene 			= new Scene(mainVbox);
		Button		 tableOfContent = new Button("Table Of Content");
		HBox 		 hbox2			= new HBox(20);
		String		 BookID			= detailesData.get(3);

		tableOfContent.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				BookHandlerController.getPDF(BookID);
			}
		});

		primaryStage.initModality(Modality.APPLICATION_MODAL);
		mainVbox.setMinHeight(390);
		mainVbox.setMinWidth(550);
		mainVbox.setMaxHeight(390);
		mainVbox.setMaxWidth(550);
		detailes.setText("Detailes result");
		detailes.setFont(new Font("Ariel", 22));
		mainVbox.getChildren().add(detailes);
		mainVbox.setAlignment(Pos.CENTER);
		primaryStage.setTitle("Detailes result");

		System.out.println(detailesData);

		if (detailesData.get(4).equals("1")) //return the location
		{
			ans.setText("The book " + detailesData.get(1) + " of the author " + detailesData.get(2) + " is in shelf- " + detailesData.get(5));
			ans.setFont(new Font("Ariel", 16));
			mainVbox.getChildren().addAll(ans,tableOfContent);
		}
		else {
			Button reserveBtn = new Button("Reserve");
			reserveBtn.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					System.out.println("liorrrrr"+detailesData);
					BookHandlerController.reserveBook(detailesData.get(3),Client.arrayUser.get(0),detailesData.get(7));		
				}
			});
			Label  ans2		  = new Label();
			ans.setText("we don't have copy of " + detailesData.get(1) + " by the author " + detailesData.get(2) + " in the library.");
			ans2.setText(" the nearest return date is in " + detailesData.get(5));
			ans.setFont(new Font("Ariel", 16));
			ans2.setFont(new Font("Ariel", 16));
			ans.setPadding(new Insets(0, 0, 0, 20));
			ans2.setPadding(new Insets(0, 0, 0, 20));
			hbox2.getChildren().addAll(reserveBtn,tableOfContent);
			hbox2.setAlignment(Pos.CENTER);
			mainVbox.getChildren().addAll(ans,ans2,hbox2);
		}
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.showAndWait();

	}

	@Override
	public void showFailed(String message) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Message");
		alert.setHeaderText(message);
		alert.showAndWait();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Main.client.clientUI = this;
		if (!Client.arrayUser.isEmpty()) {
			btnBack.setVisible(false);
		}
	}

	@Override
	public void freshStart() {
		txtBook_Name.clear();
		txtAuthor_Name.clear();
		txtBook_Theme.clear();
		txtFree_Text.clear();
	}
}
