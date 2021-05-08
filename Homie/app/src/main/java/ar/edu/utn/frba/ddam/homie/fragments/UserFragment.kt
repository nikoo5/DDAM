package ar.edu.utn.frba.ddam.homie.fragments

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import ar.edu.utn.frba.ddam.homie.R
import ar.edu.utn.frba.ddam.homie.database.LocalDatabase
import ar.edu.utn.frba.ddam.homie.entities.User
import ar.edu.utn.frba.ddam.homie.helpers.Permissions
import ar.edu.utn.frba.ddam.homie.helpers.Utils
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import kotlin.math.roundToInt


class UserFragment : Fragment() {
    private lateinit var v : View
    private var PICK_IMAGE_REQUEST = 100

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

    private lateinit var cvUserImageUploading : CardView
    private lateinit var pbUserImageUploading : ProgressBar

    private lateinit var mAuth: FirebaseAuth
    private lateinit var localDB : LocalDatabase

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
        localDB = LocalDatabase.getLocalDatabase(v.context)!!

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

        cvUserImageUploading = v.findViewById(R.id.cvUserImageUploading)
        pbUserImageUploading = v.findViewById(R.id.pbUserImageUploading)

        user = localDB.userDao()?.getByDbId(mAuth.currentUser?.uid!!)!!

        likesCount = user.getLikesCount(v.context)
        commentsCount = user.getCommentsCount(v.context)
        friendsCount = user.getFriendsCount(v.context)

        cvUserEditUserImage.setOnClickListener {
            val permissions = Permissions(v.context, PICK_IMAGE_REQUEST)

            if(permissions.askForPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, PICK_IMAGE_REQUEST)
            } else {
                Snackbar.make(v, v.context.resources.getString(R.string.permisson_rejected), Snackbar.LENGTH_SHORT).show()
            }
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
        Utils.setImage(v.context, v, ivUserImage, pbUserLoading, user.image, R.drawable.ic_user_solid, resources.getString(R.string.error_profile_image))
        loadUserData(user)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            val filePath = data.data!!
            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = v.context.contentResolver!!.query(data.data!!, filePathColumn, null, null, null)!!
            cursor.moveToFirst()
            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
            val file = File(cursor.getString(columnIndex))
            cursor.close()

            val newName = "${Utils.generateHash(12)}.${file.extension}"
            val storageRef = FirebaseStorage.getInstance().reference

            cvUserImageUploading.visibility = View.VISIBLE
            pbUserImageUploading.progress = 0

            storageRef.child("profile_pictures/${mAuth.currentUser!!.uid}/${newName}")
                    .putFile(filePath)
                    .addOnProgressListener { task ->
                        val progress = 100.0 * task.bytesTransferred / task.totalByteCount
                        pbUserImageUploading.progress = progress.roundToInt()
                    }
                    .addOnCompleteListener { task ->
                        cvUserImageUploading.visibility = View.GONE
                        pbUserImageUploading.progress = 0

                        if (task.isSuccessful) {
                            task.result?.storage!!.downloadUrl.addOnSuccessListener { task2 ->
                                val url = task2.toString()
                                user.image = url
                                localDB.userDao().update(user)
                                Snackbar.make(v, resources.getString(R.string.uploading_success), Snackbar.LENGTH_SHORT).show()
                                Utils.setImage(v.context, v, ivUserImage, pbUserLoading, user.image, R.drawable.ic_user_solid, resources.getString(R.string.error_profile_image))
                            }
                        } else if (task.isCanceled) {
                            Snackbar.make(v, resources.getString(R.string.uploading_cancel), Snackbar.LENGTH_SHORT).show()
                        } else {
                            Snackbar.make(v, resources.getString(R.string.uploading_error), Snackbar.LENGTH_SHORT).show()
                        }
                    }
        }
    }

    private fun loadUserData(user: User) {
        tvUserDisplayname.text = user.getDisplayName()
        tvUserLikesCount.text = likesCount.toString()
        tvUserCommentsCount.text = commentsCount.toString()
        tvUserFriendsCount.text = friendsCount.toString()
    }
}