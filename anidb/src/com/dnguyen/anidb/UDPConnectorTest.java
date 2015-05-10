/**
 * 
 */
package com.dnguyen.anidb;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author ndminh92
 *
 */
public class UDPConnectorTest {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}


	/**
	 * Test method for {@link com.dnguyen.anidb.UDPConnector#authenticate()}.
	 */
	@Test
	public final void testAuthenticate() {
		UDPConnector new_connection = new UDPConnector("api.anidb.net", 9000, 1500,
				"ndminh92", "9AkoOzsS9xUg8z");
		assertEquals(new_connection.authenticate(),true);
		new_connection.close();
		UDPConnector wrong_pwd = new UDPConnector("api.anidb.net", 9000, 1501,
				"ndminh92", "darkraven");
		assertEquals(wrong_pwd.authenticate(),false);
		wrong_pwd.close();
	}

	/**
	 * Test method for {@link com.dnguyen.anidb.UDPConnector#receiveMessage()}.
	 */
	@Test
	public final void testReceiveMessage() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.dnguyen.anidb.UDPConnector#sendMessage(java.lang.String)}.
	 */
	@Test
	public final void testSendMessage() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.dnguyen.anidb.UDPConnector#getAnimeInfo(int, java.util.List)}.
	 */
	@Test
	public final void testGetAnimeInfo() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.dnguyen.anidb.UDPConnector#getEpisodeInfo(int)}.
	 */
	@Test
	public final void testGetEpisodeInfo() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.dnguyen.anidb.UDPConnector#getFileInfo(int, java.lang.String, java.util.List)}.
	 */
	@Test
	public final void testGetFileInfoIntStringListOfString() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.dnguyen.anidb.UDPConnector#getFileInfo(int, java.lang.String)}.
	 */
	@Test
	public final void testGetFileInfoIntString() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.dnguyen.anidb.UDPConnector#main(java.lang.String[])}.
	 */
	@Test
	public final void testMain() {
		fail("Not yet implemented");
	}

}
