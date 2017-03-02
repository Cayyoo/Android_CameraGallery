package com.example.modifyavatar.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;

/**
 * 图片操作工具类:
 * 拍照和选择本地图片以及剪切这些主要功能都在PicturePickupTool类中。
 *
 * showDialog()方法用于打开一个包括“拍照”和“选择本地图片”选项的对话框，在其事件处理方法中使用的Intent对象分别设置了打开系统拍照和图库程序的Action，使用startActivityForResult方法能够打开程序界面，并且可以在完成拍照和图库图片选择后，获取图片作为返回值。
 * zoomPicture()方法使用了系统图片剪切程序的Action，使用startActivityForResult以获取剪切后的图片。
 * hasSdcard()方法用于判断手机是否安装了SDCard，因为图片文件需要保存在SDCard上。
 */
public class PicturePickupTool {
    //图片文件名
    public static final String IMAGE_FILE = "pic.jpg";
    //拍照请求码
    public static final int REQUEST_CODE_CAMERA = 0;
    //本地图库选择请求码
    public static final int REQUEST_CODE_GALLERY = 1;
    //剪切图片请求码
    public static final int REQUEST_CODE_CROP = 2;


    /**
     * 打开相机
     * @param context
     */
    public static void openCamera(Activity context) {
        //打开系统拍照程序
        Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(hasSdcard()){
            //定义拍照后文件保存的路径
            intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), IMAGE_FILE)));
        }
        context.startActivityForResult(intentCamera, REQUEST_CODE_CAMERA);
    }

    /**
     * 打开图库
     * @param context
     */
    public static void openAlbums(Activity context) {
        //打开系统图库程序
        Intent intentGallery = new Intent();
        intentGallery.setType("image/*");
        intentGallery.setAction(Intent.ACTION_PICK);
        context.startActivityForResult(intentGallery, REQUEST_CODE_GALLERY);
    }

    /**
     * 缩放和剪切
     * @param context
     * @param uri
     */
    public static void zoomPicture(Activity context,Uri uri){
        //使用剪切工具的Action
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是剪切的默认初始位置
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是剪切的默认结束位置
        intent.putExtra("outputX", 135);
        intent.putExtra("outputY", 135);
        // 指定返回图片文件
        intent.putExtra("return-data", true);
        context.startActivityForResult(intent, REQUEST_CODE_CROP);
    }

    /**
     * 判断是否安装了SDcard
     * @return
     */
    public static boolean hasSdcard(){
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

}
