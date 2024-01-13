package controlador;

import java.io.IOException;

import applications.Main;
import entidades.Dispositivos;
import entidades.Nodo;
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
		cargarDispositivos();
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

	private void cargarDispositivos() {
		System.out.println("holi");
		ListaEnlazada<Dispositivos> todosLosDispos = DispositivosManager.cargarDispos();
		ObservableList<Dispositivos> dispos = FXCollections.observableArrayList();

		Nodo<Dispositivos> nodoActual = todosLosDispos.getCabeza();
		while (nodoActual != null) {
			Dispositivos dispo = nodoActual.getDato();

			dispos.add(dispo);

			nodoActual = nodoActual.getEnlace();
		}

		tableAdministrarDispos.setItems(dispos);
	}

	@FXML // hay que actualizar este metodo
	private void anadirDsipositivo() throws IOException {
		Usuario usuarioActual = Sesion.getInstancia().getUsuarioActual();

		long id_dispo = DispositivosManager.obtenerNuevoId();
		long id_sensor;// hacer con ortega
		long id_usu_relacionado = usuarioActual.getId_user();
		String Tipo = tipoDispositivo.getValue();
		String Nombre = txtNombreDispo.getText();

		if (Tipo != null && !Nombre.isEmpty() && !Tipo.isEmpty()) {
			
			Dispositivos nuevoUsuario = new Dispositivos(id_dispo, 0, id_usu_relacionado, false, Tipo, Nombre);
			if (DispositivosManager.registrarDispos(nuevoUsuario)) {
				// hacer alerta de dispo añadido
				System.out.println(nuevoUsuario.toString());
				Error_Label_Registro.setVisible(false);
				cargarDispositivos();
			} else {
				Error_Label_Registro.setVisible(true);
				Error_Label_Registro.setText("Ya hay un dispositivo con ese nombre");
			}
		} else {
			Error_Label_Registro.setVisible(true);
			Error_Label_Registro.setText("Rellene todos los campos.");
		}
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
				cargarDispositivos();
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
			
				Error_Label_Registro.setVisible(false);
				cargarDispositivos();
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
