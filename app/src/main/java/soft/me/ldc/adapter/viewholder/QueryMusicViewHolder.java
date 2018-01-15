package soft.me.ldc.adapter.viewholder;

import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import soft.me.ldc.R;

/**
 * Created by liudi on 2018/1/15.
 */

public class QueryMusicViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.mIcon)
    public AppCompatImageView mIcon;
    @BindView(R.id.mTitle)
    public AppCompatTextView mTitle;
    @BindView(R.id.mAuthor)
    public AppCompatTextView mAuthor;
    @BindView(R.id.mCountry)
    public AppCompatTextView mCountry;
    @BindView(R.id.mCompany)
    public AppCompatTextView mCompany;
    @BindView(R.id.mAlbum)
    public AppCompatTextView mAlbum;

    public QueryMusicViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
