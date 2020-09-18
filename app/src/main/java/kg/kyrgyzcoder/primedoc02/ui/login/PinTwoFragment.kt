package kg.kyrgyzcoder.primedoc02.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import kg.kyrgyzcoder.primedoc02.MainActivity
import kg.kyrgyzcoder.primedoc02.R
import kg.kyrgyzcoder.primedoc02.util.*
import kotlinx.android.synthetic.main.fragment_pin_two.*


class PinTwoFragment() : Fragment() {

    private var pinOne: String = ""
    private var pin: String = ""

    private var indexDot: Int = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pin_two, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = arguments
        pinOne = bundle!!.getString(EXTRA_PIN, "")

        indexDot = 1
        button1PinTwo.setOnClickListener {
            pin += "1"
            setDot()
            indexDot++
        }
        button2PinTwo.setOnClickListener {
            pin += "2"
            setDot()
            indexDot++
        }
        button3PinTwo.setOnClickListener {
            pin += "3"
            setDot()
            indexDot++
        }
        button4PinTwo.setOnClickListener {
            pin += "4"
            setDot()
            indexDot++
        }
        button5PinTwo.setOnClickListener {
            pin += "5"
            setDot()
            indexDot++
        }
        button6PinTwo.setOnClickListener {
            pin += "6"
            setDot()
            indexDot++
        }
        button7PinTwo.setOnClickListener {
            pin += "7"
            setDot()
            indexDot++
        }
        button8PinTwo.setOnClickListener {
            pin += "8"
            setDot()
            indexDot++
        }
        button9PinTwo.setOnClickListener {
            pin += "9"
            setDot()
            indexDot++
        }
        button0PinTwo.setOnClickListener {
            pin += "0"
            setDot()
            indexDot++
        }

        buttondelPinTwo.setOnClickListener {
            if (indexDot > 1) {
                pin = pin.dropLast(1)
                deleteDot()
                indexDot--
                Log.d("PinConfirmActivity", "onCreate (line 86): $indexDot pin: $pin")
            }
        }
    }

    private fun deleteDot() {
        when (indexDot) {
            1 -> {
                dot1PinTwo.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_pin_inactive
                    )
                )
            }
            2 -> {
                dot2PinTwo.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_pin_inactive
                    )
                )
            }
            3 -> {
                dot3PinTwo.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_pin_inactive
                    )
                )
            }
            else -> {
                dot4PinTwo.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_pin_inactive
                    )
                )
            }
        }
    }

    private fun setDot() {
        when (indexDot) {
            1 -> {
                dot1PinTwo.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_pin_active
                    )
                )
            }
            2 -> {
                dot2PinTwo.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_pin_active
                    )
                )
            }
            3 -> {
                dot3PinTwo.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_pin_active
                    )
                )
            }
            else -> {
                dot4PinTwo.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_pin_active
                    )
                )
            }
        }
        if (pin.length == 4) {
            Log.d("PinTwoFragment", "setDot (line 171): $pin and $pinOne")
            if (pin == pinOne) {
                savePin(pin)
                val intent = Intent(requireContext(), MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                requireActivity().finish()
                startActivity(intent)
            } else {
                errorPin()
            }
        }
    }



    private fun savePin(pin: String) {
        val sp = requireActivity().getSharedPreferences(USER_PIN, Context.MODE_PRIVATE).edit()
        sp.putString(USER_PIN_KEY, pin)
        sp.apply()

        val spp = requireActivity().getSharedPreferences(USER_STATUS, Context.MODE_PRIVATE).edit()
        spp.putString(USER_STATUS_KEY, "done")
        spp.apply()
    }

    private fun errorPin() {
        pinDoesNotMatchTextView.visibility = View.VISIBLE
        dot1PinTwo.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_pin_error
            )
        )
        dot2PinTwo.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_pin_error
            )
        )
        dot3PinTwo.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_pin_error
            )
        )
        dot4PinTwo.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_pin_error
            )
        )
    }

}