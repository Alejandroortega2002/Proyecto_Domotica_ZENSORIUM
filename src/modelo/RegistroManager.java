package modelo;

import entidades.Nodo;
import entidades.Relaciones;
import entidades.Usuario;
import java.sql.*;
import javax.swing.JOptionPane;

import org.mindrot.jbcrypt.BCrypt;

import applications.ConexionDB;

public class RegistroManager {

	// Métodos para manejar usuarios
	public static ListaEnlazada<Usuario> cargarUsuarios() {
		ListaEnlazada<Usuario> usuarios = new ListaEnlazada<>();
		String query = "SELECT * FROM usuario";
		try (Connection conexion = ConexionDB.obtenerConexion();
				PreparedStatement stmt = conexion.prepareStatement(query)) {
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Usuario usuario = new Usuario(rs.getLong("id_user"), rs.getString("username"), rs.getString("email"),
						rs.getString("password"), rs.getString("tipodecuenta"));
				usuarios.insertarAlFinal(new Nodo<>(usuario));
			}
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "Error al cargar usuarios: " + ex.getMessage());
		}
		return usuarios;
	}

	public static void guardarUsuario(Usuario usuario) {
		// Encriptar la contraseña utilizando jBCrypt
		String encryptedPassword = BCrypt.hashpw(usuario.getPassword(), BCrypt.gensalt());

		String query = "INSERT INTO usuario (id_user, username, email, password, tipodecuenta) "
				+ "VALUES (?, ?, ?, ?, ?) " + "ON DUPLICATE KEY UPDATE username=VALUES(username), email=VALUES(email), "
				+ "password=VALUES(password), tipodecuenta=VALUES(tipodecuenta)";

		try (Connection conexion = ConexionDB.obtenerConexion();
				PreparedStatement stmt = conexion.prepareStatement(query)) {
			stmt.setLong(1, usuario.getId_user());
			stmt.setString(2, usuario.getUsername());
			stmt.setString(3, usuario.getEmail());
			stmt.setString(4, encryptedPassword);
			stmt.setString(5, usuario.getTipodecuenta());
			stmt.executeUpdate();
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "Error al guardar usuario: " + ex.getMessage());
		}
	}

	public static boolean verificarUsuario(String email, String password) {
		String query = "SELECT password FROM usuario WHERE email = ?";

		try (Connection conexion = ConexionDB.obtenerConexion();
				PreparedStatement stmt = conexion.prepareStatement(query)) {
			stmt.setString(1, email);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					String storedHashedPassword = rs.getString("password");
					// Verificar la contraseña utilizando BCrypt
					return BCrypt.checkpw(password, storedHashedPassword);
				}
			}
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "Error al verificar usuario: " + ex.getMessage());
		}
		return false; // Usuario no encontrado o contraseña incorrecta
	}

	public static boolean registrarUsuario(Usuario nuevoUsuario) {
		String query = "SELECT * FROM usuario WHERE username = ? OR email = ?";
		try (Connection conexion = ConexionDB.obtenerConexion();
				PreparedStatement stmt = conexion.prepareStatement(query)) {
			stmt.setString(1, nuevoUsuario.getUsername());
			stmt.setString(2, nuevoUsuario.getEmail());
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return false; // Usuario ya existe
			}
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "Error al registrar usuario: " + ex.getMessage());
			return false;
		}
		guardarUsuario(nuevoUsuario);
		return true;
	}

	public static long obtenerNuevoId() {
		String query = "SELECT MAX(id_user) AS max_id FROM usuario";
		try (Connection conexion = ConexionDB.obtenerConexion();
				PreparedStatement stmt = conexion.prepareStatement(query)) {
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getLong("max_id") + 1;
			}
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "Error al obtener nuevo ID: " + ex.getMessage());
		}
		return 1; // Si no hay usuarios, el primer ID será 1
	}

	// Método para cargar relaciones en una lista enlazada
	public static ListaEnlazada<Relaciones> cargarRelaciones() {
		ListaEnlazada<Relaciones> relaciones = new ListaEnlazada<>();
		String query = "SELECT * FROM relaciones";
		try (Connection conexion = ConexionDB.obtenerConexion();
				PreparedStatement stmt = conexion.prepareStatement(query)) {
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Relaciones relacion = new Relaciones(rs.getLong("id_user_logeado"), rs.getLong("id_user_relacion"),
						rs.getString("tipo_relacion"));
				relaciones.insertarAlFinal(new Nodo<>(relacion));
			}
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "Error al cargar relaciones: " + ex.getMessage());
		}
		return relaciones;
	}

	// Método para verificar si una relación ya existe en la base de datos
	public static boolean relacionExiste(long idUserLogeado, long idUserRelacion) {
		String query = "SELECT * FROM relaciones WHERE (id_user_logeado = ? AND id_user_relacion = ?) OR (id_user_logeado = ? AND id_user_relacion = ?)";
		try (Connection conexion = ConexionDB.obtenerConexion();
				PreparedStatement stmt = conexion.prepareStatement(query)) {
			stmt.setLong(1, idUserLogeado);
			stmt.setLong(2, idUserRelacion);
			stmt.setLong(3, idUserRelacion);
			stmt.setLong(4, idUserLogeado);
			ResultSet rs = stmt.executeQuery();
			return rs.next();
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "Error al verificar relación: " + ex.getMessage());
		}
		return false;
	}

	// Método para agregar una nueva relación entre usuarios
	public static boolean guardarRelacion(Relaciones nuevaRelacion) {
		if (!relacionExiste(nuevaRelacion.getTu_id(), nuevaRelacion.getId_user_relacion())) {
			String query = "INSERT INTO relaciones (id_user_logeado, id_user_relacion, tipo_relacion) VALUES (?, ?, ?)";
			try (Connection conexion = ConexionDB.obtenerConexion();
					PreparedStatement stmt = conexion.prepareStatement(query)) {
				stmt.setLong(1, nuevaRelacion.getTu_id());
				stmt.setLong(2, nuevaRelacion.getId_user_relacion());
				stmt.setString(3, nuevaRelacion.getTipo_relacion());
				int result = stmt.executeUpdate();
				return result > 0;
			} catch (SQLException ex) {
				JOptionPane.showMessageDialog(null, "Error al guardar relación: " + ex.getMessage());
			}
		}
		return false;
	}

	// Método para verificar si una relación ya existe
	public static boolean verificarRelacionExistente(long idUsuario1, long idUsuario2) {
		return relacionExiste(idUsuario1, idUsuario2);
	}

	// Métodos adicionales para la gestión de usuarios y relaciones
	public static boolean modificarUsuario(Long idUsuario, String nombreNuevoUsuario, String correoNuevoUsuario) {
		String query = "UPDATE usuario SET username = ?, email = ? WHERE id_user = ?";
		try (Connection conexion = ConexionDB.obtenerConexion();
				PreparedStatement stmt = conexion.prepareStatement(query)) {
			stmt.setString(1, nombreNuevoUsuario);
			stmt.setString(2, correoNuevoUsuario);
			stmt.setLong(3, idUsuario);
			int result = stmt.executeUpdate();
			return result > 0;
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "Error al modificar usuario: " + ex.getMessage());
		}
		return false;
	}

	public static boolean actualizarPassword(Long idUsuario, String nuevaPass) {
		String query = "UPDATE usuario SET password = ? WHERE id_user = ?";
		try (Connection conexion = ConexionDB.obtenerConexion();
				PreparedStatement stmt = conexion.prepareStatement(query)) {

			// Encriptar la nueva contraseña
			String nuevaPassEncriptada = BCrypt.hashpw(nuevaPass, BCrypt.gensalt());

			stmt.setString(1, nuevaPassEncriptada);
			stmt.setLong(2, idUsuario);
			int result = stmt.executeUpdate();
			return result > 0;
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "Error al actualizar contraseña: " + ex.getMessage());
		}
		return false;
	}

	public static Usuario buscarUsuarioPorId(long idUsuario) {
		String query = "SELECT * FROM usuario WHERE id_user = ?";
		try (Connection conexion = ConexionDB.obtenerConexion();
				PreparedStatement stmt = conexion.prepareStatement(query)) {
			stmt.setLong(1, idUsuario);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return new Usuario(rs.getLong("id_user"), rs.getString("username"), rs.getString("email"),
						rs.getString("password"), rs.getString("tipodecuenta"));
			}
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "Error al buscar usuario: " + ex.getMessage());
		}
		return null;
	}

	public static ListaEnlazada<Relaciones> cargarRelacionesPorUsuario(long idUsuario) {
		ListaEnlazada<Relaciones> relacionesDelUsuario = new ListaEnlazada<>();
		String query = "SELECT * FROM relaciones WHERE id_user_logeado = ? OR id_user_relacion = ?";
		try (Connection conexion = ConexionDB.obtenerConexion();
				PreparedStatement stmt = conexion.prepareStatement(query)) {
			stmt.setLong(1, idUsuario);
			stmt.setLong(2, idUsuario);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Relaciones relacion = new Relaciones(rs.getLong("id_user_logeado"), rs.getLong("id_user_relacion"),
						rs.getString("tipo_relacion"));
				relacionesDelUsuario.insertarAlFinal(new Nodo<>(relacion));
			}
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "Error al cargar relaciones para el usuario: " + ex.getMessage());
		}
		return relacionesDelUsuario;
	}
}
