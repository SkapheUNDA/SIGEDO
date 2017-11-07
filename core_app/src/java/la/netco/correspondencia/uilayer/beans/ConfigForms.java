package la.netco.correspondencia.uilayer.beans;

import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import la.netco.core.businesslogic.services.ServiceDao;
import la.netco.core.businesslogic.services.impl.SpringApplicationContext;
import la.netco.core.persitencia.dao.commos.GenericCommonDao;
import la.netco.persistencia.dto.commons.Asignaclasificacion;


public class ConfigForms extends HashMap<String, Propiedad> {

	
	private static final long serialVersionUID = 1L;
	private static Logger log = LoggerFactory.getLogger(ConfigForms.class);
	
	public ConfigForms(Short idDep, Short idCla){
		
		Map<Integer, Short> params = new HashMap<Integer, Short>();
		params.put(0, idCla);
		params.put(1, idDep);
		
		Asignaclasificacion asignaClasificacion = (Asignaclasificacion) lookupDataProvider().executeUniqueResult( Asignaclasificacion.NAMED_QUERY_BY_ID, params);
		
		System.out.println("  Asignaclasificacion >> " + asignaClasificacion);
		
		PropertyDescriptor[] descriptors =  BeanUtilsBean.getInstance().getPropertyUtils().getPropertyDescriptors(asignaClasificacion);
		
		//Map<String, Propiedad> campoForm = new HashMap<String, Propiedad>();
		try {
			
			
	    	for (PropertyDescriptor propertyDescriptor : descriptors) {
	    		String campo = propertyDescriptor.getName();
	    		
	    		
	    		if(campo.length() < 7 )
	    			continue;
	    		
	    		campo = campo.substring(4);
	    		
	    		
	    		if(CamposCorrespondencia.LIST_CAMPOS.contains(campo) && !containsKey(campo)){
	    			
	    			Propiedad propiedades= new Propiedad();	    			
	    			BeanUtilsBean beanUtils = BeanUtilsBean.getInstance();
	    			
	    			propiedades.setRequired(Boolean.valueOf(beanUtils.getProperty(asignaClasificacion, CamposCorrespondencia.SUF_REQUERIDO+campo)));
	    			propiedades.setDisabled(Boolean.valueOf(beanUtils.getProperty(asignaClasificacion, CamposCorrespondencia.SUF_VISIBLE+campo)));
	    			propiedades.setDefaultValue(beanUtils.getProperty(asignaClasificacion, CamposCorrespondencia.SUF_VALOR_DEFECTO+campo));
	    			propiedades.setDefaultProp(Boolean.valueOf(beanUtils.getProperty(asignaClasificacion, CamposCorrespondencia.SUF_DEFECTO+campo)));
	    			put(campo, propiedades);
	    			
	    		}
	    	}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		

	}
	
	private GenericCommonDao lookupDataProvider() {
    	ServiceDao dataProvider = (ServiceDao) SpringApplicationContext.getBean("serviceDao");
    	GenericCommonDao daoAccess = dataProvider.getGenericCommonDao();
    	return daoAccess;
    }
	
}
