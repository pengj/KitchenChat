package com.joyn.kitchenchat.com;

public class Consts {
	public static final String MY_PHONE_1 = "+33678819929";
	public static final String MY_PHONE = "+33673691264"; 
	
	public static final String KITCHEM_PHONE ="+33677240390";
	
	public static final boolean KITCHEN = false;
	
	private static String Black_Phone="cleverchat1";
	private static String White_Phone="cleverchat2";
	public static final String pass_xmpp = "clever";

	public static final String XMPP_HOST="195.157.156.86";
	public static final int XMPP_PORT=5222;	
	public static final String XMPP_HOST_NAME="uk-ilab-shindig";
	
	public static final String SENDER = KITCHEN?Black_Phone:White_Phone;
	public static final String RECEIVER = KITCHEN?White_Phone:Black_Phone;
	
}
