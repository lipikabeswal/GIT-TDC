package com.ctb.tdc.installer.action;

import java.io.*;

import com.zerog.ia.api.pub.CustomCodeAction;
import com.zerog.ia.api.pub.InstallException;
import com.zerog.ia.api.pub.InstallerProxy;
import com.zerog.ia.api.pub.UninstallerProxy;


public class ExtractValueAction extends CustomCodeAction {

	private void extractValue(InstallerProxy installerProxy) {
        String source = installerProxy.substitute("$EME_SOURCE$");
		String regExp = installerProxy.substitute("$EME_REGEXP$");
		String result = "";

		int index = source.indexOf(regExp);		
		StringBuffer buff = new StringBuffer();
		
		if (index >= 0) {
			for (int i=index ; i<source.length() ; i++) {
				char ch = source.charAt(i);
				if (ch >= '0' && ch <= '9') {
					buff.append(ch);
				}
				if (ch != 9 && ch != 11) {
					if (ch < 32 || ch == '.') {
						break;
					}	
				}
			}
			result = buff.toString();
		}
        
		installerProxy.setVariable("$EME_RESULT$", result);		
	} 


	public void install(InstallerProxy installerProxy) throws InstallException {
		extractValue(installerProxy);
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
