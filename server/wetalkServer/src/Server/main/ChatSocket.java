package Server.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Server.db.DBManager;
import Server.main.SocketMsg;
import Server.main.ChatManager;


public class ChatSocket extends Thread{
	private Socket socket;
	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWriter;
	private String message = null;
	private SocketMsg socketMsg;
	
	private Connection connection = DBManager.getDBManager().getConnection();
	
	public ChatSocket(Socket s) {
		this.socket = s;
		try {
			this.bufferedReader = new BufferedReader(new InputStreamReader(s.getInputStream(), "UTF-8"));
			this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(s.getOutputStream(), "UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	@Override
	public void run() {
		try {
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				if (!line.equals("-1")) {	
					message += line;
				} else {
					System.out.println("messages are: "+message);
					delMessage(message);
					line = null;
					message = null;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bufferedWriter.close();
				bufferedReader.close();
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void sendMsg(String msg) {
    	try {
    		while (socket == null) ;
            if (bufferedWriter != null) {
            	System.out.println("send :" + msg);
                bufferedWriter.write(msg + "\n");
                bufferedWriter.flush();
                bufferedWriter.write("-1\n");
                bufferedWriter.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	public void delMessage(String msg) {
		/*if (msg != null) {
			String action = getAction(msg);
			switch(action) {
				case "LOGIN": { dealLogin(msg); break; }
				case "REGISTER": { dealRegister(msg); break; }
				case "DRESSUP": { dealDressUp(msg); break; }
				case "GETDRESSUP": { dealGetDressUp(msg); break; }
				case "PROFILE": { dealProfile(msg); break; }
				case "GETPROFILE": { dealGetProfile(msg); break; }
				case "GETFRIENDLIST": { dealGetFriendList(msg); break; }
				case "GETGROUPLIST": { dealGetGroupList(msg); break; }
				case "GETFRIENDPROFILE": { dealGetFriendProfile(msg); break; }
				case "STATE": { dealState(msg); break; }
				case "CHATMSG": { dealChatMsg(msg); break; }
				case "USERLIST": { dealUserList(msg); break; }
				case "ADDFRIEND": { dealAddFriend(msg); break; }
				case "GROUPMEMBERLIST": { dealGroupMemberList(msg); break; }
				case "ADDGROUP": { dealAddGroup(msg); break; }
				case "GETALLGROUPLIST": { dealGetAllGroupList(msg); break;}
				default : dealError(); break;
			}
		}*/
		if(msg != null) {
			String action = getAction(msg);
			switch(action) {
			case "ADDFRIEND": {dealAddFriend(msg); break;}							//处理聊天信息
			case "CHATMSGS": {dealChatMsgs(msg); break;}							//处理聊天信息
			case "CHECKURMSGS": {dealCheckURMsgs(msg); break;}						//处理聊天信息
			case "GETCHATLIST": {dealGetChatList(msg); break;}						//获取聊天列表
			case "GETCHATMSGS": {dealGetChatMsgs(msg); break;}						//获取聊天记录（未实现）
			case "GETFRIENDLIST": { dealGetFriendList(msg); break; }				//获取好友列表
			case "GETFRIENDPROFILE": { dealGetFriendProfile(msg); break; }			//获取好友信息
			case "GETMSGSTYPE": { dealGetMsgsType(msg); break; }			
			case "GETUSERNAME": { dealGetUserName(msg); break; }				//获取用户名字
			case "REGISTER": {dealRegister(msg); break;}							//处理注册
			case "REGISTERSEND": {dealRegisterSend(msg); break;}					//发送验证短信（未实现）
			case "LOGIN": {dealLogin(msg); break;}								    //处理登录
			case "QUIT": {dealQuit(msg); break;}	
			}
		}
	}
	
	public String getAction(String msg) {			
        String p = "\\[(.*)\\]:";
        Pattern pattern = Pattern.compile(p);
        Matcher matcher = pattern.matcher(msg);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return "error";
        }
	}
	
	private void dealAddFriend(String msg) {
		int userID=0;
    	int friendID=0;
    	String p = "\\[ADDFRIEND\\]:\\[(.*), (.*)\\]";
    	Pattern pattern = Pattern.compile(p);
    	Matcher matcher = pattern.matcher(msg);
    	if (matcher.find()) {
    		userID = Integer.parseInt(matcher.group(1));
    		friendID = Integer.parseInt(matcher.group(2));
    	}
    	String sql = "INSERT INTO `friendlist`(`userID`, `friendID`) VALUES ("+userID+","+friendID+")";
    	try {
			Statement statement = connection.createStatement();
			boolean resultSet = statement.execute(sql);
			sendMsg("[ACKADDFRIEND]:[1]");
			System.out.println("add friend insert successfully!!");			
		} catch (SQLException e) {
			sendMsg("[ACKADDFRIEND]:[0]");
			e.printStackTrace();
		}
	}
	
	private void dealChatMsgs(String msg) {
		int userID=0;
    	int friendID=0;
    	//对接收者而言
    	int senderID=0;
    	int receiveID=0;
    	boolean flag = false;
    	String chatMsg = null;
    	String out = null;
    	String p = "\\[CHATMSGS\\]:\\[(.*), (.*), (.*)\\]";
    	Pattern pattern = Pattern.compile(p);
    	Matcher matcher = pattern.matcher(msg);
    	if (matcher.find()) {
    		userID = Integer.parseInt(matcher.group(1));
    		friendID = Integer.parseInt(matcher.group(2));
    		chatMsg = matcher.group(3);
    	}
		for (SocketMsg SocketMsg : ChatManager.getChatManager().socketList) {
			if (SocketMsg.getUserID()==friendID) {
				flag = true;
				senderID = userID;
				receiveID = friendID;
				out = "[TRANSMSGS]:[" + senderID + ", " + receiveID + ", " + chatMsg + "]";
				SocketMsg.getChatSocket().sendMsg(out);
				sendMsg("[ACKCHATMSGS]:[1]");
				//存储聊天记录
				String sql = "INSERT INTO `chatmsgs`(`senderID`, `receiveID`, `msgs`) VALUES ("+ userID +","+ friendID +",'"+ chatMsg +"')";
				try {
					Statement statement = connection.createStatement();
					boolean resultSet = statement.execute(sql);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		if(!flag) {
			//存储至未发送记录
			String sql = "INSERT INTO `uschatmsgs`(`senderID`, `receiveID`, `msgs`) VALUES ("+ userID +","+ friendID +",'"+ chatMsg +"')";
			String sql2 = "INSERT INTO `chatmsgs`(`senderID`, `receiveID`, `msgs`) VALUES ("+ userID +","+ friendID +",'"+ chatMsg +"')";
			try {
				Statement statement = connection.createStatement();
				boolean resultSet = statement.execute(sql);
				boolean resultSet2 = statement.execute(sql2);
				sendMsg("[ACKCHATMSGS]:[1]");
			} catch (SQLException e) {
				sendMsg("[ACKCHATMSGS]:[0]");
				e.printStackTrace();
			}
		}		
	}
	
	private void dealCheckURMsgs(String msg) {
		int userID=0;
		int friendID = 0;
    	String p = "\\[CHECKURMSGS\\]:\\[(.*), (.*)\\]";
    	Pattern pattern = Pattern.compile(p);
    	Matcher matcher = pattern.matcher(msg);
    	if (matcher.find()) {
    		userID = Integer.parseInt(matcher.group(1));
    		friendID = Integer.parseInt(matcher.group(2));
    	}
    	String out = null;
    	String sql = "SELECT msgs FROM `uschatmsgs` WHERE receiveID = " + userID + " AND senderID = " + friendID;
    	try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				out += "[ACKCHECKURMSGS]:[" + resultSet.getString(1) + "] ";
			}
			sendMsg(out);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void dealGetChatList(String msg) {
		int userID=0;
		String p = "\\[GETCHATLIST\\]:\\[(.*)\\]";
		Pattern pattern = Pattern.compile(p);
    	Matcher matcher = pattern.matcher(msg);
    	if (matcher.find()) {
    		userID = Integer.parseInt(matcher.group(1));
    	}
    	String out = null;
    	String sql = "SELECT DISTINCT receiveID FROM `chatmsgs` WHERE senderID = '" + userID + "';";
    	try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				out += "[ACKGETCHATLIST]:[" + resultSet.getString(1) + "] ";
			}
			sendMsg(out);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void dealGetChatMsgs(String msg) {
		int userID=0;
		int friendID = 0;
    	String p = "\\[GETCHATMSGS\\]:\\[(.*), (.*)\\]";
    	Pattern pattern = Pattern.compile(p);
    	Matcher matcher = pattern.matcher(msg);
    	if (matcher.find()) {
    		userID = Integer.parseInt(matcher.group(1));
    		friendID = Integer.parseInt(matcher.group(2));
    	}
    	String out = null;
    	String sql = "SELECT msgs FROM `chatmsgs` WHERE (senderID= "+userID+" AND receiveID="+ friendID+ ") OR (senderID="+ friendID+ " AND receiveID="+ userID+ ") " ;
    	try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				out += "[ACKGETCHATMSGS]:[" + resultSet.getString(1) + "] ";
			}
			sendMsg(out);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void dealGetMsgsType(String msg) {
		int userID=0;
		int friendID = 0;
    	String p = "\\[GETMSGSTYPE\\]:\\[(.*), (.*)\\]";
    	Pattern pattern = Pattern.compile(p);
    	Matcher matcher = pattern.matcher(msg);
    	if (matcher.find()) {
    		userID = Integer.parseInt(matcher.group(1));
    		friendID = Integer.parseInt(matcher.group(2));
    	}
    	String out = null;
    	String sql = "SELECT senderID FROM `chatmsgs` WHERE (senderID= "+userID+" AND receiveID="+ friendID+ ") OR (senderID="+ friendID+ " AND receiveID="+ userID+ ") " ;
    	try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				out += "[ACKGETMSGSTYPE]:[" + resultSet.getString(1) + "] ";
			}
			sendMsg(out);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void dealGetFriendList(String msg) {
		int userID=0;
		String p = "\\[GETFRIENDLIST\\]:\\[(.*)\\]";
		Pattern pattern = Pattern.compile(p);
    	Matcher matcher = pattern.matcher(msg);
    	if (matcher.find()) {
    		userID = Integer.parseInt(matcher.group(1));
    	}
    	String out = null;
    	String sql = "SELECT friendID FROM friendlist WHERE userID = '" + userID + "';";
    	try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				out += "[ACKGETFRIENDLIST]:[" + resultSet.getString(1) + "] ";
			}
			sendMsg(out);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void dealGetFriendProfile(String msg) {
    	String friendID = null;
    	String p = "\\[GETFRIENDPROFILE\\]:\\[(.*)\\]";
    	Pattern pattern = Pattern.compile(p);
    	Matcher matcher = pattern.matcher(msg);
    	if (matcher.find()) {
    		friendID = matcher.group(1);
    	} else {
			return ;
		}
    	String out = null;
    	String sql = "SELECT nickname FROM userinfo WHERE userID = '" + friendID + "';";
    	try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			if (resultSet.next()) {
				out = "[ACKGETFRIENDPROFILE]:[" + resultSet.getString(1) + "]";
				sendMsg(out);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
	
	private void dealGetUserName(String msg) {
		int userID = 0;
		String userName = null;
		String p = "\\[GETUSERNAME\\]:\\[(.*)\\]";
		Pattern pattern = Pattern.compile(p);
    	Matcher matcher = pattern.matcher(msg);
    	if (matcher.find()) {
    		userID = Integer.parseInt(matcher.group(1));
    	}
    	String out = null;
    	String sql = "SELECT nickname FROM `userinfo` WHERE userID = "+userID;
    	try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			if (resultSet.next()) {
				out = "[ACKGETUSERNAME]:[" + resultSet.getString(1) + "]";
				sendMsg(out);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void dealRegister(String msg) {
		String phoneNumber = null;
    	String password = null;
    	String nickname = null;
    	String verification = null;
    	int newId=0;
    	String p = "\\[REGISTER\\]:\\[(.*), (.*), (.*), (.*)\\]";
    	Pattern pattern = Pattern.compile(p);
    	Matcher matcher = pattern.matcher(msg);
    	if (matcher.find()) {
    		phoneNumber = matcher.group(1);
    		password = matcher.group(2);
    		nickname = matcher.group(3);
    		verification = matcher.group(4);
    	}
    	System.out.println("Register result are: "+phoneNumber+" "+password+" "+nickname+" "+verification);
    	newId=generateID();
    	String sql = "INSERT INTO `userinfo`(`userID`, `phoneNumber`, `password`, `nickname`) VALUES ("+newId+",'"+phoneNumber+"','"+password+"','"+nickname+"')";
    	try {
    		System.out.println("Register sql are: "+sql);
			Statement statement = connection.createStatement();
			boolean resultSet = statement.execute(sql);
			sendMsg("[ACKREGISTER]:[1,"+newId+"]");
			System.out.println("Register insert successfully!!");			
		} catch (SQLException e) {
			sendMsg("[ACKREGISTER]:[0]");
			e.printStackTrace();
		}
    }
	
	private void dealRegisterSend(String msg) {
		String phoneNumber = null;
		String p = "\\[REGISTERSEND\\]:\\[(.*)\\]";
		Pattern pattern = Pattern.compile(p);
    	Matcher matcher = pattern.matcher(msg);
    	if (matcher.find()) {
    		phoneNumber = matcher.group(1);
    	}
    	System.out.println("RegisterSend result are: "+phoneNumber);
	}
	
	private void dealLogin(String msg) {
		int userID = 0;
    	String password = null;
    	String p = "\\[LOGIN\\]:\\[(.*), (.*)\\]";
    	Pattern pattern = Pattern.compile(p);
    	Matcher matcher = pattern.matcher(msg);
    	if (matcher.find()) {
    		userID = Integer.parseInt(matcher.group(1));
    		password = matcher.group(2);
    	}
    	System.out.println("Login result are: "+userID+" "+password);
    	String sql = "SELECT password FROM `userinfo` WHERE userID=" + userID;
    	try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			if (resultSet.next() && password.equals(resultSet.getString(1)) ) {
				socketMsg = new SocketMsg(this,  userID);
				ChatManager.getChatManager().add(socketMsg);
				sendMsg("[ACKLOGIN]:[1]");
			}else {
				sendMsg("[ACKLOGIN]:[0]");
			}
		} catch (SQLException e) {
			sendMsg("[ACKLOGIN]:[0]");
			e.printStackTrace();
		}
	}
	

	private void dealQuit(String msg) {
		int userID = 0;
    	String p = "\\[QUIT\\]:\\[(.*)\\]";
    	Pattern pattern = Pattern.compile(p);
    	Matcher matcher = pattern.matcher(msg);
    	if (matcher.find()) {
    		userID = Integer.parseInt(matcher.group(1));
    	}
    	//socketMsg = new SocketMsg(this,  userID);
		ChatManager.getChatManager().remove(socketMsg);
	}
	
	//ID递增从1000开始
	private int generateID() {
		int newId=0;
		String sql="SELECT MAX(userID) FROM userinfo WHERE userID < (SELECT MAX(userID) FROM userinfo)";
		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			if(resultSet.next()) {
				newId=resultSet.getInt(1)+1;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return newId;
	}
}

