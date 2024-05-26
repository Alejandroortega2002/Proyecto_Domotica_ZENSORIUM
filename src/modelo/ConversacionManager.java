package modelo;

import entidades.Conversacion;
import entidades.Nodo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import applications.ConexionDB;

public class ConversacionManager {
	private static Conversacion converSeleccionada;

	public static Conversacion getConverSeleccionada() {
		return converSeleccionada;
	}

	public static void setConverSeleccionada(Conversacion conver) {
		converSeleccionada = conver;
	}

	public static ListaEnlazada<Conversacion> cargarConversaciones(long idUsuario) {
		ListaEnlazada<Conversacion> conversaciones = new ListaEnlazada<>();
		String query = "SELECT * FROM conversaciones WHERE id_e = ? OR id_r = ?";
		try (Connection conexion = ConexionDB.obtenerConexion();
				PreparedStatement stmt = conexion.prepareStatement(query)) {
			stmt.setLong(1, idUsuario);
			stmt.setLong(2, idUsuario);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					long idConversacion = rs.getLong("id_conversacion");
					long idEmisor = rs.getLong("id_e");
					long idReceptor = rs.getLong("id_r");
					String texto = rs.getString("texto");
					Date fechaEnvio = rs.getTimestamp("fecha_envio");

					Conversacion conversacion = new Conversacion(idConversacion, idEmisor, idReceptor, texto,
							fechaEnvio);
					conversaciones.insertarAlFinal(new Nodo<>(conversacion));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conversaciones;
	}

	public static boolean enviarMensaje(long idEmisor, long idReceptor, String mensaje, Date fechaEnvio) {
		String query = "INSERT INTO conversaciones (id_e, id_r, texto, fecha_envio) VALUES (?, ?, ?, ?)";
		try (Connection conexion = ConexionDB.obtenerConexion();
				PreparedStatement stmt = conexion.prepareStatement(query)) {
			stmt.setLong(1, idEmisor);
			stmt.setLong(2, idReceptor);
			stmt.setString(3, mensaje);
			stmt.setTimestamp(4, new Timestamp(fechaEnvio.getTime())); // Convertir la fecha a un Timestamp
			int rowsAffected = stmt.executeUpdate();
			return rowsAffected > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static String obtenerConversacionCompleta(long idUsuarioLogueado, long idUsuarioSeleccionado) {
		StringBuilder conversacionCompleta = new StringBuilder();
		String query = "SELECT c.texto, c.fecha_envio, u1.username AS emisor, u2.username AS receptor "
				+ "FROM conversaciones c " + "JOIN usuario u1 ON c.id_e = u1.id_user "
				+ "JOIN usuario u2 ON c.id_r = u2.id_user "
				+ "WHERE (c.id_e = ? AND c.id_r = ?) OR (c.id_e = ? AND c.id_r = ?) " + "ORDER BY c.fecha_envio";
		try (Connection conexion = ConexionDB.obtenerConexion();
				PreparedStatement stmt = conexion.prepareStatement(query)) {
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
}
