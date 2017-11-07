package la.netco.core.uilayer.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import la.netco.core.businesslogic.services.ServiceDao;
import la.netco.core.persistencia.vo.Usuario;
import la.netco.core.spring.BeansSpringConst;
import la.netco.core.spring.security.UserDetailsUtils;
import la.netco.core.uilayer.commons.GenericDataModel;
import la.netco.persistencia.dto.commons.Detalleprogramacion;
import la.netco.persistencia.dto.commons.Expediente;
import la.netco.persistencia.dto.commons.Programacion;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

@ManagedBean(name = "alertasModulosBean")
@ViewScoped
public class AlertasModulosBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<Detalleprogramacion> listaDetalleProgramacion;
	private List<Programacion> listaProgramacion;
	private Short idProgSeleccionada;
	private Usuario usuarioLogeado;

	private ExpedientesDataModel expedientesDataModel;

	@ManagedProperty(value = BeansSpringConst.BEAN_ID_SERVICE_DAO)
	private transient ServiceDao daoServicio;

	public AlertasModulosBean() {
		System.out.println("Construccion Alertas de Modulos");
	}

	@PostConstruct
	public void InitAlertarModulosBean() {
		expedientesDataModel = new ExpedientesDataModel();
		usuarioLogeado = UserDetailsUtils.usuarioLogged();
		cargaListaProgramaciones();
	}

	public Short getIdProgSeleccionada() {
		return idProgSeleccionada;
	}

	public void setIdProgSeleccionada(Short idProgSeleccionada) {
		this.idProgSeleccionada = idProgSeleccionada;
	}

	public ExpedientesDataModel getExpedientesDataModel() {
		return expedientesDataModel;
	}

	public void setExpedientesDataModel(
			ExpedientesDataModel expedientesDataModel) {
		this.expedientesDataModel = expedientesDataModel;
	}

	public ServiceDao getDaoServicio() {
		return daoServicio;
	}

	public void setDaoServicio(ServiceDao daoServicio) {
		this.daoServicio = daoServicio;
	}

	public List<Detalleprogramacion> getListaDetalleProgramacion() {
		return listaDetalleProgramacion;
	}

	public void setListaDetalleProgramacion(
			List<Detalleprogramacion> listaDetalleProgramacion) {
		this.listaDetalleProgramacion = listaDetalleProgramacion;
	}

	public List<Programacion> getListaProgramacion() {
		return listaProgramacion;
	}

	public void setListaProgramacion(List<Programacion> listaProgramacion) {
		this.listaProgramacion = listaProgramacion;
	}

	public Usuario getUsuarioLogeado() {
		return usuarioLogeado;
	}

	public void setUsuarioLogeado(Usuario usuarioLogeado) {
		this.usuarioLogeado = usuarioLogeado;
	}

	// Acciones ***
	
	@SuppressWarnings("unchecked")
	public void cargaDetallesDeProg() {
		System.out
				.println("Cargando parametros para detalle de programacion***");
		try {

			FacesContext contextoFaces = FacesContext.getCurrentInstance();
			String idProgSeleccionada = contextoFaces.getExternalContext()
					.getRequestParameterMap().get("idProgSeleccionada");
			HashMap<Integer, Short> paramsBusq = new HashMap<Integer, Short>();

			if (idProgSeleccionada != null) {
				this.idProgSeleccionada = Short.parseShort(idProgSeleccionada);

				paramsBusq.put(0, (short) 100);
				paramsBusq.put(1, this.idProgSeleccionada);

				listaDetalleProgramacion = (List<Detalleprogramacion>) daoServicio
						.getGenericCommonDao().executeFind(
								Detalleprogramacion.class, paramsBusq,
								Detalleprogramacion.NAMED_QUERY_GET_BYPOR);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ****

	// Acciones de Carga***

	@SuppressWarnings("unchecked")
	public void cargaListaProgramaciones() {
		listaProgramacion = new ArrayList<Programacion>();
		listaProgramacion = (List<Programacion>) daoServicio
				.getGenericCommonDao().loadAll(Programacion.class);

		// HashMap<Integer, Short> paramBusq = new HashMap<Integer, Short>();
		// listaProgramacion = new ArrayList<Programacion>();
		// paramBusq.put(0, UserDetailsUtils.usuarioLogged().getUsrId());
		// listaProgramacion = (List<Programacion>) daoServicio
		// .getGenericCommonDao().executeFind(Programacion.class,
		// paramBusq, Programacion.NAMED_QUERY_GET_BYDTL);

	}

	//

	// *** Datamodels

	private static final class ExpedientesDataModel extends
			GenericDataModel<Expediente, Integer> {
		private static final long serialVersionUID = 1L;

		private ExpedientesDataModel() {
			super(Expediente.class);
			byte variByte;
			Calendar calendarAuxi = Calendar.getInstance();
			calendarAuxi.add(Calendar.DATE, -1);
			Date fechaAyer = calendarAuxi.getTime();
			variByte = Byte.parseByte("2");
			List<Criterion> filtros = new ArrayList<Criterion>();
			filtros.add(Restrictions.eq("tiposexpediente.texId", variByte));
			filtros.add(Restrictions.eq("usuarioTemp.usrId", UserDetailsUtils
					.usuarioLogged().getUsrId()));
			filtros.add(Restrictions.between("expFso", fechaAyer, Calendar
					.getInstance().getTime()));
			Map<String, String> alias = new HashMap<String, String>();
			setAlias(alias);
			setFiltros(filtros);
			setOrderBy(Order.asc("expNom"));
		}

		@Override
		protected Object getId(Expediente t) {
			return t.getExpId();
		}
	}

}
