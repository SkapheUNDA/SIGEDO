/**
 * 
 */
package la.netco.sgc.uilayer.beans.util;

import java.io.FileInputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedProperty;

import la.netco.core.businesslogic.services.ServiceDao;
import la.netco.core.uilayer.beans.utils.FacesUtils;
import la.netco.sgc.persistence.dto.CortesFormato;
import la.netco.sgc.persistence.dto.DetallesFormato;
import la.netco.sgc.persistence.dto.Entidades;
import la.netco.sgc.persistence.dto.Formatos;
import la.netco.sgc.persistence.dto.FormatosTiposDato;
import la.netco.sgc.persistence.dto.PUC;
import la.netco.sgc.persistence.dto.RegistroCargue;
import la.netco.sgc.persistence.dto.RegistroCargueFormato;
import la.netco.sgc.persistence.dto.TiposDato;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 
 * 
 * @author cguzman
 * 
 */
/**
 * @author cpineros
 * 
 */
public class CargueArchivoExcel extends ICargueArchivo {

	private final Formatos formatoCargue;

	private final List<DetallesFormato> detallesFormato;

	private final Entidades entidad;

	private final List<TiposDato> listaTiposDatos;

	private final List<FormatosTiposDato> listaFormaosTiposDatos;

	private List<String> listaLlaves;
	
	private List<RegistroCargue> registroCargues;
	
	private String valorActivo;
	private String valorPasivo;
	private String valorCapital;

	private CortesFormato corte;

	private boolean esActualizacion;
	
	private int filaEncabezado;

	private static final String MSJ_CUENTASERROR = "Activos diferente a Pasivo + Capital";

	@ManagedProperty(value = "#{serviceDao}")
	transient private ServiceDao serviceDao;

	@ManagedProperty(value = "#{mapaGuardaValores}")
	private MapaGuardaValores mapaGuardaValores;

	public CargueArchivoExcel(ServiceDao serviceDao, Formatos formatoCargue, Entidades entidad, List<DetallesFormato> detallesFormato,
			List<TiposDato> tiposDatos, List<FormatosTiposDato> formatosTiposDatos) {
		this.formatoCargue = formatoCargue;
		this.detallesFormato = detallesFormato;
		this.entidad = entidad;

		this.listaTiposDatos = tiposDatos;
		this.listaFormaosTiposDatos = formatosTiposDatos;
		this.serviceDao = serviceDao;
	}

	/**
	 * Metodo para cargar archivos con tipo de validaciones unicas
	 * 
	 * @param archivos
	 * @throws Exception
	 */
	@Override
	@SuppressWarnings("unused")
	public void cargar(List<Archivo> archivos, String tipoValidacion) throws Exception {

		// Declaraciones de Formato Detallado
		FileInputStream fis = null;					

		// Declaraciones Generales en metodo y clase
		listaLlaves = new ArrayList<String>();			

				BucleArchivos: for (Archivo archivo : archivos) {

					Workbook libro = null;

					try {

						fis = new FileInputStream(archivo.getNombre());
						libro = ("xlsx".equalsIgnoreCase(archivo.getExtension())) ? new XSSFWorkbook(fis) : new HSSFWorkbook(fis);

					} catch (Exception e) {
						ErrorGenerico error = new ErrorGenerico();
						String mensaje = "No se obtuvo archivo a procesar: " + e.getMessage();
						error.setMensaje(mensaje);

						archivo.getErrores().add(error);
						continue;
					}

					if (libro == null) {

						ErrorGenerico error = new ErrorGenerico();
						String mensaje = "No se logro cargar el libro dentro del archivo a procesar";
						error.setMensaje(mensaje);

						archivo.getErrores().add(error);
						continue BucleArchivos;
					}
					
				
					cargarFormato(libro, archivos, tipoValidacion);
				

			} 
		
	}
	
	
	
	private void cargarFormato(Workbook libro, List<Archivo> archivos, String tipoValidacion) throws Exception{
		
		String campoSaldoInicial = FacesUtils.getPropsSGC().getString("campoSaldoInicial");
		String campoSaldoFinal = FacesUtils.getPropsSGC().getString("campoSaldoFinal");
		String campoCuenta = FacesUtils.getPropsSGC().getString("campoCuenta");
		String campoDebito = FacesUtils.getPropsSGC().getString("campoValorDebito");
		String campoCredito = FacesUtils.getPropsSGC().getString("campoValorCredito");				
		String campoIdentificacionSocio = FacesUtils.getPropsSGC().getString("campoIdentificacionSocio");
		String campoActor = FacesUtils.getPropsSGC().getString("campoActor");
		String campoIdentificacionActor = FacesUtils.getPropsSGC().getString("campoIdentificacionActor");
		
		String campoCategoria = FacesUtils.getPropsSGC().getString("campoCategoria");				
		String campoValorNeto = FacesUtils.getPropsSGC().getString("campoValorNeto");
		String campoIdentificacionTercero = FacesUtils.getPropsSGC().getString("campoIdentificacionTercero");
		String campoCobroJuridico = FacesUtils.getPropsSGC().getString("campoCobroJuridico");
		String campoRecaudo = FacesUtils.getPropsSGC().getString("campoRecaudo");
		String campoRepartosNetos = FacesUtils.getPropsSGC().getString("campoRepartosNetos");				
		String campoNombreTercero = FacesUtils.getPropsSGC().getString("campoNombreTercero");
		
		Integer corteAsignado = formatoCargue.getCorteAsignado();
		corte = (CortesFormato) serviceDao.getCortesFormatoDao().read(CortesFormato.class, corteAsignado);

		BucleArchivos: for (Archivo archivo : archivos) {
		String nombreHoja = formatoCargue.getForNombreHoja();
		Sheet hoja = null;

		filaEncabezado = (formatoCargue.getForLineaInicial() != null) ? formatoCargue.getForLineaInicial() : 0;

		if (nombreHoja == null) {

			hoja = libro.getSheetAt(0);

		} else {

			hoja = libro.getSheet(nombreHoja);
		}

		if (hoja == null || hoja.getRow(filaEncabezado) == null) {

			ErrorGenerico error = new ErrorGenerico();
			String mensaje = "No se encontró la hoja con nombre '" + nombreHoja + "',  indicada en la parametrización del formato";
			error.setMensaje(mensaje);

			archivo.getErrores().add(error);
			continue;
		}

		Row encabezado = hoja.getRow(filaEncabezado);
		int numeroTotalColumnas = formatoCargue.getForTotalCampos();
		int numColumna = 0;

		List<String> listaColumnas = new ArrayList<String>();

		BucleEncabezado: for (; numColumna < 256; numColumna++) {

			Cell celda = encabezado.getCell((short) numColumna);

			if ((celda != null) && (celda.getCellType() == Cell.CELL_TYPE_STRING)) {

				String valorEncabezado = celda.getStringCellValue();

				if (valorEncabezado != null) {
					listaColumnas.add(valorEncabezado);
				} else {

					break BucleEncabezado;
				}

			} else {

				break BucleEncabezado;
			}
		}

		if (listaColumnas.size() == 0 || !(listaColumnas.size() == numeroTotalColumnas)) {
			ErrorGenerico error = new ErrorGenerico();
			String mensaje = "La cantidad de campos del archivo: ("+listaColumnas.size()+") no concuerda con la cantidad indicada en la parametrizacion del formato: ("+numeroTotalColumnas+")";
			error.setMensaje(mensaje);

			archivo.getErrores().add(error);
			continue BucleArchivos;
		}

		boolean errorNombresColumnas = false;
		List<Columna> columnas = new ArrayList<Columna>();
		int pos = 0;

		BucleValidarCampos: for (String nombreColumna : listaColumnas) {
			DetallesFormato colDef=null;
			Boolean encontroColumna = tomarDefinicionColumna(nombreColumna,pos);
			if (encontroColumna){
				colDef=	detallesFormato.get(pos);
			}			

			if (colDef == null) {

				ErrorGenerico error = new ErrorGenerico();
				String mensaje = "El nombre de la columna '" + nombreColumna
						+ "' del archivo, no concuerda.  Debe ser: "+detallesFormato.get(pos).getDetNombre();
				error.setMensaje(mensaje);

				archivo.getErrores().add(error);
				errorNombresColumnas = true;
				pos++;
			} else {

				if (pos != colDef.getDetPosicion()) {

					ErrorGenerico error = new ErrorGenerico();
					String mensaje = "La posicion de la columna '" + nombreColumna
							+ "' en el archivo, no concuerda la posicion indicada en la parametrizacion del formato";
					error.setMensaje(mensaje);

					archivo.getErrores().add(error);
					errorNombresColumnas = true;

				} else {

					Columna col = new Columna();
					col.setPoscicion(pos);

					TiposDato tdatos = tomarTipoDatoColumna(colDef.getTpdCodigo());
					col.setTipoDato(tdatos.getTpdMapeo());

					FormatosTiposDato fdatos = tomarFormatosTiposDato(colDef.getFtdCodigo());
					col.setFormato(fdatos.getFtdFormato());

					col.setNombre(nombreColumna);
					col.setRequerido(colDef.getDetRequerido());
					col.setDetalle(colDef);

					columnas.add(col);
				}

				pos++;
			}
		}

		if (errorNombresColumnas) {
			continue BucleArchivos;
		}

		List<Fila> filas = new ArrayList<Fila>();

		Fila cabecera = new Fila();
		cabecera.setColumnas(columnas);
		cabecera.setEsEncabezado(true);
		cabecera.setId(0);

		filas.add(cabecera);

		BucleFilas: for (int idFila = 1, numeroFila = filaEncabezado + 1; numeroFila <= hoja.getLastRowNum(); numeroFila++, idFila++) {

			Row row = hoja.getRow(numeroFila);

			Fila fila = new Fila();		
			List<ErrorGenerico> errores = new ArrayList<ErrorGenerico>();
			fila.setId(idFila);

			List<Columna> columnasDatos = new ArrayList<Columna>();

			BucleCeldas: for (int col = 0; col < columnas.size(); col++) {

				Cell celda = row.getCell((short) col);

				Columna columnaDato = new Columna();
				columnaDato.setFormato(columnas.get(col).getFormato());
				columnaDato.setNombre(columnas.get(col).getNombre());
				columnaDato.setPoscicion(columnas.get(col).getPoscicion());
				columnaDato.setTipoDato(columnas.get(col).getTipoDato());
				columnaDato.setDetalle(columnas.get(col).getDetalle());
				columnaDato.setRequerido(columnas.get(col).isRequerido());
				
				String textoFallo = "ERROR: Fila " + idFila + ", Columna " + col + "(" + columnaDato.getNombre() + "), Detalles : ";

				if (((celda == null) || (celda.getCellType() == Cell.CELL_TYPE_BLANK)) && columnaDato.isRequerido()) {
					textoFallo = "La celda está vacía";
					ErrorGenerico error = new ErrorGenerico();
					error.setIdFila(idFila+filaEncabezado + 1);
					error.setColumna(columnaDato.getNombre());
					error.setMensaje(textoFallo);
					error.setValor(textoFallo);
					columnaDato.setError(error);
					errores.add(error);
					fila.setErrores(errores);

					columnasDatos.add(columnaDato);
					continue BucleCeldas;
				} else if (((celda == null) || (celda.getCellType() == Cell.CELL_TYPE_BLANK)) && !columnaDato.isRequerido() && !tipoValidacion.equals(FormatosEnum.BALANCE_TERCEROS.getId())) {
					continue BucleCeldas;
				}

				if ("N".equalsIgnoreCase(columnaDato.getTipoDato()) || "M".equalsIgnoreCase(columnaDato.getTipoDato())) {
					Boolean errorReturn=Boolean.FALSE;
					if (celda.getCellType() == Cell.CELL_TYPE_NUMERIC || celda.getCellType() == Cell.CELL_TYPE_FORMULA) {

						try {

							Double valor = celda.getNumericCellValue();
							String valorFormateado = formatearDouble(valor, columnaDato.getFormato());
							columnaDato.setValor(valorFormateado);	
							
							if (tipoValidacion.equals(FormatosEnum.BALANCE_GENERAL.getId()) || tipoValidacion.equals(FormatosEnum.BALANCE_TERCEROS.getId()) || tipoValidacion.equals(FormatosEnum.EJECUCION_PRESUPUESTAL.getId()) || tipoValidacion.equals(FormatosEnum.DETALLADO_DE_BIENESTAR_SOCIAL.getId()) || tipoValidacion.equals(FormatosEnum.INFORME_ANUAL_DE_GESTION_COLECTIVA.getId()) || tipoValidacion.equals(FormatosEnum.INFORME_ANUAL_DE_RESERVAS_APROBADAS.getId()) || tipoValidacion.equals(FormatosEnum.INFORME_ANUAL_DE_CARTERA_CASTIGADA.getId()) || tipoValidacion.equals(FormatosEnum.INFORME_ANUAL_DE_CARTERA_VENCIDA.getId()) || tipoValidacion.equals(FormatosEnum.INFORME_ANUAL_DE_DONACIONES.getId()) || tipoValidacion.equals(FormatosEnum.INFORME_TRIMESTRAL_DE_RECAUDOS.getId())) {							
								errorReturn=validarCuenta(campoCuenta, columnaDato, valorFormateado, errores, idFila, fila, columnasDatos);
								if (errorReturn){
									continue BucleCeldas;
								}
							}
							if (tipoValidacion.equals(FormatosEnum.BALANCE_GENERAL.getId()) || tipoValidacion.equals(FormatosEnum.BALANCE_TERCEROS.getId())) {
								errorReturn=validarCamposBalanceGeneral(campoDebito, campoCredito, campoSaldoInicial, campoSaldoFinal, campoCuenta, columnaDato, valorFormateado, errores, idFila, fila, columnasDatos);
								if (errorReturn){
									continue BucleCeldas;
								}
							}
								if (tipoValidacion.equals(FormatosEnum.EJECUCION_PRESUPUESTAL.getId())){
									errorReturn=validarTipoDatoMenorACero(valor, idFila, columnaDato, errores, fila, columnasDatos);
									if (errorReturn){
										continue BucleCeldas;
									}
								}
								
								if (tipoValidacion.equals(FormatosEnum.DETALLADO_DE_DISTRIBUCION.getId())){
									errorReturn=validarCuenta(campoCuenta, columnaDato, valorFormateado, errores, idFila, fila, columnasDatos);
									if (errorReturn){
										continue BucleCeldas;
									}
									if(campoValorNeto.equals(columnaDato.getNombre())){
										errorReturn=validarTipoDatoMenorIgualACero(valor, idFila, columnaDato, errores, fila, columnasDatos);
										if (errorReturn){
											continue BucleCeldas;
										}	
									}else{
										errorReturn=validarTipoDatoMenorACero(valor, idFila, columnaDato, errores, fila, columnasDatos);
										if (errorReturn){
											continue BucleCeldas;
										}
									}									
								}
								
								if (tipoValidacion.equals(FormatosEnum.DETALLADO_DE_BIENESTAR_SOCIAL.getId())){
									errorReturn=validarTipoDatoIgualACero(valor, idFila, columnaDato, errores, fila, columnasDatos);
									if (errorReturn){
										continue BucleCeldas;
									}
								}
								
								if (tipoValidacion.equals(FormatosEnum.INFORME_ANUAL_DE_GESTION_COLECTIVA.getId())){
									errorReturn=validarTipoDatoMenorACero(valor, idFila, columnaDato, errores, fila, columnasDatos);
									if (errorReturn){
										continue BucleCeldas;
									}
								}
								
								if (tipoValidacion.equals(FormatosEnum.INFORME_ANUAL_DE_RESERVAS_APROBADAS.getId())){
									errorReturn=validarTipoDatoMenorACero(valor, idFila, columnaDato, errores, fila, columnasDatos);
									if (errorReturn){
										continue BucleCeldas;
									}
								}
								if (tipoValidacion.equals(FormatosEnum.INFORME_ANUAL_DE_CARTERA_CASTIGADA.getId())){
									errorReturn=validarTipoDatoMenorACero(valor, idFila, columnaDato, errores, fila, columnasDatos);
									if (errorReturn){
										continue BucleCeldas;
									}
								}
								if (tipoValidacion.equals(FormatosEnum.INFORME_ANUAL_DE_CARTERA_VENCIDA.getId())){
									errorReturn=validarTipoDatoMenorIgualACero(valor, idFila, columnaDato, errores, fila, columnasDatos);
									if (errorReturn){
										continue BucleCeldas;
									}
								}
								if (tipoValidacion.equals(FormatosEnum.INFORME_ANUAL_DE_COBROS_JURIDICOS.getId())){
									errorReturn=validarTipoDatoMenorIgualACero(valor, idFila, columnaDato, errores, fila, columnasDatos);
									if (errorReturn){
										continue BucleCeldas;
									}
								}
								if (tipoValidacion.equals(FormatosEnum.INFORME_ANUAL_DE_DONACIONES.getId())){
									errorReturn=validarTipoDatoMenorIgualACero(valor, idFila, columnaDato, errores, fila, columnasDatos);
									if (errorReturn){
										continue BucleCeldas;
									}
								}
								if (tipoValidacion.equals(FormatosEnum.INFORME_TRIMESTRAL_DE_RECAUDOS.getId())){
									errorReturn=validarTipoDatoMenorIgualACero(valor, idFila, columnaDato, errores, fila, columnasDatos);
									if (errorReturn){
										continue BucleCeldas;
									}
								}
								if (tipoValidacion.equals(FormatosEnum.INFORME_SOCIEDADES_EXTRANJERAS.getId())){
									if (campoRecaudo.equals(columnaDato.getNombre()) || campoRepartosNetos.equals(columnaDato.getNombre())){
										errorReturn=validarTipoDatoMenorIgualACero(valor, idFila, columnaDato, errores, fila, columnasDatos);
										if (errorReturn){
											continue BucleCeldas;
										}
									}else{
										errorReturn=validarTipoDatoMenorACero(valor, idFila, columnaDato, errores, fila, columnasDatos);
										if (errorReturn){
											continue BucleCeldas;
										}
									}
									
								}
								
								
							
							
						} catch (Exception e) {

							textoFallo = "Existe un error al leer el dato como tipo de dato numérico, " + celda.getStringCellValue();

							ErrorGenerico error = new ErrorGenerico();
							error.setIdFila(idFila+filaEncabezado + 1);
							error.setColumna(columnaDato.getNombre());
							error.setMensaje(textoFallo);
							error.setValor(celda.getStringCellValue());
							columnaDato.setError(error);
							errores.add(error);
							fila.setErrores(errores);

							columnasDatos.add(columnaDato);

							continue BucleCeldas;
						}
					}

					if (celda.getCellType() == Cell.CELL_TYPE_STRING) {
						
						try {

							String valor = celda.getStringCellValue();
							Double val = Double.parseDouble(valor);
							String valorFormateado = formatearDouble(val, columnaDato.getFormato());
							columnaDato.setValor(valorFormateado);
							
							if (tipoValidacion.equals(FormatosEnum.BALANCE_GENERAL.getId()) || tipoValidacion.equals(FormatosEnum.BALANCE_TERCEROS.getId()) || tipoValidacion.equals(FormatosEnum.EJECUCION_PRESUPUESTAL.getId()) || tipoValidacion.equals(FormatosEnum.DETALLADO_DE_BIENESTAR_SOCIAL.getId()) || tipoValidacion.equals(FormatosEnum.INFORME_ANUAL_DE_GESTION_COLECTIVA.getId()) || tipoValidacion.equals(FormatosEnum.INFORME_ANUAL_DE_RESERVAS_APROBADAS.getId()) || tipoValidacion.equals(FormatosEnum.INFORME_ANUAL_DE_CARTERA_CASTIGADA.getId()) || tipoValidacion.equals(FormatosEnum.INFORME_ANUAL_DE_CARTERA_VENCIDA.getId()) || tipoValidacion.equals(FormatosEnum.INFORME_ANUAL_DE_DONACIONES.getId()) || tipoValidacion.equals(FormatosEnum.INFORME_TRIMESTRAL_DE_RECAUDOS.getId())) {
								errorReturn=validarCuenta(campoCuenta, columnaDato, valorFormateado, errores, idFila, fila, columnasDatos);
								if (errorReturn){
									continue BucleCeldas;
								}
							}	

						} catch (Exception e) {

							textoFallo = "Existe un error al leer el dato como tipo de dato numérico, " + celda.getStringCellValue();

							ErrorGenerico error = new ErrorGenerico();
							error.setIdFila(idFila+filaEncabezado + 1);
							error.setColumna(columnaDato.getNombre());
							error.setMensaje(textoFallo);
							error.setValor(celda.getStringCellValue());
							columnaDato.setError(error);
							errores.add(error);
							fila.setErrores(errores);

							columnasDatos.add(columnaDato);

							continue BucleCeldas;
						}
					}

				} else if ("C".equalsIgnoreCase(columnaDato.getTipoDato())) {
					Boolean errorReturn=Boolean.FALSE;
					if (celda != null) {
						if (celda.getCellType() == Cell.CELL_TYPE_STRING || (tipoValidacion.equals(FormatosEnum.BALANCE_TERCEROS.getId()) && celda.getCellType() ==Cell.CELL_TYPE_BLANK)) {

							try {

								String valor = celda.getStringCellValue();								
								String valorColumna = String.format(columnaDato.getFormato(), valor);
								columnaDato.setValor(valorColumna);
								if (tipoValidacion.equals(FormatosEnum.BALANCE_TERCEROS.getId())){
									errorReturn=validarCamposBalanceTerceros(campoIdentificacionTercero, campoNombreTercero, campoCuenta, valorColumna, idFila, columnaDato, errores, fila, columnasDatos, celda);
									if (errorReturn){
										continue BucleCeldas;
									}
								}
								if (tipoValidacion.equals(FormatosEnum.SOCIOS_Y_ESTADO.getId())){
									errorReturn=validarCamposSociosEstado(campoIdentificacionSocio, campoCategoria, valorColumna, idFila, columnaDato, errores, fila, columnasDatos);
									if (errorReturn){
										continue BucleCeldas;
									}
								}
								if (tipoValidacion.equals(FormatosEnum.DETALLADO_DE_DISTRIBUCION.getId())){
									errorReturn=validarGuiones(campoIdentificacionActor, valorColumna, idFila, columnaDato, errores, fila, columnasDatos);
									if (errorReturn){
										continue BucleCeldas;
									}
									errorReturn=validarGuiones(campoActor, valorColumna, idFila, columnaDato, errores, fila, columnasDatos);
									if (errorReturn){
										continue BucleCeldas;
									}
								}
								if (tipoValidacion.equals(FormatosEnum.DETALLADO_DE_BIENESTAR_SOCIAL.getId())){
									errorReturn=validarGuiones(campoIdentificacionTercero, valorColumna, idFila, columnaDato, errores, fila, columnasDatos);
									if (errorReturn){
										continue BucleCeldas;
									}
								}
								if (tipoValidacion.equals(FormatosEnum.INFORME_ANUAL_DE_CARTERA_CASTIGADA.getId())){
									errorReturn=validarGuiones(campoIdentificacionTercero, valorColumna, idFila, columnaDato, errores, fila, columnasDatos);
									if (errorReturn){
										continue BucleCeldas;
									}
								}
								if (tipoValidacion.equals(FormatosEnum.INFORME_ANUAL_DE_CARTERA_VENCIDA.getId())){
									errorReturn=validarGuiones(campoIdentificacionTercero, valorColumna, idFila, columnaDato, errores, fila, columnasDatos);
									if (errorReturn){
										continue BucleCeldas;
									}
									errorReturn=validarSiNo(campoCobroJuridico, valorColumna, idFila, columnaDato, errores, fila, columnasDatos);
									if (errorReturn){
										continue BucleCeldas;
									}
									
								}
								
								

							} catch (Exception e) {

								textoFallo = "Existe un error al leer el dato como tipo de dato téxto, " + e.getMessage();

								ErrorGenerico error = new ErrorGenerico();
								error.setIdFila(idFila+filaEncabezado + 1);
								error.setColumna(columnaDato.getNombre());
								error.setMensaje(textoFallo);
								error.setValor(celda.getStringCellValue());
								columnaDato.setError(error);
								errores.add(error);
								fila.setErrores(errores);

								columnasDatos.add(columnaDato);

								continue BucleCeldas;
							}
						}else{
							if (celda.getCellType() == Cell.CELL_TYPE_NUMERIC) {

								try {

									Double valor = celda.getNumericCellValue();
									String valorFormateado = String.valueOf(valor);
									columnaDato.setValor(valorFormateado);									
									
								} catch (Exception e) {

									textoFallo = "Existe un error al leer el dato como tipo de dato numérico, " + celda.getStringCellValue();

									ErrorGenerico error = new ErrorGenerico();
									error.setIdFila(idFila+filaEncabezado + 1);
									error.setColumna(columnaDato.getNombre());
									error.setMensaje(textoFallo);
									error.setValor(celda.getStringCellValue());
									columnaDato.setError(error);
									errores.add(error);
									fila.setErrores(errores);

									columnasDatos.add(columnaDato);

									continue BucleCeldas;
								}
							}
						}
					}

				}else if ("F".equalsIgnoreCase(columnaDato.getTipoDato())) {
					Boolean errorReturn=Boolean.FALSE;
					if (celda.getCellType() == Cell.CELL_TYPE_NUMERIC) {
						String datoFecha=null;
						try {							
							SimpleDateFormat formato = new SimpleDateFormat(columnaDato.getFormato());
							if (DateUtil.isCellDateFormatted(celda)) {
								datoFecha = formato.format(celda.getDateCellValue());
								columnaDato.setValor(datoFecha);
								if (tipoValidacion.equals(FormatosEnum.INFORME_ANUAL_DE_CARTERA_CASTIGADA.getId())){
									Calendar fechaVencimiento = Calendar.getInstance();
									fechaVencimiento.setTime(celda.getDateCellValue());
									errorReturn=validarFecha(fechaVencimiento, datoFecha, idFila, columnaDato, errores, fila, columnasDatos);
									if (errorReturn){
										continue BucleCeldas;
									}
								}
								if (tipoValidacion.equals(FormatosEnum.INFORME_ANUAL_DE_DONACIONES.getId())){
									Calendar fechaVencimiento = Calendar.getInstance();
									fechaVencimiento.setTime(celda.getDateCellValue());
									errorReturn=validarFecha(fechaVencimiento, datoFecha, idFila, columnaDato, errores, fila, columnasDatos);
									if (errorReturn){
										continue BucleCeldas;
									}
								}
								if (tipoValidacion.equals(FormatosEnum.INFORME_SOCIEDADES_EXTRANJERAS.getId())){
									Calendar fechaVencimiento = Calendar.getInstance();
									fechaVencimiento.setTime(celda.getDateCellValue());
									errorReturn=validarFecha(fechaVencimiento, datoFecha, idFila, columnaDato, errores, fila, columnasDatos);
									if (errorReturn){
										continue BucleCeldas;
									}
								}
							}else{
								throw new Exception("dato numerico");
							}								

						} catch (Exception ex) {

							textoFallo = "Existe un error al leer el dato como tipo de dato fecha, " + ex.getMessage();

							ErrorGenerico error = new ErrorGenerico();
							error.setIdFila(idFila+filaEncabezado + 1);
							error.setColumna(columnaDato.getNombre());
							error.setMensaje(textoFallo);
							try{
								error.setValor(celda.getStringCellValue());
							}catch (Exception e) {
								error.setValor(String.valueOf(celda.getNumericCellValue()));
							}
							columnaDato.setError(error);
							errores.add(error);
							fila.setErrores(errores);

							columnasDatos.add(columnaDato);

							continue BucleCeldas;
						}
					} else if (celda.getCellType() == Cell.CELL_TYPE_STRING) {

						try {

							SimpleDateFormat formato = new SimpleDateFormat(columnaDato.getFormato());
							String cadenaFecha = celda.getStringCellValue();
							Date fechaValor = formato.parse(cadenaFecha);

							columnaDato.setValor(cadenaFecha);

						} catch (Exception ex) {

							textoFallo = "Existe un error al leer el dato como tipo de dato fecha, " + ex.getMessage();

							ErrorGenerico error = new ErrorGenerico();
							error.setIdFila(idFila+filaEncabezado + 1);
							error.setColumna(columnaDato.getNombre());
							error.setMensaje(textoFallo);
							error.setValor(celda.getStringCellValue());
							columnaDato.setError(error);
							errores.add(error);
							fila.setErrores(errores);

							columnasDatos.add(columnaDato);

							continue BucleCeldas;
						}
					}
				}

				columnasDatos.add(columnaDato);
			}

			fila.setColumnas(columnasDatos);
			filas.add(fila);

			String colCuentaContable = FacesUtils.getPropsSGC().getString("campoCuenta");						

			for (Columna columna : columnasDatos) {
				System.out.println("Nombre : " + columna.getNombre() + " Valor : " + columna.getValor());
				if (null != columna.getValor()) {
					if (colCuentaContable.equals(columna.getNombre())) {
						listaLlaves.add(columna.getValor());
						continue BucleFilas;
					}
				}

			}

		}

		if (tipoValidacion.equals(FormatosEnum.BALANCE_GENERAL.getId()) || tipoValidacion.equals(FormatosEnum.BALANCE_TERCEROS.getId())) {			
			BigDecimal valActivo = BigDecimal.valueOf(Double.parseDouble(valorActivo)).setScale(2, RoundingMode.UP);
			BigDecimal valPasivoCapital = BigDecimal.valueOf(Double.parseDouble(valorPasivo)+Double.parseDouble(valorCapital)).setScale(2, RoundingMode.UP);			
			
			if (!valActivo.equals(valPasivoCapital)) {				
				ErrorGenerico error = new ErrorGenerico();				
				error.setMensaje(MSJ_CUENTASERROR);				
				List<ErrorGenerico> lista = new ArrayList<ErrorGenerico>();
				lista.add(error);
				archivo.setError(lista);
			}
		}


		archivo.setFilas(filas);
		archivo.setEntidad(entidad);
		archivo.setFormato(formatoCargue);
		archivo.setCorte(corte);

		}
	}
			
	
	
	private Boolean validarTipoDatoMenorIgualACero(Double valor, int idFila, Columna columnaDato, List<ErrorGenerico> errores, Fila fila, List<Columna> columnasDatos) throws Exception{							
			if (valor<=0){
				String textoFallo = "El valor no puede ser igual o menor a cero: "+valor;
				establecerError(idFila, columnaDato, errores, fila, columnasDatos, textoFallo, formatearDouble(valor, columnaDato.getFormato()));
				return Boolean.TRUE;
			}
		
		return Boolean.FALSE;	
	}
	
	private Boolean validarTipoDatoMenorACero(Double valor, int idFila, Columna columnaDato, List<ErrorGenerico> errores, Fila fila, List<Columna> columnasDatos) throws Exception{							
		if (valor<0){
			String textoFallo = "El valor no puede ser menor a cero: "+valor;
			establecerError(idFila, columnaDato, errores, fila, columnasDatos, textoFallo, formatearDouble(valor, columnaDato.getFormato()));
			return Boolean.TRUE;
		}
	
	return Boolean.FALSE;	
	}
	
	private Boolean validarFecha(Calendar dateStart, String valor, int idFila,
			Columna columnaDato, List<ErrorGenerico> errores, Fila fila,
			List<Columna> columnasDatos) throws Exception {
		Calendar dateEnd = Calendar.getInstance();

		int dateDifference = 0;

		dateStart.set(Calendar.HOUR_OF_DAY, 0);
		dateStart.set(Calendar.MINUTE, 0);
		dateStart.set(Calendar.SECOND, 0);
		dateStart.set(Calendar.MILLISECOND, 0);

		dateEnd.set(Calendar.HOUR_OF_DAY, 0);
		dateEnd.set(Calendar.MINUTE, 0);
		dateEnd.set(Calendar.SECOND, 0);
		dateEnd.set(Calendar.MILLISECOND, 0);

		dateDifference = dateStart.compareTo(dateEnd);
		if (dateDifference != -1) {
			String textoFallo = "La fecha debe ser menor a la fecha de cargue";
			establecerError(idFila, columnaDato, errores, fila, columnasDatos, textoFallo, valor);
			return Boolean.TRUE;
		}

		return Boolean.FALSE;
	}
	
	private Boolean validarTipoDatoIgualACero(Double valor, int idFila, Columna columnaDato, List<ErrorGenerico> errores, Fila fila, List<Columna> columnasDatos) throws Exception{							
		if (valor==0){
			String textoFallo = "El valor no puede ser igual a cero: "+valor;
			establecerError(idFila, columnaDato, errores, fila, columnasDatos, textoFallo, formatearDouble(valor, columnaDato.getFormato()));
			return Boolean.TRUE;
		}
	
	return Boolean.FALSE;	
}
	
	
	private void establecerError(int idFila, Columna columnaDato, List<ErrorGenerico> errores, Fila fila, List<Columna> columnasDatos, String textoFallo, String valor){
		ErrorGenerico error = new ErrorGenerico();
		error.setIdFila(idFila+filaEncabezado + 1);
		error.setColumna(columnaDato.getNombre());
		error.setMensaje(textoFallo);
		error.setValor(valor);
		columnaDato.setError(error);
		errores.add(error);
		fila.setErrores(errores);

		columnasDatos.add(columnaDato);
	}
	
	private Boolean validarGuiones(String campoIdentificacionActor, String valorFormateado, int idFila, Columna columnaDato, List<ErrorGenerico> errores, Fila fila, List<Columna> columnasDatos){		
		if (campoIdentificacionActor.equals(columnaDato.getNombre()) ){			
			if (valorFormateado.indexOf("-")>0){
				String textoFallo = "El campo no puede tener guiones '-': "+valorFormateado;
				establecerError(idFila, columnaDato, errores, fila, columnasDatos, textoFallo, valorFormateado);	
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
		}
	
	private Boolean validarSiNo(String campoCobroJuridico, String valorFormateado, int idFila, Columna columnaDato, List<ErrorGenerico> errores, Fila fila, List<Columna> columnasDatos){		
		if (campoCobroJuridico.equals(columnaDato.getNombre()) ){		
			if (!valorFormateado.trim().equalsIgnoreCase("SI") && !valorFormateado.trim().equalsIgnoreCase("NO")){
				String textoFallo = "El campo solo puede tener los valores SI o NO";
				establecerError(idFila, columnaDato, errores, fila, columnasDatos, textoFallo, valorFormateado);	
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
		}
	
	
	
	private Boolean validarCamposBalanceTerceros(
			String campoIdentificacionTercero, String campoNombreTercero,
			String campoCuenta, String valorFormateado, int idFila,
			Columna columnaDato, List<ErrorGenerico> errores, Fila fila,
			List<Columna> columnasDatos, Cell celda) {
		if (celda.getCellType() == Cell.CELL_TYPE_BLANK
				&& (campoIdentificacionTercero.equals(columnaDato.getNombre()) || campoNombreTercero
						.equals(columnaDato.getNombre()))) {
			for (Columna columna : columnasDatos) {
				if (columna.getNombre().equals(campoCuenta)) {
					if (columna.getValor().trim().length() >= 6) {
						String textoFallo = "La celda está vacía";
						ErrorGenerico error = new ErrorGenerico();
						error.setIdFila(idFila + filaEncabezado + 1);
						error.setColumna(columnaDato.getNombre());
						error.setMensaje(textoFallo);
						error.setValor(valorFormateado);
						columnaDato.setError(error);
						errores.add(error);
						fila.setErrores(errores);

						columnasDatos.add(columnaDato);

						return Boolean.TRUE;
					}
				}
			}

		}
		return Boolean.FALSE;
	}
	
	
	private Boolean validarCamposSociosEstado(String campoIdentificacionSocio, String campoCategoria, String valorFormateado, int idFila, Columna columnaDato, List<ErrorGenerico> errores, Fila fila, List<Columna> columnasDatos){		
		if (campoIdentificacionSocio.equals(columnaDato.getNombre())){			
			if (valorFormateado.indexOf("-")>0){
				String textoFallo = "El campo identificacion no puede tener guiones '-': "+valorFormateado;
	
				ErrorGenerico error = new ErrorGenerico();
				error.setIdFila(idFila+filaEncabezado + 1);
				error.setColumna(columnaDato.getNombre());
				error.setMensaje(textoFallo);
				error.setValor(valorFormateado);
				columnaDato.setError(error);
				errores.add(error);
				fila.setErrores(errores);
	
				columnasDatos.add(columnaDato);
	
				return Boolean.TRUE;
			}
		}else if (campoCategoria.equals(columnaDato.getNombre())){	
			if (!valorFormateado.trim().equalsIgnoreCase("Compositor") &&  !valorFormateado.trim().equalsIgnoreCase("Productor") && !valorFormateado.trim().equalsIgnoreCase("Editor") && !valorFormateado.trim().equalsIgnoreCase("Actor principal") && !valorFormateado.trim().equalsIgnoreCase("Actor Secundario") && !valorFormateado.trim().equalsIgnoreCase("Socio Activo") && !valorFormateado.trim().equalsIgnoreCase("Adherente") && !valorFormateado.trim().equalsIgnoreCase("Socio heredero")){
				String textoFallo = "El campo categoria solo debe contener uno de estos valores: Compositor, Productor, Editor, Actor principal, Actor secundario, Socio activo, Adherente, Socio heredero";
				
				ErrorGenerico error = new ErrorGenerico();
				error.setIdFila(idFila+filaEncabezado + 1);
				error.setColumna(columnaDato.getNombre());
				error.setMensaje(textoFallo);
				error.setValor(valorFormateado);
				columnaDato.setError(error);
				errores.add(error);
				fila.setErrores(errores);
	
				columnasDatos.add(columnaDato);
	
				return Boolean.TRUE;
			}
			
		}
		return Boolean.FALSE;	
	}
	
	
	private Boolean validarCuenta(String campoCuenta, Columna columnaDato, String valorFormateado, List<ErrorGenerico> errores, int idFila, Fila fila, List<Columna> columnasDatos){
		if (campoCuenta.equals(columnaDato.getNombre())){													
			registroCargues= new ArrayList<RegistroCargue>();
			List<RegistroCargueFormato> registros = serviceDao.getRegistroCargueFormatoDao().tomarRegistroCargueFormatoSinCorte(formatoCargue.getForCodigo(), entidad.getEntCodigo());
			if (registros!=null && registros.size()>0){
				CortesFormato corteFormatoConsulta = serviceDao.getCortesFormatoDao().obtenerCortesFormatoInActivoPorFormato();
				if (corteFormatoConsulta!=null){
					registroCargues = serviceDao.getRegistroCargueDao().tomarRegistrosCarguePorFormatoEntidadCorteCuenta(formatoCargue.getForCodigo(), entidad.getEntCodigo(), corteFormatoConsulta.getFcrCodigo(),campoCuenta,valorFormateado);									
				}
			}
			Map<Integer, Object> params = new HashMap<Integer, Object>();
			String cuentaFormateada=null;
			List<PUC> puc=null;
			String str = valorFormateado;
			int length = str.length( );
			if (!valorFormateado.startsWith("28300050") && length>6){
				cuentaFormateada=valorFormateado.substring(0, 6);
			}else{
				cuentaFormateada=valorFormateado;
			}								
			if (!valorFormateado.startsWith("28300050") && length>6){
				params.put(0, cuentaFormateada);
				puc = (List<PUC>) serviceDao.getGenericCommonDao().executeFind(PUC.class,params,PUC.NAMED_QUERY_FIND_BY_ID);
			}else{
				params.put(0, cuentaFormateada);
				puc = (List<PUC>) serviceDao.getGenericCommonDao().executeFind(PUC.class,params,PUC.NAMED_QUERY_FIND_BY_ID_EXACT);
			}
			if (puc==null || puc.size()==0){
				String textoFallo = "La cuenta con valor: "+valorFormateado+" no existe en el PUC";
				ErrorGenerico error = new ErrorGenerico();
				error.setIdFila(idFila+filaEncabezado + 1);
				error.setColumna(columnaDato.getNombre());
				error.setMensaje(textoFallo);
				error.setValor(valorFormateado);
				columnaDato.setError(error);
				errores.add(error);
				fila.setErrores(errores);

				columnasDatos.add(columnaDato);

				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}
			
	
	private Boolean validarCamposBalanceGeneral(String campoDebito, String campoCredito, String campoSaldoInicial, String campoSaldoFinal, String campoCuenta, Columna columnaDato,  String valorFormateado, List<ErrorGenerico> errores, int idFila, Fila fila, List<Columna> columnasDatos) throws Exception{		
		if (campoSaldoInicial.equals(columnaDato.getNombre())){			
			if (registroCargues!=null){									
				for (RegistroCargue registroCargue : registroCargues) {
					if (registroCargue.getDetallesFormato().getDetNombre().equals(campoSaldoFinal)){
						if (!valorFormateado.equals(registroCargue.getCrgValor())){
							String textoFallo = "El valor de saldo inicial es: "+valorFormateado+" y el valor del saldo final del anterior periodo es "+registroCargue.getCrgValor();

							ErrorGenerico error = new ErrorGenerico();
							error.setIdFila(idFila+filaEncabezado + 1);
							error.setColumna(columnaDato.getNombre());
							error.setMensaje(textoFallo);
							error.setValor(valorFormateado);
							columnaDato.setError(error);
							errores.add(error);
							fila.setErrores(errores);

							columnasDatos.add(columnaDato);

							return Boolean.TRUE;
						}
					}
				}
			}
		}else if(campoSaldoFinal.equals(columnaDato.getNombre())){
			String formato=columnaDato.getFormato();								
			double cont=0.0;
			for (Columna columna : columnasDatos) {
				if (columna.getNombre().equals(campoCuenta)){
					if (columna.getValor()!=null){
						if (columna.getValor().trim().equals("1")){
							valorActivo=valorFormateado;
						}else{
							if (columna.getValor().trim().equals("2")){
								valorPasivo=valorFormateado;	
							}else{
								if (columna.getValor().trim().equals("3")){
									valorCapital=valorFormateado;
								}
							}
						}
					}
				}
				if (columna.getNombre().equals(campoSaldoInicial)){
					if (columna.getValor()!=null){
						DecimalFormat decimalFormat = new DecimalFormat(columna.getFormato());
						cont=decimalFormat.parse(columna.getValor()).doubleValue();
					}
				}else if (columna.getNombre().equals(campoDebito)){
					if (columna.getValor()!=null){
						DecimalFormat decimalFormat = new DecimalFormat(columna.getFormato());
						cont=cont+decimalFormat.parse(columna.getValor()).doubleValue();
					}													
				}else if (columna.getNombre().equals(campoCredito)){
					if (columna.getValor()!=null){
						DecimalFormat decimalFormat = new DecimalFormat(columna.getFormato());
						cont=cont-decimalFormat.parse(columna.getValor()).doubleValue();
					}
				}
			}
			if (!valorFormateado.equals(formatearDouble(cont, formato))){
				String textoFallo = "El valor de saldo final es: "+valorFormateado+" y la sumatoria de los demas campos es "+formatearDouble(cont, formato);
				ErrorGenerico error = new ErrorGenerico();
				error.setIdFila(idFila+filaEncabezado + 1);
				error.setColumna(columnaDato.getNombre());
				error.setMensaje(textoFallo);
				error.setValor(valorFormateado);
				columnaDato.setError(error);
				errores.add(error);
				fila.setErrores(errores);

				columnasDatos.add(columnaDato);

				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}
	
	

	/**
	 * Metodo utilizado para localizar una columna del archivo dentro de la
	 * parametrizacion
	 * 
	 * @param nombreColumna
	 * @return
	 */
	private Boolean tomarDefinicionColumna(String nombreColumna, int pos) {
		
		if (detallesFormato.get(pos).getDetNombre().equals(nombreColumna)){
			return Boolean.TRUE;
		}

		return Boolean.FALSE;
	}

	/**
	 * Metodo utilizado para localizar una columna del archivo dentro de la
	 * parametrizacion
	 * 
	 * @param nombreColumna
	 * @return
	 */
	private TiposDato tomarTipoDatoColumna(Integer codigoTipoDato) {

		for (TiposDato tipo : listaTiposDatos) {

			if (tipo.getTpdCodigo() == codigoTipoDato) {
				return tipo;
			}
		}

		return null;
	}

	/**
	 * Metodo utilizado para localizar una columna del archivo dentro de la
	 * parametrizacion
	 * 
	 * @param nombreColumna
	 * @return
	 */
	private FormatosTiposDato tomarFormatosTiposDato(Integer ncodigoFoteColumna) {

		for (FormatosTiposDato fmt : listaFormaosTiposDatos) {

			if (fmt.getFtdCodigo() == (ncodigoFoteColumna)) {
				return fmt;
			}
		}

		return null;
	}

	/**
	 * 
	 * Metodo utilizado para validar que se pueda iniciar el proceso de cargue
	 * de archivo
	 * 
	 * @return
	 * @throws Exception
	 */
	private boolean pasaValidacionCargue() throws Exception {

		boolean esValido = true;

		Integer codigoFormato = formatoCargue.getForCodigo();
		Integer corteAsignado = formatoCargue.getCorteAsignado();
		Integer codEntidad = entidad.getEntCodigo();
		// corte = serviceDao.getCortesFormatoDao()
		// .obtenerCortesFormatoActivoPorFormato(codigoFormato);
		corte = (CortesFormato) serviceDao.getCortesFormatoDao().read(CortesFormato.class, corteAsignado);
		Integer codCorte = corte.getFcrCodigo();

		List<RegistroCargue> registroCargues = serviceDao.getRegistroCargueDao().tomarRegistrosCarguePorFormatoEntidadCorte(codigoFormato,
				codEntidad, codCorte);

//		Autorizaciones autorizaciones = (Autorizaciones) serviceDao.getAutorizacionesDao().obtenerAutorizacionPorFormatoEntidadVigente(codigoFormato,
//				codEntidad);

		if (registroCargues != null && registroCargues.size() > 0) {

//			if (autorizaciones == null) {
//				esValido = false;
//				throw new Exception("No existe autorizacin vigente que avale el cargue");
//
//			} else {
//
//				esActualizacion = true;
//			}

		} else {

			if (corte.getFcrVencido()) {

//				if (autorizaciones == null) {
//					esValido = false;
//					throw new Exception(
//							"La fecha de corte del archivo a cargar se encuentra vencida y no presenta autorizacin vigente que avale el cargue");
//				}

			}
		}

		return esValido;
	}

	/**
	 * Mtodo que permite foratear los datos de tipo double
	 * 
	 * @param valor
	 * @return
	 * @throws ExcepcionDatos
	 */
	private String formatearDouble(double valor, String formato) throws Exception {
		String valorFinal = null;
		try {

			DecimalFormat decimalFormat = new DecimalFormat(formato);
			valorFinal = decimalFormat.format(valor);

		} catch (Exception e) {
			throw new Exception("Formato de valor inválido en Formatear Double " + e.getMessage());
		}
		return valorFinal;
	}

	/**
	 * Metodo utilizado para realizar el borrado de un cargue anterior.
	 * 
	 * @throws Exception
	 */
	public void borrarDatosCargue() throws Exception {

		try {

			Integer idFormato = formatoCargue.getForCodigo();
			Integer idEntidad = entidad.getEntCodigo();
			Integer idCorte = corte.getFcrCodigo();

			RegistroCargueFormato cargue = serviceDao.getRegistroCargueFormatoDao()
					.tomarRegistroCargueFormatoPorCargue(idFormato, idEntidad, idCorte);

			serviceDao.getGenericCommonDao().delete(RegistroCargueFormato.class, cargue);

			String sql = "delete FROM SGC.Registro_Cargue where ENT_Codigo = " + idEntidad + " and FCR_Codigo = " + idCorte + " and FOR_Codigo = "
					+ idFormato;
			serviceDao.getSpringGenericDao().updateQuery(sql);

		} catch (Exception e) {
			String mensaje = "Se ha presentado un error inesperado durante borrado del cargue anterior, Detalles: " + e.getMessage();
			throw new Exception(mensaje);
		}

	}
	
	/**
	 * @return the serviceDao
	 */
	public ServiceDao getServiceDao() {
		return serviceDao;
	}

	/**
	 * @param serviceDao
	 *            the serviceDao to set
	 */
	public void setServiceDao(ServiceDao serviceDao) {
		this.serviceDao = serviceDao;
	}

	/**
	 * @return the esActualizacion
	 */
	public boolean isEsActualizacion() {
		return esActualizacion;
	}

	/**
	 * @param esActualizacion
	 *            the esActualizacion to set
	 */
	public void setEsActualizacion(boolean esActualizacion) {
		this.esActualizacion = esActualizacion;
	}

	/**
	 * @return the mapaGuardaValores
	 */
	public MapaGuardaValores getMapaGuardaValores() {
		return mapaGuardaValores;
	}

	/**
	 * @param mapaGuardaValores
	 *            the mapaGuardaValores to set
	 */
	public void setMapaGuardaValores(MapaGuardaValores mapaGuardaValores) {
		this.mapaGuardaValores = mapaGuardaValores;
	}

	public int getFilaEncabezado() {
		return filaEncabezado;
	}

	public void setFilaEncabezado(int filaEncabezado) {
		this.filaEncabezado = filaEncabezado;
	}
	
	

}
