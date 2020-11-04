package com.example.e_seva;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class tabpagerAdapter extends FragmentStatePagerAdapter {


    String[] tabarray = new String[]{"Home","Schemes"};

    Integer tabnumber = 2;
    private FragmentManager fm;

    public tabpagerAdapter(FragmentManager fm) {

        super(fm);

    }




    @Override

    public CharSequence getPageTitle(int position) {

        return tabarray[position];

    }



    @Override

    public Fragment getItem(int position) {



        switch (position)

        {

            case 0:


            return new home_screen ();

            case 1:



                return new Schemes ();



        }





        return null;

    }



    @Override

    public int getCount() {

        return tabnumber;

    }

}


