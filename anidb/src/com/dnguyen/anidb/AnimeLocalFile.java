package com.dnguyen.anidb;

public class AnimeLocalFile {
	protected String filename;
	protected int fid;
	protected String folder;
	protected String drive_name;
	protected String last_checked;
	
	public AnimeLocalFile(String filename, int fid, String folder, String drive_name, 
			String last_checked){
		this.filename = filename;
		this.fid = fid;
		this.folder = folder;
		this.drive_name = drive_name;
		this.last_checked = last_checked;
	}
}
