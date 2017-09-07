package com.otsi.rti;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

public class ElasticSearchConnection {

	static TransportClient client;
	static String host="localhost";
	static int portNumber=9300;
	

	public static TransportClient connection() throws UnknownHostException{
	
		if(client ==null){
			client = new PreBuiltTransportClient(Settings.EMPTY)
					.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host), portNumber))
					.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host), portNumber));
		}
	 return client;
	}

}
