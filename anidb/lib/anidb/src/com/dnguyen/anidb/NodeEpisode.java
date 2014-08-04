package com.dnguyen.anidb;

public class NodeEpisode extends MyTreeNode {
	
	public NodeEpisode(Object parent, String name, String episode, int size,
			String lastChecked) {
		super(parent, name, episode, size, lastChecked);
		// TODO Auto-generated constructor stub
	}

	public NodeEpisode() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getChildCount() {
		return 0;
	}

	@Override
	public Boolean isLeaf() {
		return true;
	}

	@Override
	public int getIndexOfChild() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getChild(int index) {
		return null;
	}
	
}
