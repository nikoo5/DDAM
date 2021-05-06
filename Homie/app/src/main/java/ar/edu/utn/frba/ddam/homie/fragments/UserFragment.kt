package ar.edu.utn.frba.ddam.homie.fragments

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import ar.edu.utn.frba.ddam.homie.R
import ar.edu.utn.frba.ddam.homie.database.LocalDatabase
import ar.edu.utn.frba.ddam.homie.entities.User
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_user.*

class UserFragment : Fragment() {
    private lateinit var v : View

    private lateinit var ivUserImage : ImageView
    private lateinit var pbUserLoading : ProgressBar
    private lateinit var cvUserEditUserImage : CardView
    private lateinit var tvUserDisplayname : TextView

    private lateinit var llUserLikesCount : LinearLayout
    private lateinit var llUserCommentsCount : LinearLayout
    private lateinit var llUserFriendsCount : LinearLayout

    private lateinit var tvUserLikesCount : TextView
    private lateinit var tvUserCommentsCount : TextView
    private lateinit var tvUserFriendsCount : TextView

    private lateinit var mAuth: FirebaseAuth
    private var db : LocalDatabase? = null

    private lateinit var user : User
    private var likesCount : Int = 0
    private var commentsCount : Int = 0
    private var friendsCount : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_user, container, false)

        mAuth = FirebaseAuth.getInstance()
        db = LocalDatabase.getLocalDatabase(v.context)

        ivUserImage = v.findViewById(R.id.ivUserImage)
        pbUserLoading = v.findViewById(R.id.pbUserLoading)
        cvUserEditUserImage = v.findViewById(R.id.cvUserEditUserImage)
        tvUserDisplayname = v.findViewById(R.id.tvUserDisplayName)

        llUserLikesCount = v.findViewById(R.id.llUserLikesCount)
        llUserCommentsCount = v.findViewById(R.id.llUserCommentsCount)
        llUserFriendsCount = v.findViewById(R.id.llUserFriendsCount)

        tvUserLikesCount = v.findViewById(R.id.tvUserLikesCount)
        tvUserCommentsCount = v.findViewById(R.id.tvUserCommentsCount)
        tvUserFriendsCount = v.findViewById(R.id.tvUserFriendsCount)

        user = db?.userDao()?.getByDbId(mAuth.currentUser?.uid!!)!!

        likesCount = user.getLikesCount(v.context)
        commentsCount = user.getCommentsCount(v.context)
        friendsCount = user.getFriendsCount(v.context)

        cvUserEditUserImage.setOnClickListener {
            Snackbar.make(v, resources.getString(R.string.future_feature), Snackbar.LENGTH_SHORT).show()
        }

        llUserLikesCount.setOnClickListener {
            Snackbar.make(v, resources.getString(R.string.user_likes_count_message).replace("{count}", likesCount.toString()), Snackbar.LENGTH_SHORT).show()
        }

        llUserCommentsCount.setOnClickListener {
            Snackbar.make(v, resources.getString(R.string.user_comments_count_message).replace("{count}", commentsCount.toString()), Snackbar.LENGTH_SHORT).show()
        }

        llUserFriendsCount.setOnClickListener {
            Snackbar.make(v, resources.getString(R.string.user_friends_count_message).replace("{count}", friendsCount.toString()), Snackbar.LENGTH_SHORT).show()
        }
        return v
    }

    override fun onStart() {
        super.onStart()

        loadUserImage(user.image)
        loadUserData(user)
    }

    private fun loadUserData(user: User) {
        tvUserDisplayname.text = "${user.name} ${user.lastName}"
        tvUserLikesCount.text = likesCount.toString()
        tvUserCommentsCount.text = commentsCount.toString()
        tvUserFriendsCount.text = friendsCount.toString()
    }

    private fun loadUserImage(imgUrl: String) {
        pbUserLoading.visibility = View.VISIBLE
        if (imgUrl != "") {
            Glide.with(v.context)
                    .load(imgUrl)
//                    .diskCacheStrategy(DiskCacheStrategy.NONE)
//                    .skipMemoryCache(true)
                    .centerCrop()
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                            ivUserImage.setImageResource(R.drawable.ic_user_solid)
                            pbUserLoading.visibility = View.INVISIBLE
                            Snackbar.make(v, resources.getString(R.string.error_profile_image), Snackbar.LENGTH_SHORT).show()
                            return true
                        }

                        override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                            pbUserLoading.visibility = View.INVISIBLE
                            return false
                        }
                    })
                    .into(ivUserImage)
        } else {
            ivUserImage.setImageResource(R.drawable.ic_user_solid)
            pbUserLoading.visibility = View.INVISIBLE
        }
    }
}