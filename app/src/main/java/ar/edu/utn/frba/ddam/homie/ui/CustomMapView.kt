package ar.edu.utn.frba.ddam.homie.ui

import android.content.Context
import android.text.BoringLayout
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.MapView


class CustomMapView : MapView {
    private var canMove : Boolean = false
    private var click : Boolean = false;

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    )

    constructor(context: Context?, options: GoogleMapOptions?) : super(context, options)

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            click = true
        } else if (ev.action == MotionEvent.ACTION_UP) {
            click = false
            canMove = !canMove
        } else {
            click = false
        }

        parent.requestDisallowInterceptTouchEvent(canMove)
        return super.dispatchTouchEvent(ev)
    }
}