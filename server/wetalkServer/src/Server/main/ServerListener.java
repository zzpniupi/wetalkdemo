package Server.main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerListener extends Thread{

	private ServerSocket serverSocket;

	@Override
	public void run() {
		try {
			serverSocket = new ServerSocket(27777, 27);		//把连接请求队列的长度设为 3/27。这意味着当队列中有了 3 个连接请求时，如果 Client 再请求连接，就会被 Server拒绝，因为服务器队列已经满了。我们使用的 serverSocket.accept()方法就是从队列中取出连接请求。 
			while (true) {
				Socket socket = serverSocket.accept();
				System.out.println("someone connect in!");
				ChatSocket chatSocket = new ChatSocket(socket);
				chatSocket.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

