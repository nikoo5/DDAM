package ar.edu.utn.frba.ddam.homie.ui

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.TextView
import androidx.preference.PreferenceCategory
import androidx.preference.PreferenceViewHolder
import ar.edu.utn.frba.ddam.homie.R
import com.google.android.material.color.MaterialColors


class CustomPreferenceCategory : PreferenceCategory {
    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
    }

    override fun onBindViewHolder(holder: PreferenceViewHolder?) {
        super.onBindViewHolder(holder)
        context?.let {
            (holder?.findViewById(android.R.id.title) as? TextView)?.setTextColor(MaterialColors.getColor(context, R.attr.customIconColor, Color.BLACK))
        }
    }
}
