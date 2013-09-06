package com.kuxun.storm.basic;

import backtype.storm.spout.Scheme;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class StringScheme implements Scheme {
  public List<Object> deserialize(byte[] bytes)
  {
    try
    {
      return new Values(new Object[] { new String(bytes, "UTF-8") }); 
    } catch (UnsupportedEncodingException e) {
    	throw new RuntimeException(e);
    }
  }

  public Fields getOutputFields()
  {
    return new Fields(new String[] { "str" });
  }
}
