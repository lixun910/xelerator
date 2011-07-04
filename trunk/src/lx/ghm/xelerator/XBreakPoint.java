package lx.ghm.xelerator;

import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class XBreakPoint extends XNode implements IXTask{
	private static final String iconFile ="/lx/ghm/xelerator/resource/XBreakPoint.png";
	public Image getIcon(int type) {
	    return new ImageIcon(getClass().getResource(iconFile)).getImage();
	}
	public ArrayList getAssertions() {
		return null;
	}
	public XTerminal getXTerminal() {
		return null;
	}
	
	private String name="XBreakPoint";
	private String comment ="";
	
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

}
