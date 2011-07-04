package lx.ghm.xelerator.share;

import java.awt.Color;
import java.util.ArrayList;

public class XDir implements IShareNode{
	private int id;
	private String name;
	private int parentID;
	
	private XDir parent;
	private ArrayList children = new ArrayList();
	private ArrayList files = new ArrayList();
	
	public XDir(){}
	public int filesSize(){
		return files.size();
	}
	public void addXFile(XFile xFile){
		files.add(xFile);
	}
	public void removeXFile(XFile xFile){
		files.remove(xFile);
	}
	public XFile getXFile(int index){
		return (XFile)files.get(index);
	}
	public XDir(int id){
		this.id = id;
	}
	public String toString(){
		return this.name;
	}
	public int size(){
		return children.size();
	}
	public XDir get(int index){
		return (XDir)children.get(index);
	}
	public void addChild(XDir child){
		children.add(child);
	}
	public ArrayList getChildren() {
		return children;
	}
	public void setChildren(ArrayList children) {
		this.children = children;
	}
	public XDir getParent() {
		return parent;
	}
	public void setParent(XDir parent) {
		this.parent = parent;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getParentID() {
		return parentID;
	}
	public void setParentID(int parentID) {
		this.parentID = parentID;
	}
	private Color color = null;
	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
}
