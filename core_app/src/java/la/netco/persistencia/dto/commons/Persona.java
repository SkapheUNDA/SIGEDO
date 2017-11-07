package la.netco.persistencia.dto.commons;
// Generated 9/10/2012 04:26:59 PM by Hibernate Tools 3.2.1.GA

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Formula;

import la.netco.core.persistencia.vo.Schemas;


@Entity
@Table(name = "PERSONA", schema = Schemas.DBO_SCHEMA)
@NamedQueries(value = {
		@NamedQuery(name=Persona.NAMEQUERY_SELECT_BY_NODOCUMENTO_TIPODOCUMENTO, query = Persona.SELECT_BY_NODOCUMENTO_TIPODOCUMENTO),
		@NamedQuery(name=Persona.NAMED_QUERY_GET_MAX_ALL,query=Persona.QUERY_GET_MAX_ALL)
})

public class Persona implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private int perId;
    private Tiposdocumento tiposdocumento;
    private Sector sector;
    private Paises paises;
    private Lugar lugarByPerSeulug;
    private Lugar lugarByPerLug;
    private String perDoc;
    private String perNom;
    private String perPap;
    private String perSap;
    private Date perFna;
    private String perDir;
    private String perTl1;
    private String perTl2;
    private String perTl3;
    private String perFax;
    private String perCe1;
    private String perCe2;
    private String perSeunom;
    private String perSeunot;
    private String perSeuesc;
    private Date perSeufec;
    private boolean perLviv;
    private Date perDeffec;
    private String perDefact;
    private String perNmo;
    private String perPwe;
    private String perCpo;
    private boolean perLsid;
    private boolean perLses;
    private boolean perLseu;
    private String perOcu;
//    private Integer id;

    private String nombreCompleto;  
    
    public Persona() {
    }

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "PER_ID", unique = true, nullable = false)
    public int getPerId() {
        return this.perId;
    }

    public void setPerId(int perId) {
        this.perId = perId;
    }

    @ManyToOne
    @JoinColumn(name = "PER_TDO", nullable=false)
    public Tiposdocumento getTiposdocumento() {
        return this.tiposdocumento;
    }

    public void setTiposdocumento(Tiposdocumento tiposdocumento) {
        this.tiposdocumento = tiposdocumento;
    }

    @ManyToOne
    @JoinColumn(name = "PER_SEC")
    public Sector getSector() {
        return this.sector;
    }

    public void setSector(Sector sector) {
        this.sector = sector;
    }

    @ManyToOne
    @JoinColumn(name = "PER_NAC")
    public Paises getPaises() {
        return this.paises;
    }

    public void setPaises(Paises paises) {
        this.paises = paises;
    }

    @ManyToOne
    @JoinColumn(name = "PER_SEULUG")
    public Lugar getLugarByPerSeulug() {
        return this.lugarByPerSeulug;
    }

    public void setLugarByPerSeulug(Lugar lugarByPerSeulug) {
        this.lugarByPerSeulug = lugarByPerSeulug;
    }

    @ManyToOne
    @JoinColumn(name = "PER_LUG")
    public Lugar getLugarByPerLug() {
        return this.lugarByPerLug;
    }

    public void setLugarByPerLug(Lugar lugarByPerLug) {
        this.lugarByPerLug = lugarByPerLug;
    }

    /**
     * 
     * Documento de la persona
     * 
     * @return
     */
    @Column(name = "PER_DOC", length = 30)
    public String getPerDoc() {
        return this.perDoc;
    }

    public void setPerDoc(String perDoc) {
        this.perDoc = perDoc;
    }

    /**
     * Nombre de la persona
     * 
     * @return
     */
    @Column(name = "PER_NOM", length = 50)
    public String getPerNom() {
        return this.perNom;
    }

    public void setPerNom(String perNom) {
        this.perNom = perNom;
    }

    /**
     * 
     * Primer apellido  de la persona
     * 
     * @return
     */
    @Column(name = "PER_PAP", length = 50)
    public String getPerPap() {
        return this.perPap;
    }

    public void setPerPap(String perPap) {
        this.perPap = perPap;
    }

    /**
     * 
     * segundo apellido de la persona
     * 
     * @return
     */
    @Column(name = "PER_SAP", length = 50)
    public String getPerSap() {
        return this.perSap;
    }

    public void setPerSap(String perSap) {
        this.perSap = perSap;
    }

    /**
     * 
     * Fecha de nacimiento de la persona
     * 
     * @return
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "PER_FNA", length = 23)
    public Date getPerFna() {
        return this.perFna;
    }

    public void setPerFna(Date perFna) {
        this.perFna = perFna;
    }

    /**
     * 
     *Direccion de residencia de la persona
     * 
     * @return
     */
    @Column(name = "PER_DIR", length = 200)
    public String getPerDir() {
        return this.perDir;
    }

    public void setPerDir(String perDir) {
        this.perDir = perDir;
    }

    /**
     * Telefono numero 1 de la persona
     * 
     * @return
     */
    @Column(name = "PER_TL1", length = 50)
    public String getPerTl1() {
        return this.perTl1;
    }

    public void setPerTl1(String perTl1) {
        this.perTl1 = perTl1;
    }

    /**
     * 
     * Telefono numero 2 de la persona
     * 
     * 
     * @return
     */
    @Column(name = "PER_TL2", length = 50)
    public String getPerTl2() {
        return this.perTl2;
    }

    public void setPerTl2(String perTl2) {
        this.perTl2 = perTl2;
    }

    /**
     * 
     * Telefono numero 3 de la persona
     * 
     * @return
     */
    @Column(name = "PER_TL3", length = 50)
    public String getPerTl3() {
        return this.perTl3;
    }

    public void setPerTl3(String perTl3) {
        this.perTl3 = perTl3;
    }

    /**
     * 
     * Fax de la persona
     * 
     * @return
     */
    @Column(name = "PER_FAX", length = 50)
    public String getPerFax() {
        return this.perFax;
    }

    public void setPerFax(String perFax) {
        this.perFax = perFax;
    }

    /**
     * 
     * Correo electronico n° 1 de la persona
     * 
     * @return
     */
    @Column(name = "PER_CE1", length = 50)
    public String getPerCe1() {
        return this.perCe1;
    }

    public void setPerCe1(String perCe1) {
        this.perCe1 = perCe1;
    }

    /**
     * 
     * Correo electronico n° 2 de la persona
     * 
     * @return
     */
    @Column(name = "PER_CE2", length = 50)
    public String getPerCe2() {
        return this.perCe2;
    }

    public void setPerCe2(String perCe2) {
        this.perCe2 = perCe2;
    }

    /**
     * 
     * Seudonimo de la persona
     * 
     * @return
     */
    @Column(name = "PER_SEUNOM", length = 100)
    public String getPerSeunom() {
        return this.perSeunom;
    }

    public void setPerSeunom(String perSeunom) {
        this.perSeunom = perSeunom;
    }

    /**
     * 
     * Notaria en donde se registro el seudonimo
     * 
     * @return
     */
    @Column(name = "PER_SEUNOT", length = 50)
    public String getPerSeunot() {
        return this.perSeunot;
    }

    public void setPerSeunot(String perSeunot) {
        this.perSeunot = perSeunot;
    }

    /**
     * 
     * Escritura del Seudonimo
     * 
     * @return
     */
    @Column(name = "PER_SEUESC", length = 50)
    public String getPerSeuesc() {
        return this.perSeuesc;
    }

    public void setPerSeuesc(String perSeuesc) {
        this.perSeuesc = perSeuesc;
    }

    /**
     * 
     * Fecha del Seudonimo
     * 
     * @return
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "PER_SEUFEC", length = 23)
    public Date getPerSeufec() {
        return this.perSeufec;
    }

    public void setPerSeufec(Date perSeufec) {
        this.perSeufec = perSeufec;
    }

    /**
     * 
     * Validar si esta persona vive
     * 
     * @return
     */
    @Column(name = "PER_LVIV", nullable = false)
    public boolean isPerLviv() {
        return this.perLviv;
    }

    public void setPerLviv(boolean perLviv) {
        this.perLviv = perLviv;
    }

    /**
     * 
     * fecha de Defuncion de la persona
     * 
     * @return
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "PER_DEFFEC", length = 23)
    public Date getPerDeffec() {
        return this.perDeffec;
    }

    public void setPerDeffec(Date perDeffec) {
        this.perDeffec = perDeffec;
    }

    /**
     * 
     * Acta de defuncion de la persona
     * 
     * @return
     */
    @Column(name = "PER_DEFACT", length = 50)
    public String getPerDefact() {
        return this.perDefact;
    }

    public void setPerDefact(String perDefact) {
        this.perDefact = perDefact;
    }

    /**
     * 
     * Numero movil de la persona
     * 
     * @return
     */
    @Column(name = "PER_NMO", length = 30)
    public String getPerNmo() {
        return this.perNmo;
    }

    public void setPerNmo(String perNmo) {
        this.perNmo = perNmo;
    }

    /**
     * 
     * Sitio Web
     * 
     * @return
     */
    @Column(name = "PER_PWE", length = 50)
    public String getPerPwe() {
        return this.perPwe;
    }

    public void setPerPwe(String perPwe) {
        this.perPwe = perPwe;
    }

    /**
     * 
     * Codigo postal de la persona 
     * 
     * @return
     */
    @Column(name = "PER_CPO", length = 20)
    public String getPerCpo() {
        return this.perCpo;
    }

    public void setPerCpo(String perCpo) {
        this.perCpo = perCpo;
    }

    /**
     * 
     * Valida si esta persona esta sin identificacion
     * 
     * @return
     */
    @Column(name = "PER_LSID", nullable = false)
    public boolean isPerLsid() {
        return this.perLsid;
    }

    public void setPerLsid(boolean perLsid) {
        this.perLsid = perLsid;
    }

    /**
     * 
     * 
     * Valida si esta persona esta sin fecha de nacimiento
     * 
     * @return
     */
    @Column(name = "PER_LSES", nullable = false)
    public boolean isPerLses() {
        return this.perLses;
    }

    public void setPerLses(boolean perLses) {
        this.perLses = perLses;
    }

    /**
     * 
     * Valida si tiene Seudonimo
     * 
     * @return
     */
    @Column(name = "PER_LSEU", nullable = false)
    public boolean isPerLseu() {
        return this.perLseu;
    }

    public void setPerLseu(boolean perLseu) {
        this.perLseu = perLseu;
    }

    /**
     * 
     * Ocupacion de la  persona
     * 
     * @return
     */
    @Column(name = "PER_OCU", length = 50)
    public String getPerOcu() {
        return this.perOcu;
    }

    public void setPerOcu(String perOcu) {
        this.perOcu = perOcu;
    }


/*    @Column(name = "ID")
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }*/
    
    @Formula(value="PER_NOM + ' ' + PER_PAP + ' ' + PER_SAP")
    public String getNombreCompleto() {
		return nombreCompleto;
	}

	public void setNombreCompleto(String nombreCompleto) {
		this.nombreCompleto = nombreCompleto;
	}


	public static final String NAMEQUERY_SELECT_BY_NODOCUMENTO_TIPODOCUMENTO= "SelectByNoDocumento";
    public static final String SELECT_BY_NODOCUMENTO_TIPODOCUMENTO = "from Persona persona Where persona.perDoc = ?  and persona.tiposdocumento.tdoId = ?";		
    
    public static final String NAMED_QUERY_GET_MAX_ALL = "getMaxNoDocumentados";
	public static final String QUERY_GET_MAX_ALL = "SELECT max(perDoc) From Persona persona WHERE persona.tiposdocumento.tdoId = ?";
	
}
