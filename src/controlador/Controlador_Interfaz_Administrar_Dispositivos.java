package controlador;

import java.io.IOException;
import java.util.Iterator;
import java.util.Random;

import applications.Main;
import entidades.Dispositivos;
import entidades.Nodo;
import entidades.Relaciones;
import entidades.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
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

public class Controlador_Interfaz_Administrar_Dispositivos {

	@FXML
	private Label lblNombreUsu;

	@FXML
	private Label lblTipoCuenta;

	@FXML
	private ComboBox<String> tipoDispositivo;

	ObservableList<String> listaTipoDeDispo = FXCollections.observableArrayList("Aire", "Puerta", "Luz", "Persiana");

	@FXML
	private TextField txtNombreDispo;

	@FXML
	private TextField lblNombreDispoSelec;
	@FXML
	private TableView<Dispositivos> tableAdministrarDispos;
	@FXML
	private TableColumn<Dispositivos, String> columnaNombreDispo;
	@FXML
	private TableColumn<Dispositivos, String> columnaTipoDispo;
	@FXML
	private TableColumn<Dispositivos, String> columnaIdSensor;
	@FXML
	private Label Error_Label_Registro;

	private String aux;

	@FXML
	public void initialize() {

		Usuario usuarioActual = Sesion.getInstancia().getUsuarioActual();
		if (usuarioActual != null) {
			String username = usuarioActual.getUsername();
			String tipoDeCuenta = usuarioActual.getTipodecuenta();
			lblNombreUsu.setText(username);
			lblTipoCuenta.setText(tipoDeCuenta);
		}

		this.columnaNombreDispo.setCellValueFactory(new PropertyValueFactory<>("Nombre"));
		this.columnaTipoDispo.setCellValueFactory(new PropertyValueFactory<>("Tipo"));
		this.columnaIdSensor.setCellValueFactory(new PropertyValueFactory<>("id_sensor"));
		cargarDispositivos(usuarioActual.getId_user());
		tipoDispositivo.setItems(listaTipoDeDispo);

	}

	@FXML
	private void seleccionarDispositivo() {
		// Obtén el dispo seleccionado y actualiza el txtnombredispo
		Dispositivos dispoSeleccionado = tableAdministrarDispos.getSelectionModel().getSelectedItem();
		if (dispoSeleccionado != null) {
			lblNombreDispoSelec.setText(dispoSeleccionado.getNombre());
			aux = lblNombreDispoSelec.getText();
		}
	}

	private void cargarDispositivos(long idUsuarioLogueado) {
		ObservableList<Dispositivos> dispos = FXCollections.observableArrayList();
		ListaEnlazada<Long> usuariosProcesados = new ListaEnlazada<>();
		cargarDispositivosDeUsuarioYRelacionados(idUsuarioLogueado, dispos, usuariosProcesados);
		tableAdministrarDispos.setItems(dispos);
	}

	private void cargarDispositivosDeUsuarioYRelacionados(long idUsuario, ObservableList<Dispositivos> dispos,
			ListaEnlazada<Long> usuariosProcesados) {
		if (contieneUsuario(usuariosProcesados, idUsuario)) {
			return; // Evitar procesar el mismo usuario más de una vez
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


	@FXML // hay que actualizar este metodo
	private void anadirDsipositivo() throws IOException {
		Usuario usuarioActual = Sesion.getInstancia().getUsuarioActual();
		String Tipo = tipoDispositivo.getValue();
		String Nombre = txtNombreDispo.getText();
		long id_dispo = DispositivosManager.obtenerNuevoId();
		long id_sensor = anadirSensorDispo(Tipo);// hacer con ortega
		long id_usu_relacionado = usuarioActual.getId_user();

		if (Tipo != null && !Nombre.isEmpty() && !Tipo.isEmpty()) {

			Dispositivos nuevoUsuario = new Dispositivos(id_dispo, id_sensor, id_usu_relacionado, false, Tipo, Nombre);
			if (DispositivosManager.registrarDispos(nuevoUsuario)) {
				// hacer alerta de dispo añadido
				System.out.println(nuevoUsuario.toString());
				Error_Label_Registro.setVisible(false);
				cargarDispositivos(usuarioActual.getId_user());
			} else {
				Error_Label_Registro.setVisible(true);
				Error_Label_Registro.setText("Ya hay un dispositivo con ese nombre");
			}
		} else {
			Error_Label_Registro.setVisible(true);
			Error_Label_Registro.setText("Rellene todos los campos.");
		}
	}

	private long anadirSensorDispo(String tipo) {
		String tipoSensor;
		long id_sensor = 0;
		Random random = new Random();

		switch (tipo) {
		case "Aire":
			tipoSensor = "Temperatura";
			id_sensor = DispositivosManager.creaNuevoSensor(0, "Sensor de Temperatura", tipoSensor);
			for (int i = 0; i < 20; i++) {
				float datoTemperatura = 12.0f + random.nextFloat() * 20.0f;
				DispositivosManager.crearSensorConId(id_sensor, datoTemperatura, "Sensor de Temperatura", tipoSensor);
			}
			break;
		case "Puerta":
			tipoSensor = "Movimiento";
			id_sensor = DispositivosManager.creaNuevoSensor(0, "Sensor de Movimiento", tipoSensor);
			for (int i = 0; i < 20; i++) {
				// Suponiendo que el valor del sensor de movimiento varía entre 0 y 10
				float datoMovimiento = random.nextFloat() * 10.0f;
				DispositivosManager.crearSensorConId(id_sensor, datoMovimiento, "Sensor de Movimiento", tipoSensor);
			}
			break;
		case "Luz":
			tipoSensor = "Luz";
			id_sensor = DispositivosManager.creaNuevoSensor(0, "Sensor de Luz", tipoSensor);
			for (int i = 0; i < 20; i++) {
				// Suponiendo que el valor del sensor de luz varía entre 0 y 100
				float datoLuz = random.nextFloat() * 100.0f;
				DispositivosManager.crearSensorConId(id_sensor, datoLuz, "Sensor de Luz", tipoSensor);
			}
			break;
		case "Persiana":
			tipoSensor = "Posición";
			id_sensor = DispositivosManager.creaNuevoSensor(0, "Sensor de Posición", tipoSensor);
			for (int i = 0; i < 20; i++) {
				// Suponiendo que el valor del sensor de posición varía entre 0 y 180 (grados)
				float datoPosicion = random.nextFloat() * 180.0f;
				DispositivosManager.crearSensorConId(id_sensor, datoPosicion, "Sensor de Posición", tipoSensor);
			}
			break;
		default:
			tipoSensor = "Tipo de Sensor Desconocido";
			break;

		}
		return id_sensor;
	}

	@FXML
	public void btnAnadirDispo(MouseEvent event) throws IOException {
		anadirDsipositivo();
	}

	@FXML
	public void btnEliminar(MouseEvent event) throws IOException {
		Dispositivos dispoSeleccionado = tableAdministrarDispos.getSelectionModel().getSelectedItem();
		if (dispoSeleccionado != null) {

			if (DispositivosManager.eliminarDispositivo(dispoSeleccionado.getNombre())) {
				Error_Label_Registro.setVisible(false);
				Usuario usuarioActual = Sesion.getInstancia().getUsuarioActual();
				cargarDispositivos(usuarioActual.getId_user());
			}
		} else {
			Error_Label_Registro.setVisible(true);
			Error_Label_Registro.setText("No hay dispositivo seleccionado para eliminar");
		}
	}

	private void modificarDispositivo() throws IOException {

		String nombre = lblNombreDispoSelec.getText();

		if (!nombre.isEmpty()) {

			if (DispositivosManager.modificarDispositivo(aux, nombre)) {
				// Hacer alerta de dispositivo modificado
				Usuario usuarioActual = Sesion.getInstancia().getUsuarioActual();
				Error_Label_Registro.setVisible(false);
				cargarDispositivos(usuarioActual.getId_user());
			} else {
				Error_Label_Registro.setVisible(true);
				Error_Label_Registro.setText("No se encontró el dispositivo con ese nombre");
			}
		} else {
			Error_Label_Registro.setVisible(true);
			Error_Label_Registro.setText("Rellene todos los campos.");
		}
	}

	@FXML
	public void btnIrModiDispo(MouseEvent event) throws IOException {
		modificarDispositivo();
		lblNombreDispoSelec.setText("");

	}

//////////////////////////////////////////////////////////////////////////////
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
