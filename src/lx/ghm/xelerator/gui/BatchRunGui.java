/*
 * BatchRunGui.java
 *
 * Created on June 20, 2008, 6:54 AM
 */

package lx.ghm.xelerator.gui;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import javax.swing.DefaultListModel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import lx.ghm.xelerator.XProject;
import lx.ghm.xelerator.XTerminal;
import lx.ghm.xelerator.XTerminalGroup;
import lx.ghm.xelerator.processor.XRunner;

/**
 *
 * @author  xunli
 */
public class BatchRunGui extends javax.swing.JPanel {
    private ArrayList xProjects;
    private XProject currentXProject;
    private MainGui gui;
    private boolean isRunning;
    private Hashtable runnerHT = new Hashtable();
    
    /** Creates new form BatchRunGui */
    public BatchRunGui() {
        initComponents();
        jTabbedPane1.removeAll();
    }
    
    public void setXGui(MainGui gui){
    	this.gui = gui;
    }
    
    private void listItemChange(){
    	Object item = jList1.getSelectedValue();
    	if(item == null ) 
    		return;
    	currentXProject = (XProject)item;
    	setTabbedPane(currentXProject);
    }
    
    private void setTabbedPane(XProject xProject){
    	jTabbedPane1.removeAll();
    	XTerminalGroup xTerminalGroup = xProject.getXTerminalGroup();
    	for(int i=0; i<xTerminalGroup.size(); i++){
    		XTerminal xTerminal = xTerminalGroup.get(i);
    		jTabbedPane1.addTab(xTerminal.getName(), xTerminal.getTerminal());
    		if(i==0)
    			xTerminal.getTerminal().setFocus();
    	}
    }
    
    private void setActiveTab(int index){
    	XTerminalGroup xTerminalGroup = currentXProject.getXTerminalGroup();
    	if(xTerminalGroup.size()==0||index<0||index>xTerminalGroup.size())
    		return;
    	xTerminalGroup.get(index).getTerminal().setFocus();
    }
    
    public void setXProjects(ArrayList xProjects){
    	this.xProjects= xProjects;
    	DefaultListModel listModel = new DefaultListModel();

    	for(int i=0;i<xProjects.size();i++){
    		XProject xProject = (XProject)xProjects.get(i);
    		listModel.add(i, xProject);
    		if (i==0)
    			currentXProject = xProject;
    	}
    	jList1.setModel(listModel);
    	jList1.addListSelectionListener(new ListSelectionListener() {
    		public void valueChanged(ListSelectionEvent e) {
    			listItemChange();
    		}
    	});
    	
    	jTabbedPane1.addChangeListener(new ChangeListener() { 
			public void stateChanged(ChangeEvent e) {
				if(e.getSource() == jTabbedPane1){
					int i = ((JTabbedPane) e.getSource()).getSelectedIndex();
					setActiveTab(i);
				}
			}
    	});
    	setTabbedPane(currentXProject);
    }
    
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    	if( isRunning == false){
    		// Start Run
    		for(int i=0;i<xProjects.size();i++){
        		XProject xProject = (XProject)xProjects.get(i);
        		XRunner xRunner = new XRunner(gui, xProject);
        		xRunner.runAsDeamon(true);
        		xRunner.start();
        		runnerHT.put(xProject, xRunner);    
        	}
    		
    		this.jButton1.setText("Stop Run");
    		isRunning = true;
    	}else{
    		// Stop Run
    		Iterator it = runnerHT.keySet().iterator();
    		while(it.hasNext()){
    			XRunner xRunner = (XRunner)runnerHT.get(it.next());
    			xRunner.stopRun();
    			xRunner.runAsDeamon(false);
    		}
    		this.jButton1.setText("Start Run");
    		gui.getTree().clearTreeColor();
    		isRunning = false;
    	}
    }//GEN-LAST:event_jButton1ActionPerformed
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane3 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jButton1 = new javax.swing.JButton();

        jScrollPane3.setViewportView(jTextPane1);

        jSplitPane1.setDividerLocation(140);

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 503, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 366, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("tab1", jPanel1);

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 503, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 366, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("tab2", jPanel2);

        jScrollPane2.setViewportView(jTabbedPane1);

        jSplitPane1.setRightComponent(jScrollPane2);

        jList1.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(jList1);

        jSplitPane1.setLeftComponent(jScrollPane1);

        jButton1.setText("Start Run");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jButton1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 556, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jSplitPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 556, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(jSplitPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jButton1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 40, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JList jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextPane jTextPane1;
    // End of variables declaration//GEN-END:variables
    
}
