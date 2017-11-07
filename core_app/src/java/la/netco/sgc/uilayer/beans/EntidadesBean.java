/**
 * 
 */
package la.netco.sgc.uilayer.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.richfaces.model.Filter;

import la.netco.core.businesslogic.services.ServiceDao;
import la.netco.core.uilayer.beans.Constants;
import la.netco.core.uilayer.beans.utils.FacesUtils;
import la.netco.sgc.persistence.dto.Entidades;

/**
 * @author Edwin Daz
 * 
 */
@ManagedBean(name = "entidadesBean")
@ViewScoped
public class EntidadesBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7897257896566366511L;

	private Entidades entidad;
	private Entidades entidadAux;

	private List<Entidades> listaEntidades;

	private Integer idEntidadSelecciona;

	@ManagedProperty(value = "#{serviceDao}")
	transient private ServiceDao serviceDao;
	
	private String objetoSocialFiltro;

	public EntidadesBean() {
		// TODO Auto-generated constructor stub
		this.entidad = new Entidades();
		this.entidadAux = new Entidades();
		this.listaEntidades = new ArrayList<Entidades>();
	}

	@SuppressWarnings({ "unused", "unchecked" })
	@PostConstruct
	private void _init() {

		this.listaEntidades = (List<Entidades>) serviceDao
				.getGenericCommonDao().loadAll(Entidades.class);

	}

	/**
	 * Metodo utilizado para navegar hacia la pagina que muestra el listado de
	 * las entidades creadas.
	 * 
	 * @return
	 */
	public String navListado() {
		return "navListado";
	}

	/**
	 * Metodo utilizado para navegar hacia la pagina de creación de entidades
	 * 
	 * @return
	 */
	public String navAdicionar() {
		return "navAdicionar";
	}

	/**
	 * Metodo utilizado para navegar hacia la pagina de modificación de
	 * entidades
	 * 
	 * @return
	 */
	public String navModificar() {
		return "navModificar";
	}

	public String navAdicionarSociedad() {
		return "agregarSociedad";
	}
	
	public String navActualizarSociedad() {
		return "actualizarSociedad";
	}
	public String navlistadoSociedades(){
		return "listadoSociedades";
	}
	/**
	 * 
	 * Metodo utilizado para registrar en la base de datos la informacion
	 * diligenciada acerca de la entidades.
	 * 
	 * @return
	 */
	public String accionGuardar() {

		String page = null;
		try {
			/* Set de valores por default USO de esa entidad */
			entidad.setEntAcreacion(2016);
			entidad.setEntAutorizacionF("s");
			entidad.setEntPjuridica(entidad.getEntObjetoSocial());
			entidad.setEntDomicilio("s");
			entidad.setEntFax("s");
			entidad.setEntNoSedes(1);
			entidad.setEntWeb("s");
			// validar Objeto Entidad NIT
			Map<Integer, Object> params = new HashMap<Integer, Object>();
			params.put(0, entidad.getEntNit());
			entidadAux = new Entidades();
			entidadAux = (Entidades) serviceDao.getGenericCommonDao()
					.executeUniqueResult(Entidades.NAMEQUERY_SELECT_BY_NIT,
							params);

			if (entidadAux != null) {
				if (entidadAux.getEntNit().intValue() == entidad.getEntNit().intValue()) {
					FacesUtils.addFacesMessageFromBundle(
							Constants.NIT_SOCIEDAD_EXISTE,
							FacesMessage.SEVERITY_FATAL);
					
					return page;

				}

			} else {

				serviceDao.getGenericCommonDao().create(Entidades.class,
						entidad);
				page = navlistadoSociedades();
				FacesUtils.addFacesMessageFromBundle(
						Constants.EXITO_OPERACIONES_ALERTAS,
						FacesMessage.SEVERITY_INFO);
			}

		} catch (Exception ex) {

			FacesUtils.addFacesMessageFromBundle(
					Constants.ERROR_OPERACIONES_ALERTAS,
					FacesMessage.SEVERITY_INFO,
					new String[] { ex.getMessage() });

		}

		return page;
	}
	
	
	public String agregarNavigationSociedades() {
		return "sociedades?faces-redirect=true";
	}
	
	
	/**
	 * 
	 * Metodo utilizado para actualizar en la base de datos la informacion
	 * diligenciada acerca de la entidades.
	 * 
	 * @return
	 */
	public String accionActualizar() {

		String page = null;
		try {
			/*Set de valores por default USO de esa entidad*/
			entidad.setEntAcreacion(2016);
			entidad.setEntAutorizacionF("s");
			entidad.setEntPjuridica(entidad.getEntObjetoSocial());
			entidad.setEntDomicilio("s");
			entidad.setEntFax("s");
			entidad.setEntNoSedes(1);
			entidad.setEntWeb("s");
			serviceDao.getGenericCommonDao().update(Entidades.class, entidad);
			page = navlistadoSociedades();
			FacesUtils.addFacesMessageFromBundle(
					Constants.EXITO_OPERACIONES_ALERTAS,
					FacesMessage.SEVERITY_INFO);

		} catch (Exception ex) {

			FacesUtils.addFacesMessageFromBundle(
					Constants.ERROR_OPERACIONES_ALERTAS,
					FacesMessage.SEVERITY_INFO,
					new String[] { ex.getMessage() });

		}

		return page;
	}
	
	/**
	 * 
	 * Metodo utilizado para eliminar de la base de datos la informacion de una entidad.
	 * 
	 * @return
	 */
	public String accionEliminar() {

		String page = null;
		try {			
			
			serviceDao.getGenericCommonDao().delete(Entidades.class, entidad);
			_init();
			FacesUtils.addFacesMessageFromBundle(
					Constants.EXITO_OPERACIONES_ALERTAS,
					FacesMessage.SEVERITY_INFO);

		} catch (Exception ex) {

			FacesUtils.addFacesMessageFromBundle(
					Constants.ERROR_OPERACIONES_ALERTAS,
					FacesMessage.SEVERITY_INFO,
					new String[] { ex.getMessage() });

		}

		return page;
	}
	
	
	/**
	 * Metodo utilizado para cargar los datos del registro a modificar
	 * 
	 */
	public void cargarDatosModificar() {
		
		FacesContext ctx = FacesContext.getCurrentInstance();
		
		try {
			
			if(idEntidadSelecciona == null){
				
			     String idEntidad = ctx.getExternalContext().getRequestParameterMap().get("idEntidad");			   
			     if(idEntidad != null) this.idEntidadSelecciona = Integer.parseInt(idEntidad);
			}
				
			if((entidad == null || entidad.getEntCodigo() == null) &&  (idEntidadSelecciona != null && !idEntidadSelecciona.equals(""))){
				entidad = (Entidades) serviceDao.getGenericCommonDao().read(Entidades.class, idEntidadSelecciona);					
			
			}else{
				entidad = new Entidades();
			}
		
		} catch (Exception e) {
		
			ctx.addMessage(null, new FacesMessage("Se ha presentado un error. Detalles: " + e.getMessage()));
		}
		
	}
	
	
	/**
	 * Metodo utlizado para proporcionar el mecanismo de comparacion de campos en la funcionalidad de busqueda.
	 * 
	 * @return
	 */
	
	public Filter<?> getFiltroObjetoSocial() {
		
		return new Filter<Entidades>() {
			
			public boolean accept(Entidades e) {
				
				String objSocial = getObjetoSocialFiltro();
				if (objSocial == null || objSocial.length() == 0 || objSocial.equals(e.getEntObjetoSocial())) {
					return true;
				}
				
				return false;
			}
		};
		
	}
	

	/**
	 * @return the entidad
	 */
	public Entidades getEntidad() {
		return entidad;
	}

	/**
	 * @param entidad the entidad to set
	 */
	public void setEntidad(Entidades entidad) {
		this.entidad = entidad;
	}

	/**
	 * @return the listaEntidades
	 */
	public List<Entidades> getListaEntidades() {
		return listaEntidades;
	}

	/**
	 * @param listaEntidades the listaEntidades to set
	 */
	public void setListaEntidades(List<Entidades> listaEntidades) {
		this.listaEntidades = listaEntidades;
	}

	/**
	 * @return the idEntidadSelecciona
	 */
	public Integer getIdEntidadSelecciona() {
		return idEntidadSelecciona;
	}

	/**
	 * @param idEntidadSelecciona the idEntidadSelecciona to set
	 */
	public void setIdEntidadSelecciona(Integer idEntidadSelecciona) {
		this.idEntidadSelecciona = idEntidadSelecciona;
	}

	/**
	 * @return the serviceDao
	 */
	public ServiceDao getServiceDao() {
		return serviceDao;
	}

	/**
	 * @param serviceDao the serviceDao to set
	 */
	public void setServiceDao(ServiceDao serviceDao) {
		this.serviceDao = serviceDao;
	}

	/**
	 * @return the objetoSocialFiltro
	 */
	public String getObjetoSocialFiltro() {
		return objetoSocialFiltro;
	}

	/**
	 * @param objetoSocialFiltro the objetoSocialFiltro to set
	 */
	public void setObjetoSocialFiltro(String objetoSocialFiltro) {
		this.objetoSocialFiltro = objetoSocialFiltro;
	}
	
	
	
	
}