package la.netco.sgc.persistence.dao;

import java.util.List;

import la.netco.core.persitencia.dao.commos.GenericCommonDao;
import la.netco.sgc.persistence.dto.Alertas;

public interface AlertasDao extends GenericCommonDao{
	
	
	public List<Alertas> obtenerAlertasPorFormato(Integer forCodigo);
	
	public List<Alertas> obtenerAlertasFiltro(Integer codigoEntidad, Integer codigoFormato, List<Integer> codigoCorteFormato);
	
	public List<Alertas> obtenerAlertasFormatoCorte(Integer codigoFormato, Integer codigoCorteFormato);
	
	public List<Alertas> obtenerAlertasPorCorte(Integer fcrCodigo);
	
	public List<Alertas> obtenerAlertasPorCampo(Integer detCodigo);

}
