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
import ar.edu.utn.frba.ddam.homie.entities.Building
import ar.edu.utn.frba.ddam.homie.entities.Location
import ar.edu.utn.frba.ddam.homie.entities.Post
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

class HomeFragment : Fragment() {
    lateinit var v : View
    lateinit var btn : Button
    lateinit var rvPosts : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_home, container, false)

        btn = v.findViewById(R.id.btn);
        rvPosts = v.findViewById(R.id.rvPosts)

        return v;
    }

    override fun onStart() {
        super.onStart()
        loadPosts();

//        btn.setOnClickListener {
//
//            var posts : MutableList<Post> = mutableListOf()
//            posts.add(Post(Utils.generateHash(12), "rental", "avaiable", Building("building", Location("Capital Federal", "Villa del Parque", "Teodoro Vilardebó", 2755, 6, "B", "lat", "long"), (42).toLong(), (42).toLong(), 2), 29000, 3200, "$"));
//            posts.add(Post(Utils.generateHash(12), "rental", "avaiable", Building("building", Location("Capital Federal", "Villa del Parque", "Argerich", 3053, 3, "C", "lat", "long"), (27).toLong(), (33).toLong(), 1), 25000, 4300, "$"));
//            posts.add(Post(Utils.generateHash(12), "rental", "avaiable", Building("building", Location("Capital Federal", "Villa del Parque", "Nogoya", 3000, 1, "A", "lat", "long"), (60).toLong(), (70).toLong(), 3), 60000, 7800, "$"));
//            posts.add(Post(Utils.generateHash(12), "rental", "avaiable", Building("building", Location("Capital Federal", "Villa del Parque", "Simbron", 2900, 1, "A", "lat", "long"), (81).toLong(), (90).toLong(), 4), 69200, 11000, "$"));
//            posts.add(Post(Utils.generateHash(12), "rental", "reserved", Building("building", Location("Capital Federal", "Villa del Parque", "Remedios Escalada de San Martin", 3006, 6, "B", "lat", "long"), (43).toLong(), (47).toLong(), 2), 34000, 3100, "$"));
//            posts.add(Post(Utils.generateHash(12), "rental", "avaiable", Building("building", Location("Capital Federal", "Villa del Parque", "Nazca", 1359, 4, "A", "lat", "long"), (31).toLong(), (31).toLong(), 1), 20000, 3000, "$"));
//            posts.add(Post(Utils.generateHash(12), "rental", "avaiable", Building("building", Location("Capital Federal", "Villa del Parque", "Melincue", 3000, 3, "C", "lat", "long"), (45).toLong(), (45).toLong(), 2), 29500, 5200, "$"));
//            posts.add(Post(Utils.generateHash(12), "rental", "avaiable", Building("building", Location("Capital Federal", "Villa del Parque", "Cuenca", 2000, 5, "B", "lat", "long"), (32).toLong(), (32).toLong(), 2), 26000, 3500, "$"));
//
//
//            val db = FirebaseDatabase.getInstance();
//            val ref = db.getReference("posts");
//            ref.setValue(posts);
//        }
    }

    private fun loadPosts() {
        val db = FirebaseDatabase.getInstance();
        val ref = db.getReference("posts");

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val posts = dataSnapshot.getValue<MutableList<Post>>()

                val llm = LinearLayoutManager(context);
                rvPosts.setHasFixedSize(true);
                rvPosts.layoutManager = llm;

                val postListAdapter = PostListAdapter(posts!!) { x ->
                    onPostClick(x);
                }

                rvPosts.adapter = postListAdapter;
                Snackbar.make(v, resources.getString(R.string.success_fetching_posts), Snackbar.LENGTH_SHORT).show();
            }

            override fun onCancelled(error: DatabaseError) {
                Snackbar.make(v, resources.getString(R.string.error_fetching_posts), Snackbar.LENGTH_SHORT).show();
            }
        })
    }

    fun onPostClick (uid : String) : Boolean {
        //Snackbar.make(v, uid.toString(), Snackbar.LENGTH_SHORT).show();
        return false;
    }

    fun onPostLike (uid : String, like : Boolean) : Boolean {
        return false;
    }
}