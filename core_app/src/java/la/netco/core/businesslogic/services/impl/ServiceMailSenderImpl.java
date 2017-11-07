
package la.netco.core.businesslogic.services.impl;


import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;

import la.netco.core.businesslogic.services.ServiceMailSender;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;

import org.apache.velocity.app.VelocityEngine;


@Service("mailService")
public class ServiceMailSenderImpl implements ServiceMailSender {

	
	private static final Log log = LogFactory.getLog(ServiceMailSenderImpl.class);


	private JavaMailSenderImpl mailSender;

	
	  public JavaMailSenderImpl getMailSender() {
		return mailSender;
	}


	private VelocityEngine velocityEngine;

	@Autowired
	@Resource(name="velocityEngine")
	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}


	@Autowired
	@Resource(name="mailSender")
	public void setMailSender(JavaMailSenderImpl mailSender) {
		this.mailSender = mailSender;
	}


	private String from;

	public void setFrom(String from) {
		this.from = from;
	}

	@Value("#{mail.app.username}")
	public String getFrom() {
		return from;
	}


	public boolean active = true;


	public boolean isActive() {
		return active;
	}

	@Value("${mail.service.active}")
	public void setActive(boolean active) {
		this.active = active;
	}

	
	public void sendTextTemplateEmail(final  Map<String, String> params, final String template, final  List<String> mails, final String subject) {
	    
		if (!active) return;	
		try {
			MimeMessagePreparator preparator = new MimeMessagePreparator() {	 
	        
			public void prepare(MimeMessage mimeMessage) {
				try {
		            MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
		            String mailsV [] = new String[mails.size()]; 
		            int i = 0;
		            for (String string : mails) {
		            	mailsV[i] = string;
		            	i++;
					}
		            
		            message.setTo(mailsV);  
		            message.setFrom(getFrom()); 
		            message.setSubject(subject);
		            String text = VelocityEngineUtils.mergeTemplateIntoString( velocityEngine,template, params);
		            message.setText(text, true);
				} catch (Exception e) {
					log.error("ERROR PREPARANDO CORREO - " + e.getMessage());
				}
	         }
	      };
	      
	      if(preparator != null){
				 log.info("ENVIANDO CORREOS - EMAILS: " + mails);
			     this.mailSender.send(preparator);
		  }

		} catch (Exception e) {
			log.error("ERROR ENVIANDO CORREOS  - "+mails+" -"   + e.getMessage());
		}
		
		
	    
	}

	public void sendTextTemplateEmail(final  Map<String, String> params, final String template, final String mail, final String subject) {
	    
		if (!active) return;
				
		try {
			MimeMessagePreparator preparator = new MimeMessagePreparator() {	        
			public void prepare(MimeMessage mimeMessage) {
				try {
					MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
					message.setTo(mail);  
		            message.setFrom(getFrom()); 
		            message.setSubject(subject);
		            String text = VelocityEngineUtils.mergeTemplateIntoString( velocityEngine,template, params);
		            message.setText(text, true);
				} catch (Exception e) {
					log.error("ERROR PREPARANDO CORREO - " + e.getMessage());
				}
	         }
	      };	      
		
		
		    if(preparator != null){
				 log.info("ENVIANDO CORREO - EMAIL: " + mail);
				 this.mailSender.send(preparator);
			}
		    
		} catch (Exception e) {
			log.error("ERROR ENVIADO CORREO - "+mail+" -"  + e.getMessage());
		}
	     
	    
	}

}
