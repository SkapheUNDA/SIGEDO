package la.netco.commons.services.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.Resource;

import la.netco.commons.services.FileSystemRepositoryService;
import la.netco.core.persitencia.dao.commos.GenericCommonDao;
import la.netco.persistencia.dto.commons.ArchivoEntrada;
import la.netco.persistencia.dto.commons.ArchivoExpediente;
import la.netco.persistencia.dto.commons.ArchivoRegistro;
import la.netco.persistencia.dto.commons.ArchivoSalida;
import la.netco.persistencia.dto.commons.ConstantsKeysFire;
import la.netco.sgc.persistence.dto.ArchivoAuditorias;
import la.netco.sgc.persistence.dto.ArchivoSGC;

import org.apache.tika.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service("fileSystemRepositoryService")
public class FileSystemRepositoryServiceImpl implements FileSystemRepositoryService {

	@Value("${archivos.rutaentradas}")
	private String rutaRootRepository;

	@Value("${archivos.rutaexpedientes}")
	private String rutaExpRepository;

	@Value("${archivos.rutaregistros}")
	private String rutaRegRepository;

	@Value("${archivos.rutasalidas}")
	private String rutaRootSalidasRepository;

	@Value("${archivos.rutasgc}")
	private String rutaSGCRepository;

	@Autowired
	@Resource(name = "genericCommonDao")
	private GenericCommonDao genericCommonDao;

	public void guardarArchivo(InputStream archivoInputStream, ArchivoEntrada archivoEntrada) throws Exception {
		System.out.println(" rutaRootRepository" + rutaRootRepository);
		Calendar fechaArchivo = Calendar.getInstance();
		fechaArchivo.setTime(archivoEntrada.getEntrada().getEntFen());
		int year = fechaArchivo.get(Calendar.YEAR);
		int month = (fechaArchivo.get(Calendar.MONTH) + 1);
		int day = fechaArchivo.get(Calendar.DAY_OF_MONTH);

		File folderRoot = new File(rutaRootRepository + File.separator + year + File.separator + month + File.separator + day + File.separator
				+ archivoEntrada.getEntrada().getEntId());
		if (!folderRoot.exists()) {
			folderRoot.mkdirs();
		}

		File archivo = new File(folderRoot.getAbsolutePath() + File.separator + archivoEntrada.getNombre());
		OutputStream output = new FileOutputStream(archivo);
		IOUtils.copy(archivoInputStream, output);
		output.close();

		genericCommonDao.create(ArchivoEntrada.class, archivoEntrada);

	}

	public InputStream obtenerInputStream(ArchivoEntrada archivoEntrada) throws Exception {
		System.out.println(" rutaRootRepository" + rutaRootRepository);
		Calendar fechaArchivo = Calendar.getInstance();
		fechaArchivo.setTime(archivoEntrada.getEntrada().getEntFen());
		int year = fechaArchivo.get(Calendar.YEAR);
		int month = (fechaArchivo.get(Calendar.MONTH) + 1);
		int day = fechaArchivo.get(Calendar.DAY_OF_MONTH);

		File archivo = new File(rutaRootRepository + File.separator + year + File.separator + month + File.separator + day + File.separator
				+ archivoEntrada.getEntrada().getEntId() + File.separator + archivoEntrada.getNombre());
		InputStream inputStream = new FileInputStream(archivo);
		return inputStream;
	}
	
	
	public void guardarArchivo (InputStream archivoInputStream, ArchivoAuditorias archivoAuditorias) throws Exception {
		System.out.println(" rutaRootRepository" + rutaRootRepository);
		Calendar fechaArchivo = Calendar.getInstance();
		fechaArchivo.setTime(archivoAuditorias.getAauAuditoria().getAudFechaEnvioOficio());
		int year = fechaArchivo.get(Calendar.YEAR);
		int month = (fechaArchivo.get(Calendar.MONTH) + 1);
		int day = fechaArchivo.get(Calendar.DAY_OF_MONTH);

		File folderRoot = new File(rutaRootRepository + File.separator + year + File.separator + month + File.separator + day + File.separator
				+ archivoAuditorias.getAauAuditoria().getAudId());
		if (!folderRoot.exists()) {
			folderRoot.mkdirs();
		}

		File archivo = new File(folderRoot.getAbsolutePath() + File.separator + archivoAuditorias.getAauNombre());
		OutputStream output = new FileOutputStream(archivo);
		IOUtils.copy(archivoInputStream, output);
		output.close();

		genericCommonDao.create(ArchivoAuditorias.class, archivoAuditorias);

	}
	
	public InputStream obtenerInputStream(ArchivoAuditorias archivoAuditorias) throws Exception {
		System.out.println(" rutaRootRepository" + rutaRootRepository);
		Calendar fechaArchivo = Calendar.getInstance();
		fechaArchivo.setTime(archivoAuditorias.getAauAuditoria().getAudFechaEnvioOficio());
		int year = fechaArchivo.get(Calendar.YEAR);
		int month = (fechaArchivo.get(Calendar.MONTH) + 1);
		int day = fechaArchivo.get(Calendar.DAY_OF_MONTH);

		File archivo = new File(rutaRootRepository + File.separator + year + File.separator + month + File.separator + day + File.separator
				+ archivoAuditorias.getAauAuditoria().getAudId() + File.separator + archivoAuditorias.getAauNombre());
		InputStream inputStream = new FileInputStream(archivo);
		return inputStream;
	}
	

	public void moverCarpeta(Date fechaAct, Date nuevaFecha, Integer idFolder, String tipoCorrespondencia) throws Exception {
		String rutaRootRepository = null;
		if (tipoCorrespondencia.equals(ConstantsKeysFire.ENTRADA)) {
			rutaRootRepository = this.rutaRootRepository;
		} else if (tipoCorrespondencia.equals(ConstantsKeysFire.SALIDA)) {
			rutaRootRepository = this.rutaRootSalidasRepository;
		}
		if (rutaRootRepository != null) {
			Calendar fechaArchivo = Calendar.getInstance();
			fechaArchivo.setTime(fechaAct);
			int year = fechaArchivo.get(Calendar.YEAR);
			int month = (fechaArchivo.get(Calendar.MONTH) + 1);
			int day = fechaArchivo.get(Calendar.DAY_OF_MONTH);

			File folderRoot = new File(rutaRootRepository + File.separator + year + File.separator + month + File.separator + day + File.separator + idFolder);
			if (folderRoot.exists()) {
				Calendar fechaNuevaArchivo = Calendar.getInstance();
				fechaNuevaArchivo.setTime(nuevaFecha);
				int yearN = fechaNuevaArchivo.get(Calendar.YEAR);
				int monthN = (fechaNuevaArchivo.get(Calendar.MONTH) + 1);
				int dayN = fechaNuevaArchivo.get(Calendar.DAY_OF_MONTH);

				File newFolderDay = new File(rutaRootRepository + File.separator + yearN + File.separator + monthN + File.separator + dayN);
				if (!newFolderDay.exists()) {
					folderRoot.mkdirs();
				}

				File newFolder = new File(rutaRootRepository + File.separator + yearN + File.separator + monthN + File.separator + dayN + File.separator + idFolder);

				Files.move(folderRoot.toPath(), newFolder.toPath(), StandardCopyOption.REPLACE_EXISTING);
			}
		}

	}

	public void guardarArchivo(InputStream archivoInputStream, ArchivoSalida archivoSalida) throws Exception {
		System.out.println(" rutaRootSalidasRepository" + rutaRootSalidasRepository);
		Calendar fechaArchivo = Calendar.getInstance();
		fechaArchivo.setTime(archivoSalida.getSalida().getSalFsa());
		int year = fechaArchivo.get(Calendar.YEAR);
		int month = (fechaArchivo.get(Calendar.MONTH) + 1);
		int day = fechaArchivo.get(Calendar.DAY_OF_MONTH);

		File folderRoot = new File(rutaRootSalidasRepository + File.separator + year + File.separator + month + File.separator + day + File.separator
				+ archivoSalida.getSalida().getSalId());
		if (!folderRoot.exists()) {
			folderRoot.mkdirs();
		}

		File archivo = new File(folderRoot.getAbsolutePath() + File.separator + archivoSalida.getNombre());
		OutputStream output = new FileOutputStream(archivo);
		IOUtils.copy(archivoInputStream, output);
		output.close();

		genericCommonDao.create(ArchivoSalida.class, archivoSalida);

	}

	public InputStream obtenerInputStream(ArchivoSalida archivoSalida) throws Exception {
		System.out.println(" rutaRootSalidasRepository" + rutaRootSalidasRepository);
		Calendar fechaArchivo = Calendar.getInstance();
		fechaArchivo.setTime(archivoSalida.getSalida().getSalFsa());
		int year = fechaArchivo.get(Calendar.YEAR);
		int month = (fechaArchivo.get(Calendar.MONTH) + 1);
		int day = fechaArchivo.get(Calendar.DAY_OF_MONTH);

		File archivo = new File(rutaRootSalidasRepository + File.separator + year + File.separator + month + File.separator + day + File.separator
				+ archivoSalida.getSalida().getSalId() + File.separator + archivoSalida.getNombre());
		InputStream inputStream = new FileInputStream(archivo);
		return inputStream;
	}

	// ***Region para Guardar Archivos de Expedientes

	public void guardarArchivo(InputStream archivoInputStream, ArchivoExpediente archivoExpediente) throws Exception {
		System.out.println(" rutaExpRepository" + rutaExpRepository);
		Calendar fechaArchivo = Calendar.getInstance();
		fechaArchivo.setTime(archivoExpediente.getExpediente().getExpFso());
		int year = fechaArchivo.get(Calendar.YEAR);
		int month = (fechaArchivo.get(Calendar.MONTH) + 1);
		int day = fechaArchivo.get(Calendar.DAY_OF_MONTH);

		File folderRoot = new File(rutaExpRepository + File.separator + year + File.separator + month + File.separator + day + File.separator
				+ archivoExpediente.getExpediente().getExpId());
		if (!folderRoot.exists()) {
			folderRoot.mkdirs();
		}

		File archivo = new File(folderRoot.getAbsolutePath() + File.separator + archivoExpediente.getNombre());
		OutputStream output = new FileOutputStream(archivo);
		IOUtils.copy(archivoInputStream, output);
		output.close();

		genericCommonDao.create(ArchivoEntrada.class, archivoExpediente);

	}

	public InputStream obtenerInputStream(ArchivoExpediente archivoExpediente) throws Exception {
		System.out.println(" rutaExpRepository" + rutaExpRepository);
		Calendar fechaArchivo = Calendar.getInstance();
		fechaArchivo.setTime(archivoExpediente.getExpediente().getExpFso());
		int year = fechaArchivo.get(Calendar.YEAR);
		int month = (fechaArchivo.get(Calendar.MONTH) + 1);
		int day = fechaArchivo.get(Calendar.DAY_OF_MONTH);

		File archivo = new File(rutaExpRepository + File.separator + year + File.separator + month + File.separator + day + File.separator
				+ archivoExpediente.getExpediente().getExpId() + File.separator + archivoExpediente.getNombre());
		InputStream inputStream = new FileInputStream(archivo);
		return inputStream;
	}

	// *****

	// ****Region para Guardar archivos de Registro

	public void guardarArchivo(InputStream archivoInputStream, ArchivoRegistro archivoRegistro) throws Exception {
		System.out.println(" rutaRegRepository" + rutaRegRepository);
		Calendar fechaArchivo = Calendar.getInstance();
		fechaArchivo.setTime(archivoRegistro.getRegistro().getRegFecReal());
		int year = fechaArchivo.get(Calendar.YEAR);
		int month = (fechaArchivo.get(Calendar.MONTH) + 1);
		String monthAlfa = convertirAnioOmes(month);
		int day = fechaArchivo.get(Calendar.DAY_OF_MONTH);
		String dayAlfa = convertirAnioOmes(day);

		File folderRoot = new File(rutaRegRepository + File.separator + year + File.separator + monthAlfa.trim() + File.separator + dayAlfa.trim() + File.separator
				+ archivoRegistro.getRegistro().getRegId());
		if (!folderRoot.exists()) {
			folderRoot.mkdirs();
		}

		File archivo = new File(folderRoot.getAbsolutePath() + File.separator + archivoRegistro.getNombre());
		OutputStream output = new FileOutputStream(archivo);
		IOUtils.copy(archivoInputStream, output);
		output.close();

		genericCommonDao.create(ArchivoRegistro.class, archivoRegistro);

	}

	public InputStream obtenerInputStream(ArchivoRegistro archivoRegistro) throws Exception {
		System.out.println(" rutaRegRepository" + rutaRegRepository);
		Calendar fechaArchivo = Calendar.getInstance();
		fechaArchivo.setTime(archivoRegistro.getRegistro().getRegFecReal());
		int year = fechaArchivo.get(Calendar.YEAR);
		int month = (fechaArchivo.get(Calendar.MONTH) + 1);
		String monthAlfa = convertirAnioOmes(month);
		int day = fechaArchivo.get(Calendar.DAY_OF_MONTH);
		String dayAlfa = convertirAnioOmes(day);

		File archivo = new File(rutaRegRepository + File.separator + year + File.separator + monthAlfa.trim() + File.separator + dayAlfa.trim() + File.separator
				+ archivoRegistro.getRegistro().getRegId() + File.separator + archivoRegistro.getNombre());
		InputStream inputStream = new FileInputStream(archivo);
		return inputStream;
	}

	// *****

	// ****Region para Guardar archivos de SGC

	public void guardarArchivo(InputStream archivoInputStream, ArchivoSGC archivoSGC) throws Exception {
		System.out.println(" rutaSGCRepository " + rutaSGCRepository);		

		File folderRoot = new File(rutaSGCRepository + File.separator + archivoSGC.getEntidades().getEntObjetoSocial() + File.separator + archivoSGC.getFormatos().getForNombre()
				+ File.separator + archivoSGC.getPeriodo()+" "+archivoSGC.getAno());
		if (!folderRoot.exists()) {
			folderRoot.mkdirs();
		}

		File archivo = new File(folderRoot.getAbsolutePath() + File.separator + archivoSGC.getNombre());
		OutputStream output = new FileOutputStream(archivo);
		IOUtils.copy(archivoInputStream, output);
		output.close();

		genericCommonDao.create(ArchivoSGC.class, archivoSGC);
	}

	public InputStream obtenerInputStream(ArchivoSGC archivoSGC) throws Exception {
		System.out.println(" rutaSGCRepository " + rutaSGCRepository);		

		File archivo = new File(rutaSGCRepository + File.separator + archivoSGC.getEntidades().getEntObjetoSocial() + File.separator + archivoSGC.getFormatos().getForNombre()
				+ File.separator + archivoSGC.getPeriodo()+" "+archivoSGC.getAno() + File.separator + archivoSGC.getNombre());
		InputStream inputStream = new FileInputStream(archivo);
		return inputStream;
	}

	// *****
	
	public String convertirAnioOmes(int convetir){
		String valueConvertido = String.valueOf(convetir);
		switch (convetir) {
		case 1:
			valueConvertido = "01";
			break;
		case 2:
			valueConvertido = "02";
			break;
		case 3:
			valueConvertido = "03";
			break;
		case 4:
			valueConvertido = "04";
			break;
		case 5:
			valueConvertido = "05";
			break;
		case 6:
			valueConvertido = "06";
			break;
		case 7:
			valueConvertido = "07";
			break;
		case 8:
			valueConvertido = "08";
			break;
		case 9:
			valueConvertido = "09";
			break;

		default:
			break;
		}
		
		return valueConvertido;
	}

}
