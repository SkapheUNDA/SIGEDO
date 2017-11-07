package la.netco.registro.uilayer.beans;

import java.io.Serializable;

public class ElementoReg implements Serializable {


	private static final long serialVersionUID = 1L;
	
	private String proNom;
	private String eleNom;
	public String getProNom() {
		return proNom;
	}
	public String getEleNom() {
		return eleNom;
	}
	public void setProNom(String proNom) {
		this.proNom = proNom;
	}
	public void setEleNom(String eleNom) {
		this.eleNom = eleNom;
	}

}
