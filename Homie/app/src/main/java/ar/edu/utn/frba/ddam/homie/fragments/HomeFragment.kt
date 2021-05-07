package ar.edu.utn.frba.ddam.homie.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ar.edu.utn.frba.ddam.homie.R
import ar.edu.utn.frba.ddam.homie.adapters.PostListAdapter
import ar.edu.utn.frba.ddam.homie.database.*
import ar.edu.utn.frba.ddam.homie.entities.*
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class HomeFragment : Fragment() {
    private lateinit var v : View
    private lateinit var btn : Button
    private lateinit var rvPosts : RecyclerView

    private lateinit var mAuth: FirebaseAuth

    private lateinit var localDB: LocalDatabase

    private lateinit var user : User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance();
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_home, container, false)

        localDB = LocalDatabase.getLocalDatabase(v.context)!!

        //val name = mAuth.currentUser?.displayName?.split('|')!!
        //LocalDatabase.initData(v.context, User(mAuth.currentUser?.uid!!, name[0], name[1], mAuth.currentUser?.email!!, "https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/profile_pictures%2FeqgfQsrUqtTo8AaTS3nkS3Ca5G73%2FuLXIzdwgnKk7.jpg?alt=media&token=ed9091fb-7dbc-4fe3-b2cb-cb32bb64a9d1"))

        user = localDB.userDao().getByDbId(mAuth.currentUser?.uid!!)!!;

        btn = v.findViewById(R.id.btn);
        rvPosts = v.findViewById(R.id.rvPosts)

        return v;
    }

    override fun onStart() {
        super.onStart()
        loadPostsFromLocal()
    }

    private fun loadPostsFromLocal() {
        val posts = localDB.postDao().getAll();
        val llm = LinearLayoutManager(context);
        rvPosts.setHasFixedSize(true);
        rvPosts.layoutManager = llm;
        val postListAdapter = PostListAdapter(context, user, posts, onPostClick = { x -> onPostOpen(x) }, onPostLike = { x, y -> onPostLike(x, y)});
        rvPosts.adapter = postListAdapter;
    }

    fun onPostOpen (postId : Int) {
        val action = HomeFragmentDirections.homeToPostDetail(postId)
        v.findNavController().navigate(action);
    }

    fun onPostLike (postId : Int, like : Boolean) {
        val userPost = localDB.userPostDao().getByBothId(user.id, postId);
        if(userPost != null) {
            if(!like) {
                localDB.userPostDao().delete(userPost);
            }
        } else {
            if(like) {
                localDB.userPostDao().insert(UserPosts(user.id, postId))
            }
        }
    }
}