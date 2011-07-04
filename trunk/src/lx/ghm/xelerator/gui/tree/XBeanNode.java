/**
 * 
 */
package lx.ghm.xelerator.gui.tree;

import java.awt.Color;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * @author xunli
 *
 */
public class XBeanNode extends DefaultMutableTreeNode {
	public Object bean;
	public String name;
	Color color = null;
	Color bgColor = null;
	boolean isBolder = false;

	public XBeanNode(Object bean, String name) {
		this.bean = bean;
		this.name = name;
	}

	public String toString() {
		return name;
	}

	public Object getBean() {
		return bean;
	}
	
	public Color getColor(){
		return color;
	}
	
	public Color getBgColor(){
		return bgColor;
	}
	
	public void setColor(Color color){
		this.color = color;
	}
	
	public void setBgColor(Color color){
		this.bgColor = color;
	}

	public boolean isBolder() {
		return isBolder;
	}

	public void setBolder(boolean isBolder) {
		this.isBolder = isBolder;
	}
}
