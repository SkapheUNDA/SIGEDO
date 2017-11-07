package la.netco.sgc.uilayer.beans;

import java.util.Calendar;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 * 
 * @author Alejo Medelln
 *
 */

@FacesValidator("auditoriasValidator")
public class AuditoriasValidator implements Validator {
	@Override
	public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {
		if (value == null) {
			return; // Let required="true" handle.
		}

		UIInput fechaEnvioC = (UIInput) component.getAttributes().get("fechaEnvioC");
		UIInput fechaCampoC = (UIInput) component.getAttributes().get("fechaCampoC");

		if (!fechaEnvioC.isValid() || !fechaCampoC.isValid()) {
			return; // Already invalidated. Don't care about it then.
		}

		Date fechaEnvio = (Date) fechaEnvioC.getValue();
		Date fechaCampo = (Date) fechaCampoC.getValue();

		if (fechaEnvio == null || fechaCampo == null) {
			return;
		}

		Date fechaInforme = (Date) value;

		if (fechaInforme == null) {
			return;
		}

		Calendar calendar = Calendar.getInstance();
		Date fechaActual = calendar.getTime();

		if (fechaEnvio.after(fechaActual)) {
			fechaEnvioC.setValid(false);
			throw new ValidatorException(
					new FacesMessage(
							FacesMessage.SEVERITY_ERROR,
							"Fecha de envío oficio debe ser menor a fecha actual",
							null));
		} else if (fechaCampo.before(fechaEnvio)) {
			fechaCampoC.setValid(false);
			throw new ValidatorException(
					new FacesMessage(
							FacesMessage.SEVERITY_ERROR,
							"Fecha trabajo de campo debe ser mayor a Fecha de envío oficio",
							null));
		} else if (fechaInforme.before(fechaCampo)) {
			throw new ValidatorException(
					new FacesMessage(
							FacesMessage.SEVERITY_ERROR,
							"Fecha entrega informe debe ser mayor a Fecha trabajo de campo",
							null));
		}
	}

}
