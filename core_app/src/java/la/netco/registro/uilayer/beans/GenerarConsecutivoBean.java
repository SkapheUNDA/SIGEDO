package la.netco.registro.uilayer.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import la.netco.commons.exceptions.NumeroRadicacionException;
import la.netco.core.businesslogic.services.ServiceDao;
import la.netco.core.persistencia.vo.Usuario;
import la.netco.core.spring.BeansSpringConst;
import la.netco.core.spring.security.UserDetailsUtils;
import la.netco.core.uilayer.beans.Constants;
import la.netco.core.uilayer.beans.utils.FacesUtils;
import la.netco.correspondencia.dto.custom.ConsecutivoRegistro;
import la.netco.persistencia.dto.commons.Configuracion;
import la.netco.persistencia.dto.commons.Expediente;
import la.netco.persistencia.dto.commons.ExpedienteEstado;
import la.netco.persistencia.dto.commons.Registro;
import la.netco.persistencia.dto.commons.Seguimientoexpediente;
import la.netco.persistencia.dto.commons.Transicion;

@ManagedBean
@ViewScoped
public class GenerarConsecutivoBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private static String ERROR_GENERAR_CODIGO = "errorGenerandoCodigo";

	private static String ERROR_VALIDACION_REG2_MENOR = "expRegistroErrorReg2Ini";

	private static String ERROR_VALIDACION_REG3_MENOR = "expRegistroErrorReg3Ini";

	private static String ERROR_VALIDACION_REG2_MAYOR = "expRegistroErrorReg2Ter";

	private static String ERROR_VALIDACION_REG3_MAYOR = "expRegistroErrorReg3Ter";

	private static String GENERO_CODIGO = "GENERO CODIGO";

	private static String GENERO_CODIGO_MANUAL = "GENERO CODIGO MANUAL";

	private Registro registroSeleccionado;

	private Integer idRegSeleccionado;

	private String regNo1;
	private Integer regNo2;
	private Integer regNo3;
	private Date regFec;

	@ManagedProperty(value = BeansSpringConst.BEAN_ID_SERVICE_DAO)
	transient private ServiceDao serviceDao;

	private String observacion = "";

	private Map<Integer, String> idsRegistrosSeleccionadas = new HashMap<Integer, String>();

	private List<Registro> registrosSeleccionados = null;

	@SuppressWarnings("unchecked")
	public void generarConsecutivoAuto(Integer regId) {
		System.out.println("prueba");
		try {

			Registro registro = (Registro) serviceDao.getGenericCommonDao()
					.read(Registro.class, regId);
			ConsecutivoRegistro consecutivo = GenerarConsecutivoUtil
					.obtenerConsecutivo(registro);

			registro.setRegNo1(consecutivo.getRegNo1().toString());
			registro.setRegNo2(consecutivo.getRegNo2());
			registro.setRegNo3(consecutivo.getRegNo3());
			registro.setRegCod(consecutivo.getConsecutivoCompleto());
			registro.setRegFec(new Date(System.currentTimeMillis()));
			registro.setRegLgen(true);
			registro.setRegLblo(true);
			Usuario usuario = UserDetailsUtils.usuarioLogged();
			registro.setUsuario(usuario);
			serviceDao.getGenericCommonDao().update(Registro.class, registro);

			Expediente expediente = registro.getExpediente();

			ExpedienteEstado estadoGeneral = (ExpedienteEstado) serviceDao
					.getGenericCommonDao().read(ExpedienteEstado.class,
							ExpedienteEstado.REGISTRADO_AUTO);

			expediente.setEstadoGeneral(estadoGeneral);
			serviceDao.getGenericCommonDao().update(Expediente.class,
					expediente);

			Map<Integer, Object> params = new HashMap<Integer, Object>();
			params.put(0, expediente.getExpId());
			List<Seguimientoexpediente> seguimientos = (List<Seguimientoexpediente>) serviceDao
					.getEntradaDao()
					.executeFindPaged(0, 1, params,
							Seguimientoexpediente.NAMED_QUERY_GET_BY_EXPEDIENTE);
			Seguimientoexpediente seguimiento = seguimientos.get(0);

			Transicion transicion = new Transicion();
			transicion.setEstadoByTrsEstfin(seguimiento.getEstadoBySgeEst());
			transicion.setEstadoByTrsEstini(seguimiento.getEstadoBySgeEstini());
			transicion.setEvento(seguimiento.getEvento());

			serviceDao.getSeguimientosDao()
					.addSeguimientoExp(
							expediente,
							GENERO_CODIGO + ">="
									+ consecutivo.getConsecutivoCompleto(),
							observacion, usuario, seguimiento.getSgeUsr(),
							transicion);

			registroSeleccionado = registro;
			FacesUtils.addFacesMessageFromBundle(
					Constants.OPERACION_EXITOSA_ACTUALIZAR_REGISTRO,
					FacesMessage.SEVERITY_INFO);
		} catch (NumeroRadicacionException e) {
			e.printStackTrace();
			FacesUtils.addFacesMessageFromBundle(ERROR_GENERAR_CODIGO,
					FacesMessage.SEVERITY_FATAL);
		} catch (Exception e) {
			e.printStackTrace();
			FacesUtils.addFacesMessageFromBundle(ERROR_GENERAR_CODIGO,
					FacesMessage.SEVERITY_FATAL);
		}
	}

	@SuppressWarnings("unchecked")
	// @SecuredAction(keyAction = ExpedientesConstants.DEV_EXP, keyMod =
	// CommonsModKeys.MNUEPAE)
	public String generarConsecutivoManual() {
		try {

			Configuracion conf = (Configuracion) serviceDao
					.getGenericCommonDao()
					.executeFind(Configuracion.NAMED_QUERY_GET_ALL_CONF).get(0);

			Integer regIni2 = conf.getRegIni2();
			Integer regIni3 = conf.getRegIni3();

			Integer regTer2 = conf.getRegTer2();
			Integer regTer3 = conf.getRegTer3();

			boolean error = false;

			if (regIni2 != 0 && regNo2 < regIni2) {
				FacesUtils.addFacesMessageFromBundle(
						ERROR_VALIDACION_REG2_MENOR,
						FacesMessage.SEVERITY_ERROR);
				error = true;
			}

			if (regIni3 != 0 && regNo3 < regIni3) {
				FacesUtils.addFacesMessageFromBundle(
						ERROR_VALIDACION_REG3_MENOR,
						FacesMessage.SEVERITY_ERROR);
				error = true;
			}

			if (regTer2 != 0 && regNo2 > regTer2) {
				FacesUtils.addFacesMessageFromBundle(
						ERROR_VALIDACION_REG2_MAYOR,
						FacesMessage.SEVERITY_ERROR);
				error = true;
			}

			if (regTer3 != 0 && regNo3 > regTer3) {
				FacesUtils.addFacesMessageFromBundle(
						ERROR_VALIDACION_REG3_MAYOR,
						FacesMessage.SEVERITY_ERROR);
				error = true;
			}

			if (error) {
				return "";
			}

			Registro registro = (Registro) serviceDao.getGenericCommonDao()
					.read(Registro.class, registroSeleccionado.getRegId());
			ConsecutivoRegistro consecutivo = GenerarConsecutivoUtil
					.obtenerConsecutivoManual(registro, regNo1, regNo2, regNo3,
							regFec);

			registro.setRegNo1(consecutivo.getRegNo1().toString());
			registro.setRegNo2(consecutivo.getRegNo2());
			registro.setRegNo3(consecutivo.getRegNo3());
			registro.setRegCod(consecutivo.getConsecutivoCompleto());
			registro.setRegFec(new Date(System.currentTimeMillis()));
			registro.setRegLgen(true);
			registro.setRegLblo(true);
			Usuario usuario = UserDetailsUtils.usuarioLogged();
			registro.setUsuario(usuario);
			serviceDao.getGenericCommonDao().update(Registro.class, registro);

			Expediente expediente = registro.getExpediente();

			ExpedienteEstado estadoGeneral = (ExpedienteEstado) serviceDao
					.getGenericCommonDao().read(ExpedienteEstado.class,
							ExpedienteEstado.REGISTRADO_AUTO);

			expediente.setEstadoGeneral(estadoGeneral);
			serviceDao.getGenericCommonDao().update(Expediente.class,
					expediente);

			Map<Integer, Object> params = new HashMap<Integer, Object>();
			params.put(0, expediente.getExpId());
			List<Seguimientoexpediente> seguimientos = (List<Seguimientoexpediente>) serviceDao
					.getEntradaDao()
					.executeFindPaged(0, 1, params,
							Seguimientoexpediente.NAMED_QUERY_GET_BY_EXPEDIENTE);
			Seguimientoexpediente seguimiento = seguimientos.get(0);

			Transicion transicion = new Transicion();
			transicion.setEstadoByTrsEstfin(seguimiento.getEstadoBySgeEst());
			transicion.setEstadoByTrsEstini(seguimiento.getEstadoBySgeEstini());
			transicion.setEvento(seguimiento.getEvento());

			serviceDao.getSeguimientosDao().addSeguimientoExp(
					expediente,
					GENERO_CODIGO_MANUAL + ">="
							+ consecutivo.getConsecutivoCompleto(), "",
					usuario, seguimiento.getSgeUsr(), transicion);

			registroSeleccionado = registro;
			FacesUtils.addFacesMessageFromBundle(
					Constants.OPERACION_EXITOSA_ACTUALIZAR_REGISTRO,
					FacesMessage.SEVERITY_INFO);

			return "transaccionExitosa";

		} catch (NumeroRadicacionException e) {
			e.printStackTrace();
			FacesUtils.addFacesMessageFromBundle(ERROR_GENERAR_CODIGO,
					FacesMessage.SEVERITY_FATAL);
			return "";
		} catch (Exception e) {
			FacesUtils.addFacesMessageFromBundle(Constants.ERROR_OPERACION,
					FacesMessage.SEVERITY_FATAL);
			e.printStackTrace();
			return "";
		}
	}

	// @SecuredAction(keyAction = ExpedientesConstants.DEV_EXP, keyMod =
	// CommonsModKeys.MNUEPAE)
	public String generarConsecutivoMasivo() {
		try {

			for (Registro registro : registrosSeleccionados) {
				if ((registro.getRegLblo() == null || registro.getRegLblo()
						.equals(Boolean.FALSE)) && !registro.isRegLgen()) {
					generarConsecutivoAuto(registro.getRegId());
				}
			}
			return "transaccionExitosa";
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	@SuppressWarnings("unchecked")
	public void cargarRegistro() {
		try {

			FacesContext facesContext = FacesContext.getCurrentInstance();
			if (idRegSeleccionado == null) {
				facesContext = FacesContext.getCurrentInstance();
				String idRegSeleccionado = facesContext.getExternalContext()
						.getRequestParameterMap().get("idRegSeleccionado");
				if (idRegSeleccionado != null)
					this.idRegSeleccionado = Integer
							.parseInt(idRegSeleccionado);
			}

			if ((registroSeleccionado == null || registroSeleccionado
					.getRegId() == null) && (idRegSeleccionado != null)) {
				registroSeleccionado = (Registro) serviceDao
						.getGenericCommonDao().read(Registro.class,
								idRegSeleccionado);
				regNo1 = GenerarConsecutivoUtil
						.obtenerReg1(registroSeleccionado);
			}

			if (idRegSeleccionado == null && registrosSeleccionados == null) {
				idsRegistrosSeleccionadas = (Map<Integer, String>) FacesUtils
						.flashScope(FacesContext.getCurrentInstance()).get(
								"idsRegistrosSeleccionadas");
				if (idsRegistrosSeleccionadas != null
						&& !idsRegistrosSeleccionadas.isEmpty()) {
					registrosSeleccionados = new ArrayList<Registro>();

					for (Entry<Integer, String> registrosId : idsRegistrosSeleccionadas
							.entrySet()) {
						if (registrosId.getValue().equals("true")) {
							registrosSeleccionados.add((Registro) serviceDao
									.getGenericCommonDao().read(Registro.class,
											registrosId.getKey()));
						}
					}

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ServiceDao getServiceDao() {
		return serviceDao;
	}

	public void setServiceDao(ServiceDao serviceDao) {
		this.serviceDao = serviceDao;
	}

	public Registro getRegistroSeleccionado() {
		return registroSeleccionado;
	}

	public void setRegistroSeleccionado(Registro registroSeleccionado) {
		this.registroSeleccionado = registroSeleccionado;
	}

	public String getRegNo1() {
		return regNo1;
	}

	public Integer getRegNo2() {
		return regNo2;
	}

	public Integer getRegNo3() {
		return regNo3;
	}

	public Date getRegFec() {
		return regFec;
	}

	public void setRegNo1(String regNo1) {
		this.regNo1 = regNo1;
	}

	public void setRegNo2(Integer regNo2) {
		this.regNo2 = regNo2;
	}

	public void setRegNo3(Integer regNo3) {
		this.regNo3 = regNo3;
	}

	public void setRegFec(Date regFec) {
		this.regFec = regFec;
	}

	public Integer getIdRegSeleccionado() {
		return idRegSeleccionado;
	}

	public void setIdRegSeleccionado(Integer idRegSeleccionado) {
		this.idRegSeleccionado = idRegSeleccionado;
	}

	public String getObservacion() {
		return observacion;
	}

	public Map<Integer, String> getIdsRegistrosSeleccionadas() {
		return idsRegistrosSeleccionadas;
	}

	public List<Registro> getRegistrosSeleccionados() {
		return registrosSeleccionados;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public void setIdsRegistrosSeleccionadas(
			Map<Integer, String> idsRegistrosSeleccionadas) {
		this.idsRegistrosSeleccionadas = idsRegistrosSeleccionadas;
	}

	public void setRegistrosSeleccionados(List<Registro> registrosSeleccionados) {
		this.registrosSeleccionados = registrosSeleccionados;
	}
}
