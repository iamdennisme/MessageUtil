package com.smscapture.smscapture.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.smscapture.smscapture.R;
import com.smscapture.smscapture.entity.MMSContentInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${dennis} on 6/21/16.
 */
public class ContentAdapter extends RecyclerView.Adapter {
    public static enum TYPE {
        IMAGE,
        TEXT,
        ERR
    }

    private Context mContext;
    private List<MMSContentInfo> data = new ArrayList<>();

    public ContentAdapter(Context context, List<MMSContentInfo> data) {
        this.data = data;
        this.mContext = context;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE.TEXT.ordinal()) {
            return new TextViewHolder(layoutInflater.inflate(R.layout.item_recycler_text, parent, false));
        } else if (viewType == TYPE.IMAGE.ordinal()) {
            return new ImageViewHolder(layoutInflater.inflate(R.layout.item_recycler_image, parent, false));
        } else {
            return new ErrViewHolder(layoutInflater.inflate(R.layout.item_recycler_err, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TextViewHolder) {
            bindTextViewHolder(holder, position);
        }
        if (holder instanceof ImageViewHolder) {
            bindImageViewHolder(holder, position);
        }

    }

    private void bindTextViewHolder(RecyclerView.ViewHolder holder, int position) {
        TextViewHolder textViewHolder = (TextViewHolder) holder;
        MMSContentInfo contentInfo = getItem(position);
        textViewHolder.tvText.setText(contentInfo.getMessage());

    }

    private void bindImageViewHolder(RecyclerView.ViewHolder holder, int position) {
        ImageViewHolder imageViewHolder = (ImageViewHolder) holder;
        MMSContentInfo contentInfo = getItem(position);
        imageViewHolder.ivImage.setImageBitmap(contentInfo.getBitmap());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position).getType() == 1) {
            return TYPE.TEXT.ordinal();
        } else if (getItem(position).getType() == 0) {
            return TYPE.IMAGE.ordinal();
        } else {
            return TYPE.ERR.ordinal();
        }
    }

    public MMSContentInfo getItem(int position) {
        return data.get(position);
    }

    public class TextViewHolder extends RecyclerView.ViewHolder {
        public TextView tvText;

        public TextViewHolder(View itemView) {
            super(itemView);
            tvText = (TextView) itemView.findViewById(R.id.tv_content);
        }
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivImage;

        public ImageViewHolder(View itemView) {
            super(itemView);
            ivImage = (ImageView) itemView.findViewById(R.id.iv_image);
        }
    }

    public class ErrViewHolder extends RecyclerView.ViewHolder {
        public ErrViewHolder(View itemView) {
            super(itemView);
        }
    }
}
