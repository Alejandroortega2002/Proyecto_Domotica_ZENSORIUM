package controlador;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

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
		// Puedes realizar inicializaciones aqu铆 si es necesario
	}

	@FXML
	private void cerrarApp(MouseEvent event) {
		// Cerrar la aplicaci贸n correctamente utilizando un Stage
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

		// Verificar si las credenciales coinciden con alg鷑 usuario registrado
		for (Usuario usuario : usuarios) {
			if (usuario.getEmail().equals(email) && usuario.getPassword().equals(password)) {
				return true; // Credenciales v醠idas
			}
		}

		return false; // Credenciales incorrectas
	}

	@FXML
	private void iniciarSesion(MouseEvent event) {
		if (email_login != null && password_login != null) {
			String email = email_login.getText();
			String password = password_login.getText();

			boolean autenticado = autenticarUsuario(email, password);

			if (autenticado) {
				// Si el usuario es autenticado con 茅xito, puedes navegar a la siguiente vista
				// o realizar otras acciones
				// En este ejemplo, simplemente mostramos un mensaje de 茅xito
				mostrarMensaje("Inicio de sesi贸n exitoso", "隆Bienvenido, " + email + "!");
			} else {
				// Si la autenticaci贸n falla, muestra un mensaje de error
				mostrarMensaje("Error de inicio de sesi贸n",
						"Credenciales incorrectas. Por favor, int茅ntalo de nuevo.", Alert.AlertType.ERROR);
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