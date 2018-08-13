package com.example.deeson.mycolormatrix;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;

import java.util.LinkedList;
import java.util.Stack;

/**
 * @author Deeson
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Bitmap bitmap;
    ImageView iv_photo;
    GridLayout matrixLayout;
    //每个edittext的宽高
    int mEtWidth;
    int mEtHeight;
    //保存20个edittext
    EditText[] mEts = new EditText[20];

    //一维数组保存20个矩阵值
    float[] mColorMatrix = new float[20];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.image);
        iv_photo = (ImageView) findViewById(R.id.iv_photo);
        matrixLayout = (GridLayout) findViewById(R.id.matrix_layout);
        Button btn_change = (Button) findViewById(R.id.btn_change);
        Button btn_reset = (Button) findViewById(R.id.btn_reset);
        btn_change.setOnClickListener(this);
        btn_reset.setOnClickListener(this);
        iv_photo.setImageBitmap(bitmap);

        //我们无法在onCreate()方法中获得视图的宽高值，所以通过View的post()方法，在视图创建完毕后获得其宽高值
        matrixLayout.post(new Runnable() {
            @Override
            public void run() {
                mEtWidth = matrixLayout.getWidth() / 5;
                mEtHeight = matrixLayout.getHeight() / 4;
                addEts();
                initMatrix();
            }

        });
    }

    //动态添加edittext
    private void addEts() {
        for (int i = 0; i < 20; i++) {
            EditText et = new EditText(this);
            et.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            mEts[i] = et;
            matrixLayout.addView(et, mEtWidth, mEtHeight);
        }
    }

    //初始化颜色矩阵
    private void initMatrix() {
        for (int i = 0; i < 20; i++) {
            if (i % 6 == 0) {
                mEts[i].setText(String.valueOf(1));
            } else {
                mEts[i].setText(String.valueOf(0));
            }
        }
    }


    //获取矩阵值
    private void getMatrix() {
        for (int i = 0; i < 20; i++) {
            String matrix = mEts[i].getText().toString();
            boolean isNone = null == matrix || "".equals(matrix);
            mColorMatrix[i] = isNone ? 0.0f : Float.valueOf(matrix);
            if (isNone) {
                mEts[i].setText("0");
            }
        }
    }

    //将矩阵设置到图像
    private void setImageMatrix() {
        Bitmap bmp = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.set(mColorMatrix);//将一维数组设置到ColorMatrix

        Canvas canvas = new Canvas(bmp);
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(bitmap, 0, 0, paint);
        iv_photo.setImageBitmap(bmp);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_change:
                break;
            case R.id.btn_reset:
                //重置矩阵效果
                initMatrix();
                break;
        }
        //作用矩阵效果
        getMatrix();
        setImageMatrix();

    }
}
