/**
 * 
 */
package la.netco.sgc.uilayer.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.model.SelectItem;

import la.netco.core.businesslogic.services.ServiceDao;
import la.netco.core.spring.security.UserDetailsUtils;
import la.netco.core.uilayer.beans.Constants;
import la.netco.core.uilayer.beans.utils.FacesUtils;
import la.netco.sgc.persistence.dto.Autorizaciones;
import la.netco.sgc.persistence.dto.Entidades;
import la.netco.sgc.persistence.dto.Formatos;

/**
 * @author cguzman
 * 
 */
@ManagedBean(name = "autorizacionesBean")
@RequestScoped
public class AutorizacionesBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{serviceDao}")
	transient private ServiceDao serviceDao;

	private List<Autorizaciones> autorizaciones;

	private List<Entidades> entidades;

	private List<Formatos> formatos;

	private List<SelectItem> listaEntidades;

	private List<SelectItem> listaFormatos;

	private Integer formatoSeleccionado;

	private Integer entidadSeleccionada;

	private String justificacion;

	/**
	 * 
	 */
	public AutorizacionesBean() {
		// TODO Auto-generated constructor stub
		this.autorizaciones = new ArrayList<Autorizaciones>();
		this.entidades = new ArrayList<Entidades>();
		this.formatos = new ArrayList<Formatos>();
		this.listaEntidades = new ArrayList<SelectItem>();
		this.listaFormatos = new ArrayList<SelectItem>();
	}

	@SuppressWarnings({ "unused", "unchecked" })
	@PostConstruct
	private void _inti() {

		autorizaciones = (List<Autorizaciones>) serviceDao.getGenericCommonDao().loadAll(Autorizaciones.class);
		cargarListaEntidades();
		cargarListaFormatos();
	}
	
	
	/**
	 * Metodo utilizado para cargar la lista desplegable de entidades
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	private void cargarListaEntidades() {
		
		Set<Entidades> entidadesUsuario = UserDetailsUtils.usuarioLogged().getEntidades();
		entidades = new ArrayList<Entidades>(entidadesUsuario);
		
		for (Entidades e : entidades) {
			
			SelectItem item = new SelectItem(e.getEntCodigo(), e.getEntObjetoSocial());
			listaEntidades.add(item);
		}
		
	}
	
	
	/**
	 * Metodo utilizado para cargar la lista desplegable de formatos
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	private void cargarListaFormatos() {
		formatos = (List<Formatos>) serviceDao.getGenericCommonDao().loadAll(Formatos.class);
		
		for (Formatos f : formatos) {
			
			SelectItem item = new SelectItem(f.getForCodigo(), f.getForDescripcion());
			listaFormatos.add(item);
		}
	}
	
	/**
	 * Metodo utlizado para realizar el guardardo de los datos en la base de datos
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String accionGuardar() {
		
		String page = null;
		
		try {
			Autorizaciones autorizaciones = new Autorizaciones();
			autorizaciones.setAutJustificacion(justificacion);
			
			Entidades ent = tomarObjetoListaSeleccionada(entidades, entidadSeleccionada);
			autorizaciones.setEntidades(ent);
			
			Formatos fmt = tomarObjetoListaSeleccionada(formatos, formatoSeleccionado);
			autorizaciones.setFormatos(fmt);
			
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(System.currentTimeMillis());
			Date fechaAutorizacion = calendar.getTime();
			
			autorizaciones.setAutFechaCreacion(fechaAutorizacion);
			
			serviceDao.getGenericCommonDao().create(Autorizaciones.class, autorizaciones);
			
			page = navListado();			
			FacesUtils.addFacesMessageFromBundle(Constants.EXITO_OPERACIONES_ALERTAS, FacesMessage.SEVERITY_INFO);
			
			this.autorizaciones = (List<Autorizaciones>) serviceDao.getGenericCommonDao().loadAll(Autorizaciones.class);
			
			
		} catch (Exception e) {
			
			FacesUtils.addFacesMessageFromBundle(Constants.ERROR_OPERACIONES_ALERTAS, FacesMessage.SEVERITY_ERROR, new String[]{e.getMessage()});
		}
		
		
		return page;
	}

	
	/**
	 * Metodo utlizado para redireccionar al usuario a la pagina de creacion de autorizacion
	 * 
	 * @return
	 */
	public String navAdicionar() {

		return "navAdicionar";
	}

	
	/**
	 * Metodo utlizado para redireccionar al usuario a la pagina de listado de autorizaciones
	 * 
	 * @return
	 */
	public String navListado() {

		return "navListado";
	}

	/**
	 * @return the serviceDao
	 */
	public ServiceDao getServiceDao() {
		return this.serviceDao;
	}
	
	/**
	 * 
	 * Metodo utilizado para tomar el objeto seleccionado de una lista desplegable
	 * 
	 * 
	 * @param lista
	 * @param seleccionado
	 * @return <T>
	 */
	@SuppressWarnings("unchecked")
	private <T> T tomarObjetoListaSeleccionada(List<T> lista, Integer seleccionado) {
		
		
		for (Object obj : lista) {
			
			if (obj instanceof Formatos) {
				
				Formatos fmtSeleccionado = (Formatos) obj; 
				
				if (seleccionado.compareTo(fmtSeleccionado.getForCodigo()) == 0) {
					
					return (T) fmtSeleccionado;
				}
			} else if (obj instanceof Entidades) {
				
				Entidades entSeleccionada = (Entidades) obj; 
				
				if (seleccionado.compareTo(entSeleccionada.getEntCodigo()) == 0) {
					
					return (T) entSeleccionada;
				}
			}	
		}
		
		return null;
	}

	/**
	 * @param serviceDao the serviceDao to set
	 */
	public void setServiceDao(ServiceDao serviceDao) {
		this.serviceDao = serviceDao;
	}

	/**
	 * @return the autorizaciones
	 */
	public List<Autorizaciones> getAutorizaciones() {
		return this.autorizaciones;
	}

	/**
	 * @param autorizaciones the autorizaciones to set
	 */
	public void setAutorizaciones(List<Autorizaciones> autorizaciones) {
		this.autorizaciones = autorizaciones;
	}

	/**
	 * @return the entidades
	 */
	public List<Entidades> getEntidades() {
		return this.entidades;
	}

	/**
	 * @param entidades the entidades to set
	 */
	public void setEntidades(List<Entidades> entidades) {
		this.entidades = entidades;
	}

	/**
	 * @return the formatos
	 */
	public List<Formatos> getFormatos() {
		return this.formatos;
	}

	/**
	 * @param formatos the formatos to set
	 */
	public void setFormatos(List<Formatos> formatos) {
		this.formatos = formatos;
	}

	/**
	 * @return the listaEntidades
	 */
	public List<SelectItem> getListaEntidades() {
		return this.listaEntidades;
	}

	/**
	 * @param listaEntidades the listaEntidades to set
	 */
	public void setListaEntidades(List<SelectItem> listaEntidades) {
		this.listaEntidades = listaEntidades;
	}

	/**
	 * @return the listaFormatos
	 */
	public List<SelectItem> getListaFormatos() {
		return this.listaFormatos;
	}

	/**
	 * @param listaFormatos the listaFormatos to set
	 */
	public void setListaFormatos(List<SelectItem> listaFormatos) {
		this.listaFormatos = listaFormatos;
	}

	/**
	 * @return the formatoSeleccionado
	 */
	public Integer getFormatoSeleccionado() {
		return this.formatoSeleccionado;
	}

	/**
	 * @param formatoSeleccionado the formatoSeleccionado to set
	 */
	public void setFormatoSeleccionado(Integer formatoSeleccionado) {
		this.formatoSeleccionado = formatoSeleccionado;
	}

	/**
	 * @return the entidadSeleccionada
	 */
	public Integer getEntidadSeleccionada() {
		return this.entidadSeleccionada;
	}

	/**
	 * @param entidadSeleccionada the entidadSeleccionada to set
	 */
	public void setEntidadSeleccionada(Integer entidadSeleccionada) {
		this.entidadSeleccionada = entidadSeleccionada;
	}

	/**
	 * @return the justificacion
	 */
	public String getJustificacion() {
		return this.justificacion;
	}

	/**
	 * @param justificacion the justificacion to set
	 */
	public void setJustificacion(String justificacion) {
		this.justificacion = justificacion;
	}
	
	

}
