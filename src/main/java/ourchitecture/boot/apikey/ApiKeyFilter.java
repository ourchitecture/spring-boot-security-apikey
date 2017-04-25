package ourchitecture.boot.apikey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	private static Logger log = LoggerFactory.getLogger(ApiKeyFilter.class);
	
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
        
		log.debug('[ApiKeyFilter] doFilter()');

		if (!this.validator.isEnabled()) {
			log.debug('[ApiKeyFilter] API Key is disabled');
			chain.doFilter(request, response);
			return;
		}

		final String apiKey = ((HttpServletRequest)request).getHeader(API_KEY_HEADER_NAME);
		
		String apiKeyError = this.validator.validateRequestApiKey(apiKey);
		
		if (apiKeyError == null) {
			log.debug('[ApiKeyFilter] API Key is valid');
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
