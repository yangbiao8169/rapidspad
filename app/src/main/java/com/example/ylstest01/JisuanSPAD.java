package com.example.ylstest01;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

public class JisuanSPAD

{
    String modelName;
    double spadofPiexlArry[];


    public double jisuanAvgSpad(float[] input, String modelName){

        if(modelName == "Endive"){
            return 51.40615546828167 + input[0] * -0.16671733832973992 + input[1] * -0.2058164081591464 + input[2] * 0.08560114728761964;

        } else if (modelName=="Strawberry") {
            return 59.85837643773187 + input[0] * 0.29893186213088196 + input[1] * -0.7034802001961845 + input[2] * 0.041524884962784836;

        } else if (modelName=="Bok Choy") {
            return 54.78181891822804 + input[0] * -0.1259720723276784 + input[1] * -0.44000100723063096 + input[2] * 0.45765912381754986;

        } else if (modelName == "China Rose") {
            return 47.503193245362795 + input[0] * -0.2451169996472612 + input[1] * -0.1240363596232992 + input[2] * 0.12529387857342816;

        } else if (modelName=="Romaine Lettuce") {
            return 58.99087523233423 + input[0] * -0.09496113435188384 + input[1] * -0.39857984728100304 + input[2] * 0.2807990518895736;

        } else {//只能调用通用模型了
            return 45.0223020846045 + input[0] * -0.2069896302960306 + input[1] * -0.1544101982939297 + input[2] * 0.20361051970695251;
        }

    }
    public Bitmap jisuanSpadFenbu(Bitmap oldBitmap,String modelName,int yuzhiOfblack,int yuzhiOfGreen,int piexlnumofleave){
        int width=oldBitmap.getWidth();
        int height=oldBitmap.getHeight();
        Bitmap newBimap=Bitmap.createBitmap(width,height,oldBitmap.getConfig());
        int intArrayOfnewBitmap[]= new int[width*height];
        double[] doublesSpad=new double[piexlnumofleave];
        int contSpad=0;
        oldBitmap.getPixels(intArrayOfnewBitmap,0,width,0,0,width,height);
        for (int i=0;i<intArrayOfnewBitmap.length;i++){
            if (Color.blue(intArrayOfnewBitmap[i])<yuzhiOfblack &&
                    Color.red(intArrayOfnewBitmap[i])<yuzhiOfblack &&
                    Color.green(intArrayOfnewBitmap[i])<yuzhiOfblack){
                intArrayOfnewBitmap[i]=Color.rgb(0,0,0);
            }else if (Color.green(intArrayOfnewBitmap[i])-Color.blue(intArrayOfnewBitmap[i])>yuzhiOfGreen ){
                // 计算SPAD 并渲染成颜色
                float redofPiexl=Color.red(intArrayOfnewBitmap[i]);
                float greenofPiexl=Color.green(intArrayOfnewBitmap[i]);
                float blueofPiexl=Color.blue(intArrayOfnewBitmap[i]);
                float[] rgbofPiexl = new float[]{redofPiexl,greenofPiexl,blueofPiexl};
                double spadofPiexl=jisuanAvgSpad(rgbofPiexl,modelName);
                doublesSpad[contSpad]=spadofPiexl;
                contSpad++;
                //渲染颜色子像素
                intArrayOfnewBitmap[i]=Color.rgb(mapToRGB(spadofPiexl)[0],mapToRGB(spadofPiexl)[1],mapToRGB(spadofPiexl)[2]);
                //intArrayOfnewBitmap[i]=Color.rgb(0,255,0);

            }else {
                intArrayOfnewBitmap[i]=Color.rgb(255,255,255);

            }

        }
        newBimap.setPixels(intArrayOfnewBitmap,0,width,0,0,width,height);
        Log.e(" contSpad","总数"+contSpad);
        spadofPiexlArry=doublesSpad;
        return newBimap;

    }
    ///根据SPAD渲染颜色
    public  int[] mapToRGB(double value) {
        if (value <= 10) {
            return new int[]{0, 0, 255}; // 蓝色
        } else if (value >= 50) {
            return new int[]{255, 0, 0}; // 红色
        } else {
            // 在10到50之间通过线性插值计算红色和蓝色的分量
            int red = (int) ((value - 10) * (255 / 40.0)); // 数值越大，红色分量越高
            int blue = 255 - red; // 数值越小，蓝色分量越高

            // 返回一个包含RGB分量的整数数组
            return new int[]{red, 0, blue};
        }
    }
    //统计各种数
    public String staticSpad(){
        StaticSpad staticSpad= new StaticSpad();
        return (staticSpad.getAllparmSting(spadofPiexlArry));
    }

}