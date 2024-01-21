package modelo;

import entidades.Usuario;

/**
 * Clase Sesion para manejar la sesión del usuario en la aplicación.
 * Implementa el patrón Singleton para mantener una única instancia de sesión.
 */
public class Sesion {
    private static Sesion instancia = new Sesion();
    private Usuario usuarioActual;

    /**
     * Constructor privado para prevenir la instanciación directa.
     */
    private Sesion() {}

    /**
     * Obtiene la instancia única de la sesión.
     * 
     * @return La instancia única de Sesion.
     */
    public static Sesion getInstancia() {
        return instancia;
    }

    /**
     * Obtiene el usuario actualmente registrado en la sesión.
     * 
     * @return El usuario actual.
     */
    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    /**
     * Establece el usuario actual para la sesión.
     * 
     * @param usuarioActual El usuario a establecer como el actual en la sesión.
     */
    public void setUsuarioActual(Usuario usuarioActual) {
        this.usuarioActual = usuarioActual;
    }
}