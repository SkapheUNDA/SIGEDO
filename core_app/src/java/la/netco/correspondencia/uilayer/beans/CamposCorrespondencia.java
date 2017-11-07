package la.netco.correspondencia.uilayer.beans;

import java.util.Arrays;
import java.util.List;

import javax.faces.bean.ManagedBean;

@ManagedBean(name ="cmpCorrespondencia")
public class CamposCorrespondencia {

	
	public static String TITULO_ASUNTO 		= "asu";
	public static String EMAIL 				= "cel";
	public static String DIRECCION 			= "dir";
	public static String ENTIDAD 			= "ent";
	public static String FAX 				= "fax";
	public static String ID_ENTIDAD 		= "ide";
	public static String CIUDAD 			= "lug";
	public static String MEDIO 				= "med";
	public static String NACIONALIDAD 		= "nac";
	public static String NUM_DOCUMENTO 		= "ndo";
	public static String NOMBRE 			= "nom";
	public static String ORIGEN_ASUNTO 		= "nrd";
	public static String ORIGEN_REFERENCIA 	= "nrf";
	public static String OBSERVACIONES 		= "obs";
	public static String PRIMER_APELLIDO 	= "pap";
	public static String SEGUNDO_APELLIDO 	= "sap";
	public static String TIPO_DOCUMENTO 	= "tdo";
	public static String TELEFONO 			= "tel";
	
	public static String SUF_REQUERIDO 		= "asgR";
	public static String SUF_VISIBLE 		= "asgL";
	public static String SUF_VALOR_DEFECTO	= "asgD";
	public static String SUF_DEFECTO		= "asgC";
	

	public static final List<String> LIST_CAMPOS = Arrays.asList(TITULO_ASUNTO,
			EMAIL, DIRECCION, ENTIDAD, FAX, ID_ENTIDAD, CIUDAD, MEDIO,
			NACIONALIDAD, NUM_DOCUMENTO, NOMBRE, ORIGEN_ASUNTO,
			ORIGEN_REFERENCIA, OBSERVACIONES, PRIMER_APELLIDO,
			SEGUNDO_APELLIDO, TIPO_DOCUMENTO, TELEFONO);


	public String getTITULO_ASUNTO() {
		return TITULO_ASUNTO;
	}


	public String getEMAIL() {
		return EMAIL;
	}


	public String getDIRECCION() {
		return DIRECCION;
	}


	public String getENTIDAD() {
		return ENTIDAD;
	}


	public String getFAX() {
		return FAX;
	}


	public String getID_ENTIDAD() {
		return ID_ENTIDAD;
	}


	public String getCIUDAD() {
		return CIUDAD;
	}


	public String getMEDIO() {
		return MEDIO;
	}


	public  String getNACIONALIDAD() {
		return NACIONALIDAD;
	}


	public String getNUM_DOCUMENTO() {
		return NUM_DOCUMENTO;
	}


	public String getNOMBRE() {
		return NOMBRE;
	}


	public String getORIGEN_ASUNTO() {
		return ORIGEN_ASUNTO;
	}


	public String getORIGEN_REFERENCIA() {
		return ORIGEN_REFERENCIA;
	}


	public String getOBSERVACIONES() {
		return OBSERVACIONES;
	}


	public String getPRIMER_APELLIDO() {
		return PRIMER_APELLIDO;
	}


	public String getSEGUNDO_APELLIDO() {
		return SEGUNDO_APELLIDO;
	}


	public String getTIPO_DOCUMENTO() {
		return TIPO_DOCUMENTO;
	}


	public String getTELEFONO() {
		return TELEFONO;
	}


	public String getSUF_REQUERIDO() {
		return SUF_REQUERIDO;
	}


	public String getSUF_VISIBLE() {
		return SUF_VISIBLE;
	}


	public String getSUF_VALOR_DEFECTO() {
		return SUF_VALOR_DEFECTO;
	}


	public String getSUF_DEFECTO() {
		return SUF_DEFECTO;
	}

	
}
