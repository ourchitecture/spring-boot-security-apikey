package ourchitecture.boot.apikey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.github.nitram509.jmacaroons.Macaroon;
import com.github.nitram509.jmacaroons.MacaroonsBuilder;
import com.github.nitram509.jmacaroons.MacaroonsVerifier;

public class ApiKeyValidator {

	private static Logger log = LoggerFactory.getLogger(ApiKeyValidator.class);
	
	private final ApiKeyProperties properties;

    @Value("${ourchitecture.boot.apikey.required:Unauthorized}")
    private String apiKeyRequiredError;

    @Value("${ourchitecture.boot.apikey.invalid:Full authentication is required to access this resource (401.400)}")
    private String invalidApiKeyError;

    @Value("${ourchitecture.boot.apikey.exception:Full authentication is required to access this resource (401.500)}")
    private String unexpectedApiKeyError;
	
	public ApiKeyValidator(final ApiKeyProperties properties) {
		this.properties = properties;
	}
    
    public boolean isEnabled() {
    	return this.properties.isEnabled();
    }
    
	public String validateRequestApiKey(final String apiKey) {
		
		log.debug("[ApiKeyValidator] validateRequestApiKey()");
		
		if (this.isEnabled()) {
			log.debug("[ApiKeyValidator] API Key Enabled");
			
			if (apiKey == null || apiKey.equals("")) {
				log.warn("[ApiKeyValidator] Missing API Key HTTP Header");
				return this.apiKeyRequiredError;
			}
			
			try {
				final Macaroon apiKeyMacaroon = MacaroonsBuilder.deserialize(apiKey);
				
				final MacaroonsVerifier apiKeyVerifier = new MacaroonsVerifier(apiKeyMacaroon);
				
				final String secret = this.properties.getSecret();
				
				if (secret == null || secret.equals("")) {
					
                    final String generatedSecret = java.util.UUID.randomUUID().toString();

					log.info("");
					log.info("");
					log.info("[ApiKeyValidator] API Key Secret was not set. Setting to: " + generatedSecret);
					log.info("");
					log.info("");
                    
					this.properties.setSecret(generatedSecret);
				}
				
				if (!apiKeyVerifier.isValid(this.properties.getSecret())) {
					log.warn("[ApiKeyValidator] Invalid API Key Macaroon: " + apiKey);
					
					return this.invalidApiKeyError;
				}
			} catch (Exception macaroonException) {
				log.error("[ApiKeyValidator] API Key Macaroon Error (" + apiKey + "): " + macaroonException.getMessage());
				
				return this.unexpectedApiKeyError;
			}
		}
		
		return null;
	}
}
