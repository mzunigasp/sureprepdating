package com.sureprep.dating.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sureprep.dating.R
import com.sureprep.dating.activities.DatingCallback

class MatchesFragment : Fragment() {

    private var callback: DatingCallback? = null


    fun setCallback(callback: DatingCallback) {
        this.callback = callback
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_matches, container, false)
    }
}