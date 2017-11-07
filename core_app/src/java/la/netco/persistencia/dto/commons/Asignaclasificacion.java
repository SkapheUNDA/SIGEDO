package la.netco.persistencia.dto.commons;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import la.netco.core.persistencia.vo.Schemas;



@Entity
@Table(name = "ASIGNACLASIFICACION", schema = Schemas.DBO_SCHEMA)
@NamedQuery(name=Asignaclasificacion.NAMED_QUERY_BY_ID,query=Asignaclasificacion.QUERY_BY_ID)
public class Asignaclasificacion implements Serializable {


	private static final long serialVersionUID = 1L;
	private AsignaclasificacionId id;
    private Clasificacion clasificacion;
    private Depend depend;
    private boolean asgLrta;
    private Short asgTie;
    private boolean asgLasu;
    private boolean asgLmed;
    private boolean asgLper;
    private boolean asgLdom;
    private boolean asgLorg;
    private boolean asgLrep;
    private boolean asgLobs;
    private boolean asgRasu;
    private boolean asgRmed;
    private boolean asgRper;
    private boolean asgRdom;
    private boolean asgRorg;
    private boolean asgRrep;
    private boolean asgRobs;
    private String asgDasu;
    private Short asgDmed;
    private String asgDndo;
    private Short asgDtdo;
    private String asgDnom;
    private String asgDpap;
    private String asgDsap;
    private Short asgDnac;
    private String asgDdir;
    private Short asgDlug;
    private String asgDtel;
    private String asgDfax;
    private String asgDcel;
    private String asgDnrf;
    private String asgDnrd;
    private String asgDide;
    private String asgDent;
    private String asgDobs;
    private boolean asgCasu;
    private boolean asgCmed;
    private boolean asgCper;
    private boolean asgCdom;
    private boolean asgCorg;
    private boolean asgCrep;
    private boolean asgCobs;
    private boolean asgAct;
    private boolean asgLndo;
    private boolean asgLtdo;
    private boolean asgLnom;
    private boolean asgLpap;
    private boolean asgLsap;
    private boolean asgLnac;
    private boolean asgLdir;
    private boolean asgLlug;
    private boolean asgLtel;
    private boolean asgLfax;
    private boolean asgLcel;
    private boolean asgLnrf;
    private boolean asgLnrd;
    private boolean asgLide;
    private boolean asgLent;
    private boolean asgRndo;
    private boolean asgRtdo;
    private boolean asgRnom;
    private boolean asgRpap;
    private boolean asgRsap;
    private boolean asgRnac;
    private boolean asgRdir;
    private boolean asgRlug;
    private boolean asgRtel;
    private boolean asgRfax;
    private boolean asgRcel;
    private boolean asgRnrf;
    private boolean asgRnrd;
    private boolean asgRide;
    private boolean asgRent;
    private boolean asgCtdo;
    private boolean asgCndo;
    private boolean asgCnom;
    private boolean asgCpap;
    private boolean asgCsap;
    private boolean asgCnac;
    private boolean asgCdir;
    private boolean asgClug;
    private boolean asgCtel;
    private boolean asgCfax;
    private boolean asgCcel;
    private boolean asgCnrd;
    private boolean asgCnrf;
    private boolean asgCide;
    private boolean asgCent;

    public Asignaclasificacion() {
    }

    public Asignaclasificacion(AsignaclasificacionId id, Clasificacion clasificacion, Depend depend, boolean asgLrta, boolean asgLasu, boolean asgLmed, boolean asgLper, boolean asgLdom, boolean asgLorg, boolean asgLrep, boolean asgLobs, boolean asgRasu, boolean asgRmed, boolean asgRper, boolean asgRdom, boolean asgRorg, boolean asgRrep, boolean asgRobs, boolean asgCasu, boolean asgCmed, boolean asgCper, boolean asgCdom, boolean asgCorg, boolean asgCrep, boolean asgCobs, boolean asgAct, boolean asgLndo, boolean asgLtdo, boolean asgLnom, boolean asgLpap, boolean asgLsap, boolean asgLnac, boolean asgLdir, boolean asgLlug, boolean asgLtel, boolean asgLfax, boolean asgLcel, boolean asgLnrf, boolean asgLnrd, boolean asgLide, boolean asgLent, boolean asgRndo, boolean asgRtdo, boolean asgRnom, boolean asgRpap, boolean asgRsap, boolean asgRnac, boolean asgRdir, boolean asgRlug, boolean asgRtel, boolean asgRfax, boolean asgRcel, boolean asgRnrf, boolean asgRnrd, boolean asgRide, boolean asgRent, boolean asgCtdo, boolean asgCndo, boolean asgCnom, boolean asgCpap, boolean asgCsap, boolean asgCnac, boolean asgCdir, boolean asgClug, boolean asgCtel, boolean asgCfax, boolean asgCcel, boolean asgCnrd, boolean asgCnrf, boolean asgCide, boolean asgCent) {
        this.id = id;
        this.clasificacion = clasificacion;
        this.depend = depend;
        this.asgLrta = asgLrta;
        this.asgLasu = asgLasu;
        this.asgLmed = asgLmed;
        this.asgLper = asgLper;
        this.asgLdom = asgLdom;
        this.asgLorg = asgLorg;
        this.asgLrep = asgLrep;
        this.asgLobs = asgLobs;
        this.asgRasu = asgRasu;
        this.asgRmed = asgRmed;
        this.asgRper = asgRper;
        this.asgRdom = asgRdom;
        this.asgRorg = asgRorg;
        this.asgRrep = asgRrep;
        this.asgRobs = asgRobs;
        this.asgCasu = asgCasu;
        this.asgCmed = asgCmed;
        this.asgCper = asgCper;
        this.asgCdom = asgCdom;
        this.asgCorg = asgCorg;
        this.asgCrep = asgCrep;
        this.asgCobs = asgCobs;
        this.asgAct = asgAct;
        this.asgLndo = asgLndo;
        this.asgLtdo = asgLtdo;
        this.asgLnom = asgLnom;
        this.asgLpap = asgLpap;
        this.asgLsap = asgLsap;
        this.asgLnac = asgLnac;
        this.asgLdir = asgLdir;
        this.asgLlug = asgLlug;
        this.asgLtel = asgLtel;
        this.asgLfax = asgLfax;
        this.asgLcel = asgLcel;
        this.asgLnrf = asgLnrf;
        this.asgLnrd = asgLnrd;
        this.asgLide = asgLide;
        this.asgLent = asgLent;
        this.asgRndo = asgRndo;
        this.asgRtdo = asgRtdo;
        this.asgRnom = asgRnom;
        this.asgRpap = asgRpap;
        this.asgRsap = asgRsap;
        this.asgRnac = asgRnac;
        this.asgRdir = asgRdir;
        this.asgRlug = asgRlug;
        this.asgRtel = asgRtel;
        this.asgRfax = asgRfax;
        this.asgRcel = asgRcel;
        this.asgRnrf = asgRnrf;
        this.asgRnrd = asgRnrd;
        this.asgRide = asgRide;
        this.asgRent = asgRent;
        this.asgCtdo = asgCtdo;
        this.asgCndo = asgCndo;
        this.asgCnom = asgCnom;
        this.asgCpap = asgCpap;
        this.asgCsap = asgCsap;
        this.asgCnac = asgCnac;
        this.asgCdir = asgCdir;
        this.asgClug = asgClug;
        this.asgCtel = asgCtel;
        this.asgCfax = asgCfax;
        this.asgCcel = asgCcel;
        this.asgCnrd = asgCnrd;
        this.asgCnrf = asgCnrf;
        this.asgCide = asgCide;
        this.asgCent = asgCent;
    }

    public Asignaclasificacion(AsignaclasificacionId id, Clasificacion clasificacion, Depend depend, boolean asgLrta, Short asgTie, boolean asgLasu, boolean asgLmed, boolean asgLper, boolean asgLdom, boolean asgLorg, boolean asgLrep, boolean asgLobs, boolean asgRasu, boolean asgRmed, boolean asgRper, boolean asgRdom, boolean asgRorg, boolean asgRrep, boolean asgRobs, String asgDasu, Short asgDmed, String asgDndo, Short asgDtdo, String asgDnom, String asgDpap, String asgDsap, Short asgDnac, String asgDdir, Short asgDlug, String asgDtel, String asgDfax, String asgDcel, String asgDnrf, String asgDnrd, String asgDide, String asgDent, String asgDobs, boolean asgCasu, boolean asgCmed, boolean asgCper, boolean asgCdom, boolean asgCorg, boolean asgCrep, boolean asgCobs, boolean asgAct, boolean asgLndo, boolean asgLtdo, boolean asgLnom, boolean asgLpap, boolean asgLsap, boolean asgLnac, boolean asgLdir, boolean asgLlug, boolean asgLtel, boolean asgLfax, boolean asgLcel, boolean asgLnrf, boolean asgLnrd, boolean asgLide, boolean asgLent, boolean asgRndo, boolean asgRtdo, boolean asgRnom, boolean asgRpap, boolean asgRsap, boolean asgRnac, boolean asgRdir, boolean asgRlug, boolean asgRtel, boolean asgRfax, boolean asgRcel, boolean asgRnrf, boolean asgRnrd, boolean asgRide, boolean asgRent, boolean asgCtdo, boolean asgCndo, boolean asgCnom, boolean asgCpap, boolean asgCsap, boolean asgCnac, boolean asgCdir, boolean asgClug, boolean asgCtel, boolean asgCfax, boolean asgCcel, boolean asgCnrd, boolean asgCnrf, boolean asgCide, boolean asgCent) {
        this.id = id;
        this.clasificacion = clasificacion;
        this.depend = depend;
        this.asgLrta = asgLrta;
        this.asgTie = asgTie;
        this.asgLasu = asgLasu;
        this.asgLmed = asgLmed;
        this.asgLper = asgLper;
        this.asgLdom = asgLdom;
        this.asgLorg = asgLorg;
        this.asgLrep = asgLrep;
        this.asgLobs = asgLobs;
        this.asgRasu = asgRasu;
        this.asgRmed = asgRmed;
        this.asgRper = asgRper;
        this.asgRdom = asgRdom;
        this.asgRorg = asgRorg;
        this.asgRrep = asgRrep;
        this.asgRobs = asgRobs;
        this.asgDasu = asgDasu;
        this.asgDmed = asgDmed;
        this.asgDndo = asgDndo;
        this.asgDtdo = asgDtdo;
        this.asgDnom = asgDnom;
        this.asgDpap = asgDpap;
        this.asgDsap = asgDsap;
        this.asgDnac = asgDnac;
        this.asgDdir = asgDdir;
        this.asgDlug = asgDlug;
        this.asgDtel = asgDtel;
        this.asgDfax = asgDfax;
        this.asgDcel = asgDcel;
        this.asgDnrf = asgDnrf;
        this.asgDnrd = asgDnrd;
        this.asgDide = asgDide;
        this.asgDent = asgDent;
        this.asgDobs = asgDobs;
        this.asgCasu = asgCasu;
        this.asgCmed = asgCmed;
        this.asgCper = asgCper;
        this.asgCdom = asgCdom;
        this.asgCorg = asgCorg;
        this.asgCrep = asgCrep;
        this.asgCobs = asgCobs;
        this.asgAct = asgAct;
        this.asgLndo = asgLndo;
        this.asgLtdo = asgLtdo;
        this.asgLnom = asgLnom;
        this.asgLpap = asgLpap;
        this.asgLsap = asgLsap;
        this.asgLnac = asgLnac;
        this.asgLdir = asgLdir;
        this.asgLlug = asgLlug;
        this.asgLtel = asgLtel;
        this.asgLfax = asgLfax;
        this.asgLcel = asgLcel;
        this.asgLnrf = asgLnrf;
        this.asgLnrd = asgLnrd;
        this.asgLide = asgLide;
        this.asgLent = asgLent;
        this.asgRndo = asgRndo;
        this.asgRtdo = asgRtdo;
        this.asgRnom = asgRnom;
        this.asgRpap = asgRpap;
        this.asgRsap = asgRsap;
        this.asgRnac = asgRnac;
        this.asgRdir = asgRdir;
        this.asgRlug = asgRlug;
        this.asgRtel = asgRtel;
        this.asgRfax = asgRfax;
        this.asgRcel = asgRcel;
        this.asgRnrf = asgRnrf;
        this.asgRnrd = asgRnrd;
        this.asgRide = asgRide;
        this.asgRent = asgRent;
        this.asgCtdo = asgCtdo;
        this.asgCndo = asgCndo;
        this.asgCnom = asgCnom;
        this.asgCpap = asgCpap;
        this.asgCsap = asgCsap;
        this.asgCnac = asgCnac;
        this.asgCdir = asgCdir;
        this.asgClug = asgClug;
        this.asgCtel = asgCtel;
        this.asgCfax = asgCfax;
        this.asgCcel = asgCcel;
        this.asgCnrd = asgCnrd;
        this.asgCnrf = asgCnrf;
        this.asgCide = asgCide;
        this.asgCent = asgCent;
    }

    @EmbeddedId
    @AttributeOverrides({
        @AttributeOverride(name = "asgCla", column =
        @Column(name = "ASG_CLA", nullable = false)),
        @AttributeOverride(name = "asgDep", column =
        @Column(name = "ASG_DEP", nullable = false))})
    public AsignaclasificacionId getId() {
        return this.id;
    }

    public void setId(AsignaclasificacionId id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ASG_CLA", nullable = false, insertable = false, updatable = false)
    public Clasificacion getClasificacion() {
        return this.clasificacion;
    }

    public void setClasificacion(Clasificacion clasificacion) {
        this.clasificacion = clasificacion;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ASG_DEP", nullable = false, insertable = false, updatable = false)
    public Depend getDepend() {
        return this.depend;
    }

    public void setDepend(Depend depend) {
        this.depend = depend;
    }

    @Column(name = "ASG_LRTA", nullable = false)
    public boolean isAsgLrta() {
        return this.asgLrta;
    }

    public void setAsgLrta(boolean asgLrta) {
        this.asgLrta = asgLrta;
    }

    @Column(name = "ASG_TIE")
    public Short getAsgTie() {
        return this.asgTie;
    }

    public void setAsgTie(Short asgTie) {
        this.asgTie = asgTie;
    }

    @Column(name = "ASG_LASU", nullable = false)
    public boolean isAsgLasu() {
        return this.asgLasu;
    }

    public void setAsgLasu(boolean asgLasu) {
        this.asgLasu = asgLasu;
    }

    @Column(name = "ASG_LMED", nullable = false)
    public boolean isAsgLmed() {
        return this.asgLmed;
    }

    public void setAsgLmed(boolean asgLmed) {
        this.asgLmed = asgLmed;
    }

    @Column(name = "ASG_LPER", nullable = false)
    public boolean isAsgLper() {
        return this.asgLper;
    }

    public void setAsgLper(boolean asgLper) {
        this.asgLper = asgLper;
    }

    @Column(name = "ASG_LDOM", nullable = false)
    public boolean isAsgLdom() {
        return this.asgLdom;
    }

    public void setAsgLdom(boolean asgLdom) {
        this.asgLdom = asgLdom;
    }

    @Column(name = "ASG_LORG", nullable = false)
    public boolean isAsgLorg() {
        return this.asgLorg;
    }

    public void setAsgLorg(boolean asgLorg) {
        this.asgLorg = asgLorg;
    }

    @Column(name = "ASG_LREP", nullable = false)
    public boolean isAsgLrep() {
        return this.asgLrep;
    }

    public void setAsgLrep(boolean asgLrep) {
        this.asgLrep = asgLrep;
    }

    @Column(name = "ASG_LOBS", nullable = false)
    public boolean isAsgLobs() {
        return this.asgLobs;
    }

    public void setAsgLobs(boolean asgLobs) {
        this.asgLobs = asgLobs;
    }

    @Column(name = "ASG_RASU", nullable = false)
    public boolean isAsgRasu() {
        return this.asgRasu;
    }

    public void setAsgRasu(boolean asgRasu) {
        this.asgRasu = asgRasu;
    }

    @Column(name = "ASG_RMED", nullable = false)
    public boolean isAsgRmed() {
        return this.asgRmed;
    }

    public void setAsgRmed(boolean asgRmed) {
        this.asgRmed = asgRmed;
    }

    @Column(name = "ASG_RPER", nullable = false)
    public boolean isAsgRper() {
        return this.asgRper;
    }

    public void setAsgRper(boolean asgRper) {
        this.asgRper = asgRper;
    }

    @Column(name = "ASG_RDOM", nullable = false)
    public boolean isAsgRdom() {
        return this.asgRdom;
    }

    public void setAsgRdom(boolean asgRdom) {
        this.asgRdom = asgRdom;
    }

    @Column(name = "ASG_RORG", nullable = false)
    public boolean isAsgRorg() {
        return this.asgRorg;
    }

    public void setAsgRorg(boolean asgRorg) {
        this.asgRorg = asgRorg;
    }

    @Column(name = "ASG_RREP", nullable = false)
    public boolean isAsgRrep() {
        return this.asgRrep;
    }

    public void setAsgRrep(boolean asgRrep) {
        this.asgRrep = asgRrep;
    }

    @Column(name = "ASG_ROBS", nullable = false)
    public boolean isAsgRobs() {
        return this.asgRobs;
    }

    public void setAsgRobs(boolean asgRobs) {
        this.asgRobs = asgRobs;
    }

    @Column(name = "ASG_DASU", length = 100)
    public String getAsgDasu() {
        return this.asgDasu;
    }

    public void setAsgDasu(String asgDasu) {
        this.asgDasu = asgDasu;
    }

    @Column(name = "ASG_DMED")
    public Short getAsgDmed() {
        return this.asgDmed;
    }

    public void setAsgDmed(Short asgDmed) {
        this.asgDmed = asgDmed;
    }

    @Column(name = "ASG_DNDO", length = 50)
    public String getAsgDndo() {
        return this.asgDndo;
    }

    public void setAsgDndo(String asgDndo) {
        this.asgDndo = asgDndo;
    }

    @Column(name = "ASG_DTDO")
    public Short getAsgDtdo() {
        return this.asgDtdo;
    }

    public void setAsgDtdo(Short asgDtdo) {
        this.asgDtdo = asgDtdo;
    }

    @Column(name = "ASG_DNOM", length = 50)
    public String getAsgDnom() {
        return this.asgDnom;
    }

    public void setAsgDnom(String asgDnom) {
        this.asgDnom = asgDnom;
    }

    @Column(name = "ASG_DPAP", length = 50)
    public String getAsgDpap() {
        return this.asgDpap;
    }

    public void setAsgDpap(String asgDpap) {
        this.asgDpap = asgDpap;
    }

    @Column(name = "ASG_DSAP", length = 50)
    public String getAsgDsap() {
        return this.asgDsap;
    }

    public void setAsgDsap(String asgDsap) {
        this.asgDsap = asgDsap;
    }

    @Column(name = "ASG_DNAC")
    public Short getAsgDnac() {
        return this.asgDnac;
    }

    public void setAsgDnac(Short asgDnac) {
        this.asgDnac = asgDnac;
    }

    @Column(name = "ASG_DDIR", length = 50)
    public String getAsgDdir() {
        return this.asgDdir;
    }

    public void setAsgDdir(String asgDdir) {
        this.asgDdir = asgDdir;
    }

    @Column(name = "ASG_DLUG")
    public Short getAsgDlug() {
        return this.asgDlug;
    }

    public void setAsgDlug(Short asgDlug) {
        this.asgDlug = asgDlug;
    }

    @Column(name = "ASG_DTEL", length = 50)
    public String getAsgDtel() {
        return this.asgDtel;
    }

    public void setAsgDtel(String asgDtel) {
        this.asgDtel = asgDtel;
    }

    @Column(name = "ASG_DFAX", length = 50)
    public String getAsgDfax() {
        return this.asgDfax;
    }

    public void setAsgDfax(String asgDfax) {
        this.asgDfax = asgDfax;
    }

    @Column(name = "ASG_DCEL", length = 50)
    public String getAsgDcel() {
        return this.asgDcel;
    }

    public void setAsgDcel(String asgDcel) {
        this.asgDcel = asgDcel;
    }

    @Column(name = "ASG_DNRF", length = 50)
    public String getAsgDnrf() {
        return this.asgDnrf;
    }

    public void setAsgDnrf(String asgDnrf) {
        this.asgDnrf = asgDnrf;
    }

    @Column(name = "ASG_DNRD", length = 50)
    public String getAsgDnrd() {
        return this.asgDnrd;
    }

    public void setAsgDnrd(String asgDnrd) {
        this.asgDnrd = asgDnrd;
    }

    @Column(name = "ASG_DIDE", length = 50)
    public String getAsgDide() {
        return this.asgDide;
    }

    public void setAsgDide(String asgDide) {
        this.asgDide = asgDide;
    }

    @Column(name = "ASG_DENT", length = 100)
    public String getAsgDent() {
        return this.asgDent;
    }

    public void setAsgDent(String asgDent) {
        this.asgDent = asgDent;
    }

    @Column(name = "ASG_DOBS", length = 1000)
    public String getAsgDobs() {
        return this.asgDobs;
    }

    public void setAsgDobs(String asgDobs) {
        this.asgDobs = asgDobs;
    }

    @Column(name = "ASG_CASU", nullable = false)
    public boolean isAsgCasu() {
        return this.asgCasu;
    }

    public void setAsgCasu(boolean asgCasu) {
        this.asgCasu = asgCasu;
    }

    @Column(name = "ASG_CMED", nullable = false)
    public boolean isAsgCmed() {
        return this.asgCmed;
    }

    public void setAsgCmed(boolean asgCmed) {
        this.asgCmed = asgCmed;
    }

    @Column(name = "ASG_CPER", nullable = false)
    public boolean isAsgCper() {
        return this.asgCper;
    }

    public void setAsgCper(boolean asgCper) {
        this.asgCper = asgCper;
    }

    @Column(name = "ASG_CDOM", nullable = false)
    public boolean isAsgCdom() {
        return this.asgCdom;
    }

    public void setAsgCdom(boolean asgCdom) {
        this.asgCdom = asgCdom;
    }

    @Column(name = "ASG_CORG", nullable = false)
    public boolean isAsgCorg() {
        return this.asgCorg;
    }

    public void setAsgCorg(boolean asgCorg) {
        this.asgCorg = asgCorg;
    }

    @Column(name = "ASG_CREP", nullable = false)
    public boolean isAsgCrep() {
        return this.asgCrep;
    }

    public void setAsgCrep(boolean asgCrep) {
        this.asgCrep = asgCrep;
    }

    @Column(name = "ASG_COBS", nullable = false)
    public boolean isAsgCobs() {
        return this.asgCobs;
    }

    public void setAsgCobs(boolean asgCobs) {
        this.asgCobs = asgCobs;
    }

    @Column(name = "ASG_ACT", nullable = false)
    public boolean isAsgAct() {
        return this.asgAct;
    }

    public void setAsgAct(boolean asgAct) {
        this.asgAct = asgAct;
    }

    @Column(name = "ASG_LNDO", nullable = false)
    public boolean isAsgLndo() {
        return this.asgLndo;
    }

    public void setAsgLndo(boolean asgLndo) {
        this.asgLndo = asgLndo;
    }

    @Column(name = "ASG_LTDO", nullable = false)
    public boolean isAsgLtdo() {
        return this.asgLtdo;
    }

    public void setAsgLtdo(boolean asgLtdo) {
        this.asgLtdo = asgLtdo;
    }

    @Column(name = "ASG_LNOM", nullable = false)
    public boolean isAsgLnom() {
        return this.asgLnom;
    }

    public void setAsgLnom(boolean asgLnom) {
        this.asgLnom = asgLnom;
    }

    @Column(name = "ASG_LPAP", nullable = false)
    public boolean isAsgLpap() {
        return this.asgLpap;
    }

    public void setAsgLpap(boolean asgLpap) {
        this.asgLpap = asgLpap;
    }

    @Column(name = "ASG_LSAP", nullable = false)
    public boolean isAsgLsap() {
        return this.asgLsap;
    }

    public void setAsgLsap(boolean asgLsap) {
        this.asgLsap = asgLsap;
    }

    @Column(name = "ASG_LNAC", nullable = false)
    public boolean isAsgLnac() {
        return this.asgLnac;
    }

    public void setAsgLnac(boolean asgLnac) {
        this.asgLnac = asgLnac;
    }

    @Column(name = "ASG_LDIR", nullable = false)
    public boolean isAsgLdir() {
        return this.asgLdir;
    }

    public void setAsgLdir(boolean asgLdir) {
        this.asgLdir = asgLdir;
    }

    @Column(name = "ASG_LLUG", nullable = false)
    public boolean isAsgLlug() {
        return this.asgLlug;
    }

    public void setAsgLlug(boolean asgLlug) {
        this.asgLlug = asgLlug;
    }

    @Column(name = "ASG_LTEL", nullable = false)
    public boolean isAsgLtel() {
        return this.asgLtel;
    }

    public void setAsgLtel(boolean asgLtel) {
        this.asgLtel = asgLtel;
    }

    @Column(name = "ASG_LFAX", nullable = false)
    public boolean isAsgLfax() {
        return this.asgLfax;
    }

    public void setAsgLfax(boolean asgLfax) {
        this.asgLfax = asgLfax;
    }

    @Column(name = "ASG_LCEL", nullable = false)
    public boolean isAsgLcel() {
        return this.asgLcel;
    }

    public void setAsgLcel(boolean asgLcel) {
        this.asgLcel = asgLcel;
    }

    @Column(name = "ASG_LNRF", nullable = false)
    public boolean isAsgLnrf() {
        return this.asgLnrf;
    }

    public void setAsgLnrf(boolean asgLnrf) {
        this.asgLnrf = asgLnrf;
    }

    @Column(name = "ASG_LNRD", nullable = false)
    public boolean isAsgLnrd() {
        return this.asgLnrd;
    }

    public void setAsgLnrd(boolean asgLnrd) {
        this.asgLnrd = asgLnrd;
    }

    @Column(name = "ASG_LIDE", nullable = false)
    public boolean isAsgLide() {
        return this.asgLide;
    }

    public void setAsgLide(boolean asgLide) {
        this.asgLide = asgLide;
    }

    @Column(name = "ASG_LENT", nullable = false)
    public boolean isAsgLent() {
        return this.asgLent;
    }

    public void setAsgLent(boolean asgLent) {
        this.asgLent = asgLent;
    }

    @Column(name = "ASG_RNDO", nullable = false)
    public boolean isAsgRndo() {
        return this.asgRndo;
    }

    public void setAsgRndo(boolean asgRndo) {
        this.asgRndo = asgRndo;
    }

    @Column(name = "ASG_RTDO", nullable = false)
    public boolean isAsgRtdo() {
        return this.asgRtdo;
    }

    public void setAsgRtdo(boolean asgRtdo) {
        this.asgRtdo = asgRtdo;
    }

    @Column(name = "ASG_RNOM", nullable = false)
    public boolean isAsgRnom() {
        return this.asgRnom;
    }

    public void setAsgRnom(boolean asgRnom) {
        this.asgRnom = asgRnom;
    }

    @Column(name = "ASG_RPAP", nullable = false)
    public boolean isAsgRpap() {
        return this.asgRpap;
    }

    public void setAsgRpap(boolean asgRpap) {
        this.asgRpap = asgRpap;
    }

    @Column(name = "ASG_RSAP", nullable = false)
    public boolean isAsgRsap() {
        return this.asgRsap;
    }

    public void setAsgRsap(boolean asgRsap) {
        this.asgRsap = asgRsap;
    }

    @Column(name = "ASG_RNAC", nullable = false)
    public boolean isAsgRnac() {
        return this.asgRnac;
    }

    public void setAsgRnac(boolean asgRnac) {
        this.asgRnac = asgRnac;
    }

    @Column(name = "ASG_RDIR", nullable = false)
    public boolean isAsgRdir() {
        return this.asgRdir;
    }

    public void setAsgRdir(boolean asgRdir) {
        this.asgRdir = asgRdir;
    }

    @Column(name = "ASG_RLUG", nullable = false)
    public boolean isAsgRlug() {
        return this.asgRlug;
    }

    public void setAsgRlug(boolean asgRlug) {
        this.asgRlug = asgRlug;
    }

    @Column(name = "ASG_RTEL", nullable = false)
    public boolean isAsgRtel() {
        return this.asgRtel;
    }

    public void setAsgRtel(boolean asgRtel) {
        this.asgRtel = asgRtel;
    }

    @Column(name = "ASG_RFAX", nullable = false)
    public boolean isAsgRfax() {
        return this.asgRfax;
    }

    public void setAsgRfax(boolean asgRfax) {
        this.asgRfax = asgRfax;
    }

    @Column(name = "ASG_RCEL", nullable = false)
    public boolean isAsgRcel() {
        return this.asgRcel;
    }

    public void setAsgRcel(boolean asgRcel) {
        this.asgRcel = asgRcel;
    }

    @Column(name = "ASG_RNRF", nullable = false)
    public boolean isAsgRnrf() {
        return this.asgRnrf;
    }

    public void setAsgRnrf(boolean asgRnrf) {
        this.asgRnrf = asgRnrf;
    }

    @Column(name = "ASG_RNRD", nullable = false)
    public boolean isAsgRnrd() {
        return this.asgRnrd;
    }

    public void setAsgRnrd(boolean asgRnrd) {
        this.asgRnrd = asgRnrd;
    }

    @Column(name = "ASG_RIDE", nullable = false)
    public boolean isAsgRide() {
        return this.asgRide;
    }

    public void setAsgRide(boolean asgRide) {
        this.asgRide = asgRide;
    }

    @Column(name = "ASG_RENT", nullable = false)
    public boolean isAsgRent() {
        return this.asgRent;
    }

    public void setAsgRent(boolean asgRent) {
        this.asgRent = asgRent;
    }

    @Column(name = "ASG_CTDO", nullable = false)
    public boolean isAsgCtdo() {
        return this.asgCtdo;
    }

    public void setAsgCtdo(boolean asgCtdo) {
        this.asgCtdo = asgCtdo;
    }

    @Column(name = "ASG_CNDO", nullable = false)
    public boolean isAsgCndo() {
        return this.asgCndo;
    }

    public void setAsgCndo(boolean asgCndo) {
        this.asgCndo = asgCndo;
    }

    @Column(name = "ASG_CNOM", nullable = false)
    public boolean isAsgCnom() {
        return this.asgCnom;
    }

    public void setAsgCnom(boolean asgCnom) {
        this.asgCnom = asgCnom;
    }

    @Column(name = "ASG_CPAP", nullable = false)
    public boolean isAsgCpap() {
        return this.asgCpap;
    }

    public void setAsgCpap(boolean asgCpap) {
        this.asgCpap = asgCpap;
    }

    @Column(name = "ASG_CSAP", nullable = false)
    public boolean isAsgCsap() {
        return this.asgCsap;
    }

    public void setAsgCsap(boolean asgCsap) {
        this.asgCsap = asgCsap;
    }

    @Column(name = "ASG_CNAC", nullable = false)
    public boolean isAsgCnac() {
        return this.asgCnac;
    }

    public void setAsgCnac(boolean asgCnac) {
        this.asgCnac = asgCnac;
    }

    @Column(name = "ASG_CDIR", nullable = false)
    public boolean isAsgCdir() {
        return this.asgCdir;
    }

    public void setAsgCdir(boolean asgCdir) {
        this.asgCdir = asgCdir;
    }

    @Column(name = "ASG_CLUG", nullable = false)
    public boolean isAsgClug() {
        return this.asgClug;
    }

    public void setAsgClug(boolean asgClug) {
        this.asgClug = asgClug;
    }

    @Column(name = "ASG_CTEL", nullable = false)
    public boolean isAsgCtel() {
        return this.asgCtel;
    }

    public void setAsgCtel(boolean asgCtel) {
        this.asgCtel = asgCtel;
    }

    @Column(name = "ASG_CFAX", nullable = false)
    public boolean isAsgCfax() {
        return this.asgCfax;
    }

    public void setAsgCfax(boolean asgCfax) {
        this.asgCfax = asgCfax;
    }

    @Column(name = "ASG_CCEL", nullable = false)
    public boolean isAsgCcel() {
        return this.asgCcel;
    }

    public void setAsgCcel(boolean asgCcel) {
        this.asgCcel = asgCcel;
    }

    @Column(name = "ASG_CNRD", nullable = false)
    public boolean isAsgCnrd() {
        return this.asgCnrd;
    }

    public void setAsgCnrd(boolean asgCnrd) {
        this.asgCnrd = asgCnrd;
    }

    @Column(name = "ASG_CNRF", nullable = false)
    public boolean isAsgCnrf() {
        return this.asgCnrf;
    }

    public void setAsgCnrf(boolean asgCnrf) {
        this.asgCnrf = asgCnrf;
    }

    @Column(name = "ASG_CIDE", nullable = false)
    public boolean isAsgCide() {
        return this.asgCide;
    }

    public void setAsgCide(boolean asgCide) {
        this.asgCide = asgCide;
    }

    @Column(name = "ASG_CENT", nullable = false)
    public boolean isAsgCent() {
        return this.asgCent;
    }

    public void setAsgCent(boolean asgCent) {
        this.asgCent = asgCent;
    }
    

    public static final String NAMED_QUERY_BY_ID = "getAllAsignacionClasificacion";
    public static final String QUERY_BY_ID = "FROM Asignaclasificacion asigcla WHERE  asigcla.id.asgCla = ? AND asigcla.id.asgDep = ? ";
    
}
