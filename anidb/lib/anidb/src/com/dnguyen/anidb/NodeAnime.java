package com.dnguyen.anidb;

import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;

public class NodeAnime extends MyTreeNode {
	private class EpisodeTreeNodeComparator implements 
	Comparator<NodeEpisode> {
		/* 
		 * Comparator to sort episodes
		 */
		public EpisodeTreeNodeComparator() {
			return;
		}
		public int compare(NodeEpisode ep1, NodeEpisode ep2) {
			return ep1.getEpisode().compareTo(ep2.getEpisode());
		}
	}
	
	private Comparator<NodeEpisode> episodeCompare = new EpisodeTreeNodeComparator();
	private TreeSet<NodeEpisode> episodeList = 
			new TreeSet<NodeEpisode>(episodeCompare);
	
	public NodeAnime(Object parent, String name, String episode, int size,
			String lastChecked) {
		super(parent, name, episode, size, lastChecked);
		// TODO Auto-generated constructor stub
	}

	public NodeAnime() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getChildCount() {
		return this.episodeList.size();
	}

	@Override
	public Boolean isLeaf() {
		return (this.episodeList.size() > 0);
	}

	@Override
	public int getIndexOfChild() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getChild(int index) {
		Iterator<NodeEpisode> itr = this.episodeList.iterator();
		NodeEpisode temp = new NodeEpisode();
		for (int i = 0; i <= index; i++) {
			temp = itr.next();
		}
		return temp;
	}


}
