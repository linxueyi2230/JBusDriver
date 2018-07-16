package me.jbusdriver.component.movie.detail.ui.holder

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import com.billy.cc.core.component.CC
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.layout_detail_image_samples.view.*
import me.jbusdriver.base.GlideApp
import me.jbusdriver.base.common.C
import me.jbusdriver.base.common.toGlideUrl
import me.jbusdriver.base.displayMetrics
import me.jbusdriver.base.dpToPx
import me.jbusdriver.base.inflate
import me.jbusdriver.base.mvp.bean.ImageSample
import me.jbusdriver.base.mvp.ui.adapter.BaseAppAdapter
import me.jbusdriver.base.mvp.ui.holder.BaseHolder
import me.jbusdriver.component.movie.detail.R
import me.jbusdriver.component.movie.detail.ui.adapter.GridSpacingItemDecoration


/**
 * Created by Administrator on 2017/5/9 0009.
 */
class ImageSampleHolder(context: Context) : BaseHolder(context) {

    //cover
    lateinit var cover: String

    val view by lazy {
        weakRef.get()?.let {
            it.inflate(R.layout.layout_detail_image_samples).apply {
                val spanCount = with(context.displayMetrics.widthPixels) {
                    when {
                        this <= 1440 -> 3
                        else -> 4
                    }
                }
                rv_recycle_images.layoutManager = GridLayoutManager(it, spanCount)
                rv_recycle_images.addItemDecoration(GridSpacingItemDecoration(spanCount, it.dpToPx(8f), false))
                imageSampleAdapter.bindToRecyclerView(rv_recycle_images)
                rv_recycle_images.isNestedScrollingEnabled = true
                imageSampleAdapter.setOnItemClickListener { _, v, position ->
                    if (position < imageSampleAdapter.data.size) {
                        val destination = arrayListOf<String>()
                        var pos = position
                        if (this@ImageSampleHolder::cover.isInitialized) {
                            pos += 1
                            destination.add(cover)

                        }
                        imageSampleAdapter.data.mapTo(destination) { if (TextUtils.isEmpty(it.image)) it.thumb else it.image }
                        CC.obtainBuilder(C.C_IMAGE_BROWSER::class.java.name)
                                .setActionName(C.C_IMAGE_BROWSER.Browser_Images)
                                .addParam("images", destination)
                                .addParam("index", pos)
                                .setTimeout(3000)
                                .build()
                                .call()
                    }
                }
            }
        } ?: error("context ref is finish")
    }


    private val imageSampleAdapter = object : BaseAppAdapter<ImageSample, BaseViewHolder>(R.layout.layout_image_sample_item) {
        override fun convert(holder: BaseViewHolder, item: ImageSample) {
            weakRef.get()?.apply {
                holder.getView<ImageView>(R.id.iv_movie_thumb)?.let {
                    GlideApp.with(this).load(item.thumb.toGlideUrl)
                            .fitCenter()
                            .placeholder(R.drawable.ic_child_care_black_24dp)
                            .error(R.drawable.ic_child_care_black_24dp)
                            .into(DrawableImageViewTarget(it))

                }
            }
        }
    }

    fun init(data: List<ImageSample>) {
        //imageSamples
        if (data.isEmpty()) view.tv_movie_images_none_tip.visibility = View.VISIBLE
        else {
            //load header
            imageSampleAdapter.setNewData(data)
        }
    }

}