package modelo;

import entidades.Usuario;

public class Sesion {
	private static Sesion instancia = new Sesion();
	private Usuario usuarioActual;

	private Sesion() {
	}

	public static Sesion getInstancia() {
		return instancia;
	}

	public Usuario getUsuarioActual() {
		return usuarioActual;
	}

	public void setUsuarioActual(Usuario usuarioActual) {
		this.usuarioActual = usuarioActual;
	}
}