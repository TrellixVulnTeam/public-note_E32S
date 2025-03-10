package MultiThread;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Logger;

public class EchoProtocol implements Runnable {

	private static final int BUFSIZE = 32;
	private final Socket clntSock;
	private final Logger logger;
	
	public EchoProtocol(Socket clntSock, Logger logger){
		this.clntSock = clntSock;
		this.logger = logger;
	}
	
	
	//静态方法，该方法为单实例的
	public static void handleEchoClient(Socket clntSock, Logger logger) {
		
		try{
			InputStream in = clntSock.getInputStream();
			OutputStream out = clntSock.getOutputStream();
			
			int recvMsgSize = 0;
			int totalBteEchoed = 0;
			byte[] echoBuffer = new byte[BUFSIZE];
			
			while((recvMsgSize = in.read(echoBuffer))!=-1){
				out.write(echoBuffer, 0, recvMsgSize);
				totalBteEchoed += recvMsgSize;
			}
			
			logger.info("Client " + clntSock.getRemoteSocketAddress() + ", echoed " + totalBteEchoed + " bytes.");	
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	
	@Override
	public void run() {
		handleEchoClient(this.clntSock, this.logger);
	}

}
