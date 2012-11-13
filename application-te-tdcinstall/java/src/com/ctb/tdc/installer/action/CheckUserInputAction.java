package com.ctb.tdc.installer.action;

import java.io.*;

import com.zerog.ia.api.pub.CustomCodeAction;
import com.zerog.ia.api.pub.InstallException;
import com.zerog.ia.api.pub.InstallerProxy;
import com.zerog.ia.api.pub.UninstallerProxy;


public class CheckUserInputAction extends CustomCodeAction {

	private void verifyString(InstallerProxy installerProxy) {
        String inputStr0 = installerProxy.substitute("$USER_INPUT_RESULT_0$");
        String inputStr1 = installerProxy.substitute("$USER_INPUT_RESULT_1$");
        inputStr0 = inputStr0.trim();
        inputStr1 = inputStr1.trim();
        
        boolean result0 = (inputStr0.length() == 0);
        boolean result1 = (inputStr1.length() == 0);
        
        Boolean result = new Boolean( result0 && result1 );
        
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
