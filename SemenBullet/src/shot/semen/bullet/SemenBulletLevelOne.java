package shot.semen.bullet;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.SurfaceView;

public class SemenBulletLevelOne {
	private int xPos, yPos; // 起始位置
	private final static float deltaX = 0;
	private final static float deltaY = 50; // 小球的速度
	private SurfaceView surfaceView;
	private ArrayList<SemenBullet> semenBulletList = new ArrayList<SemenBullet>();

	public SemenBulletLevelOne(int x, int y, Bitmap bitmap, SurfaceView surfaceView) {
		this.xPos = x;
		this.yPos = y;
		this.surfaceView = surfaceView;
		// 置中	 
		semenBulletList.add(new SemenBullet(x, y, deltaX, deltaY, bitmap, surfaceView));
	}

	public ArrayList<SemenBullet> getSemenBulletList() {
		return semenBulletList;
	}

}
