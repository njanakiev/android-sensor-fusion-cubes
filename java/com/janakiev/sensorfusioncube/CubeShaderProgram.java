package com.janakiev.sensorfusioncube;

import android.content.Context;

import util.ErrorUtil;
import util.ShaderHelper;
import util.TextResourceReader;

import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetError;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;

public class CubeShaderProgram {
    private static final String TAG = "CubeShaderProgram";

    private final int program;

    private static final String U_MODEL = "u_Model";
    private static final String U_VIEW = "u_View";
    private static final String U_PROJ = "u_Proj";
    private static final String A_POSITION = "a_Position";
    private static final String A_COLOR = "a_Color";

    // Uniform constants
    private final int uModelLocation;
    private final int uViewLocation;
    private final int uProjectionLocation;

    // Attribute locations
    private final int aPositionLocation;
    private final int aColorLocation;

    public CubeShaderProgram(Context context,
        int vertexShaderResourceId, int fragmentShaderResourceId) {

        program = ShaderHelper.buildProgram(
                TextResourceReader
                .readTextFileFromResource(context, vertexShaderResourceId),
                TextResourceReader
                .readTextFileFromResource(context, fragmentShaderResourceId));

        // Retrieve uniform locations for the shader program.
        uModelLocation = glGetUniformLocation(program, U_MODEL);
        uViewLocation = glGetUniformLocation(program, U_VIEW);
        uProjectionLocation = glGetUniformLocation(program, U_PROJ);

        ErrorUtil.glErrorLog(TAG);

        // Retrieve attribute locations for the shader program.
        aPositionLocation = glGetAttribLocation(program, A_POSITION);
        aColorLocation = glGetAttribLocation(program, A_COLOR);

        ErrorUtil.glErrorLog(TAG);
    }

    public void setMatrixUniforms(float[] model, float[] view, float[] proj){
        glUniformMatrix4fv(uModelLocation, 1, false, model, 0);
        glUniformMatrix4fv(uViewLocation, 1, false, view, 0);
        glUniformMatrix4fv(uProjectionLocation, 1, false, proj, 0);

        ErrorUtil.glErrorLog(TAG);
    }
    
    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }

    public int getColorAttributeLocation() {
        return aColorLocation;
    }

    public void useProgram() {
        glUseProgram(program);
    }
}
