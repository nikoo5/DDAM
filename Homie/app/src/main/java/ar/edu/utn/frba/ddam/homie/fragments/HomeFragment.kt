package ar.edu.utn.frba.ddam.homie.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ar.edu.utn.frba.ddam.homie.R
import ar.edu.utn.frba.ddam.homie.adapters.PostListAdapter
import ar.edu.utn.frba.ddam.homie.database.LocalDatabase
import ar.edu.utn.frba.ddam.homie.entities.Post
import ar.edu.utn.frba.ddam.homie.entities.User
import ar.edu.utn.frba.ddam.homie.entities.UserPosts
import ar.edu.utn.frba.ddam.homie.helpers.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*


class HomeFragment : Fragment() {
    private lateinit var v : View
    private lateinit var srlPosts : SwipeRefreshLayout
    private lateinit var rvPosts : RecyclerView

    private lateinit var mAuth: FirebaseAuth
    private lateinit var cloudDb : FirebaseFirestore
    private lateinit var localDb: LocalDatabase

    private var user : User? = null

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var postListAdapter: PostListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_home, container, false)

        mAuth = FirebaseAuth.getInstance();
        cloudDb = FirebaseFirestore.getInstance()
        localDb = LocalDatabase.getLocalDatabase(v.context)!!

        srlPosts = v.findViewById(R.id.srlPosts)
        rvPosts = v.findViewById(R.id.rvPosts)

        linearLayoutManager = LinearLayoutManager(context);
        rvPosts.setHasFixedSize(true);
        rvPosts.layoutManager = linearLayoutManager;
        postListAdapter = PostListAdapter(context, null, mutableListOf(), onPostClick = { x -> onPostOpen(x) }, onPostLike = { x, y -> onPostLike(x, y) });
        rvPosts.adapter = postListAdapter;

        return v;
    }

    override fun onStart() {
        super.onStart()
        //initCloudDB()
        checkAppInit()
    }

    private fun checkAppInit() {
//        if(localDb.postDao().getAll(1, 0).count() == 0) {
//            LocalDatabase.initData(v.context)
//        }

        user = localDb.userDao().getByDbId(mAuth.currentUser!!.uid)
        if (user == null) {
            localDb.userDao().clearTable()
            localDb.userDao().resetTable()

            localDb.userPostDao().clearTable()
            localDb.userPostDao().resetTable()

            cloudDb.collection("users")
                    .whereEqualTo("id", mAuth.currentUser!!.uid)
                    .get()
                    .addOnCompleteListener { task ->
                        var created = false
                        var count = 0

                        if(task.isSuccessful) {
                            count = task.result!!.documents.count()
                            if(count == 1) {
                                val data = task.result!!.documents.first().data!!
                                user = User(mAuth.currentUser?.uid!!, data["name"].toString(), data["last_name"].toString(), data["email"].toString(), data["image"].toString())
                                localDb.userDao().insert(user);
                                created = true
                            }
                        }

                        if(!created) {
                            val name = mAuth.currentUser!!.displayName?.split('|')!!
                            user = User(mAuth.currentUser?.uid!!, name[0], name[1], mAuth.currentUser?.email!!, "")
                            localDb.userDao().insert(user)

                            for(document in task.result!!.documents) {
                                cloudDb.collection("users").document(document.id).delete()
                            }
                            cloudDb.collection("users").add(user!!.getUserCloud(v.context));
                        }

                        postListAdapter.setUser(user!!)
                        updatePostsFromCloud()

                        srlPosts.setOnRefreshListener {
                            updatePostsFromCloud()
                        }
                    }
        } else {
            postListAdapter.setUser(user!!)
            updatePostsFromCloud()

            srlPosts.setOnRefreshListener {
                updatePostsFromCloud()
            }
        }
    }

    private fun initCloudDB() {
        val posts = localDb.postDao().getAll();
        for(post in posts) cloudDb.collection("posts").add(post.getPostCloud(v.context));

//        db.collection("users")
//            .add(usersCloud)
//            .addOnSuccessListener { documentReference ->
//                Log.d("TEST", "DocumentSnapshot added with ID: " + documentReference.id)
//            }
//            .addOnFailureListener { e -> Log.w("TEST", "Error adding document", e) }
    }

    private fun updatePostsFromCloud() {
        srlPosts.isRefreshing = true

        val date = Date()
        cloudDb.collection("posts")
                .whereGreaterThanOrEqualTo("last_update", Utils.getLastUpdate(v.context))
                .limit(50)
                .get()
                .addOnCompleteListener { task ->
                    if(task.isSuccessful) {
                        Utils.setLastUpdate(v.context, date)
                        for (document in task.result!!.documents) {
                            val post = Post.PostCloud(document.data!!)
                            LocalDatabase.updatePostFromCloud(v.context, post)
                        }
                        loadPostsFromLocal()
                    } else {
                        loadPostsFromLocal()
                    }
                }
    }

    private fun loadPostsFromLocal() {
        val posts = localDb.postDao().getAll();
        postListAdapter.setPosts(posts)
        srlPosts.isRefreshing = false
    }

    fun onPostOpen(postId: Int) {
        val action = HomeFragmentDirections.homeToPostDetail(postId)
        v.findNavController().navigate(action);
    }

    fun onPostLike(postId: Int, like: Boolean) {
        val userPost = localDb.userPostDao().getByBothId(user!!.id, postId);
        if(userPost != null) {
            if(!like) {
                localDb.userPostDao().delete(userPost);
            }
        } else {
            if(like) {
                localDb.userPostDao().insert(UserPosts(user!!.id, postId))
            }
        }
    }
}