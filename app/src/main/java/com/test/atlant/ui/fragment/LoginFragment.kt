package com.test.atlant.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.test.atlant.NavigationLoginController
import com.test.atlant.NavigationMainController
import com.test.atlant.NavigationScope
import com.test.atlant.R
import com.test.atlant.databinding.LoginFragmentBinding
import com.test.atlant.viewModel.LoginViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class LoginFragment : Fragment() {

    companion object {
        fun newInstance() = LoginFragment()
    }

    private val navigationLoginController: NavigationLoginController by inject{ parametersOf(activity) }
    private val loginViewModel: LoginViewModel by viewModel()
    private lateinit var binding: LoginFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = DataBindingUtil.inflate<LoginFragmentBinding>(inflater, R.layout.login_fragment, container, false).also {
        binding = it
    }.root

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
        initViewModel()
    }

    private fun initView(){
//        binding.email.setText("hello@karta.com")
//        binding.password.setText("12345678")
        binding.email.doOnTextChanged { text, _, _, _ ->
            loginViewModel.changeLoginState(text.toString(), binding.password.text.toString())
        }
        binding.password.doOnTextChanged { text, _, _, _ ->
            loginViewModel.changeLoginState(binding.email.text.toString(), text.toString())
        }
        binding.loginBtn.setOnClickListener {
            loginViewModel.login(binding.email.text.toString(), binding.password.text.toString())
        }
    }

    private fun initViewModel(){
        loginViewModel.loginEnable.observe(this, Observer {
            binding.loginBtn.isEnabled = it
        })
        loginViewModel.scope.observe(this, Observer { scope ->
            if (scope == NavigationScope.MAIN) {
                navigationLoginController.navigateToMain()
            }
        })
    }
}
