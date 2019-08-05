package com.saratms.travelmantics.utilities

import android.app.Activity
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.saratms.travelmantics.MainActivity

/**
 * Created by Sarah Al-Shamy on 03/08/2019.
 */
object FirebaseUtil {

    private val RC_SIGN_IN: Int = 123
    val firebaseDatabase = FirebaseDatabase.getInstance()
    var firebaseAuth = FirebaseAuth.getInstance()
    var storageReference = FirebaseStorage.getInstance().getReference()
    var firebaseAuthListener: FirebaseAuth.AuthStateListener
    lateinit var databaseReference: DatabaseReference
    var activity: MainActivity? = null
    var isAdmin: Boolean = false

    init {
        firebaseAuthListener = FirebaseAuth.AuthStateListener { auth ->
            if (auth.currentUser == null) {
                signIn()
                isAdmin = false
            } else {
                var userId: String = auth.currentUser!!.uid
                checkAdmin(userId)
            }
        }
    }

    fun checkAdmin(userId: String) {
        isAdmin = false
        activity!!.redrawMenu()

        firebaseDatabase.getReference().child("admins").child(userId)
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                    isAdmin = true
                    activity!!.redrawMenu()
                }

                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                }

                override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                }

                override fun onChildRemoved(p0: DataSnapshot) {

                }
            })
    }

    fun openFirebaseReference(reference: String) {
        databaseReference = firebaseDatabase.getReference().child(reference)
    }

    fun setFirebaseUtilActivity(activity: MainActivity) {
        this.activity = activity
    }

    private fun signIn() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        activity!!.startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            RC_SIGN_IN
        )
    }

    fun attachAuthListener() {
        firebaseAuth.addAuthStateListener(firebaseAuthListener)
    }

    fun detachAuthListener() {
        firebaseAuth.removeAuthStateListener(firebaseAuthListener)
    }

    fun releaseActivityReference() {
        activity = null
    }
}