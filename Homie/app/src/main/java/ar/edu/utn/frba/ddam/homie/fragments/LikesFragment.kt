package ar.edu.utn.frba.ddam.homie.fragments

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ar.edu.utn.frba.ddam.homie.R
import ar.edu.utn.frba.ddam.homie.adapters.LikeListAdapter
import ar.edu.utn.frba.ddam.homie.database.LocalDatabase
import ar.edu.utn.frba.ddam.homie.entities.User
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class LikesFragment : Fragment() {
    lateinit var v : View
    lateinit var rvLikes : RecyclerView

    lateinit var mAuth: FirebaseAuth
    lateinit var localDB : LocalDatabase

    lateinit var user : User

    lateinit var llm : LinearLayoutManager
    lateinit var likeListAdapter : LikeListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance();
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v =  inflater.inflate(R.layout.fragment_likes, container, false)

        localDB = LocalDatabase.getLocalDatabase(v.context)!!
        user = localDB.userDao().getByDbId(mAuth.currentUser!!.uid)!!

        rvLikes = v.findViewById(R.id.rvLikes)

        llm = LinearLayoutManager(context);
        rvLikes.setHasFixedSize(true);
        rvLikes.layoutManager = llm;

        return v;
    }

    override fun onStart() {
        super.onStart()
        loadLikesFromLocal();
        //loadLikes();
    }

    fun loadLikesFromLocal() {
        val posts = user.getLikePosts(v.context)

        likeListAdapter = LikeListAdapter(v.context, posts, onClick = { x -> onLikeOpen(x) }, onLongClick = { x -> onLikeRemove(x) });
        rvLikes.adapter = likeListAdapter;
    }

    private fun onLikeOpen(postId: Int) {
        val action = LikesFragmentDirections.likesToPostDetail(postId)
        v.findNavController().navigate(action);
    }

    private fun onLikeRemove(postId : Int) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(resources.getString(R.string.likes_remove_question_title));
        builder.setMessage(resources.getString(R.string.likes_remove_question_message))

        builder.setPositiveButton(resources.getString(R.string.yes).toUpperCase(), DialogInterface.OnClickListener { dialog, _ ->
            dialog.dismiss();
            val userPost = localDB.userPostDao().getByBothId(user.id, postId);
            if(userPost != null) {
                localDB.userPostDao().delete(userPost)
                loadLikesFromLocal()
            }
        })

        builder.setNegativeButton(resources.getString(R.string.no).toUpperCase(), DialogInterface.OnClickListener { dialog, _ ->
            dialog.dismiss();
        })

        builder.create().show();
    }
}