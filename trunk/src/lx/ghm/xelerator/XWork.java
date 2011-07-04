package lx.ghm.xelerator;

import java.awt.Image;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.ImageIcon;

public class XWork extends XNode{
	private static final String iconFile ="/lx/ghm/xelerator/resource/XWork.gif";
	public Image getIcon(int type) {
	    return new ImageIcon(getClass().getResource(iconFile)).getImage();
	}
	
	private String name="XWork";
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
	
	private boolean isStepByStep = false;
	public boolean isStepByStep() {
		return isStepByStep;
	}
	public void setStepByStep(boolean isStepByStep) {
		this.isStepByStep = isStepByStep;
	}
	
	private ArrayList xTaskList = new ArrayList();
	public LinkedList xTaskLinkedList = new LinkedList();
	
	public int size(){
		return xTaskList.size();
	}
	
	public XNode get(int index){
		return (XNode)xTaskList.get(index);
	}
	
	public void add(XNode xNode){
		this.xTaskLinkedList.add(xNode);
		xTaskList.add(xNode);
	}
	
	public void insertAfter(XNode xPreNode, XNode xNode){
		int index = xTaskList.indexOf(xPreNode);
		xTaskList.add(index+1, xNode);
	}
	public void remove(XNode xNode){
		xTaskList.remove(xNode);
	}
	
}
