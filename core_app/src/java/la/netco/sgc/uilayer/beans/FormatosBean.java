/**
 * 
 */
package la.netco.sgc.uilayer.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

import org.richfaces.model.Filter;

import la.netco.core.businesslogic.services.ServiceDao;
import la.netco.core.uilayer.beans.Constants;
import la.netco.core.uilayer.beans.utils.FacesUtils;
import la.netco.sgc.persistence.dto.Alertas;
import la.netco.sgc.persistence.dto.Autorizaciones;
import la.netco.sgc.persistence.dto.CortesFormato;
import la.netco.sgc.persistence.dto.DetallesFormato;
import la.netco.sgc.persistence.dto.Formatos;
import la.netco.sgc.persistence.dto.FormatosTiposDato;
import la.netco.sgc.persistence.dto.RegistroCargue;
import la.netco.sgc.persistence.dto.TiposDato;
import la.netco.sgc.uilayer.beans.util.DetallesFormatoModel;

/**
 * @author carlos
 *
 */
@ManagedBean(name = "formatosBean")
@ViewScoped
public class FormatosBean implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{serviceDao}")
	transient private ServiceDao serviceDao;
	
	private List<Formatos> arrFormatos;
	
	private String filtroNombreFormato;
	
	private Formatos formatoSeleccionado;
	
	private Integer idFormato;
	
	private String nombreFormato;
	
	private String descripcionFormato;
	
	private Integer numeroCampos;
	
	private String nombreHojaExcel;
	
	private Integer numeroLineaInicial;
	
	private List<DetallesFormatoModel> detallesFormato;
	
	private Integer idFormatoSeleccionado;
	
	private List<DetallesFormato> listaDetallesFormato;
	
	private String filtroNombreCampos;
	
	private List<TiposDato> tiposDato;
	
	private List<SelectItem> listaTiposDato;
	
	private List<FormatosTiposDato> formatosTiposDatos;
	
	private List<SelectItem> listaFormatosTiposDatos;
	
	private Integer idTipoDatoSeleccionado;
	
	private Integer idFormatoTipoDatoSeleccionado;
	
	
	
	/**
	 * 
	 */
	public FormatosBean() {
		// TODO Auto-generated constructor stub
		this.arrFormatos = new ArrayList<Formatos>();
		this.detallesFormato = new ArrayList<DetallesFormatoModel>();
		this.listaDetallesFormato = new ArrayList<DetallesFormato>();
		this.tiposDato = new ArrayList<TiposDato>();
		this.listaTiposDato = new ArrayList<SelectItem>();
		this.formatosTiposDatos = new ArrayList<FormatosTiposDato>();
		this.listaFormatosTiposDatos = new ArrayList<SelectItem>();
	}
	
	
	@SuppressWarnings("unused")
	@PostConstruct
	private void _init() {
		
		cargarDatosFormatos();
		
		if (idFormatoSeleccionado != null) {
			
			this.listaDetallesFormato = serviceDao.getDetallesFormatoDao().obtenerDetallesFormatoPorFormato(idFormatoSeleccionado); 
		}
		
		cargarCamposFormato();
		
	}
	
	@SuppressWarnings("unchecked")
	public void cargarDatosFormatos() {
		
		this.arrFormatos = (List<Formatos>) serviceDao.getGenericCommonDao().loadAll(Formatos.class);
	}
	
	/**
	 * Metodo utlizado para proporcionar el mecanismo de comparacion de campos en la funcionalidad de busqueda.
	 * 
	 * @return
	 */
	
	public Filter<?> getFiltroNombre() {
		
		return new Filter<Formatos>() {
			
			public boolean accept(Formatos f) {
				
				String formato = getFiltroNombreFormato();
				if (formato == null || formato.length() == 0 || formato.equals(f.getForNombre())) {
					return true;
				}
				
				return false;
			}
		};
		
	}
	
	/**
	 * 
	 * Accion Guardar
	 * 
	 */
	public String accionGuardar() {
		
		String page = null;
		
		try {
			Formatos formato = new Formatos();
			
			formato.setForNombre(nombreFormato);
			formato.setForDescripcion(descripcionFormato);
			formato.setForTotalCampos(numeroCampos);
			formato.setForNombreHoja(nombreHojaExcel);
			formato.setForLineaInicial(numeroLineaInicial);
			
			serviceDao.getGenericCommonDao().create(Formatos.class, formato);
			page = navListado();
			
			FacesUtils.addFacesMessageFromBundle(Constants.EXITO_OPERACIONES_ALERTAS, FacesMessage.SEVERITY_INFO);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			FacesUtils.addFacesMessageFromBundle(Constants.ERROR_OPERACIONES_ALERTAS, FacesMessage.SEVERITY_INFO, new String[]{e.getMessage()});
		}
		
		return page;
	}
	
	/**
	 * 
	 * Accion Guardar
	 * 
	 */
	public String accionActualizar() {
		
		String page = null;
		
		try {
						
			serviceDao.getGenericCommonDao().update(Formatos.class, formatoSeleccionado);
			page = navListado();
			
			FacesUtils.addFacesMessageFromBundle(Constants.EXITO_OPERACIONES_ALERTAS, FacesMessage.SEVERITY_INFO);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			FacesUtils.addFacesMessageFromBundle(Constants.ERROR_OPERACIONES_ALERTAS, FacesMessage.SEVERITY_INFO, new String[]{e.getMessage()});
		}
		
		return page;
	}
	
	/**
	 * 
	 * Accion Guardar
	 * 
	 */
	public String accionEliminar() {
		
		String page = null;
		
		try {
			
			List<RegistroCargue> registroCargues = serviceDao.getRegistroCargueDao().tomarRegistroCarguePorFormato(formatoSeleccionado.getForCodigo());
			
			if (registroCargues != null && registroCargues.size() > 0) {
				
				FacesUtils.addFacesMessageFromBundle(Constants.CARGUE_ASOCIADO, FacesMessage.SEVERITY_INFO);
				
			} else {
				
				Integer codigoFormato = formatoSeleccionado.getForCodigo();
				
				List<DetallesFormato> detalles = serviceDao.getDetallesFormatoDao().obtenerDetallesFormatoPorFormato(codigoFormato);		
				
				if (detalles != null && detalles.size() > 0) {
					FacesUtils.addFacesMessage("No es posible llevar a cabo esta operación ya que el formato seleccionado posee uno o mas campos asociados", FacesMessage.SEVERITY_INFO);
					return page;
				}

				List<CortesFormato> cortes = serviceDao.getCortesFormatoDao().obtenerCortesFormatoPorFormato(codigoFormato);
				
				if (cortes != null && cortes.size() > 0) {
					FacesUtils.addFacesMessage("No es posible llevar a cabo esta operación ya que el formato seleccionado posee uno o mas  cortes asociados", FacesMessage.SEVERITY_INFO);
					return page;
				}
								
				List<Alertas> alertas = serviceDao.getAlertasDao().obtenerAlertasPorFormato(codigoFormato);
				
				if (alertas != null && alertas.size() > 0) {
					FacesUtils.addFacesMessage("No es posible llevar a cabo esta operación ya que el formato seleccionado posee una o mas alertas asociadas", FacesMessage.SEVERITY_INFO);
					return page;
				}
				
				List<Autorizaciones> autorizaciones = serviceDao.getAutorizacionesDao().obtenerAutorizacionesPorFormato(codigoFormato);
				
				if (autorizaciones != null && autorizaciones.size() > 0) {
					FacesUtils.addFacesMessage("No es posible llevar a cabo esta operación ya que el formato seleccionado posee una o mas autorizaciones asociadas", FacesMessage.SEVERITY_INFO);
					return page;
				}
					
				serviceDao.getGenericCommonDao().delete(Formatos.class, formatoSeleccionado);
				page = navListado();
				
				FacesUtils.addFacesMessageFromBundle(Constants.EXITO_OPERACIONES_ALERTAS, FacesMessage.SEVERITY_INFO);
			}	
			
			cargarDatosFormatos();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			FacesUtils.addFacesMessageFromBundle(Constants.ERROR_OPERACIONES_ALERTAS, FacesMessage.SEVERITY_ERROR, new String[]{e.getMessage()});
		}
		
		return page;
	}
	
	
	/**
	 * Metodo utilizado para navegar hacia la pagina de lista de formatos.
	 * 
	 * @return
	 */
	public String navListado() {
		return "navListado";
	}
	
	
	/**
	 * 
	 * Metodo utilizado para navegar hacia la pagina de crear nuevo formato.
	 * 
	 * @return
	 */
	public String navAdicionar() {
		return "navAdicionar";
	}
	
	
	/**
	 * 
	 * Metodo utilizado para navegar hacia la pagina de crear nuevo formato.
	 * 
	 * @return
	 */
	public String navModificar() {
		return "navModificar";
	}
	

	/**
	 * 
	 * Metodo utilizado para navegar hacia la pagina de listar campos formato.
	 * 
	 * @return
	 */
	public String navListadoCampos() {
		
		FacesContext context = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
		session.setAttribute("formatoSeleccionado", formatoSeleccionado);
		
		return "navListadoCampos";
	}
	
	private void cargarCamposFormato() {
		
		FacesContext context = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
		
		formatoSeleccionado = (Formatos) session.getAttribute("formatoSeleccionado");
		
		this.detallesFormato = new ArrayList<DetallesFormatoModel>();
		
		if (formatoSeleccionado != null) {
			
			Integer numCampo = formatoSeleccionado.getForTotalCampos();
			
			for (int i = 0; i < numCampo; i++) {						
				DetallesFormatoModel dfm = new DetallesFormatoModel();
				dfm.setIndex(i);
				dfm.setItemsTiposDato(cargarTiposDato());						
				this.detallesFormato.add(dfm);					
			}					
		}
	}
	
	/**
	 * Metodo utilizado para cargar los datos del registro a modificar
	 * 
	 */
	public void cargarDatosModificar() {
		
		FacesContext ctx = FacesContext.getCurrentInstance();
		
		try {
			
			if(idFormatoSeleccionado == null){
				
			     String idFormato = ctx.getExternalContext().getRequestParameterMap().get("idFormato");			   
			     if(idFormato != null) this.idFormatoSeleccionado = Integer.parseInt(idFormato);
			}
				
			if((formatoSeleccionado == null) &&  (idFormatoSeleccionado != null && !idFormatoSeleccionado.equals(""))){
				formatoSeleccionado = (Formatos) serviceDao.getGenericCommonDao().read(Formatos.class, idFormatoSeleccionado);	
				
				if (formatoSeleccionado != null) {
					
					Integer numCampo = formatoSeleccionado.getForTotalCampos();
					
					for (int i = 0; i < numCampo; i++) {						
						DetallesFormatoModel dfm = new DetallesFormatoModel();
						dfm.setIndex(i);
						dfm.setItemsTiposDato(cargarTiposDato());						
						detallesFormato.add(dfm);					
					}					
				}				
			
			}else{
				formatoSeleccionado = new Formatos();
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
	
	public Filter<?> getFiltroNombresCampos() {
		
		return new Filter<DetallesFormato>() {
			
			public boolean accept(DetallesFormato df) {
				
				String nombreCampo = getFiltroNombreCampos();
				if (nombreCampo == null || nombreCampo.length() == 0 || nombreCampo.equals(df.getDetNombre())) {
					return true;
				}
				
				return false;
			}
		};
		
	}
	/**
	 * 
	 * Metodo utilizado para cargar los tipos de datos parametrizados
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<SelectItem> cargarTiposDato() {
		
		tiposDato = (List<TiposDato>) serviceDao.getGenericCommonDao().loadAll(TiposDato.class);
		this.listaTiposDato = new ArrayList<SelectItem>(); 
		
		for (TiposDato td : tiposDato) {
			
			SelectItem item = new SelectItem(td.getTpdCodigo(), td.getTpdDescripcion());
			listaTiposDato.add(item);
		}
		
		return listaTiposDato;
	}
	
	/**
	 * 
	 * Metodo utilizado para cargar los formatos de los tipos de datos parametrizados
	 * 
	 */
	public void cargarFormatoTiposDato(ValueChangeEvent e) {
		System.out.println("Paso >>>> 1");
		if (null != e.getNewValue()) {		
			idTipoDatoSeleccionado = (Integer) e.getNewValue();
			
			System.out.println("Paso >>>> 2");
			formatosTiposDatos = serviceDao.getFormatosTiposDatoDao().tomarFormatosTiposDatoPorTiposDato(idTipoDatoSeleccionado);
			this.listaFormatosTiposDatos = new ArrayList<SelectItem>();
			
			for (FormatosTiposDato ftd : formatosTiposDatos) {
				
				SelectItem item = new SelectItem(ftd.getFtdCodigo(), ftd.getFtdFormato());
				listaFormatosTiposDatos.add(item);
			}
			System.out.println("Paso >>>> 3");
		}
		
		
	}
	
	public void cambioTipoDatos(ValueChangeEvent e) {
		
		System.out.println("Paso >>>> 1");
		
		if (null != e.getNewValue()) {		
			idTipoDatoSeleccionado = (Integer) e.getNewValue();
			
			System.out.println("Paso >>>> 2");
			
			formatosTiposDatos = serviceDao.getFormatosTiposDatoDao().tomarFormatosTiposDatoPorTiposDato(idTipoDatoSeleccionado);
			this.listaFormatosTiposDatos = new ArrayList<SelectItem>();
			
			for (FormatosTiposDato ftd : formatosTiposDatos) {
				
				SelectItem item = new SelectItem(ftd.getFtdCodigo(), ftd.getFtdFormato());
				listaFormatosTiposDatos.add(item);
			}
			
			System.out.println("Paso >>>> 3");
		}
		
		
	}
	
	
	public void listener(AjaxBehaviorEvent event) {

		System.out.println("Paso >>>> 1");
		
		FacesContext ctx = FacesContext.getCurrentInstance();
		String filaCombo = ctx.getExternalContext().getRequestParameterMap().get("filaCombo");
			
		Integer index = (filaCombo != null) ? Integer.parseInt(filaCombo) : null;
		
		if (index != null) {
			
			this.detallesFormato.get(index).setTipoDato(idTipoDatoSeleccionado);
			Integer idTipoDato = idFormatoSeleccionado;
			
			System.out.println("Paso >>>> 2");
			
			formatosTiposDatos = serviceDao.getFormatosTiposDatoDao().tomarFormatosTiposDatoPorTiposDato(idTipoDato);
			
			this.listaFormatosTiposDatos = new ArrayList<SelectItem>();

			for (FormatosTiposDato ftd : formatosTiposDatos) {

				SelectItem item = new SelectItem(ftd.getFtdCodigo(),ftd.getFtdFormato());
				listaFormatosTiposDatos.add(item);
			}
			
			this.detallesFormato.get(index).setItemsFormatosTiposDato(listaFormatosTiposDatos);
			
		}
		
		System.out.println("Paso >>>> 3");
	}


	/**
	 * @return the serviceDao
	 */
	public ServiceDao getServiceDao() {
		return this.serviceDao;
	}


	/**
	 * @param serviceDao the serviceDao to set
	 */
	public void setServiceDao(ServiceDao serviceDao) {
		this.serviceDao = serviceDao;
	}


	/**
	 * @return the arrFormatos
	 */
	public List<Formatos> getArrFormatos() {
		return this.arrFormatos;
	}


	/**
	 * @param arrFormatos the arrFormatos to set
	 */
	public void setArrFormatos(List<Formatos> arrFormatos) {
		this.arrFormatos = arrFormatos;
	}


	/**
	 * @return the filtroNombreFormato
	 */
	public String getFiltroNombreFormato() {
		return this.filtroNombreFormato;
	}


	/**
	 * @param filtroNombreFormato the filtroNombreFormato to set
	 */
	public void setFiltroNombreFormato(String filtroNombreFormato) {
		this.filtroNombreFormato = filtroNombreFormato;
	}


	/**
	 * @return the formatoSeleccionado
	 */
	public Formatos getFormatoSeleccionado() {
		return this.formatoSeleccionado;
	}


	/**
	 * @param formatoSeleccionado the formatoSeleccionado to set
	 */
	public void setFormatoSeleccionado(Formatos formatoSeleccionado) {
		this.formatoSeleccionado = formatoSeleccionado;
	}


	/**
	 * @return the idFormato
	 */
	public Integer getIdFormato() {
		return this.idFormato;
	}


	/**
	 * @param idFormato the idFormato to set
	 */
	public void setIdFormato(Integer idFormato) {
		this.idFormato = idFormato;
	}


	/**
	 * @return the nombreFormato
	 */
	public String getNombreFormato() {
		return this.nombreFormato;
	}


	/**
	 * @param nombreFormato the nombreFormato to set
	 */
	public void setNombreFormato(String nombreFormato) {
		this.nombreFormato = nombreFormato;
	}


	/**
	 * @return the descripcionFormato
	 */
	public String getDescripcionFormato() {
		return this.descripcionFormato;
	}


	/**
	 * @param descripcionFormato the descripcionFormato to set
	 */
	public void setDescripcionFormato(String descripcionFormato) {
		this.descripcionFormato = descripcionFormato;
	}


	/**
	 * @return the numeroCampos
	 */
	public Integer getNumeroCampos() {
		return this.numeroCampos;
	}


	/**
	 * @param numeroCampos the numeroCampos to set
	 */
	public void setNumeroCampos(Integer numeroCampos) {
		this.numeroCampos = numeroCampos;
	}


	/**
	 * @return the nombreHojaExcel
	 */
	public String getNombreHojaExcel() {
		return this.nombreHojaExcel;
	}


	/**
	 * @param nombreHojaExcel the nombreHojaExcel to set
	 */
	public void setNombreHojaExcel(String nombreHojaExcel) {
		this.nombreHojaExcel = nombreHojaExcel;
	}


	/**
	 * @return the numeroLineaInicial
	 */
	public Integer getNumeroLineaInicial() {
		return this.numeroLineaInicial;
	}


	/**
	 * @param numeroLineaInicial the numeroLineaInicial to set
	 */
	public void setNumeroLineaInicial(Integer numeroLineaInicial) {
		this.numeroLineaInicial = numeroLineaInicial;
	}
		
	
	/**
	 * @return the detallesFormato
	 */
	public List<DetallesFormatoModel> getDetallesFormato() {
		return detallesFormato;
	}


	/**
	 * @param detallesFormato the detallesFormato to set
	 */
	public void setDetallesFormato(List<DetallesFormatoModel> detallesFormato) {
		this.detallesFormato = detallesFormato;
	}


	/**
	 * @return the idFormatoSeleccionado
	 */
	public Integer getIdFormatoSeleccionado() {
		return idFormatoSeleccionado;
	}


	/**
	 * @param idFormatoSeleccionado the idFormatoSeleccionado to set
	 */
	public void setIdFormatoSeleccionado(Integer idFormatoSeleccionado) {
		this.idFormatoSeleccionado = idFormatoSeleccionado;
	}



	/**
	 * @return the listaDetallesFormato
	 */
	public List<DetallesFormato> getListaDetallesFormato() {
		return listaDetallesFormato;
	}


	/**
	 * @param listaDetallesFormato the listaDetallesFormato to set
	 */
	public void setListaDetallesFormato(List<DetallesFormato> listaDetallesFormato) {
		this.listaDetallesFormato = listaDetallesFormato;
	}


	/**
	 * @return the filtroNombreCampos
	 */
	public String getFiltroNombreCampos() {
		return filtroNombreCampos;
	}


	/**
	 * @param filtroNombreCampos the filtroNombreCampos to set
	 */
	public void setFiltroNombreCampos(String filtroNombreCampos) {
		this.filtroNombreCampos = filtroNombreCampos;
	}

	
	
	/**
	 * @return the tiposDato
	 */
	public List<TiposDato> getTiposDato() {
		return tiposDato;
	}


	/**
	 * @param tiposDato the tiposDato to set
	 */
	public void setTiposDato(List<TiposDato> tiposDato) {
		this.tiposDato = tiposDato;
	}


	/**
	 * @return the listaTiposDato
	 */
	public List<SelectItem> getListaTiposDato() {
		return listaTiposDato;
	}


	/**
	 * @param listaTiposDato the listaTiposDato to set
	 */
	public void setListaTiposDato(List<SelectItem> listaTiposDato) {
		this.listaTiposDato = listaTiposDato;
	}


	/**
	 * @return the formatosTiposDatos
	 */
	public List<FormatosTiposDato> getFormatosTiposDatos() {
		return formatosTiposDatos;
	}


	/**
	 * @param formatosTiposDatos the formatosTiposDatos to set
	 */
	public void setFormatosTiposDatos(List<FormatosTiposDato> formatosTiposDatos) {
		this.formatosTiposDatos = formatosTiposDatos;
	}


	/**
	 * @return the listaFormatosTiposDatos
	 */
	public List<SelectItem> getListaFormatosTiposDatos() {
		return listaFormatosTiposDatos;
	}


	/**
	 * @param listaFormatosTiposDatos the listaFormatosTiposDatos to set
	 */
	public void setListaFormatosTiposDatos(List<SelectItem> listaFormatosTiposDatos) {
		this.listaFormatosTiposDatos = listaFormatosTiposDatos;
	}

	
	/**
	 * @return the idTipoDatoSeleccionado
	 */
	public Integer getIdTipoDatoSeleccionado() {
		return idTipoDatoSeleccionado;
	}


	/**
	 * @param idTipoDatoSeleccionado the idTipoDatoSeleccionado to set
	 */
	public void setIdTipoDatoSeleccionado(Integer idTipoDatoSeleccionado) {
		this.idTipoDatoSeleccionado = idTipoDatoSeleccionado;
	}


	/**
	 * @return the idFormatoTipoDatoSeleccionado
	 */
	public Integer getIdFormatoTipoDatoSeleccionado() {
		return idFormatoTipoDatoSeleccionado;
	}


	/**
	 * @param idFormatoTipoDatoSeleccionado the idFormatoTipoDatoSeleccionado to set
	 */
	public void setIdFormatoTipoDatoSeleccionado(
			Integer idFormatoTipoDatoSeleccionado) {
		this.idFormatoTipoDatoSeleccionado = idFormatoTipoDatoSeleccionado;
	}




}
