package la.netco.commons.services;

import java.io.InputStream;
import java.util.Date;

import la.netco.persistencia.dto.commons.ArchivoEntrada;
import la.netco.persistencia.dto.commons.ArchivoExpediente;
import la.netco.persistencia.dto.commons.ArchivoRegistro;
import la.netco.persistencia.dto.commons.ArchivoSalida;
import la.netco.sgc.persistence.dto.ArchivoAuditorias;
import la.netco.sgc.persistence.dto.ArchivoSGC;

public interface FileSystemRepositoryService {
	public void guardarArchivo(InputStream archivoInputStream, ArchivoEntrada archivoEntrada) throws Exception;
	
	public void guardarArchivo(InputStream archivoInputStream, ArchivoAuditorias archivoAuditorias) throws Exception;

	public void guardarArchivo(InputStream archivoInputStream, ArchivoExpediente archivoExpediente) throws Exception;

	public void guardarArchivo(InputStream archivoInputStream, ArchivoRegistro archivoRegistro) throws Exception;

	public void guardarArchivo(InputStream archivoInputStream, ArchivoSalida archivoSalida) throws Exception;

	public void guardarArchivo(InputStream archivoInputStream, ArchivoSGC archivoSGC) throws Exception;

	public InputStream obtenerInputStream(ArchivoExpediente archivoExpediete) throws Exception;

	public InputStream obtenerInputStream(ArchivoEntrada archivoEntrada) throws Exception;
	
	public InputStream obtenerInputStream(ArchivoAuditorias archivoAuditorias) throws Exception;

	public InputStream obtenerInputStream(ArchivoRegistro archivoRegistro) throws Exception;

	public InputStream obtenerInputStream(ArchivoSalida archivoSalida) throws Exception;

	public InputStream obtenerInputStream(ArchivoSGC archivoSGC) throws Exception;

	public void moverCarpeta(Date fechaAct, Date nuevaFecha, Integer idFolder, String tipoCorrespondencia) throws Exception;

}
