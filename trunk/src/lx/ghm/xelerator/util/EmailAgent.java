package lx.ghm.xelerator.util;

import java.io.IOException;
import java.net.UnknownHostException;

public class EmailAgent {
	private static EmailAgent instance = null;
	
	private EmailAgent(){
		
	}
	
	public static EmailAgent getInstance(){
		if ( instance == null) 
			instance = new EmailAgent();
		return instance;
	}
	
	public boolean doSendNotification(String server, MailMessage message) throws UnknownHostException, IOException{

		SMTPClient smtp = new SMTPClient(server, 25);
		boolean flag;
		flag = smtp.sendMail(message, server);
		
		if (flag) {
			System.out.println("邮件发送成功！");
		} else {
			System.out.println("邮件发送失败！");
		}
		
		return flag;
	}
}
