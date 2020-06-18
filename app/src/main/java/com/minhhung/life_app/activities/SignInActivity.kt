package com.minhhung.life_app.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.minhhung.life_app.R
import com.minhhung.life_app.models.SignInParams
import com.minhhung.life_app.models.SignInResponse
import com.minhhung.life_app.services.ApiService
import com.minhhung.life_app.services.implementations.AuthService
import com.minhhung.life_app.utils.isNotNullOrEmpty
import com.minhhung.life_app.utils.saveAuthInformation
import com.minhhung.life_app.widgets.LoadingDialog
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.android.synthetic.main.activity_sign_in.edtEmail
import kotlinx.android.synthetic.main.activity_sign_in.edtPassword
import kotlinx.android.synthetic.main.activity_sign_in.tilEmail
import kotlinx.android.synthetic.main.activity_sign_up.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class SignInActivity : AppCompatActivity() {

    private var email: String? = null

    private var password: String? = null

    lateinit var loadingDialog: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        init()
    }

    private fun init() {
        loadingDialog = LoadingDialog(this, R.string.signing_in_message)

        btnSignIn.setOnClickListener {
            doSignIn()
        }

        edtEmail.onTextChanged {
            email = it

            showError(null, clearPassword = false)

            updateViews()
        }

        edtPassword.onTextChanged {
            password = it

            updateViews()
        }

        tvCreateAccount.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }

    private fun doSignIn() {
        doIfValid { email, password ->
            val signInParams = SignInParams(
                email = email,
                password = password
            )

            loadingDialog.show()

            val observable = AuthService.signIn(signInParams)

            ApiService.call(
                observable = observable,
                onSuccess = this@SignInActivity::onSignInSuccess,
                onError = this@SignInActivity::onSignInFailed
            )
        }
    }

    private fun onSignInSuccess(signInResponse: SignInResponse) {
        loadingDialog.dismiss()

        toast(getString(R.string.sign_in_success_message))
        saveAuthInformation(signInResponse)
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun onSignInFailed(message: String?) {
        loadingDialog.dismiss()

        showError(message)
    }

    private fun showError(message: String?, clearPassword: Boolean = true) {
        tilEmail.error = message

        if (clearPassword) {
            edtPassword.setText("")
        }
    }

    private fun doIfValid(action: (String, String) -> Unit) {
        email?.let { email ->
            password?.let { password ->
                action.invoke(email, password)
            }
        }
    }

    private fun updateViews() {
        btnSignIn.isEnabled = email.isNotNullOrEmpty() && password.isNotNullOrEmpty()
    }

}
