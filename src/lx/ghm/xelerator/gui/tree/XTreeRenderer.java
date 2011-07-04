/**
 * 
 */
package lx.ghm.xelerator.gui.tree;

import java.awt.Component;
import java.awt.Font;
import java.awt.Image;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.util.Hashtable;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 * @author xunli
 *
 */
public class XTreeRenderer extends DefaultTreeCellRenderer {
	Hashtable icons = new Hashtable();
	ImageIcon projectIcon;

	class NodeInfo {
		ImageIcon icon;
		String tip;

		public NodeInfo(ImageIcon icon, String tip) {
			this.icon = icon;
			this.tip = tip;
		}
	}

	public XTreeRenderer() {
		// TODO projectIcon = new ImageIcon(this.getClass().getResource("/images/Project.gif"));
	}

	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		JLabel label = (JLabel) super.getTreeCellRendererComponent(tree,value, sel, expanded, leaf, row, hasFocus);
		XBeanNode node = (XBeanNode) value;
		Font font = label.getFont();
		
		if (node.isRoot()){
			label.setIcon(projectIcon);
		}
		else {
			NodeInfo info = (NodeInfo) icons.get(node.toString());
			if (info == null)
				info = makeInfo(node);
			if (info.icon != null)
				label.setIcon(info.icon);
			if (info.tip != null)
				label.setToolTipText(info.tip);
		}
		
		if ( node.getColor()!=null){
			label.setForeground(node.getColor());
		}
		
		if (node.getBgColor()!=null){
			label.setOpaque(true);
			label.setBackground(node.getBgColor());
		}else{
			label.setOpaque(false);
			label.setBackground(label.getBackground());
		}
		
		if (node.isBolder == true){
			label.setFont(font.deriveFont(Font.BOLD));
		}else{
			label.setFont(font.deriveFont(Font.PLAIN));
		}
		
		return label;
	}
	
	private NodeInfo makeInfo(XBeanNode node) {
		NodeInfo info = null;
		try {
			BeanInfo bInfo = Introspector.getBeanInfo(node.getBean().getClass());
			Image image = bInfo.getIcon(bInfo.ICON_COLOR_16x16);

			if (image != null)
				info = new NodeInfo(new ImageIcon(image), bInfo.getBeanDescriptor().getShortDescription());
			else
				info = new NodeInfo(null, bInfo.getBeanDescriptor().getShortDescription());
		} catch (IntrospectionException ex) {
			ex.printStackTrace();
		}
		return info;
	} 
}
