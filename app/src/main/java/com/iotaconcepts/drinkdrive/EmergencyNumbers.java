package com.iotaconcepts.drinkdrive;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class EmergencyNumbers extends Activity
{
    Button n1, n2, n3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emergencynumbers);

        n1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + 911));
                startActivity(callIntent);
            }
        });

        n2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + 112));
                startActivity(callIntent);
            }
        });

        n3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + 119));
                startActivity(callIntent);
            }
        });
    }

    private void init(){
        Typeface mont_reg = Typeface.createFromAsset(this.getAssets(), "fonts/Montserrat-Regular.otf");

        n1 = (Button)findViewById(R.id.bt_number_1);
        n2 = (Button)findViewById(R.id.bt_number_2);
        n3 = (Button)findViewById(R.id.bt_number_3);

        n1.setTypeface(mont_reg);
        n2.setTypeface(mont_reg);
        n3.setTypeface(mont_reg);
    }
}
