package controlador;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;

public class Controlador_pantalla_usuario_def {
	@FXML
	private Rectangle cuadrado_pasillo;

	@FXML
	private Button on_pasillo;

	@FXML
	private Button off_pasillo;

	@FXML
	private Text texto_aviso;
	
	@FXML
	private Button btn_Perfil_Menu;	

	private boolean estadoPasillo = false; // Inicialmente apagado

	@FXML
	void initialize() {
		System.out.println("Controlador inicializado");
		cargarEstadoDesdeJson();
		actualizarInterfaz();
	}

	@FXML
	void actualizarInterfaz() {
		if (cuadrado_pasillo != null) {
			Color color = estadoPasillo ? Color.GREEN : Color.RED;
			cuadrado_pasillo.setFill(color);
		} else {
			System.out.println("Error: cuadrado_pasillo es null");
		}
	}

	@FXML
	void onBotonOnPasilloClicked() {
		texto_aviso.setText("sensor activado");
		estadoPasillo = true;
		on_pasillo.setDisable(true);
		off_pasillo.setDisable(false);
		actualizarInterfaz();
		guardarEstadoEnJson();
	}

	@FXML
	void onBotonOffPasilloClicked() {
		texto_aviso.setText("sensor desactivado");
		estadoPasillo = false;
		off_pasillo.setDisable(true);
		on_pasillo.setDisable(false);
		actualizarInterfaz();
		guardarEstadoEnJson();
	}

	private void cargarEstadoDesdeJson() {
		try (BufferedReader reader = new BufferedReader(new FileReader("/ProyectoInformatica/data/datosSensores.json"))) {
			String linea;
			StringBuilder contenido = new StringBuilder();
			while ((linea = reader.readLine()) != null) {
				contenido.append(linea);
			}

			if (contenido.length() > 0) {
				String estado = contenido.toString();
				estadoPasillo = "encendido".equalsIgnoreCase(estado);
			}
		} catch (IOException e) {
			System.out.println("Error al cargar el estado desde el JSON: " + e.getMessage());
		}
	}

	private void guardarEstadoEnJson() {
		try (FileWriter writer = new FileWriter("/ProyectoInformatica/data/datosSensores.json")) {
			String estado = estadoPasillo ? "encendido" : "apagado";
			writer.write(estado);
		} catch (IOException e) {
			System.out.println("Error al guardar el estado en el JSON: " + e.getMessage());
		}
	}


	@FXML
	void cambiarPantallaPerfil(MouseEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/Interfaz_Perfil.fxml"));

			Controlador_Pantalla_Perfil control = new Controlador_Pantalla_Perfil();

			loader.setController(control);

			Parent root = loader.load();
			Stage primaryStage = new Stage();
			primaryStage.setScene(new Scene(root));
			primaryStage.show();

			Stage ventanaActual = (Stage) btn_Perfil_Menu.getScene().getWindow();
			ventanaActual.hide();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
