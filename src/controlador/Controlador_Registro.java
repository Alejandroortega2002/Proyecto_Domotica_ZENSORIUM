package controlador;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import applications.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;

public class Controlador_Registro{

	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML
	void initialize() {
		// Initialize the controller
		// You can add additional initialization logic here
	}

	@FXML
	public void cerrarApp(MouseEvent event) {
		// Implementa la lógica para cerrar la aplicación
		System.exit(0);
	}
	@FXML
	public void volverALogin(MouseEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/vista/Pantalla_Inicio_Login.fxml"));
		Main.stage.getScene().setRoot(root);
	
	}
	// You can add methods to handle user registration or other events
}