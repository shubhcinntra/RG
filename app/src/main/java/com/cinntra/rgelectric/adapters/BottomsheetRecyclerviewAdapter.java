package com.cinntra.rgelectric.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.cinntra.rgelectric.R;
import com.cinntra.rgelectric.activities.BussinessPartners;
import com.cinntra.rgelectric.activities.CalenderActivity;
import com.cinntra.rgelectric.activities.CampaignActivity;
import com.cinntra.rgelectric.activities.CashDiscount;
import com.cinntra.rgelectric.activities.DeliveryActivity;
import com.cinntra.rgelectric.activities.ExpenseActivity;
import com.cinntra.rgelectric.activities.InventoryActivity;
import com.cinntra.rgelectric.activities.InvoiceActivity;
import com.cinntra.rgelectric.activities.LeadsActivity;
import com.cinntra.rgelectric.activities.LocationListing;
import com.cinntra.rgelectric.activities.Opportunities_Pipeline_Activity;
import com.cinntra.rgelectric.activities.OrderActivity;
import com.cinntra.rgelectric.activities.QuotationActivity;
import com.cinntra.rgelectric.activities.Sale_Inovice_Reports;
import com.cinntra.rgelectric.globals.Globals;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;


public class BottomsheetRecyclerviewAdapter extends RecyclerView.Adapter<BottomsheetRecyclerviewAdapter.ViewHolder> {
    Context context;


    ArrayList<Integer> iconlist;
    ArrayList<String> namelist;

    public BottomsheetRecyclerviewAdapter(Context context, ArrayList<Integer> iconlist, ArrayList<String> namelist) {
        this.context = context;
        this.iconlist = iconlist;
        this.namelist = namelist;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.bottomsheet_adapter, parent, false);

        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {


        holder.name.setText(namelist.get(position));
        holder.icon.setBackground(ContextCompat.getDrawable(context, iconlist.get(position)));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (position) {
                    case 0:
                        Prefs.putString(Globals.BussinessPageType, "DashBoard");
                        context.startActivity(new Intent(context, LeadsActivity.class));
                        break;
                    case 1:
                        Prefs.putString(Globals.QuotationListing, "null");
                        Prefs.putBoolean(Globals.SelectQuotation, true);
                        context.startActivity(new Intent(context, QuotationActivity.class));
                        break;
                    case 2:
                        Prefs.putString(Globals.BussinessPageType, "DashBoard");
                        context.startActivity(new Intent(context, BussinessPartners.class));
                        break;
                    case 3:
                        context.startActivity(new Intent(context, OrderActivity.class));
                        break;
                    case 4:
                        Prefs.putString(Globals.SelectOpportnity, "Dashboard");
                        context.startActivity(new Intent(context, Opportunities_Pipeline_Activity.class));
                        break;

                    case 5:
                        context.startActivity(new Intent(context, InvoiceActivity.class));
                        break;
                    case 6:
                        context.startActivity(new Intent(context, CampaignActivity.class));
                        break;
                    case 7:
                        context.startActivity(new Intent(context, DeliveryActivity.class));
                        break;
                    case 8:
                        Intent i = new Intent(context, InventoryActivity.class);
                        i.putExtra("IN_Type", "All");
                        context.startActivity(i);
                        break;
                    case 9:
                        Prefs.putString("ForReports", "MainActivity_B2C_Ledger");
                        context.startActivity(new Intent(context, Sale_Inovice_Reports.class));

                        break;
                    case 10:
                        context.startActivity(new Intent(context, CashDiscount.class));
                        break;
                    case 11:
                        context.startActivity(new Intent(context, LocationListing.class));
                        break;
                    case 12:
                        context.startActivity(new Intent(context, ExpenseActivity.class));
                        break;
                    case 13:
                        context.startActivity(new Intent(context, CalenderActivity.class));
                        break;


                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return namelist.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        ImageView icon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            icon = itemView.findViewById(R.id.icon);


        }
    }
}
