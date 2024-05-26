package controlador;

import java.io.IOException;

import entidades.Dispositivos;
import entidades.Nodo;
import entidades.Relaciones;
import entidades.Sensores;
import entidades.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import modelo.DispositivosManager;
import modelo.ListaEnlazada;
import modelo.RegistroManager;
import modelo.Sesion;

public class Controlador_InterfazDispositivos {

	@FXML
	private Label lblNombreUsu;

	@FXML
	private Label lblTipoCuenta;

	@FXML
	private Button btnVerDatos;
	@FXML
	private Button btnAdministarDsipo;

	@FXML
	private TextField txtFieldNombreDispo; // Para ingresar el nombre del usuario familiar
	@FXML
	private TextField txtFieldNombreSensorRelacionado; // Para ingresar el nombre del usuario familiar

	@FXML
	private TableView<Dispositivos> tablaDsipositivos;
	@FXML
	private TableColumn<Dispositivos, String> columnaNombre;
	@FXML
	private TableColumn<Dispositivos, String> columnaEstado;
	@FXML
	private TableColumn<Dispositivos, String> columnaIdSensor;

	@FXML
	public void initialize() {

		Usuario usuarioActual = Sesion.getInstancia().getUsuarioActual();
		if (usuarioActual != null) {
			String username = usuarioActual.getUsername();
			String tipoDeCuenta = usuarioActual.getTipodecuenta();
			lblNombreUsu.setText(username);
			lblTipoCuenta.setText(tipoDeCuenta);
			deshabilitarBtns(tipoDeCuenta);
		}

		this.columnaNombre.setCellValueFactory(new PropertyValueFactory<>("Nombre"));
		this.columnaEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
		this.columnaIdSensor.setCellValueFactory(new PropertyValueFactory<>("id_sensor"));
		cargarDispositivos(usuarioActual.getId_user());
	}

	private void deshabilitarBtns(String tipoUsu) {
		Usuario usuarioActual = Sesion.getInstancia().getUsuarioActual();

		if (usuarioActual != null) {
			if (usuarioActual.getTipodecuenta().equals("Usuario")) {
				btnAdministarDsipo.setDisable(true);
				btnAdministarDsipo.setManaged(false);

			}

		}
	}

	@FXML
	private void seleccionarDispositivo(MouseEvent event) {
		// Obt�n el dispo seleccionado y actualiza el txtnombredispo
		Dispositivos dispoSeleccionado = tablaDsipositivos.getSelectionModel().getSelectedItem();
		if (dispoSeleccionado != null) {
			txtFieldNombreDispo.setText(dispoSeleccionado.getNombre());
			long idSeleccionado = dispoSeleccionado.getId_sensor();
			Sensores sensorAsociado = DispositivosManager.buscarSensorPorId(idSeleccionado);
			txtFieldNombreSensorRelacionado.setText(String.valueOf(sensorAsociado.getTipo()));
			DispositivosManager.setDispositivoSeleccionado(dispoSeleccionado);

		}
	}

	private void cargarDispositivos(long idUsuarioLogueado) {
		ObservableList<Dispositivos> dispos = FXCollections.observableArrayList();
		ListaEnlazada<Long> usuariosProcesados = new ListaEnlazada<>();
		cargarDispositivosDeUsuarioYRelacionados(idUsuarioLogueado, dispos, usuariosProcesados);
		tablaDsipositivos.setItems(dispos);
	}

	private void cargarDispositivosDeUsuarioYRelacionados(long idUsuario, ObservableList<Dispositivos> dispos,
			ListaEnlazada<Long> usuariosProcesados) {
		if (contieneUsuario(usuariosProcesados, idUsuario)) {
			return; // Evitar procesar el mismo usuario m�s de una vez
		}
		usuariosProcesados.insertarAlFinal(new Nodo<>(idUsuario));

		// Cargar dispositivos del usuario actual
		ListaEnlazada<Dispositivos> dispositivosUsuario = DispositivosManager.cargarDispositivosPorUsuario(idUsuario);
		for (Nodo<Dispositivos> nodo = dispositivosUsuario.getCabeza(); nodo != null; nodo = nodo.getEnlace()) {
			Dispositivos dispositivo = nodo.getDato();
			agregarSiNoEstaDuplicado(dispos, dispositivo);
		}

		// Cargar dispositivos de usuarios relacionados
		ListaEnlazada<Relaciones> relaciones = RegistroManager.cargarRelacionesPorUsuario(idUsuario);
		for (Nodo<Relaciones> nodoRel = relaciones.getCabeza(); nodoRel != null; nodoRel = nodoRel.getEnlace()) {
			Relaciones relacion = nodoRel.getDato();
			long idRelacionado = (relacion.getTu_id() == idUsuario) ? relacion.getId_user_relacion()
					: relacion.getTu_id();
			cargarDispositivosDeUsuarioYRelacionados(idRelacionado, dispos, usuariosProcesados);
		}
	}

	private boolean contieneUsuario(ListaEnlazada<Long> lista, Long idUsuario) {
		for (Nodo<Long> nodo = lista.getCabeza(); nodo != null; nodo = nodo.getEnlace()) {
			if (nodo.getDato().equals(idUsuario)) {
				return true;
			}
		}
		return false;
	}

	private void agregarSiNoEstaDuplicado(ObservableList<Dispositivos> lista, Dispositivos dispositivo) {
		if (!lista.stream().anyMatch(d -> d.getId_dispo() == dispositivo.getId_dispo())) {
			lista.add(dispositivo);
		}
	}

	@FXML
	private void btnEncender(MouseEvent event) throws IOException {
		Usuario usuarioActual = Sesion.getInstancia().getUsuarioActual();
		if (usuarioActual == null) {
			// Manejar el caso en que usuarioActual es null
			return;
		}

		Dispositivos dispoSeleccionado = tablaDsipositivos.getSelectionModel().getSelectedItem();
		if (dispoSeleccionado != null) {
			String nombre = dispoSeleccionado.getNombre();
			DispositivosManager.modificarEstadoDispositivo(nombre, true);
			cargarDispositivos(usuarioActual.getId_user());
		} else {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Acceso Restringido");
			alert.setHeaderText(null);
			alert.setContentText("No tienes ningun dispositivo seleccionado");
			alert.showAndWait();
		}
	}

	@FXML
	private void btnApagar(MouseEvent event) throws IOException {

		Usuario usuarioActual = Sesion.getInstancia().getUsuarioActual();
		if (usuarioActual == null) {
			// Manejar el caso en que usuarioActual es null
			return;
		}

		Dispositivos dispoSeleccionado = tablaDsipositivos.getSelectionModel().getSelectedItem();
		if (dispoSeleccionado != null) {
			String nombre = dispoSeleccionado.getNombre();
			DispositivosManager.modificarEstadoDispositivo(nombre, false);
			cargarDispositivos(usuarioActual.getId_user());
		} else {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Acceso Restringido");
			alert.setHeaderText(null);
			alert.setContentText("No tienes ningun dispositivo seleccionado");
			alert.showAndWait();
		}

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