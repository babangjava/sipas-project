package org.demo.web.utils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AccessDeniedHandler extends AccessDeniedHandlerImpl {

	@Override
	public void handle(HttpServletRequest _request, HttpServletResponse _response, AccessDeniedException _exception)
			throws IOException, ServletException {
		setErrorPage("/accessDenied");
		super.handle(_request, _response, _exception);
	}

}
