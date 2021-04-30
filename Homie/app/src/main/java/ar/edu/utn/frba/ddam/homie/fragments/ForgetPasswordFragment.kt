package ar.edu.utn.frba.ddam.homie.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import ar.edu.utn.frba.ddam.homie.R
import ar.edu.utn.frba.ddam.homie.activities.MainActivity
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class ForgetPasswordFragment : Fragment() {
    lateinit var mAuth: FirebaseAuth

    lateinit var v : View
    lateinit var etForgetPasswordEmail : TextInputLayout;
    lateinit var bForgetPasswordSend : Button;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance();
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_forget_password, container, false)

        etForgetPasswordEmail = v.findViewById(R.id.etForgetPasswordEmail);
        bForgetPasswordSend = v.findViewById(R.id.bForgetPasswordSend);

        return v;
    }

    override fun onStart() {
        super.onStart()

        bForgetPasswordSend.setOnClickListener {
            bForgetPasswordSend.isEnabled = false;

            val email = etForgetPasswordEmail.editText?.text.toString();

            if(validateEmail(email)) {
                bForgetPasswordSend.text = resources.getString(R.string.forget_password_sending);

                mAuth.languageCode = "es";
                mAuth.sendPasswordResetEmail(email).addOnCompleteListener {
                    bForgetPasswordSend.isEnabled = true;
                    bForgetPasswordSend.text = resources.getString(R.string.forget_password_send);
                    Snackbar.make(v, resources.getString(R.string.forget_password_sent), Snackbar.LENGTH_SHORT).show();
                    requireActivity().onBackPressed();
                }
            } else {
                bForgetPasswordSend.isEnabled = true;
            }
        }
    }

    private fun validateEmail(email : String) : Boolean {
        var valid : Boolean = false;
        if(email.isEmpty()) {
            etForgetPasswordEmail.error = resources.getString(R.string.forget_password_error_empty_email);
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etForgetPasswordEmail.error = resources.getString(R.string.forget_password_error_wrong_email);
        } else {
            etForgetPasswordEmail.error = null;
            valid = true;
        }
        return valid;
    }
}