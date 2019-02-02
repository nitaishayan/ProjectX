package GUI;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logic.BookHandlerController;
import logic.Main;
import logic.SearchBookController;

public class LibrarianSearchBookGUI implements GuiInterface, Initializable{

	@FXML
	private RadioButton radio_btn_copy_id;

	@FXML
	private ToggleGroup group;

	@FXML
	private TextField txtCopy_ID;

	@FXML
	private RadioButton radio_btn_book_name;

	@FXML
	private TextField txtBook_Name;

	@FXML
	private RadioButton radio_btn_authors_name;

	@FXML
	private TextField txtAuthor_name;

	@FXML
	private RadioButton radio_btn_book_theme;

	@FXML
	private TextField txtBook_Theme;

	@FXML
	private RadioButton radio_btn_free_text;

	@FXML
	private TextField txtFree_Text;

	@FXML
	void SearchBook(ActionEvent event) {
		String searchPick;
		if (group.getSelectedToggle().equals(radio_btn_book_name))
		{
			if(txtBook_Name.getText().equals(""))
			{
				return;
			}
			searchPick = "Book Name";
			SearchBookController.searchBook(searchPick,txtBook_Name.getText());
		}
		else if (group.getSelectedToggle().equals(radio_btn_authors_name))
		{
			if(txtAuthor_name.getText().equals(""))
			{
				return;
			}
			searchPick = "Authors Name";
			SearchBookController.searchBook(searchPick,txtAuthor_name.getText());
		}
		else if (group.getSelectedToggle().equals(radio_btn_book_theme))
		{
			if(txtBook_Theme.getText().equals(""))
			{
				return;
			}
			searchPick = "Book Theme";
			SearchBookController.searchBook(searchPick,txtBook_Theme.getText());
		}
		else if (group.getSelectedToggle().equals(radio_btn_free_text))
		{
			if(txtFree_Text.getText().equals(""))
			{
				return;
			}
			searchPick = "Free text";
			SearchBookController.searchBook(searchPick, txtFree_Text.getText());	
		}
		else if (group.getSelectedToggle().equals(radio_btn_copy_id))
		{
			if(txtCopy_ID.getText().equals(""))
			{
				return;
			}
			searchPick = "Copy ID";
			SearchBookController.searchBook(searchPick, txtCopy_ID.getText());	
		}
	}

	@FXML
	void searchBookEnter(KeyEvent event) {
		String searchPick;
		if (group.getSelectedToggle().equals(radio_btn_book_name))
		{
			if(txtBook_Name.getText().equals(""))
			{
				return;
			}
			searchPick = "Book Name";
			SearchBookController.searchBook(searchPick,txtBook_Name.getText());
		}
		else if (group.getSelectedToggle().equals(radio_btn_authors_name))
		{
			if(txtAuthor_name.getText().equals(""))
			{
				return;
			}
			searchPick = "Authors Name";
			SearchBookController.searchBook(searchPick,txtAuthor_name.getText());
		}
		else if (group.getSelectedToggle().equals(radio_btn_book_theme))
		{
			if(txtBook_Theme.getText().equals(""))
			{
				return;
			}
			searchPick = "Book Theme";
			SearchBookController.searchBook(searchPick,txtBook_Theme.getText());
		}
		else if (group.getSelectedToggle().equals(radio_btn_free_text))
		{
			if(txtFree_Text.getText().equals(""))
			{
				return;
			}
			searchPick = "Free text";
			SearchBookController.searchBook(searchPick, txtFree_Text.getText());	
		}
		else if (group.getSelectedToggle().equals(radio_btn_copy_id))
		{
			if(txtCopy_ID.getText().equals(""))
			{
				return;
			}
			searchPick = "Copy ID";
			SearchBookController.searchBook(searchPick, txtCopy_ID.getText());	
		}
	}

	@FXML
	void openAndCloseFields(ActionEvent event) 
	{
		if (group.getSelectedToggle().equals(radio_btn_book_name))
		{
			freshStart();
			txtBook_Name.setDisable(false);
			txtAuthor_name.setDisable(true);
			txtBook_Theme.setDisable(true);
			txtFree_Text.setDisable(true);
			txtCopy_ID.setDisable(true);
		}

		if(group.getSelectedToggle().equals(radio_btn_authors_name))
		{
			freshStart();
			txtAuthor_name.setDisable(false);
			txtBook_Name.setDisable(true);
			txtBook_Theme.setDisable(true);
			txtFree_Text.setDisable(true);
			txtCopy_ID.setDisable(true);
		}

		if(group.getSelectedToggle().equals(radio_btn_book_theme))
		{
			freshStart();
			txtBook_Theme.setDisable(false);
			txtAuthor_name.setDisable(true);
			txtBook_Name.setDisable(true);
			txtFree_Text.setDisable(true);
			txtCopy_ID.setDisable(true);
		}

		if(group.getSelectedToggle().equals(radio_btn_free_text))
		{
			freshStart();
			txtFree_Text.setDisable(false);
			txtBook_Theme.setDisable(true);
			txtAuthor_name.setDisable(true);
			txtBook_Name.setDisable(true);
			txtCopy_ID.setDisable(true);
		}

		if(group.getSelectedToggle().equals(radio_btn_copy_id))
		{
			freshStart();
			txtCopy_ID.setDisable(false);
			txtFree_Text.setDisable(true);
			txtBook_Theme.setDisable(true);
			txtAuthor_name.setDisable(true);
			txtBook_Name.setDisable(true);
		}
	}


	@Override
	public void showSuccess(String string) {
		// TODO Auto-generated method stub

	}

	@Override
	public void display(Object obj) {

		ArrayList<String>    		  datalist 			 = 	(ArrayList<String>)obj;
		int 				 		  numberOfBook  	 	 =  (datalist.size()-4)/8;
		int 			 			  i					 =	0;
		int							  j					 =  0;
		String						  ans				 =  null;
		Label						  searchLab			 =  new Label("Search book result");
		Stage 				 		  primaryStage 		 =  new Stage();
		VBox 					 	  root				 =  new VBox(20);
		ObservableList<BookPro> 	  bookList 			 =  FXCollections.observableArrayList();
		TableView<BookPro> 			  table				 =  new TableView<BookPro>();
		TableColumn<BookPro, String>  bookNameCol		 =  new TableColumn<>("Book name");
		TableColumn<BookPro, String>  authorNameCol		 =  new TableColumn<>("Author name");
		TableColumn<BookPro, String>  bookGenreCol		 =  new TableColumn<>("Book genre");
		TableColumn<BookPro, String>  descriptionCol	 =  new TableColumn<>("Description");
		TableColumn<BookPro, String>  bookIDCol	 		 =  new TableColumn<>("Book ID");
		TableColumn<BookPro, String>  numberOfCopiesCol	 =  new TableColumn<>("Number of copies");
		TableColumn<BookPro, String>  wantedCol			 =  new TableColumn<>("Is wanted");
		TableColumn<BookPro, String>  shelfLocationCol	 =  new TableColumn<>("Shelf location");


		primaryStage.initModality(Modality.APPLICATION_MODAL);
		table.getColumns().addAll(bookIDCol,bookNameCol,authorNameCol,bookGenreCol,descriptionCol,numberOfCopiesCol,wantedCol,shelfLocationCol);
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		numberOfCopiesCol.setMinWidth(40);
		bookNameCol.setCellValueFactory(cellData -> cellData.getValue().getBookName());
		authorNameCol.setCellValueFactory(cellData -> cellData.getValue().getAuthorName());
		bookGenreCol.setCellValueFactory(cellData-> cellData.getValue().getBookGenre());
		descriptionCol.setCellValueFactory(cellData -> cellData.getValue().getDescription());
		bookIDCol.setCellValueFactory(cellData -> cellData.getValue().getBookID());
		numberOfCopiesCol.setCellValueFactory(cellData -> cellData.getValue().getNumberOfCopies());
		wantedCol.setCellValueFactory(cellData -> cellData.getValue().getWanted());
		shelfLocationCol.setCellValueFactory(cellData -> cellData.getValue().getShelfLocation());
		while(i<numberOfBook)
		{
			if (datalist.get(j+10).equals("false"))
				ans= "No";
			else {
				ans = "Yes";
			}
			BookPro newBook = new BookPro(datalist.get(j+4), datalist.get(j+5),datalist.get(j+6),datalist.get(j+7),datalist.get(j+8),ans,datalist.get(j+11),datalist.get(j+9));
			bookList.add(newBook);
			i++;
			j+=8;
		}


		Platform.runLater(() -> {	
			table.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					if (table.getSelectionModel().getSelectedItem()==null)
						return;
					else {
						Stage 	   	 stage    		 = new Stage();
						VBox 	 	 mainVbox        = new VBox(20);
						Label		 detailes		 = new Label();
						Scene 		 scene 			 = new Scene(mainVbox);
						Button		 tableOfContent  = new Button("Table Of Content");
						HBox 		 hbox2			 = new HBox(20);
						String		 BookID			 = table.getSelectionModel().getSelectedItem().getBookID().getValue();
						TextArea	 description	 = new TextArea();
						Label		 descriptionBold = new Label();

						description.setMaxWidth(400);
						description.setMinWidth(400);
						description.setMaxHeight(150);
						description.setMinHeight(150);
						description.setEditable(false);
						description.setWrapText(true);
						descriptionBold.setText("The book description:");

						tableOfContent.setOnAction(new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent event) {
								BookHandlerController.getPDF(BookID);
							}
						});

						stage.initModality(Modality.APPLICATION_MODAL);
						mainVbox.setMinHeight(390);
						mainVbox.setMinWidth(550);
						mainVbox.setMaxHeight(390);
						mainVbox.setMaxWidth(550);
						detailes.setText("Detailes result");
						detailes.setFont(new Font("Ariel", 22));
						mainVbox.getChildren().add(detailes);
						mainVbox.setAlignment(Pos.CENTER);
						stage.setTitle("Detailes result");

						description.setText("The book description: " + table.getSelectionModel().getSelectedItem().getDescription().getValue());
						hbox2.getChildren().addAll(tableOfContent);
						hbox2.setAlignment(Pos.CENTER);
						mainVbox.getChildren().addAll(descriptionBold,description,hbox2);

						stage.setScene(scene);
						stage.setResizable(false);
						stage.showAndWait();
					}
				}
			});
		});

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

	@Override
	public void showFailed(String message) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Message");
		alert.setHeaderText("No matches results to your search");
		alert.showAndWait();
	}

	@Override
	public void freshStart() {
		txtCopy_ID.clear();
		txtBook_Name.clear();
		txtAuthor_name.clear();
		txtBook_Theme.clear();
		txtFree_Text.clear();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Main.client.clientUI = this;
	}

}