package com.dnguyen.anidb;

public class AnimeEpisode {
	protected int eid;
	protected int aid;
	protected String epno;
	protected String eng_name;
	protected String romanji_name;
	protected String kanji_name;
	
	public AnimeEpisode(int eid, int aid, String epno, String eng_name, String romanji_name,
			String kanji_name){
		this.eid = eid;
		this.aid = aid;
		this.epno = epno;
		this.eng_name = eng_name;
		this.romanji_name = romanji_name;
		this.kanji_name = kanji_name;
	}
	
	public AnimeEpisode(){
		
	}
	
	
}
