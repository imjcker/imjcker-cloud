package com.imjcker.api.handler.plugin.resourceCache.impl;

import java.util.ArrayList;
import java.util.List;


public class ResourceUtils {
	public final static String HOST_DELIMINITER = ",";
	public final static String PORT_DELIMINITER = ":";

	public static List<Addr> getAddress(String serialzedHosts,int defaultPort){
		List<Addr> addrs = new ArrayList<Addr>();
		final String[] hosts = serialzedHosts.split("[" + HOST_DELIMINITER + "]");
        for (final String host : hosts) {
            if (!host.isEmpty()) {
                String[] brokenUp = host.split(PORT_DELIMINITER);
                Addr addr = new Addr(brokenUp[0], brokenUp.length == 2 ? Integer.parseInt(brokenUp[1]) : defaultPort);
                addrs.add(addr);
            }
        }
        return addrs;
	}

	public static class Addr{
		private String host;
		private int port;

		Addr(String host,int port){
			this.host = host;
			this.port = port;
		}

		public String getHost() {
			return host;
		}

		public int getPort() {
			return port;
		}
	}

}
