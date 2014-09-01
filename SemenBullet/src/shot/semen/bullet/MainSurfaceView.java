package shot.semen.bullet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import shot.semen.bullet.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;

public class MainSurfaceView extends SurfaceView implements SurfaceHolder.Callback, OnTouchListener {

	private SurfaceHolder holder = getHolder();
	private int semenLevel = 1;// 當前武器等級
	private final static int SEMAN_MAX_LEVEL = 4;// 武器最高等級
	private MainAnimThread thread;// 主要繪圖線程
	private ArrayList<SemenBullet> bulletList = new ArrayList<SemenBullet>();// 子彈LIST
	private Rect rect;// 玩家
	private Bitmap bitmap;// 玩家圖片
	private Enemy enemy;// 敵人
	private Bitmap enemyBitmap;// 敵人圖片
	private Paint paint;// 寫字用
	private int collisionTime = 0;// 統計碰撞次數
	private Bitmap semenBitmap;// 武器圖片
	private WeaponUpgrade weaponUpgrade;// 武器升級包Object
	private Bitmap weaponUpgradeBitmap;// 武器升級包圖片
	private Point point = new Point();
	private boolean canDrag = false;// 判斷是否點擊在圖片上 否則拖動無效
	private int offsetX = 0, offsetY = 0;// 點擊點離圖片左上角的距離
	private Timer bulletTimer = new Timer();
	private boolean bulletCoolDown = true;
	private final int BULLET_COOL_DOWN_TIME = 500;

	public MainSurfaceView(Context context) {
		super(context);
		// 必要callback
		holder.addCallback(this);
		// 重複使用一張bitmap節省記憶體
		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.jaguar);
		enemyBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.recon);
		semenBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.semen);
		weaponUpgradeBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.heart);
		// 觸碰listener
		setOnTouchListener(this);
		// 寫字用
		paint = new Paint();
		paint.setColor(Color.GREEN);
		paint.setTextSize(14);
	}

	public MainSurfaceView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		holder.addCallback(this);
		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.jaguar);
		enemyBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.recon);
		semenBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.semen);
		weaponUpgradeBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.heart);
		setOnTouchListener(this);
		paint = new Paint();
		paint.setColor(Color.GREEN);
		paint.setTextSize(14);
	}

	public MainSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		holder.addCallback(this);
		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.jaguar);
		enemyBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.recon);
		semenBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.semen);
		weaponUpgradeBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.heart);
		setOnTouchListener(this);
		paint = new Paint();
		paint.setColor(Color.GREEN);
		paint.setTextSize(30);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {

		// 1. 鎖住畫布
		Canvas canvas = holder.lockCanvas();
		// 2. 在畫布上貼圖
		float halfCanvasWidth = (getWidth() / 2);
		float halfCanvasHeight = (getHeight() / 2);
		float halfBitmapWidth = (bitmap.getWidth() / 2);
		float halfBitmapHeight = (bitmap.getHeight() / 2);

		int xPosStartUser = Math.round(halfCanvasWidth - halfBitmapWidth);
		int yPosStartUser = Math.round(getHeight() - bitmap.getHeight());

		rect = new Rect(xPosStartUser, yPosStartUser, xPosStartUser + bitmap.getWidth(), yPosStartUser
				+ bitmap.getHeight());// 玩家的起始位置

		canvas.drawBitmap(bitmap, xPosStartUser, yPosStartUser, null);

		// 3. 解鎖並po出畫布
		holder.unlockCanvasAndPost(canvas);

		enemy = new Enemy(xPosStartUser, 0, enemyBitmap, 10, 0, this);

		thread = new MainAnimThread(this);
		thread.setRunning(true);
		thread.start();

	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
	}

	public void start() {

		thread = new MainAnimThread(this);
		thread.setRunning(true);
		thread.start();

	}

	public void stop() {
		thread.setRunning(false);
		if (Thread.interrupted())
			try {
				throw new InterruptedException();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		thread = null;
	}

	class MainAnimThread extends Thread {
		private MainSurfaceView mainSurfaceView;
		private boolean running = false;

		public MainAnimThread(MainSurfaceView testSurfaceView) {
			this.mainSurfaceView = testSurfaceView;
		}

		public void setRunning(boolean run) {
			running = run;
		}

		public boolean isRunning() {
			return running;
		}

		@Override
		public void run() {
			int SLEEP_TIME = 100;

			while (running) {

				Canvas canvas = mainSurfaceView.getHolder().lockCanvas();

				if (canvas != null) {
					synchronized (mainSurfaceView.getHolder()) {
						long start = System.currentTimeMillis();
						// reset canvas
						canvas.drawColor(Color.BLACK);
						// 子彈繪圖
						boolean isHit = bulletExecute(canvas);
						// 武器升級包繪圖
						weaponUpgradeExecute(canvas);
						// 敵人繪圖
						enemyExecute(canvas, isHit);
						// 玩家繪圖
						mainSurfaceView.userExecute(canvas);
						// 碰撞繪圖
						canvas.drawText("碰撞次數 : " + collisionTime, 40, 40, paint);
						long end = System.currentTimeMillis();

						if (end - start < SLEEP_TIME) {
							try {
								Thread.sleep(SLEEP_TIME - (end - start));
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}

					mainSurfaceView.getHolder().unlockCanvasAndPost(canvas);
				}

			}
		}
	}

	/**
	 * 子彈繪圖
	 * 
	 * @param canvas
	 * @return isHit 敵人是否被擊中
	 */
	private boolean bulletExecute(Canvas canvas) {

		boolean isHit = false;

		//子彈發射時間
		if(bulletCoolDown){
			addBulletToBulletList();
			bulletCoolDown = false;
		}
		
		Iterator<SemenBullet> iterator = bulletList.iterator();

		while (iterator.hasNext()) {
			SemenBullet bullet = iterator.next();
			bullet.nextPos();
			if (bullet.getRect().intersect(enemy.getRect())) {
				collisionTime++;
				iterator.remove();
				isHit = true;
			} else if (bullet.isExistOnScreen()) {
				bullet.drawBullet(canvas);
			} else {
				iterator.remove();
			}
		}

		return isHit;

	}

	/**
	 * 武器升級包繪圖
	 * 
	 * @param canvas
	 */
	private void weaponUpgradeExecute(Canvas canvas) {
		if (weaponUpgrade == null) {
			weaponUpgrade = new WeaponUpgrade(canvas.getHeight() / 2, 10, -10, weaponUpgradeBitmap,
					MainSurfaceView.this);
		}
		if (weaponUpgrade.isExistOnScreen()) {
			// 下一步
			weaponUpgrade.nextPos();
			if (weaponUpgrade.isIntersect(rect)) {
				if (semenLevel < SEMAN_MAX_LEVEL) {
					semenLevel++;
				}
			} else {
				weaponUpgrade.drawWeaponUpgrade(canvas);
			}
		} else if (weaponUpgrade.isCoolDownComplete()) {
			weaponUpgrade.drawWeaponUpgrade(canvas);
		}
	}

	/**
	 * 敵人繪圖
	 * 
	 * @param canvas
	 *            當前畫布
	 * @param isHit
	 *            是否被擊中
	 */
	private void enemyExecute(Canvas canvas, boolean isHit) {
		// 下一步
		enemy.nextPos();
		if (isHit) {
			if (enemy.isCoolDownComplete()) {
				enemy.startBufferTimer();
			} else {
				enemy.drawEnemy(canvas);
			}
		} else {
			enemy.drawEnemy(canvas);
		}
	}
	
	/**
	 * USER繪圖
	 * @param canvas
	 */
	protected void userExecute(Canvas canvas) {

		canvas.drawBitmap(bitmap, rect.left, rect.top, null);

	}
	
	//增加子彈
	private void addBulletToBulletList(){
		
		TimerTask timerTask = new TimerTask() {
			
			@Override
			public void run() {
				bulletCoolDown = true;
			}
		};
		switch (semenLevel) {
		case 1:
			SemenBulletLevelOne semenBulletLevelOne = new SemenBulletLevelOne(rect.centerX(), rect.top, semenBitmap,
					MainSurfaceView.this);
			bulletList.addAll(semenBulletLevelOne.getSemenBulletList());
			break;
		case 2:
			SemenBulletLevelTwo semenBulletLevelTwo = new SemenBulletLevelTwo(rect.centerX(), rect.top, semenBitmap,
					MainSurfaceView.this);
			bulletList.addAll(semenBulletLevelTwo.getSemenBulletList());
			break;

		case 3:
			SemenBulletLevelThree semenBulletLevelThree = new SemenBulletLevelThree(rect.centerX(), rect.top,
					semenBitmap, MainSurfaceView.this);
			bulletList.addAll(semenBulletLevelThree.getSemenBulletList());
			break;

		case 4:
			SemenBulletLevelFour semenBulletLevelFour = new SemenBulletLevelFour(rect.centerX(), rect.top, semenBitmap,
					MainSurfaceView.this);
			bulletList.addAll(semenBulletLevelFour.getSemenBulletList());
			break;

		default:
			SemenBulletLevelOne semenBulletLevelOne1 = new SemenBulletLevelOne(rect.centerX(), rect.top, semenBitmap,
					MainSurfaceView.this);
			bulletList.addAll(semenBulletLevelOne1.getSemenBulletList());
			break;
		}
		
		bulletTimer.schedule(timerTask, BULLET_COOL_DOWN_TIME);
		
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			point.x = (int) event.getX();
			point.y = (int) event.getY();
			if (rect.contains(point.x, point.y)) {
				canDrag = true;
				offsetX = point.x - rect.left;
				offsetY = point.y - rect.top;
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if (canDrag) {
				rect.left = (int) event.getX() - offsetX;
				rect.top = (int) event.getY() - offsetY;
				rect.right = rect.left + bitmap.getWidth();
				rect.bottom = rect.top + bitmap.getHeight();
				if (rect.left < 0) {
					rect.left = 0;
					rect.right = rect.left + bitmap.getWidth();
				}
				if (rect.right > getMeasuredWidth()) {
					rect.right = getMeasuredWidth();
					rect.left = rect.right - bitmap.getWidth();
				}
				if (rect.top < 0) {
					rect.top = 0;
					rect.bottom = rect.top + bitmap.getHeight();
				}
				if (rect.bottom > getMeasuredHeight()) {
					rect.bottom = getMeasuredHeight();
					rect.top = rect.bottom - bitmap.getHeight();
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			canDrag = false;
			break;

		default:
			break;
		}
		return true;
	}

}
