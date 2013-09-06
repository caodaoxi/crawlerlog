/*     */ package com.kuxun.storm.utils;
/*     */ 
/*     */ import java.util.Date;
/*     */ import java.util.Properties;
/*     */ import javax.mail.Address;
/*     */ import javax.mail.BodyPart;
/*     */ import javax.mail.Message;
/*     */ import javax.mail.Message.RecipientType;
/*     */ import javax.mail.MessagingException;
/*     */ import javax.mail.Multipart;
/*     */ import javax.mail.Session;
/*     */ import javax.mail.Transport;
/*     */ import javax.mail.internet.InternetAddress;
/*     */ import javax.mail.internet.MimeBodyPart;
/*     */ import javax.mail.internet.MimeMessage;
/*     */ import javax.mail.internet.MimeMultipart;
/*     */ 
/*     */ public class MailUtils
/*     */ {
/*     */   public static boolean sendTextMail(MailSenderInfo mailInfo)
/*     */   {
/*  29 */     MyAuthenticator authenticator = null;
/*  30 */     Properties pro = mailInfo.getProperties();
/*  31 */     if (mailInfo.isValidate())
/*     */     {
/*  33 */       authenticator = new MyAuthenticator(mailInfo.getUserName(), mailInfo.getPassword());
/*     */     }
/*     */ 
/*  36 */     Session sendMailSession = 
/*  37 */       Session.getDefaultInstance(pro, authenticator);
/*     */     try
/*     */     {
/*  40 */       Message mailMessage = new MimeMessage(sendMailSession);
/*     */ 
/*  42 */       Address from = new InternetAddress(mailInfo.getFromAddress());
/*     */ 
/*  44 */       mailMessage.setFrom(from);
/*     */ 
/*  46 */       Address to = new InternetAddress(mailInfo.getToAddress());
/*  47 */       mailMessage.setRecipient(Message.RecipientType.TO, to);
/*     */ 
/*  49 */       mailMessage.setSubject(mailInfo.getSubject());
/*     */ 
/*  51 */       mailMessage.setSentDate(new Date());
/*     */ 
/*  53 */       String mailContent = mailInfo.getContent();
/*  54 */       mailMessage.setText(mailContent);
/*     */ 
/*  56 */       Transport.send(mailMessage);
/*  57 */       return true;
/*     */     } catch (MessagingException ex) {
/*  59 */       ex.printStackTrace();
/*     */     }
/*  61 */     return false;
/*     */   }
/*     */ 
/*     */   public static boolean sendHtmlMail(MailSenderInfo mailInfo)
/*     */   {
/*  72 */     MyAuthenticator authenticator = null;
/*  73 */     Properties pro = mailInfo.getProperties();
/*     */ 
/*  75 */     if (mailInfo.isValidate()) {
/*  76 */       authenticator = new MyAuthenticator(mailInfo.getUserName(), 
/*  77 */         mailInfo.getPassword());
/*     */     }
/*     */ 
/*  80 */     Session sendMailSession = 
/*  81 */       Session.getDefaultInstance(pro, authenticator);
/*     */     try
/*     */     {
/*  84 */       Message mailMessage = new MimeMessage(sendMailSession);
/*     */ 
/*  86 */       Address from = new InternetAddress(mailInfo.getFromAddress());
/*     */ 
/*  88 */       mailMessage.setFrom(from);
/*     */ 
/*  90 */       Address to = new InternetAddress(mailInfo.getToAddress());
/*     */ 
/*  92 */       mailMessage.setRecipient(Message.RecipientType.TO, to);
/*     */ 
/*  94 */       mailMessage.setSubject(mailInfo.getSubject());
/*     */ 
/*  96 */       mailMessage.setSentDate(new Date());
/*     */ 
/*  98 */       Multipart mainPart = new MimeMultipart();
/*     */ 
/* 100 */       BodyPart html = new MimeBodyPart();
/*     */ 
/* 102 */       html.setContent(mailInfo.getContent(), "text/html; charset=utf-8");
/* 103 */       mainPart.addBodyPart(html);
/*     */ 
/* 105 */       mailMessage.setContent(mainPart);
/*     */ 
/* 107 */       Transport.send(mailMessage);
/* 108 */       return true;
/*     */     } catch (MessagingException ex) {
/* 110 */       ex.printStackTrace();
/*     */     }
/* 112 */     return false;
/*     */   }
/*     */ 
/*     */   public static void main(String[] args) {
/* 116 */     MailSenderInfo mailInfo = new MailSenderInfo();
/* 117 */     mailInfo.setContent("fasfasf");
/* 118 */     mailInfo.setMailServerHost("smtp.163.com");
/* 119 */     mailInfo.setFromAddress("caodaoxi1234@163.com");
/* 120 */     mailInfo.setToAddress("caodx@kuxun.com");
/* 121 */     mailInfo.setMailServerPort("25");
/* 122 */     mailInfo.setPassword("caodaoxi123321");
/* 123 */     mailInfo.setUserName("caodaoxi1234@163.com");
/* 124 */     mailInfo.setSubject("test");
/* 125 */     mailInfo.setValidate(true);
/*     */ 
/* 127 */     sendHtmlMail(mailInfo);
/*     */   }
/*     */ }

/* Location:           C:\Users\hp\Desktop\crawlerlog.jar
 * Qualified Name:     com.kuxun.storm.utils.MailUtils
 * JD-Core Version:    0.6.0
 */