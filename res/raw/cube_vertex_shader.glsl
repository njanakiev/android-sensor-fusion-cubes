//#version 120
uniform mat4 u_Model;
uniform mat4 u_View;
uniform mat4 u_Proj;

attribute vec4 a_Position;
attribute vec4 a_Color;

varying vec4 v_Color;

void main()
{
    v_Color = a_Color;
    gl_Position = u_Proj * u_View * u_Model * a_Position;
    //gl_PointSize = 10.0;
}