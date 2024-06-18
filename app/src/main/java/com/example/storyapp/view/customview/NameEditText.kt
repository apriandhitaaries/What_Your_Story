package com.example.storyapp.view.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import com.example.storyapp.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class NameEditText(context: Context, attrs: AttributeSet) : TextInputEditText(context, attrs) {
    private lateinit var parentLayout: TextInputLayout

    init {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                parentLayout.error = null
                if (s.isNullOrEmpty()) {
                    parentLayout.error = context.getString(R.string.name_required)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    fun setParentLayout(layout: TextInputLayout) {
        parentLayout = layout
    }
}