package com.cinntra.rgelectric.adapters;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cinntra.rgelectric.R;
import com.cinntra.rgelectric.activities.SelectedItems;
import com.cinntra.rgelectric.fragments.AddOrderForm_One_Fragment;
import com.cinntra.rgelectric.fragments.AddQuotationForm_One_Fragment;
import com.cinntra.rgelectric.interfaces.DatabaseClick;
import com.cinntra.rgelectric.model.ItemCategoryData;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    Context context;

    List<ItemCategoryData> taxList;
    DatabaseClick databaseClick;
    Dialog TaxListdialog;

    public CategoryAdapter(AddQuotationForm_One_Fragment context, List<ItemCategoryData> taxList, Dialog TaxListdialog)
     {

   this.taxList   = taxList;
    this.databaseClick = (DatabaseClick) context;
   this.TaxListdialog = TaxListdialog;
     }
     

    public CategoryAdapter(SelectedItems selectedItems, List<ItemCategoryData> data, Dialog taxListdialog) {
        this.taxList   = data;
        this.databaseClick = (DatabaseClick) selectedItems;
        this.TaxListdialog = taxListdialog;
    }

    public CategoryAdapter(AddOrderForm_One_Fragment addOrderForm_one_fragment, List<ItemCategoryData> data, Dialog taxListdialog) {
        this.taxList   = data;
        this.databaseClick = (DatabaseClick) addOrderForm_one_fragment;
        this.TaxListdialog = taxListdialog;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.tax_adapter_item,parent,false);
    return new ViewHolder(rootView);
      }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
      {
   holder.tax.setText(taxList.get(position).getCategoryName());
      }

    @Override
    public int getItemCount()
      {
    return taxList.size();
      }



    class ViewHolder extends RecyclerView.ViewHolder
       {
    private TextView tax;
    public ViewHolder(@NonNull View itemView) {
    super(itemView);
    tax    = itemView.findViewById(R.id.tax);
    itemView.setOnClickListener(new View.OnClickListener() {
     @Override
    public void onClick(View v) {

        databaseClick.onClick(taxList.get(getAdapterPosition()).getId());

         if(TaxListdialog!=null)
         TaxListdialog.dismiss();

        }
    });

      }
    }




}
