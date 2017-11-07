package la.netco.core.uilayer.beans.utils;

import java.text.MessageFormat;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("celularValidator")
public class CelularValidator implements Validator {

	public void validate(FacesContext facesContext,   UIComponent uIComponent, Object object)	throws ValidatorException {

		
		String valor = (String) object;
		
		if(valor != null && !valor.equals("") && !valor.equals("0")){
			if(valor.length() != 10){
				FacesMessage message = new FacesMessage();
				String msg = FacesUtils.getMessage("errorCelular", FacesContext.getCurrentInstance());				
				String label = (String) uIComponent.getAttributes().get("label");				
				String formattedMessage = MessageFormat.format(msg, label);				
				message.setDetail(formattedMessage);
				message.setSummary(formattedMessage);							
				message.setSeverity(FacesMessage.SEVERITY_ERROR);

				throw new ValidatorException(message);	
			}
		}

	}

}
