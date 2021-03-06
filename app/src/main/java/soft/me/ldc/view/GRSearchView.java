package soft.me.ldc.view;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import soft.me.ldc.R;
import soft.me.ldc.utils.StringUtil;

/**
 * Created by liudi on 2018/1/12.
 */

public class GRSearchView extends LinearLayoutCompat implements View.OnClickListener {
    View mainView = null;
    AppCompatEditText searchEt = null;
    AppCompatImageButton searchBtn = null;
    LinearLayoutCompat.LayoutParams layoutParams = null;
    onSearchListener listener = null;


    public GRSearchView(Context context) {
        super(context, null);
    }

    public GRSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public GRSearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs);
    }

    // TODO: 2018/1/12 初始化
    private void init(Context context, AttributeSet attrs) {
        mainView = LayoutInflater.from(context).inflate(R.layout.gr_searchview, null);
        searchBtn = mainView.findViewById(R.id.searchBtn);
        searchEt = mainView.findViewById(R.id.searchEt);
        if (searchBtn != null)
            searchBtn.setOnClickListener(this);
        //布局
        if (layoutParams == null)
            layoutParams = new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //添加视图
        addView(mainView, layoutParams);
    }

    // TODO: 2018/1/12 接口
    public interface onSearchListener {
        void onSearchClick(View view, String key);
    }


    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onSearchClick(v, searchEt != null ? searchEt.getText().toString() : "");
        }
    }


    // TODO: 2018/1/12 暴露接口
    public void setSearchBtnListener(onSearchListener listener) {
        this.listener = listener;
    }

    public String getKey() {
        if (searchEt == null)
            return null;
        else
            return searchEt.getText().toString();
    }

    public void setKey(String txt) {
        if (searchEt != null) {
            if (StringUtil.isNotBlank(txt)) {
                searchEt.setText(txt);
            } else {
                searchEt.setText(null);
            }
        }

    }

    public void setHint(String txt) {
        if (searchEt != null) {
            if (StringUtil.isNotBlank(txt)) {
                searchEt.setHint(txt);
            } else {
                searchEt.setHint(null);
            }
        }

    }


    public void setHintColor(int color) {
        if (searchEt != null) {
            searchEt.setHintTextColor(color);

        }

    }

    public void setHintColor(String color) {
        if (searchEt != null) {
            if (StringUtil.isNotBlank(color)) {
                searchEt.setHintTextColor(Color.parseColor(color));
            }
        }

    }

    public void setTextSize(float size) {
        if (searchEt != null) {
            if (size < 11) {
                searchEt.setTextSize(11);
            } else {
                searchEt.setTextSize(size);
            }
        }

    }

    public void setTextSize(int unit, float size) {
        if (searchEt != null) {
            if (size < 11) {
                searchEt.setTextSize(unit, 11);
            } else {
                searchEt.setTextSize(unit, size);
            }
        }

    }

}
