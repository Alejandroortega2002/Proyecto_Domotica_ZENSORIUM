package controlador;

import java.io.IOException;

import entidades.Nodo;
import entidades.Relaciones;
import entidades.Usuario;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import modelo.ListaEnlazada;
import modelo.RegistroManager;
import modelo.Sesion;

public class Controlador_Pantalla_Perfil {

	@FXML
	private Button btnRelaciones;

	@FXML
	private Button btnEnviarReporte;

	@FXML
	private Button btnVerReporte;

	@FXML
	private Label LblUsername;
	@FXML
	private Label LblTipoCuenta;
	@FXML
	private Label LblUsuariosAsociados;

	@FXML
	private void initialize() {
		// Puedes realizar inicializaciones aqu� si es necesario
		Usuario usuarioActual = Sesion.getInstancia().getUsuarioActual();
		if (usuarioActual != null) {
			String username = usuarioActual.getUsername();
			String tipoDeCuenta = usuarioActual.getTipodecuenta();
			LblUsername.setText(username);
			LblTipoCuenta.setText(tipoDeCuenta);
			cargarYMostrarUsuariosAsociados(usuarioActual);

		}
	}

	@FXML
	private void cerrarSesion(MouseEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/Pantalla_Inicio_Login.fxml"));

			Controlador_Inicio_Login control = new Controlador_Inicio_Login();

			loader.setController(control);

			Parent root = loader.load();
			Stage primaryStage = new Stage();
			primaryStage.setScene(new Scene(root));
			primaryStage.show();
			Stage ventanaActual = (Stage) LblUsername.getScene().getWindow();
			ventanaActual.hide();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	@FXML
	private void irRelaciones(MouseEvent event) {

		Usuario usuarioActual = Sesion.getInstancia().getUsuarioActual();
		if (usuarioActual != null) {
			String tipoDeCuenta = usuarioActual.getTipodecuenta();

			switch (tipoDeCuenta) {
			case "Familiar":
				try {
					FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/Relacion_FamiBasi.fxml"));

					RelacionesFB control = new RelacionesFB();

					loader.setController(control);

					Parent root = loader.load();
					Stage primaryStage = new Stage();
					primaryStage.setScene(new Scene(root));
					primaryStage.show();

					Stage ventanaActual = (Stage) btnRelaciones.getScene().getWindow();
					ventanaActual.hide();
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;

			case "Administrador":
				try {
					FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/Relacion_AdminFami.fxml"));

					RelacionesAF control = new RelacionesAF();

					loader.setController(control);

					Parent root = loader.load();
					Stage primaryStage = new Stage();
					primaryStage.setScene(new Scene(root));
					primaryStage.show();

					Stage ventanaActual = (Stage) btnRelaciones.getScene().getWindow();
					ventanaActual.hide();
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;

			case "Usuario":
				// Mostrar mensaje de error o no hacer nada
				Alert alert = new Alert(Alert.AlertType.WARNING);
				alert.setTitle("Acceso Restringido");
				alert.setHeaderText(null);
				alert.setContentText("No tienes acceso a esta funci�n.");
				alert.showAndWait();
				return; // Salir del m�todo sin hacer nada m�s

			default:
				// Manejar cualquier otro caso
				return;
			}
		}
	}

	private void cargarYMostrarUsuariosAsociados(Usuario usuarioActual) {
		ListaEnlazada<Relaciones> relacionesDelUsuario = RegistroManager
				.cargarRelacionesPorUsuario(usuarioActual.getId_user());
		ListaEnlazada<Usuario> todosLosUsuarios = RegistroManager.cargarUsuarios();

		StringBuilder nombresAsociados = new StringBuilder();
		for (Nodo<Relaciones> nodoRelacion = relacionesDelUsuario
				.getCabeza(); nodoRelacion != null; nodoRelacion = nodoRelacion.getEnlace()) {
			Relaciones relacion = nodoRelacion.getDato();
			long idUsuarioRelacionado = (relacion.getTu_id() == usuarioActual.getId_user())
					? relacion.getId_user_relacion()
					: relacion.getTu_id();

			for (Nodo<Usuario> nodoUsuario = todosLosUsuarios
					.getCabeza(); nodoUsuario != null; nodoUsuario = nodoUsuario.getEnlace()) {
				Usuario usuario = nodoUsuario.getDato();
				if (usuario.getId_user() == idUsuarioRelacionado) {
					if (nombresAsociados.length() > 0) {
						nombresAsociados.append(", ");
					}
					nombresAsociados.append(usuario.getUsername());
					break;
				}
			}

			// Solo se establece el texto si hay nombres asociados, de lo contrario, se deja
			// en blanco
			if (nombresAsociados.length() > 0) {
				LblUsuariosAsociados.setText(nombresAsociados.toString());
			} else {
				LblUsuariosAsociados.setText(""); // O puedes omitir esta l�nea si el texto ya est� en blanco por
													// defecto
			}
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

			Stage ventatnaActual = (Stage) btnRelaciones.getScene().getWindow();
			ventatnaActual.hide();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void irChatear(MouseEvent event) throws IOException {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/Interfaz_Chat.fxml"));

			Controlador_Interfaz_Chatear control = new Controlador_Interfaz_Chatear();

			loader.setController(control);

			Parent root = loader.load();
			Stage primaryStage = new Stage();
			primaryStage.setScene(new Scene(root));
			primaryStage.show();

			Stage ventatnaActual = (Stage) btnRelaciones.getScene().getWindow();
			ventatnaActual.hide();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@FXML
	private void irEditarPerfil(MouseEvent event) throws IOException {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/Interfaz_Editar_Perfil.fxml"));

			Controlador_Interfaz_Editar_Perfil control = new Controlador_Interfaz_Editar_Perfil();

			loader.setController(control);

			Parent root = loader.load();
			Stage primaryStage = new Stage();
			primaryStage.setScene(new Scene(root));
			primaryStage.show();

			Stage ventatnaActual = (Stage) btnRelaciones.getScene().getWindow();
			ventatnaActual.hide();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}