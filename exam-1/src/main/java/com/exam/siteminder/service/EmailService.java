package com.exam.siteminder.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import com.exam.siteminder.service.email.mailgun.MailgunProperties;
import com.mailgun.api.v3.MailgunMessagesApi;
import com.mailgun.client.MailgunClient;
import com.mailgun.model.message.Message;
import com.mailgun.model.message.MessageResponse;

import feign.FeignException;

@Service
public class EmailService {
	
	@Autowired
	private MailgunProperties mailgunProperties;


    @Retryable(maxAttempts = 2, value = Exception.class, backoff = @Backoff(delay = 1000))
    public ResponseEntity<Object> sendEmailWithFailover(String to, String subject, String body) {
        try {
           return sendEmail(to, subject, body);
        } catch (Exception e) {
        	e.printStackTrace();
            // Primary provider failed, retry using the backup provider
            //sendEmail(backupMailSender, to, subject, body);
        	return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    private ResponseEntity<Object> sendEmail(String to, String subject, String body) {
    	MailgunMessagesApi mailgunMessagesApi = MailgunClient.config(mailgunProperties.getApikey())
                .createApi(MailgunMessagesApi.class);

    	 Message message = Message.builder()
                 .from(mailgunProperties.getSender())
                 .to(to)
                 .subject(subject)
                 .text(body)
                 .build();

         
         try {
             MessageResponse messageResponse = mailgunMessagesApi.sendMessage(mailgunProperties.getDomain(), message);
             
             return ResponseEntity.status(200).body(messageResponse.getMessage());
         } catch (FeignException exception) {
//           Exception message
             String exceptionMessage = exception.getMessage();
//           status code                
             int statusCode = exception.status();
//           Headers
             return ResponseEntity.status(statusCode).body(exceptionMessage);
         }
         

    }
}

