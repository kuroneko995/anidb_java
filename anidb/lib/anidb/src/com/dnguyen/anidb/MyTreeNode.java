package com.dnguyen.anidb;

public abstract class MyTreeNode {
	public String name;
	public String[] columns = {"Name", "Episodes", "Size", "Last checked"};
	public String episode;
	public Object parent;
	public String lastChecked;
	public int size;
	
	public MyTreeNode(Object parent, String name, String episode, 
			int size, String lastChecked) {
		this.name = name;
		this.parent = parent;
		this.episode = episode;
		this.size = size;
		this.lastChecked = lastChecked;
	}
	
	public MyTreeNode() {
		this.name = "";
		this.parent = null;
	}
	
	public abstract int getChildCount();
	
	public abstract Boolean isLeaf();
	
	public abstract int getIndexOfChild();
	
	public abstract Object getChild(int index);
	
	public Object getValueAt(int columnNumber) {
		switch (columnNumber) {
        case 0:  return this.name;
        case 1:  return this.episode;
        case 2:	 return this.size;
        case 3:	 return this.lastChecked;
		}
		return null;
	}
		
	public String getName() {
		return this.name;
	}

	public String getEpisode() {
		return this.episode;
	}
	
	public int getSize() {
		return this.size;
	}
	
	public String getLastChecked() {
		return this.lastChecked;
	}
}
