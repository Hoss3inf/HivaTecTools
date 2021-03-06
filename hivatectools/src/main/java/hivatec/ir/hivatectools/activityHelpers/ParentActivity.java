package hivatec.ir.hivatectools.activityHelpers;

import android.app.Activity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import hivatec.ir.hivatectools.R;
import hivatec.ir.hivatectools.helper.FontsOverride;


public abstract class ParentActivity extends AppCompatActivity {

    protected ActionBarView toolbar;
    protected Activity context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        context = this;

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        //set what horizontal_item to show
        setContentViewActivity();

        //if activity has any intent data
        getIntentData();

        //set custom settings for toolbar if needed
        //setToolbar();

        //instantiate and customise toolbar view
        prepareToolbar();

        //get all views in activity content
        instantiateViews();

        //set listeners for what views should do
        setViewListeners();

        FontsOverride.setDefaultFont(this, "DEFAULT", getFontAssetName());
        FontsOverride.setDefaultFont(this, "MONOSPACE", getFontAssetName());
        FontsOverride.setDefaultFont(this, "SERIF", getFontAssetName());
        FontsOverride.setDefaultFont(this, "SANS_SERIF", getFontAssetName());

        //set views content (this makes sure all views are instantiated)
        setActivityContent();


    }

    protected String getFontAssetName(){
        return "IRANSansMobile.ttf";
    }

    protected void setToolbar() {

        toolbar = findViewById(R.id.toolbar);

        if(toolbar == null){
            return;
        }

        this.setSupportActionBar(toolbar.getToolbar());

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        toolbar.getToolbar().setContentInsetsAbsolute(0, 0);


        toolbar.leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    /**
     * dont use setContentView in onCreate
     * use this method instead
     */
    protected abstract void setContentViewActivity();

    protected abstract void getIntentData();

    protected abstract void prepareToolbar();

    protected abstract void instantiateViews();

    protected abstract void setViewListeners();

    protected abstract void setActivityContent();

    protected abstract void refreshContent();


    //*********
    //easier stuff
    //********
    protected void showToast(String str){
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }

    protected void showToastLong(String str){
        Toast.makeText(context, str, Toast.LENGTH_LONG).show();
    }


    //*******************
    //get resources easier
    //*******************
    protected int getColorRes(int color){
        return getResources().getColor(color);
    }


}