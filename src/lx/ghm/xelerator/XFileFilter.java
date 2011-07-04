/**
 * 
 */
package lx.ghm.xelerator;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * @author xunli
 *
 */
public class XFileFilter  extends FileFilter {
	private String extension;
	private String description;
	
	public XFileFilter(String extension){
		this.extension = extension;
	}
	
	public void setDescription(String description){
		this.description = description;
	}
	/* (non-Javadoc)
	 * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
	 */
	public boolean accept(File f) {
		 if (f.isDirectory()) {
	            return true;
	    }

        String extension = getExtension(f);
        if (extension != null) {
            if (extension.equalsIgnoreCase(this.extension)){
                return true;
            } 
        }
		return false;
	}

	/* (non-Javadoc)
	 * @see javax.swing.filechooser.FileFilter#getDescription()
	 */
	public String getDescription() {
		return this.description;
	}

	public String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }
}
