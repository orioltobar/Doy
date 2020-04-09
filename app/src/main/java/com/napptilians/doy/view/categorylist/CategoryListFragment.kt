package com.napptilians.doy.view.categorylist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.napptilians.commons.error.ErrorModel
import com.napptilians.domain.models.movie.CategoryModel
import com.napptilians.doy.R
import com.napptilians.doy.base.BaseFragment
import com.napptilians.doy.extensions.gone
import com.napptilians.doy.extensions.visible
import com.napptilians.features.UiStatus
import com.napptilians.features.viewmodel.CategoriesViewModel
import javax.inject.Inject
import kotlinx.android.synthetic.main.category_list_fragment.*
import kotlinx.android.synthetic.main.movie_fragment.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class CategoryListFragment : BaseFragment() {

    private val categories = listOf(
        CategoryModel(
            1,
            "Deporte y bienestar",
            "https://storage.googleapis.com/doy-proj.appspot.com/deporteybienestar.png"
        ),
        CategoryModel(
            2,
            "Música",
            "https://storage.googleapis.com/doy-proj.appspot.com/musica.png"
        ),
        CategoryModel(
            3,
            "Jocs",
            "https://storage.googleapis.com/doy-proj.appspot.com/arte.png"
        )
    )

    private val viewModel: CategoriesViewModel by viewModels { vmFactory }

    @Inject
    lateinit var categoriesAdapter: CategoryListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.category_list_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        viewModel.execute()
        viewModel.categoriesDataStream.observe(
            viewLifecycleOwner,
            Observer<UiStatus<List<CategoryModel>, ErrorModel>> { handleUiStates(it, ::processNewValue) })
    }

    private fun processNewValue(model: List<CategoryModel>) {
        categoriesAdapter.updateItems(model)
        categoryList.visible()
        loadingProgress.gone()
        loadingText.gone()
    }

    override fun onError(error: ErrorModel) {
        categoryList.gone()
        loadingProgress.visible()
        loadingText.visible()
    }

    override fun onLoading() {
        categoryList.gone()
        loadingProgress.visible()
        loadingText.visible()
    }

    private fun initViews() {
        val layoutManager = GridLayoutManager(context, NUMBER_OF_COLUMNS)
        categoryList.layoutManager = layoutManager
        categoriesAdapter = CategoryListAdapter()
        categoriesAdapter.setOnClickListener {
            // TODO: Navigate to Service list screen
            Toast.makeText(
                context,
                "Categoria ${it.name}",
                Toast.LENGTH_LONG
            ).show()
        }
        val itemOffsetDecoration = ItemOffsetDecoration(context, R.dimen.margin_xsmall)
        categoryList.addItemDecoration(itemOffsetDecoration)
        categoryList.adapter = categoriesAdapter
    }

    companion object {
        private const val NUMBER_OF_COLUMNS = 2
    }
}
