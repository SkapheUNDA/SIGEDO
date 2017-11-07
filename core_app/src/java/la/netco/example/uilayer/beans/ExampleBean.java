package la.netco.example.uilayer.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import la.netco.core.businesslogic.services.ServiceDao;
import la.netco.core.uilayer.beans.utils.FacesUtils;
import la.netco.core.uilayer.commons.BaseBean;
import la.netco.core.uilayer.commons.GenericDataModel;
import la.netco.example.persistence.dto.Example;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.primefaces.event.FileUploadEvent;

import la.netco.core.uilayer.beans.Constants;

@ManagedBean(name = "exampleBean")
@ViewScoped
public class ExampleBean extends BaseBean implements Serializable{

	private static final long serialVersionUID = -2566848064546226065L;

	private String nombre;
	

	@ManagedProperty(value = "#{serviceDao}")
	transient private ServiceDao serviceDao;
	
	private  ExampleDataModel exampleDataModel;
	
	private Example nuevoRegistro;
	
	private Integer idRegSeleccionado;
	
	private Example registroSelecionado;
	
	
	private Integer valorPrueba;
	
	
	public ExampleBean(){
		
	}
	
	@PostConstruct
	public void init(){
		nuevoRegistro = new Example();
		cargaFiltrosDataModel();
	}
	
	public String pruebaFlash(){
		valorPrueba = 25;
		
		FacesUtils.flashScope(FacesContext.getCurrentInstance()).put("bean", this);
		
		
		
		return "actualizar";
	}
	
	
	public void handleFileUpload(FileUploadEvent event) {
		  	System.out.println("Succesful" + event.getFile().getFileName() + " is uploaded.");
			//FacesMessage msg = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
			//FacesContext.getCurrentInstance().addMessage(null, msg);
	}
	  
	
	private static final class ExampleDataModel extends
			GenericDataModel<Example, Integer> {
		private static final long serialVersionUID = 1L;

		private ExampleDataModel() {
			super(Example.class);
			setOrderBy(Order.asc("nombre"));
		}

		@Override
		protected Object getId(Example t) {
			return t.getId();
		}
	}

	public void cargarObjeto(){	
		
		try {
				
			System.out.println("  valorPrueba >>>>>>>>>>>> " + valorPrueba);
			
			if(idRegSeleccionado == null){
				 FacesContext facesContext = FacesContext.getCurrentInstance();
			     String idRegSeleccionado = facesContext.getExternalContext().getRequestParameterMap().get("idRegSeleccionado");
			   
			     if(idRegSeleccionado != null) this.idRegSeleccionado = Integer.parseInt(idRegSeleccionado);
			     
			}
				
			if((registroSelecionado == null || registroSelecionado.getId() == null)&&  (idRegSeleccionado != null && !idRegSeleccionado.equals(""))){
				registroSelecionado =(Example) serviceDao.getGenericCommonDao().read(Example.class, idRegSeleccionado);			
			
			}else{
				registroSelecionado = new Example();
			}
		
		} catch (Exception e) {
		System.err.println(e.getMessage());
		}	
	}
	

	public String  crearRegistro() {
		String page =null;
		try {

			
			serviceDao.getGenericCommonDao().create(Example.class, nuevoRegistro);
			
			FacesUtils.addFacesMessageFromBundle(Constants.OPERACION_EXITOSA_CREAR_REGISTRO, FacesMessage.SEVERITY_INFO);
			
			page = "transaccionExitosa";
			nuevoRegistro = new Example();
		} catch (Exception e) {
			FacesUtils.addFacesMessageFromBundle(Constants.ERROR_OPERACION, FacesMessage.SEVERITY_ERROR);
			e.printStackTrace();
		}
		
		return page;
	}
	
	public String  actualizarRegistro() {
		String page =null;
		try {

			serviceDao.getGenericCommonDao().update(Example.class, registroSelecionado);
			FacesUtils.addFacesMessageFromBundle(Constants.OPERACION_EXITOSA_ACTUALIZAR_REGISTRO, FacesMessage.SEVERITY_INFO);
			
			
			page = "transaccionExitosa";
			registroSelecionado = new Example();
		} catch (Exception e) {
			FacesUtils.addFacesMessageFromBundle(Constants.ERROR_OPERACION, FacesMessage.SEVERITY_ERROR);
			e.printStackTrace();
		}
		
		return page;
	}
	
	public String  eliminarRegistro() {
		String page =null;
		try {
			registroSelecionado =(Example) serviceDao.getGenericCommonDao().read(Example.class, idRegSeleccionado);
			serviceDao.getGenericCommonDao().delete(Example.class, registroSelecionado);
			
			FacesUtils.addFacesMessageFromBundle(Constants.OPERACION_EXITOSA_ELIMINAR_REGISTRO, FacesMessage.SEVERITY_INFO);
			
			page = "transaccionExitosa";
			registroSelecionado = new Example();
		} catch (Exception e) {
			FacesUtils.addFacesMessageFromBundle(Constants.ERROR_OPERACION, FacesMessage.SEVERITY_ERROR);
			e.printStackTrace();
		}
		
		return page;
	}
	
	public String cargaFiltrosDataModel(){
    	List<Criterion> filtros = new ArrayList<Criterion>();
    
    	if(nombre != null  && !nombre.trim().equals("") ){    		
    		Criterion nombre = Restrictions.ilike("tdoNom", "%" + this.nombre + "%");
			filtros.add(nombre);
    	}
    	
    	exampleDataModel = new ExampleDataModel();    	
    	exampleDataModel.setFiltros(filtros);
    	return "";
    }
	
//	public List<SelectItem> getCiudadesItems() {
//		if (ciudadesItems == null) {
//			try {
//				List<Ciudad> allCiudades =serviceDao.getCiudadDao().loadAll(
//						Ciudad.class);
//				ciudadesItems = new ArrayList<SelectItem>();
//				for (Ciudad ciudad : allCiudades) {
//					ciudadesItems.add(new SelectItem(ciudad.getCodigo(), ciudad
//							.getNombre()));
//				}
//
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//
//		}
//
//		return ciudadesItems;
//	}
	
	public ExampleDataModel getExampleDataModel() {
		return exampleDataModel;
	}

	public void setExampleDataModel(ExampleDataModel estidadesDataModel) {
		this.exampleDataModel = estidadesDataModel;
	}

	public Example getNuevoRegistro() {
		return nuevoRegistro;
	}

	public void setNuevoRegistro(Example nuevoRegistro) {
		this.nuevoRegistro = nuevoRegistro;
	}

	public Integer getIdRegSeleccionado() {
		return idRegSeleccionado;
	}

	public void setIdRegSeleccionado(Integer idRegSeleccionado) {
		this.idRegSeleccionado = idRegSeleccionado;
	}

	public Example getRegistroSelecionado() {
		return registroSelecionado;
	}

	public void setRegistroSelecionado(Example registroSelecionado) {
		this.registroSelecionado = registroSelecionado;
	}
	public ServiceDao getServiceDao() {
		return serviceDao;
	}
	public void setServiceDao(ServiceDao serviceDao) {
		this.serviceDao = serviceDao;
	}
	
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Integer getValorPrueba() {
		return valorPrueba;
	}

	public void setValorPrueba(Integer valorPrueba) {
		this.valorPrueba = valorPrueba;
	}
	
}
