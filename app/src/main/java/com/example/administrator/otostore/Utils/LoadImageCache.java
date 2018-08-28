package com.example.administrator.otostore.Utils;

/**
 * Created by Administrator on 2018/5/30.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoadImageCache {

    private Context mContext;
    //网络地址
    private String mImageUrl;
    //磁盘存储文件名
    private String mFileName;
    //内存缓存
    private LruCache<String, Bitmap> mLruCache;
    //磁盘目录(sd卡的私有cache目录)
    private String mFilePath;


    public LoadImageCache(Context context, String url) {
        this.mContext = context;
        this.mImageUrl = url;
        initLruCache();
        initFileCache();
    }


    public void loadBitmap(final ImageView imageView) {
        //现在内存中查找
        Bitmap bitmap = mLruCache.get(mImageUrl);
        if (bitmap != null) {
            Log.i("tag", "thumbnail from LruCache ");
            imageView.setImageBitmap(bitmap);
            return;
        } else {
            //去磁盘查找
            byte[] bytes = loadFromSD();
            if (bytes != null) {
                //保存缩略图到cache
                saveThumbnailToLruCache(bytes, 500, 300);
                bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                Log.i("tag", "bitmap from sdcard ");
                imageView.setImageBitmap(bitmap);
                return;
            } else {

                //网络加载 使用OKHTTP异步实现
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(mImageUrl).build();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        byte[] fromNetWork = response.body().bytes();
                        //保存缩略图到cache
                        saveThumbnailToLruCache(fromNetWork, 500, 300);
                        //保存到sdcard
                        saveImageToSD(fromNetWork);
                        final Bitmap bitmap1 = BitmapFactory.decodeByteArray(fromNetWork, 0, fromNetWork.length);
                        Log.i("tag", "bitmap from network ");
                        ((Activity) mContext).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                imageView.setImageBitmap(bitmap1);
                            }
                        });

                    }
                });
            }

        }
    }


    //初始化磁盘缓存目录
    private void initFileCache() {
        //获取文件名称
        mFileName = mImageUrl.substring(mImageUrl.lastIndexOf("/") + 1);
        //判断sd卡是否挂载
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //私有缓存目录
            mFilePath = mContext.getExternalCacheDir().getAbsolutePath();
        } else {
            Log.i("tag", "sdcard is error! ");
        }

    }


    //初始化内存缓存
    private void initLruCache() {

        if (mLruCache == null) {
            //获取运行时内存总大小
            long maxMemory = Runtime.getRuntime().maxMemory();
            //一般设置图片缓存为手机内存的1/8
            mLruCache = new LruCache<String, Bitmap>((int) (maxMemory / 8)) {
                //用来衡量每张图片的大小，默认返回图片的数量
                @Override
                protected int sizeOf(String key, Bitmap value) {
                    //图片默认是ARGB_8888格式，每个像素占4个字节
//                    int values = value.getWidth() * value.getHeight() * 4;
                    int values = value.getRowBytes() * value.getHeight();
                    return values;
                }

                //结合软引用使用时，会配合这个方法，现在已经基本不用软引用，不用考虑
                @Override
                protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
                    super.entryRemoved(evicted, key, oldValue, newValue);
                }
            };
        }


    }

    //保存图片到磁盘
    private void saveImageToSD(byte[] image) {
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(new File(mFilePath, mFileName)));
            bos.write(image, 0, image.length);
            bos.flush();
            bos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //关流
            try {
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Log.i("tag", "bitmap to sdcard ");

    }

    //磁盘查找
    private byte[] loadFromSD() {
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        //内存流
        ByteArrayOutputStream baos = null;
        try {
            fis = new FileInputStream(new File(mFilePath, mFileName));
            bis = new BufferedInputStream(fis);
            baos = new ByteArrayOutputStream();

            byte[] bys = new byte[1024 * 8];
            int length = 0;
            while ((length = bis.read(bys)) != -1) {
                baos.write(bys, 0, length);
                baos.flush();
            }
            byte[] bytes = baos.toByteArray();
            return bytes;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            //关流
            try {
                if (baos != null) {
                    baos.close();
                }
                if (bis != null) {
                    bis.close();
                }
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.i("tag", "bitmap from sdcard ");
        }
    }

    //保存图片的缩略图到内存缓存,（二次采样技术）
    private void saveThumbnailToLruCache(byte[] image, int thumbWidth, int thumbHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        //只采图片的边界
        options.inJustDecodeBounds = true;
        //获取原图的高度和宽度
        BitmapFactory.decodeByteArray(image, 0, image.length, options);
        int width = options.outWidth;
        int height = options.outHeight;

        //计算缩略图宽高与原图宽高比例，取较大值作为最终缩放比例
        int size0 = (int) (thumbWidth / (float) width);
        int size1 = (int) (thumbHeight / (float) height);
        int size = size0 > size1 ? size0 : size1;


        options.inSampleSize = size;
        //设置图片格式
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        //采图片全部
        options.inJustDecodeBounds = false;

        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length, options);
        mLruCache.put(mImageUrl, bitmap);
        Log.i("tag", "thumbnail to cache ");
    }

}