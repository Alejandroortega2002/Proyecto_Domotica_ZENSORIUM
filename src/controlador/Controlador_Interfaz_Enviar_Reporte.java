package controlador;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import entidades.Nodo;
import entidades.Relaciones;
import entidades.Reporte;
import entidades.Usuario;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
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
	private Label lblDestinatario;

	@FXML
	private Button btnEnviarReporte;

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
			indicarDestinatario();
		}

	}

	private void indicarDestinatario() {
		Usuario usuarioActual = Sesion.getInstancia().getUsuarioActual();
		long idUsuarioLogueado = usuarioActual.getId_user();

		ListaEnlazada<Relaciones> relaciones = RegistroManager.cargarRelaciones();
		long idDestinatario = buscarIdDestinatario(relaciones, idUsuarioLogueado);

		if (idDestinatario == -1) {
			lblDestinatario.setText("TITULO DEL REPORTE, DESTINATARIO: ");

		} else {
			Usuario usuarioDestinatario = RegistroManager.buscarUsuarioPorId(idDestinatario);
			lblDestinatario.setText("TITULO DEL REPORTE, DESTINATARIO: " + usuarioDestinatario.getUsername() + " ("
					+ usuarioDestinatario.getTipodecuenta() + ")");

		}

	}

	@FXML
	private void btnEnviarReporte(MouseEvent event) throws IOException {
		Usuario usuarioActual = Sesion.getInstancia().getUsuarioActual();
		long idUsuarioLogueado = usuarioActual.getId_user();

		ListaEnlazada<Relaciones> relaciones = RegistroManager.cargarRelaciones();
		long idDestinatario = buscarIdDestinatario(relaciones, idUsuarioLogueado);

		if (!lblTituloReporte.getText().equals("") || !txtAreaDescripcionReporte.getText().equals("")) {
			if (idDestinatario == -1) {
				Alert alerta = new Alert(Alert.AlertType.WARNING);
				alerta.setTitle("Relación no encontrada");
				alerta.setHeaderText(null);
				alerta.setContentText("No estás relacionado con ningún otro usuario.");
				alerta.showAndWait();
				return; // Sale del método si no hay relación
			}

			Calendar calendario = Calendar.getInstance();
			Date fechaActualConCalendar = calendario.getTime();
			Reporte nuevoReporte = new Reporte(usuarioActual.getId_user(), idDestinatario, lblTituloReporte.getText(),
					txtAreaDescripcionReporte.getText(), fechaActualConCalendar, ReporteManager.obtenerNuevoId());
			boolean reporteCreado = ReporteManager.crearReporte(nuevoReporte);

			if (reporteCreado) {

				Alert alerta = new Alert(Alert.AlertType.INFORMATION);
				alerta.setTitle("Reporte creado");
				alerta.setHeaderText(null);
				alerta.setContentText("Se ha creado correctamente el reporte.");
				alerta.showAndWait();
			} else {
				Alert alerta = new Alert(Alert.AlertType.WARNING);
				alerta.setTitle("Relación no encontrada");
				alerta.setHeaderText(null);
				alerta.setContentText("No estás relacionado con ningún otro usuario.");
				alerta.showAndWait();
			}
			lblTituloReporte.setText("");
			txtAreaDescripcionReporte.setText("");
		} else {
			Alert alerta = new Alert(Alert.AlertType.INFORMATION);
			alerta.setTitle("Reporte creado");
			alerta.setHeaderText(null);
			alerta.setContentText("Rellene todos los campos");
			alerta.showAndWait();
		}

	}

	private long buscarIdDestinatario(ListaEnlazada<Relaciones> relaciones, long idUsuarioLogueado) {
		Nodo<Relaciones> nodoActual = relaciones.getCabeza();
		while (nodoActual != null) {
			Relaciones relacion = nodoActual.getDato();
			if (relacion.getId_user_relacion() == idUsuarioLogueado) {
				return relacion.getTu_id();
			}
			nodoActual = nodoActual.getEnlace();
		}
		return -1; // Retorna -1 o algún otro valor que indique que no se encontró una relación
					// válida
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

			Controlador_InterfazDispositivos control = new Controlador_InterfazDispositivos();

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
}
