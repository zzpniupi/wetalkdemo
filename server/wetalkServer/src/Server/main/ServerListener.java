package Server.main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerListener extends Thread{

	private ServerSocket serverSocket;

	@Override
	public void run() {
		try {
			serverSocket = new ServerSocket(27777, 27);		//������������еĳ�����Ϊ 3/27������ζ�ŵ����������� 3 ����������ʱ����� Client ���������ӣ��ͻᱻ Server�ܾ�����Ϊ�����������Ѿ����ˡ�����ʹ�õ� serverSocket.accept()�������ǴӶ�����ȡ���������� 
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

