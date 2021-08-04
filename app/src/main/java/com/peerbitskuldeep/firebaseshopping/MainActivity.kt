package com.peerbitskuldeep.firebaseshopping

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.peerbitskuldeep.firebaseshopping.fragments.HomeFragment
import com.peerbitskuldeep.firebaseshopping.fragments.NotificationFragment
import com.peerbitskuldeep.firebaseshopping.fragments.ProfileFragment
import com.peerbitskuldeep.firebaseshopping.fragments.SearchFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var arrFragment: ArrayList<Fragment> = arrayListOf()

    private fun init()
    {
        if (arrFragment.isEmpty())
        {
            arrFragment.add(HomeFragment.newInstance())
            arrFragment.add(NotificationFragment.newInstance())
            arrFragment.add(ProfileFragment.newInstance())
            arrFragment.add(SearchFragment.newInstance())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
        putFragment(0)
        nav_view.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)



    }

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener{

        when(it.itemId)
        {

            R.id.nav_home ->
            {
                putFragment(0)
                return@OnNavigationItemSelectedListener true
            }

            R.id.nac_search -> {
                putFragment(3)
                return@OnNavigationItemSelectedListener true
            }

            R.id.nav_add_post ->
            {

                return@OnNavigationItemSelectedListener true
            }

            R.id.nav_notification ->
            {
                putFragment(1)
                return@OnNavigationItemSelectedListener true
            }

            R.id.nav_profile ->
            {
                putFragment(2)
                return@OnNavigationItemSelectedListener true
                
            }

        }
        false
    }

    private fun putFragment(i: Int)
    {
        supportFragmentManager.beginTransaction().apply {

            replace(R.id.flayout, arrFragment.get(i))
            addToBackStack(arrFragment.get(i).javaClass.name)
            commit()

        }
    }

    override fun onBackPressed() {

        if(nav_view.selectedItemId == R.id.nav_home)
        {
//            super.onBackPressed()
            finish()
        }
        else
        {
            nav_view.selectedItemId = R.id.nav_home
        }


    }

}