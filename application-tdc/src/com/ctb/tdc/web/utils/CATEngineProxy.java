package com.ctb.tdc.web.utils;

import java.util.HashMap;

public class CATEngineProxy {

	private static int itemnum;
	private static double theta;
	private static double SEM;
	private static String nextItem;

	private static HashMap itemIdMap;

	private static void initItemMap() {
		itemIdMap = new HashMap();
		itemIdMap.put(String.valueOf(299991), Integer.valueOf(186303));
		itemIdMap.put(String.valueOf(299909), Integer.valueOf(186403));
		itemIdMap.put(String.valueOf(299943), Integer.valueOf(186503));
		itemIdMap.put(String.valueOf(300305), Integer.valueOf(186603));
		itemIdMap.put(String.valueOf(299731), Integer.valueOf(186703));
		itemIdMap.put(String.valueOf(299337), Integer.valueOf(186803));
		itemIdMap.put(String.valueOf(299585), Integer.valueOf(186903));
		itemIdMap.put(String.valueOf(299109), Integer.valueOf(187003));
		itemIdMap.put(String.valueOf(300191), Integer.valueOf(220903));
		itemIdMap.put(String.valueOf(299483), Integer.valueOf(221003));
		itemIdMap.put(String.valueOf(300213), Integer.valueOf(221103));
		itemIdMap.put(String.valueOf(299663), Integer.valueOf(221203));
		itemIdMap.put(String.valueOf(299657), Integer.valueOf(221303));
		itemIdMap.put(String.valueOf(299625), Integer.valueOf(221403));
		itemIdMap.put(String.valueOf(299623), Integer.valueOf(221503));
		itemIdMap.put(String.valueOf(299619), Integer.valueOf(221603));
		itemIdMap.put(String.valueOf(300449), Integer.valueOf(221703));
		itemIdMap.put(String.valueOf(299475), Integer.valueOf(221803));
		itemIdMap.put(String.valueOf(300389), Integer.valueOf(221903));
		itemIdMap.put(String.valueOf(299715), Integer.valueOf(222003));
		itemIdMap.put(String.valueOf(299477), Integer.valueOf(222103));
		itemIdMap.put(String.valueOf(299679), Integer.valueOf(222203));
		itemIdMap.put(String.valueOf(299531), Integer.valueOf(222303));
		itemIdMap.put(String.valueOf(300283), Integer.valueOf(222403));
		itemIdMap.put(String.valueOf(299693), Integer.valueOf(222503));
		itemIdMap.put(String.valueOf(299707), Integer.valueOf(222603));
		itemIdMap.put(String.valueOf(299729), Integer.valueOf(222703));
		itemIdMap.put(String.valueOf(299593), Integer.valueOf(222803));
		itemIdMap.put(String.valueOf(299493), Integer.valueOf(222903));
		itemIdMap.put(String.valueOf(299539), Integer.valueOf(223003));
		itemIdMap.put(String.valueOf(299579), Integer.valueOf(223103));
		itemIdMap.put(String.valueOf(299687), Integer.valueOf(223203));
		itemIdMap.put(String.valueOf(299299), Integer.valueOf(223303));
		itemIdMap.put(String.valueOf(299285), Integer.valueOf(223403));
		itemIdMap.put(String.valueOf(299207), Integer.valueOf(223503));
		itemIdMap.put(String.valueOf(299083), Integer.valueOf(223603));
		itemIdMap.put(String.valueOf(299745), Integer.valueOf(223703));
		itemIdMap.put(String.valueOf(299251), Integer.valueOf(223803));
		itemIdMap.put(String.valueOf(299129), Integer.valueOf(223903));
		itemIdMap.put(String.valueOf(299575), Integer.valueOf(224003));
		itemIdMap.put(String.valueOf(299601), Integer.valueOf(224103));
		itemIdMap.put(String.valueOf(299719), Integer.valueOf(224203));
		itemIdMap.put(String.valueOf(299223), Integer.valueOf(224303));
		itemIdMap.put(String.valueOf(299595), Integer.valueOf(224403));
		itemIdMap.put(String.valueOf(299769), Integer.valueOf(224503));
		itemIdMap.put(String.valueOf(299651), Integer.valueOf(224603));
		itemIdMap.put(String.valueOf(299267), Integer.valueOf(224703));
		itemIdMap.put(String.valueOf(299313), Integer.valueOf(224803));
		itemIdMap.put(String.valueOf(299817), Integer.valueOf(246403));
		itemIdMap.put(String.valueOf(299997), Integer.valueOf(246503));
		itemIdMap.put(String.valueOf(299813), Integer.valueOf(246603));
		itemIdMap.put(String.valueOf(299977), Integer.valueOf(246703));
		itemIdMap.put(String.valueOf(299985), Integer.valueOf(246803));
		itemIdMap.put(String.valueOf(299829), Integer.valueOf(246903));
		itemIdMap.put(String.valueOf(300009), Integer.valueOf(247003));
		itemIdMap.put(String.valueOf(299897), Integer.valueOf(247103));
		itemIdMap.put(String.valueOf(300175), Integer.valueOf(247203));
		itemIdMap.put(String.valueOf(300337), Integer.valueOf(247303));
		itemIdMap.put(String.valueOf(299861), Integer.valueOf(247403));
		itemIdMap.put(String.valueOf(299863), Integer.valueOf(247503));
		itemIdMap.put(String.valueOf(300055), Integer.valueOf(247603));
		itemIdMap.put(String.valueOf(299839), Integer.valueOf(247703));
		itemIdMap.put(String.valueOf(300043), Integer.valueOf(247803));
		itemIdMap.put(String.valueOf(300067), Integer.valueOf(247903));
		itemIdMap.put(String.valueOf(299911), Integer.valueOf(248003));
		itemIdMap.put(String.valueOf(300183), Integer.valueOf(248103));
		itemIdMap.put(String.valueOf(300361), Integer.valueOf(248203));
		itemIdMap.put(String.valueOf(299933), Integer.valueOf(248303));
		itemIdMap.put(String.valueOf(300251), Integer.valueOf(248403));
		itemIdMap.put(String.valueOf(299875), Integer.valueOf(248503));
		itemIdMap.put(String.valueOf(300039), Integer.valueOf(248903));
		itemIdMap.put(String.valueOf(300079), Integer.valueOf(249003));
		itemIdMap.put(String.valueOf(299895), Integer.valueOf(249103));
		itemIdMap.put(String.valueOf(300129), Integer.valueOf(249203));
		itemIdMap.put(String.valueOf(300133), Integer.valueOf(249303));
		itemIdMap.put(String.valueOf(299945), Integer.valueOf(249403));
		itemIdMap.put(String.valueOf(299963), Integer.valueOf(248603));
		itemIdMap.put(String.valueOf(299845), Integer.valueOf(248703));
		itemIdMap.put(String.valueOf(299939), Integer.valueOf(249503));
		itemIdMap.put(String.valueOf(299953), Integer.valueOf(249603));
		itemIdMap.put(String.valueOf(299929), Integer.valueOf(249703));
		itemIdMap.put(String.valueOf(299877), Integer.valueOf(249803));
		itemIdMap.put(String.valueOf(300123), Integer.valueOf(248803));
		itemIdMap.put(String.valueOf(300113), Integer.valueOf(249903));
		itemIdMap.put(String.valueOf(300063), Integer.valueOf(250003));
		itemIdMap.put(String.valueOf(300125), Integer.valueOf(250103));
		itemIdMap.put(String.valueOf(299903), Integer.valueOf(250203));
		itemIdMap.put(String.valueOf(299967), Integer.valueOf(250303));
		itemIdMap.put(String.valueOf(300181), Integer.valueOf(271903));
		itemIdMap.put(String.valueOf(300023), Integer.valueOf(272003));
		itemIdMap.put(String.valueOf(300195), Integer.valueOf(272103));
		itemIdMap.put(String.valueOf(300219), Integer.valueOf(272203));
		itemIdMap.put(String.valueOf(300359), Integer.valueOf(272303));
		itemIdMap.put(String.valueOf(299931), Integer.valueOf(272403));
		itemIdMap.put(String.valueOf(300221), Integer.valueOf(272503));
		itemIdMap.put(String.valueOf(300367), Integer.valueOf(272603));
		itemIdMap.put(String.valueOf(300395), Integer.valueOf(272703));
		itemIdMap.put(String.valueOf(300209), Integer.valueOf(272803));
		itemIdMap.put(String.valueOf(299973), Integer.valueOf(272903));
		itemIdMap.put(String.valueOf(299969), Integer.valueOf(273003));
		itemIdMap.put(String.valueOf(300099), Integer.valueOf(273103));
		itemIdMap.put(String.valueOf(300177), Integer.valueOf(273203));
		itemIdMap.put(String.valueOf(300445), Integer.valueOf(273303));
		itemIdMap.put(String.valueOf(300271), Integer.valueOf(273403));
		itemIdMap.put(String.valueOf(300387), Integer.valueOf(273503));
		itemIdMap.put(String.valueOf(300277), Integer.valueOf(273603));
		itemIdMap.put(String.valueOf(300211), Integer.valueOf(273703));
		itemIdMap.put(String.valueOf(299959), Integer.valueOf(273803));
		itemIdMap.put(String.valueOf(300313), Integer.valueOf(273903));
		itemIdMap.put(String.valueOf(300233), Integer.valueOf(274003));
		itemIdMap.put(String.valueOf(299639), Integer.valueOf(274103));
		itemIdMap.put(String.valueOf(300485), Integer.valueOf(274203));
		itemIdMap.put(String.valueOf(300439), Integer.valueOf(274303));
		itemIdMap.put(String.valueOf(300317), Integer.valueOf(274403));
		itemIdMap.put(String.valueOf(300455), Integer.valueOf(274503));
		itemIdMap.put(String.valueOf(300493), Integer.valueOf(274603));
		itemIdMap.put(String.valueOf(300375), Integer.valueOf(274703));
		itemIdMap.put(String.valueOf(300285), Integer.valueOf(274803));
		itemIdMap.put(String.valueOf(300401), Integer.valueOf(274903));
		itemIdMap.put(String.valueOf(300483), Integer.valueOf(275003));
		itemIdMap.put(String.valueOf(300237), Integer.valueOf(275103));
		itemIdMap.put(String.valueOf(300489), Integer.valueOf(275203));
		itemIdMap.put(String.valueOf(300207), Integer.valueOf(275303));
		itemIdMap.put(String.valueOf(299547), Integer.valueOf(275403));
		itemIdMap.put(String.valueOf(300307), Integer.valueOf(275503));
		itemIdMap.put(String.valueOf(300327), Integer.valueOf(275603));
		itemIdMap.put(String.valueOf(299597), Integer.valueOf(275703));
		itemIdMap.put(String.valueOf(299559), Integer.valueOf(275803));
		itemIdMap.put(String.valueOf(299283), Integer.valueOf(304003));
		itemIdMap.put(String.valueOf(299347), Integer.valueOf(304103));
		itemIdMap.put(String.valueOf(299399), Integer.valueOf(304203));
		itemIdMap.put(String.valueOf(299305), Integer.valueOf(301903));
		itemIdMap.put(String.valueOf(299331), Integer.valueOf(302003));
		itemIdMap.put(String.valueOf(299091), Integer.valueOf(302103));
		itemIdMap.put(String.valueOf(299141), Integer.valueOf(302203));
		itemIdMap.put(String.valueOf(299259), Integer.valueOf(302303));
		itemIdMap.put(String.valueOf(299627), Integer.valueOf(302403));
		itemIdMap.put(String.valueOf(299629), Integer.valueOf(302503));
		itemIdMap.put(String.valueOf(299201), Integer.valueOf(302603));
		itemIdMap.put(String.valueOf(299537), Integer.valueOf(302703));
		itemIdMap.put(String.valueOf(299345), Integer.valueOf(302803));
		itemIdMap.put(String.valueOf(299281), Integer.valueOf(302903));
		itemIdMap.put(String.valueOf(299153), Integer.valueOf(303003));
		itemIdMap.put(String.valueOf(299185), Integer.valueOf(303103));
		itemIdMap.put(String.valueOf(299107), Integer.valueOf(303203));
		itemIdMap.put(String.valueOf(299293), Integer.valueOf(303303));
		itemIdMap.put(String.valueOf(299735), Integer.valueOf(303403));
		itemIdMap.put(String.valueOf(299275), Integer.valueOf(303503));
		itemIdMap.put(String.valueOf(299105), Integer.valueOf(303603));
		itemIdMap.put(String.valueOf(299147), Integer.valueOf(303703));
		itemIdMap.put(String.valueOf(299309), Integer.valueOf(303803));
		itemIdMap.put(String.valueOf(299339), Integer.valueOf(303903));
		itemIdMap.put(String.valueOf(299145), Integer.valueOf(304303));
		itemIdMap.put(String.valueOf(299355), Integer.valueOf(304403));
		itemIdMap.put(String.valueOf(299231), Integer.valueOf(304503));
		itemIdMap.put(String.valueOf(299587), Integer.valueOf(304603));
		itemIdMap.put(String.valueOf(299393), Integer.valueOf(304703));
		itemIdMap.put(String.valueOf(299341), Integer.valueOf(304803));
		itemIdMap.put(String.valueOf(299161), Integer.valueOf(304903));
		itemIdMap.put(String.valueOf(299133), Integer.valueOf(305003));
		itemIdMap.put(String.valueOf(299705), Integer.valueOf(305103));
		itemIdMap.put(String.valueOf(299343), Integer.valueOf(305203));
		itemIdMap.put(String.valueOf(299363), Integer.valueOf(305303));
		itemIdMap.put(String.valueOf(299353), Integer.valueOf(305403));
		itemIdMap.put(String.valueOf(299241), Integer.valueOf(305503));
		itemIdMap.put(String.valueOf(299379), Integer.valueOf(305603));
		itemIdMap.put(String.valueOf(299311), Integer.valueOf(305703));
		itemIdMap.put(String.valueOf(299195), Integer.valueOf(305803));
		itemIdMap.put(String.valueOf(299665), Integer.valueOf(327403));
		itemIdMap.put(String.valueOf(299467), Integer.valueOf(327503));
		itemIdMap.put(String.valueOf(300399), Integer.valueOf(327603));
		itemIdMap.put(String.valueOf(299635), Integer.valueOf(327703));
		itemIdMap.put(String.valueOf(299507), Integer.valueOf(327803));
		itemIdMap.put(String.valueOf(299489), Integer.valueOf(327903));
		itemIdMap.put(String.valueOf(300379), Integer.valueOf(328003));
		itemIdMap.put(String.valueOf(299511), Integer.valueOf(328303));
		itemIdMap.put(String.valueOf(299527), Integer.valueOf(328403));
		itemIdMap.put(String.valueOf(299517), Integer.valueOf(328503));
		itemIdMap.put(String.valueOf(299721), Integer.valueOf(328603));
		itemIdMap.put(String.valueOf(299545), Integer.valueOf(328703));
		itemIdMap.put(String.valueOf(299733), Integer.valueOf(328803));
		itemIdMap.put(String.valueOf(299653), Integer.valueOf(328903));
		itemIdMap.put(String.valueOf(299529), Integer.valueOf(329003));
		itemIdMap.put(String.valueOf(299085), Integer.valueOf(329103));
		itemIdMap.put(String.valueOf(299513), Integer.valueOf(329203));
		itemIdMap.put(String.valueOf(299753), Integer.valueOf(329303));
		itemIdMap.put(String.valueOf(300423), Integer.valueOf(329403));
		itemIdMap.put(String.valueOf(299551), Integer.valueOf(329503));
		itemIdMap.put(String.valueOf(300371), Integer.valueOf(329603));
		itemIdMap.put(String.valueOf(300437), Integer.valueOf(329703));
		itemIdMap.put(String.valueOf(299571), Integer.valueOf(329803));
		itemIdMap.put(String.valueOf(299757), Integer.valueOf(329903));
		itemIdMap.put(String.valueOf(299755), Integer.valueOf(330003));
		itemIdMap.put(String.valueOf(299479), Integer.valueOf(330103));
		itemIdMap.put(String.valueOf(299555), Integer.valueOf(330203));
		itemIdMap.put(String.valueOf(299491), Integer.valueOf(330303));
		itemIdMap.put(String.valueOf(299151), Integer.valueOf(330403));
		itemIdMap.put(String.valueOf(299695), Integer.valueOf(330503));
		itemIdMap.put(String.valueOf(299649), Integer.valueOf(330603));
		itemIdMap.put(String.valueOf(299589), Integer.valueOf(330703));
		itemIdMap.put(String.valueOf(299685), Integer.valueOf(328103));
		itemIdMap.put(String.valueOf(299557), Integer.valueOf(328203));
		itemIdMap.put(String.valueOf(299751), Integer.valueOf(330803));
		itemIdMap.put(String.valueOf(299165), Integer.valueOf(330903));
		itemIdMap.put(String.valueOf(299609), Integer.valueOf(331003));
		itemIdMap.put(String.valueOf(299779), Integer.valueOf(331103));
		itemIdMap.put(String.valueOf(299689), Integer.valueOf(331203));
		itemIdMap.put(String.valueOf(299775), Integer.valueOf(331303));
		itemIdMap.put(String.valueOf(299879), Integer.valueOf(352903));
		itemIdMap.put(String.valueOf(300053), Integer.valueOf(353003));
		itemIdMap.put(String.valueOf(299955), Integer.valueOf(353103));
		itemIdMap.put(String.valueOf(300101), Integer.valueOf(353203));
		itemIdMap.put(String.valueOf(300217), Integer.valueOf(353303));
		itemIdMap.put(String.valueOf(299919), Integer.valueOf(353403));
		itemIdMap.put(String.valueOf(300249), Integer.valueOf(353503));
		itemIdMap.put(String.valueOf(300035), Integer.valueOf(353603));
		itemIdMap.put(String.valueOf(299999), Integer.valueOf(353703));
		itemIdMap.put(String.valueOf(300415), Integer.valueOf(353803));
		itemIdMap.put(String.valueOf(300265), Integer.valueOf(353903));
		itemIdMap.put(String.valueOf(300427), Integer.valueOf(354003));
		itemIdMap.put(String.valueOf(300199), Integer.valueOf(354103));
		itemIdMap.put(String.valueOf(300443), Integer.valueOf(354203));
		itemIdMap.put(String.valueOf(299497), Integer.valueOf(354303));
		itemIdMap.put(String.valueOf(300243), Integer.valueOf(354403));
		itemIdMap.put(String.valueOf(300435), Integer.valueOf(354503));
		itemIdMap.put(String.valueOf(300225), Integer.valueOf(354603));
		itemIdMap.put(String.valueOf(300353), Integer.valueOf(354703));
		itemIdMap.put(String.valueOf(300257), Integer.valueOf(354803));
		itemIdMap.put(String.valueOf(300229), Integer.valueOf(354903));
		itemIdMap.put(String.valueOf(299465), Integer.valueOf(355003));
		itemIdMap.put(String.valueOf(300321), Integer.valueOf(355103));
		itemIdMap.put(String.valueOf(300293), Integer.valueOf(355203));
		itemIdMap.put(String.valueOf(300433), Integer.valueOf(355303));
		itemIdMap.put(String.valueOf(300291), Integer.valueOf(355403));
		itemIdMap.put(String.valueOf(300267), Integer.valueOf(355503));
		itemIdMap.put(String.valueOf(300315), Integer.valueOf(355603));
		itemIdMap.put(String.valueOf(300295), Integer.valueOf(355703));
		itemIdMap.put(String.valueOf(300377), Integer.valueOf(355803));
		itemIdMap.put(String.valueOf(300299), Integer.valueOf(355903));
		itemIdMap.put(String.valueOf(300227), Integer.valueOf(356003));
		itemIdMap.put(String.valueOf(300473), Integer.valueOf(356103));
		itemIdMap.put(String.valueOf(300325), Integer.valueOf(356203));
		itemIdMap.put(String.valueOf(299717), Integer.valueOf(356303));
		itemIdMap.put(String.valueOf(299459), Integer.valueOf(356403));
		itemIdMap.put(String.valueOf(300247), Integer.valueOf(356503));
		itemIdMap.put(String.valueOf(299767), Integer.valueOf(356603));
		itemIdMap.put(String.valueOf(299739), Integer.valueOf(356703));
		itemIdMap.put(String.valueOf(300459), Integer.valueOf(356803));
		itemIdMap.put(String.valueOf(299833), Integer.valueOf(378403));
		itemIdMap.put(String.valueOf(299851), Integer.valueOf(378503));
		itemIdMap.put(String.valueOf(300001), Integer.valueOf(378603));
		itemIdMap.put(String.valueOf(299981), Integer.valueOf(378703));
		itemIdMap.put(String.valueOf(299983), Integer.valueOf(378803));
		itemIdMap.put(String.valueOf(299873), Integer.valueOf(378903));
		itemIdMap.put(String.valueOf(300007), Integer.valueOf(379003));
		itemIdMap.put(String.valueOf(300027), Integer.valueOf(379103));
		itemIdMap.put(String.valueOf(300021), Integer.valueOf(379203));
		itemIdMap.put(String.valueOf(299837), Integer.valueOf(379303));
		itemIdMap.put(String.valueOf(299881), Integer.valueOf(379603));
		itemIdMap.put(String.valueOf(299857), Integer.valueOf(379703));
		itemIdMap.put(String.valueOf(299913), Integer.valueOf(379803));
		itemIdMap.put(String.valueOf(299989), Integer.valueOf(379903));
		itemIdMap.put(String.valueOf(299869), Integer.valueOf(380003));
		itemIdMap.put(String.valueOf(300187), Integer.valueOf(380103));
		itemIdMap.put(String.valueOf(299947), Integer.valueOf(380203));
		itemIdMap.put(String.valueOf(299993), Integer.valueOf(380303));
		itemIdMap.put(String.valueOf(300095), Integer.valueOf(380403));
		itemIdMap.put(String.valueOf(300373), Integer.valueOf(380503));
		itemIdMap.put(String.valueOf(299965), Integer.valueOf(380603));
		itemIdMap.put(String.valueOf(299893), Integer.valueOf(380703));
		itemIdMap.put(String.valueOf(300403), Integer.valueOf(380803));
		itemIdMap.put(String.valueOf(300075), Integer.valueOf(380903));
		itemIdMap.put(String.valueOf(300385), Integer.valueOf(381003));
		itemIdMap.put(String.valueOf(300085), Integer.valueOf(381103));
		itemIdMap.put(String.valueOf(300407), Integer.valueOf(381203));
		itemIdMap.put(String.valueOf(299923), Integer.valueOf(381303));
		itemIdMap.put(String.valueOf(299905), Integer.valueOf(381403));
		itemIdMap.put(String.valueOf(300107), Integer.valueOf(381503));
		itemIdMap.put(String.valueOf(300119), Integer.valueOf(381603));
		itemIdMap.put(String.valueOf(300117), Integer.valueOf(381703));
		itemIdMap.put(String.valueOf(300111), Integer.valueOf(381803));
		itemIdMap.put(String.valueOf(300047), Integer.valueOf(381903));
		itemIdMap.put(String.valueOf(299915), Integer.valueOf(382003));
		itemIdMap.put(String.valueOf(299907), Integer.valueOf(382103));
		itemIdMap.put(String.valueOf(299823), Integer.valueOf(382203));
		itemIdMap.put(String.valueOf(299961), Integer.valueOf(382303));
		itemIdMap.put(String.valueOf(299841), Integer.valueOf(379403));
		itemIdMap.put(String.valueOf(299921), Integer.valueOf(379503));
		itemIdMap.put(String.valueOf(299637), Integer.valueOf(194503));
		itemIdMap.put(String.valueOf(299677), Integer.valueOf(194603));
		itemIdMap.put(String.valueOf(299137), Integer.valueOf(194703));
		itemIdMap.put(String.valueOf(299221), Integer.valueOf(194803));
		itemIdMap.put(String.valueOf(299163), Integer.valueOf(194903));
		itemIdMap.put(String.valueOf(299553), Integer.valueOf(195003));
		itemIdMap.put(String.valueOf(299173), Integer.valueOf(195103));
		itemIdMap.put(String.valueOf(299671), Integer.valueOf(195203));
		itemIdMap.put(String.valueOf(299307), Integer.valueOf(195303));
		itemIdMap.put(String.valueOf(299359), Integer.valueOf(195403));
		itemIdMap.put(String.valueOf(299383), Integer.valueOf(195503));
		itemIdMap.put(String.valueOf(299323), Integer.valueOf(195603));
		itemIdMap.put(String.valueOf(299317), Integer.valueOf(195703));
		itemIdMap.put(String.valueOf(299257), Integer.valueOf(195803));
		itemIdMap.put(String.valueOf(299247), Integer.valueOf(195903));
		itemIdMap.put(String.valueOf(299131), Integer.valueOf(196003));
		itemIdMap.put(String.valueOf(299325), Integer.valueOf(196103));
		itemIdMap.put(String.valueOf(299759), Integer.valueOf(196203));
		itemIdMap.put(String.valueOf(299747), Integer.valueOf(196303));
		itemIdMap.put(String.valueOf(299749), Integer.valueOf(196403));
		itemIdMap.put(String.valueOf(299097), Integer.valueOf(196503));
		itemIdMap.put(String.valueOf(299149), Integer.valueOf(196603));
		itemIdMap.put(String.valueOf(299189), Integer.valueOf(196703));
		itemIdMap.put(String.valueOf(299357), Integer.valueOf(196803));
		itemIdMap.put(String.valueOf(299373), Integer.valueOf(196903));
		itemIdMap.put(String.valueOf(299377), Integer.valueOf(197003));
		itemIdMap.put(String.valueOf(299369), Integer.valueOf(197103));
		itemIdMap.put(String.valueOf(299171), Integer.valueOf(197203));
		itemIdMap.put(String.valueOf(299481), Integer.valueOf(197303));
		itemIdMap.put(String.valueOf(299115), Integer.valueOf(197403));
		itemIdMap.put(String.valueOf(299279), Integer.valueOf(197503));
		itemIdMap.put(String.valueOf(299211), Integer.valueOf(197603));
		itemIdMap.put(String.valueOf(299101), Integer.valueOf(197703));
		itemIdMap.put(String.valueOf(299139), Integer.valueOf(197803));
		itemIdMap.put(String.valueOf(299351), Integer.valueOf(197903));
		itemIdMap.put(String.valueOf(299371), Integer.valueOf(198003));
		itemIdMap.put(String.valueOf(299203), Integer.valueOf(198103));
		itemIdMap.put(String.valueOf(299385), Integer.valueOf(198203));
		itemIdMap.put(String.valueOf(299403), Integer.valueOf(198303));
		itemIdMap.put(String.valueOf(299315), Integer.valueOf(198403));
	}

	public static native int setup_cat(String contentArea);
        public static native int next_item();
	public static native void set_rwo(int n);
	public static native double score();
	public static native double getSEM(double theta);
	public static native void setoff_cat();

	public static void main(String [] args){
		try {
			initCAT("MC");
			String next = getNextItem();
			while(next != null){
				scoreCurrentItem(new Integer((int) Math.round(Math.random())));
				next = getNextItem();
			}
			getAbilityScore();
			getSEM();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			getAbilityScore();
			getSEM();
		}finally {
			deInitCAT();
		}
	}

	public static void initCAT(String contentArea) {
		System.out.println("Called initCAT()");
		System.load("C:/Program Files/CTB/Online Assessment/CATABE.dll");
		itemnum = 0;
		setup_cat(contentArea);
		initItemMap();
	}

	public static String getNextItem() throws Exception{
		System.out.println("Called getNextItem()");
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
		       
		        String nextitem = String.valueOf(next_item());
			if(nextitem == null || nextitem.equals("-1")) {
				throw new Exception("CAT OVER!");
			} else {
				itemnum++;
				System.out.print("PEID ID: " + nextitem + ", ");
				Integer adsitem = (Integer)itemIdMap.get(nextitem);
				System.out.print("ADS ID: " + adsitem + "\n");
				nextItem = String.valueOf(adsitem.intValue());
			}
	}

	public static double getAbilityScore() {
		System.out.println("Called getAbilityScore()");
		System.out.println("Ability: " + theta);
		return theta;
	}

	public static double getSEM() {
		System.out.println("Called getSEM()");
		SEM = getSEM(theta);
		System.out.println("SEM: " + SEM);
		return SEM;
	}

	public static void deInitCAT() {
		System.out.println("Called deInitCAT()");
		setoff_cat();
	}
}
