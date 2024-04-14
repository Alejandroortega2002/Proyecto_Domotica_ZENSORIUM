package modelo;

import com.google.gson.Gson;

import com.google.gson.reflect.TypeToken;

import entidades.Reporte;
import entidades.Usuario;
import entidades.Nodo;
import applications.ConexionDB;
import java.sql.*;
import java.util.Date;

import javax.swing.JOptionPane;

import java.io.*;
import java.lang.reflect.Type;

public class ReporteManager {
	private static Reporte reporteSeleccionado;

	public static ListaEnlazada<Reporte> cargarReportes() {
		ListaEnlazada<Reporte> reportes = new ListaEnlazada<>();
		String query = "SELECT * FROM reportes"; // Asegúrate de que 'reportes' es el nombre correcto de tu tabla
		try (Connection conexion = ConexionDB.obtenerConexion(); // Reemplaza 'ConexionDB.obtenerConexion()' con tu
																	// método de conexión
				Statement stmt = conexion.createStatement();
				ResultSet rs = stmt.executeQuery(query)) {
			while (rs.next()) {
				long idReporte = rs.getLong("id_reporte");
				long idUserEmisor = rs.getLong("id_user_emisor");
				long idUserReceptor = rs.getLong("id_user_receptor");
				String titulo = rs.getString("titulo");
				String queja = rs.getString("queja");
				Date fecha = new Date(rs.getTimestamp("fecha").getTime()); // Asegúrate de convertir la fecha
																			// correctamente

				Reporte reporte = new Reporte(idReporte, idUserEmisor, idUserReceptor, titulo, queja, fecha);
				reportes.insertarAlFinal(new Nodo<>(reporte));
			}
		} catch (SQLException e) {
			e.printStackTrace(); // O maneja la excepción como prefieras
		}
		return reportes;
	}

	public static void guardarReporte(ListaEnlazada<Reporte> reportes) {
		// Suponiendo que tienes un método estático obtenerConexion() que te da una
		// conexión a la base de datos.
		String insertQuery = "INSERT INTO reportes (id_reporte, id_user_emisor, id_user_receptor, titulo, queja, fecha) VALUES (?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE id_user_emisor=?, id_user_receptor=?, titulo=?, queja=?, fecha=?";
		try (Connection conexion = ConexionDB.obtenerConexion();
				PreparedStatement stmt = conexion.prepareStatement(insertQuery)) {

			for (Nodo<Reporte> nodo = reportes.getCabeza(); nodo != null; nodo = nodo.getEnlace()) {
				Reporte reporte = nodo.getDato();
				stmt.setLong(1, reporte.getId_reporte());
				stmt.setLong(2, reporte.getId_user_emisor());
				stmt.setLong(3, reporte.getId_user_receptor());
				stmt.setString(4, reporte.getTitulo());
				stmt.setString(5, reporte.getQueja());
				stmt.setDate(6, new java.sql.Date(reporte.getFecha().getTime()));

				// Establecer los valores para la cláusula UPDATE también
				stmt.setLong(7, reporte.getId_user_emisor());
				stmt.setLong(8, reporte.getId_user_receptor());
				stmt.setString(9, reporte.getTitulo());
				stmt.setString(10, reporte.getQueja());
				stmt.setDate(11, new java.sql.Date(reporte.getFecha().getTime()));

				stmt.executeUpdate();
			}
		} catch (SQLException ex) {
			ex.printStackTrace(); // O maneja la excepción como prefieras
		}
	}

	public static Reporte getReporteSeleccionado() {
		return reporteSeleccionado;
	}

	public static void setReporteSeleccionado(Reporte reporte) {
		reporteSeleccionado = reporte;
	}

	public static boolean crearReporte(Reporte nuevoReporte) {
		String insertQuery = "INSERT INTO reportes (id_user_emisor, id_user_receptor, titulo, queja, fecha) VALUES (?, ?, ?, ?, ?)";

		try (Connection conexion = ConexionDB.obtenerConexion();
				PreparedStatement stmt = conexion.prepareStatement(insertQuery)) {
			stmt.setLong(1, nuevoReporte.getId_user_emisor());
			stmt.setLong(2, nuevoReporte.getId_user_receptor());
			stmt.setString(3, nuevoReporte.getTitulo());
			stmt.setString(4, nuevoReporte.getQueja());
			// La fecha se debe convertir de java.util.Date a java.sql.Date
			stmt.setDate(5, new java.sql.Date(nuevoReporte.getFecha().getTime()));

			int affectedRows = stmt.executeUpdate();
			if (affectedRows > 0) {
				return true; // El reporte fue creado exitosamente
			}
		} catch (SQLException ex) {
			ex.printStackTrace(); // Maneja la excepción como prefieras
		}
		return false; // Retorna false si la creación del reporte falló
	}

	public static String sacarNombreDeId(Long idUsuario) {
		String query = "SELECT username FROM usuarios WHERE id_user = ?";
		try (Connection conexion = ConexionDB.obtenerConexion();
				PreparedStatement stmt = conexion.prepareStatement(query)) {

			stmt.setLong(1, idUsuario);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				return rs.getString("username");
			}
		} catch (SQLException ex) {
			ex.printStackTrace(); // O maneja la excepción como prefieras
		}
		return ""; // Retorna un string vacío si el usuario no se encuentra
	}

	public static boolean eliminarReporte(String tituloReporte) {
		String deleteQuery = "DELETE FROM reportes WHERE titulo = ?";
		try (Connection conexion = ConexionDB.obtenerConexion();
				PreparedStatement stmt = conexion.prepareStatement(deleteQuery)) {

			stmt.setString(1, tituloReporte);
			int affectedRows = stmt.executeUpdate();

			return affectedRows > 0; // Retorna true si se eliminó al menos un reporte
		} catch (SQLException ex) {
			ex.printStackTrace(); // Maneja la excepción como prefieras
		}
		return false; // Retorna false si no se pudo eliminar o si ocurrió un error
	}

}