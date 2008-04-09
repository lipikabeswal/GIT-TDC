﻿/************************************************************************************
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
	//
	//private var 
	public function TN_xmlLoad(xmlURL:String,calleeFunction){
		// Create a new XML object.
		var xmlObj:XML = new XML()
		// Set the ignoreWhite property to true (default value is false).
		xmlObj.ignoreWhite = true;
		xmlObj.parseXML(String('<TerraNova_content><!-- Answering a Question – Choosing an Option --><Screen id="Screen_2" UserGuide="type22,type27,type28"><Block id="questionInst"><![CDATA[]]></Block><Block id="questionData"><![CDATA[]]></Block><Block id="question_no"><![CDATA[Question 1]]></Block><Block id="question"><![CDATA[What is your favorite color?]]></Block><Block id="ansChoice_1"><![CDATA[Purple]]></Block><Block id="ansChoice_2"><![CDATA[Yellow]]></Block><Block id="ansChoice_3"><![CDATA[Green]]></Block><Block id="ansChoice_4"><![CDATA[Blue]]></Block></Screen><!-- Answering a Question – Moving to the Next Question --><Screen id="Screen_3" UserGuide="type22,type27,type28,type1,type2"><Block id="questionInst"><![CDATA[]]></Block><Block id="question_no"><![CDATA[Question 1]]></Block><Block id="questionData"><![CDATA[]]></Block><Block id="question_no"><![CDATA[Question 1]]></Block><Block id="question"><![CDATA[2. What is your favorite color?]]></Block><Block id="ansChoice_1"><![CDATA[Purple]]></Block><Block id="ansChoice_2"><![CDATA[Yellow]]></Block><Block id="ansChoice_3"><![CDATA[Green]]></Block><Block id="ansChoice_4"><![CDATA[Blue]]></Block></Screen><!-- Select an Answer And GoBack --><Screen id="Screen_4" UserGuide="type22,type27,type28,type17,type3"><Block id="questionInst"><![CDATA[]]></Block><Block id="question_no"><![CDATA[Question 2]]></Block><Block id="question"><![CDATA[What is a good name for a puppy?]]></Block><Block id="ansChoice_1"><![CDATA[Sparky]]></Block><Block id="ansChoice_2"><![CDATA[Bones]]></Block><Block id="ansChoice_3"><![CDATA[Spot]]></Block><Block id="ansChoice_4"><![CDATA[Rex]]></Block></Screen><!-- Answering a Question – Choosing a Different Answer --><Screen id="Screen_5" UserGuide="type22,type27,type28"><Block id="questionInst"><![CDATA[]]></Block><Block id="questionData"><![CDATA[]]></Block><Block id="question_no"><![CDATA[Question 1]]></Block><Block id="question"><![CDATA[What is your favorite color?]]></Block><Block id="ansChoice_1"><![CDATA[Purple]]></Block><Block id="ansChoice_2"><![CDATA[Yellow]]></Block><Block id="ansChoice_3"><![CDATA[Green]]></Block><Block id="ansChoice_4"><![CDATA[Blue]]></Block></Screen><!-- Answering a Question – Erasing Your Answer Choice --><Screen id="Screen_6" UserGuide="type22,type27,type28,type2,type17"><Block id="questionInst"><![CDATA[]]></Block><Block id="question_no"><![CDATA[Question 2]]></Block><Block id="question"><![CDATA[What is a good name for a puppy?]]></Block><Block id="ansChoice_1"><![CDATA[Sparky]]></Block><Block id="ansChoice_2"><![CDATA[Bones]]></Block><Block id="ansChoice_3"><![CDATA[Spot]]></Block><Block id="ansChoice_4"><![CDATA[Rex]]></Block></Screen><!-- Answering a Question – Pausing a Test --><Screen id="Screen_7" UserGuide="type22,type27,type28,type3,type17"><Block id="questionInst"><![CDATA[]]></Block><Block id="question_no"><![CDATA[Question 3]]></Block><Block id="questionData"><![CDATA[The <u>submarine</u> left the dock.]]></Block><Block id="question"><![CDATA[What is the meaning of the prefix in the word <u>submarine</u>?]]></Block><Block id="ansChoice_1"><![CDATA[Over]]></Block><Block id="ansChoice_2"><![CDATA[Under]]></Block><Block id="ansChoice_3"><![CDATA[Through]]></Block><Block id="ansChoice_4"><![CDATA[Around]]></Block></Screen><!-- Answering a Question – Resuming a Test --><Screen id="Screen_8" UserGuide="type22,type28,type3,type17"><Block id=""><![CDATA[]]></Block></Screen><!-- Navigating –  Forward and Fast farward navigation button--><Screen id="Screen_9" UserGuide="type22,type27,type28,type17,type2"><Block id="questionInst"><![CDATA[]]></Block><Block id="question_no"><![CDATA[Question 3]]></Block><Block id="questionData"><![CDATA[The <u>submarine</u> left the dock.]]></Block><Block id="question"><![CDATA[What is the meaning of the prefix in the word <u>submarine</u>?]]></Block><Block id="ansChoice_1"><![CDATA[Over]]></Block><Block id="ansChoice_2"><![CDATA[Under]]></Block><Block id="ansChoice_3"><![CDATA[Through]]></Block><Block id="ansChoice_4"><![CDATA[Around]]></Block></Screen><!-- Navigating –  back  and Fast backward navigation button--><Screen id="Screen_10" UserGuide="type22,type27,type28,type17,type2"><Block id="questionInst"><![CDATA[]]></Block><Block id="question_no"><![CDATA[Question 3]]></Block><Block id="questionData"><![CDATA[The <u>submarine</u> left the dock.]]></Block><Block id="question"><![CDATA[What is the meaning of the prefix in the word <u>submarine</u>?]]></Block><Block id="ansChoice_1"><![CDATA[Over]]></Block><Block id="ansChoice_2"><![CDATA[Under]]></Block><Block id="ansChoice_3"><![CDATA[Through]]></Block><Block id="ansChoice_4"><![CDATA[Around]]></Block></Screen><!-- Navigating – You Are Here--><Screen id="Screen_11" UserGuide=""><Block id="questionInst"><![CDATA[]]></Block><Block id="question_no"><![CDATA[Question 3]]></Block><Block id="questionData"><![CDATA[The <u>submarine</u> left the dock.]]></Block><Block id="question"><![CDATA[What is the meaning of the prefix in the word <u>submarine</u>?]]></Block><Block id="ansChoice_1"><![CDATA[Over]]></Block><Block id="ansChoice_2"><![CDATA[Under]]></Block><Block id="ansChoice_3"><![CDATA[Through]]></Block><Block id="ansChoice_4"><![CDATA[Around]]></Block></Screen><!-- Navigating – Displaying the Theme Page on the Navigation Bar --><Screen id="Screen_12" UserGuide="type16,type27,type28,type3,type17"><Block id="questionInst"><![CDATA[]]></Block><Block id="question_no"><![CDATA[Question 3]]></Block><Block id="questionData"><![CDATA[The <u>submarine</u> left the dock.]]></Block><Block id="question"><![CDATA[What is the meaning of the prefix in the word <u>submarine</u>?]]></Block><Block id="ansChoice_1"><![CDATA[Over]]></Block><Block id="ansChoice_2"><![CDATA[Under]]></Block><Block id="ansChoice_3"><![CDATA[Through]]></Block><Block id="ansChoice_4"><![CDATA[Around]]></Block></Screen><!-- Navigating – Displaying the Theme Page --><Screen id="Screen_13" UserGuide="type16,type27,type25"><Block id="question_no"><![CDATA[]]></Block></Screen><!-- Features – Using the Glossary --><Screen id="Screen_29" UserGuide="type22,type27,type28"><Block id="questionInst"><![CDATA[Read the story "Snow" before answering Numbers 1 through 4.]]></Block><Block id="question_no"><![CDATA[Question 4]]></Block><Block id="question"><![CDATA[What does the author mean by this sentence from the essay?]]></Block><Block id="ansChoice_1"><![CDATA[explains how volcanoes are made]]></Block><Block id="ansChoice_2"><![CDATA[assures a loved one that he is safe and well]]></Block><Block id="ansChoice_3"><![CDATA[gives an eyewitness account of a volcanic eruption]]></Block><Block id="ansChoice_4"><![CDATA[describes how people tend to react to disasters]]></Block></Screen><!-- Features – Using the Scrollbar --><Screen id="Screen_14" UserGuide="type22,type27,type28,type3,type17"><Block id="questionInst"><![CDATA[Read the story "Snow" before answering Numbers 1 through 4.]]></Block><Block id="question_no"><![CDATA[Question 4]]></Block><Block id="question"><![CDATA[What does the author mean by this sentence from the essay?]]></Block><Block id="ansChoice_1"><![CDATA[explains how volcanoes are made]]></Block><Block id="ansChoice_2"><![CDATA[assures a loved one that he is safe and well]]></Block><Block id="ansChoice_3"><![CDATA[gives an eyewitness account of a volcanic eruption]]></Block><Block id="ansChoice_4"><![CDATA[describes how people tend to react to disasters]]></Block></Screen><!--Features – Answer Choice Eliminator --><Screen id="Screen_15" UserGuide="type16,type27,type25"><Block id="questionInst"><![CDATA[Read the story "Snow" before answering Numbers 1 through 4.]]></Block><Block id="question_no"><![CDATA[Question 4]]></Block><Block id="question"><![CDATA[What does the author mean by this sentence from the essay?]]></Block><Block id="ansChoice_1"><![CDATA[explains how volcanoes are made]]></Block><Block id="ansChoice_2"><![CDATA[assures a loved one that he is safe and well]]></Block><Block id="ansChoice_3"><![CDATA[gives an eyewitness account of a volcanic eruption]]></Block><Block id="ansChoice_4"><![CDATA[describes how people tend to react to disasters]]></Block></Screen><!-- Features – Highlighter  --><Screen id="Screen_16" UserGuide="type16,type27,type25"><Block id="questionInst"><![CDATA[]]></Block><Block id="question_no"><![CDATA[Question 5]]></Block><Block id="questionData"><![CDATA[Read the item and choose the correct answer.]]></Block><Block id="question"><![CDATA[35 + 21 =]]></Block><Block id="ansChoice_1"><![CDATA[76]]></Block><Block id="ansChoice_2"><![CDATA[57]]></Block><Block id="ansChoice_3"><![CDATA[14]]></Block><Block id="ansChoice_4"><![CDATA[56]]></Block></Screen><!-- Features –  Eraser --><Screen id="Screen_17" UserGuide="type16,type27,type25"><Block id="questionInst"><![CDATA[]]></Block><Block id="question_no"><![CDATA[Question 5]]></Block><Block id="questionData"><![CDATA[Read the item and choose the correct answer.]]></Block><Block id="question"><![CDATA[35 + 21 =]]></Block><Block id="ansChoice_1"><![CDATA[76]]></Block><Block id="ansChoice_2"><![CDATA[57]]></Block><Block id="ansChoice_3"><![CDATA[14]]></Block><Block id="ansChoice_4"><![CDATA[56]]></Block></Screen><!-- Features –  Calculator --><Screen id="Screen_18" UserGuide="type16,type27,type25"><Block id="questionInst"><![CDATA[]]></Block><Block id="question_no"><![CDATA[Question 5]]></Block><Block id="questionData"><![CDATA[Read the item and choose the correct answer.]]></Block><Block id="question"><![CDATA[35 + 21 =]]></Block><Block id="ansChoice_1"><![CDATA[76]]></Block><Block id="ansChoice_2"><![CDATA[57]]></Block><Block id="ansChoice_3"><![CDATA[14]]></Block><Block id="ansChoice_4"><![CDATA[56]]></Block></Screen><!-- Features – Reference Card  --><Screen id="Screen_19" UserGuide="type16,type27,type25"><Block id="questionInst"><![CDATA[]]></Block><Block id="question_no"><![CDATA[Question 5]]></Block><Block id="questionData"><![CDATA[Read the item and choose the correct answer.]]></Block><Block id="question"><![CDATA[35 + 21 =]]></Block><Block id="ansChoice_1"><![CDATA[76]]></Block><Block id="ansChoice_2"><![CDATA[57]]></Block><Block id="ansChoice_3"><![CDATA[14]]></Block><Block id="ansChoice_4"><![CDATA[56]]></Block></Screen><!--Features – Shared Stimulus Indicator  --><Screen id="Screen_20" UserGuide="type16,type27,type28"><Block id="questionInst"><![CDATA[]]></Block><Block id="question_no"><![CDATA[Question 5]]></Block><Block id="questionData"><![CDATA[Read the item and choose the correct answer.]]></Block><Block id="question"><![CDATA[35 + 21 =]]></Block><Block id="ansChoice_1"><![CDATA[76]]></Block><Block id="ansChoice_2"><![CDATA[57]]></Block><Block id="ansChoice_3"><![CDATA[14]]></Block><Block id="ansChoice_4"><![CDATA[56]]></Block></Screen><!--Features - Zoom Button  --><Screen id="Screen_21" UserGuide="type22,type27,type28,type3,type17"><Block id="questionInst"><![CDATA[]]></Block><Block id="question_no"><![CDATA[Question 6]]></Block><Block id="questionData"><![CDATA[Susan looked at the map and counted the cities. She counted Casper San Antonio and New York. Jason said "Susan, You missed one." ]]></Block><Block id="question"><![CDATA[What city did Susan miss?]]></Block><Block id="ansChoice_1"><![CDATA[San Francisco]]></Block><Block id="ansChoice_2"><![CDATA[Casper]]></Block><Block id="ansChoice_3"><![CDATA[San Antonio]]></Block><Block id="ansChoice_4"><![CDATA[New York]]></Block></Screen><!-- Features -Mark for Later Review  --><Screen id="Screen_22" UserGuide="type16,type27,type25,type3,type17"><Block id="questionInst"><![CDATA[]]></Block><Block id="question_no"><![CDATA[Question 6]]></Block><Block id="questionData"><![CDATA[Susan looked at the map and counted the cities. She counted Casper San Antonio and New York. Jason said "Susan, You missed one." ]]></Block><Block id="question"><![CDATA[What city did Susan miss?]]></Block><Block id="ansChoice_1"><![CDATA[San Francisco]]></Block><Block id="ansChoice_2"><![CDATA[Casper]]></Block><Block id="ansChoice_3"><![CDATA[San Antonio]]></Block><Block id="ansChoice_4"><![CDATA[New York]]></Block></Screen><!--Features - Using a Ruler --><Screen id="Screen_23" UserGuide="type16,type27,type25"><Block id="questionInst"><![CDATA[]]></Block><Block id="question_no"><![CDATA[Question 7]]></Block><Block id="question"><![CDATA[Mario made a bookmark  6 centimeters long. Besty\'s bookmark is twice as long as Mario\'s. Which bookmark must be Besty\'s?]]></Block></Screen><!--Features - Tabbed Page  --><Screen id="Screen_24" UserGuide="type22,type27,type25,type3,type17"><Block id="questionInst"><![CDATA[]]></Block><Block id="question_no"><![CDATA[Question 8]]></Block><Block id="question"><![CDATA[What phrase means the same as "explore"?]]></Block><Block id="ansChoice_1"><![CDATA[observe people carefully]]></Block><Block id="ansChoice_2"><![CDATA[examine in an orderly manner]]></Block><Block id="ansChoice_3"><![CDATA[read about archeology]]></Block><Block id="ansChoice_4"><![CDATA[learn about people]]></Block></Screen><!--Features  -Show and Hide Timer  --><Screen id="Screen_25" UserGuide="type16,type27,type25"><Block id="questionInst"><![CDATA[]]></Block><Block id="question_no"><![CDATA[Question 8]]></Block><Block id="question"><![CDATA[What phrase means the same as "explore"? ]]></Block><Block id="ansChoice_1"><![CDATA[observe people carefully]]></Block><Block id="ansChoice_2"><![CDATA[examine in an orderly manner]]></Block><Block id="ansChoice_3"><![CDATA[read about archeology]]></Block><Block id="ansChoice_4"><![CDATA[learn about people]]></Block></Screen><Screen id="Screen_26" UserGuide="type3,type18,type20"><Block id="questionInst"><![CDATA[]]></Block><Block id="question_no"><![CDATA[Question 20]]></Block><Block id="questionData"><![CDATA[]]></Block><Block id="question"><![CDATA[Find the picture that shows what Kim probably did on Sunday night.]]></Block><Block id="ansChoice_1"><![CDATA[ate a good dinner]]></Block><Block id="ansChoice_2"><![CDATA[stayed up late]]></Block><Block id="ansChoice_3"><![CDATA[went to bed early]]></Block><Block id="ansChoice_4"><![CDATA[Around]]></Block></Screen><!-- Features – Finishing the Test   --><Screen id="Screen_27" UserGuide="type16,type27,type25"><Block id="questionInst"><![CDATA[]]></Block><Block id="question_no"><![CDATA[Question 20]]></Block><Block id="questionData"><![CDATA[Read the item below and choose the correct answer.]]></Block><Block id="question"><![CDATA[63.7 + 20.2 =]]></Block><Block id="ansChoice_1"><![CDATA[83.5]]></Block><Block id="ansChoice_2"><![CDATA[84.9]]></Block><Block id="ansChoice_3"><![CDATA[43.5]]></Block><Block id="ansChoice_4"><![CDATA[83.9]]></Block></Screen><Screen id="Screen_28"></Screen><!-- The Screen Instructions  part starts here  --><Screen id="UserGuide"><!-- These occure more then 15 times  in all the document--><GuideMessage id="type1"><![CDATA[You need to read and follow these instructions to practice taking the test.]]></GuideMessage><GuideMessage id="type2"><![CDATA[Make sure you read these instructions to practice taking the test. ]]></GuideMessage><GuideMessage id="type3"><![CDATA[Make sure you read these instructions on how to practice taking the test.]]></GuideMessage><GuideMessage id="type16"><![CDATA[This is not the real test; this is just for practice. Follow the instructions on what to click next.]]></GuideMessage><GuideMessage id="type17"><![CDATA[The instructions will help you practice for the test. Please make sure you follow along. ]]></GuideMessage><GuideMessage id="type18"><![CDATA[This is just for practice. Read what the stickman is saying to find out what to do next. ]]></GuideMessage><GuideMessage id="type19"><![CDATA[Right now, you are practicing using different buttons. Do what the stickman is telling you to try next.]]></GuideMessage><GuideMessage id="type20"><![CDATA[Right now, you are practicing using all the different buttons. Do what the stickman is telling you to try next.]]></GuideMessage><GuideMessage id="type21"><![CDATA[Right now, you are practicing using all the different buttons. Do what the stickman is telling you to do next.]]></GuideMessage><GuideMessage id="type22"><![CDATA[This is not the real test; this is just for practice. Follow the instructions on what to do next.]]></GuideMessage><GuideMessage id="type23"><![CDATA[You need to follow these instructions to practice how to take the test. Make sure you read them all.]]></GuideMessage><GuideMessage id="type24"><![CDATA[You need to read and follow these instructions to practice taking the test. ]]></GuideMessage><GuideMessage id="type25"><![CDATA[Right now you are practicing using other buttons. Please do what the stickman is asking you to do.]]></GuideMessage><GuideMessage id="type26"><![CDATA[Make sure you read these instructions for how to practice taking the test.]]></GuideMessage><GuideMessage id="type27"><![CDATA[Do not worry about the question. This is just for practice. Please read what the stickman is asking you to do.]]></GuideMessage><GuideMessage id="type28"><![CDATA[Right now, you are practicing using other buttons. Do what the stickman is telling you to do next.]]></GuideMessage></Screen></TerraNova_content>'))
		// After loading is complete, call the respective function.
		xmlObj.onLoad = function(success){
			if(success)
			{
				calleeFunction(this)
			}
		}	
	
		// Load the XML into the flooring object.
		//xmlObj.load(xmlURL)
		calleeFunction(xmlObj)
	}
}