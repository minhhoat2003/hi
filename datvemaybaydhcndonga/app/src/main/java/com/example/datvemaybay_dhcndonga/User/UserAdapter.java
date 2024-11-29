package com.example.datvemaybay_dhcndonga.User;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datvemaybay_dhcndonga.R;
import com.example.datvemaybay_dhcndonga.User.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<User> userList;

    public UserAdapter(List<User> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.iduserTextView.setText("ID: " + user.getIduser());
        holder.usernameTextView.setText("Username: " + user.getUsername());
        holder.fullnameTextView.setText("Họ và Tên: " + user.getFullname());
        holder.birthdateTextView.setText("Ngày Sinh: " + user.getBirthdate());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView iduserTextView;
        TextView usernameTextView;
        TextView fullnameTextView;
        TextView birthdateTextView;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            iduserTextView = itemView.findViewById(R.id.iduserTextView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
            fullnameTextView = itemView.findViewById(R.id.fullnameTextView);
            birthdateTextView = itemView.findViewById(R.id.birthdateTextView);
        }
    }
}
