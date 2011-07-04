package lx.ghm.xelerator.xml;

import java.awt.Color;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.tree.TreePath;

import lx.ghm.xelerator.XAssertion;
import lx.ghm.xelerator.XBreakPoint;
import lx.ghm.xelerator.XNotification;
import lx.ghm.xelerator.XNode;
import lx.ghm.xelerator.XParameter;
import lx.ghm.xelerator.XProject;
import lx.ghm.xelerator.XTask;
import lx.ghm.xelerator.XTerminal;
import lx.ghm.xelerator.XTerminalGroup;
import lx.ghm.xelerator.XTimer;
import lx.ghm.xelerator.XWork;
import lx.ghm.xelerator.gui.tree.XTree;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class XMLDocument {
	public static void writeXML(XProject xProject, String fileName){
		try{
			Document doc = new Document(); 
			
			if ( xProject !=null){
				Element projectEl = new Element("XProject");
				doc.setRootElement(projectEl);
				addNameAndCommentProps(projectEl, xProject.getName(),xProject.getComment());
				addChildElement(projectEl, "boolProp", "name", "isSendNotification", xProject.isSendNotification());
				addChildElement(projectEl, "stringProp", "name", "smtpServer", xProject.getSmtpServer());
				addChildElement(projectEl, "stringProp", "name", "loginName", xProject.getLoginName());
				addChildElement(projectEl, "stringProp", "name", "password", xProject.getPassword());
				addChildElement(projectEl, "stringProp", "name", "sender", xProject.getSender());
				addChildElement(projectEl, "stringProp", "name", "reciever", xProject.getReciever());
				addChildElement(projectEl, "stringProp", "name", "title", xProject.getTitle());
				addChildElement(projectEl, "stringProp", "name", "content", xProject.getContent());
				
				XParameter xParameter = xProject.getXParameter();
				if (xParameter != null ){
					Element parametersEl = new Element("XParameters");
					addNameAndCommentProps(parametersEl, xParameter.getName(), xParameter.getComment());
					for (int i =0; i<xParameter.size(); i++){
						addChildElement(parametersEl, "parameter", "name", (String)xParameter.get(i).get(0), (String)xParameter.get(i).get(1));					
					}
					///////////////////////////////////
					projectEl.addContent(parametersEl);
				}
				
				XTerminalGroup xTerminalGroup = xProject.getXTerminalGroup();
				if ( xTerminalGroup!= null ){
					Element terminalGroupEl = new Element("XTerminalGroup");
					addNameAndCommentProps(terminalGroupEl, xTerminalGroup.getName(), xTerminalGroup.getComment());
					for ( int i=0; i<xTerminalGroup.size(); i++){
						XTerminal xTerminal = xTerminalGroup.get(i);
						Element terminalEl = new Element("XTerminal");
						addNameAndCommentProps(terminalEl, xTerminal.getName(), xTerminal.getComment());
						addChildElement(terminalEl, "stringProp", "name", "id", xTerminal.getId());
						addChildElement(terminalEl, "stringProp", "name", "server", xTerminal.getServer());
						addChildElement(terminalEl, "stringProp", "name", "loginName", xTerminal.getLoginName());
						addChildElement(terminalEl, "stringProp", "name", "password", xTerminal.getPassword());
						addChildElement(terminalEl, "stringProp", "name", "definedColor", xTerminal.getDefinedColor());
						///////////////////////////////////
						terminalGroupEl.addContent(terminalEl);
					}
					///////////////////////////////////
					projectEl.addContent(terminalGroupEl);
				}
				
				XWork xWork = xProject.getXWork();
				if (xWork != null){
					Element workEl = new Element("XWork");
					addNameAndCommentProps(workEl, xWork.getName(), xWork.getComment());
					addChildElement(workEl, "boolProp", "name", "isStepByStep", xWork.isStepByStep());
					for ( int i=0;i<xWork.size(); i++){
						Object tempTask = xWork.get(i);
						/////////////////XTask
						if ( tempTask instanceof XTask){
							Element taskEl = new Element("XTask");
							XTask xTask = (XTask)tempTask;
							addNameAndCommentProps(taskEl, xTask.getName(), xTask.getComment());
							addChildElement(taskEl, "stringProp", "name", "prompt", xTask.getPrompt());
							addChildElement(taskEl, "stringProp", "name", "command", xTask.getCommand());
							addChildElement(taskEl, "boolProp", "name", "hasGlobalVar", xTask.isHasGlobalVar());
							addChildElement(taskEl, "boolProp", "name", "hasRegularExpression", xTask.isHasRegularExpression());
							addChildElement(taskEl, "stringProp", "name", "terminalCode", xTask.getTerminalCode());
							for ( int j=0; j<xTask.size(); j++){
								Element assertionEl = new Element("XAssertion");
								XAssertion xAssertion = xTask.get(j);
								addNameAndCommentProps(assertionEl, xAssertion.getName(), xAssertion.getComment());
								addChildElement(assertionEl, "stringProp", "name", "content", xAssertion.getContent());
								addChildElement(assertionEl, "boolProp", "name", "isContain", xAssertion.isContain());
								addChildElement(assertionEl, "boolProp", "name", "isRegExp", xAssertion.isRegExp());
								addChildElement(assertionEl, "boolProp", "name", "hasGlobalVar", xAssertion.isHasGlobalVar());
								addChildElement(assertionEl, "stringProp", "name", "prompt", xAssertion.getPrompt());
								///////////////////////////////////
								taskEl.addContent(assertionEl);
							}
							///////////////////////////////////
							workEl.addContent(taskEl);
						
						///////////////////XTimer	
						}else if( tempTask instanceof XTimer){
							Element taskEl = new Element("XTimer");
							XTimer xTimer = (XTimer)tempTask;
							addNameAndCommentProps(taskEl, xTimer.getName(), xTimer.getComment());
							addChildElement(taskEl, "stringProp", "name", "seconds", xTimer.getSeconds());
							addChildElement(taskEl, "stringProp", "name", "terminalCode", xTimer.getTerminalCode());
							workEl.addContent(taskEl);
						} else if (tempTask instanceof XBreakPoint){
							Element taskEl = new Element("XBreakPoint");
							XBreakPoint xBreakPoint = (XBreakPoint)tempTask;
							addNameAndCommentProps(taskEl, xBreakPoint.getName(), xBreakPoint.getComment());
							workEl.addContent(taskEl);
							
						} else if (tempTask instanceof XNotification){
							Element taskEl = new Element("XNotification");
							XNotification xNotification = (XNotification)tempTask;
							addNameAndCommentProps(taskEl, xNotification.getName(), xNotification.getComment());
							addChildElement(taskEl, "stringProp", "name", "content", xNotification.getContent());
							workEl.addContent(taskEl);
						} 
					}
					///////////////////////////////////
					projectEl.addContent(workEl);
				}
				XMLOutputter outputter = new XMLOutputter();
				Format format=Format.getPrettyFormat(); 
				format.setEncoding("GBK");
				outputter.setFormat(format);
				
				FileWriter writer = new FileWriter(fileName);  
				outputter.output(doc, writer);  
				writer.close(); 
			}
		}catch(IOException ex){
			ex.printStackTrace();
		}
	}
	
	
	private static void addChildElement(Element parentEl, String childElementName, String att_name, String att_value, String elementContent){
		parentEl.addContent(
			buildElement(
				new Element(childElementName), 
				new Attribute[]{new Attribute(att_name,att_value)}, 
				elementContent
			)
		);
	}
	private static void addChildElement(Element parentEl, String childElementName, String att_name, String att_value, boolean elementContent){
		String content = elementContent?"true":"false";
		parentEl.addContent(
			buildElement(
				new Element(childElementName), 
				new Attribute[]{new Attribute(att_name,att_value)}, 
				content
			)
		);
	}
	private static void addNameAndCommentProps(Element el, String name, String comment){
		el.addContent(
				buildElement(new Element("stringProp"),
				new Attribute[] { new Attribute("name", "name") }, 
				name)
				);
		el.addContent(
				buildElement(new Element("stringProp"),
				new Attribute[] { new Attribute("name", "comment") }, 
				comment)
				);
	}
	private static Element buildElement(Element el, Attribute[] att, String content){
		for(int i=0;i<att.length;i++)
			el.setAttribute(att[i]);
		el.setText(content);
		return el;
	}
	
	
	public static XProject readProjectFromXML(String fileName){
		try {
			XProject xProject = new XProject();
			XParameter xParameter = new XParameter();
			XTerminalGroup xTerminalGroup = new XTerminalGroup();
			
			SAXBuilder sb = new SAXBuilder();
			Document doc = sb.build(new FileInputStream(fileName));
			Element rootEl = doc.getRootElement(); 
			
			List propertiesList = rootEl.getChildren("stringProp");
			Iterator it = propertiesList.iterator();
			while(it.hasNext()){
				Element propertyEl = (Element) it.next();
				String propertyName = propertyEl.getAttributeValue("name");
				String propertyValue = propertyEl.getText();
				xProject.setPropertyValue(propertyName, propertyValue);
			}
			setObjectFields(rootEl, xProject);
			
			List childrenList = rootEl.getChildren("XParameters");
			it = childrenList.iterator();
			while(it.hasNext()){
				Element el = (Element) it.next();

				setObjectFields(el, xParameter);
				
				List tmpList = el.getChildren("parameter");
				Iterator tmpIterator = tmpList.iterator();
				while(tmpIterator.hasNext()){
					Element propertyEl = (Element) tmpIterator.next();
					String paraName = propertyEl.getAttributeValue("name");
					String paraValue = propertyEl.getText();
					Vector vt = new Vector();
					vt.addElement(paraName);
					vt.addElement(paraValue);
					xParameter.addParameter(vt);
				}
				
				xProject.setXParameter(xParameter);
			}
			
			childrenList = rootEl.getChildren("XTerminalGroup");
			it = childrenList.iterator();
			while(it.hasNext()){
				Element el = (Element) it.next();
				setObjectFields(el, xTerminalGroup);
				xProject.setXTerminalGroup(xTerminalGroup);
				
				List tmpList = el.getChildren("XTerminal");
				Iterator tmpIterator = tmpList.iterator();
				while(tmpIterator.hasNext()){
					Element terminalEl = (Element) tmpIterator.next();
					XTerminal xTerminal = new XTerminal();
					
					setObjectFields(terminalEl, xTerminal);
					xTerminalGroup.add(xTerminal);
				}
			}
			
			childrenList = rootEl.getChildren("XWork");
			it = childrenList.iterator();
			while(it.hasNext()){
				Element el = (Element) it.next();
				XWork xWork = new XWork();
				
				setObjectFields(el, xWork);
				xProject.setXWork(xWork);
				
				List tmpList = el.getChildren();
				Iterator tmpIterator = tmpList.iterator();
				while(tmpIterator.hasNext()){
					Element taskEl = (Element) tmpIterator.next();
					String elementName = taskEl.getName();
					if (elementName.equalsIgnoreCase("xtask")){
						XTask xTask = new XTask();
						setObjectFields(taskEl, xTask);
						xWork.add(xTask);
						
						List assertionList = taskEl.getChildren("XAssertion");
						Iterator assertionIterator = assertionList.iterator();
						while(assertionIterator.hasNext()){
							Element assertionEl = (Element) assertionIterator.next();
							XAssertion xAssertion = new XAssertion();
							
							setObjectFields(assertionEl, xAssertion);
							xTask.addAssertion(xAssertion);
						}
					}else if(elementName.equalsIgnoreCase("XTimer")){
						XTimer xTimer = new XTimer();
						setObjectFields(taskEl, xTimer);
						
						xWork.add(xTimer);
			
						
					}else if (elementName.equalsIgnoreCase("XBreakPoint")){
						XBreakPoint xBreakPoint = new XBreakPoint();
						setObjectFields(taskEl, xBreakPoint);
						xWork.add(xBreakPoint);
						
					}else if (elementName.equalsIgnoreCase("XNotification")){
						XNotification xNotification = new XNotification();
						setObjectFields(taskEl, xNotification);
						
						xWork.add(xNotification);
					}
				}
			}
			
			return xProject;
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	/*
	public static XTree readXML(String fileName) {
		try {
			XTree tree = new XTree();
			
			SAXBuilder sb = new SAXBuilder();
			Document doc = sb.build(new FileInputStream(fileName));
			Element rootEl = doc.getRootElement(); 
			
			XProject xProject = tree.getProjectBean();
			List propertiesList = rootEl.getChildren("stringProp");
			Iterator it = propertiesList.iterator();
			while(it.hasNext()){
				Element propertyEl = (Element) it.next();
				String propertyName = propertyEl.getAttributeValue("name");
				String propertyValue = propertyEl.getText();
				xProject.setPropertyValue(propertyName, propertyValue);
			}
			setObjectFields(rootEl, xProject);
			
			TreePath treePath = tree.selectNodeByBean(xProject);
			List childrenList = rootEl.getChildren("XParameters");
			it = childrenList.iterator();
			while(it.hasNext()){
				Element el = (Element) it.next();
				XParameter xParameter = new XParameter();
				
				setObjectFields(el, xParameter);
				
				List tmpList = el.getChildren("parameter");
				Iterator tmpIterator = tmpList.iterator();
				while(tmpIterator.hasNext()){
					Element propertyEl = (Element) tmpIterator.next();
					String paraName = propertyEl.getAttributeValue("name");
					String paraValue = propertyEl.getText();
					Vector vt = new Vector();
					vt.addElement(paraName);
					vt.addElement(paraValue);
					xParameter.addParameter(vt);
				}
				
				xProject.setXParameter(xParameter);
				tree.addNode(treePath,xParameter,xParameter.getName());
			}
			
			childrenList = rootEl.getChildren("XTerminalGroup");
			it = childrenList.iterator();
			while(it.hasNext()){
				Element el = (Element) it.next();
				XTerminalGroup xTerminalGroup = new XTerminalGroup();
				setObjectFields(el, xTerminalGroup);
				TreePath treePathTerminal = tree.addNode(treePath,xTerminalGroup,xTerminalGroup.getName());
				xProject.setXTerminalGroup(xTerminalGroup);
				
				List tmpList = el.getChildren("XTerminal");
				Iterator tmpIterator = tmpList.iterator();
				while(tmpIterator.hasNext()){
					Element terminalEl = (Element) tmpIterator.next();
					XTerminal xTerminal = new XTerminal();
					
					setObjectFields(terminalEl, xTerminal);
					((XTerminalGroup)tree.getBeanByPath(treePathTerminal)).add(xTerminal);
					tree.addNode(treePathTerminal, xTerminal, xTerminal.getName());
					//color
					if (xTerminal.getDefinedColor().length()>0){
			    		Color color = new Color(Integer.parseInt(xTerminal.getDefinedColor()));
						TreePath xTerminalPath = tree.selectNodeForObject(xTerminal);
						tree.setNodeColor(xTerminalPath, color);
			    	}
				}
			}
			
			childrenList = rootEl.getChildren("XWork");
			it = childrenList.iterator();
			while(it.hasNext()){
				Element el = (Element) it.next();
				XWork xWork = new XWork();
				
				setObjectFields(el, xWork);
				TreePath treePathWork = tree.addNode(treePath,xWork,xWork.getName());
				xProject.setXWork(xWork);
				
				List tmpList = el.getChildren();
				Iterator tmpIterator = tmpList.iterator();
				while(tmpIterator.hasNext()){
					Element taskEl = (Element) tmpIterator.next();
					String elementName = taskEl.getName();
					if (elementName.equalsIgnoreCase("xtask")){
						XTask xTask = new XTask();
						setObjectFields(taskEl, xTask);
						XTerminal tmpXTerminal = xTask.resetTerminal(tree.getProjectBean().getXTerminalGroup());
						xWork.add(xTask);
						
						TreePath treePathTask = tree.addNode(treePathWork, xTask, xTask.getName());
						//color
						if (tmpXTerminal!=null)
		    			if (tmpXTerminal.getDefinedColor().length()>0){
		    				Color color = new Color(Integer.parseInt(tmpXTerminal.getDefinedColor()));
		    				TreePath xTaskPath = tree.selectNodeForObject(xTask);
		    				tree.setNodeColor(xTaskPath, color);
		    			}
						List assertionList = taskEl.getChildren("XAssertion");
						Iterator assertionIterator = assertionList.iterator();
						while(assertionIterator.hasNext()){
							Element assertionEl = (Element) assertionIterator.next();
							XAssertion xAssertion = new XAssertion();
							
							setObjectFields(assertionEl, xAssertion);
							tree.addNode(treePathTask, xAssertion, xAssertion.getName());
							xTask.addAssertion(xAssertion);
						}
					}else if(elementName.equalsIgnoreCase("XTimer")){
						XTimer xTimer = new XTimer();
						setObjectFields(taskEl, xTimer);
						
						XTerminal tmpXTerminal = xTimer.resetTerminal(tree.getProjectBean().getXTerminalGroup());
						
						TreePath treePathTask = tree.addNode(treePathWork, xTimer, xTimer.getName());
						xWork.add(xTimer);
						
						//color
						if (tmpXTerminal!=null)
		    			if (tmpXTerminal.getDefinedColor().length()>0){
		    				Color color = new Color(Integer.parseInt(tmpXTerminal.getDefinedColor()));
		    				TreePath xTaskPath = tree.selectNodeForObject(xTimer);
		    				tree.setNodeColor(xTaskPath, color);
		    			}
						
					}else if (elementName.equalsIgnoreCase("XBreakPoint")){
						XBreakPoint xBreakPoint = new XBreakPoint();
						setObjectFields(taskEl, xBreakPoint);
						
						TreePath treePathTask = tree.addNode(treePathWork, xBreakPoint, xBreakPoint.getName());
						xWork.add(xBreakPoint);
					}else if (elementName.equalsIgnoreCase("XNotification")){
						XNotification xNotification = new XNotification();
						setObjectFields(taskEl, xNotification);
						
						TreePath treePathTask = tree.addNode(treePathWork, xNotification, xNotification.getName());
						xWork.add(xNotification);
					}
				}
			}
			
			return tree;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	*/
	private static void setObjectFields(Element el, XNode nodeObject){
		List stringList = el.getChildren("stringProp");
		Iterator stringIterator = stringList.iterator();
		while(stringIterator.hasNext()){
			Element propertyEl = (Element) stringIterator.next();
			String fieldName = propertyEl.getAttributeValue("name");
			String fieldValue = propertyEl.getText();
			nodeObject.setPropertyValue(fieldName, fieldValue);
		}
		
		List boolList = el.getChildren("boolProp");
		Iterator boolIterator = boolList.iterator();
		while(boolIterator.hasNext()){
			Element propertyEl = (Element) boolIterator.next();
			String fieldName = propertyEl.getAttributeValue("name");
			String fieldValue = propertyEl.getText();
			nodeObject.setPropertyValue(fieldName, new Boolean(fieldValue));
		}
	}
}
