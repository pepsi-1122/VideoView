package com.ewaytek.android.bean;

import java.io.Serializable;


public class AssistBean implements Serializable {
	private static final long serialVersionUID = -5620661009186692227L;
	private boolean isTxt=true;
	private boolean isHex=true;
	private String SendTxtA="COMA",SendTxtB="COMB",SendTxtC="COMC",SendTxtD="COMD";
	private String SendHexA="CC004B5E01DD5302",SendHexB="BB",SendHexC="CC",SendHexD="DD";
	public String sTimeA="500";
	public String sTimeB="500";

	public static String TakeSample5mL = "CC004D042FDD2902";
	public static String TakeSample3mL = "CC004D3E1CDD5002";
	public static String TakeSample1mL = "CC004D6709DD6602";

	public  static String InjectSample5mL = "CC0042042FDD1E02";
	public  static String InjectSample3mL = "CC00423E1CDD4502";
	public  static String InjectSample2mL = "CC0042CE12DD5802";
	public  static String InjectSample1mL = "CC00426709DD5802";


	public static String InitDevices = "CC00450000DDEE01";
	public static String DeviceStop = "CC00490000DDF201";
	public static String MAXSpeed = "CC004B5E01DD5302";


	//滑台控制
	public  static  String SlidingTablePositive1cm= "AB01010100BA";
	public  static  String SlidingTablePositive2cm= "AB02020100BA";
	public  static  String SlidingTablePositive3cm= "AB01030000BA";
	public  static  String SlidingTableZero= "AB06000000BA";
	public  static  String SlidingTableReversal1cm= "AB01010000BA";
	public  static  String SlidingTableReversal2cm= "AB020A0000BA";
	public  static  String SlidingTableReversal5cm= "AB02280000BA";

	public  static  String SlidingTableDowm3cm ="AB021E0000BA";
	public  static  String SlidingTableU3pcm ="AB021E0100BA";
    public  static  String SlidingTableDowmEND ="AB023C0000BA";
    public  static  String SlidingTableUpEND= "AB023C0100BA";

	//托盘1控制
	public  static  String pallet1Positive24= "AC01013000CA";
	public  static  String pallet1Reversal24= "AC01013001CA";

	//托盘2控制
	public  static  String pallet2LifeEND = "AC02020300CA";
	public  static  String pallet2RightEND = "AC02020301CA";


	//托盘3控制
	public  static  String pallet3Positive24= "AC03010000CA";
	public  static  String pallet3Reversal24= "AC03010001CA";



	public boolean isHex()
	{
		return isTxt;
	}
	public void setHexMode(boolean isHex)
	{
		this.isTxt = isHex;
	}

	public boolean isTxt()
	{
		return isTxt;
	}
	public void setTxtMode(boolean isTxt)
	{
		this.isTxt = isTxt;
	}

	public String getSendA()
	{

		return SendHexA;
	}
	public String getSendB()
	{
		return SendHexB;
	}


	public void setSendA(String sendA)
	{

		SendHexA = sendA;
	}

	public void setSendB(String sendB)
	{
		SendHexB = sendB;
	}

}