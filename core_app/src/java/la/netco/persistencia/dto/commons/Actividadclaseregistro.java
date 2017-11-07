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
@Table(name = "ACTIVIDADCLASEREGISTRO", schema = Schemas.DBO_SCHEMA)
public class Actividadclaseregistro implements Serializable {

	private static final long serialVersionUID = 1L;
	private ActividadclaseregistroId id;
    private Claseregistro claseregistro;
    private Actividad actividad;
    private Tiporegistropersona tiporegistropersona;

    public Actividadclaseregistro() {
    }

    public Actividadclaseregistro(ActividadclaseregistroId id, Claseregistro claseregistro, Actividad actividad, Tiporegistropersona tiporegistropersona) {
        this.id = id;
        this.claseregistro = claseregistro;
        this.actividad = actividad;
        this.tiporegistropersona = tiporegistropersona;
    }

    @EmbeddedId
    @AttributeOverrides({
        @AttributeOverride(name = "acrCre", column =
        @Column(name = "ACR_CRE", nullable = false)),
        @AttributeOverride(name = "acrAct", column =
        @Column(name = "ACR_ACT", nullable = false)),
        @AttributeOverride(name = "acrTrp", column =
        @Column(name = "ACR_TRP", nullable = false))})
    public ActividadclaseregistroId getId() {
        return this.id;
    }

    public void setId(ActividadclaseregistroId id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ACR_CRE", nullable = false, insertable = false, updatable = false)
    public Claseregistro getClaseregistro() {
        return this.claseregistro;
    }

    public void setClaseregistro(Claseregistro claseregistro) {
        this.claseregistro = claseregistro;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ACR_ACT", nullable = false, insertable = false, updatable = false)
    public Actividad getActividad() {
        return this.actividad;
    }

    public void setActividad(Actividad actividad) {
        this.actividad = actividad;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ACR_TRP", nullable = false, insertable = false, updatable = false)
    public Tiporegistropersona getTiporegistropersona() {
        return this.tiporegistropersona;
    }

    public void setTiporegistropersona(Tiporegistropersona tiporegistropersona) {
        this.tiporegistropersona = tiporegistropersona;
    }
}
