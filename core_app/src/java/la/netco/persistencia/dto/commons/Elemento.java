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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import la.netco.core.persistencia.vo.Schemas;



@Entity
@Table(name = "ELEMENTO", schema = Schemas.DBO_SCHEMA)
@NamedQuery(name=Elemento.NAMED_QUERY_GET_BY_PROP,query=Elemento.QUERY_GET_BY_PROP)
public class Elemento implements Serializable {


	private static final long serialVersionUID = 1L;
	private int eleId;
    private Propiedad propiedad;
    private String eleNom;
    private Set<Elementoregistro> elementoregistros = new HashSet<Elementoregistro>(0);

    public Elemento() {
    }

    public Elemento(int eleId) {
        this.eleId = eleId;
    }

    public Elemento(int eleId, Propiedad propiedad, String eleNom, Set<Elementoregistro> elementoregistros) {
        this.eleId = eleId;
        this.propiedad = propiedad;
        this.eleNom = eleNom;
        this.elementoregistros = elementoregistros;
    }

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "ELE_ID", unique = true, nullable = false)
    public int getEleId() {
        return this.eleId;
    }

    public void setEleId(int eleId) {
        this.eleId = eleId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ELE_PRO")
    public Propiedad getPropiedad() {
        return this.propiedad;
    }

    public void setPropiedad(Propiedad propiedad) {
        this.propiedad = propiedad;
    }

    @Column(name = "ELE_NOM", length = 50)
    public String getEleNom() {
        return this.eleNom;
    }

    public void setEleNom(String eleNom) {
        this.eleNom = eleNom;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "elemento")
    public Set<Elementoregistro> getElementoregistros() {
        return this.elementoregistros;
    }

    public void setElementoregistros(Set<Elementoregistro> elementoregistros) {
        this.elementoregistros = elementoregistros;
    }
    
    public static final String NAMED_QUERY_GET_BY_PROP = "getElementoByPropiedad";
    public static final String QUERY_GET_BY_PROP = "SELECT elemento FROM Elemento elemento   WHERE  elemento.propiedad.proId = ? order by elemento.eleNom asc";

}
