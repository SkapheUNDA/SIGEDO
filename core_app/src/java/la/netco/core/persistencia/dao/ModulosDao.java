package la.netco.core.persistencia.dao;

import java.util.List;

import la.netco.core.persitencia.dao.commos.GenericCommonDao;


public interface  ModulosDao  extends GenericCommonDao {
	
	public List<Integer> buscaModuloPorPerfilesPadre(List<String> perfiles,Integer idParent);
	public List<Integer> buscaModulosPadre(final List<String> perfiles);
	public boolean eliminarRecursosPerfil(final Integer idPerfil);
}
