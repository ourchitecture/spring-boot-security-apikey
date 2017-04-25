package ourchitecture.boot.apikey;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("ourchitecture.boot.apikey")
public class ApiKeyProperties {

	private boolean enabled = true;

	private String secret = "";

	/**
	* Returns if the API Key verification is enabled or disabled.
	* @return boolean {@code true} if the API Key verification is enabled; otherwise {@code false}.
	*/
	public boolean isEnabled() {
		return this.enabled;
	}

	/**
	* Enables or disables API Key verification.
	* @param enabled {@code true} if verification is enabled; otherwise {@code false}.
	*/
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	* Returns the API Key secret.
	* @return String The API Key secret.
	*/
	public String getSecret() {
		return this.secret;
	}

	/**
	* Sets the API Key secret.
	* @param secret The API Key secret.
	*/
	public void setSecret(String secret) {
		this.secret = secret;
	}
}