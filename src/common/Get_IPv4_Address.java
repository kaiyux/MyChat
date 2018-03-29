package common;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class Get_IPv4_Address {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		getIPv4();
	}

	public static InetAddress getIPv4() {
		Enumeration allNetInterfaces = null;
		try {
			allNetInterfaces = NetworkInterface.getNetworkInterfaces();
		} catch (java.net.SocketException e) {
			e.printStackTrace();
		}
		InetAddress ip = null;
		while (allNetInterfaces.hasMoreElements()) {
			NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
			System.out.println(netInterface.getName());
			Enumeration addresses = netInterface.getInetAddresses();
			while (addresses.hasMoreElements()) {
				ip = (InetAddress) addresses.nextElement();
				if (ip != null && ip instanceof Inet4Address) {
					if (ip.getHostAddress().equals("127.0.0.1")) {
						continue;
					}
					System.out.println("/u672c/u673a/u7684IP = " + ip.getHostAddress());
					return ip;
				}
			}
		}
		System.out.println("网络无连接！");
		return ip;
	}
}
