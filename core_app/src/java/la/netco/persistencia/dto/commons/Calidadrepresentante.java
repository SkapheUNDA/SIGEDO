package la.netco.persistencia.dto.commons;
// Generated 9/10/2012 04:26:59 PM by Hibernate Tools 3.2.1.GA

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import la.netco.core.persistencia.vo.Schemas;


@Entity
@Table(name = "CALIDADREPRESENTANTE", schema = Schemas.DBO_SCHEMA)
@NamedQueries({
	@NamedQuery(name=Calidadrepresentante.NAMED_QUERY_ALL_CR,query=Calidadrepresentante.QUERY_ALL_CR)
})
public class Calidadrepresentante implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private short calId;
    private String calNom;
    private Set<Expediente> expedientes = new HashSet<Expediente>(0);

    
    public static Short R_MENOR_DE_EDAD = 9;
    public static Short R_PERSONA_JURIDICA = 10;
    public static Short R_PERSONA_NATURAL = 11;
    
    public Calidadrepresentante() {
    }

    public Calidadrepresentante(short calId) {
        this.calId = calId;
    }

    public Calidadrepresentante(short calId, String calNom, Set<Expediente> expedientes) {
        this.calId = calId;
        this.calNom = calNom;
        this.expedientes = expedientes;
    }

    /**
     * 
     * Id de la calidad representante(calidad de representante es el
     *  concepto de 'en nombre de quien' se va a registrar la obra)
     * @return
     */
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "CAL_ID", unique = true, nullable = false)
    public short getCalId() {
        return this.calId;
    }

    public void setCalId(short calId) {
        this.calId = calId;
    }

    /**
     * 
     * nombre o Descripcion de la calidad representante(calidad de representante es el
     *  concepto de 'en nombre de quien' se va a registrar la obra)
     * @return
     */
    @Column(name = "CAL_NOM", length = 50)
    public String getCalNom() {
        return this.calNom;
    }

    public void setCalNom(String calNom) {
        this.calNom = calNom;
    }

    /**
     * 
     * objeto de expedientes para consultar que expediente tiene este concepto de calidad
     * de representante
     * 
     * @return
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "calidadrepresentante")
    public Set<Expediente> getExpedientes() {
        return this.expedientes;
    }

    public void setExpedientes(Set<Expediente> expedientes) {
        this.expedientes = expedientes;
    }
    
    public static final String NAMED_QUERY_ALL_CR = "getAllCR";
    public static final String QUERY_ALL_CR = "FROM Calidadrepresentante calidadrepresente order by calidadrepresente.calNom asc";
    
    
}
