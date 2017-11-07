package la.netco.sgc.persistence.dao;

import java.util.List;

import la.netco.core.persitencia.dao.commos.GenericCommonDao;
import la.netco.sgc.persistence.dto.CortesFormato;


public interface CortesFormatoDao extends GenericCommonDao{
	
	public List<CortesFormato> obtenerCortesFormatoPorFormato(Integer codigoFormato);	
	
	public CortesFormato obtenerCortesFormatoActivoPorFormato();
	
	public CortesFormato obtenerCortesFormatoInActivoPorFormato();


}
