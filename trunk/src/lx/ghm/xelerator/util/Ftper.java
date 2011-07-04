package lx.ghm.xelerator.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

public class Ftper {
	private FTPClient ftp = new FTPClient();
	
	public Ftper(){
		
	}
	
	public static void main(String[] args){
//		Ftper ftp = new Ftper();
//		ftp.login("135.252.17.32", "xunli", "xunli");
//		ftp.getFile("/u/xunli/sms.jar", "c:\\");
//		ftp.putFile("c:\\sms.jar", "/u/xunli");
//		ftp.logout();
	} 
	/**
	 * Connect and logon to FTP Server
	 * @param server
	 * @param username
	 * @param password
	 */
	public void login(String server, String username, String password) throws FtpException{
		try {
			ftp.connect(server);
			ftp.login(username, password);
			System.out.println("Connected to " + server + ".");
			System.out.print(ftp.getReplyString());
		}catch(SocketException e){
			e.printStackTrace();
			throw new FtpException(e);
		}catch (IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Logout from the FTP Server and disconnect
	 * @throws FtpException 
	 *
	 */
	public void logout() throws FtpException {
		try {
			ftp.logout();
			ftp.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
			throw new FtpException(e);
		}
	}
	
	public void putFile(String origFile, String destinationFolder) throws FtpException {
		try {
			String dirSeperator = "";
			int position = -1;
			if ( (position =origFile.lastIndexOf("/")) >0) {
				dirSeperator = "/";
			}else if ((position = origFile.lastIndexOf("\\")) >0){
				dirSeperator = "\\";
			}
			
			if (!ftp.changeWorkingDirectory(destinationFolder)){
				ftp.makeDirectory(destinationFolder);
				ftp.changeWorkingDirectory(destinationFolder);
			}
			
			FileInputStream fis = new FileInputStream(origFile);
			origFile = origFile.substring(position + 1);
			ftp.storeFile(origFile, fis);
			
			System.out.println(origFile + " stored via FTP!");
		} catch (IOException e) {
			e.printStackTrace();
			throw new FtpException(e);
		} 
	}
	
	public void getFile(String origFile, String destinationFolder) throws FtpException{
		try {
			// Download a file from the FTP Server
			String fileName = origFile.substring(origFile.lastIndexOf("/") + 1);
			File file = new File(destinationFolder + File.separator + fileName);
			FileOutputStream fos = new FileOutputStream(file);
			
			ftp.retrieveFile(origFile, fos);
			fos.close();
			
			System.out.println("\t" + origFile + " retrieved!");
		} catch (IOException e) {
			e.printStackTrace();
			throw new FtpException(e);
		} 
	}
	
        public String[] getItems(String folder) throws FtpException{
                try {
                        return ftp.listNames(folder);
                } catch (IOException ex) {
                        throw new FtpException(ex);
                }
        }
	/**
	 * Retrieve the files in the directory from a specified time range
	 * @param folder
	 * @param destinationFolder
	 * @param start
	 * @param end
	 * @throws FtpException 
	 */
	public void getFiles(String folder, String destinationFolder, Calendar start, Calendar end) throws FtpException {
		try {
			ftp.changeWorkingDirectory(folder);
			
			FTPFile[] files = ftp.listFiles();
			System.out.println("Number of files in dir: " + files.length);
			
			DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
			
			for (int i = 0; i < files.length; i++) {
				Date fileDate = files[i].getTimestamp().getTime();
				if (fileDate.compareTo(start.getTime()) >= 0
						&& fileDate.compareTo(end.getTime()) <= 0) {
					// Download a file from the FTP Server
					System.out.print(df.format(files[i].getTimestamp().getTime()));
					
					File file = new File(destinationFolder + File.separator + files[i].getName());
					FileOutputStream fos = new FileOutputStream(file);
					
					ftp.retrieveFile(files[i].getName(), fos);
					fos.close();
					file.setLastModified(fileDate.getTime());
					
					System.out.println("\t" + files[i].getName() + " retrieved!");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new FtpException(e);
		} 
	}
}
