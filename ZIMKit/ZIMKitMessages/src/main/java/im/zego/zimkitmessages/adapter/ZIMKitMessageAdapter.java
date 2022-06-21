package im.zego.zimkitmessages.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import im.zego.zim.enums.ZIMMessageType;
import im.zego.zimkitcommon.adapter.BaseDifferRvAdapter;
import im.zego.zimkitmessages.BR;
import im.zego.zimkitmessages.R;
import im.zego.zimkitmessages.model.ZIMKitMessageItemModel;

public class ZIMKitMessageAdapter extends RecyclerView.Adapter<ZIMKitMessageAdapter.MessageItemViewHolder> {
    private final List<ZIMKitMessageItemModel> mList = new ArrayList<>();

    public List<ZIMKitMessageItemModel> getData() {
        return mList;
    }

    public void setNewList(List<ZIMKitMessageItemModel> list) {
        if (mList.size() == list.size()) {
            mList.clear();
            mList.addAll(list);
            this.notifyItemRangeChanged(0, list.size());
            return;
        }
        if (mList.size() > 0) {
            int count = mList.size();
            mList.clear();
            this.notifyItemRangeRemoved(0, count);
        }
        if (list.size() > 0) {
            mList.addAll(list);
            this.notifyItemRangeInserted(0, list.size());
        }
    }

    public void addListToTop(List<ZIMKitMessageItemModel> list) {
        mList.addAll(0, list);
        this.notifyItemRangeInserted(0, list.size());
    }

    public void addListToBottom(List<ZIMKitMessageItemModel> list) {
        int oldCount = mList.size();
        mList.addAll(list);
        this.notifyItemRangeInserted(oldCount, list.size());
    }

    @NonNull
    @Override
    public MessageItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewDataBinding binding = null;
        if (viewType == 99) { // temp error message type
            binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_message_system, parent, false);
        } else {
            //todo 需要优化为工厂模式，现在只有一种类型，就先这样吧.
            boolean isSend = (viewType / 100) == 0; // because send =0,RECEIVE = 1
            int type = (viewType % 100);
            if (isSend) {
                if (type == ZIMMessageType.TEXT.value()) {
                    binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_message_self, parent, false);
                }
            } else {
                if (type == ZIMMessageType.TEXT.value()) {
                    binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_message_other_side, parent, false);
                }
            }
        }
        return new MessageItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageItemViewHolder holder, int position) {
        holder.bind(BR.model, mList.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        int type = mList.get(position).getType();
        if (type == 99) { // temp error message type
            return type;
        } else {
            int direction = mList.get(position).getDirection().value();
            return (direction * 100) + type;
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MessageItemViewHolder extends RecyclerView.ViewHolder {
        private final ViewDataBinding mBinding;

        public MessageItemViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public void bind(int id, ZIMKitMessageItemModel model) {
            if (mBinding != null) {
                mBinding.setVariable(id, model);
                mBinding.executePendingBindings();
            }
        }
    }

}
