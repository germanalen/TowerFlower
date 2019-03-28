uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;
uniform mat4 normalM4;


attribute vec3 vpos;
attribute vec3 vcolor;
attribute vec3 vnormal;

varying vec3 fcolor;
varying vec3 fnormal;


void main() {
    gl_Position = projection * view * model * vec4(vpos, 1);

    fcolor = vcolor;
    fnormal = mat3(normalM4) * vnormal;
}