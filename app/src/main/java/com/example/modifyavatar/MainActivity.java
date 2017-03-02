package com.example.modifyavatar;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.modifyavatar.utils.CircleImageView;
import com.example.modifyavatar.utils.PicturePickupTool;

import java.io.File;

/**
 * 在MainActivity的onActivityResult方法中处理拍照、图片选择、图片剪切的结果：
 * 1、首先通过resultCode来判断操作不是取消状态，因为拍照等操作取消后是没有结果返回的，
 * 2、然后通过requestCode来判断进行何种操作，拍照操作和图片选择操作后图片都需要继续剪裁操作，
 * 3、剪裁操作后最后返回的图片数据在Intent参数中返回，在ImageView中显示出来。
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private View mViewHead;
    private CircleImageView mCivPhoto = null;

    private View mViewBottom;
    private Button mBtnPhotograph;
    private Button mBtnAlbums;
    private Button mBtnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        mViewHead=this.findViewById(R.id.view_Head);//头像布局
        mCivPhoto = (CircleImageView) findViewById(R.id.imgPhoto);//头像，暂时不需要设置点击事件
        mViewBottom=this.findViewById(R.id.view_bottom);//底部图片源选项卡
        mBtnPhotograph= (Button) this.findViewById(R.id.photograph);//打开相机
        mBtnAlbums= (Button) this.findViewById(R.id.albums);//打开图库
        mBtnCancel= (Button) this.findViewById(R.id.cancel);//取消，隐藏选项卡

        mViewHead.setOnClickListener(this);
        mViewBottom.setOnClickListener(this);
        mBtnPhotograph.setOnClickListener(this);
        mBtnAlbums.setOnClickListener(this);
        mBtnCancel.setOnClickListener(this);
    }

    /**
     * 显示头像选择对话框
     */
    public static void showDialog(final Activity context){
        new AlertDialog.Builder(context).setTitle("选择图片").setItems(new String[]{"拍照","从图库选取"},new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(which){
                    case 0:
                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.CAMERA}, 1);//1为参数，onRequestPermissionsResult()中回调
                        } else {
                            PicturePickupTool.openCamera(context);
                        }
                        break;
                    case 1:
                        PicturePickupTool.openAlbums(context);
                        break;
                }
            }

        }).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //显示自定义图片来源的选项卡
            case R.id.view_Head:
                mViewBottom.setVisibility(View.VISIBLE);
                //打开对话框选项卡
                showDialog(MainActivity.this);
                break;
            //打开相机拍照
            case R.id.photograph:
                //PicturePickupTool.openCamera(MainActivity.this);//6.0(API 23)以下版本只需要这一行代码

                //6.0以上版本，需要申请权限
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);//1为参数，onRequestPermissionsResult()中回调
                } else {
                    PicturePickupTool.openCamera(MainActivity.this);
                }

                break;
            //打开相簿
            case R.id.albums:
                PicturePickupTool.openAlbums(MainActivity.this);
                break;
            //隐藏自定义图片来源选项卡
            case R.id.cancel:
                mViewBottom.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //判断结果码不是取消操作
        if(resultCode != RESULT_CANCELED){
            switch(requestCode){
                //拍照图片
                case PicturePickupTool.REQUEST_CODE_CAMERA:
                    if(PicturePickupTool.hasSdcard()){
                        //拍照后处理，剪裁照片文件
                        File file = new File(Environment.getExternalStorageDirectory(),PicturePickupTool.IMAGE_FILE);
                        PicturePickupTool.zoomPicture(this, Uri.fromFile(file));
                    }else{
                        Toast.makeText(this, "找不到SDCard!", Toast.LENGTH_LONG).show();
                    }
                    break;
                //相簿图片
                case PicturePickupTool.REQUEST_CODE_GALLERY:
                    //本地图片选择后，剪裁图片
                    PicturePickupTool.zoomPicture(this, data.getData());
                    break;
                //修剪图片
                case PicturePickupTool.REQUEST_CODE_CROP:
                    //剪裁图片后,显示图片
                    if(data.getExtras()!=null){
                        //显示头像
                        Bitmap face = data.getExtras().getParcelable("data");
                        mCivPhoto.setImageDrawable(new BitmapDrawable(null,face));
                    }
                    break;
            }
        }
    }

    /**
     * 相机权限的回调方法
     *
     * 6.0以上版本需要添加此回调方法，否则不需要
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                PicturePickupTool.openCamera(MainActivity.this);
            } else {
                // Permission Denied
                Toast.makeText(MainActivity.this, "您未授权", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
