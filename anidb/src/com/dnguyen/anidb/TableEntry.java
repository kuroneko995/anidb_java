/**
 * 
 */
package com.dnguyen.anidb;

/**
 * @author ndminh92
 *
 */	
	
public class TableEntry {
	public int aid, eid, gid, fid, size;
	public String animeRomanjiName, animeEngName, animeKanjiName;
	public String episodes, year;
	public String epEngName, epno, epRomanjiName, epKanjiName;
	public String ed2k, md5, sha1, crc32;
	public String dub,sub,src,audio,video,res, file_type, grp; 
	public String fileName, drive_name, folder, last_checked;
	public Boolean accessible;
	
	public TableEntry(int aid, int eid, int gid, int fid, int size,
			String animeRomanjiName, String animeEngName, String animeKanjiName,
			String episodes, String year, String epEngName, String epno, 
			String epRomanjiName, String epKanjiName, String ed2k, String md5, 
			String sha1, String crc32, String dub, String sub, String src,
			String audio, String video, String res, String file_type, String grp, 
			String fileName, String drive_name, String folder, String last_checked,
			Boolean accessible){
		this.aid = aid;
		this.eid = eid;
		this.gid = gid;
		this.fid = fid;
		this.size = size;
		this.animeRomanjiName = animeRomanjiName;
		this.animeEngName = animeEngName;
		this.animeKanjiName = animeKanjiName;
		this.episodes = episodes;
		this.year = year;
		this.epEngName = epEngName;
		this.epno = epno;
		this.epRomanjiName = epRomanjiName;
		this.epKanjiName = epKanjiName;
		this.ed2k = ed2k;
		this.md5 = md5;
		this.sha1 = sha1;
		this.crc32 = crc32;
		this.md5 = md5;
		this.dub = dub;
		this.sub = sub;
		this.src = src;
		this.audio = audio;
		this.video = video;
		this.res = res;
		this.file_type = file_type;
		this.grp = grp;
		this.fileName = fileName;
		this.drive_name = drive_name;
		this.folder = folder;
		this.last_checked = last_checked;
		this.accessible = accessible;
	}
	
	public AnimeSeries toAnimeSeries() {
		return new AnimeSeries(this.aid, this.episodes, this.year, this.animeRomanjiName, 
				this.animeEngName, this.animeKanjiName);
	}
	
	public AnimeEpisode toAnimeEpisode() {
		return new AnimeEpisode(this.eid, this.aid, this.epno, this.epEngName, 
				this.epRomanjiName, this.epKanjiName);
	}
	
	public AnimeFile toAnimeFile() {
		return new AnimeFile(this.fid, this.aid, this.eid, this.gid, this.size, 
				this.ed2k, this.md5, this.sha1, this.crc32, this.dub, this.sub, 
				this.src, this.audio, this.video, this.res, this.grp, this.file_type);
	}
	
	public AnimeLocalFile toAnimeLocalFile() {
		return new AnimeLocalFile(this.fileName, this.fid, this.folder, this.drive_name,
				this.last_checked);
		
	}
}
