package com.saratms.travelmantics.fragments

import android.app.Activity
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.saratms.travelmantics.R
import com.saratms.travelmantics.databinding.FragmentInsertBinding
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.text.TextUtils
import android.widget.Toast
import androidx.core.view.get
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.saratms.travelmantics.models.TravelDeal
import com.saratms.travelmantics.utilities.FirebaseUtil
import com.saratms.travelmantics.viewmodels.InsertViewModel
import com.squareup.picasso.Picasso

/**
 * Created by Sarah Al-Shamy on 03/08/2019.
 */
class InsertFragment : Fragment() {

    private val RESULT_LOAD_IMG: Int = 155
    lateinit var binding: FragmentInsertBinding
    lateinit var insertViewModel: InsertViewModel
    var selectedImage: Uri? = null
    var travelDeal: TravelDeal? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentInsertBinding.inflate(inflater, container, false)
        binding.selectImageButton.setOnClickListener {selectImage()}
        insertViewModel = ViewModelProviders.of(this).get(InsertViewModel::class.java)
        initBinding()
        setHasOptionsMenu(true)
        setupObservers()
        return binding.root
    }

    private fun initBinding() {
        binding.setLifecycleOwner(this)
        binding.viewModel = insertViewModel
        checkForExistingDeal()
        binding.executePendingBindings()
    }

    private fun checkForExistingDeal() {
        travelDeal = arguments!!.getParcelable<TravelDeal>("travelArg")
        if (travelDeal != null) {
            insertViewModel.setTravelDeal(travelDeal!!)
            binding.invalidateAll()
        }
    }

    private fun setupObservers() {
        insertViewModel.isUploadSuccessful.observe(
            this,
            Observer {
                if (it) {
                    view!!.findNavController().navigate(InsertFragmentDirections.actionInsertFragmentToListFragment())
                    Toast.makeText(context, getString(R.string.successful_deal_toast), Toast.LENGTH_SHORT).show()
                    insertViewModel.uploadSuccessfulCompleted()
                }
            })

        insertViewModel.isUploadFailed.observe(this, Observer {
            if (it) Toast.makeText(context, getString(R.string.error_msg), Toast.LENGTH_SHORT).show()
            insertViewModel.uploadFailedCompleted()
        })

        insertViewModel.isDeleteSuccessful.observe(this, Observer {
            if (it) {
                view!!.findNavController().navigate(InsertFragmentDirections.actionInsertFragmentToListFragment())
                Toast.makeText(context, getString(R.string.successful_delete_toast), Toast.LENGTH_SHORT).show()
                insertViewModel.deleteSuccessfulCompleted()
            }
        })

        insertViewModel.isDeleteFailed.observe(this, Observer {
            if (it) {
                Toast.makeText(context, getString(R.string.error_msg), Toast.LENGTH_SHORT).show()
                insertViewModel.deleteSuccessfulCompleted()
            }
        })
    }

    private fun selectImage() {
        val galleryIntent = Intent(Intent.ACTION_GET_CONTENT)
        galleryIntent.setType("image/jpeg")
        galleryIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
        startActivityForResult(Intent.createChooser(galleryIntent, "Insert Picture"), RESULT_LOAD_IMG)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.insert_menu, menu)
        if (!FirebaseUtil.isAdmin) {
            menu!!.findItem(R.id.delete_item).setVisible(false)
            menu!!.findItem(R.id.save_item).setVisible(false)
        }else{
            menu!!.findItem(R.id.delete_item).setVisible(true)
            menu!!.findItem(R.id.save_item).setVisible(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.save_item -> {
                addTravelDeal()
                return true
            }
            R.id.delete_item -> {
                deleteTravelDeal()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun deleteTravelDeal() {
        if (travelDeal != null) {
            insertViewModel.deleteDealFromDatabase(travelDeal!!)
        } else {
            Toast.makeText(context, getString(R.string.save_deal_first_toast), Toast.LENGTH_SHORT).show()
        }
    }


    private fun addTravelDeal() {
        var dealTitle = binding.titleEditText.text.toString()
        var dealPrice = binding.priceEditText.text.toString()
        var dealDescription = binding.descriptionEditText.text.toString()

        if (!TextUtils.isEmpty(dealTitle) && !TextUtils.isEmpty(dealPrice) && !TextUtils.isEmpty(dealDescription)) {
            if (travelDeal != null) {
                with(travelDeal!!) {
                    title = dealTitle
                    price = dealPrice
                    description = dealDescription
                    insertViewModel.uploadTravelDeal(travelDeal!!, selectedImage, false)
                }
            } else {
                if (selectedImage != null) {
                    travelDeal = TravelDeal("", dealTitle, dealDescription, dealPrice)
                    insertViewModel.uploadTravelDeal(travelDeal!!, selectedImage, true)
                } else {
                    Toast.makeText(context, getString(R.string.upload_photo_toast), Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(context, getString(R.string.complete_fields_toast), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RESULT_LOAD_IMG && resultCode == Activity.RESULT_OK && data != null) {
            selectedImage = data.data
            var width = Resources.getSystem().displayMetrics.widthPixels
            Picasso.get().load(selectedImage).resize(width, width *2 / 3).centerCrop().into(binding.selectedImageView)
        }
    }
}