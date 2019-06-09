package com.example.popular_movies_stage2.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.popular_movies_stage2.R;
import com.example.popular_movies_stage2.model.Movie;
import com.example.popular_movies_stage2.model.MovieReviewResults;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

public class ListAdapter extends BaseAdapter {

    private final Context context;
    private final List<MovieReviewResults> mData;

    public ListAdapter(Context context, List<MovieReviewResults> mData) {
        this.context = context;
        this.mData = mData;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public MovieReviewResults getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.review_item, parent, false);
        }


        ImageView movie_review_image_view = convertView.findViewById(R.id.movie_review_image_view);
        TextView review_author_name_text_view = convertView.findViewById(R.id.review_author_name_text_view);
        TextView review_content_text_view = convertView.findViewById(R.id.review_content_text_view);

        MovieReviewResults reviewResults = mData.get(position);

        review_author_name_text_view.setText(reviewResults.getmReviewResultAuthor());
        review_content_text_view.setText(reviewResults.getmReviewResultContent());

        Picasso.get()
                .load(Uri.parse("http://i.imgur.com/DvpvklR.png"))
                .into(movie_review_image_view);

        return convertView;
    }
}
