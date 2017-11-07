package la.netco.core.uilayer.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.html.HtmlCommandLink;
import javax.faces.component.html.HtmlOutputLink;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;

import la.netco.core.businesslogic.services.ServiceDao;
import la.netco.core.persistencia.vo.Modulo;
import la.netco.core.spring.BeansSpringConst;
import la.netco.core.spring.security.UserDetailsUtils;

import org.richfaces.component.UIPanelMenu;
import org.richfaces.component.UIPanelMenuGroup;
import org.richfaces.component.UIPanelMenuItem;
import org.richfaces.component.UIToolbar;
import org.richfaces.component.UIToolbarGroup;
import org.springframework.security.core.GrantedAuthority;


@ManagedBean(name = "menuModulosBean")
@SessionScoped
public class MenuModulosBean implements Serializable{


	private static final long serialVersionUID = 1L;
	private UIPanelMenu panelMenuVertical;
	private Integer menuParentSelected = 3;
	
	private UIToolbar toolBarMenu;


	@ManagedProperty(value = BeansSpringConst.BEAN_ID_SERVICE_DAO)
	private ServiceDao servicesDaoProvider;
	
	
	@SuppressWarnings("unchecked")
	private void loadVerticalMenu(){

		
		if(panelMenuVertical != null && panelMenuVertical.getChildCount() == 0 && menuParentSelected != null){
			FacesContext context = FacesContext.getCurrentInstance();
			ELContext elContext = context.getELContext();
			ExpressionFactory factory = context.getApplication().getExpressionFactory();

			Map<Integer, Object> params = new HashMap<Integer, Object>();
			params.put(0, menuParentSelected);
			List<Modulo> rootModulos = new ArrayList<Modulo>(); 
			List<Integer> idModulos =new ArrayList<Integer>(); 
					
			Collection<GrantedAuthority> authorities = UserDetailsUtils.userLoggedAuthorities();
			if(authorities != null && !authorities.isEmpty()){
				List<String> perfiles = new ArrayList<String>();
				for (GrantedAuthority grantedAuthority : authorities) {
					//perfiles.add(grantedAuthority.getAuthority());
					perfiles.add(grantedAuthority.getAuthority().replaceAll("ROLE_", ""));
				}
				idModulos = servicesDaoProvider.getModulosDao().buscaModuloPorPerfilesPadre(perfiles, menuParentSelected);
				rootModulos = (List<Modulo>) servicesDaoProvider.getModulosDao().executeFind(Modulo.class, params, Modulo.NAMED_QUERY_FIND_BY_PARENT_ID);
				
			}
			for (Modulo modulo : rootModulos) {
				if(idModulos.contains(modulo.getId())){
					
					if(modulo.getChildren() == null || modulo.getChildren().isEmpty()){
						UIPanelMenuItem menuItem = new UIPanelMenuItem();
						menuItem.setLabel(modulo.getNombre());
						menuItem.setId(modulo.getKeyMod());	
						
						MethodExpression me = factory.createMethodExpression(elContext, modulo.getReglaNavegacion(),String.class, new Class<?>[0]);
						
						menuItem.setActionExpression(me);
						
						panelMenuVertical.getChildren().add(menuItem);
					}else{
						
						UIPanelMenuGroup menuGroup = new UIPanelMenuGroup();
						menuGroup.setLabel(modulo.getNombre());
						menuGroup.setId(modulo.getKeyMod());
						addItemsMenu(menuGroup, modulo, idModulos, elContext, factory);
						panelMenuVertical.getChildren().add(menuGroup);
						
						
					}
				}
			}
			
		}
	
	}
	
	
	private void addItemsMenu(UIPanelMenuGroup menuGroup, Modulo moduloParent, List<Integer> idModulos, ELContext elContext, ExpressionFactory factory){
		
		
		for (Modulo child : moduloParent.getChildren()) {
			if(idModulos.contains(child.getId())){
				
					if(child.getChildren() == null || child.getChildren().isEmpty()){
						
						UIPanelMenuItem menuItem = new UIPanelMenuItem();
						menuItem.setLabel(child.getNombre());
						menuItem.setId(child.getKeyMod());
						
						MethodExpression me = factory.createMethodExpression(elContext, child.getReglaNavegacion(),String.class, new Class<?>[0]);
						menuItem.setActionExpression(me);
						
						menuGroup.getChildren().add(menuItem);
						
					}else{
						
						UIPanelMenuGroup menuGroupChild = new UIPanelMenuGroup();
						menuGroupChild.setLabel(child.getNombre());
						menuGroupChild.setId(child.getKeyMod());
						addItemsMenu(menuGroupChild,child, idModulos, elContext, factory);					
						menuGroup.getChildren().add(menuGroupChild);
						
						
						
					}
			}
		}
	}
	

	@SuppressWarnings("unchecked")
	private void loadToolBar(){

		
		if(toolBarMenu != null && toolBarMenu.getChildCount() == 0){
			FacesContext context = FacesContext.getCurrentInstance();
			ELContext elContext = context.getELContext();
			Map<Integer, Object> params = new HashMap<Integer, Object>();
			List<Modulo> rootModulos = new ArrayList<Modulo>(); 
			List<Integer> idModulos =new ArrayList<Integer>(); 
					
			Collection<GrantedAuthority> authorities = UserDetailsUtils.userLoggedAuthorities();
			if(authorities != null && !authorities.isEmpty()){
				List<String> perfiles = new ArrayList<String>();
				for (GrantedAuthority grantedAuthority : authorities) {
					perfiles.add(grantedAuthority.getAuthority().replaceAll("ROLE_", ""));
				}
				idModulos = servicesDaoProvider.getModulosDao().buscaModulosPadre(perfiles);
				rootModulos = (List<Modulo>) servicesDaoProvider.getModulosDao().executeFind(Modulo.class, params, Modulo.NAMED_QUERY_FIND_ROOTS);
				
			}
			
			System.out.println(" idModulos " + idModulos );
			System.out.println(" modulo " + rootModulos );
			for (Modulo modulo : rootModulos) {
				if(idModulos.contains(modulo.getId())){
					
					UIToolbarGroup toolbarGroup = new UIToolbarGroup();
					toolbarGroup.setId(modulo.getKeyMod());
					HtmlCommandLink command = new HtmlCommandLink();
					command.setValue(modulo.getNombre());
					command.setId("command"+modulo.getKeyMod());
					
					ExpressionFactory factory = context.getApplication().getExpressionFactory();
					MethodExpression me = factory.createMethodExpression(elContext, "#{menuModulosBean.selectItemToolBar('"+ modulo.getReglaNavegacion() + "', '"+ modulo.getId() + "')}",String.class, new Class<?>[0]);
					
					command.setActionExpression(me);
					
					toolbarGroup.getChildren().add(command);
					toolBarMenu.getChildren().add(toolbarGroup);
				
				}
			}
			
			UIToolbarGroup toolbarGroup = new UIToolbarGroup();
			toolbarGroup.setId("toolWellcome");
			
			toolbarGroup.setLocation("right");
			HtmlOutputText text = new HtmlOutputText();
			text.setValue("Bienvenido: " + UserDetailsUtils.usernameLogged());
			toolbarGroup.getChildren().add(text);
			toolBarMenu.getChildren().add(toolbarGroup);
			
			
			UIToolbarGroup toolbarCambioclave = new UIToolbarGroup();
			toolbarCambioclave.setId("toolCambioClave");
			toolbarCambioclave.setLocation("right");
			HtmlOutputLink linkCambioclave = new HtmlOutputLink();
			linkCambioclave.setValue("#");
			linkCambioclave.setOnclick("modalCambioClave.show();");
			HtmlOutputText textCambioclave = new HtmlOutputText();
			textCambioclave.setValue("Cambiar Clave");
			linkCambioclave.getChildren().add(textCambioclave);
			toolbarCambioclave.getChildren().add(linkCambioclave);
			toolBarMenu.getChildren().add(toolbarCambioclave);
			
			UIToolbarGroup toolbarLogout = new UIToolbarGroup();
			toolbarLogout.setId("toolbarLogout");
			toolbarLogout.setLocation("right");
			HtmlOutputLink linkLogout = new HtmlOutputLink();
			linkLogout.setValue("/core_app/j_spring_security_logout");
			HtmlOutputText textLogout = new HtmlOutputText();
			textLogout.setValue("Salir");
			linkLogout.getChildren().add(textLogout);
			toolbarLogout.getChildren().add(linkLogout);
			toolBarMenu.getChildren().add(toolbarLogout);
			

			
		}
	
	}
	
	public String selectItemToolBar(String navigation, String idItem ){
		System.out.println(" selectItemToolBar " + navigation + " item-----------------------------------> "  + idItem) ;
		
		if(idItem != null){
			menuParentSelected = Integer.parseInt(idItem);
			if(panelMenuVertical != null)
				panelMenuVertical.getChildren().clear();
		}
		
		
		
		loadVerticalMenu();
		
		
		return navigation;
	}

	
	public UIPanelMenu getPanelMenuVertical() {		
		return panelMenuVertical;
	}

	public void setPanelMenuVertical(UIPanelMenu panelMenuVertical) {
		this.panelMenuVertical = panelMenuVertical;
		loadVerticalMenu();
	}

	public Integer getMenuParentSelected() {
		return menuParentSelected;
	}

	public void setMenuParentSelected(Integer menuParentSelected) {
		this.menuParentSelected = menuParentSelected;
	}

	public ServiceDao getServicesDaoProvider() {
		return servicesDaoProvider;
	}

	public void setServicesDaoProvider(ServiceDao servicesDaoProvider) {
		this.servicesDaoProvider = servicesDaoProvider;
	}


	public UIToolbar getToolBarMenu() {
		return toolBarMenu;
	}


	public void setToolBarMenu(UIToolbar toolBarMenu) {
		this.toolBarMenu = toolBarMenu;
		loadToolBar();
	}
	
	
}
