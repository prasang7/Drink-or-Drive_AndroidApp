package com.iotaconcepts.drinkdrive;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginName extends AppCompatActivity
{
    EditText et_name, et_number;
    Button bt_next;
    TextView bigger, smaller, message;

    SessionManager session;
    String name, number;
    int count;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        init();

        bt_next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                name = et_name.getText().toString();
                number = et_number.getText().toString();

                if (count == 0) {

                    if(name.equals("")) {
                        Toast.makeText(LoginName.this, "Name field cannot be left empty.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        count = 1;
                        et_name.setVisibility(View.INVISIBLE);
                        et_number.setVisibility(View.VISIBLE);
                        bigger.setText("Great!");
                        smaller.setText("Just one more step.\nEnter your friend's contact number.");
                        bt_next.setText("Done");
                        message.setText("This number (of your friend) will get the emergency help SMS showing your location.\nSMS will not be sent until you push the\n\'Text sms to friend\' button.");
                    }
                }
                else if(count == 1) {

                    if (number.equals("")) {
                        Toast.makeText(LoginName.this, "Contact Number field cannot be left empty.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(LoginName.this);

                        // Setting Dialog Title
                        alertDialog.setTitle("Disclaimer. Legal Notice.");

                        // Setting Dialog Message
                        alertDialog.setMessage("Users kindly understand that in no manner we support or endorse alcoholic beverage consumption. This app is intended to avoid drink and drive situation. In no way do we support the use or abuse of alcoholic beverages.\n\nThe results provided by this app are NOT A LEGAL ADVICE. This app gives an assessment of your Blood Alcohol Concentration (B.A.C.) notably based on the inputs you give. Your B.A.C. may be lower or higher than indicated by this app due to various factors such as age, hormonal changes, genes, physical conditions etc. This method is not the best means of determining whether it is safe to drive after consuming alcohol. It is NEVER SAFE to drink and drive. The result generated based on the information given by the user is only for informative and indicative purposes. Therefore, the developer makes no warranties whatsoever nor is responsible for damages of any kind regarding the use of this app or any decision made by the user.\n\nAlcohol can be lethal. Never drink and drive. DRINK RESPONSIBLY AND DRIVE SAFELY.");
                        alertDialog.setNegativeButton("Decline", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Do nothing - cancel dialog
                            }
                        });

                        alertDialog.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                session.createLoginSession(name, number);
                                Toast.makeText(LoginName.this, "Processing ...", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(LoginName.this, MainActivity.class);
                                startActivity(i);
                                finish();
                            }
                        });
                        alertDialog.show();
                    }
                }
            }
        });
    }

    private void init(){

        et_name = (EditText)findViewById(R.id.et_login_name);
        et_number = (EditText)findViewById(R.id.et_login_number);
        bt_next = (Button)findViewById(R.id.bt_login_next);
        bigger = (TextView)findViewById(R.id.tv_welcome);
        smaller = (TextView)findViewById(R.id.tv_tell_us);
        message = (TextView)findViewById(R.id.tv_friends_number_message);

        count = 0;

        // Loading Font Face
        Typeface mont_bold = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Bold.otf");
        Typeface mont_slim = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Hairline.otf");
        Typeface mont_reg = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Regular.otf");

        bigger.setTypeface(mont_reg);
        smaller.setTypeface(mont_reg);
        bt_next.setTypeface(mont_reg);
        et_name.setTypeface(mont_reg);
        et_number.setTypeface(mont_reg);
        message.setTypeface(mont_reg);

        session = new SessionManager(getApplicationContext());
    }

}
