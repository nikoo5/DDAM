package ar.edu.utn.frba.ddam.homie.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import ar.edu.utn.frba.ddam.homie.R
import ar.edu.utn.frba.ddam.homie.activities.MainActivity
import ar.edu.utn.frba.ddam.homie.entities.Post
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class PostDetailFragment : Fragment() {
    private lateinit var v : View

    private lateinit var tlPostDetail : TabLayout
    private lateinit var vpPostDetail : ViewPager2

    private var postId : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (context as MainActivity).supportActionBar?.title = resources.getString(R.string.post_detail)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_post_detail, container, false)

        activity?.actionBar?.title = ""

        postId = PostDetailFragmentArgs.fromBundle(requireArguments()).postId

        tlPostDetail = v.findViewById(R.id.tlPostDetail)
        vpPostDetail = v.findViewById(R.id.vpPostDetail)

        vpPostDetail.adapter = ViewPagerAdapter(requireActivity(), postId)
        TabLayoutMediator(tlPostDetail, vpPostDetail, TabLayoutMediator.TabConfigurationStrategy { tab, position ->
            when (position) {
                0 -> tab.text = resources.getString(R.string.post_detail_info)
                1 -> tab.text = resources.getString(R.string.post_detail_images)
            }
        }).attach()

        return  v
    }

    class ViewPagerAdapter(fragmentActivity: FragmentActivity, postId : Int) : FragmentStateAdapter(fragmentActivity) {
        var postId : Int = postId

        override fun createFragment(position: Int): Fragment {
            return when(position) {
                0 -> PostDetailInfoFragment(postId)
                1 -> PostDetailImagesFragment(postId)
                else -> PostDetailInfoFragment(postId)
            }
        }

        override fun getItemCount(): Int {
            return TAB_COUNT
        }

        companion object{
            private const val TAB_COUNT = 2
        }
    }

}