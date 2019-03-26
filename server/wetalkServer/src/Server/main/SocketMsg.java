package Server.main;

public class SocketMsg {

	private ChatSocket chatSocket;
	private int userID;
	
	public SocketMsg(ChatSocket chatSocket, int userID) {
		this.chatSocket = chatSocket;
		this.userID = userID;
	}
	

	public int getUserID() {
		return userID;
	}


	public void setUserID(int userID) {
		this.userID = userID;
	}


	public ChatSocket getChatSocket() {
		return chatSocket;
	}
	public void setChatSocket(ChatSocket chatSocket) {
		this.chatSocket = chatSocket;
	}
	
}

