package com.minhhung.life_app.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.minhhung.life_app.R
import com.minhhung.life_app.models.GetUserResponse
import com.minhhung.life_app.models.User
import com.minhhung.life_app.services.ApiService
import com.minhhung.life_app.services.implementations.UserService
import com.minhhung.life_app.utils.hide
import com.minhhung.life_app.utils.show
import kotlinx.android.synthetic.main.activity_profile.*
import org.jetbrains.anko.toast
import com.minhhung.life_app.utils.Constants.IntentKeys

class ProfileActivity : AppCompatActivity() {

    private var user: User? = null
    private val defaultImageUrl = "https://scontent.fdad2-1.fna.fbcdn.net/v/t1.0-9/98181319_1631684980311879_2873483745527070720_n.jpg?_nc_cat=101&_nc_sid=8024bb&_nc_ohc=uEhZBjdZSG0AX-L7Af-&_nc_ht=scontent.fdad2-1.fna&oh=3cfdb4ced50cf76fd1ce08f4ec632738&oe=5EF160D7"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        init()
    }

    private fun getUserProfile() {
        val observable = UserService.getUser(this)
        showLoading()
        ApiService.call(
            observable = observable,
            onSuccess = this@ProfileActivity::onGetUserSuccess,
            onError = this@ProfileActivity::onGetUserFailed
        )
    }

    private fun setUpActionBar() {
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()

        return true
    }

    private fun init() {
        getUserProfile()

        setUpActionBar()
    }

    private fun onGetUserSuccess(getUserResponse: GetUserResponse) {
        hideLoading()
        user = getUserResponse.data.user
        display(getUserResponse.data.user)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.user_profile_options_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_edit_profile -> {
                user?.apply {
                    val intent =
                        Intent(this@ProfileActivity, EditProfileActivity::class.java).apply {
                            putExtra(IntentKeys.UserName, name)
                            putExtra(IntentKeys.UserGender, gender)
                            putExtra(IntentKeys.UserPhoneNumber, phone)
                            putExtra(IntentKeys.UserImageUrl, imageUrl)
                            putExtra(IntentKeys.UserAddress, address)
                        }
                    startActivityForResult(intent, IntentKeys.UpdateUserFlag)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun display(user: User) {
        tvName.text = user.name
        tvGender.text = getString(if (user.gender) R.string.male else R.string.female)
        tvPhone.text = user.phone
        tvAddress.text = user.address
        Glide.with(this).load(user.imageUrl ?: defaultImageUrl).into(imgAvatar)
    }

    private fun onGetUserFailed(message: String?) {
        hideLoading()
        toast(getString(R.string.load_user_profile_failed_message))
    }

    private fun showLoading() {
        content.hide()
        loading.show()
    }

    private fun hideLoading() {
        content.show()
        loading.hide()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == IntentKeys.UpdateUserFlag && resultCode == Activity.RESULT_OK) {
            getUserProfile()
        }
    }

}