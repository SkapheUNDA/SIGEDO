package la.netco.core.uilayer.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.event.PhaseEvent;

import org.richfaces.component.UIFileUpload;
import org.richfaces.model.UploadedFile;




public class FileUploadBean implements Serializable {

	
	private static final long serialVersionUID = -4446480001939352466L;

	
	private List<UploadedFile> uploadData = new ArrayList<UploadedFile> ();
	private UIFileUpload  fileUpload;
	

	
	public void pahseEvent(PhaseEvent event){
		uploadData = new ArrayList<UploadedFile> ();
	}
	
	
	
	
	public void setUploadData(List<UploadedFile> uploadData) {
		this.uploadData = uploadData;
	}


	public List<UploadedFile> getUploadData() {
		return uploadData;
	}



	public void setFileUpload(UIFileUpload fileUpload) {
		this.fileUpload = fileUpload;
	}



	public UIFileUpload getFileUpload() {
		return fileUpload;
	}
	
	
	
}
