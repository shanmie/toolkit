package cn.org.toolkit.email;

import cn.org.toolkit.enums.MailType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.mail.*;

/**
 * @author deacon
 * @since 2019/3/22
 */
@Slf4j
public class EmailHelper {
    public Email getMail(MailType mailType) {
        Email email = new SimpleEmail();
        switch (mailType) {
            case simple:
                email = simpleMail();
                break;
            case attachment:
                email = attachmentMail();
                break;
            case html:
                htmlMail();
                break;
            case html_image:
                htmlWithImagesMail();
                break;
            default:
                email = simpleMail();
                break;
        }
        return email;
    }

    public void sendMail(MailType mailType,String title,String content,String to){
        try {
            Email mail = getMail(mailType);
            mail.send();
        } catch (EmailException e) {
            log.info("send mail is error",e);
        }
    }

    private Email simpleMail(){
        Email email = new SimpleEmail();
        try {
            email.setHostName("smtp.googlemail.com");
            email.setSmtpPort(465);
            email.setAuthenticator(new DefaultAuthenticator("username", "password"));
            email.setSSLOnConnect(true);
            email.setFrom("user@gmail.com");
            email.setSubject("TestMail");
            email.setMsg("This is a test mail ... :-)");
            email.addTo("foo@bar.com");
        } catch (EmailException e) {
            log.info("create simple mail is error",e);
        }
        return email;
    }
    private Email attachmentMail(){
        MultiPartEmail email = new MultiPartEmail();
        try {
            EmailAttachment attachment = new EmailAttachment();
            attachment.setPath("mypictures/john.jpg");
            attachment.setDisposition(EmailAttachment.ATTACHMENT);
            attachment.setDescription("Picture of John");
            attachment.setName("John");

            // Create the email message
            email.setHostName("mail.myserver.com");
            email.addTo("jdoe@somewhere.org", "John Doe");
            email.setFrom("me@apache.org", "Me");
            email.setSubject("The picture");
            email.setMsg("Here is the picture you wanted");

            // add the attachment
            email.attach(attachment);
        }catch (EmailException e){
            log.info("create attachment mail is error",e);
        }
        return email;
    }
    private void htmlMail(){

    }
    private void htmlWithImagesMail(){

    }
    private void getConfig(){
        //从某处读取邮件配置
    }
}
