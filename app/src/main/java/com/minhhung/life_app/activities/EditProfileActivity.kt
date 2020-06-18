package com.minhhung.life_app.activities


import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.minhhung.life_app.R
import com.minhhung.life_app.models.UpdateUserParams
import com.minhhung.life_app.models.UpdateUserResponse
import com.minhhung.life_app.services.ApiService
import com.minhhung.life_app.services.implementations.UserService
import com.minhhung.life_app.utils.Constants.IntentKeys
import com.minhhung.life_app.widgets.LoadingDialog
import kotlinx.android.synthetic.main.activity_edit_profile.*
import org.jetbrains.anko.toast

class EditProfileActivity : AppCompatActivity() {

    lateinit var loadingDialog: LoadingDialog

    private var name: String? = null

    private var gender: Boolean = false

    private var address: String? = null

    private var imageUrl: String? = null

    private var phone: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        init()
    }

    private fun getIntentData() {
        name = intent.getStringExtra(IntentKeys.UserName)
        gender = intent.getBooleanExtra(IntentKeys.UserGender, false)
        address = intent.getStringExtra(IntentKeys.UserAddress)
        imageUrl = intent.getStringExtra(IntentKeys.UserImageUrl)
        phone = intent.getStringExtra(IntentKeys.UserPhoneNumber)
    }

    private fun init() {
        loadingDialog = LoadingDialog(this, R.string.loading)

        getIntentData()

        display()

        btnOk.setOnClickListener {
            doUpdate()
        }

        btnCancel.setOnClickListener {
            // Todo Show confirmation dialog
            finish()
        }
    }

    private fun display() {
        edtName.setText(name)
        radioButtonMale.isChecked = gender
        radioButtonFemale.isChecked = !gender
        edtAddress.setText(address)
        edtPhone.setText(phone)
        Glide.with(this).load(imageUrl).into(imgAvatar)
    }

    private fun updateProfile(updateUserParams: UpdateUserParams) {
        val observable = UserService.updateUser(this, updateUserParams)
        showLoading()
        ApiService.call(
            observable = observable,
            onSuccess = this@EditProfileActivity::onUpdateProfileSuccess,
            onError = this@EditProfileActivity::onUpdateProfileFailed
        )
    }

    private fun onUpdateProfileSuccess(updateUserResponse: UpdateUserResponse) {
        hideLoading()

        updateUserResponse.data.user.let {
            name = it.name
            gender = it.gender
            address = it.address
            phone = it.phone
            imageUrl = it.imageUrl
        }

        setResult(Activity.RESULT_OK)
        finish()
    }

    private fun onUpdateProfileFailed(message: String?) {
        toast(message.toString())
    }

    private fun doUpdate() {
        name = edtName.text.toString()
        address = edtAddress.text.toString()
        phone = edtPhone.text.toString()
        gender = radioButtonMale.isChecked

        val updateUserParams = UpdateUserParams(
            name = name.toString(),
            phone = phone.toString(),
            address = address.toString(),
            gender = gender
        )

        updateProfile(updateUserParams)
    }

    private fun showLoading() {
        loadingDialog.show()
    }

    private fun hideLoading() {
        loadingDialog.hide()
    }
}