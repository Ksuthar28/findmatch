package com.saadi.findmatch.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.saadi.findmatch.utils.HelperMethod;
import com.saadi.findmatch.R;
import com.saadi.findmatch.model.MyMatchModel;
import com.saadi.findmatch.utils.Databases;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Kailash Suthar.
 */

public class FindMatchAdapter extends RecyclerView.Adapter<FindMatchAdapter.ViewHolder> {

    private Context context;
    private ArrayList<MyMatchModel> matchList;

    /*
    Provide a suitable constructor (depends on the kind of data set)
    */
    public FindMatchAdapter(Context context, ArrayList<MyMatchModel> list) {
        this.context = context;
        this.matchList = list;
    }

    /*
    Provide a reference to the views for each data item
    Complex data items may need more than one view per item, and
    you provide access to all the views for a data item in a view holder
    */
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private ImageView imageThumbnail;
        private TextView textViewName, textViewAge, textViewLocation, textViewStatus;
        private LinearLayout actionView;
        private Button buttonAccept, buttonDecline;
        private View bottomView;

        public ViewHolder(View v) {
            super(v);
            try {
                imageThumbnail = v.findViewById(R.id.imageThumbnail);
                textViewName = v.findViewById(R.id.textViewName);
                textViewAge = v.findViewById(R.id.textViewAge);
                textViewLocation = v.findViewById(R.id.textViewLocation);
                textViewStatus = v.findViewById(R.id.textViewStatus);
                actionView = v.findViewById(R.id.actionView);
                buttonAccept = v.findViewById(R.id.buttonAccept);
                buttonDecline = v.findViewById(R.id.buttonDecline);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /*
    Create new views (invoked by the layout manager)
    */
    @NonNull
    @Override
    public FindMatchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_find_match, parent, false);
        // set the view's size, margins, padding and layout parameters
        return new FindMatchAdapter.ViewHolder(v);
    }

    /*
    Replace the contents of a view (invoked by the layout manager)
    */
    @Override
    public void onBindViewHolder(@NonNull final FindMatchAdapter.ViewHolder holder, final int position) {
        try {
            holder.setIsRecyclable(false);
            MyMatchModel item = matchList.get(position);
            String fullName = item.getNameModel().getTitle() + " " + item.getNameModel().getFirst() + " " + item.getNameModel().getLast();
            String ageGender = item.getDobModel().getAge() + " yrs - " + HelperMethod.getUpperCaseWord(item.getGender());
            String location = item.getLocationModel().getCity() + ", " + item.getLocationModel().getCountry();
            holder.textViewName.setText(fullName);
            holder.textViewAge.setText(ageGender);
            holder.textViewLocation.setText(location);
            holder.actionView.setVisibility(item.getStatus().equals("0") ? View.VISIBLE : View.GONE);
            holder.textViewStatus.setVisibility(item.getStatus().equals("0") ? View.GONE : View.VISIBLE);
            holder.textViewStatus.setText(item.getStatus().equals("1") ? context.getString(R.string.status_accepted) : context.getString(R.string.status_declined));
            holder.textViewStatus.setTextColor(item.getStatus().equals("1") ? ContextCompat.getColor(context, R.color.colorGreen) : ContextCompat.getColor(context, R.color.colorRed));
            if (item.getPictureModel().getMedium() != null && !item.getPictureModel().getMedium().isEmpty()) {
                Picasso.with(context)
                        .load(item.getPictureModel().getMedium())
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .fit()
                        .into(holder.imageThumbnail);
            }
            holder.buttonAccept.setOnClickListener(view -> {
                item.setStatus("1");
                new Databases(context).updateStatus(item.getPhone(), item.getStatus());
                notifyDataSetChanged();
            });
            holder.buttonDecline.setOnClickListener(view -> {
                item.setStatus("2");
                new Databases(context).updateStatus(item.getPhone(), item.getStatus());
                notifyDataSetChanged();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateList(ArrayList<MyMatchModel> list) {
        matchList = list;
        notifyDataSetChanged();
    }

    public ArrayList<MyMatchModel> getUpdatedList() {
        return matchList;
    }

    /*
    Return the size of your data set
    */
    @Override
    public int getItemCount() {
        return Math.max(matchList.size(), 0);
    }

}