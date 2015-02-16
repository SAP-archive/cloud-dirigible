package com.sap.dirigible.repository.rcp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipImporter {
	
	public static void unzip(String destinationFolder, ZipInputStream zipInput) throws IOException {
		String workspaceFolder = RCPWorkspaceMapper.getMappedName(destinationFolder);
		File directory = new File(workspaceFolder);
        
		if(!directory.exists()) 
			directory.mkdirs();

		byte[] buffer = new byte[2048];
        
		try {
			ZipEntry entry = zipInput.getNextEntry();
            
			while(entry != null){
				String entryName = entry.getName();
				File file = new File(workspaceFolder + File.separator + entryName);
                
				if(entry.isDirectory()) {
					File newDir = new File(file.getCanonicalPath());
					if(!newDir.exists()) {
						boolean success = newDir.mkdirs();
						if(success == false) {
							System.out.println("Problem creating Folder");
						}
					}
                }
				else {
					FileUtils.createFoldersIfNecessary(file.getCanonicalPath());
					FileOutputStream fOutput = new FileOutputStream(file);
					int count = 0;
					while ((count = zipInput.read(buffer)) > 0) {
						fOutput.write(buffer, 0, count);
					}
					fOutput.close();
				}
			zipInput.closeEntry();
				entry = zipInput.getNextEntry();
			}
            
			zipInput.closeEntry();
            
			zipInput.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
