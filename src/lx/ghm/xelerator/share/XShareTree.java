package lx.ghm.xelerator.share;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;

public class XShareTree {
	private XDir root;

	public XDir getRoot() {
		return root;
	}

	public void setRoot(XDir root) {
		this.root = root;
	}

	private Hashtable dirCollection = new Hashtable();
	private ArrayList selectedFiles = new ArrayList();
	public ArrayList getSelectedFiles() {
		return selectedFiles;
	}

	public void setSelectedFiles(ArrayList selectedFiles) {
		this.selectedFiles = selectedFiles;
	}
	
	public XShareTree() throws SQLException,NullPointerException {
		this.getAllDirs();
		selectedFiles = this.getFilesFromDIR(root.getId());
		this.getAllFiles();
	}

	private XDir getDirFromHT(String id) {
		if (!dirCollection.containsKey(id)) {
			XDir dir = new XDir(Integer.parseInt(id));
			dirCollection.put(id, dir);
			return dir;
		} else {
			return (XDir) dirCollection.get(id);
		}
	}

	public void getAllDirs() throws SQLException, NullPointerException {
		Connection conn = DBConnection.getInstance().getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			String queryDIR = "select * from DIRECTORY_TREE";
			ps = conn.prepareStatement(queryDIR);
			rs = ps.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("ID");
				String name = rs.getString("NAME");
				int parentID = rs.getInt("PARENT_ID");
				
				XDir dir = getDirFromHT(String.valueOf(id));
				XDir pDir = getDirFromHT(String.valueOf(parentID));
	
				dir.setName(name);
				dir.setParentID(parentID);
				dir.setParent(pDir);
				if (id == parentID) {
					root = dir;
				}else{
					pDir.addChild(dir);
				}
			}
	
			rs.close();
			ps.close();
		}catch(NullPointerException ex){
			rs.close();
			ps.close();
			DBConnection.getInstance().closeDB();
			ex.printStackTrace();
		}
	}

	public void getAllFiles()  throws SQLException, NullPointerException {
		Connection conn = DBConnection.getInstance().getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String queryFILE = "select * from PROJECT_FILE";
		ps = conn.prepareStatement(queryFILE);
		rs = ps.executeQuery();

		while (rs.next()) {
			XFile xFile = new XFile();
			xFile.setId(rs.getInt("ID"));
			xFile.setFileName(rs.getString("FILE_NAME"));
			xFile.setKeywords(rs.getString("KEYWORDS"));
			xFile.setDescription(rs.getString("DESCRIPTION"));
			xFile.setUploadDate(rs.getDate("UPLOAD_DATE"));
			xFile.setDownloadTimes(rs.getInt("DOWNLOAD_TIMES"));
			xFile.setDirID(rs.getInt("DIR_ID"));
			// TODO: 优化CONTENT
			xFile.setContent(rs.getString("CONTENT"));
			getDirFromHT(rs.getString("DIR_ID")).addXFile(xFile);
		}
		rs.close();
		ps.close();
	}
	
	public ArrayList getFilesFromDIR(int dirID) throws SQLException, NullPointerException {
		ArrayList files = new ArrayList();
		Connection conn = DBConnection.getInstance().getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String queryFILE = "select * from PROJECT_FILE where DIR_ID = ?";
		ps = conn.prepareStatement(queryFILE);
		ps.setInt(1, dirID);
		rs = ps.executeQuery();

		while (rs.next()) {
			XFile xFile = new XFile();
			xFile.setId(rs.getInt("ID"));
			xFile.setFileName(rs.getString("FILE_NAME"));
			xFile.setKeywords(rs.getString("KEYWORDS"));
			xFile.setDescription(rs.getString("DESCRIPTION"));
			xFile.setUploadDate(rs.getDate("UPLOAD_DATE"));
			xFile.setDownloadTimes(rs.getInt("DOWNLOAD_TIMES"));
			xFile.setDirID(rs.getInt("DIR_ID"));
			files.add(xFile);
		}
		rs.close();
		ps.close();
		return files;
	}

	public int addDIR(XDir dir) throws SQLException {
		Connection conn = DBConnection.getInstance().getConnection();
		PreparedStatement ps = null;
		String queryFILE = "insert into DIRECTORY_TREE (NAME, PARENT_ID) values (?,?)";
		ps = conn.prepareStatement(queryFILE);
		ps.setString(1, dir.getName());
		ps.setInt(2, dir.getParentID());
		int result = ps.executeUpdate();

		if ( result == 0)
			return -1;
		
		ps.close();
		
		// get XDir ID
		ResultSet rs = null;
		int xDirID = -1;
		String queryDIR = "select ID from DIRECTORY_TREE where NAME='?' and PARENT_ID=?";
		ps = conn.prepareStatement(queryDIR);
		ps.setString(1, dir.getName());
		ps.setInt(1, dir.getParentID());
		rs = ps.executeQuery();
		while (rs.next()) {
			xDirID = rs.getInt("ID");
		}
		rs.close();
		ps.close();
		
		XDir currentXDir = this.getDirFromHT(String.valueOf(xDirID));
		currentXDir.addChild(dir);
		return xDirID;
	}

	public boolean deleteDIR(XDir dir) throws SQLException {
		boolean bSuccessful = false;
		Connection conn = DBConnection.getInstance().getConnection();
		PreparedStatement ps = null;
		String queryFILE = "delete from DIRECTORY_TREE where ID=?";
		ps = conn.prepareStatement(queryFILE);
		ps.setInt(1, dir.getId());
		int result = ps.executeUpdate();

		if ( result == 0)
			bSuccessful = false;
		else
			bSuccessful = true;
		
		ps.close();
		return bSuccessful;
	}

	public void updateDIR() throws SQLException {
		
	}

	public boolean addFile(XFile xFile, int xDirID) throws SQLException {
		boolean bSuccessful = false;
		Connection conn = DBConnection.getInstance().getConnection();
		PreparedStatement ps = null;
		
		String queryFILE = "insert into PROJECT_FILE (FILE_NAME, KEYWORDS, DESCRIPTION, UPLOAD_DATE, DIR_ID, CONTENT) values (?,?,?,?,?,?)";
		ps = conn.prepareStatement(queryFILE);
		ps.setString(1, xFile.getFileName());
		ps.setString(2, xFile.getKeywords());
		ps.setString(3, xFile.getDescription());
		ps.setDate(4, xFile.getUploadDate());
		ps.setInt(5, xFile.getDirID());
		ps.setString(6,xFile.getContent());
		
		int result = ps.executeUpdate();
		
		if ( result == 0)
			bSuccessful = false;
		else
			bSuccessful = true;
		
		ps.close();
		
		XDir currentXDir = this.getDirFromHT(String.valueOf(xDirID));
		currentXDir.addXFile(xFile);
		
		return bSuccessful;
	}

	public boolean updateFile(XFile xFile) throws SQLException {
		boolean bSuccessful = false;
		Connection conn = DBConnection.getInstance().getConnection();
		PreparedStatement ps = null;
		String queryFILE = "update PROJECT_FILE set KEYWORDS=?, DESCRIPTION=? where ID=?";
		ps = conn.prepareStatement(queryFILE);
		ps.setString(1, xFile.getKeywords());
		ps.setString(2, xFile.getDescription());
		ps.setInt(3, xFile.getId());
		
		int result = ps.executeUpdate();
		if ( result == 0)
			bSuccessful = false;
		else
			bSuccessful = true;
		
		ps.close();
		return bSuccessful;
	}

	public boolean deleteFile(XFile xFile, int xDirID) throws SQLException {
		boolean bSuccessful = false;
		Connection conn = DBConnection.getInstance().getConnection();
		PreparedStatement ps = null;
		String queryFILE = "delete from PROJECT_FILE where ID=?";
		ps = conn.prepareStatement(queryFILE);
		ps.setInt(1, xFile.getId());
		int result = ps.executeUpdate();

		if ( result == 0)
			bSuccessful = false;
		else
			bSuccessful = true;
		
		ps.close();
		
		XDir currentXDir = this.getDirFromHT(String.valueOf(xDirID));
		currentXDir.removeXFile(xFile);
		
		return bSuccessful;
	}
}
