 package com.kuxun.storm.topology;
 
 import backtype.storm.Config;
 import backtype.storm.LocalCluster;
 import backtype.storm.StormSubmitter;
 import backtype.storm.topology.TopologyBuilder;
 import backtype.storm.tuple.Fields;
 import com.kuxun.storm.basic.StringScheme;
 import com.kuxun.storm.bolt.PersistBolt;
 import com.kuxun.storm.bolt.SplitBolt;
 import com.kuxun.storm.spout.KestrelSpout;
 import java.util.Arrays;
 import java.util.List;
 
 public class CrawlerTopology
 {
   public static void main(String[] args)
     throws Exception
   {
     TopologyBuilder builder = new TopologyBuilder();
 
     Config conf = new Config();
     conf.setDebug(false);
 
     if (args.length == 4) {
       System.out.println("########################################");
       System.out.println("########## Submit to Storm    ##########");
       System.out.println("########################################");
 
       String jobname = args[2];
       String queuename = args[3];
       String[] hosts = args[0].split(",");
       List<String> hostList = Arrays.asList(hosts);
       builder.setSpout("spout", new KestrelSpout(hostList, Integer.parseInt(args[1]), queuename, new StringScheme()), Integer.valueOf(3));
       builder.setBolt("split", new SplitBolt(), Integer.valueOf(4)).shuffleGrouping("spout");
       builder.setBolt("persist", new PersistBolt(), Integer.valueOf(4)).fieldsGrouping("split", new Fields(new String[] { "groupkey" }));
 
       conf.setNumWorkers(16);
       StormSubmitter.submitTopology(jobname, conf, builder.createTopology());
     } else {
       LocalCluster cluster = new LocalCluster();
 
       System.out.println("########################################");
       System.out.println("########## LocalCluster Start ##########");
       System.out.println("########################################");
       String[] hosts = "60.28.214.100".split(",");
       List<String> hostList = Arrays.asList(hosts);
       builder.setSpout("spout", new KestrelSpout(hostList, 22133, "crawler", new StringScheme()), Integer.valueOf(3));
 
       builder.setBolt("split", new SplitBolt(), Integer.valueOf(4)).shuffleGrouping("spout");
       builder.setBolt("persist", new PersistBolt(), Integer.valueOf(4)).fieldsGrouping("split", new Fields(new String[] { "groupkey" }));
 
       cluster.submitTopology("crawler", conf, builder.createTopology());
     }
   }
 }
