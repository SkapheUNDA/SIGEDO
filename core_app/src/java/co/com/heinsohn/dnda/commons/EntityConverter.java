package co.com.heinsohn.dnda.commons;

import java.io.Serializable;
import java.security.MessageDigest;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;

/**
 * Clase encargada de convertir las entidades de una lista en items válidos
 * para un <code>s:selectItems</code> y luego convertir el item seleccionado en
 * una entidad. a manera del <code>s:convertEntity</code>.
 * 
 * @author Bryan Camilo Pérez Trujillo <bperez@heinsohn.com.co>
 * 
 */
@ManagedBean(name = "EntityConverter")
@ViewScoped
public class EntityConverter implements Converter, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2149182185933041144L;
	/**
	 * Mapa que contiene las entidades de una lista.
	 */
	private Map<String, Object> entityDefinitions = new ConcurrentHashMap<String, Object>();
	private static final Logger log = Logger
			.getLogger("EntityConverter");

	/**
	 * Método encargado de agregar al mapa la entidad y asignarle como key su
	 * toString().
	 * 
	 * @param facesContext
	 *            : contexto de Faces.
	 * @param cmp
	 *            : componente de faces que contiene la lista.
	 * @param entity
	 *            : objeto que representa la entidad que se desea agregar al
	 *            mapa.
	 * @return toString() de la entidad.
	 */
	public String getAsString(FacesContext facesContext, UIComponent cmp,
			Object entity) {
		if (entity == null) {
			return null;
		}
		String key = getKey(entity);
		entityDefinitions.put(key, entity);
		return key;
	}

	/**
	 * Obtiene la clave para la entidad dada, esta clave debería ser única
	 * para cada objeto, independiente del tipo de dato.
	 * 
	 * @param entity
	 * @return
	 */
	private String getKey(Object entity) {
		String key = "";
		Class<? extends Object> entityClass = entity.getClass();
		String className = entityClass.getName();
		if (className.startsWith("com.heinsohn") && !entityClass.isEnum()) {
			try {
				key = obtenerMD5(className);
			} catch (Exception ex) {
				log.error(ExceptionUtils.getStackTrace(ex));
			}
		}
		key += entity.toString().replaceAll("[\n\r]", "")
				.replaceAll("\\s+", " ");
		return key;
	}

	/**
	 * Método que permite obtener una entidad del mapa por medio de su key.
	 * 
	 * @param facesContext
	 *            : contexto de Faces.
	 * @param cmp
	 *            : componente de faces que contiene la lista.
	 * @param key
	 *            : llave para obtener la entidad de la lista.
	 * @return objeto que representa la entidad seleccionada.
	 */
	public Object getAsObject(FacesContext facesContext, UIComponent cmp,
			String key) {
		return entityDefinitions.get(key.replaceAll("[\n\r]", "").replaceAll(
				"\\s+", " "));
	}

	/**
	 * 
	 * Metodo encargado de calcular el md5 de una cadena
	 * 
	 * @author Camilo Forero <cforero@heinsohn.com.co>
	 * 
	 * @param original
	 *            La cadena a procesar
	 * @return El md5 de la cadena
	 * @throws Exception
	 *             CUando ocurre un error en la operacion.
	 */
	private String obtenerMD5(String original) throws Exception {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(original.getBytes());
		byte[] digest = md.digest();
		StringBuffer sb = new StringBuffer();
		for (byte b : digest) {
			sb.append(Integer.toHexString((int) (b & 0xff)));
		}
		return sb.toString();
	}
}
