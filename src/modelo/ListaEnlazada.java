package modelo;

import entidades.Nodo;

public class ListaEnlazada<T> {
	private Nodo<T> cabeza;

	public ListaEnlazada() {
		this.cabeza = null;
	}

	public Nodo<T> getCabeza() {
		return cabeza;
	}

	public void setCabeza(Nodo<T> cabeza) {
		this.cabeza = cabeza;
	}

	public void insertarAlPrincipio(Nodo<T> nuevoNodo) {
		if (nuevoNodo == null) {
			return;
		}
		nuevoNodo.setEnlace(cabeza);
		cabeza = nuevoNodo;
	}

	public void insertarAlFinal(Nodo<T> nuevoNodo) {
		if (nuevoNodo == null) {
			return;
		}
		if (cabeza == null) {
			cabeza = nuevoNodo;
		} else {
			Nodo<T> temp = cabeza;
			while (temp.getEnlace() != null) {
				temp = temp.getEnlace();
			}
			temp.setEnlace(nuevoNodo);
		}
	}

	public void insertarEntreNodos(Nodo<T> nodoAnterior, Nodo<T> nuevoNodo) {
		if (nodoAnterior == null || nuevoNodo == null) {
			return;
		}
		Nodo<T> nodoSiguiente = nodoAnterior.getEnlace();
		nodoAnterior.setEnlace(nuevoNodo);
		nuevoNodo.setEnlace(nodoSiguiente);
	}

	public void eliminarAlPrincipio() {
		if (cabeza != null) {
			cabeza = cabeza.getEnlace();
		}
	}

	public void eliminarAlFinal() {
		if (cabeza != null) {
			Nodo<T> temp = cabeza;
			Nodo<T> prev = null;
			while (temp.getEnlace() != null) {
				prev = temp;
				temp = temp.getEnlace();
			}
			if (prev != null) {
				prev.setEnlace(null);
			} else {
				cabeza = null;
			}
		}
	}

	public void imprimirLista() {
		Nodo<T> temp = cabeza;
		while (temp != null) {
			System.out.print(temp.getDato() + " ");
			temp = temp.getEnlace();
		}
		System.out.println();
	}

	public boolean buscar(T valor) {
		Nodo<T> temp = cabeza;
		while (temp != null) {
			if (temp.getDato().equals(valor)) {
				return true;
			}
			temp = temp.getEnlace();
		}
		return false;
	}

	public void insertarEnMedio(Nodo<T> nuevoNodo) {
		if (cabeza == null) {
			cabeza = nuevoNodo;
			return;
		}

		int longitud = 0;
		Nodo<T> temp = cabeza;
		while (temp != null) {
			longitud++;
			temp = temp.getEnlace();
		}

		int medio = longitud / 2;
		temp = cabeza;
		for (int i = 0; i < medio - 1; i++) {
			temp = temp.getEnlace();
		}

		nuevoNodo.setEnlace(temp.getEnlace());
		temp.setEnlace(nuevoNodo);
	}

	public boolean esVacia() {
		return cabeza == null;
	}
}