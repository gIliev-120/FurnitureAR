/*
 * Copyright 2018 Google LLC. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.oneseven.activities;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.Plane.Type;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Renderer;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;
import com.oneseven.utils.ScaleData;
import com.oneseven.utils.ScreenshotUtils;

import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private ArFragment arFragment;
    private ModelRenderable objectRenderable;
    private FloatingActionButton fab, removeAllButton, screenshotButton;
    private ScaleData scaleData;

    private static final SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyHHmmSS");

    @Override
    @SuppressWarnings({"AndroidApiChecker", "FutureReturnValueIgnored"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ux);

        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        removeAllButton = (FloatingActionButton) findViewById(R.id.removeAll);
        screenshotButton = (FloatingActionButton) findViewById(R.id.screenshotButton);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buildDialog();

            }
        });

        removeAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeAllObjects();
            }
        });


        screenshotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arFragment.getArSceneView().getRenderer().captureScreenshot(new Renderer.OnScreenshotListener() {
                    @Override
                    public void onScreenshotResult(ByteBuffer image, int width, int height) {
                        Bitmap screenshot = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                        screenshot.copyPixelsFromBuffer(image);
                        ScreenshotUtils.store(getApplicationContext(), screenshot, "Screenshot_" + sdf.format(new Date()) + "_FurnitureAR" + ".png");
                    }
                });
            }
        });

        arFragment.setOnTapArPlaneListener(
                (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
                    if (objectRenderable == null) {
                        Toast toast =Toast.makeText(this, R.string.errorMessageEmptyRenderable, Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        return;
                    }

                    // horizontal plane facing upward .... floor/table etc
                    if (plane.getType() != Type.HORIZONTAL_UPWARD_FACING) {
                        return;
                    }

                    Anchor anchor = hitResult.createAnchor();
                    AnchorNode anchorNode = new AnchorNode(anchor);
                    anchorNode.setParent(arFragment.getArSceneView().getScene());

                    TransformableNode object = new TransformableNode(arFragment.getTransformationSystem());

                    object.getScaleController().setMinScale(scaleData.getMinScale());
                    object.getScaleController().setMaxScale(scaleData.getMaxScale());
                    object.setLocalScale(scaleData.getCurrentScale());
                    object.setParent(anchorNode);
                    object.setRenderable(objectRenderable);
                    object.select();
                });

    }

    public void buildDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle(R.string.dialogTitle);
        String[] arr = getResources().getStringArray(R.array.ObjectsArray);
        dialogBuilder.setItems(arr, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                chooseObject(i);
            }
        });
        dialogBuilder.show();
    }


    public void chooseObject(int paramter) {
        switch (paramter) {
            case 0:
                setRenderable(R.raw.table);
                scaleData = new ScaleData(0.5f,0.8f,new Vector3(0.5f,0.5f,0.5f));
                break;
            case 1:
                setRenderable(R.raw.eames_chair_dsw);
                scaleData = new ScaleData(2.0f,2.5f,new Vector3(2.0f,2.0f,2.0f));
                break;
            case 2:
                setRenderable(R.raw.eb_lamp_01);
                scaleData = new ScaleData(0.25f,0.3f,new Vector3(0.25f,0.25f,0.25f));
                break;
            case 3:
                setRenderable(R.raw.bar_chair_2);
                scaleData = new ScaleData(0.4f,0.48f,new Vector3(0.4f,0.4f,0.4f));
                break;
            case 4:
                setRenderable(R.raw.woodenstool);
                scaleData = new ScaleData(0.4f,0.5f,new Vector3(0.4f,0.4f,0.4f));
                break;
            case 5:
                setRenderable(R.raw.mesa_pc);
                scaleData = new ScaleData(0.6f,0.9f,new Vector3(0.65f,0.65f,0.65f));
                break;
            case 6:
                setRenderable(R.raw.toilet);
                scaleData = new ScaleData(0.5f,0.7f,new Vector3(0.5f,0.5f,0.5f));
                break;
            case 7:
                setRenderable(R.raw.eb_nightstand_01);
                scaleData = new ScaleData(0.5f,0.51f,new Vector3(0.5f,0.5f,0.5f));
                break;
            case 8:
                setRenderable(R.raw.free_model_drawer);
                scaleData = new ScaleData(0.4f,0.5f,new Vector3(0.4f,0.4f,0.4f));
                break;
            case 9:
                setRenderable(R.raw.realistic_sofa);
                scaleData = new ScaleData(0.8f , 0.9f, new Vector3(0.8f,0.8f,0.8f));
                break;
          /*  case 10:
                setRenderable(R.raw.office_desk_v3_max2011);
                scaleData = new ScaleData(0.5f , 0.8f, new Vector3(0.6f,0.6f,0.6f));

                break;*/

        }

    }

    public void setRenderable(int resourceId) {
        ModelRenderable.builder()
                .setSource(this, resourceId)
                .build()
                .thenAccept(renderable -> objectRenderable = renderable)
                .exceptionally(
                        throwable -> {
                            Toast toast =
                                    Toast.makeText(this, "Unable to load andy renderable", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            return null;
                        });
    }

    private void removeAllObjects() {
        for (Anchor a : arFragment.getArSceneView().getSession().getAllAnchors()) {
            a.detach();
            }
    }



}
