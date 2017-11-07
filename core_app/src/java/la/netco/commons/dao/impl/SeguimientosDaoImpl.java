package la.netco.commons.dao.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import la.netco.commons.dao.SeguimientosDao;
import la.netco.core.persistencia.vo.Usuario;
import la.netco.core.persitencia.dao.commos.GenericCommonDaoHibernateImpl;
import la.netco.core.spring.security.UserDetailsUtils;
import la.netco.expedientes.utils.ExpedientesConstants;
import la.netco.persistencia.dto.commons.Configuracion;
import la.netco.persistencia.dto.commons.Entrada;
import la.netco.persistencia.dto.commons.Estado;
import la.netco.persistencia.dto.commons.Evento;
import la.netco.persistencia.dto.commons.Expediente;
import la.netco.persistencia.dto.commons.Salida;
import la.netco.persistencia.dto.commons.Seguimientoentrada;
import la.netco.persistencia.dto.commons.Seguimientoexpediente;
import la.netco.persistencia.dto.commons.Seguimientosalida;
import la.netco.persistencia.dto.commons.Transicion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("seguimientosDao")
public class SeguimientosDaoImpl extends GenericCommonDaoHibernateImpl
		implements SeguimientosDao {

	private static Logger log = LoggerFactory
			.getLogger(SeguimientosDaoImpl.class);

	@SuppressWarnings("unchecked")
	public void addSeguimientoExp(Expediente objExpediente, String Descrip,
			String Observacion) {
		Seguimientoexpediente nuevoSegExpediente = new Seguimientoexpediente();
		HashMap<Integer, Object> paramsEve = new HashMap<Integer, Object>();
		HashMap<Integer, Object> paramsTrs = new HashMap<Integer, Object>();
		Date FechaOrig = objExpediente.getExpFso();
		Short TrsInicial = null;
		Configuracion conf = (Configuracion) executeFind(
				Configuracion.NAMED_QUERY_GET_ALL_CONF).get(0);
		boolean sgeManual = conf.getSegManualExp();

		// Calculando Fecha de seguimiento ORIGINAL
		if (sgeManual == false) {
			FechaOrig = Calendar.getInstance().getTime();
		} else {
			if (FechaOrig == null) {
				FechaOrig = Calendar.getInstance().getTime();
			}
		}

		// Obteniendo Id de Evento por tramite de expediente
		paramsEve.put(0, objExpediente.getTramite().getTrmId());
		paramsEve
				.put(1, Byte.parseByte(ExpedientesConstants.TIPOVENTO_INICIAL));

		Short eventoID = (Short) executeUniqueResult(
				Evento.NAMED_QUERY_GET_EVEID, paramsEve);

		// Obteniendo Transiciones de Estado inicial y Final
		paramsTrs.put(0, eventoID);

		List<Transicion> transicionLista = (List<Transicion>) executeFind(
				Transicion.class, paramsTrs,
				Transicion.NAMED_QUERY_GET_ALL_BY_EVEID);

		for (Transicion transicion : transicionLista) {
			TrsInicial = transicion.getEstadoByTrsEstini().getEstId();
		}

		System.out.println("***Creando seguimiento de expediente");

		// Estableciendo Expediente
		nuevoSegExpediente.setExpediente(objExpediente);

		// Estableciendo Dependencia de expediente
		nuevoSegExpediente.setSgeDep(objExpediente.getDepend().getDepId());

		// Estableciendo usuario inicial - Cargar usuario logeado
		nuevoSegExpediente.setUsuario(UserDetailsUtils.usuarioLogged());

		// Estableciendo Usuario
		nuevoSegExpediente.setSgeUsr((Usuario) read(Usuario.class,
				objExpediente.getUsuarioTemp().getUsrId()));

		// Estableciendo Estado
		nuevoSegExpediente.setEstadoBySgeEst((Estado) read(Estado.class,
				objExpediente.getEstado().getEstId()));

		// Estableciendo Fecha
		nuevoSegExpediente.setSgeFec(Calendar.getInstance().getTime());

		// Estableciendo la Descripcion
		nuevoSegExpediente.setSgeDes(Descrip);

		// Estableciendo la observacion
		nuevoSegExpediente.setSgeObs(Observacion);

		// Estableciendo Estado inicial
		nuevoSegExpediente.setEstadoBySgeEstini((Estado) read(Estado.class,
				TrsInicial));

		// Estableciendo Evento
		nuevoSegExpediente.setEvento((Evento) read(Evento.class, eventoID));

		// Estableciendo Fecha de seguimiento ORIGINAL
		nuevoSegExpediente.setSgeFecorg(FechaOrig);

		// Creando seguimiento de expediente
		create(Seguimientoexpediente.class, nuevoSegExpediente);

		System.out.println("***Seguimiento de expediente creado correctamente");
	}

	public void addSeguimientoExp(Expediente objExpediente, String Descrip,
			String Observacion, Usuario usuLogeado, Usuario usuFinal,
			Transicion transicion) {
		Seguimientoexpediente nuevoSegExpediente = new Seguimientoexpediente();
		Date FechaOrig = objExpediente.getExpFso();
		Configuracion conf = (Configuracion) executeFind(
				Configuracion.NAMED_QUERY_GET_ALL_CONF).get(0);
		boolean sgeManual = conf.getSegManualExp();

		// Calculando Fecha de seguimiento ORIGINAL
		if (sgeManual == false) {
			FechaOrig = Calendar.getInstance().getTime();
		} else {
			if (FechaOrig == null) {
				FechaOrig = Calendar.getInstance().getTime();
			}
		}

		System.out.println("***Creando seguimiento de expediente");

		// Estableciendo Expediente
		nuevoSegExpediente.setExpediente(objExpediente);

		// Estableciendo Dependencia de expediente
		nuevoSegExpediente.setSgeDep(objExpediente.getDepend().getDepId());

		// Estableciendo usuario inicial - Cargar usuario logeado
		nuevoSegExpediente.setUsuario(usuLogeado);

		// Estableciendo Usuario
		nuevoSegExpediente.setSgeUsr(usuFinal);

		// Estableciendo Estado
		nuevoSegExpediente.setEstadoBySgeEst(transicion.getEstadoByTrsEstfin());

		// Estableciendo Fecha
		nuevoSegExpediente.setSgeFec(Calendar.getInstance().getTime());

		// Estableciendo la Descripcion
		nuevoSegExpediente.setSgeDes(Descrip);

		// Estableciendo la observacion
		nuevoSegExpediente.setSgeObs(Observacion);

		// Estableciendo Estado inicial
		nuevoSegExpediente.setEstadoBySgeEstini(transicion
				.getEstadoByTrsEstini());

		// Estableciendo Evento
		nuevoSegExpediente.setEvento(transicion.getEvento());

		// Estableciendo Fecha de seguimiento ORIGINAL
		nuevoSegExpediente.setSgeFecorg(FechaOrig);

		// Creando seguimiento de expediente
		create(Seguimientoexpediente.class, nuevoSegExpediente);

		System.out.println("***Seguimiento de expediente creado correctamente");
	}

	@SuppressWarnings("unchecked")
	public void addSeguimientoExpCambioEtapa(Expediente objExpediente,
			String Descrip, String Observacion, Short idEvento) {
		Seguimientoexpediente nuevoSegExpediente = new Seguimientoexpediente();
		HashMap<Integer, Object> paramsTrs = new HashMap<Integer, Object>();
		Date FechaOrig = objExpediente.getExpFso();
		Short TrsFinal = null;
		Configuracion conf = (Configuracion) executeFind(
				Configuracion.NAMED_QUERY_GET_ALL_CONF).get(0);
		boolean sgeManual = conf.getSegManualExp();

		// Calculando Fecha de seguimiento ORIGINAL
		if (sgeManual == false) {
			FechaOrig = Calendar.getInstance().getTime();
		} else {
			if (FechaOrig == null) {
				FechaOrig = Calendar.getInstance().getTime();
			}
		}

		// Obteniendo Transiciones Final
		paramsTrs.put(0, idEvento);
		paramsTrs.put(1, objExpediente.getEstado().getEstId());

		List<Transicion> transicionLista = (List<Transicion>) executeFind(
				Transicion.class, paramsTrs,
				Transicion.NAMED_QUERY_GET_ALL_BY_EVEIDTRSFIN);

		for (Transicion transicion : transicionLista) {
			TrsFinal = transicion.getEstadoByTrsEstfin().getEstId();
		}

		objExpediente.setEstado((Estado) read(Estado.class, TrsFinal));

		update(Expediente.class, objExpediente);

		System.out
				.println("***Creando seguimiento de expediente-CAMBIO DE ETAPA");

		// Estableciendo Expediente
		nuevoSegExpediente.setExpediente(objExpediente);

		// Estableciendo Dependencia de expediente
		nuevoSegExpediente.setSgeDep(objExpediente.getDepend().getDepId());

		// Estableciendo usuario inicial - Cargar usuario logeado
		nuevoSegExpediente.setUsuario(UserDetailsUtils.usuarioLogged());

		// Estableciendo Usuario
		nuevoSegExpediente.setSgeUsr((Usuario) read(Usuario.class,
				objExpediente.getUsuarioTemp().getUsrId()));

		// Estableciendo Estado
		nuevoSegExpediente.setEstadoBySgeEst((Estado) read(Estado.class,
				objExpediente.getEstado().getEstId()));

		// Estableciendo Fecha
		nuevoSegExpediente.setSgeFec(Calendar.getInstance().getTime());

		// Estableciendo la Descripcion
		nuevoSegExpediente.setSgeDes(Descrip);

		// Estableciendo la observacion
		nuevoSegExpediente.setSgeObs(Observacion);

		// Estableciendo Estado inicial
		nuevoSegExpediente.setEstadoBySgeEstini(objExpediente.getEstado());

		// Estableciendo Evento
		nuevoSegExpediente.setEvento((Evento) read(Evento.class, idEvento));

		// Estableciendo Fecha de seguimiento ORIGINAL
		nuevoSegExpediente.setSgeFecorg(FechaOrig);

		// Creando seguimiento de expediente
		create(Seguimientoexpediente.class, nuevoSegExpediente);

		System.out
				.println("***Seguimiento de expediente creado correctamente-CAMBIO DE ETAPA");
	}

	public void addBitacora(String modBitacora, String codInsercion) {

	}

	public void addSeguimientoEntrada(Entrada objEntrada,
			Transicion transicion, String Descrip, String Observacion,
			Usuario usuarioIni, Usuario usuarioFin) {

		try {

			Seguimientoentrada nuevoSegEntrada = new Seguimientoentrada();
			Date fechaOrig = objEntrada.getEntFen();
			Configuracion conf = (Configuracion) executeFind(
					Configuracion.NAMED_QUERY_GET_ALL_CONF).get(0);
			boolean sgeManual = conf.getSegManualExp();
			// Calculando Fecha de seguimiento ORIGINAL

			if (sgeManual == false) {
				fechaOrig = Calendar.getInstance().getTime();
			} else {
				if (fechaOrig == null) {
					fechaOrig = Calendar.getInstance().getTime();
				}
			}
			
			// Estableciendo Entrada
			nuevoSegEntrada.setEntrada(objEntrada);

			// Estableciendo Dependencia de expediente
			nuevoSegEntrada.setDepend(objEntrada.getDependencia());

			// Estableciendo usuario inicial - Cargar usuario logeado
			nuevoSegEntrada.setUsuarioBySenUsrini(usuarioIni);

			// Estableciendo Usuario final
			nuevoSegEntrada.setUsuarioBySenUsr(usuarioFin);

			// Estableciendo Estado Inicial
			nuevoSegEntrada.setEstadoBySenEstini(transicion
					.getEstadoByTrsEstini());

			// Estableciendo Estado Final
			nuevoSegEntrada
					.setEstadoBySenEst(transicion.getEstadoByTrsEstfin());

			// Estableciendo Fecha
			nuevoSegEntrada.setSenFec(Calendar.getInstance().getTime());

			// Estableciendo la Descripcion
			nuevoSegEntrada.setSenDes(Descrip);

			// Estableciendo la observacion
			nuevoSegEntrada.setSenObs(Observacion);

			// Estableciendo Evento
			nuevoSegEntrada.setEvento(transicion.getEvento());

			// Estableciendo Fecha de seguimiento ORIGINAL
			nuevoSegEntrada.setSenFecorg(fechaOrig);

			// Creando seguimiento
			create(Seguimientoentrada.class, nuevoSegEntrada);

		} catch (Exception e) {
			e.printStackTrace();
			log.error("ERROR CREANDO SEGUIMIENTO ENTRADA" + e.getMessage());
		}

	}

	public void addSeguimientoSalida(Salida objSalida, Transicion transicion,
			String Descrip, String Observacion, Usuario usuarioIni,
			Usuario usuarioFin) {

		try {

			Seguimientosalida nuevoSegSalida = new Seguimientosalida();
			Date fechaOrig = objSalida.getSalFsa();
			Configuracion conf = (Configuracion) executeFind(
					Configuracion.NAMED_QUERY_GET_ALL_CONF).get(0);
			boolean sgeManual = conf.getSegManualExp();
			// Calculando Fecha de seguimiento ORIGINAL

			if (sgeManual == false) {
				fechaOrig = Calendar.getInstance().getTime();
			} else {
				if (fechaOrig == null) {
					fechaOrig = Calendar.getInstance().getTime();
				}
			}

			// Estableciendo Salida
			nuevoSegSalida.setSalida(objSalida);

			// Estableciendo Dependencia de expediente
			nuevoSegSalida.setDepend(objSalida.getDepend());

			// Estableciendo usuario inicial - Cargar usuario logeado
			nuevoSegSalida.setUsuarioBySsaUsrini(usuarioIni);

			// Estableciendo Usuario final
			nuevoSegSalida.setUsuarioBySsaUsr(usuarioFin);

			// Estableciendo Estado Inicial
			nuevoSegSalida.setEstadoBySsaEstini(transicion
					.getEstadoByTrsEstini());

			// Estableciendo Estado Final
			nuevoSegSalida.setEstadoBySsaEst(transicion.getEstadoByTrsEstfin());

			// Estableciendo Fecha
			nuevoSegSalida.setSsaFec(Calendar.getInstance().getTime());

			// Estableciendo la Descripcion
			nuevoSegSalida.setSsaDes(Descrip);

			// Estableciendo la observacion
			nuevoSegSalida.setSsaObs(Observacion);

			// Estableciendo Evento
			nuevoSegSalida.setEvento(transicion.getEvento());

			// Estableciendo Fecha de seguimiento ORIGINAL
			nuevoSegSalida.setSsaFecorg(fechaOrig);

			// Creando seguimiento
			create(Seguimientosalida.class, nuevoSegSalida);

		} catch (Exception e) {
			e.printStackTrace();
			log.error("ERROR CREANDO SEGUIMIENTO SALIDA" + e.getMessage());
		}

	}
	
	public void addSeguimientoSalidaSinEve(Salida objSalida, Transicion transicion,	String Descrip, String Observacion, Usuario usuarioIni,
			Usuario usuarioFin) {

		try {

			Seguimientosalida nuevoSegSalida = new Seguimientosalida();
			Date fechaOrig = objSalida.getSalFsa();
			Configuracion conf = (Configuracion) executeFind(
					Configuracion.NAMED_QUERY_GET_ALL_CONF).get(0);
			boolean sgeManual = conf.getSegManualExp();
			// Calculando Fecha de seguimiento ORIGINAL

			if (sgeManual == false) {
				fechaOrig = Calendar.getInstance().getTime();
			} else {
				if (fechaOrig == null) {
					fechaOrig = Calendar.getInstance().getTime();
				}
			}

			// Estableciendo Salida
			nuevoSegSalida.setSalida(objSalida);

			// Estableciendo Dependencia de expediente
			nuevoSegSalida.setDepend(objSalida.getDepend());

			// Estableciendo usuario inicial - Cargar usuario logeado
			nuevoSegSalida.setUsuarioBySsaUsrini(usuarioIni);

			// Estableciendo Usuario final
			nuevoSegSalida.setUsuarioBySsaUsr(usuarioFin);

			// Estableciendo Fecha
			nuevoSegSalida.setSsaFec(Calendar.getInstance().getTime());

			// Estableciendo la Descripcion
			nuevoSegSalida.setSsaDes(Descrip);

			// Estableciendo la observacion
			nuevoSegSalida.setSsaObs(Observacion);

			// Estableciendo Fecha de seguimiento ORIGINAL
			nuevoSegSalida.setSsaFecorg(fechaOrig);

			// Creando seguimiento
			create(Seguimientosalida.class, nuevoSegSalida);
			
			// Estableciendo Estado Final
						nuevoSegSalida.setEstadoBySsaEst(transicion.getEstadoByTrsEstfin());

		} catch (Exception e) {
			e.printStackTrace();
			log.error("ERROR CREANDO SEGUIMIENTO SALIDA" + e.getMessage());
		}

	}

}
