/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author 797138
 */
public class GmailService {
    
    public static void sendMail(String email, String subject, String template, HashMap<String, String> tags) throws FileNotFoundException, IOException{
        
        try{
            //Reading template in to a string
            BufferedReader br = new BufferedReader(new FileReader(new File(template)));
            String line = br.readLine();
            String body = "";
            
            while(line != null){
                body += line;
                line = br.readLine();
            }
            
            for(String tag: tags.keySet()){
                body = body.replace("%" + tag + "%", tags.get(tag));
            }
            
            sendMail(email, subject, body, true);
            
        }catch(Exception ex){
            Logger.getLogger(GmailService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void sendMail(String to, String subject, String body, boolean bodyIsHTML) throws MessagingException, NamingException{
        
        Context env = (Context)new InitialContext().lookup("java:comp/env");
        String username = (String)env.lookup("webmail-username");
        String password = (String)env.lookup("webmail-password");

        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtps");
        props.put("mail.smtps.host", "smtp.gmail.com");
        props.put("mail.smtps.port", 465);
        props.put("mail.smtps.auth", "true");
        props.put("mail.smtps.quitwait", "false");
        
        Session session = Session.getDefaultInstance(props);
        session.setDebug(true);
        
        //Creating message
        Message message = new MimeMessage(session);
        message.setSubject(subject);
        if(bodyIsHTML){
            message.setContent(body, "text/html");
        }else{
            message.setText(body);
        }
        
        //Address the message
        Address fromAddress = new InternetAddress(username);
        Address toAddress = new InternetAddress(to);
        
        message.setFrom(fromAddress);
        message.setRecipient(Message.RecipientType.TO, toAddress);
        
        //send the Message
        Transport transport = session.getTransport();
        transport.connect(username, password);
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
        
    }
}
