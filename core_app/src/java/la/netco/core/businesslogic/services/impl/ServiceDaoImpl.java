package la.netco.core.businesslogic.services.impl;

import javax.annotation.Resource;

import la.netco.commons.dao.SeguimientosDao;
import la.netco.core.businesslogic.services.ServiceDao;
import la.netco.sgc.persistence.dao.AlertasDao;
import la.netco.sgc.persistence.dao.ArchivoSGCDao;
import la.netco.sgc.persistence.dao.AutorizacionesDao;
import la.netco.sgc.persistence.dao.CortesFormatoDao;
import la.netco.sgc.persistence.dao.DetallesFormatoDao;
import la.netco.sgc.persistence.dao.FormatosTiposDatoDao;
import la.netco.sgc.persistence.dao.InformeEntidadDao;
import la.netco.sgc.persistence.dao.RegistroCargueDao;
import la.netco.sgc.persistence.dao.RegistroCargueFormatoDao;
import la.netco.sgc.persistence.dao.RegistroRadicadoDao;
import la.netco.core.persistencia.dao.GenericDao;
import la.netco.core.persistencia.dao.ModulosDao;
import la.netco.core.persistencia.dao.SpringGenericDao;
import la.netco.core.persistencia.dao.UsuariosDao;
import la.netco.core.persitencia.dao.commos.GenericCommonDao;
import la.netco.correspondencia.dao.EntradaSalidaDao;
import la.netco.expedientes.dao.ExpedienteDao;
import la.netco.maestras.persistence.dto.PropTablas;
import la.netco.maestras.persistence.dto.TablasMaestras;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("serviceDao")
public class ServiceDaoImpl implements ServiceDao {


	private UsuariosDao usuariosDao;
	private GenericDao<?, ?> genericDao;
	private SpringGenericDao  springGenericDao;
	private GenericDao<TablasMaestras, Integer> tablasMaestrasDao;
	private GenericDao<PropTablas, String> propiedadesTMDao;
	private ModulosDao modulosDao;
	
	private GenericCommonDao genericCommonDao;
	
	private EntradaSalidaDao entradaDao;
	
	private AlertasDao alertasDao;
	
	private CortesFormatoDao cortesFormatoDao;
	
	private ArchivoSGCDao archivoSGCDao;
	
	private DetallesFormatoDao detallesFormatoDao;
	private SeguimientosDao seguimientosDao; 
	private ExpedienteDao expedienteDao;
	
	private FormatosTiposDatoDao formatosTiposDatoDao;
	
	private RegistroCargueDao registroCargueDao;
	
	private AutorizacionesDao autorizacionesDao;
	
	private RegistroCargueFormatoDao registroCargueFormatoDao;
	
	private InformeEntidadDao informeEntidadDao;
	
	private RegistroRadicadoDao registroRadicadoDao;

	@Autowired
	@Resource(name="usuariosDao")
	public void setUsuariosDao(UsuariosDao usuariosDao) {
		this.usuariosDao = usuariosDao;
	}

	@Autowired
	@Resource(name="genericDao")
	public void setGenericDao(GenericDao<?, ?> genericCommonDao) {
		this.genericDao = genericCommonDao;
	}	
	
	@Autowired
	@Resource(name="genericCommonDao")
	public void setGenericCommonDao(GenericCommonDao genericCommonDao) {
		this.genericCommonDao = genericCommonDao;
	}

	public UsuariosDao getUsuariosDao() {
		return usuariosDao;
	}
	@SuppressWarnings("unchecked")
	public GenericDao<TablasMaestras, Integer> getTablasMaestrasDao() {
		tablasMaestrasDao  = (GenericDao<TablasMaestras, Integer> )genericDao;
		tablasMaestrasDao.setType(TablasMaestras.class);
		return tablasMaestrasDao;
	}
	public void setTablasMaestrasDao(
			GenericDao<TablasMaestras, Integer> tablasMaestrasDao) {
		this.tablasMaestrasDao = tablasMaestrasDao;
	}
	
	@SuppressWarnings("unchecked")
	public GenericDao<PropTablas, String> getPropiedadesTMDao() {
		propiedadesTMDao = (GenericDao<PropTablas, String>) genericDao;
		propiedadesTMDao.setType(PropTablas.class);
		return propiedadesTMDao;
	}
	public void setPropiedadesTMDao(GenericDao<PropTablas, String> propiedadesTMDao) {
		this.propiedadesTMDao = propiedadesTMDao;
	}	

	@Autowired
	@Resource(name="springGenericDao")
	public void setSpringGenericDao(SpringGenericDao springGenericDao) {
		this.springGenericDao = springGenericDao;
	}

	public SpringGenericDao getSpringGenericDao() {
		return springGenericDao;
	}

	public GenericCommonDao getGenericCommonDao() {
		return genericCommonDao;
	}
	

	public ModulosDao getModulosDao() {
		return modulosDao;
	}
	
	@Autowired
	@Resource(name="modulosDao")
	public void setModulosDao(ModulosDao modulosDao) {
		this.modulosDao = modulosDao;
	}
	
	
	public EntradaSalidaDao getEntradaDao() {
		return entradaDao;
	}
	
	@Autowired
	@Resource(name="entradaDao")
	public void setEntradaDao(EntradaSalidaDao entradaDao) {
		this.entradaDao = entradaDao;
	}

	/**
	 * @return the alertasDao
	 */
	public AlertasDao getAlertasDao() {
		return alertasDao;
	}
	/**
	 * @param alertasDao the alertasDao to set
	 */
	@Autowired
	@Resource(name="alertasDao")
	public void setAlertasDao(AlertasDao alertasDao) {
		this.alertasDao = alertasDao;
	}
	
	public CortesFormatoDao getCortesFormatoDao() {
		// TODO Auto-generated method stub
		return cortesFormatoDao;
	}
	
	@Autowired
	@Resource(name="cortesFormatoDao")
	public void setCortesFormatoDao(CortesFormatoDao cortesFormatoDao) {
		this.cortesFormatoDao = cortesFormatoDao;
	}
	
	public DetallesFormatoDao getDetallesFormatoDao() {
		// TODO Auto-generated method stub
		return detallesFormatoDao;
	}
	
	@Autowired
	@Resource(name="detallesFormatoDao")
	public void setDetallesFormatoDao(DetallesFormatoDao detallesFormatoDao) {
		this.detallesFormatoDao = detallesFormatoDao;
	}
	
	public SeguimientosDao getSeguimientosDao() {
		return seguimientosDao;
	}
	
	@Autowired
	@Resource(name="seguimientosDao")
	public void setSeguimientosDao(SeguimientosDao seguimientosDao) {
		this.seguimientosDao = seguimientosDao;
	}
	
	public ExpedienteDao getExpedienteDao() {
		return expedienteDao;
	}
	
	@Autowired
	@Resource(name="expedienteDao")
	public void setExpedienteDao(ExpedienteDao expedienteDao) {
		this.expedienteDao = expedienteDao;
	}
	
	public FormatosTiposDatoDao getFormatosTiposDatoDao() {
		return formatosTiposDatoDao;
	}
	
	@Autowired
	@Resource(name="formatosTiposDatoDao")
	public void setFormatosTiposDatoDao(FormatosTiposDatoDao formatosTiposDatoDao) {
		this.formatosTiposDatoDao = formatosTiposDatoDao;
	}

	public RegistroCargueDao getRegistroCargueDao() {
		return registroCargueDao;
	}
	
	@Autowired
	@Resource(name="registroCargueDao")
	public void setRegistroCargueDao(RegistroCargueDao registroCargueDao) {
		this.registroCargueDao = registroCargueDao;
	}

	/**
	 * @return the autorizacionesDao
	 */
	public AutorizacionesDao getAutorizacionesDao() {
		return autorizacionesDao;
	}

	/**
	 * @param autorizacionesDao the autorizacionesDao to set
	 */
	@Autowired
	@Resource(name="autorizacionesDao")
	public void setAutorizacionesDao(AutorizacionesDao autorizacionesDao) {
		this.autorizacionesDao = autorizacionesDao;
	}

	/**
	 * @return the registroCargueFormatoDao
	 */
	public RegistroCargueFormatoDao getRegistroCargueFormatoDao() {
		return registroCargueFormatoDao;
	}

	/**
	 * @param registroCargueFormatoDao the registroCargueFormatoDao to set
	 */
	@Autowired
	@Resource(name="registroCargueFormatoDao")
	public void setRegistroCargueFormatoDao(RegistroCargueFormatoDao registroCargueFormatoDao) {
		this.registroCargueFormatoDao = registroCargueFormatoDao;
	}

	public InformeEntidadDao getInformeEntidadDao() {
		return informeEntidadDao;
	}

	@Autowired
	@Resource(name="informeEntidadDao")
	public void setInformeEntidadDao(InformeEntidadDao informeEntidadDao) {
		this.informeEntidadDao = informeEntidadDao;
	}
	
	public RegistroRadicadoDao getRegistroRadicadoDao() {
		return registroRadicadoDao;
	}

	@Autowired
	@Resource(name="registroRadicadoDao")
	public void setRegistroRadicadoDao(RegistroRadicadoDao registroRadicadoDao) {
		this.registroRadicadoDao = registroRadicadoDao;
	}

	//@Override	
	public ArchivoSGCDao getArchivosSGCDao() {		
		return archivoSGCDao;
	}

//	public ArchivoSGCDao getArchivoSGCDao() {
//		return archivoSGCDao;
//	}
	
	@Autowired
	@Resource(name="archivoSGCDao")
	public void setArchivoSGCDao(ArchivoSGCDao archivoSGCDao) {
		this.archivoSGCDao = archivoSGCDao;
	}

	
}
