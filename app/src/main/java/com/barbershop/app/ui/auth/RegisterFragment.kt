package com.barbershop.app.ui.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.barbershop.app.R
import com.barbershop.app.databinding.FragmentRegisterBinding
import com.barbershop.app.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment : Fragment(R.layout.fragment_register) {

    private val viewModel: AuthViewModel by viewModels()
    private lateinit var binding: FragmentRegisterBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRegisterBinding.bind(view)

        binding.btnRegister.setOnClickListener {
            val name = binding.etName.text.toString()
            val email = binding.etEmail.text.toString()
            val pass = binding.etPassword.text.toString()
            if (name.isNotEmpty() && email.isNotEmpty() && pass.isNotEmpty()) {
                viewModel.register(name, email, pass)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.authState.collectLatest { state ->
                when (state) {
                    is Resource.Loading -> binding.progressBar.visibility = View.VISIBLE
                    is Resource.Success -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(requireContext(), "Account Created", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_registerFragment_to_homeFragment)
                    }
                    is Resource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                    }
                    null -> Unit
                }
            }
        }
    }
}
