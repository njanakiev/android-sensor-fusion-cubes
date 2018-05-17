package com.janakiev.sensorfusioncube;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import util.ErrorUtil;

import static android.opengl.GLES20.GL_DEPTH_TEST;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glEnable;
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.multiplyMV;
import static android.opengl.Matrix.perspectiveM;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.scaleM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.setLookAtM;
import static android.opengl.Matrix.translateM;
import static android.opengl.Matrix.transposeM;

public class CubeRenderer implements GLSurfaceView.Renderer{
    private static final String TAG = "CubeRenderer";
    private final int CUBES = 200;

    private final float[] projectionMatrix = new float[16];
    private final float[] modelMatrix = new float[16];
    private final float[] viewMatrix = new float[16];
    private final List<float[]> modelMatrixList = new ArrayList<>();

    private final Context context;
    private CubeShaderProgram cubeShaderProgram;
    private Cube cubes[];

    private long globalStartTime;

    public CubeRenderer(Context context){
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        glEnable(GL_DEPTH_TEST);

        Random random = new Random();
        cubes = new Cube[CUBES];
        for(int i = 0; i < CUBES; i++){
            cubes[i] = new Cube();
            double u = random.nextDouble() * 2 * Math.PI;
            double v = random.nextDouble() * 2 * Math.PI;
            float translateX = 10.0f * (float) (Math.cos(u) * Math.cos(v));
            float translateY = 10.0f * (float) (Math.sin(u) * Math.cos(v));
            float translateZ = 10.0f * (float) (Math.sin(v));

            float[] modelMatrixCube = new float[16];
            setIdentityM(modelMatrixCube, 0);
            scaleM(modelMatrixCube, 0, modelMatrixCube, 0,
                    random.nextFloat() + 0.5f,
                    random.nextFloat() + 0.5f,
                    random.nextFloat() + 0.5f);
            translateM(modelMatrixCube, 0, modelMatrixCube, 0, translateX, translateY, translateZ);

            modelMatrixList.add(modelMatrixCube);
        }

        cubeShaderProgram = new CubeShaderProgram(context,
                R.raw.cube_vertex_shader, R.raw.cube_fragment_shader);
        cubeShaderProgram.useProgram();

        globalStartTime = System.nanoTime();

        setIdentityM(modelMatrix, 0);
        setLookAtM(viewMatrix, 0,
                0.0f, 0.0f, 3.0f,
                0.0f, 0.0f, 0.0f,
                0.0f, 1.0f, 0.0f);
    }

    public void resetScene(){
        Random random = new Random();
        modelMatrixList.clear();
        for(int i = 0; i < CUBES; i++){
            double u = random.nextDouble() * 2 * Math.PI;
            double v = random.nextDouble() * 2 * Math.PI;
            float translateX = 10.0f * (float) (Math.cos(u) * Math.cos(v));
            float translateY = 10.0f * (float) (Math.sin(u) * Math.cos(v));
            float translateZ = 10.0f * (float) (Math.sin(v));

            float[] modelMatrixCube = new float[16];
            setIdentityM(modelMatrixCube, 0);
            scaleM(modelMatrixCube, 0, modelMatrixCube, 0,
                    random.nextFloat() + 0.5f,
                    random.nextFloat() + 0.5f,
                    random.nextFloat() + 0.5f);
            translateM(modelMatrixCube, 0, modelMatrixCube, 0, translateX, translateY, translateZ);

            modelMatrixList.add(modelMatrixCube);
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0, 0, width, height);

        perspectiveM(projectionMatrix, 0, 45, (float) width / (float) height, 0.1f, 20.0f);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        float tmpModelMatrix[] = new float[16];
        for(int i = 0; i < CUBES; i++){
            multiplyMM(tmpModelMatrix, 0, modelMatrixList.get(i), 0, modelMatrix, 0);
            cubeShaderProgram.setMatrixUniforms(tmpModelMatrix, viewMatrix, projectionMatrix);
            cubes[i].bindData(cubeShaderProgram);
            cubes[i].draw();
        }
    }

    public void setCameraOrientation(float[] orientationVector) {
        float eye[]= new float[3];
        eye[0] = (float) (Math.cos(orientationVector[1]) * Math.cos(orientationVector[0]));
        eye[1] = (float) (Math.sin(orientationVector[1]) * Math.cos(orientationVector[0]));
        eye[2] = (float) (Math.sin(orientationVector[0]));

        setLookAtM(viewMatrix, 0,
                eye[0], eye[1], eye[2],
                0.0f, 0.0f, 0.0f,
                0.0f, 1.0f, 0.0f);
    }
}
