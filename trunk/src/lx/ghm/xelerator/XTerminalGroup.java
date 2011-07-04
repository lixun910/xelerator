package lx.ghm.xelerator;

import java.awt.Image;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.ImageIcon;

public class XTerminalGroup extends XNode{
	private static final String iconFile ="/lx/ghm/xelerator/resource/XTerminalGroup.gif";
	public Image getIcon(int type) {
	    return new ImageIcon(getClass().getResource(iconFile)).getImage();
	 }
	
	private String name="XTerminalGroup";
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
	
	private ArrayList xTerminalGroup = new ArrayList();
	public Hashtable xTerminalHT = new Hashtable();
	
	public int size(){
		return xTerminalGroup.size();
	}
	
	public XTerminal get(int index){
		return (XTerminal)xTerminalGroup.get(index);
	}
	
	public void add(XTerminal xTerminal){
		this.xTerminalHT.put(xTerminal.getId(), xTerminal);
		xTerminalGroup.add(xTerminal);
	}
	
	public void remove(XTerminal xTerminal){
		xTerminalGroup.remove(xTerminal);
	}
}
