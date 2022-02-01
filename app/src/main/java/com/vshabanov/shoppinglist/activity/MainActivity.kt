package com.vshabanov.shoppinglist.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.vshabanov.shoppinglist.R
import com.vshabanov.shoppinglist.data_classes.DataBaseHelper
import com.vshabanov.shoppinglist.data_classes.User
import com.vshabanov.shoppinglist.databinding.ActivityMainBinding
import com.vshabanov.shoppinglist.databinding.FragmentUserInfoBinding
import com.vshabanov.shoppinglist.ui.userinfo.UserInfoFragment

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    lateinit var auth: FirebaseAuth

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
//        if (auth.currentUser != null)
//            Firebase.database.setPersistenceEnabled(true)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_contacts, R.id.notificationsFragment,
                R.id.friendRequestFragment, R.id.logOutFragment
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        val headerView: View = navView.getHeaderView(0)
        if ((auth.currentUser?.isAnonymous == true)|| (auth.currentUser?.uid == null)) {
            headerView.findViewById<LinearLayout>(R.id.haveUser).visibility = View.GONE
            headerView.findViewById<LinearLayout>(R.id.noHaveUser).visibility = View.VISIBLE
        } else {
            headerView.findViewById<LinearLayout>(R.id.haveUser).visibility = View.VISIBLE
            headerView.findViewById<LinearLayout>(R.id.noHaveUser).visibility = View.GONE
        }
        if (headerView.findViewById<LinearLayout>(R.id.noHaveUser).isVisible)
            headerView.findViewById<Button>(R.id.authorization).setOnClickListener {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        DataBaseHelper().getCurrentUser(object : DataBaseHelper.CurrentUserData {
            override fun dataIsLoaded(userData: User) {
                if (headerView.findViewById<LinearLayout>(R.id.haveUser).isVisible) {
                    headerView.findViewById<TextView>(R.id.userPhone).text = userData.phone
                    headerView.findViewById<TextView>(R.id.userName).text = userData.name
                }
            }
        })
    }

    override fun onStart() {
        val currentUser = auth.currentUser
        updateUI(currentUser)
        super.onStart()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
    private fun updateUI(user: FirebaseUser?) {
        if (user == null) {
            signInAnonymously()
        }
    }

    private fun signInAnonymously() {
        auth.signInAnonymously()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.personal -> if ((auth.currentUser?.isAnonymous == true)|| (auth.currentUser == null)) {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "НЕОБХОДИМО",
                    Snackbar.LENGTH_LONG
                )
                    .setTextColor(0XFF81C784.toInt())
                    .setAction("авторизоваться", View.OnClickListener {
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    })
                    .show()
            } else {
                findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.userInfoFragment)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}