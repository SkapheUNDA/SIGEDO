
package la.netco.core.businesslogic.services;


import la.netco.commons.dao.SeguimientosDao;
import la.netco.core.persistencia.dao.GenericDao;
import la.netco.core.persistencia.dao.ModulosDao;
import la.netco.core.persistencia.dao.SpringGenericDao;
import la.netco.core.persistencia.dao.UsuariosDao;
import la.netco.core.persitencia.dao.commos.GenericCommonDao;
import la.netco.correspondencia.dao.EntradaSalidaDao;
import la.netco.expedientes.dao.ExpedienteDao;
import la.netco.maestras.persistence.dto.PropTablas;
import la.netco.maestras.persistence.dto.TablasMaestras;
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



public interface ServiceDao {
	
	public UsuariosDao getUsuariosDao();
	public ModulosDao getModulosDao() ;
	public SpringGenericDao getSpringGenericDao();
		
	public GenericDao<TablasMaestras, Integer> getTablasMaestrasDao();
	public GenericDao<PropTablas, String> getPropiedadesTMDao();
	
	public GenericCommonDao getGenericCommonDao();
	public EntradaSalidaDao getEntradaDao();
	
	public SeguimientosDao getSeguimientosDao();	
	public ExpedienteDao getExpedienteDao();
	/**
	 * Added {@link AlertasDao} DAO Invoker
	 * 
	 */
	public AlertasDao getAlertasDao();
	
	/**
	 * Added {@link CortesFormatoDao} DAO Invoker
	 * 
	 */
	public CortesFormatoDao getCortesFormatoDao();
	
	/**
	 * Added {@link DetallesFormatoDao} DAO Invoker
	 * 
	 */
	public DetallesFormatoDao getDetallesFormatoDao();
	
	/**
	 * Added {@link FormatosTiposDatoDao} DAO Invoker
	 * 
	 */
	public FormatosTiposDatoDao getFormatosTiposDatoDao();
	
	/**
	 * Added {@link RegistroCargueDao} DAO Invoker
	 * 
	 */
	public RegistroCargueDao getRegistroCargueDao();
	
	/**
	 * Added {@link AutorizacionesDao} DAO Invoker
	 * 
	 */
	public AutorizacionesDao getAutorizacionesDao();
	
	public ArchivoSGCDao getArchivosSGCDao();
	
	/**
	 * Added {@link RegistroCargueFormatoDao} DAO Invoker
	 * 
	 */
	public RegistroCargueFormatoDao getRegistroCargueFormatoDao();
	
	public InformeEntidadDao getInformeEntidadDao();	
	
	public RegistroRadicadoDao getRegistroRadicadoDao();
	
		
}
