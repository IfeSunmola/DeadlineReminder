package com.example.backend.mailsender;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

/**
 * @author Ife Sunmola
 */
@Service
@AllArgsConstructor
@Async
public class EmailSenderService {
	private final JavaMailSender mailSender;

	@Async
	public void sendMail(String subject, String fromEmail, String toEmail, String replyTo, String mailContent) {
		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
			helper.setSubject(subject);
			helper.setFrom(fromEmail, "Deadline Reminder");
			helper.setTo(toEmail);
			helper.setReplyTo(replyTo);
			helper.setText(mailContent, true);
			mailSender.send(mimeMessage);
		}
		catch (MessagingException | UnsupportedEncodingException ignored) {

		}
	}
}