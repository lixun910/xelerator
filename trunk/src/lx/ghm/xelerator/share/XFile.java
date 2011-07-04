package lx.ghm.xelerator.share;

import java.awt.Color;
import java.sql.Date;


public class XFile implements IShareNode{
	private int id;
	private String fileName;
	private String keywords;
	private String description;
	private String content;
	private Date uploadDate;
	private int downloadTimes;
	private int dirID;
	
	public String toString(){
		return this.fileName;
	}
	
	public int getDirID() {
		return dirID;
	}
	public void setDirID(int dirID) {
		this.dirID = dirID;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getDownloadTimes() {
		return downloadTimes;
	}
	public void setDownloadTimes(int downloadTimes) {
		this.downloadTimes = downloadTimes;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public Date getUploadDate() {
		return uploadDate;
	}
	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}

	private Color color = null;
	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
}
