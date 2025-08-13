package ru.vik.trials.pokemon.ui.common

import android.graphics.BitmapFactory
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toDrawable

/**
 * Устанавливает картинку над текстовым полем.
 * @param bytes Массив данных изображения.
 * @param resId Ресурс изображение по умолчанию.
 * */
fun TextView.setDrawableTop(bytes: ByteArray?, @DrawableRes resId: Int) {
    val image = if (bytes == null) {
        null
    } else {
        BitmapFactory.decodeByteArray(bytes, 0 , bytes.size)
    }

    val bitmap = image?.toDrawable(this.context.resources) ?: AppCompatResources.getDrawable(this.context, resId)
    this.setCompoundDrawablesWithIntrinsicBounds(null, bitmap, null, null)
}