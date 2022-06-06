package com.uniwa.moviender.data

import com.uniwa.moviender.R

val errorMessages: HashMap<Int, Int> = hashMapOf(
    Error.CANNOT_CONNECT.code to R.string.error_no_internet,
    Error.NETWORK_ERROR.code to R.string.error_network,
    Error.GENERAL.code to R.string.error_general
)