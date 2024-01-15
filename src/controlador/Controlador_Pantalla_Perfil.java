package controlador;

import java.io.IOException;

import entidades.Usuario;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import modelo.Sesion;

public class Controlador_Pantalla_Perfil {

	@FXML
	private Button btnRelaciones;

	@FXML
	private Label LblUsername;
	@FXML
	private Label LblTipoCuenta;

	@FXML
	private void initialize() {
		// Puedes realizar inicializaciones aquí si es necesario
		Usuario usuarioActual = Sesion.getInstancia().getUsuarioActual();
		if (usuarioActual != null) {
			String username = usuarioActual.getUsername();
			String tipoDeCuenta = usuarioActual.getTipodecuenta();
			LblUsername.setText(username);
			LblTipoCuenta.setText(tipoDeCuenta);
		}
	}
	
	@FXML
	private void cerrarSesion(MouseEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/Pantalla_Inicio_Login.fxml"));

			Controlador_Inicio_Login control = new Controlador_Inicio_Login();

			loader.setController(control);

			Parent root = loader.load();
			Stage primaryStage = new Stage();
			primaryStage.setScene(new Scene(root));
			primaryStage.show();
			Stage ventanaActual = (Stage) LblUsername.getScene().getWindow();
			ventanaActual.hide();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void irRelaciones(MouseEvent event) {

		Usuario usuarioActual = Sesion.getInstancia().getUsuarioActual();
		if (usuarioActual != null) {
			String tipoDeCuenta = usuarioActual.getTipodecuenta();

			switch (tipoDeCuenta) {
			case "Familiar":
				try {
					FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/Relacion_FamiBasi.fxml"));

					RelacionesFB control = new RelacionesFB();

					loader.setController(control);

					Parent root = loader.load();
					Stage primaryStage = new Stage();
					primaryStage.setScene(new Scene(root));
					primaryStage.show();

					Stage ventanaActual = (Stage) btnRelaciones.getScene().getWindow();
					ventanaActual.hide();
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;

			case "Administrador":
				try {
					FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/Relacion_AdminFami.fxml"));

					RelacionesAF control = new RelacionesAF();

					loader.setController(control);

					Parent root = loader.load();
					Stage primaryStage = new Stage();
					primaryStage.setScene(new Scene(root));
					primaryStage.show();

					Stage ventanaActual = (Stage) btnRelaciones.getScene().getWindow();
					ventanaActual.hide();
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;

			case "Usuario":
				// Mostrar mensaje de error o no hacer nada
				Alert alert = new Alert(Alert.AlertType.WARNING);
				alert.setTitle("Acceso Restringido");
				alert.setHeaderText(null);
				alert.setContentText("No tienes acceso a esta función.");
				alert.showAndWait();
				return; // Salir del método sin hacer nada más

			default:
				// Manejar cualquier otro caso
				return;
			}
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

			Stage ventatnaActual = (Stage) btnRelaciones.getScene().getWindow();
			ventatnaActual.hide();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@FXML
	private void irEnviarReporte(MouseEvent event) throws IOException {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/Interfaz_Enviar_Reporte.fxml"));

			Controlador_Interfaz_Enviar_Reporte control = new Controlador_Interfaz_Enviar_Reporte();

			loader.setController(control);

			Parent root = loader.load();
			Stage primaryStage = new Stage();
			primaryStage.setScene(new Scene(root));
			primaryStage.show();

			Stage ventatnaActual = (Stage) btnRelaciones.getScene().getWindow();
			ventatnaActual.hide();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@FXML
	private void irLeerReporte(MouseEvent event) throws IOException {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/Interfaz_Leer_Reporte.fxml"));

			Controlador_Interfaz_Leer_Peticion control = new Controlador_Interfaz_Leer_Peticion();

			loader.setController(control);

			Parent root = loader.load();
			Stage primaryStage = new Stage();
			primaryStage.setScene(new Scene(root));
			primaryStage.show();

			Stage ventatnaActual = (Stage) btnRelaciones.getScene().getWindow();
			ventatnaActual.hide();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
