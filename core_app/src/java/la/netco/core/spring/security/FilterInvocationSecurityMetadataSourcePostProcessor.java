package la.netco.core.spring.security;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.util.Assert;

public class FilterInvocationSecurityMetadataSourcePostProcessor implements
		BeanPostProcessor, InitializingBean {

	private FilterInvocationSecurityMetadataSource securityMetadataSource;

	public void afterPropertiesSet() throws Exception {
		Assert.notNull(securityMetadataSource,
				"securityMetadataSource cannot be null");
	}

	public Object postProcessAfterInitialization(Object bean, String name)
			throws BeansException {
		if (bean instanceof FilterSecurityInterceptor) {
			((FilterSecurityInterceptor) bean)
					.setSecurityMetadataSource(securityMetadataSource);
		}
		return bean;
	}

	public Object postProcessBeforeInitialization(Object bean, String name) {
		return bean;
	}

	public void setSecurityMetadataSource(
			FilterInvocationSecurityMetadataSource securityMetadataSource) {
		this.securityMetadataSource = securityMetadataSource;
	}

}
