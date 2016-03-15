package com.gtpd.k9.k9record;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class DogAdapter extends RecyclerView.Adapter<DogAdapter.DogHolder> {

    public DogHolder selectedDogHolder;

    private List<Dog> dogs;

    public DogAdapter(List<Dog> dogs) {
        this.dogs = dogs;
    }

    @Override
    public DogHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dog_list_item, parent, false);
        return new DogHolder(view);
    }

    @Override
    public void onBindViewHolder(DogHolder holder, int position) {
        holder.bindDog(dogs.get(position));
    }

    @Override
    public int getItemCount() {
        return dogs.size();
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
            dogImageView.setImageResource(dog.imageResource);    // FIXME: test image
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
}
