package com.ctb.tdc.installer.action;

import java.io.*;

import com.zerog.ia.api.pub.CustomCodeAction;
import com.zerog.ia.api.pub.InstallException;
import com.zerog.ia.api.pub.InstallerProxy;
import com.zerog.ia.api.pub.UninstallerProxy;


public class CheckFileExistsAction extends CustomCodeAction {

	private void loadPath(InstallerProxy installerProxy) {
        String fileName = installerProxy.substitute("$FILE_NAME$");
        File theFile = new File(fileName);
        Boolean isExists = new Boolean( theFile.exists() );
        String result = isExists.toString();
         
		installerProxy.setVariable("$FLASH_EXISTS$", result);		        
	} 


	public void install(InstallerProxy installerProxy) throws InstallException {
		loadPath(installerProxy);
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
