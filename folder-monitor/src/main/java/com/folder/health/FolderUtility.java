package com.folder.health;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class FolderUtility {
	
	public static double getFolderSize() throws IOException {
	 
	    Path folder = Paths.get("secured");
	    long size = Files.walk(folder)
	      .filter(p -> p.toFile().isFile())
	      .mapToLong(p -> p.toFile().length())
	      .sum();
	   return getReadableSize(size);
	 
	}
	
	public  static double getReadableSize(long size) {
	    if(size <= 0) return 0;
	    final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
	    int digitGroups = 2;
	    return (size/Math.pow(1024, digitGroups));
	}
	
}
