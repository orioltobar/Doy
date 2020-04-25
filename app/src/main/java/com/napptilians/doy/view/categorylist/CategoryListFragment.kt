package com.napptilians.doy.view.categorylist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.napptilians.commons.error.ErrorModel
import com.napptilians.domain.models.category.CategoryModel
import com.napptilians.doy.R
import com.napptilians.doy.base.BaseFragment
import com.napptilians.doy.behaviours.ToolbarBehaviour
import com.napptilians.doy.extensions.gone
import com.napptilians.doy.extensions.setNavigationResult
import com.napptilians.doy.extensions.visible
import com.napptilians.doy.view.customviews.DoyErrorDialog
import com.napptilians.features.UiStatus
import com.napptilians.features.viewmodel.CategoriesViewModel
import kotlinx.android.synthetic.main.category_list_fragment.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class CategoryListFragment : BaseFragment(), ToolbarBehaviour {

    override val genericToolbar: Toolbar? by lazy { activity?.findViewById<Toolbar>(R.id.toolbar) }

    private val viewModel: CategoriesViewModel by viewModels { vmFactory }
    private val args: CategoryListFragmentArgs by navArgs()
    private var selectedCategoryId: Long = -1L
    private var selectedCategoryName: String = ""

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
            loadingText.visible()
        }
    }

    override fun onError(error: ErrorModel) {
        categoryList.gone()
        loadingProgress.gone()
        loadingText.gone()
        activity?.let { DoyErrorDialog(it).show() }
    }

    override fun onLoading() {
        categoryList.gone()
        loadingProgress.visible()
        loadingText.visible()
    }

    private fun initViews() {
        if (args.isAddingService) {
            enableHomeAsUp(true) { findNavController().popBackStack() }
            titleText.visible()
            with(saveButton) {
                visible()
                isEnabled = false
                setOnClickListener {
                    setNavigationResult(selectedCategoryId.toString(), "selectCategoryId")
                    setNavigationResult(selectedCategoryName, "selectCategoryName")
                    findNavController().popBackStack()
                }
            }
            // TODO: Also show toolbar for back button
        } else {
            genericToolbar?.gone()
        }
        val layoutManager = GridLayoutManager(context, NUMBER_OF_COLUMNS)
        categoryList.layoutManager = layoutManager
        categoriesAdapter = CategoryListAdapter().apply { isAddingService = args.isAddingService }
        categoriesAdapter.setOnClickListener { clickedCategory ->
            if (args.isAddingService) {
                saveButton.isEnabled = true
                categoriesAdapter.getItems().forEachIndexed { index, categoryModel ->
                    if (categoryModel != clickedCategory && categoryModel.isSelected) {
                        categoryModel.isSelected = false
                        categoriesAdapter.notifyItemChanged(index)
                    } else if (categoryModel == clickedCategory && !categoryModel.isSelected) {
                        selectedCategoryId = categoryModel.categoryId
                        selectedCategoryName = categoryModel.name
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
        val itemOffsetDecoration = ItemOffsetDecoration(10)
        categoryList.addItemDecoration(itemOffsetDecoration)
        categoryList.adapter = categoriesAdapter
    }

    companion object {
        private const val NUMBER_OF_COLUMNS = 2
    }
}
