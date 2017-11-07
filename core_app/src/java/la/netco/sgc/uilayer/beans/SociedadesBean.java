package la.netco.sgc.uilayer.beans;

import java.io.Serializable;
import javax.faces.bean.ManagedProperty;
import la.netco.core.businesslogic.services.ServiceDao;
import la.netco.sgc.persistence.dto.Sociedades;

public class SociedadesBean implements Serializable {
	
	/**
	 * 
	 */

	private static final long serialVersionUID = 7897257893336366511L;
	
	private Sociedades sociedad;
	
	@ManagedProperty(value = "#{serviceDao}")
	transient private ServiceDao serviceDao;
	
	public SociedadesBean(){
		this.sociedad = new Sociedades();
	}


	public Sociedades getSociedad() {
		return sociedad;
	}

	public void setSociedad(Sociedades sociedad) {
		this.sociedad = sociedad;
	}

	public ServiceDao getServiceDao() {
		return serviceDao;
	}

	public void setServiceDao(ServiceDao serviceDao) {
		this.serviceDao = serviceDao;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	

}
