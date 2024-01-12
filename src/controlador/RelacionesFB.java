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

public class RelacionesFB {

	@FXML
	private Button btnConectar;
	@FXML
	private Label lblNombreUsuario; // Debería mostrar el nombre del usuario registrado
	@FXML
	private TextField txtNombreUsuarioBasico; // Para ingresar el nombre del usuario familiar
	@FXML
	private TableView<Usuario> tblUsuariosBasicos; // La tabla de usuarios familiares
	@FXML
	private TableColumn<Usuario, String> colNombre;
	@FXML
	private TableColumn<Usuario, String> colCorreo;

	// Método que se ejecuta para inicializar la clase controladora
	public void initialize() {

		this.colNombre.setCellValueFactory(new PropertyValueFactory<>("username"));
		this.colCorreo.setCellValueFactory(new PropertyValueFactory<>("email"));
		// Aquí cargarías los usuarios familiares en la tabla y establecerías el nombre
		// de usuario actual
		cargarUsuariosBasicos();
		mostrarNombreUsuarioActual();
	}

	// Este método se invocará cuando el usuario haga clic en un usuario familiar en
	// la tabla
	@FXML
	private void seleccionarUsuarioBasico() {
		// Obtén el usuario seleccionado y actualiza el txtNombreUsuarioFamiliar
		Usuario usuarioSeleccionado = tblUsuariosBasicos.getSelectionModel().getSelectedItem();
		if (usuarioSeleccionado != null) {
			txtNombreUsuarioBasico.setText(usuarioSeleccionado.getUsername());
		}
	}

	// Método para manejar la acción del botón CONECTAR
//	
	@FXML
	private void conectarUsuarios() {
		// Obtén los datos y crea una relación, luego guárdala en el JSON
		String nombreUsuario = lblNombreUsuario.getText();
		String nombreUsuarioBasico = txtNombreUsuarioBasico.getText();
		// Suponiendo que tienes métodos para obtener los ID basados en los nombres de
		// usuario
		long idUsuario = obtenerIdPorNombreUsuario(nombreUsuario);
		long idUsuarioBasico = obtenerIdPorNombreUsuario(nombreUsuarioBasico);

		if (!RegistroManager.verificarRelacionExistente(idUsuario, idUsuarioBasico)) {
			// Si la relación no existe, crea una nueva y guárdala
			Relaciones nuevaRelacion = new Relaciones(idUsuario, idUsuarioBasico, "FB"); 
			RegistroManager.guardarRelacion(nuevaRelacion);

			mostrarMensaje("Se ha completado el enlace", "Relación de FB");
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

	private void cargarUsuariosBasicos() {
		ListaEnlazada<Usuario> todosLosUsuarios = RegistroManager.cargarUsuarios();
		ObservableList<Usuario> usuariosBasicos = FXCollections.observableArrayList();

		Nodo<Usuario> nodoActual = todosLosUsuarios.getCabeza();
		while (nodoActual != null) {
			Usuario usuario = nodoActual.getDato();
			if ("Usuario".equals(usuario.getTipodecuenta())) {
				usuariosBasicos.add(usuario);
			}
			nodoActual = nodoActual.getEnlace();
		}

		tblUsuariosBasicos.setItems(usuariosBasicos);
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

			Stage ventanaActual = (Stage) tblUsuariosBasicos.getScene().getWindow();
			ventanaActual.hide();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@FXML
	private void irInicio(MouseEvent event) throws IOException {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/Interfaz_Usuario_def.fxml"));

			Controlador_pantalla_usuario_def control = new Controlador_pantalla_usuario_def();

			loader.setController(control);

			Parent root = loader.load();
			Stage primaryStage = new Stage();
			primaryStage.setScene(new Scene(root));
			primaryStage.show();

			Stage ventatnaActual = (Stage) tblUsuariosBasicos.getScene().getWindow();
			ventatnaActual.hide();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
