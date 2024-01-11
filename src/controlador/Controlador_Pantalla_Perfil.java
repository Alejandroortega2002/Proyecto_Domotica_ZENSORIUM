package controlador;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class Controlador_Pantalla_Perfil {

	@FXML
	private Button btnRelaciones;

	@FXML
	private void initialize() {
		// Puedes realizar inicializaciones aquí si es necesario
	}

	@FXML
	private void irRelaciones(MouseEvent event) {

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/Relacion_FamiBasi.fxml"));

			PersonaController control = new PersonaController();

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
	}

	@FXML
	private void irInicio(MouseEvent event) throws IOException {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/Interfaz_Usuario_def.fxml"));

			Controlador_pantalla_usuario_def control = new Controlador_pantalla_usuario_def();

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
