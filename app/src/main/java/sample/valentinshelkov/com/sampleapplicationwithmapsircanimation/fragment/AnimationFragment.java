package sample.valentinshelkov.com.sampleapplicationwithmapsircanimation.fragment;


import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.valentinshelkov.sampleapplicationwithmapsircanimation.R;


public class AnimationFragment extends Fragment {

    private static final String IMAGE_TAG = "animatedImageView";
    private View rootView;
    private ImageView animatedImageView;
    private Animation animationIn, animationOut;
    private FrameLayout container;
    private final Handler handler = new Handler();

    public AnimationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup root,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        animationIn = AnimationUtils.loadAnimation(inflater.getContext(), R.anim.rot_in);
        animationOut = AnimationUtils.loadAnimation(inflater.getContext(), R.anim.rot_out);
        rootView = inflater.inflate(R.layout.fragment_animation, container, false);
        this.container = (FrameLayout) rootView.findViewById(R.id.container);
        rootView.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent event) {
                switch (event.getAction()) {
                    case DragEvent.ACTION_DROP:
                        float X = event.getX() - container.getWidth();
                        float Y = event.getY() - container.getHeight();
                        Log.w("X, Y", X + ", " + Y);
                        container.setX(X);
                        container.setY(Y);
                        container.setLeft((int) X);
                        container.setTop((int) Y);
                        container.setRight((int) Y + container.getWidth());
                        container.setBottom((int) X + container.getHeight());
                        // Invalidates the view to force a redraw
                        container.invalidate();
                        break;
                }
                return true;
            }
        });
        animatedImageView = (ImageView) rootView.findViewById(R.id.animatedImageView);
        animatedImageView.setTag(IMAGE_TAG);
        container.setOnTouchListener(new View.OnTouchListener() {
            private GestureDetector detector;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (detector == null) {
                    detector = new InnerGestureDetector(v.getContext(), AnimationFragment.this);
                }
                return detector.onTouchEvent(event);
            }
        });
        container.setOnLongClickListener(new View.OnLongClickListener() {

            // Defines the one method for the interface, which is called when the View is long-clicked
            public boolean onLongClick(View v) {
                ClipData.Item item = new ClipData.Item((String) v.getTag());
                ClipData dragData = new ClipData((CharSequence) v.getTag(), new String[]{"text/plain"}, item);
                View.DragShadowBuilder myShadow = new MyDragShadowBuilder(animatedImageView);
                // Starts the drag
                v.startDrag(dragData,  // the data to be dragged
                        myShadow,  // the drag shadow builder
                        null,      // no need to use local data
                        0          // flags (not currently used, set to 0)
                );
                return true;
            }
        });
        container.setOnDragListener(new View.OnDragListener() {

            @Override
            public boolean onDrag(final View v, DragEvent event) {
                // Defines a variable to store the action type for the incoming event
                final int action = event.getAction();
                final Drawable imageDrawable = v.getContext().getResources().getDrawable(R.drawable.racoon_dog_src);
                Drawable transparent = new ColorDrawable(Color.TRANSPARENT);
                switch (action) {

                    case DragEvent.ACTION_DRAG_STARTED:
                        if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                            animatedImageView.setColorFilter(Color.TRANSPARENT);
                            animatedImageView.setImageDrawable(transparent);
                            animatedImageView.invalidate();
                            return true;
                        }
                    case DragEvent.ACTION_DRAG_ENTERED:
                        animatedImageView.setColorFilter(Color.TRANSPARENT);
                        animatedImageView.setImageDrawable(transparent);
                        v.invalidate();
                        return true;
                    case DragEvent.ACTION_DRAG_LOCATION:
                        // Ignored
                        return true;
                    case DragEvent.ACTION_DRAG_EXITED:
                        animatedImageView.setColorFilter(Color.TRANSPARENT);
                        animatedImageView.setImageDrawable(transparent);
                        animatedImageView.invalidate();
                        return true;
                    case DragEvent.ACTION_DRAG_ENDED:
                        animatedImageView.clearColorFilter();
                        animatedImageView.setImageDrawable(imageDrawable);
                        animatedImageView.invalidate();
                        return true;
                    default:
                        Log.e("DragDrop Example", "Unknown action type received by OnDragListener.");
                        break;
                }
                return false;

            }
        });
        return rootView;
    }

    private static class MyDragShadowBuilder extends View.DragShadowBuilder {
        private static Drawable shadow;

        public MyDragShadowBuilder(View v) {
            super(v);
            LevelListDrawable levelListDrawable = new LevelListDrawable();
            levelListDrawable.addLevel(0, 0, ((ImageView) v).getDrawable());
            shadow = levelListDrawable.mutate();
        }

        @Override
        public void onProvideShadowMetrics(Point size, Point touch) {
            int width = getView().getWidth();
            int height = getView().getHeight();
            shadow.setBounds(0, 0, width, height);
            size.set(width, height);
            touch.set(width / 2, height / 2);
        }

        @Override
        public void onDrawShadow(Canvas canvas) {
            shadow.draw(canvas);
        }
    }

    private static class InnerGestureDetector extends GestureDetector {

        private static final String TAG = InnerGestureDetector.class.getSimpleName();

        public InnerGestureDetector(final Context context, final AnimationFragment fragment) {
            super(context, new SimpleOnGestureListener() {
                final Animation.AnimationListener listener = new Animation.AnimationListener() {
                    private volatile float scale = 1f;
                    @Override
                    public void onAnimationStart(Animation animation) {
                        if(animation.equals(fragment.animationOut)){
                            scale *= -1f;
                            Log.w(TAG, "SCALE " + scale);
                            fragment.animatedImageView.setScaleX(scale);
                        }
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                };

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    Log.w("InnerGestureDetector", String.valueOf(e.getDownTime()));
                    fragment.animationIn.setAnimationListener(listener);
                    fragment.animationOut.setAnimationListener(listener);
                    fragment.handler.post(new Runnable() {
                        @Override
                        public void run() {
                            fragment.animatedImageView.clearAnimation();
                            fragment.animationIn.reset();
                            fragment.animatedImageView.startAnimation(fragment.animationIn);
                        }
                    });
                    fragment.handler.post(new Runnable() {
                        @Override
                        public void run() {
                            fragment.animatedImageView.clearAnimation();
                            fragment.animationOut.reset();
                            fragment.animatedImageView.startAnimation(fragment.animationOut);
                        }
                    });
                    return true;
                }
            });
        }
    }


}
