package la.netco.registro.uilayer.beans;

import java.util.Calendar;
import java.util.Date;

import la.netco.commons.exceptions.NumeroRadicacionException;
import la.netco.core.businesslogic.services.ServiceDao;
import la.netco.core.businesslogic.services.impl.SpringApplicationContext;
import la.netco.correspondencia.dto.custom.ConsecutivoRegistro;
import la.netco.persistencia.dto.commons.Configuracion;
import la.netco.persistencia.dto.commons.Registro;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

public class GenerarConsecutivoUtil {

	
	public static ConsecutivoRegistro obtenerConsecutivo(Registro registro) throws NumeroRadicacionException, Exception {
		

		ServiceDao serviceDao = (ServiceDao)SpringApplicationContext.getBean("serviceDao");
		
		Configuracion conf = (Configuracion) serviceDao.getGenericCommonDao().executeFind(Configuracion.NAMED_QUERY_GET_ALL_CONF).get(0);

		Date fechaReg = registro.getRegFec();
		if(fechaReg == null)
			fechaReg =new Date(System.currentTimeMillis());
		
		Integer idRegistro = registro.getRegId();
		
		Calendar fechaCal =Calendar.getInstance();
		fechaCal.setTime(conf.getHoralimite());
		Integer anio = fechaCal.get(Calendar.YEAR);
		String creCod= registro.getClaseregistro().getCreCod();

		Integer cod2 = 0;
		Integer cod3 = 0;
		Integer fij2 = conf.getRegFij2();
		Integer fij3 = conf.getRegFij3();
		String fij1 = conf.getRegFij1();
		
		boolean uti1 = conf.isRegUti1();
		boolean uti2 = conf.isRegUti2();
		boolean uti3 = conf.isRegUti3();
		
		int opc2 = conf.getRegOpc2();
		int opc1 = conf.getRegOpc1();

		int opc3 = conf.getRegOpc3();
		Session session = serviceDao.getGenericCommonDao().getSessionFromHibernate();
		
		Criteria criteria       = null;
		
		System.out.println(" probando unique cod3 " +cod3);
		//--3	[1-2]
		if(uti1){
				//--BB
				if(uti2){
					criteria = session.createCriteria(Registro.class);
					criteria.setProjection(Projections.max("regNo3"));
					criteria.add(Restrictions.ne("regId", idRegistro));
					cod3 = (Integer) criteria.uniqueResult();						
				}else{					
						if(opc2 == 0){//--BA
							criteria = session.createCriteria(Registro.class);
							criteria.setProjection(Projections.max("regNo3"));
							criteria.add(Restrictions.eq("regNo2", anio));
							criteria.add(Restrictions.ne("regId", idRegistro));
							cod3 = (Integer) criteria.uniqueResult();		
						}else if(opc2 == 1){//--BR
							cod3 = 0;
						}else if(opc2 == 2){//--BF

							criteria = session.createCriteria(Registro.class);
							criteria.setProjection(Projections.max("regNo3"));
							criteria.add(Restrictions.eq("regNo2", fij2));
							criteria.add(Restrictions.ne("regId", idRegistro));
							cod3 = (Integer) criteria.uniqueResult();							
						}else if(opc2 > 2){//--BI
							criteria = session.createCriteria(Registro.class);
							criteria.setProjection(Projections.property("regNo3"));
							criteria.add(Restrictions.ne("regId", idRegistro));
							criteria.addOrder(Order.desc("regNo2"));
							criteria.addOrder(Order.desc("regNo3"));
							criteria.setMaxResults(1);	
							cod3 = (Integer) criteria.list().get(0);
						}
				}
		}else{
			
			if(opc1 == 0){ //Año1
					if(uti2){//--AB
						criteria = session.createCriteria(Registro.class);
						criteria.setProjection(Projections.max("regNo3"));
						criteria.add(Restrictions.eq("regNo1", anio.toString()));
						criteria.add(Restrictions.ne("regId", idRegistro));
						cod3 = (Integer) criteria.uniqueResult();	
					}else{
						if(opc2 == 1) {//--AR
							cod3 = 0;
						}else {
							if(opc2 == 2){//--AF
								criteria = session.createCriteria(Registro.class);
								criteria.setProjection(Projections.max("regNo3"));
								criteria.add(Restrictions.eq("regNo1", anio.toString()));
								criteria.add(Restrictions.eq("regNo2", fij2));
								criteria.add(Restrictions.ne("regId", idRegistro));
								cod3 = (Integer) criteria.uniqueResult();	
							}else if(opc2 > 2){//--AI
								criteria = session.createCriteria(Registro.class);
								criteria.setProjection(Projections.property("regNo3"));
								criteria.add(Restrictions.eq("regNo1", anio.toString()));
								criteria.add(Restrictions.ne("regId", idRegistro));
								criteria.addOrder(Order.desc("regNo2"));
								criteria.addOrder(Order.desc("regNo3"));
								criteria.setMaxResults(1);	
								cod3 = (Integer) criteria.list().get(0);
							}
						}
					}
			}else if(opc1 == 1){ //REGISTRO1
				if(uti2){
					criteria = session.createCriteria(Registro.class);
					criteria.setProjection(Projections.max("regNo3"));
					criteria.add(Restrictions.eq("regNo1", creCod));
					criteria.add(Restrictions.ne("regId", idRegistro));
					cod3 = (Integer) criteria.uniqueResult();			
				}else{
					if(opc2 == 0){
						criteria = session.createCriteria(Registro.class);
						criteria.setProjection(Projections.max("regNo3"));
						criteria.add(Restrictions.eq("regNo1", creCod));
						criteria.add(Restrictions.eq("regNo2", anio));
						criteria.add(Restrictions.ne("regId", idRegistro));
						cod3 = (Integer) criteria.uniqueResult();	
					
					}else if(opc2 == 2){
						criteria = session.createCriteria(Registro.class);
						criteria.setProjection(Projections.max("regNo3"));
						criteria.add(Restrictions.eq("regNo1", creCod));
						criteria.add(Restrictions.eq("regNo2", fij2));
						criteria.add(Restrictions.ne("regId", idRegistro));
						cod3 = (Integer) criteria.uniqueResult();	
					}else if(opc2 > 2){
						criteria = session.createCriteria(Registro.class);
						criteria.setProjection(Projections.property("regNo3"));
						criteria.add(Restrictions.eq("regNo1", creCod));
						criteria.add(Restrictions.ne("regId", idRegistro));
						criteria.addOrder(Order.desc("regNo2"));
						criteria.addOrder(Order.desc("regNo3"));
						criteria.setMaxResults(1);	
						cod3 = (Integer) criteria.list().get(0);
					}
				}
			}else if(opc1 == 2){ // Fijo1
				if(uti2){
					criteria = session.createCriteria(Registro.class);
					criteria.setProjection(Projections.max("regNo3"));
					criteria.add(Restrictions.eq("regNo1", fij1));
					criteria.add(Restrictions.ne("regId", idRegistro));
					cod3 = (Integer) criteria.uniqueResult();
				}else{
					//FA
					if(opc2 == 0){
						criteria = session.createCriteria(Registro.class);
						criteria.setProjection(Projections.max("regNo3"));
						criteria.add(Restrictions.eq("regNo1", fij1));
						criteria.add(Restrictions.eq("regNo2", anio));
						criteria.add(Restrictions.ne("regId", idRegistro));
						cod3 = (Integer) criteria.uniqueResult();						
					//FR
					}else if(opc2 == 1){
						cod3 = 0;

					//FI
					}else if(opc2 > 2){
						criteria = session.createCriteria(Registro.class);
						criteria.setProjection(Projections.property("regNo3"));
						criteria.add(Restrictions.eq("regNo1", fij1));
						criteria.add(Restrictions.ne("regId", idRegistro));
						criteria.addOrder(Order.desc("regNo2"));
						criteria.addOrder(Order.desc("regNo3"));
						criteria.setMaxResults(1);	
						cod3 = (Integer) criteria.list().get(0);
					}
				}
			}else if(opc1 > 2){ // Incremento
				if(uti2){
					criteria = session.createCriteria(Registro.class);
					criteria.setProjection(Projections.property("regNo3"));
					criteria.add(Restrictions.ne("regId", idRegistro));
					criteria.addOrder(Order.desc("regNo1"));
					criteria.addOrder(Order.desc("regNo3"));
					criteria.setMaxResults(1);	
					cod3 = (Integer) criteria.list().get(0);
				}else{
					//--IA
					if(opc2 == 0){
						criteria = session.createCriteria(Registro.class);
						criteria.setProjection(Projections.property("regNo3"));
						criteria.add(Restrictions.eq("regNo2", anio));
						criteria.add(Restrictions.ne("regId", idRegistro));
						criteria.addOrder(Order.desc("regNo1"));
						criteria.addOrder(Order.desc("regNo3"));
						criteria.setMaxResults(1);	
						cod3 = (Integer) criteria.list().get(0);						
					}else if(opc2 == 1){
						cod3 = 0;
					}else if(opc2 > 2){
						criteria = session.createCriteria(Registro.class);
						criteria.setProjection(Projections.property("regNo3"));
						criteria.add(Restrictions.ne("regId", idRegistro));
						criteria.addOrder(Order.desc("regNo1"));
						criteria.addOrder(Order.desc("regNo2"));
						criteria.addOrder(Order.desc("regNo3"));
						criteria.setMaxResults(1);	
						cod3 = (Integer) criteria.list().get(0);
					}
				}
			}
				
		}	
		
		//--2	[1-3]
		if(uti1){
				if(uti3){
					criteria = session.createCriteria(Registro.class);
					criteria.setProjection(Projections.max("regNo2"));
					criteria.add(Restrictions.ne("regId", idRegistro));
					cod2 = (Integer) criteria.uniqueResult();
				}else{
						if(opc3 == 0){//--BA
							criteria = session.createCriteria(Registro.class);
							criteria.setProjection(Projections.max("regNo2"));
							criteria.add(Restrictions.eq("regNo3", anio));
							criteria.add(Restrictions.ne("regId", idRegistro));
							cod2 = (Integer) criteria.uniqueResult();		
						}else if(opc3 == 1){
							cod2 = 0;
						}else if(opc2 == 2){
							criteria = session.createCriteria(Registro.class);
							criteria.setProjection(Projections.max("regNo2"));
							criteria.add(Restrictions.eq("regNo3", fij3));
							criteria.add(Restrictions.ne("regId", idRegistro));
							cod2 = (Integer) criteria.uniqueResult();	
						}else if(opc2 > 2){
							criteria = session.createCriteria(Registro.class);
							criteria.setProjection(Projections.property("regNo2"));
							criteria.add(Restrictions.ne("regId", idRegistro));
							criteria.addOrder(Order.desc("regNo2"));
							criteria.addOrder(Order.desc("regNo3"));
							criteria.setMaxResults(1);	
							cod2 = (Integer) criteria.list().get(0);
						}
				}
		}else{
			
			if(opc1 == 0){ //Año1
					if(uti3){
						criteria = session.createCriteria(Registro.class);
						criteria.setProjection(Projections.max("regNo2"));
						criteria.add(Restrictions.eq("regNo1", anio.toString()));
						criteria.add(Restrictions.ne("regId", idRegistro));
						cod2 = (Integer) criteria.uniqueResult();
					}else{
						if(opc3 == 1) {
							cod2 = 0;
						}else if(opc3 == 2){
							criteria = session.createCriteria(Registro.class);
							criteria.setProjection(Projections.max("regNo2"));
							criteria.add(Restrictions.eq("regNo1", anio.toString()));
							criteria.add(Restrictions.eq("regNo3", fij3));
							criteria.add(Restrictions.ne("regId", idRegistro));
							cod2 = (Integer) criteria.uniqueResult();	
							}else if(opc3 > 2){
								criteria = session.createCriteria(Registro.class);
								criteria.setProjection(Projections.property("regNo2"));
								criteria.add(Restrictions.eq("regNo1", anio.toString()));
								criteria.add(Restrictions.ne("regId", idRegistro));
								criteria.addOrder(Order.desc("regNo2"));
								criteria.addOrder(Order.desc("regNo3"));
								criteria.setMaxResults(1);	
								cod2 = (Integer) criteria.list().get(0);
							}
			
					}
			}else if(opc1 == 1){ //REGISTRO1
				if(uti3){
					criteria = session.createCriteria(Registro.class);
					criteria.setProjection(Projections.max("regNo2"));
					criteria.add(Restrictions.eq("regNo1", creCod));
					criteria.add(Restrictions.ne("regId", idRegistro));
					cod2 = (Integer) criteria.uniqueResult();
				}else{
					if(opc3 == 0){
						criteria = session.createCriteria(Registro.class);
						criteria.setProjection(Projections.max("regNo2"));
						criteria.add(Restrictions.eq("regNo1", creCod));
						criteria.add(Restrictions.eq("regNo3", anio));
						criteria.add(Restrictions.ne("regId", idRegistro));
						cod2 = (Integer) criteria.uniqueResult();
					}else if(opc3 == 2){
						criteria = session.createCriteria(Registro.class);
						criteria.setProjection(Projections.max("regNo2"));
						criteria.add(Restrictions.eq("regNo1", creCod));
						criteria.add(Restrictions.eq("regNo3", fij3));
						criteria.add(Restrictions.ne("regId", idRegistro));
						cod2 = (Integer) criteria.uniqueResult();	
					}else if(opc3 > 2){
						criteria = session.createCriteria(Registro.class);
						criteria.setProjection(Projections.property("regNo2"));
						criteria.add(Restrictions.eq("regNo1", creCod));
						criteria.add(Restrictions.ne("regId", idRegistro));
						criteria.addOrder(Order.desc("regNo2"));
						criteria.addOrder(Order.desc("regNo3"));
						criteria.setMaxResults(1);	
						cod2 = (Integer) criteria.list().get(0);
					}
				}
			}else if(opc1 == 2){ // Fijo1
				if(uti3){
					criteria = session.createCriteria(Registro.class);
					criteria.setProjection(Projections.max("regNo2"));
					criteria.add(Restrictions.eq("regNo1", fij1));
					criteria.add(Restrictions.ne("regId", idRegistro));
					cod2 = (Integer) criteria.uniqueResult();
				}else{
					//FA
					if(opc3 == 0){
						criteria = session.createCriteria(Registro.class);
						criteria.setProjection(Projections.max("regNo2"));
						criteria.add(Restrictions.eq("regNo1", fij1));
						criteria.add(Restrictions.eq("regNo3", anio));
						criteria.add(Restrictions.ne("regId", idRegistro));
						cod2 = (Integer) criteria.uniqueResult();	
					//FR
					}else if(opc3 == 1){
						cod2 = 0;

					//FI
					}else if(opc3 > 2){
						criteria = session.createCriteria(Registro.class);
						criteria.setProjection(Projections.property("regNo2"));
						criteria.add(Restrictions.eq("regNo1", fij1));
						criteria.add(Restrictions.ne("regId", idRegistro));
						criteria.addOrder(Order.desc("regNo2"));
						criteria.addOrder(Order.desc("regNo3"));
						criteria.setMaxResults(1);	
						cod2 = (Integer) criteria.list().get(0);
					}
				}
			}else if(opc1 > 2){ // Incremento
				if(uti3){
					criteria = session.createCriteria(Registro.class);
					criteria.setProjection(Projections.property("regNo2"));
					criteria.add(Restrictions.ne("regId", idRegistro));
					criteria.addOrder(Order.desc("regNo1"));
					criteria.addOrder(Order.desc("regNo2"));
					criteria.setMaxResults(1);	
					cod2 = (Integer) criteria.list().get(0);
				}else{
					//--IA
					if(opc3 == 0){
						criteria = session.createCriteria(Registro.class);
						criteria.setProjection(Projections.property("regNo2"));
						criteria.add(Restrictions.eq("regNo3", anio));
						criteria.add(Restrictions.ne("regId", idRegistro));
						criteria.addOrder(Order.desc("regNo1"));
						criteria.addOrder(Order.desc("regNo2"));
						criteria.setMaxResults(1);	
						cod2 = (Integer) criteria.list().get(0);
					}else if(opc3 == 1){
						cod2 = 0;
					}else if(opc3 > 2){
						criteria = session.createCriteria(Registro.class);
						criteria.setProjection(Projections.property("regNo2"));
						criteria.add(Restrictions.ne("regId", idRegistro));
						criteria.addOrder(Order.desc("regNo1"));
						criteria.addOrder(Order.desc("regNo2"));
						criteria.addOrder(Order.desc("regNo3"));
						criteria.setMaxResults(1);	
						cod3 = (Integer) criteria.list().get(0);
					}
				}
			}
				
		}

		String tmp1 ="";
		String tmp2 = "";
		String tmp3 ="";
		
		int ter3 = conf.getRegTer3();
		int terc2 = conf.getRegTerc2();
		int ini3 = conf.getRegIni3();
		int ini2 = conf.getRegIni2();
		
		int inic2 = conf.getRegInic2();
		int lsw3 = 0;
		
		//-- Parte 3
		if(!uti3){			
			if(opc3 == 0){//  -- Año
				tmp3 = anio.toString();
			}else if(opc3 == 1){// -- Codigo del REGISTRO
				tmp3 = "";
			}else if(opc3 == 2){// -- Valor Fijo
				tmp3 = fij3.toString().trim();
			}else if(opc3 == 3){ //-- Auto-Incrementar	
				if(cod3 == 0){
					cod3 = ini3;
				}else{
					cod3 = cod3 + 1;
				}
				
				if(cod3 > ter3){
					cod3 = ini3;
					lsw3 = 1;
				}
				tmp3 = cod3.toString();
			}
		
		
		}
		// -- Parte 2
		if (!uti2) {
			if (opc2 == 0) { // -- Año
				tmp2 = anio.toString();
			} else if (opc2 == 1) {// -- Codigo del REGISTRO
				tmp2 = "";

			} else if (opc2 == 2) { // -- Valor Fijo
				tmp2 = fij2.toString().trim();
			} else if (opc2 == 3) { // -- Auto-Incrementar
				if (cod2 == 0) {
					cod2 = ini2;
				} else {
					cod2 = cod2 + 1;
				}
				tmp2 = cod2.toString();
			} else if (opc2 == 4) {// -- Incrementar + Continuación

				if (cod2 == 0) {
					cod2 = inic2;
				} else {
					if (lsw3 == 1) {
						cod2 =cod2 + 1;
					}
				}

				if (cod2 > terc2) {
					cod2 = inic2;
				}		
				tmp2 = cod2.toString().trim();
			}
		}
	

		//-- Parte 1
		if (!uti1) {
			tmp1 = "";
			if (opc1 == 0) {// -- Año
				tmp1 = anio.toString();

			} else if (opc1 == 1) {// -- Codigo del REGISTRO
				tmp1 = creCod;

			} else if (opc1 == 2) {// -- Valor Fijo
				tmp1 = fij1.toString().trim();
			}
		}
		
		String sep1;
		String sep2;
		
		//-- 1 separador
		if (tmp1.length() > 0 && tmp2.length() > 0) 
			sep1 = "-";
		else
			sep1 = "";

		//-- 2 separador
		if ((tmp1.length() > 0 || tmp2.length()> 0)  && tmp3.length() > 0) 
			sep2 = "-";
		else
			sep2 = "";

		String codigo = tmp1.trim()+sep1+tmp2.trim()+sep2+tmp3.trim();

		System.out.println("codigo : "  + codigo);
		
		
		ConsecutivoRegistro consecutivo = new ConsecutivoRegistro();
		consecutivo.setRegNo1(Integer.parseInt(tmp1));
		consecutivo.setRegNo2(Integer.parseInt(tmp2));
		consecutivo.setRegNo3(Integer.parseInt(tmp3));
		consecutivo.setConsecutivoCompleto(codigo);
		
		criteria = session.createCriteria(Registro.class);
		criteria.setProjection(Projections.count("regId"));
		criteria.add(Restrictions.eq("regCod", codigo));

		Long registros = (Long) criteria.uniqueResult();
		
		if(registros != null && registros.intValue() > 0){
			throw new NumeroRadicacionException("ERROR AL GENERAR CONSECUTIVO");
		}
		
		return consecutivo;
	}
	
	public static ConsecutivoRegistro obtenerConsecutivoManual(Registro registro, String regNo1, Integer regNo2, Integer regNo3, Date regFec) throws NumeroRadicacionException, Exception {
		
		
		ServiceDao serviceDao = (ServiceDao)SpringApplicationContext.getBean("serviceDao");
		
		Configuracion conf = (Configuracion) serviceDao.getGenericCommonDao().executeFind(Configuracion.NAMED_QUERY_GET_ALL_CONF).get(0);

		Session session = serviceDao.getGenericCommonDao().getSessionFromHibernate();
		
		Criteria criteria       = null;
		
		boolean uti1 = conf.isRegUti1();
		boolean uti2 = conf.isRegUti2();
		boolean uti3 = conf.isRegUti3();

		
		String codigo = "";

		if(uti1){
			regNo1 = "";
		}else{
			codigo = regNo1.trim();
		}
		
		if(uti2){
			regNo2 = 0;
		}else{
				if(!codigo.equals("")){
					codigo= codigo + "-";					
				}
				codigo =codigo +regNo2.toString();				
		}
		if(uti3){
			regNo3 = 0;
		}else{
			if(!codigo.equals("")){
				codigo= codigo + "-";
			}
			codigo =codigo +regNo3.toString();		
		}
		
		
		ConsecutivoRegistro consecutivo = new ConsecutivoRegistro();
		consecutivo.setRegNo1(Integer.parseInt(regNo1));
		consecutivo.setRegNo2(regNo2);
		consecutivo.setRegNo3(regNo3);
		consecutivo.setConsecutivoCompleto(codigo);
		
		criteria = session.createCriteria(Registro.class);
		criteria.setProjection(Projections.count("regId"));
		criteria.add(Restrictions.eq("regCod", codigo));

		Long registros = (Long) criteria.uniqueResult();
		
		if(registros != null && registros.intValue() > 0){
			throw new NumeroRadicacionException("ERROR AL GENERAR CONSECUTIVO");
		}
		
		return consecutivo;	
	}
	
	public static String obtenerReg1(Registro registro) {
		
		
		ServiceDao serviceDao = (ServiceDao)SpringApplicationContext.getBean("serviceDao");
		
		Configuracion conf = (Configuracion) serviceDao.getGenericCommonDao().executeFind(Configuracion.NAMED_QUERY_GET_ALL_CONF).get(0);
		
		int opc1 = conf.getRegOpc1();
		Calendar fechaCal =Calendar.getInstance();
		fechaCal.setTime(conf.getHoralimite());
		Integer anio = fechaCal.get(Calendar.YEAR);
		String creCod= registro.getClaseregistro().getCreCod();
		String fij1 = conf.getRegFij1();
		String	regNo1 = "";
			if (opc1 == 0) {// -- Año
				regNo1 = anio.toString();

			} else if (opc1 == 1) {// -- Codigo del REGISTRO
				regNo1 = creCod;

			} else if (opc1 == 2) {// -- Valor Fijo
				regNo1 = fij1.toString().trim();
			}
		
		return regNo1;

	}
		
	
}
