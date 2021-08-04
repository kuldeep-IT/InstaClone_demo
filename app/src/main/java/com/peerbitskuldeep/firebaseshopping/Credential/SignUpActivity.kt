package com.peerbitskuldeep.firebaseshopping.Credential

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.peerbitskuldeep.firebaseshopping.MainActivity
import com.peerbitskuldeep.firebaseshopping.R
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.util.*
import kotlin.collections.HashMap

class SignUpActivity : AppCompatActivity() {

   private lateinit var mAuth : FirebaseAuth
   private lateinit var pd :ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        pd = ProgressDialog(this@SignUpActivity)
        pd.setTitle("Authenticating...")
        pd.setMessage("Please Wait!")
        pd.setCanceledOnTouchOutside(false)

        btnSignInSignUp.setOnClickListener {

            startActivity(Intent(this, SignInActivity::class.java))

        }

        btnSignUpSignUP.setOnClickListener {

            createCredential()

        }

    }

    private fun createCredential() {
        val email = edtEmailSignUp.text.toString()
        val userName = edtUserNameSignUp.text.toString()
        val name = edtNameSignUp.text.toString()
        val passWord = edtPassWordSignUp.text.toString()

        when {

            TextUtils.isEmpty(email) -> {
                Toast.makeText(this, "Email is required!", Toast.LENGTH_SHORT).show()
                return
            }

            TextUtils.isEmpty(userName) -> {
                Toast.makeText(this, "UserName is required!", Toast.LENGTH_SHORT).show()
                return
            }

            TextUtils.isEmpty(name) -> {
                Toast.makeText(this, "Name is required!", Toast.LENGTH_SHORT).show()
                return
            }

            TextUtils.isEmpty(passWord) -> {
                Toast.makeText(this, "PassWord is required!", Toast.LENGTH_SHORT).show()
                return
            }



            else ->
            {
                pd.show()

                mAuth = FirebaseAuth.getInstance()

                mAuth.createUserWithEmailAndPassword(email, passWord)
                    .addOnCompleteListener { task ->

                        if (task.isSuccessful)
                        {
                            saveUserInfo(name,userName,email)
                        }
                        else
                        {
                            val errorMsg = task.exception.toString()
                            mAuth.signOut()
                            Toast.makeText(this@SignUpActivity, "Error! $errorMsg", Toast.LENGTH_SHORT).show()
                            pd.dismiss()
                        }


                    }

            }

        }
    }

    private fun saveUserInfo(name: String, userName: String, email: String) {

        val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
        val userRef= FirebaseDatabase.getInstance().reference.child("USERS")

        val userMap = HashMap<String, Any>()
        userMap["uid"] = currentUserId
        userMap["name"] = name.toLowerCase()
        userMap["userName"] = userName.toLowerCase()
        userMap["email"] = email
        userMap["bio"] = "Hey! I'm using insta too..."
        userMap["image"] = "https://firebasestorage.googleapis.com/v0/b/shopping-app-ce7d5.appspot.com/o/profile_pic%2Fprofile.png?alt=media&token=4a162683-24c3-4cda-bb3d-7e756aade3c4"

        userRef.child(currentUserId).setValue(userMap)
            .addOnCompleteListener { task ->

                if (task.isSuccessful)
                {
                    pd.dismiss()
                    Toast.makeText(this@SignUpActivity, "Account created successfully!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                }
                else
                {
                    val errorMsg = task.exception.toString()
                    mAuth.signOut()
                    Toast.makeText(this@SignUpActivity, "Error! $errorMsg", Toast.LENGTH_SHORT).show()
                    pd.dismiss()
                }

            }


    }
}