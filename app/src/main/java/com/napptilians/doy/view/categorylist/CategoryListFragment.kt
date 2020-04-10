package com.napptilians.doy.view.categorylist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.navigation.fragment.findNavController
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
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class CategoryListFragment : BaseFragment() {

    private val viewModel: CategoriesViewModel by viewModels { vmFactory }
    private val args: CategoryListFragmentArgs by navArgs()

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
            Observer<UiStatus<List<CategoryModel>, ErrorModel>> {
                handleUiStates(it, ::processNewValue)
            })
    }

    private fun processNewValue(model: List<CategoryModel>) {
        if (model.isNotEmpty()) {
            categoriesAdapter.updateItems(model)
            categoryList.visible()
            loadingProgress.gone()
            loadingText.gone()
        } else {
            categoryList.gone()
            loadingProgress.gone()
            loadingText.text = resources.getString(R.string.no_categories)
            loadingText.text = resources.getString(R.string.generic_error)
            loadingText.visible()
        }
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
        if (args.isAddingService) {
            titleText.visible()
            // TODO: Also show toolbar for back button
        }
        val layoutManager = GridLayoutManager(context, NUMBER_OF_COLUMNS)
        categoryList.layoutManager = layoutManager
        categoriesAdapter = CategoryListAdapter().apply { isAddingService = args.isAddingService }
        categoriesAdapter.setOnClickListener { clickedCategory ->
            if (args.isAddingService) {
                categoriesAdapter.getItems().forEachIndexed { index, categoryModel ->
                    if (categoryModel != clickedCategory && categoryModel.isSelected) {
                        categoryModel.isSelected = false
                        categoriesAdapter.notifyItemChanged(index)
                    } else if (categoryModel == clickedCategory && !categoryModel.isSelected) {
                        categoryModel.isSelected = true
                        categoriesAdapter.notifyItemChanged(index)
                    }
                }
            } else {
                val navigation =
                    CategoryListFragmentDirections.actionCategoryListFragmentToServiceListFragment(
                        clickedCategory.categoryId,
                        clickedCategory.name
                    )
                findNavController().navigate(navigation)
            }
        }
        val itemOffsetDecoration = ItemOffsetDecoration(context, R.dimen.margin_xsmall)
        categoryList.addItemDecoration(itemOffsetDecoration)
        categoryList.adapter = categoriesAdapter
    }

    companion object {
        private const val NUMBER_OF_COLUMNS = 2
    }
}
