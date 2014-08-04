package com.dnguyen.anidb;

import java.util.Comparator;
import java.util.TreeSet;

import org.jdesktop.swingx.treetable.AbstractTreeTableModel;

public class MyTreeTableModel extends AbstractTreeTableModel {
	private class AnimeTreeNodeComparator implements 
	Comparator<NodeAnime> {
		/*
		 * Comparator to sort Anime in list 
		 */
		public int compare(NodeAnime anime1, NodeAnime anime2) {
			return anime1.getName().compareTo(anime2.getName());
		}
	}
	private AnimeTreeNodeComparator animeCompare;
	private TreeSet<NodeAnime> animeList = new TreeSet<NodeAnime>(animeCompare);
	private String[] columns = {"Name", "Episodes", "Size", "Last checked"};
	
	public MyTreeTableModel() {
		// TODO Auto-generated constructor stub
	}
	
	public MyTreeTableModel(Object root) {
		super(new Object());
	}

	public void addAnime(NodeAnime anime1) {
		this.animeList.add(anime1);
	}
	
	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return 4;
	}

	@Override
	public Object getValueAt(Object node, int column) {
		return ((MyTreeNode) node).getValueAt(column);
	}

	@Override
	public Object getChild(Object parent, int index) {
		return ((MyTreeNode) parent).getChild(index);
	}

	@Override
	public int getChildCount(Object node) {
		return ((MyTreeNode) node).getChildCount();
	}

	@Override
	public int getIndexOfChild(Object parent, Object child) {
		// TODO Auto-generated method stub
		return 0;
	}

}
