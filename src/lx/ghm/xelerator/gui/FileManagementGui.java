/*
 * FileManagementGui.java
 *
 * Created on May 28, 2008, 8:39 AM
 */
package lx.ghm.xelerator.gui;


import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import lx.ghm.xelerator.share.DBConnection;
import lx.ghm.xelerator.share.IShareNode;
import lx.ghm.xelerator.share.XDir;
import lx.ghm.xelerator.share.XFile;
import lx.ghm.xelerator.share.XShareTree;

public class FileManagementGui extends javax.swing.JFrame {
	private static final String iconFile ="/lx/ghm/xelerator/resource/FileManagement.png";
	
    /** Creates new form FileManagementGui */
    public FileManagementGui() {
        initComponents();
        initTree();
        this.setIconImage(new ImageIcon(this.getClass().getResource(iconFile)).getImage());
    }
    
    private TreePath currentTreePath=null;
    private XShareTree shareTree = null;
    private MouseListener treeListener = null;
    
    private void initTree() {
        try {
            this.shareTree = new XShareTree();
            XDir xDir = shareTree.getRoot();
            DefaultMutableTreeNode root = buildTree(xDir);
            DefaultTreeModel model = new DefaultTreeModel(root);
            jTree.setModel(model);
            jTree.setCellRenderer(new MyTreeCellRenderer());
            jTree.addMouseListener(this.getTreeListener());
            expandAll(new TreePath(root), true);
        } catch (Exception e) {
            DBConnection.getInstance().closeDB();
            e.printStackTrace();
        }
    }
	private void expandAll(TreePath parent, boolean expand) {
		// Traverse children
		TreeNode node = (TreeNode) parent.getLastPathComponent();
		if (node.getChildCount() >= 0) {
			for (Enumeration e = node.children(); e.hasMoreElements();) {
				TreeNode n = (TreeNode) e.nextElement();
				TreePath path = parent.pathByAddingChild(n);
				expandAll(path, expand);
			}
		}
		if (expand) {
			jTree.expandPath(parent);
		} else {
			jTree.collapsePath(parent);
		}
	}
    public MouseListener getTreeListener() {
		if (treeListener == null) {
			treeListener = new MouseAdapter() {
				public void mouseClicked(MouseEvent evt) {
					JTree tree = (JTree) evt.getSource();
					TreePath path = tree.getPathForLocation(evt.getX(), evt.getY());

					if (path != null) {
						int selRow = tree.getRowForLocation(evt.getX(), evt.getY());
						if (selRow != -1) {
							if(evt.getModifiers() == evt.BUTTON3_MASK){
								jPopupMenu.show(evt.getComponent(),  evt.getX(),evt.getY());
							}
						}
					}
				}
			};
		}
		return treeListener;
	}
    private Object getSelectedObject(){
    	if(currentTreePath == null)
    		return null;
    	DefaultMutableTreeNode node = (DefaultMutableTreeNode)currentTreePath.getLastPathComponent();
    	Object currentObj = node.getUserObject();
    	return currentObj;
    }
    
    private void addXFileOnTree(XFile xFile){
    	try{
	    	if (currentTreePath == null){
	    		JOptionPane.showMessageDialog(this, "Please upload a XFile under a specific XDir!", "Xelerator Warning",JOptionPane.INFORMATION_MESSAGE);
	    		return;
	    	}
	    	DefaultMutableTreeNode node = new DefaultMutableTreeNode(xFile);
	    	DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode)currentTreePath.getLastPathComponent();
	    	parentNode.add(node);
	    	((DefaultTreeModel)jTree.getModel()).reload(parentNode);
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}
    }
    
    private void addXDirOnTree(XDir xDir, DefaultMutableTreeNode parentNode){
    	try{
	    	if (currentTreePath == null){
	    		JOptionPane.showMessageDialog(this, "Please add a XDir under a specific XDir!", "Xelerator Warning",JOptionPane.INFORMATION_MESSAGE);
	    		return;
	    	}
	    	DefaultMutableTreeNode node = new DefaultMutableTreeNode(xDir);
	    	node.add(new DefaultMutableTreeNode("empty"));
	    	parentNode.add(node);
	    	((DefaultTreeModel)jTree.getModel()).reload(parentNode);
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}
    }
    
    private DefaultMutableTreeNode buildTree(XDir parentDir) {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(parentDir);
        for (int i = 0; i < parentDir.size(); i++) {
            XDir dir = parentDir.get(i);
            node.add(buildTree(dir));
        }
        for (int j=0; j < parentDir.filesSize();j++)
        	node.add(new DefaultMutableTreeNode(parentDir.getXFile(j)));
        
        if ( parentDir.size()==0 && parentDir.filesSize()==0)
        	node.add(new DefaultMutableTreeNode("empty"));
        
        return node;
    }

    private void uploadXFile() {
		try {
			Object selectedNode = getSelectedObject();
			XDir selectedXDir = null;
			if ( currentTreePath == null || selectedNode instanceof XFile){
				JOptionPane.showMessageDialog(this, "Please upload a XFile under a specific XDir!", "Xelerator Warning",JOptionPane.INFORMATION_MESSAGE);
				return;
			}else if ( selectedNode instanceof XDir)
				selectedXDir = (XDir)selectedNode;
			
			String message = "Do you want to upload your XFile under the directory: " + selectedXDir.getName();
			int response = JOptionPane.showConfirmDialog(this, message, message, JOptionPane.YES_NO_OPTION);
			if (response == JOptionPane.NO_OPTION)
				return;
			
			JFileChooser fDialog = new JFileChooser(".");
			fDialog.setDialogTitle("Select Uploaeded Xelerator File:");
			fDialog.setDialogType(JFileChooser.OPEN_DIALOG);
			int result = fDialog.showDialog(this,"Upload");
			if (result == JFileChooser.APPROVE_OPTION) {
				String fname = fDialog.getSelectedFile().getAbsolutePath();
				if (fname.length() <= 0) {
					return;
				}
				jDialog1.setTitle("Complete the information for uploaded XFile!");
				jDialog1.setSize(400,300);
				jDialog1.setLocation(300, 300);
				jDialog1.setModal(true);
				jDialog1.setVisible(true);

				String keywords = jTFDlgKeywords.getText();
				String description = jTADlgDescription.getText();
				String content = readXFile(fname);
				fname = fDialog.getSelectedFile().getName();
				XFile xFile = new XFile();
				xFile.setFileName(fname);
				xFile.setKeywords(keywords);
				xFile.setDescription(description);
				xFile.setContent(content);
				xFile.setDirID(selectedXDir.getId());
				xFile.setUploadDate(new Date(System.currentTimeMillis()));
				
				this.shareTree.addFile(xFile, selectedXDir.getId());
				this.addXFileOnTree(xFile);
				
				jTFDlgKeywords.setText("");
				jTADlgDescription.setText("");
				JOptionPane.showMessageDialog(this, "Upload XFile done!","Xelerator File Uploaded!", JOptionPane.INFORMATION_MESSAGE);
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Upload XFile error!", "Xelerator Error",JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		}
	}
    
    private void downloadXFile(){
    	try{
    		Object selectedNode = getSelectedObject();
			XFile selectedXFile = null;
			if ( selectedNode instanceof XDir){
				JOptionPane.showMessageDialog(this, "Please select a XFile to download!", "Xelerator Information",JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			if ( selectedNode instanceof XFile){
				selectedXFile = (XFile)selectedNode;
				JFileChooser fDialog = new JFileChooser(".");
				fDialog.setDialogTitle("Select Saved Xelerator File:");
				fDialog.setDialogType(JFileChooser.SAVE_DIALOG);
				int result = fDialog.showOpenDialog(this);
				if (result == JFileChooser.APPROVE_OPTION) {
					String fname = fDialog.getSelectedFile().getAbsolutePath();
					this.saveXFile(fname, selectedXFile.getContent());
				}
				JOptionPane.showMessageDialog(this, "Download XFile done!","Xelerator File Saved!", JOptionPane.INFORMATION_MESSAGE);
			}
    	} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Download XFile error!", "Xelerator Error",JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		}
    }
    
    private String readXFile(String fileName) throws IOException {
		FileReader fr = new FileReader(fileName);
		BufferedReader br = new BufferedReader(fr);
		String line = br.readLine();
		while (br.ready()) { 
			line = line+ br.readLine();
			line = line + "\n";
		}
		br.close(); 
		fr.close(); 
		return line;
	}
    
    private void saveXFile(String fileName, String fileContent) throws IOException {
    	FileWriter fw = new FileWriter(fileName);
    	BufferedWriter bw = new BufferedWriter(fw);
    	bw.write(fileContent);
    	bw.flush();
    	bw.close();
    	fw.close();
    }
    
    private void jTreeValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_jTreeValueChanged
    	currentTreePath = evt.getPath();
    	Object selectedNode = getSelectedObject();
    	XFile selectedXFile = null;
    	if ( selectedNode instanceof XFile){
    		selectedXFile = (XFile)selectedNode;
			jTFKeywords.setText(selectedXFile.getKeywords());
			jTADescription.setText(selectedXFile.getDescription());
    	} else{
    		jTFKeywords.setText("");
    		jTADescription.setText("");
    	}
    }//GEN-LAST:event_jTreeValueChanged
    
	private void jBtnUploadActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBtnUploadActionPerformed
		uploadXFile();
	}// GEN-LAST:event_jBtnUploadActionPerformed

	private void jMenuItemDownloadActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItemDownloadActionPerformed
		downloadXFile();
	}// GEN-LAST:event_jMenuItemDownloadActionPerformed

	private void jMenuItemUploadActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItemUploadActionPerformed
		uploadXFile();
	}// GEN-LAST:event_jMenuItemUploadActionPerformed

	private void jBtnDlgOKActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBtnDlgOKActionPerformed
		jDialog1.setVisible(false);
	}// GEN-LAST:event_jBtnDlgOKActionPerformed
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    	jDialog2.setVisible(false);
    }//GEN-LAST:event_jButton1ActionPerformed
    
    private void jMenuItemDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemDeleteActionPerformed
    	try {
			Object selectedNode = getSelectedObject();
			if ( currentTreePath == null || selectedNode == null){
				JOptionPane.showMessageDialog(this, "Please choose a XDir or XFile to delete!", "Xelerator Warning",JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			if ( currentTreePath.getLastPathComponent() == jTree.getModel().getRoot()){
				JOptionPane.showMessageDialog(this, "Cannot delete the root XDir!", "Xelerator Warning",JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			if ( selectedNode instanceof XDir){
				XDir xDir = (XDir)selectedNode;
				if (xDir.filesSize()>0){
					JOptionPane.showMessageDialog(this, "Cannot delete a not empty XDir!", "Xelerator Warning",JOptionPane.WARNING_MESSAGE);
					return;
				}
				this.shareTree.deleteDIR(xDir);
				DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode)currentTreePath.getLastPathComponent();
				DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode)treeNode.getParent();
				treeNode.removeFromParent();
				((DefaultTreeModel) jTree.getModel()).reload(parentNode);
				jTree.expandPath(currentTreePath);
				
			}else if ( selectedNode instanceof XFile){
				XFile xFile = (XFile)selectedNode;
				
				DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode)currentTreePath.getLastPathComponent();
				DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode)treeNode.getParent();
				XDir xDir = (XDir)parentNode.getUserObject();
				this.shareTree.deleteFile(xFile, xDir.getId());
				
				treeNode.removeFromParent();
				((DefaultTreeModel) jTree.getModel()).reload(parentNode);
				jTree.expandPath(currentTreePath);
			}
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}
    }//GEN-LAST:event_jMenuItemDeleteActionPerformed

    private void jBtnSaveXFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnSaveXFileActionPerformed
    	try {
			Object selectedNode = getSelectedObject();
			if ( currentTreePath == null || selectedNode == null || selectedNode instanceof XDir)
				return;
			
			if ( selectedNode instanceof XFile){
				XFile xFile = (XFile)selectedNode;
				xFile.setKeywords(jTFKeywords.getText());
				xFile.setDescription(jTADescription.getText());
				this.shareTree.updateFile(xFile);
			}
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}
    }//GEN-LAST:event_jBtnSaveXFileActionPerformed

    private void jMenuItemAddXDirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemAddXDirActionPerformed
    	try {
			Object selectedNode = getSelectedObject();
			if ( currentTreePath == null || selectedNode == null)
				return;
			XDir parentDir = null;
			DefaultMutableTreeNode treeNode = null;
			DefaultMutableTreeNode parentNode = null;
			if ( selectedNode instanceof XFile){
				treeNode = (DefaultMutableTreeNode)currentTreePath.getLastPathComponent();
				parentNode = (DefaultMutableTreeNode)treeNode.getParent();
				parentDir = (XDir)parentNode.getUserObject();
			}else if (selectedNode instanceof XDir){
				parentNode = (DefaultMutableTreeNode)currentTreePath.getLastPathComponent();
				parentDir = (XDir)selectedNode;
			}
			
			jDialog2.setSize(280,80);
			jDialog2.setLocation(300, 300);
			jDialog2.setModal(true);
			jDialog2.setVisible(true);
			
			String name = jTFDlgXDirName.getText();	
			if (name.length()<=0)
				return;
			XDir newXDir = new XDir();
			newXDir.setName(name);
			newXDir.setParent(parentDir);
			newXDir.setParentID(parentDir.getId());
			int xDirID = this.shareTree.addDIR(newXDir);
			newXDir.setId(xDirID);
			
			this.addXDirOnTree(newXDir, parentNode);
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}
    }//GEN-LAST:event_jMenuItemAddXDirActionPerformed
    
    private void jTFSearchStringKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTFSearchStringKeyPressed
    	this.searchTree();
    }//GEN-LAST:event_jTFSearchStringKeyPressed
    
	private void jBtnSearchActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBtnBrowseActionPerformed
		this.searchTree();
	}// GEN-LAST:event_jBtnBrowseActionPerformed
	
	private void searchTree(){
		try{
			String searchWords = this.jTFSearchString.getText();
			
			DefaultTreeModel model = (DefaultTreeModel) jTree.getModel();
			DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
			for (Enumeration e = root.breadthFirstEnumeration(); e.hasMoreElements();) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.nextElement();
				if(node.getUserObject() instanceof IShareNode){
					IShareNode currentObj = (IShareNode)(node.getUserObject());
					currentObj.setColor(Color.BLACK);
					
					TreeNode[] path = model.getPathToRoot(node);
					TreePath p = new TreePath(path);
					int row = jTree.getRowForPath(p);
					MyTreeCellRenderer r = (MyTreeCellRenderer)jTree.getCellRenderer();
					r.getTreeCellRendererComponent(jTree, node, true, true, true,row, true);
					r.repaint();
				}
			}
			jTree.repaint();
			if ( searchWords.length() <=0)
				return ;
			Hashtable keywordsHT = new Hashtable();
			String[] keywords = searchWords.split(" ");
			for(int i=0;i<keywords.length;i++){
				keywordsHT.put(keywords[i], "1");
			}
			
			for (Enumeration e = root.breadthFirstEnumeration(); e.hasMoreElements();) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.nextElement();
				Object currentObj = node.getUserObject();
				
				boolean bMatched = false;
				if (currentObj instanceof XDir){
					for(int i=0;i<keywords.length;i++){
						if (currentObj.toString().indexOf(keywords[i])>=0){
							bMatched = true;
							continue;
						}
					}
				}else if ( currentObj instanceof XFile){
					XFile tmpFile = (XFile)currentObj;
					for(int i=0;i<keywords.length;i++){
						if (currentObj.toString().indexOf(keywords[i])>=0){
							bMatched = true;
							continue;
						}
					}
					if (!bMatched){
						String[] mywords = tmpFile.getKeywords().split(",");
						for ( int i=0;i<mywords.length;i++){
							if ( keywordsHT.containsKey(mywords[i])){
								bMatched = true;
								continue;
							}
						}
					}
				}
				
				TreeNode[] path = model.getPathToRoot(node);
				TreePath p = new TreePath(path);
				
				if (bMatched){
					IShareNode obj = (IShareNode)currentObj;
					obj.setColor(Color.ORANGE);
//					((DefaultTreeModel) jTree.getModel()).reload(node.getParent());
					jTree.expandPath(p);
				}
			}
			jTree.repaint();
			}catch(Exception ex){
				ex.printStackTrace();
			}
	}
	class MyTreeCellRenderer extends DefaultTreeCellRenderer {
		private static final long serialVersionUID = 1L;

		public Component getTreeCellRendererComponent(JTree tree, Object value,
				boolean selected, boolean expanded, boolean leaf, int row,
				boolean hasFocus) {
			try{
				JLabel label = (JLabel)super.getTreeCellRendererComponent(tree, value, selected, expanded,leaf, row, hasFocus);
				DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
				if(node.getUserObject() instanceof IShareNode){
					IShareNode obj = (IShareNode)(node.getUserObject());
					if (obj.getColor()!=null){
						label.setForeground(obj.getColor());
//						label.setOpaque(true);
//						label.setBackground(Color.GREEN);
					}
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}
			return this;
		}
	}

    /**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu = new javax.swing.JPopupMenu();
        jMenuItemAddXDir = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JSeparator();
        jMenuItemDownload = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        jMenuItemUpload = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JSeparator();
        jMenuItemDelete = new javax.swing.JMenuItem();
        jDialog1 = new javax.swing.JDialog();
        jLabel4 = new javax.swing.JLabel();
        jTFDlgKeywords = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTADlgDescription = new javax.swing.JTextArea();
        jBtnDlgOK = new javax.swing.JButton();
        jDialog2 = new javax.swing.JDialog();
        jLabel6 = new javax.swing.JLabel();
        jTFDlgXDirName = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTFSearchString = new javax.swing.JTextField();
        jBtnSearch = new javax.swing.JButton();
        jSplitPane2 = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTree = new javax.swing.JTree();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jTFKeywords = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTADescription = new javax.swing.JTextArea();
        jBtnSaveXFile = new javax.swing.JButton();
        jBtnUpload = new javax.swing.JButton();

        jMenuItemAddXDir.setText("Add Directory");
        jMenuItemAddXDir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAddXDirActionPerformed(evt);
            }
        });
        jPopupMenu.add(jMenuItemAddXDir);
        jPopupMenu.add(jSeparator3);

        jMenuItemDownload.setText("Download XFile");
        jMenuItemDownload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemDownloadActionPerformed(evt);
            }
        });
        jPopupMenu.add(jMenuItemDownload);
        jPopupMenu.add(jSeparator1);

        jMenuItemUpload.setText("Upload XFile");
        jMenuItemUpload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemUploadActionPerformed(evt);
            }
        });
        jPopupMenu.add(jMenuItemUpload);
        jPopupMenu.add(jSeparator2);

        jMenuItemDelete.setText("Delete");
        jMenuItemDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemDeleteActionPerformed(evt);
            }
        });
        jPopupMenu.add(jMenuItemDelete);

        jLabel4.setText("Keywords");

        jLabel5.setText("Description");

        jTADlgDescription.setColumns(20);
        jTADlgDescription.setRows(5);
        jScrollPane2.setViewportView(jTADlgDescription);

        jBtnDlgOK.setText("OK");
        jBtnDlgOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnDlgOKActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jDialog1Layout = new org.jdesktop.layout.GroupLayout(jDialog1.getContentPane());
        jDialog1.getContentPane().setLayout(jDialog1Layout);
        jDialog1Layout.setHorizontalGroup(
            jDialog1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jDialog1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jDialog1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel4)
                    .add(jLabel5))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jDialog1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 371, Short.MAX_VALUE)
                    .add(jBtnDlgOK)
                    .add(jTFDlgKeywords, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 371, Short.MAX_VALUE))
                .addContainerGap())
        );
        jDialog1Layout.setVerticalGroup(
            jDialog1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jDialog1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jDialog1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel4)
                    .add(jTFDlgKeywords, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(18, 18, 18)
                .add(jDialog1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel5)
                    .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 289, Short.MAX_VALUE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jBtnDlgOK)
                .addContainerGap())
        );

        jDialog2.setTitle("Input XDirectory Name:");

        jLabel6.setText("XDirectory Name:");

        jButton1.setText("OK");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jDialog2Layout = new org.jdesktop.layout.GroupLayout(jDialog2.getContentPane());
        jDialog2.getContentPane().setLayout(jDialog2Layout);
        jDialog2Layout.setHorizontalGroup(
            jDialog2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jDialog2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel6)
                .add(18, 18, 18)
                .add(jTFDlgXDirName, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jButton1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 57, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jDialog2Layout.setVerticalGroup(
            jDialog2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jDialog2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jDialog2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel6)
                    .add(jButton1)
                    .add(jTFDlgXDirName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Xelerator File Management");

        jLabel1.setText("Input keywords to search the Xelerator File you want:");

        jTFSearchString.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTFSearchStringKeyPressed(evt);
            }
        });

        jBtnSearch.setText("Search");
        jBtnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnSearchActionPerformed(evt);
            }
        });

        jSplitPane2.setDividerLocation(300);

        jTree.setScrollsOnExpand(false);
        jTree.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                jTreeValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jTree);

        jSplitPane2.setLeftComponent(jScrollPane1);

        jLabel2.setText("Keywords:");

        jTFKeywords.setToolTipText("use comma to add more than one keywords");

        jLabel3.setText("Description:");

        jTADescription.setColumns(20);
        jTADescription.setRows(5);
        jScrollPane3.setViewportView(jTADescription);

        jBtnSaveXFile.setText("Save");
        jBtnSaveXFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnSaveXFileActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(jLabel2)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(jTFKeywords, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 251, Short.MAX_VALUE))
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(jLabel3)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jBtnSaveXFile)
                            .add(jScrollPane3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 251, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel2)
                    .add(jTFKeywords, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel3)
                    .add(jScrollPane3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 354, Short.MAX_VALUE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jBtnSaveXFile)
                .addContainerGap())
        );

        jSplitPane2.setRightComponent(jPanel2);

        jBtnUpload.setText("Upload");
        jBtnUpload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnUploadActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jSplitPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 638, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jBtnUpload)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(jLabel1)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(jTFSearchString, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jBtnSearch)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(jBtnSearch)
                    .add(jTFSearchString, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jSplitPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jBtnUpload)
                .addContainerGap())
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FileManagementGui().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBtnDlgOK;
    private javax.swing.JButton jBtnSaveXFile;
    private javax.swing.JButton jBtnSearch;
    private javax.swing.JButton jBtnUpload;
    private javax.swing.JButton jButton1;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JDialog jDialog2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JMenuItem jMenuItemAddXDir;
    private javax.swing.JMenuItem jMenuItemDelete;
    private javax.swing.JMenuItem jMenuItemDownload;
    private javax.swing.JMenuItem jMenuItemUpload;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPopupMenu jPopupMenu;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JTextArea jTADescription;
    private javax.swing.JTextArea jTADlgDescription;
    private javax.swing.JTextField jTFDlgKeywords;
    private javax.swing.JTextField jTFDlgXDirName;
    private javax.swing.JTextField jTFKeywords;
    private javax.swing.JTextField jTFSearchString;
    private javax.swing.JTree jTree;
    // End of variables declaration//GEN-END:variables
}
