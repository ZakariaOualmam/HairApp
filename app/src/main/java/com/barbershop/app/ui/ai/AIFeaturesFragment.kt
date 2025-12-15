package com.barbershop.app.ui.ai

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.barbershop.app.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AIFeaturesFragment : Fragment(R.layout.fragment_ai_features) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Setup Card Texts manually since include IDs are tricky with binding in simple fragments
        // Simulator Card
        view.findViewById<View>(R.id.cardSimulator)?.apply {
            findViewById<TextView>(R.id.tvTitle).text = "Simulator"
            findViewById<TextView>(R.id.tvDescription).text = "Try haircuts on your photo."
            setOnClickListener {
                android.widget.Toast.makeText(context, "Opening Haircut Simulator...", android.widget.Toast.LENGTH_SHORT).show()
            }
        }

        // Face Analysis Card
        view.findViewById<View>(R.id.cardFaceAnalysis)?.apply {
            findViewById<TextView>(R.id.tvTitle).text = "Analysis"
            findViewById<TextView>(R.id.tvDescription).text = "Find your face shape."
            setOnClickListener {
                android.widget.Toast.makeText(context, "Analyzing Face Shape...", android.widget.Toast.LENGTH_SHORT).show()
            }
        }

        // Recommendations Card
        view.findViewById<View>(R.id.cardRecommendations)?.apply {
            findViewById<TextView>(R.id.tvTitle).text = "Style Guide"
            findViewById<TextView>(R.id.tvDescription).text = "Get personalized style advice."
            setOnClickListener {
                android.widget.Toast.makeText(context, "Generating Recommendations...", android.widget.Toast.LENGTH_SHORT).show()
            }
        }
    }
}
