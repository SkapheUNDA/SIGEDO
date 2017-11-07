package la.netco.registro.uilayer.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import la.netco.core.businesslogic.services.ServiceDao;
import la.netco.core.persistencia.vo.Usuario;
import la.netco.core.spring.BeansSpringConst;
import la.netco.core.spring.security.UserDetailsUtils;
import la.netco.core.uilayer.beans.Constants;
import la.netco.core.uilayer.beans.utils.FacesUtils;
import la.netco.correspondencia.dto.custom.Consecutivo;
import la.netco.persistencia.dto.commons.Clasificacion;
import la.netco.persistencia.dto.commons.ConstantsKeysFire;
import la.netco.persistencia.dto.commons.Enlace;
import la.netco.persistencia.dto.commons.EnlaceId;
import la.netco.persistencia.dto.commons.Entrada;
import la.netco.persistencia.dto.commons.Estado;
import la.netco.persistencia.dto.commons.Expediente;
import la.netco.persistencia.dto.commons.ExpedienteEstado;
import la.netco.persistencia.dto.commons.Medioscorrespondencia;
import la.netco.persistencia.dto.commons.Registro;
import la.netco.persistencia.dto.commons.Salida;
import la.netco.persistencia.dto.commons.Seguimientoentrada;
import la.netco.persistencia.dto.commons.Seguimientoexpediente;
import la.netco.persistencia.dto.commons.Tiposevento;
import la.netco.persistencia.dto.commons.Transicion;

@ManagedBean
@ViewScoped
public class GeneraSalidaRegBean  implements Serializable{

	
	private static final long serialVersionUID = 1L;
	
	private static String CREACION_SALIDA ="CREACION DE SALIDA";
	/**
	 * Respuesta verdadera de una entrada a una salida que se creara para un registro 
	 * en el listado de impresion
	 */
	private static boolean RESPUESTA_ENT = true;
	
	private Registro registroSeleccionado;
	
	@ManagedProperty(value = BeansSpringConst.BEAN_ID_SERVICE_DAO)
	transient private ServiceDao serviceDao;
	
	
	@SuppressWarnings("unchecked")
	public void generaSalidaRegistro(Integer regId) {
		try {
			
			Salida nuevaSalida =  new Salida();
			Registro registro = (Registro) serviceDao.getGenericCommonDao().read(Registro.class, regId);
			Expediente expediente = registro.getExpediente();
			Entrada entrada =expediente.getExpedientecorresponds().iterator().next().getEntrada();
			Usuario usuario = UserDetailsUtils.usuarioLogged();
			Usuario usuarioREL = (Usuario) serviceDao.getGenericCommonDao().read(Usuario.class, Usuario.SOLICITUD_REL);
			
			Map<Integer, Object> params = new HashMap<Integer, Object>();
			params.put(0, expediente.getExpId());
			List<Seguimientoexpediente> seguimientos = (List<Seguimientoexpediente>)serviceDao.getEntradaDao().executeFindPaged(0,1, params, Seguimientoexpediente.NAMED_QUERY_GET_BY_EXPEDIENTE);
			Seguimientoexpediente seguimiento = seguimientos.get(0);
			Transicion transicion = new Transicion();
			transicion.setEstadoByTrsEstfin(seguimiento.getEstadoBySgeEst());
			
			
			//**Actualizando a Impreso , El expediente y su seguimiento del Registro seleccionado
			
			registro.setUsuario(usuario);
			serviceDao.getGenericCommonDao().update(Registro.class, registro);
			
			
			ExpedienteEstado estadoGeneral = (ExpedienteEstado)serviceDao.getGenericCommonDao().read(ExpedienteEstado.class, ExpedienteEstado.REGISTRADO);
			expediente.setEstadoGeneral(estadoGeneral);
			expediente.setUsuarioTemp(usuario);
			serviceDao.getGenericCommonDao().update(Expediente.class, expediente);
			serviceDao.getSeguimientosDao().addSeguimientoExp(expediente, CREACION_SALIDA, "", usuario,usuario, transicion);
			
			//**Actualizando Entrada y su seguimiento del registro seleccionado
			entrada.setUsuario(usuario);
			serviceDao.getGenericCommonDao().update(Entrada.class, entrada);
			serviceDao.getSeguimientosDao().addSeguimientoEntrada(entrada, transicion, CREACION_SALIDA, "", usuario, usuario);
					
			registroSeleccionado =  registro;
			
			
			//**Creacion de Salida
			
			Map<Integer, Object> paramsBusq = new HashMap<Integer, Object>();
			paramsBusq.put(0,  Clasificacion.EXP_CER_REGISTRO);
			paramsBusq.put(1, Tiposevento.ETAPA_INICIAL);
			Short estadoId = null;
			List<Transicion> transicions = (List<Transicion>) serviceDao.getEntradaDao().executeFind(Transicion.class, paramsBusq, Transicion.NAMED_QUERY_BY_CLASIFICACION_TIPO_EVENTO);
			Transicion transicionSal = null;
			if(transicions != null && !transicions.isEmpty()){			
				transicionSal = transicions.get(0);					
				estadoId = transicionSal.getId().getTrsEstfin();				
			}
			Estado estadoFinal = (Estado) serviceDao.getGenericCommonDao().read(Estado.class, estadoId);
			
			nuevaSalida.setSalFsa(new Date(System.currentTimeMillis()));
			nuevaSalida.setSalAsu(entrada.getEntAsu());
			nuevaSalida.setSalLren(RESPUESTA_ENT);
			nuevaSalida.setMedio((Medioscorrespondencia) serviceDao.getGenericCommonDao().read(Medioscorrespondencia.class, Medioscorrespondencia.MEDIO_SERVIENTREGA));
			nuevaSalida.setTipospersona(entrada.getTipoPersona());
			nuevaSalida.setTiposdocumento(entrada.getTiposdocumento());
			nuevaSalida.setSalNdo(entrada.getEntNdo());
			nuevaSalida.setSalNom(entrada.getEntNom());
			nuevaSalida.setSalPap(entrada.getEntPap());
			nuevaSalida.setSalSap(entrada.getEntSap());
			nuevaSalida.setSalCar(entrada.getEntCar());
			nuevaSalida.setSalDir(entrada.getEntDir());
			nuevaSalida.setLugar(entrada.getLugar());
			nuevaSalida.setClasificacion((Clasificacion) serviceDao.getGenericCommonDao().read(Clasificacion.class, Clasificacion.EXP_CER_REGISTRO));
			nuevaSalida.setDepend(entrada.getDependencia());
			nuevaSalida.setSalTel(entrada.getEntTel());
			nuevaSalida.setSalFax(entrada.getEntFax());
			nuevaSalida.setSalCel(entrada.getEntCel());
			nuevaSalida.setUsuarioBySalUsr(usuario);
			nuevaSalida.setSalFol(ConstantsKeysFire.FOLIOS_SALIDA_DEF);
			nuevaSalida.setSalCano(Calendar.getInstance().get(Calendar.YEAR));
			nuevaSalida.setEstado(estadoFinal);
			if (entrada.getMedio().getMedId() == Short.parseShort(Medioscorrespondencia.MEDIO_REGISTRO_LINEA)) {
				nuevaSalida.setSalLent(ConstantsKeysFire.ENTREGADA);
				nuevaSalida.setUsuarioBySalUen(usuario);
				nuevaSalida.setSalPen(ConstantsKeysFire.USUARIO_PEN);
				nuevaSalida.setSalFen(new Date(System.currentTimeMillis()));
				nuevaSalida.setSalHen(new Date(System.currentTimeMillis()));
				nuevaSalida.setSalEnm(Integer.parseInt(Medioscorrespondencia.MEDIO_REGISTRO_LINEA));
			}
		
			Consecutivo numCorrespondencia = serviceDao.getEntradaDao().obtenerConsecutivo(entrada.getDependencia().getDepCod(), Consecutivo.TIPO_SALIDA);
			nuevaSalida.setSalCnro(numCorrespondencia.getConsecutivo());
			nuevaSalida.setSalNsa(numCorrespondencia.getConsecutivoCompleto());		
			Integer  idSalida = (Integer) serviceDao.getGenericCommonDao().create(Salida.class, nuevaSalida);

			Salida salidaReg = (Salida)serviceDao.getGenericCommonDao().read(Salida.class, idSalida);
			serviceDao.getSeguimientosDao().addSeguimientoSalida(salidaReg, transicionSal, Seguimientoentrada.SEGUIMIENTO_CREACION, "",usuarioREL ,usuario);
			
			if (entrada.getMedio().getMedId() == Short.parseShort(Medioscorrespondencia.MEDIO_REGISTRO_LINEA))
			{
				serviceDao.getSeguimientosDao().addSeguimientoSalida(salidaReg, transicionSal,ConstantsKeysFire.ENTREGADO_OBS,salidaReg.getSalPen(),usuarioREL , usuario);
			}
			
			System.out.println("***Exito-Salida No: " + idSalida);
			
			//**Creando Enlace
			
			Enlace nuevoEnlace =  new Enlace();
			EnlaceId nuevoEnlaceId =  new EnlaceId();
			nuevoEnlaceId.setEnlEnt(entrada.getEntId());
			nuevoEnlaceId.setEnlSal(salidaReg.getSalId());
			nuevoEnlaceId.setEnlOrg(Enlace.ORIGEN_SALIDA);
			nuevoEnlace.setId(nuevoEnlaceId);
			nuevoEnlace.setEntrada(entrada);
			nuevoEnlace.setSalida(salidaReg);
			serviceDao.getGenericCommonDao().create(Enlace.class, nuevoEnlace);
			
			FacesUtils.addFacesMessageFromBundle(Constants.OPERACION_EXITOSA_ACTUALIZAR_REGISTRO,	FacesMessage.SEVERITY_INFO);
	

		} catch (Exception e) {
			System.out.println("Error: " + e.toString());
			System.out.println("Linea:" +  e.getLocalizedMessage());
			System.out.println("Mensaje: " +  e.getMessage());
			e.printStackTrace();
			
		}
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
	 * @return the registroSeleccionado
	 */
	public Registro getRegistroSeleccionado() {
		return registroSeleccionado;
	}


	/**
	 * @param registroSeleccionado the registroSeleccionado to set
	 */
	public void setRegistroSeleccionado(Registro registroSeleccionado) {
		this.registroSeleccionado = registroSeleccionado;
	}
	
	

}
