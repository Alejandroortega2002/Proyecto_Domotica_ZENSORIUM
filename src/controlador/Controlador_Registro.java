package controlador;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import applications.Main;
import entidades.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import modelo.RegistroManager;

public class Controlador_Registro {
	
	ObservableList<String> listaTipoDeCuenta = FXCollections.observableArrayList("Usuario", "Familiar");

	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private AnchorPane contenidoAreaRegistro;

	@FXML
	private JFXTextField nombreUsuarioRegistro;

	@FXML
	private JFXTextField emailUsuarioRegistro;

	@FXML
	private JFXPasswordField contrasenaUsuarioRegistro;

	@FXML
	private JFXPasswordField repetirContrasenaUsuarioRegistro;
	
	@FXML
	private ComboBox tipoDeCuenta;
	

	@FXML
	void initialize() {
		// Initialize the controller
		// You can add additional initialization logic here
		tipoDeCuenta.setValue("Familiar");
		tipoDeCuenta.setItems(listaTipoDeCuenta);
	}

	@FXML
	private void registrarUsuario() throws IOException {
		String email = emailUsuarioRegistro.getText();
		String password = contrasenaUsuarioRegistro.getText();

		if (email.isEmpty() || password.isEmpty()) {
			// Mostrar mensaje de error si los campos están vacíos
			return;
		}

		Usuario nuevoUsuario = new Usuario(email, password);
		if (RegistroManager.registrarUsuario(nuevoUsuario)) {
			Parent fxml = FXMLLoader.load(getClass().getResource("/vista/Pantalla_Inicio_Login.fxml"));
			Main.stage.getScene().setRoot(fxml);
		} else {
			// Mostrar mensaje de error si el usuario ya existe
		}
	}
	
	
	@FXML
	public void btnRegistrarte(MouseEvent event) throws IOException {
		// Implementa la lógica para cerrar la aplicación
		registrarUsuario();
	}
	
	@FXML
	public void cerrarApp(MouseEvent event) {
		// Implementa la lógica para cerrar la aplicación
		System.exit(0);
	}

	@FXML
	public void volverALogin(MouseEvent event) throws IOException {
		Parent fxml = FXMLLoader.load(getClass().getResource("/vista/Pantalla_Inicio_Login.fxml"));
		Main.stage.getScene().setRoot(fxml);
	}
	// You can add methods to handle user registration or other events
}