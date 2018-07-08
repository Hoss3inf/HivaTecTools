package hivatec.ir.hivatectoolstest.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;

import hivatec.ir.hivatectools.hivaButtonView.HivaButton;
import hivatec.ir.hivatectools.hivaRadioView.HivaRadioView;
import hivatec.ir.hivatectoolstest.R;

public class ButtonActivity extends AppCompatActivity {

    HivaButton hivaButton;
    HivaButton hivaButton2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button);
        hivaButton=findViewById(R.id.hiva_button);

        //hivaButton.setEnabled(false);
        hivaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hivaButton.startLoadingState();

                hivaButton.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        hivaButton.stopLoadingState();
                    }
                }, 3000);

            }
        });


        hivaButton2=findViewById(R.id.hiva_button2);

        //hivaButton.setEnabled(false);
        hivaButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hivaButton2.startLoadingState();

                hivaButton2.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        hivaButton2.stopLoadingState();
                    }
                }, 3000);

            }
        });
    }


}