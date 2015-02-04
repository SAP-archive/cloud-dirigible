package com.sap.dirigible.repository.rcp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;

import com.sap.dirigible.repository.api.IRepositoryPaths;

public class FileUtils {

	public static void saveFile(String workspacePath, byte[] content)
			throws FileNotFoundException, IOException {
		Path path = FileSystems.getDefault().getPath(workspacePath);
		Files.write(path, content);
	}
	
	public static byte[] loadFile(String workspacePath)
			throws FileNotFoundException, IOException {
		Path path = FileSystems.getDefault().getPath(workspacePath);
		return Files.readAllBytes(path);
	}
	
	public static void moveFile(String workspacePathOld, String workspacePathNew)
			throws FileNotFoundException, IOException {
		Path pathOld = FileSystems.getDefault().getPath(workspacePathOld);
		Path pathNew = FileSystems.getDefault().getPath(workspacePathNew);
		Files.move(pathOld, pathNew);
	}

	public static void removeFile(String workspacePath)
			throws FileNotFoundException, IOException {
		Path path = FileSystems.getDefault().getPath(workspacePath);
		Files.delete(path);
	}
	
	public static boolean createFolder(String workspacePath) {
		File folder = new File(workspacePath);
		if (!folder.exists()) {
			return folder.mkdir();
		}
		return true;
	}
	
	public static boolean createFile(String workspacePath) throws IOException {
		File file = new File(workspacePath);
		if (!file.exists()) {
			return file.createNewFile();
		}
		return true;
	}

	public static String getExtension(String workspacePath) throws IOException {
		File f = new File(workspacePath);
		String ext = null;
	    String s = f.getName();
	    int i = s.lastIndexOf('.');

	    if (i > 0 &&  i < s.length() - 1) {
	        ext = s.substring(i+1).toLowerCase();
	    }
		return ext;
	}

	public static String getOwner(String workspacePath) throws IOException {
		String convertedPath = convertToWorkspacePath(workspacePath);
		Path path = FileSystems.getDefault().getPath(convertedPath);
		return Files.getOwner(path).getName();
	}
	
	public static Date getModifiedAt(String workspacePath) throws IOException {
		String convertedPath = convertToWorkspacePath(workspacePath);
		Path path = FileSystems.getDefault().getPath(convertedPath);
		return new Date(Files.getLastModifiedTime(path).toMillis());
	}

	private static String convertToWorkspacePath(String path) {
		String workspacePath = null;
		if (path.startsWith(IRepositoryPaths.SEPARATOR)) {
			workspacePath = path.substring(IRepositoryPaths.SEPARATOR.length());
		} else {
			workspacePath = path;
		}
		workspacePath = workspacePath.replace(IRepositoryPaths.SEPARATOR, File.separator);
		return workspacePath;
	}
}