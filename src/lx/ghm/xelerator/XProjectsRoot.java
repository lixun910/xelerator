/**
 * 
 */
package lx.ghm.xelerator;

import java.awt.Image;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

/**
 * @author xunli
 *
 */
public class XProjectsRoot extends XNode{

	/* (non-Javadoc)
	 * @see lx.ghm.xelerator.XNode#getIcon(int)
	 */
	public Image getIcon(int type) {
		return null;
	}

	private Hashtable projectsHT = new Hashtable();
	private XProject currentXProject = null;
	
	public int size(){
		return projectsHT.size();
	}
	
	public void removeXProject(XProject project){
		projectsHT.remove(String.valueOf(project.hashCode()));
	}
	public void addXProject(XProject project){
		projectsHT.put(String.valueOf(project.hashCode()), project);
	}
	
	public ArrayList getAllXProjects(){
		ArrayList xProjects = new ArrayList();
		Iterator it = projectsHT.keySet().iterator();
		while(it.hasNext()){
			xProjects.add(projectsHT.get(it.next()));
		}
		return xProjects;
	}
	
	public XProject getXProject(int projectHashCode){
		String key = String.valueOf(projectHashCode);
		if(projectsHT.containsKey(key)){
			return (XProject)projectsHT.get(key);
		}
		return null;
	}

	public XProject getCurrentXProject() {
		return currentXProject;
	}

	public void setCurrentXProject(XProject currentXProject) {
		this.currentXProject = currentXProject;
	}
}
