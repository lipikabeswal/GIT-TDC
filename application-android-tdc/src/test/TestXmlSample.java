package test;

public class TestXmlSample {
	static String xmlData;
	public static String getXmlFileDownload(){
		
		xmlData="<?xml version=\"1.0\" encoding=\"UTF-8\" ?>"
				+"<content_tracker><created_date>02-Dec-2011 03:46:26 GMT</created_date>"
				+"<tracker sequence_number = \"1\"  name = \"9503702$89B10848A7F03CC7FE6C6D7BB5C0E5BA.part.1\"  next = \"2\" >"
				+"</tracker><tracker sequence_number = \"2\"  name = \"9503702$89B10848A7F03CC7FE6C6D7BB5C0E5BA.part.2\"  next =\"3\" >"
				+"</tracker><tracker sequence_number = \"3\"  name = \"9503702$89B10848A7F03CC7FE6C6D7BB5C0E5BA.part.3\"  next = \"4\" >"
				+"</tracker><tracker sequence_number = \"4\"  name = \"9503702$89B10848A7F03CC7FE6C6D7BB5C0E5BA.part.4\"  next = \"5\" >"
				+"</tracker><tracker sequence_number = \"5\"  name = \"9503702$89B10848A7F03CC7FE6C6D7BB5C0E5BA.part.5\"  next = \"6\" >"
				+"</tracker><tracker sequence_number = \"6\"  name = \"9503702$89B10848A7F03CC7FE6C6D7BB5C0E5BA.part.6\"  next = \"7\" >"
				+"</tracker><tracker sequence_number = \"7\"  name = \"9503702$89B10848A7F03CC7FE6C6D7BB5C0E5BA.part.7\"  next = \"8\" >"
				+"</tracker><tracker sequence_number = \"8\"  name = \"9503702$89B10848A7F03CC7FE6C6D7BB5C0E5BA.part.8\"  next = \"9\" >"
				+"</tracker><tracker sequence_number = \"9\"  name = \"9503702$89B10848A7F03CC7FE6C6D7BB5C0E5BA.part.9\"  next = \"10\" >"
				+"</tracker><tracker sequence_number = \"10\"  name = \"9503702$89B10848A7F03CC7FE6C6D7BB5C0E5BA.part.10\"  next = \"11\" >"
				+"</tracker><tracker sequence_number = \"11\"  name = \"9503702$89B10848A7F03CC7FE6C6D7BB5C0E5BA.part.11\"  next = \"12\" >"
				+"</tracker><tracker sequence_number = \"12\"  name = \"9503702$89B10848A7F03CC7FE6C6D7BB5C0E5BA.part.12\"  next = \"13\" >"
				+"</tracker><tracker sequence_number = \"13\"  name = \"9503702$89B10848A7F03CC7FE6C6D7BB5C0E5BA.part.13\"  next = \"NULL\" >"
				+"</tracker></content_tracker>";
		return xmlData;
		
	}
	
	public static String getDownloadItem(){
		xmlData="<?xml version=\"1.0\" encoding=\"UTF-8\" ?>"
	               +"<adssvc_request method=\"downloadItem\" sdsid=\"string\" token=\"string\" xmlns=\"\">"
			       +"<download_item itemid=\"00000-I\" hash=\"FEBA1F02D41BFF3D2624E0E218B9015A\" key=\"n7673nBJ2n27bB4oAfme7Ugl5VV42g8\" />"
	               +"</adssvc_request>";
		return xmlData;
	}
	public static String getSubtest(){
		xmlData="<?xml version=\"1.0\" encoding=\"UTF-8\" ?>"
		+"<adssvc_request method=\"getSubtest\" sdsid=\"string\" token=\"string\" xmlns=\"\">"
		 +"<get_subtest subtestid=\"\" hash=\"\" key=\"\" />" 
		  +"</adssvc_request>";
	
	return xmlData;
	}
	public static String getTTSData(){
	xmlData = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>"
			+ "<speech method=\"readPostDataRequest\" text=\"Hi every body,happy pujo\" speedValue=\"-3\">"
			+ "</speech>";
	return xmlData;
	}

}
