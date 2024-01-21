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
	private Label lblNombreUsuario; // Deber�a mostrar el nombre del usuario registrado
	@FXML
	private TextField txtNombreUsuarioFamiliar; // Para ingresar el nombre del usuario familiar
	@FXML
	private TableView<Usuario> tblUsuariosFamiliares; // La tabla de usuarios familiares
	@FXML
	private TableColumn<Usuario, String> colNombre;
	@FXML
	private TableColumn<Usuario, String> colCorreo;

	// M�todo que se ejecuta para inicializar la clase controladora
	public void initialize() {

		this.colNombre.setCellValueFactory(new PropertyValueFactory<>("username"));
		this.colCorreo.setCellValueFactory(new PropertyValueFactory<>("email"));
		// Aqu� cargar�as los usuarios familiares en la tabla y establecer�as el nombre
		// de usuario actual
		cargarUsuariosFamiliares();
		mostrarNombreUsuarioActual();
	}

	// Este m�todo se invocar� cuando el usuario haga clic en un usuario familiar en
	// la tabla
	@FXML
	private void seleccionarUsuarioFamiliar() {
		// Obt�n el usuario seleccionado y actualiza el txtNombreUsuarioFamiliar
		Usuario usuarioSeleccionado = tblUsuariosFamiliares.getSelectionModel().getSelectedItem();
		if (usuarioSeleccionado != null) {
			txtNombreUsuarioFamiliar.setText(usuarioSeleccionado.getUsername());
		}
	}

	// M�todo para manejar la acci�n del bot�n CONECTAR
//	
	@FXML
	private void conectarUsuarios() {
		// Obt�n los datos y crea una relaci�n, luego gu�rdala en el JSON
		String nombreUsuario = lblNombreUsuario.getText();
		String nombreUsuarioFamiliar = txtNombreUsuarioFamiliar.getText();
		// Suponiendo que tienes m�todos para obtener los ID basados en los nombres de
		// usuario
		long idUsuario = obtenerIdPorNombreUsuario(nombreUsuario);
		long idUsuarioFamiliar = obtenerIdPorNombreUsuario(nombreUsuarioFamiliar);

		if (!RegistroManager.verificarRelacionExistente(idUsuario, idUsuarioFamiliar)) {
			// Si la relaci�n no existe, crea una nueva y gu�rdala
			Relaciones nuevaRelacion = new Relaciones(idUsuario, idUsuarioFamiliar, "AF"); // Asumiendo "AF" como tipo
																							// de relaci�n
			RegistroManager.guardarRelacion(nuevaRelacion);

			mostrarMensaje("Se ha completado el enlace", "Relaci�n de AF");
			// Mostrar mensaje de �xito o actualizar la interfaz seg�n sea necesario
		} else {
			// Mostrar un mensaje de error indicando que la relaci�n ya existe
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Relaci�n Existente");
			alert.setHeaderText(null);
			alert.setContentText("La relaci�n ya existe.");
			alert.showAndWait();
		}
	}

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

	public void mostrarNombreUsuarioActual() {
		Usuario usuarioActual = Sesion.getInstancia().getUsuarioActual();
		if (usuarioActual != null) {
			lblNombreUsuario.setText(usuarioActual.getUsername());
		}
	}

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

	private void mostrarMensaje(String titulo, String contenido) {
		mostrarMensaje(titulo, contenido, Alert.AlertType.INFORMATION);
	}

	private void mostrarMensaje(String titulo, String contenido, Alert.AlertType tipo) {
		Alert alert = new Alert(tipo);
		alert.setTitle(titulo);
		alert.setHeaderText(null);
		alert.setContentText(contenido);
		alert.showAndWait();
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

			Stage ventanaActual = (Stage) btnConectar.getScene().getWindow();
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

			Stage ventatnaActual = (Stage) btnConectar.getScene().getWindow();
			ventatnaActual.hide();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}