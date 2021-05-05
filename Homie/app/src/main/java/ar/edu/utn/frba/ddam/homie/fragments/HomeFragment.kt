package ar.edu.utn.frba.ddam.homie.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ar.edu.utn.frba.ddam.homie.R
import ar.edu.utn.frba.ddam.homie.adapters.PostListAdapter
import ar.edu.utn.frba.ddam.homie.database.*
import ar.edu.utn.frba.ddam.homie.entities.*
import ar.edu.utn.frba.ddam.homie.helpers.Utils
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue

class HomeFragment : Fragment() {
    private lateinit var v : View
    private lateinit var btn : Button
    private lateinit var rvPosts : RecyclerView

    private lateinit var mAuth: FirebaseAuth
    private lateinit var db : FirebaseDatabase
    private lateinit var postsRef : DatabaseReference
    private lateinit var likesRef : DatabaseReference

    private var localDB: LocalDatabase? = null
    private var userDao : UserDao? = null
    private var postDao : PostDao? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();

        postsRef = db.getReference("posts");
        likesRef = db.getReference("likes");
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_home, container, false)

        localDB = LocalDatabase.getLocalDatabase(v.context)
        postDao = localDB?.postDao()
        userDao = localDB?.userDao()

        //val name = mAuth.currentUser?.displayName?.split('|')!!
        //LocalDatabase.initData(v.context, User(mAuth.currentUser?.uid!!, name[0], name[1], mAuth.currentUser?.email!!, "https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/profile_pictures%2FeqgfQsrUqtTo8AaTS3nkS3Ca5G73%2FuLXIzdwgnKk7.jpg?alt=media&token=ed9091fb-7dbc-4fe3-b2cb-cb32bb64a9d1"))

        btn = v.findViewById(R.id.btn);
        rvPosts = v.findViewById(R.id.rvPosts)

        return v;
    }

    override fun onStart() {
        super.onStart()
        loadPostsFromLocal()
    }

    private fun loadPostsFromLocal() {
        val posts = postDao?.getAll()!!;
        val llm = LinearLayoutManager(context);
        rvPosts.setHasFixedSize(true);
        rvPosts.layoutManager = llm;
        val postListAdapter = PostListAdapter(context, posts, onPostClick = { x -> onPostClick(x) }, onPostLike = { x, y -> onPostLike(x, y)});
        rvPosts.adapter = postListAdapter;
    }

    private fun loadPosts() {
        postsRef.get()
            .addOnSuccessListener { dataSnapshot ->
                val posts = dataSnapshot.getValue<MutableList<Post>>()!!
                val llm = LinearLayoutManager(context);
                rvPosts.setHasFixedSize(true);
                rvPosts.layoutManager = llm;

                likesRef.child(mAuth.currentUser?.uid!!).get()
                    .addOnSuccessListener { dataSnapshot2 ->
                        var likes : MutableList<Int>? = dataSnapshot2.getValue<MutableList<Int>>()
                        if(likes == null) likes = mutableListOf();

                        for (id in likes) {
                           for (i in posts.indices) {
                               if(posts[i].id == id) {
                                   posts[i].like = true;
                                   break;
                               }
                           }
                        }
                    }
                    .addOnCompleteListener {
                        val postListAdapter = PostListAdapter(context, posts, onPostClick = { x -> onPostClick(x) }, onPostLike = { x, y -> onPostLike(x, y)});
                        rvPosts.adapter = postListAdapter;
                        //Snackbar.make(v, resources.getString(R.string.success_fetching_posts), Snackbar.LENGTH_SHORT).show();
                    }
            }
            .addOnFailureListener {
                Snackbar.make(v, resources.getString(R.string.error_fetching_posts), Snackbar.LENGTH_SHORT).show();
            }
    }

    fun onPostClick (id : Int) {
        Snackbar.make(v, resources.getString(R.string.future_feature), Snackbar.LENGTH_SHORT).show();
    }

    fun onPostLike (id : Int, like : Boolean) {
        likesRef.child(mAuth.currentUser?.uid!!).get()
            .addOnSuccessListener { dataSnapshot ->
                var likes : MutableList<Int>? = dataSnapshot.getValue<MutableList<Int>>()
                if(likes == null) likes = mutableListOf();

                var update : Boolean = false;

                val alreadyLike = likes.find { x -> x == id }
                if(like && alreadyLike == null) {
                    likes.add(id)
                    update = true;
                } else if (!like && alreadyLike != null) {
                    likes.remove(id);
                    update = true;
                }

                if(update) {
                    likesRef.child(mAuth.currentUser?.uid!!).setValue(likes).addOnFailureListener {
                        loadPosts();
                    }
                }
            }
    }
}