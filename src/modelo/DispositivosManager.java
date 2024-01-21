package modelo;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;

import entidades.Dispositivos;
import entidades.Nodo;
import entidades.Relaciones;
import entidades.Sensores;
import entidades.Usuario;

import java.io.*;
import java.lang.reflect.Type;
/**
 * Esta clase maneja las operaciones relacionadas con dispositivos, incluyendo cargar, guardar,
 * registrar, modificar y eliminar dispositivos, así como cargar y guardar sensores.
 */
public class DispositivosManager {
    private static final String FILE_PATH_DISPOSITIVOS = "data/Dispositivos.json";
    private static final String FILE_PATH_SENSORES = "data/Sensores.json";

    private static Dispositivos dispositivoSeleccionado;

    /**
     * Carga dispositivos desde un archivo JSON.
     * 
     * @return ListaEnlazada de dispositivos.
     */
    public static ListaEnlazada<Dispositivos> cargarDispos() {
        try (Reader reader = new FileReader(FILE_PATH_DISPOSITIVOS)) {
			Type listType = new TypeToken<ListaEnlazada<Dispositivos>>() {
			}.getType();
			ListaEnlazada<Dispositivos> dispositivosCargados = new Gson().fromJson(reader, listType);

			// Verificar si la deserialización retornó null y, de ser así, retornar una
			// lista vacía
			if (dispositivosCargados == null) {
				return new ListaEnlazada<>();
			}

			return dispositivosCargados;
		} catch (IOException e) {
			// En caso de una IOException, también retornar una lista vacía
			return new ListaEnlazada<>();
		}
    }

    /**
     * Guarda una lista de dispositivos en un archivo JSON.
     * 
     * @param dispos Lista de dispositivos a guardar.
     */
    public static void guardarDispositivos(ListaEnlazada<Dispositivos> dispos) {
        try (Writer writer = new FileWriter(FILE_PATH_DISPOSITIVOS)) {
			new Gson().toJson(dispos, writer);
		} catch (IOException e) {
			// Manejar la excepción
			e.printStackTrace();
		}
    }

    /**
     * Registra un nuevo dispositivo.
     * 
     * @param nuevoDispo El dispositivo a registrar.
     * @return true si el dispositivo fue registrado con éxito, false en caso contrario.
     */
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

    /**
     * Elimina un dispositivo específico.
     * 
     * @param nombreDispositivo El nombre del dispositivo a eliminar.
     * @return true si el dispositivo fue eliminado con éxito, false en caso contrario.
     */
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

    /**
     * Obtiene un nuevo ID para un dispositivo.
     * 
     * @return El nuevo ID generado.
     */
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

    /**
     * Modifica el nombre de un dispositivo existente.
     * 
     * @param nombreDispositivo El nombre actual del dispositivo.
     * @param nombreNuevoDispositivo El nuevo nombre para el dispositivo.
     * @return true si el dispositivo fue modificado con éxito, false en caso contrario.
     */
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

    /**
     * Modifica el estado de un dispositivo existente.
     * 
     * @param nombreDispositivo El nombre del dispositivo a modificar.
     * @param estadoDispositivo El nuevo estado para el dispositivo.
     * @return true si el dispositivo fue modificado con éxito, false en caso contrario.
     */
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

		return false; // No se encontró el dispositivo con el nombre especificado
    }

    /**
     * Obtiene el dispositivo actualmente seleccionado.
     * 
     * @return El dispositivo seleccionado.
     */
    public static Dispositivos getDispositivoSeleccionado() {
        return dispositivoSeleccionado;
    }

    /**
     * Establece el dispositivo actualmente seleccionado.
     * 
     * @param dispositivo El dispositivo a seleccionar.
     */
    public static void setDispositivoSeleccionado(Dispositivos dispositivo) {
        dispositivoSeleccionado = dispositivo;
    }

    /**
     * Carga sensores desde un archivo JSON.
     * 
     * @return ListaEnlazada de sensores.
     */
    public static ListaEnlazada<Sensores> cargarSensores() {
        try (Reader reader = new FileReader(FILE_PATH_SENSORES)) {
			Type listType = new TypeToken<ListaEnlazada<Sensores>>() {
			}.getType();
			return new Gson().fromJson(reader, listType);
		} catch (IOException e) {
			return new ListaEnlazada<>();
		}
    }

    /**
     * Guarda una lista de sensores en un archivo JSON.
     * 
     * @param sensores Lista de sensores a guardar.
     */
    public static void guardarSensores(ListaEnlazada<Sensores> sensores) {
        try (Writer writer = new FileWriter(FILE_PATH_SENSORES)) {
			new Gson().toJson(sensores, writer);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    /**
     * Crea un nuevo sensor y lo añade a la lista.
     * 
     * @param dato_actual El dato actual del sensor.
     * @param nombre El nombre del sensor.
     * @param tipo El tipo de sensor.
     * @return El ID del nuevo sensor.
     */
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

		// Añade el nuevo sensor y guarda la lista
		sensores.insertarAlFinal(new Nodo<>(nuevoSensor));
		guardarSensores(sensores);
		return nuevoId;
    }

    /**
     * Crea un sensor con un ID específico.
     * 
     * @param id_sensor El ID del sensor.
     * @param dato_actual El dato actual del sensor.
     * @param nombre El nombre del sensor.
     * @param tipo El tipo de sensor.
     */
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

		// Añade el nuevo sensor y guarda la lista
		sensores.insertarAlFinal(new Nodo<>(nuevoSensor));
		guardarSensores(sensores);
    }

    /**
     * Carga los dispositivos asociados a un usuario específico.
     * 
     * @param idUsuario El ID del usuario.
     * @return Lista de dispositivos asociados al usuario.
     */
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

    /**
     * Carga los dispositivos relacionados con un usuario específico.
     * 
     * @param idUsuario El ID del usuario.
     * @return Lista de dispositivos relacionados con el usuario.
     */
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
