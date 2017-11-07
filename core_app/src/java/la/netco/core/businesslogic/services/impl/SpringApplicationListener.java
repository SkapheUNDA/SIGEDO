
package la.netco.core.businesslogic.services.impl;

import javax.annotation.Resource;

import la.netco.core.businesslogic.events.EmailEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;



@SuppressWarnings("rawtypes")
@Service("springApplicationListener")
public class SpringApplicationListener implements ApplicationListener {


	private MailSenderExecutor mailExecutor;

	public void onApplicationEvent(ApplicationEvent appEvent) {
	 
		 if (appEvent instanceof EmailEvent){				 
			 mailExecutor.sendMail((EmailEvent)appEvent);
		 }
		 
		 
		 
	}
	@Autowired
	@Resource(name="mailSenderExecutor")
	public void setMailExecutor(MailSenderExecutor mailExecutor) {
		this.mailExecutor = mailExecutor;
	}

	public MailSenderExecutor getMailExecutor() {
		return mailExecutor;
	}

}
