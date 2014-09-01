package shot.semen.bullet;

import shot.semen.bullet.R;
import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);

//		testSurfaceView = (TestSurfaceView) findViewById(R.id.testSurfaceView);
//		button = (Button) findViewById(R.id.button);
//		
//		button.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				if(!isRunning){
//					button.setText("Stop");
//					isRunning = true;
//					testSurfaceView.start();;
//				}else{
//					button.setText("Start");
//					isRunning = false;
//					testSurfaceView.stop();
//				}
//				
//			}
//		});
		
//		WebView webView = (WebView) findViewById(R.id.webView);
//		WebViewClient webviewClient = new WebViewClient() {
//			@Override
//			public void onPageFinished(WebView view, String url) {
//				super.onPageFinished(view, url);
//				String c = CookieManager.getInstance().getCookie(url);
//				Log.i("tag", c);
//			}
//
//			@Override
//			public boolean shouldOverrideUrlLoading(WebView view, String url) {
//				view.loadUrl(url);
//				return true;
//			}
//		};
//		webView.setWebViewClient(webviewClient);
//		WebChromeClient webChromeClient = new WebChromeClient(){
//			
//		};
//		WebSettings webSettings = webView.getSettings();
//		webSettings.setJavaScriptEnabled(true);
//		webView.loadUrl("http://track.unisurf.tw/public/link.php?r=354");
		
	}


}
