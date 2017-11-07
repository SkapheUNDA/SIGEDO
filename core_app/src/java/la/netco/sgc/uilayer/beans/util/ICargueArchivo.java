package la.netco.sgc.uilayer.beans.util;

import java.util.List;

public abstract class ICargueArchivo {
	
	public abstract void cargar(List<Archivo> archivos, String formato) throws Exception;

}
