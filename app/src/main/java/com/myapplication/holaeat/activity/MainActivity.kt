package com.myapplication.holaeat.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.myapplication.holaeat.*
import com.myapplication.holaeat.fragment.*

class MainActivity : AppCompatActivity() {

    lateinit var drawerLayout : DrawerLayout
    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    lateinit var frameLayout: FrameLayout
    lateinit var navigationView: NavigationView
    lateinit var sharedPreferences: SharedPreferences

    var previousMenuItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.drawerLayout)
        coordinatorLayout = findViewById(R.id.coordinatorLayout)
        toolbar = findViewById(R.id.toolbar)
        frameLayout = findViewById(R.id.frame)
        navigationView = findViewById(R.id.navigationView)

        sharedPreferences=getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)
        setUpToolbar()

        openHome()

        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this@MainActivity,
            drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )


        drawerLayout.addDrawerListener(actionBarDrawerToggle)   //making work hamburger icon
        actionBarDrawerToggle.syncState()


        navigationView.setNavigationItemSelectedListener {
            // it will give currently selected item

            if (previousMenuItem!=null){
                previousMenuItem?.isChecked = false
            }

            it.isCheckable = true
            it.isChecked = true
            previousMenuItem = it

            when(it.itemId){
                R.id.home ->{

                    openHome()
                    drawerLayout.closeDrawers()
                }
                R.id.profile ->{
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frame,
                            ProfileFragment()
                        )                                                                                                       // dashboard fragment is replacing the blank frame
                        .commit()

                    supportActionBar?.title = "My Profile"                                                      //giving the title
                    drawerLayout.closeDrawers()
                }

                R.id.favourites ->{
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frame,
                            FavouriteFragment()
                        )                                                                                        // dashboard fragment is replacing the blank frame
                        .commit()

                    supportActionBar?.title = "Favorite Restaurants"                                                     //giving the title
                    drawerLayout.closeDrawers()
                }

                R.id.orderhistory -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frame,
                            OrderhistoryFragment()
                        )                                                                                     // dashboard fragment is replacing the blank frame
                        .commit()

                    supportActionBar?.title = "My Previous Orders"
                    drawerLayout.closeDrawers()

                }

                R.id.faqs ->{
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frame,
                            FaqsFragment()
                        )                                                                          // dashboard fragment is replacing the blank frame
                        .commit()

                    supportActionBar?.title = "Frequently Asked Qusetions"
                    drawerLayout.closeDrawers()
                }
                R.id.Logout ->{

                    val dialog= AlertDialog.Builder(this)
                    dialog.setTitle("Confirmation")
                    dialog.setMessage("Sure want to Logout")
                    dialog.setPositiveButton("Yes"){text,listener->
                        val intent= Intent(this@MainActivity,LoginActivity::class.java)
                        sharedPreferences.edit().putBoolean("isLoggedIn",false).apply()
                        startActivity(intent)
                    }
                    dialog.setNegativeButton("Cancel"){text,listener->
                        openHome()
                        drawerLayout.closeDrawers()

                    }
                    dialog.create()
                    dialog.show()
                }




            }

            return@setNavigationItemSelectedListener true
        }

    }
        fun setUpToolbar(){
            setSupportActionBar(toolbar)
            supportActionBar?.title = "Toolbar title"
            supportActionBar?.setHomeButtonEnabled(true)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

    // for the clicking on hamburger and opening of drawer from the left side
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId

        if(id==android.R.id.home)
        {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        return super.onOptionsItemSelected(item)
    }

    fun openHome(){
        val fragment = HomeFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame, fragment)   // dashboard fragment is replacing the blank frame
        transaction.commit()
        supportActionBar?.title = "All Restaurants"    //giving the title to each fragment
        navigationView.setCheckedItem(R.id.home)
    }

    override fun onBackPressed() {
        val frag = supportFragmentManager.findFragmentById(R.id.frame)

        when(frag){
            !is HomeFragment -> openHome()    // if at any frame ie other than dashboard this will bring back to dasboard on pressing back key

            else -> super.onBackPressed()     // Exit the app (default)
        }
    }

}