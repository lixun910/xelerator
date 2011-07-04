package lx.ghm.xelerator;

import java.awt.Image;

import javax.swing.ImageIcon;


public class XProject extends XNode{
	private static final String iconFile ="/lx/ghm/xelerator/resource/XProject1.png";
	public Image getIcon(int type) {
	    return new ImageIcon(getClass().getResource(iconFile)).getImage();
	 }
	
	private String name="Xelerate Project";
	private String comment;
	
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String toString(){
		return name;
	}
/*****************************************************/	
	private boolean isSendNotification = false;
	private String smtpServer = "";
	private String loginName = "";
	private String sender = "";
	private String password = "";
	private String reciever = "";
	private String content = "";
	private String title = "";
	
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getContent() {
		return content;
	}
	public boolean isSendNotification() {
		return isSendNotification;
	}
	public void setSendNotification(boolean isSendNotification) {
		this.isSendNotification = isSendNotification;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getReciever() {
		return reciever;
	}
	public void setReciever(String reciever) {
		this.reciever = reciever;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getSmtpServer() {
		return smtpServer;
	}
	public void setSmtpServer(String smtpServer) {
		this.smtpServer = smtpServer;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
/*****************************************************/	
	private XParameter xParameter = new XParameter();
	private XTerminalGroup xTerminalGroup = new XTerminalGroup();
	private XWork xWork = new XWork();

	public XParameter getXParameter() {
		return xParameter;
	}
	public void setXParameter(XParameter parameter) {
		xParameter = parameter;
	}
	public XTerminalGroup getXTerminalGroup() {
		return xTerminalGroup;
	}
	public void setXTerminalGroup(XTerminalGroup terminalGroup) {
		xTerminalGroup = terminalGroup;
	}
	public XWork getXWork() {
		return xWork;
	}
	public void setXWork(XWork work) {
		xWork = work;
	}

}
