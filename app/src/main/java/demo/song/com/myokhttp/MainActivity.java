package demo.song.com.myokhttp;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;

import okhttp3.Call;

import static android.R.string.ok;

public class MainActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    //请添加依赖   compile 'com.zhy:okhttputils:2.6.2'
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = (ProgressBar) findViewById(R.id.progress);

        okGet();
    }

    private void okGet() {

        OkHttpUtils.get()
                //网址
                .url("http://huixinguiyu.cn/Assets/js/newsnew.js")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }
                    @Override
                    public void onResponse(String response, int id) {
                        //直接获取字符串,封装过了,   字节转字符  子线程也都不需要再进行写了
                        String string = response.toString();
                        //吐司提示 成功
                        Toast.makeText(MainActivity.this,string,Toast.LENGTH_SHORT).show();
                    }
                });

    }
    public void bbb(View v){
        //让进度条显示
        progressBar.setVisibility(View.VISIBLE);
        OkHttpUtils
                .get()
                .url("http://p.gdown.baidu.com/e96035c05cfbd104bc614338d32e4998cf247dcb6b1d2f55352c28c485a21dc66ba8b52319123d749562b790fc47aaaee5da5908708263ce0b1692be82a460934eed49f570b9c59411293bb6bbc5eaa6b5d33a12fa8942809ab5006d401340245313c56321cf811b955d8594032edf172207325d3d4fe11fd2a043066ab78e7def5069cf551eefddc1250aa4f4faf971cddb65081a3004655f51e0adfa56339673d241fd87d214181c62654fcf4ec14746c495a66bbafe057a887ec1a8741de6fec48d8ed0e75ab7e22a9538a70aeb0d590439066f50f5505137d99b62a87fa5")
                .build()
                //         FileCallBack    1.获得SD卡的绝对路径   2. 文件名,如果是想安装的话,只能安装.apk文件,所以拓展名必须是.apk
                .execute(new FileCallBack(Environment.getExternalStorageDirectory().getAbsolutePath(),"shf.apk") {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(MainActivity.this,e+"",Toast.LENGTH_LONG).show();
                    }
                    //这个方法自己手动调用一下    进度条
                    @Override
                    public void inProgress(float progress, long total, int id) {
                        //给进度条设置总进度
                        progressBar.setProgress((int)(100*progress));
                    }

                    @Override
                    public void onResponse(File response, int id) {
                        //得到的是  本地文件路径以及名称
                        String string = response.toString();
                        Toast.makeText(MainActivity.this,string,Toast.LENGTH_LONG).show();
                        //判断进度条到100%时,跳转到系统的安装界面
                        if (progressBar.getProgress()==progressBar.getMax()){
                            Toast.makeText(MainActivity.this,"ok",Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setDataAndType(Uri.fromFile(response), "application/vnd.android.package-archive");
                            startActivity(intent);

                        }
                    }

                });
    }
}
