package com.qing.androidkotlinutils.core.ext


import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.recyclerview.widget.*
import com.qing.androidkotlinutils.core.base.dapter.BaseAdapter
import com.qing.androidkotlinutils.core.base.dapter.ItemDelegate
import com.qing.androidkotlinutils.core.base.dapter.MultiItemTypeAdapter
import com.qing.androidkotlinutils.core.base.dapter.ViewHolder
import com.qingyc.qlogger.QLogger


/**
 * 滑动指定item到中心
 */
fun RecyclerView.scrollPositionToCenter(position: Int, smooth: Boolean = true) {
    try {
        if (position == 0) {
            scrollToPosition(0)
        } else {
            //qtip 如果指定item没有在屏幕显示,先滑动到指定item
            val lm = layoutManager as LinearLayoutManager
            val firstPosition = lm.findFirstVisibleItemPosition()
            val lastPosition = lm.findLastVisibleItemPosition()
            if (position < firstPosition || position > lastPosition) {
                scrollToPosition(position)
            }
            postDelayed({

                val firstPosition2 = lm.findFirstVisibleItemPosition()
                val lastPosition2 = lm.findLastVisibleItemPosition()
                if (position in firstPosition2..lastPosition2) {
                    val left = getChildAt(position - firstPosition2).left
                    val right = getChildAt(lastPosition2 - position).left
                    if (smooth) {
                        smoothScrollBy((left - right) / 2, 0)
                    } else {
                        scrollBy((left - right) / 2, 0)
                    }
                }
            }, 300)
        }
    } catch (e: Exception) {
        QLogger.e(e)
    }
}


/**
 * 是否已滑动到底部
 */
fun RecyclerView.isScrolledBottom(): Boolean {
    return !canScrollVertically(1)
}

/**
 * 是否已滑动到顶部
 */
fun RecyclerView.isScrolledTop(): Boolean {
    val lm = layoutManager ?: return false
    if (lm is LinearLayoutManager) {
        return lm.findFirstCompletelyVisibleItemPosition() == 0
    } else if (lm is GridLayoutManager) {
        return lm.findFirstCompletelyVisibleItemPosition() == 0
    } else if (lm is StaggeredGridLayoutManager) {
        val items = lm.findFirstCompletelyVisibleItemPositions(null)
        return items[0] == 0
    }
    return false
}

/**
 * 滑动到底部
 */
fun RecyclerView.scrollToBottom() {
    val lm = layoutManager
    val adp = adapter ?: return
    if (lm is LinearLayoutManager) {
        lm.scrollToPositionWithOffset(adp.itemCount - 1, 0)
    }
}

/**
 * 滑动到顶部
 */
fun RecyclerView.scrollToTop() {
    val lm = layoutManager
    if (lm is LinearLayoutManager) {
        lm.scrollToPositionWithOffset(0, 0)
    }
}

/**
 * 关闭RecyclerView Item 动画
 */
fun RecyclerView.closeItemAnimator(): RecyclerView {
    val animator = itemAnimator ?: return this
    animator.addDuration = 0
    animator.changeDuration = 0
    animator.moveDuration = 0
    animator.removeDuration = 0
    if (animator is SimpleItemAnimator) {
        animator.supportsChangeAnimations = false
    }
    return this
}

/**
 * 更新GridLayout SpanCount
 * @param newSpanCount Int
 */
fun RecyclerView.updateSpanCount(newSpanCount: Int): RecyclerView {
    try {
        val glm = layoutManager as? GridLayoutManager ?: return this
        if (newSpanCount == glm.spanCount) return this
        removeAllViews()
        recycledViewPool.clear()
        glm.spanCount = newSpanCount
        adapter?.notifyDataSetChanged()
    } catch (e: Exception) {
        //
    }
    return this
}

/**
 * com.qing.androidkotlinutils.core.ext.reloadLayout
 */
fun RecyclerView.reloadLayout() {
    try {
        removeAllViews()
        recycledViewPool.clear()
        adapter?.notifyDataSetChanged()
    } catch (e: Exception) {
        //
    }
}


/**
 * item添加padding
 */
fun RecyclerView.addItemPadding(
    topPadding: Int,
    bottomPadding: Int,
    leftPadding: Int = 0,
    rightPadding: Int = 0
): RecyclerView {
    val t = topPadding.dp2Px()
    val b = bottomPadding.dp2Px()
    val l = leftPadding.dp2Px()
    val r = rightPadding.dp2Px()
    addItemDecoration(object : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            outRect.bottom = b
            outRect.top = t
            outRect.left = l
            outRect.right = r
        }

        override fun getItemOffsets(outRect: Rect, itemPosition: Int, parent: RecyclerView) {
            outRect.bottom = b
            outRect.top = t
            outRect.left = l
            outRect.right = r
        }
    })
    return this
}

/**
 * GridLayoutManager item添加padding
 *
 * item和item 之间的间隙是2倍innerPaddingVertical 或者 2倍innerPaddingHorizontal,和RecyclerView边的间隙是outPaddingLeft,outPaddingRight,outPaddingTop,outPaddingBottom
 *
 * @param innerPaddingVertical item 和 item 垂直的padding
 * @param innerPaddingHorizontal item 和 item 水平的padding
 * @param outPaddingRight item 和 视图边缘 的padding
 * @param outPaddingLeft item 和 视图边缘 的padding
 * @param outPaddingTop item 和 视图边缘 的padding
 * @param outPaddingBottom item 和 视图边缘 的padding
 */
fun RecyclerView.addGridItemPadding(
    innerPaddingVertical: Int,
    innerPaddingHorizontal: Int,
    outPaddingLeft: Int,
    outPaddingRight: Int,
    outPaddingTop: Int,
    outPaddingBottom: Int
): RecyclerView {

    val v = innerPaddingVertical.dp2Px()
    val h = innerPaddingHorizontal.dp2Px()
    val l = outPaddingLeft.dp2Px()
    val r = outPaddingRight.dp2Px()
    val t = outPaddingTop.dp2Px()
    val b = outPaddingBottom.dp2Px()
    val lm = layoutManager

    addItemDecoration(object : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(outRect: Rect, itemPosition: Int, parent: RecyclerView) {
            if (lm is GridLayoutManager) {
                //总跨数
                val totalSpanCount = lm.spanCount
                //总列数
                val totalColumn =
                    childCount / totalSpanCount + if (childCount % totalSpanCount == 0) 0 else 1

                when {
                    //第一列
                    itemPosition % totalSpanCount == 0 -> {
                        outRect.left = l
                        outRect.right = v
                    }
                    //最后一列
                    itemPosition % totalSpanCount == totalSpanCount - 1 -> {
                        outRect.left = v
                        outRect.right = r
                    }
                    //中间列
                    else -> {
                        outRect.left = v
                        outRect.right = v
                    }
                }

                when {
                    //第一行
                    itemPosition / totalSpanCount <= 1 -> {
                        outRect.top = t
                        outRect.bottom = h
                    }
                    //最后一行
                    itemPosition / totalSpanCount.toFloat() > (totalColumn - 1) -> {
                        outRect.top = h
                        outRect.bottom = b
                    }
                    //中间
                    else -> {
                        outRect.top = h
                        outRect.bottom = h
                    }
                }
            }

        }

    })
    return this
}


fun RecyclerView.addDivider(
    color: Int = Color.TRANSPARENT,
    size: Int = 1,
    isReplace: Boolean = true
): RecyclerView {
    val decoration = DividerItemDecoration(context, orientation)
    decoration.setDrawable(GradientDrawable().apply {
        setColor(color)
        shape = GradientDrawable.RECTANGLE
        setSize(size, size)
    })
    if (isReplace && itemDecorationCount > 0) {
        removeItemDecorationAt(0)
    }
    addItemDecoration(decoration)
    return this
}


fun RecyclerView.vertical(spanCount: Int = 0, isStaggered: Boolean = false): RecyclerView {
    layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    if (spanCount != 0) {
        layoutManager = GridLayoutManager(context, spanCount)
    }
    if (isStaggered) {
        layoutManager = StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL)
    }
    return this
}

fun RecyclerView.horizontal(spanCount: Int = 0, isStaggered: Boolean = false): RecyclerView {
    layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    if (spanCount != 0) {
        layoutManager = GridLayoutManager(context, spanCount, GridLayoutManager.HORIZONTAL, false)
    }
    if (isStaggered) {
        layoutManager = StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.HORIZONTAL)
    }
    return this
}


inline val RecyclerView.data
    get() = (adapter as BaseAdapter<*>).data

inline val RecyclerView.orientation
    get() = if (layoutManager == null) -1 else layoutManager.run {
        when (this) {
            is LinearLayoutManager -> orientation
            is GridLayoutManager -> orientation
            is StaggeredGridLayoutManager -> orientation
            else -> -1
        }
    }


fun <T> RecyclerView.bindData(
    data: List<T>,
    layoutId: Int,
    bindFn: (holder: ViewHolder, t: T, position: Int) -> Unit
): RecyclerView {
    adapter = object : BaseAdapter<T>(data, layoutId) {
        override fun bind(holder: ViewHolder, t: T, position: Int) {
            bindFn(holder, t, position)
        }
    }
    return this
}

/**
 * 必须在bindData之后调用，并且需要hasHeaderOrFooter为true才起作用
 */
fun RecyclerView.addHeader(headerView: View): RecyclerView {
    adapter?.apply {
        (this as BaseAdapter<*>).addHeaderView(headerView)
    }
    return this
}

/**
 * 必须在bindData之后调用，并且需要hasHeaderOrFooter为true才起作用
 */
fun RecyclerView.addFooter(footerView: View): RecyclerView {
    adapter?.apply {
        (this as BaseAdapter<*>).addFootView(footerView)
    }
    return this
}

fun <T> RecyclerView.multiTypes(data: List<T>, itemDelegates: List<ItemDelegate<T>>): RecyclerView {
    adapter = MultiItemTypeAdapter<T>(data).apply {
        itemDelegates.forEach { addItemDelegate(it) }
    }
    return this
}

fun <T> RecyclerView.itemClick(listener: (data: List<T>, holder: RecyclerView.ViewHolder, position: Int) -> Unit): RecyclerView {
    adapter?.apply {
        (adapter as MultiItemTypeAdapter<*>).setOnItemClickListener(object :
            MultiItemTypeAdapter.SimpleOnItemClickListener() {
            override fun onItemClick(view: View, holder: RecyclerView.ViewHolder, position: Int) {
                listener(data as List<T>, holder, position)
                view.clickEnableDelay()
            }
        })
    }
    return this
}

fun <T> RecyclerView.itemLongClick(listener: (data: List<T>, holder: RecyclerView.ViewHolder, position: Int) -> Unit): RecyclerView {
    adapter?.apply {
        (adapter as MultiItemTypeAdapter<*>).setOnItemClickListener(object :
            MultiItemTypeAdapter.SimpleOnItemClickListener() {
            override fun onItemLongClick(
                view: View,
                holder: RecyclerView.ViewHolder,
                position: Int
            ): Boolean {
                listener(data as List<T>, holder, position)
                return super.onItemLongClick(view, holder, position)
            }
        })
    }
    return this
}
