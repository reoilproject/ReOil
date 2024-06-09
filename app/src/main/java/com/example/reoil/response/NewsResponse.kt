package com.example.reoil.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class NewsResponse(

	@field:SerializedName("-Nzmehl7QDc3OEs9pkhG")
	val nzmehl7QDc3OEs9pkhG: Nzmehl7QDc3OEs9pkhG? = null,

	@field:SerializedName("-Nzm_54Ai3dL81utAZ6T")
	val nzm54Ai3dL81utAZ6T: Nzm54Ai3dL81utAZ6T? = null,

	@field:SerializedName("-Nzn2Pa6C9M_Ogr5VSIF")
	val nzn2Pa6C9MOgr5VSIF: Nzn2Pa6C9MOgr5VSIF? = null
)

data class Nzmehl7QDc3OEs9pkhG(

	@field:SerializedName("imageurl")
	val imageurl: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("content")
	val content: String? = null
)

data class Nzm54Ai3dL81utAZ6T(

	@field:SerializedName("imageurl")
	val imageurl: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("content")
	val content: String? = null
)

data class Nzn2Pa6C9MOgr5VSIF(

	@field:SerializedName("imageurl")
	val imageurl: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("content")
	val content: String? = null
)

@Parcelize
data class NewsItem(
	@SerializedName("imageurl")
	val imageUrl: String? = null,

	@SerializedName("title")
	val title: String? = null,

	@SerializedName("content")
	val content: String? = null
): Parcelable

