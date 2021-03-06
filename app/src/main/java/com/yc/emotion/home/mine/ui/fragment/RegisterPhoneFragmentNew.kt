package com.yc.emotion.home.mine.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import com.music.player.lib.util.ToastUtils
import com.yc.emotion.home.R
import com.yc.emotion.home.constant.Constant
import com.yc.emotion.home.utils.RegexUtils
import kotlinx.android.synthetic.main.fragment_register_phone.*

/**
 *
 * Created by suns  on 2019/10/19 10:27.
 */
class RegisterPhoneFragmentNew : BaseRegisterPhoneFragment() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_register_phone
    }


    companion object {
        fun newInstance(): RegisterPhoneFragmentNew {
            val registerPhoneFragment = RegisterPhoneFragmentNew()
            registerPhoneFragment.fragmentTag = Constant.REGISTER_PHONE

            return registerPhoneFragment
        }
    }


    override fun initViews() {
        initListener()
    }

    override fun lazyLoad() {

    }


    private fun initListener() {
        tv_get_code.setOnClickListener {
            val phone = et_input_phone.text.toString().trim()
            if (TextUtils.isEmpty(phone)) {
                ToastUtils.showCenterToast("请输入手机号")
                return@setOnClickListener
            }
            if (!RegexUtils.isMobileSimple(phone)) {
                ToastUtils.showCenterToast("手机号输入有误")
                return@setOnClickListener
            }
            val bundle = Bundle()
            bundle.putString("phone", phone)

            Navigation.findNavController(it).navigate(R.id.action_to_registerCode, bundle)

//            mMainActivity?.switchFragment(RegisterCodeFragment.newInstance(phone), fragmentTag)

        }
        et_input_phone.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val result = s.toString().trim()
                activity?.let {
                    if (!TextUtils.isEmpty(result)) {
                        et_input_phone.setTextColor(ContextCompat.getColor(it, R.color.gray_222222))
                    } else {
                        et_input_phone.setTextColor(ContextCompat.getColor(it, R.color.gray_999))
                    }
                }

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })

    }


}