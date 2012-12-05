package edu.uco.teamdawn;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

public class CallSoap {
	public String SOAP_ACTION;

	public String OPERATION_NAME;

	public final String WSDL_TARGET_NAMESPACE = "http://tempuri.org/";

	public final String SOAP_ADDRESS = "http://teamdawncps.cloudapp.net/cpsWebService.asmx";

	public CallSoap() {

	}

	public String CallVerifyUser(String a, String b) {
		SOAP_ACTION = "http://tempuri.org/VerifyUser";
		OPERATION_NAME = "VerifyUser";

		SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,
				OPERATION_NAME);
		PropertyInfo pi = new PropertyInfo();
		pi.setName("username");
		pi.setValue(a);
		pi.setType(String.class);
		request.addProperty(pi);
		pi = new PropertyInfo();
		pi.setName("password");
		pi.setValue(b);
		pi.setType(String.class);
		request.addProperty(pi);

		Log.v("username", a);
		Log.v("password", b);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;

		envelope.setOutputSoapObject(request);

		HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
		Object response = null;
		
		try {

			httpTransport.call(SOAP_ACTION, envelope);

			response = envelope.getResponse();
		}

		catch (Exception exception)

		{

			response = exception.toString();
		}

		return response.toString();
	}
	
	public String CallIsUserCheckedIn(String username) {
		SOAP_ACTION = "http://tempuri.org/usUserCheckedIn";
		OPERATION_NAME = "usUserCheckedIn";

		SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,
				OPERATION_NAME);
		PropertyInfo pi = new PropertyInfo();
		pi.setName("username");
		pi.setValue(username);
		pi.setType(String.class);
		request.addProperty(pi);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;

		envelope.setOutputSoapObject(request);

		HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
		Object response = null;
		
		try {

			httpTransport.call(SOAP_ACTION, envelope);

			response = envelope.getResponse();
		}

		catch (Exception exception)

		{
			response = exception.toString();
		}

		return response.toString();
	}
	
	public String CallSetUserStatus(String username, boolean check) {
		SOAP_ACTION = "http://tempuri.org/SetUserStatus";
		OPERATION_NAME = "SetUserStatus";

		SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,
				OPERATION_NAME);
		PropertyInfo pi = new PropertyInfo();
		pi.setName("username");
		pi.setValue(username);
		pi.setType(String.class);
		request.addProperty(pi);
		pi = new PropertyInfo();
		pi.setName("check");
		pi.setValue(check);
		pi.setType(Boolean.class);
		request.addProperty(pi);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;

		envelope.setOutputSoapObject(request);

		HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
		Object response = null;
		
		try {

			httpTransport.call(SOAP_ACTION, envelope);

			response = envelope.getResponse();
		}

		catch (Exception exception)

		{
			response = exception.toString();
		}

		return response.toString();
	}

	public String CallGetSpotsByLotID(int lotID) {
		SOAP_ACTION = "http://tempuri.org/getSpotsByLotID";
		OPERATION_NAME = "getSpotsByLotID";

		SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,
				OPERATION_NAME);
		PropertyInfo pi = new PropertyInfo();
		pi.setName("lotID");
		pi.setValue(lotID);
		pi.setType(Integer.class);
		request.addProperty(pi);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;

		envelope.setOutputSoapObject(request);

		HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
		Object response = null;
	
		try {
			httpTransport.call(SOAP_ACTION, envelope);

			response = envelope.getResponse();
		}

		catch (Exception exception)

		{
			response = exception.toString();
		}

		Log.v("response", response.toString());
		return response.toString();
	}

	public String CallLotsByUserType(String type) {
		SOAP_ACTION = "http://tempuri.org/LotsByUserTypeTest";
		OPERATION_NAME = "LotsByUserTypeTest";

		SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,
				OPERATION_NAME);
		PropertyInfo pi = new PropertyInfo();
		pi.setName("type");
		pi.setValue(type);
		pi.setType(String.class);
		request.addProperty(pi);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;

		envelope.setOutputSoapObject(request);

		HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
		Object response = null;

		try {
			httpTransport.call(SOAP_ACTION, envelope);

			response = envelope.getResponse();
		}

		catch (Exception exception)
		{
			 response=exception.toString();
		}
		return response.toString();

	}

	public String CallReserveUserSpot(int spotNumber, String username, int lotID) {
		SOAP_ACTION = "http://tempuri.org/reserveUserSpot";
		OPERATION_NAME = "reserveUserSpot";

		SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,
				OPERATION_NAME);
		PropertyInfo pi = new PropertyInfo();
		pi.setName("spotNumber");
		pi.setValue(spotNumber);
		pi.setType(Integer.class);
		request.addProperty(pi);
		pi = new PropertyInfo();
		pi.setName("username");
		pi.setValue(username);
		pi.setType(String.class);
		request.addProperty(pi);
		pi = new PropertyInfo();
		pi.setName("lotID");
		pi.setValue(lotID);
		pi.setType(Integer.class);
		request.addProperty(pi);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;

		envelope.setOutputSoapObject(request);

		HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
		Object response = null;

		try {

			httpTransport.call(SOAP_ACTION, envelope);

			response = envelope.getResponse();
		}

		catch (Exception exception)

		{
			response = exception.toString();
		}

		return response.toString();
	}

	public String CallCheckInUserSpot(int spotNumber, int lotID) {
		SOAP_ACTION = "http://tempuri.org/checkInUserSpot";
		OPERATION_NAME = "checkInUserSpot";

		SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,
				OPERATION_NAME);
		PropertyInfo pi = new PropertyInfo();
		pi.setName("spotNumber");
		pi.setValue(spotNumber);
		pi.setType(Integer.class);
		request.addProperty(pi);
		pi = new PropertyInfo();
		pi.setName("lotID");
		pi.setValue(lotID);
		pi.setType(Integer.class);
		request.addProperty(pi);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;

		envelope.setOutputSoapObject(request);

		HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
		Object response = null;
		boolean succ = false;
		try {

			httpTransport.call(SOAP_ACTION, envelope);

			response = envelope.getResponse();
		}

		catch (Exception exception)

		{

			response = exception.toString();
		}

		return response.toString();
	}

	public String CallCheckOutUserSpot(int spotNumber, int lotID) {
		SOAP_ACTION = "http://tempuri.org/checkOutUserSpot";
		OPERATION_NAME = "checkOutUserSpot";

		SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,
				OPERATION_NAME);
		PropertyInfo pi = new PropertyInfo();
		pi.setName("spotNumber");
		pi.setValue(spotNumber);
		pi.setType(Integer.class);
		request.addProperty(pi);
		pi = new PropertyInfo();
		pi.setName("lotID");
		pi.setValue(lotID);
		pi.setType(Integer.class);
		request.addProperty(pi);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;

		envelope.setOutputSoapObject(request);

		HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
		Object response = null;
		boolean succ = false;
		try {

			httpTransport.call(SOAP_ACTION, envelope);

			response = envelope.getResponse();
		}

		catch (Exception exception)

		{

			response = exception.toString();
		}

		return response.toString();
	}

	public String CallCancelReservation(int spotNumber, int lotID) {
		SOAP_ACTION = "http://tempuri.org/cancelReservation";
		OPERATION_NAME = "cancelReservation";

		SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,
				OPERATION_NAME);
		PropertyInfo pi = new PropertyInfo();
		pi.setName("spotNumber");
		pi.setValue(spotNumber);
		pi.setType(Integer.class);
		request.addProperty(pi);
		pi = new PropertyInfo();
		pi.setName("lotID");
		pi.setValue(lotID);
		pi.setType(Integer.class);
		request.addProperty(pi);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;

		envelope.setOutputSoapObject(request);

		HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
		Object response = null;
		
		try {

			httpTransport.call(SOAP_ACTION, envelope);

			response = envelope.getResponse();
		}

		catch (Exception exception)

		{

			response = exception.toString();
		}

		return response.toString();
	}
}
