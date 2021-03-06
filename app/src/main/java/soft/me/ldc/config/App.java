package soft.me.ldc.config;

import android.app.Application;

import com.tencent.bugly.Bugly;
import com.yanzhenjie.nohttp.InitializationConfig;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.OkHttpNetworkExecutor;

import soft.me.ldc.exceptionhandler.MyExceptionHandler;

/**
 * Created by ldc45 on 2018/1/13.
 */

public class App extends Application {
    InitializationConfig config = null;

    @Override
    public void onCreate() {
        super.onCreate();
        config = InitializationConfig.newBuilder(this)
                .readTimeout(40 * 1000)
                .connectionTimeout(30 * 1000)
                .networkExecutor(new OkHttpNetworkExecutor())//Okhttp网络层
                .retry(1)//提交次数
                .build();
        NoHttp.initialize(config);
       MyExceptionHandler.Self.INSTANCE.getHandler().SERVICE(getApplicationContext(), true);
        Bugly.init(getApplicationContext(), "27190edbd9", false);
    }
}
