package com.siafis.apps.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Hasil(
    var nama: String? = "",
    val hasil: Double? = 0.0,
    val nilai: Double? = 0.0,
    val predikat: String? = "",
    val tingkat: Int? = 0
) : Parcelable