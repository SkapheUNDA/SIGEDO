/**
 * 
 */
package la.netco.sgc.persistence.dao;

import la.netco.core.persitencia.dao.commos.GenericCommonDao;
import la.netco.sgc.persistence.dto.RegistroRadicado;

/**
 * @author cguzman
 *
 */
public interface RegistroRadicadoDao extends GenericCommonDao {

	RegistroRadicado tomarRegistroRadicado(Integer codEntidad, Integer codCorte);		
}
