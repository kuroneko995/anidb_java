package com.dnguyen.anidb;

import java.util.Comparator;
import java.util.TreeSet;

public class AnimeTreeNode {
	private class EpisodeTreeNodeComparator implements 
	Comparator<EpisodeTreeNode> {
		/* 
		 * Comparator to sort episodes
		 */
		public EpisodeTreeNodeComparator() {
			return;
		}
		public int compare(EpisodeTreeNode ep1, EpisodeTreeNode ep2) {
			return ep1.getEpisodeNumber().compareTo(ep2.getEpisodeNumber());
		}
	}
	
	private Object parent;
	private String name;
	private int episodes;
	private Comparator<EpisodeTreeNode> episodeCompare = new EpisodeTreeNodeComparator();
	private TreeSet<EpisodeTreeNode> episodeList = 
			new TreeSet<EpisodeTreeNode>(episodeCompare);
	
	public AnimeTreeNode(Object parent, String name, int episodes) {
		this.parent = parent;
		this.name = name;
		this.episodes = episodes;
	}
	
	public AnimeTreeNode(String name) {
		this.name = name;
	}
	
	public void addEpisode(EpisodeTreeNode episode) {
		this.episodeList.add(episode);
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getEpisodes() {
		return this.episodes;
	}
	
	public Object getParent() {
		return parent;
	}
}
