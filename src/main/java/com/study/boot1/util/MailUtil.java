package com.study.boot1.util;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailUtil {
    public static boolean send(String fromName,String to,String title,String content){
        //보내는사람 정보
        String host = "smtp.daum.net";
        String username = "tjdgur327@daum.net";
        String password = "ghxrre1@";

        Properties props = new Properties();
        props.put("mail.smtps.auth", "true");

        Session session = Session.getDefaultInstance(props);
        MimeMessage msg = new MimeMessage(session);
        try{
            // 메일 관련
            msg.setSubject(title,"utf-8");
            msg.setContent(content, "text/html;charset=utf-8");
            msg.setFrom(new InternetAddress(username,fromName));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // 발송 처리
            Transport transport = session.getTransport("smtps");
            transport.connect(host, username, password);
            transport.sendMessage(msg, msg.getAllRecipients());
            transport.close();
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
