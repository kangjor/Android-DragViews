package com.example.dragand;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewGroup;
import android.widget.Scroller;
import android.widget.Toast;

/* ���� ȭ���� �����Ҷ� �������� ��Ƽ��Ƽ�� �ٴ°;ƴ϶�  
 * viewgroup�� childview �� �־�ΰ� ��ũ���� ���� ȭ�� ��ȯ ȿ���� �̷�� ����̴�. 
 * ���� viewgroup �� ��ӹް� ȭ�鿡 �ְ����ϴ� layout�� addview() Ŭ������ ���� �����Ѵ�. 
 */ 
public class SlidingView extends ViewGroup {
    private static final String TAG = "SlidingView"; 

    // �巡�� �ӵ��� ������ �Ǵ��ϴ� Ŭ���� 
    private VelocityTracker mVelocityTracker = null; 
     
    // ȭ�� ��ȯ�� ���� �巡�� �ӵ��� �ּҰ� pixel/s (100 �������� �ӵ��� �̵��ϸ� ȭ����ȯ���� �ν�) 
    private static final int SNAP_VELOCITY = 100; 
     
    /* ȭ�鿡 ���� ��ġ�̺�Ʈ�� ȭ����ȯ�� ���� ��ġ�ΰ�? �� ȭ���� ���������� ���� 
        ��ġ�ΰ�? �����ϴ� �� (�������¿��� 10px �̵��ϸ� ȭ�� �̵����� �ν�) */ 
    private int mTouchSlop = 10; 

    private Bitmap mWallpaper = null; // ���ȭ���� ���� ��Ʈ�� 
    private Paint mPaint = null;
     
    /* ȭ�� �ڵ� ��Ȳ�� ���� �ٽ� Ŭ���� ( ȭ�� �巡���� ���� ������ 
        ȭ�� ��ȯ�̳� ���� ȭ������ �ڵ����� ��ũ�� �Ǵ� ������ �����ϴ� Ŭ����) */ 
    private Scroller mScroller = null; 
    private PointF mLastPoint = null; // ������ ��ġ ������ �����ϴ� Ŭ����  
    private int mCurPage = 0; // ���� ȭ�� ������ 

    private int mCurTouchState; // ���� ��ġ�� ���� 
    private static final int TOUCH_STATE_SCROLLING = 0; // ���� ��ũ�� ���̶�� ���� 
    private static final int TOUCH_STATE_NORMAL = 1; // ���� ��ũ�� ���°� �ƴ� 

    private Toast mToast; 

    public SlidingView(Context context) { 
        super(context); 
        init(); 
    } 

    public SlidingView(Context context, AttributeSet attrs) { 
        super(context, attrs); 
        init(); 
    } 

    public SlidingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle); 
        init(); 
    } 

    private void init() { 
        mWallpaper = BitmapFactory.decodeResource(getResources(), 
                R.drawable.a); // ���ȭ�� �ҷ����� 
        mPaint = new Paint(); 
        mScroller = new Scroller(getContext()); // ��ũ�ѷ� Ŭ���� ���� 
        mLastPoint = new PointF(); 
    } 

    // ���ϵ���� ũ�⸦ �����ϴ� �ݹ� �޼��� 
    @Override 
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec); 
        Log.d(TAG, "onMeasure"); 
        for (int i = 0; i < getChildCount(); i++) {
            // �� ���ϵ���� ũ��� �����ϰ� ���� 
            getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);  
        } 
    } 

    // ���ϵ���� ��ġ�� �����ϴ� �ݹ� �޼��� 
    @Override 
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.d(TAG, "onLayout"); 
        // �ٽ� ���� �κ����ν� 
        // ���ϵ����� ���ʴ�� ��ġ�� �ʰ� ������ �����ؼ� ��ġ�� ��ġ�Ѵ�. 
        // ������ ���ʴ�� ��ġ�� �س��� ��ũ���� ���� ������ �̵��ϴ°��� ����������. 
        for (int i = 0; i < getChildCount(); i++) {
            int w = getChildAt(i).getMeasuredWidth() * i; 
            getChildAt(i).layout(w, t, w + getChildAt(i).getMeasuredWidth(), 
                    getChildAt(i).getMeasuredHeight()); 
        } 
    } 

    // viewgroup Ŭ������ onDraw �޼���� �����ϸ� �ǰڴ�. 
    @Override 
    protected void dispatchDraw(Canvas canvas) {
        canvas.drawBitmap(mWallpaper, 0, 0, mPaint); // ����ȭ���� �׸��� 
        for (int i = 0; i < getChildCount(); i++) {
            drawChild(canvas, getChildAt(i), 100); // ���ϵ� ����� �ϳ��ϳ� �׸���. 
        } 
    } 

    @Override 
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG, "event Action : " + event.getAction()); 

        if (mVelocityTracker == null)
            mVelocityTracker = VelocityTracker.obtain(); 
         
        // ��ġ�Ǵ� ��� ��ǥ���� �����Ͽ�, ��ġ �巡�� �ӵ��� �Ǵ��ϴ� ���ʸ� ���� 
        mVelocityTracker.addMovement(event);  

        switch (event.getAction()) { 

        case MotionEvent.ACTION_DOWN: 
            // ���� ȭ���� �ڵ� ��ũ�� ���̶�� (ACTION_UP �� mScroller �κ� ����) 
            if (!mScroller.isFinished()) {  
                mScroller.abortAnimation(); // �ڵ���ũ�� �����ϰ� ��ġ ������ ���缭������ 
            } 
            mLastPoint.set(event.getX(), event.getY()); // ��ġ���� ���� 
            break; 

        case MotionEvent.ACTION_MOVE: 
            // ���� ��ġ������ ���� ��ġ������ ���̸� ���ؼ� ȭ�� ��ũ�� �ϴµ� �̿� 
            int x = (int) (event.getX() - mLastPoint.x); 
            scrollBy(-x, 0); // ���̸�ŭ ȭ�� ��ũ�� 
            invalidate(); // �ٽ� �׸��� 
            mLastPoint.set(event.getX(), event.getY()); 
            break; 

        case MotionEvent.ACTION_UP: 
            // pixel/ms ������ �巡�� �ӵ��� ���Ұ��ΰ� ���� (1�ʷ� ����) 
            // onInterceptTouchEvent �޼��忡�� ��ġ������ �����ص� ���� ���� �Ѵ�. 
            mVelocityTracker.computeCurrentVelocity(1000); 
            int v = (int) mVelocityTracker.getXVelocity(); // x �� �̵� �ӵ��� ���� 

            int gap = getScrollX() - mCurPage * getWidth(); // �巡�� �̵� �Ÿ� üũ 
            Log.d(TAG, "mVelocityTracker : " + v); 
            int nextPage = mCurPage; 

            // �巡�� �ӵ��� SNAP_VELOCITY ���� ���Ŵ� ȭ�� ���̻� �巡�� ������  
            // ȭ����ȯ �Ұ��̶�� nextPage ������ ���� ����. 
            if ((v > SNAP_VELOCITY || gap < -getWidth() / 2) && mCurPage > 0) {
                nextPage--; 
            } else if ((v < -SNAP_VELOCITY || gap > getWidth() / 2) 
                    && mCurPage < getChildCount() - 1) {
                nextPage++; 
            } 

            int move = 0;
            if (mCurPage != nextPage) { // ȭ�� ��ȯ ��ũ�� ��� 
                // ���� ��ũ�� �������� ȭ����ȯ�� ���� �̵��ؾ��ϴ� �������� �Ÿ� ��� 
                move = getChildAt(0).getWidth() * nextPage - getScrollX(); 
            } else { // ���� ȭ�� ���� ��ũ�� ��� 
                // ȭ�� ��ȯ ���� �������̸� ���� �������� ���ư��� ���� �̵��ؾ��ϴ� �Ÿ� ��� 
                move = getWidth() * mCurPage - getScrollX();  
            } 

            // �ٽ�!! ���� ��ũ�� ������ �̵��ϰ��� �ϴ� ���� ��ǥ ��ũ�� ������ �����ϴ� �޼��� 
            // ���� �������� ��ǥ �������� ��ũ�ѷ� �̵��ϱ� ���� �߰������� �ڵ����� �����ش�. 
            // ������ ���ڴ� ��ǥ �������� ��ũ�� �Ǵ� �ð��� �����ϴ� ��. �и������� �����̴�. 
            // ������ ������ �ð����� �߰� ��ũ�� ������ ��� ȭ�鿡 ��� ��ũ���� ���ش�. 
            // �׷��� ��ũ�� �ִϸ��̼��� �Ǵ°�ó�� ���δ�. (computeScroll() ����) 
            mScroller.startScroll(getScrollX(), 0, move, 0, Math.abs(move));

            if (mToast != null) {
                mToast.setText("page : " + nextPage); 
            } else { 
                mToast = Toast.makeText(getContext(), "page : " + nextPage, 
                        Toast.LENGTH_SHORT); 
            } 
            mToast.show(); 
            invalidate(); 
            mCurPage = nextPage; 

            // ��ġ�� �������� �����صξ��� ��ġ ������ �����ϰ� 
            // ��ġ���´� �Ϲ����� ���� 
            mCurTouchState = TOUCH_STATE_NORMAL; 
            mVelocityTracker.recycle(); 
            mVelocityTracker = null; 
            break; 
        } 

        return true; 
    } 

    // ���� �ٽ�!! �� �ݹ� �޼���. ��ũ�� �ɶ����� ������ �����. 
    @Override 
    public void computeScroll() {
        super.computeScroll(); 
        // onTouchEvent ���� ������ mScroller �� ��ǥ ��ũ�� �������� �̵��ϴµ� �߰� ������  
        // ��� ���� �޼���μ�, �߰����� ������ ������ true �� ���� 
        if (mScroller.computeScrollOffset()) {  
            // ���� ������ �ִٸ�. getCurrX,getCurrY �� ���� ���޵Ǵµ�,  
            // �̴� ��ǥ ������ �߰� ������ Scroller Ŭ������ �ڵ����� ����� ���̴�. 
            // scrollTo() �� ���� ȭ���� �߰� �������� ��ũ�� �ϰ�,  
            // �ռ� ���ߵ� ��ũ���� �Ǹ� �ڵ����� computeScroll() �޼��尡 ȣ��Ǳ� ������ 
            // ��ǥ ��ũ�� ������ �����ҋ����� computeScroll() �޼��尡 ��� ȣ�� �ȴ�.  
            // ���� ȭ�鿡 ��ũ�� �ִϸ��̼��� �����Ȱ�ó�� ���̰� ��. 
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY()); 
            invalidate(); 
        } 
    } 

    // ViewGroup �� childview �鿡�� ��ġ�̺�Ʈ�� �ٰ����� �ƴϸ� ���ο��� ��ġ�̺�Ʈ�� �ٰ����� 
    // �Ǵ��ϴ� �ݹ� �޼��� ( ��ġ �̺�Ʈ �߻��� ������� ���� �� ) 
    // ���ϰ����� true �� �ְ� �Ǹ� viewgroup�� onTouchEvent �޼��尡 ����ǰ� 
    // false �� �ָ� ViewGroup �� onTouchEvent�� ������� �ʰ� childview ����  
    // ��ġ �̺�Ʋ�� �Ѱ��ְ� �ȴ�. ����, ȭ�� ��ȯ �Ұ��ΰ�? ���ϵ���� ��ư�̳� ��Ÿ ������ ��Ʈ�� 
    // �ϴ� �����ΰ�? �� �����ϴ� ������ ���⼭ �ʿ��ϴ�. 
    @Override 
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.d(TAG, "onInterceptTouchEvent : " + ev.getAction()); 
        int action = ev.getAction(); 
        int x = (int) ev.getX();
        int y = (int) ev.getY();

        switch (action) { 
        case MotionEvent.ACTION_DOWN: 
            // Scroller�� ���� ��ǥ�������� ��ũ�� �Ǿ����� �Ǵ��ϴ� isFinished() �� ���� 
            // ȭ���� �ڵ� ��ũ�� �Ǵ� ���߿� ��ġ�� �Ѱ����� �ƴ����� Ȯ���Ͽ�,  
            // �ڽĿ��� �̺�Ʈ�� ������ �ٰ����� �Ǵ��Ѵ�. 
            mCurTouchState = mScroller.isFinished() ? TOUCH_STATE_NORMAL 
                    : TOUCH_STATE_SCROLLING; 
            mLastPoint.set(x, y); // ��ġ ���� ���� 
            break; 
        case MotionEvent.ACTION_MOVE: 
            // �ڽĺ��� �̺�Ʈ�ΰ� �ƴϸ� ȭ����ȯ ���� �̺�Ʈ�� �Ǵ��ϴ� ������ �⺻�� �Ǵ�  
            // �巡�� �̵� �Ÿ��� üũ ����Ѵ�. 
            int move_x = Math.abs(x - (int) mLastPoint.x);
            // ���� ó�� ��ġ�������� mTouchSlop ��ŭ �̵��Ǹ� ȭ����ȯ�� ���� �������� �Ǵ� 
            if (move_x > mTouchSlop) {  
                mCurTouchState = TOUCH_STATE_SCROLLING; // ���� ���� ��ũ�� ���·� ��ȯ 
                mLastPoint.set(x, y); 
            } 
            break; 
        } 

        // ���� ���°� ��ũ�� ���̶�� true�� �����Ͽ� viewgroup�� onTouchEvent �� ����� 
        return mCurTouchState == TOUCH_STATE_SCROLLING; 
    } 
}