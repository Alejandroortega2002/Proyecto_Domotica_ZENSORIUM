package modelo;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import entidades.Dispositivos;
import entidades.Nodo;
import entidades.Relaciones;
import entidades.Sensores;
import applications.ConexionDB;
import java.sql.*;

import javax.swing.JOptionPane;

import java.io.*;
import java.lang.reflect.Type;

public class DispositivosManager {
	private static Dispositivos dispositivoSeleccionado;

	public static ListaEnlazada<Dispositivos> cargarDispos() {
		ListaEnlazada<Dispositivos> dispositivos = new ListaEnlazada<>();
		String query = "SELECT * FROM dispositivos";
		try (Connection conexion = ConexionDB.obtenerConexion();
				PreparedStatement stmt = conexion.prepareStatement(query);
				ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				Dispositivos dispo = new Dispositivos(rs.getLong("id_dispo"), rs.getLong("id_sensor"),
						rs.getLong("id_usu_relacionado"), rs.getBoolean("estado"), rs.getString("Tipo"),
						rs.getString("Nombre"));
				dispositivos.insertarAlFinal(new Nodo<>(dispo));
			}
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "Error al cargar dispositivos: " + ex.getMessage());
		}
		return dispositivos;
	}

	public static void guardarDispositivos(ListaEnlazada<Dispositivos> dispos) {
		String query = "REPLACE INTO dispositivos (id_dispo, estado, Nombre) VALUES (?, ?, ?)";
		try (Connection conexion = ConexionDB.obtenerConexion();
				PreparedStatement stmt = conexion.prepareStatement(query)) {
			for (Nodo<Dispositivos> nodo = dispos.getCabeza(); nodo != null; nodo = nodo.getEnlace()) {
				Dispositivos dispo = nodo.getDato();
				stmt.setLong(1, dispo.getId_dispo());
				stmt.setString(2, dispo.getNombre());
				stmt.setBoolean(3, dispo.isEstado());
				stmt.executeUpdate();
			}
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "Error al guardar dispositivos: " + ex.getMessage());
		}
	}

	public static boolean registrarDispos(Dispositivos nuevoDispo) {
		ListaEnlazada<Dispositivos> dispos = cargarDispos();
		for (Nodo<Dispositivos> nodo = dispos.getCabeza(); nodo != null; nodo = nodo.getEnlace()) {
			if (nodo.getDato().getNombre().equals(nuevoDispo.getNombre())) {
				return false; // Dispositivo ya existe
			}
		}
		dispos.insertarAlFinal(new Nodo<>(nuevoDispo));
		guardarDispositivos(dispos);
		return true;
	}

	public static boolean eliminarDispositivo(String nombreDispositivo) {
		ListaEnlazada<Dispositivos> dispositivos = cargarDispos();
		Nodo<Dispositivos> actual = dispositivos.getCabeza();
		Nodo<Dispositivos> previo = null;
		while (actual != null) {
			if (actual.getDato().getNombre().equals(nombreDispositivo)) {
				if (previo == null) {
					dispositivos.setCabeza(actual.getEnlace());
				} else {
					previo.setEnlace(actual.getEnlace());
				}
				guardarDispositivos(dispositivos);
				return true;
			}
			previo = actual;
			actual = actual.getEnlace();
		}
		return false;
	}

	public static long obtenerNuevoId() {
		ListaEnlazada<Dispositivos> dispos = cargarDispos();
		long idMasAlto = 0;
		for (Nodo<Dispositivos> nodo = dispos.getCabeza(); nodo != null; nodo = nodo.getEnlace()) {
			long id = nodo.getDato().getId_dispo();
			if (id > idMasAlto) {
				idMasAlto = id;
			}
		}
		return idMasAlto + 1;
	}

	public static boolean modificarDispositivo(String nombreDispositivo, String nombreNuevoDispositivo) {
		ListaEnlazada<Dispositivos> dispositivos = cargarDispos();
		for (Nodo<Dispositivos> nodo = dispositivos.getCabeza(); nodo != null; nodo = nodo.getEnlace()) {
			Dispositivos dispo = nodo.getDato();
			if (dispo.getNombre().equals(nombreDispositivo)) {
				dispo.setNombre(nombreNuevoDispositivo);
				guardarDispositivos(dispositivos);
				return true;
			}
		}
		return false;
	}

	public static boolean modificarEstadoDispositivo(String nombreDispositivo, Boolean estadoDispositivo) {
		ListaEnlazada<Dispositivos> dispositivos = cargarDispos();
		for (Nodo<Dispositivos> nodo = dispositivos.getCabeza(); nodo != null; nodo = nodo.getEnlace()) {
			Dispositivos dispo = nodo.getDato();
			if (dispo.getNombre().equals(nombreDispositivo)) {
				dispo.setEstado(estadoDispositivo);
				guardarDispositivos(dispositivos);
				return true;
			}
		}
		return false;
	}

	public static Dispositivos getDispositivoSeleccionado() {
		return dispositivoSeleccionado;
	}

	public static void setDispositivoSeleccionado(Dispositivos dispositivo) {
		dispositivoSeleccionado = dispositivo;
	}

	public static ListaEnlazada<Sensores> cargarSensores() {
		ListaEnlazada<Sensores> sensores = new ListaEnlazada<>();
		String query = "SELECT * FROM sensores";
		try (Connection conexion = ConexionDB.obtenerConexion();
				PreparedStatement stmt = conexion.prepareStatement(query);
				ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				Sensores sensor = new Sensores(rs.getLong("id_sensor"), rs.getFloat("dato_actual"),
						rs.getString("nombre"), rs.getString("tipo"), rs.getLong("orden_registro"));
				sensores.insertarAlFinal(new Nodo<>(sensor));
			}
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "Error al cargar sensores: " + ex.getMessage());
		}
		return sensores;
	}

	public static void guardarSensores(ListaEnlazada<Sensores> sensores) {
		String query = "REPLACE INTO sensores (id_sensor, dato_actual, nombre, tipo, orden_registro) VALUES (?, ?, ?, ?, ?)";
		try (Connection conexion = ConexionDB.obtenerConexion();
				PreparedStatement stmt = conexion.prepareStatement(query)) {
			for (Nodo<Sensores> nodo = sensores.getCabeza(); nodo != null; nodo = nodo.getEnlace()) {
				Sensores sensor = nodo.getDato();
				stmt.setLong(1, sensor.getId_sensor());
				stmt.setFloat(2, sensor.getDato_actual());
				stmt.setString(3, sensor.getNombre());
				stmt.setString(4, sensor.getTipo());
				stmt.setLong(5, sensor.getOrden_registro());
				stmt.executeUpdate();
			}
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "Error al guardar sensores: " + ex.getMessage());
		}
	}

	public static long crearSensor(float dato_actual, String nombre, String tipo) {
		// Asumiendo que cada nuevo sensor empieza con un orden_registro de 1
		String query = "INSERT INTO sensores (dato_actual, nombre, tipo, orden_registro) VALUES (?, ?, ?, 1)";
		long idGenerado = 0;
		try (Connection conexion = ConexionDB.obtenerConexion();
				PreparedStatement stmt = conexion.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
			stmt.setFloat(1, dato_actual);
			stmt.setString(2, nombre);
			stmt.setString(3, tipo);
			int affectedRows = stmt.executeUpdate();
			if (affectedRows == 0) {
				throw new SQLException("Creación del sensor falló, no se afectaron filas.");
			}
			try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					idGenerado = generatedKeys.getLong(1);
				}
			}
		} catch (SQLException ex) {
			System.err.println("Error al crear sensor: " + ex.getMessage());
		}
		return idGenerado;
	}

	public static void crearSensorConId(long id_sensor, float dato_actual, String nombre, String tipo) {
		// Consultar primero el máximo orden_registro para el id_sensor dado
		String getMaxOrden = "SELECT COALESCE(MAX(orden_registro), 0) + 1 AS next_orden FROM sensores WHERE id_sensor = ?";
		String insertQuery = "INSERT INTO sensores (id_sensor, dato_actual, nombre, tipo, orden_registro) VALUES (?, ?, ?, ?, ?)";

		try (Connection conexion = ConexionDB.obtenerConexion();
				PreparedStatement getMaxStmt = conexion.prepareStatement(getMaxOrden);
				PreparedStatement insertStmt = conexion.prepareStatement(insertQuery)) {

			// Obtener el próximo orden_registro
			getMaxStmt.setLong(1, id_sensor);
			ResultSet rs = getMaxStmt.executeQuery();
			int nextOrden = 0;
			if (rs.next()) {
				nextOrden = rs.getInt("next_orden");
			}

			// Insertar el nuevo registro con el orden_registro incrementado
			insertStmt.setLong(1, id_sensor);
			insertStmt.setFloat(2, dato_actual);
			insertStmt.setString(3, nombre);
			insertStmt.setString(4, tipo);
			insertStmt.setInt(5, nextOrden);
			int affectedRows = insertStmt.executeUpdate();
			if (affectedRows == 0) {
				throw new SQLException("Inserción de datos del sensor falló, no se afectaron filas.");
			}
		} catch (SQLException ex) {
			System.err.println("Error al insertar datos para el sensor con ID: " + id_sensor + " - " + ex.getMessage());
		}
	}

	public static ListaEnlazada<Dispositivos> cargarDispositivosPorUsuario(long idUsuario) {
		ListaEnlazada<Dispositivos> dispositivosDelUsuario = new ListaEnlazada<>();
		String query = "SELECT * FROM dispositivos WHERE id_usu_relacionado = ?";
		try (Connection conexion = ConexionDB.obtenerConexion();
				PreparedStatement stmt = conexion.prepareStatement(query)) {
			stmt.setLong(1, idUsuario);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Dispositivos dispositivo = new Dispositivos(rs.getLong("id_dispo"), rs.getLong("id_sensor"),
						rs.getLong("id_usu_relacionado"), rs.getBoolean("estado"), rs.getString("Tipo"),
						rs.getString("Nombre"));
				dispositivosDelUsuario.insertarAlFinal(new Nodo<>(dispositivo));
			}
		} catch (SQLException ex) {
			System.err.println("Error al cargar dispositivos por usuario: " + ex.getMessage());
		}
		return dispositivosDelUsuario;
	}

	public static ListaEnlazada<Dispositivos> cargarDispositivosRelacionados(long idUsuario) {
		ListaEnlazada<Dispositivos> dispositivosRelacionados = new ListaEnlazada<>();
		ListaEnlazada<Relaciones> relacionesDelUsuario = RegistroManager.cargarRelacionesPorUsuario(idUsuario);
		for (Nodo<Relaciones> nodoRel = relacionesDelUsuario.getCabeza(); nodoRel != null; nodoRel = nodoRel
				.getEnlace()) {
			Relaciones relacion = nodoRel.getDato();
			long idRelacionado = (relacion.getTu_id() == idUsuario) ? relacion.getId_user_relacion()
					: relacion.getTu_id();
			String query = "SELECT * FROM dispositivos WHERE id_usuario = ?";
			try (Connection conexion = ConexionDB.obtenerConexion();
					PreparedStatement stmt = conexion.prepareStatement(query)) {
				stmt.setLong(1, idRelacionado);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					Dispositivos dispositivo = new Dispositivos(rs.getLong("id_dispo"), rs.getLong("id_sensor"),
							rs.getLong("id_usu_relacionado"), rs.getBoolean("estado"), rs.getString("Tipo"),
							rs.getString("Nombre"));
					dispositivosRelacionados.insertarAlFinal(new Nodo<>(dispositivo));
				}
			} catch (SQLException ex) {
				System.err.println("Error al cargar dispositivos relacionados: " + ex.getMessage());
			}
		}
		return dispositivosRelacionados;
	}

}