package controlador;

import java.io.IOException;

import applications.Main;
import entidades.Reporte;
import entidades.Dispositivos;
import entidades.Nodo;
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
import modelo.ReporteManager;
import modelo.DispositivosManager;
import modelo.ListaEnlazada;
import modelo.RegistroManager;
import modelo.Sesion;

public class Controlador_Interfaz_Leer_Peticion {

	@FXML
	private Label lblNombreUsu;

	@FXML
	private Label lblTipoCuenta;
	@FXML
	private Label lblFecha;
	
	@FXML
	private Button btnVerDatos;

	@FXML
	private TextField lblTitulo; // Para ingresar el nombre del usuario familiar
	@FXML
	private TextArea txtAreaDescripcion; // Para ingresar el nombre del usuario familiar
	@FXML
	private TextField lblEmisor; // Para ingresar el nombre del usuario familiar

	
	@FXML
	private TableView<Reporte> tableReportes;
	@FXML
	private TableColumn<Reporte, String> columnDescripcion;
	@FXML
	private TableColumn<Reporte, String> columTituloReporte;
	@FXML
	private TableColumn<Reporte, String> columnEmisor;

	@FXML
	public void initialize() {

		Usuario usuarioActual = Sesion.getInstancia().getUsuarioActual();
		if (usuarioActual != null) {
			String username = usuarioActual.getUsername();
			String tipoDeCuenta = usuarioActual.getTipodecuenta();
			lblNombreUsu.setText(username);
			lblTipoCuenta.setText(tipoDeCuenta);
		}

		this.columnDescripcion.setCellValueFactory(new PropertyValueFactory<>("queja"));
		this.columTituloReporte.setCellValueFactory(new PropertyValueFactory<>("titulo"));
		this.columnEmisor.setCellValueFactory(new PropertyValueFactory<>("id_user_emisor"));
		cargarReporte();
	}

	@FXML
	private void seleccionarDispositivo() {
		// Obtén el dispo seleccionado y actualiza el txtnombredispo
		Reporte repoSeleccionado = tableReportes.getSelectionModel().getSelectedItem();
		if (repoSeleccionado != null) {
			lblTitulo.setText(repoSeleccionado.getTitulo());
			txtAreaDescripcion.setText(repoSeleccionado.getQueja());
			lblEmisor.setText(String.valueOf(ReporteManager.sacarNombreDeId(repoSeleccionado.getId_user_emisor())));
			lblFecha.setText(String.valueOf(repoSeleccionado.getFecha()));

			ReporteManager.setReporteSeleccionado(repoSeleccionado);

		}
	}
	@FXML
	public void btnEliminarReporte(MouseEvent event) throws IOException {
		Reporte dispoSeleccionado = tableReportes.getSelectionModel().getSelectedItem();
		if (dispoSeleccionado != null) {

			if (ReporteManager.eliminarReporte(dispoSeleccionado.getTitulo())) {
				
				Alert alerta = new Alert(Alert.AlertType.INFORMATION);
				alerta.setTitle("Reporte");
				alerta.setHeaderText(null);
				alerta.setContentText("Se ha eliminado correctamente el reporte.");
				alerta.showAndWait();
				txtAreaDescripcion.setText("");
				lblEmisor.setText("");
				lblFecha.setText("");
				cargarReporte();
			}
			
		} else {
			
			Alert alerta = new Alert(Alert.AlertType.INFORMATION);
			alerta.setTitle("Reporte creado");
			alerta.setHeaderText(null);
			alerta.setContentText("No se ha poido eliminar el reporte.");
			alerta.showAndWait();
		}
	}

	private void cargarReporte() {
		ListaEnlazada<Reporte> todosLosRepos = ReporteManager.cargarReportes();
		ObservableList<Reporte> repos = FXCollections.observableArrayList();

		Nodo<Reporte> nodoActual = todosLosRepos.getCabeza();
		while (nodoActual != null) {
			Reporte dispo = nodoActual.getDato();

			repos.add(dispo);

			nodoActual = nodoActual.getEnlace();
		}

		tableReportes.setItems(repos);
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
