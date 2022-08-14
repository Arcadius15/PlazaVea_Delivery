package edu.pe.idat.plazaveadelivery.views.fragments.asignados

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import edu.pe.idat.plazaveadelivery.databinding.FragmentAsignadosBinding

class AsignadosFragment : Fragment() {

    private var _binding: FragmentAsignadosBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAsignadosBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}