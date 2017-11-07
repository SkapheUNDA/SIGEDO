package la.netco.core.businesslogic.services.impl;

import java.util.Map;

import la.netco.core.businesslogic.events.EmailEvent;
import la.netco.core.businesslogic.services.ServiceMailSender;




public class SendMailTask  implements Runnable {
	
	private EmailEvent email;
	private ServiceMailSender mailService;
	


	public SendMailTask(EmailEvent email, ServiceMailSender mailService) {
		super();
		this.email = email;
		this.mailService = mailService;
	}

	@SuppressWarnings("unchecked")
	public void run() {
		
		if(email.getTo() != null){
			
			mailService.sendTextTemplateEmail((Map<String, String>)email.getParams(), email.getTemplate(), email.getTo(), email.getSubject());
		}else if(email.getMails() != null && !email.getMails().isEmpty()){
			mailService.sendTextTemplateEmail((Map<String, String>)email.getParams(), email.getTemplate(), email.getMails(), email.getSubject());
			
		} 
		


	}

	public void setEmail(EmailEvent email) {
		this.email = email;
	}

	public EmailEvent getEmail() {
		return email;
	}

	public void setMailService(ServiceMailSender mailService) {
		this.mailService = mailService;
	}

	public ServiceMailSender getMailService() {
		return mailService;
	}


}
