package com.cinntra.rgelectric.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.cinntra.rgelectric.adapters.PendingDeliveryByOrderAdapter;
import com.cinntra.rgelectric.databinding.ActivityPendingByOrderBinding;
import com.cinntra.rgelectric.globals.Globals;
import com.cinntra.rgelectric.model.DataDeliveryNotePendingByOrder;
import com.cinntra.rgelectric.model.OrderWisePendingOrderData;
import com.cinntra.rgelectric.model.ResponseDeliveryNotePendingByOrder;
import com.cinntra.rgelectric.webservices.NewApiClient;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;
import java.util.HashMap;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PendingByOrderActivity extends AppCompatActivity {
    ActivityPendingByOrderBinding binding;
    String orderId="";
    OrderWisePendingOrderData responsed;
    String cardName="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityPendingByOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        cardName = getIntent().getStringExtra("cardName");
        responsed = (OrderWisePendingOrderData) getIntent().getSerializableExtra("byOrder");


        binding.toolbarPendingList.headTitle.setText(cardName);
        binding.toolbarPendingList.newQuatos.setVisibility(View.GONE);
        binding.toolbarPendingList.relativeInfoView.setVisibility(View.GONE);
        binding.toolbarPendingList.relativeCalView.setVisibility(View.GONE);
        binding.toolbarPendingList.search.setVisibility(View.INVISIBLE);
        binding.toolbarPendingList.filter.setVisibility(View.INVISIBLE);
       // binding.toolbarPendingList.filterContainer.setVisibility(View.GONE);
        binding.toolbarPendingList.back.setOnClickListener(view ->
        {
            finish();
        });

        Log.d("TAG", "onCreate: "+responsed.orderID);
        binding.loaderPending.loader.setVisibility(View.VISIBLE);
        cashDiscountApi();


    }
    PendingDeliveryByOrderAdapter adapter;
    private ArrayList<DataDeliveryNotePendingByOrder> cashList = new ArrayList<>();
    Call<ResponseDeliveryNotePendingByOrder> call;
    private void cashDiscountApi() {
        HashMap<String, String> hde = new HashMap<>();
        hde.put("OrderID",responsed.orderID);
        if(Prefs.getString(Globals.forSalePurchase,"").equalsIgnoreCase(Globals.Purchase))
        call = NewApiClient.getInstance().getApiService().getDeliveryNotePendingByOrder_purchase(hde);
        else
        call = NewApiClient.getInstance().getApiService().getDeliveryNotePendingByOrder(hde);
        call.enqueue(new Callback<ResponseDeliveryNotePendingByOrder>() {
            @Override
            public void onResponse(Call<ResponseDeliveryNotePendingByOrder> call, Response<ResponseDeliveryNotePendingByOrder> response) {
                if (response.code() == 200) {
                   // loader.setVisibility(View.GONE);
               if (response.body().getData() == null || response.body().getData().size() == 0)
                {
                 binding.loaderPending.loader.setVisibility(View.GONE);
                        Globals.setmessage(getApplicationContext());
                       // no_datafound.setVisibility(View.VISIBLE);
                    } else {
                        binding.loaderPending.loader.setVisibility(View.GONE);
                        cashList.clear();
                        cashList.addAll(response.body().getData());
                        adapter = new PendingDeliveryByOrderAdapter(PendingByOrderActivity.this, cashList,responsed.orderID);
                        binding.recyclerview.setLayoutManager(new LinearLayoutManager(PendingByOrderActivity.this, RecyclerView.VERTICAL, false));
                        binding.recyclerview.setAdapter(adapter);

                        adapter.notifyDataSetChanged();
                       // no_datafound.setVisibility(View.GONE);

                    }


                }
            }

            @Override
            public void onFailure(Call<ResponseDeliveryNotePendingByOrder> call, Throwable t) {
                binding.loaderPending.loader.setVisibility(View.GONE);
               // loader.setVisibility(View.GONE);
                Toast.makeText(PendingByOrderActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}