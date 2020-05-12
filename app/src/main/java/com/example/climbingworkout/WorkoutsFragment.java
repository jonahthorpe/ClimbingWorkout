package com.example.climbingworkout;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


public class WorkoutsFragment extends Fragment {

    private LinearLayout containerLayout;
    private LinearLayout categoryContainer;
    private String previousCategory;
    private int catid;
    private int catidTile;
    private HorizontalScrollView cardScrollView;
    private LinearLayout cardRow;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workouts, container, false);
        WorkoutsCardViewModel mCardViewModel = new ViewModelProvider(this).get(WorkoutsCardViewModel.class);
        mCardViewModel.getAllCards().observe(getViewLifecycleOwner(), cards -> {
            containerLayout = view.findViewById(R.id.workouts_container);
            containerLayout.removeAllViews();
            previousCategory = "";
            catid = 0;
            catidTile = 6;
            // add all cards to screen
            for (WorkoutCard card : cards){
                // check to see if we are on a new category
                if (!previousCategory.equals(card.getWorkoutCategory())){
                    // if there is a new category and the previous one isn't empty, add the previous
                    // to screen
                    if (!previousCategory.isEmpty()){
                        catid += 1;
                        catidTile += 1;
                    }
                    // create category
                    createCategory(card);
                }
                Resources r = getResources();
                // create and add card to category
                CardView cardView = new CardView(getContext());
                LinearLayout.LayoutParams cardViewParams =  new LinearLayout.LayoutParams(
                        Utility.dpToPx(300f,r),
                        Utility.dpToPx(200f,r)
                );
                cardViewParams.setMargins(0,0,Utility.dpToPx(10f,r),Utility.dpToPx(10f,r));
                cardView.setLayoutParams(cardViewParams);

                TextView textView = new TextView(getContext());
                textView.setText( card.getWorkoutTitle());
                textView.setBackground(r.getDrawable(R.drawable.card_title));
                CardView.LayoutParams params = new CardView.LayoutParams(
                        CardView.LayoutParams.WRAP_CONTENT,
                        CardView.LayoutParams.WRAP_CONTENT
                );



                params.setMargins(
                        Utility.dpToPx(5f, r),
                        Utility.dpToPx(5f, r),
                        Utility.dpToPx(0f, r),
                        Utility.dpToPx(0f, r)
                );
                textView.setLayoutParams(params);
                textView.setTextSize(18);
                //textView.setTextAppearance(getContext(), android.R.style.TextAppearance_DeviceDefault_Medium);
                textView.setTextColor(Color.parseColor("#FFFFFF"));
                textView.setPadding(
                        Utility.dpToPx(10f, r),
                        Utility.dpToPx(5f, r),
                        Utility.dpToPx(10f, r),
                        Utility.dpToPx(5f, r)
                );
                ImageView image = new ImageView(getContext());
                String uri = card.getImageName();
                int imageResource = getResources().getIdentifier(uri, null, "com.example.climbingworkout");
                Drawable res = getResources().getDrawable(imageResource);
                image.setImageDrawable(res);
                image.setScaleType(ImageView.ScaleType.CENTER_CROP);

                cardView.addView(image);
                cardView.addView(textView);
                cardView.setElevation(Utility.dpToPx(5f, r));
                cardView.setOnClickListener(v -> {
                    Intent intent = new Intent(getContext(), WorkoutOverview.class);
                    intent.putExtra("cardID", card.getCardID());
                    intent.putExtra("imageName", card.getImageName());
                    startActivity(intent);
                });
                cardRow.addView(cardView);
                cardRow.setPadding(0,0,Utility.dpToPx(10f, r), 0);
            }
        });
        return view;
    }

    private void createCategory(WorkoutCard card){
        // create category
        createCategoryContainer();
        // create title
        createCategoryTitle(card);
        // create scroll row
        createScrollViewCards();
        // create card container
        createCardsContainer();
        previousCategory = card.getWorkoutCategory();
    }

    private void createCategoryContainer(){
        categoryContainer = new LinearLayout(getContext());
        categoryContainer.setLayoutParams( new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                       LinearLayout.LayoutParams.WRAP_CONTENT
                )
        );
        categoryContainer.setId(catid);
        categoryContainer.setOrientation(LinearLayout.VERTICAL);
        containerLayout.addView(categoryContainer);
        // put category below the previous one
    }

    private void createCategoryTitle(WorkoutCard card){
        TextView categoryTitle = new TextView(getContext());
        categoryTitle.setText(card.getWorkoutCategory());
        categoryTitle.setId(catidTile);
        LinearLayout.LayoutParams categoryTitleLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        Resources r = getResources();
        categoryTitleLayoutParams.setMarginStart((Utility.dpToPx(10f,r)));
        categoryTitleLayoutParams.setMarginEnd((Utility.dpToPx(10f,r)));
        categoryTitle.setLayoutParams(categoryTitleLayoutParams);
        categoryTitle.setTextAppearance(getContext(), android.R.style.TextAppearance_DeviceDefault_Large);
        categoryContainer.addView(categoryTitle);
    }

    private void createScrollViewCards(){
        cardScrollView = new HorizontalScrollView(getContext());
        LinearLayout.LayoutParams cardScrollLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        cardScrollView.setLayoutParams(cardScrollLayoutParams);
        cardScrollView.setHorizontalScrollBarEnabled(false);
        categoryContainer.addView(cardScrollView);
    }

    private void createCardsContainer(){
        cardRow = new LinearLayout(getContext());
        LinearLayout.LayoutParams cardRowParams =  new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
                );
        Resources r = getResources();
        cardRowParams.setMargins(Utility.dpToPx(10f,r),
                                Utility.dpToPx(10f, r),
                                Utility.dpToPx(10f, r),
                                Utility.dpToPx(10f, r));
        cardRow.setLayoutParams(cardRowParams);
        cardRow.setOrientation(LinearLayout.HORIZONTAL);
        cardScrollView.addView(cardRow);
    }


}
