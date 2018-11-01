package com.example.kmoue.breakingnews.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kmoue.breakingnews.R;
import com.example.kmoue.breakingnews.models.NewsObject;
import com.example.kmoue.breakingnews.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ListViewAdapter extends BaseAdapter {
    private final Context mContext;
    private final LayoutInflater mInflater;
    private List<NewsObject> mNewsHeadlines;
   public ListViewAdapter(Context context, List<NewsObject> mNews){
       mContext= context;
       mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
       mNewsHeadlines = mNews;
   }
    @Override
    public int getCount() {
        return mNewsHeadlines.size();
    }

    @Override
    public NewsObject getItem(int i) {
        return mNewsHeadlines.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder;
        if (view == null) {
            view = mInflater.inflate(R.layout.news_list_item, parent, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }
        final NewsObject news = getItem(position);
       String image_url =news.getUrlToImage();
        viewHolder = (ViewHolder) view.getTag();
       Picasso.with(mContext).load(image_url).into(viewHolder.imageView);

        return view;
    }


    public static class ViewHolder {
        public final ImageView imageView;
        public final TextView textView;

        public ViewHolder(View view) {
            imageView = (ImageView) view.findViewById(R.id.imageView_news);
            textView= (TextView) view.findViewById(R.id.textView_title);
        }
    }
}
