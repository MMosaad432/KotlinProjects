package com.example.kotlinsandwichapp.utils

import com.example.kotlinsandwichapp.model.Sandwich
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*

object JsonUtils {
    fun parseSandwichJson(json: String?): Sandwich {
        val selectedSandwich = Sandwich()
        var sandwichString: JSONObject? = null
        var name: JSONObject? = null
        val alsoKnownAsArray: JSONArray
        val ingredientsArray: JSONArray
        val mainName: String
        val placeOfOrigin: String
        val description: String
        val image: String
        val alsoKnownAs: MutableList<String> =
            ArrayList()
        val ingredients: MutableList<String> =
            ArrayList()
        try {
            sandwichString = JSONObject(json)
            name = sandwichString.getJSONObject("name")
            mainName = name.getString("mainName")
            selectedSandwich.mainName = mainName
            alsoKnownAsArray = name.getJSONArray("alsoKnownAs")
            for (i in 0 until alsoKnownAsArray.length()) {
                alsoKnownAs.add(alsoKnownAsArray.getString(i))
            }
            selectedSandwich.alsoKnownAs = alsoKnownAs
            placeOfOrigin = sandwichString.getString("placeOfOrigin")
            selectedSandwich.placeOfOrigin = placeOfOrigin
            description = sandwichString.getString("description")
            selectedSandwich.description = description
            image = sandwichString.getString("image")
            selectedSandwich.image = image
            ingredientsArray = sandwichString.getJSONArray("ingredients")
            for (i in 0 until ingredientsArray.length()) {
                ingredients.add(ingredientsArray.getString(i))
            }
            selectedSandwich.ingredients = ingredients
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return selectedSandwich
    }
}