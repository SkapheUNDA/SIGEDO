package la.netco.persistencia.dto.commons;
// Generated 9/10/2012 04:26:59 PM by Hibernate Tools 3.2.1.GA

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import la.netco.core.persistencia.vo.Schemas;


@Entity
@Table(name = "TIPOSPERSONA", schema = Schemas.DBO_SCHEMA)
@NamedQueries({
	@NamedQuery(name=Tipospersona.NAMED_QUERY_ALL_TP,query=Tipospersona.QUERY_ALL_TP)
})
public class Tipospersona implements java.io.Serializable {

  
	private static final long serialVersionUID = 1L;
	private short tpeId;
    private String tpeNom;
    private boolean tpeLent;
    
    public static short TIPO_PERSONA_NATURAL= 1;
    public static short TIPO_PERSONA_JURIDICA = 2;
    	    
 //   private Set<Expedientepersona> expedientepersonas = new HashSet<Expedientepersona>(0);
//    private Set<Expediente> expedientes = new HashSet<Expediente>(0);
//    private Set<Salida> salidas = new HashSet<Salida>(0);
//    private Set<Registropersona> registropersonas = new HashSet<Registropersona>(0);
//    private Set<Parteinterviniente> parteintervinientes = new HashSet<Parteinterviniente>(0);
//    private Set<Productor> productors = new HashSet<Productor>(0);

    public Tipospersona() {
    }

    public Tipospersona(short tpeId, boolean tpeLent) {
        this.tpeId = tpeId;
        this.tpeLent = tpeLent;
    }

//    public Tipospersona(short tpeId, String tpeNom, boolean tpeLent, Set<Expedientepersona> expedientepersonas, Set<Expediente> expedientes, Set<Salida> salidas, Set<Registropersona> registropersonas, Set<Parteinterviniente> parteintervinientes, Set<Productor> productors) {
//        this.tpeId = tpeId;
//        this.tpeNom = tpeNom;
//        this.tpeLent = tpeLent;
//        this.expedientepersonas = expedientepersonas;
//        this.expedientes = expedientes;
//        this.salidas = salidas;
//        this.registropersonas = registropersonas;
//        this.parteintervinientes = parteintervinientes;
//        this.productors = productors;
//    }

    /**
     * Id del tipo de persona
     * 
     * @return
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TPE_ID", unique = true, nullable = false)
    public short getTpeId() {
        return this.tpeId;
    }

    public void setTpeId(short tpeId) {
        this.tpeId = tpeId;
    }

    /**
     * Descripcion o nombre del tipo de persona 
     * 
     * @return
     */
    @Column(name = "TPE_NOM", length = 50)
    public String getTpeNom() {
        return this.tpeNom;
    }

    public void setTpeNom(String tpeNom) {
        this.tpeNom = tpeNom;
    }

    /**
     * si este tipo de persona  corresponde a una entidad 
     * 
     * @return
     */
    @Column(name = "TPE_LENT", nullable = false)
    public boolean isTpeLent() {
        return this.tpeLent;
    }

    public void setTpeLent(boolean tpeLent) {
        this.tpeLent = tpeLent;
    }

//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "tipospersona")
//    public Set<Expedientepersona> getExpedientepersonas() {
//        return this.expedientepersonas;
//    }
//
//    public void setExpedientepersonas(Set<Expedientepersona> expedientepersonas) {
//        this.expedientepersonas = expedientepersonas;
//    }

/*    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "tipospersona")
    public Set<Expediente> getExpedientes() {
        return this.expedientes;
    }

    public void setExpedientes(Set<Expediente> expedientes) {
        this.expedientes = expedientes;
    }*/

//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "tipospersona")
//    public Set<Salida> getSalidas() {
//        return this.salidas;
//    }
//
//    public void setSalidas(Set<Salida> salidas) {
//        this.salidas = salidas;
//    }
//
//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "tipospersona")
//    public Set<Registropersona> getRegistropersonas() {
//        return this.registropersonas;
//    }
//
//    public void setRegistropersonas(Set<Registropersona> registropersonas) {
//        this.registropersonas = registropersonas;
//    }
//
//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "tipospersona")
//    public Set<Parteinterviniente> getParteintervinientes() {
//        return this.parteintervinientes;
//    }
//
//    public void setParteintervinientes(Set<Parteinterviniente> parteintervinientes) {
//        this.parteintervinientes = parteintervinientes;
//    }
//
//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "tipospersona")
//    public Set<Productor> getProductors() {
//        return this.productors;
//    }
//
//    public void setProductors(Set<Productor> productors) {
//        this.productors = productors;
//    }
    
    public static final String NAMED_QUERY_ALL_TP = "getAllTipospersonas";
    public static final String QUERY_ALL_TP = "FROM Tipospersona tipospersona order by tipospersona.tpeNom asc";
    
}
