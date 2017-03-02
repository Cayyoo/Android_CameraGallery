# 修改头像

```java
/**
 * 在MainActivity的onActivityResult方法中处理拍照、图片选择、图片剪切的结果：
 * 1、首先通过resultCode来判断操作不是取消状态，因为拍照等操作取消后是没有结果返回的，
 * 2、然后通过requestCode来判断进行何种操作，拍照操作和图片选择操作后图片都需要继续剪裁操作，
 * 3、剪裁操作后最后返回的图片数据在Intent参数中返回，在ImageView中显示出来。
 */
 
 /**
 * 图片来源选取工具类:
 * 拍照和选择本地图片以及剪切这些主要功能都在PicturePickupTool类中。
 *
 * showDialog()方法用于打开一个包括“拍照”和“选择本地图片”选项的对话框，在其事件处理方法中使用的Intent对象分别设置了打开系统拍照和图库程序的Action，使用startActivityForResult方法能够打开程序界面，并且可以在完成拍照和图库图片选择后，获取图片作为返回值。
 * zoomPicture()方法使用了系统图片剪切程序的Action，使用startActivityForResult以获取剪切后的图片。
 * hasSdcard()方法用于判断手机是否安装了SDCard，因为图片文件需要保存在SDCard上。
 */
 
 /**
 * 圆形图片工具类：
 * 流程控制的比较严谨，比如setup函数的使用
 * updateShaderMatrix保证图片损失度最小和始终绘制图片正中央的那部分
 * 作者思路是画圆用渲染器位图填充，而不是把Bitmap重绘切割成一个圆形图片。
 */
 ```
 
![img](https://github.com/ykmeory/ModifyAvatar/blob/master/img.jpg "screenshot")
