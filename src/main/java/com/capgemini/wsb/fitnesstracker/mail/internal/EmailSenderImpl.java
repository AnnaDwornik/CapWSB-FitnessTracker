package com.capgemini.wsb.fitnesstracker.mail.internal;

import com.capgemini.wsb.fitnesstracker.mail.api.EmailDto;
import com.capgemini.wsb.fitnesstracker.mail.api.EmailSender;
import com.capgemini.wsb.fitnesstracker.mail.api.EmailSendingException;
import com.capgemini.wsb.fitnesstracker.training.api.Training;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.StringJoiner;

@Service
public class EmailSenderImpl implements EmailSender {

    private final JavaMailSender mailSender;
    private final MailProperties mailProperties;

    public EmailSenderImpl(JavaMailSender mailSender, MailProperties mailProperties) {
        this.mailSender = mailSender;
        this.mailProperties = mailProperties;
    }

    @Override
    public void send(List<Training> email) {
        String toAddress = "sandbox.smtp.mailtrap.io";
        String subject = "Weekly Training Report";
        String content = generateReportContent(email);

        EmailDto newmail = new EmailDto(toAddress, subject, content);
        sendEmail(newmail);
    }

    private void sendEmail(EmailDto emailDto) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(emailDto.toAddress());
            helper.setSubject(emailDto.subject());
            helper.setText(emailDto.content(), true);
            helper.setFrom(mailProperties.getFrom());

            mailSender.send(message);
        } catch (MessagingException e) {
            System.err.println("Failed to send email: " + e.getMessage());
            throw new EmailSendingException("Failed to send email", e);
        }
    }

    private String generateReportContent(List<Training> trainings) {
        StringJoiner contentJoiner = new StringJoiner("\n\n", "Weekly Training Report\n\n", "");

        trainings.forEach(training -> {
            String trainingDetails = String.format("Training ID: %d\nStart Time: %s\nEnd Time: %s\nActivity Type: %s\nDistance: %.2f\nAverage Speed: %.2f",
                    training.getId(),
                    training.getStartTime(),
                    training.getEndTime(),
                    training.getActivityType(),
                    training.getDistance(),
                    training.getAverageSpeed()
            );
            contentJoiner.add(trainingDetails);
        });

        return contentJoiner.toString();
    }
}