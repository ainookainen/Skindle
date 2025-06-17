package com.example.skindle.util;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.skindle.R;
import com.example.skindle.data.SplashArtRepository;
import com.example.skindle.model.Champion;

import java.util.List;
import java.util.Locale;

/* Code utilizes official Android documentation template from:
 * https://developer.android.com/develop/ui/views/layout/recyclerview */
public class WrongGuessAdapter extends RecyclerView.Adapter<WrongGuessAdapter.ViewHolder> {

    private List<Champion> wrongGuesses;
    private Context context;
    private SplashArtRepository splashArtRepository;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameTextView;
        private final ImageView iconImageView;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            nameTextView = (TextView) view.findViewById(R.id.NameTextView);
            iconImageView = (ImageView) view.findViewById(R.id.IconImageView);
        }

        public TextView getNameTextView() {
            return nameTextView;
        }

        public ImageView getIconImageView() {
            return iconImageView;
        }
    }

    public WrongGuessAdapter(List<Champion> WrongGuesses, Context context) {
        this.wrongGuesses = WrongGuesses;
        this.context = context;
        this.splashArtRepository = new SplashArtRepository(context);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.wrong_guess_row_item, viewGroup, false);
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Champion wrongGuess = wrongGuesses.get(position);
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getNameTextView().setText(wrongGuess.getName().toUpperCase(Locale.ENGLISH));
        splashArtRepository.setIcon(wrongGuess,viewHolder.getIconImageView(),context);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return wrongGuesses.size();
    }
}