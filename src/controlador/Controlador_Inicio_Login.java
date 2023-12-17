package controlador;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import applications.Main;
import entidades.Usuario;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import modelo.RegistroManager;

public class Controlador_Inicio_Login {

	@FXML
	private Pane contenidoArea;

	@FXML
	private JFXTextField email_login;

	@FXML
	private JFXPasswordField password_login;

	@FXML
	private void initialize() {
		// Puedes realizar inicializaciones aquí si es necesario
	}

	@FXML
	private void cerrarApp(MouseEvent event) {
		// Cerrar la aplicación correctamente utilizando un Stage
		Stage stage = (Stage) contenidoArea.getScene().getWindow();
		stage.close();
	}

	@FXML
	private void IrRegistrate(MouseEvent event) throws IOException {
		// Cambiar a la vista de registro
		Parent fxml = FXMLLoader.load(getClass().getResource("/vista/Pagina_Registro.fxml"));
		contenidoArea.getChildren().setAll(fxml);
	}

	private boolean autenticarUsuario(String email, String password) {
		// Cargar la lista de usuarios desde el archivo JSON
		List<Usuario> usuarios = RegistroManager.cargarUsuarios();

		// Verificar si las credenciales coinciden con alg�n usuario registrado
		for (Usuario usuario : usuarios) {
			if (usuario.getEmail().equals(email) && usuario.getPassword().equals(password)) {
				return true; // Credenciales v�lidas
			}
		}

		return false; // Credenciales incorrectas
	}

	@FXML
	private void iniciarSesion(MouseEvent event) throws IOException {
		if (email_login != null && password_login != null) {
			String email = email_login.getText();
			String password = password_login.getText();

			boolean autenticado = autenticarUsuario(email, password);

			if (autenticado) {
				mostrarMensaje("Inicio de sesión exitoso", "¡Bienvenido, " + email + "!");
				
				Parent fxml = FXMLLoader.load(getClass().getResource("/vista/Interfaz_de_prueba_usuario.fxml"));
				Main.stage.getScene().setRoot(fxml);
				
			} else {
				// Si la autenticación falla, muestra un mensaje de error
				mostrarMensaje("Error de inicio de sesión",
						"Credenciales incorrectas. Por favor, inténtalo de nuevo.", Alert.AlertType.ERROR);
			}
		} else {
			System.out.println("email_login o password_login es null");
		}

	}

	private void mostrarMensaje(String titulo, String contenido) {
		mostrarMensaje(titulo, contenido, Alert.AlertType.INFORMATION);
	}

	private void mostrarMensaje(String titulo, String contenido, Alert.AlertType tipo) {
		Alert alert = new Alert(tipo);
		alert.setTitle(titulo);
		alert.setHeaderText(null);
		alert.setContentText(contenido);
		alert.showAndWait();
	}
}