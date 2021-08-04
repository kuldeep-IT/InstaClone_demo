package com.peerbitskuldeep.firebaseshopping.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.peerbitskuldeep.firebaseshopping.R
import com.peerbitskuldeep.firebaseshopping.adapters.UserAdapter
import com.peerbitskuldeep.firebaseshopping.model.User
import kotlinx.android.synthetic.main.fragment_search.view.*
import java.util.*
import kotlin.collections.ArrayList


class SearchFragment : Fragment() {

    private var userList : MutableList<User>? = null
    private var adapter: UserAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_search, container, false)
        view.recycler_view_search.setHasFixedSize(true)
        view.recycler_view_search.layoutManager = LinearLayoutManager(context)
        userList = ArrayList()
//        adapter = context?.let { UserAdapter(it, userList as ArrayList<User>, true ) }
        adapter = context?.let { UserAdapter(it,userList as ArrayList<User>, true ) }
        view.recycler_view_search.adapter = adapter

//        view.recycler_view_search.apply {
//
//            layoutManager = LinearLayoutManager(context)
//
//            adapter= adapter
//
//        }

        view.edtSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if(view.edtSearch.text.toString() == "")
                {

                }
                else
                {
                    view.recycler_view_search?.visibility = View.VISIBLE

                    retrieveData()

                    searchUser(s.toString().toLowerCase())
                }

            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        return view
    }

    //search by name
    private fun searchUser(input: String) {

        val query = FirebaseDatabase.getInstance().getReference()
            .child("USERS")
            .orderByChild("name")
            .startAt(input)
            .endAt(input + "\uf8ff") // "\uf8ff" compulsory for search

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                userList?.clear()

                for (snapshot in dataSnapshot.children)
                {
                    val user = snapshot.getValue(User::class.java)

                    if (user != null)
                    {
                        userList?.add(user)
                    }
                }

                adapter?.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context,"Error! ${error.message.toString()}", Toast.LENGTH_SHORT).show()
            }
        })


    }

    private fun retrieveData() {
        val usersRef = FirebaseDatabase.getInstance().getReference().child("USERS")
        usersRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                if (view?.edtSearch?.text.toString() == "")
                {

                    userList?.clear()

                    for (snapshot in dataSnapshot.children)
                    {
                        val user = snapshot.getValue(User::class.java)

                        if (user != null)
                        {
                            userList?.add(user)
                        }

                    }

                    adapter?.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context,"Error! ${error.message.toString()}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    companion object
    {
        fun newInstance()= SearchFragment().apply {

        }
    }
}