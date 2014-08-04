package com.dnguyen.anidb;

public class EpisodeTreeNode {
//	private String name;
	private AnimeTreeNode parent;
	private String fileName;
	private String filePath;
	private String episodeNumber;
	private String lastChecked;
	
	public EpisodeTreeNode(AnimeTreeNode parent, String fileName, 
			String filePath, String episodeNumber, String lastChecked) {
		this.parent = parent;
		this.fileName = fileName;
		this.filePath = filePath;
		this.episodeNumber = episodeNumber;
		this.lastChecked = lastChecked;
	}
	
	public EpisodeTreeNode(AnimeTreeNode parent) {
		this(parent, "", "", "", "");
	}
	
	
	
	public AnimeTreeNode getParent() {
		return this.parent;
	}
	
	public String getEpisodeNumber() {
		return this.episodeNumber;
	}
	
	public String getFileName() {
		return this.fileName;
	}
	
	public String getFilePath() {
		return this.filePath;
	}
	
	public String getLastChecked() {
		return this.lastChecked;
	}
	
	public int getChildCount() {
		return 0;
	}

}
