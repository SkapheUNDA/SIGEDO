package la.netco.persistencia.dto.commons;

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
@Table(name = "ANEXOSALIDA",  schema = Schemas.DBO_SCHEMA)
public class Anexosalida implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private AnexosalidaId id;
    private Anexo anexo;
    private Salida salida;
    private Short dasCan;

    public Anexosalida() {
    }

    public Anexosalida(AnexosalidaId id) {
        this.id = id;
    }

    public Anexosalida(AnexosalidaId id,  Short dasCan) {
        this.id = id;
        this.dasCan = dasCan;
    }

    @EmbeddedId
    @AttributeOverrides({
        @AttributeOverride(name = "dasSal", column =
        @Column(name = "DAS_SAL", nullable = false)),
        @AttributeOverride(name = "dasAnx", column =
        @Column(name = "DAS_ANX", nullable = false))})
    public AnexosalidaId getId() {
        return this.id;
    }

    public void setId(AnexosalidaId id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DAS_ANX", nullable = false, insertable = false, updatable = false)
    public Anexo getAnexo() {
        return this.anexo;
    }

    public void setAnexo(Anexo anexo) {
        this.anexo = anexo;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DAS_SAL", nullable = false, insertable = false, updatable = false)
    public Salida getSalida() {
        return this.salida;
    }

    public void setSalida(Salida salida) {
        this.salida = salida;
    }

    @Column(name = "DAS_CAN")
    public Short getDasCan() {
        return this.dasCan;
    }

    public void setDasCan(Short dasCan) {
        this.dasCan = dasCan;
    }
}
