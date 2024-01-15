package modelo;

import com.google.gson.Gson;

import com.google.gson.reflect.TypeToken;

import entidades.Reporte;
import entidades.Usuario;
import entidades.Dispositivos;
import entidades.Nodo;

import java.io.*;
import java.lang.reflect.Type;

public class ReporteManager {
	private static final String FILE_PATH_Reporte = "data/Reporte.json";
	private static Reporte reporteSeleccionado;

	public static ListaEnlazada<Reporte> cargarReportes() {
		try (Reader reader = new FileReader(FILE_PATH_Reporte)) {
			Type listType = new TypeToken<ListaEnlazada<Reporte>>() {
			}.getType();
			ListaEnlazada<Reporte> ReporteCargados = new Gson().fromJson(reader, listType);

			// Verificar si la deserialización retornó null y, de ser así, retornar una
			// lista vacía
			if (ReporteCargados == null) {
				return new ListaEnlazada<>();
			}

			return ReporteCargados;
		} catch (IOException e) {
			// En caso de una IOException, también retornar una lista vacía
			return new ListaEnlazada<>();
		}
	}

	public static void guardarReporte(ListaEnlazada<Reporte> dispos) {
		try (Writer writer = new FileWriter(FILE_PATH_Reporte)) {
			new Gson().toJson(dispos, writer);
		} catch (IOException e) {
			// Manejar la excepción
			e.printStackTrace();
		}
	}

	public static boolean crearReporte(Reporte nuevoReporte) {

		ListaEnlazada<Reporte> reportes = cargarReportes();

		if (reportes == null) {
			reportes = new ListaEnlazada<>();
		}

		reportes.insertarAlFinal(new Nodo<>(nuevoReporte));
		guardarReporte(reportes);
		return true;
	}


	public static long obtenerNuevoId() {
		ListaEnlazada<Reporte> dispos = cargarReportes();

		if (dispos.getCabeza() == null) {
			return 1; // Si la lista está vacía, empezamos desde 1
		}

		long idMasAlto = 0;
		Nodo<Reporte> nodoActual = dispos.getCabeza();
		while (nodoActual != null) {
			Reporte dispo = nodoActual.getDato();
			if (dispo.getId_reporte() > idMasAlto) {
				idMasAlto = dispo.getId_reporte();
			}
			nodoActual = nodoActual.getEnlace();
		}

		return idMasAlto + 1; // Incrementa el ID más alto en 1
	}

	public static Reporte getReporteSeleccionado() {
		return reporteSeleccionado;
	}

	public static void setReporteSeleccionado(Reporte reporte) {
		reporteSeleccionado = reporte;
	}

	public static String sacarNombreDeId(Long idUsuario) {
		ListaEnlazada<Usuario> usuarios = RegistroManager.cargarUsuarios();

		if (usuarios == null) {
			return ""; // No hay dispositivos para modificar
		}

		Nodo<Usuario> nodoActual = usuarios.getCabeza();

		while (nodoActual != null) {
			Usuario usuActual = nodoActual.getDato();

			if (usuActual.getId_user() == idUsuario) {
				// Se ha encontrado el dispositivo a modificar

				return usuActual.getUsername(); // Dispositivo modificado exitosamente
			}

			nodoActual = nodoActual.getEnlace();
		}

		return ""; // No se encontró el dispositivo con el nombre especificado
	}

	//////////////////////////////////////////////
	public static boolean eliminarReporte(String tituloReporte) {
		ListaEnlazada<Reporte> reportes = cargarReportes();

		if (reportes == null) {
			return false; // No hay dispositivos para eliminar
		}

		Nodo<Reporte> nodoActual = reportes.getCabeza();
		Nodo<Reporte> nodoAnterior = null;

		while (nodoActual != null) {
			Reporte reporteActual = nodoActual.getDato();

			if (reporteActual.getTitulo().equals(tituloReporte)) {
				// Se ha encontrado el dispositivo a eliminar

				if (nodoAnterior == null) {
					// El dispositivo a eliminar es el primer elemento de la lista
					reportes.setCabeza(nodoActual.getEnlace());
				} else {
					// El dispositivo a eliminar está en medio o al final de la lista
					nodoAnterior.setEnlace(nodoActual.getEnlace());
				}

				guardarReporte(reportes);
				return true; // Dispositivo eliminado exitosamente
			}

			nodoAnterior = nodoActual;
			nodoActual = nodoActual.getEnlace();
		}

		return false; // No se encontró el dispositivo con el nombre especificado
	}

}
