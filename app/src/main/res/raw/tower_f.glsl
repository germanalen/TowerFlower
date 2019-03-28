precision mediump float;

varying vec3 fcolor;
varying vec3 fnormal;

void main() {
    vec3 temp = fcolor + fnormal;
    gl_FragColor = vec4(fcolor + fnormal*0.01, 1);
}
