package org.wit.danielsapplication.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Session  (
    val userid: String = "",
    val resource: String = "",
    val time: String = "",
    val date: String = "",
    var session_id: String = ""
) : Parcelable