package soft.me.ldc.layout;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.OnClick;
import soft.me.ldc.R;
import soft.me.ldc.base.RootFragment;
import soft.me.ldc.model.PlayMusicSongBean;
import soft.me.ldc.service.PlayService;
import soft.me.ldc.utils.StringUtil;
import soft.me.ldc.view.GRToastView;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlayMusicCoverFragment extends RootFragment {
    volatile PlayService.ServiceBind playService = null;
    volatile PlayMusicSongBean mData = null;
    @BindView(R.id.song_icon)
    AppCompatImageView micon;
    @BindView(R.id.title)
    AppCompatTextView title;
    @BindView(R.id.author)
    AppCompatTextView author;
    @BindView(R.id.replay)
    AppCompatImageView replay;

    //构造函数
    public void pushData(PlayMusicSongBean mData) {
        this.mData = mData;
    }


    @Override
    protected void NewCreate(@Nullable Bundle savedInstanceState) throws Exception {
        PlayMusicActivity playMusicActivity = (PlayMusicActivity) act;
        playService = playMusicActivity.getPlayService();
    }

    @Override
    protected View UI(LayoutInflater inflater) throws Exception {
        View mainView = inflater.inflate(R.layout.fragment_play_music_cover, null, false);
        mainView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return mainView;
    }

    @Override
    protected void Init() throws Exception {
        if (StringUtil.isNotBlank(mData.songinfo.pic_premium))
            Picasso.with(ctx).load(mData.songinfo.pic_premium).resize(500, 500).centerCrop().onlyScaleDown().into(micon);
        if (StringUtil.isNotBlank(mData.songinfo.title))
            title.setText(mData.songinfo.title);
        if (StringUtil.isNotBlank(mData.songinfo.author))
            author.setText(mData.songinfo.author);

    }

    @Override
    protected void Main() throws Exception {
        if (playService.Player() != null) {
            if (playService.Player().isLooping()) {
                replay.setImageResource(R.drawable.ic_play_replay_pre);
            } else {
                replay.setImageResource(R.drawable.ic_play_replay);
            }
        }
    }

    @Override
    protected void Exception(Exception e) {
        GRToastView.show(ctx, "系统异常", Toast.LENGTH_SHORT);
    }

    @OnClick({R.id.replay})
    public void ClickListener(View view) {
        switch (view.getId()) {
            case R.id.replay:
                if (playService.Player() != null) {
                    if (playService.Player().isLooping()) {
                        playService.Looping(false);
                        replay.setImageResource(R.drawable.ic_play_replay);
                    } else {
                        playService.Looping(true);
                        replay.setImageResource(R.drawable.ic_play_replay_pre);
                    }
                }
                break;
        }
    }
}
