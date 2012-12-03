package edu.uco.teamdawn;


import java.util.ArrayList;

import android.app.AlertDialog;





public class Caller2  extends Thread 
{
	public String spotActivity;
	public AlertDialog ad;
	public CallSoap cs;
	String a;
	String b;
	String type;

	public void run()
	{
		/*try
		{*/
		
		cs=new CallSoap();	
		String[] resp=cs.CallLotsByUserType(type);
		SelectSpotActivity.lotsByUserType=resp;
		
		/*}catch(Exception ex)
		{
			LoginActivity.rslt=ex.toString();	
		}*/

	
    }
}