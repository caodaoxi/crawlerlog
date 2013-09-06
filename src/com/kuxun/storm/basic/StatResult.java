package com.kuxun.storm.basic;

import java.io.Serializable;

public class StatResult
  implements Serializable
{
  private String md5 = null;
  private String date = null;
  private int hour = 0;
  private String channel = null;
  private String engine = null;
  private String type = null;
  private long num = 0L;
  private String talbetype = null;

  public StatResult()
  {
  }

  public StatResult(String md5, String date, int hour, String channel, String engine, String type, long num, String talbetype)
  {
    this.md5 = md5;
    this.date = date;
    this.hour = hour;
    this.channel = channel;
    this.engine = engine;
    this.type = type;
    this.num = num;
    this.talbetype = talbetype;
  }

  public String getMd5() {
    return this.md5;
  }

  public void setMd5(String md5) {
    this.md5 = md5;
  }

  public String getDate() {
    return this.date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public int getHour() {
    return this.hour;
  }

  public void setHour(int hour) {
    this.hour = hour;
  }

  public String getChannel() {
    return this.channel;
  }

  public void setChannel(String channel) {
    this.channel = channel;
  }

  public String getEngine() {
    return this.engine;
  }

  public void setEngine(String engine) {
    this.engine = engine;
  }

  public String getType() {
    return this.type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public long getNum() {
    return this.num;
  }

  public void setNum(long num) {
    this.num = num;
  }

  public String getTalbetype() {
    return this.talbetype;
  }

  public void setTalbetype(String talbetype) {
    this.talbetype = talbetype;
  }

  public String toString()
  {
    return "StatResult [md5=" + this.md5 + ", date=" + this.date + ", hour=" + this.hour + 
      ", channel=" + this.channel + ", engine=" + this.engine + ", type=" + 
       this.type + ", num=" + this.num + ", talbetype=" + this.talbetype + "]";
  }
}
