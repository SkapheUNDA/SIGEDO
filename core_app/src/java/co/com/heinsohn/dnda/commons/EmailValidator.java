package co.com.heinsohn.dnda.commons;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 * <b>Descripcin:<b> Pojo de tipo validador de faces que se encarga de validar
 * que las cadenas ingresadas contrengan una direccin de correo vlido
 * 
 * @author Fabian Hernando Lpez Higuera <flopez@heinsohn.com.co>
 * 
 */
@FacesValidator("emailValidator")
public class EmailValidator implements Validator {

	/**
	 * patrn que representa la expresin regular.
	 */
	private Pattern pattern;

	/**
	 * secuencia de caracteres que queremos validar.
	 */
	private Matcher matcher;

	/**
	 * Expresion regular de los caracteres no permitidos.
	 */
	private static final String EMAIL_PATTERN = "(^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)+$)";

	/**
	 * Metodo constructor por defecto de la clase.
	 */
	public EmailValidator() {
		pattern = Pattern.compile(EMAIL_PATTERN);
	}

	/**
	 * (non-Javadoc) Mtodo que valida que el correo electrnico sea correcto
	 * 
	 * @see javax.faces.validator.Validator#validate(javax.faces.context.FacesContext
	 *      , javax.faces.component.UIComponent, java.lang.Object)
	 */
	@Override
	public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {
		
		final StringBuilder sb = new StringBuilder(((CharSequence) value).length());
		sb.append((CharSequence) value);
		
		
		if (!validar(sb.toString())) {
			FacesMessage msg = new FacesMessage("Valicación E-mail falló",
					"E-mail inválido");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(msg);
		}
	}
	
	public boolean validar(String texto) {
		// Se obtiene el valor ingresado
		matcher = pattern.matcher(texto);

		// Se valida que el valor el valor de la hora sea correcto
		if (!matcher.matches()) {
			return false;
		}
		return true;
	}
}
