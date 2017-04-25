package ourchitecture.boot.apikey;

import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ApiKeyProperties.class)
public class ApiKeyAutoConfiguration {

	@Autowired
	private ApiKeyProperties properties;

	@Bean(name = "apiKeyFilter")
	public Filter apiKeyFilter() {
		return new ApiKeyFilter(getApiKeyValidator());
	}
	
	@Bean(name = "apiKeyValidator")
	public ApiKeyValidator getApiKeyValidator() {
		return new ApiKeyValidator(this.properties);
	}
}
