package com.messenger;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.messenger.database.model.ThreadEntity;
import com.messenger.ui.BindConversationListItem;
import com.messenger.ui.ConversationListItem;

import java.util.List;

/**
 *
 *
 * @author equals on 17.11.16.
 */
public class ConversationListAdapter extends RecyclerView.Adapter<ConversationListAdapter.ViewHolder> {

    private List<ThreadEntity> mThreads;
    private LayoutInflater mLayoutInflater;
    private ConversationClickListener mClickListener;

    static class ViewHolder extends RecyclerView.ViewHolder {

        ViewHolder(final @NonNull View itemView,
                   final @NonNull ConversationClickListener conversationClickListener) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    conversationClickListener.onConversationClick(((ConversationListItem)getView()).getThreadEntity());
                }
            });
        }
        public BindConversationListItem getView() {
            return (BindConversationListItem) itemView;
        }

    }

    ConversationListAdapter(@NonNull Context mContext,
                            @NonNull List<ThreadEntity> mThreads,
                            @NonNull ConversationClickListener mClickListener) {
        this.mLayoutInflater = LayoutInflater.from(mContext);
        this.mThreads = mThreads;
        this.mClickListener = mClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.conversation_list_item, parent, false);
        return new ViewHolder(view, mClickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.getView().bind(mThreads.get(position));
    }

    @Override
    public int getItemCount() {
        return mThreads.size();
    }

    public void setThreads(List<ThreadEntity> threads) {
        this.mThreads = threads;
        this.notifyDataSetChanged();
    }

    interface ConversationClickListener {
        void onConversationClick(ThreadEntity threadEntity);
    }
}
