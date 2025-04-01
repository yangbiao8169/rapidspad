package com.example.ylstest01;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class handleImage {
    handleImage(){
        Log.i("Class handleImage ","created!");
    }
    public float[] averageRGBofLeave ={0,0,0};   //定义叶片区域的平均RGB值
    public int[] sumRGBofLeave ={0,0,0};   //定义叶片区域RGB值的和
    public int getBlackAreaCount(Bitmap bitmap,int yuZhi ){
        int width=bitmap.getWidth();
        int height=bitmap.getHeight();
        int intArrayOfBitMap[]= new int[width*height];
        Log.d("allPointCount",""+intArrayOfBitMap.length);
        int blackPointCount=0;
        bitmap.getPixels(intArrayOfBitMap,0,width,0,0,width,height);
        for (int i=0;i<intArrayOfBitMap.length;i++){
            if (Color.red(intArrayOfBitMap[i])<yuZhi &&
                    Color.blue(intArrayOfBitMap[i])<yuZhi && Color.green(intArrayOfBitMap[i])<yuZhi) blackPointCount++;
        }
        Log.d("greenPointCount",""+blackPointCount);
        return blackPointCount;

    }
    public int getLeavesAreaCount(Bitmap bitmap,int yuZhi){
        int width=bitmap.getWidth();
        int height=bitmap.getHeight();
        int intArrayOfBitMap[]= new int[width*height];
        int greenPointCount=0;
        bitmap.getPixels(intArrayOfBitMap,0,width,0,0,width,height);
        for (int i=0;i<intArrayOfBitMap.length;i++){
            if (Color.green(intArrayOfBitMap[i])-Color.blue(intArrayOfBitMap[i])>yuZhi )
            {greenPointCount++;
                sumRGBofLeave [0]+=Color.red(intArrayOfBitMap[i]); //求叶片区域红通道的和
                sumRGBofLeave [1]+=Color.green(intArrayOfBitMap[i]);
                sumRGBofLeave [2]+=Color.blue(intArrayOfBitMap[i]);
                //Log.d("leaveRGB",""+Color.red(intArrayOfBitMap[i])+"+"+ Color.green(intArrayOfBitMap[i])+"+"+Color.blue(intArrayOfBitMap[i]));

            }
        }
        averageRGBofLeave [0]=(float) sumRGBofLeave[0]/(float) greenPointCount;
        averageRGBofLeave [1]=(float) sumRGBofLeave[1]/(float) greenPointCount;
        averageRGBofLeave [2]=(float) sumRGBofLeave[2]/(float) greenPointCount;
        Log.d("averageRGBofLeave",""+averageRGBofLeave [0]);
        Log.d("averageRGBofLeave",""+averageRGBofLeave [1]);
        Log.d("averageRGBofLeave",""+averageRGBofLeave [2]);

        Log.d("greenPointCount",""+greenPointCount);

        return greenPointCount;
    }
    public double leavesAreaCal(int numOfBlakPoint, int numOfGreenPont){
        double leavesArea=0;
        leavesArea=((double) numOfGreenPont/(double) numOfBlakPoint)*9.0;//计算面积
        return leavesArea;
    }
    public Bitmap leaveAndBlackShowHandle(Bitmap oldBitmap,int yuzhiOfblack,int yuzhiOfGreen){
        int width=oldBitmap.getWidth();
        int height=oldBitmap.getHeight();
        Bitmap newBimap=Bitmap.createBitmap(width,height,oldBitmap.getConfig());
        int intArrayOfnewBitmap[]= new int[width*height];
        oldBitmap.getPixels(intArrayOfnewBitmap,0,width,0,0,width,height);
        for (int i=0;i<intArrayOfnewBitmap.length;i++){
            if (Color.blue(intArrayOfnewBitmap[i])<yuzhiOfblack &&
                    Color.red(intArrayOfnewBitmap[i])<yuzhiOfblack &&
                    Color.green(intArrayOfnewBitmap[i])<yuzhiOfblack){
                intArrayOfnewBitmap[i]=Color.rgb(0,0,0);
            }else if (Color.green(intArrayOfnewBitmap[i])-Color.blue(intArrayOfnewBitmap[i])>yuzhiOfGreen ){
                intArrayOfnewBitmap[i]=Color.rgb(0,255,0);
            }else {
                intArrayOfnewBitmap[i]=Color.rgb(255,255,255);

            }

        }
        newBimap.setPixels(intArrayOfnewBitmap,0,width,0,0,width,height);
        return newBimap;

    }

    //从URi读取bitmap
    public Bitmap getBitmapFromUri(Context context, Uri uri) {
        try {
            InputStream input = context.getContentResolver().openInputStream(uri);
            if (input != null) {
                return BitmapFactory.decodeStream(input);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    public float [] getAverageRGBofLeave(){
        return averageRGBofLeave;
    }





}
