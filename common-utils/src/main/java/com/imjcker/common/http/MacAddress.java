package com.imjcker.common.http;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * @author thh 2018-11-30
 * @version 1.0.0
 * description:
 **/
public class MacAddress {
    /**
     * get current OS MAC address
     *
     * @return standard MAC address as string
     */
    public static String getMac() {
        InetAddress inetAddress;
        try {
            inetAddress = InetAddress.getLocalHost();
            byte[] mac = NetworkInterface.getByInetAddress(inetAddress).getHardwareAddress();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                if (i != 0) {
                    sb.append("-");
                }
                // mac[i] & 0xFF 是为了把byte转化为正整数
                String s = Integer.toHexString(mac[i] & 0xFF);
                sb.append(s.length() == 1 ? 0 + s : s);
            }
            return sb.toString().toUpperCase();
        } catch (UnknownHostException | SocketException e) {
            System.err.println("获取Mac地址失败.");
            e.printStackTrace();
        }
        return "34-17-EB-C2-46-7F";
    }
}
