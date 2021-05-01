package ar.edu.utn.frba.ddam.homie.adapters

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
import androidx.recyclerview.widget.RecyclerView
import ar.edu.utn.frba.ddam.homie.R
import ar.edu.utn.frba.ddam.homie.entities.Post
import ar.edu.utn.frba.ddam.homie.helpers.Utils
import com.bumptech.glide.Glide
import java.text.NumberFormat
import kotlin.math.exp

class PostListAdapter(private var postList: MutableList<Post>, val onPostClick: (String) -> Unit, val onPostLike: (String, Boolean) -> Unit) : RecyclerView.Adapter<PostListAdapter.PostHolder>() {
//    lateinit var onPostClick : (String) -> Boolean;
//    lateinit var onPostLike : (String, Boolean) -> Boolean;

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

        if(post.building.images.count() > 0) {
            holder.setImage(post.building.images[0]);
        }

        holder.setStatus(post.status);
        holder.setType(post.type);
        holder.setTitle(post.building.type, post.building.surface_open, post.building.rooms);
        holder.setAddress(post.building.location.address, post.building.location.number);
        holder.setDistrict(post.building.location.district);
        holder.setPrice(post.price, post.currency);
        holder.setExpenses(post.expenses, post.currency);

        holder.getLikeCheckBox().setOnCheckedChangeListener {_, _ ->}
        holder.setLike(post.like);

        holder.getCardLayout().setOnClickListener {
            onPostClick(post.uid);
        }

        holder.getLikeCheckBox().setOnCheckedChangeListener { buttonView, isChecked ->
            val cb = buttonView as CheckBox;
            if(isChecked) {
                cb.backgroundTintList = cb.context.getColorStateList(R.color.red_900);
            } else {
                cb.backgroundTintList = cb.context.getColorStateList(R.color.black);
            }
            onPostLike(post.uid, isChecked);
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

        fun setImage(imgUrl : String) {
            val img = view.findViewById<ImageView>(R.id.ivPostImage);
            if (imgUrl != "") {
                Glide.with(view.context)
                    .load(imgUrl)
                    .centerCrop()
                    .into(img);
            } else {
                img.setImageResource(R.drawable.no_image)
            }
        }

        fun setStatus(status : String) {
            val tv = view.findViewById<TextView>(R.id.tvPostStatus)
            if (status == "reserved") {
                tv.visibility = View.VISIBLE;
            } else {
                tv.visibility = View.INVISIBLE;
            }
            tv.text = Utils.getString(view.context, "posts_status_" + status);
        }

        fun setType(type : String) {
            val tv = view.findViewById<TextView>(R.id.tvPostType)
            tv.text = Utils.getString(view.context, "posts_types_" + type);
        }

        fun setTitle(building_type : String, surface : Long, rooms : Int) {
            val tv = view.findViewById<TextView>(R.id.tvPostTitle)
            var txt : String = Utils.getString(view.context, "buildings_types_" + building_type) + " · " + surface.toString() + "m² · " + rooms.toString() + " " + view.resources.getString(R.string.room);
            if(rooms > 1) txt += "s";
            tv.text = txt;
        }

        fun setAddress(address : String, number : Int) {
            val tv = view.findViewById<TextView>(R.id.tvPostAddress)
            tv.text = address + " " + number.toString();
        }

        fun setDistrict(district : String) {
            val tv = view.findViewById<TextView>(R.id.tvPostDistrict)
            tv.text = district
        }

        fun setPrice(price : Int, currency : String) {
            val tv = view.findViewById<TextView>(R.id.tvPostPrice);
            tv.text = currency + " " + NumberFormat.getIntegerInstance().format(price).replace(",", ".");
        }

        fun setExpenses(expenses : Int, currency: String) {
            val tv = view.findViewById<TextView>(R.id.tvPostExpenses);
            if(expenses > 0) {
                tv.visibility = View.VISIBLE
            } else {
                tv.visibility = View.INVISIBLE
            }
            tv.text = "+ " + currency + " " + NumberFormat.getIntegerInstance().format(expenses).replace(",", ".") + " " + view.resources.getString(R.string.expenses);
        }

        fun setLike(like : Boolean) {
            val cb = view.findViewById<CheckBox>(R.id.cbPostLike);
            cb.isChecked = like;

            if(like) {
                cb.backgroundTintList = cb.context.getColorStateList(R.color.red_900);
            }
        }
    }
}