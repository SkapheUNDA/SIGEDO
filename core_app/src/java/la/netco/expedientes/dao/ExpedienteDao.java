package la.netco.expedientes.dao;

import la.netco.core.persitencia.dao.commos.GenericCommonDao;
import la.netco.correspondencia.dto.custom.Consecutivo;
import la.netco.persistencia.dto.commons.Claseregistro;
import la.netco.persistencia.dto.commons.Entrada;
import la.netco.persistencia.dto.commons.Expediente;
import la.netco.persistencia.dto.commons.Persona;
import la.netco.persistencia.dto.commons.Salida;

public interface ExpedienteDao extends GenericCommonDao {

	public Consecutivo obtenerConsecutivoExp(String depCod) throws Exception;

	public Integer crearExpedienteEsp(Entrada objEntrada, Claseregistro claseRegistro) throws Exception;

	public Integer crearRegistroExp(Expediente objExpediente, Claseregistro objClaseRegistro);

	public void crearExpedienteCorrespond(Expediente objExpediente,
			Entrada objEntrada, Salida objSalida, Byte tipCorrespond,
			Boolean correspondIni);

	public Persona validaPersona(Entrada objEntrada);

	public void eliminarExpediente(Expediente objExpediente);
	
}
