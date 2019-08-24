package com.compli.bean.access;

public class Activities {
	boolean changestatus  = true;
	boolean downloadFiles = true;
	boolean uploadFile = true;
	
	public boolean isUploadFile() {
		return uploadFile;
	}
	public void setUploadFile(boolean uploadFile) {
		this.uploadFile = uploadFile;
	}
	public boolean isChangestatus() {
		return changestatus;
	}
	public boolean isDownloadFiles() {
		return downloadFiles;
	}
	public void setChangestatus(boolean changestatus) {
		this.changestatus = changestatus;
	}
	public void setDownloadFiles(boolean downloadFiles) {
		this.downloadFiles = downloadFiles;
	}
	
	
}
