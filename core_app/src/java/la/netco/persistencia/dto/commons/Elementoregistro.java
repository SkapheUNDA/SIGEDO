package la.netco.persistencia.dto.commons;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import la.netco.core.persistencia.vo.Schemas;


@Entity
@Table(name = "ELEMENTOREGISTRO", schema = Schemas.DBO_SCHEMA)
public class Elementoregistro implements Serializable {


	private static final long serialVersionUID = 1L;
	private ElementoregistroId id;
    private Registro registro;
    private Elemento elemento;
    private String elrObs;

    public Elementoregistro() {
    }

    public Elementoregistro(ElementoregistroId id, Registro registro, Elemento elemento) {
        this.id = id;
        this.registro = registro;
        this.elemento = elemento;
    }

    public Elementoregistro(ElementoregistroId id, Registro registro, Elemento elemento, String elrObs) {
        this.id = id;
        this.registro = registro;
        this.elemento = elemento;
        this.elrObs = elrObs;
    }

    @EmbeddedId
    @AttributeOverrides({
        @AttributeOverride(name = "elrReg", column =
        @Column(name = "ELR_REG", nullable = false)),
        @AttributeOverride(name = "elrEle", column =
        @Column(name = "ELR_ELE", nullable = false))})
    public ElementoregistroId getId() {
        return this.id;
    }

    public void setId(ElementoregistroId id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ELR_REG", nullable = false, insertable = false, updatable = false)
    public Registro getRegistro() {
        return this.registro;
    }

    public void setRegistro(Registro registro) {
        this.registro = registro;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ELR_ELE", nullable = false, insertable = false, updatable = false)
    public Elemento getElemento() {
        return this.elemento;
    }

    public void setElemento(Elemento elemento) {
        this.elemento = elemento;
    }

    @Column(name = "ELR_OBS", length = 300)
    public String getElrObs() {
        return this.elrObs;
    }

    public void setElrObs(String elrObs) {
        this.elrObs = elrObs;
    }
    
    @Override
   	public boolean equals(Object object) {
   		if (!(object instanceof Elementoregistro)) {
   			return false;
   		}
   		Elementoregistro other = (Elementoregistro) object;
   		if (this.id.equals(other.id)) {
   			return true;
   		}
   		return false;
   	}

}
