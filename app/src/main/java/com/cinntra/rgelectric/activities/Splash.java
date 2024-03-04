package com.cinntra.rgelectric.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.cinntra.rgelectric.R;
import com.cinntra.rgelectric.globals.Globals;
import com.cinntra.rgelectric.model.LogInDetail;
import com.cinntra.rgelectric.model.NewLogINResponse;
import com.cinntra.rgelectric.webservices.NewApiClient;
import com.google.gson.Gson;
import com.pixplicity.easyprefs.library.Prefs;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Splash extends AppCompatActivity {

    private boolean isFirstAnimation = false;

    @BindView(R.id.header_icon)
    ImageView ivLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        Animation hold = AnimationUtils.loadAnimation(this, R.anim.hold);

        final Animation translateScale = AnimationUtils.loadAnimation(this, R.anim.translate_scale);
        final ImageView imageView = findViewById(R.id.header_icon);
        Glide.with(this)
                .load(R.drawable.ledger_icon) // Replace with your GIF resource
                // Replace with a placeholder image
                .into(imageView);



        Handler handler = new Handler();

        // Define the delay (5 seconds = 5000 milliseconds)
        int delayMillis = 2000;

        // Post a delayed action to start the new activity
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Prefs.getString(Globals.USERNAME, "").isEmpty() && Prefs.getString(Globals.USER_PASSWORD, "").isEmpty())
                    AutoLogIn = false;
                callLogInApi(Prefs.getString(Globals.USERNAME, ""), Prefs.getString(Globals.USER_PASSWORD, ""));

            }
        }, delayMillis);


    }


    private void callLogInApi(String username, String password) {

        LogInDetail logInDetail = new LogInDetail();
        logInDetail.setUserName(username);
        logInDetail.setPassword(password);
        logInDetail.setFcm("");
        Prefs.putString(Globals.USER_PASSWORD, password);

        Call<NewLogINResponse> call = NewApiClient.getInstance().getApiService().loginEmployee(logInDetail);
        call.enqueue(new Callback<NewLogINResponse>() {
            @Override
            public void onResponse(Call<NewLogINResponse> call, Response<NewLogINResponse> response) {

                if (response.body().getStatus() == 200) {


                    Gson gson = new Gson();
                    String json = gson.toJson(response.body().getLogInDetail());
                    Prefs.putString(Globals.AppUserDetails, json);
                    // Globals.APILog = "APILog";
                    Globals.TeamSalesEmployeCode = response.body().getLogInDetail().getSalesEmployeeCode();
                    Globals.TeamRole = response.body().getLogInDetail().getRole();
                    Globals.TeamEmployeeID = String.valueOf(response.body().getLogInDetail().getId());
                    Globals.SelectedDB = String.valueOf(response.body().getSap().getCompanyDB());

                    Prefs.putString(Globals.USER_PASSWORD, response.body().getLogInDetail().getPassword());
                    Prefs.putString(Globals.Employee_Name, response.body().getLogInDetail().getUserName());
                    Prefs.putString(Globals.CHECK_IN_STATUS, response.body().getLogInDetail().getCheckInStatus());
                    Prefs.putString(Globals.EmployeeID, String.valueOf(response.body().getLogInDetail().getId()));
                    Prefs.putString(Globals.SalesEmployeeCode, String.valueOf(response.body().getLogInDetail().getSalesEmployeeCode()));
                    Prefs.putString(Globals.SalesEmployeeName, String.valueOf(response.body().getLogInDetail().getSalesEmployeeName()));
                    Prefs.putString(Globals.SelectedDB, String.valueOf(response.body().getSap().getCompanyDB()));
                    Prefs.putString(Globals.Role, String.valueOf(response.body().getLogInDetail().getRole()));
                    Prefs.putString(Globals.MyID, String.valueOf(response.body().getLogInDetail().getId()));
                    Prefs.putString(Globals.BranchId, String.valueOf(response.body().getLogInDetail().getBranch()));
                    Prefs.putString(Globals.ZONE, String.valueOf(response.body().getLogInDetail().getZone()));

                    if (response.body().getTripExpenses().size() > 0) {
                        Prefs.putString(Globals.BP_TYPE_CHECK_IN, response.body().getTripExpenses().get(0).getBPType());
                        Prefs.putString(Globals.BP_NAME_CHECK_IN, response.body().getTripExpenses().get(0).getBPName());
                        Prefs.putDouble(Globals.START_LAT, Double.parseDouble(response.body().getTripExpenses().get(0).getCheckInLat()));
                        Prefs.putDouble(Globals.START_LONG, Double.parseDouble(response.body().getTripExpenses().get(0).getCheckInLong()));
                        Prefs.putString(Globals.START_DATE, response.body().getTripExpenses().get(0).getCheckInDate());
                        Prefs.putString(Globals.MODE_OF_TRANSPORT, response.body().getTripExpenses().get(0).getModeOfTransport());
                        Prefs.putString(Globals.TRIP_ID, response.body().getTripExpenses().get(0).getId());
                    } else {

                    }

                    //Prefs.putString(Globals.MYEmployeeID, String.valueOf(response.body().getLogInDetail().getId()));

                    long session = Long.parseLong("30");
                    session = session * 60 * 1000;

                    Prefs.putLong(Globals.SESSION_TIMEOUT, session);
                    Prefs.putLong(Globals.SESSION_REMAIN_TIME, 0);
                    AutoLogIn = true;
                    gotoHome();
                } else {


                }
            }

            @Override
            public void onFailure(Call<NewLogINResponse> call, Throwable t) {
                AutoLogIn = false;
                gotoHome();
            }
        });
    }

    private boolean AutoLogIn = false;

    private void gotoHome() {


        if (!AutoLogIn) {
            Intent intent = new Intent(Splash.this, Login.class);
            startActivity(intent);
            finish();


        } else {
            Intent i = new Intent(Splash.this, MainActivity_B2C.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        }

    }
}