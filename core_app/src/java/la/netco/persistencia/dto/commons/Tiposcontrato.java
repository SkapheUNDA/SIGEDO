package la.netco.persistencia.dto.commons;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import la.netco.core.persistencia.vo.Schemas;

@Entity
@Table(name = "TIPOSCONTRATO", schema = Schemas.DBO_SCHEMA)
@NamedQuery(name=Tiposcontrato.NAMED_QUERY_GET_BY_CLASE,query=Tiposcontrato.QUERY_GET_BY_CLASE)
public class Tiposcontrato implements Serializable {

	private static final long serialVersionUID = 1L;
	private short tcoId;
    private String tcoNom;
    private Set<Claseregistro> claseregistros = new HashSet<Claseregistro>(0);
    //private Set<Relacioncontrato> relacioncontratos = new HashSet<Relacioncontrato>(0);

    public Tiposcontrato() {
    }

    public Tiposcontrato(short tcoId) {
        this.tcoId = tcoId;
    }

    public Tiposcontrato(short tcoId, String tcoNom, Set<Claseregistro> claseregistros) {
        this.tcoId = tcoId;
        this.tcoNom = tcoNom;
        this.claseregistros = claseregistros;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TCO_ID", unique = true, nullable = false)
    public short getTcoId() {
        return this.tcoId;
    }

    public void setTcoId(short tcoId) {
        this.tcoId = tcoId;
    }

    @Column(name = "TCO_NOM", length = 50)
    public String getTcoNom() {
        return this.tcoNom;
    }

    public void setTcoNom(String tcoNom) {
        this.tcoNom = tcoNom;
    }

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "REGISTROCONTRATO", schema = "dbo", catalog = "AUTOR", joinColumns = {
        @JoinColumn(name = "RGC_TCO", nullable = false, updatable = false)}, inverseJoinColumns = {
        @JoinColumn(name = "RGC_CRE", nullable = false, updatable = false)})
    public Set<Claseregistro> getClaseregistros() {
        return this.claseregistros;
    }

    public void setClaseregistros(Set<Claseregistro> claseregistros) {
        this.claseregistros = claseregistros;
    }

/*    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "tiposcontrato")
    public Set<Relacioncontrato> getRelacioncontratos() {
        return this.relacioncontratos;
    }

    public void setRelacioncontratos(Set<Relacioncontrato> relacioncontratos) {
        this.relacioncontratos = relacioncontratos;
    }*/
    


    public static final String NAMED_QUERY_GET_BY_CLASE = "getTiposcontratoByClaseregistro";
    public static final String QUERY_GET_BY_CLASE = "SELECT tipo FROM Tiposcontrato tipo  INNER JOIN tipo.claseregistros clase WHERE  clase.creId = ? order by tipo.tcoNom asc";
    
}
