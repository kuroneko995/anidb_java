package com.dnguyen.anidb;

import static java.lang.Integer.parseInt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


public class Database {
	private Connection connection = null;
	private Statement statement = null;
	
	private ArrayList<String> tableList = new ArrayList<String>(Arrays.asList
			("anime","episode", "file", "job"));
	
	private final String[] animeColumn = {"aid" , "romanji_name",
			"episodes", "year", "eng_name", "kanji_name"}; 
	private final String[] episodeColumn = {"eid" , "epno", "eng_name",
			"romanji_name", "kanji_name"};
	private final String[] fileColumn = {"fid", "aid", "eid", "gid", "size", 
			"ed2k", "md5", "sha1", "crc32", "dub", "sub", "src", "audio", 
			"video", "res", "file_type", "grp"};
	private final String[] jobColumn = {"filename", "fid", "folder",
			"drive_name", "last_checked"};
	
	
	public Database(String filePath) throws ClassNotFoundException {
		Class.forName("org.sqlite.JDBC");
		try {
			
			this.connection = DriverManager.getConnection(
					"jdbc:sqlite:"+filePath);
			this.statement = connection.createStatement();
	    	this.statement.setQueryTimeout(30);  // set timeout to 30 sec.
	    	this.statement.execute("CREATE TABLE IF NOT EXISTS anime "
	    			+ "(aid INTEGER PRIMARY KEY, romanji_name TEXT, "
	    			+ "episodes NUMBER, year NUMBER, eng_name TEXT, "
	    			+ "kanji_name TEXT)");
	    	this.statement.execute("CREATE TABLE IF NOT EXISTS episode "
	    			+ "(eid INTEGER PRIMARY KEY, aid INTEGER, epno TEXT, "
	    			+ "eng_name TEXT, romanji_name TEXT, kanji_name TEXT)");
	    	this.statement.execute("CREATE TABLE IF NOT EXISTS file "
	    			+ "(fid INTEGER PRIMARY KEY, aid INTEGER, eid INTEGER,"
	    			+ " gid INTEGER, size INTEGER, ed2k TEXT, md5 TEXT, "
	    			+ "sha1 TEXT, crc32 TEXT, dub TEXT, sub TEXT, src TEXT, "
	    			+ "audio TEXT, video TEXT, res TEXT, grp TEXT, "
	    			+ "file_type TEXT)");
	    	this.statement.execute("CREATE TABLE IF NOT EXISTS job "
	    			+ "(filename TEXT, fid INTEGER, folder TEXT, "
	    			+ "drive_name TEXT, last_checked DATETIME)");
	    	

		} catch(SQLException e) {
			System.err.println(e.getMessage());
		    }
	}
	
	public Database() throws ClassNotFoundException {
		/* 
		 * Default constructor. Create local_db.db in folder
		 */
		this("local_db.db");
	}
	
	public void close() {
		try {
			this.connection.close();
		} catch (SQLException e) {
			System.out.println("SQLite Database could not close");
			e.printStackTrace();
		}
	}
	
	public void addAnime(Anime anime) {
		// TODO Unicode check
		String AddStatement = "INSERT or IGNORE INTO anime (aid, romanji_name, "
				+ "episodes, year, eng_name, kanji_name)" 
				+ "VALUES (?,?,?,?,?,?)";
		PreparedStatement prepStmt;
		try {
			prepStmt = this.connection.prepareStatement(AddStatement);
			prepStmt.setInt(1, anime.aid);
			prepStmt.setString(2, anime.romanji_name);
			prepStmt.setString(3, anime.episodes);
			prepStmt.setString(4, anime.year);
			prepStmt.setString(5, anime.eng_name);
			prepStmt.setString(6, anime.kanji_name);
			prepStmt.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("ERROR: Cannot add anime to local database");
			e.printStackTrace();
		}
	}
	
	public void addEpisode(Episode episode) {
		// TODO Unicode check
		String AddStatement = "INSERT OR IGNORE INTO episode (eid, epno, "
				+ "eng_name, romanji_name, kanji_name) VALUES (?,?,?,?,?)";
		PreparedStatement prepStmt;
		try {
			prepStmt = this.connection.prepareStatement(AddStatement);
			prepStmt.setInt(1, episode.eid);
			prepStmt.setString(2, episode.epno);
			prepStmt.setString(3, episode.eng_name);
			prepStmt.setString(4, episode.romanji_name);
			prepStmt.setString(5, episode.kanji_name);
			prepStmt.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("ERROR: Cannot add episode to local database");
			e.printStackTrace();
		}
	}
	
	public void addFile(File file) {
		// TODO Unicode check
		String AddStatement = "INSERT OR IGNORE INTO file "
				+ "(fid, aid, eid, gid, size, "
				+ "ed2k, md5, sha1, crc32, dub, "
				+ "sub, src, audio, video, res, "
				+ "file_type, grp) "
				+ "VALUES (?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?)";
		PreparedStatement prepStmt;
		try {
			prepStmt = this.connection.prepareStatement(AddStatement);
			prepStmt.setInt(1, file.fid);
			prepStmt.setInt(2, file.aid);
			prepStmt.setInt(3, file.eid);
			prepStmt.setInt(4, file.gid);
			prepStmt.setInt(5, file.size);
			prepStmt.setString(6, file.ed2k);
			prepStmt.setString(7, file.md5);
			prepStmt.setString(8, file.sha1);
			prepStmt.setString(9, file.crc32);
			prepStmt.setString(10, file.dub);
			prepStmt.setString(11, file.sub);
			prepStmt.setString(12, file.src);
			prepStmt.setString(13, file.audio);
			prepStmt.setString(14, file.video);
			prepStmt.setString(15, file.res);
			prepStmt.setString(16, file.file_type);
			prepStmt.setString(17, file.grp);
			prepStmt.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("ERROR: Cannot add episode to local database");
			e.printStackTrace();
		}
	}
	
	public void addJob(LocalFile local_file) {
		// TODO Unicode check
		String AddStatement = "INSERT OR IGNORE INTO job (filename, fid, "
				+ "drive_name, folder, last_checked) VALUES (?,?,?,?,?)";
		PreparedStatement prepStmt;		
		try {
			prepStmt = this.connection.prepareStatement(AddStatement);
			prepStmt.setString(1, local_file.filename);
			prepStmt.setInt(2, local_file.fid);
			prepStmt.setString(3, local_file.drive_name);
			prepStmt.setString(4, local_file.folder);
			prepStmt.setString(5, getTimestamp());
			prepStmt.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("ERROR: Cannot add episode to local database");
			e.printStackTrace();
		}
	}
	
	public Boolean inDB(String tableName, int id) {
		if (this.tableList.contains(tableName)) {
			String selectStatement = "SELECT * FROM " + tableName  
					+ " WHERE " + tableName.charAt(0) + "id ="+ id;
			try {
				ResultSet result = statement.executeQuery(selectStatement);
				if (result.next()) { // No item found
					return true;
				} else {
					return false;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return false;
		} else {
			System.out.println("Table not in database");
			return false;
		}
	}
	
	public HashMap<String, String> getInfoAid(int aid) {
		HashMap<String,String> result = new HashMap<String,String>();
		String selectStatement = "SELECT * FROM anime WHERE aid = ?";
		PreparedStatement prepStmt;
		
		try {
			prepStmt = this.connection.prepareStatement(selectStatement);
			prepStmt.setInt(1, aid);
			ResultSet returnTable = prepStmt.executeQuery();
			if (returnTable.next()) {
				for (String columnName:this.animeColumn){
					String data = returnTable.getObject(columnName).toString();
					result.put(columnName, data);
					System.out.print(columnName);
					System.out.println(data);
				}
			} else { // No result found, return blank
				return result;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public Anime findAnime(int aid){
		String selectStatement = "SELECT * FROM anime WHERE aid = ?";
		PreparedStatement prepStmt;
		try {
			prepStmt = this.connection.prepareStatement(selectStatement);
			prepStmt.setInt(1, aid);
			ResultSet returnTable = prepStmt.executeQuery();
			if (returnTable.next()) {
				String episodes = returnTable.getObject("episodes").toString();
				String year = returnTable.getObject("year").toString();
				String romanji_name = returnTable.getObject("romanji_name").toString();
				String eng_name = returnTable.getObject("eng_name").toString();
				String kanji_name = returnTable.getObject("kanji_name").toString();
				Anime anime = new Anime(aid, episodes, year, romanji_name, eng_name, kanji_name);
				return anime;
			} else { // No result found, return blank
				return null;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public HashMap<String, String> getInfoEid(int eid) {
		HashMap<String,String> result = new HashMap<String,String>();
		String selectStatement = "SELECT * FROM episode WHERE eid = ?";
		PreparedStatement prepStmt;
		
		try {
			prepStmt = this.connection.prepareStatement(selectStatement);
			prepStmt.setInt(1, eid);
			ResultSet returnTable = prepStmt.executeQuery();
			if (returnTable.next()) {
				for (String columnName:this.episodeColumn){
//					System.out.println(columnName);
					String data = returnTable.getObject(columnName).toString();
					result.put(columnName, data);
					System.out.print(columnName);
					System.out.println(data);
				}
			} else { // No result found, return blank
			}
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public Episode findEpisode(int eid){
		String selectStatement = "SELECT * FROM episode WHERE eid = ?";
		PreparedStatement prepStmt;
		try {
			prepStmt = this.connection.prepareStatement(selectStatement);
			prepStmt.setInt(1, eid);
			ResultSet returnTable = prepStmt.executeQuery();
			if (returnTable.next()) {
				int aid = parseInt(returnTable.getObject("aid").toString());
				String epno = returnTable.getObject("epno").toString();
				String romanji_name = returnTable.getObject("romanji_name").toString();
				String eng_name = returnTable.getObject("eng_name").toString();
				String kanji_name = returnTable.getObject("kanji_name").toString();
				Episode episode = new Episode(eid, aid, epno, eng_name, romanji_name, 
						kanji_name);
				return episode;
			} else { // No result found, return blank
				return null;
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	
	}
	
	public HashMap<String, String> getInfoFid(int fid) {
		HashMap<String,String> result = new HashMap<String,String>();
		String selectStatement = "SELECT * FROM file WHERE fid = ?";
		PreparedStatement prepStmt;
		
		try {
			prepStmt = this.connection.prepareStatement(selectStatement);
			prepStmt.setInt(1, fid);
			ResultSet returnTable = prepStmt.executeQuery();
			if (returnTable.next()) {
				for (String columnName:this.fileColumn){
//					System.out.println(columnName);
					String data = returnTable.getObject(columnName).toString();
					result.put(columnName, data);
					System.out.print(columnName);
					System.out.println(data);
				}
			} else { // No result found, return blank
			}
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public File findFile(int fid){
		String selectStatement = "SELECT * FROM file WHERE fid = ?";
		PreparedStatement prepStmt;
		
		try {
			prepStmt = this.connection.prepareStatement(selectStatement);
			prepStmt.setInt(1, fid);
			ResultSet returnTable = prepStmt.executeQuery();
			if (returnTable.next()) {
				int aid = parseInt(returnTable.getObject("aid").toString());
				int eid = parseInt(returnTable.getObject("eid").toString());
				int gid = parseInt(returnTable.getObject("gid").toString());
				int size = parseInt(returnTable.getObject("size").toString());
				String ed2k = returnTable.getObject("ed2k").toString();
				String md5 = returnTable.getObject("md5").toString();
				String sha1 = returnTable.getObject("sha1").toString();
				String crc32 = returnTable.getObject("crc32").toString();
				String dub = returnTable.getObject("dub").toString();
				String sub = returnTable.getObject("sub").toString();
				String src = returnTable.getObject("src").toString();
				String audio = returnTable.getObject("audio").toString();
				String video = returnTable.getObject("video").toString();
				String res = returnTable.getObject("res").toString();
				String grp = returnTable.getObject("grp").toString();
				String file_type = returnTable.getObject("file_type").toString();
				File file = new File(fid, aid, eid, gid, size, ed2k, md5, sha1, crc32, 
						dub, sub, src, audio, video, res, grp, file_type);
				return file;
			} else { // No result found, return blank
				return null;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public HashMap<String, String> getInfoHash(int size, String ed2k) {
		HashMap<String,String> result = new HashMap<String,String>();
		String selectStatement = "SELECT * FROM file WHERE (size = ? AND "
				+ "ed2k = ?)";
		PreparedStatement prepStmt;
		
		try {
			prepStmt = this.connection.prepareStatement(selectStatement);
			prepStmt.setInt(1, size);
			prepStmt.setString(2, ed2k);
			ResultSet returnTable = prepStmt.executeQuery();
			if (returnTable.next()) {
				for (String columnName:this.fileColumn){
//					System.out.println(columnName);
					String data = returnTable.getObject(columnName).toString();
					result.put(columnName, data);
					System.out.print(columnName);
					System.out.println(data);
				}
			} else { // No result found, return blank
				return null;
			}
//			System.out.println(result);
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	public HashMap<String, String> getInfoFilename(String filename) {
		HashMap<String,String> result = new HashMap<String,String>();
		String selectStatement = "SELECT * FROM job WHERE filename = ?";
		PreparedStatement prepStmt;
		
		try {
			prepStmt = this.connection.prepareStatement(selectStatement);
			prepStmt.setString(1, filename);
			ResultSet returnTable = prepStmt.executeQuery();
			if (returnTable.next()) {
				for (String columnName:this.jobColumn){
//					System.out.println(columnName);
					String data = returnTable.getObject(columnName).toString();
					result.put(columnName, data);
					System.out.print(columnName);
					System.out.println(data);
				}
			} else { // No result found, return blank
				
			}
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public LocalFile findLocalFile(String filename){
		String selectStatement = "SELECT * FROM job WHERE filename = ?";
		PreparedStatement prepStmt;
		
		try {
			prepStmt = this.connection.prepareStatement(selectStatement);
			prepStmt.setString(1, filename);
			ResultSet returnTable = prepStmt.executeQuery();
			if (returnTable.next()) {
				int fid = parseInt(returnTable.getObject("fid").toString());
				String folder = returnTable.getObject("folder").toString();
				String drive_name = returnTable.getObject("drive_name").toString();
				String last_checked = returnTable.getObject("last_checked").toString();
				LocalFile local_file = new LocalFile(filename, fid, folder, drive_name, 
						last_checked);
				return local_file;
			} else { // No result found, return blank
				return null;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public String updateJob(String filename) {
		// Get current UTC time as seconds
		
		
		String AddStatement = "UPDATE job SET last_checked = datetime(?)"
                       +" WHERE filename = ?";
		PreparedStatement prepStmt;
		String timeStamp = getTimestamp();
		try {
			prepStmt = this.connection.prepareStatement(AddStatement);
			prepStmt.setString(2, filename);
			prepStmt.setString(1, timeStamp);
			prepStmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return timeStamp.substring(0,19);
		
	}
	
	public ArrayList<HashMap<String,String>> getAllJob() {
		ArrayList<HashMap<String,String>> result;
		String selectStatement = "SELECT anime.romanji_name, anime.episodes,"
				+ "episode.epno, episode.eng_name, file.fid, job.filename, "
				+ "job.folder, job.last_checked, file.size FROM anime, episode, "
				+ "file, job WHERE (file.eid = episode.eid AND "
				+ "file.aid = anime.aid AND job.fid = file.fid)";
		String[] req_fields = {"anime_name", "anime_episodes", "epno", "ep_name", "fid", 
		          "file_name", "folder", "last_checked", "size"};
		
		try {
			ResultSet returnTable;
			returnTable = this.statement.executeQuery(selectStatement);
			int size = returnTable.getFetchSize();
			result = new ArrayList<HashMap<String,String>>(size);
			int index = 0;
			HashMap<String, String> temp;
			while (returnTable.next()) {
				temp = new HashMap<String,String>();
				for (int i = 0; i < req_fields.length; i++) {
					String field = req_fields[i];
					String data = returnTable.getObject(i+1).toString();
					temp.put(field, data);	
				}
				result.add(index,temp);
				index++;
				
			}
			
			return result;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	
	public void addEntry(int aid, int eid, int gid, int fid, String animeRomanjiName, 
			String episodes, String year, String animeEngName, String animeKanjiName,
			String epEngName, String epno, String epRomanjiName, 
			String epKanjiName, int size, String ed2k, String md5, String sha1, 
			String crc32, String dub, String sub, String src, String audio, 
			String video, String res, String fileType, String grp, 
			String fileName, String driveName, String folder) {
		
		Anime anime = new Anime(aid, episodes, year, animeRomanjiName, animeEngName, animeKanjiName);
		this.addAnime(anime);
		System.out.println("Adding episode: "+ epno);
		Episode episode = new Episode(eid, aid, epno, epEngName, epRomanjiName, epKanjiName);
		this.addEpisode(episode);
		File file = new File(fid, aid, eid, gid, size, ed2k, md5, sha1, crc32, dub, 
				sub, src, audio, video, res, fileType, grp);
		this.addFile(file);
		LocalFile local_file = new LocalFile(fileName, fid, folder, driveName, "never");
		this.addJob(local_file);
	}
	
	public static String getTimestamp(){
		java.util.Date utcTime = new java.util.Date();
		long sec = utcTime.getTime();
		sec = sec - sec % 1000;
		java.sql.Timestamp ts = new java.sql.Timestamp(sec);
		return ts.toString().substring(0,19);
	}
	

}
