package com.example.storyapp.view.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import com.example.storyapp.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class EmailEditText(context: Context, attrs: AttributeSet) : TextInputEditText(context, attrs) {
    private lateinit var parentLayout: TextInputLayout

    init {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                parentLayout.error = null
                val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
                if (!s.toString().matches(emailPattern.toRegex())) {
                    parentLayout.error = context.getString(R.string.email_invalid)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    fun setParentLayout(layout: TextInputLayout) {
        parentLayout = layout
    }
}