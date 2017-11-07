package la.netco.core.spring.security;

import org.springframework.security.core.AuthenticationException;
public class MaxUsersSessionsException extends AuthenticationException{

	private static final long serialVersionUID = 8788254432388569405L;

	public MaxUsersSessionsException(String msg) {
		super(msg);
	}

}
