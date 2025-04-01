package com.example.ylstest01;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    public String modelSeletedSting = "Generic";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //学习这个网址即可：https://www.runoob.com/w3cnote/android-tutorial-alertdialog.html


        Button button = findViewById(R.id.buttonPicSelet);
        Button buttonModelSelect = findViewById(R.id.buttonModelSelect);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 按钮点击事件的处理逻辑
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, 100);
            }
        });
        buttonModelSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String[] fruits = new String[]{"Endive", "Strawberry", "Bok Choy", "China Rose", "Romaine Lettuce", "Generic"};
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setIcon(R.mipmap.ic_launcher)
                        .setTitle("Select the type of leaf you want to test, only one can be selected！")
                        .setSingleChoiceItems(fruits, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d("alertClick", "onClick: ");
                                // 直接使用 dialog 参数来关闭对话框
                                dialog.dismiss();
                                modelSeletedSting=fruits[which];
                                Toast.makeText(getApplicationContext(), "You've chosen" + fruits[which], Toast.LENGTH_SHORT).show();
                            }
                        }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 如果需要在这里处理确定按钮的逻辑
                                Toast.makeText(getApplicationContext(), "Looks like we're stuck with the generic model." , Toast.LENGTH_SHORT).show();

                            }
                        });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        long startTime = System.nanoTime();

        if (requestCode == 100 && resultCode == RESULT_OK) {
            Uri uriOfPic = data.getData();
            // 获取照片的存储路径
            String path = uriOfPic.getPath();

            //获取图像bitmap
            handleImage handleImage=new handleImage();
            Bitmap bitmapofPic= handleImage.getBitmapFromUri(this,uriOfPic);
            //临时显示结果
            Log.d("Bitmap Info", "Width: " + bitmapofPic.getWidth());
            Log.d("Bitmap Info", "Height: " + bitmapofPic.getHeight());
            Log.d("Bitmap Info", "Config: " + bitmapofPic.getConfig());
            ImageBlurHelper imageBlurHelper =new ImageBlurHelper();
            Bitmap blurBitmap=imageBlurHelper.fastBlur(bitmapofPic, 0.2F,4);//高斯模糊
            ImageView imageViewToShow = findViewById(R.id.imageView);
            //图像分割
            TextView textViewToShow = findViewById(R.id.textViewPath);
            TextView textViewShowRGB = findViewById(R.id.textViewAvreageRGB);
            int blackPointCount = 0;
            int leavesPointCount = 0;
            double leaveArea=0.0;
            blackPointCount = handleImage.getBlackAreaCount(blurBitmap,40);
            leavesPointCount=handleImage.getLeavesAreaCount(blurBitmap,25);
            leaveArea=handleImage.leavesAreaCal(blackPointCount,leavesPointCount);
            DecimalFormat format=new DecimalFormat("#.00");
            String stringOfArea=format.format(leaveArea);
            textViewToShow .setText("Area:"+stringOfArea+"cm²");
            //计算平均R、G、B
            float [] avrageRBGofLeave = handleImage.getAverageRGBofLeave();
            //计算平均SPAD
            JisuanSPAD jisuanSPAD= new JisuanSPAD();
            jisuanSPAD.modelName=modelSeletedSting;
            double avgSpad= jisuanSPAD.jisuanAvgSpad(avrageRBGofLeave, jisuanSPAD.modelName);
            textViewShowRGB.setText("Avreage SPAD:"+format.format(avgSpad));
            //显示叶绿素分布
            Bitmap bitmapShowResult=jisuanSPAD.jisuanSpadFenbu(blurBitmap,modelSeletedSting,40,25,leavesPointCount);
            imageViewToShow.setImageBitmap(bitmapShowResult);
            //保存分布值
            SaveImageHelper saveImageHelper =new SaveImageHelper();
            String  fileNameofPic= saveImageHelper.getFileNameFromUri(this,uriOfPic);
            fileNameofPic=fileNameofPic.substring(0,fileNameofPic.length()-4)+"chlorophyll distribution";
            String stringshowStatic=jisuanSPAD.staticSpad();
            textViewShowRGB.setText(stringshowStatic);
            saveImageHelper.saveImageToGallery(this,bitmapShowResult,fileNameofPic,stringshowStatic);

            long endTime = System.nanoTime();
            long duration = (endTime - startTime) / 1_000_000L;
            Log.d("TimeTest", "运行这段代码花费了 " + duration + " 毫秒");


        } else if (requestCode == 200 && resultCode == RESULT_OK) {
            //
            Log.d("按钮是：", "onActivityResult: modelselet");

        }

    }
}