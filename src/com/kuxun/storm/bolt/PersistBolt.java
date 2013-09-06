 package com.kuxun.storm.bolt;
 
 import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import com.kuxun.storm.basic.StatResult;
import com.kuxun.storm.basic.TimeCacheMapS;
import com.kuxun.storm.basic.TimeCacheMapS.ExpiredCallback;
import com.kuxun.storm.utils.DBUtils;
import com.kuxun.storm.utils.MD5;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
 
 public class PersistBolt extends CommonBolt {
   private OutputCollector collector = null;
   private TimeCacheMapS<String, StatResult> cache = null;
 
   public void cleanup(){
	   
   }
 
   public void prepare(Map stormConf, TopologyContext context, OutputCollector collector)
   {
     this.collector = collector;
 
     this.cache = new TimeCacheMapS<String, StatResult>(30*60, 4, new TimeCacheMapS.ExpiredCallback<String, StatResult>()
     {
       public void expire(String key, StatResult stat) {
         DBUtils.insertOrUpdate(stat);
       }
     });
   }
 
   public void declareOutputFields(OutputFieldsDeclarer declarer){
   }
 
   public Map<String, Object> getComponentConfiguration() {
     return null;
   }
 
   public void execute(Tuple input) {
	 Map<String, String> logFieldMap = (Map)input.getValueByField("logFieldMap");
     String channel = (String)logFieldMap.get("channel");
     boolean flag = false;
     if (("hotel".equals(channel)) || ("jipiao".equals(channel))) {
       extraField(logFieldMap);
     } else {
       log.error("[channel is error]");
       return;
     }
     this.collector.emit(new Values());
     if (flag)
       this.collector.ack(input);
   }
 
   public void extraField(Map<String, String> logFieldMap) {
     StringBuffer codeKey = new StringBuffer();
     codeKey.append((String)logFieldMap.get("hour")).append((String)logFieldMap.get("channel"));
     codeKey.append((String)logFieldMap.get("engine")).append((String)logFieldMap.get("status"));
     codeKey.append((String)logFieldMap.get("date")).append("code");
     String codeMd5 = MD5.md5(codeKey.toString());
     StatResult stat = null;
     if (!this.cache.containsKey(codeMd5)) {
       this.cache.put(codeMd5, 
         new StatResult(codeMd5, (String)logFieldMap.get("date"), 
         Integer.parseInt((String)logFieldMap.get("hour")), (String)logFieldMap.get("channel"), 
         (String)logFieldMap.get("engine"), (String)logFieldMap.get("status"), 0, "code"));
     }
     stat = (StatResult)this.cache.get(codeMd5);
     stat.setNum(stat.getNum() + 1);
 
     codeKey = new StringBuffer();
     codeKey.append((String)logFieldMap.get("hour")).append((String)logFieldMap.get("channel"));
     codeKey.append((String)logFieldMap.get("engine")).append((String)logFieldMap.get("pageType"));
     codeKey.append((String)logFieldMap.get("date")).append("page");
     codeMd5 = MD5.md5(codeKey.toString());
     if (!this.cache.containsKey(codeMd5)) {
       this.cache.put(codeMd5, 
         new StatResult(codeMd5, (String)logFieldMap.get("date"), 
         Integer.parseInt((String)logFieldMap.get("hour")), (String)logFieldMap.get("channel"), 
         (String)logFieldMap.get("engine"), (String)logFieldMap.get("pageType"), 0, "page"));
     }
     stat = (StatResult)this.cache.get(codeMd5);
     stat.setNum(stat.getNum() + 1);
   }
 }
