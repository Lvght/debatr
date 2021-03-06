package br.ufscar.dc.dsw1.debatr.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.jboss.jandex.Main;

import br.ufscar.dc.dsw1.debatr.domain.User;
import br.ufscar.dc.dsw1.debatr.helper.JwtHelper;

public class EmailService {

    /**
     * Envia um email com o assunto e o corpo passados como parametros.
     *
     * @param subject Assunto do email.
     * @param body    Corpo do email.
     * @param to      Destinatario do email.
     * @param from    Remetente do email.
     */
    public void send(InternetAddress from, InternetAddress to, String subject, String body, File file) {

        try {

            Properties prop = new Properties();
            InputStream is = Main.class.getClassLoader().getResourceAsStream("application.properties");

            if (is != null) {
                prop.load(is);
            } else {
                throw new FileNotFoundException("config.properties not found in the classpath");
            }

            String username = System.getenv("GMAIL_ADDRESS");
            String password = System.getenv("GMAIL_PASSWORD");

            Session session = Session.getInstance(prop, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(from);
            message.setRecipient(Message.RecipientType.TO, to);
            message.setSubject(subject);

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(body, "text/plain");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            if (file != null) {
                MimeBodyPart attachmentBodyPart = new MimeBodyPart();
                attachmentBodyPart.attachFile(file);
                multipart.addBodyPart(attachmentBodyPart);
            }

            message.setContent(multipart);
            Transport.send(message);

            System.out.println("Mensagem enviada com sucesso!");

        } catch (Exception e) {
            System.out.println("Mensagem n??o enviada!");
            e.printStackTrace();
        }
    }

    /**
     * Envia um email com o assunto e o corpo passados como parametros.
     *
     * @param from    Remetente do email.
     * @param to      Destinatario do email.
     * @param subject Assunto do email.
     * @param body    Corpo do email.
     */
    public void send(InternetAddress from, InternetAddress to, String subject, String body) {
        send(from, to, subject, body, null);
    }

    public void sendVerifyYourEmail(User user, String path) {
        final String subject = "Verifique o email da sua conta Debatr";
        final String token = JwtHelper.generateJWTToken(user, "verify");

        final String body = "" + "Ol??,\n\n"
                + "Bem-vindo(a) ao Debatr. Para continuar usando sua conta sem limita????es verifique sua conta "
                + "com o link abaixo.\n\n" + "<a href=" + path + "/config/verify-email/" + token
                + ">Verifique sua conta</a>";

        try {
            send(new InternetAddress("no-reply@debatr.com", "Debatr"),
                    new InternetAddress(user.getEmail(), user.getDisplayName()), subject, body);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void sendPasswordResetEmail(User user, String path) {
        final String subject = "Recupera????o de senha";
        final String token = JwtHelper.generateJWTToken(user, "reset");

        final String body = "" + "Ol??,\n\n"
                + "Voc?? est?? recebendo este email porque voc?? ou algu??m solicitou a recupera????o de senha para sua conta.\n\n"
                + "<a href=" + path + "/config/password-reset/" + token
                + ">Recuperar senha</a>";

        try {
            send (
                    new InternetAddress("no-reply@debatr.com", "Debatr"),
                    new InternetAddress(user.getEmail(), user.getDisplayName()),
                    subject,
                    body
            );
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
