package com.saratms.travelmantics.adapters

import android.content.res.Resources
import android.graphics.Color
import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.saratms.travelmantics.R
import com.saratms.travelmantics.models.TravelDeal
import com.squareup.picasso.Picasso

/**
 * Created by Sarah Al-Shamy on 04/08/2019.
 */

@BindingAdapter("imageUrl")
fun ImageView.setImageUrl(travelDeal: TravelDeal?){
    Picasso.get().load(Uri.parse(travelDeal!!.imageUrl)).placeholder(R.mipmap.ic_launcher).into(this)
}

@BindingAdapter("imageDealUrl")
fun ImageView.setImageDealUrl(travelDeal: TravelDeal?){
    var width = Resources.getSystem().displayMetrics.widthPixels
    Picasso.get().load(Uri.parse(travelDeal!!.imageUrl)).resize(width, width *2 / 3).centerCrop().into(this)
}