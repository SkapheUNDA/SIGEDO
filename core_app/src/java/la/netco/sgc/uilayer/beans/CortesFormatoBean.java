/**
 * 
 */
package la.netco.sgc.uilayer.beans;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import la.netco.core.businesslogic.services.ServiceDao;
import la.netco.core.uilayer.beans.Constants;
import la.netco.core.uilayer.beans.utils.FacesUtils;
import la.netco.sgc.persistence.dto.Alertas;
import la.netco.sgc.persistence.dto.CortesFormato;
import la.netco.sgc.persistence.dto.Formatos;
import la.netco.sgc.persistence.dto.ParametrosSGC;
import la.netco.sgc.persistence.dto.RegistroCargue;
import la.netco.sgc.uilayer.beans.util.ReportesUtil;

/**
 * @author carlos
 * 
 */
/**
 * @author cpineros
 *
 */
@ManagedBean(name = "cortesFormatoBean")
@ViewScoped
public class CortesFormatoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{serviceDao}")
	transient private ServiceDao serviceDao;

	private Integer formatoSeleccionado;

	private List<SelectItem> listaFormatos;

	private List<SelectItem> listaParametros;

	private List<Formatos> arrFormatos;

	private List<ParametrosSGC> listaParamsSGC;

	private List<CortesFormato> cortesFormato;

	private Date fechaCorteFormato;

	private Date fechaActual;

	@Deprecated
	private boolean activo;

	@Deprecated
	private boolean vigente;

	private ReportesUtil reportesUtil;

	private CortesFormato cortesFormatoSeleccionado;

	private Integer idCorte;

	private Integer idParametroSeleccionado;

	/**
	 * 
	 */
	public CortesFormatoBean() {

		this.listaFormatos = new ArrayList<SelectItem>();
		this.arrFormatos = new ArrayList<Formatos>();
		this.cortesFormato = new ArrayList<CortesFormato>();
		this.listaParamsSGC = new ArrayList<ParametrosSGC>();
	}

	@SuppressWarnings("unused")
	@PostConstruct
	private void init() {

		System.out.println(">>>> Iniciando PostConstructor ");
		cargarFormatos();
		cargaListaParametros();

		Calendar cal = Calendar.getInstance();
		this.fechaActual = cal.getTime();
	}

	@SuppressWarnings("unchecked")
	public void cargaListaParametros() {
		HashMap<Integer, Integer> paramsBusq = new HashMap<Integer, Integer>();
		paramsBusq.put(
				0,
				Integer.parseInt(FacesUtils.getPropsSGC().getString(
						"idParamTrim")));
		paramsBusq.put(
				1,
				Integer.parseInt(FacesUtils.getPropsSGC().getString(
						"idParamSem")));
		paramsBusq.put(
				2,
				Integer.parseInt(FacesUtils.getPropsSGC().getString(
						"idParamAnual")));
		if (listaParametros == null) {
			listaParamsSGC = (List<ParametrosSGC>) serviceDao
					.getGenericCommonDao().executeFind(ParametrosSGC.class,
							paramsBusq, ParametrosSGC.NAMED_QUERY_GET_BY_THREE);

			listaParametros = new ArrayList<SelectItem>();

			for (ParametrosSGC parametrosSGC : listaParamsSGC) {
				listaParametros.add(new SelectItem(parametrosSGC.getIdParam(),
						parametrosSGC.getNombreParam()));
			}
		}
	}

	/**
	 * 
	 * Cargar Formatos.
	 */
	@SuppressWarnings("unchecked")
	private void cargarFormatos() {
		arrFormatos = (List<Formatos>) serviceDao.getGenericCommonDao()
				.loadAll(Formatos.class);

		for (Formatos f : arrFormatos) {
			listaFormatos
					.add(new SelectItem(f.getForCodigo(), f.getForNombre()));
			System.out.println(f.getForCodigo() + " " + f.getForNombre());
		}
		System.out.println("Sale formatos " + arrFormatos.size());
	}

	/**
	 * Accion Buscar
	 * 
	 * @param e
	 */
	public void buscar(ActionEvent e) {

		try {
			if (formatoSeleccionado != null) {

				cortesFormato = serviceDao
						.getCortesFormatoDao().obtenerCortesFormatoPorFormato(
								formatoSeleccionado);

				SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");

				for (CortesFormato corte : cortesFormato) {

					Date fechaCorte = formato.parse(corte.getFcrCorte());

					if (fechaCorte.compareTo(fechaActual) < 0
							&& !corte.getFcrVencido()) {
						corte.setFcrVencido(true);
						serviceDao.getGenericCommonDao().update(
								CortesFormato.class, corte);
					}
				}

				cortesFormato = serviceDao
						.getCortesFormatoDao().obtenerCortesFormatoPorFormato(
								formatoSeleccionado);
			}
		} catch (ParseException e1) {

			FacesUtils.addFacesMessageFromBundle(
					Constants.ERROR_OPERACIONES_ALERTAS,
					FacesMessage.SEVERITY_INFO,
					new String[] { e1.getMessage() });
		}

	}

	/**
	 * 
	 * Metodo utilizado para llevar a cabo el guardado de los datos
	 * 
	 * @return {@link String}
	 */
	@SuppressWarnings({ "unchecked" })
	public String accionGuardar() {

		String page = null;
		Integer acumVencidos = 0;

		try {
			HashMap<Integer, Integer> paramsBusq = new HashMap<Integer, Integer>();
			paramsBusq.put(0, formatoSeleccionado);
			List<CortesFormato> corteFormatoConsul = (List<CortesFormato>) serviceDao
					.getCortesFormatoDao().executeFind(CortesFormato.class,
							paramsBusq,
							CortesFormato.NAMED_QUERY_GET_BY_FORMATO);

			if (corteFormatoConsul != null) {

				for (CortesFormato cortesFormato : corteFormatoConsul) {
					if (cortesFormato.getFcrVencido()) {
						acumVencidos = acumVencidos + 1;
					}
				}
				Integer tamanoLista = corteFormatoConsul.size() + 1;

				if (acumVencidos.equals(tamanoLista)
						|| corteFormatoConsul.isEmpty()) {

					CortesFormato cortesFormato = new CortesFormato();

					SimpleDateFormat formato = new SimpleDateFormat(
							"yyyy-MM-dd");
					String fechaCorte = formato.format(fechaCorteFormato);

					Calendar fechaActual = Calendar.getInstance();
					fechaActual.set(Calendar.HOUR_OF_DAY, 0);
					fechaActual.set(Calendar.MINUTE, 0);
					fechaActual.set(Calendar.SECOND, 0);
					Calendar fechaCorteUso = Calendar.getInstance();
					fechaCorteUso.setTime(fechaCorteFormato);
					Date fechaAct = fechaActual.getTime();
					Date fechaUso;

					reportesUtil = new ReportesUtil(serviceDao);
					listaParamsSGC = (List<ParametrosSGC>) serviceDao
							.getGenericCommonDao().loadAll(ParametrosSGC.class);
					HashMap<Integer, Date> mapaFechas = new HashMap<Integer, Date>();

					Integer valTipoCorte = Integer.parseInt(listaParamsSGC.get(
							idParametroSeleccionado).getValorParam());
					Integer ValTrimestral = Integer.parseInt(reportesUtil
							.getProps().getString("idParamTrim"));
					Integer ValSemestral = Integer.parseInt(reportesUtil
							.getProps().getString("idParamSem"));
					Integer ValAnual = Integer.parseInt(reportesUtil.getProps()
							.getString("idParamAnual"));
					Integer diaCorte, mesCorte, anoCorte;
					Integer idRegistroCF;

					boolean corteActivo = false;
					boolean corteVencido = false;

					System.out.println("Valor de Periodicidad " + valTipoCorte);

					diaCorte = fechaCorteUso.get(Calendar.DAY_OF_MONTH);
					mesCorte = fechaCorteUso.get(Calendar.MONTH);
					anoCorte = fechaCorteUso.get(Calendar.YEAR);

					if (idParametroSeleccionado == ValTrimestral) {

						System.out.println("***Registrando periodo Trimestral");

						for (int Tri = 0; Tri < valTipoCorte; Tri++) {
							mapaFechas.put(Tri, fechaCorteUso.getTime());
							System.out.println("Fecha No " + (Tri + 1) + " : "
									+ mapaFechas.get(Tri));
							mesCorte = fechaCorteUso.get(Calendar.MONTH)
									+ (valTipoCorte - 1);
							if (mesCorte > 12) {
								mesCorte = mesCorte - 12;
								anoCorte = anoCorte + 1;
							}
							fechaCorteUso.set(anoCorte, mesCorte, diaCorte);
						}

						for (int Trim = 0; Trim < valTipoCorte; Trim++) {
							fechaUso = mapaFechas.get(Trim);

							if (fechaUso.before(fechaAct)) {
								corteActivo = false;
								corteVencido = true;
								cortesFormato.setFcrActivo(corteActivo);
								cortesFormato.setFcrVencido(corteVencido);
								System.out.println("***Corte Vencido");
							} else if (fechaUso.after(fechaAct)
									&& corteActivo == true) {
								corteActivo = false;
								corteVencido = false;
								cortesFormato.setFcrActivo(corteActivo);
								cortesFormato.setFcrVencido(corteVencido);
								corteActivo = true;
								System.out.println("***Corte Vigente");
							} else if (fechaUso.after(fechaAct)
									&& corteActivo == false) {
								corteActivo = true;
								corteVencido = false;
								cortesFormato.setFcrActivo(corteActivo);
								cortesFormato.setFcrVencido(corteVencido);
								System.out.println("***Corte Activo");
							}

							fechaCorte = formato.format(mapaFechas.get(Trim));

							cortesFormato.setFcrCorte(fechaCorte);
							Formatos formatoCorte = tomarObjetoListaSeleccionada(
									arrFormatos, formatoSeleccionado);
							//cortesFormato.setFormatos(formatoCorte);

							idRegistroCF = (Integer) serviceDao
									.getGenericCommonDao().create(
											CortesFormato.class, cortesFormato);

							System.out
									.println("***Exitoso Registro Corte Cod. : "
											+ idRegistroCF);
							cortesFormato = new CortesFormato();

						}

					} else if (idParametroSeleccionado == ValSemestral) {

						System.out.println("***Registrando periodo Semestral");

						for (int Sem = 0; Sem < valTipoCorte; Sem++) {

							mapaFechas.put(Sem, fechaCorteUso.getTime());
							System.out.println("Fecha No " + (Sem + 1) + " : "
									+ mapaFechas.get(Sem));
							mesCorte = fechaCorteUso.get(Calendar.MONTH)
									+ (valTipoCorte * 3);
							if (mesCorte > 12) {
								mesCorte = mesCorte - 12;
								anoCorte = anoCorte + 1;
							}
							fechaCorteUso.set(anoCorte, mesCorte, diaCorte);

						}

						for (int Sems = 0; Sems < valTipoCorte; Sems++) {
							fechaUso = mapaFechas.get(Sems);

							if (fechaUso.before(fechaAct)) {
								corteActivo = false;
								corteVencido = true;
								cortesFormato.setFcrActivo(corteActivo);
								cortesFormato.setFcrVencido(corteVencido);
								System.out.println("***Corte Vencido");
							} else if (fechaUso.after(fechaAct)
									&& corteActivo == true) {
								corteActivo = false;
								corteVencido = false;
								cortesFormato.setFcrActivo(corteActivo);
								cortesFormato.setFcrVencido(corteVencido);
								corteActivo = true;
								System.out.println("***Corte Vigente");
							} else if (fechaUso.after(fechaAct)
									&& corteActivo == false) {
								corteActivo = true;
								corteVencido = false;
								cortesFormato.setFcrActivo(corteActivo);
								cortesFormato.setFcrVencido(corteVencido);
								System.out.println("***Corte Activo");
							}

							fechaCorte = formato.format(mapaFechas.get(Sems));

							cortesFormato.setFcrCorte(fechaCorte);
							Formatos formatoCorte = tomarObjetoListaSeleccionada(
									arrFormatos, formatoSeleccionado);
							//cortesFormato.setFormatos(formatoCorte);

							idRegistroCF = (Integer) serviceDao
									.getGenericCommonDao().create(
											CortesFormato.class, cortesFormato);

							System.out
									.println("***Exitoso Registro Corte Cod. : "
											+ idRegistroCF);
							cortesFormato = new CortesFormato();

						}

					} else if (idParametroSeleccionado == ValAnual) {

						System.out.println("***Registrando periodo Anual");

						for (int Anu = 0; Anu < valTipoCorte; Anu++) {

							mesCorte = fechaCorteUso.get(Calendar.MONTH)
									+ (valTipoCorte + 11);
							if (mesCorte > 12) {
								mesCorte = mesCorte - 12;
								anoCorte = anoCorte + 1;
							}
							fechaCorteUso.set(anoCorte, mesCorte, diaCorte);
							mapaFechas.put(Anu, fechaCorteUso.getTime());
							System.out.println("Fecha No " + (Anu + 1) + " : "
									+ mapaFechas.get(Anu));

						}

						for (int Anual = 0; Anual < valTipoCorte; Anual++) {
							fechaUso = mapaFechas.get(Anual);

							if (fechaUso.before(fechaAct)) {
								corteActivo = false;
								corteVencido = true;
								cortesFormato.setFcrActivo(corteActivo);
								cortesFormato.setFcrVencido(corteVencido);
								System.out.println("***Corte Vencido");
							} else if (fechaUso.after(fechaAct)
									&& corteActivo == true) {
								corteActivo = false;
								corteVencido = false;
								cortesFormato.setFcrActivo(corteActivo);
								cortesFormato.setFcrVencido(corteVencido);
								corteActivo = true;
								System.out.println("***Corte Vigente");
							} else if (fechaUso.after(fechaAct)
									&& corteActivo == false) {
								corteActivo = true;
								corteVencido = false;
								cortesFormato.setFcrActivo(corteActivo);
								cortesFormato.setFcrVencido(corteVencido);
								System.out.println("***Corte Activo");
							}

							fechaCorte = formato.format(mapaFechas.get(Anual));

							cortesFormato.setFcrCorte(fechaCorte);
							Formatos formatoCorte = tomarObjetoListaSeleccionada(
									arrFormatos, formatoSeleccionado);
							//cortesFormato.setFormatos(formatoCorte);

							idRegistroCF = (Integer) serviceDao
									.getGenericCommonDao().create(
											CortesFormato.class, cortesFormato);

							System.out
									.println("***Exitoso Registro Corte Cod. : "
											+ idRegistroCF);

							cortesFormato = new CortesFormato();

						}

					}

					page = "listado";

					FacesUtils.addFacesMessageFromBundle(
							Constants.EXITO_OPERACIONES_ALERTAS,
							FacesMessage.SEVERITY_INFO);

				} else {
					FacesUtils.addFacesMessageFromBundle(
							Constants.INFO_OPERACION_FORMATOR,
							FacesMessage.SEVERITY_WARN);
				}
			} else {
				CortesFormato cortesFormato = new CortesFormato();

				SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
				String fechaCorte = formato.format(fechaCorteFormato);

				Calendar fechaActual = Calendar.getInstance();
				fechaActual.set(Calendar.HOUR_OF_DAY, 0);
				fechaActual.set(Calendar.MINUTE, 0);
				fechaActual.set(Calendar.SECOND, 0);
				Calendar fechaCorteUso = Calendar.getInstance();
				fechaCorteUso.setTime(fechaCorteFormato);
				Date fechaAct = fechaActual.getTime();
				Date fechaUso;

				reportesUtil = new ReportesUtil(serviceDao);
				listaParamsSGC = (List<ParametrosSGC>) serviceDao
						.getGenericCommonDao().loadAll(ParametrosSGC.class);
				HashMap<Integer, Date> mapaFechas = new HashMap<Integer, Date>();

				Integer valTipoCorte = Integer.parseInt(listaParamsSGC.get(
						idParametroSeleccionado).getValorParam());
				Integer ValTrimestral = Integer.parseInt(reportesUtil
						.getProps().getString("idParamTrim"));
				Integer ValSemestral = Integer.parseInt(reportesUtil.getProps()
						.getString("idParamSem"));
				Integer ValAnual = Integer.parseInt(reportesUtil.getProps()
						.getString("idParamAnual"));
				Integer diaCorte, mesCorte, anoCorte;
				Integer idRegistroCF;

				boolean corteActivo = false;
				boolean corteVencido = false;

				System.out.println("Valor de Periodicidad " + valTipoCorte);

				diaCorte = fechaCorteUso.get(Calendar.DAY_OF_MONTH);
				mesCorte = fechaCorteUso.get(Calendar.MONTH);
				anoCorte = fechaCorteUso.get(Calendar.YEAR);

				if (idParametroSeleccionado == ValTrimestral) {

					System.out.println("***Registrando periodo Trimestral");

					for (int Tri = 0; Tri < valTipoCorte; Tri++) {
						mapaFechas.put(Tri, fechaCorteUso.getTime());
						System.out.println("Fecha No " + (Tri + 1) + " : "
								+ mapaFechas.get(Tri));
						mesCorte = fechaCorteUso.get(Calendar.MONTH)
								+ (valTipoCorte - 1);
						if (mesCorte > 12) {
							mesCorte = mesCorte - 12;
							anoCorte = anoCorte + 1;
						}
						fechaCorteUso.set(anoCorte, mesCorte, diaCorte);
					}

					for (int Trim = 0; Trim < valTipoCorte; Trim++) {
						fechaUso = mapaFechas.get(Trim);

						if (fechaUso.before(fechaAct)) {
							corteActivo = false;
							corteVencido = true;
							cortesFormato.setFcrActivo(corteActivo);
							cortesFormato.setFcrVencido(corteVencido);
							System.out.println("***Corte Vencido");
						} else if (fechaUso.after(fechaAct)
								&& corteActivo == true) {
							corteActivo = false;
							corteVencido = false;
							cortesFormato.setFcrActivo(corteActivo);
							cortesFormato.setFcrVencido(corteVencido);
							corteActivo = true;
							System.out.println("***Corte Vigente");
						} else if (fechaUso.after(fechaAct)
								&& corteActivo == false) {
							corteActivo = true;
							corteVencido = false;
							cortesFormato.setFcrActivo(corteActivo);
							cortesFormato.setFcrVencido(corteVencido);
							System.out.println("***Corte Activo");
						}

						fechaCorte = formato.format(mapaFechas.get(Trim));

						cortesFormato.setFcrCorte(fechaCorte);
						Formatos formatoCorte = tomarObjetoListaSeleccionada(
								arrFormatos, formatoSeleccionado);
						//cortesFormato.setFormatos(formatoCorte);

						idRegistroCF = (Integer) serviceDao
								.getGenericCommonDao().create(
										CortesFormato.class, cortesFormato);

						System.out.println("***Exitoso Registro Corte Cod. : "
								+ idRegistroCF);
						cortesFormato = new CortesFormato();

					}

				} else if (idParametroSeleccionado == ValSemestral) {

					System.out.println("***Registrando periodo Semestral");

					for (int Sem = 0; Sem < valTipoCorte; Sem++) {

						mapaFechas.put(Sem, fechaCorteUso.getTime());
						System.out.println("Fecha No " + (Sem + 1) + " : "
								+ mapaFechas.get(Sem));
						mesCorte = fechaCorteUso.get(Calendar.MONTH)
								+ (valTipoCorte * 3);
						if (mesCorte > 12) {
							mesCorte = mesCorte - 12;
							anoCorte = anoCorte + 1;
						}
						fechaCorteUso.set(anoCorte, mesCorte, diaCorte);

					}

					for (int Sems = 0; Sems < valTipoCorte; Sems++) {
						fechaUso = mapaFechas.get(Sems);

						if (fechaUso.before(fechaAct)) {
							corteActivo = false;
							corteVencido = true;
							cortesFormato.setFcrActivo(corteActivo);
							cortesFormato.setFcrVencido(corteVencido);
							System.out.println("***Corte Vencido");
						} else if (fechaUso.after(fechaAct)
								&& corteActivo == true) {
							corteActivo = false;
							corteVencido = false;
							cortesFormato.setFcrActivo(corteActivo);
							cortesFormato.setFcrVencido(corteVencido);
							corteActivo = true;
							System.out.println("***Corte Vigente");
						} else if (fechaUso.after(fechaAct)
								&& corteActivo == false) {
							corteActivo = true;
							corteVencido = false;
							cortesFormato.setFcrActivo(corteActivo);
							cortesFormato.setFcrVencido(corteVencido);
							System.out.println("***Corte Activo");
						}

						fechaCorte = formato.format(mapaFechas.get(Sems));

						cortesFormato.setFcrCorte(fechaCorte);
						Formatos formatoCorte = tomarObjetoListaSeleccionada(
								arrFormatos, formatoSeleccionado);
						//cortesFormato.setFormatos(formatoCorte);

						idRegistroCF = (Integer) serviceDao
								.getGenericCommonDao().create(
										CortesFormato.class, cortesFormato);

						System.out.println("***Exitoso Registro Corte Cod. : "
								+ idRegistroCF);
						cortesFormato = new CortesFormato();

					}

				} else if (idParametroSeleccionado == ValAnual) {

					System.out.println("***Registrando periodo Anual");

					for (int Anu = 0; Anu < valTipoCorte; Anu++) {

						mesCorte = fechaCorteUso.get(Calendar.MONTH)
								+ (valTipoCorte + 11);
						if (mesCorte > 12) {
							mesCorte = mesCorte - 12;
							anoCorte = anoCorte + 1;
						}
						fechaCorteUso.set(anoCorte, mesCorte, diaCorte);
						mapaFechas.put(Anu, fechaCorteUso.getTime());
						System.out.println("Fecha No " + (Anu + 1) + " : "
								+ mapaFechas.get(Anu));

					}

					for (int Anual = 0; Anual < valTipoCorte; Anual++) {
						fechaUso = mapaFechas.get(Anual);

						if (fechaUso.before(fechaAct)) {
							corteActivo = false;
							corteVencido = true;
							cortesFormato.setFcrActivo(corteActivo);
							cortesFormato.setFcrVencido(corteVencido);
							System.out.println("***Corte Vencido");
						} else if (fechaUso.after(fechaAct)
								&& corteActivo == true) {
							corteActivo = false;
							corteVencido = false;
							cortesFormato.setFcrActivo(corteActivo);
							cortesFormato.setFcrVencido(corteVencido);
							corteActivo = true;
							System.out.println("***Corte Vigente");
						} else if (fechaUso.after(fechaAct)
								&& corteActivo == false) {
							corteActivo = true;
							corteVencido = false;
							cortesFormato.setFcrActivo(corteActivo);
							cortesFormato.setFcrVencido(corteVencido);
							System.out.println("***Corte Activo");
						}

						fechaCorte = formato.format(mapaFechas.get(Anual));

						cortesFormato.setFcrCorte(fechaCorte);
						Formatos formatoCorte = tomarObjetoListaSeleccionada(
								arrFormatos, formatoSeleccionado);
						//cortesFormato.setFormatos(formatoCorte);

						idRegistroCF = (Integer) serviceDao
								.getGenericCommonDao().create(
										CortesFormato.class, cortesFormato);

						System.out.println("***Exitoso Registro Corte Cod. : "
								+ idRegistroCF);

						cortesFormato = new CortesFormato();

					}

				}

				page = "listado";

				FacesUtils.addFacesMessageFromBundle(
						Constants.EXITO_OPERACIONES_ALERTAS,
						FacesMessage.SEVERITY_INFO);
			}

		} catch (Exception ex) {

			FacesUtils.addFacesMessageFromBundle(
					Constants.ERROR_OPERACIONES_ALERTAS,
					FacesMessage.SEVERITY_INFO,
					new String[] { ex.getMessage() });

		}

		return page;

	}

	/**
	 * 
	 * Metodo utilizado para llevar a cabo la eliminacion de los datos
	 * 
	 * @return {@link String}
	 */
	public String accionEliminar() {

		String page = null;

		try {

			List<Alertas> alertasCorte = serviceDao.getAlertasDao()
					.obtenerAlertasPorCorte(
							cortesFormatoSeleccionado.getFcrCodigo());

			if (alertasCorte != null && alertasCorte.size() > 0) {
				FacesUtils
						.addFacesMessage(
								"No es posible llevar a cabo esta operación ya que el corte seleccionado posee una o mas alertas asociadas",
								FacesMessage.SEVERITY_INFO);
				return page;
			}

			List<RegistroCargue> carguesCorte = serviceDao
					.getRegistroCargueDao().tomarRegistroCarguePorCorte(
							cortesFormatoSeleccionado.getFcrCodigo());

			if (carguesCorte != null && carguesCorte.size() > 0) {
				FacesUtils
						.addFacesMessage(
								"No es posible llevar a cabo esta operación ya que el corte seleccionado posee uno o mas cargues asociados",
								FacesMessage.SEVERITY_INFO);
				return page;
			}

			serviceDao.getCortesFormatoDao().delete(CortesFormato.class,
					cortesFormatoSeleccionado);
			page = "listado";

			FacesUtils.addFacesMessageFromBundle(
					Constants.EXITO_OPERACIONES_ALERTAS,
					FacesMessage.SEVERITY_INFO);

		} catch (Exception ex) {

			FacesUtils.addFacesMessageFromBundle(
					Constants.ERROR_OPERACIONES_ALERTAS,
					FacesMessage.SEVERITY_INFO,
					new String[] { ex.getMessage() });

		}

		return page;

	}

	/**
	 * 
	 * Metodo utilizado para llevar a cabo la actualizacion de los datos
	 * 
	 * @return {@link String}
	 */
	@SuppressWarnings("unchecked")
	public String accionActualizar() {

		String page = null;

		try {

			SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
			String fechaCorte = formato.format(fechaCorteFormato);
			Calendar fechaActual = Calendar.getInstance();
			fechaActual.set(Calendar.HOUR_OF_DAY, 0);
			fechaActual.set(Calendar.MINUTE, 0);
			fechaActual.set(Calendar.SECOND, 0);
			Date fechaAct = fechaActual.getTime();

			boolean corteActivo = false;
			boolean corteVencido = false;
			boolean corteActivoCorte;
			boolean corteVencidoCorte;

			CortesFormato corteFormatoUso = new CortesFormato();
			corteFormatoUso = (CortesFormato) serviceDao.getGenericCommonDao()
					.read(CortesFormato.class, idCorte);

			HashMap<Integer, Integer> paramsBusq = new HashMap<Integer, Integer>();
			//paramsBusq.put(0, corteFormatoUso.getFormatos().getForCodigo());
			List<CortesFormato> listaCortes = (List<CortesFormato>) serviceDao
					.getGenericCommonDao().executeFind(CortesFormato.class,
							paramsBusq,
							CortesFormato.NAMED_QUERY_GET_BY_FORMATO);

			Integer idCorteSeleccionado = corteFormatoUso.getFcrCodigo();

			for (CortesFormato cortesFormato : listaCortes) {
				Date fechaUso = formato.parse(cortesFormato.getFcrCorte());
				corteActivo = cortesFormato.getFcrActivo();
				corteVencido = cortesFormato.getFcrVencido();

				if (idCorteSeleccionado.equals(cortesFormato.getFcrCodigo())) {
					System.out.println("***Validando este Corte");

				} else if ((fechaCorteFormato.before(fechaAct) && fechaCorteFormato
						.before(fechaUso))
						|| fechaCorteFormato.before(fechaAct)) {
					corteActivoCorte = false;
					corteVencidoCorte = true;
					corteFormatoUso.setFcrActivo(corteActivoCorte);
					corteFormatoUso.setFcrVencido(corteVencidoCorte);
					System.out.println("***Corte Vencido");

				} else if ((fechaCorteFormato.after(fechaAct) && fechaCorteFormato
						.before(fechaUso)) && corteActivo == true) {
					corteActivoCorte = true;
					corteVencidoCorte = false;
					corteFormatoUso.setFcrActivo(corteActivoCorte);
					corteFormatoUso.setFcrVencido(corteVencidoCorte);
					System.out.println("***Corte Activo");

				} else if ((fechaCorteFormato.after(fechaAct) && fechaCorteFormato
						.after(fechaUso)) && corteActivo == true) {
					corteActivoCorte = false;
					corteVencidoCorte = false;
					corteFormatoUso.setFcrActivo(corteActivoCorte);
					corteFormatoUso.setFcrVencido(corteVencidoCorte);
					System.out.println("***Corte Vigente");

				} else if ((fechaCorteFormato.after(fechaAct) && fechaCorteFormato
						.after(fechaUso)) && corteActivo == false) {
					corteActivoCorte = false;
					corteVencidoCorte = false;
					corteFormatoUso.setFcrActivo(corteActivoCorte);
					corteFormatoUso.setFcrVencido(corteVencidoCorte);
					System.out.println("***Corte Vigente");
				}
			}

			corteFormatoUso.setFcrCorte(fechaCorte);
			Formatos formatoCorte = tomarObjetoListaSeleccionada(arrFormatos,
					formatoSeleccionado);
			//corteFormatoUso.setFormatos(formatoCorte);

			serviceDao.getCortesFormatoDao().update(CortesFormato.class,
					corteFormatoUso);

			// Actualizacion de vencimiento , activo y vigencia de otras Fechas

			List<CortesFormato> listaCortesActualizar = (List<CortesFormato>) serviceDao
					.getGenericCommonDao().executeFind(CortesFormato.class,
							paramsBusq,
							CortesFormato.NAMED_QUERY_GET_BY_FORMATO);

			corteActivo = false;
			corteVencido = false;
			Integer contActivos = 0;

			for (CortesFormato cortesFormato : listaCortesActualizar) {
				if (cortesFormato.getFcrActivo()) {
					contActivos = contActivos + 1;
				}
			}

			if (contActivos > 1) {
				contActivos = 0;
				for (CortesFormato cortesFormato : listaCortesActualizar) {
					CortesFormato corteFormatoActualizar = new CortesFormato();
					corteFormatoActualizar = cortesFormato;
					Date fechaCorteUso = formato.parse(cortesFormato
							.getFcrCorte());
					corteActivo = corteFormatoActualizar.getFcrActivo();
					if ((fechaCorteUso.after(fechaAct) && corteActivo == true)
							&& contActivos.equals(0)) {
						contActivos = contActivos + 1;
					} else if ((fechaCorteUso.after(fechaAct) && corteActivo == true)
							&& contActivos.equals(1)) {
						corteFormatoActualizar.setFcrActivo(false);
						corteFormatoActualizar.setFcrVencido(false);
						serviceDao.getCortesFormatoDao().update(
								CortesFormato.class, corteFormatoActualizar);
					}
				}
			} else if (contActivos.equals(0)) {
				for (CortesFormato cortesFormato : listaCortesActualizar) {
					CortesFormato corteFormatoActualizar = new CortesFormato();
					corteFormatoActualizar = cortesFormato;
					Date fechaCorteUso = formato.parse(cortesFormato
							.getFcrCorte());
					corteActivo = corteFormatoActualizar.getFcrActivo();
					if ((fechaCorteUso.after(fechaAct) && corteActivo == false)
							&& contActivos.equals(0)) {
						contActivos = contActivos + 1;
						corteFormatoActualizar.setFcrActivo(true);
						corteFormatoActualizar.setFcrVencido(false);
						serviceDao.getCortesFormatoDao().update(
								CortesFormato.class, corteFormatoActualizar);
					}
				}
			}

			page = "listado";
			FacesUtils.addFacesMessageFromBundle(
					Constants.EXITO_OPERACIONES_ALERTAS,
					FacesMessage.SEVERITY_INFO);

		} catch (Exception ex) {

			FacesUtils.addFacesMessageFromBundle(
					Constants.ERROR_OPERACIONES_ALERTAS,
					FacesMessage.SEVERITY_INFO,
					new String[] { ex.getMessage() });
			ex.printStackTrace();

		}

		return page;

	}

	/**
	 * Accion navegar hacia la pagina de inicio del modulo
	 * 
	 * @return
	 */
	public String accionCancelar() {

		return "listado";
	}

	/**
	 * 
	 * Accion Navegar hacia la pagina de creacion de cortes
	 * 
	 * @return
	 */
	public String navAdicionar() {

		return "navAdicionar";
	}

	/**
	 * 
	 * Accion Navegar hacia la pagina de modificacion de cortes
	 * 
	 * @return
	 */
	public String navModificar() {

		return "navModificar";
	}

	/**
	 * 
	 * Metodo utilizado para tomar el objeto seleccionado de una lista
	 * desplegable
	 * 
	 * 
	 * @param lista
	 * @param seleccionado
	 * @return <T>
	 */
	@SuppressWarnings("unchecked")
	private <T> T tomarObjetoListaSeleccionada(List<T> lista,
			Integer seleccionado) {

		for (Object obj : lista) {

			if (obj instanceof Formatos) {

				Formatos formatoSeleccionado = (Formatos) obj;

				if (seleccionado.compareTo(formatoSeleccionado.getForCodigo()) == 0) {

					return (T) formatoSeleccionado;
				}
			}
		}

		return null;
	}

	/**
	 * Metodo utilizado para cargar los datos del registro a modificar
	 * 
	 */
	public void cargarDatosModificar() {

		FacesContext ctx = FacesContext.getCurrentInstance();

		try {

			if (idCorte == null) {

				String idCorteFormato = ctx.getExternalContext()
						.getRequestParameterMap().get("idCorte");
				if (idCorteFormato != null)
					this.idCorte = Integer.parseInt(idCorteFormato);
			}

			if ((cortesFormatoSeleccionado == null)
					&& (idCorte != null && !idCorte.equals(""))) {
				cortesFormatoSeleccionado = (CortesFormato) serviceDao
						.getGenericCommonDao().read(CortesFormato.class,
								idCorte);

				if (cortesFormatoSeleccionado != null) {

//					formatoSeleccionado = cortesFormatoSeleccionado
//							.getFormatos().getForCodigo();
					SimpleDateFormat formato = new SimpleDateFormat(
							"yyyy-MM-dd");

					fechaCorteFormato = formato.parse(cortesFormatoSeleccionado
							.getFcrCorte());

					cargarFormatos();
				}

			} else {
				cortesFormatoSeleccionado = new CortesFormato();
			}

		} catch (Exception e) {

			ctx.addMessage(null, new FacesMessage(
					"Se ha presentado un error. Detalles: " + e.getMessage()));
		}

	}

	public void calcularVigencia(ValueChangeEvent e) {

		if (null != e.getNewValue()) {

			fechaCorteFormato = (Date) e.getNewValue();
			vigente = (fechaCorteFormato.compareTo(fechaActual) >= 0);
		}
	}

	/**
	 * @return the formatoSeleccionado
	 */
	public Integer getFormatoSeleccionado() {
		return this.formatoSeleccionado;
	}

	/**
	 * @param formatoSeleccionado
	 *            the formatoSeleccionado to set
	 */
	public void setFormatoSeleccionado(Integer formatoSeleccionado) {
		this.formatoSeleccionado = formatoSeleccionado;
	}

	/**
	 * @return the listaFormatos
	 */
	public List<SelectItem> getListaFormatos() {
		return this.listaFormatos;
	}

	/**
	 * @param listaFormatos
	 *            the listaFormatos to set
	 */
	public void setListaFormatos(List<SelectItem> listaFormatos) {
		this.listaFormatos = listaFormatos;
	}

	/**
	 * @return the cortesFormato
	 */
	public List<CortesFormato> getCortesFormato() {
		return this.cortesFormato;
	}

	/**
	 * @param cortesFormato
	 *            the cortesFormato to set
	 */
	public void setCortesFormato(List<CortesFormato> cortesFormato) {
		this.cortesFormato = cortesFormato;
	}

	/**
	 * @return the serviceDao
	 */
	public ServiceDao getServiceDao() {
		return this.serviceDao;
	}

	/**
	 * @param serviceDao
	 *            the serviceDao to set
	 */
	public void setServiceDao(ServiceDao serviceDao) {
		this.serviceDao = serviceDao;
	}

	/**
	 * @return the arrFormatos
	 */
	public List<Formatos> getArrFormatos() {
		return this.arrFormatos;
	}

	/**
	 * @param arrFormatos
	 *            the arrFormatos to set
	 */
	public void setArrFormatos(List<Formatos> arrFormatos) {
		this.arrFormatos = arrFormatos;
	}

	/**
	 * @return the fechaCorteFormato
	 */
	public Date getFechaCorteFormato() {
		return this.fechaCorteFormato;
	}

	/**
	 * @param fechaCorteFormato
	 *            the fechaCorteFormato to set
	 */
	public void setFechaCorteFormato(Date fechaCorteFormato) {
		this.fechaCorteFormato = fechaCorteFormato;
	}

	/**
	 * @return the activo
	 */
	public boolean isActivo() {
		return this.activo;
	}

	/**
	 * @param activo
	 *            the activo to set
	 */
	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	/**
	 * @return the cortesFormatoSeleccionado
	 */
	public CortesFormato getCortesFormatoSeleccionado() {
		return this.cortesFormatoSeleccionado;
	}

	/**
	 * @param cortesFormatoSeleccionado
	 *            the cortesFormatoSeleccionado to set
	 */
	public void setCortesFormatoSeleccionado(
			CortesFormato cortesFormatoSeleccionado) {
		this.cortesFormatoSeleccionado = cortesFormatoSeleccionado;
	}

	/**
	 * @return the idCorte
	 */
	public Integer getIdCorte() {
		return this.idCorte;
	}

	/**
	 * @param idCorte
	 *            the idCorte to set
	 */
	public void setIdCorte(Integer idCorte) {
		this.idCorte = idCorte;
	}

	/**
	 * @return the fechaActual
	 */
	public Date getFechaActual() {
		return fechaActual;
	}

	/**
	 * @param fechaActual
	 *            the fechaActual to set
	 */
	public void setFechaActual(Date fechaActual) {
		this.fechaActual = fechaActual;
	}

	/**
	 * @return the listaParametros
	 */
	public List<SelectItem> getListaParametros() {
		return listaParametros;
	}

	/**
	 * @param listaParametros
	 *            the listaParametros to set
	 */
	public void setListaParametros(List<SelectItem> listaParametros) {
		this.listaParametros = listaParametros;
	}

	/**
	 * @return the idParametroSeleccionado
	 */
	public Integer getIdParametroSeleccionado() {
		return idParametroSeleccionado;
	}

	/**
	 * @param idParametroSeleccionado
	 *            the idParametroSeleccionado to set
	 */
	public void setIdParametroSeleccionado(Integer idParametroSeleccionado) {
		this.idParametroSeleccionado = idParametroSeleccionado;
	}

	/**
	 * @return the listaParamsSGC
	 */
	public List<ParametrosSGC> getListaParamsSGC() {
		return listaParamsSGC;
	}

	/**
	 * @param listaParamsSGC
	 *            the listaParamsSGC to set
	 */
	public void setListaParamsSGC(List<ParametrosSGC> listaParamsSGC) {
		this.listaParamsSGC = listaParamsSGC;
	}

}
