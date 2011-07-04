package lx.ghm.xelerator.processor;

import lx.ghm.xelerator.XAssertion;

public class TaskExecuteException extends Exception {
	public XAssertion xAssertion;
	public TaskExecuteException(){
		
	}
	public TaskExecuteException(String s) {
		super(s);
	}
	public TaskExecuteException(XAssertion xAssertion) {
		this.xAssertion = xAssertion;
	}
}
