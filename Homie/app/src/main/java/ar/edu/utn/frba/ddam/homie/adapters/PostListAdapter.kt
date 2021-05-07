package ar.edu.utn.frba.ddam.homie.adapters

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.ColorFilter
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.graphics.toColor
import androidx.recyclerview.widget.RecyclerView
import ar.edu.utn.frba.ddam.homie.R
import ar.edu.utn.frba.ddam.homie.entities.Post
import ar.edu.utn.frba.ddam.homie.entities.User
import ar.edu.utn.frba.ddam.homie.helpers.Utils
import com.bumptech.glide.Glide
import com.google.android.material.color.MaterialColors
import java.text.NumberFormat
import kotlin.math.exp

class PostListAdapter(private var context : Context?, private var user : User, private var postList: MutableList<Post>, val onPostClick: (Int) -> Unit, val onPostLike: (Int, Boolean) -> Unit) : RecyclerView.Adapter<PostListAdapter.PostHolder>() {
    companion object {
        private val TAG = "PostListAdapter"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val view =  LayoutInflater.from(parent.context).inflate(R.layout.item_post,parent,false)
        return (PostHolder(view))
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    fun setData(newData: ArrayList<Post>) {
        this.postList = newData
        this.notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        val post = postList[position];
        val building = post.getBuilding(context!!)!!
        val location = building.getLocation(context!!)!!
        val like = post.getUserLike(context!!, user.id)

        holder.setImage(context!!, building.images);
        holder.setStatus(post.status);
        holder.setType(post.type);
        holder.setTitle(building.type, building.surfaceOpen, building.rooms);
        holder.setAddress(location.address, location.number);
        holder.setDistrict(location.district);
        holder.setPrice(post.price, post.currency);
        holder.setExpenses(post.expenses, post.currency);
        holder.setLike(context!!, like);

        holder.getCardLayout().setOnClickListener {
            onPostClick(post.id);
        }

        holder.getLikeCheckBox().setOnCheckedChangeListener { buttonView, isChecked ->
            val cb = buttonView as CheckBox;
            if(isChecked) {
                cb.backgroundTintList = context!!.getColorStateList(R.color.red_900);
            } else {
                cb.backgroundTintList = ColorStateList.valueOf(MaterialColors.getColor(context!!, R.attr.colorOnPrimary, Color.BLACK))
            }
            onPostLike(post.id, isChecked);
        }

    }


    class PostHolder (v: View) : RecyclerView.ViewHolder(v) {
        private var view: View
        init {
            this.view = v
        }

        fun getCardLayout () : CardView {
            return view.findViewById(R.id.item_post_card_view)
        }

        fun getLikeCheckBox () : CheckBox {
            return view.findViewById(R.id.cbPostLike)
        }

        fun setImage(context : Context, images : MutableList<String>) {
            val img = view.findViewById<ImageView>(R.id.ivPostImage);
            if(images.count() > 0) {
                if (images.first() != "") {
                    Glide.with(context)
                            .load(images.first())
                            .centerCrop()
                            .into(img);
                } else {
                    img.setImageResource(R.drawable.no_image)
                }
            } else {
                img.setImageResource(R.drawable.no_image)
            }
        }

        fun setStatus(status : String) {
            val cv = view.findViewById<CardView>(R.id.cvPostStatus)
            val tv = view.findViewById<TextView>(R.id.tvPostStatus)
            if (status == "reserved") {
                cv.visibility = View.VISIBLE;
            } else {
                cv.visibility = View.INVISIBLE;
            }
            tv.text = Utils.getString(view.context, "posts_status_${status}");
        }

        fun setType(type : String) {
            val tv = view.findViewById<TextView>(R.id.tvPostType)
            tv.text = Utils.getString(view.context, "posts_types_${type}");
        }

        fun setTitle(building_type : String, surface : Long, rooms : Int) {
            val tv = view.findViewById<TextView>(R.id.tvPostTitle)
            var txt : String = "${Utils.getString(view.context, "buildings_types_${building_type}")} · ${surface.toString()} m² · ${rooms.toString()} ${view.resources.getString(R.string.room)}";
            if(rooms > 1) txt += "s";
            tv.text = txt;
        }

        fun setAddress(address : String, number : Int) {
            val tv = view.findViewById<TextView>(R.id.tvPostAddress)
            tv.text = "$address ${number.toString()}";
        }

        fun setDistrict(district : String) {
            val tv = view.findViewById<TextView>(R.id.tvPostDistrict)
            tv.text = district
        }

        fun setPrice(price : Int, currency : String) {
            val tv = view.findViewById<TextView>(R.id.tvPostPrice);
            tv.text = "$currency ${NumberFormat.getIntegerInstance().format(price).replace(",", ".")}";
        }

        fun setExpenses(expenses : Int, currency: String) {
            val tv = view.findViewById<TextView>(R.id.tvPostExpenses);
            if(expenses > 0) {
                tv.visibility = View.VISIBLE
            } else {
                tv.visibility = View.INVISIBLE
            }
            tv.text = "+ $currency ${NumberFormat.getIntegerInstance().format(expenses).replace(",", ".")} ${view.resources.getString(R.string.expenses)}";
        }

        fun setLike(context : Context, like : Boolean) {
            val cb = view.findViewById<CheckBox>(R.id.cbPostLike);
            cb.setOnCheckedChangeListener(null)

            cb.isChecked = like;
            if (like) {
                cb.backgroundTintList = context.getColorStateList(R.color.red_900);
            } else {
                cb.backgroundTintList = ColorStateList.valueOf(MaterialColors.getColor(context, R.attr.colorOnPrimary, Color.BLACK))
            }
        }
    }
}