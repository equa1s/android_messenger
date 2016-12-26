package com.messenger;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.messenger.database.model.MessageEntity;
import com.messenger.ui.BindableConversationItem;

import java.util.List;

/**
 * Simple message adapter.
 *
 * @author equals on 15.11.16.
 */
public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ViewHolder> {

    private static final int OUTGOING = MessageEntity.OUTGOING;
    private static final int INCOMING = MessageEntity.INCOMING;

    private List<MessageEntity> mMessages;
    private LayoutInflater layoutInflater;

    static class ViewHolder extends RecyclerView.ViewHolder {

        ViewHolder(final @NonNull View itemView) {
            super(itemView);
        }

        public BindableConversationItem getView() {
            return (BindableConversationItem) itemView;
        }
    }

    public ConversationAdapter(Context context, List<MessageEntity> messageEntities) {
        this.mMessages = messageEntities;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemViewType(int position) {
        if (mMessages.get(position).isIncoming())
            return INCOMING;

        return OUTGOING;
    }

    private @LayoutRes int getLayoutForViewType(int viewType) {
        switch (viewType) {
            case OUTGOING:
                return R.layout.conversation_activity_item_sent;
            case INCOMING:
                return R.layout.conversation_activity_item_received;
            default:
                throw new IllegalArgumentException("Unsupported item view type given to ConversationAdapter");
        }
    }

    @Override
    public ConversationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = layoutInflater.inflate(getLayoutForViewType(viewType), parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.getView().bind(mMessages.get(position));
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        holder.getView().unbind();
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    void setMessages(List<MessageEntity> messages) {
        this.mMessages = messages;
        this.notifyDataSetChanged();
    }
}
