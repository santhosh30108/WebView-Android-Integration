package com.byjus.acefe

import com.google.gson.annotations.SerializedName

data class PayloadData (
    val button: String,
    val data : String,
    val status:String,
    val pre_transaction:Boolean
){
}