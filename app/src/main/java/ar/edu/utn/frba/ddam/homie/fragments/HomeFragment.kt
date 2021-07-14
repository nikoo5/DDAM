package ar.edu.utn.frba.ddam.homie.fragments

import android.os.Bundle
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
import ar.edu.utn.frba.ddam.homie.entities.FunctionsResponse
import ar.edu.utn.frba.ddam.homie.entities.Post
import ar.edu.utn.frba.ddam.homie.entities.User
import ar.edu.utn.frba.ddam.homie.entities.UserPosts
import ar.edu.utn.frba.ddam.homie.helpers.Utils
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.Source
import com.google.firebase.functions.FirebaseFunctions
import com.google.gson.Gson
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import java.util.*
import kotlin.concurrent.fixedRateTimer


class HomeFragment : Fragment() {
    private lateinit var v : View
    private lateinit var srlPosts : SwipeRefreshLayout
    private lateinit var rvPosts : RecyclerView

    private lateinit var date : Date

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

        checkAppInit()

        return v;
    }

    override fun onStart() {
        super.onStart()
        //initCloudDB()
        if(Utils.getStartApp(v.context)) {
            Utils.setStartApp(v.context, false)
            val user = localDb.userDao().getByDbId(mAuth.currentUser!!.uid)
            var name : String = ""
            if(mAuth.currentUser!!.displayName != null) {
                name = mAuth.currentUser!!.displayName?.split('|')!![0]
            }
            if (user != null) {
                name = user.getDisplayName()
            }
            Utils.welcomeMessage(v, name)
        }
    }

    private fun checkAppInit() {
        user = localDb.userDao().getByDbId(mAuth.currentUser!!.uid)
        if (user == null) {
            try {
                localDb.userDao().clearTable()
                localDb.userDao().resetTable()

                localDb.userPostDao().clearTable()
                localDb.userPostDao().resetTable()
            } catch (e : Exception) { }

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
                                user = localDb.userDao().getByDbId(mAuth.currentUser!!.uid)
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
                            cloudDb.collection("users").add(user!!.getUserCloud());
                        }

                        postListAdapter.setUser(user!!)
                        updatePosts(false)
                        //Snackbar.make(v, R.string.scroll_to_update, Snackbar.LENGTH_SHORT).show()

                        srlPosts.setOnRefreshListener {
                            updatePosts(true)
                        }
                    }
        } else {
            postListAdapter.setUser(user!!)
            updatePosts(false)

            srlPosts.setOnRefreshListener {
                updatePosts(true)
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

    private fun updatePosts(forceCloud : Boolean) {
        try {
            var getFromCloud = forceCloud
            if (!forceCloud) {
                getFromCloud = localDb.postDao().getAll(1, 0).count() == 0;
            }

            if (getFromCloud) {
                date = Date()

                val parentJob = Job()
                val scope = CoroutineScope(Dispatchers.Default + parentJob)

                scope.launch() {
                    val docs = getPostsFromCloud()

                    for (doc in docs) updateLocalPostData(doc)

                    val views = getViewCountsFromCloud()

                    for (view in views) updateLocalViewsData(view);

                    val likes = getLikesFromCloud();

                    for (like in likes) {
                        val postId = updateLocalLikesData(like)
                        if (postId != "") {
                            val post = getSinglePostFromCloud(postId)

                            if (post != null) updateLocalPostData(post)

                            updateLocalLikesData(like)
                        }
                    }

                    syncLocalLikesToCloud(likes)

                    loadPostsFromLocalWithContext()
                }
            } else {
                loadPostsFromLocal()
            }
        } catch (e : Exception) {
            Snackbar.make(v, R.string.scroll_to_update, Snackbar.LENGTH_SHORT).show()
        }
    }

    suspend fun getPostsFromCloud() : List<DocumentSnapshot> {
        try {
            val result = cloudDb.collection("posts")
                    .whereGreaterThanOrEqualTo("last_update", Utils.getLastUpdate(v.context))
                    .limit(50)
                    .get(Source.SERVER)
                    .await();
            return result.documents
        } catch (e : Exception) {}
        return mutableListOf()
    }

    suspend fun updateLocalPostData(document : DocumentSnapshot) {
        val post = Post.PostCloud(document.data!!)
        LocalDatabase.updatePostFromCloud(v.context, document.id, post)
    }

    suspend fun getViewCountsFromCloud() : List<DocumentSnapshot> {
        try {
            val ids: MutableList<String> = mutableListOf()
            for (post in localDb.postDao().getAll()) {
                ids.add(post.dbId)
            }
            val result = cloudDb.collection("views")
                    .whereIn("post_id", ids)
                    .get(Source.SERVER)
                    .await()
            return result.documents
        } catch (e : Exception) { }
        return mutableListOf()
    }

    suspend fun updateLocalViewsData(document: DocumentSnapshot) : Boolean {
        val post = localDb.postDao().getByDbId(document.data!!["post_id"].toString())
        if(post != null) {
            post.viewCount = document.data!!["count"].toString().toInt()
            localDb.postDao().update(post);
            return true;
        }
        return false;
    }

    suspend fun getLikesFromCloud() : List<DocumentSnapshot> {
        try {
            val result = cloudDb.collection("likes")
                    .whereEqualTo("user_id", user!!.dbId)
                    .get(Source.SERVER)
                    .await()
            return result.documents
        } catch (e : Exception) { }
        return mutableListOf()
    }

    suspend fun updateLocalLikesData(document: DocumentSnapshot) : String {
        val data = document.data!!;
        val postId = data["post_id"].toString()
        val post = localDb.postDao().getByDbId(postId)
        if (post != null) {
            var userPost = localDb.userPostDao().getByBothId(user!!.id, post.id);
            if(userPost == null) {
                userPost = UserPosts(user!!.id, post.id);
                localDb.userPostDao().insert(userPost);
            }
            return ""
        }
        return postId
    }

    suspend fun getSinglePostFromCloud(postId : String) : DocumentSnapshot? {
        try {
            val result = cloudDb.collection("posts")
                    .whereEqualTo("post_id", postId)
                    .limit(1)
                    .get(Source.SERVER)
                    .await()
            if(result.documents.count() == 1) {
                return result.documents.first()
            }
        } catch (e : Exception) { }
        return null
    }

    suspend fun syncLocalLikesToCloud(likes : List<DocumentSnapshot>) {
        for (userPost in localDb.userPostDao().getByUserId(user!!.id)!!) {
            val userPostCloud = userPost.getUserPostCloud(v.context)
            var found = false
            for (like in likes) {
                if (like.data!!["post_id"].toString() == userPostCloud.post_id) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                if(userPost.date > Utils.getLastUpdate(v.context)) {
                    cloudDb.collection("likes").add(userPostCloud).await()
                } else {
                    localDb.userPostDao().delete(userPost)
                }
            }
        }
    }

    suspend fun loadPostsFromLocalWithContext() {
        withContext(Dispatchers.Main) {
            Snackbar.make(v, resources.getString(R.string.updated), Snackbar.LENGTH_SHORT).show()
            Utils.setLastUpdate(v.context, date)
            loadPostsFromLocal()
        }
    }
    private fun loadPostsFromLocal() {
        val posts = localDb.postDao().getAll();
        postListAdapter.setPosts(posts)
        srlPosts.isRefreshing = false
    }

    fun onPostOpen(postId: Int) {
        val post = localDb.postDao().getById(postId)!!
        post.viewCountRelative++
        localDb.postDao().update(post);

        val data = hashMapOf(
                "postId" to post.dbId,
                "viewCount" to post.viewCountRelative
        )
        data["postId"] = post.dbId
        data["viewCount"] = post.viewCountRelative
        val func = FirebaseFunctions.getInstance();
        //func.useEmulator("192.168.99.24", 5001)
        func.getHttpsCallable("incrementPostViews")
            .call(data)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val result = task.result!!.data
                    val data = FunctionsResponse(result as Map<String, Any>)

                    if(data.errorCode == 0 && data.result > 0) {
                        post.viewCount = data.result
                        post.viewCountRelative = 0
                        localDb.postDao().update(post);
                    }
                }
            }

        val action = HomeFragmentDirections.homeToPostDetail(postId)
        v.findNavController().navigate(action);
    }

    fun onPostLike(postId: Int, like: Boolean) {
        if(user!!.id == 0) {
            user = localDb.userDao().getByDbId(mAuth.currentUser!!.uid)
        }

        var userPost = localDb.userPostDao().getByBothId(user!!.id, postId);
        if(userPost != null) {
            if(!like) {
                localDb.userPostDao().delete(userPost);
            }
        } else {
            if(like) {
                userPost = UserPosts(user!!.id, postId);
                localDb.userPostDao().insert(userPost)
            }
        }

        updateLikeCloud(userPost, like);
    }

    fun updateLikeCloud(userPost : UserPosts?, add : Boolean) {
        if(userPost != null) {
            val userPostCloud = userPost.getUserPostCloud(v.context);
            cloudDb.collection("likes")
                    .whereEqualTo("user_id", userPostCloud.user_id)
                    .whereEqualTo("post_id", userPostCloud.post_id)
                    .get(Source.SERVER)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val data = task.result!!.documents

                            if (data.count() == 0 && add) {
                                cloudDb.collection("likes").add(userPostCloud)
                            } else {
                                var firstDoc = add
                                for (document in data) {
                                    if (!firstDoc) {
                                        cloudDb.collection("likes").document(document.id).delete();
                                    }
                                    firstDoc = false;
                                }
                            }
                        }
                    }
        }
    }
}