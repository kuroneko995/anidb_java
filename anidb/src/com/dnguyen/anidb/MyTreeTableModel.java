package com.dnguyen.anidb;

import java.util.Arrays;
import java.util.List;

import javax.swing.tree.TreePath;

import org.jdesktop.swingx.treetable.DefaultTreeTableModel;

public class MyTreeTableModel extends DefaultTreeTableModel {
	private static final List<String> COLUMNS_NAMES = Arrays.asList("Name", "Episode", 
		"Size","Last checked");
	
	public MyTreeTableModel() {
		// TODO Auto-generated constructor stub
	}

	public MyTreeTableModel(MyTreeTableNode root) {
		super(root, COLUMNS_NAMES);
	}

	@Override
	public Object getValueAt(Object node, int column) {
		return ((MyTreeTableNode) node).getValueAt(column);
	}
	
 	public MyTreeTableModel(MyTreeTableNode root, List<String> columnNames) {
		super(root, columnNames);
		// TODO Auto-generated constructor stub
	}

	public void addAnime(MyTreeTableNode anime1) {
		((MyTreeTableNode) super.getRoot()).addEpisode(anime1);
		MyTreeTableNode root = (MyTreeTableNode) super.getRoot();
		modelSupport.fireChildAdded(new TreePath(root), 
				root.getIndex(anime1), anime1);
		
	}
	
	public Boolean hasAnime(String animeName) {
		return ((MyTreeTableNode) super.getRoot()).hasName(animeName);
	}
	
	public MyTreeTableNode getAnime(String animeName) {
		return ((MyTreeTableNode) super.getRoot()).getChild(animeName);
	}
	
	public void addEpisode(MyTreeTableNode anime, MyTreeTableNode episode) {
		anime.addMap(episode);
		anime.setSize(anime.getSize() + episode.getSize());
		String epName = episode.getName();
		Boolean hasInserted = false;
		
		// Insert in right order
		for (int i = 0; i < anime.getChildCount(); i++) {
			String name2 = ((MyTreeTableNode) anime.getChildAt(i)).getName();
			if (epName.compareTo(name2) < 0) {
				anime.insert(episode, i);
				hasInserted = true;
				break;
			}
		}
		if (!hasInserted) {
			anime.insert(episode, anime.getChildCount());
		}
		
		if (episode.getAvailability()) {
			anime.setAvailability(true);
		}

		TreePath animePath = new TreePath(this.getRoot());
		modelSupport.fireChildAdded(animePath.pathByAddingChild(anime), 
				anime.getIndex(episode), episode);
	}
	
	public void updateLastChecked(String animeName, String fileName, 
			String newTime) {
		MyTreeTableNode anime = this.getAnime(animeName);
		MyTreeTableNode episode = anime.getChild(fileName);
		episode.setLastChecked(newTime);
	}
	
	public void addEntry(String animeName, String animeEpisode, 
			String episodeName, String episodeNumber, int episodeSize, 
			String episodeLastChecked, String folder, Boolean isAvailable) {
		MyTreeTableNode anime;
		if (this.hasAnime(animeName)) {
			anime = this.getAnime(animeName);
		} else {
			anime = new MyTreeTableNode(animeName, animeEpisode, 0, 
					"", true);
			anime.setAvailability(false);
			this.addAnime(anime);
		}
		
		if (anime.hasName(episodeName)){
			return;
		} else {
			MyTreeTableNode episode = new MyTreeTableNode(episodeName, 
					episodeNumber, episodeSize, episodeLastChecked, false);
			episode.setPath(folder);
			episode.setAvailability(isAvailable);
			this.addEpisode(anime,episode);
		}	
	}
	
	public void addEntry(TableEntry entry) {
		MyTreeTableNode anime;
		if (this.hasAnime(entry.animeRomanjiName)) {
			anime = this.getAnime(entry.animeRomanjiName);
		} else {
			anime = new MyTreeTableNode(entry.animeRomanjiName, entry.episodes, 0, 
					"", true);
			anime.setAvailability(false);
			this.addAnime(anime);
		}
		
		if (anime.hasName(entry.epRomanjiName)){
			return;
		} else {
			MyTreeTableNode episode = new MyTreeTableNode(entry.epRomanjiName, 
					entry.epno, entry.size/1000000, entry.last_checked, false);
			episode.setPath(entry.folder);
			episode.setAvailability(entry.accessible);
			this.addEpisode(anime,episode);
		}	
	}

}
