package com.example.insult.pojo

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Insult {

        @SerializedName("number")
        @Expose
        var number: String? = null

        @SerializedName("language")
        @Expose
        var language: String? = null


        @SerializedName("insult")
        @Expose
        var insult: String? = null

        @SerializedName("created")
        @Expose
        var created: String? = null

        @SerializedName("shown")
        @Expose
        var shown: String? = null

        @SerializedName("createdby")
        @Expose
        var createdby: String? = null

        @SerializedName("active")
        @Expose
        var active: String? = null

        @SerializedName("comment")
        @Expose
        var comment: String? = null

}