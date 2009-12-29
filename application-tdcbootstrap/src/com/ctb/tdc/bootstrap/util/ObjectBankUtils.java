package com.ctb.tdc.bootstrap.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import com.ctb.tdc.bootstrap.exception.BootstrapException;

/**
 * Solution for Deferred Defect No: 50573 
 * Utility class checks the contents of ObjectBank folder whether it is 
 * older than the given specific day or not. If so, then delete those files
 */
public class ObjectBankUtils {



	public static final String TDC_HOME = "tdc.home";
	private String aging = "60";
	public static final String CONTENT_FOLDER = "/data/objectbank/";
	public static final String CONTENT_FOLDER_PATH = System.getProperty(TDC_HOME)+ CONTENT_FOLDER;


	/**
	 * Call the deletFiles method to get the files
	 * which is older than 60 days and then delete
	 * those files
	 */
	public void deleteContents () {

		try {

			deleteFiles();

		} catch (BootstrapException e ) {

			ConsoleUtils.messageErr("An error has occured within " + this.getClass().getName(), e);

		} catch (Exception e ) {

			ConsoleUtils.messageErr("An error has occured within " + this.getClass().getName(), e);
		}

	}


	/**
	 * Deletes the older files which is older than aging value
	 * @throws BootstrapException
	 */

	private void deleteFiles() throws BootstrapException {

		ArrayList<File> deleteList = new ArrayList<File>();

		Date currentDate = new Date();
		long currentTime = currentDate.getTime();


		String folderPath=CONTENT_FOLDER_PATH;

		File  contentpath = new File(folderPath);

		if ( !contentpath.isDirectory() ) {
			throw new BootstrapException(
					ResourceBundleUtils.getString(
					"bootstrap.main.error.tdcHomeNotDirectory"));
		}


		File[] listofFiles = contentpath.listFiles();


		for(File file : listofFiles) {

			if ( file.getName().endsWith("eam") || file.getName().endsWith("ecp")) {

				long filecreateTime = file.lastModified();
				long diff = currentTime - filecreateTime;
				long diffDays = diff / (24 * 60 * 60 * 1000);

				if ( (( Long.valueOf(aging) ).longValue()) < diffDays ) {

					deleteList.add(file);
				}
			}
		}



		for(File file : deleteList) {

			file.delete();

		}




	}



}
