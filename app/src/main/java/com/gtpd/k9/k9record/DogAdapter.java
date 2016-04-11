package com.gtpd.k9.k9record;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class DogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int SECTION_TYPE = 0;

    public DogHolder selectedDogHolder;

    private List<Dog> myDogs;
    private List<Dog> dogs;

    public DogAdapter(List<Dog> myDogs, List<Dog> dogs) {
        this.myDogs = myDogs;
        this.dogs = dogs;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == SECTION_TYPE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dog_list_header, parent, false);
            return new SectionHeaderHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dog_list_item, parent, false);
            return new DogHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == SECTION_TYPE) {
            ((SectionHeaderHolder) holder).setSection(position);
        } else if (position <= myDogs.size()) {
            ((DogHolder) holder).bindDog(myDogs.get(position - 1));
        } else {
            ((DogHolder) holder).bindDog(dogs.get(position - myDogs.size() - 2));
        }
    }

    @Override
    public int getItemCount() {
        return myDogs.size() + dogs.size() + 2;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0 || position == myDogs.size() + 1) ? SECTION_TYPE : 1;
    }

    public class DogHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView dogImageView;
        private final TextView dogNameTextView;
        private final TextView dogDescriptionTextView;
        private final ImageView dogSelectedImageView;
        private Dog dog;

        public DogHolder(View itemView) {
            super(itemView);

            dogImageView = (ImageView) itemView.findViewById(R.id.dogImage);
            dogNameTextView = (TextView) itemView.findViewById(R.id.dogName);
            dogDescriptionTextView = (TextView) itemView.findViewById(R.id.dogDescription);
            dogSelectedImageView = (ImageView) itemView.findViewById(R.id.dogSelectedImage);
            itemView.setOnClickListener(this);
        }

        public void bindDog(Dog dog) {
            this.dog = dog;
            dogImageView.setImageResource(dog.imageResource);
            dogNameTextView.setText(dog.name);
            dogDescriptionTextView.setText(dog.description);
        }

        @Override
        public void onClick(View v) {
            if (dog != null) {
                if (selectedDogHolder != null) {
                    selectedDogHolder.dogSelectedImageView.setVisibility(View.INVISIBLE);
                }

                dogSelectedImageView.setVisibility(View.VISIBLE);
                selectedDogHolder = this;
            }
        }

        public Dog getDog() {
            return dog;
        }
    }

    public class SectionHeaderHolder extends RecyclerView.ViewHolder {
        private final View topSpacing;
        private final TextView sectionTitleTextView;
        private int section;

        public SectionHeaderHolder(View itemView) {
            super(itemView);

            topSpacing = itemView.findViewById(R.id.topSpacing);
            sectionTitleTextView = (TextView) itemView.findViewById(R.id.sectionTitle);
        }

        public void setSection(int section) {
            this.section = section;

            if (section == 0) {
                sectionTitleTextView.setText("My Dogs");
                topSpacing.setVisibility(View.GONE);
            } else {
                sectionTitleTextView.setText("Other Dogs");
                topSpacing.setVisibility(View.VISIBLE);
            }
        }
    }
}
