package controlador;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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

public class Controlador_InterfazDispositivos {

    // Componentes de la interfaz de usuario definidos con la anotación @FXML
    @FXML
    private Label lblNombreUsu;
    @FXML
    private Label lblTipoCuenta;
    @FXML
    private Button btnVerDatos;
    @FXML
    private Button btnAdministarDsipo;
    @FXML
    private TextField txtFieldNombreDispo;
    @FXML
    private TextField txtFieldNombreSensorRelacionado;
    @FXML
    private TableView<Dispositivos> tablaDsipositivos;
    @FXML
    private TableColumn<Dispositivos, String> columnaNombre;
    @FXML
    private TableColumn<Dispositivos, String> columnaEstado;
    @FXML
    private TableColumn<Dispositivos, String> columnaIdSensor;

    /**
     * Método de inicialización de JavaFX.
     * Configura la interfaz de usuario con los datos del usuario actual y carga los dispositivos.
     */
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

    /**
     * Deshabilita botones en función del tipo de usuario.
     * @param tipoUsu El tipo de cuenta del usuario actual.
     */
    private void deshabilitarBtns(String tipoUsu) {
        Usuario usuarioActual = Sesion.getInstancia().getUsuarioActual();

		if (usuarioActual != null) {
			if (usuarioActual.getTipodecuenta().equals("Usuario")) {
				btnAdministarDsipo.setDisable(true);
				btnAdministarDsipo.setManaged(false);

			}

    }}

    /**
     * Maneja la selección de un dispositivo en la tabla.
     * @param event Información del evento de clic del mouse.
     */
    @FXML
    private void seleccionarDispositivo(MouseEvent event) {
        // Obtén el dispo seleccionado y actualiza el txtnombredispo
		Dispositivos dispoSeleccionado = tablaDsipositivos.getSelectionModel().getSelectedItem();
		if (dispoSeleccionado != null) {
			txtFieldNombreDispo.setText(dispoSeleccionado.getNombre());
			txtFieldNombreSensorRelacionado.setText(String.valueOf(dispoSeleccionado.getId_sensor()));
			DispositivosManager.setDispositivoSeleccionado(dispoSeleccionado);

		}
    }

    /**
     * Carga los dispositivos del usuario logueado y de sus usuarios relacionados.
     * @param idUsuarioLogueado El ID del usuario actual.
     */
    private void cargarDispositivos(long idUsuarioLogueado) {
        ObservableList<Dispositivos> dispos = FXCollections.observableArrayList();
		ListaEnlazada<Long> usuariosProcesados = new ListaEnlazada<>();
		cargarDispositivosDeUsuarioYRelacionados(idUsuarioLogueado, dispos, usuariosProcesados);
		tablaDsipositivos.setItems(dispos);
    }

    /**
     * Carga los dispositivos de un usuario y sus usuarios relacionados.
     * @param idUsuario El ID del usuario para cargar sus dispositivos.
     * @param dispos Lista observable donde se cargan los dispositivos.
     * @param usuariosProcesados Lista de usuarios ya procesados para evitar duplicados.
     */
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
    }}

    // ... (Continuación de métodos para manejar eventos y navegación en la interfaz)
    /**
     * Verifica si una lista enlazada contiene un usuario específico basado en el ID.
     * @param lista Lista enlazada de IDs de usuario para buscar.
     * @param idUsuario El ID del usuario a buscar en la lista.
     * @return true si el usuario está en la lista, false en caso contrario.
     */
private boolean contieneUsuario(ListaEnlazada<Long> lista, Long idUsuario) {
		for (Nodo<Long> nodo = lista.getCabeza(); nodo != null; nodo = nodo.getEnlace()) {
			if (nodo.getDato().equals(idUsuario)) {
				return true;
			}
		}
		return false;
	}
/**
 * Agrega un dispositivo a la lista observable si no está duplicado.
 * @param lista La lista observable de dispositivos donde se agregará el nuevo dispositivo.
 * @param dispositivo El dispositivo a agregar a la lista.
 */
	private void agregarSiNoEstaDuplicado(ObservableList<Dispositivos> lista, Dispositivos dispositivo) {
		if (!lista.stream().anyMatch(d -> d.getId_dispo() == dispositivo.getId_dispo())) {
			lista.add(dispositivo);
		}
	}

    /**
     * Maneja el evento para encender un dispositivo seleccionado.
     * @param event Información del evento de clic del mouse.
     * @throws IOException Si ocurre un error al procesar la acción.
     */
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

    /**
     * Maneja el evento para apagar un dispositivo seleccionado.
     * @param event Información del evento de clic del mouse.
     * @throws IOException Si ocurre un error al procesar la acción.
     */
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

    // ... (Más métodos para la navegación y gestión de la interfaz)
    /**
     * Maneja el evento para ir al menú del perfil del usuario.
     * @param event Información del evento de clic del mouse.
     * @throws IOException Si ocurre un error al cargar el recurso FXML.
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
 * Maneja el evento para ir a la interfaz principal de dispositivos.
 * @param event Información del evento de clic del mouse.
 * @throws IOException Si ocurre un error al cargar el recurso FXML.
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
	/**
     * Maneja el evento para ir a la interfaz de administración de dispositivos.
     * @param event Información del evento de clic del mouse.
     * @throws IOException Si ocurre un error al cargar el recurso FXML.
     */
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
	/**
     * Maneja el evento para ir a la interfaz de visualización de datos de un dispositivo seleccionado.
     * @param event Información del evento de clic del mouse.
     * @throws IOException Si ocurre un error al cargar el recurso FXML o si no hay un dispositivo seleccionado.
     */
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

