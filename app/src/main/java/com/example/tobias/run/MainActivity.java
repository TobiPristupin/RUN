/**
 * RUN. application let's users track their running by adding them manually
 * with a high level of customization and visualize them in varied ways,
 * such as in various graphs and ordered by several criteria.
 *
 *@author Tobias Pristupin
 *@version 1.0
 *@since 09/01/2017
 */

package com.example.tobias.run;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


/**
 * MainAcitvity of application. Displays a TabLayout for History and Stats fragments and
 * a ViewPager to switch between them.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Deletes black shadow between toolbar and TabLayout for visual purposes.
        getSupportActionBar().setElevation(0);



        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager()));
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        //Unable to add icons in xml, adding them programatically as a workaround
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_format_list_bulleted_white_36dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_insert_chart_white_36dp);
    }
}
