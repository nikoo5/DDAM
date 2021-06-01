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

class NewUserFragment : Fragment() {
    lateinit var mAuth: FirebaseAuth

    lateinit var v : View
    lateinit var etNewUserName : TextInputLayout;
    lateinit var etNewUserLastname : TextInputLayout;
    lateinit var etNewUserEmail : TextInputLayout;
    lateinit var etNewUserReEmail : TextInputLayout;
    lateinit var etNewUserPassword : TextInputLayout;
    lateinit var etNewUserRePassword : TextInputLayout;
    lateinit var bNewUserCreate : Button;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance();
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_new_user, container, false)

        etNewUserName = v.findViewById(R.id.etNewUserName);
        etNewUserLastname = v.findViewById(R.id.etNewUserLastname);
        etNewUserEmail = v.findViewById(R.id.etNewUserEmail);
        etNewUserReEmail = v.findViewById(R.id.etNewUserReEmail);
        etNewUserPassword = v.findViewById(R.id.etNewUserPassword);
        etNewUserRePassword = v.findViewById(R.id.etNewUserRePassword);
        bNewUserCreate = v.findViewById(R.id.bNewUserCreate);

        return v;
    }

    override fun onStart() {
        super.onStart()

        bNewUserCreate.setOnClickListener {
            bNewUserCreate.isEnabled = false;

            val name = etNewUserName.editText?.text.toString();
            val lastname = etNewUserLastname.editText?.text.toString();
            val email = etNewUserEmail.editText?.text.toString();
            val reemail = etNewUserReEmail.editText?.text.toString();
            val pass = etNewUserPassword.editText?.text.toString();
            val repass = etNewUserRePassword.editText?.text.toString();

            if(validateName(name) && validateLastName(lastname) && validateEmail(email) && validateReEmail(email, reemail) && validatePassword(pass) && validateRePassword(pass, repass)) {
                bNewUserCreate.text = resources.getString(R.string.new_user_creating);

                mAuth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(requireActivity()) { task ->
                        Snackbar.make(v, resources.getString(R.string.new_user_success), Snackbar.LENGTH_SHORT).show()
                        if (task.isSuccessful) {
                            etNewUserName.editText?.setText("");
                            etNewUserLastname.editText?.setText("");
                            etNewUserEmail.editText?.setText("");
                            etNewUserReEmail.editText?.setText("");
                            etNewUserPassword.editText?.setText("");
                            etNewUserRePassword.editText?.setText("");

                            val user = mAuth.currentUser!!

                            val profileUpdate = UserProfileChangeRequest.Builder()
                                    .setDisplayName(name + "|" + lastname)
                                    .build();

                            user.updateProfile(profileUpdate).addOnCompleteListener(requireActivity()) { task2 ->
                                if(task2.isSuccessful) {
                                    Snackbar.make(v, resources.getString(R.string.new_user_success), Snackbar.LENGTH_SHORT).show()
                                } else {
                                    Snackbar.make(v, resources.getString(R.string.new_user_fail_update), Snackbar.LENGTH_SHORT).show()
                                }
                                startActivity(Intent(requireContext(), MainActivity::class.java));
                                requireActivity().finish();
                            }
                        } else {
                            Snackbar.make(v, resources.getString(R.string.new_user_fail), Snackbar.LENGTH_SHORT).show()
                            bNewUserCreate.text = resources.getString(R.string.new_user_create);
                            bNewUserCreate.isEnabled = true;
                        }
                    }
            } else {
                bNewUserCreate.isEnabled = true;
            }
        }
    }

    private fun validateName(name : String) : Boolean {
        var valid : Boolean = false;
        if(name.isEmpty()) {
            etNewUserName.error = resources.getString(R.string.new_user_error_empty_name);
        } else if (name.length < 4) {
            etNewUserName.error = resources.getString(R.string.new_user_error_wrong_name);
        } else {
            etNewUserName.error = null;
            valid = true;
        }
        return valid;
    }

    private fun validateLastName(lastname : String) : Boolean {
        var valid : Boolean = false;
        if(lastname.isEmpty()) {
            etNewUserLastname.error = resources.getString(R.string.new_user_error_empty_lastname);
        } else if (lastname.length < 4) {
            etNewUserLastname.error = resources.getString(R.string.new_user_error_wrong_lastname);
        } else {
            etNewUserLastname.error = null;
            valid = true;
        }
        return valid;
    }

    private fun validateEmail(email : String) : Boolean {
        var valid : Boolean = false;
        if(email.isEmpty()) {
            etNewUserEmail.error = resources.getString(R.string.new_user_error_empty_email);
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etNewUserEmail.error = resources.getString(R.string.new_user_error_wrong_email);
        } else {
            etNewUserEmail.error = null;
            valid = true;
        }
        return valid;
    }

    private fun validateReEmail(email : String, reemail : String) : Boolean {
        var valid : Boolean = false;
        if(reemail.isEmpty()) {
            etNewUserReEmail.error = resources.getString(R.string.new_user_error_empty_reemail);
        } else if (email != reemail) {
            etNewUserReEmail.error = resources.getString(R.string.new_user_error_wrong_reemail);
        } else {
            etNewUserReEmail.error = null;
            valid = true;
        }
        return valid;
    }

    private fun validatePassword(pass : String) : Boolean {
        var valid : Boolean = false;
        if (pass.isEmpty()) {
            etNewUserPassword.error = resources.getString(R.string.new_user_error_empty_password);
        } else if (pass.length < 6) {
            etNewUserPassword.error = resources.getString(R.string.new_user_error_wrong_password);
        } else {
            etNewUserPassword.error = null;
            valid = true;
        }
        return valid;
    }

    private fun validateRePassword(pass : String, repass : String) : Boolean {
        var valid : Boolean = false;
        if (repass.isEmpty()) {
            etNewUserRePassword.error = resources.getString(R.string.new_user_error_empty_repassword);
        } else if (pass != repass) {
            etNewUserRePassword.error = resources.getString(R.string.new_user_error_wrong_repassword);
        } else {
            etNewUserRePassword.error = null;
            valid = true;
        }
        return valid;
    }
}