package controlador;

import entidades.Sensores;
import entidades.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class Controlador_Ver_Datos {

	@FXML
	private TableView<Sensores> tblDatosSensores; // La tabla de usuarios familiares
	@FXML
	private TableColumn<Sensores, Long> colNRegistro;
	@FXML
	private TableColumn<Sensores, Float> colDato;
	@FXML
	private TableColumn<Sensores, String> colTipo;
	@FXML
	private TableColumn<Sensores, Long> colId;

	@FXML
	void initialize() {
		// Initialize the controller
		this.colNRegistro.setCellValueFactory(new PropertyValueFactory<>("orden_registro"));
		this.colDato.setCellValueFactory(new PropertyValueFactory<>("dato_actual"));
		this.colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
		this.colId.setCellValueFactory(new PropertyValueFactory<>("id_sensor"));
	}

}
