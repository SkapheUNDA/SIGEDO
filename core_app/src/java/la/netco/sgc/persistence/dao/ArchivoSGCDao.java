package la.netco.sgc.persistence.dao;

import java.util.List;

import la.netco.core.persitencia.dao.commos.GenericCommonDao;
import la.netco.sgc.persistence.dto.ArchivoSGC;


public interface ArchivoSGCDao extends GenericCommonDao{
	
	public List<ArchivoSGC> obtenerArchivos(Integer codigoEntidad, Integer codigoFormato, String periodo, String ano, String consulta);	
	
	
}
