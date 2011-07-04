package lx.ghm.xelerator.gui.tree;

import java.awt.Color;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import lx.ghm.xelerator.XProject;
import lx.ghm.xelerator.XProjectsRoot;

public class XTree extends JTree { 
	public final static Color WARNING_COLOR = Color.RED;
	public final static Color NORMAL_COLOR = Color.BLACK;
	
	private Hashtable nodesMap = new Hashtable();
	
	public XTree() {
		super();
		setRowHeight(0);
		setRootVisible(false);
		setShowsRootHandles(true);
		XBeanNode projNode = new XBeanNode(new XProject(), "Xelerator Project");
		
		XBeanNode projRootNode = new XBeanNode(new XProjectsRoot(), "Projects Root");
		nodesMap.put(projRootNode.bean, projRootNode);

		this.setModel(new DefaultTreeModel(projRootNode));
		this.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		this.setCellRenderer(new XTreeRenderer());
		this.setSelectionRow(0);
	}
	
	public XProjectsRoot getRoot(){
		XBeanNode root = (XBeanNode) this.getModel().getRoot();
		return (XProjectsRoot) root.getBean();
	}
//	public XProject getProjectBean() {
//		XBeanNode root = (XBeanNode) this.getModel().getRoot();
//		XProjectsRoot projectsRoot = (XProjectsRoot)root.getBean();
//		return projectsRoot.getCurrentXProject();
//	}
	
	public void clearTreeColor() {
		DefaultTreeModel model = (DefaultTreeModel) getModel();
		XBeanNode root = (XBeanNode) model.getRoot();
		for (Enumeration e = root.breadthFirstEnumeration(); e.hasMoreElements();) {
			XBeanNode node = (XBeanNode) e.nextElement();
			node.setBgColor(null);
		}
		this.repaint();
	}
        
	public void setNodeColor(TreePath path,Color color){
		XBeanNode node = (XBeanNode) path.getLastPathComponent();
		node.setColor(color);
	}
	public void setNodeBackgroundColor(TreePath path,Color color){
		XBeanNode node = (XBeanNode) path.getLastPathComponent();
		node.setBgColor(color);
	}
	public void clearNodeBackgroundColor(TreePath path){
		XBeanNode node = (XBeanNode) path.getLastPathComponent();
		node.setBgColor(null);
	}
	public void setNodeBolder(TreePath path){
		XBeanNode node = (XBeanNode) path.getLastPathComponent();
		node.setBolder(true);
	}
	public void clearNodeBolder(TreePath path){
		XBeanNode node = (XBeanNode) path.getLastPathComponent();
		node.setBolder(false);
	}
	
	public TreePath addNode(TreePath path, Object bean, String name) {
		XBeanNode parent = (XBeanNode) path.getLastPathComponent();
		XBeanNode node = new XBeanNode(bean, name);
		
		nodesMap.put(bean, node);
		parent.add(node);
		
		path = path.pathByAddingChild(node);
		((DefaultTreeModel) getModel()).reload(parent);
		return path;
	}
	
	public void insertNode(TreePath path, Object bean, String name){
		XBeanNode neighbor = (XBeanNode) path.getLastPathComponent();
		XBeanNode parent = (XBeanNode)neighbor.getParent();
		XBeanNode node = new XBeanNode(bean, name);
		
		nodesMap.put(bean, node);
		int childIndex = parent.getIndex(neighbor) +1;
		parent.insert(node, childIndex);
		((DefaultTreeModel) getModel()).reload(parent);
	}
	
	public void removeNode(TreePath path) {
		XBeanNode node = (XBeanNode) path.getLastPathComponent();
		if (node != getModel().getRoot()) {
			XBeanNode parent = (XBeanNode) node.getParent();
			node.removeFromParent();
			((DefaultTreeModel) getModel()).reload(parent);
//			setSelectionPath(path.getParentPath());
			nodesMap.remove(node.getBean());
		}
	}
	
	public TreePath selectNodeForObject(Object bean) {
		DefaultTreeModel model = (DefaultTreeModel) getModel();
		XBeanNode root = (XBeanNode) model.getRoot();
		for (Enumeration e = root.breadthFirstEnumeration(); e.hasMoreElements();) {
			XBeanNode node = (XBeanNode) e.nextElement();
			if (node.getBean().equals(bean)) {
				TreeNode[] path = model.getPathToRoot(node);
				TreePath p = new TreePath(path);
				setSelectionPath(p);
				return p;
			}
		}
		return null;
	}
	
	public TreePath getNodeByClassName(String className){
		DefaultTreeModel model = (DefaultTreeModel) getModel();
		XBeanNode root = (XBeanNode) model.getRoot();
		for (Enumeration e = root.breadthFirstEnumeration(); e.hasMoreElements();) {
			XBeanNode node = (XBeanNode) e.nextElement();
			if (node.getBean().getClass().getName().endsWith(className)) {
				TreeNode[] path = model.getPathToRoot(node);
				TreePath p = new TreePath(path);
				setSelectionPath(p);
				return p;
			}
		}
		return null;
	}
	
	public Object getCurrentSelectedBean(){
		XBeanNode node = (XBeanNode)getLastSelectedPathComponent();
		if ( node == null) return null;
		return node.getBean();
	}
	
	public XBeanNode getSelectedNode(TreePath path){
		return (XBeanNode) path.getLastPathComponent();
	}
	
	public Object getSelectedBean(TreePath path) {
		XBeanNode node = (XBeanNode) path.getLastPathComponent();
		return node.getBean();
	}
	
	public Object getBeanByPath(TreePath path){
		XBeanNode node = (XBeanNode) path.getLastPathComponent();
		return node.getBean();
	}
	
	public TreePath selectNodeByBean(Object bean) {
		XBeanNode node = (XBeanNode) nodesMap.get(bean);
		TreeNode[] path = ((DefaultTreeModel) getModel()).getPathToRoot(node);
		TreePath p = new TreePath(path);
		setSelectionPath(p);
		return p;
	}
	

	public void renameNode(Object bean, String oldName, String newName) {
		XBeanNode node = (XBeanNode) nodesMap.remove(bean);
		node.name = newName;
		nodesMap.put(bean, node);
		((DefaultTreeModel) getModel()).reload(node);
	}
	
	public void expandAll() {
		TreeNode root = (TreeNode) this.getModel().getRoot();
		// Traverse tree from root
		expandAll(new TreePath(root), true);
	}

	public void expandAll(TreePath parent, boolean expand) {
		// Traverse children
		TreeNode node = (TreeNode) parent.getLastPathComponent();
		if (node.getChildCount() >= 0) {
			for (Enumeration e = node.children(); e.hasMoreElements();) {
				TreeNode n = (TreeNode) e.nextElement();
				TreePath path = parent.pathByAddingChild(n);
				expandAll(path, expand);
			}
		}
		// Expansion or collapse must be done bottom-up
		if (expand) {
			this.expandPath(parent);
		} else {
			this.collapsePath(parent);
		}
	}
}
