package com.saratms.travelmantics.viewmodels

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.saratms.travelmantics.models.TravelDeal
import com.saratms.travelmantics.utilities.FirebaseUtil

/**
 * Created by Sarah Al-Shamy on 04/08/2019.
 */

val TRAVEL_DEALS = "travel_deals"

class InsertViewModel : ViewModel() {
    private var _uploading = MutableLiveData<Boolean>()
    val uploading: LiveData<Boolean>
        get() = _uploading

    private val _isUploadSuccessful = MutableLiveData<Boolean>()
    val isUploadSuccessful: LiveData<Boolean>
        get() = _isUploadSuccessful

    private val _isUploadFailed = MutableLiveData<Boolean>()
    val isUploadFailed: LiveData<Boolean>
        get() = _isUploadFailed

    private val _isDeleteSuccessful = MutableLiveData<Boolean>()
    val isDeleteSuccessful: LiveData<Boolean>
        get() = _isDeleteSuccessful

    private val _isDeleteFailed = MutableLiveData<Boolean>()
    val isDeleteFailed: LiveData<Boolean>
        get() = _isDeleteFailed

    private val _isAdmin = MutableLiveData<Boolean>()
    val isAdmin: LiveData<Boolean>
    get() = _isAdmin

    private var _travelDeal = TravelDeal()
    val travelDeal: TravelDeal
        get() = _travelDeal

    init {
        _isAdmin.value = FirebaseUtil.isAdmin
    }

    fun uploadTravelDeal(travelDeal: TravelDeal, imageUri: Uri?, isNewTravelDeal: Boolean) {

        _uploading.value = true

        FirebaseUtil.openFirebaseReference(TRAVEL_DEALS)

        if (imageUri != null) {
            FirebaseUtil.storageReference.child("images/${imageUri.lastPathSegment}").putFile(imageUri)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        val result = it.result!!.metadata!!.reference!!.downloadUrl
                        result.addOnSuccessListener {
                            travelDeal.imageUrl = it.toString()
                            travelDeal.imageName = imageUri.lastPathSegment
                            if(isNewTravelDeal) uploadToDatabase(travelDeal)
                            else updateTravelDeal(travelDeal)
                        }
                    } else {
                        _isUploadFailed.value = true
                        _uploading.value = false
                    }
                }
        } else {
            updateTravelDeal(travelDeal)
        }
    }

    private fun uploadToDatabase(travelDeal: TravelDeal) {
        FirebaseUtil.databaseReference.push().setValue(travelDeal).addOnCompleteListener {
            if (it.isSuccessful) _isUploadSuccessful.value = true
            else _isUploadFailed.value = true

            _uploading.value = false
        }
    }

    fun updateTravelDeal(travelDeal: TravelDeal) {
        FirebaseUtil.databaseReference.child(travelDeal.id!!).setValue(travelDeal).addOnCompleteListener {
            if (it.isSuccessful) _isUploadSuccessful.value = true
            else _isUploadFailed.value = true

            _uploading.value = false
        }
    }

    fun deleteDealFromDatabase(travelDeal: TravelDeal) {
        FirebaseUtil.databaseReference.child(travelDeal.id!!).removeValue().addOnCompleteListener {
            if (it.isSuccessful) {
                FirebaseUtil.storageReference.child("images/${travelDeal.imageName!!}").delete()
                _isDeleteSuccessful.value = true
            }
            else _isDeleteFailed.value = true
        }
    }

    fun setTravelDeal(travelDeal: TravelDeal) {
        _travelDeal = travelDeal
    }

    fun uploadSuccessfulCompleted() {
        _isUploadSuccessful.value = false
    }

    fun uploadFailedCompleted() {
        _isUploadFailed.value = false
    }

    fun deleteSuccessfulCompleted(){
        _isDeleteSuccessful.value = false
    }

    fun deleteFailedCompleted(){
        _isDeleteFailed.value = false
    }
}