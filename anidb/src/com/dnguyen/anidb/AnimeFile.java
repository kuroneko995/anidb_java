package com.dnguyen.anidb;

public class AnimeFile {
	protected int fid;
	protected int eid;
	protected int aid;
	protected int gid;
	protected int size;
	protected String ed2k;
	protected String md5;
	protected String sha1;
	protected String crc32;
	protected String dub;
	protected String sub;
	protected String src;
	protected String audio;
	protected String video;
	protected String res;
	protected String grp;
	protected String file_type;
	protected String epno;
	protected String eng_name;
	protected String romanji_name;
	protected String kanji_name;
	
	public AnimeFile(int fid, int aid, int eid, int gid, int size, String ed2k, String md5,
			String sha1, String crc32, String dub, String sub, String src, String audio,
			String video, String res, String grp, String file_type){
		this.fid = fid;
		this.aid = aid;
		this.eid = eid;
		this.gid = gid;
		this.size = size;
		this.ed2k = ed2k;
		this.md5 = md5;
		this.sha1 = sha1;
		this.crc32 = crc32;
		this.dub = dub;
		this.sub = sub;
		this.src = src;
		this.audio = audio;
		this.video = video;
		this.res = res;
		this.grp = grp;
		this.file_type = file_type;
	}
}
