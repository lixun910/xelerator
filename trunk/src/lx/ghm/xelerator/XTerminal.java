package lx.ghm.xelerator;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import cz.dhl.term.AEmulator;
import cz.dhl.term.JTerminal;
import cz.dhl.term.Telnet;
import cz.dhl.term.VTEmulator;
import cz.dhl.term.VTMapper;
import cz.dhl.term.VTTranslator;


public class XTerminal extends XNode implements Runnable{
	private static final String iconFile ="/lx/ghm/xelerator/resource/XTerminal.gif";
	public Image getIcon(int type) {
	    return new ImageIcon(getClass().getResource(iconFile)).getImage();
	 }
	
	private String name="XTerminal";
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
	
	private String id;
	private String loginName;
	private String password;
	private String server = "135.2.89.142";
	private int port = 23;
	private String definedColor="";
	
	private Telnet telnet;
	private AEmulator emu;
	private JTerminal term;
	private JFrame frame;
	public boolean isClose = true;
	
	private static int posX=200;
	private static int posY=100;
	
	public String logFileName;
	
	public XTerminal(){
		emu = new VTEmulator(new VTMapper(), new VTTranslator());
        term = new JTerminal();
        term.setEmulator(emu);
        telnet = new Telnet(emu);
	}
	
	public String toString(){
		return this.getName();
	}
	
	public XTerminal(String hostIP, String hostPort, String loginName, String password){
		this.server = hostIP;
		this.port = Integer.parseInt(hostPort);
		this.loginName = loginName;
		this.password = password;
		
		emu = new VTEmulator(new VTMapper(), new VTTranslator());
        term = new JTerminal();
        term.setEmulator(emu);
        telnet = new Telnet(emu);
	}
	
	public void close(){
		try{
			if (isClose == true || this.frame == null ) return;
			this.frame.dispose();
			
			this.emu = null;
			this.term = null;
			this.telnet = null;
			this.emu = new VTEmulator(new VTMapper(), new VTTranslator());
			this.term = new JTerminal();
			this.term.setEmulator(emu);
			this.telnet = new Telnet(emu);
			
			this.isClose = true;
			posX=200;
			posY=100;
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	 
	public JFrame getFrame(){
		return frame;
	}
	
	public void setLogFile(String logFile){
		telnet.debug(logFile);
	}
	public void show(boolean isDeamon){
		if(!isDeamon){
			frame = new JFrame("XTelnet: " + name);
			frame.setIconImage(new ImageIcon(getClass().getResource(iconFile)).getImage());
	        frame.setContentPane(term);
	        frame.addWindowListener(new WindowAdapter() {
	            public void windowClosing(WindowEvent e) {
	            	close();
	            }
	        });
	        frame.pack();
	        frame.setVisible(true);
	        frame.setLocation(posX, posY);
	        posX = (posX<800)?posX+30:0;
	        posY = (posY<600)?posY+30:0;
		}
		if(definedColor==null || !definedColor.equals("")){
			term.setBackgrounColor(new Color(Integer.parseInt(definedColor)));
		}
        logFileName ="log/" + xProject.getName() + "_" + this.getName() +".log";
        telnet.debug( logFileName);
        try {
            System.setOut(new PrintStream(new FileOutputStream("debug.log", true), true));
        } catch (Exception e) {
        	e.printStackTrace();
        }
        telnet.connect(server, port);
        isClose = false;
	}	
	
	private int timer = 10000;
	private XProject xProject;
	
	public void login(XProject xProject) {
		this.xProject = xProject;
	}
	public void run() {
		Date date = new Date(); 
		long currentTime = date.getTime();
		XParameter xParameter = xProject.getXParameter();
		String _loginName = xParameter.doTranslate(getLoginName());
		String _password = xParameter.doTranslate(getPassword());
		
		try {
			boolean bLogin = true;
			boolean bPassword = true;
			
			while(isClose==false){ // Wait for prompt
				if(bLogin&&getTerminal().buffer.getLatestLine().trim().endsWith("login:")){
					getTerminal().send(_loginName);
					getTerminal().send('\r');
					bLogin = false;
					Thread.sleep(500);
					continue;
				}else if (bPassword && getTerminal().buffer.getLatestLine().trim().endsWith("Password:")){
					getTerminal().send(_password);
					getTerminal().send('\r');
					bPassword = false;
					Thread.sleep(500);
					continue;
				}else if ((new Date().getTime() - currentTime)>timer){
					if (getTerminal().buffer.getContent().indexOf("Login incorrect")>=0 ){
						JOptionPane.showMessageDialog(frame, "Login Failed: Timeout!", "Xelerator Login Error",JOptionPane.ERROR_MESSAGE);
						System.out.println("Login Failed!");
					}
					return;
				}
				Thread.sleep(500);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public JTerminal getTerminal() {
		return term;
	}

	public void setTerminal(JTerminal term) {
		this.term = term;
	}
	
	public VTEmulator getEmulator(){
		return (VTEmulator)this.emu;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getDefinedColor() {
		return definedColor;
	}

	public void setDefinedColor(String definedColor) {
		this.definedColor = definedColor;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

}
