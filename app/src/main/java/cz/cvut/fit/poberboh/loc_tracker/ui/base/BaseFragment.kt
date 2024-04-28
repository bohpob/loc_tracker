package cz.cvut.fit.poberboh.loc_tracker.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import cz.cvut.fit.poberboh.loc_tracker.network.RemoteDataSource
import cz.cvut.fit.poberboh.loc_tracker.repository.BaseRepository

abstract class BaseFragment<BVM : BaseViewModel, VB : ViewBinding, BR : BaseRepository> :
    Fragment() {

    protected lateinit var binding: VB
    protected lateinit var viewModel: BVM
    protected val remoteDataSource = RemoteDataSource()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = getFragmentBinding(inflater, container)
        val factory = ViewModelFactory(getFragmentRepository())
        viewModel = ViewModelProvider(this, factory)[getViewModel()]
        return binding.root
    }

    abstract fun getViewModel(): Class<BVM>

    abstract fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    abstract fun getFragmentRepository(): BR
}