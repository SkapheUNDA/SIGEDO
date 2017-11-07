package la.netco.persistencia.dto.commons;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

import la.netco.core.persistencia.vo.Schemas;
import la.netco.core.persistencia.vo.Usuario;


@Entity
@Table(name = "SEGUIMIENTOENTRADA", schema = Schemas.DBO_SCHEMA	)
@NamedQuery(name=Seguimientoentrada.NAMED_QUERY_GET_BY_ENTRADA,query=Seguimientoentrada.QUERY_GET_BY_ENTRADA)
public class Seguimientoentrada implements Serializable {


	private static final long serialVersionUID = 1L;
	private int senId;
    private Estado estadoBySenEst;
    private Depend depend;
    private Evento evento;
    private Estado estadoBySenEstini;
    private Entrada entrada;
    private Usuario usuarioBySenUsrini;
    private Usuario usuarioBySenUsr;
    private Date senFec;
    private String senDes;
    private String senObs;
    private Date senFecorg;

    
	/**
	 * Estado de creacion para un nueva entrada.
	 */
	public static final String SEGUIMIENTO_CREACION	 = "CREACION";
	
	public static final String SEGUIMIENTO_DESCARGUE_EN_CERO	 = "DESCARGUE EN CERO";
	
	public static final String SEGUIMIENTO_CAMBIO_ETAPA	 = "CAMBIO DE ETAPA";
    
    public Seguimientoentrada() {
    }

    public Seguimientoentrada(int senId, Estado estadoBySenEst) {
        this.senId = senId;
        this.estadoBySenEst = estadoBySenEst;
    }

    public Seguimientoentrada(int senId, Estado estadoBySenEst, Depend depend, Evento evento, Estado estadoBySenEstini, Entrada entrada, Usuario usuarioBySenUsrini, Usuario usuarioBySenUsr, Date senFec, String senDes, String senObs, Date senFecorg) {
        this.senId = senId;
        this.estadoBySenEst = estadoBySenEst;
        this.depend = depend;
        this.evento = evento;
        this.estadoBySenEstini = estadoBySenEstini;
        this.entrada = entrada;
        this.usuarioBySenUsrini = usuarioBySenUsrini;
        this.usuarioBySenUsr = usuarioBySenUsr;
        this.senFec = senFec;
        this.senDes = senDes;
        this.senObs = senObs;
        this.senFecorg = senFecorg;
    }

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "SEN_ID", unique = true, nullable = false)
    public int getSenId() {
        return this.senId;
    }

    public void setSenId(int senId) {
        this.senId = senId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SEN_EST", nullable = false)
    public Estado getEstadoBySenEst() {
        return this.estadoBySenEst;
    }

    public void setEstadoBySenEst(Estado estadoBySenEst) {
        this.estadoBySenEst = estadoBySenEst;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SEN_DEP")
    public Depend getDepend() {
        return this.depend;
    }

    public void setDepend(Depend depend) {
        this.depend = depend;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SEN_EVE")
    public Evento getEvento() {
        return this.evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SEN_ESTINI")
    public Estado getEstadoBySenEstini() {
        return this.estadoBySenEstini;
    }

    public void setEstadoBySenEstini(Estado estadoBySenEstini) {
        this.estadoBySenEstini = estadoBySenEstini;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SEN_ENT")
    public Entrada getEntrada() {
        return this.entrada;
    }

    public void setEntrada(Entrada entrada) {
        this.entrada = entrada;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SEN_USRINI")
    public Usuario getUsuarioBySenUsrini() {
        return this.usuarioBySenUsrini;
    }

    public void setUsuarioBySenUsrini(Usuario usuarioBySenUsrini) {
        this.usuarioBySenUsrini = usuarioBySenUsrini;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SEN_USR")
    public Usuario getUsuarioBySenUsr() {
        return this.usuarioBySenUsr;
    }

    public void setUsuarioBySenUsr(Usuario usuarioBySenUsr) {
        this.usuarioBySenUsr = usuarioBySenUsr;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "SEN_FEC", length = 23)
    public Date getSenFec() {
        return this.senFec;
    }

    public void setSenFec(Date senFec) {
        this.senFec = senFec;
    }

    @Column(name = "SEN_DES", length = 254)
    public String getSenDes() {
        return this.senDes;
    }

    public void setSenDes(String senDes) {
        this.senDes = senDes;
    }

    @Column(name = "SEN_OBS", length = 254)
    public String getSenObs() {
        return this.senObs;
    }

    public void setSenObs(String senObs) {
        this.senObs = senObs;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "SEN_FECORG", length = 23)
    public Date getSenFecorg() {
        return this.senFecorg;
    }

    public void setSenFecorg(Date senFecorg) {
        this.senFecorg = senFecorg;
    }
    
    public static final String NAMED_QUERY_GET_BY_ENTRADA = "getSeguimientoByEntrada";
    public static final String QUERY_GET_BY_ENTRADA = "FROM Seguimientoentrada WHERE  entrada.entId = ? order by  senId desc ";
    
}
