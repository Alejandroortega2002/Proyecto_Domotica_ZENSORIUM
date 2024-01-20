package controlador;

import java.io.IOException;

import entidades.Dispositivos;
import entidades.Nodo;
import entidades.Sensores;
import entidades.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import modelo.DispositivosManager;
import modelo.ListaEnlazada;
import modelo.Sesion;

public class Controlador_Ver_Datos {

	@FXML
	private TableView<Sensores> tblDatosSensores; // La tabla de usuarios familiares
	@FXML
	private TableColumn<Sensores, Long> colNRegistro;
	@FXML
	private TableColumn<Sensores, Float> colDato;
	@FXML
	private TableColumn<Sensores, String> colTipo;
	@FXML
	private TableColumn<Sensores, Long> colId;
	@FXML
	private BarChart<String, Number> barChart;
	@FXML
	private Label lblNombreUsu;
	@FXML
	private Label lblTipoCuenta;
	@FXML
	private CategoryAxis xAxis;

	@FXML
	private NumberAxis yAxis;

	@FXML
	void initialize() {
		// Initialize the controller
		Usuario usuarioActual = Sesion.getInstancia().getUsuarioActual();
		if (usuarioActual != null) {
			String username = usuarioActual.getUsername();
			String tipoDeCuenta = usuarioActual.getTipodecuenta();
			lblNombreUsu.setText(username);
			lblTipoCuenta.setText(tipoDeCuenta);
		}

		Dispositivos dispositivo = DispositivosManager.getDispositivoSeleccionado();
		this.colNRegistro.setCellValueFactory(new PropertyValueFactory<>("orden_registro"));
		this.colDato.setCellValueFactory(new PropertyValueFactory<>("dato_actual"));
		this.colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
		this.colId.setCellValueFactory(new PropertyValueFactory<>("id_sensor"));
		cargarSensoresDelDispositivo(dispositivo);
		cargarDatosEnGrafico();
		estilizarGrafico(dispositivo);
	}

	private void cargarSensoresDelDispositivo(Dispositivos dispositivoSeleccionado) {
		if (dispositivoSeleccionado == null)
			return;

		ListaEnlazada<Sensores> todosLosSensores = DispositivosManager.cargarSensores();
		ObservableList<Sensores> sensoresRelacionados = FXCollections.observableArrayList();

		Nodo<Sensores> nodoActual = todosLosSensores.getCabeza();
		while (nodoActual != null) {
			Sensores sensor = nodoActual.getDato();
			if (sensor.getId_sensor() == (dispositivoSeleccionado.getId_sensor())) {
				sensoresRelacionados.add(sensor);
			}
			nodoActual = nodoActual.getEnlace();
		}

		tblDatosSensores.setItems(sensoresRelacionados);
	}

	private void cargarDatosEnGrafico() {
		try {
			barChart.getData().clear(); // Limpia los datos antiguos del gráfico
			XYChart.Series<String, Number> series = new XYChart.Series<>();
			series.setName("Recopilación"); // Nombre de la serie

			// Crear un punto de datos para cada sensor y añadirlo a la serie
			for (Sensores sensor : tblDatosSensores.getItems()) {
				XYChart.Data<String, Number> data = new XYChart.Data<>(String.valueOf(sensor.getOrden_registro()),
						sensor.getDato_actual());
				series.getData().add(data);
			}

			// Añade la serie al gráfico después de haber agregado todos los datos
			barChart.getData().add(series);

			// Estilizar las barras después de que se han añadido al gráfico
			barChart.lookupAll(".data0").forEach(bar -> bar.setStyle("-fx-bar-fill: navy;")); // Ajusta el color como
																								// desees

			// Ajustar el ancho de las barras
			barChart.setCategoryGap(10);
			barChart.setBarGap(3);

		} catch (NullPointerException e) {
			e.printStackTrace();
			// Manejo adicional del error o log
		}
	}

	private void estilizarGrafico(Dispositivos dispositivo) {

		switch (dispositivo.getTipo()) {
		case "Aire":
			yAxis.setLabel("Temperatura (°C)");
			break;
		case "Puerta":
			yAxis.setLabel("Movimiento");
			break;
		case "Luz":
			yAxis.setLabel("Luminosidad (%)");
			break;
		case "Persiana":
			yAxis.setLabel("Posicionamiento");
			break;
		default:
			yAxis.setLabel("Valor");
			break;
		}
		xAxis.setLabel("Datos de Sensores");

		// Otras personalizaciones...
	}

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

			Stage ventanaActual = (Stage) tblDatosSensores.getScene().getWindow();
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

			Stage ventatnaActual = (Stage) tblDatosSensores.getScene().getWindow();
			ventatnaActual.hide();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
