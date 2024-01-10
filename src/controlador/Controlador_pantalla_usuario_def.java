package controlador;

import java.io.IOException;

import applications.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Controlador_pantalla_usuario_def {
	@FXML
	private Rectangle cuadrado_pasillo;

	@FXML
	private Button on_pasillo;

	@FXML
	private Button off_pasillo;

	@FXML
	private Button btn_Perfil_Menu;

	private boolean estadoPasillo = false; // Inicialmente apagado

	@FXML
	void initialize() {
		System.out.println("Controlador inicializado");
		actualizarInterfaz();
	}

	private void actualizarInterfaz() {
		Color color = estadoPasillo ? Color.GREEN : Color.RED;
		// cuadrado_pasillo.setFill(color);
	}

	@FXML
	void onBotonOnPasilloClicked() {
		estadoPasillo = true;
		actualizarInterfaz();
		System.out.println("hola");
	}

	@FXML
	private void onBotonOffPasilloClicked() {
		System.out.println("off");

		estadoPasillo = false;
		actualizarInterfaz();
	}

	@FXML
	private void cambiarPantallaPerfil(MouseEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/Interfaz_Perfil.fxml"));

			Controlador_Pantalla_Perfil control = new Controlador_Pantalla_Perfil();

			loader.setController(control);

			Parent root = loader.load();
			Stage primaryStage = new Stage();
			primaryStage.setScene(new Scene(root));
			primaryStage.show();

			Stage ventatnaActual = (Stage) btn_Perfil_Menu.getScene().getWindow();
			ventatnaActual.hide();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
