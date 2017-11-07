package la.netco.correspondencia.dao;

import java.util.Date;

import la.netco.core.persitencia.dao.commos.GenericCommonDao;
import la.netco.correspondencia.dto.custom.Consecutivo;
import la.netco.persistencia.dto.commons.Entrada;

public interface EntradaSalidaDao  extends GenericCommonDao {
	
	public Consecutivo obtenerConsecutivo(String depCod, Integer tipoCorrespondencia) throws Exception;
	public Date obtenerFechaRadicacion();
	public Date obtenerFechaRespuesta(Entrada entrada);
	public long obtenerDiffDiasHabiles(Date desde, Date hasta);
	
}
