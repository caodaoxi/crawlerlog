/*     */ package com.kuxun.storm.utils;
/*     */ 
/*     */ import java.util.Properties;
/*     */ 
/*     */ public class MailSenderInfo
/*     */ {
/*     */   private String mailServerHost;
/*  11 */   private String mailServerPort = "25";
/*     */   private String fromAddress;
/*     */   private String toAddress;
/*     */   private String userName;
/*     */   private String password;
/*  20 */   private boolean validate = false;
/*     */   private String subject;
/*     */   private String content;
/*     */   private String[] attachFileNames;
/*     */ 
/*     */   public Properties getProperties()
/*     */   {
/*  32 */     Properties p = new Properties();
/*  33 */     p.put("mail.smtp.host", this.mailServerHost);
/*  34 */     p.put("mail.smtp.port", this.mailServerPort);
/*  35 */     p.put("mail.smtp.auth", this.validate ? "true" : "false");
/*  36 */     return p;
/*     */   }
/*     */ 
/*     */   public String getMailServerHost() {
/*  40 */     return this.mailServerHost;
/*     */   }
/*     */ 
/*     */   public void setMailServerHost(String mailServerHost) {
/*  44 */     this.mailServerHost = mailServerHost;
/*     */   }
/*     */ 
/*     */   public String getMailServerPort() {
/*  48 */     return this.mailServerPort;
/*     */   }
/*     */ 
/*     */   public void setMailServerPort(String mailServerPort) {
/*  52 */     this.mailServerPort = mailServerPort;
/*     */   }
/*     */ 
/*     */   public boolean isValidate() {
/*  56 */     return this.validate;
/*     */   }
/*     */ 
/*     */   public void setValidate(boolean validate) {
/*  60 */     this.validate = validate;
/*     */   }
/*     */ 
/*     */   public String[] getAttachFileNames() {
/*  64 */     return this.attachFileNames;
/*     */   }
/*     */ 
/*     */   public void setAttachFileNames(String[] fileNames) {
/*  68 */     this.attachFileNames = fileNames;
/*     */   }
/*     */ 
/*     */   public String getFromAddress() {
/*  72 */     return this.fromAddress;
/*     */   }
/*     */ 
/*     */   public void setFromAddress(String fromAddress) {
/*  76 */     this.fromAddress = fromAddress;
/*     */   }
/*     */ 
/*     */   public String getPassword() {
/*  80 */     return this.password;
/*     */   }
/*     */ 
/*     */   public void setPassword(String password) {
/*  84 */     this.password = password;
/*     */   }
/*     */ 
/*     */   public String getToAddress() {
/*  88 */     return this.toAddress;
/*     */   }
/*     */ 
/*     */   public void setToAddress(String toAddress) {
/*  92 */     this.toAddress = toAddress;
/*     */   }
/*     */ 
/*     */   public String getUserName() {
/*  96 */     return this.userName;
/*     */   }
/*     */ 
/*     */   public void setUserName(String userName) {
/* 100 */     this.userName = userName;
/*     */   }
/*     */ 
/*     */   public String getSubject() {
/* 104 */     return this.subject;
/*     */   }
/*     */ 
/*     */   public void setSubject(String subject) {
/* 108 */     this.subject = subject;
/*     */   }
/*     */ 
/*     */   public String getContent() {
/* 112 */     return this.content;
/*     */   }
/*     */ 
/*     */   public void setContent(String textContent) {
/* 116 */     this.content = textContent;
/*     */   }
/*     */ }

/* Location:           C:\Users\hp\Desktop\crawlerlog.jar
 * Qualified Name:     com.kuxun.storm.utils.MailSenderInfo
 * JD-Core Version:    0.6.0
 */