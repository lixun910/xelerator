/*
 * MainGui.java
 *
 * Created on May 11, 2008, 10:28 AM
 */
package lx.ghm.xelerator.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;

import lx.ghm.xelerator.XAssertion;
import lx.ghm.xelerator.XController;
import lx.ghm.xelerator.XFileFilter;
import lx.ghm.xelerator.gui.tree.XTree;
import lx.ghm.xelerator.share.DBConnection;
 
/**
 *
 * @author  xunli
 */
public class MainGui extends javax.swing.JFrame {
	private XController controller;
    private XTree tree;
    
    private static final String iconFile ="/lx/ghm/xelerator/resource/Xelerator.gif";
    
    /** Creates new form MainGui */
    public MainGui() {
		initComponents();
		this.setIconImage(new ImageIcon(this.getClass().getResource(iconFile)).getImage());
    }
    
    public void init(){
    	initTree();
    	initToolbar();
    	initForms();
    	
    	this.continueBtn.setEnabled(false);
		this.pauseBtn.setEnabled(false);
		this.stopRecordBtn.setEnabled(false);
		this.menuPauseRun.setEnabled(false);
		this.menuContinueRun.setEnabled(false);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		screenSize.height -= 50;
		this.setSize(screenSize);
		this.setLocation(0, 0);
    }
    
    private void initTree() {
        tree = new XTree();
        this.setTree(tree);
    }
    public final String NEW_PROJECT_ICON = "/lx/ghm/xelerator/resource/newFile24.png";
    public final String OPEN_PROJECT_ICON = "/lx/ghm/xelerator/resource/openProject24.png";
    public final String SAVE_PROJECT_ICON = "/lx/ghm/xelerator/resource/Save.gif";
    public final String RUN_ICON = "/lx/ghm/xelerator/resource/runProject24.png";
    public final String CONTINUE_ICON = "/lx/ghm/xelerator/resource/Continue24.gif";
    public final String PAUSE_ICON = "/lx/ghm/xelerator/resource/Pause24.gif";
    public final String STOP_ICON = "/lx/ghm/xelerator/resource/Kill24.gif";
    public final String RECORDER_ICON = "/lx/ghm/xelerator/resource/NonLineBreakpointHit.gif";
    public final String STOP_RECORDER_ICON = "/lx/ghm/xelerator/resource/stoprecord_1.gif";
    public JButton newProjectBtn, openProjectBtn, saveProjectBtn, runBtn, continueBtn, pauseBtn, stopBtn, recordBtn, stopRecordBtn;

    
    private void initToolbar(){
		this.newProjectBtn = addToolbarButton(NEW_PROJECT_ICON, "New Project", controller.NewProjectAction);
		this.openProjectBtn = addToolbarButton(OPEN_PROJECT_ICON, "Open Project", controller.OpenProjectAction);
		this.saveProjectBtn = addToolbarButton(SAVE_PROJECT_ICON, "Save Project", controller.SaveProjectAction);
		jToolBar1.addSeparator();
		
		this.runBtn = addToolbarButton(RUN_ICON, "Start Run", controller.StartRunAction);
		this.continueBtn = addToolbarButton(CONTINUE_ICON, "Continue Run", controller.ContinueRunAction);
		this.pauseBtn = addToolbarButton(PAUSE_ICON, "Pause", controller.PauseRunAction);
		this.stopBtn = addToolbarButton(STOP_ICON, "Stop", controller.StopRunAction);
		jToolBar1.addSeparator();
		
		this.recordBtn = addToolbarButton(RECORDER_ICON, "Start Record", controller.RecordAction);
		this.stopRecordBtn = addToolbarButton(STOP_RECORDER_ICON, "Stop Record", controller.StopRecordAction);
    }
    private JButton addToolbarButton(String iconFile, String toolTipText, Action action){
    	JButton bNew = new JButton(new ImageIcon(getClass().getResource(iconFile)));
		bNew.setToolTipText(toolTipText);
		bNew.addActionListener(action);
		setButtonSize(bNew);
		jToolBar1.add(bNew);

		return bNew;
    }
	private void setButtonSize(JButton button) {
		Dimension d = new Dimension(30, 30);
		button.setMinimumSize(d);
		button.setMaximumSize(d);
		button.setPreferredSize(d);
	}
	
    public ProjectGui projectGUI;
    public AssertionGui assertionGUI;
    public ParameterGui parameterGUI;
    public TaskGui taskGUI;
    public TerminalGroupGui terminalGroupGUI;
    public TerminalGui terminalGUI;
    public WorkGui workGUI;
    public TimerGui timerGUI;
    public NotificationGui notificationGUI;
    
    private void initForms(){
    	projectGUI = new ProjectGui();
    	assertionGUI = new AssertionGui();
    	parameterGUI = new ParameterGui();
    	taskGUI = new TaskGui();
    	terminalGroupGUI = new TerminalGroupGui();
    	terminalGUI = new TerminalGui();
    	workGUI = new WorkGui();
    	timerGUI = new TimerGui();
    	notificationGUI = new NotificationGui();
    	
    	getContentPanel().setViewportView(projectGUI);
    }
    
    public void setController(XController c){
    	controller = c;
    }
    
    public JPopupMenu getPopupMenu(){
    	return popupMenu;
    }
    
	public JScrollPane getContentPanel() {
		return jScrollPane2;
	}

	public void setContentPanel(JScrollPane contentPanel) {
		this.jScrollPane2 = contentPanel;
	}
	
    public void showError(String message) {
		JOptionPane.showMessageDialog(this, message, "Xelerator Error",
				JOptionPane.ERROR_MESSAGE);
	}
	
	public boolean askYesNo(String message) {
		int response = JOptionPane.showConfirmDialog(this, message, message,JOptionPane.YES_NO_OPTION);
		return response == JOptionPane.YES_OPTION;
	}

	public int askYesNoCancel(String message) {
		return JOptionPane.showConfirmDialog(this, message);
	}
	
	public XTree getTree() {
		return tree;
	}

	public void setTree(XTree tree){
		this.tree = tree;
		JScrollPane treePane = new JScrollPane(tree);
        splitPane.setLeftComponent(treePane);
		splitPane.setDividerLocation(180);
		tree.addMouseListener(controller.getTreeListener());
	}

	public JDialog getXTerminalChooser(){
		return jDialogChooseXTerm;
	}
	
	private void menuAddXTerminal1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_menuAddXTerminal1ActionPerformed
		controller.addXTerminal("hostIP", "23", "loginName", "password");
	}// GEN-LAST:event_menuAddXTerminal1ActionPerformed
	private void menuAddXTerminalActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_menuAddXTerminalActionPerformed
		controller.addXTerminal("hostIP", "23", "loginName", "password");
	}// GEN-LAST:event_menuAddXTerminalActionPerformed
	
	private void menuAddXWorkActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_menuAddXWorkActionPerformed
		controller.createXWrok();
	}// GEN-LAST:event_menuAddXWorkActionPerformed
	private void menuAddXWork1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_menuAddXWork1ActionPerformed
		controller.createXWrok();
	}// GEN-LAST:event_menuAddXWork1ActionPerformed

	private void menuAddXParameterActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_menuAddXParameterActionPerformed
		controller.createXParametersNode();
	}// GEN-LAST:event_menuAddXParameterActionPerformed
	private void menuAddXParameter1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_menuAddXParameter1ActionPerformed
		controller.createXParametersNode();
	}// GEN-LAST:event_menuAddXParameter1ActionPerformed

	private void menuAddXTaskActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_menuAddXTaskActionPerformed
		controller.addXTask(null, "taskName", "currentPrompt", "command");
	}// GEN-LAST:event_menuAddXTaskActionPerformed
	private void menuAddXTask1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_menuAddXTask1ActionPerformed
		controller.addXTask(null, "taskName", "currentPrompt", "command");
	}// GEN-LAST:event_menuAddXTask1ActionPerformed

	private void menuAddXAssertionActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_menuAddXAssertionActionPerformed
		controller.addXAssertion("assertionContent", XAssertion.TEXT, XAssertion.NOT_HAS_GLOBAL_VARIABLE, XAssertion.CONTAINS);
	}// GEN-LAST:event_menuAddXAssertionActionPerformed
	private void menuAddXAssertion1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_menuAddXAssertion1ActionPerformed
		controller.addXAssertion("assertionContent", XAssertion.TEXT, XAssertion.NOT_HAS_GLOBAL_VARIABLE,XAssertion.CONTAINS);
	}// GEN-LAST:event_menuAddXAssertion1ActionPerformed
    
	private void menuAddXTerminalGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuAddXTerminalGroupActionPerformed
       controller.createXTerminalGroup();
    }//GEN-LAST:event_menuAddXTerminalGroupActionPerformed
    private void menuAddXTerminalGroup2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuAddXTerminalGroup2ActionPerformed
    	controller.createXTerminalGroup();
    }//GEN-LAST:event_menuAddXTerminalGroup2ActionPerformed
    private void menuAddXTimerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuAddXTimerActionPerformed
    	controller.addXTimer(null, "0");
    }//GEN-LAST:event_menuAddXTimerActionPerformed
    private void jMenuItemAddXTimerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemAddXTimerActionPerformed
    	controller.addXTimer(null, "0");
    }//GEN-LAST:event_jMenuItemAddXTimerActionPerformed
    
    private void menuRemoveActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_menuRemoveActionPerformed
		controller.removeNode();
	}// GEN-LAST:event_menuRemoveActionPerformed
	private void menuRemove1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_menuRemove1ActionPerformed
		controller.removeNode();
	}// GEN-LAST:event_menuRemove1ActionPerformed
	
	private void jMenuItemAddXBreakPointActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemAddXBreakPointActionPerformed
		controller.addXBreakPoint();
    }//GEN-LAST:event_jMenuItemAddXBreakPointActionPerformed
    private void menuAddXBreakPointActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuAddXBreakPointActionPerformed
        controller.addXBreakPoint();
    }//GEN-LAST:event_menuAddXBreakPointActionPerformed
    private void menuAddXInfoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuAddXInfoActionPerformed
        controller.addXNotification();
    }//GEN-LAST:event_menuAddXInfoActionPerformed
    private void jMenuItemAddXNotificationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemAddXNotificationActionPerformed
    	controller.addXNotification();
    }//GEN-LAST:event_jMenuItemAddXNotificationActionPerformed
    
	private void menuNewProjectActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_menuNewProjectActionPerformed
		controller.newProject();
	}// GEN-LAST:event_menuNewProjectActionPerformed
	private void menuOpenProjectActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_menuOpenProjectActionPerformed
		JFileChooser fDialog = new JFileChooser(".");
		fDialog.setMultiSelectionEnabled(true);
		fDialog.setDialogType(JFileChooser.OPEN_DIALOG);
		
		XFileFilter  filter =  new XFileFilter ("xml");
		filter.setDescription("Xelerator File(xml)");
		fDialog.addChoosableFileFilter(filter);
		
		int result = fDialog.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
			File[] files = fDialog.getSelectedFiles();
			for(int i=0;i<files.length;i++){
				String fname = files[i].getAbsolutePath();
				controller.openProject(fname);
			}
		}
	}// GEN-LAST:event_menuOpenProjectActionPerformed
	private void menuSaveProjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuSaveProjectActionPerformed
		JFileChooser fDialog = new JFileChooser("."); 
		fDialog.setDialogTitle("Save Xelerator Project");
		fDialog.setDialogType(JFileChooser.SAVE_DIALOG);
		int result = fDialog.showSaveDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
			String fname = fDialog.getSelectedFile().getAbsolutePath();
			controller.saveProject(fname);
		}
	}//GEN-LAST:event_menuSaveProjectActionPerformed
	private void menuCloseProjectActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_menuCloseProjectActionPerformed
		controller.closeProject();
	}// GEN-LAST:event_menuCloseProjectActionPerformed
	private void menuExitActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_menuExitActionPerformed
		 this.dispose();
         System.exit(0);
	}// GEN-LAST:event_menuExitActionPerformed


	
	private void menuStartRunActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_menuStartRunActionPerformed
		controller.StartRun();
	}// GEN-LAST:event_menuStartRunActionPerformed
	private void menuContinueRunActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuContinueRunActionPerformed
		controller.ContinueRun();
	}//GEN-LAST:event_menuContinueRunActionPerformed
	private void menuPauseRunActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_menuPauseRunActionPerformed
		controller.PauseRun();
	}// GEN-LAST:event_menuPauseRunActionPerformed
	private void menuStopRunActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_menuStopRunActionPerformed
		controller.StopRun();
	}// GEN-LAST:event_menuStopRunActionPerformed

    private void jMenuItemHelpContentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemHelpContentActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItemHelpContentActionPerformed

	
	private void menuStartRecordActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_menuStartRecordActionPerformed
		controller.startRecord();
	}// GEN-LAST:event_menuStartRecordActionPerformed
	private void menuStopRecordActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_menuStopRecordActionPerformed
		controller.stopRecord();
	}// GEN-LAST:event_menuStopRecordActionPerformed
   
   
    private void jMenuItemShareActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemShareActionPerformed
    	final FileManagementGui fManagementGui = new FileManagementGui();
    	fManagementGui.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
				DBConnection.getInstance().closeDB();
            	fManagementGui.dispose();
				return;
            }
        });
    	
    	fManagementGui.pack();
    	fManagementGui.setVisible(true);
    }//GEN-LAST:event_jMenuItemShareActionPerformed
    
    private void jMenuItemSetMainProjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSetMainProjectActionPerformed
    	 controller.setMainProject();
    }//GEN-LAST:event_jMenuItemSetMainProjectActionPerformed
    private void menuSetMainProjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuSetMainProjectActionPerformed
        controller.setMainProject();
    }//GEN-LAST:event_menuSetMainProjectActionPerformed

     private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
         // Batch Run
    	 controller.batchRun();
     }//GEN-LAST:event_jMenuItem1ActionPerformed
    
     private void menuCheckXWorkScriptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuCheckXWorkScriptActionPerformed
         controller.checkXTaskScript();
     }//GEN-LAST:event_menuCheckXWorkScriptActionPerformed
     private void jMenuItemCheckXWorkScriptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemCheckXWorkScriptActionPerformed
    	 controller.checkXTaskScript();
     }//GEN-LAST:event_jMenuItemCheckXWorkScriptActionPerformed

     


	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String args[]) {
		java.awt.EventQueue.invokeLater(new Runnable() {

			public void run() {
				XController c = new XController();
				MainGui gui = new MainGui();
				gui.setController(c);
				c.setGui(gui);
				gui.init();
//				c.newProject();
				gui.setVisible(true);
			}
		});
	}
        
    /**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        popupMenu = new javax.swing.JPopupMenu();
        menuSetMainProject = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JSeparator();
        menuAddXTask1 = new javax.swing.JMenuItem();
        menuAddXTimer = new javax.swing.JMenuItem();
        menuAddXAssertion1 = new javax.swing.JMenuItem();
        menuAddXBreakPoint = new javax.swing.JMenuItem();
        menuAddXInfo = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JSeparator();
        menuAddXTerminal1 = new javax.swing.JMenuItem();
        menuAddXParameter1 = new javax.swing.JMenuItem();
        menuAddXWork1 = new javax.swing.JMenuItem();
        menuAddXTerminalGroup2 = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JSeparator();
        menuRemove1 = new javax.swing.JMenuItem();
        jSeparator7 = new javax.swing.JSeparator();
        menuCheckXWorkScript = new javax.swing.JMenuItem();
        jDialogChooseXTerm = new javax.swing.JDialog();
        jPanel2 = new javax.swing.JPanel();
        comboboxChooseXTerm = new javax.swing.JComboBox();
        jToolBar1 = new javax.swing.JToolBar();
        splitPane = new javax.swing.JSplitPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jLabelStatus = new javax.swing.JLabel();
        jMenuBar2 = new javax.swing.JMenuBar();
        jMenu3 = new javax.swing.JMenu();
        menuNewProject = new javax.swing.JMenuItem();
        menuOpenProject = new javax.swing.JMenuItem();
        menuSaveProject = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        menuCloseProject = new javax.swing.JMenuItem();
        menuExit = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        jMenuItemSetMainProject = new javax.swing.JMenuItem();
        jSeparator8 = new javax.swing.JSeparator();
        menuAddXTask = new javax.swing.JMenuItem();
        jMenuItemAddXTimer = new javax.swing.JMenuItem();
        menuAddXAssertion = new javax.swing.JMenuItem();
        jMenuItemAddXBreakPoint = new javax.swing.JMenuItem();
        jMenuItemAddXNotification = new javax.swing.JMenuItem();
        jSeparator10 = new javax.swing.JSeparator();
        menuAddXTerminal = new javax.swing.JMenuItem();
        menuAddXWork = new javax.swing.JMenuItem();
        menuAddXParameter = new javax.swing.JMenuItem();
        menuAddXTerminalGroup = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JSeparator();
        menuRemove = new javax.swing.JMenuItem();
        jSeparator9 = new javax.swing.JSeparator();
        jMenuItemCheckXWorkScript = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        menuStartRun = new javax.swing.JMenuItem();
        menuContinueRun = new javax.swing.JMenuItem();
        menuPauseRun = new javax.swing.JMenuItem();
        menuStopRun = new javax.swing.JMenuItem();
        jSeparator6 = new javax.swing.JSeparator();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu6 = new javax.swing.JMenu();
        menuStartRecord = new javax.swing.JMenuItem();
        menuStopRecord = new javax.swing.JMenuItem();
        jMenu7 = new javax.swing.JMenu();
        jMenuItemShare = new javax.swing.JMenuItem();
        jMenuHelp = new javax.swing.JMenu();
        jMenuItemHelpContent = new javax.swing.JMenuItem();
        jMenuItemHelpAbout = new javax.swing.JMenuItem();

        menuSetMainProject.setText("Set as Main XProject");
        menuSetMainProject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuSetMainProjectActionPerformed(evt);
            }
        });
        popupMenu.add(menuSetMainProject);
        popupMenu.add(jSeparator5);

        menuAddXTask1.setText("Add XTask");
        menuAddXTask1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuAddXTask1ActionPerformed(evt);
            }
        });
        popupMenu.add(menuAddXTask1);

        menuAddXTimer.setText("Add XTimer");
        menuAddXTimer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuAddXTimerActionPerformed(evt);
            }
        });
        popupMenu.add(menuAddXTimer);

        menuAddXAssertion1.setText("Add XAssertion");
        menuAddXAssertion1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuAddXAssertion1ActionPerformed(evt);
            }
        });
        popupMenu.add(menuAddXAssertion1);

        menuAddXBreakPoint.setText("Add XBreakPoint");
        menuAddXBreakPoint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuAddXBreakPointActionPerformed(evt);
            }
        });
        popupMenu.add(menuAddXBreakPoint);

        menuAddXInfo.setText("Add XNotification");
        menuAddXInfo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuAddXInfoActionPerformed(evt);
            }
        });
        popupMenu.add(menuAddXInfo);
        popupMenu.add(jSeparator4);

        menuAddXTerminal1.setText("Add XTerminal");
        menuAddXTerminal1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuAddXTerminal1ActionPerformed(evt);
            }
        });
        popupMenu.add(menuAddXTerminal1);

        menuAddXParameter1.setText("Add XParameter");
        menuAddXParameter1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuAddXParameter1ActionPerformed(evt);
            }
        });
        popupMenu.add(menuAddXParameter1);

        menuAddXWork1.setText("Add XWork");
        menuAddXWork1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuAddXWork1ActionPerformed(evt);
            }
        });
        popupMenu.add(menuAddXWork1);

        menuAddXTerminalGroup2.setText("Add XTerminalGroup");
        menuAddXTerminalGroup2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuAddXTerminalGroup2ActionPerformed(evt);
            }
        });
        popupMenu.add(menuAddXTerminalGroup2);
        popupMenu.add(jSeparator3);

        menuRemove1.setText("Remove");
        menuRemove1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuRemove1ActionPerformed(evt);
            }
        });
        popupMenu.add(menuRemove1);
        popupMenu.add(jSeparator7);

        menuCheckXWorkScript.setText("Check XWork Scripts");
        menuCheckXWorkScript.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuCheckXWorkScriptActionPerformed(evt);
            }
        });
        popupMenu.add(menuCheckXWorkScript);

        jDialogChooseXTerm.setTitle("Choose XTerminal");

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Choose XTerminal for recording"));

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .add(30, 30, 30)
                .add(comboboxChooseXTerm, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 236, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(102, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(comboboxChooseXTerm, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(23, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout jDialogChooseXTermLayout = new org.jdesktop.layout.GroupLayout(jDialogChooseXTerm.getContentPane());
        jDialogChooseXTerm.getContentPane().setLayout(jDialogChooseXTermLayout);
        jDialogChooseXTermLayout.setHorizontalGroup(
            jDialogChooseXTermLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jDialogChooseXTermLayout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jDialogChooseXTermLayout.setVerticalGroup(
            jDialogChooseXTermLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jDialogChooseXTermLayout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Xelerator V3.0");

        jToolBar1.setRollover(true);

        splitPane.setDividerLocation(190);
        splitPane.setRightComponent(jScrollPane2);

        jLabelStatus.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelStatus.setText("A Product of MiRingBack Team");
        jLabelStatus.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(404, Short.MAX_VALUE)
                .add(jLabelStatus, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 460, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(jLabelStatus, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jMenu3.setText("File");

        menuNewProject.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        menuNewProject.setText("New Project");
        menuNewProject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuNewProjectActionPerformed(evt);
            }
        });
        jMenu3.add(menuNewProject);

        menuOpenProject.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        menuOpenProject.setText("Open Project");
        menuOpenProject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuOpenProjectActionPerformed(evt);
            }
        });
        jMenu3.add(menuOpenProject);

        menuSaveProject.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        menuSaveProject.setText("Save Project");
        menuSaveProject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuSaveProjectActionPerformed(evt);
            }
        });
        jMenu3.add(menuSaveProject);
        jMenu3.add(jSeparator1);

        menuCloseProject.setText("Close");
        menuCloseProject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuCloseProjectActionPerformed(evt);
            }
        });
        jMenu3.add(menuCloseProject);

        menuExit.setText("Exit");
        menuExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuExitActionPerformed(evt);
            }
        });
        jMenu3.add(menuExit);

        jMenuBar2.add(jMenu3);

        jMenu5.setText("Edit");

        jMenuItemSetMainProject.setText("Set As Main Project");
        jMenuItemSetMainProject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSetMainProjectActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItemSetMainProject);
        jMenu5.add(jSeparator8);

        menuAddXTask.setText("Add XTask");
        menuAddXTask.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuAddXTaskActionPerformed(evt);
            }
        });
        jMenu5.add(menuAddXTask);

        jMenuItemAddXTimer.setText("Add XTimer");
        jMenuItemAddXTimer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAddXTimerActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItemAddXTimer);

        menuAddXAssertion.setText("Add XAssertion");
        menuAddXAssertion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuAddXAssertionActionPerformed(evt);
            }
        });
        jMenu5.add(menuAddXAssertion);

        jMenuItemAddXBreakPoint.setText("Add XBreakPoint");
        jMenuItemAddXBreakPoint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAddXBreakPointActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItemAddXBreakPoint);

        jMenuItemAddXNotification.setText("Add XNotification");
        jMenuItemAddXNotification.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAddXNotificationActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItemAddXNotification);
        jMenu5.add(jSeparator10);

        menuAddXTerminal.setText("Add XTerminal");
        menuAddXTerminal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuAddXTerminalActionPerformed(evt);
            }
        });
        jMenu5.add(menuAddXTerminal);

        menuAddXWork.setText("Add XWork");
        menuAddXWork.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuAddXWorkActionPerformed(evt);
            }
        });
        jMenu5.add(menuAddXWork);

        menuAddXParameter.setText("Add XParameter");
        menuAddXParameter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuAddXParameterActionPerformed(evt);
            }
        });
        jMenu5.add(menuAddXParameter);

        menuAddXTerminalGroup.setText("Add XTerminal Group");
        menuAddXTerminalGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuAddXTerminalGroupActionPerformed(evt);
            }
        });
        jMenu5.add(menuAddXTerminalGroup);
        jMenu5.add(jSeparator2);

        menuRemove.setText("Remove");
        menuRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuRemoveActionPerformed(evt);
            }
        });
        jMenu5.add(menuRemove);
        jMenu5.add(jSeparator9);

        jMenuItemCheckXWorkScript.setText("Check XWork Scripts");
        jMenuItemCheckXWorkScript.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCheckXWorkScriptActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItemCheckXWorkScript);

        jMenuBar2.add(jMenu5);

        jMenu4.setText("Run");

        menuStartRun.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        menuStartRun.setText("Start");
        menuStartRun.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuStartRunActionPerformed(evt);
            }
        });
        jMenu4.add(menuStartRun);

        menuContinueRun.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        menuContinueRun.setText("Continue");
        menuContinueRun.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuContinueRunActionPerformed(evt);
            }
        });
        jMenu4.add(menuContinueRun);

        menuPauseRun.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        menuPauseRun.setText("Pause");
        menuPauseRun.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuPauseRunActionPerformed(evt);
            }
        });
        jMenu4.add(menuPauseRun);

        menuStopRun.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        menuStopRun.setText("Stop");
        menuStopRun.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuStopRunActionPerformed(evt);
            }
        });
        jMenu4.add(menuStopRun);
        jMenu4.add(jSeparator6);

        jMenuItem1.setText("Batch Run");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem1);

        jMenuBar2.add(jMenu4);

        jMenu6.setText("Record");

        menuStartRecord.setText("Start Record");
        menuStartRecord.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuStartRecordActionPerformed(evt);
            }
        });
        jMenu6.add(menuStartRecord);

        menuStopRecord.setText("Stop Record");
        menuStopRecord.setEnabled(false);
        menuStopRecord.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuStopRecordActionPerformed(evt);
            }
        });
        jMenu6.add(menuStopRecord);

        jMenuBar2.add(jMenu6);

        jMenu7.setText("Share");

        jMenuItemShare.setText("Share Management");
        jMenuItemShare.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemShareActionPerformed(evt);
            }
        });
        jMenu7.add(jMenuItemShare);

        jMenuBar2.add(jMenu7);

        jMenuHelp.setText("Help");

        jMenuItemHelpContent.setText("Help Content");
        jMenuItemHelpContent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemHelpContentActionPerformed(evt);
            }
        });
        jMenuHelp.add(jMenuItemHelpContent);

        jMenuItemHelpAbout.setText("About");
        jMenuHelp.add(jMenuItemHelpAbout);

        jMenuBar2.add(jMenuHelp);

        setJMenuBar(jMenuBar2);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .add(jToolBar1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 864, Short.MAX_VALUE)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, splitPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 864, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .add(jToolBar1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 36, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(splitPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 497, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 17, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox comboboxChooseXTerm;
    private javax.swing.JDialog jDialogChooseXTerm;
    private javax.swing.JLabel jLabelStatus;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenu jMenu7;
    private javax.swing.JMenuBar jMenuBar2;
    private javax.swing.JMenu jMenuHelp;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItemAddXBreakPoint;
    private javax.swing.JMenuItem jMenuItemAddXNotification;
    private javax.swing.JMenuItem jMenuItemAddXTimer;
    private javax.swing.JMenuItem jMenuItemCheckXWorkScript;
    private javax.swing.JMenuItem jMenuItemHelpAbout;
    private javax.swing.JMenuItem jMenuItemHelpContent;
    private javax.swing.JMenuItem jMenuItemSetMainProject;
    private javax.swing.JMenuItem jMenuItemShare;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JMenuItem menuAddXAssertion;
    private javax.swing.JMenuItem menuAddXAssertion1;
    private javax.swing.JMenuItem menuAddXBreakPoint;
    private javax.swing.JMenuItem menuAddXInfo;
    private javax.swing.JMenuItem menuAddXParameter;
    private javax.swing.JMenuItem menuAddXParameter1;
    private javax.swing.JMenuItem menuAddXTask;
    private javax.swing.JMenuItem menuAddXTask1;
    private javax.swing.JMenuItem menuAddXTerminal;
    private javax.swing.JMenuItem menuAddXTerminal1;
    private javax.swing.JMenuItem menuAddXTerminalGroup;
    private javax.swing.JMenuItem menuAddXTerminalGroup2;
    private javax.swing.JMenuItem menuAddXTimer;
    private javax.swing.JMenuItem menuAddXWork;
    private javax.swing.JMenuItem menuAddXWork1;
    private javax.swing.JMenuItem menuCheckXWorkScript;
    private javax.swing.JMenuItem menuCloseProject;
    private javax.swing.JMenuItem menuContinueRun;
    private javax.swing.JMenuItem menuExit;
    private javax.swing.JMenuItem menuNewProject;
    private javax.swing.JMenuItem menuOpenProject;
    private javax.swing.JMenuItem menuPauseRun;
    private javax.swing.JMenuItem menuRemove;
    private javax.swing.JMenuItem menuRemove1;
    private javax.swing.JMenuItem menuSaveProject;
    private javax.swing.JMenuItem menuSetMainProject;
    private javax.swing.JMenuItem menuStartRecord;
    private javax.swing.JMenuItem menuStartRun;
    private javax.swing.JMenuItem menuStopRecord;
    private javax.swing.JMenuItem menuStopRun;
    private javax.swing.JPopupMenu popupMenu;
    private javax.swing.JSplitPane splitPane;
    // End of variables declaration//GEN-END:variables

	public javax.swing.JMenuItem getMenuPauseRun() {
		return menuPauseRun;
	}

	public void setMenuPauseRun(javax.swing.JMenuItem menuPauseRun) {
		this.menuPauseRun = menuPauseRun;
	}

	public javax.swing.JMenuItem getMenuStartRecord() {
		return menuStartRecord;
	}

	public void setMenuStartRecord(javax.swing.JMenuItem menuStartRecord) {
		this.menuStartRecord = menuStartRecord;
	}

	public javax.swing.JMenuItem getMenuStartRun() {
		return menuStartRun;
	}

	public void setMenuStartRun(javax.swing.JMenuItem menuStartRun) {
		this.menuStartRun = menuStartRun;
	}

	public javax.swing.JMenuItem getMenuStopRecord() {
		return menuStopRecord;
	}

	public void setMenuStopRecord(javax.swing.JMenuItem menuStopRecord) {
		this.menuStopRecord = menuStopRecord;
	}

	public javax.swing.JMenuItem getMenuStopRun() {
		return menuStopRun;
	}

	public void setMenuStopRun(javax.swing.JMenuItem menuStopRun) {
		this.menuStopRun = menuStopRun;
	}

	public javax.swing.JMenuItem getMenuContinueRun() {
		return menuContinueRun;
	}

	public void setMenuContinueRun(javax.swing.JMenuItem menuContinueRun) {
		this.menuContinueRun = menuContinueRun;
	}
}
