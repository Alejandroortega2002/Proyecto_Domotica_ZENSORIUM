package controlador;

/**
 * Sample Skeleton for 'firstGUI.fxml' Controller Class
 */

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class firstController {

	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() {
	}

	@FXML
	void mostrarPantallaEntrar(ActionEvent event) {
		// Método invocado al hacer clic en el botón

		try {
			// Crea un cargador de FXML para cargar otra vista llamada verTexto.fxml
			FXMLLoader loader2 = new FXMLLoader(getClass().getResource("/vista/segundaPantallaEntrar.fxml"));

			// Crea una instancia del controladorTexto, que probablemente manejará la nueva
			// vista
			ControladorPantallaEntrar control2 = new ControladorPantallaEntrar();

			// Asocia el controlador control2 con el cargador loader2
			loader2.setController(control2);

			// Carga la vista desde el archivo FXML y obtiene el nodo raíz
			Parent root2 = loader2.load();

			// Crea una nueva ventana emergente (Stage)
			Stage stage = new Stage();

			// Configura la escena de la ventana emergente con el nodo raíz cargado
			// previamente
			stage.setScene(new Scene(root2));

			// Configura la modalidad de la ventana emergente como WINDOW_MODAL, lo que la
			// hace modal
			stage.initModality(Modality.WINDOW_MODAL);

			// Obtén una referencia a la ventana actual
			Stage ventanaActual = (Stage) ((Node) event.getSource()).getScene().getWindow();

			// Cierra la ventana actual
			ventanaActual.close();

			// Establece la ventana principal como la propietaria de la ventana emergente
			stage.initOwner(((Node) (event.getSource())).getScene().getWindow());

			// Muestra la ventana emergente
			stage.show();
		} catch (Exception e) {
			// Captura y maneja cualquier excepción que pueda ocurrir
			e.printStackTrace();
		}
	}

	@FXML
	void alClicar(MouseEvent event) {

		// Método invocado al hacer clic en el botón

		try {
			// Crea un cargador de FXML para cargar otra vista llamada verTexto.fxml
			FXMLLoader loader2 = new FXMLLoader(getClass().getResource("/vista/pantallaRecuperarContrasena.fxml"));

			// Crea una instancia del controladorTexto, que probablemente manejará la nueva
			// vista
			ControladorRecContrasena control2 = new ControladorRecContrasena();

			// Asocia el controlador control2 con el cargador loader2
			loader2.setController(control2);

			// Carga la vista desde el archivo FXML y obtiene el nodo raíz
			Parent root2 = loader2.load();

			// Crea una nueva ventana emergente (Stage)
			Stage stage = new Stage();

			// Configura la escena de la ventana emergente con el nodo raíz cargado
			// previamente
			stage.setScene(new Scene(root2));

			// Configura la modalidad de la ventana emergente como WINDOW_MODAL, lo que la
			// hace modal
			stage.initModality(Modality.WINDOW_MODAL);

			// Obtén una referencia a la ventana actual
			Stage ventanaActual = (Stage) ((Node) event.getSource()).getScene().getWindow();

			// Cierra la ventana actual
			ventanaActual.close();

			// Establece la ventana principal como la propietaria de la ventana emergente
			stage.initOwner(((Node) (event.getSource())).getScene().getWindow());

			// Muestra la ventana emergente
			stage.show();
		} catch (Exception e) {
			// Captura y maneja cualquier excepción que pueda ocurrir
			e.printStackTrace();
		}
	}

}