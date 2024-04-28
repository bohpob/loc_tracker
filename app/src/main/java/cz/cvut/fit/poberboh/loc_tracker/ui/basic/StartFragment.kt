package cz.cvut.fit.poberboh.loc_tracker.ui.basic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import cz.cvut.fit.poberboh.loc_tracker.R
import cz.cvut.fit.poberboh.loc_tracker.databinding.FragmentStartBinding
import cz.cvut.fit.poberboh.loc_tracker.repository.BaseRepository
import cz.cvut.fit.poberboh.loc_tracker.repository.StartRepository
import cz.cvut.fit.poberboh.loc_tracker.ui.base.BaseFragment

class StartFragment : BaseFragment<StartViewModel, FragmentStartBinding, BaseRepository>() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_start, container, false)
        val startButton: Button = view.findViewById(R.id.button_start)

        startButton.setOnClickListener {
            findNavController().navigate(R.id.action_start_to_map)
        }
        return view
    }

    override fun getViewModel() = StartViewModel::class.java
    override fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentStartBinding.inflate(inflater, container, false)

    override fun getFragmentRepository() = StartRepository()
}