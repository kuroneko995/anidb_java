package com.dnguyen.anidb;

import java.sql.ResultSet;

public class AnimeSeries {
	protected int aid;
	protected String episodes, year;
	protected String romanji_name, eng_name, kanji_name;
	
	public AnimeSeries(int aid, String episodes, String year, String romanji_name,  String eng_name,
			String kanji_name){
		this.aid = aid;
		this.year = year;
		this.romanji_name = romanji_name;
		this.episodes = episodes;
		this.eng_name = eng_name;
		this.kanji_name = kanji_name;
	}
	
	public AnimeSeries(){
		this.aid = 0;
		this.romanji_name = "";
		this.episodes = "";
		this.eng_name = "";
		this.kanji_name = "";
	}
	

	
}
