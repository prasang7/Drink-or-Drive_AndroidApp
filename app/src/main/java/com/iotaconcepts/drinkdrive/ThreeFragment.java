package com.iotaconcepts.drinkdrive;

import android.content.Intent;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.iotaconcepts.drinkdrive.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ThreeFragment extends Fragment
{
    double lat,lon;
    GPSTracker gps;
    String mAddress,mCity,mState,mCountry,mPostalAddress,mKnownname; String nAddress;
    Button searchRide, searchGoogleBt, emergencyButton;
    View view3;
    TextView note;
    WebView mWebView;

    int a;

    public ThreeFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view3 = inflater.inflate(R.layout.fragment_three, container, false);
        Typeface mont_reg = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Montserrat-Regular.otf");

        searchRide = (Button)view3.findViewById(R.id.bt_ride);
        note = (TextView)view3.findViewById(R.id.tv_note);
        searchGoogleBt = (Button)view3.findViewById(R.id.bt_searchCab);
        emergencyButton = (Button)view3.findViewById(R.id.bt_emergencyButton);

        searchRide.setText("Find cab / taxi in my location.");
        searchRide.setTypeface(mont_reg);
        searchGoogleBt.setTypeface(mont_reg);
        emergencyButton.setTypeface(mont_reg);

        a = 0;

        if(!isInternetOn())
        {
            searchRide.setText("Internet connection not found!");
            a = 1;
        }

        mWebView = (WebView)view3.findViewById(R.id.wv_myWebview);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.getJavaScriptEnabled();
        mWebView.setWebViewClient(new customWebViewClient());

        searchRide.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (a == 0)  // if internet available
                {
                    searchRide.setText("Finding...");
                    Toast.makeText(getActivity(), "Finding your location ...", Toast.LENGTH_SHORT).show();

                    //Location Tracker
                    gps = new GPSTracker(getActivity());

                    // check if GPS enabled
                    if (gps.canGetLocation()) {
                        lat = gps.getLatitude();
                        lon = gps.getLongitude();
                    } else {      // can't get location or GPS or Network is not enabled then Ask user to enable GPS/network in settings
                        gps.showSettingsAlert();
                    }

                    Thread timer = new Thread() {
                        public void run() {
                            try {
                                //Getting address with Geocoder
                                Geocoder geocoder;
                                List<Address> addresses;
                                geocoder = new Geocoder(getActivity().getApplicationContext(), Locale.getDefault());
                                try {
                                    addresses = geocoder.getFromLocation(lat, lon, 1); // Here 1 represents max location result to returned, by documents it recommended 1 to 5
                                    mAddress = addresses.get(0).getAddressLine(1); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                                    nAddress = addresses.get(0).getAddressLine(0);
                                    mCity = addresses.get(0).getLocality();
                                    mState = addresses.get(0).getAdminArea();
                                    mCountry = addresses.get(0).getCountryName();
                                    mPostalAddress = addresses.get(0).getPostalCode();
                                    mKnownname = addresses.get(0).getFeatureName(); // Only if available else return NULL
                                } catch (IOException e) {
                                    //
                                }
                            } finally {

                            }
                        }
                    };
                    timer.start();

                    try {
                        timer.join();
                        Toast.makeText(getActivity(), "Lat " + lat + " Lon " + lon + "\nAddress: " + mAddress + "\nCity: " + mCity + "\nState: " + mState + "\nCountry: " + mCountry + "\nPostal Address: " + mPostalAddress + " " + mKnownname + "\n" + nAddress, Toast.LENGTH_LONG).show();

                        mWebView.loadUrl("https://www.google.co.in/search?q=" + "book taxi or cab online " + nAddress + " " + mCity);

                        mWebView.setVisibility(View.VISIBLE);
                        searchRide.setVisibility(View.INVISIBLE);
                        searchGoogleBt.setVisibility(View.INVISIBLE);
                        note.setVisibility(View.INVISIBLE);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        emergencyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity(), EmergencyNumbers.class);
                startActivity(i);
            }
        });

        // Inflate the layout for this fragment
        return view3;
    }

    public final boolean isInternetOn()
    {
        // get Connectivity Manager object to check connection
        ConnectivityManager connec = (ConnectivityManager)getActivity().getSystemService(getActivity().getBaseContext().CONNECTIVITY_SERVICE);

        // Check for network connections
        if ( connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {

            // if connected with internet

            //Toast.makeText(getActivity(), " Connected ", Toast.LENGTH_LONG).show();
            return true;
        }
        else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED  ) {

            //Toast.makeText(getActivity(), " Not Connected ", Toast.LENGTH_LONG).show();
            return false;
        }
        return false;
    }

    private class customWebViewClient extends WebViewClient
    {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}





