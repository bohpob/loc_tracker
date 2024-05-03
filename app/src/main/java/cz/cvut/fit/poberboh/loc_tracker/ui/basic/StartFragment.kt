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

/**
 * Fragment class for the start screen of the application.
 */
class StartFragment : BaseFragment<StartViewModel, FragmentStartBinding, BaseRepository>() {

    /**
     * Called to create the view hierarchy associated with the fragment.
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_start, container, false)
        val startButton: Button = view.findViewById(R.id.button_start)

        // Navigate to the map screen when the start button is clicked
        startButton.setOnClickListener {
            findNavController().navigate(R.id.action_start_to_map)
        }
        return view
    }

    /**
     * Get the ViewModel associated with this fragment.
     * @return The ViewModel class.
     */
    override fun getViewModel() = StartViewModel::class.java

    /**
     * Inflate the binding layout for this fragment.
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     * @return The binding object for the fragment's layout.
     */
    override fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentStartBinding.inflate(inflater, container, false)

    /**
     * Get the repository associated with this fragment.
     * @return The repository object.
     */
    override fun getFragmentRepository() = StartRepository()
}