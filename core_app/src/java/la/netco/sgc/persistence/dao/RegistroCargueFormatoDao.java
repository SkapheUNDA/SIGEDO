/**
 * 
 */
package la.netco.sgc.persistence.dao;

import java.util.List;

import la.netco.core.persitencia.dao.commos.GenericCommonDao;
import la.netco.sgc.persistence.dto.RegistroCargueFormato;

/**
 * @author cguzman
 *
 */
public interface RegistroCargueFormatoDao extends GenericCommonDao {

	RegistroCargueFormato tomarRegistroCargueFormatoPorCargue(Integer codFormato, Integer codEntidad, Integer codCorte);
	
	List<RegistroCargueFormato> tomarRegistroCargueFormatoSinCorte(Integer codFormato, Integer codEntidad);
}
