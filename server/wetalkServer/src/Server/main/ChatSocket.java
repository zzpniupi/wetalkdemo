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


public class ChatSocket extends Thread{
	private Socket socket;
	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWriter;
	private String message = null;
	
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
			case "REGISTER": {dealRegister(msg); break;}
			case "REGISTERSEND": {dealRegisterSend(msg); break;}
			case "LOGIN": {dealLogin(msg); break;}
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
		String userID = null;
    	String password = null;
    	String p = "\\[LOGIN\\]:\\[(.*), (.*)\\]";
    	Pattern pattern = Pattern.compile(p);
    	Matcher matcher = pattern.matcher(msg);
    	if (matcher.find()) {
    		userID = matcher.group(1);
    		password = matcher.group(2);
    	}
    	System.out.println("Login result are: "+userID+" "+password);
    	String sql = "SELECT password FROM `userinfo` WHERE userID=" + userID;
    	try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			if (resultSet.next() && password.equals(resultSet.getString(1)) ) {
				sendMsg("[ACKLOGIN]:[1]");
			}else {
				sendMsg("[ACKLOGIN]:[0]");
			}
		} catch (SQLException e) {
			sendMsg("[ACKLOGIN]:[0]");
			e.printStackTrace();
		}
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

