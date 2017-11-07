package la.netco.persistencia.dto.commons;

import java.io.Serializable;
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
@Table(name = "SEGUIMIENTOSALIDA", schema = Schemas.DBO_SCHEMA	)
@NamedQuery(name=Seguimientosalida.NAMED_QUERY_GET_BY_SALIDA,query=Seguimientosalida.QUERY_GET_BY_SALIDA)
public class Seguimientosalida implements Serializable {


	private static final long serialVersionUID = 1L;
	private int ssaId;
    private Estado estadoBySsaEstini;
    private Estado estadoBySsaEst;
    private Salida salida;
    private Depend depend;
    private Usuario usuarioBySsaUsrini;
    private Usuario usuarioBySsaUsr;
    private Evento evento;
    private Date ssaFec;
    private String ssaDes;
    private String ssaObs;
    private Date ssaFecorg;
    

	public static final String SEGUIMIENTO_ENTREGADO	 = "ENTREGADO";

    public Seguimientosalida() {
    }

    public Seguimientosalida(int ssaId) {
        this.ssaId = ssaId;
    }

    public Seguimientosalida(int ssaId, Estado estadoBySsaEstini, Estado estadoBySsaEst, Salida salida, Depend depend, Usuario usuarioBySsaUsrini, Usuario usuarioBySsaUsr, Evento evento, Date ssaFec, String ssaDes, String ssaObs, Date ssaFecorg) {
        this.ssaId = ssaId;
        this.estadoBySsaEstini = estadoBySsaEstini;
        this.estadoBySsaEst = estadoBySsaEst;
        this.salida = salida;
        this.depend = depend;
        this.usuarioBySsaUsrini = usuarioBySsaUsrini;
        this.usuarioBySsaUsr = usuarioBySsaUsr;
        this.evento = evento;
        this.ssaFec = ssaFec;
        this.ssaDes = ssaDes;
        this.ssaObs = ssaObs;
        this.ssaFecorg = ssaFecorg;
    }

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "SSA_ID", unique = true, nullable = false)
    public int getSsaId() {
        return this.ssaId;
    }

    public void setSsaId(int ssaId) {
        this.ssaId = ssaId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SSA_ESTINI")
    public Estado getEstadoBySsaEstini() {
        return this.estadoBySsaEstini;
    }

    public void setEstadoBySsaEstini(Estado estadoBySsaEstini) {
        this.estadoBySsaEstini = estadoBySsaEstini;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SSA_EST")
    public Estado getEstadoBySsaEst() {
        return this.estadoBySsaEst;
    }

    public void setEstadoBySsaEst(Estado estadoBySsaEst) {
        this.estadoBySsaEst = estadoBySsaEst;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SSA_SAL")
    public Salida getSalida() {
        return this.salida;
    }

    public void setSalida(Salida salida) {
        this.salida = salida;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SSA_DEP")
    public Depend getDepend() {
        return this.depend;
    }

    public void setDepend(Depend depend) {
        this.depend = depend;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SSA_USRINI")
    public Usuario getUsuarioBySsaUsrini() {
        return this.usuarioBySsaUsrini;
    }

    public void setUsuarioBySsaUsrini(Usuario usuarioBySsaUsrini) {
        this.usuarioBySsaUsrini = usuarioBySsaUsrini;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SSA_USR")
    public Usuario getUsuarioBySsaUsr() {
        return this.usuarioBySsaUsr;
    }

    public void setUsuarioBySsaUsr(Usuario usuarioBySsaUsr) {
        this.usuarioBySsaUsr = usuarioBySsaUsr;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SSA_EVE")
    public Evento getEvento() {
        return this.evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "SSA_FEC", length = 23)
    public Date getSsaFec() {
        return this.ssaFec;
    }

    public void setSsaFec(Date ssaFec) {
        this.ssaFec = ssaFec;
    }

    @Column(name = "SSA_DES", length = 254)
    public String getSsaDes() {
        return this.ssaDes;
    }

    public void setSsaDes(String ssaDes) {
        this.ssaDes = ssaDes;
    }

    @Column(name = "SSA_OBS", length = 254)
    public String getSsaObs() {
        return this.ssaObs;
    }

    public void setSsaObs(String ssaObs) {
        this.ssaObs = ssaObs;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "SSA_FECORG", length = 23)
    public Date getSsaFecorg() {
        return this.ssaFecorg;
    }

    public void setSsaFecorg(Date ssaFecorg) {
        this.ssaFecorg = ssaFecorg;
    }
    
    public static final String NAMED_QUERY_GET_BY_SALIDA = "getSeguimientoBySalida";
    public static final String QUERY_GET_BY_SALIDA = "FROM Seguimientosalida WHERE  salida.salId = ? order by  ssaId desc ";
    
}
