package com.example.kotlinsandwichapp.model

class Sandwich {
    var mainName: String? = null
    var alsoKnownAs: List<String>? = null
    var placeOfOrigin: String? = null
    var description: String? = null
    var image: String? = null
    var ingredients: List<String>? = null

    /**
     * No args constructor for use in serialization
     */
    constructor() {}

    constructor(
        mainName: String?,
        alsoKnownAs: List<String>?,
        placeOfOrigin: String?,
        description: String?,
        image: String?,
        ingredients: List<String>?
    ) {
        this.mainName = mainName
        this.alsoKnownAs = alsoKnownAs
        this.placeOfOrigin = placeOfOrigin
        this.description = description
        this.image = image
        this.ingredients = ingredients
    }

}