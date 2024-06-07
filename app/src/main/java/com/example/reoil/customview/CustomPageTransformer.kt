package com.example.reoil.customview

import android.view.View
import androidx.viewpager2.widget.ViewPager2

class CustomPageTransformer : ViewPager2.PageTransformer {
    private val margin = 10f
    private val scaleFactor = 0.005f

    override fun transformPage(page: View, position: Float) {
        val translationX = position * page.width * scaleFactor
        when {
            position < -1 -> { // [-Infinity,-1)
                page.translationX = -margin
                page.scaleX = 0.09f
                page.scaleY = 0.09f
                page.translationX = translationX
                page.scaleX = 1 - (Math.abs(position) * scaleFactor)
            }

            position <= 0 -> { // [-1,0]
                page.translationX = position * -margin
                page.scaleX = 1 - Math.abs(position) * 0.2f
                page.scaleY = 1 - Math.abs(position) * 0.2f
            }

            position <= 1 -> { // (0,1]
                page.translationX = position * margin
                page.scaleX = 1 - Math.abs(position) * 0.2f
                page.scaleY = 1 - Math.abs(position) * 0.2f
            }

            else -> { // (1,+Infinity]
                page.translationX = margin
                page.scaleX = 0.09f
                page.scaleY = 0.09f
            }
        }
    }
}