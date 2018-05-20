package com.folder.health;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.apache.commons.io.filefilter.FileFileFilter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class FolderManager {
	
	@Scheduled(fixedRate = 2*60*1000)
	void readFromTemp(){
		System.out.println("Scheduler triggered!!");

		File[] fileList = orderFilesByDate();
		for (File file : fileList) {
				InputStream inStream = null;
				OutputStream outStream = null;

				try {
					File afile = new File("temp" + "/" + file.getName());
					File bfile = new File("secured" + "/" + file.getName());

					inStream = new FileInputStream(afile);
					outStream = new FileOutputStream(bfile);

					byte[] buffer = new byte[1024];
					int length;
					while ((length = inStream.read(buffer)) > 0) {
						outStream.write(buffer, 0, length);
					}

					inStream.close();
					outStream.close();
					System.out.println("File- "+afile.getName()+" moved to secured");
					afile.delete();

				} catch (IOException e) {
					e.printStackTrace();
				}
			} 
		}
	
	
	@Scheduled(fixedRate = 3*60*1000)
	void readSummaryFromSecured() throws IOException{
		System.out.println("Current size of secured folder - "+FolderUtility.getFolderSize());
		System.out.println("--Below are the archived files--");
		Path folder = Paths.get("archived");
		Files.walk(folder)
		      .filter(p -> p.toFile().isFile()).forEach(a-> System.out.println(a.getFileName()));
		
		
	}
	
	private File[] orderFilesByDate() {
		File directory = new File("temp");
		File[] files = directory.listFiles((FileFilter) FileFileFilter.FILE);
		Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_COMPARATOR);
		return files;
	}
	
}