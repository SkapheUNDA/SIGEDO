package la.netco.persistencia.dto.commons;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import la.netco.core.persistencia.vo.Schemas;
import la.netco.core.persistencia.vo.Usuario;


@Entity
@Table(name = "SEGUIMIENTOEXPEDIENTE", schema = Schemas.DBO_SCHEMA)
@NamedQuery(name=Seguimientoexpediente.NAMED_QUERY_GET_BY_EXPEDIENTE,query=Seguimientoexpediente.QUERY_GET_BY_EXPEDIENTE)
public class Seguimientoexpediente implements java.io.Serializable {

    
	private static final long serialVersionUID = 1L;
	private int sgeId;
    private Evento evento;
    private Estado estadoBySgeEstini;
    private Estado estadoBySgeEst;
    private Expediente expediente;
    private Usuario usuario;
    private Short sgeDep;
    private Usuario sgeUsr;
    private Date sgeFec;
    private String sgeDes;
    private String sgeObs;
    private Date sgeFecorg;
    private Integer sgeDev;

    public Seguimientoexpediente() {
    }

    public Seguimientoexpediente(int sgeId) {
        this.sgeId = sgeId;
    }

    public Seguimientoexpediente(int sgeId, Evento evento, Estado estadoBySgeEstini, Estado estadoBySgeEst, Expediente expediente, Usuario usuario, Short sgeDep, Usuario sgeUsr, Date sgeFec, String sgeDes, String sgeObs, Date sgeFecorg, Integer sgeDev) {
        this.sgeId = sgeId;
        this.evento = evento;
        this.estadoBySgeEstini = estadoBySgeEstini;
        this.estadoBySgeEst = estadoBySgeEst;
        this.expediente = expediente;
        this.usuario = usuario;
        this.sgeDep = sgeDep;
        this.sgeUsr = sgeUsr;
        this.sgeFec = sgeFec;
        this.sgeDes = sgeDes;
        this.sgeObs = sgeObs;
        this.sgeFecorg = sgeFecorg;
        this.sgeDev = sgeDev;
    }

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "SGE_ID", unique = true, nullable = false)
    public int getSgeId() {
        return this.sgeId;
    }

    public void setSgeId(int sgeId) {
        this.sgeId = sgeId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SGE_EVE")
    public Evento getEvento() {
        return this.evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SGE_ESTINI")
    public Estado getEstadoBySgeEstini() {
        return this.estadoBySgeEstini;
    }

    public void setEstadoBySgeEstini(Estado estadoBySgeEstini) {
        this.estadoBySgeEstini = estadoBySgeEstini;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SGE_EST")
    public Estado getEstadoBySgeEst() {
        return this.estadoBySgeEst;
    }

    public void setEstadoBySgeEst(Estado estadoBySgeEst) {
        this.estadoBySgeEst = estadoBySgeEst;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SGE_EXP")
    public Expediente getExpediente() {
        return this.expediente;
    }

    public void setExpediente(Expediente expediente) {
        this.expediente = expediente;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SGE_USRINI")
    public Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Column(name = "SGE_DEP")
    public Short getSgeDep() {
        return this.sgeDep;
    }

    public void setSgeDep(Short sgeDep) {
        this.sgeDep = sgeDep;
    }
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SGE_USR")
    public Usuario getSgeUsr() {
        return this.sgeUsr;
    }

    public void setSgeUsr(Usuario sgeUsr) {
        this.sgeUsr = sgeUsr;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "SGE_FEC", length = 23)
    public Date getSgeFec() {
        return this.sgeFec;
    }

    public void setSgeFec(Date sgeFec) {
        this.sgeFec = sgeFec;
    }

    @Column(name = "SGE_DES", length = 254)
    public String getSgeDes() {
        return this.sgeDes;
    }

    public void setSgeDes(String sgeDes) {
        this.sgeDes = sgeDes;
    }

    @Column(name = "SGE_OBS", length = 254)
    public String getSgeObs() {
        return this.sgeObs;
    }

    public void setSgeObs(String sgeObs) {
        this.sgeObs = sgeObs;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "SGE_FECORG", length = 23)
    public Date getSgeFecorg() {
        return this.sgeFecorg;
    }

    public void setSgeFecorg(Date sgeFecorg) {
        this.sgeFecorg = sgeFecorg;
    }

    @Column(name = "SGE_DEV")
    public Integer getSgeDev() {
        return this.sgeDev;
    }

    public void setSgeDev(Integer sgeDev) {
        this.sgeDev = sgeDev;
    }
    
    public static final String NAMED_QUERY_GET_BY_EXPEDIENTE = "getSeguimientoByExpediente";
    public static final String QUERY_GET_BY_EXPEDIENTE = "FROM Seguimientoexpediente WHERE  expediente.expId = ? order by  sgeId desc ";

}
