package la.netco.core.businesslogic.events;

import java.util.List;

import org.springframework.context.ApplicationEvent;

public class EmailEvent extends ApplicationEvent {

	private static final long serialVersionUID = 4336751970523453428L;
	private List<String>  mails;
	private String subject;
	private String text;
	private String template; 
	private String to; 
	private Object params;
	
	public EmailEvent(Object source) {
		super(source);
	}

	
	public EmailEvent(Object source, List<String> mails, String template,  String subject, Object params) {
		super(source);
		this.mails = mails;
		this.template = template;
		this.params =  params;
		this.subject =  subject;
	}


	public EmailEvent(Object source, String to, String template,  String subject, Object params) {
		super(source);
		this.setTo(to);
		this.template = template;
		this.params =  params;
		this.subject =  subject;
	}

	public void setMails(List<String> mails) {
		this.mails = mails;
	}



	public List<String> getMails() {
		return mails;
	}



	public void setSubject(String subject) {
		this.subject = subject;
	}



	public String getSubject() {
		return subject;
	}



	public void setText(String text) {
		this.text = text;
	}



	public String getText() {
		return text;
	}



	public void setTemplate(String template) {
		this.template = template;
	}



	public String getTemplate() {
		return template;
	}



	public void setParams(Object params) {
		this.params = params;
	}



	public Object getParams() {
		return params;
	}


	public void setTo(String to) {
		this.to = to;
	}


	public String getTo() {
		return to;
	}

	
	
}
