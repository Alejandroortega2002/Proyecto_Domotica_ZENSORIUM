package modelo;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import entidades.Usuario;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class RegistroManager {
	private static final String FILE_PATH = "data/usuarios.json";

	public static List<Usuario> cargarUsuarios() {
		try (Reader reader = new FileReader(FILE_PATH)) {
			Type listType = new TypeToken<ArrayList<Usuario>>() {
			}.getType();
			return new Gson().fromJson(reader, listType);
		} catch (IOException e) {
			return new ArrayList<>();
		}
	}

	public static void guardarUsuarios(List<Usuario> usuarios) {
		try (Writer writer = new FileWriter(FILE_PATH)) {
			new Gson().toJson(usuarios, writer);
		} catch (IOException e) {
			// Manejar la excepción
			e.printStackTrace();
		}
	}

	public static boolean registrarUsuario(Usuario nuevoUsuario) {

		List<Usuario> usuarios = cargarUsuarios();

		if (usuarios == null) {
			usuarios = new ArrayList<>();
		}

		for (Usuario usuario : usuarios) {
			if (usuario.getUsername().equals(nuevoUsuario.getUsername())) {
				// Usuario ya registrado, no se puede registrar de nuevo
				return false;
			}
		}

		usuarios.add(nuevoUsuario);
		guardarUsuarios(usuarios);
		return true;
	}

}
