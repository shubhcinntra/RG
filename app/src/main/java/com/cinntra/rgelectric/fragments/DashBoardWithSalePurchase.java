package com.cinntra.rgelectric.fragments;

import static com.github.mikephil.charting.utils.ColorTemplate.rgb;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cinntra.rgelectric.R;
import com.cinntra.rgelectric.activities.ProfileActivity;
import com.cinntra.rgelectric.activities.Reports;
import com.cinntra.rgelectric.activities.Sale_Inovice_Reports;
import com.cinntra.rgelectric.customUI.RoundedBarChart;
import com.cinntra.rgelectric.databinding.BottomSheetDialogSelectDateBinding;
import com.cinntra.rgelectric.databinding.FragmentDashBoardWithSalePurchaseBinding;
import com.cinntra.rgelectric.globals.Globals;
import com.cinntra.rgelectric.model.DashboardCounterResponse;
import com.cinntra.rgelectric.model.SalesGraphResponse;
import com.cinntra.rgelectric.newapimodel.LeadResponse;
import com.cinntra.rgelectric.newapimodel.ResponseReceivableGraph;
import com.cinntra.rgelectric.webservices.NewApiClient;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pixplicity.easyprefs.library.Prefs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DashBoardWithSalePurchase extends Fragment {
    private FragmentDashBoardWithSalePurchaseBinding binding;
    private static final String TAG = "DashBoardWithSalePurcha";
    public static List<BarEntry> LocalBarEntries = new ArrayList<>();

    AlertDialog.Builder builder;
    AlertDialog alertDialog;


    public static List<Entry> Paybleentries = new ArrayList<>();
    public static List<String> PayableentriesForMarker = new ArrayList<>();
    public static List<String> PayableentriesXaxis = new ArrayList<>();


    public static List<Entry> receivableentries = new ArrayList<>();
    public static List<String> receivableentriesForMarker = new ArrayList<>();
    public static List<String> receivableentriesXaxis = new ArrayList<>();

    public DashBoardWithSalePurchase() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dash_board_with_sale_purchase, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentDashBoardWithSalePurchaseBinding.bind(view);


        builder = new AlertDialog.Builder(requireContext());
        builder.setView(R.layout.progress_dialog_alert);
        alertDialog = builder.create();


        setupPieChart();
        setupPieChartExpense();
        xaxisOfDueCustomer.add("Apple");
        xaxisOfDueCustomer.add("Intel");
        xaxisOfDueCustomer.add("Lava");
        xaxisOfDueCustomer.add("hp");
        xaxisOfDueCustomer.add("Nokia");
        xaxisOfMonth.add("Apr");
        xaxisOfMonth.add("May");
        xaxisOfMonth.add("June");
        xaxisOfMonth.add("July");
        xaxisOfMonth.add("Aug");
        xaxisOfMonth.add("Sept");
        xaxisOfMonth.add("Oct");
        xaxisOfMonth.add("Nov");
        xaxisOfMonth.add("Dec");
        xaxisOfMonth.add("Jan");
        xaxisOfMonth.add("Feb");
        xaxisOfMonth.add("Mar");


        LocalBarEntries.add(new BarEntry(1, 420f));
        LocalBarEntries.add(new BarEntry(2, 100f));
        LocalBarEntries.add(new BarEntry(3, 130f));
        LocalBarEntries.add(new BarEntry(4, 230f));
        LocalBarEntries.add(new BarEntry(5, 340f));


        SalesGraphApi();


        fiveCustomerDueAmountNew(binding.chartTopFiveCustomersByDuePayment, LocalBarEntries);
        fiveCustomerSalesAmount(binding.chartTopFiveCustomersBySalesPayment, LocalBarEntries);

        totalReceivablesPayablesLineGraph(binding.chartTotalReceivablesPayables);
        setBarChartData(binding.chartTotalOrderFullFillVsIncoming);
        callDashboardCounter_Payables();
        callDashboardCounter_Receiable();
        callDashboardCounterForSales();
        callDashboardCounterForPurchase();


        binding.cardTotalRevenue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Prefs.putString(Globals.forSalePurchase, Globals.Sale);
                Prefs.putString("ForReports", "MainActivity_B2C_Ledger");
                startActivity(new Intent(requireContext(), Sale_Inovice_Reports.class));

            }
        });

        binding.cardTotalDueAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Prefs.putString(Globals.forSalePurchase, Globals.Purchase);
                Prefs.putString("ForReports", "MainActivity_B2C_Ledger");
                startActivity(new Intent(requireContext(), Sale_Inovice_Reports.class));

            }
        });

        binding.cardTotalGrossAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Prefs.putString(Globals.forSalePurchase, Globals.Sale);
                Prefs.putString("ForReports", "ReceiptLedger");
                startActivity(new Intent(requireContext(), Reports.class));

            }
        });

        binding.cardReceivable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Prefs.putString("ForReports", "Receivable");
                Prefs.putString(Globals.forSalePurchase, Globals.Sale);
                startActivity(new Intent(requireContext(), Reports.class));
            }
        });

        binding.cardPayable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Prefs.putString("ForReports", "Receivable");
                Prefs.putString(Globals.forSalePurchase, Globals.Purchase);
                startActivity(new Intent(requireContext(), Reports.class));
            }
        });


        binding.ibCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateInDashboardBottomSheetDialog(requireContext());
            }
        });

        binding.userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(requireContext(), ProfileActivity.class);
                startActivity(i);
            }
        });


    }


    float valueOrderSummary[] = {50, 25, 25};
    float valueTotalExpenditure[] = {35, 15, 25, 25};
    String orderSummaryLegends[] = {"Closed", "Pending", "Cancel"};
    String expenditureLegends[] = {"Vendor", "Hotel", "Employee Salary", "Travel"};

    public static final int[] COLORS = {
            rgb("#4A79E4"), rgb("#FFA63D"), rgb("#FF6258"), rgb("#3498db")
    };

    public static final int[] COLORSExpenditure = {
            rgb("#4A79E4"), rgb("#04339D"), rgb("#FFA63D"), rgb("#FF6258")
    };

    private List<String> xaxisOfDueCustomer = new ArrayList<>();
    private List<String> xaxisOfMonth = new ArrayList<>();


    private void callDashboardCounterForSales() {
        // binding.contentData.tvDateText.setText("" + Globals.convertDateFormatInReadableFormat(startDate) + " to " + Globals.convertDateFormatInReadableFormat(endDate));
        alertDialog.show();

        HashMap obj = new HashMap<String, String>();
        obj.put("Filter", "");
        obj.put("Code", "");
        obj.put("Type", "Gross");
        obj.put("FromDate", startDate);
        obj.put("ToDate", endDate);
        obj.put("SalesPersonCode", Prefs.getString(Globals.SalesEmployeeCode, ""));

        Prefs.putString(Globals.FROM_DATE, startDate);
        Prefs.putString(Globals.TO_DATE, endDate);
        Call<DashboardCounterResponse> call = NewApiClient.getInstance().getApiService().getDashBoardCounterForLedger(obj);
        call.enqueue(new Callback<DashboardCounterResponse>() {
            @Override
            public void onResponse(Call<DashboardCounterResponse> call, Response<DashboardCounterResponse> response) {
                if (response.code() == 200) {
                    alertDialog.dismiss();
                    if (response.body().getData().size() > 0) {
                        try {
                            binding.tvtotalRevenue.setText(requireActivity().getResources().getString(R.string.Rs) + " " + Globals.numberToK(String.valueOf(response.body().getData().get(0).getTotalSales())));
                            binding.tvtotalCollectionAmount.setText(requireActivity().getResources().getString(R.string.Rs) + " " + Globals.numberToK(String.valueOf(response.body().getData().get(0).getTotalReceivePayment())));
                        } catch (Exception e) {

                        }

                    }


                } else {
                    alertDialog.dismiss();

                 /*   Gson gson = new GsonBuilder().create();
                    LeadResponse mError = new LeadResponse();
                    try {
                        String s = response.errorBody().string();
                        mError = gson.fromJson(s, LeadResponse.class);
                    } catch (IOException e) {
                        //handle failure to read error
                    }*/

                }

            }

            @Override
            public void onFailure(Call<DashboardCounterResponse> call, Throwable t) {
                alertDialog.dismiss();

                Toast.makeText(requireContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }


    private void callDashboardCounterForPurchase() {


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
                        try {
                            binding.tvtotalDueAmount.setText(requireContext().getResources().getString(R.string.Rs) + " " + Globals.numberToK(String.valueOf(response.body().getData().get(0).getTotalSales())));
                        } catch (Exception e) {

                        }

                    }


                } else {
                    alertDialog.dismiss();

               /*     Gson gson = new GsonBuilder().create();
                    LeadResponse mError = new LeadResponse();
                    try {
                        String s = response.errorBody().string();
                        mError = gson.fromJson(s, LeadResponse.class);
                    } catch (IOException e) {
                        //handle failure to read error
                    }*/

                }

            }

            @Override
            public void onFailure(Call<DashboardCounterResponse> call, Throwable t) {
                alertDialog.dismiss();

                Toast.makeText(requireContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void setupPieChart() {

        //pupulating list of PieEntires
        List<PieEntry> pieEntires = new ArrayList<>();
        for (int i = 0; i < valueOrderSummary.length; i++) {
            pieEntires.add(new PieEntry(valueOrderSummary[i], orderSummaryLegends[i]));
        }
        PieDataSet dataSet = new PieDataSet(pieEntires, "");
        dataSet.setColors(COLORS);
        dataSet.setValueTextColor(getActivity().getResources().getColor(R.color.white));
        PieData data = new PieData(dataSet);
        //Get the chart
        binding.pieChartOrderSummary.setData(data);
        binding.pieChartOrderSummary.invalidate();
        binding.pieChartOrderSummary.setCenterText("1200\nOrders");
        binding.pieChartOrderSummary.setDrawEntryLabels(false);
        binding.pieChartOrderSummary.setDescription(null);
        binding.pieChartOrderSummary.setContentDescription("");
        //pieChart.setDrawMarkers(true);
        //pieChart.setMaxHighlightDistance(34);
        binding.pieChartOrderSummary.setEntryLabelTextSize(12);
        ;
        //pieChart.setHoleRadius(75);

        //legend attributes
        Legend legend = binding.pieChartOrderSummary.getLegend();
        legend.setXEntrySpace(4);
        legend.setForm(Legend.LegendForm.CIRCLE); // Set legend forms to circles
        //  legend.setDirection(Legend.LegendDirection.VERTICAL); // Set legend direction to vertical
        //  legend.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER); // Align the legend vertically to the center
        //  legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT); // Align the legend horizontally to the right
        // legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        //legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setTextSize(10);
        legend.setFormSize(10);


    }

    private void setBarChartData(BarChart barChart) {
        List<BarEntry> entries1 = new ArrayList<>();
        List<BarEntry> entries2 = new ArrayList<>();
        List<BarEntry> entries3 = new ArrayList<>();

        // Add sample data to entries1 and entries2
        entries1.add(new BarEntry(0f, 20));
        entries1.add(new BarEntry(1f, 30));
        entries1.add(new BarEntry(2f, 20));
        entries1.add(new BarEntry(3f, 30));
        entries1.add(new BarEntry(4f, 50));
        entries1.add(new BarEntry(5f, 70));
        entries1.add(new BarEntry(6f, 40));
        entries1.add(new BarEntry(7f, 80));


        entries2.add(new BarEntry(0f, 10));
        entries2.add(new BarEntry(1f, 25));
        entries2.add(new BarEntry(2f, 25));
        entries2.add(new BarEntry(3f, 25));
        entries2.add(new BarEntry(4f, 75));
        entries2.add(new BarEntry(5f, 65));
        entries2.add(new BarEntry(6f, 21));
        entries2.add(new BarEntry(7f, 45));


        BarDataSet set1 = new BarDataSet(entries1, "Data Set 1");
        set1.setColor(requireContext().getResources().getColor(R.color.order_incoming_graph_color));
        set1.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return ""; // Return empty string to disable top value
            }
        });

        BarDataSet set2 = new BarDataSet(entries2, "Data Set 2");
        set2.setColor(requireContext().getResources().getColor(R.color.colorPrimary));

        set2.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return ""; // Return empty string to disable top value
            }
        });


        float groupSpace = 0.2f; // Space between groups
        float barSpace = 0.01f; // Space between individual bars within a group
        float barWidth = 0.14f; // Width of each bar

        BarData barData = new BarData(set1, set2);

        barChart.setData(barData);

        int visibleRange = 2; // Set the desired visible range
        //   int dataCount = chartList.size(); // Update with the actual size of your data list

        barData.setBarWidth(barWidth);
        barChart.setDragEnabled(true);
        barData.groupBars(0, groupSpace, barSpace);

        barChart.setDrawGridBackground(false);
        barChart.getDescription().setEnabled(false);
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getAxisRight().setEnabled(false);
        barChart.setPinchZoom(false);
        barChart.setDoubleTapToZoomEnabled(false);

        barChart.animateY(1000);

        barChart.setNoDataTextColor(getResources().getColor(R.color.colorPrimary));
        // barChart.getAxisLeft().setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);

        // Set chart padding
    /*    barChart.setExtraTopOffset(10f);
        barChart.setExtraBottomOffset(10f);
        barChart.setExtraLeftOffset(20f);
        barChart.setExtraRightOffset(10f);*/

        // Customize x-axis labels
/*        XAxis xAxis = barChart.getXAxis();
      //  xAxis.setValueFormatter(new IndexAxisValueFormatter(xaxisOfMonth));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
    *//*    xAxis.setGranularityEnabled(true);
        xAxis.setCenterAxisLabels(true);*//*
      //  xAxis.setGranularity(1f); // Display one label per data point
        xAxis.setAxisMinimum(0f);
         xAxis.setAxisMaximum(barData.getGroupWidth(groupSpace, barSpace) * entries1.size());

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawGridLines(false);*/


        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        try {
            barChart.getXAxis().setTextColor(requireActivity().getResources().getColor(R.color.black));
        } catch (Exception e) {

        }
        barChart.getXAxis().setLabelCount(13, false);
        barChart.getXAxis().setDrawGridLines(false);

        barChart.getXAxis().setGranularity(1f);

        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xaxisOfMonth));

        barChart.getAxisLeft().setDrawGridLines(false);


        barChart.resetZoom();
        barChart.fitScreen();
        // barChart.setVisibleXRangeMaximum(2);
        //  barChart.moveViewToX(0);
        barChart.getLegend().setEnabled(false);
        barChart.invalidate();
    }


    private void setupPieChartExpense() {

        //pupulating list of PieEntires
        List<PieEntry> pieEntires = new ArrayList<>();
        for (int i = 0; i < valueTotalExpenditure.length; i++) {
            pieEntires.add(new PieEntry(valueTotalExpenditure[i], expenditureLegends[i]));
        }
        PieDataSet dataSet = new PieDataSet(pieEntires, "");
        dataSet.setColors(COLORSExpenditure);
        dataSet.setValueTextColor(getActivity().getResources().getColor(R.color.white));
        PieData data = new PieData(dataSet);
        //Get the chart
        binding.pieChartTotalExpenditure.setData(data);
        binding.pieChartTotalExpenditure.invalidate();
        //  binding.pieChartTotalExpenditure.setCenterText("1200\nOrders");
        binding.pieChartTotalExpenditure.setDrawEntryLabels(false);
        binding.pieChartTotalExpenditure.setDescription(null);
        binding.pieChartTotalExpenditure.setContentDescription("");
        //pieChart.setDrawMarkers(true);
        //pieChart.setMaxHighlightDistance(34);
        binding.pieChartTotalExpenditure.setEntryLabelTextSize(12);
        ;
        //pieChart.setHoleRadius(75);

        //legend attributes
        Legend legend = binding.pieChartTotalExpenditure.getLegend();
        legend.setXEntrySpace(4);
        legend.setForm(Legend.LegendForm.CIRCLE); // Set legend forms to circles
        //  legend.setDirection(Legend.LegendDirection.VERTICAL); // Set legend direction to vertical
        //  legend.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER); // Align the legend vertically to the center
        //  legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT); // Align the legend horizontally to the right
        // legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        //legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setTextSize(10);
        legend.setFormSize(10);


    }


    private void fiveCustomerDueAmount(View view, List<BarEntry> entries) {


        List<String> xvalues = Arrays.asList(">90", "61-90", "0-30", "31-60");
        //List<String> xvalues= Arrays.asList("Jan","Feb","March","April","May","June");

        BarChart customer_barChart = view.findViewById(R.id.chartTopFiveCustomersByDuePayment);


        /*CustomMarkerViewReceivables markerView = new CustomMarkerViewReceivables(requireContext(), R.layout.barchart_marker, MainActivity_B2C.ReceivableentriesForMarker);


        customer_barChart.setMarker(markerView);*/


        RoundedBarChart roundedBarChartRenderer = new RoundedBarChart(customer_barChart, customer_barChart.getAnimator(), customer_barChart.getViewPortHandler());
        roundedBarChartRenderer.setmRadius(5f);
        customer_barChart.setRenderer(roundedBarChartRenderer);

        customer_barChart.setDrawBarShadow(false);
        customer_barChart.setDrawValueAboveBar(false);
        customer_barChart.getDescription().setEnabled(false);
        customer_barChart.setDrawGridBackground(false);


        customer_barChart.getAxisRight().setEnabled(false);
        Legend legend = customer_barChart.getLegend();
        legend.setEnabled(false);


        List<IBarDataSet> dataSets = new ArrayList<>();
        BarDataSet dataSet = new BarDataSet(entries, "Values");
        try {
            dataSet.setColor(requireActivity().getResources().getColor(R.color.orange_bar_color));
        } catch (Exception e) {

        }
        dataSet.setDrawValues(false);
        dataSets.add(dataSet);


        BarData data = new BarData(dataSets);
        data.setBarWidth(0.3f);
        try {
            data.setValueTextColor(requireActivity().getResources().getColor(R.color.white));
        } catch (Exception e) {

        }
        //  data.setValueTextColor(Color.WHITE);
        customer_barChart.setData(data);
        customer_barChart.setFitBars(false);


        // customer_barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xvalues));
        customer_barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xaxisOfDueCustomer));
        customer_barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        try {
            customer_barChart.getXAxis().setTextColor(requireActivity().getResources().getColor(R.color.black));
        } catch (Exception e) {

        }
        //  customer_barChart.getXAxis().setLabelCount(13, false);
        customer_barChart.getXAxis().setDrawGridLines(false);
        // customer_barChart.getXAxis().setCenterAxisLabels(true);
        customer_barChart.getXAxis().setAxisMinimum(0.5f);
        customer_barChart.getXAxis().setGranularity(1f);


        YAxis yAxis = customer_barChart.getAxisLeft();
        try {
            yAxis.setTextColor(requireActivity().getResources().getColor(R.color.black));
        } catch (Exception e) {

        }
        yAxis.setAxisMinimum(0f);

        customer_barChart.setTouchEnabled(false);
        customer_barChart.setDrawBarShadow(false);


        //hide grid lines
        customer_barChart.getAxisLeft().setDrawGridLines(false);


        //remove right y-axis
        customer_barChart.getAxisRight().setEnabled(false);

        //remove legend


        //remove description label
        customer_barChart.getDescription().setEnabled(false);

        //add animation
        customer_barChart.animateY(2000);


        //draw chart
        customer_barChart.invalidate();


    }


    private void fiveCustomerDueAmountNew(View view, List<BarEntry> entries) {


        List<String> xvalues = Arrays.asList(">90", "61-90", "0-30", "31-60");
        //List<String> xvalues= Arrays.asList("Jan","Feb","March","April","May","June");

        BarChart customer_barChart = view.findViewById(R.id.chartTopFiveCustomersByDuePayment);


        /*CustomMarkerViewReceivables markerView = new CustomMarkerViewReceivables(requireContext(), R.layout.barchart_marker, MainActivity_B2C.ReceivableentriesForMarker);


        customer_barChart.setMarker(markerView);*/

        RoundedBarChart roundedBarChartRenderer = new RoundedBarChart(customer_barChart, customer_barChart.getAnimator(), customer_barChart.getViewPortHandler());
        roundedBarChartRenderer.setmRadius(5f);
        customer_barChart.setRenderer(roundedBarChartRenderer);

        customer_barChart.setDrawBarShadow(false);
        customer_barChart.setDrawValueAboveBar(false);
        customer_barChart.getDescription().setEnabled(false);
        customer_barChart.setDrawGridBackground(false);


        customer_barChart.getAxisRight().setEnabled(false);
        Legend legend = customer_barChart.getLegend();
        legend.setEnabled(false);


        List<IBarDataSet> dataSets = new ArrayList<>();
        BarDataSet dataSet = new BarDataSet(entries, "Values");
        try {
            dataSet.setColor(requireActivity().getResources().getColor(R.color.orange_bar_color));
        } catch (Exception e) {

        }
        dataSet.setDrawValues(false);
        dataSets.add(dataSet);


        BarData data = new BarData(dataSets);
        data.setBarWidth(0.75f);
        try {
            data.setValueTextColor(requireActivity().getResources().getColor(R.color.white));
        } catch (Exception e) {

        }
        //  data.setValueTextColor(Color.WHITE);
        customer_barChart.setData(data);
        customer_barChart.setFitBars(false);


        // customer_barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xvalues));
        customer_barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        try {
            customer_barChart.getXAxis().setTextColor(requireActivity().getResources().getColor(R.color.black));
        } catch (Exception e) {

        }
        customer_barChart.getXAxis().setLabelCount(13, false);
        customer_barChart.getXAxis().setDrawGridLines(false);

        customer_barChart.getXAxis().setGranularity(1f);


        customer_barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xaxisOfDueCustomer));


        YAxis yAxis = customer_barChart.getAxisLeft();
        try {
            yAxis.setTextColor(requireActivity().getResources().getColor(R.color.black));
        } catch (Exception e) {

        }
        yAxis.setAxisMinimum(0f);

        customer_barChart.setTouchEnabled(false);
        customer_barChart.setDrawBarShadow(false);


        //hide grid lines
        customer_barChart.getAxisLeft().setDrawGridLines(false);


        //remove right y-axis
        customer_barChart.getAxisRight().setEnabled(false);

        //remove legend


        //remove description label
        customer_barChart.getDescription().setEnabled(false);

        //add animation
        customer_barChart.animateY(2000);


        //draw chart
        customer_barChart.invalidate();


    }


    private void fiveCustomerSalesAmount(View view, List<BarEntry> entries) {


        List<String> xvalues = Arrays.asList(">90", "61-90", "0-30", "31-60");
        //List<String> xvalues= Arrays.asList("Jan","Feb","March","April","May","June");

        BarChart customer_barChart = view.findViewById(R.id.chartTopFiveCustomersBySalesPayment);


       /* CustomMarkerViewReceivables markerView = new CustomMarkerViewReceivables(requireContext(), R.layout.barchart_marker, MainActivity_B2C.ReceivableentriesForMarker);


        customer_barChart.setMarker(markerView);
*/

        RoundedBarChart roundedBarChartRenderer = new RoundedBarChart(customer_barChart, customer_barChart.getAnimator(), customer_barChart.getViewPortHandler());
        roundedBarChartRenderer.setmRadius(5f);
        customer_barChart.setRenderer(roundedBarChartRenderer);

        customer_barChart.setDrawBarShadow(false);
        customer_barChart.setDrawValueAboveBar(false);
        customer_barChart.getDescription().setEnabled(false);
        customer_barChart.setDrawGridBackground(false);


        customer_barChart.getAxisRight().setEnabled(false);
        Legend legend = customer_barChart.getLegend();
        legend.setEnabled(false);


        List<IBarDataSet> dataSets = new ArrayList<>();
        BarDataSet dataSet = new BarDataSet(entries, "Values");
        try {
            dataSet.setColor(requireActivity().getResources().getColor(R.color.purple_700));
        } catch (Exception e) {

        }
        dataSet.setDrawValues(false);
        dataSets.add(dataSet);


        BarData data = new BarData(dataSets);
        data.setBarWidth(0.3f);
        try {
            data.setValueTextColor(requireActivity().getResources().getColor(R.color.white));
        } catch (Exception e) {

        }
        //  data.setValueTextColor(Color.WHITE);
        customer_barChart.setData(data);
        customer_barChart.setFitBars(false);


        // customer_barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xvalues));
        customer_barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        try {
            customer_barChart.getXAxis().setTextColor(requireActivity().getResources().getColor(R.color.black));
        } catch (Exception e) {

        }
        //   customer_barChart.getXAxis().setLabelCount(13, false);
        customer_barChart.getXAxis().setDrawGridLines(false);

        customer_barChart.getXAxis().setGranularity(1f);

        customer_barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xaxisOfDueCustomer));


        YAxis yAxis = customer_barChart.getAxisLeft();
        try {
            yAxis.setTextColor(requireActivity().getResources().getColor(R.color.black));
        } catch (Exception e) {

        }
        yAxis.setAxisMinimum(0f);

        customer_barChart.setTouchEnabled(false);
        customer_barChart.setDrawBarShadow(false);


        //hide grid lines
        customer_barChart.getAxisLeft().setDrawGridLines(false);


        //remove right y-axis
        customer_barChart.getAxisRight().setEnabled(false);

        //remove legend


        //remove description label
        customer_barChart.getDescription().setEnabled(false);

        //add animation
        customer_barChart.animateY(2000);


        //draw chart
        customer_barChart.invalidate();


    }

    private void totalSalePurchaseLineGraph(LineChart lineChart) {
        // Sample data for two lines
        List<Entry> entries1 = new ArrayList<>();
        entries1.add(new Entry(0, 30));
        entries1.add(new Entry(1, 40));
        entries1.add(new Entry(2, 35));
        entries1.add(new Entry(3, 50));
        entries1.add(new Entry(4, 45));
        entries1.add(new Entry(5, 65));
        entries1.add(new Entry(6, 75));
        entries1.add(new Entry(7, 35));
        entries1.add(new Entry(8, 85));
        entries1.add(new Entry(9, 35));
        entries1.add(new Entry(10, 95));
        entries1.add(new Entry(11, 35));

        List<Entry> entries2 = new ArrayList<>();
        entries2.add(new Entry(0, 30));
        entries2.add(new Entry(1, 35));
        entries2.add(new Entry(2, 75));
        entries2.add(new Entry(3, 50));
        entries2.add(new Entry(4, 95));
        entries2.add(new Entry(5, 65));
        entries2.add(new Entry(6, 75));
        entries2.add(new Entry(7, 95));
        entries2.add(new Entry(8, 85));
        entries2.add(new Entry(9, 35));
        entries2.add(new Entry(10, 55));
        entries2.add(new Entry(11, 35));

        // Create LineDataSet for each line
        LineDataSet dataSet1 = new LineDataSet(Saleentries, "Sale");
        try {
            dataSet1.setColor(requireActivity().getResources().getColor(R.color.purple_700));
        } catch (Exception e) {

        }
        dataSet1.setValueTextColor(R.color.white);
        dataSet1.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return ""; // Return empty string to disable top value
            }
        });

        LineDataSet dataSet2 = new LineDataSet(Purchaseentries, "Purchase");
        try {
            dataSet2.setColor(requireActivity().getResources().getColor(R.color.orange_bar_color));
        } catch (Exception e) {

        }
        dataSet2.setValueTextColor(R.color.white);
        dataSet2.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return ""; // Return empty string to disable top value
            }
        });


        // Add LineDataSet to LineData
        LineData lineData = new LineData(dataSet1, dataSet2);

        // Set data to the chart
        lineChart.setData(lineData);


        // Customize x-axis
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.setDrawGridBackground(false);
        // Customize y-axis
        lineChart.getAxisLeft().setDrawGridLines(false); // Disable grid lines on the left Y-axis
        lineChart.getAxisRight().setDrawGridLines(false); // Disable grid lines on the right Y-axis
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.getAxisLeft().setAxisMinimum(0);
        lineChart.setDescription(null);


        lineChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xaxisOfMonth));

       /* CustomMarkerView markerView = new CustomMarkerView(requireContext(), R.layout.barchart_marker, SalesValueForMarker);
        lineChart.setMarker(markerView);*/


        // Customize legend
        Legend legend = lineChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);
        legend.setFormSize(14);
    }


    private void totalReceivablesPayablesLineGraph(LineChart lineChart) {
        // Sample data for two lines
        List<Entry> entries1 = new ArrayList<>();
        entries1.add(new Entry(0, 30));
        entries1.add(new Entry(1, 40));
        entries1.add(new Entry(2, 35));
        entries1.add(new Entry(3, 50));
        entries1.add(new Entry(4, 45));
        entries1.add(new Entry(5, 65));
        entries1.add(new Entry(6, 75));
        entries1.add(new Entry(7, 35));
        entries1.add(new Entry(8, 85));
        entries1.add(new Entry(9, 35));
        entries1.add(new Entry(10, 95));
        entries1.add(new Entry(11, 35));

        List<Entry> entries2 = new ArrayList<>();
        entries2.add(new Entry(0, 30));
        entries2.add(new Entry(1, 35));
        entries2.add(new Entry(2, 75));
        entries2.add(new Entry(3, 50));
        entries2.add(new Entry(4, 95));
        entries2.add(new Entry(5, 65));
        entries2.add(new Entry(6, 75));
        entries2.add(new Entry(7, 95));
        entries2.add(new Entry(8, 85));
        entries2.add(new Entry(9, 35));
        entries2.add(new Entry(10, 55));
        entries2.add(new Entry(11, 35));

        List<String> xvalues = Arrays.asList("0-30", "31-45", "46-60", "61-90", ">90");


        // Create LineDataSet for each line
        LineDataSet dataSet1 = new LineDataSet(receivableentries, "Receivables");
        try {
            dataSet1.setColor(requireActivity().getResources().getColor(R.color.green));
        } catch (Exception e) {

        }
        dataSet1.setValueTextColor(R.color.white);
        dataSet1.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return ""; // Return empty string to disable top value
            }
        });

        LineDataSet dataSet2 = new LineDataSet(Paybleentries, "Payables");
        try {
            dataSet2.setColor(requireActivity().getResources().getColor(R.color.payables_graph_color));
        } catch (Exception e) {

        }
        dataSet2.setValueTextColor(R.color.white);
        dataSet2.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return ""; // Return empty string to disable top value
            }
        });

        // Add LineDataSet to LineData
        LineData lineData = new LineData(dataSet1, dataSet2);

        // Set data to the chart
        lineChart.setData(lineData);


        // Customize x-axis
        lineChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(receivableentriesXaxis));
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.setDrawGridBackground(false);
        // Customize y-axis
        lineChart.getAxisLeft().setDrawGridLines(false); // Disable grid lines on the left Y-axis
        lineChart.getAxisRight().setDrawGridLines(false); // Disable grid lines on the right Y-axis
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.setDescription(null);


        // Customize legend
        Legend legend = lineChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);
        legend.setFormSize(14);
    }

    Long startDatelng = (long) 0.0;
    Long endDatelng = (long) 0.0;
    MaterialDatePicker<Pair<Long, Long>> materialDatePicker;
    String startDate = Globals.firstDateOfFinancialYear();
    String endDate = Globals.lastDateOfFinancialYear();

    public static List<Entry> Purchaseentries = new ArrayList<>();
    public static List<Entry> Saleentries = new ArrayList<>();
    public static List<String> SalesValueForMarker = new ArrayList<>();


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
            //  binding.contentData.tvDateText.setText("Today");
            callDashboardCounter_Payables();


            callDashboardCounter_Receiable();


            bottomSheetDialog.dismiss();
        });

        bindingDate.tvYesterdayDateBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.yesterDayCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.Date_yyyy_mm_dd(startDatelng);
            endDate = Globals.Date_yyyy_mm_dd(endDatelng);
            alertDialog.show();
            //   binding.contentData.tvDateText.setText("Yesterday");
            //  callDashboardCounter();
            callDashboardCounter_Payables();
            callDashboardCounter_Receiable();

            bottomSheetDialog.dismiss();
        });
        bindingDate.tvThisWeekDateBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.thisWeekCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.thisWeekfirstDayOfMonth();
            endDate = Globals.thisWeekLastDayOfMonth();
            alertDialog.show();
            //  binding.contentData.tvDateText.setText("This Week");
/*
            callDashboardCounter();
*/
            callDashboardCounter_Payables();
            callDashboardCounter_Receiable();

            bottomSheetDialog.dismiss();
        });


        bindingDate.tvThisMonthBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.thisMonthCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.firstDateOfMonth();
            endDate = Globals.lastDateOfMonth();
            alertDialog.show();
            //  binding.contentData.tvDateText.setText("This Month");

            callDashboardCounter_Payables();
            callDashboardCounter_Receiable();
            bottomSheetDialog.dismiss();

        });
        bindingDate.tvLastMonthDateBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.lastMonthCal().getTimeInMillis();
            endDatelng = Globals.thisMonthCal().getTimeInMillis();
            startDate = Globals.lastMonthFirstDate();
            endDate = Globals.lastMonthlastDate();
            alertDialog.show();
            //  binding.contentData.tvDateText.setText("" + Globals.convertDateFormatInReadableFormat(startDate) + " to " + Globals.convertDateFormatInReadableFormat(endDate));

            callDashboardCounter_Payables();
            callDashboardCounter_Receiable();

            bottomSheetDialog.dismiss();
        });
        bindingDate.tvThisQuarterDateBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.thisQuarterCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.lastQuarterFirstDate();
            endDate = Globals.lastQuarterlastDate();
            alertDialog.show();
            //   binding.contentData.tvDateText.setText("" + Globals.convertDateFormatInReadableFormat(startDate) + " to " + Globals.convertDateFormatInReadableFormat(endDate));
            callDashboardCounter_Payables();
            callDashboardCounter_Receiable();

            bottomSheetDialog.dismiss();
        });
        bindingDate.tvThisYearDateBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.thisyearCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.firstDateOfFinancialYear();
            endDate = Globals.lastDateOfFinancialYear();
            alertDialog.show();
            //  binding.contentData.tvDateText.setText("" + Globals.convertDateFormatInReadableFormat(startDate) + " to " + Globals.convertDateFormatInReadableFormat(endDate));
            callDashboardCounter_Payables();
            callDashboardCounter_Receiable();

            bottomSheetDialog.dismiss();
        });
        bindingDate.tvLastYearBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.lastyearCal().getTimeInMillis();
            endDatelng = Globals.thisyearCal().getTimeInMillis();
            startDate = Globals.lastYearFirstDate();
            endDate = Globals.lastYearLastDate();
            alertDialog.show();
            //  binding.contentData.tvDateText.setText("" + Globals.convertDateFormatInReadableFormat(startDate) + " to " + Globals.convertDateFormatInReadableFormat(endDate));
            callDashboardCounter_Payables();
            callDashboardCounter_Receiable();

            bottomSheetDialog.dismiss();
        });
        bindingDate.tvAllBottomSheetSelectDate.setOnClickListener(view -> {
            startDate = "";
            endDate = "";
            alertDialog.show();
            //   binding.contentData.tvDateText.setText("All");
            callDashboardCounter_Payables();
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
                //   binding.contentData.tvDateText.setText("" + Globals.convertDateFormatInReadableFormat(startDate) + " to " + Globals.convertDateFormatInReadableFormat(endDate));
                callDashboardCounter_Payables();
                callDashboardCounter_Receiable();


            }
        });


    }


    private void callDashboardCounter_Receiable() {
        alertDialog.show();


        HashMap obj = new HashMap<String, String>();
        obj.put("Filter", "");
        obj.put("Code", "");
        obj.put("Type", "Gross");
        obj.put("FromDate", "");
        obj.put("ToDate", "");
        obj.put("SalesPersonCode", Prefs.getString(Globals.SalesEmployeeCode, ""));
        Call<DashboardCounterResponse> call = NewApiClient.getInstance().getApiService().getDashBoardCounterForLedger(obj);
        call.enqueue(new Callback<DashboardCounterResponse>() {
            @Override
            public void onResponse(Call<DashboardCounterResponse> call, Response<DashboardCounterResponse> response) {
                if (response.code() == 200) {
                    alertDialog.dismiss();
                    Prefs.putString(Globals.Total_Receivables, Globals.numberToK(String.valueOf(response.body().getData().get(0).getDifferenceAmount())));

                    try {
                        binding.tvRecievableCunt.setText(requireActivity().getResources().getString(R.string.Rs) + " " + Globals.numberToK(String.valueOf(response.body().getData().get(0).getDifferenceAmount())));
                    } catch (Exception e) {

                    }

                    //  binding.contentData.totalPendings.setText(getResources().getString(R.string.Rs) + " " + Globals.numberToK(String.valueOf(response.body().getData().get(0).getDifferenceAmount())));
                    //  binding.contentData.totalPendings.setText(requireActivity().getResources().getString(R.string.Rs) + " " + Globals.numberToK(String.valueOf(response.body().getData().get(0).getTotalPendingSales())));

                } else {
                    alertDialog.dismiss();

               /*     Gson gson = new GsonBuilder().create();
                    LeadResponse mError = new LeadResponse();
                    try {
                        String s = response.errorBody().string();
                        mError = gson.fromJson(s, LeadResponse.class);
                    } catch (IOException e) {
                        //handle failure to read error
                    }*/
                    //Toast.makeText(CreateContact.this, msz, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<DashboardCounterResponse> call, Throwable t) {
                alertDialog.dismiss();

                Toast.makeText(requireContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        //  callBplistApi(bp_spinner, cp_spinner);
    }

    private void callDashboardCounter_Payables() {
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
                        try {
                            binding.tvPaybleCount.setText(requireActivity().getResources().getString(R.string.Rs) + " " + Globals.numberToK(String.valueOf(response.body().getData().get(0).getDifferenceAmount())));
                        } catch (Exception e) {

                        }
                        //   binding.contentData.totalPendings.setText(requireActivity().getResources().getString(R.string.Rs) + " " + Globals.numberToK(String.valueOf(response.body().getData().get(0).getTotalPendingSales())));
                    }

                } else {
                    alertDialog.dismiss();

               /*     Gson gson = new GsonBuilder().create();
                    LeadResponse mError = new LeadResponse();
                    try {
                        String s = response.errorBody().string();
                        mError = gson.fromJson(s, LeadResponse.class);
                    } catch (IOException e) {
                        //handle failure to read error
                    }*/
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


    private void purchaseGraphApi() {

        HashMap obj = new HashMap<String, String>();
        obj.put("FromDate", "2023-04-01");
        obj.put("ToDate", "2024-03-31");
        obj.put("SalesPersonCode", Prefs.getString(Globals.SalesEmployeeCode, ""));

        Call<SalesGraphResponse> call = NewApiClient.getInstance().getApiService().salesGraphPurchase(obj);
        call.enqueue(new Callback<SalesGraphResponse>() {
            @Override
            public void onResponse(Call<SalesGraphResponse> call, Response<SalesGraphResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().status == 200) {
                        if (response.body() != null && response.body().data.size() > 0)
                            Purchaseentries.clear();

                        for (int i = 0; i < response.body().data.size(); i++) {
                            Purchaseentries.add(new BarEntry(i, Float.parseFloat(response.body().data.get(i).getMonthlySales())));
                        }
                        // opengraph();


                        PayableGraphApi();

                    }


                }
            }

            @Override
            public void onFailure(Call<SalesGraphResponse> call, Throwable t) {

            }
        });
    }


    private void SalesGraphApi() {

        HashMap obj = new HashMap<String, String>();
        obj.put("FromDate", "2023-04-01");
        obj.put("ToDate", "2024-03-31");
        obj.put("SalesPersonCode", Prefs.getString(Globals.SalesEmployeeCode, ""));

        Call<SalesGraphResponse> call = NewApiClient.getInstance().getApiService().salesGraph(obj);
        call.enqueue(new Callback<SalesGraphResponse>() {
            @Override
            public void onResponse(Call<SalesGraphResponse> call, Response<SalesGraphResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().status == 200) {
                        if (response.body() != null && response.body().data.size() > 0)
                            Saleentries.clear();
                        SalesValueForMarker.clear();

                        for (int i = 0; i < response.body().data.size(); i++) {
                            Saleentries.add(new BarEntry(i, Float.parseFloat(response.body().data.get(i).getMonthlySales())));
                            SalesValueForMarker.add(Globals.convertToLakhAndCroreFromString(response.body().data.get(i).getMonthlySales()));
                        }
                        // opengraph();
                        purchaseGraphApi();

                    }


                }
            }

            @Override
            public void onFailure(Call<SalesGraphResponse> call, Throwable t) {

            }
        });
    }


    /********************** Payable Graph*************************/

    private void PayableGraphApi() {

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
                if (response .isSuccessful()) {
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
                        ReceivableGraphApi();
                    }


                }
            }

            @Override
            public void onFailure(Call<ResponseReceivableGraph> call, Throwable t) {

            }
        });
    }


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

        Call<ResponseReceivableGraph> call = NewApiClient.getInstance().getApiService().receivableDueMonthGraph(obj);
        call.enqueue(new Callback<ResponseReceivableGraph>() {
            @Override
            public void onResponse(Call<ResponseReceivableGraph> call, Response<ResponseReceivableGraph> response) {
                if (response .isSuccessful()) {
                    if (response.body().status == 200) {
                        if (response.body() != null && response.body().data.size() > 0)
                            receivableentries.clear();
                        receivableentriesXaxis.clear();
                        receivableentriesForMarker.clear();
                        for (int i = 0; i < response.body().data.size(); i++) {
                            ArrayList<String> daysGroup = new ArrayList<>();
                           /* if (response.body().data.get(i).getOverDueDaysGroup().equalsIgnoreCase("")) {

                            }*/
                            receivableentriesXaxis.add(response.body().getData().get(i).getOverDueDaysGroup());
                            receivableentries.add(new BarEntry(i, (float) response.body().getData().get(i).getTotalDue()));
                            receivableentriesForMarker.add(String.valueOf(Globals.convertToLakhAndCroreFromString(response.body().data.get(i).getTotalDue())));

                        }
                        // opengraph();
                        totalSalePurchaseLineGraph(binding.chartTotalSalePurchase);
                        totalReceivablesPayablesLineGraph(binding.chartTotalReceivablesPayables);
                    }


                }
            }

            @Override
            public void onFailure(Call<ResponseReceivableGraph> call, Throwable t) {

            }
        });
    }

}