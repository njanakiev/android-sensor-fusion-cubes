package util;

import android.util.Log;

import static android.opengl.GLES20.GL_INVALID_ENUM;
import static android.opengl.GLES20.GL_INVALID_FRAMEBUFFER_OPERATION;
import static android.opengl.GLES20.GL_INVALID_OPERATION;
import static android.opengl.GLES20.GL_INVALID_VALUE;
import static android.opengl.GLES20.GL_NO_ERROR;
import static android.opengl.GLES20.GL_OUT_OF_MEMORY;
import static android.opengl.GLES20.glGetError;

public class ErrorUtil {
    public static void glErrorLog(String tag){
        int errorCode = glGetError();

        if(errorCode != GL_NO_ERROR){
            String errorName;
            switch (errorCode){
                case GL_INVALID_ENUM:
                    errorName = "GL_INVALID_ENUM"; break;
                case GL_INVALID_VALUE:
                    errorName = "GL_INVALID_VALUE"; break;
                case GL_INVALID_OPERATION:
                    errorName = "GL_INVALID_OPERATION"; break;
                case GL_INVALID_FRAMEBUFFER_OPERATION:
                    errorName = "GL_INVALID_FRAMEBUFFER_OPERATION"; break;
                case GL_OUT_OF_MEMORY:
                    errorName = "GL_OUT_OF_MEMORY"; break;
                default:
                    errorName = "Unspecified Error : " + errorCode; break;
            }
            Log.e(tag, errorName + " occurred");
        }
    }
}
