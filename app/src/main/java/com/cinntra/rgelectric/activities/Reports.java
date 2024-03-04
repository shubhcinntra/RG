package com.cinntra.rgelectric.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.cinntra.rgelectric.R;
import com.cinntra.rgelectric.fragments.CreditNotes_Fragment;
import com.cinntra.rgelectric.fragments.DueFragment;
import com.cinntra.rgelectric.fragments.DuePaymentZoneFragment;
import com.cinntra.rgelectric.fragments.PaymentCollection_Fragment;
import com.cinntra.rgelectric.fragments.Ledger_Comp_Fragment;
import com.cinntra.rgelectric.fragments.PaymentCollection_ZoneFragment;
import com.cinntra.rgelectric.fragments.PaymentReceipt_Fragment;
import com.cinntra.rgelectric.globals.Globals;
import com.cinntra.rgelectric.model.Customers_Report;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Reports extends AppCompatActivity {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.groupby_dropdown)
    Spinner groupby_dropdown;
    @BindView(R.id.salesvalue)
    TextView salesvalue;
    @BindView(R.id.type_dropdown)
    Spinner type_dropdown;
    @BindView(R.id.from_to_date)
    TextView from_to_date;
    String groupCode, filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_reports);
        ButterKnife.bind(this);
        //   Toast.makeText(this, "Reports", Toast.LENGTH_SHORT).show();
        toolbar.getMenu().findItem(R.id.filterAtoZ).setChecked(true);

        ActionBar actionBar = getSupportActionBar();
//        getActionBar().hide();

        if (Prefs.getString("ForReports", "").equalsIgnoreCase("Receivable")) {

            if (Prefs.getString(Globals.forSalePurchase, Globals.Sale).equalsIgnoreCase(Globals.Purchase)) {
                toolbar.setTitle("Payable");
            } else {
                toolbar.setTitle("Receivable");
            }

            toolbar.getMenu().findItem(R.id.info_trans).setVisible(false);
            toolbar.getMenu().findItem(R.id.ledger).setVisible(false);
            toolbar.getMenu().findItem(R.id.share_received).setVisible(true);
            toolbar.getMenu().findItem(R.id.filterAtoZ).setVisible(true);
            toolbar.getMenu().findItem(R.id.filterZtoA).setVisible(true);
            toolbar.getMenu().findItem(R.id.calendar).setVisible(false);
            toolbar.getMenu().findItem(R.id.filterAmount).setVisible(true);
            toolbar.getMenu().findItem(R.id.clearAllFilter).setVisible(true);

            Prefs.putString(Globals.FROM_DATE_receivable, "All");

            //groupby_dropdown.setSelection(5);

            loadLedgerCompFragment(new PaymentCollection_Fragment(salesvalue, from_to_date, type_dropdown,groupby_dropdown));
        }else if (Prefs.getString("ForReports", "").equalsIgnoreCase("DueZoneRe")) {

            toolbar.setTitle("Zones");
            toolbar.getMenu().findItem(R.id.info_trans).setVisible(false);
            toolbar.getMenu().findItem(R.id.ledger).setVisible(false);
            toolbar.getMenu().findItem(R.id.share_received).setVisible(true);
            toolbar.getMenu().findItem(R.id.filterAtoZ).setVisible(true);
            toolbar.getMenu().findItem(R.id.filterZtoA).setVisible(true);
            toolbar.getMenu().findItem(R.id.calendar).setVisible(false);
            toolbar.getMenu().findItem(R.id.filterAmount).setVisible(true);
            toolbar.getMenu().findItem(R.id.clearAllFilter).setVisible(true);

            //   Prefs.putString(Globals.FROM_DATE_receivable, "All");

            //groupby_dropdown.setSelection(5);

            loadLedgerCompFragment(new DuePaymentZoneFragment(salesvalue, from_to_date, type_dropdown));
        }


        else if (Prefs.getString("ForReports", "").equalsIgnoreCase("ZoneRe")) {

            if (Prefs.getString(Globals.forSalePurchase, Globals.Sale).equalsIgnoreCase(Globals.Purchase)) {
                toolbar.setTitle("Payable");
            } else {
                toolbar.setTitle("Receivable");
            }

            toolbar.getMenu().findItem(R.id.info_trans).setVisible(false);
            toolbar.getMenu().findItem(R.id.ledger).setVisible(false);
            toolbar.getMenu().findItem(R.id.share_received).setVisible(true);
            toolbar.getMenu().findItem(R.id.filterAtoZ).setVisible(true);
            toolbar.getMenu().findItem(R.id.filterZtoA).setVisible(true);
            toolbar.getMenu().findItem(R.id.calendar).setVisible(false);
            toolbar.getMenu().findItem(R.id.filterAmount).setVisible(true);
            toolbar.getMenu().findItem(R.id.clearAllFilter).setVisible(true);

            Prefs.putString(Globals.FROM_DATE_receivable, "All");

            //groupby_dropdown.setSelection(5);

            loadLedgerCompFragment(new PaymentCollection_ZoneFragment(salesvalue, from_to_date, type_dropdown));
        } else if (Prefs.getString("ForReports", "").equalsIgnoreCase("payment")) {

            toolbar.setTitle("Dues");
            toolbar.getMenu().findItem(R.id.info_trans).setVisible(false);
            toolbar.getMenu().findItem(R.id.ledger).setVisible(false);
            toolbar.getMenu().findItem(R.id.share_received).setVisible(true);
            toolbar.getMenu().findItem(R.id.filterAtoZ).setVisible(true);
            toolbar.getMenu().findItem(R.id.filterZtoA).setVisible(true);
            toolbar.getMenu().findItem(R.id.calendar).setVisible(false);
            toolbar.getMenu().findItem(R.id.filterAmount).setVisible(true);
            toolbar.getMenu().findItem(R.id.clearAllFilter).setVisible(true);

            Prefs.putString(Globals.OVER_DUE_DATE_PREF, "7");

            //groupby_dropdown.setSelection(5);
            loadLedgerCompFragment(new DueFragment(salesvalue, from_to_date, type_dropdown,groupby_dropdown));
        } else if (Prefs.getString("ForReports", "").equalsIgnoreCase("overDue")) {

            toolbar.setTitle("OverDues");
            toolbar.getMenu().findItem(R.id.info_trans).setVisible(false);
            toolbar.getMenu().findItem(R.id.ledger).setVisible(false);
            toolbar.getMenu().findItem(R.id.share_received).setVisible(true);
            toolbar.getMenu().findItem(R.id.filterAtoZ).setVisible(true);
            toolbar.getMenu().findItem(R.id.filterZtoA).setVisible(true);
            toolbar.getMenu().findItem(R.id.calendar).setVisible(false);
            toolbar.getMenu().findItem(R.id.filterAmount).setVisible(true);
            toolbar.getMenu().findItem(R.id.clearAllFilter).setVisible(true);

            Prefs.putString(Globals.FROM_DATE_receivable, "All");

            //groupby_dropdown.setSelection(5);
            loadLedgerCompFragment(new DueFragment(salesvalue, from_to_date, type_dropdown,groupby_dropdown));
        } else if (Prefs.getString("ForReports", "").equalsIgnoreCase("MainActivity_B2C_CreditNotes")) {
            toolbar.setTitle("Total Credits");
            toolbar.getMenu().findItem(R.id.info_trans).setVisible(false);
            toolbar.getMenu().findItem(R.id.ledger).setVisible(false);


            loadLedgerCompFragment(new CreditNotes_Fragment(salesvalue, from_to_date, type_dropdown));
        } else if (Prefs.getString("ForReports", "").equalsIgnoreCase("ReceiptLedger")) {


            if (Prefs.getString(Globals.forSalePurchase, Globals.Sale).equalsIgnoreCase(Globals.Purchase)) {
                toolbar.setTitle("Total Payment");
            } else {
                toolbar.setTitle("Total Received");
            }

            toolbar.getMenu().findItem(R.id.info_trans).setVisible(false);
            toolbar.getMenu().findItem(R.id.ledger).setVisible(false);
            toolbar.getMenu().findItem(R.id.share_received).setVisible(true);
            toolbar.getMenu().findItem(R.id.filterAtoZ).setVisible(true);
            toolbar.getMenu().findItem(R.id.filterZtoA).setVisible(true);
            // toolbar.getMenu().findItem(R.id.filterDate).setVisible(true);
            toolbar.getMenu().findItem(R.id.filterAmount).setVisible(true);
            toolbar.getMenu().findItem(R.id.clearAllFilter).setVisible(true);


            loadLedgerCompFragment(new PaymentReceipt_Fragment(salesvalue, from_to_date, type_dropdown));
        } else {
            toolbar.setTitle("Total Sales");
            toolbar.getMenu().findItem(R.id.info_trans).setVisible(false);
            toolbar.getMenu().findItem(R.id.ledger).setVisible(false);
            toolbar.getMenu().findItem(R.id.ledger_menu).setVisible(false);
            toolbar.getMenu().findItem(R.id.filterAtoZ).setVisible(true);
            toolbar.getMenu().findItem(R.id.filterAtoZ).setChecked(true);
            toolbar.getMenu().findItem(R.id.filterZtoA).setVisible(true);
            toolbar.getMenu().findItem(R.id.filterDate).setVisible(true);
            toolbar.getMenu().findItem(R.id.filterAmount).setVisible(true);

            //  Toast.makeText(this, "" + Prefs.getString(Globals.PREFS_ATOZ, ""), Toast.LENGTH_SHORT).show();


          /*  toolbar.getMenu().findItem(R.id.filterAtoZ).setChecked(Prefs.getString(Globals.PREFS_ATOZ, "").equalsIgnoreCase(Globals.ATOZ));
            toolbar.getMenu().findItem(R.id.filterZtoA).setChecked(Prefs.getString(Globals.PREFS_ATOZ, "").equalsIgnoreCase(Globals.ZTOA));
            toolbar.getMenu().findItem(R.id.filterAmount).setChecked(Prefs.getString(Globals.PREFS_Amount, "").equalsIgnoreCase(Globals.DESC));*/


            loadLedgerCompFragment(new Ledger_Comp_Fragment(salesvalue, from_to_date, type_dropdown, groupby_dropdown, "", "", ""));
        }
        // loadLedgerCompFragment(new Ledger_Comp_Fragment(salesvalue,type_dropdown));//

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    private void loadLedgerCompFragment(Fragment fragment) {
        Bundle b = new Bundle();
        b.putSerializable(Globals.LedgerCompanyData, customerList);
//        Ledger_Comp_Fragment fragment = new Ledger_Comp_Fragment();
        fragment.setArguments(b);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        if (fm.getBackStackEntryCount() > 0)
            transaction.replace(R.id.container, fragment);
        else
            transaction.add(R.id.container, fragment);
        transaction.commit();


    }

    ArrayList<Customers_Report> customerList = new ArrayList<>();


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.transaction_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.calendar:
                // Globals.selectDat(this);
                break;
            case R.id.search:
                Toast.makeText(this, "click new ", Toast.LENGTH_SHORT).show();

                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

        //   Toast.makeText(this, "call", Toast.LENGTH_SHORT).show();

    }
}