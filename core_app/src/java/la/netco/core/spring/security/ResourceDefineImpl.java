package la.netco.core.spring.security;


import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("resourceDefine")
public class ResourceDefineImpl implements ResourceDefine {

	
	private CustomFilterInvocationSecurityMetadataSource filterService;
	
	public void updateResourceDefine() {
		try {
			filterService.loadResourceDefine();
		} catch (Exception e) {		
			e.printStackTrace();
		}
	}

	public CustomFilterInvocationSecurityMetadataSource getFilterService() {
		return filterService;
	}
	
	@Autowired
	@Resource(name="customFilterInvocationSec")
	public void setFilterService(
			CustomFilterInvocationSecurityMetadataSource filterService) {
		this.filterService = filterService;
	}

}
