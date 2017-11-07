package la.netco.persistencia.dto.commons;

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

import la.netco.core.persistencia.vo.Schemas;


@Entity
@Table(name = "EXPEDIENTECORRESPOND", schema = Schemas.DBO_SCHEMA)
@NamedQuery(name=Expedientecorrespond.NAMED_QUERY_FIND_BY_ENTRADA, query=Expedientecorrespond.QUERY_FIND_BY_ENTRADA)
public class Expedientecorrespond implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	private int excId;
    private Salida salida;
    private Expediente expediente;
    private Entrada entrada;
    private byte excTip;
    private Boolean excCin;
    private Boolean excFin;

    public Expedientecorrespond() {
    }

    public Expedientecorrespond(int excId, Expediente expediente, byte excTip) {
        this.excId = excId;
        this.expediente = expediente;
        this.excTip = excTip;
    }

    public Expedientecorrespond(int excId, Salida salida, Expediente expediente, Entrada entrada, byte excTip, Boolean excCin, Boolean excFin) {
        this.excId = excId;
        this.salida = salida;
        this.expediente = expediente;
        this.entrada = entrada;
        this.excTip = excTip;
        this.excCin = excCin;
        this.excFin = excFin;
    }

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "EXC_ID", unique = true, nullable = false)
    public int getExcId() {
        return this.excId;
    }

    public void setExcId(int excId) {
        this.excId = excId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EXC_SAL", nullable = true)
    public Salida getSalida() {
        return this.salida;
    }

    public void setSalida(Salida salida) {
        this.salida = salida;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EXC_EXP", nullable = false)
    public Expediente getExpediente() {
        return this.expediente;
    }

    public void setExpediente(Expediente expediente) {
        this.expediente = expediente;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EXC_ENT", nullable = true)
    public Entrada getEntrada() {
        return this.entrada;
    }

    public void setEntrada(Entrada entrada) {
        this.entrada = entrada;
    }

    @Column(name = "EXC_TIP", nullable = false)
    public byte getExcTip() {
        return this.excTip;
    }

    public void setExcTip(byte excTip) {
        this.excTip = excTip;
    }

    @Column(name = "EXC_CIN")
    public Boolean getExcCin() {
        return this.excCin;
    }

    public void setExcCin(Boolean excCin) {
        this.excCin = excCin;
    }

    @Column(name = "EXC_FIN")
    public Boolean getExcFin() {
        return this.excFin;
    }

    public void setExcFin(Boolean excFin) {
        this.excFin = excFin;
    }
    
	public static final String NAMED_QUERY_FIND_BY_ENTRADA  = "findExpedientecorrespondByEntrada";
	public static final String QUERY_FIND_BY_ENTRADA 	= "FROM Expedientecorrespond expedientecorres  WHERE expedientecorres.entrada.entId = ? ";
   
}
