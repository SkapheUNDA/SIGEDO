package la.netco.correspondencia.dao.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import la.netco.commons.exceptions.NumeroRadicacionException;
import la.netco.core.persitencia.dao.commos.GenericCommonDaoHibernateImpl;
import la.netco.correspondencia.dao.EntradaSalidaDao;
import la.netco.correspondencia.dto.custom.Consecutivo;
import la.netco.persistencia.dto.commons.Configuracion;
import la.netco.persistencia.dto.commons.Diasfestivos;
import la.netco.persistencia.dto.commons.Entrada;
import la.netco.persistencia.dto.commons.Registro;
import la.netco.persistencia.dto.commons.Salida;

import org.springframework.stereotype.Service;

@Service("entradaDao")
public class EntradaSalidaDaoImpl extends GenericCommonDaoHibernateImpl implements EntradaSalidaDao {

	public Consecutivo obtenerConsecutivo(String depCod, Integer tipoCorrespondencia) throws NumeroRadicacionException, Exception {
		Consecutivo consecutivo = new Consecutivo();
		String consecutivoCompleto;

		Configuracion conf = (Configuracion) executeFind(Configuracion.NAMED_QUERY_GET_ALL_CONF).get(0);

		String prefijo = null;
		if(tipoCorrespondencia.equals(Consecutivo.TIPO_ENTRADA))
			prefijo = conf.getEntPre();
		else
			prefijo = conf.getSalPre();
		
		consecutivoCompleto = prefijo;
		if (Integer.parseInt(prefijo) > 0) {
			consecutivoCompleto = consecutivoCompleto + "-";
		}

		Integer opcionConf = conf.getEntOpc();
		Integer consecutivoNum = null;

		if (opcionConf.equals(Configuracion.ENT_OPC_CONSECUTIVO)) {
			
			if(tipoCorrespondencia.equals(Consecutivo.TIPO_ENTRADA))
				consecutivoNum = (Integer) executeUniqueResult(Entrada.NAMED_QUERY_GET_MAX_ALL, null);
			else if(tipoCorrespondencia.equals(Consecutivo.TIPO_SALIDA))
				consecutivoNum = (Integer) executeUniqueResult(Salida.NAMED_QUERY_GET_MAX_ALL, null);
			
		
			consecutivoCompleto = consecutivoCompleto + (consecutivoNum+1);

		} else if (opcionConf.equals(Configuracion.ENT_OPC_ANIO_CONSECUTIVO))  {
			Map<Integer, Object> params = new HashMap<Integer, Object>();
			int year = Calendar.getInstance().get(Calendar.YEAR);
			params.put(0, year);
			
			if(tipoCorrespondencia.equals(Consecutivo.TIPO_ENTRADA))
				consecutivoNum = (Integer) executeUniqueResult(Entrada.NAMED_QUERY_GET_MAX_BY_YEAR, params);
			else if(tipoCorrespondencia.equals(Consecutivo.TIPO_SALIDA))
				consecutivoNum = (Integer) executeUniqueResult(Salida.NAMED_QUERY_GET_MAX_BY_YEAR, params);
			
			if(consecutivoNum ==null)
			{
				consecutivoNum = 0;
				consecutivoNum = consecutivoNum + 1;
			}
			else
			{
				consecutivoNum = consecutivoNum + 1;
			}
			
			consecutivoCompleto = consecutivoCompleto + year + "-" + consecutivoNum;
		}else if (opcionConf.equals(Configuracion.ENT_OPC_DEPEND_CONSECUTIVO))  {
			Map<Integer, Object> params = new HashMap<Integer, Object>();
			params.put(0, depCod);
			if(tipoCorrespondencia.equals(Consecutivo.TIPO_ENTRADA))
				consecutivoNum = (Integer) executeUniqueResult(Entrada.NAMED_QUERY_GET_MAX_BY_COD_DEPEND, params);
			else if(tipoCorrespondencia.equals(Consecutivo.TIPO_SALIDA))
				consecutivoNum = (Integer) executeUniqueResult(Salida.NAMED_QUERY_GET_MAX_BY_COD_DEPEND, params);
			
			consecutivoCompleto = consecutivoCompleto + depCod + "-" + (consecutivoNum+1);
		}else if (opcionConf.equals(Configuracion.ENT_OPC_DEPEND_ANIO_CONSECUTIVO))  {
			int year = Calendar.getInstance().get(Calendar.YEAR);
			Map<Integer, Object> params = new HashMap<Integer, Object>();
			params.put(0, depCod);
			params.put(1, year);

			if(tipoCorrespondencia.equals(Consecutivo.TIPO_ENTRADA))
				consecutivoNum = (Integer) executeUniqueResult(Entrada.NAMED_QUERY_GET_MAX_BY_COD_DEPEN_YEAR, params);
			else if(tipoCorrespondencia.equals(Consecutivo.TIPO_SALIDA))
				consecutivoNum = (Integer) executeUniqueResult(Salida.NAMED_QUERY_GET_MAX_BY_COD_DEPEN_YEAR, params);
			
			if(consecutivoNum ==null)
			{
				consecutivoNum = 0;
				consecutivoNum = consecutivoNum + 1;
			}
			else
			{
				consecutivoNum = consecutivoNum + 1;
			}
			
			consecutivoCompleto = consecutivoCompleto + depCod + "-" + year + "-" + consecutivoNum;
		}else if (opcionConf.equals(Configuracion.ENT_OPC__ANIO_DEPEND_CONSECUTIVO))  {
			int year = Calendar.getInstance().get(Calendar.YEAR);
			Map<Integer, Object> params = new HashMap<Integer, Object>();
			params.put(0, depCod);
			params.put(1, year);
			
			if(tipoCorrespondencia.equals(Consecutivo.TIPO_ENTRADA))
				consecutivoNum = (Integer) executeUniqueResult(Entrada.NAMED_QUERY_GET_MAX_BY_COD_DEPEN_YEAR, params);
			else if(tipoCorrespondencia.equals(Consecutivo.TIPO_SALIDA))
				consecutivoNum = (Integer) executeUniqueResult(Salida.NAMED_QUERY_GET_MAX_BY_COD_DEPEN_YEAR, params);
			
			if(consecutivoNum ==null)
			{
				consecutivoNum = 0;
				consecutivoNum = consecutivoNum + 1;
			}
			else
			{
				consecutivoNum = consecutivoNum + 1;
			}
			
			consecutivoCompleto = consecutivoCompleto + year + "-" + depCod + "-" + consecutivoNum;
		}
		
		if(consecutivoNum == null || consecutivoNum.equals(0)){
			throw new NumeroRadicacionException("ERROR AL GENERAR CONSECUTIVO");
		}
		consecutivo.setConsecutivo(consecutivoNum + 1);
		consecutivo.setConsecutivoCompleto(consecutivoCompleto);

		return consecutivo;
	}

	
	public Date obtenerFechaRadicacion() {

		Configuracion conf = (Configuracion) executeFind(Configuracion.NAMED_QUERY_GET_ALL_CONF).get(0);
		Date fechaRadicacion = null;
		
		Calendar tiempoLimite =Calendar.getInstance();
		tiempoLimite.setTime(conf.getHoralimite());
		
		Calendar timeInicio = Calendar.getInstance();
		timeInicio.setTime(conf.getHorainicio());
		
		boolean festivo = false;
		Calendar fechaActual = Calendar.getInstance();
		fechaActual.set(Calendar.HOUR_OF_DAY, 0);
		fechaActual.set(Calendar.MINUTE, 0);
		fechaActual.set(Calendar.SECOND, 0);
		
		Map<Integer, Object> params = new HashMap<Integer, Object>();
		params.put(0, fechaActual.getTime());
		
		List<?> diafestivo = executeFind(Diasfestivos.class, params, Diasfestivos.NAMED_QUERY_GET_BY_DATE);
		if(diafestivo != null && !diafestivo.isEmpty()){
			festivo = true;
		}
		
		fechaActual =	Calendar.getInstance();
		
		Integer minutosActual = fechaActual.get(Calendar.MINUTE) + ( fechaActual.get(Calendar.HOUR_OF_DAY)*60);
		
		Integer minutosLimite = tiempoLimite.get(Calendar.MINUTE) + ( tiempoLimite.get(Calendar.HOUR_OF_DAY)*60);
	
		if(festivo == false && minutosActual > minutosLimite ){
			fechaActual.add(Calendar.DAY_OF_MONTH, 1);
			Map<Integer, Object> paramsDias = new HashMap<Integer, Object>();
			paramsDias.put(0, fechaActual.getTime());
			List<?> diafestivos = executeFind(Diasfestivos.class, paramsDias, Diasfestivos.NAMED_QUERY_GET_BY_DATE);
			while(diafestivos != null && !diafestivos.isEmpty()){
				 diafestivos = executeFind(Diasfestivos.class, paramsDias, Diasfestivos.NAMED_QUERY_GET_BY_DATE);
				 fechaActual.add(Calendar.DAY_OF_MONTH, 1);
			}
			
			fechaActual.set(Calendar.HOUR_OF_DAY, timeInicio.get(Calendar.HOUR_OF_DAY));
			fechaActual.set(Calendar.MINUTE, timeInicio.get(Calendar.MINUTE));
			
			fechaRadicacion = fechaActual.getTime();
			
		}else{
			fechaRadicacion =  Calendar.getInstance().getTime();
		}
		return fechaRadicacion;
	}
	
	public Date obtenerFechaRespuesta(Entrada entrada) {
		
		Date fechaRadicacion = entrada.getEntFen();
		Calendar fechaRespuesta = Calendar.getInstance();
		fechaRespuesta.setTime(fechaRadicacion);
		fechaRespuesta.set(Calendar.HOUR_OF_DAY, 0);
		fechaRespuesta.set(Calendar.MINUTE, 0);
		fechaRespuesta.set(Calendar.SECOND, 0);
		
		Short diasLimite = entrada.getEntTrt();
		Byte tipoRespuesta = entrada.getClasificacion().getClaTrt();
		
		short i = 1;
		while (i <= diasLimite) {
			fechaRespuesta.add(Calendar.DAY_OF_MONTH, 1);
			int dia = fechaRespuesta.get(Calendar.DAY_OF_WEEK);
			if (tipoRespuesta == 1 && (dia == Calendar.SUNDAY || dia == Calendar.SATURDAY)) {
				//Si son días hábiles y además es sábado o domingo...
				i++;
			} else {
				if (tipoRespuesta == 1) {
					//Si se trata de días hábiles
					Map<Integer, Object> paramsDias = new HashMap<Integer, Object>();
					paramsDias.put(0, fechaRespuesta.getTime());
					List<?> diafestivos = executeFind(Diasfestivos.class, paramsDias, Diasfestivos.NAMED_QUERY_GET_BY_DATE);
					if(diafestivos == null || diafestivos.isEmpty()) {
						//Incrementa si no es festivo
						i++;
					}
				} else {
					//Siempre incrementa
					i++;
				}
			}
		}
		
		return fechaRespuesta.getTime();
	}
	
	public long obtenerDiffDiasHabiles(Date desde, Date hasta) {
		long diff = 0;
		
		Calendar fechaIni = Calendar.getInstance();
		fechaIni.setTime(desde);
		fechaIni.set(Calendar.HOUR_OF_DAY, 0);
		fechaIni.set(Calendar.MINUTE, 0);
		fechaIni.set(Calendar.SECOND, 0);
		
		
		Calendar fechaFin = Calendar.getInstance();
		fechaFin.setTime(hasta);
		fechaFin.set(Calendar.HOUR_OF_DAY, 23);	
		fechaFin.set(Calendar.MINUTE, 59);
		fechaFin.set(Calendar.SECOND, 59);
	    diff = (fechaIni.getTimeInMillis() - fechaFin.getTimeInMillis() )/ (24 * 60 * 60 * 1000);	
	    
	    
	    System.out.println("   >>>>> " + diff);
	    Map<Integer, Object> paramsDias = new HashMap<Integer, Object>();
		
	    if(fechaIni.compareTo(fechaFin) <= 0 ){
		    paramsDias.put(0, fechaIni.getTime());
			paramsDias.put(1, fechaFin.getTime());
	    }else{
		    paramsDias.put(0, fechaFin.getTime());
			paramsDias.put(1, fechaIni.getTime());
	    	
	    }
	    
		
		Long diafestivos = (Long)executeUniqueResult(Diasfestivos.NAMED_QUERY_COUNT_BETWEEN, paramsDias);
		System.out.println("   >>>>> " + diafestivos);
		if(diff >= diafestivos )
			diff = diff - diafestivos; 
		else
			diff = diff + diafestivos;
		
		return diff;
	}
	
	
	
}
