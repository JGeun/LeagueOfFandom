package com.soma.lof.login.ui

import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.getSystemService
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.soma.common.ui.R.string.nick_text_cnt
import com.soma.common.ui.base.BaseFragment
import com.soma.lof.login.R
import com.soma.lof.login.databinding.FragmentNickFirstSetBinding
import com.soma.lof.login.util.LoginUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class SetFirstNickFragment :
    BaseFragment<FragmentNickFirstSetBinding>(R.layout.fragment_nick_first_set), SetFirstNickFragmentListener {

    @Inject
    @Named("SelectTeam")
    lateinit var selectTeamActivityClass: Class<*>

    private val viewModel by viewModels<SetNickNameViewModel>()

    override fun initView() {

        bind {
            listener = this@SetFirstNickFragment
        }

        // check user's input char cnt
        binding.nickInputField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0 != null) {
                    binding.nicknameCompleteBtn.isEnabled = p0.isNotEmpty()
                    binding.nickInputTextCnt.text = getString(nick_text_cnt, p0.length, 16)
                }
            }
        })

        // Hide keyboard when user pressed Enter key
        binding.nickInputField.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    val imm = requireContext().getSystemService() as InputMethodManager?
                    imm?.hideSoftInputFromWindow(binding.nickInputField.windowToken, 0)
                    return true
                }
                return false
            }
        })

        lifecycleScope.launchWhenStarted {
            viewModel.nickNameSetSuccess.collectLatest { isSuccess ->
                if (isSuccess) navigateToSelectTeam()
            }
        }
    }

    private fun navigateToSelectTeam() {
        LoginUtil.startSelectTeamActivity(requireActivity(), selectTeamActivityClass)
    }

    /** [SetFirstNickFragmentListener] */
    override fun checkInputNickname() {
        val userInputNickname = binding.nickInputField.text.toString()
        if (validationNickName(userInputNickname)) {
            viewModel.setNickName(binding.nickInputField.text.toString())
        }
    }

    // Lof Nickname Rule ( 3 ~ 16 Length, Only English and Number)
    private fun validationNickName(nickName: String): Boolean {
        if (nickName.length < 3) {
            Toast.makeText(requireContext(), "3?????? ????????? ??????????????? ?????????.", Toast.LENGTH_SHORT).show()
            return false
        }
        val regex = "([0-9a-zA-Z])*".toRegex()
        val matchResult: MatchResult? = regex.matchEntire(nickName)
        if (matchResult == null) {
            Toast.makeText(requireContext(), "????????? ????????? ??????????????? ?????????.", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }
}

interface SetFirstNickFragmentListener {
    fun checkInputNickname()
}