package com.browser2app.pse.psenativedemo;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

	private static final String TAG = MainActivity.class.getSimpleName();
	private EditText ecus;
	private EditText amount;
	private String authorizerId;
	private EditText subject;
	private EditText merchant;
	private String userTypeId;
	private EditText returnURL;
	private EditText payerEmail;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);


		checkIfPSEisInstalled();


		ecus = (EditText) findViewById(R.id.ecus);
		amount = (EditText) findViewById(R.id.amount);
		Spinner authorizerSpinner = (Spinner) findViewById(R.id.authorizerId);
		ArrayList<Authorizer> authorizers = new ArrayList<>();
		authorizers.add(new Authorizer("1040", "BANCO AGRARIO"));
		authorizers.add(new Authorizer("1052", "BANCO AV VILLAS"));
		authorizers.add(new Authorizer("1013", "BANCO BBVA COLOMBIA S.A."));
		authorizers.add(new Authorizer("1032", "BANCO CAJA SOCIAL"));
		authorizers.add(new Authorizer("1019", "BANCO COLPATRIA"));
		authorizers.add(new Authorizer("1066", "BANCO COOPERATIVO COOPCENTRAL"));
		authorizers.add(new Authorizer("1006", "BANCO CORPBANCA S.A"));
		authorizers.add(new Authorizer("1051", "BANCO DAVIVIENDA"));
		authorizers.add(new Authorizer("1001", "BANCO DE BOGOTA"));
		authorizers.add(new Authorizer("1023", "BANCO DE OCCIDENTE"));
		authorizers.add(new Authorizer("1062", "BANCO FALABELLA"));
		authorizers.add(new Authorizer("1012", "BANCO GNB SUDAMERIS"));
		authorizers.add(new Authorizer("1060", "BANCO PICHINCHA S.A."));
		authorizers.add(new Authorizer("1002", "BANCO POPULAR"));
		authorizers.add(new Authorizer("1058", "BANCO PROCREDIT"));
		authorizers.add(new Authorizer("1007", "BANCOLOMBIA"));
		authorizers.add(new Authorizer("1061", "BANCOOMEVA S.A."));
		authorizers.add(new Authorizer("1009", "CITIBANK"));
		authorizers.add(new Authorizer("1014", "HELM BANK S.A."));
		authorizers.add(new Authorizer("1507", "NEQUI"));

		ArrayAdapter<Authorizer> authorizerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, authorizers);
		authorizerSpinner.setAdapter(authorizerAdapter);
		authorizerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
				Authorizer authorizer = (Authorizer)adapterView.getSelectedItem();
				authorizerId = authorizer.getId();
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {

			}
		});
		subject = (EditText) findViewById(R.id.subject);
		merchant = (EditText) findViewById(R.id.merchant);
		Spinner userTypeSpinner = (Spinner) findViewById(R.id.userType);
		ArrayList<UserType> userTypes = new ArrayList<>();
		userTypes.add(new UserType("0", "Persona Natural"));
		userTypes.add(new UserType("1", "Persona Jurídica"));
		ArrayAdapter<UserType> userTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, userTypes);
		userTypeSpinner.setAdapter(userTypeAdapter);
		userTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
				UserType userType = (UserType)adapterView.getSelectedItem();
				userTypeId = userType.getId();
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {

			}
		});
		returnURL = (EditText) findViewById(R.id.returnURL);
		payerEmail = (EditText) findViewById(R.id.payerEmail);
	}

	private void checkIfPSEisInstalled() {
		Intent intent = null;
		try {
			intent = Intent.parseUri("pseb2a://openapp/", Intent.URI_INTENT_SCHEME);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		if (intent == null || intent.resolveActivity(getPackageManager()) == null) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Para continuar debes instalar PSE Móvil")
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							Intent i = new Intent(android.content.Intent.ACTION_VIEW);
							i.setData(Uri.parse("market://details?id=com.browser2app.pse"));
							startActivity(i);
						}
					});
			builder.create().show();
		}
	}


	public void doPay(View view) {
		String automatonId = userTypeId + authorizerId;
		String sEcus = ecus.getText().toString();
		String sAmount = amount.getText().toString() + ".00";
		String sSubject = subject.getText().toString();
		String sMerchant = merchant.getText().toString();
		String sPaymentId = sEcus;
		String sReturnURL = returnURL.getText().toString();
		String sCancelURL = sReturnURL;
		String sPayerEmail = payerEmail.getText().toString();


		Call<AutomatonRequestResponse> call = ((PSENativeDemo)getApplicationContext()).getAutomatonServer().automatonRequest(automatonId,
					userTypeId, authorizerId, sEcus, sAmount, sSubject, sMerchant, sPaymentId, sReturnURL, sCancelURL, sPayerEmail);


		call.enqueue(new Callback<AutomatonRequestResponse>() {
			@Override
			public void onResponse(Call<AutomatonRequestResponse> call, Response<AutomatonRequestResponse> response) {
				AutomatonRequestResponse automatonRequestResponse = response.body();

				Intent intent = null;
				try {
					intent = Intent.parseUri("pseb2a://openapp/" + automatonRequestResponse.getAutomatonRequestId(), Intent.URI_INTENT_SCHEME);
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}
				if (intent != null && intent.resolveActivity(getPackageManager()) != null) {
					startActivity(intent);
				}

			}

			@Override
			public void onFailure(Call<AutomatonRequestResponse> call, Throwable t) {
				Log.e(TAG, "fail", t);
			}
		});


	}

}
