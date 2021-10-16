package com.example.pkl_v1.model

class ModelChat(val idChat: String, val message: String, val from: String, val status: Boolean,val date:String){
    constructor():this("","","",false,"0")
}