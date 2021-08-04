package com.peerbitskuldeep.firebaseshopping.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.peerbitskuldeep.firebaseshopping.R
import com.peerbitskuldeep.firebaseshopping.editprofile.EditProfile
import com.peerbitskuldeep.firebaseshopping.model.User
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*


class ProfileFragment : Fragment() {

    private lateinit var profileId: String
    private lateinit var firebaseUser: FirebaseUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        firebaseUser =  FirebaseAuth.getInstance().currentUser!!

        val pref = context?.getSharedPreferences("PREF", Context.MODE_PRIVATE)

        if (pref != null)
        {
            this.profileId = pref.getString("PROFILEID","")!!
        }

        if (profileId == firebaseUser.uid)
        {
            view.tvEdtProfile.text = "Edit Profile"
        }
        else if(profileId != firebaseUser.uid)
        {
            checkFollowAndFollowingTvStatus()
        }

        view.tvEdtProfile.setOnClickListener {

            startActivity(Intent(context, EditProfile::class.java))

        }

        getFollowers()
        getFollowing()
        userInfo()

        return view
    }


    private fun checkFollowAndFollowingTvStatus() {
        val followingRef = firebaseUser?.uid.let { it1 ->

            FirebaseDatabase.getInstance().reference
                .child("Follow").child(it1.toString())
                .child("Following")
        }

        if (followingRef != null)
        {
            followingRef.addValueEventListener(object : ValueEventListener {
                @SuppressLint("SetTextI18n")
                override fun onDataChange(snapshot: DataSnapshot) {

                    if (snapshot.child(profileId).exists())
                    {
                        view?.tvEdtProfile?.text = "Following"
                    }
                    else
                    {
                        view?.tvEdtProfile?.text = "Follow"
                    }

                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
        }
    }

    private fun getFollowing() {
//        val followersRef =
            FirebaseDatabase.getInstance().reference
            .child("Follow").child(profileId)
            .child("Following")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    if (snapshot.exists())
                    {
                        view?.tvFollowing_profile?.text = snapshot.childrenCount.toString()
                    }

                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }


    private fun getFollowers()
    {
//        val followersRef =
            FirebaseDatabase.getInstance().reference
            .child("Follow").child(profileId)
            .child("Followers")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    if (snapshot.exists())
                    {
                        view?.tvFollowers_profile?.text = snapshot.childrenCount.toString()
                    }

                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }

    private fun userInfo()
    {
//        val userRef =
            FirebaseDatabase.getInstance().getReference()
            .child("USERS")
            .child(profileId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    if (snapshot.exists())
                    {
                        val user = snapshot.getValue<User>(User::class.java)

                        if (user?.getImage()?.isEmpty() == true) {
                            profile_image.setImageResource(R.drawable.profile);
                        } else{
                            Picasso.get().load(user?.getImage()).into(profile_image);
                        }

//                        Picasso.get().load(user?.getImage()).placeholder(R.drawable.profile).into(profile_image)


                        view?.tvNamemProfile?.text = user?.getName()
                        view?.tvUserName_profile?.text = user?.getUserName()
                        view?.tvBioProfile?.text = user?.getBio()

                    }

                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }

    override fun onStop() {
        super.onStop()

        val pref = context?.getSharedPreferences("PREF",Context.MODE_PRIVATE)?.edit()
        pref?.putString("PROFILEID", firebaseUser.uid)
        pref?.apply()
    }

    override fun onPause() {
        super.onPause()

        val pref = context?.getSharedPreferences("PREF",Context.MODE_PRIVATE)?.edit()
        pref?.putString("PROFILEID", firebaseUser.uid)
        pref?.apply()
    }

    override fun onDestroy() {
        super.onDestroy()

        val pref = context?.getSharedPreferences("PREF",Context.MODE_PRIVATE)?.edit()
        pref?.putString("PROFILEID", firebaseUser.uid)
        pref?.apply()
    }


    companion object
    {
        fun newInstance()= ProfileFragment().apply {  }
    }
}