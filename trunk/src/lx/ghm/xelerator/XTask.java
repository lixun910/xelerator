package lx.ghm.xelerator;

import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class XTask extends XNode implements IXTask{
	private static final String iconFile ="/lx/ghm/xelerator/resource/XTask.png";
	public Image getIcon(int type) {
	    return new ImageIcon(getClass().getResource(iconFile)).getImage();
	 }
	
	private String name = "taskName";
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
	
	private String id = "";
	private String prompt = "";
	private String command = "";
	private String regexCommand = "";
	private ArrayList assertions = new ArrayList();
	private XTerminal xTerminal;
	private String terminalCode = "";
	
	private boolean hasGlobalVar= false;
	private boolean hasRegularExpression = false;
	
	public XTask(){
		
	}
	
	public XTask(String prompt,String test){
		this.prompt = prompt;
		this.command = test;
	}
	
	public XTask(XTerminal xTerm, String currentPrompt, String command){
		this.xTerminal = xTerm;
		this.prompt = currentPrompt;
		this.command = command;
		this.id = new Integer(this.hashCode()).toString();
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

	public String getPrompt() {
		return prompt;
	}

	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}
	
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRegexCommand() {
		return regexCommand;
	}

	public void setRegexCommand(String regexCommand) {
		this.regexCommand = regexCommand;
	}

	public XTerminal getXTerminal() {
		if ( xTerminal == null) return null;
//		if ( !xTerminal.isInit()){
//			xTerminal.init();
//		}
		return xTerminal;
	}

	public void setXTerminal(XTerminal terminal) {
		if( terminal!=null){
			xTerminal = terminal;
			this.terminalCode = terminal.getId();
		}
	}
	
	public boolean isHasGlobalVar() {
		return hasGlobalVar;
	}
	public void setHasGlobalVar(boolean hasGlobalVariable) {
		this.hasGlobalVar = hasGlobalVariable;
	}
	public String getTerminalCode() {
		return terminalCode;
	}
	
	public void setTerminalCode(String terminalCode) {
		this.terminalCode = terminalCode;
	}
	public boolean isHasRegularExpression() {
		return hasRegularExpression;
	}
	public void setHasRegularExpression(boolean hasRegularExpression) {
		this.hasRegularExpression = hasRegularExpression;
	}
}
