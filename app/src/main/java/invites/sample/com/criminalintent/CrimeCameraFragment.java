package invites.sample.com.criminalintent;

import android.support.v4.app.Fragment;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.IOException;
import java.util.List;

/**
 * Created by mpudota on 6/11/17.
 */

public class CrimeCameraFragment extends Fragment {

    Button takeAPicture;
    SurfaceView cameraView;
    Camera camera;
    private static final String TAG = "CrimeCameraFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.crime_camera, container, false);
        takeAPicture = (Button) v.findViewById(R.id.take_a_picture);
        takeAPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        cameraView = (SurfaceView) v.findViewById(R.id.crime_picture_view);
        final SurfaceHolder holder = cameraView.getHolder();
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                try {
                    if (camera != null)
                    camera.setPreviewDisplay(holder);
                } catch (IOException e) {
                    Log.e(TAG, "Error while displaying camera", e);
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
                if (camera == null) { return; }
                Camera.Parameters parameters = camera.getParameters();
                Camera.Size size = getBestSupportedSize(parameters.getSupportedPictureSizes(), i1, i2);
                parameters.setPreviewSize(size.width, size.height);
                camera.setParameters(parameters);
                try {
                    camera.startPreview();
                } catch (Exception e) {
                    Log.e(TAG, "Error while start previewing camera ", e);
                    camera.release();
                    camera = null;
                }
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                if (camera != null)
                    camera.stopPreview();
            }
        });
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        camera = android.hardware.Camera.open();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }

    private Camera.Size getBestSupportedSize (List<Camera.Size> sizes, int width, int height) {
        Camera.Size bestSize = sizes.get(0);
        int largeArea = bestSize.width * bestSize.height;
        for (Camera.Size s : sizes) {
            int area = s.width * s.height;
            if (area < largeArea) {
                bestSize = s;
                largeArea = area;
            }
        }
        return bestSize;
    }
}
