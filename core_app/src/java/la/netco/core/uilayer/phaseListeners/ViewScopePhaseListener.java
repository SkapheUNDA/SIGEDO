package la.netco.core.uilayer.phaseListeners;



import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.el.ELContext;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import la.netco.core.uilayer.beans.LoginBean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ViewScopePhaseListener implements PhaseListener {

	// serialVersionUID
	private static final long serialVersionUID = 4328185923344558170L;

	// Logger
	private static final Log logger = LogFactory.getLog(LoginBean.class);

	public void afterPhase(PhaseEvent phaseEvent) {

		FacesContext facesContext = phaseEvent.getFacesContext();

		if (facesContext.isPostback()) {
			processViewScopedManagedBeans(facesContext);
		}
	}

	public void beforePhase(PhaseEvent phaseEvent) {
		// This method is required by the interface but is not used.
	}

	protected void injectManagedProperties(Object managedBean, ManagedPropertyInjector managedPropertyInjector) {

		if (managedBean.getClass().isAnnotationPresent(ManagedBean.class)) {
			Map<String, Field> managedPropertyFields = getManagedPropertyFields(managedBean.getClass());
			Set<Map.Entry<String, Field>> managedPropertyEntrySet = managedPropertyFields.entrySet();

			for (Map.Entry<String, Field> managedPropertyMapEntry : managedPropertyEntrySet) {
				String managedPropertyName = managedPropertyMapEntry.getKey();
				Field managedPropertyField = managedPropertyMapEntry.getValue();
				ManagedProperty managedPropertyAnnotation = managedPropertyField.getAnnotation(ManagedProperty.class);
				String managedPropertyExpression = managedPropertyAnnotation.value();

				if ((managedPropertyExpression != null) && (managedPropertyExpression.length() > 0)) {
					managedPropertyInjector.inject(managedBean, managedPropertyName, managedPropertyField.getType(),
						managedPropertyExpression);
				}
			}
		}
	}

	protected void processViewScopedManagedBeans(FacesContext facesContext) {
		UIViewRoot viewRoot = facesContext.getViewRoot();

		if (viewRoot != null) {

			ManagedPropertyInjector managedPropertyInjector = new ManagedPropertyInjector(facesContext);
			Map<String, Object> viewMap = viewRoot.getViewMap();
			Set<Map.Entry<String, Object>> managedBeanEntrySet = viewMap.entrySet();

			for (Map.Entry<String, Object> managedBeanMapEntry : managedBeanEntrySet) {
				Object managedBean = managedBeanMapEntry.getValue();

				if (managedBean != null) {
					injectManagedProperties(managedBean, managedPropertyInjector);
				}
			}
		}
	}

	protected Map<String, Field> getManagedPropertyFields(Class<?> managedBeanClass) {

		Map<String, Field> managedPropertyFields = new HashMap<String, Field>();

		// For each of the delcared fields of the specified class:
		Field[] declaredFields = managedBeanClass.getDeclaredFields();

		for (Field declaredField : declaredFields) {

			// If there is a @ManagedProperty annotation present on the field, then
			ManagedProperty managedPropertyAnnotation = declaredField.getAnnotation(ManagedProperty.class);

			if (managedPropertyAnnotation != null) {

				// Add the managed-property field to the return value map.
				String managedPropertyName = getManagedPropertyName(managedPropertyAnnotation, declaredField);
				managedPropertyFields.put(managedPropertyName, declaredField);

				// Recurse with the superclass of the specified class, so as to gather up all the @ManagedProperty
				// fields in the class hierarchy.
				Class<?> superClass = managedBeanClass.getSuperclass();

				if (!Object.class.equals(superClass)) {

					Map<String, Field> superClassFields = getManagedPropertyFields(superClass);

					Set<Map.Entry<String, Field>> superClassFieldsEntrySet = superClassFields.entrySet();

					for (Map.Entry<String, Field> superClassMapEntry : superClassFieldsEntrySet) {

						// If the managed-property name not yet been processed, then add it to the map. If it has
						// already been processed then that means it's an @Override from a subclass, and therefore it
						// would not be appropriate to clobber it.
						String superClassManagedPropertyName = superClassMapEntry.getKey();

						if (!managedPropertyFields.containsKey(superClassManagedPropertyName)) {
							Field superClassManagedPropertyField = superClassMapEntry.getValue();
							managedPropertyFields.put(superClassManagedPropertyName, superClassManagedPropertyField);
						}
					}
				}
			}
		}

		return managedPropertyFields;
	}

	protected String getManagedPropertyName(ManagedProperty managedProperty, Field field) {

		// Get the name of the managed-property from the @ManagedProperty annotation.
		String managedPropertyName = managedProperty.name();

		// Since the name element of the @ManagedProperty annotation is optional, then if it is missing, use
		// the name of the field itself.
		if ((managedPropertyName == null) || (managedPropertyName.length() == 0)) {
			managedPropertyName = field.getName();
		}

		return managedPropertyName;
	}

	public PhaseId getPhaseId() {
		return PhaseId.RESTORE_VIEW;
	}

	protected class ManagedPropertyInjector {

		// Private Constants
		private static final String EXPRESSION_PREFIX = ManagedPropertyInjector.POUND + ManagedPropertyInjector.OPEN_CURLY_BRACE;
		private static final String EXPRESSION_SUFFIX = ManagedPropertyInjector.CLOSE_CURLY_BRACE;
		private static final String METHOD_PREFIX_SET = "set";
		
		public static final String CLOSE_CURLY_BRACE = "}";
		public static final String POUND = "#";
		public static final String OPEN_CURLY_BRACE = "{";

		// Private Data Members
		private Application application;
		private ELContext elContext;

		public ManagedPropertyInjector(FacesContext facesContext) {
			this.application = facesContext.getApplication();
			this.elContext = facesContext.getELContext();
		}

		public void inject(Object managedBean, String managedPropertyName, Class<?> managedPropertyClass,
			String elExpression) {

			String expressionWithoutSyntax = removeExpressionSyntax(elExpression);

			try {
				Object managedPropertyValue = application.getELResolver().getValue(elContext, null,
						expressionWithoutSyntax);
				String methodName = METHOD_PREFIX_SET + managedPropertyName.toUpperCase().substring(0, 1) +
					managedPropertyName.substring(1);
				Method setterMethod = managedBean.getClass().getMethod(methodName, managedPropertyClass);

				if (setterMethod != null) {
					setterMethod.invoke(managedBean, managedPropertyValue);
					
					logger.debug("Injected @ManagedProperty");
				}
				else {
					logger.debug("Unable to inject managed-property ");
				}
			}
			catch (Exception e) {
				logger.error(e);
			}
		}

		protected String removeExpressionSyntax(String elExpression) {

			String expressionWithoutSyntax = elExpression;

			if (expressionWithoutSyntax != null) {

				if (expressionWithoutSyntax.startsWith(EXPRESSION_PREFIX)) {
					expressionWithoutSyntax = expressionWithoutSyntax.substring(EXPRESSION_PREFIX.length());
				}

				if (expressionWithoutSyntax.endsWith(EXPRESSION_SUFFIX)) {
					expressionWithoutSyntax = expressionWithoutSyntax.substring(0,
							expressionWithoutSyntax.length() - EXPRESSION_SUFFIX.length());
				}
			}

			return expressionWithoutSyntax;
		}
	}
}