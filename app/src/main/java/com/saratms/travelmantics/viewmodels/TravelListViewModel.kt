package com.saratms.travelmantics.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.saratms.travelmantics.models.TravelDeal
import com.saratms.travelmantics.utilities.FirebaseUtil

/**
 * Created by Sarah Al-Shamy on 04/08/2019.
 */
class TravelListViewModel : ViewModel() {

    val VISIBLE: Int = 0
    val GONE: Int = 8

    private val _travelDeals = MutableLiveData<List<TravelDeal>>()
    val travelDeals: LiveData<List<TravelDeal>>
        get() = _travelDeals

    private val _isLoading = MutableLiveData<Int>()
    val isLoading: LiveData<Int>
        get() = _isLoading


    fun fetchTravelDeals() {
        _isLoading.value = VISIBLE
        var travelDealList = mutableListOf<TravelDeal>()

        FirebaseUtil.openFirebaseReference(TRAVEL_DEALS)
        FirebaseUtil.databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    var travelDeal = it.getValue(TravelDeal::class.java) as TravelDeal
                    travelDeal.id = it.key
                    travelDealList.add(travelDeal)
                }
                _travelDeals.value = travelDealList
            }

            override fun onCancelled(error: DatabaseError) {
                _isLoading.value = GONE
            }
        })
    }

    fun finishLoading() {
        _isLoading.value = GONE
    }
}