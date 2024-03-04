package com.cinntra.rgelectric.fragments;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cinntra.rgelectric.R;
import com.cinntra.rgelectric.activities.AddCampaign;
import com.cinntra.rgelectric.activities.AddContact;
import com.cinntra.rgelectric.activities.AddLead;
import com.cinntra.rgelectric.activities.AddOpportunityActivity;
import com.cinntra.rgelectric.activities.AddOrderAct;
import com.cinntra.rgelectric.activities.AddQuotationAct;
import com.cinntra.rgelectric.activities.PendingOrders;
import com.cinntra.rgelectric.activities.ProfileActivity;
import com.cinntra.rgelectric.activities.Reports;
import com.cinntra.rgelectric.activities.Sale_Inovice_Reports;
import com.cinntra.rgelectric.adapters.ContactPersonAdapter;
import com.cinntra.rgelectric.adapters.LeadDropDownAdapter;
import com.cinntra.rgelectric.adapters.RecentAdapter;
import com.cinntra.rgelectric.adapters.RecentOrderAdapter;
import com.cinntra.rgelectric.adapters.YoutubeLisAdapter;
import com.cinntra.rgelectric.databinding.BottomSheetDialogSelectDateBinding;
import com.cinntra.rgelectric.databinding.CheckOutExpenseDialogBinding;
import com.cinntra.rgelectric.databinding.CheckinDialogBinding;
import com.cinntra.rgelectric.databinding.FragmentDashboardFromActivityBinding;
import com.cinntra.rgelectric.globals.Globals;
import com.cinntra.rgelectric.globals.MainBaseActivity;
import com.cinntra.rgelectric.model.ContactPerson;
import com.cinntra.rgelectric.model.ContactPersonData;
import com.cinntra.rgelectric.model.CounterResponse;
import com.cinntra.rgelectric.model.CountryResponse;
import com.cinntra.rgelectric.model.DashboardCounterData;
import com.cinntra.rgelectric.model.DashboardCounterResponse;
import com.cinntra.rgelectric.model.EventResponse;
import com.cinntra.rgelectric.model.EventValue;
import com.cinntra.rgelectric.model.GraphModel;
import com.cinntra.rgelectric.model.MapData;
import com.cinntra.rgelectric.model.MapResponse;
import com.cinntra.rgelectric.model.QuotationItem;
import com.cinntra.rgelectric.model.QuotationResponse;
import com.cinntra.rgelectric.model.SalesEmployeeItem;
import com.cinntra.rgelectric.model.SalesGraphResponse;
import com.cinntra.rgelectric.newapimodel.LeadResponse;
import com.cinntra.rgelectric.newapimodel.LeadValue;
import com.cinntra.rgelectric.newapimodel.ResponsePayMentDueCounter;
import com.cinntra.rgelectric.newapimodel.ResponseReceivableGraph;
import com.cinntra.rgelectric.viewpager.GraphPagerPurchaseAdapter;
import com.cinntra.rgelectric.webservices.NewApiClient;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.pixplicity.easyprefs.library.Prefs;
import com.skydoves.powermenu.MenuAnimation;
import com.skydoves.powermenu.OnMenuItemClickListener;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PurchaseDashBoardFragment extends Fragment {


    FragmentDashboardFromActivityBinding binding;

    AlertDialog.Builder builder;
    AlertDialog alertDialog;
    private static final int PICK_FILE_REQUEST_CODE = 11111;
    private static final int PICK_FILE_CHECKOUT_REQUEST_CODE = 14321;
    private static final int REQUEST_IMAGE_CAPTURE = 10111;

    String locationtype;
    LocationManager locationManager;
    double latitude;
    double longitude;
    LeadDropDownAdapter leadDropDownAdapter;
    FusedLocationProviderClient client;


    RecentAdapter adapter;
    YoutubeLisAdapter youtubeadapter;
    String dateSelectorValue = "";

    BarChart volumeReportChart;
    ArrayList<String> youtubelist = new ArrayList<>();
    ArrayList<LeadValue> leadValueList = new ArrayList<>();
    String bpName = "";
    String bpId = "";
    private String picturePath = "";
    private String checkOUtPicturePath = "";
    private String cameraImagePath = "";
    private Uri cameraCaptureUri;

    CheckinDialogBinding checkinDialogBinding;


    Context context;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Access the Context here
        context = getContext();
    }


    private void callDashboardCounter_Receiable() {
        alertDialog.show();


        HashMap obj = new HashMap<String, String>();
        obj.put("Filter", "");
        obj.put("Code", "");
        obj.put("Type", "Gross");
        obj.put("FromDate", startDate);
        obj.put("ToDate", endDate);
        obj.put("SearchText", "");
        obj.put("DueDaysGroup", "");
        obj.put("SalesPersonCode", Prefs.getString(Globals.SalesEmployeeCode, ""));
        Call<DashboardCounterResponse> call = NewApiClient.getInstance().getApiService().getDashBoardCounterForLedger_purchase(obj);
        call.enqueue(new Callback<DashboardCounterResponse>() {
            @Override
            public void onResponse(Call<DashboardCounterResponse> call, Response<DashboardCounterResponse> response) {
                if (response.code() == 200) {
                    alertDialog.dismiss();
                    if (response.body().getData().size() > 0) {
                        Prefs.putString(Globals.Total_Receivables, Globals.numberToK(String.valueOf(response.body().getData().get(0).getDifferenceAmount())));
                        binding.contentData.totalCollection.setText(context.getResources().getString(R.string.Rs) + " " + Globals.numberToK(String.valueOf(response.body().getData().get(0).getDifferenceAmount())));
                        binding.contentData.totalPendings.setText(context.getResources().getString(R.string.Rs) + " " + Globals.numberToK(String.valueOf(response.body().getData().get(0).getTotalPendingSales())));
                    }

                } else {
                    alertDialog.dismiss();

                    Gson gson = new GsonBuilder().create();
                    LeadResponse mError = new LeadResponse();
                    try {
                        String s = response.errorBody().string();
                        mError = gson.fromJson(s, LeadResponse.class);
                    } catch (IOException e) {
                        //handle failure to read error
                    }
                    //Toast.makeText(CreateContact.this, msz, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<DashboardCounterResponse> call, Throwable t) {
                alertDialog.dismiss();

                Toast.makeText(requireContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        //callBplistApi(bp_spinner, cp_spinner);
    }

    private void callDashboardCounter() {
        binding.contentData.tvDateText.setText("" + Globals.convertDateFormatInReadableFormat(startDate) + " to " + Globals.convertDateFormatInReadableFormat(endDate));
        alertDialog.show();

        HashMap obj = new HashMap<String, String>();
        obj.put("Filter", "");
        obj.put("Code", "");
        obj.put("Type", "Gross");
        obj.put("FromDate", startDate);
        obj.put("ToDate", endDate);
        obj.put("DueDaysGroup", "");
        obj.put("SearchText", "");
        obj.put("SalesPersonCode", Prefs.getString(Globals.SalesEmployeeCode, ""));

        Prefs.putString(Globals.FROM_DATE, startDate);
        Prefs.putString(Globals.TO_DATE, endDate);
        Call<DashboardCounterResponse> call = NewApiClient.getInstance().getApiService().getDashBoardCounterForLedger_purchase(obj);
        call.enqueue(new Callback<DashboardCounterResponse>() {
            @Override
            public void onResponse(Call<DashboardCounterResponse> call, Response<DashboardCounterResponse> response) {
                if (response.code() == 200) {
                    alertDialog.dismiss();
                    if (response.body().getData().size() > 0) {
                        setCounter(response.body().getData().get(0));
                    }


                } else {
                    alertDialog.dismiss();

                    Gson gson = new GsonBuilder().create();
                    LeadResponse mError = new LeadResponse();
                    try {
                        String s = response.errorBody().string();
                        mError = gson.fromJson(s, LeadResponse.class);
                    } catch (IOException e) {
                        //handle failure to read error
                    }

                }

            }

            @Override
            public void onFailure(Call<DashboardCounterResponse> call, Throwable t) {
                alertDialog.dismiss();

                Toast.makeText(requireContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }


    private void setCounter(DashboardCounterData data) {
        binding.contentData.totalAmnt.setText(context.getResources().getString(R.string.Rs) + " " + Globals.numberToK(String.valueOf(data.getTotalSales())));
        binding.contentData.receivedAmountValue.setText(context.getResources().getString(R.string.Rs) + " " + Globals.numberToK(String.valueOf(data.getTotalReceivePayment())));
        //binding.contentData.totalCollection.setText("Rs." + Globals.numberToK(String.valueOf(data.getDifferenceAmount())));
    }

    Long startDatelng = (long) 0.0;
    Long endDatelng = (long) 0.0;
    MaterialDatePicker<Pair<Long, Long>> materialDatePicker;

    private void showDateInDashboardBottomSheetDialog(Context context) {
        BottomSheetDialogSelectDateBinding bindingDate;
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetDialogTheme);
        bindingDate = BottomSheetDialogSelectDateBinding.inflate(getLayoutInflater());
        bottomSheetDialog.setContentView(bindingDate.getRoot());

        bindingDate.ivCloseBottomSheet.setOnClickListener(view -> {
            bottomSheetDialog.dismiss();
        });
        bindingDate.tvCustomDateBottomSheetSelectDate.setOnClickListener(view -> {

            bottomSheetDialog.dismiss();
            dateRangeSelector();

        });
        bindingDate.tvTodayDateBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Calendar.getInstance().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.Date_yyyy_mm_dd(startDatelng);
            endDate = Globals.Date_yyyy_mm_dd(endDatelng);
            alertDialog.show();
            binding.contentData.tvDateText.setText("Today");
            callDashboardCounter();
            callDashboardCounter_Receiable();


            bottomSheetDialog.dismiss();
        });

        bindingDate.tvYesterdayDateBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.yesterDayCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.Date_yyyy_mm_dd(startDatelng);
            endDate = Globals.Date_yyyy_mm_dd(endDatelng);
            alertDialog.show();
            binding.contentData.tvDateText.setText("Yesterday");
            callDashboardCounter();
            callDashboardCounter_Receiable();

            bottomSheetDialog.dismiss();
        });
        bindingDate.tvThisWeekDateBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.thisWeekCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.thisWeekfirstDayOfMonth();
            endDate = Globals.thisWeekLastDayOfMonth();
            alertDialog.show();
            binding.contentData.tvDateText.setText("This Week");
            callDashboardCounter();
            callDashboardCounter_Receiable();

            bottomSheetDialog.dismiss();
        });


        bindingDate.tvThisMonthBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.thisMonthCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.firstDateOfMonth();
            endDate = Globals.lastDateOfMonth();
            alertDialog.show();
            binding.contentData.tvDateText.setText("This Month");
            callDashboardCounter();
            callDashboardCounter_Receiable();
            bottomSheetDialog.dismiss();

        });
        bindingDate.tvLastMonthDateBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.lastMonthCal().getTimeInMillis();
            endDatelng = Globals.thisMonthCal().getTimeInMillis();
            startDate = Globals.lastMonthFirstDate();
            endDate = Globals.lastMonthlastDate();
            alertDialog.show();
            binding.contentData.tvDateText.setText("" + Globals.convertDateFormatInReadableFormat(startDate) + " to " + Globals.convertDateFormatInReadableFormat(endDate));
            callDashboardCounter();
            callDashboardCounter_Receiable();

            bottomSheetDialog.dismiss();
        });
        bindingDate.tvThisQuarterDateBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.thisQuarterCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.lastQuarterFirstDate();
            endDate = Globals.lastQuarterlastDate();
            alertDialog.show();
            binding.contentData.tvDateText.setText("" + Globals.convertDateFormatInReadableFormat(startDate) + " to " + Globals.convertDateFormatInReadableFormat(endDate));
            callDashboardCounter();
            callDashboardCounter_Receiable();

            bottomSheetDialog.dismiss();
        });
        bindingDate.tvThisYearDateBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.thisyearCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.firstDateOfFinancialYear();
            endDate = Globals.lastDateOfFinancialYear();
            alertDialog.show();
            binding.contentData.tvDateText.setText("" + Globals.convertDateFormatInReadableFormat(startDate) + " to " + Globals.convertDateFormatInReadableFormat(endDate));
            callDashboardCounter();
            callDashboardCounter_Receiable();

            bottomSheetDialog.dismiss();
        });
        bindingDate.tvLastYearBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.lastyearCal().getTimeInMillis();
            endDatelng = Globals.thisyearCal().getTimeInMillis();
            startDate = Globals.lastYearFirstDate();
            endDate = Globals.lastYearLastDate();
            alertDialog.show();
            binding.contentData.tvDateText.setText("" + Globals.convertDateFormatInReadableFormat(startDate) + " to " + Globals.convertDateFormatInReadableFormat(endDate));
            callDashboardCounter();
            callDashboardCounter_Receiable();

            bottomSheetDialog.dismiss();
        });
        bindingDate.tvAllBottomSheetSelectDate.setOnClickListener(view -> {
            startDate = "";
            endDate = "";
            alertDialog.show();
            binding.contentData.tvDateText.setText("All");
            callDashboardCounter();
            callDashboardCounter_Receiable();

            bottomSheetDialog.dismiss();
        });


        bottomSheetDialog.show();

    }

    private void dateRangeSelector() {


        if (startDatelng == 0.0) {
            materialDatePicker = MaterialDatePicker.Builder.dateRangePicker().setSelection(Pair.create(MaterialDatePicker.thisMonthInUtcMilliseconds(), MaterialDatePicker.todayInUtcMilliseconds())).build();
        } else {
            materialDatePicker = MaterialDatePicker.Builder.dateRangePicker().setSelection(Pair.create(startDatelng, endDatelng)).build();

        }

        materialDatePicker.show(getChildFragmentManager(), "Tag_Picker");

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
            @Override
            public void onPositiveButtonClick(Pair<Long, Long> selection) {

                // binding.loader.setVisibility(View.VISIBLE);
                startDatelng = selection.first;
                endDatelng = selection.second;
                startDate = Globals.Date_yyyy_mm_dd(startDatelng);
                endDate = Globals.Date_yyyy_mm_dd(endDatelng);
                alertDialog.show();
                binding.contentData.tvDateText.setText("" + Globals.convertDateFormatInReadableFormat(startDate) + " to " + Globals.convertDateFormatInReadableFormat(endDate));
                callDashboardCounter();
                callDashboardCounter_Receiable();


            }
        });


    }


    // GraphViewPagerAdapter fakeLiveMatchAdapter;
    GraphPagerPurchaseAdapter graphPagerAdapter;
    public static View dateSPinner;
    ArrayAdapter<CharSequence> dateSpinnerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

 /*   @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Objects.requireNonNull(((MainActivity_B2C) requireActivity()).getSupportActionBar()).hide();
        getActivity().findViewById(R.id.app_bar).setVisibility(View.GONE);
        getActivity().findViewById(R.id.date_selector).setVisibility(View.GONE);
        MainActivity_B2C.dateSPinner.setVisibility(View.GONE);
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_dashboard_from_activity, container, false);
    }

    private static final String TAG = "DashboardFragmentFromAc";

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentDashboardFromActivityBinding.bind(view);
        String reverse = Globals.convertDateFormat("2023-03-01");
        Log.e("REVERSE", "onCreate: " + reverse);
        Globals.CURRENT_CLASS = getClass().getName();
        dateSPinner = binding.contentData.dateSelector;
        binding.contentData.totalPendings.setText("NA");
        leadDropDownAdapter = new LeadDropDownAdapter(requireContext(), leadValueList);
        client = LocationServices.getFusedLocationProviderClient(requireContext());
        Toolbar toolbar = binding.toolbar;
        // setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = binding.toolbarLayout;
        // toolBarLayout.setTitle(getTitle());
        binding.contentData.loader.loader.setVisibility(View.GONE);
        binding.appBar.setVisibility(View.VISIBLE);
        binding.lineartopToolbar.setVisibility(View.GONE);
        binding.mainTitle.setText("Purchase");
        binding.contentData.topHeadingGraph.setText("Purchase");
        //  FloatingActionButton fab = binding.fab;
        volumeReportChart = binding.contentData.anyChartView;
        binding.contentData.topHeadingGraph.setOnClickListener(view1 -> {

        });

        callPaymentDueCounter();
        callAllDueCounter();
        saleGraphApi();


        binding.contentData.paymentCollectionCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Prefs.putString("ForReports", "Receivable");
                startActivity(new Intent(requireContext(), Reports.class));

            }
        });


        binding.contentData.newtypeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "onClick: Card");
                Prefs.putString("ForReports", "payment");
                startActivity(new Intent(requireActivity(), Reports.class));

            }
        });


        // Create an ArrayAdapter using a custom layout for the dropdown items
        dateSpinnerAdapter = ArrayAdapter.createFromResource(requireContext(), R.array.date_selector, // Replace with your item array resource
                R.layout.spinner_textview_dashboard);

// Set the dropdown view resource
        dateSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

// Set the adapter for the Spinner
        binding.contentData.dateSelector.setAdapter(dateSpinnerAdapter);

        // binding.contentData.totalPendings.setText(getResources().getString(R.string.Rs) + " " + Globals.numberToK(String.valueOf("87625341")));


        builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Loading....").setMessage("Please Wait").setCancelable(false);


        alertDialog = builder.create();

        binding.contentData.ivGraphshubh.setOnClickListener(view1 -> {
            Log.e("TAG", "onViewCreated=======================: ");
            Toast.makeText(requireContext(), "click", Toast.LENGTH_SHORT).show();

        });


        if (Prefs.getString(Globals.CHECK_IN_STATUS, "").equalsIgnoreCase("Start")) {

            MainBaseActivity.boolean_permission = true;
        }


        callDashboardCounter();
        callDashboardCounter_Receiable();
        //binding.contentData.headingDue.setText("shubh");
        //callAllDueCounter();
        binding.contentData.headingSale.setText("Purchase");
        binding.contentData.headingReciept.setText("Payment");
        binding.contentData.headingCollection.setText("Payable");
        binding.contentData.headingPendings.setText("Pending");
        binding.contentData.overDuecard.setVisibility(View.VISIBLE);

        //callPaymentDueCounter();
        binding.contentData.newtypeCard.setVisibility(View.VISIBLE);

      //  setupChartViewPgaer();

        binding.contentData.linearDate.setOnClickListener(bind ->
        {
            showDateInDashboardBottomSheetDialog(requireContext());
        });
        if (Prefs.getString(Globals.CHECK_IN_STATUS, "").equalsIgnoreCase("Start")) {
            binding.toggleCheckIN.setChecked(true);
            binding.headingcheckIn.setText("Check Out");
            binding.slideView.setText(" Check Out");
            MainBaseActivity.boolean_permission = true;
        } else {
            binding.toggleCheckIN.setChecked(false);
            binding.headingcheckIn.setText("Check In");
            binding.slideView.setText(" Check In");
        }

        if (Prefs.getString(Globals.EXPENSE_STATUS, "").equalsIgnoreCase("Start")) {
            binding.slideExpense.setText(" Start");
        } else {
            binding.slideExpense.setText(" Stop");
        }
        binding.datePicker.setVisibility(View.GONE);

        binding.contentData.pendingOrderCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Prefs.getString(Globals.Role, "").trim().equalsIgnoreCase("admin") || Prefs.getString(Globals.Role, "").trim().equalsIgnoreCase("Director") || Prefs.getString(Globals.Role, "").trim().equalsIgnoreCase("Accounts")) {
                    startActivity(new Intent(requireContext(), PendingOrders.class));
                } else {

                    Toast.makeText(getActivity(), "Under Maintenance", Toast.LENGTH_SHORT).show();
                }

            }
        });
        binding.contentData.receivedCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Prefs.putString("ForReports", "ReceiptLedger");
                startActivity(new Intent(requireContext(), Reports.class));

            }
        });
        binding.contentData.salesAmountCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Prefs.putString("ForReports", "MainActivity_B2C_Ledger");
                startActivity(new Intent(requireContext(), Sale_Inovice_Reports.class));

            }
        });
        binding.contentData.overDuecard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Prefs.putString("ForReports", "overDue");
                startActivity(new Intent(requireContext(), Reports.class));

            }
        });


/*        binding.contentData.headingDue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Prefs.putString("ForReports", "Due");
//                startActivity(new Intent(requireContext(), Reports.class));

            }
        });*/

        binding.contentData.dateSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                dateSelectorValue = binding.contentData.dateSelector.getSelectedItem().toString().trim();
                //selectDate(dateSelectorValue);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        dashboardcounter();

        callCountryApi();

        binding.proImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(requireContext(), ProfileActivity.class);
                startActivity(i);
            }
        });


        youtubeadapter = new YoutubeLisAdapter(requireContext(), youtubelist);

        youtubeadapter.notifyDataSetChanged();

        binding.quickCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openpopup();
                //  startActivity(new Intent(MainActivity_B2C.this,Reports.class));
            }
        });


        // saleGraphApi();

    }


    private void callAllDueCounter() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("SalesPersonCode", Prefs.getString(Globals.SalesEmployeeCode, ""));
        jsonObject.addProperty("DueDaysGroup", "-1");

        Call<ResponsePayMentDueCounter> call = NewApiClient.getInstance().getApiService().getPaymentDueCounterPurchase(jsonObject);
        call.enqueue(new Callback<ResponsePayMentDueCounter>() {
            @Override
            public void onResponse(Call<ResponsePayMentDueCounter> call, Response<ResponsePayMentDueCounter> response) {
                if (response.code() == 200) {
                    //  Toast.makeText(requireContext(), "success", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                    if (response.body().getStatus() == 200) {
                        //  Toast.makeText(requireContext(), "success 200", Toast.LENGTH_SHORT).show();
                        binding.contentData.tvOverDueCounter.setText(getResources().getString(R.string.Rs) + " " + Globals.numberToK(String.valueOf(response.body().getTotalPaybal())));

                    } else {
                        //  Toast.makeText(requireContext(), "Failure", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    alertDialog.dismiss();


                }

            }

            @Override
            public void onFailure(Call<ResponsePayMentDueCounter> call, Throwable t) {
                alertDialog.dismiss();

                Toast.makeText(requireContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void callPaymentDueCounter() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("SalesPersonCode", Prefs.getString(Globals.SalesEmployeeCode, ""));
        jsonObject.addProperty("DueDaysGroup", "7");

        Call<ResponsePayMentDueCounter> call = NewApiClient.getInstance().getApiService().getPaymentDueCounterPurchase(jsonObject);
        call.enqueue(new Callback<ResponsePayMentDueCounter>() {
            @Override
            public void onResponse(Call<ResponsePayMentDueCounter> call, Response<ResponsePayMentDueCounter> response) {
                if (response.code() == 200) {
                    //  Toast.makeText(requireContext(), "success", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                    if (response.body().getStatus() == 200) {
                        //  Toast.makeText(requireContext(), "success 200", Toast.LENGTH_SHORT).show();
                        binding.contentData.tvAnjaliDuePayment.setText(getResources().getString(R.string.Rs) + " " + Globals.numberToK(String.valueOf(response.body().getTotalPaybal())));

                    } else {
                        //  Toast.makeText(requireContext(), "Failure", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    alertDialog.dismiss();


                }

            }

            @Override
            public void onFailure(Call<ResponsePayMentDueCounter> call, Throwable t) {
                alertDialog.dismiss();

                try {
                    Toast.makeText(requireContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {

                }
            }
        });
    }

    private void setupChartViewPgaer() {
        graphPagerAdapter = new GraphPagerPurchaseAdapter(requireContext());
        binding.contentData.viewPagerChart.setAdapter(graphPagerAdapter);
        binding.contentData.tabLayout.setupWithViewPager(binding.contentData.viewPagerChart, true);
        binding.contentData.tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    binding.contentData.topHeadingGraph.setText("Purchase");
                    binding.mainTitle.setText("Purchase");
                } else if (tab.getPosition() == 1) {
                    binding.contentData.topHeadingGraph.setText("Payment");
                    binding.mainTitle.setText("Payment");
                } else if (tab.getPosition() == 2) {
                    binding.contentData.topHeadingGraph.setText("Payables");
                    binding.mainTitle.setText("Payables");
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }


    String attchmentName = "";
    String attachentUri = "";
    String attachmentcheckOut = "";
    EditText attachmentName;
    EditText etNewAttachemnt;


    CheckOutExpenseDialogBinding checkOutExpenseDialogBinding;

    private ArrayList<ContactPersonData> ContactEmployeesList = new ArrayList<>();

    private void callcontactpersonApi(Spinner cp_spinner, String cardCode) {
        ContactPersonData contactPersonData = new ContactPersonData();
        contactPersonData.setCardCode(cardCode);
        binding.contentData.loader.loader.setVisibility(View.VISIBLE);
        Call<ContactPerson> call = NewApiClient.getInstance().getApiService().contactemplist(contactPersonData);
        call.enqueue(new Callback<ContactPerson>() {
            @Override
            public void onResponse(Call<ContactPerson> call, Response<ContactPerson> response) {
                binding.contentData.loader.loader.setVisibility(View.GONE);
                if (response.code() == 200) {
                    if (response.body().getData().size() > 0) {
                        ContactEmployeesList.clear();
                        ContactEmployeesList.addAll(response.body().getData());
                        cp_spinner.setAdapter(new ContactPersonAdapter(requireContext(), ContactEmployeesList));
                    }


                } else {
                    //Globals.ErrorMessage(CreateContact.this,response.errorBody().toString());
                    Gson gson = new GsonBuilder().create();
                    QuotationResponse mError = new QuotationResponse();
                    try {
                        String s = response.errorBody().string();
                        mError = gson.fromJson(s, QuotationResponse.class);
                        Toast.makeText(requireContext(), mError.getError().getMessage().getValue(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        //handle failure to read error
                    }
                    //Toast.makeText(CreateContact.this, msz, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ContactPerson> call, Throwable t) {
                binding.contentData.loader.loader.setVisibility(View.GONE);
                Toast.makeText(requireContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    String startDate = Globals.firstDateOfFinancialYear();
    String endDate = Globals.lastDateOfFinancialYear();


    public static final double AVERAGE_RADIUS_OF_EARTH_KM = 6371;

    public static double distance(double startLat, double startLong, double endLat, double endLong) {

        double latDistance = Math.toRadians(endLat - startLat);
        double lonDistance = Math.toRadians(endLong - startLong);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + Math.cos(Math.toRadians(startLat)) * Math.cos(Math.toRadians(endLat)) * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return AVERAGE_RADIUS_OF_EARTH_KM * c;
    }

    private void opengraph() {

        List<String> dates = new ArrayList<>();
        dates.add("2020-01-25");
        dates.add("2020-02-25");
        dates.add("2020-03-25");
        dates.add("2020-04-25");
        dates.add("2020-05-25");
        dates.add("2020-06-25");
        dates.add("2020-07-25");
        dates.add("2020-08-25");
        dates.add("2020-09-25");
        dates.add("2020-10-25");
        dates.add("2020-11-25");
        dates.add("2020-12-25");
        List<Double> allAmounts = new ArrayList<>();
        allAmounts.add(1.2);
        allAmounts.add(2.9);
        allAmounts.add(3.4);
        allAmounts.add(2.2);
        allAmounts.add(1.5);
        allAmounts.add(3.8);
        allAmounts.add(4.2);
        allAmounts.add(5.7);
        allAmounts.add(4.5);
        allAmounts.add(2.2);
        allAmounts.add(1.1);
        allAmounts.add(0.9);

        binding.contentData.anyChartView.setTouchEnabled(true);
        binding.contentData.anyChartView.setPinchZoom(true);
        XAxis xAxis = volumeReportChart.getXAxis();
        XAxis.XAxisPosition position = XAxis.XAxisPosition.BOTTOM;
        xAxis.setPosition(position);
        //  xAxis.setValueFormatter(new MainActivity_B2C.ClaimsXAxisValueFormatter(dates));
        renderData(dates, allAmounts);


    }

    public void renderData(List<String> dates, List<Double> allAmounts) {

        final ArrayList<String> xAxisLabel = new ArrayList<>();
        xAxisLabel.add("1");
        xAxisLabel.add("7");
        xAxisLabel.add("14");
        xAxisLabel.add("21");
        xAxisLabel.add("28");
        xAxisLabel.add("30");

        Log.e("Test=====>", "SaleGraph");

        XAxis xAxis = volumeReportChart.getXAxis();
        XAxis.XAxisPosition position = XAxis.XAxisPosition.BOTTOM;
        xAxis.setPosition(position);
        //  xAxis.enableGridDashedLine(2f, 7f, 0f);
        xAxis.setAxisMaximum(13f);
        xAxis.setAxisMinimum(0f);
        xAxis.setTextColor(context.getResources().getColor(R.color.white));
        xAxis.setLabelCount(12, true);
        xAxis.setGranularityEnabled(true);
        xAxis.setGranularity(7f);
        xAxis.setLabelRotationAngle(315f);

        //  xAxis.setValueFormatter(new MainActivity_B2C.ClaimsXAxisValueFormatter(dates));

        xAxis.setCenterAxisLabels(true);


        xAxis.setDrawLimitLinesBehindData(false);


        LimitLine ll2 = new LimitLine(35f, "");
        ll2.setLineWidth(4f);
//        ll2.enableDashedLine(10f, 10f, 0f);
        ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        ll2.setTextSize(10f);
        ll2.setTextColor(context.getResources().getColor(R.color.white));
        ll2.setLineColor(context.getResources().getColor(R.color.safron_barChart));

        xAxis.removeAllLimitLines();


        volumeReportChart.getAxisRight().setEnabled(false);

        //setData()-- allAmounts is data to display;
        setDataForWeeksWise(allAmounts);


    }

    private void setDataForWeeksWise(List<Double> amounts) {

        ArrayList<BarEntry> values = new ArrayList<>();
        values.add(new BarEntry(1, amounts.get(0).floatValue()));
        values.add(new BarEntry(2, amounts.get(1).floatValue()));
        values.add(new BarEntry(3, amounts.get(2).floatValue()));
        values.add(new BarEntry(4, amounts.get(3).floatValue()));
        values.add(new BarEntry(5, amounts.get(4).floatValue()));
        values.add(new BarEntry(6, amounts.get(5).floatValue()));
        values.add(new BarEntry(7, amounts.get(6).floatValue()));
        values.add(new BarEntry(8, amounts.get(7).floatValue()));
        values.add(new BarEntry(9, amounts.get(8).floatValue()));
        values.add(new BarEntry(10, amounts.get(9).floatValue()));
        values.add(new BarEntry(11, amounts.get(10).floatValue()));
        values.add(new BarEntry(12, amounts.get(11).floatValue()));


        BarDataSet set1;
        if (volumeReportChart.getData() != null && volumeReportChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) volumeReportChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            volumeReportChart.getData().notifyDataChanged();
            volumeReportChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(values, "Total volume");

            //  set1.setDrawCircles(true);

            set1.setColor(context.getResources().getColor(R.color.yellow));
            //  set1.setCircleColor(getResources().getColor(R.color.yellow));
            //   set1.setLineWidth(2f);//line size
            //  set1.setCircleRadius(5f);
            set1.setValueTextColor(context.getResources().getColor(R.color.white));
            //    set1.setDrawCircleHole(true);
            set1.setValueTextSize(10f);
            //    set1.setDrawFilled(true);

            set1.setFormLineWidth(5f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(5.f);
            //   set1.setFillColor(Color.WHITE);
            set1.setDrawValues(true);
            //   set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            ArrayList<BarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);
            BarData data = new BarData();

            volumeReportChart.setData(data);
            volumeReportChart.getAxisLeft().setDrawLabels(false);
            volumeReportChart.getAxisRight().setDrawLabels(false);
            //  volumeReportChart.getXAxis().setDrawLabels(false);
            volumeReportChart.getDescription().setEnabled(false);
            volumeReportChart.setDrawGridBackground(false);
            volumeReportChart.getLegend().setEnabled(false);
            volumeReportChart.animate();
        }
    }

    boolean doubleBackToExitPressedOnce = false;


    private void dashboardcounter() {

        SalesEmployeeItem salesEmployeeItem = new SalesEmployeeItem();
        salesEmployeeItem.setSalesEmployeeCode(Prefs.getString(Globals.SalesEmployeeCode, ""));
        Call<CounterResponse> call = NewApiClient.getInstance().getApiService().dashboardcounter(salesEmployeeItem);
        call.enqueue(new Callback<CounterResponse>() {
            @Override
            public void onResponse(Call<CounterResponse> call, Response<CounterResponse> response) {
                if (response != null) {


                    //  binding.contentData.allLead.setText(response.body().getValue().get(0).getLeads());
                    //  binding.contentData.revenue.setText(response.body().getValue().get(0).getAmount());
                    //  binding.contentData.difference.setText(response.body().getValue().get(0).getSale_diff());


                }
            }

            @Override
            public void onFailure(Call<CounterResponse> call, Throwable t) {

            }
        });
    }


    private void callCountryApi() {
        Call<CountryResponse> call = NewApiClient.getInstance().getApiService().getCountryList();
        call.enqueue(new Callback<CountryResponse>() {
            @Override
            public void onResponse(Call<CountryResponse> call, Response<CountryResponse> response) {
                if (response.code() == 200) {
                    if (response.body().getData().size() > 0) {
                        Dashboard.countrylist.clear();
                        Dashboard.countrylist.addAll(response.body().getData());
                    }

                }
            }

            @Override
            public void onFailure(Call<CountryResponse> call, Throwable t) {
                Toast.makeText(requireContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // callrecentactivityapi();
        // callrecent_5_order();
    }

    List<EventValue> recentlist = new ArrayList<>();
    List<QuotationItem> recenOrdertlist = new ArrayList<>();

    private void callrecentactivityapi() {
        SalesEmployeeItem si = new SalesEmployeeItem();
        Log.e("TAG", "callrecentactivityapi: " + Prefs.getString(Globals.EmployeeID, ""));
        si.setEmp(Prefs.getString(Globals.EmployeeID, ""));
        // Call<EventResponse> call = NewApiClient.getInstance().getApiService().getcalendardata(si);
        Call<EventResponse> call = NewApiClient.getInstance().getApiService().getrecentactivity(si);
        call.enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                if (response.code() == 200) {
                    recentlist.clear();
                    recentlist.addAll(response.body().getData());
                    if (response.body().getData().size() == 0) {
                        EventValue eventValue = new EventValue();
                        eventValue.setTitle("No Activity");
                        eventValue.setCreateDate(" None ");
                        eventValue.setComment("No recent activity created.");
                        recentlist.add(eventValue);

                    }
                    Context context = requireContext();
                    if (context != null) {
                        adapter = new RecentAdapter(context, recentlist);

                        binding.contentData.cominguprecyvlerview.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
                        binding.contentData.recentrecyvlerview.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
                        binding.contentData.cominguprecyvlerview.setAdapter(adapter);
                        binding.contentData.recentrecyvlerview.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }


                }
            }

            @Override
            public void onFailure(Call<EventResponse> call, Throwable t) {
                Toast.makeText(requireContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void callrecent_5_order() {
        HashMap<String, String> hde = new HashMap<>();
        hde.put("SalesPersonCode", Prefs.getString(Globals.SalesEmployeeCode, ""));
        Call<QuotationResponse> call = NewApiClient.getInstance().getApiService().top5order(hde);
        call.enqueue(new Callback<QuotationResponse>() {
            @Override
            public void onResponse(Call<QuotationResponse> call, Response<QuotationResponse> response) {
                if (response.code() == 200) {
                    recenOrdertlist.clear();
                    recenOrdertlist.addAll(response.body().getValue());
                    if (response.body().getValue().size() == 0) {
                        EventValue eventValue = new EventValue();
                        eventValue.setTitle("No Activity");
                        eventValue.setCreateDate(" None ");
                        eventValue.setComment("No recent activity created.");

                    }
                    RecentOrderAdapter adapter = new RecentOrderAdapter(requireContext(), recenOrdertlist);
                    binding.contentData.recentrecyvlerview.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false));
                    binding.contentData.recentrecyvlerview.setAdapter(adapter);

                    adapter.notifyDataSetChanged();


                }
            }

            @Override
            public void onFailure(Call<QuotationResponse> call, Throwable t) {
                Toast.makeText(requireContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void openpopup() {
// .addItem(new PowerMenuItem("New BP",R.drawable.ic_newbp, false)) // add an item.

        PowerMenu powerMenu = new PowerMenu.Builder(requireContext()).addItem(new PowerMenuItem("New Lead", R.drawable.ic_newlead, false)) // aad an item list.
                // .addItem(new PowerMenuItem("New Opportunity", R.drawable.ic_newopp, false)) // aad an item list.
                .addItem(new PowerMenuItem("New Contact", R.drawable.ic_newcontact, false)) // aad an item list.
                //  .addItem(new PowerMenuItem("New Quotation", R.drawable.ic_newtask, false)) // aad an item list.
                // .addItem(new PowerMenuItem("New Order", R.drawable.ic_new_event, false)) // aad an item list.
                .addItem(new PowerMenuItem("New Campaign", R.drawable.ic_note, false)) // aad an item list.
                .setAnimation(MenuAnimation.SHOWUP_TOP_RIGHT) // Animation start point (TOP | LEFT).
                .setMenuRadius(10f) // sets the corner radius.
                .setMenuShadow(10f) // sets the shadow.
                .setTextColor(ContextCompat.getColor(requireContext(), R.color.black)).setTextGravity(Gravity.START).setTextSize(12).setTextTypeface(Typeface.createFromAsset(requireActivity().getAssets(), "poppins_regular.ttf")).setSelectedTextColor(Color.BLACK).setWidth(Globals.pxFromDp(requireContext(), 220f)).setMenuColor(Color.WHITE).setSelectedMenuColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary)).build();
        powerMenu.showAsDropDown(binding.quickCreate);


        OnMenuItemClickListener<PowerMenuItem> onMenuItemClickListener = new OnMenuItemClickListener<PowerMenuItem>() {
            @Override
            public void onItemClick(int position, PowerMenuItem item) {
                powerMenu.setSelectedPosition(position); // change selected item

                switch (position) {

                    case 0:
                        Prefs.putString(Globals.BussinessPageType, "DashBoard");
                        startActivity(new Intent(requireContext(), AddLead.class));
                        break;
                    case 1:
                        Prefs.putString(Globals.SelectOpportnity, "Dashboard");
                        startActivity(new Intent(requireContext(), AddOpportunityActivity.class));
                        break;
                    case 2:
                        Intent intent = new Intent(requireContext(), AddContact.class);
                        Prefs.putString(Globals.AddContactPerson, "Dashboard");
                        startActivity(intent);
                        break;
                    case 3:
                        Prefs.putString(Globals.QuotationListing, "null");
                        Prefs.putBoolean(Globals.SelectQuotation, true);
                        Intent i = new Intent(requireContext(), AddQuotationAct.class);
                        startActivity(i);
                        break;
                    case 4:
                        Prefs.putString(Globals.FromQuotation, "Order");
                        startActivity(new Intent(requireContext(), AddOrderAct.class));
                        break;
                    case 5:
                        startActivity(new Intent(requireContext(), AddCampaign.class));
                        break;

                }

                powerMenu.dismiss();
            }
        };
        powerMenu.setOnMenuItemClickListener(onMenuItemClickListener);
    }


    public class ClaimsXAxisValueFormatter extends ValueFormatter {

        List<String> datesList;

        public ClaimsXAxisValueFormatter(List<String> arrayOfDates) {
            this.datesList = arrayOfDates;
        }


        @Override
        public String getAxisLabel(float value, AxisBase axis) {
/*
Depends on the position number on the X axis, we need to display the label, Here, this is the logic to convert the float value to integer so that I can get the value from array based on that integer and can convert it to the required value here, month and date as value. This is required for my data to show properly, you can customize according to your needs.
*/
            Integer position = Math.round(value);
            SimpleDateFormat sdf = new SimpleDateFormat("MMM");

            if (value > 1 && value < 2) {
                position = 0;
            } else if (value > 2 && value < 3) {
                position = 1;
            } else if (value > 3 && value < 4) {
                position = 2;
            } else if (value > 4 && value < 5) {
                position = 3;
            } else if (value > 5 && value < 6) {
                position = 4;
            } else if (value > 6 && value < 7) {
                position = 5;
            } else if (value > 7 && value < 8) {
                position = 6;
            } else if (value > 8 && value < 9) {
                position = 7;
            } else if (value > 9 && value <= 10) {
                position = 8;
            }
            if (position < datesList.size())
                return sdf.format(new Date((Globals.getDateInMilliSeconds(datesList.get(position), "yyyy-MM-dd"))));
            return "";
        }
    }

    private static final int LOCATION_PERMISSION_CODE = 100;

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);
        } else {
            getCurrentLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == RESULT_OK) {
            // Get the URI of the selected file
            Uri uri = data.getData();
            attachentUri = uri.toString();
            attchmentName = getFileName(uri);
            attachmentName.setText(attchmentName);
            etNewAttachemnt.setText(attchmentName);
            picturePath = getRealPathFromURI(uri);
            //  Toast.makeText(this, picturePath, Toast.LENGTH_SHORT).show();


            // Use the URI to access the file
            // ...
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            if (data.getData() != null) {
                Uri uri = data.getData();
                //  attachmentcheckOut=getFileName(uri);
                attachmentcheckOut = getRealPathFromURI(uri);
                checkinDialogBinding.etAttachmentsNameCHeckIN.setText(attachmentcheckOut);
            }

        } else if (requestCode == PICK_FILE_CHECKOUT_REQUEST_CODE && resultCode == RESULT_OK) {

            Uri uri = data.getData();
            //  attachmentcheckOut=getFileName(uri);
            attachmentcheckOut = getRealPathFromURIchekOut(uri);
            checkOutExpenseDialogBinding.etAttachmentsNameOut.setText(attachmentcheckOut);


        }
    }


    private void getCurrentLocation() {
        locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                getAddressFromLocation(latitude, longitude);
            }
        } else {
            showGPSDisabledAlert();
        }
    }

    private void showGPSDisabledAlert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(requireContext());
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?").setCancelable(false).setPositiveButton("Enable GPS", (dialog, id) -> {
            Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(callGPSSettingIntent);
        });
        alertDialogBuilder.setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    private void getAddressFromLocation(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (addresses != null && addresses.size() > 0) {
            Address address = addresses.get(0);
            String addressLine = address.getAddressLine(0);
            String city = address.getLocality();
            String state = address.getAdminArea();
            String country = address.getCountryName();
            String postalCode = address.getPostalCode();
            String knownName = address.getFeatureName();
            // Do something with the address information
            //Toast.makeText(this, addressLine, Toast.LENGTH_SHORT).show();
            callMapApi(latitude, longitude, locationtype, addressLine);


        }
    }


    private void callMapApi(double latitude, double longitude, String type, String address) {
        Log.d("TAG", "callMapApi: ");
        MapData mapData = new MapData();
        mapData.setEmp_Id(Prefs.getString(Globals.EmployeeID, ""));
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
        mapData.setContactPerson("");
        mapData.setSourceType("");
        Call<MapResponse> call = NewApiClient.getInstance().getApiService().sendMaplatlong(mapData);
        call.enqueue(new Callback<MapResponse>() {
            @Override
            public void onResponse(Call<MapResponse> call, Response<MapResponse> response) {
                if (response != null) {
                    try {
                        if (response.isSuccessful()) {
                            Log.e("success", "success-Bhupi");
                            Log.d("response", "success-shubh");
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


    public String getRealPathFromURI(Uri uri) {
        String result = null;
        String[] projection = {MediaStore.Files.FileColumns.DATA};
        Cursor cursor = requireActivity().getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA);
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            picturePath = filePath;
            result = filePath;
            //  Toast.makeText(this, filePath, Toast.LENGTH_SHORT).show();

            // Do something with the file path, such as displaying it in a TextView
            // TextView filePathTextView = findViewById(R.id.file_path_text_view);
            //  filePathTextView.setText(filePath);
        }
        return result;

    }


    public String getRealPathFromURIchekOut(Uri uri) {
        String result = null;
        String[] projection = {MediaStore.Files.FileColumns.DATA};
        Cursor cursor = requireActivity().getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA);
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            checkOUtPicturePath = filePath;
            result = filePath;
            //  Toast.makeText(this, filePath, Toast.LENGTH_SHORT).show();

            // Do something with the file path, such as displaying it in a TextView
            // TextView filePathTextView = findViewById(R.id.file_path_text_view);
            //  filePathTextView.setText(filePath);
        }
        return result;

    }


    // Helper method to get the file name from the URI
    @SuppressLint("Range")
    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = requireActivity().getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }


    /*********************** Graphs APIs**************************/

    ArrayList<GraphModel> salesGraphList = new ArrayList<>();


    /*********************** Graphs APIs**************************/
    public static List<BarEntry> Purchaseentries = new ArrayList<>();
    public static List<BarEntry> Paymententries = new ArrayList<>();
    public static List<BarEntry> Paybleentries = new ArrayList<>();
    public static List<String> PayableentriesForMarker = new ArrayList<>();
    public static List<String> PayableentriesXaxis = new ArrayList<>();


    public static List<String> PurchaseValueForMarker = new ArrayList<>();
    public static List<String> ReceivableValueForMarker = new ArrayList<>();
    public static List<String> PaymentValueForMarker = new ArrayList<>();

    /********************** Sales Graph*************************/

    private void saleGraphApi() {

        HashMap obj = new HashMap<String, String>();
        obj.put("FromDate", "2023-04-01");
        obj.put("ToDate", "2024-03-31");
        obj.put("SalesPersonCode", Prefs.getString(Globals.SalesEmployeeCode, ""));

        Call<SalesGraphResponse> call = NewApiClient.getInstance().getApiService().salesGraphPurchase(obj);
        call.enqueue(new Callback<SalesGraphResponse>() {
            @Override
            public void onResponse(Call<SalesGraphResponse> call, Response<SalesGraphResponse> response) {
                if (response != null) {
                    if (response.body().status == 200) {
                        if (response.body() != null && response.body().data.size() > 0)
                            Purchaseentries.clear();
                        PurchaseValueForMarker.clear();

                        for (int i = 0; i < response.body().data.size(); i++) {
                            Purchaseentries.add(new BarEntry(i, Float.parseFloat(response.body().data.get(i).getMonthlySales())));
                         //   PurchaseValueForMarker.add(new BarEntry(i, Float.parseFloat(response.body().data.get(i).getMonthlySales())));
                            PurchaseValueForMarker.add(Globals.convertToLakhAndCroreFromString(response.body().data.get(i).getMonthlySales()));
                        }
                        // opengraph();

                    }

                    ReceiptGraphApi();
                }
            }

            @Override
            public void onFailure(Call<SalesGraphResponse> call, Throwable t) {

            }
        });
    }

    /********************** Receipt Graph*************************/

    private void ReceiptGraphApi() {

        HashMap obj = new HashMap<String, String>();
        obj.put("FromDate", "2023-04-01");
        obj.put("ToDate", "2024-03-31");
        obj.put("SalesPersonCode", Prefs.getString(Globals.SalesEmployeeCode, ""));

        Call<SalesGraphResponse> call = NewApiClient.getInstance().getApiService().receiptGraphPurchase(obj);
        call.enqueue(new Callback<SalesGraphResponse>() {
            @Override
            public void onResponse(Call<SalesGraphResponse> call, Response<SalesGraphResponse> response) {
                if (response != null) {
                    if (response.body().status == 200) {
                        if (response.body() != null && response.body().data.size() > 0)
                            Paymententries.clear();
                            PaymentValueForMarker.clear();

                        for (int i = 0; i < response.body().data.size(); i++) {
                            Paymententries.add(new BarEntry(i, Float.parseFloat(response.body().data.get(i).getMonthlySales())));
                            PaymentValueForMarker.add(Globals.convertToLakhAndCroreFromString(response.body().data.get(i).getMonthlySales()));

                        }
                        // opengraph();


                    }

                    ReceivableGraphApi();
                }
            }

            @Override
            public void onFailure(Call<SalesGraphResponse> call, Throwable t) {

            }
        });
    }


    /********************** Receivable Graph*************************/

    private void ReceivableGraphApi() {

       /* {
            "FromDate":"2023-01-01",
                "ToDate":"2023-12-31",
                "SalesPersonCode":-1
        }*/


        HashMap obj = new HashMap<String, String>();
        obj.put("FromDate", Globals.firstDateOfFinancialYear());
        obj.put("ToDate", Globals.lastDateOfFinancialYear());
        obj.put("SalesPersonCode", Prefs.getString(Globals.SalesEmployeeCode, ""));

        Call<ResponseReceivableGraph> call = NewApiClient.getInstance().getApiService().receivableDueMonthGraphPurchase(obj);
        call.enqueue(new Callback<ResponseReceivableGraph>() {
            @Override
            public void onResponse(Call<ResponseReceivableGraph> call, Response<ResponseReceivableGraph> response) {
                if (response != null) {
                    if (response.body().status == 200) {
                        if (response.body() != null && response.body().data.size() > 0)
                            Paybleentries.clear();
                        PayableentriesXaxis.clear();
                        PayableentriesForMarker.clear();
                        for (int i = 0; i < response.body().data.size(); i++) {
                            ArrayList<String> daysGroup = new ArrayList<>();
                           /* if (response.body().data.get(i).getOverDueDaysGroup().equalsIgnoreCase("")) {

                            }*/
                            PayableentriesXaxis.add(response.body().getData().get(i).getOverDueDaysGroup());
                            Paybleentries.add(new BarEntry(i, (float) response.body().getData().get(i).getTotalDue()));
                            PayableentriesForMarker.add(String.valueOf(Globals.convertToLakhAndCroreFromString(response.body().data.get(i).getTotalDue())));

                        }
                        // opengraph();

                    }

                    setupChartViewPgaer();
                }
            }

            @Override
            public void onFailure(Call<ResponseReceivableGraph> call, Throwable t) {

            }
        });
    }



}