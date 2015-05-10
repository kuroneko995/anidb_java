package com.dnguyen.anidb;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;


public class UDPConnector {
	/**
	 * A class that handles all communication with anidb.net server via their UDP API
	 * Refer here for anidb.net API
	 * http://wiki.anidb.net/w/UDP_API_Definition#Authing_Commands
	 * 
	 */
	private int anidbPort, myPort;
	private String anidbAddress;
	private String sessionID;
	private String username, password;
	private DatagramSocket udpSocket = null;
	private long timer;
	
	private String[] anime_amask_full = {"aid","unused","year","type","related_aid_list",
		      "related_aid_type","category_list","category_weight_list",
		      "romanji_name","kanji_name","eng_name","other_name",
		      "short_name_list","synonym_list","retired","retired","episodes",
		      "highest_episode_number","special_ep_count","air_date","end_date",
		      "url","picname","category_id_list","rating","vote_count",
		      "temp_rating","temp_vote_count","average_review_rating",
		      "review_count","award_list","is_18_restricted","anime_planet_id",
		      "ANN_id","allcinema_id","AnimeNfo_id","unused","unused","unused",
		      "date_record_updated","character_id_list","creator_id_list",
		      "main_creator_id_list","main_creator_name_list","unused","unused",
		      "unused","unused","specials_count","credits_count","other_count",
		      "trailer_count","parody_count","unused","unused","unused"};
	
    private String [] file_amask_full = {"anime_total_episodes","highest_episode_number",
    		"year","file_type","related_aid_list","related_aid_type",
    		"category_list","reserved","romanji_name","kanji_name",
    		"english_name","other_name","short_name_list","synonym_list",
    		"retired","retired","epno","ep_name","ep_romanji_name",
    		"ep_kanji_name","episode_rating","episode_vote_count","unused",
    		"unused","group_name","group_short_name","unused","unused",
    		"unused","unused","unused","date_aid_record_updated"};
    
    private String[] file_fmask_full = {"unused","aid","eid","gid","mylist_id",
			"list_other_episodes","IsDeprecated","state","size","ed2k",
			"md5","sha1","crc32","unused","unused","reserved","quality",
			"src","audio","audio_bitrate_list","video","video_bitrate",
			"res","file_type","dub","sub","length_in_seconds","description",
			"aired_date","unused","unused","anidb_file_name","mylist_state",
			"mylist_filestate","mylist_viewed","mylist_viewdate",
			"mylist_storage","mylist_source","mylist_other","unused"};
	
    
	public UDPConnector(String anidbAddress, int anidbPort, int myPort, 
			String username, String password) {
		/**
		 * Set up the UDP Connector. Load address and port from 
		 * config.properties. 
		 * Also load anidb username and password
		 */
		this.anidbAddress = anidbAddress;
		this.anidbPort = anidbPort;
		this.myPort = myPort;
		this.username = username;
		this.password = password;
		
		
		// Initialize a datagram socket
		try { 
			udpSocket = new DatagramSocket(myPort);
		} catch (SocketException e) {
			System.out.println("Could not create socket");
			e.printStackTrace();
		}
		
		// Start timer
		timer = System.currentTimeMillis();
	 
	}
	
	
	public Boolean authenticate() {
		/**
		 * Authenticate to anidb using username and password in config files
		 * Set the session ID for the UDP Connector
		 * TODO: Add automatic pinging to keep connection alive
		 */
		String msg = String.format("AUTH user=%s&pass=%s&protover=3&"
					+ "client=anidbfilemanager&clientver=0&"
					+ "nat=1&enc=utf-8",this.username, this.password);
		this.sendMessage(msg);
		String message = this.receiveMessage();
		
		String[] data = message.split(" ");
		if (data[0].equals("200")) {
			System.out.println("Authentication successful");
			this.sessionID = data[1];
			return true;
		} else {
			System.out.println("Authentication failed: " + message);
			return false;
		}
	}
	
	public String receiveMessage() {
		DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);
		try {
			this.udpSocket.receive(packet);
			byte[] receivedData = packet.getData();
			String message = new String(receivedData, "UTF-8");
			return message;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return "";
	}
	
	public void close() {
		this.udpSocket.close();
	}
	
	public void sendMessage(String msg) {
		byte[] msgByte = msg.getBytes(Charset.forName("UTF-8")); 
//		System.out.print("Sending message:");
//		System.out.println(msg);
		try {
			InetAddress address = InetAddress.getByName(this.anidbAddress);
			DatagramPacket packet = new DatagramPacket(msgByte, msgByte.length, 
					address, this.anidbPort);
			long currentTime = System.currentTimeMillis();
			long diff = currentTime - this.timer;
			if (diff < 4000) {
				try {
				    System.out.println(String.format
				    		("Waiting for %d milliseconds", 4000-diff));
					Thread.sleep(4000-diff);
				} catch(InterruptedException ex) {
				    Thread.currentThread().interrupt();
				}
			}
			
			try {
				this.udpSocket.send(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		this.timer = System.currentTimeMillis();
	}
	
	public HashMap<String, String> getAnimeInfo(int aid, List<String> reqFields) {

		
//        System.out.println(bitstr);
        String amask =  getMask(this.anime_amask_full, reqFields);
//        System.out.println(amask);
        String msg = String.format("ANIME aid=%d&amask=%s&s=%s",
        		aid, amask, this.sessionID);
        this.sendMessage(msg);	
        String data = this.receiveMessage();
//        System.out.println(data);
        if (data.contains("230 ANIME")) { // Anime found reply
    		System.out.println("Anidb: Anime found");
    		data = data.split("\n")[1]; // Ignore first line
    		String[] fields = data.split("\\|");
    		HashMap<String, String> result = new HashMap<String, String>();
    		for (int i = 0; i < reqFields.size(); i++) {
    			result.put(reqFields.get(i), fields[i]);
    		}
    		return result;
        } else {	// No anime found
        	System.out.println("Anidb: Anime not found");
        	return null;
        } 
	}
	
	public HashMap<String, String> getEpisodeInfo(int eid) {
		// epno special character S(special), C(credits), T(trailer), P(parody)
		// , O(other).
		ArrayList<String> reqFields = new ArrayList<String>(
				Arrays.asList("eid", "aid", "epno", "eng_name", 
						"romanji_name", "kanji_name"));
		String msg = String.format("EPISODE eid=%d&s=%s", eid, this.sessionID);
		this.sendMessage(msg);
		String data = this.receiveMessage();
//		System.out.println(data);
		if (data.contains("240 EPISODE")) { // Episode found
			data = data.split("\n")[1]; // Ignore first line
			String[] fields = data.split("\\|");
			HashMap<String, String> result = new HashMap<String, String>();
			for (int i = 0; i < reqFields.size(); i++) {
				result.put(reqFields.get(i), fields[i]);
			}
			return result;
		} else { // No episode found
			System.out.println("Anidb: No episode found");
			return null;
		}
		
	}
	
	public HashMap<String, String> getFileInfo(int size, String ed2k, 
			List<String> reqFields) {
		String fbitstr = "";
		String abitstr = "";
		ArrayList<String> returnFields = new ArrayList<String>(reqFields.size());
		
		// fid always returned as first field
		// See anidb UDP Definition for more info
		int returnFieldsIndex = 1;
		returnFields.add(0, "fid");
		
		String[] fmap = {"unused","aid","eid","gid","mylist_id",
				"list_other_episodes","IsDeprecated","state","size","ed2k",
				"md5","sha1","crc32","unused","unused","reserved","quality",
				"src","audio","audio_bitrate_list","video","video_bitrate",
				"res","file_type","dub","sub","length_in_seconds","description",
				"aired_date","unused","unused","anidb_file_name","mylist_state",
				"mylist_filestate","mylist_viewed","mylist_viewdate",
				"mylist_storage","mylist_source","mylist_other","unused"};
		for (int i = 0; i < fmap.length; i++) {
        	if (reqFields.contains(fmap[i])) {
        		fbitstr += "1";
        		returnFields.add(returnFieldsIndex,fmap[i]);
        		returnFieldsIndex ++;
        	} else {
        		fbitstr += "0";
        	}
        }
        String fmask =  String.format("%010X", Long.parseLong(fbitstr,2));
        
        String [] amap = {"anime_total_episodes","highest_episode_number",
        		"year","file_type","related_aid_list","related_aid_type",
        		"category_list","reserved","romanji_name","kanji_name",
        		"english_name","other_name","short_name_list","synonym_list",
        		"retired","retired","epno","ep_name","ep_romanji_name",
        		"ep_kanji_name","episode_rating","episode_vote_count","unused",
        		"unused","group_name","group_short_name","unused","unused",
        		"unused","unused","unused","date_aid_record_updated"};
        for (int i = 0; i < amap.length; i++) {
        	if (reqFields.contains(amap[i])) {
        		abitstr += "1";
        		returnFields.add(returnFieldsIndex,amap[i]);
        		returnFieldsIndex ++;
        	} else {
        		abitstr += "0";
        	}
        }
        String amask =  String.format("%08X", Long.parseLong(abitstr,2));
        
        // Format request MSG based on needed fields
        String msg = String.format("FILE size=%d&ed2k=%s&fmask=%s&amask=%s&s=%s",
        		size, ed2k, fmask, amask, this.sessionID); 
        this.sendMessage(msg);
        String data = this.receiveMessage();
        System.out.println(data);
        if (data.contains("220 FILE")) { // File found on anidb
        	System.out.println("Anidb: File found");
        	data = data.split("\n")[1]; // Ignore first line of response
    		String[] fields = data.split("\\|");
    		HashMap<String, String> result = new HashMap<String, String>();
    		
    		result.put("fid", fields[0]); 
    		
    		for (int i = 0; i < returnFields.size(); i++) {
    			result.put(returnFields.get(i), fields[i]);
    		}
    		
//    		for (String name: result.keySet()){
//                String key =name.toString();
//                String value = result.get(name);  
//                System.out.println(key + " " + value);  
//			} 
    		return result;
            
        	
        } else { // No file found
        	System.out.println("Anidb: File not found");
        	return null;
        }
	}
	
	public HashMap<String, String> getFileInfo(int size, String ed2k) {
		/*
		 * Request default fields
		 */
		ArrayList<String> req_fields = new ArrayList<String>(Arrays.asList("fid", 
				"aid", "eid", "gid", "size", "ed2k", "md5", "sha1","crc32", 
				"dub", "sub", "src", "audio", "video", "res", "file_type", 
				"group_short_name","epno","ep_name", "ep_romanji_name", 
				"ep_kanji_name","year", "anime_total_episodes", "romanji_name", 
				"english_name",	"kanji_name")); 
		HashMap<String,String> result = this.getFileInfo(size, ed2k, req_fields);
//		System.out.println(result);
		return result;
	}
	
	
	private String getMask(String[] full_list, List<String> req_list) {
		/**
		 * Return a hex string that serves as the bit mask for the request.
		 */
		String result = "";
		for (int i = 0; i < full_list.length; i++) {
	    	if (req_list.contains(full_list[i])) {
	    		result += "1";
	    	} else {
	    		result += "0";
	    	}
	    }
		return String.format("%08X", Long.parseLong(result,2));
	}
}
