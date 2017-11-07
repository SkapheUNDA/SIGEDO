package la.netco.commons.utils;

import java.io.Serializable;

public class CriteriaAlias implements Serializable {

	private static final long serialVersionUID = 1L;
	private String columna;
	private String alias;
	private int tipo;
	
	public CriteriaAlias(String columna, String alias, int tipo) {
		super();
		this.columna = columna;
		this.alias = alias;
		this.tipo = tipo;
	}
	public String getColumna() {
		return columna;
	}
	public String getAlias() {
		return alias;
	}
	public int getTipo() {
		return tipo;
	}
	public void setColumna(String columna) {
		this.columna = columna;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public void setTipo(int tipo) {
		this.tipo = tipo;
	}
}
