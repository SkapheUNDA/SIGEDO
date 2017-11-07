package la.netco.expedientes.dao.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import la.netco.commons.exceptions.NumeroRadicacionException;
import la.netco.core.businesslogic.services.ServiceDao;
import la.netco.core.persitencia.dao.commos.GenericCommonDaoHibernateImpl;
import la.netco.correspondencia.dto.custom.Consecutivo;
import la.netco.expedientes.dao.ExpedienteDao;
import la.netco.expedientes.utils.ExpedientesConstants;
import la.netco.persistencia.dto.commons.Claseregistro;
import la.netco.persistencia.dto.commons.Configuracion;
import la.netco.persistencia.dto.commons.ConstantsKeysFire;
import la.netco.persistencia.dto.commons.Entrada;
import la.netco.persistencia.dto.commons.Estado;
import la.netco.persistencia.dto.commons.Evento;
import la.netco.persistencia.dto.commons.Expediente;
import la.netco.persistencia.dto.commons.ExpedienteEstado;
import la.netco.persistencia.dto.commons.Expedientecorrespond;
import la.netco.persistencia.dto.commons.Lugar;
import la.netco.persistencia.dto.commons.NoDocumentados;
import la.netco.persistencia.dto.commons.Persona;
import la.netco.persistencia.dto.commons.Registro;
import la.netco.persistencia.dto.commons.Salida;
import la.netco.persistencia.dto.commons.Sector;
import la.netco.persistencia.dto.commons.Tiposexpediente;
import la.netco.persistencia.dto.commons.Tramite;
import la.netco.persistencia.dto.commons.Transicion;
import la.netco.persistencia.dto.commons.Ubicacion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author cpineros
 * 
 *         para el uso de constantes se usa la clase
 *         {@link ExpedientesConstants}
 * 
 */
@Service("expedienteDao")
public class ExpedienteDaoImpl extends GenericCommonDaoHibernateImpl implements
		ExpedienteDao {

	@Autowired
	@Resource(name = "serviceDao")
	private transient ServiceDao daoServicio;
	Integer consecutivoNum = null;
	String  consecutivoNumStr = null;

	public ServiceDao getDaoServicio() {
		return daoServicio;
	}

	public void setDaoServicio(ServiceDao daoServicio) {
		this.daoServicio = daoServicio;
	}

	public Consecutivo obtenerConsecutivoExp(String depCod)
			throws NumeroRadicacionException, Exception {
		Consecutivo consecutivo = new Consecutivo();
		String consecutivoCompleto;

		Configuracion conf = (Configuracion) executeFind(
				Configuracion.NAMED_QUERY_GET_ALL_CONF).get(0);

		String prefijo = conf.getExpPre();

		consecutivoCompleto = prefijo;
		if (Integer.parseInt(prefijo) > 0) {
			consecutivoCompleto = consecutivoCompleto + "-";
		}

		Integer opcionConf = conf.getExpOpc();
		Integer consecutivoNum = null;

		if (opcionConf.equals(ExpedientesConstants.EXP_OPC_CONSECUTIVO)) {
			// Aplica para cambio de años
						if(consecutivoNum == null){
							consecutivoNum = 0;
						}
			consecutivoNum = (Integer) executeUniqueResult(
					Expediente.NAMED_QUERY_GET_EXP_MAX_ALL, null);
			consecutivoCompleto = consecutivoCompleto + (consecutivoNum + 1);

		} else if (opcionConf
				.equals(ExpedientesConstants.EXP_OPC_ANIO_CONSECUTIVO)) {
			Map<Integer, Object> paramsZero = new HashMap<Integer, Object>();
			int year = Calendar.getInstance().get(Calendar.YEAR);
			paramsZero.put(0, year);
			
			if (consecutivoNum == null) {
				consecutivoNum = 0;
			}
			
			consecutivoNum = (Integer) executeUniqueResult(
					Expediente.NAMED_QUERY_GET_EXP_MAX_BY_YEAR, paramsZero);
			consecutivoCompleto = consecutivoCompleto + year + "-"
					+ (consecutivoNum + 1);
		} else if (opcionConf
				.equals(ExpedientesConstants.EXP_OPC_DEPEND_CONSECUTIVO)) {
			Map<Integer, Object> paramsThree = new HashMap<Integer, Object>();
			paramsThree.put(0, depCod);
			// Aplica para cambio de años
			if(consecutivoNum == null){
				consecutivoNum = 0;
			}
			consecutivoNum = (Integer) executeUniqueResult(
					Expediente.NAMED_QUERY_GET_EXP_MAX_BY_COD_DEPEND,
					paramsThree);
			consecutivoCompleto = consecutivoCompleto + depCod + "-"
					+ (consecutivoNum + 1);
		} else if (opcionConf
				.equals(ExpedientesConstants.EXP_OPC_DEPEND_ANIO_CONSECUTIVO)) {
			int year = Calendar.getInstance().get(Calendar.YEAR);
			Map<Integer, Object> paramsTwo = new HashMap<Integer, Object>();
			paramsTwo.put(0, depCod);
			paramsTwo.put(1, year);
			consecutivoNum = (Integer) executeUniqueResult(
					Expediente.NAMED_QUERY_GET_EXP_MAX_BY_COD_DEPEN_YEAR,
					paramsTwo);
			// Aplica para cambio de años
			if(consecutivoNum == null){
				consecutivoNum = 0;
			}
			consecutivoCompleto = consecutivoCompleto + depCod + "-" + year
					+ "-" + (consecutivoNum + 1);
		} else if (opcionConf
				.equals(ExpedientesConstants.EXP_OPC__ANIO_DEPEND_CONSECUTIVO)) {
			int year = Calendar.getInstance().get(Calendar.YEAR);
			Map<Integer, Object> paramsFour = new HashMap<Integer, Object>();
			paramsFour.put(0, depCod);
			paramsFour.put(1, year);
			// Aplica para cambio de años
			if(consecutivoNum == null){
				consecutivoNum = 0;
			}
			consecutivoNum = (Integer) executeUniqueResult(
					Expediente.NAMED_QUERY_GET_EXP_MAX_BY_COD_DEPEN_YEAR,
					paramsFour);
			consecutivoCompleto = consecutivoCompleto + year + "-" + depCod
					+ "-" + (consecutivoNum + 1);
		}
		//no aplica consecutivoNum.equals(0) 	
		if (consecutivoNum == null ) {
			throw new NumeroRadicacionException("ERROR AL GENERAR CONSECUTIVO");
		}

		consecutivo.setConsecutivo((consecutivoNum + 1));
		consecutivo.setConsecutivoCompleto(consecutivoCompleto);

		return consecutivo;
	}

	@SuppressWarnings("unchecked")
	public Integer crearExpedienteEsp(Entrada objEntrada,
			Claseregistro claseRegistro) throws NumeroRadicacionException,
			Exception {
		Integer num_registro = 0;
		Expediente nuevoExpediente = new Expediente();
		HashMap<Integer, Object> paramsEve = new HashMap<Integer, Object>();
		HashMap<Integer, Object> paramsTrs = new HashMap<Integer, Object>();
		Short TrsFinal = null, medioAuxi = null;
		Integer variUbicacion = null;

		Consecutivo consecutivoCapt = obtenerConsecutivoExp(objEntrada
				.getDependencia().getDepCod().trim());

		// Obteniendo Id de Evento por tramite de expediente
		paramsEve
				.put(0, Short.parseShort(ExpedientesConstants.VAL_TRM_EXP_ENT));
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
			TrsFinal = transicion.getEstadoByTrsEstfin().getEstId();
		}

		if (TrsFinal == null || TrsFinal.equals(0)) {
			TrsFinal = Short.parseShort(ExpedientesConstants.VAL_EST_INI);
		}

		System.out.println("***Creando expediente desde entrada***");
		nuevoExpediente.setDepend(objEntrada.getDependencia());

		nuevoExpediente.setTramite((Tramite) read(Tramite.class,
				Short.parseShort(ExpedientesConstants.VAL_TRM_EXP_ENT)));

		nuevoExpediente.setPersona(validaPersona(objEntrada));

		nuevoExpediente.setCalidadrepresentante(objEntrada
				.getCalidadRepresentante());

		nuevoExpediente.setExpNom(objEntrada.getEntAsu());

		nuevoExpediente.setExpCod(consecutivoCapt.getConsecutivoCompleto());

		nuevoExpediente.setTiposexpediente((Tiposexpediente) read(
				Tiposexpediente.class,
				Byte.parseByte(ExpedientesConstants.TIPOEXP_CR)));

		nuevoExpediente.setExpFso(objEntrada.getEntFen());

		nuevoExpediente.setUsuarioTemp(objEntrada.getUsuario());

		nuevoExpediente.setEstado((Estado) read(Estado.class, TrsFinal));

		nuevoExpediente.setExpCdep(objEntrada.getEntCdep());

		nuevoExpediente.setExpCano(objEntrada.getEntCano());

		nuevoExpediente.setExpCnro(consecutivoCapt.getConsecutivo());

		nuevoExpediente.setTipospersona(objEntrada.getTipoPersona());

		nuevoExpediente.setEstadoGeneral((ExpedienteEstado) daoServicio
				.getGenericCommonDao().read(ExpedienteEstado.class,
						ExpedienteEstado.TRAMITE));

		medioAuxi = objEntrada.getMedio().getMedId();

		if (medioAuxi == 17) {
			variUbicacion = Integer
					.parseInt(ExpedientesConstants.VAL_EXP_UBIUNO);
		} else if (medioAuxi != 17) {
			variUbicacion = Integer
					.parseInt(ExpedientesConstants.VAL_EXP_UBIDOS);
		}

		nuevoExpediente.setUbicacion((Ubicacion) read(Ubicacion.class,
				variUbicacion));

		Integer idExp = (Integer) create(Expediente.class, nuevoExpediente);
		System.out.println("***Terminado: expediente desde entrada***Exp No: "
				+ idExp);

		daoServicio.getSeguimientosDao().addSeguimientoExp(nuevoExpediente,
				ConstantsKeysFire.CREACIONEXP, null);
		num_registro = crearRegistroExp(nuevoExpediente, claseRegistro);

		crearExpedienteCorrespond(nuevoExpediente, objEntrada, null,
				Byte.parseByte(ExpedientesConstants.TIPOCORRESP_EXP),
				Boolean.parseBoolean(ExpedientesConstants.CORRESP_INI_EXP));
		return num_registro;

	}

	public Integer crearRegistroExp(Expediente objExpediente,
			Claseregistro objClaseRegistro) {
		Integer num_registro = 0;
		System.out.println("***Comenzando Registro para entrada***");
		Registro nuevoRegistro = new Registro();
		String tituloTOR, objetoContratoOBJ;

		if (objClaseRegistro.getCreFrm() == 2) {
			tituloTOR = "";
			objetoContratoOBJ = objExpediente.getExpNom();
		} else {
			tituloTOR = objExpediente.getExpNom();
			objetoContratoOBJ = "";
		}

		nuevoRegistro.setExpediente(objExpediente);
		nuevoRegistro.setClaseregistro(objClaseRegistro);
		nuevoRegistro.setRegTor(tituloTOR);
		nuevoRegistro.setRegFecReal(new Date(System.currentTimeMillis()));
		nuevoRegistro.setRegObj(objetoContratoOBJ);

		create(Registro.class, nuevoRegistro);
		num_registro = nuevoRegistro.getRegId();
		System.out.println("***Registro: ***" +nuevoRegistro.getRegId());
		System.out.println("***Terminado: Registro para entrada***");
		return num_registro;
	}

	public void crearExpedienteCorrespond(Expediente objExpediente,
			Entrada objEntrada, Salida objSalida, Byte tipCorrespond,
			Boolean correspondIni) {

		System.out
				.println("***Comenzando ExpedienteCorrespond  para entrada***");

		String descripEntrada = null, descripSalida = null; // *** Tener en
															// cuenta
		Expedientecorrespond nuevoExpedientecorrespond = new Expedientecorrespond();

		if (tipCorrespond == 1) {
			descripEntrada = objEntrada.getEntNen();
		} else {
			descripSalida = objSalida.getSalNsa();
		}

		nuevoExpedientecorrespond.setExpediente(objExpediente);
		nuevoExpedientecorrespond.setExcTip(tipCorrespond);
		nuevoExpedientecorrespond.setEntrada(objEntrada);
		nuevoExpedientecorrespond.setSalida(objSalida);
		nuevoExpedientecorrespond.setExcCin(correspondIni);

		create(Expedientecorrespond.class, nuevoExpedientecorrespond);

		System.out
				.println("***Terminado: ExpedienteCorrespond  para entrada***");

	}

	@SuppressWarnings("unchecked")
	public Persona validaPersona(Entrada objEntrada) {
		System.out.println("***Validando Persona***");
		HashMap<Integer, Object> parametrosBusq = new HashMap<Integer, Object>();
		parametrosBusq.put(0, objEntrada.getEntNdo());
		parametrosBusq.put(1, objEntrada.getTiposdocumento().getTdoId());
		System.out.println("***Validando Persona objEntrada.getEntNdo  ***" + objEntrada.getEntNdo());
		System.out.println("***Validando Persona  obeEntrada TipoDocumento ***" + objEntrada.getTiposdocumento().getTdoId());

		Persona objPersona = new Persona();

		List<Persona> listaPersona = (List<Persona>) executeFind(Persona.class,
				parametrosBusq,
				Persona.NAMEQUERY_SELECT_BY_NODOCUMENTO_TIPODOCUMENTO);

		if (listaPersona.size() == 0) {
			System.out.println("***Creando persona no existente***");
			// valida NO Documentados
			if(objEntrada.getTiposdocumento().getTdoNom().equals("NO DOCUMENTADO")){
				String prefijo = "NN-";
				String consecutivoCompleto;
				HashMap<Integer, Object> parametrosBusqNN = new HashMap<Integer, Object>();
				parametrosBusqNN.put(0, objEntrada.getTiposdocumento().getTdoId());
				consecutivoNumStr = (String) executeUniqueResult(Persona.NAMED_QUERY_GET_MAX_ALL, parametrosBusqNN);
				if(consecutivoNumStr != null){
				String[] parts = consecutivoNumStr.split("-");
				String part2 = parts[1]; 
				consecutivoNum = Integer.parseInt(part2);
				} 
				if(consecutivoNumStr ==null)
				{
					consecutivoNum = 0;
					consecutivoNum = consecutivoNum + 1;
				}
				else
				{
					consecutivoNum = consecutivoNum + 1;
				}
				consecutivoCompleto = prefijo + consecutivoNum;
				objPersona.setPerDoc(consecutivoCompleto);
			
			} else{
				objPersona.setPerDoc(objEntrada.getEntNdo());
			}
			objPersona.setTiposdocumento(objEntrada.getTiposdocumento());
			objPersona.setPerNom(objEntrada.getEntNom());
			objPersona.setPerPap(objEntrada.getEntPap());
			objPersona.setPerSap(objEntrada.getEntSap());
			objPersona.setPerLviv(ConstantsKeysFire.PERVIV);
			objPersona.setPaises(objEntrada.getPais());
			objPersona.setLugarByPerSeulug((Lugar) read(Lugar.class,
					ConstantsKeysFire.PERSEULUG));
			objPersona.setLugarByPerLug(objEntrada.getLugar());
			objPersona.setPerDir(objEntrada.getEntDir());
			objPersona.setPerFax(objEntrada.getEntFax());
			objPersona.setPerTl1(objEntrada.getEntTel());
			objPersona.setPerCe1(objEntrada.getEntCel());
			objPersona.setSector((Sector) read(Sector.class,
					ConstantsKeysFire.PERSEC));
			objPersona.setPerLses(ConstantsKeysFire.BOOLFECHNAC);
			Integer idPersona = (Integer) create(Persona.class, objPersona);
			objPersona.setPerId(idPersona);
			return objPersona;
		} else if (listaPersona.size() != 0) {
			System.out.println("***Obteniendo persona existente");
			System.out.println("***ID" + listaPersona.get(0).getPerId());
			System.out.println("***DOC" + listaPersona.get(0).getPerDoc());
			objPersona.setPerId((listaPersona.get(0).getPerId()));
			objPersona.setPerDoc(listaPersona.get(0).getPerDoc());
			objPersona.setTiposdocumento(listaPersona.get(0)
					.getTiposdocumento());
			objPersona.setPerNom(listaPersona.get(0).getPerNom());
			objPersona.setPerPap(listaPersona.get(0).getPerPap());
			objPersona.setPerSap(listaPersona.get(0).getPerSap());
			objPersona.setPerLviv(ConstantsKeysFire.PERVIV);
			objPersona.setPaises(listaPersona.get(0).getPaises());
			objPersona.setLugarByPerSeulug((Lugar) read(Lugar.class,
					ConstantsKeysFire.PERSEULUG));
			objPersona.setLugarByPerLug(listaPersona.get(0).getLugarByPerLug());
			objPersona.setPerDir(listaPersona.get(0).getPerDir());
			objPersona.setPerFax(listaPersona.get(0).getPerFax());
			objPersona.setPerTl1(listaPersona.get(0).getPerTl1());
			objPersona.setPerCe1(listaPersona.get(0).getPerCe1());
			objPersona.setSector((Sector) read(Sector.class,
					ConstantsKeysFire.PERSEC));
			objPersona.setPerLses(ConstantsKeysFire.BOOLFECHNAC);
			return objPersona;
		}
		return objPersona;

	}

	public void eliminarExpediente(Expediente objExpediente) {
	}

}
