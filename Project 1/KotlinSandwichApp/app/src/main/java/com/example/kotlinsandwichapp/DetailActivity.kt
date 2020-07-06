package com.example.kotlinsandwichapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.kotlinsandwichapp.utils.JsonUtils
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        var sandwichDetailsStringList = resources.getStringArray(R.array.sandwich_details)
        var sandwichDetails = JsonUtils.parseSandwichJson(sandwichDetailsStringList[intent.getIntExtra("position",-1)])

        Picasso.get().load(sandwichDetails.image).into(image_iv)
        originTextView.text = sandwichDetails.placeOfOrigin
        descriptionTextView.text = sandwichDetails.description
        if (sandwichDetails.alsoKnownAs!!.isEmpty()) alsoKnownAsTextView.text= "None"
        else {
            for(str:String in sandwichDetails.alsoKnownAs!!)
            alsoKnownAsTextView.append(str+"\n")
        }
        if (sandwichDetails.ingredients!!.isEmpty()) ingredientsTextView.text= "None"
        else {
            for(str:String in sandwichDetails.ingredients!!)
                ingredientsTextView.append(str+"\n")
        }
    }
}
