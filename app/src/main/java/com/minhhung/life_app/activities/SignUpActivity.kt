package com.minhhung.life_app.activities


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.minhhung.life_app.R
import com.minhhung.life_app.models.SignUpParams
import com.minhhung.life_app.models.SignUpResponse
import com.minhhung.life_app.services.ApiService
import com.minhhung.life_app.services.implementations.AuthService
import com.minhhung.life_app.utils.isNotNullOrEmpty
import com.minhhung.life_app.widgets.LoadingDialog
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.activity_sign_up.edtEmail
import kotlinx.android.synthetic.main.activity_sign_up.edtPassword
import kotlinx.android.synthetic.main.activity_sign_up.tilEmail
import org.jetbrains.anko.toast

class SignUpActivity : AppCompatActivity() {

    private var name: String? = null

    private var email: String? = null

    private var password: String? = null

    private var confirmPassword: String? = null

    lateinit var loadingDialog: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        init()
    }

    private fun init() {
        loadingDialog = LoadingDialog(this, R.string.sign_up_message)

        btnSignUp.setOnClickListener() {
            doSignUp()
        }

        edtName.onTextChanged {
            name = it

            showError(null, clearPassword = false)

            updateViews()
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

        edtPasswordConfirmation.onTextChanged {
            confirmPassword = it

            updateViews()
        }

        tvLogin.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
        }
    }

    private fun doSignUp() {
        doIfValid { name, email, password, confirmPassword ->
            val signUpParams = SignUpParams(
                name = name,
                email = email,
                password = password,
                confirmPassword = confirmPassword
            )

            loadingDialog.show()

            val observable = AuthService.signUp(signUpParams)

            ApiService.call(
                observable = observable,
                onSuccess = this@SignUpActivity::onSignUpSuccess,
                onError = this@SignUpActivity::onSignUpFailed
            )
        }
    }

    private fun onSignUpSuccess(signUpResponse: SignUpResponse) {
        loadingDialog.dismiss()

        toast(R.string.created_account_success_message)
        startActivity(Intent(this, SignInActivity::class.java))
        finish()
    }

    private fun onSignUpFailed(message: String?) {
        loadingDialog.dismiss()

        showError(message)
    }

    private fun showError(message: String?, clearPassword: Boolean = true) {
        tilName.error = message
        tilEmail.error = message

        if (clearPassword) {
            edtPassword.setText("")
            edtPasswordConfirmation.setText("")
        }
    }

    private fun doIfValid(action: (String, String, String, String) -> Unit) {
        name?.let { name ->
            email?.let { email ->
                password?.let { password ->
                    confirmPassword?.let { confirmPassword ->
                        action.invoke(name, email, password, confirmPassword)
                    }
                }
            }
        }
    }

    private fun updateViews() {
        btnSignUp.isEnabled = name.isNotNullOrEmpty() && email.isNotNullOrEmpty() &&
                password.isNotNullOrEmpty() && confirmPassword.isNotNullOrEmpty() && (password == confirmPassword)
    }
}
