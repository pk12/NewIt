package com.example.oppai.top10.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.oppai.top10.Activities.WebViewActivity;
import com.example.oppai.top10.Article;
import com.example.oppai.top10.ArticleFilter;
import com.example.oppai.top10.Fragments.FavoritesFragment;
import com.example.oppai.top10.Fragments.RecyclerViewFragment;
import com.example.oppai.top10.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RvAdapter extends RecyclerView.Adapter<RvAdapter.RvViewHolder> implements Filterable {
    private ArrayList<Article> data, dataIntact;
    private Activity activity;
    private int itemLayout;
    private DatabaseReference reference;
    private TextView textView;
    private ImageView imageView;
    private int lastPosition;

    public RvAdapter(ArrayList<Article> data, Activity activity, TextView textView, ImageView imageView, Fragment fragment) {
        this.data = data;
        this.dataIntact = new ArrayList<>();
        this.dataIntact.addAll(data);
        this.activity = activity;
        this.textView = textView;
        this.imageView = imageView;
        this.fragment = fragment;
        this.itemLayout = R.layout.recycleritem;
        lastPosition = -1;
        reference = FirebaseDatabase.getInstance().getReference("Favorites").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

    }

    private Fragment fragment;

    public RvAdapter(ArrayList<Article> data, Activity activity, boolean isVertical, Fragment fragment) {
        this.data = data;
        this.activity = activity;
        this.fragment = fragment;
        if (isVertical){
            this.itemLayout = R.layout.recycleritem;
        }
        else {
            this.itemLayout = R.layout.recycler_item_horizontal;

        }
        this.dataIntact = new ArrayList<>();
        this.dataIntact.addAll(data);
        lastPosition = -1;
        reference = FirebaseDatabase.getInstance().getReference("Favorites").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
    }

    public RvAdapter(ArrayList<Article> data, Activity activity, boolean isVertical){
        this.data = data;
        this.activity = activity;
        if (isVertical){
            this.itemLayout = R.layout.recycleritem;
        }
        else {
            this.itemLayout = R.layout.recycler_item_horizontal;

        }

        this.dataIntact = new ArrayList<>();
        this.dataIntact.addAll(data);
        lastPosition = -1;
        reference = FirebaseDatabase.getInstance().getReference("Favorites").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

    }

    @Override
    public void onViewRecycled(@NonNull RvViewHolder holder) {
        super.onViewRecycled(holder);
        holder.urlPic.setVisibility(View.VISIBLE);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(holder.constraintLayout);
        constraintSet.connect(R.id.titleTextView,ConstraintSet.TOP, R.id.urlImageView, ConstraintSet.BOTTOM, 16);
        constraintSet.applyTo(holder.constraintLayout);

        if (fragment instanceof RecyclerViewFragment)
            holder.action.setBackgroundResource(R.drawable.ic_favorite_border_black_24dp);
    }

    @NonNull
    @Override
    public RvViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(this.itemLayout, parent, false);
        return new RvViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RvViewHolder holder, int position) {
        //Set the data here
        Article article = data.get(position);
        holder.title.setText(article.getTitle());
        String str = article.getDescription();

        //Add the listener out of the onClick to make sure not to initialize a lot of listeners needlessly


        //set onclick listener for action button
        DatabaseReference finalReference = reference;


//        holder.action.setOnCheckedChangeListener((view, isChecked) -> {
//            if (fragment instanceof FavoritesFragment){
//                finalReference.removeValue();
//                Toast.makeText(activity.getApplicationContext(), "Removed from favorites", Toast.LENGTH_SHORT).show();
//            }
//            else {
//                //Update the view
//                //TODO: check the saved hearts
//                if (isChecked){
//                    view.setBackgroundResource(R.drawable.ic_favorite_black_24dp);
//                    finalReference.setValue(data.get(position));
//                    Toast.makeText(activity.getApplicationContext(), "Added to favorites", Toast.LENGTH_SHORT).show();
//                }
//                else {
//                    view.setBackgroundResource(R.drawable.ic_favorite_border_black_24dp);
//                    finalReference.removeValue();
//                    Toast.makeText(activity.getApplicationContext(), "Removed from favorites", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//
//        });



        setAnimation(holder.cardView, position);
        //Check if some of the data equals null
        if(!str.equals("null")){
            holder.description.setText(str);
        }
        else {
            holder.description.setText("");
        }

        if (!article.getImageUrl().equals("null")) {
            Picasso.get().load(article.getImageUrl()).placeholder(R.drawable.image_placeholder).into(holder.urlPic);
        }
        else {

            holder.urlPic.setVisibility(View.GONE);

            //create constraint for titleView
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(holder.constraintLayout);
            constraintSet.connect(R.id.titleTextView,ConstraintSet.TOP, R.id.cardConstraintLayout, ConstraintSet.TOP, 8);
            constraintSet.applyTo(holder.constraintLayout);
            //Have to make the view visible for the next item so i set it to Visible on the OnRecycled

        }




    }
    public ArrayList<Article> getDataIntact() {
        return dataIntact;
    }
    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public Filter getFilter() {
        if (fragment instanceof FavoritesFragment){
            return new ArticleFilter(data, dataIntact, this, textView, imageView);
        }
        return new ArticleFilter(data, this, dataIntact, activity);
    }

    private void setAnimation(View v, int position){
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(activity.getApplicationContext(), R.anim.expand_in);
            v.startAnimation(animation);
            lastPosition = position;
        }
    }



    public class RvViewHolder extends RecyclerView.ViewHolder implements RecyclerView.OnClickListener {
        //Create View variables
        private TextView title;
        private TextView description;
        private ImageView urlPic;
        private ToggleButton action;
        private ImageButton share;
        private CardView cardView;
        ConstraintLayout constraintLayout;

        public RvViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titleTextView);
            description = itemView.findViewById(R.id.descriptionTextView);
            urlPic = itemView.findViewById(R.id.urlImageView);
            action = itemView.findViewById(R.id.favButton);
            constraintLayout = itemView.findViewById(R.id.cardConstraintLayout);
            share = itemView.findViewById(R.id.shareButton);
            cardView = itemView.findViewById(R.id.RVCardview);
            itemView.setOnClickListener(this);
            share.setOnClickListener(this::onClick);
            action.setOnClickListener(this::onClick);

            if (fragment instanceof FavoritesFragment){
                action.setBackgroundResource(R.drawable.ic_delete_black_24dp);
            }
        }

        @Override
        public void onClick(View v) {

            //Avoid binding many listeners by using one generic
            switch (v.getId()){
                case R.id.favButton:
                    //ToggleButton button = activity.findViewById(R.id.favButton);
                    ToggleButton button = (ToggleButton) v;
                    boolean isChecked = button.isChecked();
                    reference = reference.child(data.get(getAdapterPosition()).getTitle().replace('.',' ').replace('#', ' ')
                            .replace('$', ' ').replace('[', ' ').replace(']', ' '));

                    if (fragment instanceof FavoritesFragment){
                        reference.removeValue();
                        data.remove(getAdapterPosition());
                        dataIntact.remove(getAdapterPosition());
                        RvAdapter.this.notifyDataSetChanged();
                        if (getItemCount() == 0){
                            textView.setVisibility(View.VISIBLE);
                            imageView.setVisibility(View.VISIBLE);
                        }
                        else {
                            textView.setVisibility(View.GONE);
                            imageView.setVisibility(View.GONE);
                        }
                        Toast.makeText(activity.getApplicationContext(), "Removed from favorites", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        //Update the view
                        //TODO: check the saved hearts
                        if (isChecked){
                            v.setBackgroundResource(R.drawable.ic_favorite_black_24dp);
                            reference.setValue(data.get(getAdapterPosition()));
                            Toast.makeText(activity.getApplicationContext(), "Added to favorites", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            v.setBackgroundResource(R.drawable.ic_favorite_border_black_24dp);
                            reference.removeValue();
                            Toast.makeText(activity.getApplicationContext(), "Removed from favorites", Toast.LENGTH_SHORT).show();
                        }
                    }
                    ResetReference();
                    break;
                case R.id.shareButton:
                    //start a share intent
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_ORIGINATING_URI, Uri.parse(getData().get(getAdapterPosition()).getUrl()));
                    intent.putExtra(Intent.EXTRA_TEXT,  getData().get(getAdapterPosition()).getUrl());
                    intent.putExtra(Intent.EXTRA_STREAM, getData().get(getAdapterPosition()).getUrl());
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Share link");
                    activity.startActivity(Intent.createChooser(intent, "Share URL"));
                    break;
                default:
                    Intent intenWebt = new Intent(RvAdapter.this.activity.getApplicationContext(), WebViewActivity.class);
                    int position = getLayoutPosition();
                    intenWebt.putExtra("URL", data.get(position).getUrl());
                    activity.startActivity(intenWebt);
                    break;




            }




        }
    }

    public ArrayList<Article> getData() {
        return data;
    }

    private void ResetReference(){
        reference = FirebaseDatabase.getInstance().getReference("Favorites").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
    }
}
