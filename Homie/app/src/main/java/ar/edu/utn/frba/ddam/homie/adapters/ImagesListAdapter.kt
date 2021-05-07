package ar.edu.utn.frba.ddam.homie.adapters

import android.app.ActionBar
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.cardview.widget.CardView
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import androidx.recyclerview.widget.RecyclerView
import ar.edu.utn.frba.ddam.homie.R
import ar.edu.utn.frba.ddam.homie.helpers.Utils

class ImagesListAdapter(private var context : Context, private var imagesList: MutableList<String>, private var onClick : (Int) -> Unit) : RecyclerView.Adapter<ImagesListAdapter.ImagesHolder>() {
    companion object {
        private val TAG = "ImagesListAdapter"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesHolder {
        val view =  LayoutInflater.from(parent.context).inflate(R.layout.item_image,parent,false)
        return (ImagesHolder(view))
    }

    override fun getItemCount(): Int {
        return imagesList.size
    }

    fun setData(newData: MutableList<String>) {
        this.imagesList = newData
        this.notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ImagesHolder, position: Int) {
        val image = imagesList[position]

        if(image != "") {
            holder.setImage(image);
        }

        holder.setLastMargin(position == (itemCount - 1))

        holder.getCardView().setOnClickListener {
            onClick(position)
        }
    }

    class ImagesHolder (v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v

        fun getCardView() : CardView {
            return view.findViewById<CardView>(R.id.cvItemImage)
        }

        fun setLastMargin(lastItem : Boolean) {
            val cv = view.findViewById<CardView>(R.id.cvItemImageSpace)
            if(lastItem) cv.visibility = View.VISIBLE
            else cv.visibility = View.GONE
        }

        fun setImage(image : String) {
            val img = view.findViewById<ImageView>(R.id.ivItemImage)
            val pb = view.findViewById<ProgressBar>(R.id.pbItemImage)
            Utils.setImage(view.context, view, img, pb, image, R.drawable.no_image, view.context.resources.getString(R.string.error_fetching_image))
        }
    }
}