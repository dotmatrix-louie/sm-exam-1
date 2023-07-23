package com.exam.siteminder.service.email.mailgun;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.exam.siteminder.service.email.EmailProperties;

@Component
@ConfigurationProperties(prefix="email.services.mailgun")
public class MailgunProperties extends EmailProperties	{

	private String domain;

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}
	
}
