package lx.ghm.xelerator;

import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import lx.ghm.xelerator.XNode;

public class XAssertion extends XNode{
	private static final String iconFile ="/lx/ghm/xelerator/resource/XAssertion.gif";
	public Image getIcon(int type) {
	    return new ImageIcon(getClass().getResource(iconFile)).getImage();
	 }
	
	private String name="AssertionName";
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
	
	public String targetContent;
	
	public final static boolean TEXT = false;
	public final static boolean REGEXP = true;
	public final static boolean CONTAINS=true;
	public final static boolean NOT_CONTAINS=false;
	public final static boolean HAS_GLOBAL_VARIABLE = true;
	public final static boolean NOT_HAS_GLOBAL_VARIABLE = false;
	
	private boolean isRegExp;
	private boolean isContain;
	private boolean hasGlobalVar;
	private String content;
	private String prompt = "";
	
	public String getPrompt() {
		return prompt.equals("")?content:prompt;
	}
	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}
	public XAssertion(){}
	
	public XAssertion(String assertionContent, boolean isRegExp, boolean hasGlobalVar, boolean assertionContain){
		this.content = assertionContent;
		this.isRegExp = isRegExp;
		this.hasGlobalVar = hasGlobalVar;
		this.isContain = assertionContain;
	}

	public String toString(){
		return this.content;
	}

	public boolean isContain() {
		return isContain;
	}

	public void setContain(boolean contain) {
		isContain = contain;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isRegExp() {
		return isRegExp;
	}

	public void setRegExp(boolean type) {
		isRegExp = type;
	}
	public boolean isHasGlobalVar() {
		return hasGlobalVar;
	}
	public void setHasGlobalVar(boolean hasGlobalVar) {
		this.hasGlobalVar = hasGlobalVar;
	}
}
