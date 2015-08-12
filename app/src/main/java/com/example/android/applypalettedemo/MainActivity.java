/*
 * Copyright 2015 Udacity, Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.applypalettedemo;

import android.animation.Animator;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionManager;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainActivity extends Activity {
    static String baconTitle = "Bacon";
    static String baconText = "Bacon ipsum dolor amet pork belly meatball kevin spare ribs. Frankfurter swine corned beef meatloaf, strip steak.";
    static String veggieTitle = "Veggie";
    static String veggieText = "Veggies es bonus vobis, proinde vos postulo essum magis kohlrabi welsh onion daikon amaranth tatsoi tomatillo melon azuki bean garlic.";

    /**
     * This SparseBooleanArray will track whether or not views are in Bacon state
     * or not. An index will return false if it is in Bacon state and true
     * if it is in Veggie state.
     */
    public SparseBooleanArray mSelections = new SparseBooleanArray(10);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout)).setTitle(getString(R.string.app_name));
        RecyclerView rv = (RecyclerView) findViewById(R.id.recyclerview);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new RecyclerView.Adapter<ViewHolder>() {
            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int position) {
                return new ViewHolder(getLayoutInflater().inflate(R.layout.list_item, parent, false));
            }

            @Override
            public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
                viewHolder.text1.setText(baconTitle);
                viewHolder.text2.setText(baconText);

                // If position is not in "vegetative" state, make green, set proper text
                if (mSelections.get(position)) {
                    viewHolder.text1.setText(veggieTitle);
                    viewHolder.text2.setText(veggieText);
                    viewHolder.rootView.setBackgroundColor(getResources().getColor(R.color.green));
                } else {
                    viewHolder.text1.setText(baconTitle);
                    viewHolder.text2.setText(baconText);
                    viewHolder.rootView
                            .setBackgroundColor(getResources()
                                    .getColor(R.color.background_material_light));
                }

                viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isVeggie
                                 = ((ColorDrawable)v.getBackground()) != null
                                && ((ColorDrawable)v.getBackground()).getColor() == getResources().getColor(R.color.green);

                        TransitionManager.beginDelayedTransition((ViewGroup) v);
                        int finalRadius = Math.max(v.getWidth(), v.getHeight()) / 2;

                        if (isVeggie) {
                            mSelections.put(position, false);
                            viewHolder.text1.setText(baconTitle);
                            viewHolder.text2.setText(baconText);
                            viewHolder.rootView.setBackgroundColor(getResources()
                                    .getColor(R.color.background_material_light));
                        } else {
                            mSelections.put(position, true);
                            Animator anim =
                                    ViewAnimationUtils
                                            .createCircularReveal(
                                                    v,
                                                    (int) v.getWidth()/2,
                                                    (int) v.getHeight()/2,
                                                    0,
                                                    finalRadius);
                            viewHolder.text1.setText(veggieTitle);
                            viewHolder.text2.setText(veggieText);
                            viewHolder.rootView.setBackgroundColor(getResources().getColor(R.color.green));
                            anim.start();
                        }
                    }
                });
            }

            @Override
            public int getItemCount() {
                return 10;
            }
        });
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {
        public View rootView;
        public TextView text1;
        public TextView text2;
        static int green;
        static int white;

        public ViewHolder(View itemView) {
            super(itemView);

            rootView = itemView.findViewById(R.id.rootView);
            text1 = (TextView) itemView.findViewById(android.R.id.text1);
            text2 = (TextView) itemView.findViewById(android.R.id.text2);

            if (green == 0)
                green = itemView.getContext().getResources().getColor(R.color.green);
            if (white == 0)
                white = itemView.getContext().getResources().getColor(R.color.background_material_light);
        }
    }
}
