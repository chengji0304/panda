package com.panda.store.entity;

import java.io.Serializable;
import java.util.List;


public class StoreVidwo {
	public static class Data implements Serializable{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		/**
		 * 
		 */
	
		private String fileName;
		private long fileSize;
		private String lastModified;
		private String url;
	
		public String getFileName() {
			return fileName;
		}
		public void setFileName(String fileName) {
			this.fileName = fileName;
		}
		public long getFileSize() {
			return fileSize;
		}
		public void setFileSize(long fileSize) {
			this.fileSize = fileSize;
		}
		public String getLastModified() {
			return lastModified;
		}
		public void setLastModified(String lastModified) {
			this.lastModified = lastModified;
		}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
	
	
		};
     
	public String results;
	public String depict;
	public String msgNo;
	public int managementCompany;
	public int store;
	public int storePassWord;
	public List<Data> FileList;
	public boolean ok(){
		if(results.equals("1")){
			return true;
		}else{
			return false;
		}
	}
	
	public List<Data> getData(){
		return this.FileList;
	}
}
