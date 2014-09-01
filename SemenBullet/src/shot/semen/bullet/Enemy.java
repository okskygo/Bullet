package shot.semen.bullet;

import java.util.Timer;
import java.util.TimerTask;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.SurfaceView;

public class Enemy {

	private int xPos;
	private int yPos;
	private float deltaX = 10;// 移動X軸多少
	private float deltaY = 0;// 移動Y軸多少
	private SurfaceView surfaceView;
	private boolean isExistOnScreen = true;// 是否在螢幕上
	private Bitmap bitmap;
	private Rect rect;// 敵人位置
	private Timer timer = new Timer();
	private final long ENEMY_BUFFER_TIME = 500;// 閃爍冷卻時間
	private boolean coolDownComplete = true;// 閃爍冷卻完成

	/**
	 * 
	 * @param x
	 *            起始位置
	 * @param y
	 *            起始位置
	 * @param bitmap
	 *            敵人圖片
	 * @param deltaX
	 *            移動X軸多少 (暫寫死)
	 * @param deltaY
	 *            移動Y軸多少 (暫寫死)
	 * @param surfaceView
	 */
	public Enemy(int x, int y, Bitmap bitmap, float deltaX, float deltaY, SurfaceView surfaceView) {
		this.xPos = x;
		this.yPos = y;
		this.bitmap = bitmap;
		this.surfaceView = surfaceView;
		rect = new Rect(x, y, x + bitmap.getWidth(), y + bitmap.getHeight());
	}

	/**
	 * 繪圖
	 * 
	 * @param canvas
	 */
	protected void drawEnemy(Canvas canvas) {

		// 觸碰邊界反轉
		if (deltaX > 0) {
			if (rect.left >= surfaceView.getWidth() - bitmap.getWidth()) {
				deltaX *= -1;
			}
		} else {
			if (rect.left <= 0) {
				deltaX *= -1;
			}
		}

		canvas.drawBitmap(bitmap, rect.left, rect.top, null);

	}

	/**
	 * 執行下一個移動
	 */
	public void nextPos() {
		rect.left += deltaX;
		rect.right += deltaX;
		rect.top += deltaY;
		rect.bottom += deltaY;
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

	/**
	 * 
	 * @return 閃爍冷卻是否完成
	 */
	public boolean isCoolDownComplete() {
		return coolDownComplete;
	}

	/**
	 * 開始閃爍冷卻計時
	 */
	public void startBufferTimer() {
		this.coolDownComplete = false;
		timer.schedule(new EnemyTimerTask(), ENEMY_BUFFER_TIME);
	}

	class EnemyTimerTask extends TimerTask {

		@Override
		public void run() {
			coolDownComplete = true;
		}
	};

}
