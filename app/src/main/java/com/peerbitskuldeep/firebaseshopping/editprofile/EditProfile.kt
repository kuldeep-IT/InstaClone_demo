package com.peerbitskuldeep.firebaseshopping.editprofile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.peerbitskuldeep.firebaseshopping.Credential.SignInActivity
import com.peerbitskuldeep.firebaseshopping.MainActivity
import com.peerbitskuldeep.firebaseshopping.R
import com.peerbitskuldeep.firebaseshopping.fragments.ProfileFragment
import com.peerbitskuldeep.firebaseshopping.model.User
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*

class EditProfile : AppCompatActivity() {

    private lateinit var firebaseUser: FirebaseUser

    private var checker = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        firebaseUser = FirebaseAuth.getInstance().currentUser!!

        btnLogoutAccEdt.setOnClickListener {

            FirebaseAuth.getInstance().signOut()

            Toast.makeText(this, "LogOut Successfully!", Toast.LENGTH_SHORT).show()

            val intent = Intent(this@EditProfile, SignInActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()

        }

        imgSaveEdt.setOnClickListener {

            if (checker == "clicked") {

            } else {
                updateInfoOnly()
            }

        }

        userInfo()

    }

    private fun updateInfoOnly() {

        when {

            TextUtils.isEmpty(edtNameEdt.text.toString()) ->
                Toast.makeText(this, "Please enter your Name!", Toast.LENGTH_SHORT).show()

            TextUtils.isEmpty(edtUserNameEdt.text.toString()) ->
                Toast.makeText(this, "Please enter your User Name!", Toast.LENGTH_SHORT).show()

            TextUtils.isEmpty(edtBioEdt.text.toString()) ->
                Toast.makeText(this, "Please enter your Bio!", Toast.LENGTH_SHORT).show()

            else -> {

                val userRef = FirebaseDatabase.getInstance().reference
                    .child("USERS")

                val userMap = HashMap<String, Any>()

                userMap["name"] = edtNameEdt.text.toString().toLowerCase()
                userMap["userName"] = edtUserNameEdt.text.toString().toLowerCase()
                userMap["bio"] = edtBioEdt.text.toString().toLowerCase()

                userRef.child(firebaseUser.uid).updateChildren(userMap)

                Toast.makeText(this, "Data Updated Successfully!", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()

            }

        }
    }

    private fun userInfo() {
//        val userRef =
        FirebaseDatabase.getInstance().getReference()
            .child("USERS")
            .child(firebaseUser.uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    if (snapshot.exists()) {
                        val user = snapshot.getValue<User>(User::class.java)

                        if (user?.getImage()?.isEmpty() == true) {
                            imgProfileEdit.setImageResource(R.drawable.profile);
                        } else {
                            Picasso.get().load(user?.getImage()).into(imgProfileEdit);
                        }

//                        Picasso.get().load(user?.getImage()).placeholder(R.drawable.profile).into(profile_image)


                        edtNameEdt.setText(user?.getName())
                        edtUserNameEdt.setText(user?.getUserName())
                        edtBioEdt.setText(user?.getBio())

                    }

                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }

}