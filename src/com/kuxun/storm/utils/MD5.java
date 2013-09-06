/*    */ package com.kuxun.storm.utils;
/*    */ 
/*    */ import java.security.MessageDigest;
/*    */ 
/*    */ public class MD5
/*    */ {
/*    */   public static final String md5(String s)
/*    */   {
/*  5 */     char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
/*    */     try {
/*  7 */       byte[] strTemp = s.getBytes();
/*    */ 
/*  9 */       MessageDigest mdTemp = MessageDigest.getInstance("MD5");
/* 10 */       mdTemp.update(strTemp);
/* 11 */       byte[] md = mdTemp.digest();
/* 12 */       int j = md.length;
/* 13 */       char[] str = new char[j * 2];
/* 14 */       int k = 0;
/* 15 */       for (int i = 0; i < j; i++) {
/* 16 */         byte b = md[i];
/*    */ 
/* 19 */         str[(k++)] = hexDigits[(b >> 4 & 0xF)];
/* 20 */         str[(k++)] = hexDigits[(b & 0xF)];
/*    */       }
/* 22 */       return new String(str); } catch (Exception e) {
/*    */     }
/* 24 */     return null;
/*    */   }
/*    */ }

/* Location:           C:\Users\hp\Desktop\crawlerlog.jar
 * Qualified Name:     com.kuxun.storm.utils.MD5
 * JD-Core Version:    0.6.0
 */