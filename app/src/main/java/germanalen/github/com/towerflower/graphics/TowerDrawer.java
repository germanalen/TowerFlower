package germanalen.github.com.towerflower.graphics;

import germanalen.github.com.towerflower.R;

import android.content.res.Resources;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import germanalen.github.com.towerflower.MainActivity;

public class TowerDrawer {

    private int shader;
    private FloatBuffer vertexBuffer;

    private float phi = 0;
    private int numVertices = 0;

    private double[] dna = new double[DNA_SIZE];


    public void setDna(double[] dna) {
        if (dna.length != DNA_SIZE)
            throw new RuntimeException("Wrong dna size");
        this.dna = dna;
    }


    public TowerDrawer() {

    }


    // call after surface is created
    public void init(Resources resources) {
        String vertexShaderCode = "";
        String fragmentShaderCode = "";
        try (InputStream is = resources.openRawResource(R.raw.tower_v)) {
            vertexShaderCode = readString(is);
        } catch (IOException ex) {
            Log.e(MainActivity.TAG, ex.getMessage());
        }
        try (InputStream is = resources.openRawResource(R.raw.tower_f)) {
            fragmentShaderCode = readString(is);
        } catch (IOException ex) {
            Log.e(MainActivity.TAG, ex.getMessage());
        }

        // prepare shaders and OpenGL program
        int vertexShader = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        shader = GLES20.glCreateProgram();             // create empty OpenGL Program
        GLES20.glAttachShader(shader, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(shader, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(shader);                  // create OpenGL program executables
        generateMesh();
    }


    public void draw(float[] viewMatrix, float[] projectionMatrix) {
        boolean err = GLES20.glIsProgram(shader);
        GLES20.glUseProgram(shader);

        phi += 1;

        int vpos = GLES20.glGetAttribLocation(shader, "vpos");
        int vcolor = GLES20.glGetAttribLocation(shader, "vcolor");
        int vnormal = GLES20.glGetAttribLocation(shader, "vnormal");

        int byteStride = 3/*num attributes*/ * (3 * 4)/*sizeof(vec3)*/;


        vertexBuffer.position(0);
        GLES20.glEnableVertexAttribArray(vpos);
        GLES20.glVertexAttribPointer(
                vpos, 3,
                GLES20.GL_FLOAT, false,
                byteStride, vertexBuffer);


        vertexBuffer.position(3);
        GLES20.glEnableVertexAttribArray(vcolor);
        GLES20.glVertexAttribPointer(
                vcolor, 3,
                GLES20.GL_FLOAT, false,
                byteStride, vertexBuffer);


        vertexBuffer.position(2 * 3);
        GLES20.glEnableVertexAttribArray(vnormal);
        GLES20.glVertexAttribPointer(
                vnormal, 3,
                GLES20.GL_FLOAT, false,
                byteStride, vertexBuffer);




        float[] modelMatrix = new float[16];
        Matrix.setRotateM(modelMatrix, 0, phi, 0, 1, 0);
        float[] normalMatrix4 = new float[16];
        Matrix.invertM(normalMatrix4, 0, modelMatrix, 0);
        Matrix.transposeM(normalMatrix4, 0, normalMatrix4, 0);

        int model = GLES20.glGetUniformLocation(shader, "model");
        int view = GLES20.glGetUniformLocation(shader, "view");
        int projection = GLES20.glGetUniformLocation(shader, "projection");
        int normalM4 = GLES20.glGetUniformLocation(shader, "normalM4");



        GLES20.glUniformMatrix4fv(model, 1, false, modelMatrix, 0);
        GLES20.glUniformMatrix4fv(view, 1, false, viewMatrix, 0);
        GLES20.glUniformMatrix4fv(projection, 1, false, projectionMatrix, 0);
        GLES20.glUniformMatrix4fv(normalM4, 1, false, normalMatrix4, 0);


        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, numVertices);
    }

    public static final int DNA_SIZE = 5;

    public void generateMesh() {
        // {vx, vy, vz, r, g, b, nx, ny, nz, ...}
        ArrayList<Float> v = new ArrayList<>();

        int numY = (int)(5 + dna[0] * 20);
        int numP = (int)(5 + dna[1] * 20);

        float freq = (float)dna[2] * 10;
        float minR = (float)dna[3] + 0.1f;
        float offR = (float)dna[4];


        float height = 4;


        numVertices = 0;
        // generating
        for (int y = 0; y < numY; ++y) {
            for (int p = 0; p < numP; ++p) {
                float y0 = (y / ((float)numY) - 0.5f) * height;
                float y1 = ((y+1) / ((float)numY) - 0.5f) * height;
                float radius0 = ((float)Math.sin(y0 * freq) * 0.5f + 0.5f) * offR + minR;
                float radius1 = ((float)Math.sin(y1 * freq) * 0.5f + 0.5f) * offR + minR;

                float p0 = (p / ((float)numP)) * (float)Math.PI * 2;
                float p1 = ((p+1) / ((float)numP)) * (float)Math.PI * 2;
                float x0 = (float)Math.cos(p0);
                float x1 = (float)Math.cos(p1);
                float z0 = (float)Math.sin(p0);
                float z1 = (float)Math.sin(p1);

                // y0 p0 | x0 y0 z0
                // y0 p1 | x1 y0 z1
                // y1 p1 | x1 y1 z1

                // y1 p1 | x1 y1 z1
                // y1 p0 | x0 y1 z0
                // y0 p0 | x0 y0 z0
                
                v.add(x0*radius0);v.add(y0);v.add(z0*radius0); v.add(1f);v.add(0f);v.add(0f); v.add(0f);v.add(0f);v.add(0f);
                v.add(x1*radius0);v.add(y0);v.add(z1*radius0); v.add(1f);v.add(1f);v.add(0f); v.add(0f);v.add(0f);v.add(0f);
                v.add(x1*radius1);v.add(y1);v.add(z1*radius1); v.add(0f);v.add(1f);v.add(0f); v.add(0f);v.add(0f);v.add(0f);

                v.add(x1*radius1);v.add(y1);v.add(z1*radius1); v.add(0f);v.add(1f);v.add(0f); v.add(0f);v.add(0f);v.add(0f);
                v.add(x0*radius1);v.add(y1);v.add(z0*radius1); v.add(0f);v.add(0f);v.add(1f); v.add(0f);v.add(0f);v.add(0f);
                v.add(x0*radius0);v.add(y0);v.add(z0*radius0); v.add(1f);v.add(0f);v.add(0f); v.add(0f);v.add(0f);v.add(0f);
                numVertices += 6;
            }
        }


        // saving to vertexBuffer
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(v.size() * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        FloatBuffer newVertexBuffer = byteBuffer.asFloatBuffer();

        float[] verticesArr = new float[v.size()];
        for (int i = 0; i < v.size(); ++i)
            verticesArr[i] = v.get(i);


        try {
            newVertexBuffer.put(verticesArr, 0, verticesArr.length);
            vertexBuffer = newVertexBuffer;
        } catch (BufferOverflowException ex) {
            Log.e(MainActivity.TAG, "Vertex buffer failed to update");
        }
    }


    private String readString(InputStream is) throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(is));
        StringBuilder total = new StringBuilder();
        for (String line; (line = r.readLine()) != null; ) {
            total.append(line).append('\n');
        }
        return total.toString();
    }
}
