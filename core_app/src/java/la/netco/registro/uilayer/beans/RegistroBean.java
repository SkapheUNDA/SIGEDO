package la.netco.registro.uilayer.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import la.netco.core.businesslogic.services.ServiceDao;
import la.netco.core.spring.BeansSpringConst;
import la.netco.core.uilayer.beans.Constants;
import la.netco.core.uilayer.beans.utils.FacesUtils;
import la.netco.persistencia.dto.commons.Actividad;
import la.netco.persistencia.dto.commons.Elemento;
import la.netco.persistencia.dto.commons.Elementoregistro;
import la.netco.persistencia.dto.commons.ElementoregistroId;
import la.netco.persistencia.dto.commons.Entidad;
import la.netco.persistencia.dto.commons.Obras;
import la.netco.persistencia.dto.commons.Paises;
import la.netco.persistencia.dto.commons.Persona;
import la.netco.persistencia.dto.commons.Propiedad;
import la.netco.persistencia.dto.commons.Registro;
import la.netco.persistencia.dto.commons.Registropersona;
import la.netco.persistencia.dto.commons.Tiporegistropersona;
import la.netco.persistencia.dto.commons.Tiposcontrato;
import la.netco.persistencia.dto.commons.Tipospersona;
import la.netco.persistencia.dto.commons.Titulos;



@ManagedBean
@ViewScoped
public class RegistroBean implements Serializable {


	private static final long serialVersionUID = 1L;
	private Registro registroSeleccionado;
	
	@ManagedProperty(value = BeansSpringConst.BEAN_ID_SERVICE_DAO)
	transient private ServiceDao serviceDao;
	
	private List<SelectItem> paisesItems;

	private List<SelectItem> tipoContratosItems;
	private Integer idRegSeleccionado;
	
	private boolean registroObraInfoAdicional;
	
	private List<Elementoregistro> propiedadesSeleccionadas = new ArrayList<Elementoregistro>();
	
	private Elementoregistro elementoregistro;
	
	private List<SelectItem> propiedadesItems;
	private Integer idPropiedadSeleccionado;
	
	private List<SelectItem> elementosItems;
	private Integer idElementoSeleccionado;
	
	private List<Registropersona> registropersonas = new ArrayList<Registropersona>();
	
	private List<SelectItem> tiposPersonasRegistroItems;
	private Integer idTipoPersonsaRegistro;	
	
	private List<SelectItem> tiposPersonaItems;
	private Short idTiposPersonaSeleccionado;
	
	private List<SelectItem> actividadesItems;
	private Short idActividadSeleccionado;
	
	private Persona personaNaturalSeleccionada;	
	private Entidad personaJuridicaSeleccionada;
	
	private String tituloAnterior;
	private List<Titulos> titulosAnteriores = new ArrayList<Titulos>();
	
	private Registro registroRelacionado;
	
	private Obras obraRegistro;
	
	private List<Obras> obrasRelacionadas = new ArrayList<Obras>();
	
	private Integer tipoObra;
	
	//private List<Firma> fechasFirmas = new ArrayList<Obras>();
	
	public  RegistroBean(){
		elementoregistro = new Elementoregistro();
		obraRegistro= new Obras();
	}
	
	
	public String actualizarRegistro(){
		String page =	null;
		try {
			
			//ELIMINA RELACIONES ACTUALES
			for (Elementoregistro elemento : registroSeleccionado.getElementoregistros()) {
				serviceDao.getGenericCommonDao().delete(Elementoregistro.class, elemento);
			}

			for (Registropersona persona : registroSeleccionado.getRegistropersonas()) {
				serviceDao.getGenericCommonDao().delete(Registropersona.class, persona);
			}
			
			for (Titulos titulo : registroSeleccionado.getTituloses()) {
				serviceDao.getGenericCommonDao().delete(Titulos.class, titulo);
			}
			
			for (Obras obra : registroSeleccionado.getObrasesForObrReg()) {
				serviceDao.getGenericCommonDao().delete(Obras.class, obra);
			}
			
			//AGREGA NUEVAS RELACIONES
			for (Elementoregistro elemento : propiedadesSeleccionadas) {
				serviceDao.getGenericCommonDao().create(Elementoregistro.class, elemento);
			}

			for (Registropersona persona : registropersonas) {
				serviceDao.getGenericCommonDao().create(Registropersona.class, persona);
			}
			
			for (Titulos titulo : titulosAnteriores) {
				serviceDao.getGenericCommonDao().create(Titulos.class, titulo);
			}
			
			for (Obras obra : obrasRelacionadas) {
				serviceDao.getGenericCommonDao().create(Obras.class, obra);
			}
			
			
			serviceDao.getGenericCommonDao().update(Registro.class, registroSeleccionado);
			
			FacesUtils.addFacesMessageFromBundle(Constants.OPERACION_EXITOSA_ACTUALIZAR_REGISTRO,	FacesMessage.SEVERITY_INFO);
			page = "transaccionExitosa";
			
		} catch (Exception e) {
			e.printStackTrace();
			FacesUtils.addFacesMessageFromBundle(Constants.ERROR_OPERACION,	FacesMessage.SEVERITY_FATAL);
		}
		return page;
		
	}
	
	public void cargarRegistroDetalle(){	
		
		try {						
			if(idRegSeleccionado == null){
				 FacesContext facesContext = FacesContext.getCurrentInstance();
			     String idRegSeleccionado = facesContext.getExternalContext().getRequestParameterMap().get("idRegSeleccionado");			   
			     if(idRegSeleccionado != null) this.idRegSeleccionado = Integer.parseInt(idRegSeleccionado);		
			}
			
			if((registroSeleccionado == null || registroSeleccionado.getRegId() == null) 	&&  (idRegSeleccionado != null)){
				registroSeleccionado =(Registro) serviceDao.getGenericCommonDao().read(Registro.class, idRegSeleccionado);

				for (Elementoregistro elemento : registroSeleccionado.getElementoregistros()) {
					propiedadesSeleccionadas.add(elemento);
				}

				for (Registropersona persona : registroSeleccionado.getRegistropersonas()) {
					registropersonas.add(persona);
				}
				
				for (Titulos titulo : registroSeleccionado.getTituloses()) {
					titulosAnteriores.add(titulo);
				}
				
				for (Obras obra : registroSeleccionado.getObrasesForObrReg()) {
					obrasRelacionadas.add(obra);
				}
				
				
				if((registroSeleccionado.getClaseregistro().getCreLdes() != null && registroSeleccionado.getClaseregistro().getCreLdes() ) || 
						(registroSeleccionado.getClaseregistro().getCreLpor() != null && registroSeleccionado.getClaseregistro().getCreLpor() )	|| 
						(registroSeleccionado.getClaseregistro().getCreLdob() != null && registroSeleccionado.getClaseregistro().getCreLdob()  ) || 
						(registroSeleccionado.getClaseregistro().getCreLned() != null && registroSeleccionado.getClaseregistro().getCreLned() ) || 
						(registroSeleccionado.getClaseregistro().getCreLnpg() != null && registroSeleccionado.getClaseregistro().getCreLnpg()) ||
						(registroSeleccionado.getClaseregistro().getCreLtir() != null && registroSeleccionado.getClaseregistro().getCreLtir()) || 
						(registroSeleccionado.getClaseregistro().getCreLfte() != null && registroSeleccionado.getClaseregistro().getCreLfte()) || 
						(registroSeleccionado.getClaseregistro().getCreLfpp() != null && registroSeleccionado.getClaseregistro().getCreLfpp()) || 
						(registroSeleccionado.getClaseregistro().getCreLnre() != null && registroSeleccionado.getClaseregistro().getCreLnre()) || 
						(registroSeleccionado.getClaseregistro().getCreLfre() != null && registroSeleccionado.getClaseregistro().getCreLfre()) || 
						(registroSeleccionado.getClaseregistro().getCreLisbn() != null && registroSeleccionado.getClaseregistro().getCreLisbn())){
					
					registroObraInfoAdicional = true;
				}
			}			
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	
	public void agregarElemento(){
		ElementoregistroId elementoId = new ElementoregistroId(registroSeleccionado.getRegId(), idElementoSeleccionado);
		
		elementoregistro = new Elementoregistro();
		elementoregistro.setId(elementoId);
		
		elementoregistro.setElemento((Elemento) serviceDao.getGenericCommonDao().read(Elemento.class, idElementoSeleccionado));
		
		if(!propiedadesSeleccionadas.contains(elementoregistro)){
			propiedadesSeleccionadas.add(elementoregistro);
			elementoregistro = new Elementoregistro();
			idElementoSeleccionado = null;
			idPropiedadSeleccionado = null;
		}
	}
	
	
	public void agregarPersona(){
		
		Registropersona registropersona = new Registropersona();		
		registropersona.setTiporegistropersona((Tiporegistropersona) serviceDao.getGenericCommonDao().read(Tiporegistropersona.class, idTipoPersonsaRegistro.byteValue()));
		registropersona.setActividad((Actividad) serviceDao.getGenericCommonDao().read(Actividad.class, idActividadSeleccionado));
		registropersona.setRegistro(registroSeleccionado);
		registropersona.setTipospersona((Tipospersona) serviceDao.getGenericCommonDao().read(Tipospersona.class, idTiposPersonaSeleccionado));
		registropersona.setEntidad(personaJuridicaSeleccionada);
		registropersona.setPersona(personaNaturalSeleccionada);
		
		registropersonas.add(registropersona);
		
		idTipoPersonsaRegistro = null;
		idActividadSeleccionado = null;
		idTiposPersonaSeleccionado = null;
		personaJuridicaSeleccionada = null;
		personaNaturalSeleccionada =null;

	}
	
	
	public void agregarTitulo(){
		
		Titulos titulo = new Titulos();
		titulo.setTitNom(tituloAnterior);
		titulo.setRegistro(registroSeleccionado);		
		titulosAnteriores.add(titulo);		
		tituloAnterior = null;

	}
	
	public void agregarObra(){
		
		obraRegistro.setRegistroByObrReg(registroSeleccionado);
		if(registroRelacionado != null){
			obraRegistro.setRegistroByObrRgt(registroRelacionado);
		}
		obrasRelacionadas.add(obraRegistro);		
		registroRelacionado = null;
		obraRegistro = new Obras();

	}
	
	
	
	
	public void quitarPropiedad(Elementoregistro elemento){
		if(propiedadesSeleccionadas.contains(elemento)){
			propiedadesSeleccionadas.remove(elemento);
		}
	}
	
	public void quitarPersona(int index){
		registropersonas.remove(index);
	}	

	public void quitarTitulo(int index){
		titulosAnteriores.remove(index);
	}	
	
	public void quitarObra(int index){
		obrasRelacionadas.remove(index);
	}	

	
	@SuppressWarnings("unchecked")
	public void cargaPaisesItems() {
		if (paisesItems == null) {
			List<Paises> allData = (List<Paises>) serviceDao.getGenericCommonDao().executeFind(Paises.NAMED_QUERY_ALL);
			paisesItems = new ArrayList<SelectItem>();
			for (Paises dato : allData) {
				paisesItems.add(new SelectItem(dato.getPaiId(),	dato.getPaiNom()));
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public void cargaTiposcontratosItems() {
		if (tipoContratosItems == null) {
			Map<Integer, Object> params = new HashMap<Integer, Object>();
			params.put(0, registroSeleccionado.getClaseregistro().getCreId());		
			
			List<Tiposcontrato> allData = (List<Tiposcontrato>) serviceDao.getGenericCommonDao().executeFind(Tiposcontrato.class, params, Tiposcontrato.NAMED_QUERY_GET_BY_CLASE);
			tipoContratosItems = new ArrayList<SelectItem>();
			for (Tiposcontrato dato : allData) {
				tipoContratosItems.add(new SelectItem(dato.getTcoId(),	dato.getTcoNom()));
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public void cargaPropiedadesItems() {
		if (propiedadesItems == null) {
			Map<Integer, Object> params = new HashMap<Integer, Object>();
			params.put(0, registroSeleccionado.getClaseregistro().getCreId());		
			
			List<Propiedad> allData = (List<Propiedad>) serviceDao.getGenericCommonDao().executeFind(Propiedad.class, params, Propiedad.NAMED_QUERY_GET_BY_CLASE);
			propiedadesItems = new ArrayList<SelectItem>();
			for (Propiedad dato : allData) {
				propiedadesItems.add(new SelectItem(dato.getProId(),	dato.getProNom()));
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public void cargaElementosItems() {
		if(idPropiedadSeleccionado != null){
		
			
			Map<Integer, Object> params = new HashMap<Integer, Object>();
			params.put(0, idPropiedadSeleccionado);

			List<Elemento> allData = (List<Elemento>) serviceDao.getGenericCommonDao().executeFind(Elemento.class, params, Elemento.NAMED_QUERY_GET_BY_PROP);
			
		
			elementosItems = new ArrayList<SelectItem>();
			 
			for (Elemento dato : allData) {
				elementosItems.add(new SelectItem(dato.getEleId(), dato.getEleNom()));
			}
		}else if(elementosItems == null){
			elementosItems = new ArrayList<SelectItem>();
		}
	}
	
	@SuppressWarnings("unchecked")
	public void cargaTipoPersonasRegistroItems() {
		if (tiposPersonasRegistroItems == null) {
			Map<Integer, Object> params = new HashMap<Integer, Object>();
			params.put(0, registroSeleccionado.getClaseregistro().getCreId());		
			
			List<Tiporegistropersona> allData = (List<Tiporegistropersona>) serviceDao.getGenericCommonDao().executeFind(Tiporegistropersona.class, params, Tiporegistropersona.NAMED_QUERY_GET_BY_CLASE);
			tiposPersonasRegistroItems = new ArrayList<SelectItem>();
			for (Tiporegistropersona dato : allData) {
				tiposPersonasRegistroItems.add(new SelectItem(dato.getTrpId(),	dato.getTrpNom()));
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public void cargaTipoPersonaItems() {
		if (tiposPersonaItems == null) {
			List<Tipospersona> tipospersonaAuxi = (List<Tipospersona>) serviceDao.getGenericCommonDao().executeFind(Tipospersona.NAMED_QUERY_ALL_TP);

			tiposPersonaItems = new ArrayList<SelectItem>();
			for (Tipospersona tipospersona : tipospersonaAuxi) {
				tiposPersonaItems.add(new SelectItem(tipospersona.getTpeId(),
						tipospersona.getTpeNom()));
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public void cargActividadItems() {
		try {			
	
			if(idTipoPersonsaRegistro != null){
			
				
				Map<Integer, Object> params = new HashMap<Integer, Object>();
				params.put(0, registroSeleccionado.getClaseregistro().getCreId());
				params.put(1, idTipoPersonsaRegistro.byteValue());
	
				List<Actividad> allData = (List<Actividad>) serviceDao.getGenericCommonDao().executeFind(Actividad.class, params, Actividad.NAMED_QUERY_GET_BY_CLASE_TIPO);
				
			
				actividadesItems = new ArrayList<SelectItem>();
				 
				for (Actividad dato : allData) {
					actividadesItems.add(new SelectItem(dato.getActId(), dato.getActNom()));
				}
			}else if(actividadesItems == null){
				actividadesItems = new ArrayList<SelectItem>();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	public Registro getRegistroSeleccionado() {
		return registroSeleccionado;
	}

	public void setRegistroSeleccionado(Registro registroSeleccionado) {
		this.registroSeleccionado = registroSeleccionado;
	}

	public ServiceDao getServiceDao() {
		return serviceDao;
	}

	public List<SelectItem> getPaisesItems() {
		cargaPaisesItems();
		return paisesItems;
	}

	public void setServiceDao(ServiceDao serviceDao) {
		this.serviceDao = serviceDao;
	}

	public void setPaisesItems(List<SelectItem> paisesItems) {
		this.paisesItems = paisesItems;
	}

	public List<SelectItem> getTipoContratosItems() {
		return tipoContratosItems;
	}

	public void setTipoContratosItems(List<SelectItem> tipoContratosItems) {
		this.tipoContratosItems = tipoContratosItems;
	}

	public Integer getIdRegSeleccionado() {
		return idRegSeleccionado;
	}

	public void setIdRegSeleccionado(Integer idRegSeleccionado) {
		this.idRegSeleccionado = idRegSeleccionado;
	}

	public boolean isRegistroObraInfoAdicional() {
		return registroObraInfoAdicional;
	}

	public void setRegistroObraInfoAdicional(boolean registroObraInfoAdicional) {
		this.registroObraInfoAdicional = registroObraInfoAdicional;
	}


	public List<Elementoregistro> getPropiedadesSeleccionadas() {
		return propiedadesSeleccionadas;
	}

	public void setPropiedadesSeleccionadas(List<Elementoregistro> propiedadesSeleccionadas) {
		this.propiedadesSeleccionadas = propiedadesSeleccionadas;
	}

	public Elementoregistro getElementoregistro() {
		return elementoregistro;
	}

	public void setElementoregistro(Elementoregistro elementoregistro) {
		this.elementoregistro = elementoregistro;
	}

	public List<SelectItem> getPropiedadesItems() {
		 cargaPropiedadesItems();
		return propiedadesItems;
	}

	public Integer getIdPropiedadSeleccionado() {
		return idPropiedadSeleccionado;
	}

	public List<SelectItem> getElementosItems() {
		cargaElementosItems();
		return elementosItems;
	}

	public Integer getIdElementoSeleccionado() {
		return idElementoSeleccionado;
	}

	public void setPropiedadesItems(List<SelectItem> propiedadesItems) {
		this.propiedadesItems = propiedadesItems;
	}

	public void setIdPropiedadSeleccionado(Integer idPropiedadSeleccionado) {
		this.idPropiedadSeleccionado = idPropiedadSeleccionado;
	}

	public void setElementosItems(List<SelectItem> elementosItems) {
		this.elementosItems = elementosItems;
	}

	public void setIdElementoSeleccionado(Integer idElementoSeleccionado) {
		this.idElementoSeleccionado = idElementoSeleccionado;
	}

	public List<Registropersona> getRegistropersonas() {
		return registropersonas;
	}

	public void setRegistropersonas(List<Registropersona> registropersonas) {
		this.registropersonas = registropersonas;
	}

	public List<SelectItem> getTiposPersonasRegistroItems() {
		cargaTipoPersonasRegistroItems() ;
		return tiposPersonasRegistroItems;
	}

	public Integer getIdTipoPersonsaRegistro() {
		return idTipoPersonsaRegistro;
	}

	public void setTiposPersonasRegistroItems(
			List<SelectItem> tiposPersonasRegistroItems) {
		this.tiposPersonasRegistroItems = tiposPersonasRegistroItems;
	}

	public void setIdTipoPersonsaRegistro(Integer idTipoPersonsaRegistro) {
		this.idTipoPersonsaRegistro = idTipoPersonsaRegistro;
	}

	public List<SelectItem> getTiposPersonaItems() {
		cargaTipoPersonaItems();
		return tiposPersonaItems;
	}

	public Short getIdTiposPersonaSeleccionado() {
		return idTiposPersonaSeleccionado;
	}

	public void setTiposPersonaItems(List<SelectItem> tiposPersonaItems) {
		this.tiposPersonaItems = tiposPersonaItems;
	}

	public void setIdTiposPersonaSeleccionado(Short idTiposPersonaSeleccionado) {
		this.idTiposPersonaSeleccionado = idTiposPersonaSeleccionado;
	}

	public List<SelectItem> getActividadesItems() {
		cargActividadItems();
		return actividadesItems;
	}

	public Short getIdActividadSeleccionado() {
		return idActividadSeleccionado;
	}

	public void setActividadesItems(List<SelectItem> actividadesItems) {
		this.actividadesItems = actividadesItems;
	}

	public void setIdActividadSeleccionado(Short idActividadSeleccionado) {
		this.idActividadSeleccionado = idActividadSeleccionado;
	}

	public Persona getPersonaNaturalSeleccionada() {
		return personaNaturalSeleccionada;
	}

	public void setPersonaNaturalSeleccionada(Persona personaNaturalSeleccionada) {
		this.personaNaturalSeleccionada = personaNaturalSeleccionada;
	}

	public Entidad getPersonaJuridicaSeleccionada() {
		return personaJuridicaSeleccionada;
	}

	public void setPersonaJuridicaSeleccionada(
			Entidad personaJuridicaSeleccionada) {
		this.personaJuridicaSeleccionada = personaJuridicaSeleccionada;
	}

	public String getTituloAnterior() {
		return tituloAnterior;
	}

	public void setTituloAnterior(String tituloAnterior) {
		this.tituloAnterior = tituloAnterior;
	}

	public List<Titulos> getTitulosAnteriores() {
		return titulosAnteriores;
	}

	public void setTitulosAnteriores(List<Titulos> titulosAnteriores) {
		this.titulosAnteriores = titulosAnteriores;
	}

	public Registro getRegistroRelacionado() {
		return registroRelacionado;
	}

	public void setRegistroRelacionado(Registro registroRelacionado) {
		this.registroRelacionado = registroRelacionado;
	}

	public Obras getObraRegistro() {
		return obraRegistro;
	}

	public void setObraRegistro(Obras obraRegistro) {
		this.obraRegistro = obraRegistro;
	}

	public List<Obras> getObrasRelacionadas() {
		return obrasRelacionadas;
	}

	public void setObrasRelacionadas(List<Obras> obrasRelacionadas) {
		this.obrasRelacionadas = obrasRelacionadas;
	}

	public Integer getTipoObra() {
		return tipoObra;
	}

	public void setTipoObra(Integer tipoObra) {
		this.tipoObra = tipoObra;
	}

	
}
