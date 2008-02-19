/************************************************************************************
@Copyright (C) 2006 Hurix Systems Pvt. Ltd. All Rights Reserved.
@Author						: Deepak Kumar Sahu
@Client						: CTB
@Project					: TerraNova
@Date						: 28/12/07
@Version					: 1.0
@Class						: TN_XmlLoad
@Description				: 
This class is used for Loading the XML.
@Extends					: 
@Used In					: 
@Change Log					: 
/******************************************************************

/******************************************************************
@Method						: 
@Description				: 
Calling the respective Fuction
@Input						: 
@Output						: 
/****************************************************************************************/


class com.TN_xmlLoad {
	private var xmlObj:XML;
	
	public function TN_xmlLoad(xmlURL:String,calleeFunction){
		// Create a new XML object.
		var xmlObj:XML = new XML()
		// Set the ignoreWhite property to true (default value is false).
		xmlObj.ignoreWhite = true;
		// After loading is complete, call the respective function.
		xmlObj.onLoad = function(success){
			if(success)
			{
				calleeFunction(this)
			}
		}	
	
		// Load the XML into the flooring object.
		xmlObj.load(xmlURL)
	}
}