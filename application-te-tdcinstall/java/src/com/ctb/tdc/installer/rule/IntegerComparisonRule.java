package com.ctb.tdc.installer.rule;

import com.zerog.ia.api.pub.CustomCodeRule;

/**
 * Custom Code Rule for InstallAnywhere to compare two integer numbers
 * based on one of supported operators.  InstallAnywhere variables expected
 * to be set prior to execution:
 * <ul>
 *   <li>$ICR_OPERAND1$</li>
 *   <li>$ICR_OPERAND2$</li>
 *   <li>$ICR_OPERATOR$</li>
 * </ul>
 * 
 * @author Giuseppe_Gennaro
 *
 */
public class IntegerComparisonRule extends CustomCodeRule {

	private static final String LESS_THAN = "<";
	private static final String LESS_THAN_OR_EQUAL = "<=";
	private static final String EQUAL = "=";
	private static final String GREATER_THAN_OR_EQUAL = ">=";
	private static final String GREATER_THAN = ">";

	/**
	 * Compares the float values of $FCR_OPERAND1$ with $FCR_OPERAND2$ based
	 * on one of the following values for $FCR_OPERATOR$:
	 * <ul>
	 *   <li>&lt;</li>
	 *   <li>&lt;=</li>
	 *   <li>=</li>
	 *   <li>&gt;=</li>
	 *   <li>&gt;</li>
	 * </ul>
	 */
	public boolean evaluateRule() {
		boolean results = false;
		
		String strOperand1 = IntegerComparisonRule.ruleProxy.substitute("$ICR_OPERAND1$");
		String strOperand2 = IntegerComparisonRule.ruleProxy.substitute("$ICR_OPERAND2$");
		String strOperator = IntegerComparisonRule.ruleProxy.substitute("$ICR_OPERATOR$");

        if( (strOperand1 == null) && (strOperand2 == null) && (strOperator == null) ) {
            System.err.println("Integer Comparison Rule: ignored!");            
        } else if( (strOperand1 == null) || strOperand1.equals("") ) {
            System.err.println("$ICR_OPERAND1$ was not specified!");
        } else if( (strOperand2 == null) || strOperand2.equals("") ) {
            System.err.println("$ICR_OPERAND2$ was not specified!");
        } else if( (strOperator == null) || strOperator.equals("") ) {
            System.err.println("$ICR_OPERATOR$ was not specified!");
        } else {
            strOperand1 = strOperand1.trim();
            strOperand2 = strOperand2.trim();			
            System.err.println("Integer Comparison Rule: " + strOperand1 + " " + strOperator + " " + strOperand2);
			
            try {                
                int operand1 = Integer.parseInt(strOperand1);
				int operand2 = Integer.parseInt(strOperand2);

				if( strOperator.equalsIgnoreCase(LESS_THAN) ) {
					results = (operand1 < operand2);					
					System.out.println("Integer Comparison Rule: " + strOperand1 + " < " + strOperand2 + " = " + results);
				
				} else if( strOperator.equalsIgnoreCase(LESS_THAN_OR_EQUAL) ) {
					results = (operand1 <= operand2);
					System.out.println("Integer Comparison Rule: " + strOperand1 + " <= " + strOperand2 + " = " + results);
				
				} else if( strOperator.equalsIgnoreCase(EQUAL) ) {
					results = (operand1 == operand2);
					System.out.println("Integer Comparison Rule: " + strOperand1 + " == " + strOperand2 + " = " + results);
				
				} else if( strOperator.equalsIgnoreCase(GREATER_THAN_OR_EQUAL) ) {
					results = (operand1 >= operand2);
					System.out.println("Integer Comparison Rule: " + strOperand1 + " >= " + strOperand2 + " = " + results);

				} else if( strOperator.equalsIgnoreCase(GREATER_THAN) ) {
					results = (operand1 > operand2);
					System.out.println("Integer Comparison Rule: " + strOperand1 + " > " + strOperand2 + " = " + results);

				} else {
					System.err.println("Operator (" + strOperator + ") did not match supported operators!");
				}
					
				
			} catch( NumberFormatException nfe ) {
				System.err.println("NumberFormatException thrown with either operand1 (" + strOperand1 + ") or operand2 (" + strOperand2 + ").");
				nfe.printStackTrace(System.err);
			}
			
		}
		
		return results;
	}

}
