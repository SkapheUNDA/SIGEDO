package la.netco.correspondencia.uilayer.beans;

import java.io.Serializable;

public class Propiedad implements Serializable{

	private static final long serialVersionUID = 1L;
	private boolean required;
	private boolean disabled;
	private Object defaultValue;
	private boolean defaultProp;
	
	public boolean isRequired() {
		return required;
	}
	public boolean isDisabled() {
		return disabled;
	}
	public Object getDefaultValue() {
		return defaultValue;
	}
	public void setRequired(boolean required) {
		this.required = required;
	}
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
	public void setDefaultValue(Object defaultValue) {
		this.defaultValue = defaultValue;
	}
	public boolean isDefaultProp() {
		return defaultProp;
	}
	public void setDefaultProp(boolean defaultProp) {
		this.defaultProp = defaultProp;
	}
	
}
