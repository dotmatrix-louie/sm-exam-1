package com.exam.siteminder.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import com.exam.siteminder.service.email.mailgun.MailgunProperties;
import com.exam.siteminder.service.email.sendgrid.SendGridProperties;
import com.mailgun.api.v3.MailgunMessagesApi;
import com.mailgun.client.MailgunClient;
import com.mailgun.model.message.Message;
import com.mailgun.model.message.MessageResponse;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

import feign.FeignException;

@Service
public class EmailService {

	@Autowired
	private MailgunProperties mailgunProperties;

	@Autowired
	private SendGridProperties sendgridProperties;

	/**
	 * Uses spring retry dependency to retry email action if fails
	 * @param to
	 * @param subject
	 * @param body
	 * @return
	 * @throws IOException
	 */
	@Retryable(maxAttempts = 2, value = Exception.class, backoff = @Backoff(delay = 1000))
	public ResponseEntity<Object> sendEmailWithFailover(String to, String subject, String body) throws IOException {
		try {
			return sendEmailSendgrid(to, subject, body);
		} catch (Exception e) {
			// Primary provider failed, retry using the backup provider
			return sendEmailMailgun(to, subject, body);
		}
	}
	
	/**
	 * Uses SendGrid Email API 
	 * @param to
	 * @param subject
	 * @param body
	 * @return
	 * @throws IOException
	 */
	private ResponseEntity<Object> sendEmailSendgrid(String to, String subject, String body) throws IOException {
		Email from = new Email(sendgridProperties.getSender());
		Email toSd = new Email(to);
		Content content = new Content("text/plain", body);
		Mail mail = new Mail(from, subject, toSd, content);

		SendGrid sg = new SendGrid(System.getenv(sendgridProperties.getApikey()));
		Request request = new Request();
		try {
			request.setMethod(Method.POST);
			request.setEndpoint("mail/send");
			request.setBody(mail.build());
			Response response = sg.api(request);

			return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
		} catch (IOException ex) {
			throw ex;
		}
	}

	/**
	 * Uses Mailgun API to send email
	 * @param to
	 * @param subject
	 * @param body
	 * @return
	 */
	private ResponseEntity<Object> sendEmailMailgun(String to, String subject, String body) {
		MailgunMessagesApi mailgunMessagesApi = MailgunClient.config(mailgunProperties.getApikey())
				.createApi(MailgunMessagesApi.class);

		Message message = Message.builder().from(mailgunProperties.getSender()).to(to).subject(subject).text(body)
				.build();

		try {
			MessageResponse messageResponse = mailgunMessagesApi.sendMessage(mailgunProperties.getDomain(), message);

			return ResponseEntity.status(200).body(messageResponse.getMessage());
		} catch (FeignException exception) {
			String exceptionMessage = exception.getMessage();
			int statusCode = exception.status();
			return ResponseEntity.status(statusCode).body(exceptionMessage);
		}

	}
}
