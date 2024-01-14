package controlador;


import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import applications.Main;
import entidades.Dispositivos;
import entidades.Nodo;
import entidades.Reporte;
import entidades.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import modelo.DispositivosManager;
import modelo.ListaEnlazada;
import modelo.RegistroManager;
import modelo.ReporteManager;
import modelo.Sesion;

public class Controlador_Interfaz_Enviar_Reporte {

	@FXML
	private Label lblNombreUsu;

	@FXML
	private Label lblTipoCuenta;

	@FXML
	private Button btnEnviarReporte;

	@FXML
	private TextField lblNombreDesti; // Para ingresar el nombre del usuario familiar
	@FXML
	private TextField lblTituloReporte; // Para ingresar el nombre del usuario familiar

	@FXML
	private TextArea txtAreaDescripcionReporte; // Para ingresar el nombre del usuario familiar


	@FXML
	public void initialize() {

		Usuario usuarioActual = Sesion.getInstancia().getUsuarioActual();
		if (usuarioActual != null) {
			String username = usuarioActual.getUsername();
			String tipoDeCuenta = usuarioActual.getTipodecuenta();
			lblNombreUsu.setText(username);
			lblTipoCuenta.setText(tipoDeCuenta);
		}

		
	}

//	this.id_user_emisor = id_user_emisor;
//	this.id_user_receptor = id_user_receptor;
//	this.titulo = titulo;
//	this.queja = queja;
//	this.fecha = fecha;
//	this.id_reporte = id_reporte;

	@FXML
	private void btnEnviarReporte(MouseEvent event) throws IOException {
		Usuario usuarioActual = Sesion.getInstancia().getUsuarioActual();
		Calendar calendario = Calendar.getInstance();
        Date fechaActualConCalendar = calendario.getTime();
		Reporte nuevoReporte = new Reporte(usuarioActual.getId_user(),0,lblTituloReporte.getText(),txtAreaDescripcionReporte.getText(),fechaActualConCalendar,ReporteManager.obtenerNuevoId());
		ReporteManager.crearReporte(nuevoReporte);

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

			Stage ventanaActual = (Stage) lblNombreUsu.getScene().getWindow();
			ventanaActual.hide();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@FXML
	private void irInicio(MouseEvent event) throws IOException {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/Interfaz_Dispositivos.fxml"));

			Controlador_Interfaz_Enviar_Reporte control = new Controlador_Interfaz_Enviar_Reporte();

			loader.setController(control);

			Parent root = loader.load();
			Stage primaryStage = new Stage();
			primaryStage.setScene(new Scene(root));
			primaryStage.show();

			Stage ventatnaActual = (Stage) lblNombreUsu.getScene().getWindow();
			ventatnaActual.hide();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@FXML
	private void irAdministrar(MouseEvent event) throws IOException {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/Interfaz_Administrar_Dispositivos.fxml"));

			Controlador_Interfaz_Administrar_Dispositivos control = new Controlador_Interfaz_Administrar_Dispositivos();

			loader.setController(control);

			Parent root = loader.load();
			Stage primaryStage = new Stage();
			primaryStage.setScene(new Scene(root));
			primaryStage.show();

			Stage ventatnaActual = (Stage) lblNombreUsu.getScene().getWindow();
			ventatnaActual.hide();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@FXML
	private void IrVerDato(MouseEvent event) throws IOException {
		try {
			if (DispositivosManager.getDispositivoSeleccionado() != null) {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/Interfaz_Visualizacion_Datos.fxml"));

				Controlador_Ver_Datos control = new Controlador_Ver_Datos();

				loader.setController(control);

				Parent root = loader.load();
				Stage primaryStage = new Stage();
				primaryStage.setScene(new Scene(root));
				primaryStage.show();

				Stage ventatnaActual = (Stage) lblNombreUsu.getScene().getWindow();
				ventatnaActual.hide();

			} else {
				// Mostrar mensaje de error o no hacer nada
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Acceso Restringido");
				alert.setHeaderText(null);
				alert.setContentText("No tienes ningun dispositivo seleccionado");
				alert.showAndWait();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
