/*
 * ParameterGui.java
 *
 * Created on May 8, 2008, 2:24 PM
 */

package lx.ghm.xelerator.gui;

import java.util.ArrayList;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;

import lx.ghm.xelerator.XParameter;
import lx.ghm.xelerator.gui.tree.XBeanNode;

/**
 *
 * @author  xunli
 */
public class ParameterGui extends javax.swing.JPanel {
    
    /** Creates new form ParameterGui */
    public ParameterGui() {
        initComponents();
    }
    private XParameter xParameter;
    private boolean NEED_SAVE=false;
    private XBeanNode node;
    
    public void setObject(XBeanNode node){
    	this.node = node;
    	this.xParameter = (XParameter)node.getBean();
    	this.TFName.setText(xParameter.getName());
    	this.TFComment.setText(xParameter.getComment());
    	
    	jTable1.setFocusable(false);
    	DefaultTableModel tableModel = (DefaultTableModel)this.jTable1.getModel();
    	while(tableModel.getRowCount()>0){
    		tableModel.removeRow(0);
    	}
    	
    	ArrayList parameters = xParameter.getParametersList();
    	for ( int i=0; i<parameters.size();i++){
    		tableModel.addRow((Vector)parameters.get(i));
    	}
    }

    
    private void SaveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SaveBtnActionPerformed
    	TableCellEditor cellEditor = jTable1.getCellEditor();
    	if( cellEditor != null) {
    		cellEditor.stopCellEditing();
    	}
    	this.xParameter.setName(this.TFName.getText());
    	this.xParameter.setComment(this.TFComment.getText());
    	
    	DefaultTableModel tableModel = (DefaultTableModel)this.jTable1.getModel();
    	tableModel.fireTableDataChanged();
    	
    	int rowCount = tableModel.getRowCount();
    	int colCount = tableModel.getColumnCount();
    	this.xParameter.resetParameter();
    	for(int i=0;i<rowCount; i++){
    		Vector rowData = new Vector();
    		for(int j=0;j<colCount; j++){
    			rowData.addElement(tableModel.getValueAt(i, j));
    		}
    		this.xParameter.addParameter(rowData);
    	}
    	
    	this.node.name = this.TFName.getText();
    }//GEN-LAST:event_SaveBtnActionPerformed

    public void save(){
    	
    	this.xParameter.setName(this.TFName.getText());
    	this.xParameter.setComment(this.TFComment.getText());
    	
    	DefaultTableModel tableModel = (DefaultTableModel)this.jTable1.getModel();
    	int rowCount = tableModel.getRowCount();
    	int colCount = tableModel.getColumnCount();
    	this.xParameter.resetParameter();
    	for(int i=0;i<rowCount; i++){
    		Vector rowData = new Vector();
    		for(int j=0;j<colCount; j++){
    			rowData.addElement(tableModel.getValueAt(i, j));
    		}
    		this.xParameter.addParameter(rowData);
    	}
    	
    	this.node.name = this.TFName.getText();
    	
    	this.NEED_SAVE=false;
    }
    private void RemoveParameterBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RemoveParameterBtnActionPerformed
    	DefaultTableModel tableModel = (DefaultTableModel)this.jTable1.getModel();
    	tableModel.removeRow(this.jTable1.getSelectedRow());
    	
    	this.NEED_SAVE=true;
    }//GEN-LAST:event_RemoveParameterBtnActionPerformed

    private void AddParameterBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddParameterBtnActionPerformed
    	DefaultTableModel tableModel = (DefaultTableModel)this.jTable1.getModel();
    	tableModel.addRow(new String[]{"",""});
    	this.NEED_SAVE=true;
    }//GEN-LAST:event_AddParameterBtnActionPerformed
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        TFName = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        TFComment = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        AddParameterBtn = new javax.swing.JButton();
        RemoveParameterBtn = new javax.swing.JButton();
        SaveBtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 13));
        jLabel1.setText("User Parameters");

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel3.setText("Name");

        jLabel4.setText("Comment");

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel3)
                    .add(jLabel4))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(TFName, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 372, Short.MAX_VALUE)
                    .add(TFComment, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 372, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel3)
                    .add(TFName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(TFComment, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel4))
                .add(19, 19, 19))
        );

        jLabel2.setText("Parameters");

        AddParameterBtn.setText("Add Parameter");
        AddParameterBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddParameterBtnActionPerformed(evt);
            }
        });

        RemoveParameterBtn.setText("Remove Parameter");
        RemoveParameterBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RemoveParameterBtnActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel1)
                    .add(jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 445, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(AddParameterBtn)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(RemoveParameterBtn))
                    .add(jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 22, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 80, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel2)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(AddParameterBtn)
                    .add(RemoveParameterBtn))
                .addContainerGap())
        );

        SaveBtn.setText("Save");
        SaveBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SaveBtnActionPerformed(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null}
            },
            new String [] {
                "Name", "Value"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(SaveBtn, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 71, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(384, Short.MAX_VALUE))
            .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 445, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 268, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(SaveBtn)
                .addContainerGap(50, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AddParameterBtn;
    private javax.swing.JButton RemoveParameterBtn;
    private javax.swing.JButton SaveBtn;
    private javax.swing.JTextField TFComment;
    private javax.swing.JTextField TFName;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
    
}
