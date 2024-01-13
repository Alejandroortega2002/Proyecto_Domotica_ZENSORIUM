package modelo;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;

import entidades.Dispositivos;
import entidades.Nodo;
import entidades.Relaciones;
import entidades.Usuario;

import java.io.*;
import java.lang.reflect.Type;

public class DispositivosManager {
	private static final String FILE_PATH = "data/Dispositivos.json";

	public static ListaEnlazada<Dispositivos> cargarDispos() {
		try (Reader reader = new FileReader(FILE_PATH)) {
			Type listType = new TypeToken<ListaEnlazada<Dispositivos>>() {
			}.getType();
			return new Gson().fromJson(reader, listType);
		} catch (IOException e) {
			return new ListaEnlazada<>();
		}
	}

	public static void guardarDispositivos(ListaEnlazada<Dispositivos> dispos) {
		try (Writer writer = new FileWriter(FILE_PATH)) {
			new Gson().toJson(dispos, writer);
		} catch (IOException e) {
			// Manejar la excepción
			e.printStackTrace();
		}
	}

	public static boolean registrarDispos(Dispositivos nuevoDispo) {

		ListaEnlazada<Dispositivos> dispos = cargarDispos();

		if (dispos == null) {
			dispos = new ListaEnlazada<>();
		}

		Nodo<Dispositivos> nodoActual = dispos.getCabeza();
		while (nodoActual != null) {
			Dispositivos disposs = nodoActual.getDato();
			if (disposs.getNombre().equals(nuevoDispo.getNombre())) {

				return false;
			}
			nodoActual = nodoActual.getEnlace();
		}

		dispos.insertarAlFinal(new Nodo<>(nuevoDispo));
		guardarDispositivos(dispos);
		return true;
	}
	public static boolean eliminarDispositivo(String nombreDispositivo) {
	    ListaEnlazada<Dispositivos> dispositivos = cargarDispos();

	    if (dispositivos == null) {
	        return false; // No hay dispositivos para eliminar
	    }

	    Nodo<Dispositivos> nodoActual = dispositivos.getCabeza();
	    Nodo<Dispositivos> nodoAnterior = null;

	    while (nodoActual != null) {
	        Dispositivos dispositivoActual = nodoActual.getDato();

	        if (dispositivoActual.getNombre().equals(nombreDispositivo)) {
	            // Se ha encontrado el dispositivo a eliminar

	            if (nodoAnterior == null) {
	                // El dispositivo a eliminar es el primer elemento de la lista
	                dispositivos.setCabeza(nodoActual.getEnlace());
	            } else {
	                // El dispositivo a eliminar está en medio o al final de la lista
	                nodoAnterior.setEnlace(nodoActual.getEnlace());
	            }

	            guardarDispositivos(dispositivos);
	            return true; // Dispositivo eliminado exitosamente
	        }

	        nodoAnterior = nodoActual;
	        nodoActual = nodoActual.getEnlace();
	    }

	    return false; // No se encontró el dispositivo con el nombre especificado
	}

	public static long obtenerNuevoId() {
		ListaEnlazada<Dispositivos> dispos = cargarDispos();

		if (dispos.getCabeza() == null) {
			return 1; // Si la lista está vacía, empezamos desde 1
		}

		long idMasAlto = 0;
		Nodo<Dispositivos> nodoActual = dispos.getCabeza();
		while (nodoActual != null) {
			Dispositivos dispo = nodoActual.getDato();
			if (dispo.getId_dispo() > idMasAlto) {
				idMasAlto = dispo.getId_dispo();
			}
			nodoActual = nodoActual.getEnlace();
		}

		return idMasAlto + 1; // Incrementa el ID más alto en 1
	}

	public static boolean modificarDispositivo(String nombreDispositivo, String nombreNuevoDispositivo) {
	    ListaEnlazada<Dispositivos> dispositivos = cargarDispos();

	    if (dispositivos == null) {
	        return false; // No hay dispositivos para modificar
	    }

	    Nodo<Dispositivos> nodoActual = dispositivos.getCabeza();

	    while (nodoActual != null) {
	        Dispositivos dispositivoActual = nodoActual.getDato();

	        if (dispositivoActual.getNombre().equals(nombreDispositivo)) {
	            // Se ha encontrado el dispositivo a modificar

	
	            dispositivoActual.setNombre(nombreNuevoDispositivo);

	            guardarDispositivos(dispositivos);
	            return true; // Dispositivo modificado exitosamente
	        }

	        nodoActual = nodoActual.getEnlace();
	    }

	    return false; // No se encontró el dispositivo con el nombre especificado
	}

}
