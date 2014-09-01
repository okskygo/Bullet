package shot.semen.bullet;

import java.util.Timer;
import java.util.TimerTask;

import shot.semen.bullet.Enemy.EnemyTimerTask;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.SurfaceView;

public class WeaponUpgrade {
	private float deltaX;// 移動X軸多少
	private float deltaY; // 移動Y軸多少
	private SurfaceView surfaceView;
	private boolean isExistOnScreen = true;// 是否在螢幕上
	private Bitmap bitmap;
	private Rect rect;// 主目標
	private int startY;
	private Timer timer = new Timer();
	private final long COOL_DOWN_TIME = 5000;// 冷卻時間
	private boolean coolDownComplete = true;// 冷卻完成

	/**
	 * 
	 * @param y
	 *            起始位置
	 * @param bitmap
	 *            圖片
	 * @param deltaX
	 *            移動X軸多少
	 * @param deltaY
	 *            移動Y軸多少
	 * @param surfaceView
	 */
	public WeaponUpgrade(int y, float deltaX, float deltaY, Bitmap bitmap, SurfaceView surfaceView) {
		this.startY = y;
		this.deltaX = deltaX;
		this.deltaY = deltaY;
		this.surfaceView = surfaceView;
		this.bitmap = bitmap;
		// 置中
		rect = new Rect(-bitmap.getWidth(), y - (bitmap.getHeight() / 2), 0, y + (bitmap.getHeight() / 2));
	}

	/**
	 * 繪圖
	 * 
	 * @param canvas
	 */
	protected void drawWeaponUpgrade(Canvas canvas) {
		// 超過畫布就不畫
		if (!(rect.right < 0 || rect.left >= surfaceView.getWidth() || rect.centerY() < 0 || rect.centerY() >= surfaceView
				.getHeight())) {
			isExistOnScreen = true;
			canvas.drawBitmap(bitmap, rect.left, rect.top, null);
		} else {
			isExistOnScreen = false;
			resetLocation();
			startBufferTimer();			
		}
	}

	/**
	 * 執行下一個移動
	 */
	public void nextPos() {
		// 高度到一定就反彈
		if (rect.centerY() < startY - (bitmap.getHeight() / 2) || rect.centerY() > startY + (bitmap.getHeight() / 2)) {
			deltaY *= -1;
		}
		rect.left += deltaX;
		rect.right += deltaX;
		rect.top += deltaY;
		rect.bottom += deltaY;
	}

	/**
	 * 是否碰撞
	 * 
	 * @param rect
	 * @return
	 */
	public boolean isIntersect(Rect rect) {
		boolean isInersect = this.rect.intersect(rect);
		if (isInersect) {
			eatWeapon();
		}
		return isInersect;
	}

	public void eatWeapon() {
		isExistOnScreen = false;
		startBufferTimer();
		resetLocation();
	}

	/**
	 * Rect是否還存在螢幕上
	 * 
	 * @return
	 */
	public boolean isExistOnScreen() {
		return isExistOnScreen;
	}

	public void setExistOnScreen(boolean isExistOnScreen) {
		this.isExistOnScreen = isExistOnScreen;
	}

	/**
	 * @return 當前Rect
	 */
	public Rect getRect() {
		return rect;
	}

	public int getTop() {
		return rect.top;
	}

	public int getCenterX() {
		return rect.centerX();
	}

	/**
	 * 開始冷卻計時
	 */
	public void startBufferTimer() {
		this.coolDownComplete = false;
		timer.schedule(new WeaponUpgradeTimerTask(), COOL_DOWN_TIME);
	}

	/**
	 * 
	 * @return 冷卻是否完成
	 */
	public boolean isCoolDownComplete() {
		return coolDownComplete;
	}

	/**
	 * 回復本來位置
	 */
	public void resetLocation() {
		rect.left = -bitmap.getWidth();
		rect.right = 0;
		rect.top = startY - (bitmap.getHeight() / 2);
		rect.bottom = startY + (bitmap.getHeight() / 2);
	}

	class WeaponUpgradeTimerTask extends TimerTask {

		@Override
		public void run() {
			coolDownComplete = true;
		}
	};
}
