package com.wildlifebackend.wildlife.service;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to,String subject, String htmlContent){

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // true = HTML content
            helper.setFrom("your_email@gmail.com"); // Replace with your SMTP email
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }

    }

    public void sendWelcomeEmail(String to, String competitorName, String photographerId) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject("Welcome to the National Wildlife Photography Award 2025 – Your Photographer ID");
            helper.setFrom("your_email@gmail.com"); // Replace with your email configured in SMTP

            String emailContent = """
                <p>Dear <strong>%s</strong>,</p>
                <p>Thank you for registering for the <strong>National Wildlife Photography Award 2025</strong>.
                We are excited to have you join this prestigious celebration of wildlife photography.</p>
                <p><strong>Your Photographer ID is:</strong> %s</p>
                <p>(Please keep this ID safe. You will need it for all submissions and competition-related correspondence.)</p>
                <h3>Competition Timeline:</h3>
                <ul>
                    <li><strong>Submission Opening:</strong> 01 September 2025</li>
                    <li><strong>Submission Closing:</strong> 30 September 2025</li>
                    <li><strong>Judging Period:</strong> 10–20 October 2025</li>
                    <li><strong>Winner Announcement:</strong> 25 October 2025</li>
                </ul>
                <p>We wish you the best of luck and look forward to seeing your work capture the beauty of the wild!</p>
                <p>Best Regards,<br>
                <em>National Wildlife Photography Award Committee</em><br>
                <a href="https://www.nwpa.lk">https://www.nwpa.lk</a><br>
                123 Wildlife St, Colombo, Sri Lanka - (+94) 11 234 5678</p>
                """.formatted(competitorName, photographerId);

            helper.setText(emailContent, true); // true = HTML content

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send welcome email", e);
        }
    }
}