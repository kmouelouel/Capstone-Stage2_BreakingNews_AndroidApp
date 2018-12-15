package com.example.kmoue.breakingnews.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kmoue.breakingnews.R;
import com.example.kmoue.breakingnews.data.NewsContract;
import com.squareup.picasso.Picasso;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsAdapterViewHolder> {
    private Cursor mCursor;
    private final NewsAdapterOnClickHandler mClickHandler;
    private final Context mContext;

   public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        // After the new Cursor is set, call notifyDataSetChanged
        notifyDataSetChanged();
    }

    public interface NewsAdapterOnClickHandler {
        void onClick(int position);
    }

    public NewsAdapter(NewsAdapterOnClickHandler clickHandler, @NonNull Context context) {
        mClickHandler = clickHandler;
        mContext=context;
    }
    @NonNull
    @Override
    public NewsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      Context context= parent.getContext();
        int layoutIdForListItem = R.layout.news_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, parent   , shouldAttachToParentImmediately);
        return new NewsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapterViewHolder holder, int position) {
         mCursor.moveToPosition(position);
        int titleCol = mCursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_TITLE);
        int imageCol = mCursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_URL_TO_IMAGE);
        int sourceCol = mCursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_SOURCE_NAME);
        holder.textView_newsTitle.setText(mCursor.getString(titleCol));
        if (mCursor.getString(imageCol).equals("null")) {
             Picasso.with(mContext).load(R.drawable.worldconnection).into(holder.imageView_news);
         } else {
            Picasso.with(mContext).load(mCursor.getString(imageCol)).into(holder.imageView_news);
        }
        if (mCursor.getString(sourceCol).equals("null")) {
            holder.textView_newsSource.setVisibility(View.INVISIBLE);
        } else {
            String sourceValue=mCursor.getString(sourceCol);
            String sourceLabel="Source: ";
            sourceLabel=sourceLabel.concat(sourceValue);
            holder.textView_newsSource.setText(sourceLabel);
        }


    }

    @Override
    public int getItemCount() {
       if(mCursor ==null) return 0;
       return mCursor.getCount();
    }

    public class NewsAdapterViewHolder extends RecyclerView.ViewHolder   implements View.OnClickListener {
        public final ImageView imageView_news;
        public final TextView textView_newsTitle;
        public final TextView textView_newsSource;
        public NewsAdapterViewHolder(View view) {
            super(view);
            imageView_news = (ImageView) view.findViewById(R.id.imageView_news);
            textView_newsTitle = (TextView) view.findViewById(R.id.tv_newsTitle);
            textView_newsSource = (TextView) view.findViewById(R.id.tv_newsSource);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(adapterPosition);
        }
    }

    public Cursor getCursorValue(){
       return mCursor;
    }


}
