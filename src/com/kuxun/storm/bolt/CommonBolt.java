 package com.kuxun.storm.bolt;
 
 import backtype.storm.topology.IRichBolt;
 import java.io.IOException;
 import java.util.Properties;
 import org.apache.log4j.Logger;
 import org.apache.log4j.PropertyConfigurator;
 
 public abstract class CommonBolt
   implements IRichBolt
 {
   private static final long serialVersionUID = 1L;
   public static Logger log = null;
 
   static { Properties props = new Properties();
     try {
       props.load(IRichBolt.class.getClassLoader().getResourceAsStream("filter.properties"));
       String filterPath = System.getProperty("storm.home") + "/" + props.getProperty("log4j.appender.filter.File");
       props.setProperty("log4j.appender.filter.File", filterPath);
       PropertyConfigurator.configure(props);
       log = Logger.getLogger("filter");
     } catch (IOException e) {
       e.printStackTrace();
     }
   }
 }
