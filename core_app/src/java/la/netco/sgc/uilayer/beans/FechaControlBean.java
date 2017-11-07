package la.netco.sgc.uilayer.beans;

import java.io.Serializable;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import la.netco.core.businesslogic.services.ServiceDao;
import la.netco.core.spring.BeansSpringConst;
import la.netco.core.spring.security.CommonsActionsKey;
import la.netco.core.spring.security.CommonsModKeys;
import la.netco.core.spring.security.SecuredAction;
import la.netco.core.uilayer.beans.Constants;
import la.netco.core.uilayer.beans.utils.FacesUtils;
import la.netco.sgc.persistence.dto.CortesFormato;
import la.netco.sgc.persistence.dto.InformeEntidad;
import la.netco.uilayer.beans.PrimeDataModel;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ManagedBean(name = "fechasControlBean")
@ViewScoped
public class FechaControlBean implements Serializable {

	/* Log */
	private static Logger log = LoggerFactory
			.getLogger(InformeEntidadBean.class);

	/* Atributos de nivel Visual Filtros */
	private static final long serialVersionUID = 1L;
	@ManagedProperty(value = BeansSpringConst.BEAN_ID_SERVICE_DAO)
	transient private ServiceDao serviceDao;
	private CortesFomatosDataModel listDatamodel = null;
	CortesFormato cortesFormato;
	CortesFormato cortesFormatoAux;

	private Integer idFechaControlSeleccionado;
	
	/*Atributos a nivel de filtros*/
	private String fcrCorte; 
	private String fcrPeriodo;
	private String ano;
	private Date fcrCorteDate;
	private int anioFcorte;
	private int anioFormulario;
	
	
	
	
	/*Constructor de la clase*/
	public FechaControlBean() {
		cortesFormato  = new CortesFormato();
		cortesFormatoAux = new CortesFormato();
	}


	/**
	 * Carga listado inicial
	 */
	public void cargaListaDataModel() {
		if (listDatamodel == null) {
			listDatamodel = new CortesFomatosDataModel();
			List<Criterion> filtros = new ArrayList<Criterion>();
			Map<String, String> alias = new HashMap<String, String>();
			
			
			filtros.add(Restrictions.isNotNull("fcrCorte"));
			filtros.add(Restrictions.ne("fcrCorte",  "")); 
			
			filtros.add(Restrictions.isNotNull("fcrPeriodo"));
			filtros.add(Restrictions.ne("fcrPeriodo",  ""));
			
			listDatamodel.setAlias(alias);
			listDatamodel.setFiltros(filtros);
		}
		
		
		
	}
	
	/*
	 * Metodos que retonar la regla de navegacion. Tinene la anotacion @SecuredAction que valida si el usuario
	 * registrado tiene permisos sobre la accion
	 *
	 * */
	
	@SecuredAction(keyAction = CommonsActionsKey.UPDATE, keyMod = CommonsModKeys.SE_USU_USU)
	public String agregarNavigation() {		
		return "agregarFechaControl";
	}
	
	@SecuredAction(keyAction = CommonsActionsKey.UPDATE, keyMod = CommonsModKeys.SE_USU_USU)
	public String editarNavigation() {		
		return "actualizarFechaControl";
	}
	
	public String navlistado(){
		return "listadoFechasControl";
	}
	
	/**
	 * Busqueda de Fechas Control, seg�n los atributos dados en pantalla
	 */
	public void buscar(){
		log.info("Ingreso a buscar Fechas Control");
		List<Criterion> filtros = new ArrayList<Criterion>();
		if(fcrCorteDate != null){
		fcrCorte = convertirFechaString(fcrCorteDate);
		}
		if(fcrCorte != null  && !fcrCorte.trim().equals("") ){
    		filtros.add(Restrictions.ilike("fcrCorte", "%" + fcrCorte + "%"));  
    	}
		
		if(fcrPeriodo != null  && !fcrPeriodo.trim().equals("") ){
    		filtros.add(Restrictions.ilike("fcrPeriodo",  "%" + fcrPeriodo + "%"));  
    	}
		
		if(ano != null  && !ano.trim().equals("") ){
    		filtros.add(Restrictions.ilike("ano",  "%" + ano + "%"));  
    	}
		
		filtros.add(Restrictions.isNotNull("fcrCorte"));
		filtros.add(Restrictions.ne("fcrCorte",  "")); 
		
		filtros.add(Restrictions.isNotNull("fcrPeriodo"));
		filtros.add(Restrictions.ne("fcrPeriodo",  ""));
		
		filtros.add(Restrictions.isNotNull("ano"));
		filtros.add(Restrictions.ne("ano",  ""));
		
		
		Map<String, String> alias = new HashMap<String, String>();
		listDatamodel.setAlias(alias);
		listDatamodel.setFiltros(filtros);
	}
	
	/**
	 * Crear Fecha Control
	 */
	public String createOrReplace() {
		String page = null;
		try {
			log.info("Ingreso a crear Fecha Control"
					+ cortesFormato.getFcrPeriodo());

			if (fcrCorteDate != null) {
				if(cortesFormato.getFcrActivo()){
				List<Map<String, Object>> lectura = null;
				String query = " select * from SGC.Cortes_Formato cf " +
						" where cf.FCR_Activo = 1";
				lectura = serviceDao.getSpringGenericDao().executeQuery(query);
				if (lectura.size()>0){
				 if(lectura != null){
						FacesUtils.addFacesMessageFromBundle(
								Constants.FECHA_CONTROL_PERIODO_YA_ACTIVO,
								FacesMessage.SEVERITY_FATAL);
						return page;
				 }
				}
				}
				
				fcrCorte = convertirFechaString(fcrCorteDate);
				Map<Integer, Object> params = new HashMap<Integer, Object>();
				params.put(0, fcrCorte);
				params.put(1, cortesFormato.getFcrPeriodo().trim());
				params.put(2, cortesFormato.getAno().trim());
				cortesFormatoAux = new CortesFormato();
				cortesFormatoAux = (CortesFormato) serviceDao
						.getGenericCommonDao().executeUniqueResult(
								CortesFormato.NAMED_QUERY_GET_BY_DUPLICADO,
								params);

				if (cortesFormatoAux != null) {
					FacesUtils.addFacesMessageFromBundle(
							Constants.FECHA_CONTROL_EXISTE,
							FacesMessage.SEVERITY_FATAL);
					return page;

				} else {
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(fcrCorteDate);
					System.out.println(calendar.get(Calendar.YEAR));
					anioFcorte = calendar.get(Calendar.YEAR);
					anioFormulario = Integer.parseInt(cortesFormato.getAno());
					cortesFormato.setFcrCorte(fcrCorte);
				}
			}
			if (anioFcorte == anioFormulario) {
				serviceDao.getGenericCommonDao().create(CortesFormato.class,
						cortesFormato);
				FacesUtils.addFacesMessageFromBundle(
						Constants.OPERACION_EXITOSA_CREAR_REGISTRO,
						FacesMessage.SEVERITY_INFO);
				page = "transaccionExitosaFechasControl";

			} else {
				FacesUtils.addFacesMessageFromBundle(
						Constants.FECHA_CONTROL_ANIO_DIFERENTE,
						FacesMessage.SEVERITY_FATAL);
				return page;

			}

		} catch (Exception exception) {
			FacesUtils.addFacesMessageFromBundle(Constants.ERROR_OPERACION,
					FacesMessage.SEVERITY_FATAL);
			log.error(exception.getMessage());
		}
		return page;
	}
	
	/**
	 * Actualizar Fecha Control
	 */
	public String actualizar() {
		String page = null;
		try {
			log.info("Ingreso a crear Fecha Control"
					+ cortesFormato.getFcrPeriodo());
			/*if(fcrCorteDate != null){
				fcrCorte = convertirFechaString(fcrCorteDate);
				cortesFormato.setFcrCorte(fcrCorte);
			}
			serviceDao.getGenericCommonDao().update(CortesFormato.class,
					cortesFormato);
			FacesUtils.addFacesMessageFromBundle(
					Constants.OPERACION_EXITOSA_ACTUALIZAR_REGISTRO,
					FacesMessage.SEVERITY_INFO);
			page = "transaccionExitosaFechasControl";*/
			if (fcrCorteDate != null) {
				if (cortesFormato.getFcrActivo()) {
					List<Map<String, Object>> lectura = null;
					String query = " select * from SGC.Cortes_Formato cf "
							+ " where cf.FCR_Activo = 1";
					lectura = serviceDao.getSpringGenericDao().executeQuery(
							query);
					if (lectura != null) {
						FacesUtils.addFacesMessageFromBundle(
								Constants.FECHA_CONTROL_PERIODO_YA_ACTIVO,
								FacesMessage.SEVERITY_FATAL);
						return page;
					}
				}

				fcrCorte = convertirFechaString(fcrCorteDate);
				Map<Integer, Object> params = new HashMap<Integer, Object>();
				params.put(0, fcrCorte);
				params.put(1, cortesFormato.getFcrPeriodo().trim());
				params.put(2, cortesFormato.getAno().trim());
				cortesFormatoAux = new CortesFormato();
				cortesFormatoAux = (CortesFormato) serviceDao
						.getGenericCommonDao().executeUniqueResult(
								CortesFormato.NAMED_QUERY_GET_BY_DUPLICADO,
								params);

				if (cortesFormatoAux != null) {
					FacesUtils.addFacesMessageFromBundle(
							Constants.FECHA_CONTROL_EXISTE,
							FacesMessage.SEVERITY_FATAL);
					return page;

				} else {
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(fcrCorteDate);
					System.out.println(calendar.get(Calendar.YEAR));
					anioFcorte = calendar.get(Calendar.YEAR);
					anioFormulario = Integer.parseInt(cortesFormato.getAno());
					cortesFormato.setFcrCorte(fcrCorte);
				}
			}
			if (anioFcorte == anioFormulario) {
				serviceDao.getGenericCommonDao().create(CortesFormato.class,
						cortesFormato);
				FacesUtils.addFacesMessageFromBundle(
						Constants.OPERACION_EXITOSA_ACTUALIZAR_REGISTRO,
						FacesMessage.SEVERITY_INFO);
				page = "transaccionExitosaFechasControl";
  
			} else {
				FacesUtils.addFacesMessageFromBundle(
						Constants.FECHA_CONTROL_ANIO_DIFERENTE,
						FacesMessage.SEVERITY_FATAL);
				return page;

			}
			
		}
		catch(Exception exception) {
			FacesUtils.addFacesMessageFromBundle(Constants.ERROR_OPERACION,
					FacesMessage.SEVERITY_FATAL);
			log.error(exception.getMessage());
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
			if(idFechaControlSeleccionado == null){
			     String idfechaControl = ctx.getExternalContext().getRequestParameterMap().get("idFechaControlSeleccionada");			   
			     if(idfechaControl != null) this.idFechaControlSeleccionado = Integer.parseInt(idfechaControl);
			}
				
			if((cortesFormato == null || cortesFormato.getFcrCorte() == null) &&  (idFechaControlSeleccionado != null && !idFechaControlSeleccionado.equals(""))){
		
				cortesFormato = (CortesFormato) serviceDao.getGenericCommonDao().read(CortesFormato.class, idFechaControlSeleccionado);
				if(cortesFormato != null){
				fcrCorteDate =deStringADate(cortesFormato.getFcrCorte()); 
				}
						
			}else{
				cortesFormato = new CortesFormato();
			}
		
		} catch (Exception e) {
		
			ctx.addMessage(null, new FacesMessage("Se ha presentado un error. Detalles: " + e.getMessage()));
		}
		
	}
	

	public static final class CortesFomatosDataModel extends
			PrimeDataModel<CortesFormato, Integer> {
		private static final long serialVersionUID = 1L;

		private CortesFomatosDataModel() {
			super(CortesFormato.class);
			setOrderBy(Order.asc("fcrCodigo"));
		}

		@Override
		protected Object getId(CortesFormato t) {
			return t.getClass();
		}
	}

	/**
	 * Convertir Date a String
	 * @param date
	 * @return
	 */
	public String convertirFechaString(Date date) {
		Format formatter = new SimpleDateFormat("dd/MM/yyyy");
		return formatter.format(date);
	}
	
	/**
	 * Permite convertir un String en fecha (Date). 
	 * @param fecha (String) la fecha a la cual deseo convertir
	 * @return Date la fecha en formato Date
	 */
	 
	public Date deStringADate(String fecha){
	        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
	        String strFecha = fecha;
	        Date fechaDate = null;
	        try {
	            fechaDate = formato.parse(strFecha);
	                        System.out.println(fechaDate.toString());
	            return fechaDate;
	        } catch (ParseException ex) {
	            ex.printStackTrace();
	            return fechaDate;
	        }
	      
	}
	
	public ServiceDao getServiceDao() {
		return serviceDao;
	}


	public void setServiceDao(ServiceDao serviceDao) {
		this.serviceDao = serviceDao;
	}


	public CortesFomatosDataModel getListDatamodel() {
		cargaListaDataModel();
		return listDatamodel;
	}


	public void setListDatamodel(CortesFomatosDataModel listDatamodel) {
		this.listDatamodel = listDatamodel;
	}


	public CortesFormato getCortesFormato() {
		return cortesFormato;
	}


	public void setCortesFormato(CortesFormato cortesFormato) {
		this.cortesFormato = cortesFormato;
	}


	public String getFcrCorte() {
		return fcrCorte;
	}


	public void setFcrCorte(String fcrCorte) {
		this.fcrCorte = fcrCorte;
	}


	public String getFcrPeriodo() {
		return fcrPeriodo;
	}


	public void setFcrPeriodo(String fcrPeriodo) {
		this.fcrPeriodo = fcrPeriodo;
	}


	public String getAno() {
		return ano;
	}


	public void setAno(String ano) {
		this.ano = ano;
	}


	public Date getFcrCorteDate() {
		return fcrCorteDate;
	}


	public void setFcrCorteDate(Date fcrCorteDate) {
		this.fcrCorteDate = fcrCorteDate;
	}


	public Integer getIdFechaControlSeleccionado() {
		return idFechaControlSeleccionado;
	}


	public void setIdFechaControlSeleccionado(Integer idFechaControlSeleccionado) {
		this.idFechaControlSeleccionado = idFechaControlSeleccionado;
	}
	
	
	

}
