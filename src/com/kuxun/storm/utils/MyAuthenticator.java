/*    */ package com.kuxun.storm.utils;
/*    */ 
/*    */ import javax.mail.Authenticator;
/*    */ import javax.mail.PasswordAuthentication;
/*    */ 
/*    */ public class MyAuthenticator extends Authenticator
/*    */ {
/*  6 */   String userName = null;
/*  7 */   String password = null;
/*    */ 
/*    */   public MyAuthenticator() {
/*    */   }
/*    */ 
/*    */   public MyAuthenticator(String username, String password) {
/* 13 */     this.userName = username;
/* 14 */     this.password = password;
/*    */   }
/*    */ 
/*    */   protected PasswordAuthentication getPasswordAuthentication() {
/* 18 */     return new PasswordAuthentication(this.userName, this.password);
/*    */   }
/*    */ }

/* Location:           C:\Users\hp\Desktop\crawlerlog.jar
 * Qualified Name:     com.kuxun.storm.utils.MyAuthenticator
 * JD-Core Version:    0.6.0
 */