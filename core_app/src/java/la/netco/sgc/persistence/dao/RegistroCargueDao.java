package la.netco.sgc.persistence.dao;

import java.util.List;

import la.netco.core.persitencia.dao.commos.GenericCommonDao;
import la.netco.sgc.persistence.dto.RegistroCargue;

public interface RegistroCargueDao extends GenericCommonDao {

	public List<RegistroCargue> tomarRegistroCarguePorFormato(Integer idFormato);

	public List<RegistroCargue> tomarRegistrosCarguePorFormatoEntidadCorte(Integer codFormato, Integer codEntidad, Integer codCorte);
	
	public List<RegistroCargue> tomarRegistrosCarguePorFormatoEntidadCorteCuenta(Integer codFormato, Integer codEntidad, Integer codCorte, String cuenta, String valor);
	
	public List<RegistroCargue> tomarRegistrosCarguePorFormatoEntidadCorteDetVal(Integer codFormato, Integer codEntidad, Integer codCorte, Integer detCodigo, String valor);
	
	public RegistroCargue tomarRegistrosCarguePorFormatoEntidadCorteDetReg(Integer codFormato, Integer codEntidad, Integer codCorte, Integer detCodigo, Integer reg);
	
	public List<RegistroCargue> tomarRegistrosCarguePorFormatoEntidadCorteDet(Integer codFormato, Integer codEntidad, Integer codCorte, Integer detCodigo);
	
	public List<RegistroCargue> tomarRegistroCarguePorCorte(Integer idCorte);
	
	public List<RegistroCargue> tomarRegistroCarguePorCampo(Integer idCampo);

}
