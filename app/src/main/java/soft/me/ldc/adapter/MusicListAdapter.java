package soft.me.ldc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.List;

import soft.me.ldc.R;
import soft.me.ldc.adapter.viewholder.MusicListViewHolder;
import soft.me.ldc.base.RootRecyclerViewAdapter;
import soft.me.ldc.model.MusicListBean;
import soft.me.ldc.utils.StringUtil;

/**
 * Created by ldc45 on 2018/1/13.
 */

public class MusicListAdapter extends RootRecyclerViewAdapter<MusicListViewHolder> implements View.OnClickListener {
    List<MusicListBean.SongListBean> mData = null;
    OnItemListener listener = null;
    Context ctx = null;

    public void pushData(List<MusicListBean.SongListBean> mData) {
        if (this.mData == null) {
            this.mData = mData;
        } else {
            if (this.mData.containsAll(mData)) {
                this.mData.addAll(mData);
            }
        }
    }

    @Override
    public MusicListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.ctx = parent.getContext();
        View mainView = LayoutInflater.from(parent.getContext()).inflate(R.layout.music_list_list_item, parent, false);
        mainView.setOnClickListener(this);
        return new MusicListViewHolder(mainView);
    }

    @Override
    public void onBindViewHolder(MusicListViewHolder holder, int position) {
        MusicListBean.SongListBean data = mData.get(position);
        holder.itemView.setTag(data);
        setAnimator(holder.itemView);
        holder.mTitle.setText("" + data.title);
        holder.mAuthor.setText("" + data.author);
        if (StringUtil.isNotBlank(data.pic_big))
            Picasso.with(ctx).load(data.pic_big).resize(300, 300).centerInside().into(holder.mIcon);

    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public void onClick(View v) {
        if (this.listener != null) {
            this.listener.onItem(v, (MusicListBean.SongListBean) v.getTag());
        }
    }

    //接口
    public interface OnItemListener {
        void onItem(View view, MusicListBean.SongListBean type);
    }

    //暴露接口
    public void setListener(OnItemListener listener) {
        this.listener = listener;
    }
}
