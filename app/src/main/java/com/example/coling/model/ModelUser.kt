package com.example.coling.model

data class ModelUser(var uid : String? = null,
                     var start_date : Long? = null,
                     var today_again : Boolean = false,
                     var act_num : Int? = 1) {}