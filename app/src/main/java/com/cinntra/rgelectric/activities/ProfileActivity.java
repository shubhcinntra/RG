package com.cinntra.rgelectric.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import com.cinntra.rgelectric.R;
import com.cinntra.rgelectric.adapters.ZonesAdapter;
import com.cinntra.rgelectric.databinding.BottomSheetZonesBinding;
import com.cinntra.rgelectric.databinding.ProfilenewPageBinding;
import com.cinntra.rgelectric.fragments.SettingFragment;
import com.cinntra.rgelectric.globals.Globals;
import com.cinntra.rgelectric.globals.MainBaseActivity;
import com.cinntra.rgelectric.model.MapData;
import com.cinntra.rgelectric.model.MapResponse;
import com.cinntra.rgelectric.model.NewLoginData;
import com.cinntra.rgelectric.services.GoogleService;
import com.cinntra.rgelectric.webservices.NewApiClient;
import com.cinntra.roomdb.ItemsDatabase;
import com.cinntra.roomdb.ItemsFilterDatabase;
import com.cinntra.roomdb.ItemsInSalesCardDatabase;
import com.cinntra.roomdb.LedgerGroupDatabase;
import com.cinntra.roomdb.LedgerZoneDatabase;
import com.cinntra.roomdb.PendingOrderDatabase;
import com.cinntra.roomdb.ReceiptDatabase;
import com.cinntra.roomdb.ReceivableDatabase;
import com.cinntra.roomdb.SaleLedgerDatabase;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.pixplicity.easyprefs.library.Prefs;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    ProfilenewPageBinding pageBinding;
    public static String locationtype;
    FusedLocationProviderClient client;
    ItemsDatabase itemsDatabase;
    ItemsFilterDatabase itemsFilterDatabase;
    ItemsInSalesCardDatabase itemsInSalesCardDatabase;
    LedgerGroupDatabase ledgerGroupDatabase;
    LedgerZoneDatabase ledgerZoneDatabase;
    PendingOrderDatabase pendingOrderDatabase;
    ReceiptDatabase receiptDatabase;
    ReceivableDatabase receivableDatabase;
    SaleLedgerDatabase saleLedgerDatabase;
    long thirtyFiveMillis = 30 * 60 * 1000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageBinding = ProfilenewPageBinding.inflate(getLayoutInflater());
        setContentView(pageBinding.getRoot());
        Gson gson = new Gson();
        client = LocationServices.getFusedLocationProviderClient(this);
        long currentTimeMillis = System.currentTimeMillis();
        Prefs.putLong("MilliSeconds",0);

        if(currentTimeMillis-Prefs.getLong("MilliSeconds",0)>=thirtyFiveMillis)
          {
            Prefs.putLong("MilliSeconds",currentTimeMillis);
            lastSync();
          }


        itemsDatabase = ItemsDatabase.getDatabase(this);
        itemsFilterDatabase = ItemsFilterDatabase.getDatabase(this);
        itemsInSalesCardDatabase = ItemsInSalesCardDatabase.getDatabase(this);
        ledgerGroupDatabase = LedgerGroupDatabase.getDatabase(this);
        ledgerZoneDatabase = LedgerZoneDatabase.getDatabase(this);
        pendingOrderDatabase = PendingOrderDatabase.getDatabase(this);
        receiptDatabase = ReceiptDatabase.getDatabase(this);
        receivableDatabase = ReceivableDatabase.getDatabase(this);
        saleLedgerDatabase = SaleLedgerDatabase.getDatabase(this);



        String json = Prefs.getString(Globals.AppUserDetails, "");
        NewLoginData obj = gson.fromJson(json, NewLoginData.class);
        pageBinding.backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        SetData(obj);

        pageBinding.linearZone.setOnClickListener(view -> {
            showBottomSheetDialog();
        });


        pageBinding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openConfirmDialog();
            }
        });

        pageBinding.search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingFragment fragment = new SettingFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.main_page, fragment);
                transaction.addToBackStack("Back");
                transaction.commit();
            }
        });

        pageBinding.mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pageBinding.mode.isChecked()) {
                    Prefs.putString(Globals.locationcondition, "On");
                    locationtype = "Start";
                    //   Toast.makeText(ProfileActivity.this, Prefs.getString(Globals.locationcondition, "Off"), Toast.LENGTH_SHORT).show();
                    if (MainBaseActivity.boolean_permission) {
                        Log.e("start", "start");
                        Intent intent = new Intent(getApplicationContext(), GoogleService.class);
                        getApplicationContext().startService(intent);
                    } else {
                        givepermission(locationtype);
                    }
                } else {
                    Prefs.putString(Globals.locationcondition, "Off");
                    locationtype = "Stop";
                    Intent intent = new Intent(getApplicationContext(), GoogleService.class);
                    getApplicationContext().stopService(intent);
                }
            }
        });

        pageBinding.mode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                }
            }
        });
        if (Prefs.getString(Globals.locationcondition, "Off").equalsIgnoreCase("On")) {
            pageBinding.mode.setChecked(true);
        } else {
            pageBinding.mode.setChecked(false);
        }


    }

    private void SetData(NewLoginData obj) {
        pageBinding.empId.setText("EMP00" + obj.getId());

        pageBinding.phoneValue.setText(obj.getMobile());

        pageBinding.mail.setText(obj.getEmail());

        if (obj.getAddress().isEmpty()){
            pageBinding.productInterest.setText("N/A");
        }else {
            pageBinding.productInterest.setText(obj.getAddress());
        }



        pageBinding.tvDesignation.setText(obj.getRole());

        pageBinding.emailUser.setText(obj.getEmail());

        if (obj.getActive().equalsIgnoreCase("tYES"))
            pageBinding.empTyp.setText("Active");
        else
            pageBinding.empTyp.setText("InActive");
        pageBinding.roleValue.setText(obj.getRole());
        pageBinding.lastlogin.setText("Last Login at " + obj.getLastLoginOn());
        pageBinding.companyName.setText(obj.getSalesEmployeeName());


        //  reporting_to.setText("Reporting to : "+ obj.getReportingName());


    }


    private void openConfirmDialog() {

        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("You Want to Logout!")
                .setConfirmText("Yes!")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        Prefs.clear();
                        itemsDatabase.myDataDao().deleteAll();
                        itemsFilterDatabase.myDataDao().deleteAll();
                        itemsInSalesCardDatabase.myDataDao().deleteAll();
                        ledgerGroupDatabase.myDataDao().deleteAll();
                        ledgerZoneDatabase.myDataDao().deleteAll();
                        pendingOrderDatabase.myDataDao().deleteAll();
                        receiptDatabase.myDataDao().deleteAll();
                        receivableDatabase.myDataDao().deleteAll();
                        saleLedgerDatabase.myDataDao().deleteAll();



                        Intent intent = new Intent(ProfileActivity.this, Login.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                    }
                })

                .show();

    }

    private void callApi(double latitude, double longitude, String type, String address) {
        MapData mapData = new MapData();
        mapData.setEmp_Id(Prefs.getString(Globals.MyID, ""));
        mapData.setEmp_Name(Prefs.getString(Globals.Employee_Name, ""));
        mapData.setLat(String.valueOf(latitude));
        mapData.setLong(String.valueOf(longitude));
        mapData.setUpdateDate(Globals.getTodaysDatervrsfrmt());
        mapData.setUpdateTime(Globals.getTCurrentTime());
        mapData.setAddress(address);
        mapData.setShape("meeting");
        mapData.setType(type);
        mapData.setRemark("");
        mapData.setResourceId("");
        mapData.setSourceType("");
        mapData.setContactPerson("");


        Call<MapResponse> call = NewApiClient.getInstance().getApiService().sendMaplatlong(mapData);
        call.enqueue(new Callback<MapResponse>() {
            @Override
            public void onResponse(Call<MapResponse> call, Response<MapResponse> response) {
                if (response != null) {
                    try {
                        if (response.isSuccessful()) {
                            Log.e("success", "success");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<MapResponse> call, Throwable t) {

            }
        });
    }

    //todo permission for device location...
    private void givepermission(String locationtype) {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE)
                .withListener(new MultiplePermissionsListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            // do you work now
                            getmyCurrentLocation(locationtype);
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            requestPermissions(new String[]{
                                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
                            }, 100);
                            // permission is denied permenantly, navigate user to app settings
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }

                })
                .onSameThread()
                .check();
    }

    //todo getting current location lat and long...
    @SuppressLint("MissingPermission")
    private void getmyCurrentLocation(String type) {
        // Initialize Location manager
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Check condition
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            // When location service is enabled
            // Get last location
            client.getLastLocation().addOnCompleteListener(
                    new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(
                                @NonNull Task<Location> task) {

                            // Initialize location
                            Location location = task.getResult();
                            // Check condition
                            if (location != null) {
                                // When location result is not
                                // null set latitude
                                Geocoder geocoder;
                                List<Address> addresses = null;
                                geocoder = new Geocoder(ProfileActivity.this, Locale.getDefault());
                                try {
                                    addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                                String city = addresses.get(0).getLocality();
                                String state = addresses.get(0).getAdminArea();
                                String country = addresses.get(0).getCountryName();
                                String postalCode = addresses.get(0).getPostalCode();
                                String knownName = addresses.get(0).getFeatureName();
                                callApi(location.getLatitude(), location.getLongitude(), type, address);
                            } else {
                                // When location result is null
                                // initialize location request
                                LocationRequest locationRequest = new LocationRequest()
                                        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                        .setInterval(10000)
                                        .setFastestInterval(
                                                1000)
                                        .setNumUpdates(1);

                                // Initialize location call back
                                LocationCallback
                                        locationCallback
                                        = new LocationCallback() {
                                    @Override
                                    public void
                                    onLocationResult(LocationResult locationResult) {
                                        // Initialize
                                        // location
                                        Location location1
                                                = locationResult
                                                .getLastLocation();

                                        Geocoder geocoder;
                                        List<Address> addresses = null;
                                        geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                                        try {
                                            addresses = geocoder.getFromLocation(location1
                                                    .getLatitude(), location1
                                                    .getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                                        String city = addresses.get(0).getLocality();
                                        String state = addresses.get(0).getAdminArea();
                                        String country = addresses.get(0).getCountryName();
                                        String postalCode = addresses.get(0).getPostalCode();
                                        String knownName = addresses.get(0).getFeatureName();
                                        callApi(location1
                                                .getLatitude(), location1
                                                .getLongitude(), type, address);
                                    }
                                };

                                // Request location updates
                                client.requestLocationUpdates(
                                        locationRequest,
                                        locationCallback,
                                        Looper.myLooper());
                            }
                        }
                    });
        } else {
            // When location service is not enabled
            // open location setting
            startActivity(
                    new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                            .setFlags(
                                    Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }


    private void showBottomSheetDialog() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        final ProgressDialog progressDialog = new ProgressDialog(ProfileActivity.this);
        progressDialog.setMessage("Please wait");


        BottomSheetZonesBinding bindingBottom;
        bindingBottom = BottomSheetZonesBinding.inflate(getLayoutInflater());
        bottomSheetDialog.setContentView(bindingBottom.getRoot());
        String zones_all[] = {"All"};
        String zones[] = Prefs.getString(Globals.ZONE, "").split(",");


        int aLen = zones_all.length;
        int bLen = zones.length;
        String[] result = new String[aLen + bLen];

        System.arraycopy(zones_all, 0, result, 0, aLen);
        System.arraycopy(zones, 0, result, aLen, bLen);


        ZonesAdapter adapter = new ZonesAdapter(this, result);
        bindingBottom.zonesList.setAdapter(adapter);

        bottomSheetDialog.show();
        bindingBottom.headingBottomSheetShareReport.setText(R.string.share_customer_list);
        bindingBottom.ivCloseBottomSheet.setOnClickListener(view -> {
            progressDialog.dismiss();
            bottomSheetDialog.dismiss();
        });
        bottomSheetDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                progressDialog.dismiss();
            }
        });


    }

    private void lastSync()
    {

        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime dateTime45MinutesBefore = currentDateTime.minus(45, ChronoUnit.MINUTES);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        DateTimeFormatter dateformatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

// Format and print the current date and time
        String formattedTime = dateTime45MinutesBefore.format(formatter);
        String formattedDate = dateTime45MinutesBefore.format(dateformatter);

        pageBinding.lastSync.setText(" "+formattedTime+" & "+formattedDate);
        pageBinding.lastUpdate.setText(" 1:00 "+" & "+formattedDate);
    }
}
