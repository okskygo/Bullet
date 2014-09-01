package shot.semen.bullet;

import java.util.ArrayList;

import shot.semen.bullet.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.SurfaceView;

public class SemenBulletLevelFour {

	private int xPos, yPos; // 起始位置
	private final static float deltaX = 0;
	private final static float deltaY = 50;

	private final static float deltaX1 = -50;
	private final static float deltaY1 = 50;

	private final static float deltaX2 = 50;
	private final static float deltaY2 = 50;

	private SurfaceView surfaceView;
	private ArrayList<SemenBullet> semenBulletList = new ArrayList<SemenBullet>();

	public SemenBulletLevelFour(int x, int y, Bitmap bitmap, SurfaceView surfaceView) {
		this.xPos = x;
		this.yPos = y;
		this.surfaceView = surfaceView;

		semenBulletList.add(new SemenBullet(x - (bitmap.getWidth() / 2), y, deltaX, deltaY, bitmap, surfaceView));
		semenBulletList.add(new SemenBullet(x + (bitmap.getWidth() / 2), y, deltaX, deltaY, bitmap, surfaceView));
		
		semenBulletList.add(new SemenBullet(x + (bitmap.getWidth()), y, deltaX1, deltaY1, bitmap, surfaceView));
		semenBulletList.add(new SemenBullet(x - (bitmap.getWidth()), y, deltaX2, deltaY2, bitmap, surfaceView));
		
	}

	public ArrayList<SemenBullet> getSemenBulletList() {
		return semenBulletList;
	}

}
