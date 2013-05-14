package com.ctb.tdc.installer.action;

import com.zerog.ia.api.pub.CustomCodeAction;
import com.zerog.ia.api.pub.InstallException;
import com.zerog.ia.api.pub.InstallerProxy;
import com.zerog.ia.api.pub.UninstallerProxy;

/**
 * Custom action for InstallAnywhere to append string values
 * to a supplied variable.
 * 
 * @author Giuseppe_Gennaro
 *
 */
public class AppendToVariableAction extends CustomCodeAction {

	/**
	 * Appends the specified value to the source and stores in the result.
	 * <ul>
	 *   <li>$ATV_SOURCE$</li>
	 *   <li>$ATV_VALUE$</li>
	 *   <li>$ATV_RESULT$</li>
	 * </ul>
	 */
	private void appendValueToSource(InstallerProxy installerProxy) {
		String source = installerProxy.substitute("$ATV_SOURCE$");
		String value  = installerProxy.substitute("$ATV_VALUE$");
		String result = installerProxy.substitute("$ATV_RESULT$");

		result = source + value;
		installerProxy.setVariable("$ATV_RESULT$", result);
	}
	
	
	public void install(InstallerProxy installerProxy) throws InstallException {
		appendValueToSource(installerProxy);
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
