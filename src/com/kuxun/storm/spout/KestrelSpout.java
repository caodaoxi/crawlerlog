package com.kuxun.storm.spout;

import backtype.storm.spout.RawScheme;
import backtype.storm.spout.Scheme;
import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichSpout;
import backtype.storm.topology.OutputFieldsDeclarer;

import java.io.*;
import java.util.Map;
import java.util.List;
import backtype.storm.tuple.Fields;
import backtype.storm.utils.Utils;
import java.util.ArrayList;
import java.util.Arrays;


import org.apache.log4j.Logger;


import backtype.storm.tuple.Values;

/**
 * This spout can be used to consume messages in a reliable way from a cluster 
 * of Kestrel servers. It is recommended that you set the parallelism hint to a
 * multiple of the number of Kestrel servers, otherwise the read load will be
 * higher on some Kestrel servers than others.
 */


public class KestrelSpout implements IRichSpout {
    public static Logger LOG = Logger.getLogger(KestrelSpout.class);

    public static final long BLACKLIST_TIME_MS = 1000;// * 60;

    private List<String> _hosts = null;
    private int _port = -1;
    private String _queueName = null;
    private SpoutOutputCollector _collector;
    private Scheme _scheme;

    private List<KestrelClientInfo> _kestrels;
    private int _emitIndex;

    private static class KestrelSourceId {
        public KestrelSourceId(int index, int id) {
            this.index = index;
            this.id = id;
        }
        
        int index;
        int id;
    }
    
    private static class KestrelClientInfo {
        public KestrelClient client;
        public Long blacklistTillTimeMs;
        public String host;
        public int port;
        
        public KestrelClientInfo(String host, int port) {
            this.host = host;
            this.port = port;
            this.blacklistTillTimeMs = 0L;
            this.client  = null;
        }
    }
    
    public KestrelSpout(List<String> hosts, int port, String queueName, Scheme scheme) {
        if(hosts.isEmpty()) {
            throw new IllegalArgumentException("Must configure at least one host");
        }
        _port = port;
        _hosts = hosts;
        _queueName = queueName;
        _scheme = scheme;
    }
    
    public KestrelSpout(String host, int port, String queue, Scheme scheme) {
        this(Arrays.asList(host), port, queue, scheme);
    }

    public KestrelSpout(String host, int port, String queue) {
        this(host, port, queue, new RawScheme());
    }
    
    public KestrelSpout(List<String> hosts, int port, String queue) {
        this(hosts, port, queue, new RawScheme());
    }
    
    public KestrelSpout() {
    }

    public Fields getOutputFields() {
       return _scheme.getOutputFields();
    }
    
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        _collector = collector;
        _emitIndex = 0;
        _kestrels = new ArrayList<KestrelClientInfo>();
        int numTasks = context.getComponentTasks(context.getThisComponentId()).size();
        int myIndex = context.getThisTaskIndex();
        int numHosts = _hosts.size();
        if(numTasks < numHosts) {
            for(String host: _hosts) {
                _kestrels.add(new KestrelClientInfo(host, _port));
            }
        } else {
            String host = _hosts.get(myIndex % numHosts);
            _kestrels.add(new KestrelClientInfo(host, _port));
        }
    }

    public void close() {
        try {
            for(KestrelClientInfo info: _kestrels) {
                if(info.client!=null) {
                    info.client.close();
                }
            }
        } catch( IOException e ) {
            throw new RuntimeException(e);
        }
        _kestrels.clear();
    }

    public void nextTuple() {
        long now = System.currentTimeMillis();
        boolean success = false;
        
        for(int i=0; i<_kestrels.size(); i++) {
            int index = (_emitIndex + i) % _kestrels.size();
            KestrelClientInfo info = _kestrels.get(index);
            
            if(now > info.blacklistTillTimeMs) {
                try {
                    if(info.client==null) {
                        info.client = new KestrelClient(info.host, info.port);
                    }
                    KestrelClient.Item item = info.client.dequeue(_queueName);                    
                    if(item!=null) {                        
                    	String line = new String(item._data);
                        _collector.emit(new Values(line));
                        
                        success = true;
                        _emitIndex = index;
                        
                        break;
                    } else {
                    	try {
                    		Thread.sleep(100);
                    	} catch(Exception ex) {
                    		//
                    	}
                    }
                } catch(IOException ioe) {
                    blacklist(info, ioe);
                } catch(KestrelClient.ParseError e) {
                    blacklist(info, e);
                }
            }
        }
        // �����´���ѯ����ʼ����Ϊ�շ��ʹ����һ������
        _emitIndex = (_emitIndex + 1) % _kestrels.size();
        if(!success) {
            Utils.sleep(10);
        }
    }
    
    private void blacklist(KestrelClientInfo info, Throwable t) {
        LOG.warn("Failed to read from Kestrel at " + info.host + ":" + info.port, t);
        try {
          //this case can happen when it fails to connect to Kestrel (and so never stores the connection)
          if(info.client!=null) {
            info.client.close();
          }
        } catch (IOException ex) {
            LOG.warn("Failed to close Kestrel client at " + info.host + ":" + info.port, t);
        }
        info.client = null;
        info.blacklistTillTimeMs = System.currentTimeMillis() + BLACKLIST_TIME_MS;
    }

    public void ack(Object msgId) {
//        KestrelSourceId sourceId = (KestrelSourceId) msgId;
//        KestrelClientInfo info = _kestrels.get(sourceId.index);
//        
//        //if the transaction didn't exist, it just returns false. so this code works
//        //even if client gets blacklisted, disconnects, and kestrel puts the item 
//        //back on the queue
//        try {
//            if(info.client!=null) {
//                info.client.ack(_queueName, sourceId.id);
//            }
//        } catch(IOException ioe) {
//            blacklist(info, ioe);
//            System.out.println("######## Ack #######################");
//            System.out.println("######## IOException ###############");
//            System.out.println("####################################");
//        } catch(KestrelClient.ParseError e) {
//            blacklist(info, e);
//            System.out.println("######## Ack #######################");
//            System.out.println("######## KestrelClient.ParseError ##");
//            System.out.println("####################################");
//        }
    }
    
    
    public void fail(Object msgId) {
//        KestrelSourceId sourceId = (KestrelSourceId) msgId;
//        KestrelClientInfo info = _kestrels.get(sourceId.index);
//        
//        // see not above about why this works with blacklisting strategy
//        try {
//            if(info.client!=null) {
//                info.client.fail(_queueName, sourceId.id);
//            }
//        } catch(IOException ioe) {
//            blacklist(info, ioe);
//            System.out.println("######## Fail ######################");
//            System.out.println("######## IOException ###############");
//            System.out.println("####################################");
//        } catch(KestrelClient.ParseError e) {
//            blacklist(info, e);
//            System.out.println("######## Fail ######################");
//            System.out.println("######## KestrelClient.ParseError ##");
//            System.out.println("####################################");
//        }
    }

    public boolean isDistributed() {
        return true;
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(getOutputFields());
        //HOHO debug
    	//declarer.declare(new Fields("word"));
    	//System.out.println("######## declarer.declare(new Fields()) ########");
    }

	@Override
	public void activate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deactivate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

}

