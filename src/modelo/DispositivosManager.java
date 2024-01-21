package modelo;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import entidades.Dispositivos;
import entidades.Nodo;
import entidades.Relaciones;
import entidades.Sensores;

import java.io.*;
import java.lang.reflect.Type;

public class DispositivosManager {
	private static final String FILE_PATH_DISPOSITIVOS = "data/Dispositivos.json";
	private static final String FILE_PATH_SENSORES = "data/Sensores.json";

	private static Dispositivos dispositivoSeleccionado;

	public static ListaEnlazada<Dispositivos> cargarDispos() {
		try (Reader reader = new FileReader(FILE_PATH_DISPOSITIVOS)) {
			Type listType = new TypeToken<ListaEnlazada<Dispositivos>>() {
			}.getType();
			ListaEnlazada<Dispositivos> dispositivosCargados = new Gson().fromJson(reader, listType);

			// Verificar si la deserializaci�n retorn� null y, de ser as�, retornar una
			// lista vac�a
			if (dispositivosCargados == null) {
				return new ListaEnlazada<>();
			}

			return dispositivosCargados;
		} catch (IOException e) {
			// En caso de una IOException, tambi�n retornar una lista vac�a
			return new ListaEnlazada<>();
		}
	}

	public static void guardarDispositivos(ListaEnlazada<Dispositivos> dispos) {
		try (Writer writer = new FileWriter(FILE_PATH_DISPOSITIVOS)) {
			new Gson().toJson(dispos, writer);
		} catch (IOException e) {
			// Manejar la excepci�n
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
					// El dispositivo a eliminar est� en medio o al final de la lista
					nodoAnterior.setEnlace(nodoActual.getEnlace());
				}

				guardarDispositivos(dispositivos);
				return true; // Dispositivo eliminado exitosamente
			}

			nodoAnterior = nodoActual;
			nodoActual = nodoActual.getEnlace();
		}

		return false; // No se encontr� el dispositivo con el nombre especificado
	}

	public static long obtenerNuevoId() {
		ListaEnlazada<Dispositivos> dispos = cargarDispos();

		if (dispos.getCabeza() == null) {
			return 1; // Si la lista est� vac�a, empezamos desde 1
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

		return idMasAlto + 1; // Incrementa el ID m�s alto en 1
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

		return false; // No se encontr� el dispositivo con el nombre especificado
	}

	public static boolean modificarEstadoDispositivo(String nombreDispositivo, Boolean estadoDispositivo) {
		ListaEnlazada<Dispositivos> dispositivos = cargarDispos();

		if (dispositivos == null) {
			return false; // No hay dispositivos para modificar
		}

		Nodo<Dispositivos> nodoActual = dispositivos.getCabeza();

		while (nodoActual != null) {
			Dispositivos dispositivoActual = nodoActual.getDato();

			if (dispositivoActual.getNombre().equals(nombreDispositivo)) {
				// Se ha encontrado el dispositivo a modificar

				dispositivoActual.setEstado(estadoDispositivo);
				System.out.println(dispositivoActual.isEstado());

				guardarDispositivos(dispositivos);
				return true; // Dispositivo modificado exitosamente
			}

			nodoActual = nodoActual.getEnlace();
		}

		return false; // No se encontr� el dispositivo con el nombre especificado
	}

	public static Dispositivos getDispositivoSeleccionado() {
		return dispositivoSeleccionado;
	}

	public static void setDispositivoSeleccionado(Dispositivos dispositivo) {
		dispositivoSeleccionado = dispositivo;
	}

	public static ListaEnlazada<Sensores> cargarSensores() {
		try (Reader reader = new FileReader(FILE_PATH_SENSORES)) {
			Type listType = new TypeToken<ListaEnlazada<Sensores>>() {
			}.getType();
			return new Gson().fromJson(reader, listType);
		} catch (IOException e) {
			return new ListaEnlazada<>();
		}
	}

	public static void guardarSensores(ListaEnlazada<Sensores> sensores) {
		try (Writer writer = new FileWriter(FILE_PATH_SENSORES)) {
			new Gson().toJson(sensores, writer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static long creaNuevoSensor(float dato_actual, String nombre, String tipo) {
		ListaEnlazada<Sensores> sensores = cargarSensores();

		if (sensores == null) {
			sensores = new ListaEnlazada<>();
		}

		long idMasAlto = 0;
		for (Nodo<Sensores> nodoActual = sensores.getCabeza(); nodoActual != null; nodoActual = nodoActual
				.getEnlace()) {
			long idActual = nodoActual.getDato().getId_sensor();
			if (idActual > idMasAlto) {
				idMasAlto = idActual;
			}
		}

		long nuevoId = idMasAlto + 1;
		long ordenRegistroMasAlto = 0;

		// Busca el mayor orden_registro para el nuevo ID
		for (Nodo<Sensores> nodoActual = sensores.getCabeza(); nodoActual != null; nodoActual = nodoActual
				.getEnlace()) {
			Sensores sensorActual = nodoActual.getDato();
			if (sensorActual.getId_sensor() == nuevoId) {
				long ordenActual = sensorActual.getOrden_registro();
				if (ordenActual > ordenRegistroMasAlto) {
					ordenRegistroMasAlto = ordenActual;
				}
			}
		}

		long nuevoOrdenRegistro = ordenRegistroMasAlto + 1;
		Sensores nuevoSensor = new Sensores(nuevoId, dato_actual, nombre, tipo, nuevoOrdenRegistro);

		// A�ade el nuevo sensor y guarda la lista
		sensores.insertarAlFinal(new Nodo<>(nuevoSensor));
		guardarSensores(sensores);
		return nuevoId;
	}

	public static void crearSensorConId(long id_sensor, float dato_actual, String nombre, String tipo) {
		ListaEnlazada<Sensores> sensores = cargarSensores();

		if (sensores == null) {
			sensores = new ListaEnlazada<>();
		}

		long ordenRegistroMasAlto = 0;
		// Busca el mayor orden_registro para el ID dado
		for (Nodo<Sensores> nodoActual = sensores.getCabeza(); nodoActual != null; nodoActual = nodoActual
				.getEnlace()) {
			Sensores sensorActual = nodoActual.getDato();
			if (sensorActual.getId_sensor() == id_sensor) {
				long ordenActual = sensorActual.getOrden_registro();
				if (ordenActual > ordenRegistroMasAlto) {
					ordenRegistroMasAlto = ordenActual;
				}
			}
		}

		long nuevoOrdenRegistro = ordenRegistroMasAlto + 1;
		Sensores nuevoSensor = new Sensores(id_sensor, dato_actual, nombre, tipo, nuevoOrdenRegistro);

		// A�ade el nuevo sensor y guarda la lista
		sensores.insertarAlFinal(new Nodo<>(nuevoSensor));
		guardarSensores(sensores);
	}

	public static ListaEnlazada<Dispositivos> cargarDispositivosPorUsuario(long idUsuario) {
		ListaEnlazada<Dispositivos> todosLosDispositivos = cargarDispos();
		ListaEnlazada<Dispositivos> dispositivosDelUsuario = new ListaEnlazada<>();

		for (Nodo<Dispositivos> nodoActual = todosLosDispositivos
				.getCabeza(); nodoActual != null; nodoActual = nodoActual.getEnlace()) {
			Dispositivos dispositivo = nodoActual.getDato();
			if (dispositivo.getId_usu_relacionado() == idUsuario) {
				dispositivosDelUsuario.insertarAlFinal(new Nodo<>(dispositivo));
			}
		}

		return dispositivosDelUsuario;
	}

	public static ListaEnlazada<Dispositivos> cargarDispositivosRelacionados(long idUsuario) {
		ListaEnlazada<Dispositivos> todosLosDispositivos = cargarDispos();
		ListaEnlazada<Dispositivos> dispositivosRelacionados = new ListaEnlazada<>();
		ListaEnlazada<Relaciones> relacionesDelUsuario = RegistroManager.cargarRelacionesPorUsuario(idUsuario);
		for (Nodo<Relaciones> nodoRel = relacionesDelUsuario.getCabeza(); nodoRel != null; nodoRel = nodoRel
				.getEnlace()) {
			Relaciones relacion = nodoRel.getDato();
			long idRelacionado = (relacion.getTu_id() == idUsuario) ? relacion.getId_user_relacion()
					: relacion.getTu_id();

			for (Nodo<Dispositivos> nodoDispo = todosLosDispositivos
					.getCabeza(); nodoDispo != null; nodoDispo = nodoDispo.getEnlace()) {
				Dispositivos dispositivo = nodoDispo.getDato();
				if (dispositivo.getId_usu_relacionado() == idRelacionado) {
					dispositivosRelacionados.insertarAlFinal(new Nodo<>(dispositivo));
				}
			}
		}

		return dispositivosRelacionados;
	}

}