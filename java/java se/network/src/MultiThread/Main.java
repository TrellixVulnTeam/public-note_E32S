package MultiThread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class Main {
	
	public static void TcpServer1() throws Exception{
		ServerSocket servSock = new ServerSocket(1000);
		Logger logger = Logger.getLogger("practical");
		
		while(true){
			Socket clntSock = servSock.accept();
			Thread thread = new Thread(new EchoProtocol(clntSock, logger));
			thread.start();
			logger.info("Created and Started Thread " + thread.getName());
		}
	}
	
	public static void TcpServerPool() throws Exception{
		final ServerSocket servSock = new ServerSocket(1000);
		final Logger logger = Logger.getLogger("practical");
		
		for(int i=0; i<10; i++){
			Thread thread = new Thread(){
				@Override
				public void run(){
					Socket clntSock;
					try {
						while(true){
							clntSock = servSock.accept();						//线程得到socket
							EchoProtocol.handleEchoClient(clntSock, logger);	//处理客户端socket
						}
					} catch (IOException e) {e.printStackTrace();}
				}
			};
		}
	}
	
	public static void TcpServerExecutor() throws Exception{
		ServerSocket servSock = new ServerSocket(1000);
		Logger logger = Logger.getLogger("practical");
		
		Executor service = Executors.newCachedThreadPool();	//这个其实也是线程池
		while(true){
			Socket clntSock = servSock.accept();
			service.execute(new EchoProtocol(clntSock, logger));
		}
	}
	
	public static void main(String[] args) throws Exception {
		System.out.println("Main Done!");
	}
}
