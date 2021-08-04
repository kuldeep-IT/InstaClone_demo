package com.peerbitskuldeep.firebaseshopping.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.peerbitskuldeep.firebaseshopping.R
import com.peerbitskuldeep.firebaseshopping.fragments.ProfileFragment
import com.peerbitskuldeep.firebaseshopping.model.User
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.user_item_layout.view.*

class UserAdapter(
    private var context: Context,
    private var user: List<User>,
    private var isFragment: Boolean = false
) :
    RecyclerView.Adapter<UserAdapter.RecViewHolder>() {

    private var firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser

    inner class RecViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.user_item_layout, parent, false)
        return RecViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecViewHolder, position: Int) {
        val currentPosition = user[position]

        holder.itemView.userNameSearch.text = currentPosition.getUserName()
        holder.itemView.nameSearch.text = currentPosition.getName()

        Picasso.get().load(currentPosition.getImage()).placeholder(R.drawable.profile)
            .into(holder.itemView.imgProfileSearch)

        checkFollowingStatus(currentPosition.getUid(), holder.itemView.btnFollowSearch)

        holder.itemView.setOnClickListener {

            val pref = context.getSharedPreferences("PREF", Context.MODE_PRIVATE).edit()

            pref.putString("PROFILEID", currentPosition.getUid())
            pref.apply()

            (context as FragmentActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.flayout, ProfileFragment()).commit()

        }


        holder.itemView.btnFollowSearch.setOnClickListener {

            if (holder.itemView.btnFollowSearch.text.toString() == "Follow") {

                firebaseUser?.uid.let { it1 ->

                    FirebaseDatabase.getInstance().reference
                        .child("Follow").child(it1.toString()) //currentloggedIn account id
                        .child("Following").child(currentPosition.getUid()) //other user id
                        .setValue(true)
                        .addOnCompleteListener { task ->

                            if (task.isSuccessful) {
                                firebaseUser?.uid.let { it1 ->
                                    FirebaseDatabase.getInstance().reference
                                        .child("Follow").child(currentPosition.getUid())
                                        .child("Followers").child(it1.toString()) //
                                        .setValue(true)
                                        .addOnCompleteListener { task ->

                                            if (task.isSuccessful) {

                                            }
                                        }
                                }

                            }

                        }

                }

            } else //button text following //user want to unfollow
            {
                firebaseUser?.uid.let { it1 ->

                    FirebaseDatabase.getInstance().reference
                        .child("Follow").child(it1.toString())
                        .child("Following").child(currentPosition.getUid()) //
                        .removeValue()
                        .addOnCompleteListener { task ->

                            if (task.isSuccessful) {
                                firebaseUser?.uid.let { it1 ->
                                    FirebaseDatabase.getInstance().reference
                                        .child("Follow").child(currentPosition.getUid())
                                        .child("Followers").child(it1.toString()) //
                                        .removeValue()
                                        .addOnCompleteListener { task ->

                                            if (task.isSuccessful) {

                                            }
                                        }
                                }

                            }

                        }

                }
            }

        }

    }

    override fun getItemCount(): Int {
        return user.size
    }

    private fun checkFollowingStatus(uid: String, btnFollowSearch: Button?) {

        val followingRef = firebaseUser?.uid.let { it1 ->

            FirebaseDatabase.getInstance().reference
                .child("Follow").child(it1.toString())
                .child("Following")
        }

        followingRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.child(uid).exists()) {
                    btnFollowSearch!!.text = "Following"
                } else {
                    btnFollowSearch!!.text = "Follow"
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

    }
}