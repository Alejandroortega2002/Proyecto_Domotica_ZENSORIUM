package controlador;

import java.io.IOException;
import java.util.Set;

import com.jfoenix.controls.JFXPasswordField;

import entidades.Usuario;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import modelo.DispositivosManager;
import modelo.RegistroManager;
import modelo.Sesion;

public class Controlador_Interfaz_Editar_Perfil {

	@FXML
	private Label lblTipoCuenta;

	@FXML
	private Button btnGuardarCambios;
	@FXML
	private Button btnNuevaPass;

	@FXML
	private TextField lblNombreUsu;
	@FXML
	private TextField lblCorreoUsu;

	@FXML
	private JFXPasswordField passNueva;
	@FXML
	private JFXPasswordField passRepetirNuevaPass;

	@FXML
	public void initialize() {

		Usuario usuarioActual = Sesion.getInstancia().getUsuarioActual();
		if (usuarioActual != null) {
			String username = usuarioActual.getUsername();
			lblNombreUsu.setText(username);
			lblTipoCuenta.setText(usuarioActual.getTipodecuenta());
			lblCorreoUsu.setText(usuarioActual.getEmail());

		}

	}

	@FXML
	private void btnAplicarCambios() throws IOException {

		String nuevoNombre = lblNombreUsu.getText();
		String nuevoCorreo = lblCorreoUsu.getText();
		Usuario usuarioActual = Sesion.getInstancia().getUsuarioActual();

		if (!nuevoNombre.isEmpty() || !nuevoCorreo.isEmpty()) {

			if (RegistroManager.modificarUsuario(usuarioActual.getId_user(), nuevoNombre, nuevoCorreo)) {
				Alert alerta = new Alert(Alert.AlertType.WARNING);
				alerta.setTitle("Modificacion Exitosa");
				alerta.setHeaderText(null);
				alerta.setContentText("Nuevo nombre de usuario:" + nuevoNombre
						+ "                             Nuevo correo de usuario:" + nuevoCorreo);
				alerta.showAndWait();
				usuarioActual.setUsername(nuevoNombre);
				lblNombreUsu.setText("");
				lblCorreoUsu.setText("");
			} else {
				Alert alerta = new Alert(Alert.AlertType.WARNING);
				alerta.setTitle("Modificacion no exitosa");
				alerta.setHeaderText(null);
				alerta.setContentText("No se han realizado correctamente los cambios");
				alerta.showAndWait();
			}
		} else {
			Alert alerta = new Alert(Alert.AlertType.WARNING);
			alerta.setTitle("Modificacion fallida");
			alerta.setHeaderText(null);
			alerta.setContentText("Rellene todos los campos porfavor");
			alerta.showAndWait();
		}
	}

/////////////////////////////////////////////////////////////////////////////////////////////////////////

	@FXML
	private void irMenuPerfil(MouseEvent event) throws IOException {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/Interfaz_Perfil.fxml"));

			Controlador_Pantalla_Perfil control = new Controlador_Pantalla_Perfil();

			loader.setController(control);

			Parent root = loader.load();
			Stage primaryStage = new Stage();
			primaryStage.setScene(new Scene(root));
			primaryStage.show();

			Stage ventanaActual = (Stage) lblTipoCuenta.getScene().getWindow();
			ventanaActual.hide();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@FXML
	private void irInicio(MouseEvent event) throws IOException {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/Interfaz_Dispositivos.fxml"));

			Controlador_InterfazDispositivos control = new Controlador_InterfazDispositivos();

			loader.setController(control);

			Parent root = loader.load();
			Stage primaryStage = new Stage();
			primaryStage.setScene(new Scene(root));
			primaryStage.show();

			Stage ventatnaActual = (Stage) lblTipoCuenta.getScene().getWindow();
			ventatnaActual.hide();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
