package com.saratms.travelmantics.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.saratms.travelmantics.R
import com.saratms.travelmantics.adapters.TravelDealsAdapter
import com.saratms.travelmantics.databinding.FragmentListBinding
import com.saratms.travelmantics.utilities.FirebaseUtil
import com.saratms.travelmantics.viewmodels.TravelListViewModel

/**
 * Created by Sarah Al-Shamy on 03/08/2019.
 */
class ListFragment : Fragment() {

    lateinit var listViewModel: TravelListViewModel
    lateinit var binding: FragmentListBinding
    lateinit var travelAdapter: TravelDealsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentListBinding.inflate(inflater, container, false)
        listViewModel = ViewModelProviders.of(this).get(TravelListViewModel::class.java)
        binding.listViewModel = listViewModel
        setHasOptionsMenu(true)
        setupAdapter()
        setupObservers()
        return binding.root
    }

    private fun setupAdapter() {
        var travelClickListener = TravelDealsAdapter.TravelItemClickListener{
            val action = ListFragmentDirections.actionListFragmentToInsertFragment(it)
            view!!.findNavController().navigate(action)
        }
        travelAdapter = TravelDealsAdapter(travelClickListener)
    }

    private fun setupObservers() {
        listViewModel.travelDeals.observe(this, Observer {
            travelAdapter.travelDealList = it
            listViewModel.finishLoading()
            binding.invalidateAll()
            binding.recycler.adapter = travelAdapter

        })
    }

    override fun onStart() {
        super.onStart()
        listViewModel.fetchTravelDeals()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.list_menu, menu!!)
        if (!FirebaseUtil.isAdmin) {
            menu!!.findItem(R.id.add_new_deal).setVisible(false)
        }else{
            menu!!.findItem(R.id.add_new_deal).setVisible(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId){
            R.id.add_new_deal -> {
                val action = ListFragmentDirections.actionListFragmentToInsertFragment(null)
                view!!.findNavController().navigate(action)
                return true
            }
            R.id.sign_out ->{
                FirebaseUtil.firebaseAuth.signOut()
                return true
            }
            else ->  return super.onOptionsItemSelected(item)
        }
    }
}