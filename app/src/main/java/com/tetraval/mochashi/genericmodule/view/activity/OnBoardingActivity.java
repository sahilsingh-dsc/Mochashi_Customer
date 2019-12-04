package com.tetraval.mochashi.genericmodule.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.button.MaterialButton;
import com.tetraval.mochashi.R;
import com.tetraval.mochashi.genericmodule.model.OnBoardModel;
import com.tetraval.mochashi.genericmodule.view.activity.authmodule.LoginActivity;
import com.tetraval.mochashi.genericmodule.view.activity.authmodule.MobileActivity;
import com.tetraval.mochashi.genericmodule.view.activity.authmodule.RegisterActivity;
import com.tetraval.mochashi.genericmodule.view.adapter.OnBoardAdapter;

import java.util.ArrayList;
import java.util.Objects;

public class OnBoardingActivity extends AppCompatActivity {

    private LinearLayout pager_indicator;
    private int dotsCount;
    private ImageView[] dots;
    private OnBoardAdapter onBoardAdapter;
    ArrayList<OnBoardModel> onBoardModelList;
    MaterialButton btnSkipNext;

    int previous_pos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);

        // support action and toolbar setup here
        Toolbar toolbarOnBoard = findViewById(R.id.toolbarOnBoard);
        setSupportActionBar(toolbarOnBoard);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Welcome");
        toolbarOnBoard.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        Objects.requireNonNull(toolbarOnBoard.getOverflowIcon()).setColorFilter(Color.WHITE , PorterDuff.Mode.SRC_ATOP);

        pager_indicator = findViewById(R.id.pager_indicator);
        ViewPager onBoardPager = findViewById(R.id.onBoardPager);
        btnSkipNext = findViewById(R.id.btnSkipNext);
        btnSkipNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MobileActivity.class));
            }
        });


        onBoardModelList = new ArrayList<>();

        loadData();

        onBoardAdapter = new OnBoardAdapter(this, onBoardModelList);
        onBoardPager.setAdapter(onBoardAdapter);
        onBoardPager.setCurrentItem(0);
        onBoardPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for (int i = 0; i < dotsCount; i++) {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(OnBoardingActivity.this, R.drawable.non_selected_item_dot));
                }

                dots[position].setImageDrawable(ContextCompat.getDrawable(OnBoardingActivity.this, R.drawable.selected_item_dot));

                int pos=position+1;

                if(pos==dotsCount&&previous_pos==(dotsCount-1)){
                    btnSkipNext.setText("NEXT");
                    btnSkipNext.setVisibility(View.VISIBLE);
                    //btn_state = 2;
                }
//                    show_animation();
                else if(pos==(dotsCount-1)&&previous_pos==dotsCount){
                    btnSkipNext.setVisibility(View.VISIBLE);
                    btnSkipNext.setText("SKIP");
                    //btn_state = 1;
                }
//                    hide_animation();

                previous_pos=pos;

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        setUiPageViewController();

    }

    public void loadData()
    {

        int[] header = {R.string.ob_header1, R.string.ob_header2, R.string.ob_header3};
        int[] desc = {R.string.ob_desc1, R.string.ob_desc2, R.string.ob_desc3};
        int[] imageId = {R.drawable.fruit, R.drawable.funnel, R.drawable.place};

        for(int i=0;i<imageId.length;i++)
        {
            OnBoardModel onboardModel = new OnBoardModel();
            onboardModel.setOb_image(imageId[i]);
            onboardModel.setOb_title(getResources().getString(header[i]));
            onboardModel.setOb_description(getResources().getString(desc[i]));
            onBoardModelList.add(onboardModel);
        }
    }

    private void setUiPageViewController() {

        dotsCount = onBoardAdapter.getCount();
        dots = new ImageView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(OnBoardingActivity.this, R.drawable.non_selected_item_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(6, 0, 6, 0);

            pager_indicator.addView(dots[i], params);
        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(OnBoardingActivity.this, R.drawable.selected_item_dot));
    }

}
