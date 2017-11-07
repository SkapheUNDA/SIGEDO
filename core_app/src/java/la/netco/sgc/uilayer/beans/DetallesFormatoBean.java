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
import javax.faces.component.UIComponent;
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
@ManagedBean(name = "detallesFormatoBean" )
@ViewScoped
public class DetallesFormatoBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 611535952457683775L;

	private List<DetallesFormatoModel> detallesFormato;
	
	private Integer idFormatoSeleccionado;
	
	private List<DetallesFormato> listaDetallesFormato;
	
	private Formatos formatoSeleccionado;
	
	private String filtroNombreCampos;
	
	private List<TiposDato> tiposDato;
	
	private List<SelectItem> listaTiposDato;
	
	private List<FormatosTiposDato> formatosTiposDatos;
	
	private List<SelectItem> listaFormatosTiposDatos;
	
	private Integer idTipoDatoSeleccionado;
	
	private Integer idFormatoTipoDatoSeleccionado;
	
	@ManagedProperty(value = "#{serviceDao}")
	transient private ServiceDao serviceDao;
	
	private DetallesFormato detalleSeleccionado;
	
	private Integer idDetalleSeleccionado;
	
	
	/**
	 * 
	 */
	public DetallesFormatoBean() {
		// TODO Auto-generated constructor stub
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
		
		cargarCamposFormato();
		
	}
	
	/**
	 * 
	 * Metodo utilizado para navegar hacia la pagina de crear campos formato.
	 * 
	 * @return
	 */
	public String navAdicionarCampos() {
		return "navAdicionarCampos";
	}
	
	/**
	 * 
	 * Metodo utilizado para navegar hacia la pagina de listar campos formato.
	 * 
	 * @return
	 */
	public String navListadoCampos() {
		
		return "navListadoCampos";
	}
	
	/**
	 * 
	 * Metodo utilizado para navegar hacia la pagina de listar campos formato.
	 * 
	 * @return
	 */
	public String navActualizarCampo() {
		
		return "navActualizarCampo";
	}
	
	private void cargarCamposFormato() {
		
		FacesContext context = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
		
		formatoSeleccionado = (Formatos) session.getAttribute("formatoSeleccionado");
		
		this.detallesFormato = new ArrayList<DetallesFormatoModel>();
		
		if (formatoSeleccionado != null) {
			
			
			idFormatoSeleccionado = formatoSeleccionado.getForCodigo();
			this.listaDetallesFormato = serviceDao.getDetallesFormatoDao().obtenerDetallesFormatoPorFormato(idFormatoSeleccionado); 
			
			Integer numCampo = formatoSeleccionado.getForTotalCampos();
			numCampo = numCampo - this.listaDetallesFormato.size();
			
			for (int i = 0; i < numCampo; i++) {						
				DetallesFormatoModel dfm = new DetallesFormatoModel();
				dfm.setIndex(i);
				dfm.setItemsTiposDato(cargarTiposDato(i));						
				this.detallesFormato.add(dfm);					
			}					
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
	public List<SelectItem> cargarTiposDato(Integer index) {
		
		tiposDato = (List<TiposDato>) serviceDao.getGenericCommonDao().loadAll(TiposDato.class);
		this.listaTiposDato = new ArrayList<SelectItem>(); 
		
		for (TiposDato td : tiposDato) {
			
			SelectItem item = new SelectItem(index + "-" + td.getTpdCodigo(), td.getTpdDescripcion());
			listaTiposDato.add(item);
		}
		
		return listaTiposDato;
	}
	
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
	 * 
	 * Metodo utilizado para cargar los formatos de los tipos de datos parametrizados
	 * 
	 */
	public void cargarFormatoTiposDato(ValueChangeEvent e) {
		System.out.println("Paso >>>> 1");
		
		if (null != e.getNewValue()) {		
			System.out.println("Paso >>>> 2");
			
			String valor = (String) e.getNewValue();
			String[] valores = valor.split("-");
				
			Integer index = Integer.parseInt(valores[0]);
			
			
			if (index != null) {
				
				Integer idTipoDato = Integer.parseInt(valores[1]);
				this.detallesFormato.get(index).setTipoDato(idTipoDato);
				
				
				System.out.println("Paso >>>> 3");
				
				formatosTiposDatos = serviceDao.getFormatosTiposDatoDao().tomarFormatosTiposDatoPorTiposDato(idTipoDato);
				
				this.listaFormatosTiposDatos = new ArrayList<SelectItem>();

				for (FormatosTiposDato ftd : formatosTiposDatos) {

					SelectItem item = new SelectItem(ftd.getFtdCodigo(),ftd.getFtdFormato());
					listaFormatosTiposDatos.add(item);
				}
				
				this.detallesFormato.get(index).setItemsFormatosTiposDato(listaFormatosTiposDatos);
				
			}
			
			System.out.println("Paso >>>> 4");
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
	
	public String accionGuardar() {
		
		String page = null;
		
		try {
			if (validarCamposRequeridos()) {
				
				if (detallesFormato.size() > 0) {
				
					for (DetallesFormatoModel model : detallesFormato) {
						
						DetallesFormato detalle = new DetallesFormato();
						
						detalle.setFormatos(formatoSeleccionado);
						detalle.setDetNombre(model.getNombreCampo());
						detalle.setDetDescripcion(model.getDescipcionCampo());
						detalle.setDetPosicion(model.getIndex());
						detalle.setDetRequerido(model.isRequerido());				
						detalle.setFtdCodigo(model.getFormatoTipoDato());
						detalle.setTpdCodigo(model.getTipoDato());
						
						serviceDao.getGenericCommonDao().create(DetallesFormato.class, detalle);
							
						
					}
					
					page = navListadoCampos();		
					FacesUtils.addFacesMessageFromBundle(Constants.EXITO_OPERACIONES_ALERTAS,FacesMessage.SEVERITY_INFO);
				}
				
			}
		} catch (Exception e) {
			
			FacesUtils.addFacesMessageFromBundle(Constants.ERROR_OPERACIONES_ALERTAS, FacesMessage.SEVERITY_INFO, new String[]{e.getMessage()});
		}
		
		return page;
	}
	
	public String accionActualizar() {
		
		String page = null;
		
		try {
	
			serviceDao.getGenericCommonDao().update(DetallesFormato.class, detalleSeleccionado);
				
			page = navListadoCampos();		
			FacesUtils.addFacesMessageFromBundle(Constants.EXITO_OPERACIONES_ALERTAS,FacesMessage.SEVERITY_INFO);				
			
		} catch (Exception e) {
			
			FacesUtils.addFacesMessageFromBundle(Constants.ERROR_OPERACIONES_ALERTAS, FacesMessage.SEVERITY_INFO, new String[]{e.getMessage()});
		}
		
		return page;
	}
	
	
	/**
	 * 
	 * Accion Eliminar
	 * 
	 */
	public String accionEliminar() {
		
		String page = null;
		
		try {
			
			List<Alertas> alertasCorte = serviceDao.getAlertasDao().obtenerAlertasPorCampo(detalleSeleccionado.getDetCodigo());
			
			if (alertasCorte != null && alertasCorte.size() > 0) {
				FacesUtils.addFacesMessage("No es posible llevar a cabo esta operación ya que el campo seleccionado posee una o mas alertas asociadas", FacesMessage.SEVERITY_INFO);
				return page;
			}
			
			List<RegistroCargue> carguesCorte = serviceDao.getRegistroCargueDao().tomarRegistroCarguePorCampo(detalleSeleccionado.getDetCodigo());
			
			if (carguesCorte != null && carguesCorte.size() > 0) {
				FacesUtils.addFacesMessage("No es posible llevar a cabo esta operación ya que el campo seleccionado posee uno o mas cargues asociados", FacesMessage.SEVERITY_INFO);
				return page;
			}
			
			List<RegistroCargue> registroCargues = serviceDao.getRegistroCargueDao().tomarRegistroCarguePorFormato(formatoSeleccionado.getForCodigo());
			
			if (registroCargues != null && registroCargues.size() > 0) {
				FacesUtils.addFacesMessage("No es posible llevar a cabo esta operación ya que el campo seleccionado hace parte de un formato que posee uno o mas cargues asociados", FacesMessage.SEVERITY_INFO);
				return page;
			}
			
			serviceDao.getGenericCommonDao().delete(DetallesFormato.class, detalleSeleccionado);				
			FacesUtils.addFacesMessageFromBundle(Constants.EXITO_OPERACIONES_ALERTAS, FacesMessage.SEVERITY_INFO);

			cargarCamposFormato();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			FacesUtils.addFacesMessageFromBundle(Constants.ERROR_OPERACIONES_ALERTAS, FacesMessage.SEVERITY_INFO, new String[]{e.getMessage()});
		}
		
		return page;
	}
	
	
	
	public boolean validarCamposRequeridos() {

		boolean retorno = true;

		for (DetallesFormatoModel model : detallesFormato) {

			if (model.getNombreCampo() == null || "".equals(model.getNombreCampo())) {
				retorno = false;
				FacesUtils.addFacesMessageFromBundle(Constants.CAMPOS_REQUERIDOS,FacesMessage.SEVERITY_INFO);
				break;

			}

			if (model.getDescipcionCampo() == null || "".equals(model.getDescipcionCampo())) {
				retorno = false;
				FacesUtils.addFacesMessageFromBundle(Constants.CAMPOS_REQUERIDOS, FacesMessage.SEVERITY_INFO);
				break;
			}

			if (model.getTipoDato() == null || "".equals(model.getTipoDato())) {
				retorno = false;
				FacesUtils.addFacesMessageFromBundle(Constants.CAMPOS_REQUERIDOS,FacesMessage.SEVERITY_INFO);
				break;

			}

			if (model.getFormatoTipoDato() == null || "".equals(model.getFormatoTipoDato())) {
				retorno = false;
				FacesUtils.addFacesMessageFromBundle(Constants.CAMPOS_REQUERIDOS, FacesMessage.SEVERITY_INFO);
				break;
			}

		}

		return retorno;
	}
	
	
	/**
	 * Metodo utilizado para cargar los datos del registro a modificar
	 * 
	 */
	public void cargarDatosModificar() {
		
		UIComponent componente = null;
		FacesContext ctx = FacesContext.getCurrentInstance();
		
		try {
			
			if(idDetalleSeleccionado == null){
				
				 componente = ctx.getViewRoot().findComponent(ctx.getViewRoot().getClientId());
			     String idDetalle = ctx.getExternalContext().getRequestParameterMap().get("idDetalle");			   
			     if(idDetalle != null) this.idDetalleSeleccionado = Integer.parseInt(idDetalle);
			}
				
			if((detalleSeleccionado == null) &&  (idDetalleSeleccionado != null && !idDetalleSeleccionado.equals(""))){
				detalleSeleccionado = (DetallesFormato) serviceDao.getGenericCommonDao().read(DetallesFormato.class, idDetalleSeleccionado);	
				
				if (detalleSeleccionado != null) {
					cargarTiposDato();
					Integer idTipoDatoSel = detalleSeleccionado.getTpdCodigo();
					
					cambioTipoDatos(new ValueChangeEvent(componente, null, idTipoDatoSel));
					
				}				
			
			}else{
				formatoSeleccionado = new Formatos();
			}
		
		} catch (Exception e) {
		
			ctx.addMessage(null, new FacesMessage("Se ha presentado un error. Detalles: " + e.getMessage()));
		}
		
	}
	

	/**
	 * @return the formatoSeleccionado
	 */
	public Formatos getFormatoSeleccionado() {
		return formatoSeleccionado;
	}

	/**
	 * @param formatoSeleccionado the formatoSeleccionado to set
	 */
	public void setFormatoSeleccionado(Formatos formatoSeleccionado) {
		this.formatoSeleccionado = formatoSeleccionado;
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
	 * @return the detalleSeleccionado
	 */
	public DetallesFormato getDetalleSeleccionado() {
		return detalleSeleccionado;
	}

	/**
	 * @param detalleSeleccionado the detalleSeleccionado to set
	 */
	public void setDetalleSeleccionado(DetallesFormato detalleSeleccionado) {
		this.detalleSeleccionado = detalleSeleccionado;
	}

	/**
	 * @return the idDetalleSeleccionado
	 */
	public Integer getIdDetalleSeleccionado() {
		return idDetalleSeleccionado;
	}

	/**
	 * @param idDetalleSeleccionado the idDetalleSeleccionado to set
	 */
	public void setIdDetalleSeleccionado(Integer idDetalleSeleccionado) {
		this.idDetalleSeleccionado = idDetalleSeleccionado;
	}
	
	
	
	
}
