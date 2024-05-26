package entidades;

public class HistoricoDato {
	private long id_dato;
	private double valor_dato;

	public HistoricoDato(long id_dato, double valor_dato) {
		this.id_dato = id_dato;
		this.valor_dato = valor_dato;
	}

	public long getId_dato() {
		return id_dato;
	}

	public void setId_dato(long id_dato) {
		this.id_dato = id_dato;
	}

	public double getValor_dato() {
		return valor_dato;
	}

	public void setValor_dato(double valor_dato) {
		this.valor_dato = valor_dato;
	}
}