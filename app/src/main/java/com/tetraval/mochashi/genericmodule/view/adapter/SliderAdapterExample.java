package com.tetraval.mochashi.genericmodule.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.smarteist.autoimageslider.SliderViewAdapter;
import com.tetraval.mochashi.R;

public class SliderAdapterExample extends SliderViewAdapter<SliderAdapterExample.SliderAdapterVH> {

    Context context;

    public SliderAdapterExample(Context context) {
        this.context = context;
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, int position) {
        //viewHolder.textViewDescription.setText("This is slider item " + position);
//        SliderPojo sliderPojo = sliderPojoList.get(position);
//        Glide.with(context).load(sliderPojo.getIamge()).into(viewHolder.imageViewBackground);

        switch (position) {
            case 0:
                viewHolder.imageViewBackground.setImageDrawable(context.getResources().getDrawable(R.drawable.banner1));
                break;
            case 1:
                viewHolder.imageViewBackground.setImageDrawable(context.getResources().getDrawable(R.drawable.banner2));
//                Glide.with(viewHolder.itemView)
//                        .load("http://34.87.126.245/laptop/uploads/brand.jpg")
//                        .into(viewHolder.imageViewBackground);
                break;
            default:
                viewHolder.imageViewBackground.setImageDrawable(context.getResources().getDrawable(R.drawable.banner3));
//                Glide.with(viewHolder.itemView)
//                        .load("http://34.87.126.245/laptop/uploads/brand.jpg")
//                        .into(viewHolder.imageViewBackground);
                break;

        }


    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return 3;
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        View itemView;
        ImageView imageViewBackground;
        //TextView textViewDescription;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
            //textViewDescription = itemView.findViewById(R.id.tv_auto_image_slider);
            this.itemView = itemView;
        }
    }
}