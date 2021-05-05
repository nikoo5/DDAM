package ar.edu.utn.frba.ddam.homie.fragments

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ar.edu.utn.frba.ddam.homie.R
import ar.edu.utn.frba.ddam.homie.activities.LoginActivity
import ar.edu.utn.frba.ddam.homie.adapters.LikeListAdapter
import ar.edu.utn.frba.ddam.homie.entities.Post
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue

class LikesFragment : Fragment() {
    lateinit var v : View
    lateinit var rvLikes : RecyclerView

    lateinit var mAuth: FirebaseAuth
    lateinit var db : FirebaseDatabase
    lateinit var rootRef : DatabaseReference
    lateinit var postsRef : DatabaseReference
    lateinit var likesRef : DatabaseReference

    lateinit var llm : LinearLayoutManager
    lateinit var likeListAdapter : LikeListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        rootRef = db.reference;
        postsRef = db.getReference("posts");
        likesRef = db.getReference("likes");
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v =  inflater.inflate(R.layout.fragment_likes, container, false)

        rvLikes = v.findViewById(R.id.rvLikes)

        llm = LinearLayoutManager(context);
        likeListAdapter = LikeListAdapter(mutableListOf(), { }, { } );

        rvLikes.setHasFixedSize(true);
        rvLikes.layoutManager = llm;
        rvLikes.adapter = likeListAdapter;

        return v;
    }

    override fun onStart() {
        super.onStart()

        loadLikes();
    }

    private fun loadLikes() {
        var posts_likes : MutableList<Post> = mutableListOf();
        var count : Int = 0;

        rootRef.child("likes/${mAuth.currentUser.uid}")
                .get()
                .addOnSuccessListener { snap ->
                    val ids = snap.getValue<List<Int>>()
                    if(ids != null) {
                        count = ids.size
                        for (id in ids) {
                            val post = rootRef.child("posts")
                                    .orderByKey()
                                    .equalTo(id.toString())
                                    .limitToFirst(1)
                                    .get()
                                    .addOnSuccessListener { snap2 ->
                                        val post = snap2.children.first().getValue<Post>()!!;
                                        posts_likes.add(post);

                                        count--;
                                        if (count == 0) {
                                            likeListAdapter = LikeListAdapter(posts_likes, onClick = { x -> onLikeOpen(x) }, onLongClick = { x -> onLikeRemove(x) });
                                            rvLikes.adapter = likeListAdapter;
                                            Snackbar.make(v, resources.getString(R.string.success_fetching_posts), Snackbar.LENGTH_SHORT).show();
                                        }
                                    }
                        }
                    }
                }
                .addOnFailureListener {
                    Snackbar.make(v, resources.getString(R.string.error_fetching_posts), Snackbar.LENGTH_SHORT).show();
                }

    }

    private fun onLikeOpen(id : Int) {
        Snackbar.make(v, "Click", Snackbar.LENGTH_SHORT).show();
    }

    private fun onLikeRemove(id : Int) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(resources.getString(R.string.likes_remove_question_title));
        builder.setMessage(resources.getString(R.string.likes_remove_question_message))

        builder.setPositiveButton(resources.getString(R.string.yes).toUpperCase(), DialogInterface.OnClickListener { dialog, which ->
            dialog.dismiss();

        })

        builder.setNegativeButton(resources.getString(R.string.no).toUpperCase(), DialogInterface.OnClickListener { dialog, which ->
            dialog.dismiss();
        })

        builder.create().show();
    }
}