package lx.ghm.xelerator;

import java.awt.Image;
import java.beans.SimpleBeanInfo;
import java.lang.reflect.Field;

import javax.swing.ImageIcon;

public abstract class XNode extends SimpleBeanInfo{
	public final String iconFile ="/lx/ghm/xelerator/resource/XProject.gif";
	abstract public Image getIcon(int type);
	   
	public void setPropertyValue( String fieldName, String fieldValue){
		try {
			Field field = this.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			field.set(this, fieldValue);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	public void setPropertyValue(String fieldName, Boolean fieldValue){
		try {
			Field field = getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			field.set(this,fieldValue);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}
