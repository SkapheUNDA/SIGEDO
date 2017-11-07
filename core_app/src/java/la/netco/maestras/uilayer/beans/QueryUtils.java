package la.netco.maestras.uilayer.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import la.netco.maestras.persistence.dto.PropTablas;

public class QueryUtils {

	public static String getQueryByProperties(List<PropTablas> propiedades,
			String tableMaster, int tablaActual) {

		StringBuffer consul = new StringBuffer();
		consul.append("SELECT  ");
		ArrayList<String> auxiLista = new ArrayList<String>();

		System.out.println("Id de tabla Actual:" + tablaActual);

		for (PropTablas propTablas : propiedades) {

			if (propTablas.getTablasMaestras().getId_tma() == tablaActual) {
				auxiLista.add(propTablas.getProp_cam());
			}
		}

		for (int Hu = 0; Hu < auxiLista.size(); Hu++) {
			if (Hu != 0) {
				consul.append("  , ");
			}
			consul.append(auxiLista.get(Hu));

		}

		consul.append(" FROM  " + tableMaster + " WHERE ESTADO=1");

		System.out.println(consul);

		String consulPersonalizada = consul.toString();

		return consulPersonalizada;
	}

	public static String getQueryForInsert(VarTMaestrasBean mapTMaster,
			String tablaMaster) {

		StringBuffer consul = new StringBuffer();
		int ContAux = 0;

		consul.append("INSERT  INTO " + tablaMaster + " (");

		for (Map.Entry<String, String> variItera : mapTMaster.entrySet()) {
			if (ContAux != 0) {
				consul.append(" , ");
			}
			consul.append(" " + variItera.getKey());
			ContAux++;
		}

		consul.append(" )  VALUES ('");
		ContAux = 0;

		for (Map.Entry<String, String> variIteraValor : mapTMaster.entrySet()) {
			if (ContAux != 0) {
				consul.append("' , '");
			}
			consul.append(variIteraValor.getValue());
			ContAux++;
		}

		consul.append("')");

		String consulFinal = consul.toString();
		System.out.println(consulFinal);

		return consulFinal;
	}

	public static String getQueryForUpdate(VarTMaestrasBean mapTMaster,
			String tablaMaster, String CampoPK, String idRegUpdate) {

		StringBuffer consul = new StringBuffer();
		int ContAux = 0;

		consul.append("UPDATE " + tablaMaster + " SET ");

		for (Map.Entry<String, String> variItera : mapTMaster.entrySet()) {
			if (ContAux != 0) {
				consul.append(" , " + variItera.getKey());
				consul.append(" = '" + variItera.getValue() + "'");
			}
			else  if(ContAux == 0)
			{
				consul.append(" " + variItera.getKey());
				consul.append(" = '" + variItera.getValue() + "'");
			}
			ContAux++;
		}

		consul.append(" WHERE " + CampoPK + " = '" + idRegUpdate + "'");
		
		String consulFinal = consul.toString();
		System.out.println(consulFinal);
		return consulFinal;
	}
	
	public static  String getQueryForDelete(String tablaMaster,String CampoPK, String idRegUpdate)
	{
		StringBuffer consul = new StringBuffer();
		consul.append("UPDATE " + tablaMaster + " SET ");
		consul.append(" ESTADO = 'False' ");
		consul.append(" WHERE " + CampoPK + " = '" + idRegUpdate + "'");
		String consulFinal = consul.toString();
		System.out.println(consulFinal);
		return consulFinal;
	}

	
}
