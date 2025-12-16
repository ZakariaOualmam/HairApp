


package com.barbershop.app.ui.ai

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.barbershop.app.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AIFeaturesFragment : Fragment(R.layout.fragment_ai_features) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inline header title + Return button
        view.findViewById<TextView>(R.id.tvTitle)?.text = "AI Studio"
        view.findViewById<View>(R.id.btnReturnHome)?.setOnClickListener {
            findNavController().navigate(R.id.action_global_homeFragment)
        }

        // Setup click listeners for AI feature cards
        view.findViewById<View>(R.id.cardSimulator)?.setOnClickListener {
            Toast.makeText(context, "Opening Haircut Simulator...", Toast.LENGTH_SHORT).show()
        }

        view.findViewById<View>(R.id.cardFaceAnalysis)?.setOnClickListener {
            Toast.makeText(context, "Analyzing Face Shape...", Toast.LENGTH_SHORT).show()
        }

        view.findViewById<View>(R.id.cardRecommendations)?.setOnClickListener {
            Toast.makeText(context, "Generating Style Recommendations...", Toast.LENGTH_SHORT).show()
        }
    }
}
