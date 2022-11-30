package com.aqp.brainiton.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aqp.brainiton.R;
import com.aqp.brainiton.model.UserRankingTen;

import java.io.InputStream;
import java.util.List;

public class RankingTopTenAdapter extends RecyclerView.Adapter<RankingTopTenAdapter.RankingViewHolder> {
    Context mCtx;
    List<UserRankingTen> userList;
    final int limit = 7;
    String avatarName;

    public RankingTopTenAdapter(Context mCtx, List<UserRankingTen> userList) {
        this.mCtx = mCtx;
        this.userList = userList;
    }

    @NonNull
    @Override
    public RankingTopTenAdapter.RankingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.recyclerview_ranking_top_ten, parent, false);
        return new RankingViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RankingTopTenAdapter.RankingViewHolder holder, int position) {

        UserRankingTen user = userList.get(position);
        holder.textViewName.setText(user.Username);
        holder.textViewPoints.setText(String.valueOf(user.getRiddle()));
        avatarName = user.Avatar;
        GetAvatarInfo(holder);

        switch (position){
            case 0: {
                holder.textView_Rank.setText("4\u1d40\u1d78");
                holder.linearLayoutTopTen.setOnClickListener(view -> Toast.makeText(holder.linearLayoutTopTen.getContext(),
                        holder.textViewName.getText().toString().trim()+" 4th place!", Toast.LENGTH_SHORT).show());
                break;
            }
            case 1: {
                holder.textView_Rank.setText("5\u1d40\u1d78");
                holder.linearLayoutTopTen.setOnClickListener(view -> Toast.makeText(holder.linearLayoutTopTen.getContext(),
                        holder.textViewName.getText().toString().trim()+" 5th place!", Toast.LENGTH_SHORT).show());
                break;
            }
            case 2: {
                holder.textView_Rank.setText("6\u1d40\u1d78");
                holder.linearLayoutTopTen.setOnClickListener(view -> Toast.makeText(holder.linearLayoutTopTen.getContext(),
                        holder.textViewName.getText().toString().trim()+" 6th place!", Toast.LENGTH_SHORT).show());
                break;
            }
            case 3: {
                holder.textView_Rank.setText("7\u1d40\u1d78");
                holder.linearLayoutTopTen.setOnClickListener(view -> Toast.makeText(holder.linearLayoutTopTen.getContext(),
                        holder.textViewName.getText().toString().trim()+" 7th place!", Toast.LENGTH_SHORT).show());
                break;
            }
            case 4: {
                holder.textView_Rank.setText("8\u1d40\u1d78");
                holder.linearLayoutTopTen.setOnClickListener(view -> Toast.makeText(holder.linearLayoutTopTen.getContext(),
                        holder.textViewName.getText().toString().trim()+" 8th place!", Toast.LENGTH_SHORT).show());
                break;
            }
            case 5: {
                holder.textView_Rank.setText("9\u1d40\u1d78");
                holder.linearLayoutTopTen.setOnClickListener(view -> Toast.makeText(holder.linearLayoutTopTen.getContext(),
                        holder.textViewName.getText().toString().trim()+" 9th place!", Toast.LENGTH_SHORT).show());
                break;
            }
            case 6: {
                holder.textView_Rank.setText("10\u1d40\u1d78");
                holder.linearLayoutTopTen.setOnClickListener(view -> Toast.makeText(holder.linearLayoutTopTen.getContext(),
                        holder.textViewName.getText().toString().trim()+" 10th place!", Toast.LENGTH_SHORT).show());
                break;
            }
            default: {
                holder.textView_Rank.setText("#");
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return Math.min(userList.size(), limit);
    }

    public static class RankingViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayoutTopTen;
        TextView textViewName, textViewPoints, textView_Rank;
        ImageView profileAvatar;
        SharedPreferences prefs;

        public RankingViewHolder(@NonNull View itemView) {
            super(itemView);

            prefs = PreferenceManager.getDefaultSharedPreferences(itemView.getContext());

            linearLayoutTopTen = itemView.findViewById(R.id.linearTopTen);
            textViewName = itemView.findViewById(R.id.textView_Name);
            textViewPoints = itemView.findViewById(R.id.textView_Points);
            textView_Rank = itemView.findViewById(R.id.textView_Rank);
            profileAvatar = itemView.findViewById(R.id.profileAvatar);
        }
    }

    public void removeItem(int position) {
        userList.remove(position);
        notifyItemRemoved(position);
    }

    //Getting Avatar info and Apply to system
    private void GetAvatarInfo(@NonNull RankingTopTenAdapter.RankingViewHolder holder){
        switch (avatarName) {
            case "avatar_1": {
                InputStream imageStream = holder.profileAvatar.getResources().openRawResource(R.raw.avatar_1);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                holder.profileAvatar.setImageBitmap(bitmap);
                break;
            }
            case "avatar_2": {
                InputStream imageStream = holder.profileAvatar.getResources().openRawResource(R.raw.avatar_2);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                holder.profileAvatar.setImageBitmap(bitmap);
                break;
            }
            case "avatar_3": {
                InputStream imageStream = holder.profileAvatar.getResources().openRawResource(R.raw.avatar_3);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                holder.profileAvatar.setImageBitmap(bitmap);
                break;
            }
            case "avatar_4": {
                InputStream imageStream = holder.profileAvatar.getResources().openRawResource(R.raw.avatar_4);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                holder.profileAvatar.setImageBitmap(bitmap);
                break;
            }
            case "avatar_5": {
                InputStream imageStream = holder.profileAvatar.getResources().openRawResource(R.raw.avatar_5);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                holder.profileAvatar.setImageBitmap(bitmap);
                break;
            }
            case "avatar_6": {
                InputStream imageStream = holder.profileAvatar.getResources().openRawResource(R.raw.avatar_6);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                holder.profileAvatar.setImageBitmap(bitmap);
                break;
            }
            case "avatar_7": {
                InputStream imageStream = holder.profileAvatar.getResources().openRawResource(R.raw.avatar_7);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                holder.profileAvatar.setImageBitmap(bitmap);
                break;
            }
            case "avatar_8": {
                InputStream imageStream = holder.profileAvatar.getResources().openRawResource(R.raw.avatar_8);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                holder.profileAvatar.setImageBitmap(bitmap);
                break;
            }
            default: {
                InputStream imageStream = holder.profileAvatar.getResources().openRawResource(R.raw.profile);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                holder.profileAvatar.setImageBitmap(bitmap);
                break;
            }
        }
    }
}
