package cz.cvut.fit.poberboh.loc_tracker.ui.base

import androidx.lifecycle.ViewModel
import cz.cvut.fit.poberboh.loc_tracker.repository.BaseRepository

/**
 * Base class for ViewModels in the application.
 * This class provides a common base for all ViewModels and holds a reference to the BaseRepository.
 *
 * @param repository The instance of BaseRepository associated with the ViewModel.
 */
abstract class BaseViewModel(private val repository: BaseRepository) : ViewModel()
// This ViewModel class is currently empty but provides a common base for all ViewModels
// It can be extended in the future to include common functionality or data handling methods.