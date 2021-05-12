package ar.edu.utn.frba.ddam.homie.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ar.edu.utn.frba.ddam.homie.R
import ar.edu.utn.frba.ddam.homie.adapters.ImagesListAdapter
import ar.edu.utn.frba.ddam.homie.database.LocalDatabase
import ar.edu.utn.frba.ddam.homie.helpers.Utils

class PostDetailImagesFragment(postId : Int = 0) : Fragment() {
    private var postId : Int = postId

    private lateinit var v : View
    private lateinit var localDB : LocalDatabase

    private lateinit var cvPostDetailImageFull : CardView
    private lateinit var ivPostDetailImageFull : ImageView
    private lateinit var pbPostDetailImageFull : ProgressBar
    private lateinit var rvPostDetailImages : RecyclerView
    private lateinit var cvPostDetailImageBack : CardView
    private lateinit var cvPostDetailImageForward : CardView

    private lateinit var llm : LinearLayoutManager
    private lateinit var imagesListAdapter : ImagesListAdapter

    private lateinit var images : MutableList<String>
    private var currentPosition : Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_post_detail_images, container, false)

        localDB = LocalDatabase.getLocalDatabase(v.context)!!

        cvPostDetailImageFull = v.findViewById(R.id.cvPostDetailImageFull)
        ivPostDetailImageFull = v.findViewById(R.id.ivPostDetailImageFull)
        pbPostDetailImageFull = v.findViewById(R.id.pbPostDetailImageFull)
        rvPostDetailImages = v.findViewById(R.id.rvPostDetailImages)
        cvPostDetailImageBack = v.findViewById(R.id.cvPostDetailImageBack)
        cvPostDetailImageForward = v.findViewById(R.id.cvPostDetailImageForward)

        llm = LinearLayoutManager(context);
        rvPostDetailImages.setHasFixedSize(true);
        rvPostDetailImages.layoutManager = llm;
        imagesListAdapter = ImagesListAdapter(v.context, mutableListOf(), onClick = {position -> openImage(position)})
        rvPostDetailImages.adapter = imagesListAdapter

        cvPostDetailImageFull.setOnClickListener {
            cvPostDetailImageFull.visibility = View.GONE
        }

        cvPostDetailImageBack.setOnClickListener {
            if(currentPosition > 0) openImage(--currentPosition);
        }

        cvPostDetailImageForward.setOnClickListener {
            if(currentPosition < (images.size - 2)) openImage(++currentPosition);
        }

        return v
    }

    override fun onStart() {
        super.onStart()
        val post = localDB.postDao().getById(postId)!!
        val building = post.getBuilding(v.context)!!
        images = building.images
        imagesListAdapter.setData(images)
    }

    private fun openImage(position : Int) {
        currentPosition = position
        val img = images[position]
        cvPostDetailImageFull.visibility = View.VISIBLE
        Utils.setImage(v.context, v, ivPostDetailImageFull, pbPostDetailImageFull, img, R.drawable.no_image, resources.getString(R.string.error_fetching_image)) { loaded ->
            if(!loaded)
                cvPostDetailImageFull.visibility = View.GONE
        }
    }
}