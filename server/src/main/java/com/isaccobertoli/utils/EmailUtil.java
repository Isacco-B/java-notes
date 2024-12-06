package com.isaccobertoli.utils;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

public class EmailUtil {
    private static String serverUrl = EnvUtil.getEnv("SERVER_URL");
    private static String serverPort = EnvUtil.getEnv("SERVER_PORT");

    private static void sendEmail(String to, String subject, String htmlMessage, String plainTextMessage)
            throws EmailException {
        HtmlEmail email = new HtmlEmail();
        email.setHostName(EnvUtil.getEnv("EMAIL_HOST"));
        email.setSmtpPort(Integer.parseInt(EnvUtil.getEnv("EMAIL_PORT")));
        email.setAuthenticator(new DefaultAuthenticator(EnvUtil.getEnv("EMAIL_ADDRESS"),
                EnvUtil.getEnv("EMAIL_PASSWORD")));
        email.setSSLOnConnect(true);
        email.setFrom(EnvUtil.getEnv("EMAIL_ADDRESS"), "NotesApp");
        email.setSubject(subject);
        email.setHtmlMsg(htmlMessage);
        email.setTextMsg(plainTextMessage);
        email.addTo(to);
        email.send();
    }

    public static void sendVerificationEmail(String to, String token) throws EmailException {
        String verificationLink = serverUrl + ":" + serverPort + "/api/auth/new-verification/" + token;

        if ("development".equalsIgnoreCase(EnvUtil.getEnv("MODE"))) {
            System.out.println("Verification link: " + verificationLink);
        } else {
            String subject = "Confirm your identity";
            String htmlMessage = "<p>Click <a href='" + verificationLink + "'>here</a> to verify your account.</p>";
            String plainTextMessage = "Click here to verify your account:" + verificationLink;
            sendEmail(to, subject, htmlMessage, plainTextMessage);
        }

    }

    public static void sendPasswordResetEmail(String to, String token) throws EmailException {
        String passwordResetLink = serverUrl + ":" + serverPort + "/api/auth/reset-password-confirm/"
                + token;

        if ("development".equalsIgnoreCase(EnvUtil.getEnv("MODE"))) {
            System.out.println("Password reset link: " + passwordResetLink);
        } else {
            String subject = "Password Reset Request for NotesApp";
            String htmlMessage = "<p>Click <a href='" + passwordResetLink + "'>here</a> to reset your password.</p>";
            String plainTextMessage = "Click here to reset your password:" + passwordResetLink;
            sendEmail(to, subject, htmlMessage, plainTextMessage);
        }

    }
}
