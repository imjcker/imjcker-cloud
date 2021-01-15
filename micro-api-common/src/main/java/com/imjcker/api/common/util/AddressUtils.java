package com.imjcker.api.common.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import javax.servlet.ServletRequest;

public class AddressUtils {

	private  AddressUtils() {
	}

	public static String getLocalIP() {
		try{
			Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
			while (netInterfaces.hasMoreElements()) {
				NetworkInterface netInterface = netInterfaces.nextElement();

				if (netInterface.isLoopback() || netInterface.isVirtual() || !netInterface.isUp()) {
					continue;
				} else {
					Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
					while (addresses.hasMoreElements()) {
						InetAddress ip = addresses.nextElement();
						if (ip != null && ip instanceof Inet4Address) {

								return ip.getHostAddress();
						}
					}
				}
			}
		}catch (SocketException e) {

		}

		return null;
	}

	public static int getPort(ServletRequest request){
		return request.getServerPort();
	}
}
