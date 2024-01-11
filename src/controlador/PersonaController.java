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

//public class PersonaController implements Initializable {
//
//    @FXML
//    private Button btnAgregar;
//    @FXML
//    private TextField txtNombre;
//    @FXML
//    private TextField txtApellidos;
//    @FXML
//    private TextField txtEdad;
//    @FXML
//    private TableView<Persona> tblPersonas;
//    @FXML
//    private TableColumn colNombre;
//    @FXML
//    private TableColumn colApellidos;
//    @FXML
//    private TableColumn colEdad;
//
//    private ObservableList<Usuario> usuarios;
//    @FXML
//    private Button btnModificar;
//    @FXML
//    private Button btnEliminar;
//
//    /**
//     * Initializes the controller class.
//     */
//    @Override
//    public void initialize(URL url, ResourceBundle rb) {
//        usuarios = FXCollections.observableArrayList();
//
//        this.colNombre.setCellValueFactory(new PropertyValueFactory("nombre"));
//        this.colApellidos.setCellValueFactory(new PropertyValueFactory("apellidos"));
//        this.colEdad.setCellValueFactory(new PropertyValueFactory("edad"));
//    }
//
//    @FXML
//    private void agregarRelacion(ActionEvent event) {
//
//        try {
//
//            // Obtengo los datos del formulario
//            String nombre = this.txtNombre.getText();
//            String apellidos = this.txtApellidos.getText();
//            int edad = Integer.parseInt(this.txtEdad.getText());
//
//            // Creo una persona
//            Persona p = new Persona(nombre, apellidos, edad);
//
//            // Compruebo si la persona esta en el lista
//            if (!this.personas.contains(p)) {
//                // Lo añado a la lista
//                this.personas.add(p);
//                // Seteo los items
//                this.tblPersonas.setItems(personas);
//
//                Alert alert = new Alert(Alert.AlertType.INFORMATION);
//                alert.setHeaderText(null);
//                alert.setTitle("Info");
//                alert.setContentText("Persona añadida");
//                alert.showAndWait();
//            } else {
//
//                Alert alert = new Alert(Alert.AlertType.ERROR);
//                alert.setHeaderText(null);
//                alert.setTitle("Error");
//                alert.setContentText("La persona existe");
//                alert.showAndWait();
//            }
//        } catch (NumberFormatException e) {
//
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setHeaderText(null);
//            alert.setTitle("Error");
//            alert.setContentText("Formato incorrecto");
//            alert.showAndWait();
//        }
//
//    }
//
//    @FXML
//    private void seleccionar(MouseEvent event) {
//
//        // Obtengo la persona seleccionada
//        Persona p = this.tblPersonas.getSelectionModel().getSelectedItem();
//
//        // Sino es nula seteo los campos
//        if (p != null) {
//            this.txtNombre.setText(p.getNombre());
//            this.txtApellidos.setText(p.getApellidos());
//            this.txtEdad.setText(p.getEdad() + "");
//        }
//
//    }
//
//    @FXML
//    private void modificar(ActionEvent event) {
//
//        // Obtengo la persona seleccionada
//        Persona p = this.tblPersonas.getSelectionModel().getSelectedItem();
//
//        // Si la persona es nula, lanzo error
//        if (p == null) {
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setHeaderText(null);
//            alert.setTitle("Error");
//            alert.setContentText("Debes seleccionar una persona");
//            alert.showAndWait();
//        } else {
//
//            try {
//                // Obtengo los datos del formulario
//                String nombre = this.txtNombre.getText();
//                String apellidos = this.txtApellidos.getText();
//                int edad = Integer.parseInt(this.txtEdad.getText());
//
//                // Creo una persona
//                Persona aux = new Persona(nombre, apellidos, edad);
//
//                // Compruebo si la persona esta en el lista
//                if (!this.personas.contains(aux)) {
//
//                    // Modifico el objeto
//                    p.setNombre(aux.getNombre());
//                    p.setApellidos(aux.getApellidos());
//                    p.setEdad(aux.getEdad());
//
//                    // Refresco la tabla
//                    this.tblPersonas.refresh();
//
//                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
//                    alert.setHeaderText(null);
//                    alert.setTitle("Info");
//                    alert.setContentText("Persona modificada");
//                    alert.showAndWait();
//
//                } else {
//
//                    Alert alert = new Alert(Alert.AlertType.ERROR);
//                    alert.setHeaderText(null);
//                    alert.setTitle("Error");
//                    alert.setContentText("La persona existe");
//                    alert.showAndWait();
//                }
//            } catch (NumberFormatException e) {
//
//                Alert alert = new Alert(Alert.AlertType.ERROR);
//                alert.setHeaderText(null);
//                alert.setTitle("Error");
//                alert.setContentText("Formato incorrecto");
//                alert.showAndWait();
//            }
//
//        }
//
//    }
//
//    @FXML
//    private void eliminar(ActionEvent event) {
//
//        // Obtengo la persona seleccionada
//        Persona p = this.tblPersonas.getSelectionModel().getSelectedItem();
//
//        // Si la persona es nula, lanzo error
//        if (p == null) {
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setHeaderText(null);
//            alert.setTitle("Error");
//            alert.setContentText("Debes seleccionar una persona");
//            alert.showAndWait();
//        } else {
//
//            // La elimino de la lista
//            this.personas.remove(p);
//            // Refresco la lista
//            this.tblPersonas.refresh();
//
//            Alert alert = new Alert(Alert.AlertType.INFORMATION);
//            alert.setHeaderText(null);
//            alert.setTitle("Info");
//            alert.setContentText("Persona eliminada");
//            alert.showAndWait();
//
//        }
//
//    }
//
//}
public class PersonaController {

	@FXML
	private Button btnConectar;
	@FXML
	private Label lblNombreUsuario; // Debería mostrar el nombre del usuario registrado
	@FXML
	private TextField txtNombreUsuarioFamiliar; // Para ingresar el nombre del usuario familiar
	@FXML
	private TableView<Usuario> tblUsuariosFamiliares; // La tabla de usuarios familiares
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
		cargarUsuariosFamiliares();
		mostrarNombreUsuarioActual();
	}

	// Este método se invocará cuando el usuario haga clic en un usuario familiar en
	// la tabla
	@FXML
	private void seleccionarUsuarioFamiliar() {
		// Obtén el usuario seleccionado y actualiza el txtNombreUsuarioFamiliar
		Usuario usuarioSeleccionado = tblUsuariosFamiliares.getSelectionModel().getSelectedItem();
		if (usuarioSeleccionado != null) {
			txtNombreUsuarioFamiliar.setText(usuarioSeleccionado.getUsername());
		}
	}

	// Método para manejar la acción del botón CONECTAR
//	
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
			Relaciones nuevaRelacion = new Relaciones(idUsuario, idUsuarioFamiliar, "F->B"); // Asumiendo "AF" como tipo
																								// de relación
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
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/Interfaz_Usuario_def.fxml"));

			Controlador_pantalla_usuario_def control = new Controlador_pantalla_usuario_def();

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
