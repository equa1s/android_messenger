package com.messenger;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.messenger.database.model.MessageEntity;

import java.util.List;

/**
 * @author equals on 15.11.16.
 * TODO: add insert new message & redraw adapter
 */
public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ViewHolder> {

    private List<MessageEntity> messageEntities;
    private LayoutInflater layoutInflater;

    static class ViewHolder extends RecyclerView.ViewHolder {

        ViewHolder(final @NonNull View itemView) {
            super(itemView);
        }
        public BindConversationItem getView() {
            return (BindConversationItem) itemView;
        }
    }

    public ConversationAdapter(Context context, List<MessageEntity> messageEntities) {
        this.messageEntities = messageEntities;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemViewType(int position) {
        if (messageEntities.get(position).isIncoming())
            return MessageEntity.INCOMING;

        return MessageEntity.OUTGOING;
    }

    private @LayoutRes int getLayoutForViewType(int viewType) {
        switch (viewType) {
            case MessageEntity.OUTGOING:
                return R.layout.conversation_activity_item_sent;
            case MessageEntity.INCOMING:
                return R.layout.conversation_activity_item_received;
            default:
                throw new IllegalArgumentException("unsupported item view type given to ConversationAdapter");
        }
    }

    @Override
    public ConversationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = layoutInflater.inflate(getLayoutForViewType(viewType), parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // TODO: bind message to view holder
        holder.getView().bind(messageEntities.get(position));
    }

    @Override
    public int getItemCount() {
        return messageEntities.size();
    }
}
