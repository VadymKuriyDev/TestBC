package com.test.atlant.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.test.atlant.databinding.MainFragmentBinding
import com.test.atlant.ui.adapter.TransactionAdapter
import com.test.atlant.viewModel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.recyclerview.widget.DividerItemDecoration
import com.test.atlant.NavigationMainController
import com.test.atlant.NavigationScope
import com.test.atlant.R
import com.test.atlant.ui.util.toBTCString
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf


class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val navigationMainController: NavigationMainController by inject{ parametersOf(activity) }
    private val mainViewModel: MainViewModel by viewModel()
    private lateinit var binding: MainFragmentBinding
    private lateinit var adapter: TransactionAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = DataBindingUtil.inflate<MainFragmentBinding>(inflater, R.layout.main_fragment, container, false).also {
        binding = it
    }.root

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
        initViewModel()
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.updateProfileData()
    }

    override fun onPause() {
        super.onPause()
        mainViewModel.unSubscribe()
    }

    private fun initView(){
        binding.startBtn.setOnClickListener {
            mainViewModel.subscribe()
        }
        binding.stopBtn.setOnClickListener {
            mainViewModel.unSubscribe()
        }
        binding.dropBtn.setOnClickListener {
            mainViewModel.drop()
        }
        binding.logoutBtn.setOnClickListener {
            mainViewModel.logout()
        }
        adapter = TransactionAdapter()
        binding.list.adapter = adapter
        binding.list.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.list.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
    }

    private fun initViewModel(){
        mainViewModel.scope.observe(this, Observer { scope ->
            if (scope == NavigationScope.LOGIN) {
                navigationMainController.navigateToLogin()
            }
        })

        mainViewModel.transactions.observe(this, Observer { transactions ->
            transactions?.let{
                adapter.updateData(it)
                binding.list.scrollToPosition(0)
            }
        })
        mainViewModel.inValue.observe(this, Observer { sum ->
            sum?.let{
                binding.inSum.text = resources
                    .getString(R.string.transaction_value_in, it.toBTCString())
            }
        })
        mainViewModel.outValue.observe(this, Observer { sum ->
            sum?.let{
                binding.outSum.text = resources
                    .getString(R.string.transaction_value_out, it.toBTCString())
            }
        })
        mainViewModel.profile.observe(this, Observer { profile ->
            profile?.let{
                binding.name.text = resources
                    .getString(R.string.user_name_value, it.firstName, it.lastName)
                binding.userType.text = resources
                    .getString(R.string.user_type_value, it.profileType)
            }
        })
    }

}
