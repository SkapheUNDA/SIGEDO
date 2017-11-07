package la.netco.sgc.persistence.dao;

import java.util.List;

import la.netco.core.persitencia.dao.commos.GenericCommonDao;
import la.netco.sgc.persistence.dto.Autorizaciones;

public interface AutorizacionesDao extends GenericCommonDao {

	public List<Autorizaciones> obtenerAutorizacionesPorFormato(Integer forCodigo);

	public Autorizaciones obtenerAutorizacionPorFormatoEntidadVigente(Integer codFormato, Integer codEntidad);

}
