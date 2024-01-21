package modelo;

import entidades.Nodo;
/**
 * Implementación de una lista enlazada genérica.
 * 
 * @param <T> El tipo de elementos que la lista contendrá.
 */
public class ListaEnlazada<T> {
    private Nodo<T> cabeza;

    /**
     * Constructor que inicializa la lista enlazada.
     */
    public ListaEnlazada() {
        this.cabeza = null;
    }

    /**
     * Obtiene el primer nodo de la lista.
     * 
     * @return El primer nodo de la lista.
     */
    public Nodo<T> getCabeza() {
        return cabeza;
    }

    /**
     * Establece el primer nodo de la lista.
     * 
     * @param cabeza El nodo a establecer como cabeza de la lista.
     */
    public void setCabeza(Nodo<T> cabeza) {
        this.cabeza = cabeza;
    }

    /**
     * Inserta un nodo al principio de la lista.
     * 
     * @param nuevoNodo El nodo a insertar.
     */
    public void insertarAlPrincipio(Nodo<T> nuevoNodo) {
        if (nuevoNodo == null) {
			return;
		}
		nuevoNodo.setEnlace(cabeza);
		cabeza = nuevoNodo;
    }

    /**
     * Inserta un nodo al final de la lista.
     * 
     * @param nuevoNodo El nodo a insertar.
     */
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

    /**
     * Inserta un nodo entre dos nodos existentes.
     * 
     * @param nodoAnterior El nodo anterior al lugar de inserción.
     * @param nuevoNodo El nodo a insertar.
     */
    public void insertarEntreNodos(Nodo<T> nodoAnterior, Nodo<T> nuevoNodo) {
        if (nodoAnterior == null || nuevoNodo == null) {
			return;
		}
		Nodo<T> nodoSiguiente = nodoAnterior.getEnlace();
		nodoAnterior.setEnlace(nuevoNodo);
		nuevoNodo.setEnlace(nodoSiguiente);
    }

    /**
     * Elimina el primer nodo de la lista.
     */
    public void eliminarAlPrincipio() {
        if (cabeza != null) {
			cabeza = cabeza.getEnlace();
		}
    }

    /**
     * Elimina el último nodo de la lista.
     */
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

    /**
     * Imprime los elementos de la lista en consola.
     */
    public void imprimirLista() {
        Nodo<T> temp = cabeza;
		while (temp != null) {
			System.out.print(temp.getDato() + " ");
			temp = temp.getEnlace();
		}
		System.out.println();
    }

    /**
     * Busca un valor en la lista.
     * 
     * @param valor El valor a buscar.
     * @return true si el valor se encuentra en la lista, false en caso contrario.
     */
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

    /**
     * Inserta un nodo en el medio de la lista.
     * 
     * @param nuevoNodo El nodo a insertar.
     */
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

    /**
     * Verifica si la lista está vacía.
     * 
     * @return true si la lista está vacía, false en caso contrario.
     */
    public boolean esVacia() {
       return cabeza == null;
    }
}
