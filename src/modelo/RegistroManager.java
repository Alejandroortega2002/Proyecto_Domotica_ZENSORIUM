
package modelo;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;

import entidades.Nodo;
import entidades.Relaciones;
import entidades.Usuario;

import java.io.*;
import java.lang.reflect.Type;

/**
 * Maneja las operaciones relacionadas con el registro y gestión de usuarios y sus relaciones.
 */
public class RegistroManager {
    private static final String FILE_PATH = "data/usuarios.json";
    private static final String FILE_PATH_RELACIONES = "data/relaciones.json";

    /**
     * Carga una lista de usuarios desde un archivo JSON.
     * 
     * @return Lista enlazada de usuarios.
     */
    public static ListaEnlazada<Usuario> cargarUsuarios() {
       try (Reader reader = new FileReader(FILE_PATH)) {
			Type listType = new TypeToken<ListaEnlazada<Usuario>>() {
			}.getType();
			ListaEnlazada<Usuario> usuarios = new Gson().fromJson(reader, listType);
			return usuarios != null ? usuarios : new ListaEnlazada<>();
		} catch (IOException e) {
			return new ListaEnlazada<>();
		}
    }

    /**
     * Guarda una lista de usuarios en un archivo JSON.
     * 
     * @param usuarios Lista enlazada de usuarios a guardar.
     */
    public static void guardarUsuarios(ListaEnlazada<Usuario> usuarios) {
        try (Writer writer = new FileWriter(FILE_PATH)) {
			new Gson().toJson(usuarios, writer);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    /**
     * Registra un nuevo usuario en la lista.
     * 
     * @param nuevoUsuario Usuario a registrar.
     * @return true si el registro fue exitoso, false si el usuario ya existe.
     */
    public static boolean registrarUsuario(Usuario nuevoUsuario) {
        ListaEnlazada<Usuario> usuarios = cargarUsuarios();
		for (Nodo<Usuario> nodoActual = usuarios.getCabeza(); nodoActual != null; nodoActual = nodoActual.getEnlace()) {
			Usuario usuario = nodoActual.getDato();
			if (usuario.getUsername().equals(nuevoUsuario.getUsername())
					|| usuario.getEmail().equals(nuevoUsuario.getEmail())) {
				return false;
			}
		}

		usuarios.insertarAlFinal(new Nodo<>(nuevoUsuario));
		guardarUsuarios(usuarios);
		return true;
    }

    /**
     * Obtiene un nuevo ID único para un usuario.
     * 
     * @return El nuevo ID generado.
     */
    public static long obtenerNuevoId() {
       ListaEnlazada<Usuario> usuarios = cargarUsuarios();
		if (usuarios.getCabeza() == null) {
			return 1;
		}

		long idMasAlto = 0;
		for (Nodo<Usuario> nodoActual = usuarios.getCabeza(); nodoActual != null; nodoActual = nodoActual.getEnlace()) {
			Usuario usuario = nodoActual.getDato();
			if (usuario.getId_user() > idMasAlto) {
				idMasAlto = usuario.getId_user();
			}
		}
		return idMasAlto + 1;
    }

    /**
     * Carga una lista de relaciones desde un archivo JSON.
     * 
     * @return Lista enlazada de relaciones.
     */
    public static ListaEnlazada<Relaciones> cargarRelaciones() {
        try (Reader reader = new FileReader(FILE_PATH_RELACIONES)) {
			Type listType = new TypeToken<ListaEnlazada<Relaciones>>() {
			}.getType();
			ListaEnlazada<Relaciones> relaciones = new Gson().fromJson(reader, listType);
			return relaciones != null ? relaciones : new ListaEnlazada<>();
		} catch (IOException e) {
			e.printStackTrace();
			return new ListaEnlazada<>();
		}
    }

    /**
     * Verifica si ya existe una relación entre dos usuarios.
     * 
     * @param idUsuario1 ID del primer usuario.
     * @param idUsuario2 ID del segundo usuario.
     * @return true si la relación existe, false en caso contrario.
     */
    public static boolean verificarRelacionExistente(long idUsuario1, long idUsuario2) {
       ListaEnlazada<Relaciones> relacionesExistentes = cargarRelaciones();
		for (Nodo<Relaciones> nodoActual = relacionesExistentes.getCabeza(); nodoActual != null; nodoActual = nodoActual
				.getEnlace()) {
			Relaciones rel = nodoActual.getDato();
			if (rel.getTu_id() == idUsuario1 && rel.getId_user_relacion() == idUsuario2) {
				return true;
			}
		}
		return false;
    }

    /**
     * Carga las relaciones de un usuario específico.
     * 
     * @param idUsuario ID del usuario cuyas relaciones se van a cargar.
     * @return Lista enlazada de relaciones del usuario.
     */
    public static ListaEnlazada<Relaciones> cargarRelacionesPorUsuario(long idUsuario) {
       ListaEnlazada<Relaciones> todasLasRelaciones = cargarRelaciones();
		ListaEnlazada<Relaciones> relacionesDelUsuario = new ListaEnlazada<>();

		for (Nodo<Relaciones> nodoActual = todasLasRelaciones.getCabeza(); nodoActual != null; nodoActual = nodoActual
				.getEnlace()) {
			Relaciones relacion = nodoActual.getDato();
			if (relacion.getTu_id() == idUsuario || relacion.getId_user_relacion() == idUsuario) {
				relacionesDelUsuario.insertarAlFinal(new Nodo<>(relacion));
			}
		}

		return relacionesDelUsuario;
    }

    /**
     * Guarda una nueva relación en la lista y en el archivo JSON.
     * 
     * @param nuevaRelacion La relación a guardar.
     * @return true si la relación fue guardada con éxito, false en caso contrario.
     */
    public static boolean guardarRelacion(Relaciones nuevaRelacion) {
        if (!verificarRelacionExistente(nuevaRelacion.getTu_id(), nuevaRelacion.getId_user_relacion())) {
			ListaEnlazada<Relaciones> relacionesExistentes = cargarRelaciones();
			relacionesExistentes.insertarAlFinal(new Nodo<>(nuevaRelacion));
			try (Writer writer = new FileWriter(FILE_PATH_RELACIONES)) {
				new Gson().toJson(relacionesExistentes, writer);
				return true;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		} else {
			return false;
		}
    }

    /**
     * Modifica la información de un usuario existente.
     * 
     * @param idUsuario ID del usuario a modificar.
     * @param nombreNuevoUusario Nuevo nombre del usuario.
     * @param correoNuevoUusario Nuevo correo electrónico del usuario.
     * @return true si el usuario fue modificado con éxito, false en caso contrario.
     */
    public static boolean modificarUsuario(Long idUsuario, String nombreNuevoUusario, String correoNuevoUusario) {
       ListaEnlazada<Usuario> usuarios = cargarUsuarios();

		if (usuarios == null) {
			return false; // No hay dispositivos para modificar
		}

		Nodo<Usuario> nodoActual = usuarios.getCabeza();

		while (nodoActual != null) {
			Usuario usuarioActual = nodoActual.getDato();

			if (usuarioActual.getId_user() == idUsuario) {
				// Se ha encontrado el dispositivo a modificar

				usuarioActual.setUsername(nombreNuevoUusario);
				usuarioActual.setEmail(correoNuevoUusario);

				guardarUsuarios(usuarios);
				return true; // Dispositivo modificado exitosamente
			}

			nodoActual = nodoActual.getEnlace();
		}

		return false; // No se encontró el dispositivo con el nombre especificado
    }

    /**
     * Actualiza la contraseña de un usuario.
     * 
     * @param idUsuario ID del usuario cuya contraseña se actualizará.
     * @param nuevaPass La nueva contraseña del usuario.
     * @return true si la contraseña fue actualizada con éxito, false en caso contrario.
     */
    public static boolean actualizaPass(Long idUsuario, String nuevaPass) {
       ListaEnlazada<Usuario> usuarios = cargarUsuarios();

		if (usuarios == null) {
			return false; // No hay dispositivos para modificar
		}

		Nodo<Usuario> nodoActual = usuarios.getCabeza();

		while (nodoActual != null) {
			Usuario usuarioActual = nodoActual.getDato();

			if (usuarioActual.getId_user() == idUsuario) {
				// Se ha encontrado el dispositivo a modificar

				usuarioActual.setPassword(nuevaPass);
				usuarioActual.setRepetirPass(nuevaPass);

				guardarUsuarios(usuarios);
				return true; // Dispositivo modificado exitosamente
			}

			nodoActual = nodoActual.getEnlace();
		}

		return false; // No se encontró el dispositivo con el nombre especificado
    }

<<<<<<< HEAD
    /**
     * Busca un usuario por su ID.
     * 
     * @param idUsuario ID del usuario a buscar.
     * @return El usuario si es encontrado, null en caso contrario.
     */
    public static Usuario buscarUsuarioPorId(long idUsuario) {
       ListaEnlazada<Usuario> usuarios = cargarUsuarios();
		if (usuarios == null || usuarios.esVacia()) {
			return null; // O podrías lanzar una excepción si prefieres
		}

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
=======
	}
>>>>>>> parent of ac0d6a4 (Ultimos retoques antes de comentar)
}


