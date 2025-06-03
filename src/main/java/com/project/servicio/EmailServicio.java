package com.project.servicio;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

@Service
public class EmailServicio {

	@Value("${sendgrid.api.key}")
	private String sendGridApiKey;

	public void enviarEmail(String destinatario, String asunto, String contenido) throws IOException {
		Email from = new Email("contacto@ivaniglesias.es");
		Email to = new Email(destinatario);
		Content content = new Content("text/plain", contenido);
		Mail mail = new Mail(from, asunto, to, content);

		SendGrid sg = new SendGrid(sendGridApiKey);
		Request request = new Request();

		try {
			request.setMethod(Method.POST);
			request.setEndpoint("mail/send");
			request.setBody(mail.build());

			Response response = sg.api(request);
			System.out.println("Status: " + response.getStatusCode());
			System.out.println("Body: " + response.getBody());
			System.out.println("Headers: " + response.getHeaders());

		} catch (IOException ex) {
			System.err.println("Error al enviar email: " + ex.getMessage());
			throw ex;
		}
	}
}
