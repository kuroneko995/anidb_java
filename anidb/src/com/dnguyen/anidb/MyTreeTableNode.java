package com.dnguyen.anidb;

import java.util.HashMap;

import javax.swing.JFileChooser;

import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;

public class MyTreeTableNode extends DefaultMutableTreeTableNode {
	private Boolean isAnime;
	private String name;
	private String episode;
	private String lastChecked;
	private int size;
	private String filePath;
	private Boolean isAvailable;
	
	
	private HashMap<String, MyTreeTableNode> episodeMap = 
			new HashMap<String, MyTreeTableNode>();
	
	
	public MyTreeTableNode() {
		// TODO Auto-generated constructor stub
	}

	public MyTreeTableNode(Object userObject) {
		super(userObject);
		// TODO Auto-generated constructor stub
		
	}
	
	public MyTreeTableNode(Object userObject, boolean allowsChildren) {
		super(userObject, allowsChildren);
		// TODO Auto-generated constructor stub
	}

	public MyTreeTableNode(String name, String episode, int size, 
			String lastChecked, Boolean isAnime) {
		super();
		super.allowsChildren = true;
		
		this.name = name;
		this.episode = episode;
		this.size = size;
		this.lastChecked = lastChecked;
		this.isAnime = isAnime;
	}
	
	public void addEpisode(MyTreeTableNode ep1) {
		this.episodeMap.put(ep1.getName(), ep1);
		this.size += ep1.getSize();
		String epName = ep1.getName();
		Boolean hasInserted = false;
		for (int i = 0; i < this.getChildCount(); i++) {
			String name2 = ((MyTreeTableNode) this.getChildAt(i)).getName();
			if (epName.compareTo(name2) < 0) {
				super.insert(ep1, i);
				hasInserted = true;
				break;
			}
		}
		if (!hasInserted) {
			super.insert(ep1, super.getChildCount());
		}
		
		if (ep1.isAvailable) {
			this.isAvailable = true;
		}
//		super.insert(ep1,  0);
	}
	
	public MyTreeTableNode getChild(String episodeName) {
		return this.episodeMap.get(episodeName);
	}
	
	public void setSize(int size) {
		this.size = size;
	}
	public String getEpisode() {
		return this.episode;
	}
	
	public void addMap(MyTreeTableNode episode) {
		this.episodeMap.put(episode.getName(), episode);
		
	}
	
	public String getLastChecked() {
		return this.lastChecked;
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getSize() {
		return this.size;
	}
	
	@Override
	public Object getValueAt(int column) {
		switch (column) {
        case 0: return (String) this.name;
        case 1: return (String) this.episode;
        case 2: return Integer.toString(this.size) + " MB";
        case 3:	return (String) this.lastChecked;
		}
		return null;
	}
	
	public Boolean hasName(String episodeName) {
		return this.episodeMap.containsKey(episodeName);
	}

	public void setLastChecked(String lastChecked) {
		this.lastChecked = lastChecked;
	}

	public Boolean getAnimeStatus(){
		return this.isAnime;
	}
	
	public void makeAnime(Boolean isAnime) {
		this.isAnime = isAnime;
	}

	public void setPath (String filePath) {
		this.filePath = filePath;
	}
	
	public String getPath() {
		return this.filePath;
	}
	
	public Boolean getAvailability() {
		return this.isAvailable;
	}

	public void setAvailability(Boolean available) {
		this.isAvailable = available;
	}
	
}
