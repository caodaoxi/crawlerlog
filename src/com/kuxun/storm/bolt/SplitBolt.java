package com.kuxun.storm.bolt;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import com.kuxun.storm.utils.LogUtils;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;

public class SplitBolt extends CommonBolt
{
  private static final long serialVersionUID = 1L;
  private OutputCollector collector = null;
  private HashMap<String, String> logFieldMap;
  private HashMap<String, String> tempFieldMap;

  public void cleanup()
  {
  }

  public void declareOutputFields(OutputFieldsDeclarer declarer)
  {
    declarer.declare(new Fields(new String[] { "groupkey", "logFieldMap" }));
  }

  public Map<String, Object> getComponentConfiguration()
  {
    return null;
  }

  public void prepare(Map stormConf, TopologyContext context, OutputCollector collector)
  {
    this.collector = collector;
  }

  public void execute(Tuple input)
  {
    String line = input.getString(0);
    this.logFieldMap = new HashMap<String, String>();
    try
    {
      String[] fields = null;
      int len = -1;
      if (line.endsWith("hotel")) {
        fields = line.split("\t");
        len = fields.length;
        this.logFieldMap.put("ip", fields[1]);
        this.logFieldMap.put("channel", fields[(len - 1)]);
        this.logFieldMap.put("status", fields[6]);
        extractDateAndHour(fields[4].trim());
        this.logFieldMap.put("engine", getUAType(fields[9]));
      } else if (line.endsWith("jipiao")) {
        fields = line.split("\\s+");
        len = fields.length;
        this.logFieldMap.put("ip", fields[0].trim());
        this.logFieldMap.put("channel", fields[(len - 1)].trim());

        this.logFieldMap.put("status", fields[8].trim());
        extractDateAndHour(fields[3].trim());
        this.logFieldMap.put("engine", getUAType(line));
        if (((String)this.logFieldMap.get("status")).indexOf("-") != -1)
          throw new Exception("status is null");
      }
      else
      {
        throw new Exception("channel is error");
      }

      if ("hotel".equals(this.logFieldMap.get("channel")))
        this.logFieldMap.put("pageType", getHotelPageType(cutUrl(fields[5].trim())));
      else if ("jipiao".equals(this.logFieldMap.get("channel")))
        this.logFieldMap.put("pageType", getJipiaoPageType(cutUrl(fields[6].trim())));
    }
    catch (Exception e) {
      log.error("[" + e.getMessage() + "] " + line);
      return;
    }
    this.collector.emit(new Values(new Object[] { (String)this.logFieldMap.get("channel") + (String)this.logFieldMap.get("engine"), this.logFieldMap }));
    this.collector.ack(input);
  }

  private String cutUrl(String url) throws Exception {
    int index = -1;

    if (url.indexOf(" ") != -1) {
      url = url.split(" ")[1];
    }

    if ((index = url.indexOf("?")) != -1) {
      url = url.substring(0, index);
    }
    if (url.endsWith("jpg")) {
      throw new Exception("url is jpg");
    }
    return url;
  }

  private String getJipiaoPageType(String url)
  {
    if ("/".equals(url))
      return "homepage";
    if ("".equals(url.trim()))
      return "other";
    if (Pattern.compile("^/tejia-(\\w+)-(\\w+).html$").matcher(url).matches())
    {
      return "tejia_line";
    }if (Pattern.compile("^/(\\w+)-(\\w+)-guoji.html$").matcher(url).matches())
      return "guoji";
    if (Pattern.compile("^/(\\w+)-(\\w+).html$").matcher(url).matches())
      return "line";
    if (Pattern.compile("^/tejia-(\\w+).html$").matcher(url).matches())
      return "tejia_city";
    if (Pattern.compile("^/shike-(.*).html$").matcher(url).matches())
      return "shike";
    if (Pattern.compile("^/airport/(.*)$").matcher(url).matches())
      return "airport";
    if (Pattern.compile("^(.*)zixun(.*)$").matcher(url).matches())
      return "zixun";
    if (Pattern.compile("^/ask(.*)-(\\d+)-index.html$").matcher(url).matches()) {
      return "ask";
    }
    log.error("jipiao:" + url);
    return "other";
  }

  private String getHotelPageType(String url) throws Exception {
    if ("/".equals(url))
      return "homepage";
    if ("".equals(url.trim()))
      return "other";
    if (Pattern.compile("^/(\\w+)-jiudian-daquan.html$").matcher(url).matches())
      return "city_home";
    if (Pattern.compile("^/(\\w+)-jiudian-daquan-p(\\d+).html$").matcher(url).matches())
      return "city_p";
    if (Pattern.compile("^/(\\w+)-jiudian-daquan-p(\\d+).html$").matcher(url).matches())
      return "dibiao_home";
    if (Pattern.compile("^/(\\w+)-jiudian-daquan-p(\\d+).html$").matcher(url).matches())
      return "dibiao_otherp";
    if (Pattern.compile("^/(.*)-jiudian-daquan-p(\\d+)(.*).html$").matcher(url).matches())
      return "other_result_p";
    if (Pattern.compile("^/(.*)-jiudian-daquan(.*).html$").matcher(url).matches())
      return "other_result_home";
    if (Pattern.compile("^/(.*)-jiudian.html$").matcher(url).matches())
      return "detail_home";
    if (Pattern.compile("^/(.*)-jiudian-jiage.html$").matcher(url).matches())
      return "detail_jiage";
    if (Pattern.compile("^/(.*)-jiudian-tupian.html$").matcher(url).matches())
      return "detail_tupian";
    if (Pattern.compile("^/(.*)-jiudian-(ditu|map).html$").matcher(url).matches())
      return "detail_ditu";
    if (Pattern.compile("^/(.*)-jiudian-wenti(.*).html$").matcher(url).matches())
      return "detail_wenti";
    if (Pattern.compile("^/(\\w+-){1,2}(\\w+)-jiudian-dianping.html$").matcher(url).matches())
      return "detail_pinglun_home";
    if (Pattern.compile("^/(\\w+-){1,2}(\\w+)-jiudian-dianping-p(\\d+).html$").matcher(url).matches())
      return "detail_pinglun_p";
    if (Pattern.compile("^/(\\w+-){1,2}(\\w+)-(\\d+)-jiudian-dianping.html$").matcher(url).matches())
      return "single_pinglun_home";
    if (Pattern.compile("^/(\\w+-){1,2}(\\w+)-(\\d+)-jiudian-dianping-p(\\d+).html$").matcher(url).matches())
      return "single_pinglun_p";
    if (Pattern.compile("^/jiudianwenda-(\\d+).html$").matcher(url).matches())
      return "single_wenda";
    if (Pattern.compile("^/(.*)-jiudian-pinpai(.*).html$").matcher(url).matches())
      return "pinpai";
    if (Pattern.compile("^/(.*)-jiudian-effect(.*).html$").matcher(url).matches()) {
      return "effect";
    }
    log.error("hotel:" + url);
    return "other";
  }

  private String getUAType(String ua) throws Exception {
    if (ua != null) {
      if (ua.indexOf("Baiduspider") != -1)
        return "baidu";
      if (ua.indexOf("Googlebot/2.1") != -1)
        return "google";
      if (ua.indexOf("360Spider") != -1) {
        return "360";
      }
      return "-";
    }

    throw new Exception("ua type error");
  }

  private void extractDateAndHour(String value) {
    this.tempFieldMap = new HashMap<String, String>();

    String currentTime = LogUtils.rltrim(value, new String[] { "[", "]" }).split(" ")[0];
    long dt = LogUtils.parseToTime(currentTime);
    String date = LogUtils.parseToDate(dt, "yyyy-MM-dd");
    String dtime = LogUtils.parseToDate(dt, "HH");

    this.tempFieldMap.put("date", date);
    this.tempFieldMap.put("hour", dtime);
    this.logFieldMap.putAll(this.tempFieldMap);
  }
}
