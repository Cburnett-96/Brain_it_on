package com.aqp.brainiton.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aqp.brainiton.R;
import com.aqp.brainiton.model.UserRankingTwoThree;

import java.io.InputStream;
import java.util.List;

public class RankingTopTwoThreeAdapter extends RecyclerView.Adapter<RankingTopTwoThreeAdapter.RankingViewHolder> {
    Context mCtx;
    List<UserRankingTwoThree> userList;
    final int limit = 2;
    String avatarName;

    public RankingTopTwoThreeAdapter(Context mCtx, List<UserRankingTwoThree> userList) {
        this.mCtx = mCtx;
        this.userList = userList;
    }

    @NonNull
    @Override
    public RankingTopTwoThreeAdapter.RankingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.recyclerview_ranking_top_two_three, parent, false);
        return new RankingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RankingTopTwoThreeAdapter.RankingViewHolder holder, int position) {
        UserRankingTwoThree user = userList.get(position);
        holder.textViewName.setText(user.Username);
        holder.textView_XP.setText(String.valueOf(user.getRiddle()));
        avatarName = user.Avatar;
        GetAvatarInfo(holder);



        if (position == 0){
            InputStream imageStream = holder.badgesRank.getResources().openRawResource(R.raw.pangalawa);
            Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
            holder.badgesRank.setImageBitmap(bitmap);
            holder.profileAvatar.setOnClickListener(view -> Toast.makeText(holder.profileAvatar.getContext(),
                    holder.textViewName.getText().toString().trim()+" 2nd place!", Toast.LENGTH_SHORT).show());
        } else {
            InputStream imageStream = holder.badgesRank.getResources().openRawResource(R.raw.pangatlo);
            Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
            holder.badgesRank.setImageBitmap(bitmap);
            holder.profileAvatar.setOnClickListener(view -> Toast.makeText(holder.profileAvatar.getContext(),
                    holder.textViewName.getText().toString().trim()+" 3rd place!", Toast.LENGTH_SHORT).show());
        }
    }

    @Override
    public int getItemCount() {
        return Math.min(userList.size(), limit);
    }

    public static class RankingViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textView_XP;
        ImageView profileAvatar, badgesRank;
        SharedPreferences prefs;

        public RankingViewHolder(@NonNull View itemView) {
            super(itemView);
            prefs = PreferenceManager.getDefaultSharedPreferences(itemView.getContext());

            textViewName = itemView.findViewById(R.id.textView_Name);
            textView_XP = itemView.findViewById(R.id.textView_XP);
            profileAvatar = itemView.findViewById(R.id.profileAvatar);
            badgesRank = itemView.findViewById(R.id.badgesRank);
        }
    }

    public void removeItem(int position) {
        userList.remove(position);
        notifyItemRemoved(position);
    }

    //Getting Avatar info and Apply to system
    private void GetAvatarInfo(@NonNull RankingTopTwoThreeAdapter.RankingViewHolder holder){
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
