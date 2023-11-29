package controlador;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class Controlador_Inicio_Login {

	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;
	
	@FXML 
	private Pane contenidoArea;

	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() {
	}

	@FXML
	public void cerrarApp(MouseEvent event) {
		// Implementa la lógica para cerrar la aplicación
		System.exit(0);
	}

	@FXML
	public void IrRegistrate(MouseEvent event) throws IOException {
		Parent fxml = FXMLLoader.load(getClass().getResource("/vista/Pagina_Registro.fxml"));
		contenidoArea.getChildren().removeAll();
		contenidoArea.getChildren().setAll(fxml);
	}

}