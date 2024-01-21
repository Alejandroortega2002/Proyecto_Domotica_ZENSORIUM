package controlador;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import applications.Main;
import entidades.Dispositivos;
import entidades.Nodo;
import entidades.Relaciones;
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
	private TextField lblTituloReporte; // Para ingresar el nombre del usuario familiar

	@FXML
	private TextArea txtAreaDescripcionReporte; // Para ingresar el nombre del usuario familiar

	@FXML
	private Label lblDestinatario;

	/**
	 * MÈtodo para inicializar componentes de la interfaz con informaciÛn del
	 * usuario actual.
	 */
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

	/**
	 * EnvÌa un reporte, creando un nuevo objeto de reporte y gestion·ndolo a travÈs
	 * del ReporteManager.
	 * 
	 * @param event Evento del ratÛn que activa el mÈtodo.
	 * @throws IOException Si ocurre un error durante el proceso.
	 */
	@FXML
	private void btnEnviarReporte(MouseEvent event) throws IOException {
		Usuario usuarioActual = Sesion.getInstancia().getUsuarioActual();
		long idUsuarioLogueado = usuarioActual.getId_user();

		ListaEnlazada<Relaciones> relaciones = RegistroManager.cargarRelaciones();
		long idDestinatario = buscarIdDestinatario(relaciones, idUsuarioLogueado);

		if (!lblTituloReporte.getText().equals("") || !txtAreaDescripcionReporte.getText().equals("")) {
			if (idDestinatario == -1) {
				Alert alerta = new Alert(Alert.AlertType.WARNING);
				alerta.setTitle("Relaci√≥n no encontrada");
				alerta.setHeaderText(null);
				alerta.setContentText("No est√°s relacionado con ning√∫n otro usuario.");
				alerta.showAndWait();
				return; // Sale del m√©todo si no hay relaci√≥n
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
				alerta.setTitle("Relaci√≥n no encontrada");
				alerta.setHeaderText(null);
				alerta.setContentText("No est√°s relacionado con ning√∫n otro usuario.");
				alerta.showAndWait();
			}
			lblTituloReporte.setText("");
			txtAreaDescripcionReporte.setText("");
		}else {
			Alert alerta = new Alert(Alert.AlertType.INFORMATION);
			alerta.setTitle("Reporte creado");
			alerta.setHeaderText(null);
			alerta.setContentText("Rellene todos los campos");
			alerta.showAndWait();
		}
		
	}

	/**
	 * Busca el ID del destinatario de un reporte en una lista de relaciones.
	 * 
	 * @param relaciones        Lista de relaciones de usuarios.
	 * @param idUsuarioLogueado ID del usuario que est· logueado y quiere enviar un
	 *                          reporte.
	 * @return El ID del destinatario del reporte.
	 */
	private long buscarIdDestinatario(ListaEnlazada<Relaciones> relaciones, long idUsuarioLogueado) {
		Nodo<Relaciones> nodoActual = relaciones.getCabeza();
		while (nodoActual != null) {
			Relaciones relacion = nodoActual.getDato();
			if (relacion.getId_user_relacion() == idUsuarioLogueado) {
				return relacion.getTu_id();
			}
			nodoActual = nodoActual.getEnlace();
		}
		return -1; // Retorna -1 o alg√∫n otro valor que indique que no se encontr√≥ una relaci√≥n
					// v√°lida
	}
/////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Navega al men˙ del perfil del usuario.
	 * 
	 * @param event Evento del ratÛn que activa el mÈtodo.
	 * @throws IOException Si ocurre un error al cargar la interfaz de perfil.
	 */
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

	/**
	 * Regresa a la pantalla de inicio.
	 * 
	 * @param event Evento del ratÛn que activa el mÈtodo.
	 * @throws IOException Si ocurre un error al cargar la interfaz de inicio.
	 */
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
