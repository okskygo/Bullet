package shot.semen.bullet;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.SurfaceView;

public class SemenBullet {
	private int xPos, yPos;// 起始位置
	private float deltaX;// 移動X軸
	private float deltaY; // 移動Y軸
	private SurfaceView surfaceView;
	private boolean isExistOnScreen = true;
	private static Bitmap bitmap;
	private Rect rect;

	public SemenBullet(int x, int y, float deltaX, float deltaY, Bitmap bitmap, SurfaceView surfaceView) {
		this.xPos = x;
		this.yPos = y;
		this.deltaX = deltaX;
		this.deltaY = deltaY;
		this.surfaceView = surfaceView;
		this.bitmap = bitmap;
		// 置中
		rect = new Rect(x - (bitmap.getWidth() / 2), y, x + (bitmap.getWidth() / 2), y + (bitmap.getHeight() / 2));
	}

	/**
	 * 繪圖
	 * @param canvas
	 */
	protected void drawBullet(Canvas canvas) {
		if (!(rect.right < 0 || rect.left >= surfaceView.getWidth() || rect.bottom < 0 || rect.top >= surfaceView
				.getHeight())) {
			canvas.drawBitmap(bitmap, rect.left, rect.top, null);
		} else {
			isExistOnScreen = false;
		}
	}

	public void nextPos() {
		rect.left -= deltaX;
		rect.right -= deltaX;
		rect.top -= deltaY;
		rect.bottom -= deltaY;
	}

	public boolean isExistOnScreen() {
		return isExistOnScreen;
	}

	public void setExistOnScreen(boolean isExistOnScreen) {
		this.isExistOnScreen = isExistOnScreen;
	}

	public int getTop() {
		return rect.top;
	}

	public int getCenterX() {
		return rect.centerX();
	}

	public Rect getRect() {
		return rect;
	}

}
