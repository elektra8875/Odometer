package com.example.cindylou.workout;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity
    implements WorkoutListFragment.WorkoutListListener {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


    @Override
    public void itemClicked(long id) {
        View fragmentContainer =findViewById(R.id.fragment_container);
        if (fragmentContainer != null){
        WorkoutDetailFragment details = new WorkoutDetailFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        details.setWorkout(id);
        ft.replace(R.id.fragment_container, details);
        ft.addToBackStack(null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();

    } else{
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(DetailActivity.EXTRA_WORKOUT_ID, (int) id);
            startActivity(intent);
        }
        }

}
