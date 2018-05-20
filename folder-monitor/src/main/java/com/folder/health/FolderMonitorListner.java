package com.folder.health;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.apache.commons.io.filefilter.FileFileFilter;
import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationObserver;

public class FolderMonitorListner implements FileAlterationListener {

	@Override
	public void onDirectoryChange(File arg0) {
	}

	@Override
	public void onDirectoryCreate(File arg0) {
	}

	@Override
	public void onDirectoryDelete(File arg0) {
	}

	@Override
	public void onFileChange(File file) {
		try {
			if (FolderUtility.getFolderSize() > 10) {
				System.out.println("Folder Capacity exceeded!!");
				moveToArchive();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onFileCreate(File file) {
		try {
			if (checkIfValidFile(file)) {
				if (FolderUtility.getFolderSize() > 10) {
					moveToArchive();
				}
			} else {

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onFileDelete(File file) {
	}

	@Override
	public void onStart(FileAlterationObserver obs) {
		System.out.println("The FileListener has started on "
	               + obs.getDirectory().getAbsolutePath());
	}

	@Override
	public void onStop(FileAlterationObserver obs) {
		System.out.println("The FileListener has stopped on "
				               + obs.getDirectory().getAbsolutePath());

	}

	private boolean checkIfValidFile(File file) throws IOException {
		boolean isValid = true;
		String extension = FilenameUtils.getExtension(file.getCanonicalPath());
		switch (extension) {
		case "exe":
			file.delete();
			isValid = false;
			break;
		case "sh":
			isValid = false;
			file.delete();
			break;
		}
		return isValid;
	}

	private File[] orderFilesByDate() {
		File directory = new File("secured");
		File[] files = directory.listFiles((FileFilter) FileFileFilter.FILE);
		Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_COMPARATOR);
		return files;
	}

	private void moveToArchive() throws IOException {
		File[] fileList = orderFilesByDate();
		for (File file : fileList) {
			if (FolderUtility.getFolderSize() > 10) {
				System.out.println("Folder Secured Size : "+ FolderUtility.getFolderSize());
				InputStream inStream = null;
				OutputStream outStream = null;

				try {
					File afile = new File("secured" + "/" + file.getName());
					File bfile = new File("archived" + "/" + file.getName());

					inStream = new FileInputStream(afile);
					outStream = new FileOutputStream(bfile);

					byte[] buffer = new byte[1024];
					int length;
					while ((length = inStream.read(buffer)) > 0) {
						outStream.write(buffer, 0, length);
					}

					inStream.close();
					outStream.close();
					System.out.println("File: " + file.getName() + " moved to archived folder");
					// delete the original file
					afile.delete();

				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				break;
			}
		}
	}

}
