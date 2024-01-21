package controlador;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import applications.Main;
import entidades.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import modelo.RegistroManager;

public class Controlador_Registro {

    // Definición de variables de la interfaz de usuario y controlador
    ObservableList<String> listaTipoDeCuenta = FXCollections.observableArrayList("Usuario", "Familiar", "Administrador");

    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
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
    private ComboBox<String> tipoDeCuenta;
    @FXML
    private Label Error_Label_Registro;

    /**
     * Método de inicialización de JavaFX.
     */
    @FXML
    void initialize() {
        // Initialize the controller
		// You can add additional initialization logic here
		tipoDeCuenta.setItems(listaTipoDeCuenta);
    }

    /**
     * Registra un nuevo usuario en el sistema.
     * @throws IOException Si ocurre un error al cargar el recurso FXML.
     */
    private void registrarUsuario() throws IOException {
        String username = nombreUsuarioRegistro.getText();
		String email = emailUsuarioRegistro.getText();
		String password = contrasenaUsuarioRegistro.getText();
		String repetirPass = repetirContrasenaUsuarioRegistro.getText();
		String tipoDecuenta = tipoDeCuenta.getValue();

		if (!email.isEmpty() && !password.isEmpty() && !username.isEmpty() && !repetirPass.isEmpty()
				&& tipoDecuenta != null && !tipoDecuenta.isEmpty()) {

			if (validarEmail(email)) {

				if (contrasenaSegura(password) == true) {

					if (password.equals(repetirPass)) {
						long nuevoId = RegistroManager.obtenerNuevoId();
						Usuario nuevoUsuario = new Usuario(nuevoId, username, email, password, repetirPass,
								tipoDecuenta);
						if (RegistroManager.registrarUsuario(nuevoUsuario)) {
							try {
								FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/Pantalla_Inicio_Login.fxml"));

								Controlador_Inicio_Login control = new Controlador_Inicio_Login();

								loader.setController(control);

								Parent root = loader.load();
								Stage primaryStage = new Stage();
								primaryStage.setScene(new Scene(root));
								primaryStage.show();

								Stage ventatnaActual = (Stage) nombreUsuarioRegistro.getScene().getWindow();
								ventatnaActual.hide();

							} catch (Exception e) {
								e.printStackTrace();
							}

						} else {
							// Mostrar mensaje de error si el usuario ya existe.
							Error_Label_Registro.setVisible(true);
							Error_Label_Registro
									.setText("El nombre de usuario o el email que intentas introducir ya existe");
						}

					} else {
						Error_Label_Registro.setVisible(true);
						Error_Label_Registro.setText("La contraseñas no coinciden.");
					}

				} else {
					Error_Label_Registro.setVisible(true);
					Error_Label_Registro.setText("La contraseña no es segura.");
				}

			} else {
				Error_Label_Registro.setVisible(true);
				Error_Label_Registro.setText("El email no es correcto.");
			}

		} else {
			// Mostrar mensaje de error si los campos están vacíos.
			Error_Label_Registro.setVisible(true);
			Error_Label_Registro.setText("Hay campos vacíos o no se seleccionó un tipo de cuenta.");
		}
    }

    /**
     * Maneja el evento de clic para registrar un nuevo usuario.
     * @param event Información del evento de clic del mouse.
     * @throws IOException Si ocurre un error al procesar la acción.
     */
    @FXML
    public void btnRegistrarte(MouseEvent event) throws IOException {
		registrarUsuario();
    }

    /**
     * Cierra la aplicación.
     * @param event Información del evento de clic del mouse.
     */
    @FXML
    public void cerrarApp(MouseEvent event) {
        // Implementa la lógica para cerrar la aplicación
		System.exit(0);
    }

<<<<<<< HEAD
    /**
     * Vuelve a la pantalla de inicio de sesión.
     * @param event Información del evento de clic del mouse.
     * @throws IOException Si ocurre un error al cargar el recurso FXML.
     */
    @FXML
    public void volverALogin(MouseEvent event) throws IOException {
        try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/Pantalla_Inicio_Login.fxml"));

			Controlador_Inicio_Login control = new Controlador_Inicio_Login();

			loader.setController(control);

			Parent root = loader.load();
			Stage primaryStage = new Stage();
			primaryStage.setScene(new Scene(root));
			primaryStage.show();

			Stage ventatnaActual = (Stage) nombreUsuarioRegistro.getScene().getWindow();
			ventatnaActual.hide();

		} catch (Exception e) {
			e.printStackTrace();
		}
    }
=======
	@FXML
	public void volverALogin(MouseEvent event) throws IOException {
		Parent fxml = FXMLLoader.load(getClass().getResource("/vista/Pantalla_Inicio_Login.fxml"));
		Main.stage.getScene().setRoot(fxml);
	}
>>>>>>> parent of ac0d6a4 (Ultimos retoques antes de comentar)

    /**
     * Valida si un email es válido según una expresión regular.
     * @param email El email a validar.
     * @return true si el email es válido, false en caso contrario.
     */
    public boolean validarEmail(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
		return email.matches(regex);
    }

    /**
     * Verifica si una contraseña cumple con los criterios de seguridad establecidos.
     * @param contrasena La contraseña a verificar.
     * @return true si la contraseña es segura, false en caso contrario.
     */
    public boolean contrasenaSegura(String contrasena) {
        
		if (contrasena.length() > 8) { // una contraseña tiene más de 12 caracteres.
			boolean mayuscula = false;
			boolean numero = false;
			boolean letraOsimbolo = false;
			boolean especial = false;

			// Define caracteres especiales
			Pattern special = Pattern.compile("[?!¡@¿.,´)]");
			Matcher hasSpecial = special.matcher(contrasena);

			int i;
			char l;

			for (i = 0; i < contrasena.length(); i++) {
				l = contrasena.charAt(i);

				if (Character.isDigit(l)) {// mínimo un número.
					numero = true;
				}
				if (Character.isLetter(l)) {// contiene letras o símbolos (?!¡@¿.,´)
					letraOsimbolo = true;
				}
				if (Character.isUpperCase(l)) { // mínimo una letra mayúscula
					mayuscula = true;
				}
				if (hasSpecial.find()) { // Valida "caracteres especiales".
					especial = true;
				}
			}

			if (numero == true && mayuscula == true && letraOsimbolo == true && especial == true) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
    }
}
