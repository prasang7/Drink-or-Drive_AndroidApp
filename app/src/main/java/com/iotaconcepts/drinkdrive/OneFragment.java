package com.iotaconcepts.drinkdrive;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.iotaconcepts.drinkdrive.R;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class OneFragment extends Fragment
{
    View view;
    SessionManager session;
    String name, number;
    Button bt_sms, bt_call, bt_emerg;

    double lat,lon;
    GPSTracker gps;

    public OneFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_one, container, false);

        //Get user's login data from session
        session = new SessionManager(getActivity().getApplicationContext());
        session.checkLogin();
        HashMap<String, String> user = session.getUserDetails();
        name = user.get(SessionManager.KEY_NAME);
        number = user.get(SessionManager.KEY_EMAIL);

        // Loading Font Face
        Typeface mont_reg = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Montserrat-Regular.otf");

        //bt_ride = (Button)view.findViewById(R.id.bt_ride);
        bt_sms = (Button)view.findViewById(R.id.bt_smsHelp);
        bt_call = (Button)view.findViewById(R.id.bt_callHelp);
        bt_emerg = (Button)view.findViewById(R.id.bt_emergencyButton_one);

        bt_sms.setText("Send Help SMS to : " + number);
        bt_call.setText("Make a Call to : " + number);
        bt_emerg.setText("Emergency Numbers");

        //bt_ride.setTypeface(mont_reg);
        bt_sms.setTypeface(mont_reg);
        bt_call.setTypeface(mont_reg);
        bt_emerg.setTypeface(mont_reg);

        //Location Tracker
        gps = new GPSTracker(getActivity());

        // check if GPS enabled
        if(gps.canGetLocation()) {
            lat = gps.getLatitude();
            lon = gps.getLongitude();
        }
        else {
              // can't get location or GPS or Network is not enabled then Ask user to enable GPS/network in settings
            //gps.showSettingsAlert();
        }

        bt_sms.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    // check if GPS enabled
                    if(gps.canGetLocation()) {
                    }
                    else {
//                        gps.showSettingsAlert();
                    }

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

                    // Setting Dialog Title
                    alertDialog.setTitle("Send Help SMS?");

                    // Setting Dialog Message
                    alertDialog.setMessage("\nSend SMS to " + number + " ?\n\n\nContent of SMS : \n\n" + "Contact " + name + ", he/she is not in a condition to drive & needs your help to get back home from location : \'Google Maps URL of your location\' \n\n" );

                    alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to invoke NO event
                        }
                    });

                    alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            // Write your code here to invoke NO event

                            SmsManager smsManager_2 = SmsManager.getDefault();
                            String smsBody;
                            smsBody = "http://maps.google.com/?q=" + lat + "," + lon;
                            smsManager_2.sendTextMessage(number, null,"Contact " + name + ", he/she is not in a condition to drive & needs your help to get back home from location : " + smsBody, null, null);
                            Toast.makeText(getActivity().getApplicationContext(), "SMS successfully sent!", Toast.LENGTH_LONG).show();
                        }
                    });

                    // Showing Alert Message
                    alertDialog.show();
                }
                catch (Exception e)
                {
                    Toast.makeText(getActivity().getApplicationContext(), "SMS failed, please try again.", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });

        bt_call.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

                // Setting Dialog Title
                alertDialog.setTitle("Are you sure ?");

                // Setting Dialog Message
                alertDialog.setMessage("Make a call to " + number + " ?");

                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to invoke NO event
                    }
                });

                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to invoke NO event
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + number));
                        startActivity(callIntent);
                    }
                });

                alertDialog.show();
            }
        });

        bt_emerg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), EmergencyNumbers.class);
                startActivity(i);
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

}