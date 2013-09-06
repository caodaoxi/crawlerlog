package com.kuxun.storm.utils;

import com.kuxun.storm.basic.Conf;
import com.kuxun.storm.basic.StatResult;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class DBUtils
{
  private static Connection con = null;

  public static boolean batchInsertToMysql(List<String[]> list) { boolean flag = false;
    Statement st = null;
    String sql = null;
    try {
      con = getConnection();
      st = con.createStatement();
      for (String[] fields : list) {
        sql = "insert into realtime values ('" + fields[0] + "','" + fields[1] + "','" + fields[2] + "','" + fields[3] + "')";
        st.addBatch(sql);
      }
      int[] num = st.executeBatch();
      if ((num != null) && (num.length > 0))
        flag = true;
    }
    catch (SQLException e) {
      e.printStackTrace();
    } finally {
      close(con, st);
    }
    return flag;
  }

  public static Connection getConnection() {
    Connection connection = null;
    String prefix = "";
    try {
      Class.forName("com.mysql.jdbc.Driver");
      InetAddress address = InetAddress.getLocalHost();
      String hostName = address.getHostName();
      if ("kuxun".equals(hostName)) {
        prefix = hostName + ".";
      }
      connection = DriverManager.getConnection(Conf.getProperty(prefix + "host"), Conf.getProperty(prefix + "username"), Conf.getProperty(prefix + "password"));
    } catch (UnknownHostException e) {
      e.printStackTrace();
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    return connection;
  }

  public static void close(Statement st) {
    try {
      st.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public static void close(Connection con, Statement st) {
    try {
      if (st != null) {
        st.close();
      }
      if (con != null)
        con.close();
    }
    catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public static boolean insertOrUpdate(StatResult stat) {
    boolean flag = false;
    Statement st = null;
    String sql = null;
    String insertSql = null;
    int count = 0;
    try {
      con = getConnection();
      st = con.createStatement();
      ResultSet rs = null;
      String tableName = null;
      if ("code".equals(stat.getTalbetype())) {
        tableName = "seo_spider_code_monitor";
        insertSql = "insert into seo_spider_code_monitor(`date`,`hour`,channel,engine,code_type,`num`, md5code) values('" + 
          stat.getDate() + "'," + stat.getHour() + ",'" + stat.getChannel() + "','" + 
          stat.getEngine() + "'," + stat.getType() + "," + stat.getNum() + ",'" + stat.getMd5() + "')";
      } else {
        tableName = "seo_spider_page_monitor";
        insertSql = "insert into seo_spider_page_monitor(`date`,`hour`,channel,engine,page_type,`num`, md5code) values('" + 
          stat.getDate() + "'," + stat.getHour() + ",'" + stat.getChannel() + "','" + 
          stat.getEngine() + "','" + stat.getType() + "'," + stat.getNum() + ",'" + stat.getMd5() + "')";
      }
      sql = "select md5code, num from " + tableName + " where md5code='" + stat.getMd5() + "'";
      rs = st.executeQuery(sql);
      if (rs.next()) {
        sql = "update " + tableName + " set num = " + (stat.getNum() + rs.getLong("num")) + " where md5code='" + stat.getMd5() + "'";
        count = st.executeUpdate(sql);
      } else {
        count = st.executeUpdate(insertSql);
      }
      if (count > 0)
        flag = true;
    }
    catch (SQLException e) {
      System.out.println(sql);
      System.out.println(insertSql);
      e.printStackTrace();
    } finally {
      close(con, st);
    }
    return flag;
  }
}
