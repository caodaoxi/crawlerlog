package com.kuxun.storm.basic;

import java.io.Serializable;

public class ThreadS extends Thread implements Serializable{
	
	public ThreadS(Runnable runa){
		super(runa);
	}
	public ThreadS(){
		
	};
}
