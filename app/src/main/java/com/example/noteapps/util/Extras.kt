package com.example.noteapps.util

import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment

/**
 *  مخفی (گان) کردن لایه ها
 */
fun goneLayout(vararg view: View) {
    view.forEach { it.visibility = View.GONE }
}

/**
 * نمایان کردن لایه ها
 */
fun visibleLayout(vararg view: View) {
    view.forEach { it.visibility = View.VISIBLE }
}

/**
 * خالی کردن ادیت تکست ها
 */
fun clearFields(vararg etText: EditText) {
    etText.forEach { it.setText("") }
}

/**
 *گرفتن متن و نمایش آن بصورت توست
 */
fun Fragment.showToast(message: String) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
}