package BasicSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class Main {
	
	public static void Test1() throws Exception{
		Enumeration<NetworkInterface> interfaceList = NetworkInterface.getNetworkInterfaces();
		
		if(interfaceList == null){
			System.out.println("--No interfaces found--");
		}else{
			while(interfaceList.hasMoreElements()){
				NetworkInterface iface = interfaceList.nextElement();
				System.out.println("Interface " + iface.getName() + ":");
				Enumeration<InetAddress> addrList = iface.getInetAddresses();
				if(addrList == null){
					System.out.println("\t(No addresses for this interface)");
				}
				while(addrList.hasMoreElements()){
					InetAddress address = addrList.nextElement();
					System.out.println("\tAddress " + address.getHostAddress());
				}
			}
		}
	}
	
	public static void Test2(String[] args) throws Exception{
		for(String host : args){
			System.out.println(host + ":");
			InetAddress[] addressList = InetAddress.getAllByName(host);
			for(InetAddress address : addressList){
				System.out.println("\t"+address.toString()+"|"+address.getHostName()+"/"+address.getHostAddress());
			}
		}		
	}
	
	public static void TcpClient() throws Exception{
		Socket socket = new Socket("localhost", 1020);
		
		InputStream in = socket.getInputStream();
		OutputStream out = socket.getOutputStream();
		
		out.write("feiqilai...".getBytes());
		byte[] rec = new byte[1024];
		int length = in.read(rec);
		System.out.println(new String(rec));
		socket.close();
//		socket.shutdownOutput();
	}
	
	public static void TcpServer() throws Exception{
		ServerSocket servSock = new ServerSocket(1020);
		
		while(true){
			Socket clntSock  = servSock.accept();
			
			SocketAddress clientAddress = clntSock.getRemoteSocketAddress();
			System.out.println("Handling client at : " + clientAddress);
			InputStream in = clntSock.getInputStream();
			OutputStream out = clntSock.getOutputStream();
			byte[] receive = new byte[1024];
			int recSize = 0;
			while((recSize = in.read(receive)) != -1){
				out.write(receive, 0, recSize);
			}
			System.out.println(clientAddress+" close");
			clntSock.close();
		}
	}
	
	public static void UdpServer() throws Exception{
		DatagramSocket socket = new DatagramSocket(1030);	//与1030端口绑定
		DatagramPacket recPacket = new DatagramPacket(new byte[1024], 1024);
		
		while(true){
			socket.receive(recPacket);
			System.out.println("Handling client at " + recPacket.getAddress().getHostAddress());
			socket.send(recPacket);			//
			recPacket.setLength(1024);
		}
	}
	
	public static void UdpClient() throws Exception{
		DatagramSocket socket = new DatagramSocket();		//与默认端口绑定
		DatagramPacket sendPacket = new DatagramPacket("nihaoa".getBytes(), "nihaoa".getBytes().length, InetAddress.getByName("localhost"), 1030);
		DatagramPacket recPacket = new DatagramPacket(new byte[1024], 1024);
		
		socket.send(sendPacket);
		socket.receive(recPacket);
		
		System.out.println("Received: " + new String(recPacket.getData()));
	}
	
	public static void main(String[] args) throws Exception{
		//Test1();
		//Test2(new String[]{"localhost", "www.baidu.com"});
		
		TcpClient();
		//TcpServer();
		//UdpClient();
		System.out.println("Main Done");
	}
}
