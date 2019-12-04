package com.tetraval.mochashi.genericmodule.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.tetraval.mochashi.R;
import com.tetraval.mochashi.genericmodule.model.OnBoardModel;

import java.util.ArrayList;

public class OnBoardAdapter extends PagerAdapter {

    private Context mContext;
    private ArrayList<OnBoardModel> onBoardModelsList = new ArrayList<>();

    public OnBoardAdapter(Context mContext, ArrayList<OnBoardModel> onBoardModelsList) {
        this.mContext = mContext;
        this.onBoardModelsList = onBoardModelsList;
    }

    @Override
    public int getCount() {
        return onBoardModelsList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.onboard_item, container, false);

        OnBoardModel onboardModel = onBoardModelsList.get(position);

        ImageView imgOnboard = itemView.findViewById(R.id.imgOnboard);
        imgOnboard.setImageResource(onboardModel.getOb_image());

        TextView txtTitle = itemView.findViewById(R.id.txtTitle);
        txtTitle.setText(onboardModel.getOb_title());

        TextView txtDesc = itemView.findViewById(R.id.txtDesc);
        txtDesc.setText(onboardModel.getOb_description());

        container.addView(itemView);

        return itemView;
    }

}
