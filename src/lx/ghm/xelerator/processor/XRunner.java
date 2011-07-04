package lx.ghm.xelerator.processor;

import java.awt.Color;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;
import javax.swing.tree.TreePath;

import lx.ghm.xelerator.IXTask;
import lx.ghm.xelerator.XAssertion;
import lx.ghm.xelerator.XBreakPoint;
import lx.ghm.xelerator.XNotification;
import lx.ghm.xelerator.XProject;
import lx.ghm.xelerator.XTask;
import lx.ghm.xelerator.XTerminal;
import lx.ghm.xelerator.XTerminalGroup;
import lx.ghm.xelerator.XTimer;
import lx.ghm.xelerator.XWork;
import lx.ghm.xelerator.gui.MainGui;
import lx.ghm.xelerator.util.EmailAgent;
import lx.ghm.xelerator.util.MailMessage;

public class XRunner extends Thread{
	private boolean isDeamon = false;
	private boolean isRunning = true;
	public boolean isStop = false;
	
	private MainGui gui;
	private XProject xProject;
	
	public XRunner(){}
	public XRunner(MainGui gui, XProject xProject){
		this.gui = gui;
		this.xProject = xProject;
	}
	
	public void setGui(MainGui gui){
		this.gui = gui;
	}
	
	public void setXProject(XProject xProject){
		this.xProject = xProject;
	}
	
	public void continueRun(){
		isRunning = true;
		this.resume();
	}
	
	public void pauseRun(){
		isRunning = false;
	}
	
	public void stopRun(){
		isStop = true;
		isRunning = false;
		this.resume();
		
		XTerminalGroup xTerminalGroup = xProject.getXTerminalGroup();
		for ( int i=0; i<xTerminalGroup.size(); i++){
			XTerminal xTerminal = xTerminalGroup.get(i);
			if(xTerminal.isClose==false)
				xTerminal.close();
		}
	}
	
	public void runAsDeamon(boolean flag){
		isDeamon = flag;
	}
	
	private int initRun(XProject xProject){
		Object selectedBean = gui.getTree().getCurrentSelectedBean();
		int index = 0;
		boolean isNewStart = false;
		if ( isStop && selectedBean != null){
			//continue running
			if( selectedBean instanceof XTask){
				XTask xTask = (XTask)selectedBean;
				if (xTask.getXTerminal().isClose==false) {
					index = xProject.getXWork().xTaskLinkedList.indexOf((XTask)selectedBean);
				}else
					isNewStart = true;
			}
		}else
			isNewStart = true;
		
		if (isNewStart){
			//new running
			XTerminalGroup xTerminalGroup = xProject.getXTerminalGroup();
			for ( int i=0; i<xTerminalGroup.size(); i++){
				XTerminal xTerminal = xTerminalGroup.get(i);
				if(xTerminal.isClose==false)
					xTerminal.close();
				xTerminal.login(xProject);
				xTerminal.show(isDeamon);
			}
			for ( int i=0; i<xTerminalGroup.size(); i++){
				XTerminal xTerminal = xTerminalGroup.get(i);
				xTerminal.login(xProject);
				new Thread(xTerminal).start();
			}
		}
		return index;
	}
	
	public void run(){
		if (xProject == null){
			gui.showError("XProject Error!");
			return;
		}
		XWork xWork = xProject.getXWork();
		
		try {
			int runningIndex = initRun(xProject);
			for ( int i=runningIndex; i<xWork.size(); i++){
				if ( isStop) // for STOP button
					return;
				else if ( !isRunning && !isStop) // for PAUSE button
					this.suspend();
				
				IXTask xCurrentTask = (IXTask)xWork.get(i);
				
				if (xCurrentTask instanceof XBreakPoint){
					if(!isDeamon){
					gui.continueBtn.setEnabled(true);
					gui.pauseBtn.setEnabled(false);
					gui.getMenuContinueRun().setEnabled(true);
					gui.getMenuPauseRun().setEnabled(false);
					}
					isRunning = false;
					
					setRunningNodeFormat(xCurrentTask, Color.green);
//					gui.getTree().setSelectionPath(gui.getTree().selectNodeByBean(xCurrentTask));
					if ( xProject.isSendNotification() == true){
						sendNotification(xProject, 
								"Current project meets XBreakPoint, please pay attention!");
					}
					this.suspend();
				}else if ( xCurrentTask instanceof XTask){
					XTerminal xCurrentTerminal = xCurrentTask.getXTerminal();
					if (xCurrentTerminal==null){
						JOptionPane.showMessageDialog(gui, "No XTerminal is assigned to this XTask!\n", "Xelerator Error",JOptionPane.ERROR_MESSAGE);
						return;
					}
					setRunningNodeFormat(xCurrentTask, Color.green);
					if(!isDeamon){
						xCurrentTerminal.getFrame().toFront();
					}
//					gui.getTree().setSelectionPath(gui.getTree().selectNodeByBean(xCurrentTask));
					XTask xTask = (XTask)xCurrentTask;
					while(!isStop && isRunning && xCurrentTerminal.isClose==false){	
						String prompt = xTask.getPrompt();
						if ( xTask.isHasGlobalVar())
							prompt = xProject.getXParameter().doTranslate(prompt);
						
						if(prompt.equals("")||xCurrentTerminal.getTerminal().buffer.getLatestLine().trim().endsWith(prompt)){
							// Execute CMD for current Node 
							String command = xTask.getCommand();
							if ( xTask.isHasGlobalVar()) 
								command = xProject.getXParameter().doTranslate(command);
							if ( xTask.isHasRegularExpression()){
								command = replaceRegExInCMD(command, xCurrentTerminal.getTerminal().buffer.getContent());
							}
							if ( xWork.isStepByStep()){
								isRunning = false;
								if(!isDeamon){
								gui.continueBtn.setEnabled(true);
								gui.pauseBtn.setEnabled(false);
								gui.getMenuContinueRun().setEnabled(true);
								gui.getMenuPauseRun().setEnabled(false);
								}
								String title = "Do you want to execute this Command?";
								String message = command.equals("")?"  ENTER  ":command;
								int response = JOptionPane.showConfirmDialog(gui, "["+xCurrentTerminal.getName() + "] " + message, title, JOptionPane.YES_NO_OPTION);
								if (response == JOptionPane.NO_OPTION)
									this.suspend();
								else{
									isRunning = true;
									if(!isDeamon){
									gui.continueBtn.setEnabled(false);
									gui.pauseBtn.setEnabled(true);
									gui.getMenuContinueRun().setEnabled(false);
									gui.getMenuPauseRun().setEnabled(true);
									}
								}
							}
								
							xCurrentTerminal.getTerminal().send(command);
							xCurrentTerminal.getTerminal().send('\r');

							sleep(1000);
							
							//Check Assertions for previous Node
							for (int k=0;k<xTask.getAssertions().size();k++){
								XAssertion assertion =(XAssertion)xTask.getAssertions().get(k);
//								gui.getTree().setSelectionPath(gui.getTree().selectNodeByBean(assertion));
								setRunningNodeFormat(assertion, Color.green);
								
								while(!isStop && isRunning && xTask.getXTerminal().isClose==false){
									String assertionPrompt = assertion.getPrompt();
									if (assertion.isHasGlobalVar())
										assertionPrompt = xProject.getXParameter().doTranslate(assertionPrompt);
									if(xTask.getXTerminal().getTerminal().buffer.getContent().indexOf(assertionPrompt)>=0){
										checkAssertion(assertion, xTask.getXTerminal().getTerminal().buffer.getContent());
										break;
									}
									sleep(1);
								}
							}
							break;
						}
						sleep(1);
					}
				}else if(xCurrentTask instanceof XTimer){
					XTimer xTimer = (XTimer)xCurrentTask;
					setRunningNodeFormat(xTimer, Color.green);
					if ( xWork.isStepByStep()){
						isRunning = false;
						if(!isDeamon){
						gui.continueBtn.setEnabled(true);
						gui.pauseBtn.setEnabled(false);
						gui.getMenuContinueRun().setEnabled(true);
						gui.getMenuPauseRun().setEnabled(false);
						}
						this.suspend();
					}
					sleep(xTimer.getTimeout());
					
				}else if(xCurrentTask instanceof XNotification){
					XNotification xNotification = (XNotification)xCurrentTask;
					setRunningNodeFormat(xNotification, Color.green);
					if ( xWork.isStepByStep()){
						isRunning = false;
						if(!isDeamon){
						gui.continueBtn.setEnabled(true);
						gui.pauseBtn.setEnabled(false);
						gui.getMenuContinueRun().setEnabled(true);
						gui.getMenuPauseRun().setEnabled(false);
						}
						this.suspend();
					}
					sendNotification(xProject,xNotification.getContent());
				}
				
				if (!isStop && !isRunning){
					i = i>0?i-1:i;
					this.suspend();
				}
			}
			isStop = true;
			return;
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}catch (TaskExecuteException e) {
			XAssertion xAssetion = e.xAssertion;
			setRunningNodeFormat(xAssetion, Color.red);
			
			if ( xProject.isSendNotification() == true){
				sendNotification(xProject,xAssetion.targetContent);
			}
			JOptionPane.showMessageDialog(gui, "TaskExecuteException Failed!\n", "Xelerator Error",JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			return;
		}
	}

	private void setRunningNodeFormat(Object node, Color color){
		TreePath path = gui.getTree().selectNodeByBean(node);
		gui.getTree().setNodeBackgroundColor(path, color);
//		gui.getTree().setSelectionPath(path);
	}
	
	private String replaceRegExInCMD(String command, String content){
		String regEx=command;
		Pattern p=Pattern.compile(regEx);
		Matcher m=p.matcher(content);
		String match="";
		
		while(m.find()){
			match = m.group(1);
//			int start = m.start();
//			int end = m.end();
//			match = content.substring(start, end);
		}
		return match;
	}
	
	private void sendNotification(XProject xProject, String notificationContent) {
		try {
			MailMessage message = new MailMessage();
			message.setFrom(xProject.getSender());
			message.setTo(xProject.getReciever());
			message.setSubject(xProject.getTitle());
			message.setContent(xProject.getContent()+"\n\n"+ notificationContent);
			message.setDatafrom(xProject.getSender()); 
			message.setDatato(xProject.getReciever()); 
			message.setUser(xProject.getLoginName()); 
			message.setPassword(xProject.getPassword()); 
		
			EmailAgent.getInstance().doSendNotification(xProject.getSmtpServer(), message);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(gui, "Email Notification Failed!\n", "Xelerator Error",JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		
	}
	
	private void checkAssertion(XAssertion assertion, String text) throws TaskExecuteException{
		assertion.targetContent = text;
		if ( assertion.isHasGlobalVar()){
			assertion.setContent(xProject.getXParameter().doTranslate(assertion.getContent()));
		}
		if( assertion.isRegExp() == false){
			if ( assertion.isContain() == true){
				if ( text.indexOf(assertion.toString())<0)
					throw new TaskExecuteException(assertion);
			}else{
				if ( text.indexOf(assertion.toString())>=0)
					throw new TaskExecuteException(assertion);
			}
		}else if( assertion.isRegExp() == true){
			String regEx=assertion.toString();
			Pattern p=Pattern.compile(regEx);
			Matcher m=p.matcher(text);

			if ( assertion.isContain() == true){
				if (!m.matches())
					throw new TaskExecuteException(assertion);
			}else{
				if (m.matches())
					throw new TaskExecuteException(assertion);
			}
		}
	}
}
