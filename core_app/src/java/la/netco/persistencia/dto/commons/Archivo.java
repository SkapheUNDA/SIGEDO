package la.netco.persistencia.dto.commons;

import javax.persistence.*;

import la.netco.core.persistencia.vo.Schemas;


@Entity
@Table(name = "ARCHIVO", schema = Schemas.DBO_SCHEMA)
public class Archivo implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private int arcId;
    private Salida salida;
    private Expediente expediente;
    private Entrada entrada;
    private String arcNom;
    private String arcFil;

    public Archivo() {
    }

    public Archivo(int arcId) {
        this.arcId = arcId;
    }

    public Archivo(int arcId, Salida salida, Expediente expediente, Entrada entrada, String arcNom, String arcFil) {
        this.arcId = arcId;
        this.salida = salida;
        this.expediente = expediente;
        this.entrada = entrada;
        this.arcNom = arcNom;
        this.arcFil = arcFil;
    }

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "ARC_ID", unique = true, nullable = false)
    public int getArcId() {
        return this.arcId;
    }

    public void setArcId(int arcId) {
        this.arcId = arcId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ARC_SAL")
    public Salida getSalida() {
        return this.salida;
    }

    public void setSalida(Salida salida) {
        this.salida = salida;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ARC_EXP")
    public Expediente getExpediente() {
        return this.expediente;
    }

    public void setExpediente(Expediente expediente) {
        this.expediente = expediente;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ARC_ENT")
    public Entrada getEntrada() {
        return this.entrada;
    }

    public void setEntrada(Entrada entrada) {
        this.entrada = entrada;
    }

    @Column(name = "ARC_NOM")
    public String getArcNom() {
        return this.arcNom;
    }

    public void setArcNom(String arcNom) {
        this.arcNom = arcNom;
    }

    @Column(name = "ARC_FIL")
    public String getArcFil() {
        return this.arcFil;
    }

    public void setArcFil(String arcFil) {
        this.arcFil = arcFil;
    }
}
