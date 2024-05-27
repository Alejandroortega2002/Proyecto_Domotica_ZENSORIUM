package controlador;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSet;

import entidades.Dispositivos;
import entidades.Nodo;
import entidades.Sensores;
import entidades.Usuario;
import javafx.animation.Timeline;
import javafx.application.Platform;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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
	private TableView<Sensores> tblDatosSensores; // La tabla de datos históricos
	@FXML
	private TableColumn<Sensores, Long> colNRegistro;
	@FXML
	private TableColumn<Sensores, String> colDato; // Usamos String temporalmente
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
	private Label lblMedia;
	@FXML
	private CategoryAxis xAxis;
	@FXML
	private NumberAxis yAxis;

	private Timeline timeline;

	@FXML
	void initialize() {
		Usuario usuarioActual = Sesion.getInstancia().getUsuarioActual();
		if (usuarioActual != null) {
			String username = usuarioActual.getUsername();
			String tipoDeCuenta = usuarioActual.getTipodecuenta();
			lblNombreUsu.setText(username);
			lblTipoCuenta.setText(tipoDeCuenta);
		}

		Dispositivos dispositivo = DispositivosManager.getDispositivoSeleccionado();
		this.colNRegistro.setCellValueFactory(new PropertyValueFactory<>("id_sensor")); // Temporalmente usamos
																						// id_sensor
		this.colDato.setCellValueFactory(new PropertyValueFactory<>("descripcion")); // Temporalmente usamos descripcion
		cargarSensoresDelDispositivo(dispositivo);
		cargarDatosEnGrafico(dispositivo.getId_sensor());
		cargarDatosEnTabla(dispositivo.getId_sensor());
		cargarDatosMedia(dispositivo.getId_sensor());
		estilizarGrafico(dispositivo);

		/**
		 * // Configurar el Timeline para actualizar cada 3 segundos timeline = new
		 * Timeline(new KeyFrame(Duration.seconds(3), event -> {
		 * actualizarDatos(dispositivo.getId_sensor()); }));
		 * timeline.setCycleCount(Timeline.INDEFINITE); timeline.play();
		 */
	}

	public static double calcularMediaDivideYVenceras(List<Double> vector) {
		if (vector.isEmpty()) {
			return 0;
		}

		double sumaTotal = sumaDivideYVenceras(vector, 0, vector.size());
		return sumaTotal / vector.size();
	}

	private static double sumaDivideYVenceras(List<Double> vector, int inicio, int fin) {
		if (inicio == fin - 1) {
			return vector.get(inicio);
		} else {
			int mid = (inicio + fin) / 2;
			double leftSum = sumaDivideYVenceras(vector, inicio, mid);
			double rightSum = sumaDivideYVenceras(vector, mid, fin);
			return leftSum + rightSum;
		}
	}

	private void cargarDatosMedia(long SensorID) {
		Double a = calcularMediaDivideYVenceras(DispositivosManager.cargarDatosMedia(SensorID));
		DecimalFormat formato = new DecimalFormat("#.00");
		String media = formato.format(a);

		lblMedia.setText(media);

		// ponerlo a un textpanel

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

	private void cargarDatosEnGrafico(long sensorId) {
		ObservableList<XYChart.Data<String, Number>> datos = DispositivosManager.cargarDatosEnGrafico(sensorId);

		barChart.getData().clear(); // Limpia los datos antiguos del gráfico
		XYChart.Series<String, Number> series = new XYChart.Series<>();
		series.setName("Recopilación"); // Nombre de la serie

		series.getData().addAll(datos);

		// Añade la serie al gráfico después de haber agregado todos los datos
		barChart.getData().add(series);

		// Estilizar las barras después de que se han añadido al gráfico
		barChart.lookupAll(".data0").forEach(bar -> bar.setStyle("-fx-bar-fill: navy;")); // Ajusta el color como
																							// desees

		// Ajustar el ancho de las barras
		barChart.setCategoryGap(10);
		barChart.setBarGap(3);

		// Configurar el rango del eje Y
		yAxis.setAutoRanging(false);
		yAxis.setLowerBound(0);
		yAxis.setUpperBound(100);
		yAxis.setTickUnit(10);
	}

	private void cargarDatosEnTabla(long sensorId) {
		ObservableList<Sensores> historicoDatos = DispositivosManager.cargarDatosEnTabla(sensorId);

		if (historicoDatos.isEmpty()) { // Verifica si el ResultSet está vacío
			mostrarMensajeDesconexion(sensorId);
			return;
		}

		// Establecer los datos en la tabla
		tblDatosSensores.setItems(historicoDatos);
	}

	private void mostrarMensajeDesconexion(long sensorId) {
		Sensores sensor = DispositivosManager.buscarSensorPorId(sensorId);
		if (sensor != null) {
			Platform.runLater(() -> {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Sensor Desconectado");
				alert.setHeaderText(null);
				alert.setContentText("Sensor desconectado: hay que conectar el sensor de tipo: " + sensor.getTipo()
						+ " y ID: " + sensor.getId_sensor() + " para que tome los datos.");
				alert.showAndWait();
			});
		} else {
			Platform.runLater(() -> {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Sensor No Encontrado");
				alert.setHeaderText(null);
				alert.setContentText("El sensor con ID: " + sensorId + " no fue encontrado en la base de datos.");
				alert.showAndWait();
			});
		}
	}

	/**
	 * private void actualizarDatos(long sensorId) { cargarDatosEnGrafico(sensorId);
	 * cargarDatosEnTabla(sensorId); }
	 */
	private void estilizarGrafico(Dispositivos dispositivo) {
		switch (dispositivo.getTipo()) {
		case "Aire":
			yAxis.setLabel("Temperatura (ºC)");
			break;
		case "Humidificador":
			yAxis.setLabel("Humedad (%)");
			break;
		case "Luz":
			yAxis.setLabel("Luminosidad (%)");
			break;
		case "Persiana":
			yAxis.setLabel("Distancia (cm)");
			break;
		default:
			yAxis.setLabel("Valor");
			break;
		}
		xAxis.setLabel("Datos de Sensores");
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
