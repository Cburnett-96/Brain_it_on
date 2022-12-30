package com.aqp.brainiton.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aqp.brainiton.R;
import com.aqp.brainiton.model.UserRankingOne;

import java.io.InputStream;
import java.util.List;

public class RankingTopOneAdapter extends RecyclerView.Adapter<RankingTopOneAdapter.RankingViewHolder> {
    Context mCtx;
    List<UserRankingOne> userList;
    final int limit = 1;
    String avatarName;

    public RankingTopOneAdapter(Context mCtx, List<UserRankingOne> userList) {
        this.mCtx = mCtx;
        this.userList = userList;
    }

    @NonNull
    @Override
    public RankingTopOneAdapter.RankingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.recyclerview_ranking_top_one, parent, false);
        return new RankingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RankingTopOneAdapter.RankingViewHolder holder, int position) {
        UserRankingOne user = userList.get(position);
        holder.textViewName.setText(user.Username);
        holder.textViewPoints.setText(String.valueOf(user.getPoints()));
        avatarName = user.Avatar;
        GetAvatarInfo(holder);

        holder.profileAvatar.setOnClickListener(view -> Toast.makeText(holder.profileAvatar.getContext(),
                holder.textViewName.getText().toString().trim()+" the only 1st place!", Toast.LENGTH_SHORT).show());
    }

    @Override
    public int getItemCount() {
        return Math.min(userList.size(), limit);
    }

    class RankingViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewPoints;
        ImageView profileAvatar;
        SharedPreferences prefs;

        public RankingViewHolder(@NonNull View itemView) {
            super(itemView);
            prefs = PreferenceManager.getDefaultSharedPreferences(itemView.getContext());

            textViewName = itemView.findViewById(R.id.textViewNameTop1);
            textViewPoints = itemView.findViewById(R.id.textViewXpTop1);
            profileAvatar = itemView.findViewById(R.id.avatarTop1);

            textViewName.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            textViewName.setSelected(true);
        }
    }

    //Getting Avatar info and Apply to system
    private void GetAvatarInfo(@NonNull RankingTopOneAdapter.RankingViewHolder holder){
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
