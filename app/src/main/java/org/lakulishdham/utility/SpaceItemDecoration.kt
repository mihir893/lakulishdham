package org.lakulishdham.utility

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SpaceItemDecoration(s: Int) : RecyclerView.ItemDecoration() {
    private var space = s

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {

//        outRect.bottom = space;

        // Add top margin only for the first item to avoid double space between items
        if (parent.getChildLayoutPosition(view) % 2 == 0) {
//            outRect.top = space;
            outRect.left = 0
            outRect.right = space/2

        } else {
            outRect.left = space/2
            outRect.right = 0
        }


        if (parent.getChildLayoutPosition(view) == 0 ||
                parent.getChildLayoutPosition(view) == 1) {
            outRect.top = 0
            outRect.bottom = space/2
        }
        else {
            outRect.top = space/2
            outRect.bottom = space/2
        }

    }
}