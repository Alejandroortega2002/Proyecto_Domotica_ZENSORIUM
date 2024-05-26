package modelo;

import entidades.Dispositivos;
import entidades.Nodo;
import entidades.Sensores;
import applications.ConexionDB;
import java.sql.*;
import javax.swing.JOptionPane;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;

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

	public static ListaEnlazada<Dispositivos> cargarDispositivosPorUsuario(long idUsuario) {
		ListaEnlazada<Dispositivos> dispositivos = new ListaEnlazada<>();
		String query = "SELECT * FROM dispositivos WHERE id_usu_relacionado = ?";
		try (Connection conexion = ConexionDB.obtenerConexion();
				PreparedStatement stmt = conexion.prepareStatement(query)) {
			stmt.setLong(1, idUsuario);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Dispositivos dispo = new Dispositivos(rs.getLong("id_dispo"), rs.getLong("id_sensor"),
						rs.getLong("id_usu_relacionado"), rs.getBoolean("estado"), rs.getString("Tipo"),
						rs.getString("Nombre"));
				dispositivos.insertarAlFinal(new Nodo<>(dispo));
			}
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "Error al cargar dispositivos por usuario: " + ex.getMessage());
		}
		return dispositivos;
	}

	public static void guardarDispositivo(Dispositivos dispo) {
		String query = "INSERT INTO dispositivos (id_dispo, id_sensor, id_usu_relacionado, estado, tipo, nombre) "
				+ "VALUES (?, ?, ?, ?, ?, ?) "
				+ "ON DUPLICATE KEY UPDATE id_sensor=VALUES(id_sensor), id_usu_relacionado=VALUES(id_usu_relacionado), "
				+ "estado=VALUES(estado), tipo=VALUES(tipo), nombre=VALUES(nombre)";
		try (Connection conexion = ConexionDB.obtenerConexion();
				PreparedStatement stmt = conexion.prepareStatement(query)) {
			stmt.setLong(1, dispo.getId_dispo());
			stmt.setLong(2, dispo.getId_sensor());
			stmt.setLong(3, dispo.getId_usu_relacionado());
			stmt.setBoolean(4, dispo.isEstado());
			stmt.setString(5, dispo.getTipo());
			stmt.setString(6, dispo.getNombre());
			stmt.executeUpdate();
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "Error al guardar dispositivo: " + ex.getMessage());
		}
	}

	public static void guardarSensor(Sensores sensor) {
		String query = "INSERT INTO sensores (id_sensor, descripcion, tipo) " + "VALUES (?, ?, ?) "
				+ "ON DUPLICATE KEY UPDATE descripcion=VALUES(descripcion), tipo=VALUES(tipo)";
		try (Connection conexion = ConexionDB.obtenerConexion();
				PreparedStatement stmt = conexion.prepareStatement(query)) {
			stmt.setLong(1, sensor.getId_sensor());
			stmt.setString(2, sensor.getDescripcion());
			stmt.setString(3, sensor.getTipo());
			stmt.executeUpdate();
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "Error al guardar sensor: " + ex.getMessage());
		}
	}

	public static Sensores buscarSensorPorId(long id_sensor) {
		String query = "SELECT id_sensor, descripcion, tipo FROM sensores WHERE id_sensor = ?";
		try (Connection conexion = ConexionDB.obtenerConexion();
				PreparedStatement stmt = conexion.prepareStatement(query)) {
			stmt.setLong(1, id_sensor);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				long id = rs.getLong("id_sensor");
				String descripcion = rs.getString("descripcion");
				String tipo = rs.getString("tipo");
				return new Sensores(id, descripcion, tipo);
			} else {
				return null; // Sensor no encontrado
			}
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "Error al buscar sensor: " + ex.getMessage());
			return null;
		}
	}

	public static ObservableList<XYChart.Data<String, Number>> cargarDatosEnGrafico(long sensorId) {
		ObservableList<XYChart.Data<String, Number>> datos = FXCollections.observableArrayList();
		String query = "SELECT valor_dato, fecha FROM historicodatos WHERE id_sensor = ? ORDER BY fecha DESC LIMIT 20";

		try (Connection conexion = ConexionDB.obtenerConexion();
				PreparedStatement stmt = conexion.prepareStatement(query)) {

			stmt.setLong(1, sensorId); // Establece el ID del sensor para la consulta
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				String valorDato = rs.getString("valor_dato");
				String fecha = rs.getString("fecha");
				double valorNumerico = Double.parseDouble(valorDato.replaceAll("[^0-9.]", ""));

				// Asegurarse de que los valores están en el rango 0-100
				if (valorNumerico >= 0 && valorNumerico <= 100) {
					datos.add(new XYChart.Data<>(fecha, valorNumerico));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			// Manejo adicional del error o log
		} catch (NullPointerException e) {
			e.printStackTrace();
			// Manejo adicional del error o log
		}
		return datos;
	}

	public static ObservableList<Sensores> cargarDatosEnTabla(long sensorId) {
		ObservableList<Sensores> historicoDatos = FXCollections.observableArrayList();
		String query = "SELECT id_dato, valor_dato FROM historicodatos WHERE id_sensor = ? ORDER BY fecha DESC LIMIT 20";

		try (Connection conexion = ConexionDB.obtenerConexion();
				PreparedStatement stmt = conexion.prepareStatement(query)) {

			stmt.setLong(1, sensorId); // Establece el ID del sensor para la consulta
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				long idDato = rs.getLong("id_dato");
				String valorDato = rs.getString("valor_dato");
				double valorNumerico = Double.parseDouble(valorDato.replaceAll("[^0-9.]", ""));

				// Crear un objeto Sensores temporal para cargar los datos históricos en la
				// tabla
				Sensores sensorHistorico = new Sensores(idDato, String.valueOf(valorNumerico), ""); // Uso temporal
				historicoDatos.add(sensorHistorico);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			// Manejo adicional del error o log
		} catch (NullPointerException e) {
			e.printStackTrace();
			// Manejo adicional del error o log
		}
		return historicoDatos;
	}

	public static boolean registrarDispos(Dispositivos nuevoDispo) {
		if (!existeDispositivo(nuevoDispo.getNombre())) {
			guardarDispositivo(nuevoDispo);
			return true;
		} else {
			return false; // Dispositivo ya existe
		}
	}

	private static boolean existeDispositivo(String nombre) {
		String query = "SELECT 1 FROM dispositivos WHERE nombre = ?";
		try (Connection conexion = ConexionDB.obtenerConexion();
				PreparedStatement stmt = conexion.prepareStatement(query)) {
			stmt.setString(1, nombre);
			ResultSet rs = stmt.executeQuery();
			return rs.next();
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "Error al verificar dispositivo: " + ex.getMessage());
			return false;
		}
	}

	public static boolean eliminarDispositivo(String nombreDispositivo) {
		String queryDispositivo = "SELECT id_sensor FROM dispositivos WHERE nombre = ?";
		String deleteDispositivo = "DELETE FROM dispositivos WHERE nombre = ?";
		String deleteSensor = "DELETE FROM sensores WHERE id_sensor = ?";
		long idSensor = -1;
		boolean isDeleted = false;

		try (Connection conexion = ConexionDB.obtenerConexion();
				PreparedStatement stmtDispositivo = conexion.prepareStatement(queryDispositivo);
				PreparedStatement stmtDeleteDispositivo = conexion.prepareStatement(deleteDispositivo);
				PreparedStatement stmtDeleteSensor = conexion.prepareStatement(deleteSensor)) {

			// Primero obtener el id del sensor asociado con el dispositivo
			stmtDispositivo.setString(1, nombreDispositivo);
			ResultSet rs = stmtDispositivo.executeQuery();
			if (rs.next()) {
				idSensor = rs.getLong("id_sensor");
			}

			// Eliminar el dispositivo
			stmtDeleteDispositivo.setString(1, nombreDispositivo);
			int affectedRows = stmtDeleteDispositivo.executeUpdate();
			if (affectedRows > 0) {
				// Si se eliminó el dispositivo, ahora eliminar el sensor asociado
				stmtDeleteSensor.setLong(1, idSensor);
				affectedRows = stmtDeleteSensor.executeUpdate();
				isDeleted = affectedRows > 0;
			}
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "Error al eliminar el dispositivo y sensor: " + ex.getMessage());
		}

		return isDeleted;
	}

	public static long obtenerNuevoId() {
		String query = "SELECT MAX(id_dispo) AS max_id FROM dispositivos";
		try (Connection conexion = ConexionDB.obtenerConexion();
				PreparedStatement stmt = conexion.prepareStatement(query);
				ResultSet rs = stmt.executeQuery()) {
			if (rs.next()) {
				return rs.getLong("max_id") + 1;
			}
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "Error al obtener nuevo ID: " + ex.getMessage());
		}
		return 1; // Si no hay dispositivos, el primer ID será 1
	}

	public static boolean modificarDispositivo(String nombreDispositivo, String nombreNuevoDispositivo) {
		String query = "UPDATE dispositivos SET nombre = ? WHERE nombre = ?";
		try (Connection conexion = ConexionDB.obtenerConexion();
				PreparedStatement stmt = conexion.prepareStatement(query)) {
			stmt.setString(1, nombreNuevoDispositivo);
			stmt.setString(2, nombreDispositivo);
			int result = stmt.executeUpdate();
			return result > 0;
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "Error al modificar dispositivo: " + ex.getMessage());
		}
		return false;
	}

	public static boolean modificarEstadoDispositivo(String nombreDispositivo, Boolean estadoDispositivo) {
		String query = "UPDATE dispositivos SET estado = ? WHERE nombre = ?";
		try (Connection conexion = ConexionDB.obtenerConexion();
				PreparedStatement stmt = conexion.prepareStatement(query)) {
			stmt.setBoolean(1, estadoDispositivo);
			stmt.setString(2, nombreDispositivo);
			int result = stmt.executeUpdate();
			return result > 0;
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "Error al modificar estado del dispositivo: " + ex.getMessage());
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
				Sensores sensor = new Sensores(rs.getLong("id_sensor"), rs.getString("descripcion"),
						rs.getString("tipo"));
				sensores.insertarAlFinal(new Nodo<>(sensor));
			}
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "Error al cargar sensores: " + ex.getMessage());
		}
		return sensores;
	}

	public static long crearSensor(String descripcion, String tipo) {
		String query = "INSERT INTO sensores (descripcion, tipo) VALUES (?, ?)";
		try (Connection conexion = ConexionDB.obtenerConexion();
				PreparedStatement stmt = conexion.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
			stmt.setString(1, descripcion);
			stmt.setString(2, tipo);
			int affectedRows = stmt.executeUpdate();
			if (affectedRows == 0) {
				throw new SQLException("Creación del sensor falló, no se afectaron filas.");
			}

			// Obtener el ID generado
			try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					return generatedKeys.getLong(1);
				} else {
					throw new SQLException("Creación del sensor falló, no se obtuvo el ID.");
				}
			}
		} catch (SQLException ex) {
			System.err.println("Error al crear sensor: " + ex.getMessage());
			return -1;
		}
	}
}
