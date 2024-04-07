package cz.cvut.fit.poberboh.loc_tracker.ui.base

import androidx.lifecycle.ViewModel
import cz.cvut.fit.poberboh.loc_tracker.repository.BaseRepository

abstract class BaseViewModel(private val repository: BaseRepository) : ViewModel()