package controlador;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

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
import modelo.Sesion;
import modelo.ListaEnlazada;

public class Controlador_Inicio_Login {

	@FXML
	private Pane contenidoArea;

	@FXML
	private JFXTextField email_login;

	@FXML
	private JFXPasswordField password_login;

	/**
	 * M閠odo para inicializar componentes si es necesario.
	 */
	@FXML
	private void initialize() {
		// Puedes realizar inicializaciones aqu铆 si es necesario
	}

	/**
	 * Cierra la aplicaci髇.
	 * 
	 * @param event Evento del rat髇 que activa el m閠odo.
	 */
	@FXML
	private void cerrarApp(MouseEvent event) {
		// Cerrar la aplicaci贸n correctamente utilizando un Stage
		Stage stage = (Stage) contenidoArea.getScene().getWindow();
		stage.close();
	}

	/**
	 * Cambia a la vista de registro.
	 * 
	 * @param event Evento del rat髇 que activa el m閠odo.
	 * @throws IOException Si ocurre un error al cargar la vista de registro.
	 */
	@FXML
	private void IrRegistrate(MouseEvent event) throws IOException {
		// Cambiar a la vista de registro
		Parent fxml = FXMLLoader.load(getClass().getResource("/vista/Pagina_Registro.fxml"));
		contenidoArea.getChildren().setAll(fxml);
	}

	/**
	 * Autentica a un usuario bas醤dose en su email y contrase馻.
	 * 
	 * @param email    El email del usuario.
	 * @param password La contrase馻 del usuario.
	 * @return Verdadero si las credenciales son correctas, falso de lo contrario.
	 */
	private boolean autenticarUsuario(String email, String password) {
		// Cargar la lista de usuarios desde el archivo JSON
		ListaEnlazada<Usuario> usuarios = RegistroManager.cargarUsuarios();

		// Verificar si las credenciales coinciden con alg煤n usuario registrado
		Nodo<Usuario> nodoActual = usuarios.getCabeza();
		while (nodoActual != null) {
			Usuario usuario = nodoActual.getDato();
			if (usuario.getEmail().equals(email) && usuario.getPassword().equals(password)) {
				Sesion.getInstancia().setUsuarioActual(usuario);
				return true; // Credenciales v谩lidas
			}
			nodoActual = nodoActual.getEnlace();
		}

		return false; // Credenciales incorrectas
	}

	/**
	 * Maneja el proceso de inicio de sesi髇.
	 * 
	 * @param event Evento del rat髇 que activa el m閠odo.
	 * @throws IOException Si ocurre un error durante el proceso.
	 */
	@FXML
	private void iniciarSesion(MouseEvent event) throws IOException {
		if (email_login != null && password_login != null) {
			String email = email_login.getText();
			String password = password_login.getText();

			boolean autenticado = autenticarUsuario(email, password);

			if (autenticado) {
				mostrarMensaje("Inicio de sesi贸n exitoso", "隆Bienvenido, " + email + "!");

				try {
					FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/Interfaz_Dispositivos.fxml"));

					Controlador_InterfazDispositivos control = new Controlador_InterfazDispositivos();

					loader.setController(control);

					Parent root = loader.load();
					Stage primaryStage = new Stage();
					primaryStage.setScene(new Scene(root));
					primaryStage.show();

					Stage ventatnaActual = (Stage) email_login.getScene().getWindow();
					ventatnaActual.hide();

				} catch (Exception e) {
					e.printStackTrace();
				}

			} else {
				// Si la autenticaci贸n falla, muestra un mensaje de error
				mostrarMensaje("Error de inicio de sesi贸n",
						"Credenciales incorrectas. Por favor, int茅ntalo de nuevo.", Alert.AlertType.ERROR);
			}
		} else {
			System.out.println("email_login o password_login es null");
		}
	}

	/**
	 * Muestra un mensaje en la interfaz de usuario.
	 * 
	 * @param titulo    T韙ulo del mensaje.
	 * @param contenido Contenido del mensaje.
	 */
	private void mostrarMensaje(String titulo, String contenido) {
		mostrarMensaje(titulo, contenido, Alert.AlertType.INFORMATION);
	}

	/**
	 * Muestra un mensaje en la interfaz de usuario con un tipo de alerta
	 * espec韋ico.
	 * 
	 * @param titulo    T韙ulo del mensaje.
	 * @param contenido Contenido del mensaje.
	 * @param tipo      Tipo de alerta (p.ej., informaci髇, error).
	 */
	private void mostrarMensaje(String titulo, String contenido, Alert.AlertType tipo) {
		Alert alert = new Alert(tipo);
		alert.setTitle(titulo);
		alert.setHeaderText(null);
		alert.setContentText(contenido);
		alert.showAndWait();
	}
}
