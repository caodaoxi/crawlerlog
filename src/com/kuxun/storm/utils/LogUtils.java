/*     */ package com.kuxun.storm.utils;
/*     */ 
/*     */ import java.text.ParseException;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.apache.log4j.Logger;
/*     */ 
/*     */ public class LogUtils
/*     */ {
/*  16 */   public static Logger log = Logger.getLogger("filter");
/*     */ 
/*     */   public static Map<String, String> arrayToMap(String[] keys, String[] values) throws Exception {
/*  19 */     if (keys.length != values.length) {
/*  20 */       throw new Exception("layout is not unified");
/*     */     }
/*  22 */     Map kv = new HashMap();
/*  23 */     for (int i = 0; i < keys.length; i++) {
/*  24 */       if (values[i] == null)
/*  25 */         kv.put(keys[i], "-");
/*     */       else {
/*  27 */         kv.put(keys[i], values[i]);
/*     */       }
/*     */     }
/*  30 */     return kv;
/*     */   }
/*     */ 
/*     */   public static Map<String, String> getUrlParams(String url)
/*     */   {
/*  35 */     Map paramMap = new HashMap();
/*  36 */     int index = url.indexOf('?');
/*  37 */     url = url.substring(index + 1);
/*  38 */     String[] params = url.split("&");
/*  39 */     for (String param : params) {
/*  40 */       String[] kv = param.split("=");
/*  41 */       if (kv.length == 1)
/*  42 */         paramMap.put(kv[0], "-");
/*  43 */       else if (kv.length >= 2) {
/*  44 */         paramMap.put(kv[0], kv[1]);
/*     */       }
/*     */     }
/*  47 */     return paramMap;
/*     */   }
/*     */ 
/*     */   public static String rltrim(String line, String[] fixs) {
/*  51 */     String retval = null;
/*  52 */     for (String fix : fixs) {
/*  53 */       if (retval != null) {
/*  54 */         line = retval;
/*     */       }
/*  56 */       if (line.startsWith(fix)) {
/*  57 */         retval = line.substring(fix.length());
/*     */       }
/*  59 */       if ((retval != null) && (retval.endsWith(fix)))
/*  60 */         retval = retval.substring(0, retval.length() - fix.length());
/*  61 */       else if (line.endsWith(fix)) {
/*  62 */         retval = line.substring(0, line.length() - fix.length());
/*     */       }
/*     */     }
/*  65 */     return retval;
/*     */   }
/*     */ 
/*     */   public static long parseToTime(String value)
/*     */   {
/*  70 */     SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd H:m:s");
/*  71 */     Date dt = new Date();
/*  72 */     int index = value.indexOf(':');
/*  73 */     String hms = value.substring(index + 1);
/*  74 */     String date = value.substring(0, index);
/*  75 */     String[] dmy = date.split("/");
/*  76 */     dmy[1] = getMoth(dmy[1]);
/*  77 */     value = dmy[2] + "-" + dmy[1] + "-" + dmy[0] + " " + hms;
/*     */     try {
/*  79 */       dt = timeFormat.parse(value);
/*     */     } catch (ParseException e) {
/*  81 */       e.printStackTrace();
/*     */     }
/*  83 */     return dt.getTime() / 1000L;
/*     */   }
/*     */ 
/*     */   public static String getMoth(String month) {
/*  87 */     if ("Jan".equals(month))
/*  88 */       return "01";
/*  89 */     if ("Feb".equals(month))
/*  90 */       return "02";
/*  91 */     if ("Mar".equals(month))
/*  92 */       return "03";
/*  93 */     if ("Apr".equals(month))
/*  94 */       return "04";
/*  95 */     if ("May".equals(month))
/*  96 */       return "05";
/*  97 */     if ("Jun".equals(month))
/*  98 */       return "06";
/*  99 */     if ("Jul".equals(month))
/* 100 */       return "07";
/* 101 */     if ("Aug".equals(month))
/* 102 */       return "08";
/* 103 */     if ("Sep".equals(month))
/* 104 */       return "09";
/* 105 */     if ("Oct".equals(month))
/* 106 */       return "10";
/* 107 */     if ("Nov".equals(month))
/* 108 */       return "11";
/* 109 */     if ("Dec".equals(month)) {
/* 110 */       return "12";
/*     */     }
/* 112 */     return null;
/*     */   }
/*     */ 
/*     */   public static String parseToDate(long date, String format) {
/* 116 */     SimpleDateFormat dateFormat = new SimpleDateFormat(format);
/* 117 */     return dateFormat.format(Long.valueOf(date * 1000L));
/*     */   }
/*     */ 
/*     */   public static String getDomain(String url)
/*     */   {
/*     */     try {
/* 123 */       if ((!"\"-\"".equals(url)) && (!"-".equals(url))) {
/* 124 */         Pattern p = Pattern.compile("(?<=http://|\\.)[^.]*?\\.(com|cn|net|org|biz|info|cc|tv)", 2);
/* 125 */         Matcher matcher = p.matcher(url);
/* 126 */         matcher.find();
/* 127 */         return matcher.group();
/*     */       }
/* 129 */       return url;
/*     */     }
/*     */     catch (Exception e) {
/* 132 */       e.printStackTrace();
/*     */     }
/* 134 */     return url;
/*     */   }
/*     */ 
/*     */   public static String formatLog(String[] logLayout, Map<String, String> logMap)
/*     */   {
/* 140 */     StringBuffer log = new StringBuffer();
/* 141 */     String value = null;
/* 142 */     String[] arrayOfString = logLayout; int j = logLayout.length; for (int i = 0; i < j; i++) { String logField = arrayOfString[i];
/* 143 */       value = (String)logMap.get(logField);
/* 144 */       if (value != null)
/* 145 */         log.append(value).append("\t");
/*     */       else {
/* 147 */         log.append("-").append("\t");
/*     */       }
/*     */     }
/* 150 */     return log.toString();
/*     */   }
/*     */ 
/*     */   public static void main(String[] args)
/*     */     throws ParseException
/*     */   {
/*     */   }
/*     */ }

/* Location:           C:\Users\hp\Desktop\crawlerlog.jar
 * Qualified Name:     com.kuxun.storm.utils.LogUtils
 * JD-Core Version:    0.6.0
 */