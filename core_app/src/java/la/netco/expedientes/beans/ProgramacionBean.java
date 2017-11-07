package la.netco.expedientes.beans;

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
import javax.faces.model.SelectItem;

import la.netco.core.businesslogic.services.ServiceDao;
import la.netco.core.persistencia.vo.Usuario;
import la.netco.core.spring.BeansSpringConst;
import la.netco.core.spring.security.CommonsActionsKey;
import la.netco.core.spring.security.CommonsModKeys;
import la.netco.core.spring.security.SecuredAction;
import la.netco.core.uilayer.commons.GenericDataModel;
import la.netco.expedientes.utils.ExpedientesConstants;
import la.netco.persistencia.dto.commons.Depend;
import la.netco.persistencia.dto.commons.Detalleprogramacion;
import la.netco.persistencia.dto.commons.Entidad;
import la.netco.persistencia.dto.commons.Programacion;
import la.netco.persistencia.dto.commons.Salida;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

@ManagedBean(name = "programacionBean")
@ViewScoped
public class ProgramacionBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = BeansSpringConst.BEAN_ID_SERVICE_DAO)
	private transient ServiceDao daoServicio;

	private Entidad entidadSeleccionada;
	private String nombreInforme;
	private String resSoporteLegal;
	private Short idProgSeleccionada;
	private Date fechaVencimientoLegal;
	private String textoUnoGenerico;
	private String textoDosGenerico;
	private String textoTresGenerico;
	private Date fechaUnoGenerico;
	private Date fechaDosGenerico;
	private Short porcentajeGenerico;
	private Salida salidaSeleccionada;
	private Integer valorRenderizado;
	private Programacion programacionSeleccionada;
	private Detalleprogramacion dtlProgramacionSeleccionado;
	private List<Programacion> listaDummyProgramacion;

	private List<SelectItem> dependItems;
	private Short idDependSeleccionada;

	private List<SelectItem> usuariosItems;
	private Short idUsuarioSeleccionado;

	private List<Detalleprogramacion> listaDetallesProgramacion;
	private Integer idDetallesProgramacion;

	private ProgramacionDataModel programacionDataModel;

	private Programacion nuevaProgramacion;

	public ProgramacionBean() {
		programacionDataModel = new ProgramacionDataModel();

		if (entidadSeleccionada == null) {
			entidadSeleccionada = new Entidad();
		}

		if (salidaSeleccionada == null) {
			salidaSeleccionada = new Salida();
		}

		System.out.println("Construyendose Programacion");
	}

	@PostConstruct
	public void inicializador() {
		nuevaProgramacion = new Programacion();
		dtlProgramacionSeleccionado = new Detalleprogramacion();
		// cargaFiltrosDataModel();
		System.out.println("Construyendose Post-Programacion");
	}

	public String getTextoTresGenerico() {
		return textoTresGenerico;
	}

	public void setTextoTresGenerico(String textoTresGenerico) {
		this.textoTresGenerico = textoTresGenerico;
	}

	public Detalleprogramacion getDtlProgramacionSeleccionado() {
		return dtlProgramacionSeleccionado;
	}

	public void setDtlProgramacionSeleccionado(
			Detalleprogramacion dtlProgramacionSeleccionado) {
		this.dtlProgramacionSeleccionado = dtlProgramacionSeleccionado;
	}

	public List<Detalleprogramacion> getListaDetallesProgramacion() {
		return listaDetallesProgramacion;
	}

	public void setListaDetallesProgramacion(
			List<Detalleprogramacion> listaDetallesProgramacion) {
		this.listaDetallesProgramacion = listaDetallesProgramacion;
	}

	public Integer getIdDetallesProgramacion() {
		return idDetallesProgramacion;
	}

	public void setIdDetallesProgramacion(Integer idDetallesProgramacion) {
		this.idDetallesProgramacion = idDetallesProgramacion;
	}

	public Integer getValorRenderizado() {
		return valorRenderizado;
	}

	public void setValorRenderizado(Integer valorRenderizado) {
		this.valorRenderizado = valorRenderizado;
	}

	public Salida getSalidaSeleccionada() {
		return salidaSeleccionada;
	}

	public void setSalidaSeleccionada(Salida salidaSeleccionada) {
		this.salidaSeleccionada = salidaSeleccionada;
	}

	public Short getPorcentajeGenerico() {
		return porcentajeGenerico;
	}

	public void setPorcentajeGenerico(Short porcentajeGenerico) {
		this.porcentajeGenerico = porcentajeGenerico;
	}

	public Date getFechaUnoGenerico() {
		return fechaUnoGenerico;
	}

	public void setFechaUnoGenerico(Date fechaUnoGenerico) {
		this.fechaUnoGenerico = fechaUnoGenerico;
	}

	public Date getFechaDosGenerico() {
		return fechaDosGenerico;
	}

	public void setFechaDosGenerico(Date fechaDosGenerico) {
		this.fechaDosGenerico = fechaDosGenerico;
	}

	public String getTextoUnoGenerico() {
		return textoUnoGenerico;
	}

	public void setTextoUnoGenerico(String textoUnoGenerico) {
		this.textoUnoGenerico = textoUnoGenerico;
	}

	public String getTextoDosGenerico() {
		return textoDosGenerico;
	}

	public void setTextoDosGenerico(String textoDosGenerico) {
		this.textoDosGenerico = textoDosGenerico;
	}

	public Date getFechaVencimientoLegal() {
		return fechaVencimientoLegal;
	}

	public void setFechaVencimientoLegal(Date fechaVencimientoLegal) {
		this.fechaVencimientoLegal = fechaVencimientoLegal;
	}

	public Programacion getProgramacionSeleccionada() {
		return programacionSeleccionada;
	}

	public void setProgramacionSeleccionada(
			Programacion programacionSeleccionada) {
		this.programacionSeleccionada = programacionSeleccionada;
	}

	public Short getIdProgSeleccionada() {
		return idProgSeleccionada;
	}

	public void setIdProgSeleccionada(Short idProgSeleccionada) {
		this.idProgSeleccionada = idProgSeleccionada;
	}

	public String getResSoporteLegal() {
		return resSoporteLegal;
	}

	public void setResSoporteLegal(String resSoporteLegal) {
		this.resSoporteLegal = resSoporteLegal;
	}

	public Entidad getEntidadSeleccionada() {
		return entidadSeleccionada;
	}

	public void setEntidadSeleccionada(Entidad entidadSeleccionada) {
		this.entidadSeleccionada = entidadSeleccionada;
	}

	public Programacion getNuevaProgramacion() {
		return nuevaProgramacion;
	}

	public void setNuevaProgramacion(Programacion nuevaProgramacion) {
		this.nuevaProgramacion = nuevaProgramacion;
	}

	public List<SelectItem> getUsuariosItems() {
		cargaUsuariosItems();
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

	public List<SelectItem> getDependItems() {
		cargaDependItems();
		return dependItems;
	}

	public void setDependItems(List<SelectItem> dependItems) {
		this.dependItems = dependItems;
	}

	public Short getIdDependSeleccionada() {
		return idDependSeleccionada;
	}

	public void setIdDependSeleccionada(Short idDependSeleccionada) {
		this.idDependSeleccionada = idDependSeleccionada;
	}

	public String getNombreInforme() {
		return nombreInforme;
	}

	public void setNombreInforme(String nombreInforme) {
		this.nombreInforme = nombreInforme;
	}

	public ServiceDao getDaoServicio() {
		return daoServicio;
	}

	public void setDaoServicio(ServiceDao daoServicio) {
		this.daoServicio = daoServicio;
	}

	public List<Programacion> getListaDummyProgramacion() {
		return listaDummyProgramacion;
	}

	public void setListaDummyProgramacion(
			List<Programacion> listaDummyProgramacion) {
		this.listaDummyProgramacion = listaDummyProgramacion;
	}

	public ProgramacionDataModel getProgramacionDataModel() {
		return programacionDataModel;
	}

	public void setProgramacionDataModel(
			ProgramacionDataModel programacionDataModel) {
		this.programacionDataModel = programacionDataModel;
	}

	// Navegaciones *****

	@SecuredAction(keyAction = CommonsActionsKey.UPDATE, keyMod = CommonsModKeys.MNUEPPI)
	public String actualizarNavegacion() {
		return "actualizar";
	}

	public String listadoNavegacion() {
		return "listado";
	}

	@SecuredAction(keyAction = CommonsActionsKey.DELETE, keyMod = CommonsModKeys.MNUEPPI)
	public String eliminarNavegacion() {
		return "eliminar";
	}

	@SecuredAction(keyAction = CommonsActionsKey.ADD, keyMod = CommonsModKeys.MNUEPPI)
	public String agregarNavegacion() {
		return "agregar";
	}

	@SecuredAction(keyAction = CommonsActionsKey.VIEW, keyMod = CommonsModKeys.MNUEPPI)
	public String detalleNavegacion() {
		return "detalleProg";
	}

	@SecuredAction(keyAction = ExpedientesConstants.ADD_DTL, keyMod = CommonsModKeys.MNUEPPI)
	public String agregarDetalleNavegacion() {
		return "agregarDetalleProg";
	}

	@SecuredAction(keyAction = ExpedientesConstants.UPD_DTL, keyMod = CommonsModKeys.MNUEPPI)
	public String actualizarDetalleNavegacion() {
		return "actualizarDetalleProg";
	}

	@SecuredAction(keyAction = ExpedientesConstants.DEL_DTL, keyMod = CommonsModKeys.MNUEPPI)
	public String eliminarDetalleNavegacion() {
		return "eliminarDetalleProg";
	}

	@SecuredAction(keyAction = ExpedientesConstants.DUPLEX_DTL, keyMod = CommonsModKeys.MNUEPPI)
	public String duplicarDetalleNavegacion() {
		return "duplicarDetalleProg";
	}

	// ******

	// Acciones propias de programacion ****

	@SecuredAction(keyAction = CommonsActionsKey.ADD, keyMod = CommonsModKeys.MNUEPPI)
	public String agregarProgramacion() {
		try {
			System.out.println("Agregando Programacion*****");

			System.out.println("El Nombre del Informe es: "
					+ this.nombreInforme);
			nuevaProgramacion.setPrgNom(this.nombreInforme);

			System.out.println("La dependencia es: " + idDependSeleccionada);
			nuevaProgramacion.setDepend((Depend) daoServicio
					.getGenericCommonDao().read(Depend.class,
							idDependSeleccionada));

			System.out.println("La Empresa es: "
					+ entidadSeleccionada.getEtdNom());
			nuevaProgramacion.setEntidad(entidadSeleccionada);

			System.out.println("El Soporte legal es:" + this.resSoporteLegal);
			nuevaProgramacion.setPrgRes(this.resSoporteLegal);

			Integer idProgC = (Integer) daoServicio.getGenericCommonDao()
					.create(Programacion.class, nuevaProgramacion);

			System.out
					.println("Programacion agregada exitosamente**** Prog. No: "
							+ idProgC);
			return "transaccionExitosa";
		} catch (Exception e) {
			e.printStackTrace();
			return "transaccionExitosa";
		}

	}

	@SecuredAction(keyAction = CommonsActionsKey.UPDATE, keyMod = CommonsModKeys.MNUEPPI)
	public String actualizarProgramacion() {

		try {
			System.out.println("Actualizando Programacion***");
			daoServicio.getGenericCommonDao().update(Programacion.class,
					programacionSeleccionada);
			System.out.println("Programacion Actualizada***");
			return "transaccionExitosa";
		} catch (Exception e) {
			e.printStackTrace();
			return "transaccionExitosa";
		}

	}

	// Acciones propias de detalle de programacion ****

	@SecuredAction(keyAction = ExpedientesConstants.ADD_DTL, keyMod = CommonsModKeys.MNUEPPI)
	public String agregarDetalleProg() {

		System.out.println("Creando Detalle de Programacion****");

		Detalleprogramacion nuevoDetalleProgramacion = new Detalleprogramacion();

		System.out.println("La programacion seleccionada es: "
				+ programacionSeleccionada.getPrgNom());
		nuevoDetalleProgramacion.setProgramacion(programacionSeleccionada);

		System.out.println("El Usuario es :" + idUsuarioSeleccionado);
		nuevoDetalleProgramacion.setUsuario((Usuario) daoServicio
				.getGenericCommonDao().read(Usuario.class,
						idUsuarioSeleccionado));

		System.out.println("La Dependencia es  : " + idDependSeleccionada);
		nuevoDetalleProgramacion
				.setDepend((Depend) daoServicio.getGenericCommonDao().read(
						Depend.class, idDependSeleccionada));

		System.out.println("La fecha de vencimiento es: "
				+ fechaVencimientoLegal);
		nuevoDetalleProgramacion.setDprFve(fechaVencimientoLegal);

		System.out.println("La fecha de entrega es: " + fechaUnoGenerico);
		nuevoDetalleProgramacion.setDprFen(fechaVencimientoLegal);

		if (salidaSeleccionada.getSalId() != null) {
			porcentajeGenerico = 100;
			System.out.println("El porcentaje es: " + porcentajeGenerico);
			nuevoDetalleProgramacion.setDprPor(porcentajeGenerico);
		} else if (salidaSeleccionada.getSalId() == null) {
			System.out.println("El porcentaje es: " + porcentajeGenerico);
			nuevoDetalleProgramacion.setDprPor(porcentajeGenerico);
		}

		System.out.println("Los Comentarios son: " + textoDosGenerico);
		nuevoDetalleProgramacion.setDprCom(textoDosGenerico);

		System.out.println("El periodo del informe es : " + textoUnoGenerico);
		nuevoDetalleProgramacion.setDprPrd(textoUnoGenerico);

		System.out.println("La fecha real de entrega interna es: "
				+ fechaDosGenerico);
		nuevoDetalleProgramacion.setDprFre(fechaDosGenerico);

		if (porcentajeGenerico == 100 && salidaSeleccionada.getSalId() == null) {
			System.out.println("La fecha oficial  de entrega de salida: "
					+ new Date(System.currentTimeMillis()));
			nuevoDetalleProgramacion.setDprFeo(new Date(System
					.currentTimeMillis()));
		} else if (porcentajeGenerico == 100
				&& salidaSeleccionada.getSalId() != null) {
			System.out.println("La fecha oficial  de entrega de salida: "
					+ salidaSeleccionada.getSalFsa());
			nuevoDetalleProgramacion.setDprFeo(salidaSeleccionada.getSalFsa());

			System.out.println("el numero de radicacion de salida es: "
					+ salidaSeleccionada.getSalNsa());
			nuevoDetalleProgramacion.setDprNsa(salidaSeleccionada.getSalNsa());

			System.out.println("La salida es:" + salidaSeleccionada.getSalId());
			nuevoDetalleProgramacion.setSalida(salidaSeleccionada);
		}
		Integer idNDtllProg = (Integer) daoServicio.getGenericCommonDao()
				.create(Detalleprogramacion.class, nuevoDetalleProgramacion);

		System.out.println("Detalle de programacion creado**** Detalle No. :"
				+ idNDtllProg);
		return "detalleProg";
	}

	@SecuredAction(keyAction = ExpedientesConstants.UPD_DTL, keyMod = CommonsModKeys.MNUEPPI)
	public String actualizarDetalleProg() {

		System.out.println("Actualizacion de detalle de programacion****");
		daoServicio.getGenericCommonDao().update(Detalleprogramacion.class,
				dtlProgramacionSeleccionado);
		System.out.println("Detalle de programacion actualizado****");
		return "detalleProg";
	}

	@SecuredAction(keyAction = ExpedientesConstants.DEL_DTL, keyMod = CommonsModKeys.MNUEPPI)
	public String eliminarDetalleProg() {
		try {
			System.out.println("Eliminando de detalle de programacion****");
			daoServicio.getGenericCommonDao().delete(Detalleprogramacion.class,
					dtlProgramacionSeleccionado);
			System.out.println("Detalle de programacion eliminado****");
			return "detalleProg";
		} catch (Exception e) {
			e.printStackTrace();
			return "detalleProg";
		}
	}

	@SecuredAction(keyAction = ExpedientesConstants.DUPLEX_DTL, keyMod = CommonsModKeys.MNUEPPI)
	public String duplicarDetalleProg() {
		try {
			System.out.println("Duplicando detalle de programacion****");

			Detalleprogramacion detalleProgramacionDuplex = new Detalleprogramacion();

			detalleProgramacionDuplex
					.setProgramacion(dtlProgramacionSeleccionado
							.getProgramacion());
			detalleProgramacionDuplex.setUsuario(dtlProgramacionSeleccionado
					.getUsuario());
			detalleProgramacionDuplex.setDepend(dtlProgramacionSeleccionado
					.getDepend());
			detalleProgramacionDuplex.setDprFve(dtlProgramacionSeleccionado
					.getDprFve());
			detalleProgramacionDuplex.setDprFen(dtlProgramacionSeleccionado
					.getDprFen());

			if (dtlProgramacionSeleccionado.getSalida() != null ) {
				porcentajeGenerico = 100;
				detalleProgramacionDuplex.setDprPor(porcentajeGenerico);
			} else if (dtlProgramacionSeleccionado.getSalida() == null) {
				detalleProgramacionDuplex.setDprPor(dtlProgramacionSeleccionado
						.getDprPor());
			}

			detalleProgramacionDuplex.setDprCom(dtlProgramacionSeleccionado
					.getDprCom());
			detalleProgramacionDuplex.setDprPrd(dtlProgramacionSeleccionado
					.getDprPrd());
			detalleProgramacionDuplex.setDprFre(dtlProgramacionSeleccionado
					.getDprFre());

			if (dtlProgramacionSeleccionado.getDprPor() == 100
					&& dtlProgramacionSeleccionado.getSalida()== null) {
				System.out.println("La fecha oficial  de entrega de salida: "
						+ new Date(System.currentTimeMillis()));
				detalleProgramacionDuplex.setDprFeo(new Date(System
						.currentTimeMillis()));
			} else if (dtlProgramacionSeleccionado.getDprPor() == 100
					&& dtlProgramacionSeleccionado.getSalida().getSalId() != null) {
				detalleProgramacionDuplex.setDprFeo(dtlProgramacionSeleccionado
						.getSalida().getSalFsa());
				detalleProgramacionDuplex.setDprNsa(dtlProgramacionSeleccionado
						.getSalida().getSalNsa());
				detalleProgramacionDuplex.setSalida(dtlProgramacionSeleccionado
						.getSalida());
			}

			Integer idDtlDuplex = (Integer) daoServicio.getGenericCommonDao()
					.create(Detalleprogramacion.class,
							detalleProgramacionDuplex);

			System.out
					.println("Detalle de programacion duplicado**** Detalle No.: "
							+ idDtlDuplex);
			return "detalleProg";
		} catch (Exception e) {
			e.printStackTrace();
			return "detalleProg";
		}
	}

	// ******

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

	/**
	 * Cargar lista de items de dependencias
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void cargaDependItems() {

		if (dependItems == null) {
			List<Depend> dependAuxi = (List<Depend>) daoServicio
					.getGenericCommonDao().executeFind(
							Depend.NAMED_QUERY_ALL_DEPEND);

			dependItems = new ArrayList<SelectItem>();

			for (Depend depend : dependAuxi) {
				dependItems.add(new SelectItem(depend.getDepId(), depend
						.getDepNom()));
			}
		}

	}

	public void cargaParametrosAct() {
		System.out
				.println("Cargando parametros de actualizacion de detalle de prog.****");
		try {

			if (idDetallesProgramacion == null) {
				FacesContext contextoFaces = FacesContext.getCurrentInstance();
				String idDtlProgSeleccionada = contextoFaces
						.getExternalContext().getRequestParameterMap()
						.get("idDtlProgSeleccionada");
				if (idDtlProgSeleccionada != null) {
					this.idDetallesProgramacion = Integer
							.parseInt(idDtlProgSeleccionada);
				}

				if ((dtlProgramacionSeleccionado == null || dtlProgramacionSeleccionado
						.getDprId() == 0) && (idDetallesProgramacion != null)) {
					dtlProgramacionSeleccionado = (Detalleprogramacion) daoServicio
							.getGenericCommonDao().read(
									Detalleprogramacion.class,
									this.idDetallesProgramacion);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void cargarProgramacionDetalle() {
		System.out
				.println("Cargando parametros para detalle de programacion***");
		try {
			if (idProgSeleccionada == null) {
				FacesContext contextoFaces = FacesContext.getCurrentInstance();
				String idProgSeleccionada = contextoFaces.getExternalContext()
						.getRequestParameterMap().get("idProgSeleccionada");

				if (idProgSeleccionada != null) {
					this.idProgSeleccionada = Short
							.parseShort(idProgSeleccionada);
				}

				if ((programacionSeleccionada == null || programacionSeleccionada
						.getPrgId() == 0) && (idProgSeleccionada != null)) {
					programacionSeleccionada = (Programacion) daoServicio
							.getGenericCommonDao().read(Programacion.class,
									this.idProgSeleccionada);
					cargaDetallesTabla();
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public void cargaDetallesTabla() {
		System.out.println("***Cargando detalles de programacion***");

		HashMap<Integer, Short> parametrosBusq = new HashMap<Integer, Short>();

		parametrosBusq.put(0, idProgSeleccionada);
		listaDetallesProgramacion = new ArrayList<Detalleprogramacion>();
		listaDetallesProgramacion = (List<Detalleprogramacion>) daoServicio
				.getGenericCommonDao().executeFind(Detalleprogramacion.class,
						parametrosBusq,
						Detalleprogramacion.NAMED_QUERY_GET_BYPROG);

	}

	public void cargaFiltrosDataModel() {
		List<Criterion> filtros = new ArrayList<Criterion>();
		Map<String, String> alias = new HashMap<String, String>();

		if (nombreInforme != null && !nombreInforme.trim().equals("")) {
			filtros.add(Restrictions.like("prgNom", "%" + this.nombreInforme
					+ "%"));
			System.out.println("Buscando por nombre de programacion.");
		}

		if (idDependSeleccionada != null && idDependSeleccionada != 0) {
			filtros.add(Restrictions.eq("depend.depId", idDependSeleccionada));
			System.out.println("Buscando por dependencia.");
		}

		if (idUsuarioSeleccionado != null && idUsuarioSeleccionado != 0) {
			alias.put("detalleprogramacions", "detalleprogramacions");
			filtros.add(Restrictions.eq("detalleprogramacions.usuario.usrId",
					idUsuarioSeleccionado));
			System.out.println("Buscando por usuario.");
		}

		if (textoTresGenerico != null && !textoTresGenerico.trim().equals("")) {
			filtros.add(Restrictions.like("entidad.etdNom", textoTresGenerico));
			System.out.println("Buscando por Entidad");
		}

		if (fechaUnoGenerico != null || fechaDosGenerico != null) {
			alias.put("detalleprogramacions", "detalleprogramacions");
			if (fechaUnoGenerico == null) {
				fechaUnoGenerico = Calendar.getInstance().getTime();
			}
			if (fechaDosGenerico == null) {
				fechaDosGenerico = Calendar.getInstance().getTime();
			}
			filtros.add(Restrictions.between("detalleprogramacions.dprFve",
					fechaUnoGenerico, fechaDosGenerico));
		}
	
		programacionDataModel.setAlias(alias);
		programacionDataModel.setFiltros(filtros);

	}

	private static final class ProgramacionDataModel extends
			GenericDataModel<Programacion, Integer> {
		private static final long serialVersionUID = 1L;

		private ProgramacionDataModel() {
			super(Programacion.class);
			setOrderBy(Order.asc("prgNom"));
		}

		@Override
		protected Object getId(Programacion t) {
			return t.getPrgId();
		}
	}

}
