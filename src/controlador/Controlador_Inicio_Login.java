package controlador;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import Controlador.firstController;
import applications.Main;
import entidades.Nodo;
import entidades.Usuario;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import modelo.RegistroManager;
import modelo.ListaEnlazada;

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
		ListaEnlazada<Usuario> usuarios = RegistroManager.cargarUsuarios();

		// Verificar si las credenciales coinciden con algún usuario registrado
		Nodo<Usuario> nodoActual = usuarios.getCabeza();
		while (nodoActual != null) {
			Usuario usuario = nodoActual.getDato();
			if (usuario.getEmail().equals(email) && usuario.getPassword().equals(password)) {
				return true; // Credenciales válidas
			}
			nodoActual = nodoActual.getEnlace();
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
				/*Parent fxml = FXMLLoader.load(getClass().getResource("/vista/Interfaz_Usuario_def.fxml"));
				Main.stage.getScene().setRoot(fxml);*/
				
				try {
					FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/Interfaz_Usuario_def.fxml"));
					
					Controlador_pantalla_usuario_def control = new Controlador_pantalla_usuario_def();
					
					loader.setController(control);

					Parent root = loader.load();
					Stage primaryStage = new Stage();
					primaryStage.setScene(new Scene(root));
					primaryStage.show();
					
					Stage ventatnaActual = (Stage) email_login.getScene().getWindow();
					ventatnaActual.hide();
					
				} catch(Exception e) {
					e.printStackTrace();
				}
				
			} else {
				// Si la autenticación falla, muestra un mensaje de error
				mostrarMensaje("Error de inicio de sesión", "Credenciales incorrectas. Por favor, inténtalo de nuevo.",
						Alert.AlertType.ERROR);
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
