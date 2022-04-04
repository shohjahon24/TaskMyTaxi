package com.example.taskmytaxi.presentation.ui.search

import android.app.Dialog
import android.graphics.Paint
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.taskmytaxi.R
import com.example.taskmytaxi.databinding.DialogFavoriteBinding
import com.example.taskmytaxi.databinding.FragmentSearchBinding
import com.example.taskmytaxi.domain.model.Location
import com.example.taskmytaxi.presentation.list.LocationListener
import com.example.taskmytaxi.presentation.list.favorite.FavoriteAdapter
import com.example.taskmytaxi.presentation.list.location.LocationAdapter
import com.example.taskmytaxi.presentation.session_manager.SessionManager
import com.example.taskmytaxi.util.MyTextWatcher
import com.example.taskmytaxi.util.location.LocationManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : BottomSheetDialogFragment(), MyTextWatcher, View.OnClickListener,
    View.OnFocusChangeListener, LocationListener {

    private lateinit var binding: FragmentSearchBinding

    private var favoriteDialog: BottomSheetDialog? = null

    @Inject
    lateinit var favoriteAdapter: FavoriteAdapter

    @Inject
    lateinit var adapter: LocationAdapter

    @Inject
    lateinit var locationManager: LocationManager


    private lateinit var dialog: BottomSheetDialog

    private val navController: NavController by lazy { findNavController() }

    private val viewModel: SearchViewModel by viewModels()

    private var type = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.let {
            type = it.getInt("id")
            setupLocationsToView(type, it.getBoolean("isEdit"))
            setupFocus(type)
        }
        binding.listLocation.adapter = adapter
        binding.etFromLocation.addTextChangedListener(this)
        binding.etToLocation.addTextChangedListener(this)
        binding.btnMapFrom.setOnClickListener(this)
        binding.btnMapTo.setOnClickListener(this)
        binding.btnClearTo.setOnClickListener(this)
        binding.btnClearFrom.setOnClickListener(this)
        binding.btnFavorite.setOnClickListener(this)
        binding.etFromLocation.onFocusChangeListener = this
        binding.etToLocation.onFocusChangeListener = this
        adapter.listener = this
        favoriteAdapter.listener = this
        setupObserves()
    }

    private fun setupObserves() {
        viewModel.apply {
            locations.observe(viewLifecycleOwner) {
                when (it) {
                    is SearchEvent.Loading -> {
                        showShimmerView()
                    }
                    is SearchEvent.Success -> {
                        it.data?.let { it1 ->
                            adapter.setData(it1)
                        }
                        hideShimmerView()
                    }
                    else -> {
                        hideShimmerView()
                    }
                }
            }
            favorites.observe(viewLifecycleOwner) {
                favoriteAdapter.setData(it)
            }
        }
    }

    private fun hideShimmerView() {
        binding.shimmerLayout.apply {
            stopShimmer()
            visibility = View.GONE
        }
        binding.listLocation.visibility = View.VISIBLE
    }

    private fun showShimmerView() {
        binding.shimmerLayout.apply {
            startShimmer()
            visibility = View.VISIBLE
        }
        binding.listLocation.visibility = View.GONE
    }

    private fun setupFocus(id: Int) {
        binding.apply {
            when (id) {
                0 -> {
                    llFromLocation.isSelected = true
                    btnMapFrom.isSelected = true
                    llToLocation.isSelected = false
                    etFromLocation.isSelected = true
                    etToLocation.isSelected = false
                    btnMapTo.visibility = View.GONE
                    btnMapFrom.visibility = View.VISIBLE
                    btnClearTo.visibility = View.GONE
                    btnClearFrom.visibility = View.VISIBLE
                }
                else -> {
                    llFromLocation.isSelected = false
                    llToLocation.isSelected = true
                    btnMapTo.isSelected = true
                    etFromLocation.isSelected = false
                    etToLocation.isSelected = true
                    btnMapTo.visibility = View.VISIBLE
                    btnMapFrom.visibility = View.GONE
                    btnClearTo.visibility = View.VISIBLE
                    btnClearFrom.visibility = View.GONE
                }
            }
        }
    }

    private fun setupLocationsToView(id: Int, isEdit: Boolean) {
        SessionManager.locationManager.getFromLocation()?.let {
            binding.etFromLocation.setText(it.address)
        }
        SessionManager.locationManager.getToLocations().let {
            if (it.isNotEmpty() && it.size >= id)
                binding.etToLocation.setText(if (id == 0) it[id].address else it[id - 1].address)
        }
        if (isEdit) {
            binding.llFrom.visibility = View.GONE
        }
    }

    private fun showFavoriteDialog() {
        if (favoriteDialog == null) {
            context?.let {
                val binding = DialogFavoriteBinding.inflate(layoutInflater)
                binding.apply {
                    listFavorite.adapter = favoriteAdapter
                }
                favoriteDialog = BottomSheetDialog(it, R.style.BottomSheetDialogTheme)
                favoriteDialog?.run {
                    setContentView(binding.root)
                    behavior.state = BottomSheetBehavior.STATE_EXPANDED
                }
                viewModel.getFavorites()
            }
        }
        favoriteDialog?.show()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater)
        return binding.root
    }

    override fun afterTextChanged(s: Editable?) {
        s?.let {
            if (s.toString() != "")
                viewModel.search("$s")
            else
                adapter.setData(arrayListOf())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogTheme)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.setOnShowListener {
            val d = it as BottomSheetDialog
            val sheet = d.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)!!
            val behavior = BottomSheetBehavior.from(sheet)
            behavior.isHideable = false
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            val newHeight = (activity?.window?.decorView?.measuredHeight?.times(.9))?.toInt()
            val viewGroupLayoutParams = sheet.layoutParams
            viewGroupLayoutParams.height = newHeight ?: 0
            behavior.peekHeight = 0
            behavior.maxHeight = newHeight ?: LinearLayout.LayoutParams.MATCH_PARENT
            sheet.layoutParams = viewGroupLayoutParams
        }
        return dialog
    }

    override fun onClick(p0: View) {
        when (p0.id) {
            R.id.btn_map_from -> {
                navController.navigate(R.id.action_searchFragment_to_mapFragment, bundleOf(Pair("id", 0)))
            }
            R.id.btn_map_to -> {
                var id = arguments?.getInt("id")
                if (id == 0) id = 1
                navController.navigate(R.id.action_searchFragment_to_mapFragment, bundleOf(Pair("id", id)))
            }
            R.id.btn_clear_from -> {
                binding.etFromLocation.setText("")
            }
            R.id.btn_clear_to -> {
                binding.etToLocation.setText("")
            }
            R.id.btn_favorite -> showFavoriteDialog()
        }
    }

    override fun onFocusChange(p0: View, p1: Boolean) {
        p0 as EditText
        if (p1) {
            p0.setCompoundDrawablesWithIntrinsicBounds(context?.let {
                ContextCompat.getDrawable(
                    it, R.drawable.ic_search
                )
            }, null, null, null)
            val inputType = if (p0.id == R.id.et_from_location) 0 else 1
            setupFocus(inputType)
            if (type < 2)
                type = inputType
        } else {
            p0.setCompoundDrawablesWithIntrinsicBounds(context?.let {
                if (p0.id == R.id.et_from_location)
                    ContextCompat.getDrawable(
                        it, R.drawable.ic_point_red
                    )
                else
                    ContextCompat.getDrawable(
                        it, R.drawable.ic_point_blue
                    )
            }, null, null, null)
        }
    }

    override fun onItemClick(location: Location) {
        when (type) {
            0 -> SessionManager.locationManager.changeFrom(location)
            1 -> SessionManager.locationManager.changeToLocation(location)
            else -> SessionManager.locationManager.addToLocation(location)
        }
        favoriteDialog?.dismiss()
        if (navController.previousBackStackEntry?.destination?.id == R.id.mainFragment)
            navController.popBackStack(R.id.mainFragment, true)
        navController.navigate(R.id.mainFragment)

    }
}