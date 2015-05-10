package com.dnguyen.anidb;

public class Anime {
	protected int aid;
	protected String romanji_name;
	protected String episodes;
	protected String year;
	protected String eng_name;
	protected String kanji_name;
	
	public Anime(int aid, String episodes, String year, String romanji_name,  String eng_name,
			String kanji_name){
		this.aid = aid;
		this.year = year;
		this.romanji_name = romanji_name;
		this.episodes = episodes;
		this.eng_name = eng_name;
		this.kanji_name = kanji_name;
	}
	
	public Anime(){
		this.aid = 0;
		this.romanji_name = "";
		this.episodes = "";
		this.eng_name = "";
		this.kanji_name = "";
	}
	
	
}
