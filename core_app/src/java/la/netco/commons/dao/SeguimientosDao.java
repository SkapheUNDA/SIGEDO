package la.netco.commons.dao;

import la.netco.core.persistencia.vo.Usuario;
import la.netco.core.persitencia.dao.commos.GenericCommonDao;
import la.netco.persistencia.dto.commons.Entrada;
import la.netco.persistencia.dto.commons.Expediente;
import la.netco.persistencia.dto.commons.Salida;
import la.netco.persistencia.dto.commons.Transicion;

public interface SeguimientosDao extends GenericCommonDao {

	public void addSeguimientoExp(Expediente objExpediente, String Descrip,
			String Observacion);

	public void addSeguimientoExp(Expediente objExpediente, String Descrip,
			String Observacion, Usuario usuLogeado, Usuario usuFinal,
			Transicion transicion);

	public void addBitacora(String modBitacora, String codInsercion);

	public void addSeguimientoEntrada(Entrada objEntrada,
			Transicion transicion, String Descrip, String Observacion,
			Usuario usuarioIni, Usuario usuarioFin);

	public void addSeguimientoExpCambioEtapa(Expediente objExpediente,
			String Descrip, String Observacion, Short idEvento);

	public void addSeguimientoSalida(Salida objSalida, Transicion transicion,
			String Descrip, String Observacion, Usuario usuarioIni,
			Usuario usuarioFin);

	public void addSeguimientoSalidaSinEve(Salida objSalida, Transicion transicion,String Descrip,
			String Observacion, Usuario usuarioIni, Usuario usuarioFin);
}
