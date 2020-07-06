 package com.example.kotlinsandwichapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*


 class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var sandwichesNamesList = resources.getStringArray(R.array.sandwich_names);

        sandwichesRecyclerView.adapter = SandwichesNamesRecyclerViewAdapter(this,sandwichesNamesList);
        sandwichesRecyclerView.layoutManager =LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        sandwichesRecyclerView.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )

        }

    }
