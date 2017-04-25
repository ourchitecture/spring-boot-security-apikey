package ourchitecture.boot.apikey;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ApiKeyFilter implements Filter {

	private final String API_KEY_HEADER_NAME = "X-API-KEY";

	private final ApiKeyValidator validator;

    public ApiKeyFilter(final ApiKeyValidator validator) {
    	this.validator = validator;
    }

	@Override
	public void init(final FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(
			final ServletRequest request, 
			final ServletResponse response, 
			final FilterChain chain)
			throws IOException, ServletException {
        
		if (!this.validator.isEnabled()) {
			chain.doFilter(request, response);
			return;
		}

		final String apiKey = ((HttpServletRequest)request).getHeader(API_KEY_HEADER_NAME);
		
		String apiKeyError = this.validator.validateRequestApiKey(apiKey);
		
		if (apiKeyError == null) {
			chain.doFilter(request, response);
			return;
		}
		
		HttpServletResponse httpResponse = (HttpServletResponse)response;
		
		httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, apiKeyError);
	}

	@Override
	public void destroy() {
	}
}
