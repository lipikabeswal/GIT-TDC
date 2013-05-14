package com.ctb.tdc.installer.rule;

import com.zerog.ia.api.pub.CustomCodeRule;

/**
 * Custom rule for InstallAnywhere for testing purposes only.
 * 
 * @author Giuseppe_Gennaro
 *
 */
public class AlwaysPassRule extends CustomCodeRule {

	public boolean evaluateRule() {

		System.out.println(this.getClass().getName() + " testing");
		
		return true;
	}

}
