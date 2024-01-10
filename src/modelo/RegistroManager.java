package modelo;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import entidades.Nodo;
import entidades.Usuario;

import java.io.*;
import java.lang.reflect.Type;

public class RegistroManager {
    private static final String FILE_PATH = "data/usuarios.json";

    public static ListaEnlazada<Usuario> cargarUsuarios() {
        try (Reader reader = new FileReader(FILE_PATH)) {
            Type listType = new TypeToken<ListaEnlazada<Usuario>>() {}.getType();
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
            if (usuario.getUsername().equals(nuevoUsuario.getUsername()) || usuario.getEmail().equals(nuevoUsuario.getEmail())) {
                // Usuario ya registrado, no se puede registrar de nuevo
                return false;
            }
            nodoActual = nodoActual.getEnlace();
        }

        usuarios.insertarAlFinal(new Nodo<>(nuevoUsuario));
        guardarUsuarios(usuarios);
        return true;
    }
}
