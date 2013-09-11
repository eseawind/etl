package org.intercom.test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import au.com.bytecode.opencsv.CSVReader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:Test-context.xml")
public class TestApp {

	// @Autowired
	// private ApplicationContext applicationContext;

	String BIG_CSV_FILE = "c:\\UTRAN-SNAP20111211052507.txt";
	String SMALL_CSV_FILE = "c:\\USPresident.csv";
	
//	@Test
//	public void testCSVParser() throws IOException {
//		InputStreamReader sreamReader = new InputStreamReader(
//				new FileInputStream(SMALL_CSV_FILE), "UTF-8");
//		BufferedReader bufferedReader = new BufferedReader(sreamReader);
//		CSVReader reader = new CSVReader(bufferedReader, '/');
//
//		String[] line;
//		for (int row = 0; (line = reader.readNext()) != null; row++) {
//		for (int col = 0; col < line.length; col++) {
//			System.out.print(line[col] + " | ");
//		}
//			System.out.println();
//		}
//		reader.close();
//	}
//
//	@Test
//	public void getColumnCount() throws IOException {
//		InputStreamReader sreamReader = new InputStreamReader(
//				new FileInputStream(SMALL_CSV_FILE), "UTF-8");
//		BufferedReader bufferedReader = new BufferedReader(sreamReader);
//		CSVReader reader = new CSVReader(bufferedReader, ',');
//		String[] firstLine = reader.readNext();
//		for (int col = 0; col < firstLine.length; col++) {
//			System.out.println(firstLine[col] + " | ");
//		}
//		//System.out.println(firstLine.length);
//		reader.close();
//	}
//	
	@Test
	public void testReadFTPFile () throws SocketException, IOException {
		BufferedReader reader = null;
		String firstLine = null;
		FTPClient client = new FTPClient();
		try {
			client.connect("127.0.0.1", 21);
			client.login("admin", "admin");
		    FTPFile[] ftpFiles = client.listFiles();
		    System.out.println("number of files : "+ ftpFiles.length); 
			for (FTPFile ftpFile : ftpFiles) {
				if (ftpFile.getType() == FTPFile.FILE_TYPE) {
					System.out.println("FTPFile: " + ftpFile.getName() + "; "
							+ ftpFile.getSize());
				}
			}
		    InputStream stream = client.retrieveFileStream("contract.txt");
		    reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
		    firstLine = reader.readLine();
		    System.out.println(firstLine);
		} finally {
		    if (reader != null) try { reader.close(); } catch (IOException logOrIgnore) {}
			client.disconnect();
		}

	}
}
