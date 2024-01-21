package controlador;

import java.io.IOException;

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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import modelo.ListaEnlazada;
import modelo.RegistroManager;
import modelo.Sesion;

public class RelacionesAF {

    @FXML
    private Button btnConectar;
    @FXML
    private Label lblNombreUsuario;
    @FXML
    private TextField txtNombreUsuarioFamiliar;
    @FXML
    private TableView<Usuario> tblUsuariosFamiliares;
    @FXML
    private TableColumn<Usuario, String> colNombre;
    @FXML
    private TableColumn<Usuario, String> colCorreo;

    /**
     * Método de inicialización del controlador.
     */
    public void initialize() {
        
		this.colNombre.setCellValueFactory(new PropertyValueFactory<>("username"));
		this.colCorreo.setCellValueFactory(new PropertyValueFactory<>("email"));
		// Aquí cargarías los usuarios familiares en la tabla y establecerías el nombre
		// de usuario actual
		cargarUsuariosFamiliares();
		mostrarNombreUsuarioActual();
    }

    /**
     * Selecciona un usuario familiar de la tabla y actualiza el campo de texto correspondiente.
     */
    @FXML
    private void seleccionarUsuarioFamiliar() {
       // Obtén el usuario seleccionado y actualiza el txtNombreUsuarioFamiliar
		Usuario usuarioSeleccionado = tblUsuariosFamiliares.getSelectionModel().getSelectedItem();
		if (usuarioSeleccionado != null) {
			txtNombreUsuarioFamiliar.setText(usuarioSeleccionado.getUsername());
		}
    }

    /**
     * Establece una conexión entre usuarios.
     * Crea una relación entre el usuario actual y el usuario familiar seleccionado.
     */
    @FXML
    private void conectarUsuarios() {
        // Obtén los datos y crea una relación, luego guárdala en el JSON
		String nombreUsuario = lblNombreUsuario.getText();
		String nombreUsuarioFamiliar = txtNombreUsuarioFamiliar.getText();
		// Suponiendo que tienes métodos para obtener los ID basados en los nombres de
		// usuario
		long idUsuario = obtenerIdPorNombreUsuario(nombreUsuario);
		long idUsuarioFamiliar = obtenerIdPorNombreUsuario(nombreUsuarioFamiliar);

		if (!RegistroManager.verificarRelacionExistente(idUsuario, idUsuarioFamiliar)) {
			// Si la relación no existe, crea una nueva y guárdala
			Relaciones nuevaRelacion = new Relaciones(idUsuario, idUsuarioFamiliar, "AF"); // Asumiendo "AF" como tipo
																								// de relación
			RegistroManager.guardarRelacion(nuevaRelacion);

			mostrarMensaje("Se ha completado el enlace", "Relación de AF");
			// Mostrar mensaje de éxito o actualizar la interfaz según sea necesario
		} else {
			// Mostrar un mensaje de error indicando que la relación ya existe
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Relación Existente");
			alert.setHeaderText(null);
			alert.setContentText("La relación ya existe.");
			alert.showAndWait();
		}
    }

    /**
     * Carga los usuarios con el tipo de cuenta 'Familiar' en la tabla.
     */
    private void cargarUsuariosFamiliares() {
        ListaEnlazada<Usuario> todosLosUsuarios = RegistroManager.cargarUsuarios();
		ObservableList<Usuario> usuariosFamiliares = FXCollections.observableArrayList();

		Nodo<Usuario> nodoActual = todosLosUsuarios.getCabeza();
		while (nodoActual != null) {
			Usuario usuario = nodoActual.getDato();
			if ("Familiar".equals(usuario.getTipodecuenta())) {
				usuariosFamiliares.add(usuario);
			}
			nodoActual = nodoActual.getEnlace();
		}

		tblUsuariosFamiliares.setItems(usuariosFamiliares);
    }

    /**
     * Muestra el nombre del usuario actual en la etiqueta correspondiente.
     */
    public void mostrarNombreUsuarioActual() {
        Usuario usuarioActual = Sesion.getInstancia().getUsuarioActual();
		if (usuarioActual != null) {
			lblNombreUsuario.setText(usuarioActual.getUsername());
		}
    }

    /**
     * Obtiene el ID de un usuario a partir de su nombre.
     * @param nombreUsuario El nombre del usuario cuyo ID se desea obtener.
     * @return El ID del usuario o -1 si no se encuentra.
     */
    private long obtenerIdPorNombreUsuario(String nombreUsuario) {
        ListaEnlazada<Usuario> usuarios = RegistroManager.cargarUsuarios();

		Nodo<Usuario> nodoActual = usuarios.getCabeza();
		while (nodoActual != null) {
			Usuario usuario = nodoActual.getDato();
			if (usuario.getUsername().equals(nombreUsuario)) {
				return usuario.getId_user();
			}
			nodoActual = nodoActual.getEnlace();
		}

		return -1; // Retorna -1 si no se encuentra el usuario
    }

    /**
     * Muestra un mensaje con un título y contenido especificados.
     * @param titulo El título del mensaje.
     * @param contenido El contenido del mensaje.
     */
    private void mostrarMensaje(String titulo, String contenido) {
        mostrarMensaje(titulo, contenido, Alert.AlertType.INFORMATION);
    }

    /**
     * Muestra un mensaje con un título, contenido y tipo especificados.
     * @param titulo El título del mensaje.
     * @param contenido El contenido del mensaje.
     * @param tipo El tipo de alerta (por ejemplo, ERROR, INFORMACIÓN).
     */
    private void mostrarMensaje(String titulo, String contenido, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
		alert.setTitle(titulo);
		alert.setHeaderText(null);
		alert.setContentText(contenido);
		alert.showAndWait();
    }

    /**
     * Navega al menú del perfil del usuario.
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

			Stage ventanaActual = (Stage) btnConectar.getScene().getWindow();
			ventanaActual.hide();
		} catch (Exception e) {
			e.printStackTrace();
		}

    }

    /**
     * Navega a la interfaz principal de dispositivos.
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

			Stage ventatnaActual = (Stage) btnConectar.getScene().getWindow();
			ventatnaActual.hide();

		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
