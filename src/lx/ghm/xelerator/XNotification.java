package lx.ghm.xelerator;

import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class XNotification extends XNode implements IXTask{
	private static final String iconFile ="/lx/ghm/xelerator/resource/XNotification.png";
	public Image getIcon(int type) {
	    return new ImageIcon(getClass().getResource(iconFile)).getImage();
	}
	public ArrayList getAssertions() {
		return null;
	}
	public XTerminal getXTerminal() {
		return null;
	}
	
	private String name="Notification";
	private String comment ="";
	
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getName() {
		return this.name;
	}
	public void setName(String n) {
		this.name = n;
	}
	
	private String content = "";
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
}
