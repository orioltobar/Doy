package com.napptilians.doy.view.categorylist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.napptilians.commons.error.ErrorModel
import com.napptilians.domain.models.category.Category
import com.napptilians.doy.R
import com.napptilians.doy.base.BaseFragment
import com.napptilians.doy.extensions.visible
import javax.inject.Inject
import kotlinx.android.synthetic.main.category_list_fragment.*

class CategoryListFragment : BaseFragment() {

    private val categories = listOf(
        Category(
            1,
            "Deporte y bienestar",
            "https://cdn.pixabay.com/photo/2015/04/23/22/00/tree-736885__340.jpg"
        ),
        Category(
            2,
            "Música",
            "https://en.wikipedia.org/wiki/Image#/media/File:Image_created_with_a_mobile_phone.png"
        ),
        Category(
            3,
            "Jocs",
            "https://storage.cloud.google.com/doy-proj.appspot.com/juegos.png?folder&hl=es-419&organizationId/doy-proj.appspot.com/juegos.png"
        )
    )
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
        categoriesAdapter.updateItems(categories)
        categoryList.visible()
    }

    override fun onError(error: ErrorModel) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun onLoading() {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    private fun initViews() {
        val layoutManager = GridLayoutManager(context, NUMBER_OF_COLUMNS)
        categoryList.layoutManager = layoutManager
        categoriesAdapter = CategoryListAdapter()
        categoriesAdapter.setOnClickListener {
            Toast.makeText(
                context,
                "Categoria ${it.categoryId}",
                Toast.LENGTH_LONG
            ).show()
        }
        val itemOffsetDecoration = ItemOffsetDecoration(context, R.dimen.margin_small)
        categoryList.addItemDecoration(itemOffsetDecoration)
        categoryList.adapter = categoriesAdapter
    }

    companion object {
        private const val NUMBER_OF_COLUMNS = 2
    }
}
