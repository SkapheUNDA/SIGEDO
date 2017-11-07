/**
 * 
 */
package la.netco.sgc.persistence.dao;

import java.util.List;

import la.netco.core.persitencia.dao.commos.GenericCommonDao;
import la.netco.sgc.persistence.dto.InformeEntidad;

/**
 * @author cguzman
 *
 */
public interface InformeEntidadDao extends GenericCommonDao {

	List<InformeEntidad> tomarInformesEntidad(Integer codEntidad);	
	
	List<InformeEntidad> obtenerInformesFaltantes(Integer codCorte);
}
