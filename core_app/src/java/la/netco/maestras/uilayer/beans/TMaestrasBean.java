package la.netco.maestras.uilayer.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.FacesContext;

import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import la.netco.core.businesslogic.services.ServiceDao;
import la.netco.core.spring.BeansSpringConst;
import la.netco.core.uilayer.beans.Constants;
import la.netco.core.uilayer.beans.utils.FacesUtils;
import la.netco.core.uilayer.commons.GenericDataModel;
import la.netco.maestras.persistence.dto.PropTablas;
import la.netco.maestras.persistence.dto.TablasMaestras;

@ManagedBean(name = "tMaestrasBean")
@ViewScoped
public class TMaestrasBean implements Serializable {


	private static final long serialVersionUID = 634669698438947842L;

	@ManagedProperty(value = BeansSpringConst.BEAN_ID_SERVICE_DAO)
	transient ServiceDao daoServicio;

	@ManagedProperty(value = "#{formMaster}")
	private VarTMaestrasBean tMaster;

	private List<SelectItem> itemsTMaestras;
	private Integer idTMSeleccionada;
	private Integer idTablaSeleccionada;
	private String idRegSeleccionado;

	private HtmlSelectOneMenu tablaSelecta;
	private String nombreTMaestra;

	private TablasMaestras tablaMaestraSeleccionada;

	private List<PropTablas> propTMList;

	private List<PropTablas> completaListaProp;

	private String columnaPropPK;

	private List<Map<String, Object>> mapaValoresLista;

	private List<PropTablas> listaPropPersistida;
	private List<Map<String, Object>> mapValoresPersistida;

	private List<Map<String, Object>> listaValoresPersistidos;

	private TMaestrasDataModel tMaestrasDataModel;
	private String nombre;

	public TMaestrasBean() {

	}

	public ServiceDao getDaoServicio() {
		return daoServicio;
	}

	public void setDaoServicio(ServiceDao daoServicio) {
		this.daoServicio = daoServicio;
	}

	@SuppressWarnings("deprecation")
	public List<SelectItem> getItemsTMaestras() {

		if (itemsTMaestras == null) {
			if (tablaSelecta != null
					&& tablaSelecta.getSubmittedValue() != null
					&& !tablaSelecta.getSubmittedValue().equals(""))
				idTMSeleccionada = Integer.parseInt((String) tablaSelecta
						.getSubmittedValue());

			List<TablasMaestras> allTablasMaestras = daoServicio
					.getTablasMaestrasDao().executeFind(
							TablasMaestras.NAMED_QUERY_GET_ALL);

			itemsTMaestras = new ArrayList<SelectItem>();
			for (TablasMaestras tablasMaestras : allTablasMaestras) {
				itemsTMaestras.add(new SelectItem(tablasMaestras.getId_tma(),
						tablasMaestras.getEti_tma(), tablasMaestras
								.getNom_tma()));
			}
		}

		return itemsTMaestras;
	}

	public void setItemsTMaestras(List<SelectItem> itemsTMaestras) {
		this.itemsTMaestras = itemsTMaestras;
	}

	public Integer getIdTMSeleccionada() {
		if (tablaMaestraSeleccionada != null
				&& tablaMaestraSeleccionada.getId_tma() != 0) {
			idTMSeleccionada = tablaMaestraSeleccionada.getId_tma();
		}
		return idTMSeleccionada;
	}

	public void setIdTMSeleccionada(Integer idTMSeleccionada) {
		this.idTMSeleccionada = idTMSeleccionada;
	}

	public HtmlSelectOneMenu getTablaSelecta() {
		return tablaSelecta;
	}

	public void setTablaSelecta(HtmlSelectOneMenu tablaSelecta) {
		this.tablaSelecta = tablaSelecta;
	}

	public String getNombreTMaestra() {
		return nombreTMaestra;
	}

	public void setNombreTMaestra(String nombreTMaestra) {
		this.nombreTMaestra = nombreTMaestra;
	}

	public List<Map<String, Object>> getMapaValoresLista() {
		return mapaValoresLista;
	}

	public void setMapaValoresLista(List<Map<String, Object>> mapaValoresLista) {
		this.mapaValoresLista = mapaValoresLista;
	}

	public List<PropTablas> getPropTMList() {
		return propTMList;
	}

	public void setPropTMList(List<PropTablas> propTMList) {
		this.propTMList = propTMList;
	}

	public List<PropTablas> getCompletaListaProp() {
		return completaListaProp;
	}

	public void setCompletaListaProp(List<PropTablas> completaListaProp) {
		this.completaListaProp = completaListaProp;
	}

	public String getColumnaPropPK() {
		return columnaPropPK;
	}

	public void setColumnaPropPK(String columnaPropPK) {
		this.columnaPropPK = columnaPropPK;
	}

	public Integer getIdTablaSeleccionada() {
		return idTablaSeleccionada;
	}

	public void setIdTablaSeleccionada(Integer idTablaSeleccionada) {
		this.idTablaSeleccionada = idTablaSeleccionada;
	}

	public List<PropTablas> getListaPropPersistida() {
		return listaPropPersistida;
	}

	public void setListaPropPersistida(List<PropTablas> listaPropPersistida) {
		this.listaPropPersistida = listaPropPersistida;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public TMaestrasDataModel gettMaestrasDataModel() {
		return tMaestrasDataModel;
	}

	public void settMaestrasDataModel(TMaestrasDataModel tMaestrasDataModel) {
		this.tMaestrasDataModel = tMaestrasDataModel;
	}

	public VarTMaestrasBean gettMaster() {
		return tMaster;
	}

	public void settMaster(VarTMaestrasBean tMaster) {
		this.tMaster = tMaster;
	}

	public String getIdRegSeleccionado() {
		return idRegSeleccionado;
	}

	public void setIdRegSeleccionado(String idRegSeleccionado) {
		this.idRegSeleccionado = idRegSeleccionado;
	}

	public List<Map<String, Object>> getMapValoresPersistida() {
		return mapValoresPersistida;
	}

	public void setMapValoresPersistida(
			List<Map<String, Object>> mapValoresPersistida) {
		this.mapValoresPersistida = mapValoresPersistida;
	}

	public List<Map<String, Object>> getListaValoresPersistidos() {
		return listaValoresPersistidos;
	}

	public void setListaValoresPersistidos(
			List<Map<String, Object>> listaValoresPersistidos) {
		this.listaValoresPersistidos = listaValoresPersistidos;
	}

	/*
	 * Datamodel
	 */

	private static final class TMaestrasDataModel extends
			GenericDataModel<TablasMaestras, Integer> {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private TMaestrasDataModel() {
			super(TablasMaestras.class);
			setOrderBy(Order.asc("nom_tma"));
		}

		@Override
		protected Object getId(TablasMaestras ObjTablasMaestras) {
			return ObjTablasMaestras.getId_tma();
		}

	}

	/*
	 * Metodos
	 */

	// Insertar
	public String insertarRegistro() {
		try {

			TablasMaestras tablaMaster = daoServicio.getTablasMaestrasDao()
					.read(idTablaSeleccionada);

			for (Map.Entry<String, String> variMap : tMaster.entrySet()) {
				System.out.println("Llave: " + variMap.getKey() + " Value: "
						+ variMap.getValue());
			}

			daoServicio.getSpringGenericDao().insertQuery(
					QueryUtils.getQueryForInsert(tMaster,
							tablaMaster.getNom_tma()));

			FacesUtils.addFacesMessageFromBundle(
					Constants.OPERACION_EXITOSA_CREAR_REGISTRO,
					FacesMessage.SEVERITY_INFO);

			return "transaccionExitosa";

		} catch (Exception exc) {
			FacesUtils.addFacesMessageFromBundle(Constants.ERROR_OPERACION,
					FacesMessage.SEVERITY_ERROR);
			exc.printStackTrace();
			return "transaccionExitosa";
		}
	}

	// Actualizar
	public String actualizarRegistro() {
		try {

			TablasMaestras tablaMaster = daoServicio.getTablasMaestrasDao()
					.read(idTablaSeleccionada);

			daoServicio.getSpringGenericDao().updateQuery(
					QueryUtils.getQueryForUpdate(tMaster,
							tablaMaster.getNom_tma(), columnaPropPK,
							idRegSeleccionado));

			FacesUtils.addFacesMessageFromBundle(
					Constants.OPERACION_EXITOSA_ACTUALIZAR_REGISTRO,
					FacesMessage.SEVERITY_INFO);
			return "transaccionExitosa";

		} catch (Exception e) {
			FacesUtils.addFacesMessageFromBundle(Constants.ERROR_OPERACION,
					FacesMessage.SEVERITY_ERROR);
			e.printStackTrace();
			return "transaccionExitosa";
		}
	}

	// Eliminar
	public String eliminarRegistro() {
		try {

			TablasMaestras tablaMaster = daoServicio.getTablasMaestrasDao()
					.read(idTablaSeleccionada);

			daoServicio.getSpringGenericDao().updateQuery(
					QueryUtils.getQueryForDelete(tablaMaster.getNom_tma(),
							columnaPropPK, idRegSeleccionado));
			
			FacesUtils.addFacesMessageFromBundle(Constants.OPERACION_EXITOSA_ELIMINAR_REGISTRO, FacesMessage.SEVERITY_INFO);
			return "transaccionExitosa";

		} catch (Exception exc) {
			FacesUtils.addFacesMessageFromBundle(Constants.ERROR_OPERACION,
					FacesMessage.SEVERITY_ERROR);
			exc.printStackTrace();
			return "transaccionExitosa";
		}
	}

	// cargar propiedades de tablas
	public List<PropTablas> cargarPropiTablas() {

		HashMap<Integer, Object> ParametrosBusqueda = new HashMap<Integer, Object>();

		int AuxiTM;

		if (idTMSeleccionada != null) {

			TablasMaestras tablaMaster = daoServicio.getTablasMaestrasDao().read(idTMSeleccionada);

			if (tablaMaster != null) {
				
				System.out.println("La tabla maestra actual es :"
						+ tablaMaster.getNom_tma() + " Su Id es: "
						+ tablaMaster.getId_tma());
	
				AuxiTM = tablaMaster.getId_tma();
	
				completaListaProp = daoServicio.getPropiedadesTMDao().executeFind(
						PropTablas.NAMED_QUERY_BY_PROP);
	
				mapaValoresLista = daoServicio.getSpringGenericDao().executeQuery(
						QueryUtils.getQueryByProperties(completaListaProp,
								tablaMaster.getNom_tma(), tablaMaster.getId_tma()));
	
				for (PropTablas propTemp : completaListaProp) {
	
					if (propTemp.getTablasMaestras().getId_tma() == AuxiTM) {
						if (propTemp.isProp_espk() == true) {
							columnaPropPK = propTemp.getProp_cam();
						}
					}
				}
	
				ParametrosBusqueda.put(0, tablaMaster.getId_tma());
	
				propTMList = daoServicio.getPropiedadesTMDao().executeFind(
						ParametrosBusqueda, PropTablas.NAMED_QUERYBY_PROP_VI);
			}

		}

		return null;
	}

	// cargar datos por modelo de filtros
	public String cargaFiltrosDataModel() {
		List<Criterion> filtros = new ArrayList<Criterion>();
		if (nombre != null && nombre.trim().equals("")) {
			Criterion nombre = Restrictions.ilike("tdoNom", "%" + this.nombre
					+ "%");
			filtros.add(nombre);
		}

		tMaestrasDataModel = new TMaestrasDataModel();
		tMaestrasDataModel.setFiltros(filtros);
		return "";

	}

	// Verificador de Map
	public void funcionVerificarMap() {

		for (Map.Entry<String, String> variMap : tMaster.entrySet()) {
			System.out.println("Key: " + variMap.getKey() + " Value: "
					+ variMap.getValue());
		}

		// return "tmaestrasSecc";
	}

	public void InitOP() {
		cargaFiltrosDataModel();
	}

	// Carga Valores de contexto
	public void cargaValores() {
		try {

			HttpServletRequest request = (HttpServletRequest) FacesContext
					.getCurrentInstance().getExternalContext().getRequest();
			listaPropPersistida = (List<PropTablas>) request
					.getAttribute("listaPropPersistida");

			mapValoresPersistida = (List<Map<String, Object>>) request
					.getAttribute("mapValoresPersistida");

			if (idTablaSeleccionada == null) {
				String idTablaSeleccionada = FacesContext.getCurrentInstance()
						.getExternalContext().getRequestParameterMap()
						.get("idTablaSeleccionada");

				if (idTablaSeleccionada != null) {
					this.idTablaSeleccionada = Integer
							.parseInt(idTablaSeleccionada);
				}

			}

			if (columnaPropPK == null) {
				String columnaPropPK = FacesContext.getCurrentInstance()
						.getExternalContext().getRequestParameterMap()
						.get("nameColumPK");

				if (columnaPropPK != null) {
					this.columnaPropPK = columnaPropPK;
				}
			}

			if (idRegSeleccionado == null) {
				String idRegSeleccionado = FacesContext.getCurrentInstance()
						.getExternalContext().getRequestParameterMap()
						.get("idRegSeleccionado");
				if (idRegSeleccionado != null) {
					
					this.idRegSeleccionado = idRegSeleccionado;
					
					cargaListaValPersistidos();
				}
			}

			if ((tablaMaestraSeleccionada == null || tablaMaestraSeleccionada
					.getId_tma() == 0)
					&& (idTablaSeleccionada != null && !idTablaSeleccionada
							.equals(""))) {
				tablaMaestraSeleccionada = (TablasMaestras) daoServicio
						.getGenericCommonDao().read(TablasMaestras.class,
								idTablaSeleccionada);

			} else {
				tablaMaestraSeleccionada = new TablasMaestras();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// Guarda Contexto para lista
	public String guardaContexto() {
		HttpServletRequest request = (HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();
		request.setAttribute("listaPropPersistida", listaPropPersistida);
		return "agregar";
	}

	public String guardaContextoActualizar() {
		HttpServletRequest request = (HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();
		request.setAttribute("listaPropPersistida", listaPropPersistida);
		request.setAttribute("mapValoresPersistida", mapValoresPersistida);
		return "actualizar";
	}

	public String guardaContextoEliminar() {
		HttpServletRequest request = (HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();
		request.setAttribute("listaPropPersistida", listaPropPersistida);
		request.setAttribute("mapValoresPersistida", mapValoresPersistida);
		return "eliminar";
	}

	
	public void cargaListaValPersistidos() {

		String NomCampo = "";
		for (PropTablas varPropT : listaPropPersistida) {
			NomCampo = varPropT.getProp_cam();
			for (Map<String, Object> variItera : mapValoresPersistida) {
				if (idRegSeleccionado.trim().equals(variItera.get(
						columnaPropPK).toString().trim())) {
					System.out.println("*****   " + NomCampo + " : "
							+ variItera.get(NomCampo).toString());
					tMaster.put(NomCampo, variItera.get(NomCampo).toString());
				}
			}
		}
	}

}
