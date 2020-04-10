package com.napptilians.features.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.napptilians.commons.error.ErrorModel
import com.napptilians.domain.models.movie.CategoryModel
import com.napptilians.domain.models.movie.MovieModel
import com.napptilians.domain.usecases.GetCategoriesUseCase
import com.napptilians.features.UiStatus
import com.napptilians.features.base.BaseViewModel
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class CategoriesViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase
) : BaseViewModel<CategoryModel>() {

    private val _categoriesDataStream = MutableLiveData<UiStatus<List<CategoryModel>, ErrorModel>>()
    val categoriesDataStream: LiveData<UiStatus<List<CategoryModel>, ErrorModel>>
        get() = _categoriesDataStream

    fun execute(categoryIds: List<Long> = emptyList()) {
        viewModelScope.launch {
            _categoriesDataStream.value = emitLoadingState()
            val request = getCategoriesUseCase.execute(categoryIds)
            _categoriesDataStream.value = processModel(request)
        }
    }
}
