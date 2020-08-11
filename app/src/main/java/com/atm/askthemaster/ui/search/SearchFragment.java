package com.atm.askthemaster.ui.search;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.atm.askthemaster.R;
import com.atm.askthemaster.ui.question.QuestionFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    private ExpandableListAdapter listAdapter;
    private ExpandableListView expListView;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;
    private TextView selectCategory;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.list_view_main, null);
        expListView = (ExpandableListView) rootView.findViewById(R.id.expListView);
        prepareListData();
        listAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                Bundle bundle = new Bundle();
                bundle.putString("key", String.valueOf(listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition)));
                QuestionFragment fragment = new QuestionFragment();
                fragment.setArguments(bundle);
                selectCategory = (TextView) rootView.findViewById(R.id.selectCategory);
                selectCategory.setVisibility(View.GONE);
                getFragmentManager().beginTransaction().replace(R.id.LinearLayout2, fragment).commit();
                SearchFragment.this.getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.LinearLayout2, fragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack(null).commit();
                //Toast.makeText(getActivity().getApplicationContext(),listDataHeader.get(groupPosition) + " : " + listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        return rootView;
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        listDataHeader.add("Amateur");
        listDataHeader.add("Business");
        listDataHeader.add("Career");
        listDataHeader.add("Fashion");
        listDataHeader.add("Fitness");
        listDataHeader.add("Food");
        listDataHeader.add("Health");
        listDataHeader.add("Housing");
        listDataHeader.add("Law");
        listDataHeader.add("Motherhood");
        listDataHeader.add("Travel");

        List<String> amateur = new ArrayList<>();
        amateur.add("Fishing");
        amateur.add("Pet");
        amateur.add("Photography");
        amateur.add("Pottery");
        amateur.add("Other");

        List<String> business = new ArrayList<>();
        business.add("Entrepreneurship");
        business.add("Investment");
        business.add("Other");

        List<String> career = new ArrayList<>();
        career.add("Interview");
        career.add("Job position");
        career.add("Resume");
        career.add("Other");

        List<String> fashion = new ArrayList<>();
        fashion.add("Beauty");
        fashion.add("Fashion look");
        fashion.add("Hair style");
        fashion.add("Other");

        List<String> fitness = new ArrayList<>();
        fitness.add("Diet");
        fitness.add("Outdoor");
        fitness.add("Personal training");
        fitness.add("Equipment");
        fitness.add("Other");

        List<String> food = new ArrayList<>();
        food.add("Baking");
        food.add("Grilling");
        food.add("Wining");
        food.add("Kitchenware");
        food.add("Other");

        List<String> health = new ArrayList<>();
        health.add("First-aid");
        health.add("Mental health");
        health.add("Meditation");
        health.add("OTC");
        health.add("Physio therapy");
        health.add("Other");

        List<String> housing = new ArrayList<>();
        housing.add("Gardening");
        housing.add("Hardware");
        housing.add("House design");
        housing.add("Plumbing");
        housing.add("Other");

        List<String> law = new ArrayList<>();
        law.add("Civil law");
        law.add("Family law");
        law.add("Immigration");
        law.add("Traffic law");
        law.add("Tax");
        law.add("Other");

        List<String> motherhood = new ArrayList<>();
        motherhood.add("Nursing");
        motherhood.add("Baby sleep");
        motherhood.add("Nutrition");
        motherhood.add("Baby health");
        motherhood.add("Other");

        List<String> travel = new ArrayList<>();
        travel.add("Local practices");
        travel.add("Restaurants");
        travel.add("Ticketing");
        travel.add("Visa");
        travel.add("Other");


        listDataChild.put(listDataHeader.get(0), amateur);
        listDataChild.put(listDataHeader.get(1), business);
        listDataChild.put(listDataHeader.get(2), career);
        listDataChild.put(listDataHeader.get(3), fashion);
        listDataChild.put(listDataHeader.get(4), fitness);
        listDataChild.put(listDataHeader.get(5), food);
        listDataChild.put(listDataHeader.get(6), health);
        listDataChild.put(listDataHeader.get(7), housing);
        listDataChild.put(listDataHeader.get(8), law);
        listDataChild.put(listDataHeader.get(9), motherhood);
        listDataChild.put(listDataHeader.get(10), travel);
    }

}