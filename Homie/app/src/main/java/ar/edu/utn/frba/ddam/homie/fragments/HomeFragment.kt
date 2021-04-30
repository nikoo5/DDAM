package ar.edu.utn.frba.ddam.homie.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import ar.edu.utn.frba.ddam.homie.R
import ar.edu.utn.frba.ddam.homie.activities.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class HomeFragment : Fragment() {
    lateinit var v : View
    lateinit var bLogOut : Button;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_home, container, false)

        bLogOut = v.findViewById(R.id.bLogOut);

        return v;
    }

    override fun onStart() {
        super.onStart()

        bLogOut.setOnClickListener {
//            val profileUpdate = UserProfileChangeRequest.Builder()
//                .setDisplayName("Usuario|Prueba")
//                .setPhotoUri(Uri.parse("https://images.unsplash.com/photo-1535713875002-d1d0cf377fde"))
//                .build();
//
//            user.updateProfile(profileUpdate).addOnCompleteListener(requireActivity()) { task ->
//                if(task.isSuccessful) {
//
//                }
//            }

            FirebaseAuth.getInstance().signOut();
            startActivity(Intent(requireContext(), LoginActivity::class.java));
            requireActivity().finish();
        }
    }
}