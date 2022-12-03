package com.fikri.moviebox.core.helper

import android.content.Context

object DimensManipulation {
    fun dpToPx(context: Context, dp: Float): Float {
        return dp * context.resources.displayMetrics.density
    }
}