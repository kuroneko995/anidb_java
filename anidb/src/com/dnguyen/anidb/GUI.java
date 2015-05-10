package com.dnguyen.anidb;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import org.jdesktop.swingx.JXTreeTable;

public class GUI extends JFrame implements ActionListener {

	private JPanel contentPane;
	private JXTreeTable treeTable;
	private JButton btnOpenFolder, btnAddFiles;
	private JRadioButton rdbtnShowUnavailableFiles;
	private JFileChooser fc;
	
	private MyTreeTableModel md2;
	private MyTreeTableModel  mdAvailable;
	private Database db;
	private UDPConnector udp;
	private ArrayList<File> currentFiles;
	
	private final ArrayList<String> FILE_TYPES = new ArrayList<String>(
			Arrays.asList("avi", "mkv", "mp4"));


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI frame = new GUI();
					frame.setVisible(true);
					frame.setMinimumSize(new Dimension(800, 600));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void actionPerformed(ActionEvent e) {
		/*
		 * Handle button click for the GUI
		 */
	    if ("openFolder".equals(e.getActionCommand())) {
	        int rowNumber = treeTable.getSelectedRow();
	        String fileName = (String) treeTable.getValueAt(rowNumber, 0);
	        if (md2.hasAnime(fileName)) {
	        	// Is an anime. Nothing to do here.
	        } else { // is episode
	        	TreePath path = treeTable.getPathForRow(rowNumber);
	        	MyTreeTableNode anime1 = (MyTreeTableNode) path.getPathComponent(1);
	        	String filePath = anime1.getChild(fileName).getPath();	
	        	File file = new File (filePath);
	        	Desktop desktop = Desktop.getDesktop();
	        	if (file.isDirectory()) {
		        	try {
						desktop.open(file);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
	        	} else {
	        		System.out.println("File not available");
	        	}		
	        }
	    } else if ("ConnectUDP".equals(e.getActionCommand())){
	    	udp.authenticate();
	    } else if ("Rehash".equals(e.getActionCommand())){
	    	int rowNumber = treeTable.getSelectedRow();
	        String fileName = (String) treeTable.getValueAt(rowNumber, 0);
	        if (md2.hasAnime(fileName)) {
	        	// Is an anime. Nothing to do here.
	        } else { // is episode
	        	TreePath path = treeTable.getPathForRow(rowNumber);
	        	MyTreeTableNode anime1 = (MyTreeTableNode) path.getPathComponent(1);
	        	MyTreeTableNode episode = anime1.getChild(fileName);
	        	String animeName = anime1.getName();
	        	String fileFolder = episode.getPath();	
	        	
	        	Path filePath = Paths.get(fileFolder, fileName);
//	        	System.out.println(filePath);
	        	File file = filePath.toFile();
	        	if (file.isFile()) {
		        	String ed2k = Ed2k.getEd2k(filePath.toString());
		        	// TO-DO: add checking mechanism
		        	
		        	String newChecked = db.updateJob(fileName);
		        	this.md2.updateLastChecked(animeName, fileName, newChecked);
		        	this.mdAvailable.updateLastChecked(animeName, fileName, newChecked);
	        	} else {
	        		System.out.println("File not available");
	        	}			
	        }
	        
	    } else if (e.getSource() == rdbtnShowUnavailableFiles){
	    	if (rdbtnShowUnavailableFiles.isSelected()) {
	    		this.treeTable.setTreeTableModel(md2);
	    	} else {
	    		this.treeTable.setTreeTableModel(mdAvailable);
	    	}
	    	setupTable(treeTable);
	    	
	    } else if (e.getSource() == btnAddFiles) {
            int returnVal = fc.showOpenDialog(GUI.this);
            ArrayList<File> fileList = new ArrayList<File>();
            
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File[] selectedFiles = fc.getSelectedFiles();
            	for (File file: selectedFiles) {
            		
//	            	File file = fc.getSelectedFile();
	                //This is where a real application would open the file.
//	                System.out.println("Opening: " + file.getAbsolutePath());
//	                String fldr = file.toPath().getRoot().toString();
	                
	                fileList.add(file);
	                for (int i = 0; i < fileList.size(); i++) {
	                	File temp = fileList.get(i);
	                	if (temp.isDirectory()) {
	                		fileList.addAll(Arrays.asList(temp.listFiles()));
	                	}
	                }
	                
//	                for (File myFile:fileList) {
//	                	this.currentFile = myFile;
	                
//	                }
                
            	}
            	currentFiles = fileList;
                System.out.println("Line 158");
                fileCheck a = new fileCheck();
                a.execute();
            } else {
            	System.out.println("Open command cancelled by user");
            }
	    }
	
	}
	
	


	public GUI() {
		udp = start_UDP();
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		btnAddFiles = new JButton("Add files");
		menuBar.add(btnAddFiles);
		btnAddFiles.addActionListener(this);
		
		JButton btnConnect = new JButton("Connect");
		menuBar.add(btnConnect);
		btnConnect.setActionCommand("ConnectUDP");
		btnConnect.addActionListener(this);
		
		btnOpenFolder = new JButton("Open folder");
		menuBar.add(btnOpenFolder);
		btnOpenFolder.setActionCommand("openFolder");
		btnOpenFolder.addActionListener(this);
		
		JButton btnRehash = new JButton("Rehash");
		menuBar.add(btnRehash);
		btnRehash.setActionCommand("Rehash");
		btnRehash.addActionListener(this);
		
		rdbtnShowUnavailableFiles = new JRadioButton("Show unavailable files");
		menuBar.add(rdbtnShowUnavailableFiles);
		rdbtnShowUnavailableFiles.setActionCommand("ToggleView");
		rdbtnShowUnavailableFiles.setSelected(true);
		rdbtnShowUnavailableFiles.addActionListener(this);
		
		fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		fc.setMultiSelectionEnabled(true);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JTextArea textArea = new JTextArea();
		textArea.setRows(7);
		
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		PrintStream printStream = new PrintStream(new CustomOutputStream(textArea));
		
//		System.setOut(printStream);
//		System.setErr(printStream);
//		System.out.println("Output stream now redirects here");
		
		JScrollPane text = new JScrollPane(textArea);
		text.setMaximumSize(new Dimension(1000,100));
		contentPane.add(text, BorderLayout.SOUTH);
		
		
		MyTreeTableNode myRoot = new MyTreeTableNode("name", "eps", 0, "never",false);
		myRoot.setAvailability(true);
		this.md2 = new MyTreeTableModel(myRoot);
		md2.addTreeModelListener(new MyTreeModelListener());
		
		try {
			this.db = new Database();
			getJobFromDatabase(this.db.getAllJob(),md2,true);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		MyTreeTableNode myRoot2 = new MyTreeTableNode("name", "eps", 0, "never",false);
		myRoot.setAvailability(true);
		
		this.mdAvailable = new MyTreeTableModel(myRoot2);
		mdAvailable.addTreeModelListener(new MyTreeModelListener());
		
		try {
			this.db = new Database();
			getJobFromDatabase(this.db.getAllJob(),mdAvailable,false);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		treeTable = new JXTreeTable(md2);
		setupTable(treeTable);
        
        
        JScrollPane sp = new JScrollPane(treeTable);
     
		contentPane.add(sp,BorderLayout.CENTER);
       
	}
	
	public static void setupTable(JXTreeTable tb) {
		tb.setRootVisible(false);  // hide the root
		tb.setEditable(false);
		tb.getColumnModel().getColumn(0).setMinWidth(300);
		tb.getColumnModel().getColumn(1).setMinWidth(60);
		tb.getColumnModel().getColumn(1).setMaxWidth(60);
		tb.getColumnModel().getColumn(2).setMinWidth(80);
		tb.getColumnModel().getColumn(2).setMaxWidth(80);
		tb.getColumnModel().getColumn(3).setMinWidth(130);
		tb.getColumnModel().getColumn(3).setMaxWidth(130);
	}
	
	public static void getJobFromDatabase(
			ArrayList<HashMap<String,String>> allJob, MyTreeTableModel md2, Boolean showUnavailable) {
		for (HashMap<String,String> job: allJob) {
//			{"anime_name", "anime_episodes", "epno", "ep_name", "fid", 
//		          "file_name", "folder", "last_checked", "size"}
			
			String animeName = job.get("anime_name");
			String fileName = job.get("file_name");
			String epNumber = job.get("epno");
			String lastChecked = job.get("last_checked");
			int size = (Integer.parseInt(job.get("size"))) / 1000000 ;
			String filePath = job.get("folder");
			String animeEps = job.get("anime_episodes");
			
		
			File file = new File (filePath);
			Boolean isAvailable = file.isDirectory();

        	
        	if (showUnavailable || isAvailable) {
				md2.addEntry(animeName, animeEps, fileName, epNumber, size, 
						lastChecked, filePath, isAvailable);
        	}
		}
		
	}
	
	class MyTreeModelListener implements TreeModelListener {
        public void treeNodesChanged(TreeModelEvent e) {
            DefaultMutableTreeNode node;
            node = (DefaultMutableTreeNode)(e.getTreePath().getLastPathComponent()); 
                int index = e.getChildIndices()[0];
                node = (DefaultMutableTreeNode)(node.getChildAt(index));  
        }
        public void treeNodesInserted(TreeModelEvent e) {
        	
        }
        public void treeNodesRemoved(TreeModelEvent e) {
        }
        public void treeStructureChanged(TreeModelEvent e) {
        }
    }
	
	public class CustomOutputStream extends OutputStream {
	    private JTextArea textArea;
	     
	    public CustomOutputStream(JTextArea textArea) {
	        this.textArea = textArea;
	    }
	     
	    @Override
	    public void write(int b) throws IOException {
	        // redirects data to the text area
	        textArea.append(String.valueOf((char)b));
	        // scrolls the text area to the end of data
	        textArea.setCaretPosition(textArea.getDocument().getLength());
	    }
	}

	
	class fileCheck extends SwingWorker<Boolean, String> {
		
		public fileCheck() {
			super();
		}
		
		@Override
		public Boolean doInBackground() {
			System.out.println("Worker running");
			for (File file: currentFiles) {
				addFile(file);
			}
			return true;
			
		}
	}

/*
	public void addOneFile(File file) {
		String fileName = file.getName();
		String file_type = fileName.substring(fileName.length() - 3);
		if (!file.isFile() || !FILE_TYPES.contains(file_type)) {
			return;
		} else {
			System.out.println("Checking: " + file.getName());
			HashMap<String, String> fileInfo;
			String ed2k = Ed2k.getEd2k(file);
			Integer size = ((int) (long) file.length()) ;
			String folder = file.toPath().getParent().toString();
			String drive_name = file.toPath().getRoot().toString();
			
			fileInfo = this.db.getInfoHash(size, ed2k);
			System.out.println("ed2k hash: "+ed2k+ ". File size: "+ size.toString());
			if (fileInfo == null) {
				System.out.print("File not found in local db. Searching anidb.");
				fileInfo = this.udp.getFileInfo(size, ed2k);
				if (fileInfo == null) {
					System.out.println("File cannot be identified");
					return;
				} else {
					System.out.println("File found on anidb");
				}
			} else {
				System.out.println("File found in local db");
				int fid = Integer.parseInt(fileInfo.get("fid"));
				AnimeLocalFile local_file = new AnimeLocalFile(fileName, fid, folder, drive_name, "never");
				this.db.addJob(local_file);
				System.out.println("File has been added to database");
				return;
			}
			
			
			int fid = Integer.parseInt(fileInfo.get("fid"));
			int aid = Integer.parseInt(fileInfo.get("aid"));
			int eid = Integer.parseInt(fileInfo.get("eid"));
			int gid = Integer.parseInt(fileInfo.get("gid"));
			String md5 = fileInfo.get("md5");
			String sha1 = fileInfo.get("sha1");
			String crc32 = fileInfo.get("crc32");
			String dub = fileInfo.get("dub");
			String sub = fileInfo.get("sub");
			String src = fileInfo.get("src");
			String audio = fileInfo.get("audio");
			String video = fileInfo.get("video");
			String res = fileInfo.get("res");
//			String file_type = fileInfo.get("file_type");
			String grp = fileInfo.get("group_short_name");
			String animeRomanjiName = fileInfo.get("romanji_name");
			String episodes = fileInfo.get("anime_total_episodes");
			String year = fileInfo.get("year");
			String animeEngName = fileInfo.get("english_name");
			String animeKanjiName = fileInfo.get("kanji_name");
			String epEngName = fileInfo.get("ep_name");
			String epno = fileInfo.get("epno");
			String epRomanjiName = fileInfo.get("ep_romanji_name");
			String epKanjiName = fileInfo.get("ep_kanji_name");
			
			this.db.addEntry(aid, eid, gid, fid, animeRomanjiName, episodes, 
					year, animeEngName, animeKanjiName, epEngName, epno, 
					epRomanjiName, epKanjiName, size, ed2k, md5, sha1, crc32, 
					dub, sub, src, audio, video, res, file_type, grp, fileName, 
					drive_name, folder);
			
			size = size / 1000000;
			System.out.println("File has been added to database");
			this.md2.addEntry(animeRomanjiName, episodes, fileName, epno, size, 
					Database.getTimestamp(), folder, true);
			this.mdAvailable.addEntry(animeRomanjiName, episodes, fileName, 
					epno, size, Database.getTimestamp(), folder, true);
		}
		
	}
	
	*/
	public void addFile(File file){
		/*
		 * TODO: Keep refractoring
		 */
		String fileName = file.getName();
		String file_type = fileName.substring(fileName.length() - 3);
		if (!file.isFile() || !FILE_TYPES.contains(file_type)) {
			return;
		} else {
			System.out.println("Checking: " + file.getName());
			TableEntry fileInfo;
			String ed2k = Ed2k.getEd2k(file);
			Integer size = ((int) (long) file.length()) ;
			String folder = file.toPath().getParent().toString();
			String drive_name = file.toPath().getRoot().toString();
			
			AnimeFile dbfile = this.db.findFileHash(size, ed2k);
			System.out.println("ed2k hash: "+ed2k+ ". File size: "+ size.toString());
			if (dbfile == null) {
				System.out.print("File not found in local db. Searching anidb.");
				fileInfo = this.udp.findFileOnAnidb(size, ed2k);
				if (fileInfo == null) {
					System.out.println("File cannot be identified");
					return;
				} else {
					System.out.println("File found on anidb");
					
				}
			} else {
				System.out.println("File found in local db");
				int fid = dbfile.fid;
				AnimeLocalFile local_file = new AnimeLocalFile(fileName, fid, folder, drive_name, "never");
				this.db.addJob(local_file);
				System.out.println("File has been added to database");
				return;
			}
			fileInfo.fileName = fileName;
			fileInfo.folder = folder;
			fileInfo.drive_name = drive_name;
			fileInfo.accessible = true;
			
			this.db.addTableEntry(fileInfo);
			
			size = size / 1000000;
			System.out.println("File has been added to database");
			this.md2.addEntry(fileInfo);
			this.mdAvailable.addEntry(fileInfo);
		}
	}
	
	private UDPConnector start_UDP(){
		Properties prop = new Properties();
		InputStream input = null;
		try {
			input = new FileInputStream("config.properties");
			prop.load(input);
			
			// Load config file
			String anidbAddress = prop.getProperty("anidbAddress");
			int anidbPort = Integer.parseInt(prop.getProperty("anidbPort"));
			int myPort = Integer.parseInt(prop.getProperty("myPort"));
			String username = prop.getProperty("username");
			String password = prop.getProperty("password");
		
			UDPConnector new_connection = new UDPConnector(anidbAddress, anidbPort, myPort,
					username, password);
			return new_connection;
			
			
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
}