package com.ctb.tdc.web.utils;

public class CATEngineProxy {

	private static int itemnum;
	private static double theta;
	private static double SEM;
	private static int testLength;
	
	public static native int setup_cat(String contentArea, char level);
	public static native int getTestLength();
	public static native int adapt_n_item(int n);
	public static native void set_rwo(int n);
	public static native double score();
	public static native double getSEM(double theta);
	public static native void setoff_cat();
	
	public static void main(String [] args){
		try {
			initCAT("RD", 'M');
			String next = getNextItem(null);
			while(next != null){
				next = getNextItem(new Integer(1));
				//System.out.println(theta);
			}
			getAbilityScore();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void initCAT(String contentArea, char level) {
		System.load("C:/Program Files/CTB/Online Assessment/CATABE.dll");
		itemnum = 0;
		setup_cat(contentArea, level);
		testLength = getTestLength();
	}
	
	public static String getNextItem(Integer currentItemRawScore) {
		if(itemnum < testLength) {
			if(currentItemRawScore != null) {
				set_rwo(currentItemRawScore.intValue());
				theta = score();
			}
			String nextitem = String.valueOf(adapt_n_item(itemnum));
			itemnum++;
			return nextitem;
		} else {
			return null;
		}
	}
	
	public static double getAbilityScore() {
		SEM = getSEM(theta); 
		System.out.println("SEM: " + SEM);
		System.out.println("Ability: " + theta);
		return theta;
	}
	
	public static void deInitCAT() {
		setoff_cat();
	}
}
