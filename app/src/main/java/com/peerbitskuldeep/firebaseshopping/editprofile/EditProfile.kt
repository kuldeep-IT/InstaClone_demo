package com.peerbitskuldeep.firebaseshopping.editprofile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.peerbitskuldeep.firebaseshopping.Credential.SignInActivity
import com.peerbitskuldeep.firebaseshopping.MainActivity
import com.peerbitskuldeep.firebaseshopping.R
import kotlinx.android.synthetic.main.activity_edit_profile.*

class EditProfile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        btnLogoutAccEdt.setOnClickListener {

            FirebaseAuth.getInstance().signOut()

            Toast.makeText( this, "LogOut Successfully!", Toast.LENGTH_SHORT).show()

            val intent = Intent(this@EditProfile, SignInActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()

        }
    }
}