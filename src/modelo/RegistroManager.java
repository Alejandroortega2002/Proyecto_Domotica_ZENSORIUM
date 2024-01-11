package modelo;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;

import entidades.Nodo;
import entidades.Relaciones;
import entidades.Usuario;

import java.io.*;
import java.lang.reflect.Type;

public class RegistroManager {
	private static final String FILE_PATH = "data/usuarios.json";
    private static final String FILE_PATH_RELACIONES = "data/relaciones.json"; // Asegúrate de que la ruta es correcta


	public static ListaEnlazada<Usuario> cargarUsuarios() {
		try (Reader reader = new FileReader(FILE_PATH)) {
			Type listType = new TypeToken<ListaEnlazada<Usuario>>() {
			}.getType();
			return new Gson().fromJson(reader, listType);
		} catch (IOException e) {
			return new ListaEnlazada<>();
		}
	}

	public static void guardarUsuarios(ListaEnlazada<Usuario> usuarios) {
		try (Writer writer = new FileWriter(FILE_PATH)) {
			new Gson().toJson(usuarios, writer);
		} catch (IOException e) {
			// Manejar la excepción
			e.printStackTrace();
		}
	}

	public static boolean registrarUsuario(Usuario nuevoUsuario) {

		ListaEnlazada<Usuario> usuarios = cargarUsuarios();

		if (usuarios == null) {
			usuarios = new ListaEnlazada<>();
		}

		Nodo<Usuario> nodoActual = usuarios.getCabeza();
		while (nodoActual != null) {
			Usuario usuario = nodoActual.getDato();
			if (usuario.getUsername().equals(nuevoUsuario.getUsername())
					|| usuario.getEmail().equals(nuevoUsuario.getEmail())) {
				// Usuario ya registrado, no se puede registrar de nuevo
				return false;
			}
			nodoActual = nodoActual.getEnlace();
		}

		usuarios.insertarAlFinal(new Nodo<>(nuevoUsuario));
		guardarUsuarios(usuarios);
		return true;
	}

	public static long obtenerNuevoId() {
		ListaEnlazada<Usuario> usuarios = cargarUsuarios();

		if (usuarios.getCabeza() == null) {
			return 1; // Si la lista está vacía, empezamos desde 1
		}

		long idMasAlto = 0;
		Nodo<Usuario> nodoActual = usuarios.getCabeza();
		while (nodoActual != null) {
			Usuario usuario = nodoActual.getDato();
			if (usuario.getId_user() > idMasAlto) {
				idMasAlto = usuario.getId_user();
			}
			nodoActual = nodoActual.getEnlace();
		}

		return idMasAlto + 1; // Incrementa el ID más alto en 1
	}
	
	 public static ListaEnlazada<Relaciones> cargarRelaciones() {
	        try (FileReader reader = new FileReader(FILE_PATH_RELACIONES)) {
	            // Crea un tipo para la lista enlazada de relaciones
	            Type listType = new TypeToken<ListaEnlazada<Relaciones>>() {}.getType();
	            // Deserializa el JSON al tipo correspondiente
	            ListaEnlazada<Relaciones> relaciones = new Gson().fromJson(reader, listType);
	            return relaciones != null ? relaciones : new ListaEnlazada<>();
	        } catch (IOException e) {
	            e.printStackTrace();
	            // Si ocurre un error, devuelve una lista enlazada vacía
	            return new ListaEnlazada<>();
	        }
	    }
	 
	 public static boolean verificarRelacionExistente(long idUsuario, long idUsuarioFamiliar) {
			ListaEnlazada<Relaciones> relacionesExistentes = RegistroManager.cargarRelaciones();

			Nodo<Relaciones> nodoActual = relacionesExistentes.getCabeza();
			while (nodoActual != null) {
				Relaciones rel = nodoActual.getDato();
				if (rel.getTu_id() == idUsuario && rel.getId_user_relacion() == idUsuarioFamiliar) {
					return true; // La relación ya existe
				}
				nodoActual = nodoActual.getEnlace(); // Suponiendo que `getEnlace()` devuelve el siguiente nodo
			}
			return false; // No se encontró la relación, se puede crear una nueva
		}

		public static boolean guardarRelacion(Relaciones nuevaRelacion) throws JsonIOException {
			// Verifica si la relación ya existe
			if (!verificarRelacionExistente(nuevaRelacion.getTu_id(), nuevaRelacion.getId_user_relacion())) {
				ListaEnlazada<Relaciones> relacionesExistentes = cargarRelaciones();
				relacionesExistentes.insertarAlFinal(new Nodo<>(nuevaRelacion));

				// Guarda la lista actualizada en el archivo JSON
				try (Writer writer = new FileWriter(FILE_PATH_RELACIONES)) {
					Gson gson = new Gson();
					gson.toJson(relacionesExistentes, writer);
					return true; // Indica que la relación se ha guardado con éxito
				} catch (IOException e) {
					e.printStackTrace();
					return false; // Indica un fallo al guardar
				}
			} else {
				return false; // Indica que la relación ya existe y no se guardó
			}
		}

}
