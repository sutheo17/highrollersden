package hu.bme.aut.android.highrollersden.player

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ProfilePageAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

  override fun getItemCount(): Int = NUM_PAGES  

  override fun createFragment(position: Int): Fragment = when(position){  
      0 -> main
      1 -> details
      else -> MainProfileFragment()  
  }


  companion object{  
      const val NUM_PAGES = 2
      var main = MainProfileFragment()
      var details = DetailsProfileFragment()
  }  
}