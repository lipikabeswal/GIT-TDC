//package com.ctb.tdc.web.utils;

import java.text.DecimalFormat;
import java.util.HashMap;

public class CATEngineProxy {

	private static int itemnum;
	private static double theta;
	private static double SEM;
	private static String nextItem;
	private static int totObjNum;
	private static int obj_id;
	private static double obj_score;
	private static char obj_lvl;  // E, M, D, A
	   
	private static double obj_SSsem;
	private static int obj_rs ;
	private static int totObj_rs ;
	private static int obj_masteryLvl; 

	public static HashMap itemIdMap = new HashMap();
	//public static HashMap itemIdMap;
	
	private static void initItemMap() {
		itemIdMap.put(String.valueOf(299817), Integer.valueOf(38682402));
		itemIdMap.put(String.valueOf(299997), Integer.valueOf(38682502));
		itemIdMap.put(String.valueOf(299813), Integer.valueOf(38682602));
		itemIdMap.put(String.valueOf(299977), Integer.valueOf(38682702));
		itemIdMap.put(String.valueOf(299985), Integer.valueOf(38682802));
		itemIdMap.put(String.valueOf(299829), Integer.valueOf(38682902));
		itemIdMap.put(String.valueOf(300009), Integer.valueOf(38683002));
		itemIdMap.put(String.valueOf(299897), Integer.valueOf(38683102));
		itemIdMap.put(String.valueOf(300175), Integer.valueOf(38683202));
		itemIdMap.put(String.valueOf(300337), Integer.valueOf(38683302));
		itemIdMap.put(String.valueOf(299861), Integer.valueOf(38683402));
		itemIdMap.put(String.valueOf(299863), Integer.valueOf(38683502));
		itemIdMap.put(String.valueOf(300055), Integer.valueOf(38683602));
		itemIdMap.put(String.valueOf(299839), Integer.valueOf(38683702));
		itemIdMap.put(String.valueOf(300043), Integer.valueOf(38683802));
		itemIdMap.put(String.valueOf(300067), Integer.valueOf(38683902));
		itemIdMap.put(String.valueOf(299911), Integer.valueOf(38684002));
		itemIdMap.put(String.valueOf(300183), Integer.valueOf(38684102));
		itemIdMap.put(String.valueOf(300361), Integer.valueOf(38684202));
		itemIdMap.put(String.valueOf(299933), Integer.valueOf(38684302));
		itemIdMap.put(String.valueOf(300251), Integer.valueOf(38684402));
		itemIdMap.put(String.valueOf(299875), Integer.valueOf(38684502));
		itemIdMap.put(String.valueOf(299963), Integer.valueOf(38684602));
		itemIdMap.put(String.valueOf(299845), Integer.valueOf(38684702));
		itemIdMap.put(String.valueOf(300123), Integer.valueOf(38684802));
		itemIdMap.put(String.valueOf(300039), Integer.valueOf(38684902));
		itemIdMap.put(String.valueOf(300079), Integer.valueOf(38685002));
		itemIdMap.put(String.valueOf(299895), Integer.valueOf(38685102));
		itemIdMap.put(String.valueOf(300129), Integer.valueOf(38685202));
		itemIdMap.put(String.valueOf(300133), Integer.valueOf(38685302));
		itemIdMap.put(String.valueOf(299945), Integer.valueOf(38685402));
		itemIdMap.put(String.valueOf(299939), Integer.valueOf(38685502));
		itemIdMap.put(String.valueOf(299953), Integer.valueOf(38685602));
		itemIdMap.put(String.valueOf(299929), Integer.valueOf(38685702));
		itemIdMap.put(String.valueOf(299877), Integer.valueOf(38685802));
		itemIdMap.put(String.valueOf(300113), Integer.valueOf(38685902));
		itemIdMap.put(String.valueOf(300063), Integer.valueOf(38686002));
		itemIdMap.put(String.valueOf(300125), Integer.valueOf(38686102));
		itemIdMap.put(String.valueOf(299903), Integer.valueOf(38686202));
		itemIdMap.put(String.valueOf(299967), Integer.valueOf(38686302));
		itemIdMap.put(String.valueOf(300181), Integer.valueOf(38686402));
		itemIdMap.put(String.valueOf(300023), Integer.valueOf(38686502));
		itemIdMap.put(String.valueOf(300195), Integer.valueOf(38686602));
		itemIdMap.put(String.valueOf(300219), Integer.valueOf(38686702));
		itemIdMap.put(String.valueOf(300359), Integer.valueOf(38686802));
		itemIdMap.put(String.valueOf(299931), Integer.valueOf(38686902));
		itemIdMap.put(String.valueOf(300221), Integer.valueOf(38687002));
		itemIdMap.put(String.valueOf(300367), Integer.valueOf(38687102));
		itemIdMap.put(String.valueOf(300395), Integer.valueOf(38687202));
		itemIdMap.put(String.valueOf(300209), Integer.valueOf(38687302));
		itemIdMap.put(String.valueOf(299973), Integer.valueOf(38687402));
		itemIdMap.put(String.valueOf(299969), Integer.valueOf(38687502));
		itemIdMap.put(String.valueOf(300099), Integer.valueOf(38687602));
		itemIdMap.put(String.valueOf(300177), Integer.valueOf(38687702));
		itemIdMap.put(String.valueOf(300445), Integer.valueOf(38687802));
		itemIdMap.put(String.valueOf(300271), Integer.valueOf(38687902));
		itemIdMap.put(String.valueOf(300387), Integer.valueOf(38688002));
		itemIdMap.put(String.valueOf(300277), Integer.valueOf(38688102));
		itemIdMap.put(String.valueOf(300211), Integer.valueOf(38688202));
		itemIdMap.put(String.valueOf(299959), Integer.valueOf(38688302));
		itemIdMap.put(String.valueOf(300313), Integer.valueOf(38688402));
		itemIdMap.put(String.valueOf(300233), Integer.valueOf(38688502));
		itemIdMap.put(String.valueOf(299639), Integer.valueOf(38688602));
		itemIdMap.put(String.valueOf(300485), Integer.valueOf(38688702));
		itemIdMap.put(String.valueOf(300439), Integer.valueOf(38688802));
		itemIdMap.put(String.valueOf(300317), Integer.valueOf(38688902));
		itemIdMap.put(String.valueOf(300455), Integer.valueOf(38689002));
		itemIdMap.put(String.valueOf(300493), Integer.valueOf(38689102));
		itemIdMap.put(String.valueOf(300375), Integer.valueOf(38689202));
		itemIdMap.put(String.valueOf(300285), Integer.valueOf(38689302));
		itemIdMap.put(String.valueOf(300401), Integer.valueOf(38689402));
		itemIdMap.put(String.valueOf(300483), Integer.valueOf(38689502));
		itemIdMap.put(String.valueOf(300237), Integer.valueOf(38689602));
		itemIdMap.put(String.valueOf(300489), Integer.valueOf(38689702));
		itemIdMap.put(String.valueOf(300207), Integer.valueOf(38689802));
		itemIdMap.put(String.valueOf(299547), Integer.valueOf(38689902));
		itemIdMap.put(String.valueOf(300307), Integer.valueOf(38690002));
		itemIdMap.put(String.valueOf(300327), Integer.valueOf(38690102));
		itemIdMap.put(String.valueOf(299597), Integer.valueOf(38690202));
		itemIdMap.put(String.valueOf(299559), Integer.valueOf(38690302));
		itemIdMap.put(String.valueOf(300191), Integer.valueOf(38690402));
		itemIdMap.put(String.valueOf(299483), Integer.valueOf(38690502));
		itemIdMap.put(String.valueOf(300213), Integer.valueOf(38690602));
		itemIdMap.put(String.valueOf(299663), Integer.valueOf(38690702));
		itemIdMap.put(String.valueOf(299657), Integer.valueOf(38690802));
		itemIdMap.put(String.valueOf(299625), Integer.valueOf(38690902));
		itemIdMap.put(String.valueOf(299623), Integer.valueOf(38691002));
		itemIdMap.put(String.valueOf(299619), Integer.valueOf(38691102));
		itemIdMap.put(String.valueOf(300449), Integer.valueOf(38691202));
		itemIdMap.put(String.valueOf(299475), Integer.valueOf(38691302));
		itemIdMap.put(String.valueOf(300389), Integer.valueOf(38691402));
		itemIdMap.put(String.valueOf(299715), Integer.valueOf(38691502));
		itemIdMap.put(String.valueOf(299477), Integer.valueOf(38691602));
		itemIdMap.put(String.valueOf(299679), Integer.valueOf(38691702));
		itemIdMap.put(String.valueOf(299531), Integer.valueOf(38691802));
		itemIdMap.put(String.valueOf(300283), Integer.valueOf(38691902));
		itemIdMap.put(String.valueOf(299693), Integer.valueOf(38692002));
		itemIdMap.put(String.valueOf(299707), Integer.valueOf(38692102));
		itemIdMap.put(String.valueOf(299729), Integer.valueOf(38692202));
		itemIdMap.put(String.valueOf(299593), Integer.valueOf(38692302));
		itemIdMap.put(String.valueOf(299493), Integer.valueOf(38692402));
		itemIdMap.put(String.valueOf(299539), Integer.valueOf(38692502));
		itemIdMap.put(String.valueOf(299579), Integer.valueOf(38692602));
		itemIdMap.put(String.valueOf(299687), Integer.valueOf(38692702));
		itemIdMap.put(String.valueOf(299299), Integer.valueOf(38692802));
		itemIdMap.put(String.valueOf(299285), Integer.valueOf(38692902));
		itemIdMap.put(String.valueOf(299207), Integer.valueOf(38693002));
		itemIdMap.put(String.valueOf(299083), Integer.valueOf(38693102));
		itemIdMap.put(String.valueOf(299745), Integer.valueOf(38693202));
		itemIdMap.put(String.valueOf(299251), Integer.valueOf(38693302));
		itemIdMap.put(String.valueOf(299129), Integer.valueOf(38693402));
		itemIdMap.put(String.valueOf(299575), Integer.valueOf(38693502));
		itemIdMap.put(String.valueOf(299601), Integer.valueOf(38693602));
		itemIdMap.put(String.valueOf(299719), Integer.valueOf(38693702));
		itemIdMap.put(String.valueOf(299223), Integer.valueOf(38693802));
		itemIdMap.put(String.valueOf(299595), Integer.valueOf(38693902));
		itemIdMap.put(String.valueOf(299769), Integer.valueOf(38694002));
		itemIdMap.put(String.valueOf(299651), Integer.valueOf(38694102));
		itemIdMap.put(String.valueOf(299267), Integer.valueOf(38694202));
		itemIdMap.put(String.valueOf(299313), Integer.valueOf(38694302));
		itemIdMap.put(String.valueOf(299637), Integer.valueOf(38694402));
		itemIdMap.put(String.valueOf(299677), Integer.valueOf(38694502));
		itemIdMap.put(String.valueOf(299137), Integer.valueOf(38694602));
		itemIdMap.put(String.valueOf(299221), Integer.valueOf(38694702));
		itemIdMap.put(String.valueOf(299163), Integer.valueOf(38694802));
		itemIdMap.put(String.valueOf(299553), Integer.valueOf(38694902));
		itemIdMap.put(String.valueOf(299173), Integer.valueOf(38695002));
		itemIdMap.put(String.valueOf(299671), Integer.valueOf(38695102));
		itemIdMap.put(String.valueOf(299307), Integer.valueOf(38695202));
		itemIdMap.put(String.valueOf(299359), Integer.valueOf(38695302));
		itemIdMap.put(String.valueOf(299383), Integer.valueOf(38695402));
		itemIdMap.put(String.valueOf(299323), Integer.valueOf(38695502));
		itemIdMap.put(String.valueOf(299317), Integer.valueOf(38695602));
		itemIdMap.put(String.valueOf(299257), Integer.valueOf(38695702));
		itemIdMap.put(String.valueOf(299247), Integer.valueOf(38695802));
		itemIdMap.put(String.valueOf(299131), Integer.valueOf(38695902));
		itemIdMap.put(String.valueOf(299325), Integer.valueOf(38696002));
		itemIdMap.put(String.valueOf(299759), Integer.valueOf(38696102));
		itemIdMap.put(String.valueOf(299747), Integer.valueOf(38696202));
		itemIdMap.put(String.valueOf(299749), Integer.valueOf(38696302));
		itemIdMap.put(String.valueOf(299097), Integer.valueOf(38696402));
		itemIdMap.put(String.valueOf(299149), Integer.valueOf(38696502));
		itemIdMap.put(String.valueOf(299189), Integer.valueOf(38696602));
		itemIdMap.put(String.valueOf(299357), Integer.valueOf(38696702));
		itemIdMap.put(String.valueOf(299373), Integer.valueOf(38696802));
		itemIdMap.put(String.valueOf(299377), Integer.valueOf(38696902));
		itemIdMap.put(String.valueOf(299369), Integer.valueOf(38697002));
		itemIdMap.put(String.valueOf(299171), Integer.valueOf(38697102));
		itemIdMap.put(String.valueOf(299481), Integer.valueOf(38697202));
		itemIdMap.put(String.valueOf(299115), Integer.valueOf(38697302));
		itemIdMap.put(String.valueOf(299279), Integer.valueOf(38697402));
		itemIdMap.put(String.valueOf(299211), Integer.valueOf(38697502));
		itemIdMap.put(String.valueOf(299101), Integer.valueOf(38697602));
		itemIdMap.put(String.valueOf(299139), Integer.valueOf(38697702));
		itemIdMap.put(String.valueOf(299351), Integer.valueOf(38697802));
		itemIdMap.put(String.valueOf(299371), Integer.valueOf(38697902));
		itemIdMap.put(String.valueOf(299203), Integer.valueOf(38698002));
		itemIdMap.put(String.valueOf(299385), Integer.valueOf(38698102));
		itemIdMap.put(String.valueOf(299403), Integer.valueOf(38698202));
		itemIdMap.put(String.valueOf(299315), Integer.valueOf(38698302));
		itemIdMap.put(String.valueOf(299833), Integer.valueOf(38698402));
		itemIdMap.put(String.valueOf(299851), Integer.valueOf(38698502));
		itemIdMap.put(String.valueOf(300001), Integer.valueOf(38698602));
		itemIdMap.put(String.valueOf(299981), Integer.valueOf(38698702));
		itemIdMap.put(String.valueOf(299983), Integer.valueOf(38698802));
		itemIdMap.put(String.valueOf(299873), Integer.valueOf(38698902));
		itemIdMap.put(String.valueOf(300007), Integer.valueOf(38699002));
		itemIdMap.put(String.valueOf(300027), Integer.valueOf(38699102));
		itemIdMap.put(String.valueOf(300021), Integer.valueOf(38699202));
		itemIdMap.put(String.valueOf(299837), Integer.valueOf(38699302));
		itemIdMap.put(String.valueOf(299841), Integer.valueOf(38699402));
		itemIdMap.put(String.valueOf(299921), Integer.valueOf(38699502));
		itemIdMap.put(String.valueOf(299881), Integer.valueOf(38699602));
		itemIdMap.put(String.valueOf(299857), Integer.valueOf(38699702));
		itemIdMap.put(String.valueOf(299913), Integer.valueOf(38699802));
		itemIdMap.put(String.valueOf(299989), Integer.valueOf(38699902));
		itemIdMap.put(String.valueOf(299869), Integer.valueOf(38700002));
		itemIdMap.put(String.valueOf(300187), Integer.valueOf(38700102));
		itemIdMap.put(String.valueOf(299947), Integer.valueOf(38700202));
		itemIdMap.put(String.valueOf(299993), Integer.valueOf(38700302));
		itemIdMap.put(String.valueOf(300095), Integer.valueOf(38700402));
		itemIdMap.put(String.valueOf(300373), Integer.valueOf(38700502));
		itemIdMap.put(String.valueOf(299965), Integer.valueOf(38700602));
		itemIdMap.put(String.valueOf(299893), Integer.valueOf(38700702));
		itemIdMap.put(String.valueOf(300403), Integer.valueOf(38700802));
		itemIdMap.put(String.valueOf(300075), Integer.valueOf(38700902));
		itemIdMap.put(String.valueOf(300385), Integer.valueOf(38701002));
		itemIdMap.put(String.valueOf(300085), Integer.valueOf(38701102));
		itemIdMap.put(String.valueOf(300407), Integer.valueOf(38701202));
		itemIdMap.put(String.valueOf(299923), Integer.valueOf(38701302));
		itemIdMap.put(String.valueOf(299905), Integer.valueOf(38701402));
		itemIdMap.put(String.valueOf(300107), Integer.valueOf(38701502));
		itemIdMap.put(String.valueOf(300119), Integer.valueOf(38701602));
		itemIdMap.put(String.valueOf(300117), Integer.valueOf(38701702));
		itemIdMap.put(String.valueOf(300111), Integer.valueOf(38701802));
		itemIdMap.put(String.valueOf(300047), Integer.valueOf(38701902));
		itemIdMap.put(String.valueOf(299915), Integer.valueOf(38702002));
		itemIdMap.put(String.valueOf(299907), Integer.valueOf(38702102));
		itemIdMap.put(String.valueOf(299823), Integer.valueOf(38702202));
		itemIdMap.put(String.valueOf(299961), Integer.valueOf(38702302));
		itemIdMap.put(String.valueOf(299879), Integer.valueOf(38702402));
		itemIdMap.put(String.valueOf(300053), Integer.valueOf(38702502));
		itemIdMap.put(String.valueOf(299955), Integer.valueOf(38702602));
		itemIdMap.put(String.valueOf(300101), Integer.valueOf(38702702));
		itemIdMap.put(String.valueOf(300217), Integer.valueOf(38702802));
		itemIdMap.put(String.valueOf(299919), Integer.valueOf(38702902));
		itemIdMap.put(String.valueOf(300249), Integer.valueOf(38703002));
		itemIdMap.put(String.valueOf(300035), Integer.valueOf(38703102));
		itemIdMap.put(String.valueOf(299999), Integer.valueOf(38703202));
		itemIdMap.put(String.valueOf(300415), Integer.valueOf(38703302));
		itemIdMap.put(String.valueOf(300265), Integer.valueOf(38703402));
		itemIdMap.put(String.valueOf(300427), Integer.valueOf(38703502));
		itemIdMap.put(String.valueOf(300199), Integer.valueOf(38703602));
		itemIdMap.put(String.valueOf(300443), Integer.valueOf(38703702));
		itemIdMap.put(String.valueOf(299497), Integer.valueOf(38703802));
		itemIdMap.put(String.valueOf(300243), Integer.valueOf(38703902));
		itemIdMap.put(String.valueOf(300435), Integer.valueOf(38704002));
		itemIdMap.put(String.valueOf(300225), Integer.valueOf(38704102));
		itemIdMap.put(String.valueOf(300353), Integer.valueOf(38704202));
		itemIdMap.put(String.valueOf(300257), Integer.valueOf(38704302));
		itemIdMap.put(String.valueOf(300229), Integer.valueOf(38704402));
		itemIdMap.put(String.valueOf(299465), Integer.valueOf(38704502));
		itemIdMap.put(String.valueOf(300321), Integer.valueOf(38704602));
		itemIdMap.put(String.valueOf(300293), Integer.valueOf(38704702));
		itemIdMap.put(String.valueOf(300433), Integer.valueOf(38704802));
		itemIdMap.put(String.valueOf(300291), Integer.valueOf(38704902));
		itemIdMap.put(String.valueOf(300267), Integer.valueOf(38705002));
		itemIdMap.put(String.valueOf(300315), Integer.valueOf(38705102));
		itemIdMap.put(String.valueOf(300295), Integer.valueOf(38705202));
		itemIdMap.put(String.valueOf(300377), Integer.valueOf(38705302));
		itemIdMap.put(String.valueOf(300299), Integer.valueOf(38705402));
		itemIdMap.put(String.valueOf(300227), Integer.valueOf(38705502));
		itemIdMap.put(String.valueOf(300473), Integer.valueOf(38705602));
		itemIdMap.put(String.valueOf(300325), Integer.valueOf(38705702));
		itemIdMap.put(String.valueOf(299717), Integer.valueOf(38705802));
		itemIdMap.put(String.valueOf(299459), Integer.valueOf(38705902));
		itemIdMap.put(String.valueOf(300247), Integer.valueOf(38706002));
		itemIdMap.put(String.valueOf(299767), Integer.valueOf(38706102));
		itemIdMap.put(String.valueOf(299739), Integer.valueOf(38706202));
		itemIdMap.put(String.valueOf(300459), Integer.valueOf(38706302));
		itemIdMap.put(String.valueOf(299665), Integer.valueOf(38706402));
		itemIdMap.put(String.valueOf(299467), Integer.valueOf(38706502));
		itemIdMap.put(String.valueOf(300399), Integer.valueOf(38706602));
		itemIdMap.put(String.valueOf(299635), Integer.valueOf(38706702));
		itemIdMap.put(String.valueOf(299507), Integer.valueOf(38706802));
		itemIdMap.put(String.valueOf(299489), Integer.valueOf(38706902));
		itemIdMap.put(String.valueOf(300379), Integer.valueOf(38707002));
		itemIdMap.put(String.valueOf(299685), Integer.valueOf(38707102));
		itemIdMap.put(String.valueOf(299557), Integer.valueOf(38707202));
		itemIdMap.put(String.valueOf(299511), Integer.valueOf(38707302));
		itemIdMap.put(String.valueOf(299527), Integer.valueOf(38707402));
		itemIdMap.put(String.valueOf(299517), Integer.valueOf(38707502));
		itemIdMap.put(String.valueOf(299721), Integer.valueOf(38707602));
		itemIdMap.put(String.valueOf(299545), Integer.valueOf(38707702));
		itemIdMap.put(String.valueOf(299733), Integer.valueOf(38707802));
		itemIdMap.put(String.valueOf(299653), Integer.valueOf(38707902));
		itemIdMap.put(String.valueOf(299529), Integer.valueOf(38708002));
		itemIdMap.put(String.valueOf(299085), Integer.valueOf(38708102));
		itemIdMap.put(String.valueOf(299513), Integer.valueOf(38708202));
		itemIdMap.put(String.valueOf(299753), Integer.valueOf(38708302));
		itemIdMap.put(String.valueOf(300423), Integer.valueOf(38708402));
		itemIdMap.put(String.valueOf(299551), Integer.valueOf(38708502));
		itemIdMap.put(String.valueOf(300371), Integer.valueOf(38708602));
		itemIdMap.put(String.valueOf(300437), Integer.valueOf(38708702));
		itemIdMap.put(String.valueOf(299571), Integer.valueOf(38708802));
		itemIdMap.put(String.valueOf(299757), Integer.valueOf(38708902));
		itemIdMap.put(String.valueOf(299755), Integer.valueOf(38709002));
		itemIdMap.put(String.valueOf(299479), Integer.valueOf(38709102));
		itemIdMap.put(String.valueOf(299555), Integer.valueOf(38709202));
		itemIdMap.put(String.valueOf(299491), Integer.valueOf(38709302));
		itemIdMap.put(String.valueOf(299151), Integer.valueOf(38709402));
		itemIdMap.put(String.valueOf(299695), Integer.valueOf(38709502));
		itemIdMap.put(String.valueOf(299649), Integer.valueOf(38709602));
		itemIdMap.put(String.valueOf(299589), Integer.valueOf(38709702));
		itemIdMap.put(String.valueOf(299751), Integer.valueOf(38709802));
		itemIdMap.put(String.valueOf(299165), Integer.valueOf(38709902));
		itemIdMap.put(String.valueOf(299609), Integer.valueOf(38710002));
		itemIdMap.put(String.valueOf(299779), Integer.valueOf(38710102));
		itemIdMap.put(String.valueOf(299689), Integer.valueOf(38710202));
		itemIdMap.put(String.valueOf(299775), Integer.valueOf(38710302));
		itemIdMap.put(String.valueOf(299305), Integer.valueOf(38710402));
		itemIdMap.put(String.valueOf(299331), Integer.valueOf(38710502));
		itemIdMap.put(String.valueOf(299091), Integer.valueOf(38710602));
		itemIdMap.put(String.valueOf(299141), Integer.valueOf(38710702));
		itemIdMap.put(String.valueOf(299259), Integer.valueOf(38710802));
		itemIdMap.put(String.valueOf(299627), Integer.valueOf(38710902));
		itemIdMap.put(String.valueOf(299629), Integer.valueOf(38711002));
		itemIdMap.put(String.valueOf(299201), Integer.valueOf(38711102));
		itemIdMap.put(String.valueOf(299537), Integer.valueOf(38711202));
		itemIdMap.put(String.valueOf(299345), Integer.valueOf(38711302));
		itemIdMap.put(String.valueOf(299281), Integer.valueOf(38711402));
		itemIdMap.put(String.valueOf(299153), Integer.valueOf(38711502));
		itemIdMap.put(String.valueOf(299185), Integer.valueOf(38711602));
		itemIdMap.put(String.valueOf(299107), Integer.valueOf(38711702));
		itemIdMap.put(String.valueOf(299293), Integer.valueOf(38711802));
		itemIdMap.put(String.valueOf(299735), Integer.valueOf(38711902));
		itemIdMap.put(String.valueOf(299275), Integer.valueOf(38712002));
		itemIdMap.put(String.valueOf(299105), Integer.valueOf(38712102));
		itemIdMap.put(String.valueOf(299147), Integer.valueOf(38712202));
		itemIdMap.put(String.valueOf(299309), Integer.valueOf(38712302));
		itemIdMap.put(String.valueOf(299339), Integer.valueOf(38712402));
		itemIdMap.put(String.valueOf(299283), Integer.valueOf(38712502));
		itemIdMap.put(String.valueOf(299347), Integer.valueOf(38712602));
		itemIdMap.put(String.valueOf(299399), Integer.valueOf(38712702));
		itemIdMap.put(String.valueOf(299145), Integer.valueOf(38712802));
		itemIdMap.put(String.valueOf(299355), Integer.valueOf(38712902));
		itemIdMap.put(String.valueOf(299231), Integer.valueOf(38713002));
		itemIdMap.put(String.valueOf(299587), Integer.valueOf(38713102));
		itemIdMap.put(String.valueOf(299393), Integer.valueOf(38713202));
		itemIdMap.put(String.valueOf(299341), Integer.valueOf(38713302));
		itemIdMap.put(String.valueOf(299161), Integer.valueOf(38713402));
		itemIdMap.put(String.valueOf(299133), Integer.valueOf(38713502));
		itemIdMap.put(String.valueOf(299705), Integer.valueOf(38713602));
		itemIdMap.put(String.valueOf(299343), Integer.valueOf(38713702));
		itemIdMap.put(String.valueOf(299363), Integer.valueOf(38713802));
		itemIdMap.put(String.valueOf(299353), Integer.valueOf(38713902));
		itemIdMap.put(String.valueOf(299241), Integer.valueOf(38714002));
		itemIdMap.put(String.valueOf(299379), Integer.valueOf(38714102));
		itemIdMap.put(String.valueOf(299311), Integer.valueOf(38714202));
		itemIdMap.put(String.valueOf(299195), Integer.valueOf(38714302));
		itemIdMap.put(String.valueOf(1379066), Integer.valueOf(38714402));
		itemIdMap.put(String.valueOf(1379068), Integer.valueOf(38714502));
		itemIdMap.put(String.valueOf(1379070), Integer.valueOf(38714602));
		itemIdMap.put(String.valueOf(1379072), Integer.valueOf(38714702));
		itemIdMap.put(String.valueOf(1379074), Integer.valueOf(38714802));
		itemIdMap.put(String.valueOf(1379076), Integer.valueOf(38714902));
		
	}
	public static native void printhello();
	public static native int setup_cat(String contentArea);
    public static native int next_item();
	public static native void set_rwo(int n);
	public static native double score();
	public static native double getSEM(double theta);
	public static native void setoff_cat();
	public static native int get_nObj();
	public static native int get_objID(int j);
	public static native double get_objScaleScore(char obj_lvl, int obj_id);
	public static native char get_objLvl(double theta);  // E, M, D, A
	public static native double get_objSSsem(double obj_score, int obj_id);  
	public static native int get_objRS();
	public static native int get_totObjRS();
	public static native int get_objMasteryLvl(double obj_score, int obj_id, char obj_lvl);
	public static native int getTestLength();
	//public static native void resumeCAT(int itemCount,int [] itemC,int [] itemScore);
	
	
	public static void main(String [] args){
		try {
			//printhello();
			initCAT("MC");
			System.out.println("before next");
			String next = getNextItem();
			System.out.println("next "+next);
			
			while(next != null){
				scoreCurrentItem(new Integer((int) Math.round(Math.random())));
				next = getNextItem();
			}
			getAbilityScore();
			getSEM();
			getObjScore();
		
		} catch (Exception e) {
			System.out.println(e.getMessage());
			getAbilityScore();
			getSEM();
			getObjScore();
		}finally {
			deInitCAT();
		}
	}

	public static void initCAT(String contentArea){
		System.out.println("Called initCAT()");
		//Determine the type of OS
		String dllPath="";
		String ostype=System.getProperty("os.name").toLowerCase();
		if (ostype.indexOf( "win" ) >= 0){
			System.out.println("For Windows ");
			dllPath = System.getProperty("tdc.home") + "/CATABE.dll";
			//System.load(dllPath);
		}
		else 
			if (ostype.indexOf( "mac" ) >= 0){
				System.out.println("For MAC");
				
					dllPath = System.getProperty("tdc.home") + "/libCATABE.jnilib";
					//System.load("/Users/mcgrawhillctb/documents/CAT_mac/libCATABE.jnilib")
				
			}
		else 
			if (ostype.indexOf( "nix") >=0 || ostype.indexOf( "nux") >=0){
				System.out.println("For UNIX/LINUX");
				dllPath = System.getProperty("tdc.home") + "/libCATABE.so";
				//System.loadLibrary("libCATABE.so");
			}
		else
			System.out.println("OS Not supported");
        try{
        	//System.load(dllPath); 
        	//System.load("C:/CAT_test/priyanka/src/CATABE.dll");
			System.load("/Users/mcgrawhillctb/documents/mac_CAT_working/build/release/libCATABE.jnilib");
			System.out.println("loaded successfully");
        }catch(UnsatisfiedLinkError e){
			System.err.println("Failed Load"+e);
		}
		//printhello();
		//itemnum = 0;
		//nextItem = null;//set null to make it ready for next subtest
		System.out.println("before setup");
		try{
		setup_cat(contentArea);
			
		}catch(UnsatisfiedLinkError e){
			System.err.println("Failed Load"+e);
		}
		System.out.println("failed here");
		initItemMap();
		
	}

	public static String getNextItem() throws Exception{
		System.out.println("Called getNextItem()" + itemIdMap.size());
		System.out.println("next item is "+ nextItem);
		
		if(nextItem == null) {
				scoreCurrentItem(null);
		}
		return nextItem;
	}

	public static void scoreCurrentItem(Integer currentItemRawScore) throws Exception {
		System.out.println("Called scoreCurrentItem()");
			if(currentItemRawScore != null) {
				set_rwo(currentItemRawScore.intValue());
				theta = score();
				System.out.println("item score: " + currentItemRawScore + ", new theta: " + theta);
			}
		//String nextitem="";
		//System.out.println("xxx");
		String nextitem = String.valueOf(next_item());
		//System.out.println("YYY");
		        
		        
			if(nextitem == null || nextitem.equals("-1")) {
				throw new Exception("CAT OVER!");
			} else {
				itemnum++;
				System.out.println(" scoreCurrentItem CATEngineProxy itemIdMap size :"+CATEngineProxy.itemIdMap.size());
				System.out.print("PEID ID: " + nextitem + ", ");
				Integer adsitem = (Integer)itemIdMap.get(nextitem);
				System.out.print("ADS ID: " + adsitem + "\n");
				nextItem = String.valueOf(adsitem.intValue());
			}
	}

	public static double getAbilityScore() {
		//System.out.println("Called getAbilityScore()");
		//System.out.println("Ability: " + theta);
		return theta;
	}

	public static double getSEM() {
		//System.out.println("Called getSEM()");
		SEM = getSEM(theta);
		//System.out.println("SEM: " + SEM + "::"+ theta);
		return SEM;
	}
	
	public static String getObjScore() {
		//System.out.println("Called getObjScore()");
		totObjNum = get_nObj();
		String scoreString = null;
		for ( int j = 0; j < totObjNum; j++) {
			   obj_id = get_objID(j);
			   obj_lvl = get_objLvl(theta);  // E, M, D, A
			   obj_score = get_objScaleScore(obj_lvl, obj_id);
			   if (obj_score > 0) {
				   obj_SSsem = get_objSSsem(obj_score, obj_id);
			       obj_rs = get_objRS();
				   totObj_rs = get_totObjRS();
			       obj_masteryLvl = get_objMasteryLvl(obj_score, obj_id, obj_lvl); // 0 = Non-Mastery, 1=Partial-Mastery, 2=Mastery
	               if (obj_masteryLvl < 0){
	            	   System.out.println("Error: invalid objective level call. \n");
	               }else{
					   System.out.println( (j+1) + " id= " + obj_id + " rs= "+ obj_rs + " totRS= " + totObj_rs 
							   + " SS = "+ obj_score + " sem= " + obj_SSsem + " lvl = "+ obj_lvl +
							   " Mastery-level = " + obj_masteryLvl);
	               }
	               System.out.println("obj_masteryLvl :"+obj_masteryLvl);
	               if(scoreString == null)
	            	   scoreString = obj_id +","+ obj_rs +","+ totObj_rs +","+ obj_score +","+ obj_SSsem +","+ obj_lvl +","+ obj_masteryLvl ;
	               else
	            	   scoreString = scoreString + "|" + obj_id +","+ obj_rs +","+ totObj_rs +","+ obj_score +","+ obj_SSsem +","+ obj_lvl +","+ obj_masteryLvl ;
			   }
			   else {
				   if(scoreString == null)
					   scoreString = "0,0,0,0,0,0,0";
				else
				   scoreString = scoreString + "|" + "0,0,0,0,0,0,0";
			   }  // not report objective score
		}
		return scoreString;
	}
	

	public static void deInitCAT() {
		System.out.println("Called deInitCAT()");
		setoff_cat();
	}
}
