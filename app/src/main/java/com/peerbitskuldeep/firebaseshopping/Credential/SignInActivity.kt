package com.peerbitskuldeep.firebaseshopping.Credential

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.peerbitskuldeep.firebaseshopping.MainActivity
import com.peerbitskuldeep.firebaseshopping.R
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.fragment_home.*

class SignInActivity : AppCompatActivity() {

    private lateinit var mAuth : FirebaseAuth
    private lateinit var pd : ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        pd = ProgressDialog(this@SignInActivity)
        pd.setTitle("Authenticating...")
        pd.setMessage("Please Wait!")
        pd.setCanceledOnTouchOutside(false)


        btnSignUpSignIn.setOnClickListener {

            startActivity(Intent(this@SignInActivity, SignUpActivity::class.java))

        }

        btnSignInSignIn.setOnClickListener {

            signInCredential()

        }

    }

    private fun signInCredential() {

        val email = edtEmailSignIn.text.toString()
        val passWord = edtPassWordSignIn.text.toString()

        when {

            TextUtils.isEmpty(email) -> {
                Toast.makeText(this, "Email is required!", Toast.LENGTH_SHORT).show()
                return
            }
            TextUtils.isEmpty(passWord) -> {
                Toast.makeText(this, "Email is required!", Toast.LENGTH_SHORT).show()
                return
            }
            else ->
            {
                mAuth = FirebaseAuth.getInstance()

                mAuth.signInWithEmailAndPassword(email, passWord)
                    .addOnCompleteListener { task ->

                    if (task.isSuccessful)
                    {
                        val intent = Intent(this@SignInActivity, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finish()
                    }
                    else
                    {
                        val errorMsg = task.exception.toString()
                        mAuth.signOut()
                        Toast.makeText(this, "Error! $errorMsg", Toast.LENGTH_SHORT).show()
                        pd.dismiss()
                    }

                }


            }
        }

    }

    override fun onStart() {
        super.onStart()

        if(FirebaseAuth.getInstance().currentUser != null)
        {
            val intent = Intent(this@SignInActivity, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }

    }

}