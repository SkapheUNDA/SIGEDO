package la.netco.persistencia.dto.commons;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import la.netco.core.persistencia.vo.Schemas;



@Entity
@Table(name = "ANEXOENTRADA",  schema = Schemas.DBO_SCHEMA)
@NamedQuery(name=Anexoentrada.NAMED_QUERY_GET_BY_ENTRADA,query=Anexoentrada.QUERY_GET_BY_ENTRADA)
public class Anexoentrada implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private AnexoentradaId id;
    private Short daeCan;
    private Anexo anexo;
    private Entrada entrada;
    
    public Anexoentrada() {
    }

    public Anexoentrada(AnexoentradaId id) {
        this.id = id;
    }

    public Anexoentrada(AnexoentradaId id, Short daeCan) {
        this.id = id;
        this.daeCan = daeCan;
    }

    @EmbeddedId
    @AttributeOverrides({
        @AttributeOverride(name = "daeEnt", column =
        @Column(name = "DAE_ENT", nullable = false)),
        @AttributeOverride(name = "daeAnx", column =
        @Column(name = "DAE_ANX", nullable = false))})
    public AnexoentradaId getId() {
        return this.id;
    }

    public void setId(AnexoentradaId id) {
        this.id = id;
    }

    @Column(name = "DAE_CAN")
    public Short getDaeCan() {
        return this.daeCan;
    }

    public void setDaeCan(Short daeCan) {
        this.daeCan = daeCan;
    }
    
    @ManyToOne
    @JoinColumn(name = "DAE_ANX", nullable = false, insertable = false, updatable = false)
    public Anexo getAnexo() {
        return this.anexo;
    }

    public void setAnexo(Anexo anexo) {
        this.anexo = anexo;
    }
    
    @ManyToOne
    @JoinColumn(name = "DAE_ENT", nullable = false, insertable = false, updatable = false)
	public Entrada getEntrada() {
		return entrada;
	}

	public void setEntrada(Entrada entrada) {
		this.entrada = entrada;
	}

    public static final String NAMED_QUERY_GET_BY_ENTRADA = "getAnexoByEntrada";
    public static final String QUERY_GET_BY_ENTRADA = "FROM Anexoentrada WHERE  id.daeEnt = ? ";
  
}
