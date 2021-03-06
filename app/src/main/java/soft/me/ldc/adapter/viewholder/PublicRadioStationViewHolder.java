package soft.me.ldc.adapter.viewholder;

import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import soft.me.ldc.R;
import soft.me.ldc.config.App;

/**
 * Created by liudi on 2018/1/15.
 */

public class PublicRadioStationViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.mIcon)
    public AppCompatImageView mIcon;
    @BindView(R.id.mName)
    public AppCompatTextView mName;
    @BindView(R.id.mCate_sname)
    public AppCompatTextView mCate_sname;

    public PublicRadioStationViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
