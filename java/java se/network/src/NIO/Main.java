package NIO;

import java.io.IOException;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class Main {

	public static void TestClient() throws Exception{
		
		String send = "";
		int totalBytesRcvd = 0;
		int bytesRcvd = 0;
		
		SocketChannel clntChan = SocketChannel.open();
		
		clntChan.configureBlocking(false);
		
		ByteBuffer writeBuf = ByteBuffer.wrap(send.getBytes());
		ByteBuffer readBuf  = ByteBuffer.allocate(send.length());
		while(totalBytesRcvd < send.length()){
			if(writeBuf.hasRemaining()){
				clntChan.write(writeBuf);
			}
			
			if((bytesRcvd = clntChan.read(readBuf)) == -1){
				throw new SocketException("Connection closed prematurely");
			}
			
			totalBytesRcvd += bytesRcvd;
			System.out.print(".");
		}
	}
	
	public static void main(String[] args) throws Exception {
		TestClient();
	}

}
