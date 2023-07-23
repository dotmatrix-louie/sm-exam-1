package com.exam.siteminder.service.email.sendgrid;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.exam.siteminder.service.email.EmailProperties;

@Component
@ConfigurationProperties(prefix="email.services.sendgrid")
public class SendGridProperties extends EmailProperties{
	
}
