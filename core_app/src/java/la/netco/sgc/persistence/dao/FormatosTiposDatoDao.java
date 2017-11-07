package la.netco.sgc.persistence.dao;

import java.util.List;

import la.netco.core.persitencia.dao.commos.GenericCommonDao;
import la.netco.sgc.persistence.dto.FormatosTiposDato;

public interface FormatosTiposDatoDao extends GenericCommonDao {
	
	public List<FormatosTiposDato> tomarFormatosTiposDatoPorTiposDato(Integer idTipoDato);

}
