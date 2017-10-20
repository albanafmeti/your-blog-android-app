package com.techalin.yourblog.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.techalin.yourblog.EditPostActivity;
import com.techalin.yourblog.R;
import com.techalin.yourblog.ShowPostActivity;
import com.techalin.yourblog.controllers.PostsController;
import com.techalin.yourblog.models.Post;

import java.util.List;


public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.MyViewHolder> {
    List<Post> posts_list;
    private LayoutInflater inflater;

    private Context context;
    private PostsController postCon;
    private int position;

    public PostsAdapter(Context context, List<Post> data) {
        inflater = LayoutInflater.from(context);

        PostsController postsCon = new PostsController(context);
        this.posts_list = postsCon.posts_list();
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Post current = posts_list.get(position);
        holder.setData(current, position);
        holder.setListeners();
    }

    @Override
    public int getItemCount() {
        return posts_list.size();
    }


    public void removeItem(int pos) {
        //Remove Item

        this.position = pos;
        postCon = new PostsController(context);

        //Me Alert konfirmues
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setTitle("Warning");
        alertBuilder.setMessage("Are you sure you want to delete this item ?");

        alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                boolean is_deleted = postCon.delete_post(posts_list.get(position));

                if (is_deleted) {
                    posts_list.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, posts_list.size());
                }
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                dialog.dismiss();
            }
        });
        alertBuilder.create().show();
    }


    public void editItem(int pos) {
        //Edit Item
        Intent intent = new Intent(context, EditPostActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("id_post", posts_list.get(pos).getId());
        context.startActivity(intent);
    }

    public void showItem(int pos) {
        Intent intent = new Intent(context, ShowPostActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("id_post", posts_list.get(pos).getId());
        context.startActivity(intent);
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        TextView subtitle;
        ImageView imgDelete;
        ImageView imgEdit;
        CardView card_item;

        int position;
        Post current;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.post_title);
            subtitle = (TextView) itemView.findViewById(R.id.post_subtitle);
            imgDelete = (ImageView) itemView.findViewById(R.id.img_row_delete);
            imgEdit = (ImageView) itemView.findViewById(R.id.img_row_edit);
            card_item = (CardView) itemView.findViewById(R.id.card_item);
        }

        public void setData(Post current, int position) {
            String title = (current.getTitle().length() > 26) ? current.getTitle().substring(0, 25) + "..." : current.getTitle();
            String subtitle = (current.getSubtitle().length() > 31) ? current.getSubtitle().substring(0, 30) + "..." : current.getSubtitle();


            this.title.setText(title);
            this.subtitle.setText(subtitle);
            //this.imgThumb.setImageResource(current.getImageID());
            this.position = position;
            this.current = current;
        }

        public void setListeners() {

            imgDelete.setOnClickListener(MyViewHolder.this);
            imgEdit.setOnClickListener(MyViewHolder.this);
            card_item.setOnClickListener(MyViewHolder.this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.img_row_delete:
                    removeItem(position);
                    break;
                case R.id.img_row_edit:
                    editItem(position);
                    break;
                case R.id.card_item:
                    showItem(position);
                    break;
            }
        }
    }
}
