package la.netco.sgc.uilayer.beans;

import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 * 
 * @author Alejo Medelln
 *
 */

@FacesValidator("hallazgosValidator")
public class HallazgosValidator implements Validator {
	@Override
	public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {
		if (value == null) {
			return; // Let required="true" handle.
		}

		Date fechaTrabajoCampo = (Date) component.getAttributes().get(
				"fechaTrabajoCampoC");

		if (fechaTrabajoCampo == null) {
			return;
		}

		Date fechaVencimiento = (Date) value;

		if (fechaVencimiento == null) {
			return;
		}

		if (fechaVencimiento.before(fechaTrabajoCampo)) {
			throw new ValidatorException(
					new FacesMessage(
							FacesMessage.SEVERITY_ERROR,
							"Fecha vencimiento debe ser mayor o igual a fecha trabajo campo " + fechaTrabajoCampo.toString(),
							null));
		}
	}

}
