package com.ctb.tdc.installer.action;

import java.io.File;
import java.io.FileOutputStream;

import com.zerog.ia.api.pub.CustomCodeAction;
import com.zerog.ia.api.pub.InstallException;
import com.zerog.ia.api.pub.InstallerProxy;
import com.zerog.ia.api.pub.UninstallerProxy;

/**
 * Custom action for InstallAnywhere to append string values
 * to a supplied variable.
 *
 * @author Tai_Truong
 *
 */
public class CreateOnlineSurveyAction extends CustomCodeAction {

	private void createOnlineSurvey(InstallerProxy installerProxy) {
        String filePath = installerProxy.substitute("$FILE_PATH$");
        String iconPath = installerProxy.substitute("$ICON_PATH$");
        String iconName = installerProxy.substitute("$ICON_NAME$");
         
        String filename = "FCAT Student Comment Form.URL";
        File onlineFile = new File(filePath + "\\" + filename);
        
        try {
            FileOutputStream fos = new FileOutputStream(onlineFile, false);
            String header = "[InternetShortcut]" + "\n";
            String url = "URL=http://oas.ctb.com/survey/fcat/index.html" + "\n";
            String iconIndex = "IconIndex=0" + "\n";
            String iconFile = "IconFile=" + iconPath + "\\" + iconName + "\n";
            
            fos.write(header.getBytes(), 0, header.length());
            fos.write(url.getBytes(), 0, url.length());
            fos.write(iconIndex.getBytes(), 0, iconIndex.length());
            fos.write(iconFile.getBytes(), 0, iconFile.length());
            
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
	} 


	public void install(InstallerProxy installerProxy) throws InstallException {
		createOnlineSurvey(installerProxy);
	}

	public void uninstall(UninstallerProxy installerProxy) throws InstallException {

	}

	public String getInstallStatusMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getUninstallStatusMessage() {
		// TODO Auto-generated method stub
		return null;
	}

}
