package la.netco.core.uilayer.commons;



public class BaseBean {

	private Boolean hasPermissionUpdate;
	private Boolean hasPermissionAdd;
	private Boolean hasPermissionDelete;
	private Boolean hasPermissionView;
	
	public Boolean getHasPermissionUpdate() {
		return hasPermissionUpdate;
	}
	public void setHasPermissionUpdate(Boolean hasPermissionUpdate) {
		this.hasPermissionUpdate = hasPermissionUpdate;
	}
	public Boolean getHasPermissionAdd() {
		return hasPermissionAdd;
	}
	public void setHasPermissionAdd(Boolean hasPermissionAdd) {
		this.hasPermissionAdd = hasPermissionAdd;
	}
	public Boolean getHasPermissionDelete() {
		return hasPermissionDelete;
	}
	public void setHasPermissionDelete(Boolean hasPermissionDelete) {
		this.hasPermissionDelete = hasPermissionDelete;
	}
	public Boolean getHasPermissionView() {
		return hasPermissionView;
	}
	public void setHasPermissionView(Boolean hasPermissionView) {
		this.hasPermissionView = hasPermissionView;
	}
	
}
