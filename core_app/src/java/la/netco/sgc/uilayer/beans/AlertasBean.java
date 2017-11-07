package la.netco.sgc.uilayer.beans;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;


import la.netco.core.businesslogic.services.ServiceDao;
import la.netco.core.spring.security.UserDetailsUtils;
import la.netco.core.uilayer.beans.Constants;
import la.netco.core.uilayer.beans.utils.FacesUtils;
import la.netco.sgc.persistence.dto.Alertas;
import la.netco.sgc.persistence.dto.CortesFormato;
import la.netco.sgc.persistence.dto.DetallesFormato;
import la.netco.sgc.persistence.dto.Entidades;
import la.netco.sgc.persistence.dto.Formatos;
import la.netco.sgc.persistence.dto.FormatosTiposDato;
import la.netco.sgc.persistence.dto.Operadores;
import la.netco.sgc.persistence.dto.RegistroCargue;
import la.netco.sgc.persistence.dto.TiposDato;

/**
 * 
 * @author cguzman
 *
 */
@ManagedBean(name = "alertas")
@ViewScoped
public class AlertasBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5651542339179507506L;

	private Integer formatoSeleccionado;
	
	private List<SelectItem> listaFormatos;
	
	private List<SelectItem> listaEntidades;
	
	private List<Entidades> entidades;
	
	private Entidades entidadSeleccionadaObj;
	
	private Integer entidadSeleccionadaValor;
	
	private CortesFormato corteFormatoSeleccionado;
	
	private Date fechaCorteSeleccionado;	
	
	private List<SelectItem> listaFormatosAdicionarOrigen;
	
	private List<SelectItem> listaFormatosAdicionarDestino;
	
	private List<SelectItem> listaOperadores;
	
	private List<SelectItem> listaCortesOrigen;
	
	private List<SelectItem> listaCortesDestino;
	
	private List<SelectItem> listaPosicionCampoFiltroOrigen;
	
	private List<SelectItem> listaCampoValorOrigen;
	
	private List<SelectItem> listaPosicionCampoFiltroDestino;
	
	private List<SelectItem> listaCampoValorDestino;
	
	private List<Formatos> arrFormatos;
	
	private List<Formatos> formatosAdicionarOrigen;
	
	private List<Formatos> formatosAdicionarDestino;
	
	private List<CortesFormato> cortesFormatosOrigen;
	
	private List<CortesFormato> cortesFormatosDestino;
	
	private List<DetallesFormato> detallesFormatoOrigen;
	
	private List<DetallesFormato> detallesFormatoDestino;
		
	private List<Operadores> operadores;
	
	private List<Alertas> alertasFormato;
	
	private Integer fechaCorteInicialSeleccionada;
	
	private Formatos formatoOrigenAdicionarSeleccionado;
	
	private Formatos formatoDestinoAdicionarSeleccionado;
	
	private DetallesFormato posicionCampoFiltroOrigenSeleccionado;
	
	private DetallesFormato campoValorOrigenSeleccionado;
	
	private CortesFormato corteOrigenSelecionado;
	
	private CortesFormato corteDestinoSelecionado;
	
	private DetallesFormato posicionCampoFiltroDestinoSeleccionado;
	
	private DetallesFormato campoValorDestinoSeleccionado;
	
	private Operadores operaSeleccionado;
	
	private Integer formatoOrigenSeleccionado;
	
	private Integer posicionCampoFiltroOrigen;
	
	private Integer valorFiltroOrigen;
	
	private Integer campoValorOrigen;
	
	private Integer operadorSeleccionado;
	
	private Integer fechaCorteDestinoSeleccionada;
	
	private Integer formatoDestinoSeleccionado;
	
	private Integer posicionCampoFiltroDestino;
	
	private Integer valorFiltroDestino;
	
	private Integer campoValorDestino;
	
	private String descripcionAlerta;
	
	private String valorConstante;
	
	private boolean valorConstanteAsignado;
	
	private Alertas alertaSeleccionada;
	
	private Integer idAlerta;
	
	private int numeroFilaSeleccionada;
	
	private List<AlertasModel> alertasFormatoModel;
	
	private String fechaCorte;
	
	private CortesFormato corteFormatoConsulta;
	
	
	
	@ManagedProperty(value = "#{serviceDao}")
	transient private ServiceDao serviceDao;
	
	public AlertasBean() {
		this.listaFormatos = new ArrayList<SelectItem>();
		this.arrFormatos = new ArrayList<Formatos>();
		this.formatosAdicionarOrigen = new ArrayList<Formatos>();
		this.formatosAdicionarDestino = new ArrayList<Formatos>();
		this.listaPosicionCampoFiltroOrigen = new ArrayList<SelectItem>();
		this.listaCampoValorOrigen = new ArrayList<SelectItem>();
		this.listaCortesOrigen = new ArrayList<SelectItem>();
		this.listaFormatosAdicionarOrigen = new ArrayList<SelectItem>();
		this.listaFormatosAdicionarDestino = new ArrayList<SelectItem>();
		this.operadores = new ArrayList<Operadores>();
		this.listaOperadores = new ArrayList<SelectItem>();
		this.listaPosicionCampoFiltroDestino = new ArrayList<SelectItem>();
		this.listaCampoValorDestino = new ArrayList<SelectItem>();
		this.listaCortesDestino = new ArrayList<SelectItem>();
		this.listaEntidades = new ArrayList<SelectItem>();
		this.entidades = new ArrayList<Entidades>();
		this.alertasFormatoModel = new ArrayList<AlertasBean.AlertasModel>();
	}

	@SuppressWarnings("unused")
	@PostConstruct
	private void init() {
		
		System.out.println(">>>> Iniciando PostConstructor ");
		cargarEntidades();
		cargarFormatos();
		cargarOperadores();
		cargarFormatosDestinoAdicionar();
		cargarFormatosOrigenAdicionar();
		
		if (idAlerta != null) {
			
			alertaSeleccionada = tomarObjetoListaSeleccionada(alertasFormato, idAlerta);
		}
		
		if (formatoOrigenSeleccionado != null || (alertaSeleccionada != null && alertaSeleccionada.getFormatoOrigen() != null)) {
			cargarCortesFormatosOrigenAdicionar();
			cargarDetallesFormatoOrigenAdicionar();
		}
		
		if (formatoDestinoSeleccionado != null || (alertaSeleccionada != null && alertaSeleccionada.getForCodigoD() != null)) {
			cargarCortesFormatosDestinoAdicionar();
			cargarDetallesFormatoDestinoAdicionar();
		}
		
		
	}
	
	public void cargarDependientesOrigen(ValueChangeEvent e) {
		if (null != e.getNewValue()) {		
			formatoOrigenSeleccionado = (Integer) e.getNewValue();
			formatoOrigenAdicionarSeleccionado = tomarObjetoListaSeleccionada(formatosAdicionarOrigen, formatoOrigenSeleccionado);
			cargarDetallesFormatoOrigenAdicionar();
			cargarCortesFormatosOrigenAdicionar();
		}
	}
	
	public void cargarDependientesDestino(ValueChangeEvent e) {
		
		if ( null !=  e.getNewValue()) {
			formatoDestinoSeleccionado = (Integer) e.getNewValue();
			cargarDetallesFormatoDestinoAdicionar();
			cargarCortesFormatosDestinoAdicionar();
		}
	}
	
	public void validarCampoConstante(ValueChangeEvent e) {
		
		if ( null !=  e.getNewValue()) {
			valorConstante = (String) e.getNewValue();
			
			if (!"".equals(valorConstante) && !" ".equals(valorConstante)) {
				
				formatoDestinoSeleccionado = null;
				formatoDestinoAdicionarSeleccionado = null;
				
				fechaCorteDestinoSeleccionada = null;
				corteDestinoSelecionado = null;
				
				posicionCampoFiltroDestino = null;
				posicionCampoFiltroDestinoSeleccionado = null;
				
				campoValorDestino = null;
				campoValorDestinoSeleccionado = null;
				
				valorFiltroDestino = null;				
				valorConstanteAsignado = true;
				
			} else {
				
				valorConstante = null;
				valorConstanteAsignado = false;
			}
		}
	}
	
	
	public String navModificar() {
		return "modificar";
	}
	
	public String navVerDetalle() {
		return "detalle";
	}
	
	private void cargarEntidades() {
		
		Set<Entidades> entidadesUsuario = UserDetailsUtils.usuarioLogged().getEntidades();
		entidades = new ArrayList<Entidades>(entidadesUsuario);

		for (Entidades e : entidades) {
			listaEntidades.add(new SelectItem(e.getEntCodigo(), e
					.getEntObjetoSocial()));
		}
		
	}
	
	@SuppressWarnings("unchecked")
	private void cargarFormatos() {
		arrFormatos = (List<Formatos>) serviceDao.getGenericCommonDao()
				.loadAll(Formatos.class);
		
		listaFormatos = new ArrayList<SelectItem>();

		for (Formatos f : arrFormatos) {
			listaFormatos
					.add(new SelectItem(f.getForCodigo(), f.getForNombre()));
			
		}
		System.out.println("Sale formatos " + arrFormatos.size());
	}
	
	@SuppressWarnings("unchecked")
	private void cargarOperadores() {
		operadores = (List<Operadores>) serviceDao.getGenericCommonDao().loadAll(Operadores.class);
		
		listaOperadores = new ArrayList<SelectItem>();
		
		for (Operadores o : operadores) {
			SelectItem item = new SelectItem(o.getOpeCodigo(), o.getOpeDescripcion());
			listaOperadores.add(item);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void cargarFormatosOrigenAdicionar() {
		formatosAdicionarOrigen = (List<Formatos>) serviceDao.getGenericCommonDao().loadAll(Formatos.class);
		
		listaFormatosAdicionarOrigen = new ArrayList<SelectItem>();
		
		for (Formatos f : formatosAdicionarOrigen) {
			SelectItem item = new SelectItem(f.getForCodigo(), f.getForDescripcion());
			listaFormatosAdicionarOrigen.add(item);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void cargarFormatosDestinoAdicionar() {
		formatosAdicionarDestino = (List<Formatos>) serviceDao.getGenericCommonDao().loadAll(Formatos.class);
		
		listaFormatosAdicionarDestino = new ArrayList<SelectItem>();
		
		for (Formatos f : formatosAdicionarDestino) {
			SelectItem item = new SelectItem(f.getForCodigo(), f.getForDescripcion());
			listaFormatosAdicionarDestino.add(item);
		}
	}
	
	private void cargarDetallesFormatoOrigenAdicionar() {
		Integer codFormato = formatoOrigenAdicionarSeleccionado.getForCodigo();
		detallesFormatoOrigen = serviceDao.getDetallesFormatoDao().obtenerDetallesFormatoPorFormato(codFormato);
		
		listaPosicionCampoFiltroOrigen = new ArrayList<SelectItem>();
		listaCampoValorOrigen = new ArrayList<SelectItem>();
		
		for (DetallesFormato df : detallesFormatoOrigen) {
			SelectItem posicion = new SelectItem(df.getDetCodigo(), df.getDetPosicion().toString() + " - " + df.getDetNombre());
			listaPosicionCampoFiltroOrigen.add(posicion);
			
			SelectItem campoValor = new SelectItem(df.getDetCodigo(), df.getDetNombre());
			listaCampoValorOrigen.add(campoValor);
		}
	}
	

	private void cargarDetallesFormatoDestinoAdicionar() {
		Integer codFormato = formatoDestinoSeleccionado;
		detallesFormatoDestino = serviceDao.getDetallesFormatoDao().obtenerDetallesFormatoPorFormato(codFormato);
		
		listaPosicionCampoFiltroDestino = new ArrayList<SelectItem>();
		listaCampoValorDestino = new ArrayList<SelectItem>();
		
		for (DetallesFormato df : detallesFormatoDestino) {
			SelectItem posicion = new SelectItem(df.getDetCodigo(), df.getDetPosicion().toString() + " - " + df.getDetNombre());
			listaPosicionCampoFiltroDestino.add(posicion);
			
			SelectItem campoValor = new SelectItem(df.getDetCodigo(), df.getDetNombre());
			listaCampoValorDestino.add(campoValor);
		}
	}
		

	private void cargarCortesFormatosOrigenAdicionar() {
		
		Integer codFormato = formatoOrigenAdicionarSeleccionado.getForCodigo();
		cortesFormatosOrigen = serviceDao.getCortesFormatoDao().obtenerCortesFormatoPorFormato(codFormato);
		
		listaCortesOrigen = new ArrayList<SelectItem>();
		
		for (CortesFormato cf : cortesFormatosOrigen) {
			SelectItem item = new SelectItem(cf.getFcrCodigo(), cf.getFcrCorte());
			listaCortesOrigen.add(item);
		}
	}
	

	private void cargarCortesFormatosDestinoAdicionar() {
		Integer codFormato = formatoDestinoSeleccionado;
		cortesFormatosDestino = serviceDao.getCortesFormatoDao().obtenerCortesFormatoPorFormato(codFormato);
		
		listaCortesDestino = new ArrayList<SelectItem>();
		
		for (CortesFormato cf : cortesFormatosDestino) {
			SelectItem item = new SelectItem(cf.getFcrCodigo(), cf.getFcrCorte());
			listaCortesDestino.add(item);
		}
	}
	
	/**
	 * @return the serviceDao
	 */
	public ServiceDao getServiceDao() {
		return serviceDao;
	}

	/**
	 * @param serviceDao
	 *            the serviceDao to set
	 */
	public void setServiceDao(ServiceDao serviceDao) {
		this.serviceDao = serviceDao;
	}

	/**
	 * @return the formatoSeleccionado
	 */
	public Integer getFormatoSeleccionado() {
		return formatoSeleccionado;
	}

	/**
	 * @param formatoSeleccionado the formatoSeleccionado to set
	 */
	public void setFormatoSeleccionado(Integer formatoSeleccionado) {
		this.formatoSeleccionado = formatoSeleccionado;
	}

	/**
	 * @return the listaFormatos
	 */
	public List<SelectItem> getListaFormatos() {
		return listaFormatos;
	}

	/**
	 * @param listaFormatos the listaFormatos to set
	 */
	public void setListaFormatos(List<SelectItem> listaFormatos) {
		this.listaFormatos = listaFormatos;
	}
	
	/**
	 * Accion Buscar
	 * 
	 * @param e
	 */
	public void buscar(ActionEvent e) {
		
		if (formatoSeleccionado != null) {
			
			alertasFormato = serviceDao.getAlertasDao().obtenerAlertasPorFormato(formatoSeleccionado);			
			alertasFormatoModel = new ArrayList<AlertasBean.AlertasModel>(alertasFormato.size() + 1);
			
			for (Alertas a : alertasFormato) {
				
				AlertasModel alerta = null; 
				
				if (a.getForCodigoD() != null && !(a.getForCodigoD().compareTo(0) == 0)) {
					
					Formatos formatoDest = (Formatos) serviceDao.getGenericCommonDao().read(Formatos.class, a.getForCodigoD());	
					CortesFormato corteDest = (CortesFormato) serviceDao.getGenericCommonDao().read(CortesFormato.class, a.getFcrCodigoD());
					DetallesFormato detalleDest = (DetallesFormato) serviceDao.getGenericCommonDao().read(DetallesFormato.class, a.getDetCodigoD());
					DetallesFormato detallePosDest = (DetallesFormato) serviceDao.getGenericCommonDao().read(DetallesFormato.class, a.getDetCodigoValorD());
					
					alerta = new AlertasModel(a, formatoDest, corteDest, detalleDest, detallePosDest);
					
				} else {
					
					alerta = new AlertasModel(a, null, null, null, null);
				}			
				
				alertasFormatoModel.add(alerta);				
			}
		}
		
	}
	
	public void buscar(Integer idFormato) {
		
		alertasFormato = serviceDao.getAlertasDao().obtenerAlertasPorFormato(idFormato);			
		alertasFormatoModel = new ArrayList<AlertasBean.AlertasModel>(alertasFormato.size() + 1);
		
		for (Alertas a : alertasFormato) {
			
			AlertasModel alerta = null; 
			
			if (a.getForCodigoD() != null && !(a.getForCodigoD().compareTo(0) == 0)) {
				
				Formatos formatoDest = (Formatos) serviceDao.getGenericCommonDao().read(Formatos.class, a.getForCodigoD());	
				CortesFormato corteDest = (CortesFormato) serviceDao.getGenericCommonDao().read(CortesFormato.class, a.getFcrCodigoD());
				DetallesFormato detalleDest = (DetallesFormato) serviceDao.getGenericCommonDao().read(DetallesFormato.class, a.getDetCodigoD());
				DetallesFormato detallePosDest = (DetallesFormato) serviceDao.getGenericCommonDao().read(DetallesFormato.class, a.getDetCodigoValorD());
				
				alerta = new AlertasModel(a, formatoDest, corteDest, detalleDest, detallePosDest);
				
			} else {
				
				alerta = new AlertasModel(a, null, null, null, null);
			}			
			
			alertasFormatoModel.add(alerta);				
		}
	}
	
	/**
	 * Accion Buscar
	 * 
	 * @param e
	 */
	public String guardar() {
		
		String page = null;
		
		try {
			Alertas alerta = new Alertas();
			
			alerta.setAltDescripcion(descripcionAlerta);
			
			if (formatoOrigenAdicionarSeleccionado == null)
				formatoOrigenAdicionarSeleccionado = tomarObjetoListaSeleccionada(arrFormatos, formatoOrigenSeleccionado);		
			
			alerta.setFormatoOrigen(formatoOrigenAdicionarSeleccionado);
			
			corteOrigenSelecionado = tomarObjetoListaSeleccionada(cortesFormatosOrigen, fechaCorteInicialSeleccionada);	
			alerta.setCortesFormatoOrigen(corteOrigenSelecionado);
			
			posicionCampoFiltroOrigenSeleccionado = tomarObjetoListaSeleccionada(detallesFormatoOrigen, posicionCampoFiltroOrigen);
			alerta.setDetallesFormatoValorOrigen(posicionCampoFiltroOrigenSeleccionado);
			
			campoValorOrigenSeleccionado = tomarObjetoListaSeleccionada(detallesFormatoOrigen, campoValorOrigen);
			alerta.setDetallesFormatoOrigen(campoValorOrigenSeleccionado);
			
			alerta.setAltFiltroO(String.valueOf(valorFiltroOrigen));
			
			operaSeleccionado = tomarObjetoListaSeleccionada(operadores, operadorSeleccionado);
			alerta.setOperadores(operaSeleccionado);
			
			if (valorConstante == null || "".equals(valorConstante)) {
				
				alerta.setForCodigoD(formatoDestinoSeleccionado);				
				alerta.setFcrCodigoD(fechaCorteDestinoSeleccionada);		
				alerta.setDetCodigoD(campoValorDestino);
				alerta.setDetCodigoValorD(posicionCampoFiltroDestino);
				alerta.setAltFiltroD(String.valueOf(valorFiltroDestino));
				
			} else {
				
				alerta.setAltValorConstante(valorConstante);
			}			
			
			serviceDao.getAlertasDao().create(Alertas.class, alerta);
			page = cancelar();
			
			FacesUtils.addFacesMessageFromBundle(Constants.EXITO_OPERACIONES_ALERTAS, FacesMessage.SEVERITY_INFO);

			
		} catch (Exception ex) {
			
			FacesUtils.addFacesMessageFromBundle(Constants.ERROR_OPERACIONES_ALERTAS, FacesMessage.SEVERITY_ERROR, new String[]{ex.getMessage()});

		}
				
		return page;
	}
	
	/**
	 * Accion Actualizar
	 * 
	 */
	public String actualizar() {
		
		String page = null;
		System.out.println("Inicia Actualizacion >>>>");
		
		try {
			
			formatoOrigenAdicionarSeleccionado = tomarObjetoListaSeleccionada(arrFormatos, formatoOrigenSeleccionado);		
			alertaSeleccionada.setFormatoOrigen(formatoOrigenAdicionarSeleccionado);
			
			corteOrigenSelecionado = tomarObjetoListaSeleccionada(cortesFormatosOrigen, fechaCorteInicialSeleccionada);		
			alertaSeleccionada.setCortesFormatoOrigen(corteOrigenSelecionado);
			
			posicionCampoFiltroOrigenSeleccionado = tomarObjetoListaSeleccionada(detallesFormatoOrigen, posicionCampoFiltroOrigen);
			alertaSeleccionada.setDetallesFormatoValorOrigen(posicionCampoFiltroOrigenSeleccionado);
			
			campoValorOrigenSeleccionado = tomarObjetoListaSeleccionada(detallesFormatoOrigen, campoValorOrigen);
			alertaSeleccionada.setDetallesFormatoOrigen(campoValorOrigenSeleccionado);
			
     		operaSeleccionado = tomarObjetoListaSeleccionada(operadores, operadorSeleccionado);
			alertaSeleccionada.setOperadores(operaSeleccionado);
			
			if (null == alertaSeleccionada.getAltValorConstante() || "".equals(alertaSeleccionada.getAltValorConstante()))
					alertaSeleccionada.setAltValorConstante(null);
			
			System.out.println("Actualizando >>>>");
			serviceDao.getGenericCommonDao().update(Alertas.class, alertaSeleccionada);			
			page = cancelar();
			
			FacesUtils.addFacesMessageFromBundle(Constants.EXITO_OPERACIONES_ALERTAS, FacesMessage.SEVERITY_INFO);
			
		} catch (Exception ex) {
			
			System.out.println("Fallo Actualizacion >>>>");
			
			FacesUtils.addFacesMessageFromBundle(Constants.ERROR_OPERACIONES_ALERTAS, FacesMessage.SEVERITY_ERROR, new String[]{ex.getMessage()});

		}
		
		System.out.println("Fin Actualizacion >>>>");
		
		return page;
		
	}
	
	/**
	 * Accion Actualizar
	 * 
	 */
	public String eliminar() {
		
		String page = null;
			
		try {
			
			FacesContext ctx = FacesContext.getCurrentInstance();
		    String idAlert = ctx.getExternalContext().getRequestParameterMap().get("idAlerta");			   
		    
		    Alertas alertaEliminar = (Alertas) serviceDao.getGenericCommonDao().read(Alertas.class, Integer.valueOf(idAlert)); 
		     
		     
			System.out.println("Alerta Seleccionada >>>>> " + alertaEliminar.getAltDescripcion());
			serviceDao.getGenericCommonDao().delete(Alertas.class, alertaEliminar);			
			
			buscar(formatoSeleccionado);
			
			
			FacesUtils.addFacesMessageFromBundle(Constants.EXITO_OPERACIONES_ALERTAS, FacesMessage.SEVERITY_INFO);
			
			
		} catch (Exception ex) {
			
			FacesUtils.addFacesMessageFromBundle(Constants.ERROR_OPERACIONES_ALERTAS, FacesMessage.SEVERITY_ERROR, new String[]{ex.getMessage()});

		}
		
		return page;
		
		
	}
	
	/**
	 * Accion Buscar
	 * 
	 * @param e
	 */
	public String cancelar() {
		
		return "listado";
		
	}
	
	/**
	 * Accion Adicionar
	 * 
	 */
	public String adicionar() {
	
		return "adicionarAlerta";
	}

	/**
	 * @return the alertasFormato
	 */
	public List<Alertas> getAlertasFormato() {
		return alertasFormato;
	}

	/**
	 * @param alertasFormato the alertasFormato to setformato
	 */
	public void setAlertasFormato(List<Alertas> alertasFormato) {
		this.alertasFormato = alertasFormato;
	}

	/**
	 * @return the fechaCorteInicialSeleccionada@SecuredAction(keyAction = CommonsActionsKey.ADD,
	 */
	public Integer getFechaCorteInicialSeleccionada() {
		return fechaCorteInicialSeleccionada;
	}

	/**
	 * @param fechaCorteInicialSeleccionada the fechaCorteInicialSeleccionada to set
	 */
	public void setFechaCorteInicialSeleccionada(Integer fechaCorteInicialSeleccionada) {
		this.fechaCorteInicialSeleccionada = fechaCorteInicialSeleccionada;
	}

	/**
	 * @return the formatoOrigenAdicionarSeleccionado
	 */
	public Formatos getFormatoOrigenAdicionarSeleccionado() {
		return formatoOrigenAdicionarSeleccionado;
	}

	/**
	 * @param formatoOrigenAdicionarSeleccionado the formatoOrigenAdicionarSeleccionado to set
	 */
	public void setFormatoOrigenAdicionarSeleccionado(
			Formatos formatoOrigenAdicionarSeleccionado) {
		this.formatoOrigenAdicionarSeleccionado = formatoOrigenAdicionarSeleccionado;
	}

	/**
	 * @return the posicionCampoFiltroOrigen
	 */
	public Integer getPosicionCampoFiltroOrigen() {
		return posicionCampoFiltroOrigen;
	}

	/**
	 * @param posicionCampoFiltroOrigen the posicionCampoFiltroOrigen to set
	 */
	public void setPosicionCampoFiltroOrigen(Integer posicionCampoFiltroOrigen) {
		this.posicionCampoFiltroOrigen = posicionCampoFiltroOrigen;
	}

	/**
	 * @return the valorFiltroOrigen
	 */
	public Integer getValorFiltroOrigen() {
		return valorFiltroOrigen;
	}

	/**
	 * @param valorFiltroOrigen the valorFiltroOrigen to set
	 */
	public void setValorFiltroOrigen(Integer valorFiltroOrigen) {
		this.valorFiltroOrigen = valorFiltroOrigen;
	}

	/**
	 * @return the campoValorOrigen
	 */
	public Integer getCampoValorOrigen() {
		return campoValorOrigen;
	}

	/**
	 * @param campoValorOrigen the campoValorOrigen to set
	 */
	public void setCampoValorOrigen(Integer campoValorOrigen) {
		this.campoValorOrigen = campoValorOrigen;
	}

	/**
	 * @return the operadorSeleccionado
	 */
	public Integer getOperadorSeleccionado() {
		return operadorSeleccionado;
	}

	/**
	 * @param operadorSeleccionado the operadorSeleccionado to set
	 */
	public void setOperadorSeleccionado(Integer operadorSeleccionado) {
		this.operadorSeleccionado = operadorSeleccionado;
	}

	/**
	 * @return the fechaCorteDestinoSeleccionada
	 */
	public Integer getFechaCorteDestinoSeleccionada() {
		return fechaCorteDestinoSeleccionada;
	}

	/**
	 * @param fechaCorteDestinoSeleccionada the fechaCorteDestinoSeleccionada to set
	 */
	public void setFechaCorteDestinoSeleccionada(Integer fechaCorteDestinoSeleccionada) {
		this.fechaCorteDestinoSeleccionada = fechaCorteDestinoSeleccionada;
	}

	/**
	 * @return the formatoDestinoAdicionarSeleccionado
	 */
	public Integer getFormatoDestinoSeleccionado() {
		return formatoDestinoSeleccionado;
	}

	/**
	 * @param formatoDestinoAdicionarSeleccionado the formatoDestinoAdicionarSeleccionado to set
	 */
	public void setFormatoDestinoSeleccionado(
			Integer formatoDestinoSeleccionado) {
		this.formatoDestinoSeleccionado = formatoDestinoSeleccionado;
	}

	/**
	 * @return the posicionCampoFiltroDestino
	 */
	public Integer getPosicionCampoFiltroDestino() {
		return posicionCampoFiltroDestino;
	}

	/**
	 * @param posicionCampoFiltroDestino the posicionCampoFiltroDestino to set
	 */
	public void setPosicionCampoFiltroDestino(Integer posicionCampoFiltroDestino) {
		this.posicionCampoFiltroDestino = posicionCampoFiltroDestino;
	}

	/**
	 * @return the valorFiltroDestino
	 */
	public Integer getValorFiltroDestino() {
		return valorFiltroDestino;
	}

	/**
	 * @param valorFiltroDestino the valorFiltroDestino to set
	 */
	public void setValorFiltroDestino(Integer valorFiltroDestino) {
		this.valorFiltroDestino = valorFiltroDestino;
	}

	/**
	 * @return the campoValorDestino
	 */
	public Integer getCampoValorDestino() {
		return campoValorDestino;
	}

	/**
	 * @param campoValorDestino the campoValorDestino to set
	 */
	public void setCampoValorDestino(Integer campoValorDestino) {
		this.campoValorDestino = campoValorDestino;
	}

	/**
	 * @return the listaFormatosAdicionarOrigen
	 */
	public List<SelectItem> getListaFormatosAdicionarOrigen() {
		return listaFormatosAdicionarOrigen;
	}

	/**
	 * @param listaFormatosAdicionarOrigen the listaFormatosAdicionarOrigen to set
	 */
	public void setListaFormatosAdicionarOrigen(
			List<SelectItem> listaFormatosAdicionarOrigen) {
		this.listaFormatosAdicionarOrigen = listaFormatosAdicionarOrigen;
	}

	/**
	 * @return the listaFormatosAdicionarDestino
	 */
	public List<SelectItem> getListaFormatosAdicionarDestino() {
		return listaFormatosAdicionarDestino;
	}

	/**
	 * @param listaFormatosAdicionarDestino the listaFormatosAdicionarDestino to set
	 */
	public void setListaFormatosAdicionarDestino(
			List<SelectItem> listaFormatosAdicionarDestino) {
		this.listaFormatosAdicionarDestino = listaFormatosAdicionarDestino;
	}

	/**
	 * @return the formatosAdicionarOrigen
	 */
	public List<Formatos> getFormatosAdicionarOrigen() {
		return formatosAdicionarOrigen;
	}

	/**
	 * @param formatosAdicionarOrigen the formatosAdicionarOrigen to set
	 */
	public void setFormatosAdicionarOrigen(List<Formatos> formatosAdicionarOrigen) {
		this.formatosAdicionarOrigen = formatosAdicionarOrigen;
	}

	/**
	 * @return the formatosAdicionarDestino
	 */
	public List<Formatos> getFormatosAdicionarDestino() {
		return formatosAdicionarDestino;
	}

	/**
	 * @param formatosAdicionarDestino the formatosAdicionarDestino to set
	 */
	public void setFormatosAdicionarDestino(List<Formatos> formatosAdicionarDestino) {
		this.formatosAdicionarDestino = formatosAdicionarDestino;
	}

	/**
	 * @return the listaOperadores
	 */
	public List<SelectItem> getListaOperadores() {
		return listaOperadores;
	}

	/**
	 * @param listaOperadores the listaOperadores to set
	 */
	public void setListaOperadores(List<SelectItem> listaOperadores) {
		this.listaOperadores = listaOperadores;
	}

	/**
	 * @return the listaCortesOrigen
	 */
	public List<SelectItem> getListaCortesOrigen() {
		return listaCortesOrigen;
	}

	/**
	 * @param listaCortesOrigen the listaCortesOrigen to set
	 */
	public void setListaCortesOrigen(List<SelectItem> listaCortesOrigen) {
		this.listaCortesOrigen = listaCortesOrigen;
	}

	/**
	 * @return the listaCortesDestino
	 */
	public List<SelectItem> getListaCortesDestino() {		
		return listaCortesDestino;
	}

	/**
	 * @param listaCortesDestino the listaCortesDestino to set
	 */
	public void setListaCortesDestino(List<SelectItem> listaCortesDestino) {
		this.listaCortesDestino = listaCortesDestino;
	}

	/**
	 * @return the listaPosicionCampoFiltroOrigen
	 */
	public List<SelectItem> getListaPosicionCampoFiltroOrigen() {
		return listaPosicionCampoFiltroOrigen;
	}

	/**
	 * @param listaPosicionCampoFiltroOrigen the listaPosicionCampoFiltroOrigen to set
	 */
	public void setListaPosicionCampoFiltroOrigen(
			List<SelectItem> listaPosicionCampoFiltroOrigen) {
		this.listaPosicionCampoFiltroOrigen = listaPosicionCampoFiltroOrigen;
	}

	/**
	 * @return the listaCampoValorOrigen
	 */
	public List<SelectItem> getListaCampoValorOrigen() {
		return listaCampoValorOrigen;
	}

	/**
	 * @param listaCampoValorOrigen the listaCampoValorOrigen to set
	 */
	public void setListaCampoValorOrigen(List<SelectItem> listaCampoValorOrigen) {
		this.listaCampoValorOrigen = listaCampoValorOrigen;
	}

	/**
	 * @return the listaPosicionCampoFiltroDestino
	 */
	public List<SelectItem> getListaPosicionCampoFiltroDestino() {
		return listaPosicionCampoFiltroDestino;
	}

	/**
	 * @param listaPosicionCampoFiltroDestino the listaPosicionCampoFiltroDestino to set
	 */
	public void setListaPosicionCampoFiltroDestino(
			List<SelectItem> listaPosicionCampoFiltroDestino) {
		this.listaPosicionCampoFiltroDestino = listaPosicionCampoFiltroDestino;
	}

	/**
	 * @return the listaCampoValorDestino
	 */
	public List<SelectItem> getListaCampoValorDestino() {
		return listaCampoValorDestino;
	}

	/**
	 * @param listaCampoValorDestino the listaCampoValorDestino to set
	 */
	public void setListaCampoValorDestino(List<SelectItem> listaCampoValorDestino) {
		this.listaCampoValorDestino = listaCampoValorDestino;
	}

	/**
	 * @return the operadores
	 */
	public List<Operadores> getOperadores() {
		return operadores;
	}

	/**
	 * @param operadores the operadores to set
	 */
	public void setOperadores(List<Operadores> operadores) {
		this.operadores = operadores;
	}

	/**
	 * @return the cortesFormatosOrigen
	 */
	public List<CortesFormato> getCortesFormatosOrigen() {
		return cortesFormatosOrigen;
	}

	/**
	 * @param cortesFormatosOrigen the cortesFormatosOrigen to set
	 */
	public void setCortesFormatosOrigen(List<CortesFormato> cortesFormatosOrigen) {
		this.cortesFormatosOrigen = cortesFormatosOrigen;
	}

	/**
	 * @return the cortesFormatosDestino
	 */
	public List<CortesFormato> getCortesFormatosDestino() {
		return cortesFormatosDestino;
	}

	/**
	 * @param cortesFormatosDestino the cortesFormatosDestino to set
	 */
	public void setCortesFormatosDestino(List<CortesFormato> cortesFormatosDestino) {
		this.cortesFormatosDestino = cortesFormatosDestino;
	}

	/**
	 * @return the detallesFormatosPosicionOrigen
	 */
	public List<DetallesFormato> getDetallesFormatoOrigen() {
		return detallesFormatoOrigen;
	}

	/**
	 * @param detallesFormatosPosicionOrigen the detallesFormatosPosicionOrigen to set
	 */
	public void setDetallesFormatoOrigen(
			List<DetallesFormato> detallesFormatoOrigen) {
		this.detallesFormatoOrigen = detallesFormatoOrigen;
	}

	/**
	 * @return the detallesFormatosCampoValorOrigen
	 */
	public List<DetallesFormato> getDetallesFormatoDestino() {
		return detallesFormatoDestino;
	}

	/**
	 * @param detallesFormatosCampoValorOrigen the detallesFormatosCampoValorOrigen to set
	 */
	public void setDetallesFormatoDestino(
			List<DetallesFormato> detallesFormatoDestino) {
		this.detallesFormatoDestino = detallesFormatoDestino;
	}

	public Integer getFormatoOrigenSeleccionado() {
		return formatoOrigenSeleccionado;
	}

	public void setFormatoOrigenSeleccionado(Integer formatoOrigenSeleccionado) {
		this.formatoOrigenSeleccionado = formatoOrigenSeleccionado;
	}

	/**
	 * @return the descripcionAlerta
	 */
	public String getDescripcionAlerta() {
		return this.descripcionAlerta;
	}

	/**
	 * @param descripcionAlerta the descripcionAlerta to set
	 */
	public void setDescripcionAlerta(String descripcionAlerta) {
		this.descripcionAlerta = descripcionAlerta;
	}

	/**
	 * @return the valorConstante
	 */
	public String getValorConstante() {
		return this.valorConstante;
	}

	/**
	 * @param valorConstante the valorConstante to set
	 */
	public void setValorConstante(String valorConstante) {
		this.valorConstante = valorConstante;
	}

	/**
	 * @return the valorConstanteAsignado
	 */
	public boolean isValorConstanteAsignado() {
		return this.valorConstanteAsignado;
	}

	/**
	 * @param valorConstanteAsignado the valorConstanteAsignado to set
	 */
	public void setValorConstanteAsignado(boolean valorConstanteAsignado) {
		this.valorConstanteAsignado = valorConstanteAsignado;
	}
	
	
	
	
	/**
	 * @return the formatoDestinoAdicionarSeleccionado
	 */
	public Formatos getFormatoDestinoAdicionarSeleccionado() {
		return this.formatoDestinoAdicionarSeleccionado;
	}

	/**
	 * @param formatoDestinoAdicionarSeleccionado the formatoDestinoAdicionarSeleccionado to set
	 */
	public void setFormatoDestinoAdicionarSeleccionado(Formatos formatoDestinoAdicionarSeleccionado) {
		this.formatoDestinoAdicionarSeleccionado = formatoDestinoAdicionarSeleccionado;
	}

	/**
	 * @return the posicionCampoFiltroOrigenSeleccionado
	 */
	public DetallesFormato getPosicionCampoFiltroOrigenSeleccionado() {
		return this.posicionCampoFiltroOrigenSeleccionado;
	}

	/**
	 * @param posicionCampoFiltroOrigenSeleccionado the posicionCampoFiltroOrigenSeleccionado to set
	 */
	public void setPosicionCampoFiltroOrigenSeleccionado(DetallesFormato posicionCampoFiltroOrigenSeleccionado) {
		this.posicionCampoFiltroOrigenSeleccionado = posicionCampoFiltroOrigenSeleccionado;
	}

	/**
	 * @return the campoValorOrigenSeleccionado
	 */
	public DetallesFormato getCampoValorOrigenSeleccionado() {
		return this.campoValorOrigenSeleccionado;
	}

	/**
	 * @param campoValorOrigenSeleccionado the campoValorOrigenSeleccionado to set
	 */
	public void setCampoValorOrigenSeleccionado(DetallesFormato campoValorOrigenSeleccionado) {
		this.campoValorOrigenSeleccionado = campoValorOrigenSeleccionado;
	}

	/**
	 * @return the corteOrigenSelecionado
	 */
	public CortesFormato getCorteOrigenSelecionado() {
		return this.corteOrigenSelecionado;
	}

	/**
	 * @param corteOrigenSelecionado the corteOrigenSelecionado to set
	 */
	public void setCorteOrigenSelecionado(CortesFormato corteOrigenSelecionado) {
		this.corteOrigenSelecionado = corteOrigenSelecionado;
	}

	/**
	 * @return the corteDestinoSelecionado
	 */
	public CortesFormato getCorteDestinoSelecionado() {
		return this.corteDestinoSelecionado;
	}

	/**
	 * @param corteDestinoSelecionado the corteDestinoSelecionado to set
	 */
	public void setCorteDestinoSelecionado(CortesFormato corteDestinoSelecionado) {
		this.corteDestinoSelecionado = corteDestinoSelecionado;
	}

	/**
	 * @return the posicionCampoFiltroDestinoSeleccionado
	 */
	public DetallesFormato getPosicionCampoFiltroDestinoSeleccionado() {
		return this.posicionCampoFiltroDestinoSeleccionado;
	}

	/**
	 * @param posicionCampoFiltroDestinoSeleccionado the posicionCampoFiltroDestinoSeleccionado to set
	 */
	public void setPosicionCampoFiltroDestinoSeleccionado(DetallesFormato posicionCampoFiltroDestinoSeleccionado) {
		this.posicionCampoFiltroDestinoSeleccionado = posicionCampoFiltroDestinoSeleccionado;
	}

	/**
	 * @return the campoValorDestinoSeleccionado
	 */
	public DetallesFormato getCampoValorDestinoSeleccionado() {
		return this.campoValorDestinoSeleccionado;
	}

	/**
	 * @param campoValorDestinoSeleccionado the campoValorDestinoSeleccionado to set
	 */
	public void setCampoValorDestinoSeleccionado(DetallesFormato campoValorDestinoSeleccionado) {
		this.campoValorDestinoSeleccionado = campoValorDestinoSeleccionado;
	}

	/**
	 * @return the operaSeleccionado
	 */
	public Operadores getOperaSeleccionado() {
		return this.operaSeleccionado;
	}

	/**
	 * @param operaSeleccionado the operaSeleccionado to set
	 */
	public void setOperaSeleccionado(Operadores operaSeleccionado) {
		this.operaSeleccionado = operaSeleccionado;
	}
	
	

	/**
	 * @return the alertaSeleccionada
	 */
	public Alertas getAlertaSeleccionada() {
		return this.alertaSeleccionada;
	}

	/**
	 * @param alertaSeleccionada the alertaSeleccionada to set
	 */
	public void setAlertaSeleccionada(Alertas alertaSeleccionada) {
		this.alertaSeleccionada = alertaSeleccionada;
	}
	

	/**
	 * @return the idAlerta
	 */
	public Integer getIdAlerta() {
		return this.idAlerta;
	}

	/**
	 * @param idAlerta the idAlerta to set
	 */
	public void setIdAlerta(Integer idAlerta) {
		this.idAlerta = idAlerta;
	}
	
	

	/**
	 * @return the numeroFilaSeleccionada
	 */
	public int getNumeroFilaSeleccionada() {
		return this.numeroFilaSeleccionada;
	}

	/**
	 * @param numeroFilaSeleccionada the numeroFilaSeleccionada to set
	 */
	public void setNumeroFilaSeleccionada(int numeroFilaSeleccionada) {
		this.numeroFilaSeleccionada = numeroFilaSeleccionada;
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
	 * @return the entidadSeleccionadaObj
	 */
	public Entidades getEntidadSeleccionadaObj() {
		return this.entidadSeleccionadaObj;
	}

	/**
	 * @param entidadSeleccionadaObj the entidadSeleccionadaObj to set
	 */
	public void setEntidadSeleccionadaObj(Entidades entidadSeleccionadaObj) {
		this.entidadSeleccionadaObj = entidadSeleccionadaObj;
	}

	/**
	 * @return the entidadSeleccionadaValor
	 */
	public Integer getEntidadSeleccionadaValor() {
		return this.entidadSeleccionadaValor;
	}

	/**
	 * @param entidadSeleccionadaValor the entidadSeleccionadaValor to set
	 */
	public void setEntidadSeleccionadaValor(Integer entidadSeleccionadaValor) {
		this.entidadSeleccionadaValor = entidadSeleccionadaValor;
	}

	/**
	 * @return the corteFormatoSeleccionado
	 */
	public CortesFormato getCorteFormatoSeleccionado() {
		return this.corteFormatoSeleccionado;
	}

	/**
	 * @param corteFormatoSeleccionado the corteFormatoSeleccionado to set
	 */
	public void setCorteFormatoSeleccionado(CortesFormato corteFormatoSeleccionado) {
		this.corteFormatoSeleccionado = corteFormatoSeleccionado;
	}

	/**
	 * @return the fechaCorteSeleccionado
	 */
	public Date getFechaCorteSeleccionado() {
		return this.fechaCorteSeleccionado;
	}

	/**
	 * @param fechaCorteSeleccionado the fechaCorteSeleccionado to set
	 */
	public void setFechaCorteSeleccionado(Date fechaCorteSeleccionado) {
		this.fechaCorteSeleccionado = fechaCorteSeleccionado;
	}
	

	/**
	 * @return the alertasFormatoModel
	 */
	public List<AlertasModel> getAlertasFormatoModel() {
		return alertasFormatoModel;
	}

	/**
	 * @param alertasFormatoModel the alertasFormatoModel to set
	 */
	public void setAlertasFormatoModel(List<AlertasModel> alertasFormatoModel) {
		this.alertasFormatoModel = alertasFormatoModel;
	}

	/**
	 * @return the fechaCorte
	 */
	public String getFechaCorte() {
		return fechaCorte;
	}

	/**
	 * @param fechaCorte the fechaCorte to set
	 */
	public void setFechaCorte(String fechaCorte) {
		this.fechaCorte = fechaCorte;
	}
	
	
	/**
	 * @return the corteFormatoConsulta
	 */
	public CortesFormato getCorteFormatoConsulta() {
		return corteFormatoConsulta;
	}

	/**
	 * @param corteFormatoConsulta the corteFormatoConsulta to set
	 */
	public void setCorteFormatoConsulta(CortesFormato corteFormatoConsulta) {
		this.corteFormatoConsulta = corteFormatoConsulta;
	}

	@SuppressWarnings("unchecked")
	private <T> T tomarObjetoListaSeleccionada(List<T> lista, Integer seleccionado) {
		
		
		for (Object obj : lista) {
			
			if (obj instanceof Formatos) {
				
				Formatos formatoSeleccionado = (Formatos) obj; 
				
				if (seleccionado.compareTo(formatoSeleccionado.getForCodigo()) == 0) {
					
					return (T) formatoSeleccionado;
				}
			} else if (obj instanceof CortesFormato) {
				
				CortesFormato corteFormatoSeleccionado = (CortesFormato) obj; 
				
				if (seleccionado.compareTo(corteFormatoSeleccionado.getFcrCodigo()) == 0) {
					
					return (T) corteFormatoSeleccionado;
				}
			} else if (obj instanceof DetallesFormato) {
				
				DetallesFormato datalleFormatoSeleccionado = (DetallesFormato) obj; 
				
				if (seleccionado.compareTo(datalleFormatoSeleccionado.getDetCodigo()) == 0) {
					
					return (T) datalleFormatoSeleccionado;
				}
			} else if (obj instanceof Operadores) {
				
				Operadores operSeleccionado = (Operadores) obj; 
				
				if (seleccionado.compareTo(operSeleccionado.getOpeCodigo()) == 0) {
					
					return (T) operSeleccionado;
				}
			} else if (obj instanceof Entidades) {
				
				Entidades ent = (Entidades) obj;
				
				if (seleccionado.compareTo(ent.getEntCodigo()) == 0) {
					
					return (T) ent;
				}
			}
			
			
		}
		
		return null;
	}
	
	/**
	 * 
	 */
	public void cargarDatosModificar() {
		
		UIComponent componente = null;
		FacesContext ctx = FacesContext.getCurrentInstance();
		
		try {
			
			if(idAlerta == null){
				 
				 componente = ctx.getViewRoot().findComponent(ctx.getViewRoot().getClientId());
			     String idAlert = ctx.getExternalContext().getRequestParameterMap().get("idAlerta");			   
			     if(idAlert != null) this.idAlerta = Integer.parseInt(idAlert);
			}
			System.out.println("Tomando alerta con id >>>> " + this.idAlerta);
			if((alertaSeleccionada == null) &&  (idAlerta != null && !idAlerta.equals(""))){
				alertaSeleccionada = (Alertas) serviceDao.getGenericCommonDao().read(Alertas.class, idAlerta);	
				
				if (alertaSeleccionada != null && alertaSeleccionada.getFormatoOrigen() != null) {
					
					formatoOrigenAdicionarSeleccionado = alertaSeleccionada.getFormatoOrigen();
					formatoOrigenSeleccionado = formatoOrigenAdicionarSeleccionado.getForCodigo();
					fechaCorteInicialSeleccionada = alertaSeleccionada.getCortesFormatoOrigen().getFcrCodigo();
					posicionCampoFiltroOrigen = alertaSeleccionada.getDetallesFormatoValorOrigen().getDetCodigo();
					campoValorOrigen = alertaSeleccionada.getDetallesFormatoOrigen().getDetCodigo();
					operadorSeleccionado = alertaSeleccionada.getOperadores().getOpeCodigo();
					
					cargarCortesFormatosOrigenAdicionar();
					cargarDetallesFormatoOrigenAdicionar();
					
					validarCampoConstante(new ValueChangeEvent(componente, null, alertaSeleccionada.getAltValorConstante()));
					
					if (alertaSeleccionada.getAltValorConstante() == null) {
						
						formatoDestinoSeleccionado = alertaSeleccionada.getForCodigoD();
						fechaCorteDestinoSeleccionada = alertaSeleccionada.getFcrCodigoD();
						posicionCampoFiltroDestino = alertaSeleccionada.getDetCodigoValorD();
						campoValorDestino = alertaSeleccionada.getDetCodigoD();
						valorFiltroDestino = Integer.parseInt(alertaSeleccionada.getAltFiltroD());
						
						cargarCortesFormatosDestinoAdicionar();
						cargarDetallesFormatoDestinoAdicionar();
					}
				}				
			
			}else{
				alertaSeleccionada = new Alertas();
			}
		
		} catch (Exception e) {
		
			ctx.addMessage(null, new FacesMessage("Se ha presentado un error. Detalles: " + e.getMessage()));
		}
		
	}
	
	
	/**
	 * Metodo utilizado para consultar la fecha de corte activa dependiendo del formato seleccionado
	 * 
	 * @param e
	 */
	public void hallarFechaCorteFormato(ValueChangeEvent e) {

		if (null != e.getNewValue()) {

			formatoSeleccionado = (Integer) e.getNewValue();
			corteFormatoConsulta = serviceDao.getCortesFormatoDao().obtenerCortesFormatoActivoPorFormato();

			if (corteFormatoConsulta != null) {
				fechaCorte = corteFormatoConsulta.getFcrCorte();
			} else {
				fechaCorte = "Sin fecha de corte vigente";
			}
		}
	}
	
	
	public void buscarFiltros() {

		try {

			Integer corteFiltro = corteFormatoConsulta.getFcrCodigo();

			List<Alertas> alertasValidar = serviceDao.getAlertasDao().obtenerAlertasFormatoCorte(formatoSeleccionado, corteFiltro);
			alertasFormatoModel = new ArrayList<AlertasBean.AlertasModel>(alertasValidar.size() + 1);

			for (Alertas a : alertasValidar) {
				
				AlertasModel alert = null;
				
				DetallesFormato campoPosicionFiltro = a.getDetallesFormatoValorOrigen(); 
				Integer detPosId = campoPosicionFiltro.getDetCodigo();
				
				String valor = a.getAltFiltroO();
				
				List<RegistroCargue> posFiltro = serviceDao.getRegistroCargueDao().tomarRegistrosCarguePorFormatoEntidadCorteDetVal(formatoSeleccionado,
						entidadSeleccionadaValor, corteFiltro, detPosId, valor);
				
				if (posFiltro == null || posFiltro.size() == 0) {
					
					throw new Exception("Antes de ejecutar una alerta se debe realizar el cargue del formato!");
				}
				
				int idReg = posFiltro.get(0).getCrgNoRegistro();	
				DetallesFormato detCampoValorFiltro = a.getDetallesFormatoOrigen();				
				Integer idDetalleCampoFiltroOrigen = detCampoValorFiltro.getDetCodigo();
								
				RegistroCargue campoValorFiltro = serviceDao.getRegistroCargueDao().tomarRegistrosCarguePorFormatoEntidadCorteDetReg(formatoSeleccionado,
						entidadSeleccionadaValor, corteFiltro, idDetalleCampoFiltroOrigen, idReg);
				
				String valorReconciliar = null;
				
				TiposDato tdato = (TiposDato) serviceDao.getGenericCommonDao().read(TiposDato.class,
						campoValorFiltro.getDetallesFormato().getTpdCodigo());
				
				FormatosTiposDato formatosTiposDato = (FormatosTiposDato) serviceDao.getGenericCommonDao().read(FormatosTiposDato.class,
						campoValorFiltro.getDetallesFormato().getFtdCodigo());
				
				String tipoDato = tdato.getTpdMapeo();
				String operador = a.getOperadores().getOpeDescripcion();
				
				if (null != a.getAltValorConstante() && !"".equals(a.getAltValorConstante())) {
					
					valorReconciliar = a.getAltValorConstante();	
					alert = new AlertasModel(a, null, null, null, null);
					
				} else {
					
					Formatos formatoDest = (Formatos) serviceDao.getGenericCommonDao().read(Formatos.class, a.getForCodigoD());
					CortesFormato corteDest = (CortesFormato) serviceDao.getGenericCommonDao().read(CortesFormato.class, a.getFcrCodigoD());
					DetallesFormato detCampoValorFiltroDest = (DetallesFormato) serviceDao.getGenericCommonDao().read(DetallesFormato.class,
							a.getDetCodigoD());
					
  				    DetallesFormato campoPosicionFiltroDest = (DetallesFormato) serviceDao.getGenericCommonDao().read(DetallesFormato.class, a.getDetCodigoValorD());
					Integer detPosIdDest = campoPosicionFiltroDest.getDetCodigo();
					
					alert = new AlertasModel(a, formatoDest, corteDest, detCampoValorFiltroDest, campoPosicionFiltroDest);
					
					String valorDest = a.getAltFiltroD();
					
					Integer idFormatoDest = a.getForCodigoD();
					Integer idCorteDest = a.getFcrCodigoD();
					
					List<RegistroCargue> posFiltroDest = serviceDao.getRegistroCargueDao().tomarRegistrosCarguePorFormatoEntidadCorteDetVal(idFormatoDest,
							entidadSeleccionadaValor, idCorteDest, detPosIdDest, valorDest);
					
					if (posFiltroDest == null || posFiltroDest.size() == 0) {
						
						throw new Exception("Para ejecutar una alerta compuesta por formatos, se deben cargar antes los formatos involucrados");
					}
					
					int idRegDest = posFiltroDest.get(0).getCrgNoRegistro();	
										
					Integer idDetalleCampoFiltroDest = detCampoValorFiltroDest.getDetCodigo();
									
					RegistroCargue campoValorFiltroDest = serviceDao.getRegistroCargueDao().tomarRegistrosCarguePorFormatoEntidadCorteDetReg(idFormatoDest,
							entidadSeleccionadaValor, idCorteDest, idDetalleCampoFiltroDest, idRegDest);
					
					valorReconciliar = campoValorFiltroDest.getCrgValor();
				}
				
				boolean esValido = false;
				
				if ("N".equals(tipoDato) || "M".equals(tipoDato)) {
					
					Double valorRec = formatearDouble(valorReconciliar, formatosTiposDato.getFtdFormato());
					Double valReg = formatearDouble(campoValorFiltro.getCrgValor(), formatosTiposDato.getFtdFormato());
					esValido = validar(valReg, valorRec, operador, tipoDato);					
					
				} else if ("C".equals(tipoDato)) {
					
					String valorRec = valorReconciliar;
					String valReg = campoValorFiltro.getCrgValor();
					esValido = validar(valReg, valorRec, operador, tipoDato);
					
				} else if ("F".equals(tipoDato)) {
					
					SimpleDateFormat formato = new SimpleDateFormat(formatosTiposDato.getFtdFormato());
					Date valorRec = formato.parse(valorReconciliar);
					Date valReg = formato.parse(campoValorFiltro.getCrgValor());
					esValido = validar(valReg, valorRec, operador, tipoDato);
				}
				
				alert.setPasaValidacion(esValido);
				alertasFormatoModel.add(alert);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			FacesUtils.addFacesMessage("Se ha presentado un fallo inesperado en la ejecución de alertas, " + " Detalles: " + e.getMessage(),
					FacesMessage.SEVERITY_ERROR);
		}

	}
	
	public boolean validar (Object valor, Object valorReconciliar, String operador, String tipoDato) {
		
		boolean esValido = false;
		
		if ("N".equals(tipoDato) || "M".equals(tipoDato)) {
			
			Double val1 = (Double) valor;
			Double val2 = (Double) valorReconciliar;
			
			if ("=".equals(operador)) {
				
				esValido = (val1.compareTo(val2) == 0) ;
			} else if ("!=".equals(operador)) {
							
				esValido = (val1.compareTo(val2) != 0) ;
			} else if (">".equals(operador)) {
								
				esValido = (val1.compareTo(val2) > 0) ;
			} else if ("<".equals(operador)) {
				
				esValido = (val1.compareTo(val2) < 0) ;
			} else if (">=".equals(operador)) {
				
				esValido = (val1.compareTo(val2) >= 0) ;
			} else if ("<=".equals(operador)) {

				esValido = (val1.compareTo(val2) <= 0) ;
			}
		} else if ("C".equals(tipoDato)) {
			
			String val1 = (String) valor;
			String val2 = (String) valorReconciliar;

			if ("=".equals(operador)) {
				
				esValido = (val1.equals(val2)) ;
			} else if ("!=".equals(operador)) {
				
				esValido = (!val1.equals(val2)) ;
			} 
		} else if ("F".equals(tipoDato)) {
			
			Date val1 = (Date) valor;
			Date val2 = (Date) valorReconciliar;		
			
			if ("=".equals(operador)) {
				
				esValido = (val1.compareTo(val2) == 0) ;
			} else if ("!=".equals(operador)) {
								
				esValido = (val1.compareTo(val2) != 0) ;
			} else if (">".equals(operador)) {
								
				esValido = (val1.compareTo(val2) > 0) ;
			} else if ("<".equals(operador)) {
				
				esValido = (val1.compareTo(val2) < 0) ;
			} else if (">=".equals(operador)) {
								
				esValido = (val1.compareTo(val2) >= 0) ;
			} else if ("<=".equals(operador)) {
				
				esValido = (val1.compareTo(val2) <= 0) ;
			}
		}
		
		return esValido;
	}
	
	/**
	 * Mtodo que permite foratear los datos de tipo double
	 * 
	 * @param valor
	 * @return
	 * @throws ExcepcionDatos
	 */
	private Double formatearDouble(String valor, String formato) throws Exception {
		Double valorFinal = null;
		try {

			DecimalFormat decimalFormat = new DecimalFormat(formato);
			valorFinal = decimalFormat.parse(valor).doubleValue();

		} catch (Exception e) {
			throw new Exception("Formato de valor inválido en Formatear Double " +
					e.getMessage());
		}
		return valorFinal;
	}
	
	public class AlertasModel implements Serializable{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 2191968596177544832L;

		public Integer codigoAlerta;
		
		public String corteOrigen;
		
		public String formatoOrigen;
		
		public String posicionCampoOrigen;
		
		public String campoValorOrigen;
		
		public String valorFiltroOrigen;
		
		public String operador;
		
		public String valorConstate;
		
		public String corteDesitno;
		
		public String formatoDesitno;
		
		public String posicionDesitno;
		
		public String campoValorDesitno;
		
		public String valorFiltroDesitno;
		
		public boolean pasaValidacion;
		
		
		public AlertasModel(Alertas alerta, Formatos formatoDest, CortesFormato corteDest, DetallesFormato detalleDest, DetallesFormato detPoscicion) {
			// TODO Auto-generated constructor stub
			
			this.codigoAlerta = alerta.getAltCodigo();
			corteOrigen = alerta.getCortesFormatoOrigen().getFcrCorte();			
			formatoOrigen = alerta.getFormatoOrigen().getForNombre();			
			posicionCampoOrigen = String.valueOf(alerta.getDetallesFormatoValorOrigen().getDetNombre());			
			campoValorOrigen = alerta.getDetallesFormatoOrigen().getDetNombre();			
			valorFiltroOrigen = alerta.getAltFiltroO();			
			operador = alerta.getOperadores().getOpeDescripcion();			
			valorConstate = (alerta.getAltValorConstante() != null) ? alerta.getAltValorConstante() : "N/A";			
			
			if (alerta.getForCodigoD() != null && !(alerta.getForCodigoD().compareTo(0) == 0)) {
				
				corteDesitno = corteDest.getFcrCorte();		
				formatoDesitno = formatoDest.getForNombre();		
				posicionDesitno = detPoscicion.getDetNombre();			
				campoValorDesitno = detalleDest.getDetNombre();			
				valorFiltroDesitno = String.valueOf(alerta.getAltFiltroD());				
				
			} else {
				
				corteDesitno = "N/A";		
				formatoDesitno = "N/A";			
				posicionDesitno = "N/A";			
				campoValorDesitno = "N/A";			
				valorFiltroDesitno = "N/A";
			}
		}
		
		

		/**
		 * @return the codigoAlerta
		 */
		public Integer getCodigoAlerta() {
			return codigoAlerta;
		}



		/**
		 * @param codigoAlerta the codigoAlerta to set
		 */
		public void setCodigoAlerta(Integer codigoAlerta) {
			this.codigoAlerta = codigoAlerta;
		}



		/**
		 * @return the corteOrigen
		 */
		public String getCorteOrigen() {
			return corteOrigen;
		}

		/**
		 * @param corteOrigen the corteOrigen to set
		 */
		public void setCorteOrigen(String corteOrigen) {
			this.corteOrigen = corteOrigen;
		}

		/**
		 * @return the formatoOrigen
		 */
		public String getFormatoOrigen() {
			return formatoOrigen;
		}

		/**
		 * @param formatoOrigen the formatoOrigen to set
		 */
		public void setFormatoOrigen(String formatoOrigen) {
			this.formatoOrigen = formatoOrigen;
		}

		/**
		 * @return the posicionCampoOrigen
		 */
		public String getPosicionCampoOrigen() {
			return posicionCampoOrigen;
		}

		/**
		 * @param posicionCampoOrigen the posicionCampoOrigen to set
		 */
		public void setPosicionCampoOrigen(String posicionCampoOrigen) {
			this.posicionCampoOrigen = posicionCampoOrigen;
		}

		/**
		 * @return the campoValorOrigen
		 */
		public String getCampoValorOrigen() {
			return campoValorOrigen;
		}

		/**
		 * @param campoValorOrigen the campoValorOrigen to set
		 */
		public void setCampoValorOrigen(String campoValorOrigen) {
			this.campoValorOrigen = campoValorOrigen;
		}

		/**
		 * @return the valorFiltroOrigen
		 */
		public String getValorFiltroOrigen() {
			return valorFiltroOrigen;
		}

		/**
		 * @param valorFiltroOrigen the valorFiltroOrigen to set
		 */
		public void setValorFiltroOrigen(String valorFiltroOrigen) {
			this.valorFiltroOrigen = valorFiltroOrigen;
		}

		/**
		 * @return the operador
		 */
		public String getOperador() {
			return operador;
		}

		/**
		 * @param operador the operador to set
		 */
		public void setOperador(String operador) {
			this.operador = operador;
		}

		/**
		 * @return the valorConstate
		 */
		public String getValorConstate() {
			return valorConstate;
		}

		/**
		 * @param valorConstate the valorConstate to set
		 */
		public void setValorConstate(String valorConstate) {
			this.valorConstate = valorConstate;
		}

		/**
		 * @return the corteDesitno
		 */
		public String getCorteDesitno() {
			return corteDesitno;
		}

		/**
		 * @param corteDesitno the corteDesitno to set
		 */
		public void setCorteDesitno(String corteDesitno) {
			this.corteDesitno = corteDesitno;
		}

		/**
		 * @return the formatoDesitno
		 */
		public String getFormatoDesitno() {
			return formatoDesitno;
		}

		/**
		 * @param formatoDesitno the formatoDesitno to set
		 */
		public void setFormatoDesitno(String formatoDesitno) {
			this.formatoDesitno = formatoDesitno;
		}

		/**
		 * @return the posicionDesitno
		 */
		public String getPosicionDesitno() {
			return posicionDesitno;
		}

		/**
		 * @param posicionDesitno the posicionDesitno to set
		 */
		public void setPosicionDesitno(String posicionDesitno) {
			this.posicionDesitno = posicionDesitno;
		}

		/**
		 * @return the campoValorDesitno
		 */
		public String getCampoValorDesitno() {
			return campoValorDesitno;
		}

		/**
		 * @param campoValorDesitno the campoValorDesitno to set
		 */
		public void setCampoValorDesitno(String campoValorDesitno) {
			this.campoValorDesitno = campoValorDesitno;
		}

		/**
		 * @return the valorFiltroDesitno
		 */
		public String getValorFiltroDesitno() {
			return valorFiltroDesitno;
		}

		/**
		 * @param valorFiltroDesitno the valorFiltroDesitno to set
		 */
		public void setValorFiltroDesitno(String valorFiltroDesitno) {
			this.valorFiltroDesitno = valorFiltroDesitno;
		}



		/**
		 * @return the pasaValidacion
		 */
		public boolean isPasaValidacion() {
			return pasaValidacion;
		}



		/**
		 * @param pasaValidacion the pasaValidacion to set
		 */
		public void setPasaValidacion(boolean pasaValidacion) {
			this.pasaValidacion = pasaValidacion;
		}
		
		
		
	}
	

}
