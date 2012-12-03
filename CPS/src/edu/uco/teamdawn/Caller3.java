package edu.uco.teamdawn;


import java.util.ArrayList;

import android.app.AlertDialog;





public class Caller3  extends Thread 
{
	public String spotActivity;
	public AlertDialog ad;
	public CallSoap cs;
	String type;
	int spotNumber, lotID;
	String username;

	public void run()
	{
		/*try
		{*/
		
		cs=new CallSoap();	
		String resp=cs.CallReserveUserSpot(spotNumber, username, lotID);
		SelectSpotActivity.reserve=resp;
		
		/*}catch(Exception ex)
		{
			LoginActivity.rslt=ex.toString();	
		}*/

	
    }
}