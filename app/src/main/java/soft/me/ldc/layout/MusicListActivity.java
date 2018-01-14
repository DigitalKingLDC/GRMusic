package soft.me.ldc.layout;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.JsonObjectRequest;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.SimpleResponseListener;

import org.json.JSONObject;

import butterknife.BindView;
import soft.me.ldc.R;
import soft.me.ldc.adapter.MusicListAdapter;
import soft.me.ldc.base.RootActivity;
import soft.me.ldc.config.AppConfig;
import soft.me.ldc.http.nohttpTool.noHttpQueue;
import soft.me.ldc.model.Music;
import soft.me.ldc.model.MusicType;
import soft.me.ldc.service.MusicService;
import soft.me.ldc.view.GRLoadDialog;
import soft.me.ldc.view.GRToastView;
import soft.me.ldc.view.GRToolbar;

public class MusicListActivity extends RootActivity {


    @BindView(R.id.mToolbar)
    GRToolbar mToolbar;
    @BindView(R.id.smartrefreshlayout)
    SmartRefreshLayout smartrefreshlayout;
    @BindView(R.id.mList)
    RecyclerView mList;

    MusicType musicType = null;
    //分页
    int pageNo = 0;
    //消息
    Message msg = null;
    //持久任务
    RefreshTask refreshTask = null;
    LoadmoreTask loadmoreTask = null;
    //数据
    Music mData = null;
    Gson gson = null;
    //
    LinearLayoutManager llm = null;
    MusicListAdapter musicListAdapter = null;
    //等待对话框
    private GRLoadDialog loadDialog = null;

    static final int REFRESHCODE = 0x000;//下拉刷新
    static final int LOADMORECODE = 0x001;//上拉刷新
    static final int UPDATEDATACODE = 0x002;//更新数据
    static final int NODATACODE = 0x003;//错误
    static final int ERRORCODE = 0x004;//错误
    private Handler dkhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REFRESHCODE:
                    RefreshTaskRun();
                    break;
                case LOADMORECODE:
                    LoadmoreTaskRun();
                    break;
                case UPDATEDATACODE:
                    mData = (Music) msg.obj;
                    if (mData != null && mData.song_list.size() > 0) {
                        if (musicListAdapter != null) {
                            musicListAdapter.pushData(mData.song_list);
                            musicListAdapter.notifyDataSetChanged();
                        }
                    } else {
                        dkhandler.sendEmptyMessage(ERRORCODE);
                    }

                    break;
                case NODATACODE:
                    GRToastView.show(ctx, "没有数据!", Toast.LENGTH_SHORT);
                    break;
                case ERRORCODE:
                    GRToastView.show(ctx, "加载错误!", Toast.LENGTH_SHORT);
                    break;

            }
        }
    };


    @Override
    protected void NewCreate(@Nullable Bundle savedInstanceState) {
        musicType = (MusicType) getIntent().getSerializableExtra("type");
    }

    @Override
    protected Integer UI() {
        return R.layout.activity_music;
    }


    @Override
    protected void Main() {
        {
            mToolbar.setLeftText("返回");
            mToolbar.setTitleText("" + musicType.typeName);
            mToolbar.setColorRes(R.color.colorPrimary);
            mToolbar.setLeftBtnListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            setSupportActionBar(mToolbar);
        }

        if (gson == null)
            gson = new Gson();
        //刷新监听
        smartrefreshlayout.setOnRefreshListener(new RefreshListener());
        smartrefreshlayout.setOnLoadmoreListener(new RefreshListener());

        if (llm == null)
            llm = new LinearLayoutManager(ctx, LinearLayoutManager.VERTICAL, false);
        if (mData == null)
            mData = new Music();
        if (musicListAdapter == null)
            musicListAdapter = new MusicListAdapter();
        musicListAdapter.pushData(mData.song_list);
        musicListAdapter.setListener(new ItemListener());
        mList.setLayoutFrozen(true);
        mList.setHasFixedSize(true);
        mList.setLayoutManager(llm);
        mList.setAdapter(musicListAdapter);
        //加载任务
        dkhandler.sendEmptyMessage(REFRESHCODE);


    }


    @Override
    protected void Error(Exception e) {
        GRToastView.show(ctx, "系统异常", Toast.LENGTH_SHORT);
    }

    // TODO: 2018/1/13  刷新事件
    class RefreshListener implements OnRefreshListener, OnLoadmoreListener {


        @Override
        public void onLoadmore(RefreshLayout refreshlayout) {
            msg = new Message();
            msg.what = LOADMORECODE;
            dkhandler.sendMessage(msg);
            refreshlayout.finishLoadmore(2000);

        }

        @Override
        public void onRefresh(RefreshLayout refreshlayout) {
            msg = new Message();
            msg.what = REFRESHCODE;
            dkhandler.sendMessage(msg);
            refreshlayout.finishRefresh(2000);
        }
    }

    class ItemListener implements MusicListAdapter.OnItemListener {


        @Override
        public void onItem(View view, Music type) {
            GRToastView.show(ctx, type.billboard.name, Toast.LENGTH_SHORT);
        }
    }

    // TODO: 2018/1/13 执行下拉刷新任务
    private void RefreshTaskRun() {
        if (refreshTask != null && !refreshTask.isCancelled()) {
            refreshTask.cancel(true);
        }
        refreshTask = new RefreshTask();
        refreshTask.execute();
    }

    // TODO: 2018/1/13 执行上拉刷新任务
    private void LoadmoreTaskRun() {
        if (loadmoreTask != null && !loadmoreTask.isCancelled()) {
            loadmoreTask.cancel(true);
        }
        loadmoreTask = new LoadmoreTask();
        loadmoreTask.execute();
    }


    //下拉刷新
    class RefreshTask extends AsyncTask<Void, Void, Music> {
        @Override
        protected void onPreExecute() {
            if (loadDialog != null && loadDialog.isShow())
                loadDialog.dismiss();
            loadDialog = GRLoadDialog.Instance(ctx, GRLoadDialog.Style.Blue).show("数据加载中···", true);
        }

        @Override
        protected Music doInBackground(Void... voids) {
            Music result = null;
            try {
                pageNo = 0;
                String str = MusicService.Instance(ctx).MusicList(musicType.typeCode, 20 + "", pageNo + "");
                if (gson == null)
                    gson = new Gson();
                result = gson.fromJson(str, Music.class);
            } catch (Exception e) {
                dkhandler.sendEmptyMessage(ERRORCODE);
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(Music result) {
            msg = new Message();
            if (result != null) {
                msg.what = UPDATEDATACODE;
                msg.obj = result;
            } else {
                msg.what = NODATACODE;
            }
            loadDialog.dismiss();
            dkhandler.sendMessage(msg);
        }
    }

    //上拉刷新
    class LoadmoreTask extends AsyncTask<Void, Void, Music> {
        @Override
        protected void onPreExecute() {
            if (loadDialog != null && loadDialog.isShow())
                loadDialog.dismiss();
            loadDialog = GRLoadDialog.Instance(ctx, GRLoadDialog.Style.Blue).show("数据加载中···", true);
        }

        @Override
        protected Music doInBackground(Void... voids) {
            Music result = null;
            try {
                pageNo++;
                String str = MusicService.Instance(ctx).MusicList(musicType.typeCode, 20 + "", pageNo + "");
                if (gson == null)
                    gson = new Gson();
                result = gson.fromJson(str, Music.class);
            } catch (Exception e) {
                dkhandler.sendEmptyMessage(ERRORCODE);
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(Music result) {
            msg = new Message();
            if (result != null) {
                msg.what = UPDATEDATACODE;
                msg.obj = result;
            } else {
                msg.what = NODATACODE;
            }
            loadDialog.dismiss();
            dkhandler.sendMessage(msg);
        }
    }

}
