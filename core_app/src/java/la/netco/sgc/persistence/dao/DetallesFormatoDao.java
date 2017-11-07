package la.netco.sgc.persistence.dao;

import java.util.List;

import la.netco.core.persitencia.dao.commos.GenericCommonDao;
import la.netco.sgc.persistence.dto.DetallesFormato;

public interface DetallesFormatoDao extends GenericCommonDao {
	
	public List<DetallesFormato> obtenerDetallesFormatoPorFormato(Integer codigoFormato);

}
