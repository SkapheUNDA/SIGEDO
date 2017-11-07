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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import la.netco.core.persistencia.vo.Schemas;



@Entity
@Table(name = "ENLACE", schema = Schemas.DBO_SCHEMA)
@NamedQueries({
		@NamedQuery(name = Enlace.NAMED_QUERY_GET_BY_ENTRADA_ORIGEN_ENTRADA, query = Enlace.QUERY_GET_BY_ENTRADA_ORIGEN_ENTRADA),
		@NamedQuery(name = Enlace.NAMED_QUERY_GET_BY_SALIDA_ORIGEN_SALIDA, query = Enlace.QUERY_GET_BY_SALIDA_ORIGEN_SALIDA),
		@NamedQuery(name = Enlace.NAMED_QUERY_GET_BY_ENTRADA_ORIGEN_SALIDA, query = Enlace.QUERY_GET_BY_ENTRADA_ORIGEN_SALIDA)})
public class Enlace implements Serializable {


	private static final long serialVersionUID = 1L;
	private EnlaceId id;

	public static byte ORIGEN_ENTRADA	= 1;
	public static byte ORIGEN_SALIDA	= 2;
	
	public Salida salida;
	public Entrada entrada;


    public static final String NAMED_QUERY_GET_BY_ENTRADA_ORIGEN_ENTRADA = "getEnlaceEntradaOrigenEntrada";
    public static final String QUERY_GET_BY_ENTRADA_ORIGEN_ENTRADA = "From Enlace enlace WHERE id.enlEnt = ? AND id.enlOrg = 1 ";
    
    public static final String NAMED_QUERY_GET_BY_ENTRADA_ORIGEN_SALIDA = "getEnlaceEntradaOrigenSalida";
    public static final String QUERY_GET_BY_ENTRADA_ORIGEN_SALIDA= "From Enlace enlace WHERE id.enlEnt = ? AND id.enlOrg = 2";
    
    
    public static final String NAMED_QUERY_GET_BY_SALIDA_ORIGEN_SALIDA = "getEnlaceSalidaOrigenSalida";
    public static final String QUERY_GET_BY_SALIDA_ORIGEN_SALIDA = "From Enlace enlace WHERE id.enlSal = ? AND id.enlOrg = 2";
	
    
    public Enlace() {
    }

    public Enlace(EnlaceId id) {
        this.id = id;
    }

    @EmbeddedId
    @AttributeOverrides({
        @AttributeOverride(name = "enlOrg", column =
        @Column(name = "ENL_ORG", nullable = false)),
        @AttributeOverride(name = "enlEnt", column =
        @Column(name = "ENL_ENT", nullable = false)),
        @AttributeOverride(name = "enlSal", column =
        @Column(name = "ENL_SAL", nullable = false))})
    public EnlaceId getId() {
        return this.id;
    }

    public void setId(EnlaceId id) {
        this.id = id;
    }
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ENL_SAL", nullable = false, insertable = false, updatable = false)
    public Salida getSalida() {
        return this.salida;
    }

    public void setSalida(Salida salida) {
        this.salida = salida;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ENL_ENT", nullable = false, insertable = false, updatable = false)
	public Entrada getEntrada() {
		return entrada;
	}

	public void setEntrada(Entrada entrada) {
		this.entrada = entrada;
	}

}
