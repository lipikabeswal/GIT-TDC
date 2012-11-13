package com.ctb.tdc.installer.action;

import java.io.*;

import com.zerog.ia.api.pub.CustomCodeAction;
import com.zerog.ia.api.pub.InstallException;
import com.zerog.ia.api.pub.InstallerProxy;
import com.zerog.ia.api.pub.UninstallerProxy;


public class CheckDashAction extends CustomCodeAction {

	private void verifyString(InstallerProxy installerProxy) {
        String inputStr = installerProxy.substitute("$ATV_SOURCE$");

		int index = inputStr.indexOf('-');		
        Boolean result = new Boolean( index >= 0 );
        
		installerProxy.setVariable("$EME_RESULT$", result.toString());		        
	} 


	public void install(InstallerProxy installerProxy) throws InstallException {
		verifyString(installerProxy);
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
