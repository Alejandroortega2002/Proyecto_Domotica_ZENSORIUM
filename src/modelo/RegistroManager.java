package modelo;

import entidades.Nodo;
import entidades.Relaciones;
import entidades.Usuario;
import java.sql.*;
import javax.swing.JOptionPane;

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
						rs.getString("password"), rs.getString("repetirPass"), rs.getString("tipodecuenta"));
				usuarios.insertarAlFinal(new Nodo<>(usuario));
			}
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "Error al cargar usuarios: " + ex.getMessage());
		}
		return usuarios;
	}

	public static void guardarUsuarios(ListaEnlazada<Usuario> usuarios) {
		try (Connection conexion = ConexionDB.obtenerConexion()) {
			String query = "INSERT INTO usuario (id_user, username, email, password, repetirPass, tipodecuenta) "
					+ "VALUES (?, ?, ?, ?, ?, ?) "
					+ "ON DUPLICATE KEY UPDATE username=VALUES(username), email=VALUES(email), "
					+ "password=VALUES(password), repetirPass=VALUES(repetirPass), tipodecuenta=VALUES(tipodecuenta)";
			PreparedStatement stmt = conexion.prepareStatement(query);
			Nodo<Usuario> actual = usuarios.getCabeza();
			while (actual != null) {
				Usuario usuario = actual.getDato();
				stmt.setLong(1, usuario.getId_user());
				stmt.setString(2, usuario.getUsername());
				stmt.setString(3, usuario.getEmail());
				stmt.setString(4, usuario.getPassword());
				stmt.setString(5, usuario.getRepetirPass());
				stmt.setString(6, usuario.getTipodecuenta());
				stmt.executeUpdate();
				actual = actual.getEnlace();
			}
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "Error al guardar usuarios: " + ex.getMessage());
		}
	}

	public static boolean registrarUsuario(Usuario nuevoUsuario) {
		ListaEnlazada<Usuario> usuarios = cargarUsuarios();
		for (Nodo<Usuario> nodoActual = usuarios.getCabeza(); nodoActual != null; nodoActual = nodoActual.getEnlace()) {
			Usuario usuario = nodoActual.getDato();
			if (usuario.getUsername().equals(nuevoUsuario.getUsername())
					|| usuario.getEmail().equals(nuevoUsuario.getEmail())) {
				return false; // Usuario ya existe
			}
		}
		usuarios.insertarAlFinal(new Nodo<>(nuevoUsuario));
		guardarUsuarios(usuarios);
		return true;
	}

	public static long obtenerNuevoId() {
		ListaEnlazada<Usuario> usuarios = cargarUsuarios();
		long idMasAlto = 0;
		for (Nodo<Usuario> nodoActual = usuarios.getCabeza(); nodoActual != null; nodoActual = nodoActual.getEnlace()) {
			Usuario usuario = nodoActual.getDato();
			if (usuario.getId_user() > idMasAlto) {
				idMasAlto = usuario.getId_user();
			}
		}
		return idMasAlto + 1;
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

	// Método para verificar si una relación ya existe en la lista enlazada
	private static boolean relacionExiste(ListaEnlazada<Relaciones> relaciones, Relaciones nuevaRelacion) {
		for (Nodo<Relaciones> nodo = relaciones.getCabeza(); nodo != null; nodo = nodo.getEnlace()) {
			Relaciones rel = nodo.getDato();
			if ((rel.getTu_id() == nuevaRelacion.getTu_id()
					&& rel.getId_user_relacion() == nuevaRelacion.getId_user_relacion())
					|| (rel.getTu_id() == nuevaRelacion.getId_user_relacion()
							&& rel.getId_user_relacion() == nuevaRelacion.getTu_id())) {
				return true;
			}
		}
		return false;
	}

	// Método para agregar una nueva relación entre usuarios
	public static boolean guardarRelacion(Relaciones nuevaRelacion) {
		ListaEnlazada<Relaciones> relaciones = cargarRelaciones();
		if (!relacionExiste(relaciones, nuevaRelacion)) {
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

	public static boolean verificarRelacionExistente(long idUsuario1, long idUsuario2) {
		ListaEnlazada<Relaciones> relacionesExistentes = cargarRelaciones();
		for (Nodo<Relaciones> nodoActual = relacionesExistentes.getCabeza(); nodoActual != null; nodoActual = nodoActual
				.getEnlace()) {
			Relaciones rel = nodoActual.getDato();
			if ((rel.getTu_id() == idUsuario1 && rel.getId_user_relacion() == idUsuario2)
					|| (rel.getTu_id() == idUsuario2 && rel.getId_user_relacion() == idUsuario1)) {
				return true;
			}
		}
		return false;
	}

	// Métodos adicionales para la gestión de usuarios y relaciones
	public static boolean modificarUsuario(Long idUsuario, String nombreNuevoUsuario, String correoNuevoUsuario) {
		ListaEnlazada<Usuario> usuarios = cargarUsuarios();
		boolean usuarioModificado = false;
		Nodo<Usuario> nodoActual = usuarios.getCabeza();

		while (nodoActual != null) {
			Usuario usuarioActual = nodoActual.getDato();
			if (usuarioActual.getId_user() == idUsuario) {
				usuarioActual.setUsername(nombreNuevoUsuario);
				usuarioActual.setEmail(correoNuevoUsuario);
				usuarioModificado = true;
				break;
			}
			nodoActual = nodoActual.getEnlace();
		}

		if (usuarioModificado) {
			guardarUsuarios(usuarios);
		}
		return usuarioModificado;
	}

	public static boolean actualizarPassword(Long idUsuario, String nuevaPass) {
		ListaEnlazada<Usuario> usuarios = cargarUsuarios();
		boolean passwordActualizado = false;
		Nodo<Usuario> nodoActual = usuarios.getCabeza();

		while (nodoActual != null) {
			Usuario usuarioActual = nodoActual.getDato();
			if (usuarioActual.getId_user() == idUsuario) {
				usuarioActual.setPassword(nuevaPass);
				usuarioActual.setRepetirPass(nuevaPass);
				passwordActualizado = true;
				break;
			}
			nodoActual = nodoActual.getEnlace();
		}

		if (passwordActualizado) {
			guardarUsuarios(usuarios);
		}
		return passwordActualizado;
	}

	public static Usuario buscarUsuarioPorId(long idUsuario) {
		ListaEnlazada<Usuario> usuarios = cargarUsuarios();
		Nodo<Usuario> nodoActual = usuarios.getCabeza();
		while (nodoActual != null) {
			Usuario usuario = nodoActual.getDato();
			if (usuario.getId_user() == idUsuario) {
				return usuario; // Usuario encontrado
			}
			nodoActual = nodoActual.getEnlace();
		}
		return null; // Usuario no encontrado
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