package ar.edu.utn.frba.ddam.homie.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import ar.edu.utn.frba.ddam.homie.R
import ar.edu.utn.frba.ddam.homie.activities.MainActivity
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_login.*


class LoginFragment : Fragment() {
    lateinit var mAuth: FirebaseAuth

    lateinit var v : View
    lateinit var etEmail : TextInputLayout;
    lateinit var etPassword : TextInputLayout;
    lateinit var btnLogIn : Button;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance();
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_login, container, false)

        etEmail = v.findViewById(R.id.etEmail);
        etPassword = v.findViewById(R.id.etPassword);
        btnLogIn = v.findViewById(R.id.bLogIn);

        return v;
    }

    override fun onStart() {
        super.onStart()
        val currentUser = mAuth.currentUser;
        if(currentUser != null) {
            startActivity(Intent(requireContext(), MainActivity::class.java));
            requireActivity().finish();
        } else {
            tvLoginForgetPass.setOnClickListener {
                Snackbar.make(v, resources.getString(R.string.future_feature), Snackbar.LENGTH_SHORT).show();
//                val action = LoginFragmentDirections.toForgetPassword();
//                v.findNavController().navigate(action);
            }

            tvLoginNewUser.setOnClickListener {
                val action = LoginFragmentDirections.toNewUser();
                v.findNavController().navigate(action);
            }

            btnLogIn.setOnClickListener {
                btnLogIn.isEnabled = false;

                val email = etEmail.editText?.text.toString();
                val pass = etPassword.editText?.text.toString();

                if(validateEmail(email) && validatePassword(pass)) {
                    btnLogIn.text = resources.getString(R.string.login_in);

                    mAuth.signInWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(requireActivity()) { task ->
                            if (task.isSuccessful) {
                                startActivity(Intent(requireContext(), MainActivity::class.java));
                                requireActivity().finish();
                            } else {
                                Snackbar.make(v, resources.getString(R.string.login_error_email_password), Snackbar.LENGTH_SHORT).show();
                                btnLogIn.isEnabled = true;
                                btnLogIn.text = resources.getString(R.string.login);
                            }
                        }
                } else {
                    btnLogIn.isEnabled = true;
                }
            }
        }
    }

    private fun validateEmail(email : String) : Boolean {
        var valid : Boolean = false;
        if(email.isEmpty()) {
            etEmail.error = resources.getString(R.string.login_error_empty_email);
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.error = resources.getString(R.string.login_error_wrong_email);
        } else {
            etEmail.error = null;
            valid = true;
        }
        return valid;
    }

    private fun validatePassword(pass : String) : Boolean {
        var valid : Boolean = false;
        if (pass.isEmpty()) {
            etPassword.error = resources.getString(R.string.login_error_empty_password);
        } else if (pass.length < 6) {
            etPassword.error = resources.getString(R.string.login_error_wrong_password);
        } else {
            etPassword.error = null;
            valid = true;
        }
        return valid;
    }
}