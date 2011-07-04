package lx.ghm.xelerator;

import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class XTimer extends XNode implements IXTask{
	private static final String iconFile ="/lx/ghm/xelerator/resource/XTimer.png";
	public Image getIcon(int type) {
		return new ImageIcon(getClass().getResource(iconFile)).getImage();
	}
	
	private String name="XTimer";
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
	
	public XTimer(){}
	public XTimer(XTerminal xTerm, String seconds){
		this.xTerminal = xTerm;
		this.seconds = seconds;
		this.timeout = setTimeout(seconds);
	}
	
	private long timeout;
	private String seconds;
	private XTerminal xTerminal;
	private String terminalCode = "";
	private ArrayList assertions = new ArrayList();
	
	public long getTimeout() {
		this.timeout = Integer.parseInt(seconds)*1000;
		return timeout;
	}
	
	private long setTimeout(String seconds) {
		this.timeout = Integer.parseInt(seconds)*1000;
		return this.timeout;
	}

	public String getSeconds() {
		return seconds;
	}
	public void setSeconds(String seconds) {
		this.seconds = seconds;
		this.setTimeout(seconds);
	}
	public XTerminal getXTerminal() {
		return xTerminal;
	}
	public void setXTerminal(XTerminal terminal) {
		if( terminal!=null){
			xTerminal = terminal;
			this.terminalCode = terminal.getId();
		}
	}
	public XTerminal resetTerminal(XTerminalGroup xTerminalGroup){
		for ( int i =0;i<xTerminalGroup.size();i++){
			XTerminal xTerm = xTerminalGroup.get(i);
			if ( xTerm.getId().equals(this.terminalCode)){
				this.xTerminal = xTerm;
				return this.xTerminal;
			}
		}
		return null;
	}
	
	public String getTerminalCode() {
		return terminalCode;
	}
	
	public void setTerminalCode(String terminalCode) {
		this.terminalCode = terminalCode;
	}
	
	public int size(){
		return assertions.size();
	}
	
	public XAssertion get(int index){
		return (XAssertion)assertions.get(index);
	}
	
	public void removeAssertion(XAssertion assertion){
		this.assertions.remove(assertion);
	}
	
	public void addAssertion(XAssertion condi){
		this.assertions.add(condi);
	}
	
	public ArrayList getAssertions(){
		return this.assertions;
	}
}
