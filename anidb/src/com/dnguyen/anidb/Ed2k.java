package com.dnguyen.anidb;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

public class Ed2k {
	public static String getEd2k(InputStream in) throws Exception {
		/*
		 * Return ed2k hash of an input stream as a string in lowercase
		 */
		
		int CHUNK_SIZE = 9500 * 1024; 
	    MessageDigest md4 = MessageDigest.getInstance("MD4", new BouncyCastleProvider());
	    byte[] buffer = new byte[8192];
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    int len = 0, chunk = 0;
	    while ((len = in.read(buffer)) > 0) {
	        if (chunk + len >= CHUNK_SIZE) {
	            int offset = CHUNK_SIZE - chunk;
	            md4.update(buffer, 0, offset);
	            out.write(md4.digest());
	            md4.reset();
	            chunk = len - offset;
	            md4.update(buffer, offset, chunk);
	        } else {
	            md4.update(buffer, 0, len);
	            chunk += len;
	        }    
	    }
	    out.write(md4.digest());
	    md4.reset();
	 
	    if (out.size() > md4.getDigestLength()) {
	        md4.update(out.toByteArray());
	        out.reset();
	        out.write(md4.digest());
	    }
	 
	    return Hex.toHexString(out.toByteArray()).toUpperCase();
	}
	
	public static String getEd2k(String filePath) {
		try {
			InputStream is = Files.newInputStream(Paths.get(filePath));
			try {
				String hash = getEd2k(is);
				is.close();
				return hash;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("ERROR: Cannot access file for hashing");
			e.printStackTrace();
		}
		return "";
	}
	
	public static String getEd2k(File file) {
		String filePath = file.getAbsolutePath();
		return getEd2k(filePath);
	}
	
	public static void main(String[] args) throws Exception {

//		InputStream is = Files.newInputStream(Paths.get("[FFF] Hataraku Maou-sama! - OP04 [BD][1080p-FLAC][6E1759A3].mkv"));
//		DigestInputStream dg = new DigestInputStream(is, md4);
//		byte[] digest = new byte[16]; 
//		md4.doFinal(digest,0);
//		
////		try (InputStream is = Files.newInputStream(Paths.get("config.txt"))) {
////		  DigestInputStream dis = new DigestInputStream(is, md);
////		  /* Read stream to EOF as normal... */
////		}
////		byte[] digest = md.digest();
//		byte[] b = digest;
//	       String result = "";
//
//	       for (int i=0; i < b.length; i++) {
//	           result += Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
//	       }
//	    System.out.println(result);
	    
		System.out.println(getEd2k("[FFF] Hataraku Maou-sama! - OP04 [BD][1080p-FLAC][6E1759A3].mkv").toLowerCase());
	}
}
