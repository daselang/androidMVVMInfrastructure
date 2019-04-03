package com.demo.mvvm.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.demo.mvvm.R
import com.demo.mvvm.models.AccountUser
import kotlinx.android.synthetic.main.view_account_login.view.*


@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_MATCH_HEIGHT)
class AccountLogInView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    FrameLayout(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.view_account_login, this)

    }

    @ModelProp
    fun setAccountUser(user: AccountUser?) {
        mUserName.setText(user?.userName, TextView.BufferType.EDITABLE)
        mPassword.setText(user?.userPwd, TextView.BufferType.EDITABLE)
    }

    @CallbackProp
    fun onLoginClicked(listener: View.OnClickListener?) {
        mAccountUserLoginBtn.setOnClickListener(listener)
    }

    fun getUIData(): AccountUser {
        val userName = mUserName.text.trim().toString()
        val password = mPassword.text.trim().toString()
        return AccountUser("", userName, password)
    }

}