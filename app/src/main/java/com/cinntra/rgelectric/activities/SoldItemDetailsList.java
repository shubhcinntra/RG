package com.cinntra.rgelectric.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cinntra.rgelectric.R;
import com.cinntra.rgelectric.adapters.SoldItemDetails_Adapter;
import com.cinntra.rgelectric.databinding.ActivitySolidItemDetailListBinding;
import com.cinntra.rgelectric.model.SoldItem;
import com.cinntra.rgelectric.model.SoldItemResponse;

import java.util.ArrayList;


public class SoldItemDetailsList extends AppCompatActivity {

    ActivitySolidItemDetailListBinding binding;
    LinearLayoutManager layoutManager;
    ArrayList<SoldItem> AllItemList = new ArrayList<>();
    SoldItemResponse response;
    SoldItemDetails_Adapter adapter;
    String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySolidItemDetailListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        response = (SoldItemResponse) getIntent().getSerializableExtra("content");
        name = getIntent().getStringExtra("name");

        AllItemList.addAll(response.getItemOrderList());
        binding.toolbarSoldItemList.headTitle.setText(name);
        binding.toolbarSoldItemList.newQuatos.setVisibility(View.INVISIBLE);
        binding.toolbarSoldItemList.relativeInfoView.setVisibility(View.INVISIBLE);
        binding.toolbarSoldItemList.search.setVisibility(View.INVISIBLE);
        binding.toolbarSoldItemList.filter.setVisibility(View.INVISIBLE);
        binding.toolbarSoldItemList.relativeCalView.setVisibility(View.INVISIBLE);
        binding.toolbarSoldItemList.backPress.setOnClickListener(view ->
        {
            finish();
        });


        // Toast.makeText(this, AllItemList.toString(), Toast.LENGTH_SHORT).show();

        adapter = new SoldItemDetails_Adapter(SoldItemDetailsList.this, AllItemList,"soldItem");
        layoutManager = new LinearLayoutManager(SoldItemDetailsList.this, RecyclerView.VERTICAL, false);
        binding.recyclerview.setAdapter(adapter);
        binding.recyclerview.setLayoutManager(layoutManager);


    }
}