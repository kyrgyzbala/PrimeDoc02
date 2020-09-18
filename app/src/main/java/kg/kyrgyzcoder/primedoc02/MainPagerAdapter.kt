package kg.kyrgyzcoder.primedoc02

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import kg.kyrgyzcoder.primedoc02.ui.chat.ChatMainFragment
import kg.kyrgyzcoder.primedoc02.ui.medCard.MedCardMainFragment
import kg.kyrgyzcoder.primedoc02.ui.timetable.TimeTableMainFragment

class MainPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {

        return when (position) {
            0 -> {
                TimeTableMainFragment()
            }
            1 -> {
                MedCardMainFragment()
            }
            else -> {
                ChatMainFragment()
            }
        }
    }

}