package com.ctb.tdc.installer.action;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import com.zerog.ia.api.pub.CustomCodeAction;
import com.zerog.ia.api.pub.InstallException;
import com.zerog.ia.api.pub.InstallerProxy;
import com.zerog.ia.api.pub.UninstallerProxy;

public class ExtractMatchedExpressionAction extends CustomCodeAction {

	/**
	 * Appends the specified value to the source and stores in the result.
	 * <ul>
	 *   <li>$EME_SOURCE$</li>
	 *   <li>$EME_REGEXP$</li>
	 *   <li>$EME_RESULT$</li>
	 * </ul>
	 */
	private void extractMatchedExpression(InstallerProxy installerProxy) {
		String source = installerProxy.substitute("$EME_SOURCE$");
		String regExp = installerProxy.substitute("$EME_REGEXP$");
		String result = "";

		try {
			Pattern p = Pattern.compile(regExp);
			Matcher m = p.matcher(source);
			if( m.matches() ) {
				result = m.group(1);
			}
		} catch( PatternSyntaxException pse) {
			System.err.println("Invalid pattern match exception! " + regExp);
		}
		
		installerProxy.setVariable("$EME_RESULT$", result);
	}
	
	
	public void install(InstallerProxy installerProxy) throws InstallException {
		extractMatchedExpression(installerProxy);
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
