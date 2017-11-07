/**
 * 
 */
package la.netco.sgc.uilayer.beans.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedProperty;

import la.netco.core.businesslogic.services.ServiceDao;
import la.netco.core.uilayer.beans.utils.FacesUtils;
import la.netco.sgc.persistence.dto.Autorizaciones;
import la.netco.sgc.persistence.dto.CortesFormato;
import la.netco.sgc.persistence.dto.DetallesFormato;
import la.netco.sgc.persistence.dto.Entidades;
import la.netco.sgc.persistence.dto.Formatos;
import la.netco.sgc.persistence.dto.FormatosTiposDato;
import la.netco.sgc.persistence.dto.RegistroCargue;
import la.netco.sgc.persistence.dto.RegistroCargueFormato;
import la.netco.sgc.persistence.dto.TiposDato;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
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
public class CargueArchivo {

	private final Formatos formatoCargue;

	private final List<DetallesFormato> detallesFormato;

	private final Entidades entidad;

	private final List<TiposDato> listaTiposDatos;

	private final List<FormatosTiposDato> listaFormaosTiposDatos;

	private List<String> listaLlaves;

	private List<String> listaValores;

	private CortesFormato corte;

	private boolean esActualizacion;

	private Integer formatoDetalladoId;

	private Integer formatoComparativoId;

	private Integer formatoInformeGestionId;

	private Integer formatoGeneralId;

	private Double valorC1 = 0.0, valorC2 = 0.0, valorC3 = 0.0;

	private String valCuenta1, valCuenta2, valCuenta3;

	private static final String MSJ_CUENTASERROR = "Las cuentas no coinciden";

	@ManagedProperty(value = "#{serviceDao}")
	transient private ServiceDao serviceDao;

	@ManagedProperty(value = "#{mapaGuardaValores}")
	private MapaGuardaValores mapaGuardaValores;

	public CargueArchivo(ServiceDao serviceDao, Formatos formatoCargue, Entidades entidad, List<DetallesFormato> detallesFormato,
			List<TiposDato> tiposDatos, List<FormatosTiposDato> formatosTiposDatos) {
		this.formatoCargue = formatoCargue;
		this.detallesFormato = detallesFormato;
		this.entidad = entidad;

		this.listaTiposDatos = tiposDatos;
		this.listaFormaosTiposDatos = formatosTiposDatos;
		this.serviceDao = serviceDao;

		valCuenta1 = FacesUtils.getPropsSGC().getString("valorActivo");
		valCuenta2 = FacesUtils.getPropsSGC().getString("valorPasivo");
		valCuenta3 = FacesUtils.getPropsSGC().getString("valorPatrimonio");
	}

	@SuppressWarnings("unused")
	public void cargar(List<Archivo> archivos) throws Exception {

		FileInputStream fis = null;

		if (pasaValidacionCargue()) {

			BucleArchivos: for (Archivo archivo : archivos) {

				Workbook libro = null;

				try {

					fis = new FileInputStream(archivo.getNombre());
					libro = ("xlsx".equalsIgnoreCase(archivo.getExtension())) ? new XSSFWorkbook(fis) : new HSSFWorkbook(fis);

				} catch (IOException e) {
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

				String nombreHoja = formatoCargue.getForNombreHoja();
				Sheet hoja = null;

				int filaEncabezado = (formatoCargue.getForLineaInicial() != null) ? formatoCargue.getForLineaInicial() : 0;

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
					String mensaje = "La cantidad de campos encontrada no concuerda con la cantidad indicada en la parametrizacion del formato";
					error.setMensaje(mensaje);

					archivo.getErrores().add(error);
					continue BucleArchivos;
				}

				boolean errorNombresColumnas = false;
				List<Columna> columnas = new ArrayList<Columna>();
				int pos = 0;

				BucleValidarCampos: for (String nombreColumna : listaColumnas) {

					DetallesFormato colDef = tomarDefinicionColumna(nombreColumna);

					if (colDef == null) {

						ErrorGenerico error = new ErrorGenerico();
						String mensaje = "El nombre de la columna '" + nombreColumna
								+ "' en el archivo, no concuerda con ningun campo indicado en la parametrizacion del formato";
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

							textoFallo += "Valor nulo o en blanco, es requerido";

							ErrorGenerico error = new ErrorGenerico();
							error.setIdFila(idFila);
							error.setColumna(columnaDato.getNombre());
							error.setMensaje(textoFallo);
							columnaDato.setError(error);
							//fila.setError(error);

							columnasDatos.add(columnaDato);

							continue BucleCeldas;
						}

						if ("N".equalsIgnoreCase(columnaDato.getTipoDato()) || "M".equalsIgnoreCase(columnaDato.getTipoDato())) {

							if (celda.getCellType() == Cell.CELL_TYPE_NUMERIC) {

								try {

									Double valor = celda.getNumericCellValue();
									String valorFormateado = formatearDouble(valor, columnaDato.getFormato());
									columnaDato.setValor(valorFormateado);

								} catch (Exception e) {

									textoFallo = "Existe un error al leer el dato como tipo de dato numérico, " + e.getMessage();

									ErrorGenerico error = new ErrorGenerico();
									error.setIdFila(idFila);
									error.setColumna(columnaDato.getNombre());
									error.setMensaje(textoFallo);
									error.setValor(celda.getStringCellValue());
									columnaDato.setError(error);
									//fila.setError(error);

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

								} catch (Exception e) {

									textoFallo = "Existe un error al leer el dato como tipo de dato numérico, " + e.getMessage();

									ErrorGenerico error = new ErrorGenerico();
									error.setIdFila(idFila);
									error.setColumna(columnaDato.getNombre());
									error.setMensaje(textoFallo);
									error.setValor(celda.getStringCellValue());
									columnaDato.setError(error);
									//fila.setError(error);

									columnasDatos.add(columnaDato);

									continue BucleCeldas;
								}
							}

						} else if ("C".equalsIgnoreCase(columnaDato.getTipoDato())) {

							if (celda.getCellType() == Cell.CELL_TYPE_STRING) {

								try {

									String valor = celda.getStringCellValue();
									String valorColumna = String.format(columnaDato.getFormato(), valor);

									columnaDato.setValor(valorColumna);

								} catch (Exception e) {

									textoFallo = "Existe un error al leer el dato como tipo de dato téxto, " + e.getMessage();

									ErrorGenerico error = new ErrorGenerico();
									error.setIdFila(idFila);
									error.setColumna(columnaDato.getNombre());
									error.setMensaje(textoFallo);
									error.setValor(celda.getStringCellValue());
									columnaDato.setError(error);
									//fila.setError(error);

									columnasDatos.add(columnaDato);

									continue BucleCeldas;
								}
							}

						} else if ("F".equalsIgnoreCase(columnaDato.getTipoDato())) {

							if (celda.getCellType() == Cell.CELL_TYPE_NUMERIC) {

								try {

									SimpleDateFormat formato = new SimpleDateFormat(columnaDato.getFormato());
									String datoFecha = formato.format(celda.getDateCellValue());
									columnaDato.setValor(datoFecha);

								} catch (Exception ex) {

									textoFallo = "Existe un error al leer el dato como tipo de dato fecha, " + ex.getMessage();

									ErrorGenerico error = new ErrorGenerico();
									error.setIdFila(idFila);
									error.setColumna(columnaDato.getNombre());
									error.setMensaje(textoFallo);
									error.setValor(celda.getStringCellValue());
									columnaDato.setError(error);
									//fila.setError(error);

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
									error.setIdFila(idFila);
									error.setColumna(columnaDato.getNombre());
									error.setMensaje(textoFallo);
									error.setValor(celda.getStringCellValue());
									columnaDato.setError(error);
									//fila.setError(error);

									columnasDatos.add(columnaDato);

									continue BucleCeldas;
								}
							}
						}

						columnasDatos.add(columnaDato);
					}

					fila.setColumnas(columnasDatos);
					filas.add(fila);
				}

				archivo.setFilas(filas);
				archivo.setEntidad(entidad);
				archivo.setFormato(formatoCargue);
				archivo.setCorte(corte);
			}

		} else {

			String mensajeError = "El archivo a cargar no cumple con la validación de vigencias y no existe una autorización vigente que lo avale";
			throw new Exception(mensajeError);
		}
	}

	/**
	 * Convierte el valor string con puntos a un string que sera usado en un
	 * parseo de Integer.
	 * 
	 * @return
	 */
	public String convertidorPuntos(String valorRecibido) {
		String finalValor;
		CharSequence caracContent = ",";
		finalValor = valorRecibido.replace('.', 'p');
		finalValor = finalValor.replaceAll("p", "");
		if (finalValor.contains(caracContent)) {
			finalValor = finalValor.replaceAll(",", ".");
		} else {
			finalValor = finalValor.concat(".00");
		}
		return finalValor;
	}

	/**
	 * 
	 * 
	 * 
	 * @return
	 */
	public boolean validadorGenerico(String valorEvaluar) {
		try {
			System.out.println("***Evaluando Cuentas Tipo : " + valorEvaluar);
			HashMap<String, String> mapaAuxiliar = new HashMap<String, String>();
			List<String> listaAuxLlaves = new ArrayList<String>();
			List<String> listaAuxValores = new ArrayList<String>();
			Double valorSuma = 0.0, valorPadre = 0.0;
			Integer contAuxi = 0;
			boolean bien = true;
			boolean acaboVali = false;
			String pTa;

			for (int La = 0; La < listaLlaves.size(); La++) {
				if (listaLlaves.get(La).substring(0, 1).equals(valorEvaluar)) {
					listaAuxLlaves.add(listaLlaves.get(La));
					listaAuxValores.add(listaValores.get(La));
				}
			}

			Integer sizeFinal = listaAuxValores.size();
			if (sizeFinal.equals(1)) {
				sizeFinal = 1;
			} else {
				sizeFinal = sizeFinal - 1;
			}

			if (!listaAuxLlaves.isEmpty()) {

				while (bien == true && !acaboVali) {

					if (sizeFinal > contAuxi) {

						if (listaAuxLlaves.get(contAuxi).length() == 1) {
							if (valorEvaluar.equals(valCuenta1)) {
								listaAuxValores.get(contAuxi);
								pTa = convertidorPuntos(listaAuxValores.get(contAuxi));
								valorC1 = Double.parseDouble(pTa);
							} else if (valorEvaluar.equals(valCuenta2)) {
								pTa = convertidorPuntos(listaAuxValores.get(contAuxi));
								valorC2 = Double.parseDouble(pTa);
							} else if (valorEvaluar.equals(valCuenta3)) {
								pTa = convertidorPuntos(listaAuxValores.get(contAuxi));
								valorC3 = Double.parseDouble(pTa);
							}
						}
						String valorFinal = convertidorPuntos(listaAuxValores.get(contAuxi));
						valorPadre = Double.parseDouble(valorFinal);

						for (int Pa = contAuxi; Pa < listaAuxLlaves.size(); Pa++) {

							if (null != listaAuxLlaves.get(Pa)) {
								Integer valSig = listaAuxLlaves.get(Pa).length();
								Integer valAct = listaAuxLlaves.get(contAuxi).length();
								Integer resActSig = valSig - valAct;
								if (resActSig.equals(1) && listaAuxLlaves.get(Pa).substring(0, valAct).equals(listaAuxLlaves.get(contAuxi))) {
									String valorFinalInt = convertidorPuntos(listaAuxValores.get(Pa));
									mapaAuxiliar.put(listaAuxLlaves.get(Pa), valorFinalInt);
								}
							}

						}

						if (!mapaAuxiliar.isEmpty()) {
							for (Map.Entry<String, String> variMap : mapaAuxiliar.entrySet()) {
								valorSuma = valorSuma + Double.parseDouble(variMap.getValue());
							}

							if (!valorSuma.equals(valorPadre)) {
								bien = false;
							} else {
								bien = true;
							}

						} else {
							bien = true;
						}

						mapaAuxiliar = new HashMap<String, String>();
						valorSuma = 0.0;
						contAuxi = contAuxi + 1;
					} else {
						acaboVali = true;
					}

				}

				if (bien == true) {
					return true;
				} else {
					return false;
				}
			} else {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Metodo para cargar archivos con tipo de validaciones unicas
	 * 
	 * @param archivos
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	public void cargar(List<Archivo> archivos, Integer tipoValidacion) throws Exception {

		// Declaraciones de Formato Detallado
		FileInputStream fis = null;
		formatoDetalladoId = Integer.parseInt(FacesUtils.getPropsSGC().getString("idFormatoDetallado"));
		List<String> valCreditos = new ArrayList<String>();
		List<String> valDebitos = new ArrayList<String>();
		String columnaDebito = FacesUtils.getPropsSGC().getString("campoDebito");
		String columnaCredito = FacesUtils.getPropsSGC().getString("campoCredito");

		// Declaraciones de formato comparativo
		formatoComparativoId = Integer.parseInt(FacesUtils.getPropsSGC().getString("idFormatoComparativo"));

		// Declaraciones de formato informe gestion colectiva
		formatoInformeGestionId = Integer.parseInt(FacesUtils.getPropsSGC().getString("idFormatoInformeGestion"));
		String cuentasValidar = FacesUtils.getPropsSGC().getString("cuentasExistentes");
		List<String> listaCuentasExistentes = new ArrayList<String>();

		// Declaracion de formato general
		formatoGeneralId = Integer.parseInt(FacesUtils.getPropsSGC().getString("idFormatoGeneral"));
		List<String> listaValoresPas = new ArrayList<String>();
		List<String> listaValoresPat = new ArrayList<String>();

		// Declaraciones Generales en metodo y clase
		listaLlaves = new ArrayList<String>();
		listaValores = new ArrayList<String>();
		List<String> listaCuentas = new ArrayList<String>();
		Double sumaC3C2 = 0.0;

		if (pasaValidacionCargue()) {
			if (tipoValidacion.equals(formatoGeneralId)) {

				BucleArchivos: for (Archivo archivo : archivos) {

					Workbook libro = null;

					try {

						fis = new FileInputStream(archivo.getNombre());
						libro = ("xlsx".equalsIgnoreCase(archivo.getExtension())) ? new XSSFWorkbook(fis) : new HSSFWorkbook(fis);

					} catch (IOException e) {
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

					String nombreHoja = formatoCargue.getForNombreHoja();
					Sheet hoja = null;

					int filaEncabezado = (formatoCargue.getForLineaInicial() != null) ? formatoCargue.getForLineaInicial() : 0;

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
						String mensaje = "La cantidad de campos encontrada no concuerda con la cantidad indicada en la parametrizacion del formato";
						error.setMensaje(mensaje);

						archivo.getErrores().add(error);
						continue BucleArchivos;
					}

					boolean errorNombresColumnas = false;
					List<Columna> columnas = new ArrayList<Columna>();
					int pos = 0;

					BucleValidarCampos: for (String nombreColumna : listaColumnas) {

						DetallesFormato colDef = tomarDefinicionColumna(nombreColumna);

						if (colDef == null) {

							ErrorGenerico error = new ErrorGenerico();
							String mensaje = "El nombre de la columna '" + nombreColumna
									+ "' en el archivo, no concuerda con ningun campo indicado en la parametrizacion del formato";
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

								// textoFallo +=
								// "Valor nulo o en blanco, es requerido";
								//
								// ErrorGenerico
								// error = new
								// ErrorGenerico();
								// error.setIdFila(idFila);
								// error.setColumna(columnaDato.getNombre());
								// error.setMensaje(textoFallo);
								// columnaDato.setError(error);
								// //fila.setError(error);
								//
								// columnasDatos.add(columnaDato);

								continue BucleCeldas;
							} else if (((celda == null) || (celda.getCellType() == Cell.CELL_TYPE_BLANK)) && !columnaDato.isRequerido()) {
								continue BucleCeldas;
							}

							if ("N".equalsIgnoreCase(columnaDato.getTipoDato()) || "M".equalsIgnoreCase(columnaDato.getTipoDato())) {

								if (celda.getCellType() == Cell.CELL_TYPE_NUMERIC) {

									try {

										Double valor = celda.getNumericCellValue();
										String valorFormateado = formatearDouble(valor, columnaDato.getFormato());
										columnaDato.setValor(valorFormateado);

									} catch (Exception e) {

										textoFallo = "Existe un error al leer el dato como tipo de dato numérico, " + e.getMessage();

										ErrorGenerico error = new ErrorGenerico();
										error.setIdFila(idFila);
										error.setColumna(columnaDato.getNombre());
										error.setMensaje(textoFallo);
										error.setValor(celda.getStringCellValue());
										columnaDato.setError(error);
										//fila.setError(error);

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

									} catch (Exception e) {

										textoFallo = "Existe un error al leer el dato como tipo de dato numérico, " + e.getMessage();

										ErrorGenerico error = new ErrorGenerico();
										error.setIdFila(idFila);
										error.setColumna(columnaDato.getNombre());
										error.setMensaje(textoFallo);
										error.setValor(celda.getStringCellValue());
										columnaDato.setError(error);
										//fila.setError(error);

										columnasDatos.add(columnaDato);

										continue BucleCeldas;
									}
								}

							} else if ("C".equalsIgnoreCase(columnaDato.getTipoDato())) {

								if (celda != null) {
									if (celda.getCellType() == Cell.CELL_TYPE_STRING) {

										try {

											String valor = celda.getStringCellValue();
											String valorColumna = String.format(columnaDato.getFormato(), valor);

											columnaDato.setValor(valorColumna);

										} catch (Exception e) {

											textoFallo = "Existe un error al leer el dato como tipo de dato téxto, " + e.getMessage();

											ErrorGenerico error = new ErrorGenerico();
											error.setIdFila(idFila);
											error.setColumna(columnaDato.getNombre());
											error.setMensaje(textoFallo);
											error.setValor(celda.getStringCellValue());
											columnaDato.setError(error);
											//fila.setError(error);

											columnasDatos.add(columnaDato);

											continue BucleCeldas;
										}
									}
								}

							} else if ("F".equalsIgnoreCase(columnaDato.getTipoDato())) {

								if (celda.getCellType() == Cell.CELL_TYPE_NUMERIC) {

									try {

										SimpleDateFormat formato = new SimpleDateFormat(columnaDato.getFormato());
										String datoFecha = formato.format(celda.getDateCellValue());
										columnaDato.setValor(datoFecha);

									} catch (Exception ex) {

										textoFallo = "Existe un error al leer el dato como tipo de dato fecha, " + ex.getMessage();

										ErrorGenerico error = new ErrorGenerico();
										error.setIdFila(idFila);
										error.setColumna(columnaDato.getNombre());
										error.setMensaje(textoFallo);
										error.setValor(celda.getStringCellValue());
										columnaDato.setError(error);
										//fila.setError(error);

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
										error.setIdFila(idFila);
										error.setColumna(columnaDato.getNombre());
										error.setMensaje(textoFallo);
										error.setValor(celda.getStringCellValue());
										columnaDato.setError(error);
										//fila.setError(error);

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
						String colValor = FacesUtils.getPropsSGC().getString("campoActivo");
						String colValorPas = FacesUtils.getPropsSGC().getString("campoPasivo");
						String colValorPat = FacesUtils.getPropsSGC().getString("campoPatrimonio");

						for (Columna columna : columnasDatos) {
							System.out.println("Nombre : " + columna.getNombre() + " Valor : " + columna.getValor());
							if (null != columna.getValor()) {
								if (colCuentaContable.equals(columna.getNombre())) {
									listaLlaves.add(columna.getValor());
								} else if (colValor.equals(columna.getNombre())) {
									listaValores.add(columna.getValor());
								} else if (colValorPas.equals(columna.getNombre())) {
									listaValoresPas.add(columna.getValor());
								} else if (colValorPat.equals(columna.getNombre())) {
									listaValoresPat.add(columna.getValor());
								}

							}

						}

					}

					for (int la = 0; la < listaLlaves.size(); la++) {
						String valorActs = convertidorPuntos(listaValores.get(la));
						String valorPass = convertidorPuntos(listaValoresPas.get(la));
						String valorPats = convertidorPuntos(listaValoresPat.get(la));
						Double valorAct = Double.parseDouble(valorActs);
						Double valorPas = Double.parseDouble(valorPass);
						Double valorPat = Double.parseDouble(valorPats);
						Double sumaTotal = valorPas + valorPat;
						if (!sumaTotal.equals(valorAct)) {
							throw new Exception(MSJ_CUENTASERROR);
						}
					}

					listaCuentas.add(valCuenta1);
					listaCuentas.add(valCuenta2);
					listaCuentas.add(valCuenta3);

					for (int Ol = 0; Ol < listaCuentas.size(); Ol++) {
						boolean ResultadoValidacion = validadorGenerico(listaCuentas.get(Ol));
						if (!ResultadoValidacion) {
							throw new Exception(MSJ_CUENTASERROR);
						}
						// sumaC3C2 = valorC2 + valorC3;
					}

					// if (!sumaC3C2.equals(valorC1)
					// && (valorC2 != 0 && valorC3 != 0)) {
					// throw new
					// Exception(MSJ_CUENTASERROR);
					// }

					archivo.setFilas(filas);
					archivo.setEntidad(entidad);
					archivo.setFormato(formatoCargue);
					archivo.setCorte(corte);

				}

			} else if (tipoValidacion.equals(formatoInformeGestionId)) {

				BucleArchivos: for (Archivo archivo : archivos) {

					Workbook libro = null;

					try {

						fis = new FileInputStream(archivo.getNombre());
						libro = ("xlsx".equalsIgnoreCase(archivo.getExtension())) ? new XSSFWorkbook(fis) : new HSSFWorkbook(fis);

					} catch (IOException e) {
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

					String nombreHoja = formatoCargue.getForNombreHoja();
					Sheet hoja = null;

					int filaEncabezado = (formatoCargue.getForLineaInicial() != null) ? formatoCargue.getForLineaInicial() : 0;

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
						String mensaje = "La cantidad de campos encontrada no concuerda con la cantidad indicada en la parametrizacion del formato";
						error.setMensaje(mensaje);

						archivo.getErrores().add(error);
						continue BucleArchivos;
					}

					boolean errorNombresColumnas = false;
					List<Columna> columnas = new ArrayList<Columna>();
					int pos = 0;

					BucleValidarCampos: for (String nombreColumna : listaColumnas) {

						DetallesFormato colDef = tomarDefinicionColumna(nombreColumna);

						if (colDef == null) {

							ErrorGenerico error = new ErrorGenerico();
							String mensaje = "El nombre de la columna '" + nombreColumna
									+ "' en el archivo, no concuerda con ningun campo indicado en la parametrizacion del formato";
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

								// textoFallo +=
								// "Valor nulo o en blanco, es requerido";
								//
								// ErrorGenerico
								// error = new
								// ErrorGenerico();
								// error.setIdFila(idFila);
								// error.setColumna(columnaDato.getNombre());
								// error.setMensaje(textoFallo);
								// columnaDato.setError(error);
								// //fila.setError(error);
								//
								// columnasDatos.add(columnaDato);

								continue BucleCeldas;
							} else if (((celda == null) || (celda.getCellType() == Cell.CELL_TYPE_BLANK)) && !columnaDato.isRequerido()) {
								continue BucleCeldas;
							}

							if ("N".equalsIgnoreCase(columnaDato.getTipoDato()) || "M".equalsIgnoreCase(columnaDato.getTipoDato())) {

								if (celda.getCellType() == Cell.CELL_TYPE_NUMERIC) {

									try {

										Double valor = celda.getNumericCellValue();
										String valorFormateado = formatearDouble(valor, columnaDato.getFormato());
										columnaDato.setValor(valorFormateado);

									} catch (Exception e) {

										textoFallo = "Existe un error al leer el dato como tipo de dato numérico, " + e.getMessage();

										ErrorGenerico error = new ErrorGenerico();
										error.setIdFila(idFila);
										error.setColumna(columnaDato.getNombre());
										error.setMensaje(textoFallo);
										error.setValor(celda.getStringCellValue());
										columnaDato.setError(error);
										//fila.setError(error);

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

									} catch (Exception e) {

										textoFallo = "Existe un error al leer el dato como tipo de dato numérico, " + e.getMessage();

										ErrorGenerico error = new ErrorGenerico();
										error.setIdFila(idFila);
										error.setColumna(columnaDato.getNombre());
										error.setMensaje(textoFallo);
										error.setValor(celda.getStringCellValue());
										columnaDato.setError(error);
										//fila.setError(error);

										columnasDatos.add(columnaDato);

										continue BucleCeldas;
									}
								}

							} else if ("C".equalsIgnoreCase(columnaDato.getTipoDato())) {

								if (celda != null) {
									if (celda.getCellType() == Cell.CELL_TYPE_STRING) {

										try {

											String valor = celda.getStringCellValue();
											String valorColumna = String.format(columnaDato.getFormato(), valor);

											columnaDato.setValor(valorColumna);

										} catch (Exception e) {

											textoFallo = "Existe un error al leer el dato como tipo de dato téxto, " + e.getMessage();

											ErrorGenerico error = new ErrorGenerico();
											error.setIdFila(idFila);
											error.setColumna(columnaDato.getNombre());
											error.setMensaje(textoFallo);
											error.setValor(celda.getStringCellValue());
											columnaDato.setError(error);
											//fila.setError(error);

											columnasDatos.add(columnaDato);

											continue BucleCeldas;
										}
									}
								}

							} else if ("F".equalsIgnoreCase(columnaDato.getTipoDato())) {

								if (celda.getCellType() == Cell.CELL_TYPE_NUMERIC) {

									try {

										SimpleDateFormat formato = new SimpleDateFormat(columnaDato.getFormato());
										String datoFecha = formato.format(celda.getDateCellValue());
										columnaDato.setValor(datoFecha);

									} catch (Exception ex) {

										textoFallo = "Existe un error al leer el dato como tipo de dato fecha, " + ex.getMessage();

										ErrorGenerico error = new ErrorGenerico();
										error.setIdFila(idFila);
										error.setColumna(columnaDato.getNombre());
										error.setMensaje(textoFallo);
										error.setValor(celda.getStringCellValue());
										columnaDato.setError(error);
										//fila.setError(error);

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
										error.setIdFila(idFila);
										error.setColumna(columnaDato.getNombre());
										error.setMensaje(textoFallo);
										error.setValor(celda.getStringCellValue());
										columnaDato.setError(error);
										//fila.setError(error);

										columnasDatos.add(columnaDato);

										continue BucleCeldas;
									}
								}
							}

							columnasDatos.add(columnaDato);
						}

						fila.setColumnas(columnasDatos);
						filas.add(fila);

						String colCuentaContable = FacesUtils.getPropsSGC().getString("campoCuentaContable");
						String colValor = FacesUtils.getPropsSGC().getString("campoValorInformeGestion");

						for (Columna columna : columnasDatos) {
							System.out.println("Nombre : " + columna.getNombre() + " Valor : " + columna.getValor());
							if (null != columna.getValor()) {
								if (colCuentaContable.equals(columna.getNombre())) {
									listaLlaves.add(columna.getValor());
								} else if (colValor.equals(columna.getNombre())) {
									listaValores.add(columna.getValor());
								}
							}

						}

					}

					if (cuentasValidar != null) {

						Integer contCuentasAuxi = 0;

						for (String cuenta : cuentasValidar.split(",")) {
							listaCuentasExistentes.add(cuenta);
						}

						for (int pa = 0; pa < listaCuentasExistentes.size(); pa++) {
							for (int ol = 0; ol < listaLlaves.size(); ol++) {
								if (listaLlaves.get(ol).equals(listaCuentasExistentes.get(pa))) {
									contCuentasAuxi = contCuentasAuxi + 1;
								}
							}
						}

						if (!contCuentasAuxi.equals(listaCuentasExistentes.size())) {
							String mensajeError = "No existen las cuentas;  " + cuentasValidar
									+ "  verificar que se encuentren en el archivo ó no se encuentren duplicadas.";
							throw new Exception(mensajeError);
						}
					}

					listaCuentas.add(valCuenta1);
					listaCuentas.add(valCuenta2);
					listaCuentas.add(valCuenta3);

					for (int Ol = 0; Ol < listaCuentas.size(); Ol++) {
						boolean ResultadoValidacion = validadorGenerico(listaCuentas.get(Ol));
						if (!ResultadoValidacion) {
							throw new Exception(MSJ_CUENTASERROR);
						}
						// sumaC3C2 = valorC2 + valorC3;
					}

					// if (!sumaC3C2.equals(valorC1)
					// && (valorC2 != 0 && valorC3 != 0)) {
					// throw new
					// Exception(MSJ_CUENTASERROR);
					// }

					archivo.setFilas(filas);
					archivo.setEntidad(entidad);
					archivo.setFormato(formatoCargue);
					archivo.setCorte(corte);

				}

			} else if (tipoValidacion.equals(formatoComparativoId)) {

				BucleArchivos: for (Archivo archivo : archivos) {

					Workbook libro = null;

					try {

						fis = new FileInputStream(archivo.getNombre());
						libro = ("xlsx".equalsIgnoreCase(archivo.getExtension())) ? new XSSFWorkbook(fis) : new HSSFWorkbook(fis);

					} catch (IOException e) {
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

					String nombreHoja = formatoCargue.getForNombreHoja();
					Sheet hoja = null;

					int filaEncabezado = (formatoCargue.getForLineaInicial() != null) ? formatoCargue.getForLineaInicial() : 0;

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
						String mensaje = "La cantidad de campos encontrada no concuerda con la cantidad indicada en la parametrizacion del formato";
						error.setMensaje(mensaje);

						archivo.getErrores().add(error);
						continue BucleArchivos;
					}

					boolean errorNombresColumnas = false;
					List<Columna> columnas = new ArrayList<Columna>();
					int pos = 0;

					BucleValidarCampos: for (String nombreColumna : listaColumnas) {

						DetallesFormato colDef = tomarDefinicionColumna(nombreColumna);

						if (colDef == null) {

							ErrorGenerico error = new ErrorGenerico();
							String mensaje = "El nombre de la columna '" + nombreColumna
									+ "' en el archivo, no concuerda con ningun campo indicado en la parametrizacion del formato";
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

								// textoFallo +=
								// "Valor nulo o en blanco, es requerido";
								//
								// ErrorGenerico
								// error = new
								// ErrorGenerico();
								// error.setIdFila(idFila);
								// error.setColumna(columnaDato.getNombre());
								// error.setMensaje(textoFallo);
								// columnaDato.setError(error);
								// //fila.setError(error);
								//
								// columnasDatos.add(columnaDato);

								continue BucleCeldas;
							} else if (((celda == null) || (celda.getCellType() == Cell.CELL_TYPE_BLANK)) && !columnaDato.isRequerido()) {
								continue BucleCeldas;
							}

							if ("N".equalsIgnoreCase(columnaDato.getTipoDato()) || "M".equalsIgnoreCase(columnaDato.getTipoDato())) {

								if (celda.getCellType() == Cell.CELL_TYPE_NUMERIC) {

									try {

										Double valor = celda.getNumericCellValue();
										String valorFormateado = formatearDouble(valor, columnaDato.getFormato());
										columnaDato.setValor(valorFormateado);

									} catch (Exception e) {

										textoFallo = "Existe un error al leer el dato como tipo de dato numérico, " + e.getMessage();

										ErrorGenerico error = new ErrorGenerico();
										error.setIdFila(idFila);
										error.setColumna(columnaDato.getNombre());
										error.setMensaje(textoFallo);
										error.setValor(celda.getStringCellValue());
										columnaDato.setError(error);
										//fila.setError(error);

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

									} catch (Exception e) {

										textoFallo = "Existe un error al leer el dato como tipo de dato numérico, " + e.getMessage();

										ErrorGenerico error = new ErrorGenerico();
										error.setIdFila(idFila);
										error.setColumna(columnaDato.getNombre());
										error.setMensaje(textoFallo);
										error.setValor(celda.getStringCellValue());
										columnaDato.setError(error);
										//fila.setError(error);

										columnasDatos.add(columnaDato);

										continue BucleCeldas;
									}
								}

							} else if ("C".equalsIgnoreCase(columnaDato.getTipoDato())) {

								if (celda != null) {
									if (celda.getCellType() == Cell.CELL_TYPE_STRING) {

										try {

											String valor = celda.getStringCellValue();
											String valorColumna = String.format(columnaDato.getFormato(), valor);

											columnaDato.setValor(valorColumna);

										} catch (Exception e) {

											textoFallo = "Existe un error al leer el dato como tipo de dato téxto, " + e.getMessage();

											ErrorGenerico error = new ErrorGenerico();
											error.setIdFila(idFila);
											error.setColumna(columnaDato.getNombre());
											error.setMensaje(textoFallo);
											error.setValor(celda.getStringCellValue());
											columnaDato.setError(error);
											//fila.setError(error);

											columnasDatos.add(columnaDato);

											continue BucleCeldas;
										}
									}
								}

							} else if ("F".equalsIgnoreCase(columnaDato.getTipoDato())) {

								if (celda.getCellType() == Cell.CELL_TYPE_NUMERIC) {

									try {

										SimpleDateFormat formato = new SimpleDateFormat(columnaDato.getFormato());
										String datoFecha = formato.format(celda.getDateCellValue());
										columnaDato.setValor(datoFecha);

									} catch (Exception ex) {

										textoFallo = "Existe un error al leer el dato como tipo de dato fecha, " + ex.getMessage();

										ErrorGenerico error = new ErrorGenerico();
										error.setIdFila(idFila);
										error.setColumna(columnaDato.getNombre());
										error.setMensaje(textoFallo);
										error.setValor(celda.getStringCellValue());
										columnaDato.setError(error);
										//fila.setError(error);

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
										error.setIdFila(idFila);
										error.setColumna(columnaDato.getNombre());
										error.setMensaje(textoFallo);
										error.setValor(celda.getStringCellValue());
										columnaDato.setError(error);
										//fila.setError(error);

										columnasDatos.add(columnaDato);

										continue BucleCeldas;
									}
								}
							}

							columnasDatos.add(columnaDato);
						}

						fila.setColumnas(columnasDatos);
						filas.add(fila);

						String colCuentaContable = FacesUtils.getPropsSGC().getString("campoCuentaContable");
						String colValor = FacesUtils.getPropsSGC().getString("campoValor");

						for (Columna columna : columnasDatos) {
							System.out.println("Nombre : " + columna.getNombre() + " Valor : " + columna.getValor());
							if (null != columna.getValor()) {
								if (colCuentaContable.equals(columna.getNombre())) {
									listaLlaves.add(columna.getValor());
								} else if (colValor.equals(columna.getNombre())) {
									listaValores.add(columna.getValor());
								}
							}

						}

					}

					listaCuentas.add(valCuenta1);
					listaCuentas.add(valCuenta2);
					listaCuentas.add(valCuenta3);

					for (int Ol = 0; Ol < listaCuentas.size(); Ol++) {
						boolean ResultadoValidacion = validadorGenerico(listaCuentas.get(Ol));
						if (!ResultadoValidacion) {
							throw new Exception(MSJ_CUENTASERROR);
						}
						sumaC3C2 = valorC2 + valorC3;
					}

					if (!sumaC3C2.equals(valorC1) && (valorC2 != 0.0 && valorC3 != 0.0)) {
						throw new Exception(MSJ_CUENTASERROR);
					}

					archivo.setFilas(filas);
					archivo.setEntidad(entidad);
					archivo.setFormato(formatoCargue);
					archivo.setCorte(corte);

				}

			} else if (tipoValidacion.equals(formatoDetalladoId)) {

				BucleArchivos: for (Archivo archivo : archivos) {
					Double acumDebitos = 0.0, acumCreditos = 0.0;
					Integer contAuxi = 0;
					Integer contAuxi1 = 0;

					Workbook libro = null;

					try {

						fis = new FileInputStream(archivo.getNombre());
						libro = ("xlsx".equalsIgnoreCase(archivo.getExtension())) ? new XSSFWorkbook(fis) : new HSSFWorkbook(fis);

					} catch (IOException e) {
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

					String nombreHoja = formatoCargue.getForNombreHoja();
					Sheet hoja = null;

					int filaEncabezado = (formatoCargue.getForLineaInicial() != null) ? formatoCargue.getForLineaInicial() : 0;

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
						String mensaje = "La cantidad de campos encontrada no concuerda con la cantidad indicada en la parametrizacion del formato";
						error.setMensaje(mensaje);

						archivo.getErrores().add(error);
						continue BucleArchivos;
					}

					boolean errorNombresColumnas = false;
					List<Columna> columnas = new ArrayList<Columna>();
					int pos = 0;

					BucleValidarCampos: for (String nombreColumna : listaColumnas) {

						DetallesFormato colDef = tomarDefinicionColumna(nombreColumna);

						if (colDef == null) {

							ErrorGenerico error = new ErrorGenerico();
							String mensaje = "El nombre de la columna '" + nombreColumna
									+ "' en el archivo, no concuerda con ningun campo indicado en la parametrizacion del formato";
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

								textoFallo += "Valor nulo o en blanco, es requerido";

								ErrorGenerico error = new ErrorGenerico();
								error.setIdFila(idFila);
								error.setColumna(columnaDato.getNombre());
								error.setMensaje(textoFallo);
								columnaDato.setError(error);
								//fila.setError(error);

								columnasDatos.add(columnaDato);

								continue BucleCeldas;
							}

							if ("N".equalsIgnoreCase(columnaDato.getTipoDato()) || "M".equalsIgnoreCase(columnaDato.getTipoDato())) {

								if (celda.getCellType() == Cell.CELL_TYPE_NUMERIC) {

									try {

//										String valorFormateado = null;
										Double valor = celda.getNumericCellValue();
										String valorFormateado = formatearDouble(valor, columnaDato.getFormato());
										columnaDato.setValor(valorFormateado);

//										if (columnaDato.getNombre().equals(columnaDebito)) {
//											System.out.println("***Sumando en Debito");
//											acumDebitos = acumDebitos + celda.getNumericCellValue();
//											valorFormateado = formatearDouble(acumDebitos, columnaDato.getFormato());
//										} else if (columnaDato.getNombre().equals(columnaCredito)) {
//											System.out.println("***Sumando en Credito");
//											acumCreditos = acumCreditos + celda.getNumericCellValue();
//											valorFormateado = formatearDouble(acumCreditos, columnaDato.getFormato());
//										} else {
//											Double valor = celda.getNumericCellValue();
//											valorFormateado = formatearDouble(valor, columnaDato.getFormato());
//										}
//
//										columnaDato.setValor(valorFormateado);

									} catch (Exception e) {

										textoFallo = "Existe un error al leer el dato como tipo de dato numérico, " + e.getMessage();

										ErrorGenerico error = new ErrorGenerico();
										error.setIdFila(idFila);
										error.setColumna(columnaDato.getNombre());
										error.setMensaje(textoFallo);
										error.setValor(celda.getStringCellValue());
										columnaDato.setError(error);
										//fila.setError(error);

										columnasDatos.add(columnaDato);

										continue BucleCeldas;
									}
								}

								if (celda.getCellType() == Cell.CELL_TYPE_STRING) {

									try {

										String valorCelda = celda.getStringCellValue();
										Double val = Double.parseDouble(valorCelda);
										String valorFormateado = formatearDouble(val, columnaDato.getFormato());
										columnaDato.setValor(valorFormateado);

									} catch (Exception e) {

										textoFallo = "Existe un error al leer el dato como tipo de dato numérico, " + e.getMessage();

										ErrorGenerico error = new ErrorGenerico();
										error.setIdFila(idFila);
										error.setColumna(columnaDato.getNombre());
										error.setMensaje(textoFallo);
										error.setValor(celda.getStringCellValue());
										columnaDato.setError(error);
										//fila.setError(error);

										columnasDatos.add(columnaDato);

										continue BucleCeldas;
									}
								}

							} else if ("C".equalsIgnoreCase(columnaDato.getTipoDato())) {

								if (celda.getCellType() == Cell.CELL_TYPE_STRING) {

									try {

										String valor = celda.getStringCellValue();
										String valorColumna = String.format(columnaDato.getFormato(), valor);

										columnaDato.setValor(valorColumna);

									} catch (Exception e) {

										textoFallo = "Existe un error al leer el dato como tipo de dato téxto, " + e.getMessage();

										ErrorGenerico error = new ErrorGenerico();
										error.setIdFila(idFila);
										error.setColumna(columnaDato.getNombre());
										error.setMensaje(textoFallo);
										error.setValor(celda.getStringCellValue());
										columnaDato.setError(error);
										//fila.setError(error);

										columnasDatos.add(columnaDato);

										continue BucleCeldas;
									}
								}

							} else if ("F".equalsIgnoreCase(columnaDato.getTipoDato())) {

								if (celda.getCellType() == Cell.CELL_TYPE_NUMERIC) {

									try {

										SimpleDateFormat formato = new SimpleDateFormat(columnaDato.getFormato());
										String datoFecha = formato.format(celda.getDateCellValue());
										columnaDato.setValor(datoFecha);

									} catch (Exception ex) {

										textoFallo = "Existe un error al leer el dato como tipo de dato fecha, " + ex.getMessage();

										ErrorGenerico error = new ErrorGenerico();
										error.setIdFila(idFila);
										error.setColumna(columnaDato.getNombre());
										error.setMensaje(textoFallo);
										error.setValor(celda.getStringCellValue());
										columnaDato.setError(error);
										//fila.setError(error);

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
										error.setIdFila(idFila);
										error.setColumna(columnaDato.getNombre());
										error.setMensaje(textoFallo);
										error.setValor(celda.getStringCellValue());
										columnaDato.setError(error);
										//fila.setError(error);

										columnasDatos.add(columnaDato);

										continue BucleCeldas;
									}
								}
							}

							columnasDatos.add(columnaDato);
						}
						fila.setColumnas(columnasDatos);
						filas.add(fila);

						for (Columna columna : columnasDatos) {
							if (null != columna.getValor()) {
								if (columna.getNombre().equals(columnaCredito)) {
									valCreditos.add(columna.getValor());
								} else if (columna.getNombre().equals(columnaDebito)) {
									valDebitos.add(columna.getValor());
								}
							}

						}

					}
					
					for (int la = 0; la < valCreditos.size(); la++) {
						String valorCre= convertidorPuntos(valCreditos.get(la));
						String valorDeb= convertidorPuntos(valDebitos.get(la));
						Double valorCredito = Double.parseDouble(valorCre);
						Double valorDebito = Double.parseDouble(valorDeb);
						acumCreditos = acumCreditos + valorCredito;
						acumDebitos =  acumDebitos+ valorDebito;
					}

					System.out.println("Acumulado de " + columnaCredito + ": " + acumCreditos);
					System.out.println("Acumulado de " + columnaDebito + ": " + acumDebitos);
					if (!acumCreditos.equals(acumDebitos)) {
						String mensajeError = "Los creditos y debitos no corcuerdan.";
						throw new Exception(mensajeError);
					} else {
						archivo.setFilas(filas);
						archivo.setEntidad(entidad);
						archivo.setFormato(formatoCargue);
						archivo.setCorte(corte);
					}

				}
			} else {
				String mensajeError = "Este formato no esta disponible para validaciones";
				throw new Exception(mensajeError);
			}

		} else {

			String mensajeError = "El archivo a cargar no cumple con la validación de vigencias y no existe una autorización vigente que lo avale";
			throw new Exception(mensajeError);
		}
	}

	/**
	 * Metodo utilizado para localizar una columna del archivo dentro de la
	 * parametrizacion
	 * 
	 * @param nombreColumna
	 * @return
	 */
	private DetallesFormato tomarDefinicionColumna(String nombreColumna) {

		for (DetallesFormato det : detallesFormato) {

			if (det.getDetNombre().equals(nombreColumna)) {
				return det;
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

		Autorizaciones autorizaciones = serviceDao.getAutorizacionesDao().obtenerAutorizacionPorFormatoEntidadVigente(codigoFormato,
				codEntidad);

		if (registroCargues != null && registroCargues.size() > 0) {

			if (autorizaciones == null) {
				esValido = false;
				throw new Exception("No existe autorización vigente que avale el cargue");

			} else {

				esActualizacion = true;
			}

		} else {

			if (corte.getFcrVencido()) {

				if (autorizaciones == null) {
					esValido = false;
					throw new Exception(
							"La fecha de corte del archivo a cargar se encuentra vencida y no presenta autorización vigente que avale el cargue");
				}

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

	public void cargarDatosListas(Object datoCarga) {

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

}
