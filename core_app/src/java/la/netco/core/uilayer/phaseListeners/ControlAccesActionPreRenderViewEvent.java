package la.netco.core.uilayer.phaseListeners;

import java.lang.reflect.Method;
import java.util.Iterator;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;

import la.netco.core.spring.security.SecuredAction;
import la.netco.core.spring.security.ValidationActionUtils;

import org.primefaces.component.menuitem.MenuItem;
import org.richfaces.component.UIMenuItem;

public class ControlAccesActionPreRenderViewEvent  implements SystemEventListener {

	public void processEvent(SystemEvent event) throws AbortProcessingException {

		UIViewRoot root = (UIViewRoot) event.getSource();
		
		for (UIComponent component : root.getChildren()) {
			findActionCommand(component);
			findComponent(component);
		}
		
	}

    @SuppressWarnings("rawtypes")
	private static UIComponent findComponent(UIComponent base) {
		UIComponent children = null;
		UIComponent result = null;
		Iterator childrens = base.getFacetsAndChildren();
		while (childrens.hasNext() && (result == null)) {

			children = (UIComponent) childrens.next();

			findActionCommand(children);

			result = findComponent(children);
			if (result != null) {
				break;
			}
		}
		return result;
	}

	
	private static void findActionCommand(UIComponent children) {
		
		if (children.getFamily().equals(UICommand.COMPONENT_FAMILY)	|| children.getFamily().equals(UIMenuItem.COMPONENT_FAMILY) || children.getFamily().equals(MenuItem.COMPONENT_FAMILY) ) {			
			
			MethodExpression action = null;
			
			if(children.getFamily().equals(UICommand.COMPONENT_FAMILY)){
				UICommand command = (UICommand) children;
				action = command.getActionExpression();
			}
			else if(children instanceof UIMenuItem){
				UIMenuItem  command = (UIMenuItem ) children;
				action = command.getActionExpression();
			}else if(children instanceof MenuItem){
				MenuItem  command = (MenuItem ) children;
				action = command.getActionExpression();
			}

			

			if(action != null){
				FacesContext context = FacesContext.getCurrentInstance();
				ELContext elContext = context.getELContext();
				
				//MethodInfo methodInfo = action.getMethodInfo(elContext);
				String expr = action.getExpressionString();
				if (!expr.startsWith("#")) {
					return;
				}

				int idx = expr.indexOf('.');
				String target = expr.substring(0, idx).substring(2);
				String t = expr.substring(idx + 1);
				String method = t.substring(0, (t.length() - 1));
				int idexM = method.indexOf('(');
				if(idexM!=-1)
					method =method.substring(0, idexM); 
				
				ExpressionFactory factory = context.getApplication()
						.getExpressionFactory();
				ValueExpression ve = factory.createValueExpression(elContext, "#{"
						+ target + '}', Object.class);
				Object objectBean = ve.getValue(elContext);

				SecuredAction securedAnnotation = checkMethodAnnotationSecured(objectBean,method);

				if(securedAnnotation != null){
					boolean permission = validatePermission(securedAnnotation);
					if(!permission){
						children.setRendered(permission);
					}
				}

			}
			
			
		}

	}
	
	private static SecuredAction checkMethodAnnotationSecured(Object objectBean, String method){
		Method[] methods = objectBean.getClass().getMethods();
        for (Method meth : methods) {
            if (meth.getName().equals(method)) {
                if (meth.isAnnotationPresent(SecuredAction.class)) {
                	
                	return  meth.getAnnotation(SecuredAction.class);
                }
            }
        }
		return null;
	} 
	 
	
	private static boolean validatePermission(SecuredAction annontation){
    	String actionKey = annontation.keyAction();
    	String actionKeyMod = annontation.keyMod();
    	return ValidationActionUtils.validUserAction(actionKey, actionKeyMod);
	}
	
	public boolean isListenerForSource(Object source) {
		return (source instanceof UIViewRoot);
	}
}
