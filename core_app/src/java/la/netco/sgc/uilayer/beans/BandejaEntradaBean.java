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
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

import la.netco.core.businesslogic.services.ServiceDao;
import la.netco.core.spring.security.UserDetailsUtils;
import la.netco.sgc.persistence.dto.CortesFormato;
import la.netco.sgc.persistence.dto.Entidades;
import la.netco.sgc.persistence.dto.Formatos;
import la.netco.sgc.persistence.dto.RegistroCargueFormato;

/**
 * @author cguzman
 * 
 */
/**
 * @author cpineros
 * 
 */
@ManagedBean(name = "bandejaEntradaBean")
@RequestScoped
public class BandejaEntradaBean implements Serializable {

	private static final long serialVersionUID = 1L;

	public class BandejaEntradaModel {

		private String entidad;

		private String formato;

		private String corte;

		private boolean alDia;

		/**
		 * 
		 * @return the entidad
		 */
		public String getEntidad() {
			return entidad;
		}

		/**
		 * @param entidad
		 *            the entidad to set
		 */
		public void setEntidad(String entidad) {
			this.entidad = entidad;
		}

		/**
		 * @return the formato
		 */
		public String getFormato() {
			return formato;
		}

		/**
		 * @param formato
		 *            the formato to set
		 */
		public void setFormato(String formato) {
			this.formato = formato;
		}

		/**
		 * @return the corte
		 */
		public String getCorte() {
			return corte;
		}

		/**
		 * @param corte
		 *            the corte to set
		 */
		public void setCorte(String corte) {
			this.corte = corte;
		}

		/**
		 * @return the alDia
		 */
		public boolean isAlDia() {
			return alDia;
		}

		/**
		 * @param alDia
		 *            the alDia to set
		 */
		public void setAlDia(boolean alDia) {
			this.alDia = alDia;
		}

	}

	private List<BandejaEntradaModel> listaBandejaEntrada;

	@ManagedProperty(value = "#{serviceDao}")
	transient private ServiceDao serviceDao;

	public BandejaEntradaBean() {
	}

	@SuppressWarnings({ "unused", "unchecked" })
	@PostConstruct
	private void _init() {

		try {
			actualizaCortes();
			Set<Entidades> entidadesUsuario = UserDetailsUtils.usuarioLogged()
					.getEntidades();
			List<Entidades> entidades = new ArrayList<Entidades>(
					entidadesUsuario);

			List<Formatos> formatos = (List<Formatos>) serviceDao
					.getGenericCommonDao().loadAll(Formatos.class);

			Calendar calendarioActual = Calendar.getInstance();
			calendarioActual.setTimeInMillis(System.currentTimeMillis());
			Date fechaActual = calendarioActual.getTime();

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

			listaBandejaEntrada = new ArrayList<BandejaEntradaModel>();

			for (Entidades e : entidades) {

				for (Formatos f : formatos) {

					CortesFormato corte = serviceDao.getCortesFormatoDao()
							.obtenerCortesFormatoActivoPorFormato(
									);
					boolean alDia = false;

					if (corte != null) {

						Date fechaCorte = dateFormat.parse(corte.getFcrCorte());

						Integer idEntidad = e.getEntCodigo();
						Integer idFormato = f.getForCodigo();
						Integer idCorte = corte.getFcrCodigo();

						RegistroCargueFormato cargue = serviceDao
								.getRegistroCargueFormatoDao()
								.tomarRegistroCargueFormatoPorCargue(idFormato,
										idEntidad, idCorte);

						if (cargue == null) {

							if (fechaActual.compareTo(fechaCorte) <= 0)
								alDia = true;

						} else {

							alDia = true;
						}

						BandejaEntradaModel bandejaEntradaModel = new BandejaEntradaModel();
						bandejaEntradaModel.setAlDia(alDia);
						bandejaEntradaModel.setCorte(corte.getFcrCorte());
						bandejaEntradaModel.setEntidad(e.getEntObjetoSocial());
						bandejaEntradaModel.setFormato(f.getForNombre());

						listaBandejaEntrada.add(bandejaEntradaModel);
					}
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

	@SuppressWarnings("unchecked")
	private void actualizaCortes() throws ParseException {
//		System.out.println("***Actualizando fechas de corte");
//
//		List<Formatos> listaFormatos = (List<Formatos>) serviceDao
//				.getGenericCommonDao().loadAll(Formatos.class);
//		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
//		Calendar fechaActual = Calendar.getInstance();
//		fechaActual.set(Calendar.HOUR_OF_DAY, 0);
//		fechaActual.set(Calendar.MINUTE, 0);
//		fechaActual.set(Calendar.SECOND, 0);
//		Date fechaAct = fechaActual.getTime();
//
//		for (Formatos formatos : listaFormatos) {
//			HashMap<Integer, Integer> params = new HashMap<Integer, Integer>();
//			Integer formatoUso = formatos.getForCodigo();
//			params.put(0, formatoUso);
//			boolean corteActivo = false;
//			boolean corteVencido = false;
//			List<CortesFormato> listaCortesFormato = (List<CortesFormato>) serviceDao
//					.getGenericCommonDao().executeFind(CortesFormato.class,
//							params, CortesFormato.NAMED_QUERY_GET_BY_FORMATO);
//			for (CortesFormato cortesFormato : listaCortesFormato) {
//				Date fechaUso = formato.parse(cortesFormato.getFcrCorte());
//
//				if (fechaUso.before(fechaAct)) {
//					corteActivo = false;
//					corteVencido = true;
//					cortesFormato.setFcrActivo(corteActivo);
//					cortesFormato.setFcrVencido(corteVencido);
//					System.out.println("***Corte Vencido");
//				} else if (fechaUso.after(fechaAct) && corteActivo == true) {
//					corteActivo = false;
//					corteVencido = false;
//					cortesFormato.setFcrActivo(corteActivo);
//					cortesFormato.setFcrVencido(corteVencido);
//					corteActivo = true;
//					System.out.println("***Corte Vigente");
//				} else if (fechaUso.after(fechaAct) && corteActivo == false) {
//					corteActivo = true;
//					corteVencido = false;
//					cortesFormato.setFcrActivo(corteActivo);
//					cortesFormato.setFcrVencido(corteVencido);
//					System.out.println("***Corte Activo");
//				}
//
//				serviceDao.getGenericCommonDao().update(CortesFormato.class,
//						cortesFormato);
//
//			}
//
//		}

	}

	/**
	 * @return the listaBandejaEntrada
	 */
	public List<BandejaEntradaModel> getListaBandejaEntrada() {
		return listaBandejaEntrada;
	}

	/**
	 * @param listaBandejaEntrada
	 *            the listaBandejaEntrada to set
	 */
	public void setListaBandejaEntrada(
			List<BandejaEntradaModel> listaBandejaEntrada) {
		this.listaBandejaEntrada = listaBandejaEntrada;
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

}
