package la.netco.core.businesslogic.services.impl;

import javax.annotation.Resource;

import la.netco.core.businesslogic.events.EmailEvent;
import la.netco.core.businesslogic.services.ServiceMailSender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;


@Service("mailSenderExecutor")
public class MailSenderExecutor {

	
	
	private TaskExecutor taskExecutor;
	private ServiceMailSender mailService;
	
	
	public MailSenderExecutor(){
		
	}
	 
	public MailSenderExecutor(TaskExecutor taskExecutor, ServiceMailSender mailService) {

		this.taskExecutor = taskExecutor;
		this.mailService = mailService;
	}

	
	public void sendMail(EmailEvent email){
		
		taskExecutor.execute(new SendMailTask(email, mailService));
		
	}
	
	@Autowired
	@Resource(name="taskExecutor")
	public void setTaskExecutor(TaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}

	public TaskExecutor getTaskExecutor() {
		return taskExecutor;
	}


	@Autowired
	@Resource(name="mailService")
	public void setMailService(ServiceMailSender mailService) {
		this.mailService = mailService;
	}



	public ServiceMailSender getMailService() {
		return mailService;
	}
}
