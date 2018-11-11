package com.example.kmoue.breakingnews.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kmoue.breakingnews.R;
import com.example.kmoue.breakingnews.data.NewsContract;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsAdapterViewHolder> {
    private Cursor mCursor;
    private final NewsAdapterOnClickHandler mClickHandler;

   public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        // After the new Cursor is set, call notifyDataSetChanged
        notifyDataSetChanged();
    }

    public interface NewsAdapterOnClickHandler {
        void onClick(String newsElement);
    }

    public NewsAdapter(NewsAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
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

        String newsElement=mCursor.getString(
                mCursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_TITLE));
        holder.mResultsTextView.setText(newsElement);
    }

    @Override
    public int getItemCount() {
       if(mCursor ==null) return 0;
       return mCursor.getCount();
    }

    public class NewsAdapterViewHolder extends RecyclerView.ViewHolder   implements View.OnClickListener {
        public final TextView mResultsTextView;
        public NewsAdapterViewHolder(View view) {
            super(view);
            mResultsTextView= (TextView) view.findViewById(R.id.tv_news_data);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            String newsElement=mCursor.getString(4);
            mClickHandler.onClick(newsElement);
        }
    }


}
