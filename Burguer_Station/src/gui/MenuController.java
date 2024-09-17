package gui;

import javafx.event.ActionEvent;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MenuController {
	
	@FXML
	private Button btnSistema;
	
	@FXML
	private Button btnCardapio;
	
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	 @FXML
	    public void TelaSistema(ActionEvent event) throws IOException {
		 root = FXMLLoader.load(getClass().getResource("Tela.fxml"));
		  stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		  scene = new Scene(root);
		  stage.setScene(scene);
		  stage.show();
	    }
	 
	 @FXML
	    public void TelaCardapio(ActionEvent event) throws IOException {
		 root = FXMLLoader.load(getClass().getResource("Cardapio.fxml"));
		  stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		  scene = new Scene(root);
		  stage.setScene(scene);
		  stage.show();
	    }
}
