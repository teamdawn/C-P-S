package edu.uco.teamdawn;


import android.app.AlertDialog;





public class Caller  extends Thread 
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
		String resp=cs.CallVerifyUser(a, b);
		LoginActivity.rslt=resp;
		
		/*}catch(Exception ex)
		{
			LoginActivity.rslt=ex.toString();	
		}*/

	
    }
}