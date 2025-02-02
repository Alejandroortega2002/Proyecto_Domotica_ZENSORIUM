package entidades;

public class Nodo<T> {
	private T dato;
	private Nodo<T> enlace;

	public Nodo(T dato) {
		this.dato = dato;
		this.enlace = null;
	}

	public T getDato() {
		return dato;
	}

	public void setDato(T dato) {
		this.dato = dato;
	}

	public Nodo<T> getEnlace() {
		return enlace;
	}

	public void setEnlace(Nodo<T> enlace) {
		this.enlace = enlace;
	}
}