package ourchitecture.boot.apikey;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("ourchitecture.boot.apikey")
public class ApiKeyProperties {

	private boolean enabled = true;

	private String secret = "";

	public boolean isEnabled() {
		return this.enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getSecret() {
		return this.secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}
}