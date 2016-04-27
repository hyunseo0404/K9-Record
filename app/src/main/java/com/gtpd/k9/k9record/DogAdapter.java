package com.gtpd.k9.k9record;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class DogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String IMAGE_URL = "http://ec2-52-207-245-173.compute-1.amazonaws.com";
    private static final int SECTION_TYPE = 0;
    private static final int ITEM_TYPE = 1;

    private List<Dog> myDogs;
    private List<Dog> dogs;
    private List<Dog> filteredDogs = null;
    private DogHolder selectedDogHolder;
    private Dog selectedDog;
    private DogSelectionFragment fragment;
    private int selectedPosition;
    private boolean filtered = false;

    public DogAdapter(List<Dog> myDogs, List<Dog> dogs, DogSelectionFragment fragment, Dog selectedDog, int selectedPosition) {
        this.myDogs = myDogs;
        this.dogs = dogs;
        this.selectedDog = selectedDog;
        this.selectedPosition = selectedPosition;
        this.fragment = fragment;
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
        } else if (filtered) {
            ((DogHolder) holder).bindDog(filteredDogs.get(position - 1));
        } else if (position <= myDogs.size()) {
            ((DogHolder) holder).bindDog(myDogs.get(position - 1));
        } else {
            ((DogHolder) holder).bindDog(dogs.get(position - myDogs.size() - 2));
        }
    }

    @Override
    public int getItemCount() {
        return filtered ? (filteredDogs.size() + 1) : (myDogs.size() + dogs.size() + 2);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 || (position == myDogs.size() + 1 && !filtered)) {
            return SECTION_TYPE;
        } else {
            return ITEM_TYPE;
        }
    }

    public Dog getSelectedDog() {
        return selectedDog;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void setMyDogs(List<Dog> myDogs) {
        this.myDogs = myDogs;

        if (filtered) removeFilteredDogs();

        notifyDataSetChanged();
    }

    public void setDogs(List<Dog> dogs) {
        dogs.removeAll(myDogs);
        this.dogs = dogs;

        if (filtered) removeFilteredDogs();

        notifyDataSetChanged();
    }

    public void setFilteredDogs(List<Dog> filteredDogs) {
        this.filteredDogs = filteredDogs;
        filtered = true;
        notifyDataSetChanged();
    }

    public void restoreDogs() {
        removeFilteredDogs();
        notifyDataSetChanged();
    }

    private void removeFilteredDogs() {
        this.filteredDogs = null;
        filtered = false;
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

            if (dog.getImageResource() != null) {
                new MainActivity.ImageDownloadTask(dogImageView).execute(IMAGE_URL + dog.getImageResource());
            }

            dogNameTextView.setText(dog.getName());
            dogDescriptionTextView.setText(dog.getDescription());

            if (dog.equals(selectedDog)) {
                if (selectedDogHolder != null) {
                    selectedDogHolder.dogSelectedImageView.setVisibility(View.INVISIBLE);
                }

                dogSelectedImageView.setVisibility(View.VISIBLE);
                selectedDogHolder = this;
            }
        }

        @Override
        public void onClick(View v) {
            if (dog != null) {
                if (selectedDogHolder != null) {
                    selectedDogHolder.dogSelectedImageView.setVisibility(View.INVISIBLE);
                }

                dogSelectedImageView.setVisibility(View.VISIBLE);
                selectedDogHolder = this;
                selectedDog = dog;
                selectedPosition = getLayoutPosition();
                fragment.animateContinueButton(true);
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

            if (section == 0 && filtered) {
                sectionTitleTextView.setText("Matching Dog" + (filteredDogs.size() > 1 ? "s" : ""));
                topSpacing.setVisibility(View.GONE);
            } else if (section == 0) {
                sectionTitleTextView.setText("My Dog" + (myDogs.size() > 1 ? "s" : ""));
                topSpacing.setVisibility(View.GONE);
            } else {
                sectionTitleTextView.setText("Other Dog" + (dogs.size() > 1 ? "s" : ""));
                topSpacing.setVisibility(View.VISIBLE);
            }
        }
    }
}
