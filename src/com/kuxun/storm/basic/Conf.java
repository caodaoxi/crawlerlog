package com.kuxun.storm.basic;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Conf
{
  private static Properties pps = null;

  static { InputStream in = Conf.class.getClassLoader().getResourceAsStream("layout.properties");
    pps = new Properties();
    try {
      pps.load(in);
    } catch (IOException e) {
      e.printStackTrace();
    } }

  public static String getProperty(String key)
  {
    return pps.getProperty(key);
  }

  public static void main(String[] args)
  {
  }
}
