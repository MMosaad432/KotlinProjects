package com.example.kotlinsandwichapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.sandwiches_names_list.view.*

class SandwichesNamesRecyclerViewAdapter(val context:Context, var sandwichesNamesArrayList: Array<String>) : RecyclerView.Adapter<SandwichesNamesRecyclerViewAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var positionSelected : Int = 0
        init {
            itemView.setOnClickListener{
                var fromMainToDetialsIntent = Intent(context.applicationContext,DetailActivity::class.java).putExtra("position",positionSelected)
                context.startActivity(fromMainToDetialsIntent)
            }
        }

        fun setData(text:String,pos:Int){
            itemView.sandwichNameTextView.text = text
            this.positionSelected = pos
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.sandwiches_names_list,parent,false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return sandwichesNamesArrayList.size;
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.setData(sandwichesNamesArrayList[position],position)
    }

}