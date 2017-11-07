package la.netco.expedientes.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import la.netco.core.businesslogic.services.ServiceDao;
import la.netco.core.persistencia.vo.Usuario;
import la.netco.core.spring.BeansSpringConst;
import la.netco.core.spring.security.CommonsModKeys;
import la.netco.core.spring.security.SecuredAction;
import la.netco.expedientes.utils.ExpedientesConstants;
import la.netco.persistencia.dto.commons.ConstantsKeysFire;
import la.netco.persistencia.dto.commons.Evento;
import la.netco.persistencia.dto.commons.Expediente;

@ManagedBean
@ViewScoped
public class CambiaEtapaExpBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = BeansSpringConst.BEAN_ID_SERVICE_DAO)
	private transient ServiceDao daoServicio;

	private Expediente expedienteSeleccionado;
	private Integer idExpedienteSeleccionado;
	private String observacionGeneric;

	private List<SelectItem> eventoItems;
	private Short idEventoSeleccionado;
	
	private List<SelectItem> usuariosItems;
	private Short idUsuarioSeleccionado;


	public CambiaEtapaExpBean() {
		System.out.println("Construccion cambio etapa expediente");
	}

	public String getObservacionGeneric() {
		return observacionGeneric;
	}

	public void setObservacionGeneric(String observacionGeneric) {
		this.observacionGeneric = observacionGeneric;
	}

	public List<SelectItem> getUsuariosItems() {
		return usuariosItems;
	}

	public void setUsuariosItems(List<SelectItem> usuariosItems) {
		this.usuariosItems = usuariosItems;
	}

	public Short getIdUsuarioSeleccionado() {
		return idUsuarioSeleccionado;
	}

	public void setIdUsuarioSeleccionado(Short idUsuarioSeleccionado) {
		this.idUsuarioSeleccionado = idUsuarioSeleccionado;
	}

	public List<SelectItem> getEventoItems() {
		return eventoItems;
	}

	public void setEventoItems(List<SelectItem> eventoItems) {
		this.eventoItems = eventoItems;
	}

	public Short getIdEventoSeleccionado() {
		return idEventoSeleccionado;
	}

	public void setIdEventoSeleccionado(Short idEventoSeleccionado) {
		this.idEventoSeleccionado = idEventoSeleccionado;
	}

	public ServiceDao getDaoServicio() {
		return daoServicio;
	}

	public void setDaoServicio(ServiceDao daoServicio) {
		this.daoServicio = daoServicio;
	}

	public Expediente getExpedienteSeleccionado() {
		return expedienteSeleccionado;
	}

	public void setExpedienteSeleccionado(Expediente expedienteSeleccionado) {
		this.expedienteSeleccionado = expedienteSeleccionado;
	}

	public Integer getIdExpedienteSeleccionado() {
		return idExpedienteSeleccionado;
	}

	public void setIdExpedienteSeleccionado(Integer idExpedienteSeleccionado) {
		this.idExpedienteSeleccionado = idExpedienteSeleccionado;
	}

	public void cargaParametros() {
		try {
			if (idExpedienteSeleccionado == null) {
				FacesContext facesContext = FacesContext.getCurrentInstance();
				String idExpSeleccionado = facesContext.getExternalContext()
						.getRequestParameterMap().get("idExpSeleccionado");
				if (idExpSeleccionado != null)
					this.idExpedienteSeleccionado = Integer
							.parseInt(idExpSeleccionado);
			}

			if ((expedienteSeleccionado == null || expedienteSeleccionado
					.getExpId() == 0) && (idExpedienteSeleccionado != null)) {
				expedienteSeleccionado = (Expediente) daoServicio
						.getGenericCommonDao().read(Expediente.class,
								idExpedienteSeleccionado);
			}
			
			cargaEventoItems();
			cargaUsuariosItems();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Carga lista de eventos para el expediente seleccionado
	 */
	@SuppressWarnings({ "unchecked" })
	public void cargaEventoItems() {

		if (eventoItems == null && expedienteSeleccionado.getExpId() != 0) {
			HashMap<Integer, Object> parametrosBusqueda = new HashMap<Integer, Object>();

			System.out.println("Eventos****");
			System.out.println("Id Tramite de expediente: "
					+ expedienteSeleccionado.getTramite().getTrmId());
			System.out.println("Id Estado de expediente: "
					+ expedienteSeleccionado.getEstado().getEstId());
			System.out
					.println("Tipo de evento: "
							+ Byte.parseByte(ExpedientesConstants.TIPOEVENTO_SECUNDARIO));

			parametrosBusqueda.put(0, expedienteSeleccionado.getTramite()
					.getTrmId());
			parametrosBusqueda.put(1, expedienteSeleccionado.getEstado()
					.getEstId());
			parametrosBusqueda.put(2, Byte.parseByte(
					ExpedientesConstants.TIPOEVENTO_SECUNDARIO, 3));

			List<Evento> eventoAuxi = (List<Evento>) daoServicio
					.getGenericCommonDao()
					.executeFind(Evento.class, parametrosBusqueda,
							Evento.NAMED_QUERY_GET_EVEBYEXP);

			eventoItems = new ArrayList<SelectItem>();

			for (Evento evento : eventoAuxi) {
				System.out.println("Evento Id :" + evento.getEveId()
						+ " Evento Nombre : " + evento.getEveNom());
				eventoItems.add(new SelectItem(evento.getEveId(), evento
						.getEveNom()));
			}
		}

	}
	
	/**
	 * 
	 * carga lista de usuarios
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void cargaUsuariosItems() {
		if (usuariosItems == null) {
			List<Usuario> usuariosAuxi = (List<Usuario>) daoServicio
					.getGenericCommonDao().executeFind(
							Usuario.NAMED_QUERY_ALL_USUARIOS);

			usuariosItems = new ArrayList<SelectItem>();

			for (Usuario usuarioTemp : usuariosAuxi) {
				usuariosItems.add(new SelectItem(usuarioTemp.getUsrId(),
						usuarioTemp.getUsrNom() + usuarioTemp.getUsrPap()
								+ usuarioTemp.getUsrSap()));
			}
		}
	}
	
	//Acciones
	
	@SecuredAction(keyAction = ExpedientesConstants.CAMBIA_E, keyMod = CommonsModKeys.MNUEPAE)
	public String cambioEtapaExpediente() {


		expedienteSeleccionado.setUsuarioTemp((Usuario) daoServicio
				.getGenericCommonDao().read(Usuario.class,
						idUsuarioSeleccionado));

		daoServicio.getSeguimientosDao().addSeguimientoExpCambioEtapa(
				expedienteSeleccionado, ConstantsKeysFire.CAMETAPA,
				this.observacionGeneric, idEventoSeleccionado);

		return "infoExpedienteExp";
	}

}
