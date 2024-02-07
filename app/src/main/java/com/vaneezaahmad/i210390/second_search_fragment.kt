package com.vaneezaahmad.i210390

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.jaredrummler.materialspinner.MaterialSpinner

class second_search_fragment : Fragment(R.layout.second_fragment_search) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val back = view.findViewById<ImageView>(R.id.arrow)

        back.setOnClickListener {
            // Replace the current fragment with the new fragment
            val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_container_view, Search_fragment())
            fragmentTransaction.addToBackStack(null) // Optional: Add transaction to back stack
            fragmentTransaction.commit()
        }

        val spinner = view.findViewById<View>(R.id.filter) as MaterialSpinner
        spinner.setItems("Filter","Price: Low to High", "Price: High to Low", "Rating: Low to High", "Rating: High to Low")
        spinner.setOnItemSelectedListener { view, position, id, item ->
            Snackbar.make(
                view,
                "Selected $item",
                Snackbar.LENGTH_LONG
            ).show()
        }

    }
}