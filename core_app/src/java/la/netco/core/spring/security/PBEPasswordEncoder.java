package la.netco.core.spring.security;

import org.jasypt.encryption.pbe.PBEStringEncryptor;
import org.jasypt.util.text.BasicTextEncryptor;
import org.jasypt.util.text.TextEncryptor;
import org.springframework.dao.DataAccessException;

public class PBEPasswordEncoder implements org.springframework.security.authentication.encoding.PasswordEncoder {

	private TextEncryptor textEncryptor;
	private PBEStringEncryptor pbeStringEncryptor;
	private Boolean useTextEncryptor;

	public PBEPasswordEncoder() {
		textEncryptor = null;
		pbeStringEncryptor = null;
		useTextEncryptor = null;
	}

	public String encodePassword(String rawPass, Object salt)
			throws DataAccessException {
		try {
			checkInitialization();
		} catch (Exception e) {

			e.printStackTrace();
		}
		if (useTextEncryptor.booleanValue())
			return textEncryptor.encrypt(rawPass);
		else
			return pbeStringEncryptor.encrypt(rawPass);
		// return null;
	}

	public boolean isPasswordValid(String encPass, String rawPass, Object salt)
			throws DataAccessException {
		try {
			checkInitialization();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String decPassword = null;
		if (useTextEncryptor.booleanValue())
			decPassword = textEncryptor.decrypt(encPass);
		else
			decPassword = pbeStringEncryptor.decrypt(encPass);
		if (decPassword == null || rawPass == null)
			return decPassword == rawPass;
		else
			return decPassword.equals(rawPass);
		// return false;
	}

	private synchronized void checkInitialization() throws Exception {
		if (useTextEncryptor == null) {
			textEncryptor = new BasicTextEncryptor();
			useTextEncryptor = Boolean.TRUE;
		} else if (useTextEncryptor.booleanValue()) {
			if (textEncryptor == null) {
				throw new Exception(
						"PBE Password encoder not initialized: text encryptor is null");
			}
		} else if (pbeStringEncryptor == null) {
			throw new Exception(
					"PBE Password encoder not initialized: PBE string encryptor is null");
		}
	}

	public void setTextEncryptor(TextEncryptor textEncryptor) {
		this.textEncryptor = textEncryptor;
		useTextEncryptor = Boolean.TRUE;
	}

	public void setPbeStringEncryptor(PBEStringEncryptor pbeStringEncryptor) {
		this.pbeStringEncryptor = pbeStringEncryptor;
		useTextEncryptor = Boolean.FALSE;
	}

}
