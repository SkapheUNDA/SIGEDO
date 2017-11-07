package la.netco.persistencia.dto.commons;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import la.netco.core.persistencia.vo.Schemas;



@Entity
@Table(name = "REGISTROPERSONA", schema = Schemas.DBO_SCHEMA)
public class Registropersona implements Serializable {


	private static final long serialVersionUID = 1L;
	private int rpeId;
    private Tiporegistropersona tiporegistropersona;
    private Relacioncontrato relacioncontrato;
    private Actividad actividad;
    private Registro registro;
    private Tipospersona tipospersona;
    private Entidad entidad;
    private Persona persona;

    public Registropersona() {
    }

    public Registropersona(int rpeId) {
        this.rpeId = rpeId;
    }

    public Registropersona(int rpeId, Tiporegistropersona tiporegistropersona, Relacioncontrato relacioncontrato, Actividad actividad, Registro registro, Tipospersona tipospersona, Entidad entidad, Persona persona) {
        this.rpeId = rpeId;
        this.tiporegistropersona = tiporegistropersona;
        this.relacioncontrato = relacioncontrato;
        this.actividad = actividad;
        this.registro = registro;
        this.tipospersona = tipospersona;
        this.entidad = entidad;
        this.persona = persona;
    }

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "RPE_ID", unique = true, nullable = false)
    public int getRpeId() {
        return this.rpeId;
    }

    public void setRpeId(int rpeId) {
        this.rpeId = rpeId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RPE_TRP")
    public Tiporegistropersona getTiporegistropersona() {
        return this.tiporegistropersona;
    }

    public void setTiporegistropersona(Tiporegistropersona tiporegistropersona) {
        this.tiporegistropersona = tiporegistropersona;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RPE_RCO")
    public Relacioncontrato getRelacioncontrato() {
        return this.relacioncontrato;
    }

    public void setRelacioncontrato(Relacioncontrato relacioncontrato) {
        this.relacioncontrato = relacioncontrato;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RPE_ACT")
    public Actividad getActividad() {
        return this.actividad;
    }

    public void setActividad(Actividad actividad) {
        this.actividad = actividad;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RPE_REG")
    public Registro getRegistro() {
        return this.registro;
    }

    public void setRegistro(Registro registro) {
        this.registro = registro;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RPE_TPE")
    public Tipospersona getTipospersona() {
        return this.tipospersona;
    }

    public void setTipospersona(Tipospersona tipospersona) {
        this.tipospersona = tipospersona;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RPE_ETD")
    public Entidad getEntidad() {
        return this.entidad;
    }

    public void setEntidad(Entidad entidad) {
        this.entidad = entidad;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RPE_PER")
    public Persona getPersona() {
        return this.persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }
    
    public boolean equals(Object other) {
        if ((this == other)) {
            return true;
        }
        if ((other == null)) {
            return false;
        }
        if (!(other instanceof Registropersona)) {
            return false;
        }
        Registropersona castOther = (Registropersona) other;

        return (this.getRpeId() == castOther.getRpeId());
    }
}
