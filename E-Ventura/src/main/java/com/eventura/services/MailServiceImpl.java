package com.eventura.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Service
public class MailServiceImpl implements MailService {

	@Autowired
	private JavaMailSender sender;
	
	@Override
	public boolean send(String from, String to, String subject, String content) {
		try {
			MimeMessage mimeMessage = sender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
			helper.setFrom(from);
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(content, true);
			sender.send(mimeMessage);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
