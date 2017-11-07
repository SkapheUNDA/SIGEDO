/**
 * 
 */
package la.netco.sgc.uilayer.beans.util;

import java.io.InputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javax.faces.bean.ManagedProperty;

import la.netco.core.businesslogic.services.ServiceDao;
import la.netco.core.uilayer.beans.utils.FacesUtils;
import la.netco.sgc.persistence.dto.CortesFormato;
import la.netco.sgc.persistence.dto.DetallesFormato;
import la.netco.sgc.persistence.dto.Entidades;
import la.netco.sgc.persistence.dto.Formatos;
import la.netco.sgc.persistence.dto.FormatosTiposDato;
import la.netco.sgc.persistence.dto.ParametrosSGC;
import la.netco.sgc.persistence.dto.RegistroCargue;
import la.netco.sgc.persistence.dto.ReporteGerencialConsolidado;
import la.netco.sgc.persistence.dto.ReporteGerencialDetallado;
import la.netco.sgc.persistence.dto.ReporteTrimestral;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;

/**
 * @author cguzman
 * 
 */
public class ReportesUtil implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String REPORTE_TRIMESTRAL = "TRIMESTRAL";

	private static final String REPORTE_GERENCIAL_DETALLADO = "GERENCIAL_DETALLADO";

	private static final String REPORTE_GERENCIAL_CONSOLIDADO = "GERENCIAL_CONSOLIDADO";

	private static final String NOMREPORTE_GERENCIAL_DETALLADO = "gerencial-detallado";

	private static final String NOMREPORTE_GERENCIAL_CONSOLIDADO = "gerencial-consolidado";

	private static final String REPORTE_ANUAL_TRIMESTRAL = "ANUAL_TRIMESTRAL";

	private static final String REPORTE_BALANCE_GENERAL = "BALANCE_GENERAL";

	private static final String MSJ_ERRCORTES = "Este formato no tiene  cortes de tipo trimestral.";

	@ManagedProperty(value = "#{serviceDao}")
	transient final private ServiceDao serviceDao;

	private final ResourceBundle props;

	private Formatos formatoReporte;

	public ReportesUtil(ServiceDao serviceDao) {
		// TODO Auto-generated constructor stub
		props = ResourceBundle
				.getBundle("la.netco.sgc.reportes.sources.propiedades");
		this.serviceDao = serviceDao;
	}

	private String construir(Map<String, Object> params, String reporte,
			String formato, String nombreReporteGenerado, String querySQL)
			throws Exception {

		String nomReporteGenerado = null;

		try {
			String nombreArchivo = tomarArchivoReporte(reporte);
			String rutaReportes = props.getString("rutaReportes");

			InputStream archivo = ReportesUtil.class
					.getResourceAsStream(rutaReportes + nombreArchivo);
			JasperReport jasperReports = JasperCompileManager
					.compileReport(archivo);

			Connection conn = obtenerConexion();
			JasperPrint rep = JasperFillManager.fillReport(jasperReports,
					params, conn);

			String rutaSalida = ReportesUtil.class.getResource("").toString();
			rutaSalida = rutaSalida.substring(6, rutaSalida.length());

			if ("PDF".equals(formato)) {

				JRPdfExporter pdfExporter = new JRPdfExporter();

				pdfExporter.setParameter(JRExporterParameter.JASPER_PRINT, rep);
				pdfExporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME,
						rutaSalida + nombreReporteGenerado + ".pdf");

				pdfExporter.exportReport();

				nomReporteGenerado = nombreReporteGenerado + ".pdf";

			} else if ("XLS".equals(formato)) {

				JRXlsExporter xlsExporter = new JRXlsExporter();

				xlsExporter.setParameter(JRExporterParameter.JASPER_PRINT, rep);
				xlsExporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME,
						rutaSalida + nombreReporteGenerado + ".xls");

				xlsExporter.exportReport();

				nomReporteGenerado = nombreReporteGenerado + ".xls";
			}
			borrarDatos(querySQL);

		} catch (JRException e) {
			throw new Exception(e.getMessage());
		}

		return nomReporteGenerado;
	}

	public String ejecutarReporteTrimestral(Entidades entidad,
			CortesFormato trimestreInicial, CortesFormato trimestreFinal,
			Formatos formatoReporte) throws Exception {

		String sql = "delete FROM SGC.Reporte_Trimestral";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("P_TRIMESTRE_INICIAL", trimestreInicial.getFcrCorte());
		params.put("P_TRIMESTRE_FINAL", trimestreFinal.getFcrCorte());

		this.formatoReporte = formatoReporte;
		tomarDatosReporteTrimestral(entidad, trimestreInicial, trimestreFinal,
				params);

		return construir(params, REPORTE_TRIMESTRAL, "XLS",
				"reporte-trimestral", sql);
	}

	public String ejecutarReporteGerencialDetallado() throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		String sql = "delete FROM SGC.Reporte_Gerencial_Detallado";
		Integer idFormatoUso = Integer.parseInt(props
				.getString("idFormatoInformeGestion"));
		this.formatoReporte = (Formatos) serviceDao.getGenericCommonDao().read(
				Formatos.class, idFormatoUso);
		tomarDatosReporteGerencial(2);

		return construir(params, REPORTE_GERENCIAL_DETALLADO, "XLS",
				NOMREPORTE_GERENCIAL_DETALLADO, sql);

	}

	@SuppressWarnings("unchecked")
	public String ejecutarReporteGerencialConsolidado() throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		HashMap<Integer, Integer> paramsBusq = new HashMap<Integer, Integer>();
		paramsBusq = new HashMap<Integer, Integer>();
		paramsBusq.put(
				0,
				Integer.parseInt(FacesUtils.getPropsSGC().getString(
						"idConceptoUno")));
		paramsBusq.put(
				1,
				Integer.parseInt(FacesUtils.getPropsSGC().getString(
						"idConceptoDos")));
		paramsBusq.put(
				2,
				Integer.parseInt(FacesUtils.getPropsSGC().getString(
						"idConceptoTres")));
		paramsBusq.put(
				3,
				Integer.parseInt(FacesUtils.getPropsSGC().getString(
						"idConceptoCuatro")));

		List<ParametrosSGC> listaParametosSGC = (List<ParametrosSGC>) serviceDao
				.getGenericCommonDao().executeFind(ParametrosSGC.class,
						paramsBusq, ParametrosSGC.NAMED_QUERY_GET_BY_FOUR);
		params.put("PARAM_CONCEPTOUNO", listaParametosSGC.get(0)
				.getNombreParam());
		params.put("PARAM_CONCEPTODOS", listaParametosSGC.get(1)
				.getNombreParam());
		params.put("PARAM_CONCEPTOTRES", listaParametosSGC.get(2)
				.getNombreParam());
		params.put("PARAM_CONCEPTOCUATRO", listaParametosSGC.get(3)
				.getNombreParam());
		String sql = "delete FROM SGC.Reporte_Gerencial_Consolidado";
		Integer idFormatoUso = Integer.parseInt(props
				.getString("idFormatoInformeGestion"));
		this.formatoReporte = (Formatos) serviceDao.getGenericCommonDao().read(
				Formatos.class, idFormatoUso);
		tomarDatosReporteGerencial(1);

		return construir(params, REPORTE_GERENCIAL_CONSOLIDADO, "XLS",
				NOMREPORTE_GERENCIAL_CONSOLIDADO, sql);

	}

	public String ejecutarReporteAnualTrimestral(String anio) throws Exception {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("P_ANIO", anio);
		String sql = "delete FROM SGC.";
		return construir(params, REPORTE_ANUAL_TRIMESTRAL, "XLS",
				"reporte-anual-trimestral", sql);
	}

	public String ejecutarReporteBalanceGeneral() throws Exception {

		Map<String, Object> params = new HashMap<String, Object>();
		String sql = "delete FROM SGC.";
		return construir(params, REPORTE_BALANCE_GENERAL, "XLS",
				"reporte-balance-general", sql);
	}

	private Connection obtenerConexion() throws Exception {

		Connection conn = null;
		String url = props.getString("url");
		String userName = props.getString("userName");
		String password = props.getString("password");

		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			conn = java.sql.DriverManager
					.getConnection(url, userName, password);

		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return conn;
	}

	private static String tomarArchivoReporte(String reporte) {

		String archivo = "";

		if (REPORTE_TRIMESTRAL.equals(reporte)) {
			archivo = "trimestral";
		} else if (REPORTE_ANUAL_TRIMESTRAL.equals(reporte)) {
			archivo = "anual-trimestral";
		} else if (REPORTE_BALANCE_GENERAL.equals(reporte)) {
			archivo = "balance-general";
		} else if (REPORTE_GERENCIAL_DETALLADO.equals(reporte)) {
			archivo = "gerencialdetallado";
		} else if (REPORTE_GERENCIAL_CONSOLIDADO.equals(reporte)) {
			archivo = "gerencialConsolidado";
		}

		return archivo + ".jrxml";
	}

	@SuppressWarnings("unchecked")
	private void tomarDatosReporteGerencial(Integer casoUso) throws Exception {

		Set<DetallesFormato> columnas = formatoReporte.getDetallesFormatos();
		List<Entidades> listaEntidades = new ArrayList<Entidades>();
		List<String> listaSumas = new ArrayList<String>();
		List<String> listaRepFinalAgg = new ArrayList<String>();
		List<CortesFormato> listaCorteFormatos = new ArrayList<CortesFormato>();
		HashMap<Integer, Integer> paramsBusq = new HashMap<Integer, Integer>();
		HashMap<Integer, Object> paramsBusqDos = new HashMap<Integer, Object>();
		HashMap<Integer, Object> paramsBusqTres = new HashMap<Integer, Object>();
		Integer idColumnaValor = null;
		Integer valorTrimestral = null;
		Integer idFormato = formatoReporte.getForCodigo();
		Double sumaValores = 0.0;
		String formatoTipoDato = "";
		String valorCuenta = props.getString("campoValorInformeGestion");

		Calendar calendarioAuxi = Calendar.getInstance();
		String anoActual = String.valueOf(calendarioAuxi.get(Calendar.YEAR));

		// Cargando Detalles de Formato
		for (DetallesFormato detallesFormato : columnas) {

			if (valorCuenta.equals(detallesFormato.getDetNombre())) {
				idColumnaValor = detallesFormato.getDetCodigo();
				FormatosTiposDato ftd = (FormatosTiposDato) serviceDao
						.getGenericCommonDao().read(FormatosTiposDato.class,
								detallesFormato.getFtdCodigo());
				formatoTipoDato = ftd.getFtdFormato();
			}
		}

		// Cargando Cortes de Formato por el Formato y validando que el numero
		// de cortes sea trimestral
		paramsBusq.put(0, idFormato);
		listaCorteFormatos = (List<CortesFormato>) serviceDao
				.getGenericCommonDao().executeFind(CortesFormato.class,
						paramsBusq, CortesFormato.NAMED_QUERY_GET_BY_FORMATO);

		ParametrosSGC objParametrosSGC = (ParametrosSGC) serviceDao
				.getGenericCommonDao().read(
						ParametrosSGC.class,
						Integer.parseInt(FacesUtils.getPropsSGC().getString(
								"idParamTrim")));

		valorTrimestral = Integer.parseInt(objParametrosSGC.getValorParam());

		if (!valorTrimestral.equals(listaCorteFormatos.size())) {
			throw new Exception(MSJ_ERRCORTES);
		}

		// Cargando Conceptos de Cuentas Existentes y Cuentas Existentes
		paramsBusq = new HashMap<Integer, Integer>();
		paramsBusq.put(
				0,
				Integer.parseInt(FacesUtils.getPropsSGC().getString(
						"idConceptoUno")));
		paramsBusq.put(
				1,
				Integer.parseInt(FacesUtils.getPropsSGC().getString(
						"idConceptoDos")));
		paramsBusq.put(
				2,
				Integer.parseInt(FacesUtils.getPropsSGC().getString(
						"idConceptoTres")));
		paramsBusq.put(
				3,
				Integer.parseInt(FacesUtils.getPropsSGC().getString(
						"idConceptoCuatro")));

		List<ParametrosSGC> listaParametosSGC = (List<ParametrosSGC>) serviceDao
				.getGenericCommonDao().executeFind(ParametrosSGC.class,
						paramsBusq, ParametrosSGC.NAMED_QUERY_GET_BY_FOUR);

		// Recorrido por Entidad
		listaEntidades = (List<Entidades>) serviceDao.getGenericCommonDao()
				.loadAll(Entidades.class);

		for (Entidades entidades : listaEntidades) {

			Integer idEntidadActual = entidades.getEntCodigo();

			listaRepFinalAgg.add(entidades.getEntObjetoSocial());

			for (int pa = 0; pa < listaParametosSGC.size(); pa++) {
				listaRepFinalAgg
						.add(listaParametosSGC.get(pa).getNombreParam());
			}

			for (int pa = 0; pa < listaParametosSGC.size(); pa++) {

				for (int ol = 0; ol < listaCorteFormatos.size(); ol++) {

					paramsBusqDos.put(0, idFormato);
					paramsBusqDos.put(1, idEntidadActual);
					paramsBusqDos.put(2, listaParametosSGC.get(pa)
							.getValorParam());
					paramsBusqDos.put(3, listaCorteFormatos.get(ol)
							.getFcrCodigo());
					Integer crgNoRegistroReci = (Integer) serviceDao
							.getGenericCommonDao()
							.executeUniqueResult(
									RegistroCargue.NAMED_QUERY_GET_REGISTRO_CARGUE_FOR_REP,
									paramsBusqDos);
					if (null == crgNoRegistroReci) {
						listaRepFinalAgg.add("0");
						sumaValores = sumaValores + 0.0;
					} else {
						if (listaCorteFormatos.get(ol).getFcrCorte()
								.substring(0, 4).equals(anoActual)) {

							paramsBusqTres.put(0, idFormato);
							paramsBusqTres.put(1, idEntidadActual);
							paramsBusqTres.put(2, listaCorteFormatos.get(ol)
									.getFcrCodigo());
							paramsBusqTres.put(3, crgNoRegistroReci);
							paramsBusqTres.put(4, idColumnaValor);
							String valorRegistroCargue = (String) serviceDao
									.getGenericCommonDao()
									.executeUniqueResult(
											RegistroCargue.NAMED_QUERY_GET_REGISTRO_CARGUE_FOR_REPDOS,
											paramsBusqTres);
							listaRepFinalAgg.add(valorRegistroCargue);
							sumaValores = sumaValores
									+ formatearDouble(valorRegistroCargue,
											formatoTipoDato);
						} else {
							listaRepFinalAgg.add("0");
						}
					}
				}
				String valorTotalSuma = sumaValores.toString();
				listaSumas.add(valorTotalSuma);
				sumaValores = 0.0;
				paramsBusqDos = new HashMap<Integer, Object>();
				paramsBusqTres = new HashMap<Integer, Object>();
			}

			for (int la = 0; la < listaSumas.size(); la++) {
				listaRepFinalAgg.add(listaSumas.get(la));
			}

			if (casoUso.equals(1)) {

				ReporteGerencialConsolidado repGC = new ReporteGerencialConsolidado();
				repGC.setRgcNomEntidad(entidades.getEntObjetoSocial());
				repGC.setRgcValorConceptoUno(listaSumas.get(0));
				repGC.setRgcValorConceptoDos(listaSumas.get(1));
				repGC.setRgcValorConceptoTres(listaSumas.get(2));
				repGC.setRgcValorConceptoCuatro(listaSumas.get(3));
				serviceDao.getGenericCommonDao().create(
						ReporteGerencialConsolidado.class, repGC);

			} else if (casoUso.equals(2)) {
				ReporteGerencialDetallado repGD = new ReporteGerencialDetallado();

				repGD.setRgdNomEntidad(listaRepFinalAgg.get(0));
				repGD.setRgdConceptoUno(listaRepFinalAgg.get(1));
				repGD.setRgdConceptoDos(listaRepFinalAgg.get(2));
				repGD.setRgdConceptoTres(listaRepFinalAgg.get(3));
				repGD.setRgdConceptoCuatro(listaRepFinalAgg.get(4));
				repGD.setRgdValorRecaudoUno(listaRepFinalAgg.get(5));
				repGD.setRgdValorRecaudoDos(listaRepFinalAgg.get(6));
				repGD.setRgdValorRecaudoTres(listaRepFinalAgg.get(7));
				repGD.setRgdValorRecaudoCuatro(listaRepFinalAgg.get(8));
				repGD.setRgdValorGAdmonUno(listaRepFinalAgg.get(9));
				repGD.setRgdValorGAdmonDos(listaRepFinalAgg.get(10));
				repGD.setRgdValorGAdmonTres(listaRepFinalAgg.get(11));
				repGD.setRgdValorGAdmonCuatro(listaRepFinalAgg.get(12));
				repGD.setRgdValorGBienestarUno(listaRepFinalAgg.get(13));
				repGD.setRgdValorGBienestarDos(listaRepFinalAgg.get(14));
				repGD.setRgdValorGBienestarTres(listaRepFinalAgg.get(15));
				repGD.setRgdValorGBienestarCuatro(listaRepFinalAgg.get(16));
				repGD.setRgdValorDistribucionesUno(listaRepFinalAgg.get(17));
				repGD.setRgdValorDistribucionesDos(listaRepFinalAgg.get(18));
				repGD.setRgdValorDistribucionesTres(listaRepFinalAgg.get(19));
				repGD.setRgdValorDistribucionesCuatro(listaRepFinalAgg.get(20));
				repGD.setRgdSumaConceptoUno(listaRepFinalAgg.get(21));
				repGD.setRgdSumaConceptoDos(listaRepFinalAgg.get(22));
				repGD.setRgdSumaConceptoTres(listaRepFinalAgg.get(23));
				repGD.setRgdSumaConceptoCuatro(listaRepFinalAgg.get(24));

				serviceDao.getGenericCommonDao().create(
						ReporteGerencialDetallado.class, repGD);
			}
			listaRepFinalAgg = new ArrayList<String>();
			listaSumas = new ArrayList<String>();
		}

	}

	@SuppressWarnings("unused")
	private void tomarDatosReporteTrimestral(Entidades entidad,
			CortesFormato trimestreInicial, CortesFormato trimestreFinal,
			Map<String, Object> params) throws Exception {

		Set<DetallesFormato> columnas = formatoReporte.getDetallesFormatos();
		Integer idColumnaCuenta = null;
		Integer idColumnaConcepto = null;
		Integer idColumnaSaldoActual = null;

		String cuenta = props.getString("campoCuenta");
		String concepto = props.getString("campoNombreCuenta");
		String saldo = props.getString("campoSaldoActual");
		String formatoTipoDato = "";

		for (DetallesFormato df : columnas) {

			if (cuenta.equals(df.getDetNombre())) {
				idColumnaCuenta = df.getDetCodigo();
			}

			if (concepto.equals(df.getDetNombre())) {
				idColumnaConcepto = df.getDetCodigo();
			}

			if (saldo.equals(df.getDetNombre())) {
				idColumnaSaldoActual = df.getDetCodigo();
				FormatosTiposDato ftd = (FormatosTiposDato) serviceDao
						.getGenericCommonDao().read(FormatosTiposDato.class,
								df.getFtdCodigo());
				formatoTipoDato = ftd.getFtdFormato();
			}
		}

		params.put("P_FORMATO_TIPO_DATO", formatoTipoDato);
		System.out.println("formatoTipoDato >>> " + formatoTipoDato);

		Integer idFormato = formatoReporte.getForCodigo();
		Integer idEntidad = entidad.getEntCodigo();
		Integer idCorteInicial = trimestreInicial.getFcrCodigo();
		Integer idCorteFinal = trimestreFinal.getFcrCodigo();

		List<RegistroCargue> datosCuentaA = serviceDao.getRegistroCargueDao()
				.tomarRegistrosCarguePorFormatoEntidadCorteDet(idFormato,
						idEntidad, idCorteInicial, idColumnaCuenta);
		List<RegistroCargue> datosConceptoA = serviceDao.getRegistroCargueDao()
				.tomarRegistrosCarguePorFormatoEntidadCorteDet(idFormato,
						idEntidad, idCorteInicial, idColumnaConcepto);
		List<RegistroCargue> datosSaldoA = serviceDao.getRegistroCargueDao()
				.tomarRegistrosCarguePorFormatoEntidadCorteDet(idFormato,
						idEntidad, idCorteInicial, idColumnaSaldoActual);
		List<RegistroCargue> datosSaldoB = serviceDao.getRegistroCargueDao()
				.tomarRegistrosCarguePorFormatoEntidadCorteDet(idFormato,
						idEntidad, idCorteFinal, idColumnaSaldoActual);
		List<RegistroCargue> datosCuentaB = serviceDao.getRegistroCargueDao()
				.tomarRegistrosCarguePorFormatoEntidadCorteDet(idFormato,
						idEntidad, idCorteFinal, idColumnaCuenta);
		List<RegistroCargue> datosConceptoB = serviceDao.getRegistroCargueDao()
				.tomarRegistrosCarguePorFormatoEntidadCorteDet(idFormato,
						idEntidad, idCorteFinal, idColumnaConcepto);

		List<String> variaciones = new ArrayList<String>();
		List<String> incrementos = new ArrayList<String>();
		List<String> saldoA = new ArrayList<String>();
		List<String> saldoB = new ArrayList<String>();
		List<String> cuentas = new ArrayList<String>();
		List<String> conceptos = new ArrayList<String>();

		Double totalA = 0D;

		for (RegistroCargue r : datosSaldoA) {
			String valor = r.getCrgValor();
			Double valorDouble = formatearDouble(valor, formatoTipoDato);
			totalA += valorDouble;
		}

		BucleA: for (int i = 0; i < datosCuentaA.size(); i++) {

			for (int j = 0; j < datosCuentaB.size(); j++) {

				if (datosCuentaA.get(i).getCrgValor()
						.equals(datosCuentaB.get(j).getCrgValor())) {

					Double valorA = formatearDouble(datosSaldoA.get(i)
							.getCrgValor(), formatoTipoDato);
					Double valorB = formatearDouble(datosSaldoB.get(j)
							.getCrgValor(), formatoTipoDato);

					Double var = valorB - valorA;
					Double incr = var / totalA;

					String variacion = formatearDoubleString(var,
							formatoTipoDato);
					String incremento = formatearDoubleString(incr,
							formatoTipoDato);

					cuentas.add(datosCuentaA.get(i).getCrgValor());
					conceptos.add(datosConceptoA.get(i).getCrgValor());
					saldoA.add(datosSaldoA.get(i).getCrgValor());
					saldoB.add(datosSaldoB.get(j).getCrgValor());
					variaciones.add(variacion);
					incrementos.add(incremento);

					continue BucleA;
				}
			}
		}

		for (int i = 0; i < cuentas.size(); i++) {

			ReporteTrimestral rep = new ReporteTrimestral();
			rep.setRtrCuenta(cuentas.get(i));
			rep.setRtrConcepto(conceptos.get(i));
			rep.setRtrValorRecaudoA(saldoA.get(i));
			rep.setRtrValorRecaudoB(saldoB.get(i));
			rep.setRtrVariacion(variaciones.get(i));
			rep.setRtrIncremento(incrementos.get(i));

			serviceDao.getGenericCommonDao().create(ReporteTrimestral.class,
					rep);
		}
	}

	/**
	 * Mtodo que permite foratear los datos de tipo double
	 * 
	 * @param valor
	 * @return
	 * @throws ExcepcionDatos
	 */
	private Double formatearDouble(String valor, String formato)
			throws Exception {
		Double valorFinal = null;
		try {

			DecimalFormat decimalFormat = new DecimalFormat(formato);
			valorFinal = decimalFormat.parse(valor).doubleValue();

		} catch (Exception e) {
			throw new Exception(
					"Formato de valor inválido en Formatear Double "
							+ e.getMessage());
		}
		return valorFinal;
	}

	/**
	 * Mtodo que permite foratear los datos de tipo double
	 * 
	 * @param valor
	 * @return
	 * @throws ExcepcionDatos
	 */
	private String formatearDoubleString(Double valor, String formato)
			throws Exception {
		String valorFinal = null;
		try {

			DecimalFormat decimalFormat = new DecimalFormat(formato);
			valorFinal = decimalFormat.format(valor);

		} catch (Exception e) {
			throw new Exception(
					"Formato de valor inválido en Formatear Double "
							+ e.getMessage());
		}
		return valorFinal;
	}

	private void borrarDatos(String querySQL) throws Exception {

		try {

			serviceDao.getSpringGenericDao().updateQuery(querySQL);

		} catch (Exception e) {
			throw new Exception(
					"Se ha presentando un error indeterminado durante la generación del reporte "
							+ e.getMessage());
		}
	}

	/**
	 * @return the serviceDao
	 */
	public ServiceDao getServiceDao() {
		return serviceDao;
	}

	/**
	 * @return the props
	 */
	public ResourceBundle getProps() {
		return props;
	}

}
