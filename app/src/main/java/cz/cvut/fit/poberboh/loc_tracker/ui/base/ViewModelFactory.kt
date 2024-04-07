package cz.cvut.fit.poberboh.loc_tracker.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cz.cvut.fit.poberboh.loc_tracker.repository.BaseRepository
import cz.cvut.fit.poberboh.loc_tracker.repository.BasicRepository
import cz.cvut.fit.poberboh.loc_tracker.ui.basic.BasicViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
    private val repository: BaseRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(BasicViewModel::class.java) -> BasicViewModel(repository as BasicRepository) as T
            else -> throw IllegalArgumentException("ViewModelClass Not Found")
        }
    }
}