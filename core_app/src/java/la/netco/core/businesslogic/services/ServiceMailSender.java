
package la.netco.core.businesslogic.services;

import java.util.List;
import java.util.Map;

public interface ServiceMailSender {
	public void sendTextTemplateEmail(final  Map<String, String> params, final String template, final  List<String> mails, final String subject) ;
	public void sendTextTemplateEmail(final  Map<String, String> params, final String template, final String mail, final String subject) ;
}
