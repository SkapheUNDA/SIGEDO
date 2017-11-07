package la.netco.persistencia.dto.commons;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import la.netco.core.persistencia.vo.Schemas;


@Entity
@Table(name = "CLASEREGISTRO", schema = Schemas.DBO_SCHEMA)

@NamedQuery(name=Claseregistro.NAMED_QUERY_GET_ALL_ACTIVOS,query=Claseregistro.QUERY_GET_BY_ALL_ACTIVOS)
public class Claseregistro implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	private int creId;
    private String creNom;
    private String creCod;
    private short creFrm;
    private Boolean creLact;
    private Boolean creLfte;
    private Boolean creLfpp;
    private Boolean creLned;
    private Boolean creLtir;
    private Boolean creLnpg;
    private Boolean creLpor;
    private Boolean creLdob;
    private Boolean creLdes;
    private Boolean creLnre;
    private Boolean creLfre;
    private Boolean creLisbn;
//    private Set<Tiporegistropersona> tiporegistropersonas = new HashSet<Tiporegistropersona>(0);
//    private Set<Actividadclaseregistro> actividadclaseregistros = new HashSet<Actividadclaseregistro>(0);
//    private Set<Propiedad> propiedads = new HashSet<Propiedad>(0);
//    private Set<Registro> registros = new HashSet<Registro>(0);
//    private Set<Tiposcontrato> tiposcontratos = new HashSet<Tiposcontrato>(0);

    

    public static final String NAMED_QUERY_GET_ALL_ACTIVOS = "getAllClaseRegistroActivos";
    public static final String QUERY_GET_BY_ALL_ACTIVOS = "FROM Claseregistro clase  WHERE  clase.creLact = 1 order by clase.creNom asc";
    
    
    public Claseregistro() {
    }

    public Claseregistro(int creId, short creFrm) {
        this.creId = creId;
        this.creFrm = creFrm;
    }

//    public Claseregistro(int creId, String creNom, String creCod, short creFrm, Boolean creLact, Boolean creLfte, Boolean creLfpp, Boolean creLned, Boolean creLtir, Boolean creLnpg, Boolean creLpor, Boolean creLdob, Boolean creLdes, Boolean creLnre, Boolean creLfre, Boolean creLisbn, Set<Tiporegistropersona> tiporegistropersonas, Set<Actividadclaseregistro> actividadclaseregistros, Set<Propiedad> propiedads, Set<Registro> registros, Set<Tiposcontrato> tiposcontratos) {
//        this.creId = creId;
//        this.creNom = creNom;
//        this.creCod = creCod;
//        this.creFrm = creFrm;
//        this.creLact = creLact;
//        this.creLfte = creLfte;
//        this.creLfpp = creLfpp;
//        this.creLned = creLned;
//        this.creLtir = creLtir;
//        this.creLnpg = creLnpg;
//        this.creLpor = creLpor;
//        this.creLdob = creLdob;
//        this.creLdes = creLdes;
//        this.creLnre = creLnre;
//        this.creLfre = creLfre;
//        this.creLisbn = creLisbn;
//        this.tiporegistropersonas = tiporegistropersonas;
//        this.actividadclaseregistros = actividadclaseregistros;
//        this.propiedads = propiedads;
//        this.registros = registros;
//        this.tiposcontratos = tiposcontratos;
//    }

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "CRE_ID", unique = true, nullable = false)
    public int getCreId() {
        return this.creId;
    }

    public void setCreId(int creId) {
        this.creId = creId;
    }

    @Column(name = "CRE_NOM", length = 100)
    public String getCreNom() {
        return this.creNom;
    }

    public void setCreNom(String creNom) {
        this.creNom = creNom;
    }

    @Column(name = "CRE_COD", length = 10)
    public String getCreCod() {
        return this.creCod;
    }

    public void setCreCod(String creCod) {
        this.creCod = creCod;
    }

    @Column(name = "CRE_FRM", nullable = false)
    public short getCreFrm() {
        return this.creFrm;
    }

    public void setCreFrm(short creFrm) {
        this.creFrm = creFrm;
    }

    @Column(name = "CRE_LACT")
    public Boolean getCreLact() {
        return this.creLact;
    }

    public void setCreLact(Boolean creLact) {
        this.creLact = creLact;
    }

    @Column(name = "CRE_LFTE")
    public Boolean getCreLfte() {
        return this.creLfte;
    }

    public void setCreLfte(Boolean creLfte) {
        this.creLfte = creLfte;
    }

    @Column(name = "CRE_LFPP")
    public Boolean getCreLfpp() {
        return this.creLfpp;
    }

    public void setCreLfpp(Boolean creLfpp) {
        this.creLfpp = creLfpp;
    }

    @Column(name = "CRE_LNED")
    public Boolean getCreLned() {
        return this.creLned;
    }

    public void setCreLned(Boolean creLned) {
        this.creLned = creLned;
    }

    @Column(name = "CRE_LTIR")
    public Boolean getCreLtir() {
        return this.creLtir;
    }

    public void setCreLtir(Boolean creLtir) {
        this.creLtir = creLtir;
    }

    @Column(name = "CRE_LNPG")
    public Boolean getCreLnpg() {
        return this.creLnpg;
    }

    public void setCreLnpg(Boolean creLnpg) {
        this.creLnpg = creLnpg;
    }

    @Column(name = "CRE_LPOR")
    public Boolean getCreLpor() {
        return this.creLpor;
    }

    public void setCreLpor(Boolean creLpor) {
        this.creLpor = creLpor;
    }

    @Column(name = "CRE_LDOB")
    public Boolean getCreLdob() {
        return this.creLdob;
    }

    public void setCreLdob(Boolean creLdob) {
        this.creLdob = creLdob;
    }

    @Column(name = "CRE_LDES")
    public Boolean getCreLdes() {
        return this.creLdes;
    }

    public void setCreLdes(Boolean creLdes) {
        this.creLdes = creLdes;
    }

    @Column(name = "CRE_LNRE")
    public Boolean getCreLnre() {
        return this.creLnre;
    }

    public void setCreLnre(Boolean creLnre) {
        this.creLnre = creLnre;
    }

    @Column(name = "CRE_LFRE")
    public Boolean getCreLfre() {
        return this.creLfre;
    }

    public void setCreLfre(Boolean creLfre) {
        this.creLfre = creLfre;
    }

    @Column(name = "CRE_LISBN")
    public Boolean getCreLisbn() {
        return this.creLisbn;
    }

    public void setCreLisbn(Boolean creLisbn) {
        this.creLisbn = creLisbn;
    }

//    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JoinTable(name = "CLASEREGISTROPERSONA", schema = "dbo", catalog = "AUTOR", joinColumns = {
//        @JoinColumn(name = "CRP_CRE", nullable = false, updatable = false)}, inverseJoinColumns = {
//        @JoinColumn(name = "CRP_TRP", nullable = false, updatable = false)})
//    public Set<Tiporegistropersona> getTiporegistropersonas() {
//        return this.tiporegistropersonas;
//    }
//
//    public void setTiporegistropersonas(Set<Tiporegistropersona> tiporegistropersonas) {
//        this.tiporegistropersonas = tiporegistropersonas;
//    }
//
//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "claseregistro")
//    public Set<Actividadclaseregistro> getActividadclaseregistros() {
//        return this.actividadclaseregistros;
//    }
//
//    public void setActividadclaseregistros(Set<Actividadclaseregistro> actividadclaseregistros) {
//        this.actividadclaseregistros = actividadclaseregistros;
//    }
//
//    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JoinTable(name = "PROPIEDADREGISTRO", schema = "dbo", catalog = "AUTOR", joinColumns = {
//        @JoinColumn(name = "PRE_CRE", nullable = false, updatable = false)}, inverseJoinColumns = {
//        @JoinColumn(name = "PRE_PRO", nullable = false, updatable = false)})
//    public Set<Propiedad> getPropiedads() {
//        return this.propiedads;
//    }
//
//    public void setPropiedads(Set<Propiedad> propiedads) {
//        this.propiedads = propiedads;
//    }
//
//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "claseregistro")
//    public Set<Registro> getRegistros() {
//        return this.registros;
//    }
//
//    public void setRegistros(Set<Registro> registros) {
//        this.registros = registros;
//    }
//
//    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JoinTable(name = "REGISTROCONTRATO", schema = "dbo", catalog = "AUTOR", joinColumns = {
//        @JoinColumn(name = "RGC_CRE", nullable = false, updatable = false)}, inverseJoinColumns = {
//        @JoinColumn(name = "RGC_TCO", nullable = false, updatable = false)})
//    public Set<Tiposcontrato> getTiposcontratos() {
//        return this.tiposcontratos;
//    }
//
//    public void setTiposcontratos(Set<Tiposcontrato> tiposcontratos) {
//        this.tiposcontratos = tiposcontratos;
//    }
}
