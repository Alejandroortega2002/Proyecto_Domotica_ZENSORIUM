package controlador;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;

import com.mysql.jdbc.PreparedStatement;

import applications.ConexionDB;
import entidades.Reporte;
import entidades.Conversacion;
import entidades.Nodo;
import entidades.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import modelo.ReporteManager;
import modelo.ConversacionManager;
import modelo.ListaEnlazada;
import modelo.RegistroManager;
import modelo.Sesion;

public class Controlador_Interfaz_Chatear {

	static final String USER = "piizensorium";
	static final String PASS = "Zensorium@2024";

	@FXML
	private Label lblNombreUsu;

	@FXML
	private Label lblTipoCuenta;
	@FXML
	private Label lblIdReceptor;

	@FXML
	private TextField lblTitulo;
	@FXML
	private TextArea txtAreaDescripcion;
	@FXML
	private TextField textFieldMensaje;
	@FXML
	private Button btnEnviarMensaje;
	@FXML
	private TableView<Conversacion> tableConvers;
	@FXML
	private TableColumn<Conversacion, String> columnTexto;
	@FXML
	private TableColumn<Conversacion, String> columnIdReceptor;
	@FXML
	private TableColumn<Conversacion, String> columnIdConversacion;

	@FXML
	public void initialize() {
		Usuario usuarioActual = Sesion.getInstancia().getUsuarioActual();
		if (usuarioActual != null) {
			String username = usuarioActual.getUsername();
			String tipoDeCuenta = usuarioActual.getTipodecuenta();
			lblNombreUsu.setText(username);
			lblTipoCuenta.setText(tipoDeCuenta);
		}
		this.columnIdReceptor.setCellValueFactory(cellData -> {
			Conversacion conversacion = cellData.getValue();
			long idReceptor = conversacion.getIdReceptor();
			long idEmisor = conversacion.getIdEmisor();
			// Verificar si el usuario actual es el receptor
			if (usuarioActual.getId_user() == idReceptor) {
				Usuario emisor = RegistroManager.buscarUsuarioPorId(idEmisor);
				return new SimpleStringProperty(emisor != null ? emisor.getUsername() : "Desconocido");
			} else {
				Usuario receptor = RegistroManager.buscarUsuarioPorId(idReceptor);
				return new SimpleStringProperty(receptor != null ? receptor.getUsername() : "Desconocido");
			}
		});
		this.columnTexto.setCellValueFactory(new PropertyValueFactory<>("texto"));
		this.columnIdConversacion.setCellValueFactory(new PropertyValueFactory<>("idConversacion"));

		cargarConversaciones(usuarioActual.getId_user());
		// Iniciar el hilo para actualizar el chat
		iniciarHiloActualizacionChat();
	}

	@FXML
	private void seleccionarConversacion() {
		Conversacion conversacionSeleccionada = tableConvers.getSelectionModel().getSelectedItem();
		if (conversacionSeleccionada != null) {
			long idUsuarioLogueado = Sesion.getInstancia().getUsuarioActual().getId_user();
			long idReceptorSeleccionado = conversacionSeleccionada.getIdReceptor();
			long idEmisorSeleccionado = conversacionSeleccionada.getIdEmisor();

			long idUsuarioSeleccionado = (idReceptorSeleccionado == idUsuarioLogueado) ? idEmisorSeleccionado
					: idReceptorSeleccionado;

			// Obtener la conversación completa entre el usuario logueado y el usuario
			// seleccionado
			String conversacionCompleta = obtenerConversacionCompleta(idUsuarioLogueado, idUsuarioSeleccionado);

			// Mostrar la conversación en el TextArea
			txtAreaDescripcion.setText(conversacionCompleta);

			// Establecer la conversación seleccionada en el manager
			ConversacionManager.setConverSeleccionada(conversacionSeleccionada);
		}
	}

	private void cargarConversaciones(long idUsuarioLogueado) {
		ListaEnlazada<Conversacion> todasLasConversaciones = ConversacionManager
				.cargarConversaciones(idUsuarioLogueado);
		ObservableList<Long> idsReceptores = FXCollections.observableArrayList();
		ObservableList<Conversacion> conversacionesFiltradas = FXCollections.observableArrayList();

		Nodo<Conversacion> nodoActual = todasLasConversaciones.getCabeza();
		while (nodoActual != null) {
			Conversacion conversacionActual = nodoActual.getDato();

			long idReceptor = conversacionActual.getIdReceptor();
			long idEmisor = conversacionActual.getIdEmisor();
			long idUsuario = (idEmisor == idUsuarioLogueado) ? idReceptor : idEmisor;
			if (!idsReceptores.contains(idUsuario)) {
				idsReceptores.add(idUsuario);
				conversacionesFiltradas.add(conversacionActual);
			}

			nodoActual = nodoActual.getEnlace();
		}

		tableConvers.setItems(conversacionesFiltradas);
	}

	@FXML
	private void enviarMensaje() {
		Usuario usuarioActual = Sesion.getInstancia().getUsuarioActual();
		String mensaje = textFieldMensaje.getText().trim(); // Obtener el mensaje y eliminar espacios en blanco al
															// principio y al final

		// Verificar si el mensaje está vacío
		if (mensaje.isEmpty()) {
			return; // No hacer nada si el mensaje está vacío
		}

		long idEmisor = usuarioActual.getId_user();
		Conversacion conversacionSeleccionada = tableConvers.getSelectionModel().getSelectedItem();
		if (conversacionSeleccionada != null) {
			long idReceptor = (conversacionSeleccionada.getIdReceptor() == idEmisor)
					? conversacionSeleccionada.getIdEmisor()
					: conversacionSeleccionada.getIdReceptor();
			Date fechaEnvio = new Date(); // Obtener la fecha actual

			if (ConversacionManager.enviarMensaje(idEmisor, idReceptor, mensaje, fechaEnvio)) { // Verifica si el
																								// mensaje se envió
																								// correctamente
				// Actualiza el TextArea con el nuevo mensaje
				String nombreUsuario = usuarioActual.getUsername();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // Formato de fecha
				String fechaFormateada = LocalDateTime.now().format(formatter); // Formatear la fecha actual
				String mensajeConFecha = nombreUsuario + " - " + fechaFormateada + ": \n " + mensaje; // Incluye la fecha
																									// en el mensaje
				txtAreaDescripcion.appendText(mensajeConFecha); // Agrega el mensaje al TextArea
				textFieldMensaje.clear(); // Limpiar el TextField después de enviar el mensaje

				// Volver a seleccionar la conversación
				tableConvers.getSelectionModel().select(conversacionSeleccionada);
			} else {
				// Muestra un mensaje de error si no se pudo enviar el mensaje
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Error al enviar el mensaje");
				alert.setHeaderText(null);
				alert.setContentText("No se pudo enviar el mensaje. Inténtalo nuevamente.");
				alert.showAndWait();
			}
		}
	}

	private String obtenerConversacionCompleta(long idUsuarioLogueado, long idUsuarioSeleccionado) {
		StringBuilder conversacionCompleta = new StringBuilder();
		String query = "SELECT c.texto, c.fecha_envio, u1.username AS emisor, u2.username AS receptor "
				+ "FROM conversaciones c " + "JOIN usuario u1 ON c.id_e = u1.id_user "
				+ "JOIN usuario u2 ON c.id_r = u2.id_user "
				+ "WHERE (c.id_e = ? AND c.id_r = ?) OR (c.id_e = ? AND c.id_r = ?) " + "ORDER BY c.fecha_envio";
		try (Connection conexion = ConexionDB.obtenerConexion();
				PreparedStatement stmt = (PreparedStatement) conexion.prepareStatement(query)) {
			stmt.setLong(1, idUsuarioLogueado);
			stmt.setLong(2, idUsuarioSeleccionado);
			stmt.setLong(3, idUsuarioSeleccionado);
			stmt.setLong(4, idUsuarioLogueado);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					String emisor = rs.getString("emisor");
					String texto = rs.getString("texto");

					// Verificar si el mensaje no está en blanco antes de agregarlo
					if (!texto.isEmpty()) {
						Timestamp fechaEnvio = rs.getTimestamp("fecha_envio");
						SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String fechaFormateada = dateFormat.format(fechaEnvio);
						conversacionCompleta.append(emisor).append(" - ").append(fechaFormateada).append(": \n")
								.append(texto).append("\n");
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conversacionCompleta.toString();
	}

	private void iniciarHiloActualizacionChat() {
		Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				while (true) {
					try {
						Thread.sleep(1000); // Espera 1 segundos

						// Actualiza el chat en el hilo de JavaFX
						Platform.runLater(() -> {
							Conversacion conversacionSeleccionada = tableConvers.getSelectionModel().getSelectedItem();
							if (conversacionSeleccionada != null) {
								long idUsuarioLogueado = Sesion.getInstancia().getUsuarioActual().getId_user();
								long idReceptorSeleccionado = conversacionSeleccionada.getIdReceptor();
								long idEmisorSeleccionado = conversacionSeleccionada.getIdEmisor();

								long idUsuarioSeleccionado = (idReceptorSeleccionado == idUsuarioLogueado)
										? idEmisorSeleccionado
										: idReceptorSeleccionado;

								// Obtener la conversación completa entre el usuario logueado y el usuario
								// seleccionado
								String conversacionCompleta = obtenerConversacionCompleta(idUsuarioLogueado,
										idUsuarioSeleccionado);

								// Mostrar la conversación en el TextArea
								txtAreaDescripcion.setText(conversacionCompleta);
							}
						});
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};

		Thread hilo = new Thread(task);
		hilo.setDaemon(true); // Permitir que el hilo se detenga cuando se cierre la aplicación
		hilo.start();
	}

///////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////
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