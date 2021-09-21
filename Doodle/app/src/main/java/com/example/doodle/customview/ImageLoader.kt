package com.example.doodle.customview

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.ImageView

private const val TAG = "ImageLoader"

object ImageLoader {

    fun loadImage(context: Context, imageView: ImageView) {
        imageView.setImageBitmap(decodeSampleBitmap(context, imageView.width, imageView.height))
    }

    //参考了谷歌文档 https://developer.android.com/topic/performance/graphics/load-bitmap#kotlin
    private fun decodeSampleBitmap(
        context: Context,
        requestWidth: Int,
        requestHeight: Int
    ): Bitmap {
        return BitmapFactory.Options().run {
            inJustDecodeBounds = true
            //加载原始bitmap的宽高信息
            BitmapFactory.decodeStream(
                context.assets.open("image.JPG"),
                null,
                this
            )
            this.inSampleSize = calculateSubSampleSizeForBitmap(this, requestHeight, requestWidth)
            this.inJustDecodeBounds = false
            BitmapFactory.decodeStream(
                context.assets.open("image.JPG"), null, this
            )!!
        }
    }

    private fun calculateSubSampleSizeForBitmap(
        option: BitmapFactory.Options,
        requestHeight: Int,
        requestWidth: Int
    ): Int {
        var inSample = 1
        //计算新的采样率，来确定使用多少内存来显示图片
        val (imageHeight, imageWidth) = option.run { (outHeight to outWidth) } //1.获取图片原始宽高，可估算出图片完整加载后的内存大小
        if (imageHeight > requestHeight || imageWidth > requestWidth) { //2.如果图片大于目标imageView的宽高，则需要下采样
            val halfHeight = imageHeight / 2 //图片高度像素减少一半
            val halfWidth = imageWidth / 2//图片宽度像素减少一半
            //如果图片原始高度是其要载入的目标ImageView的宽高一倍，则需要下采样。
            if (halfHeight / inSample >= requestHeight && halfWidth / inSample >= requestWidth) {
                inSample *= 2 //每次缩小的比例为2的幂次方，这是因为底层解码器最终会向下舍入为最接近2的幂。
            }
        }
        Log.d(
            TAG,
            "imageHeight = $imageHeight, imageWidth = $imageWidth newInSampleSize = $inSample"
        )
        return inSample
    }
}