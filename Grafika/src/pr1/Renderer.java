package pr1;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;

import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

import transforms.Camera;
import transforms.Mat4;
import transforms.Mat4PerspRH;
import transforms.Vec3D;
import utils.OGLBuffers;
import utils.ShaderUtils;
import utils.ToFloatArray;
import utils.OGLTexture;

/**
* Ukazka pro praci s shadery v GLSL
* nacteni souboru s texturou, pouziti tridy OGLTexture
* upraveno pro JOGL 2.3.0 a vyssi
* 
* @author PGRF FIM UHK
* @version 2.0
* @since   2015-09-05 
*/

public class Renderer implements GLEventListener, MouseListener,
		MouseMotionListener, KeyListener {

	int width, height, ox, oy;

	OGLBuffers buffers, grid;

	int shaderProgram, shaderGrid, locMat, locMatGrid, locLightGrid;
	
	OGLTexture texture;

	Camera cam = new Camera();
	Mat4 proj;
	Vec3D lighPos = new Vec3D(4, 2, 1); //svìtlo, pošleme do uniformu ve VS

	public void init(GLAutoDrawable glDrawable) {
		GL2 gl = glDrawable.getGL().getGL2();

		//shaderProgram = ShaderUtils.loadProgram(gl, "./shader/glsl03/texture");
		shaderGrid = ShaderUtils.loadProgram(gl, "./shader/glsl03/grid");
		
		//createBuffers(gl);
		grid = GeometryGenerator.generateGrid(gl, "inPosition", 10, 10);

		//locMat = gl.glGetUniformLocation(shaderProgram, "mat");
		locMatGrid = gl.glGetUniformLocation(shaderGrid, "mat");
		locLightGrid = gl.glGetUniformLocation(shaderGrid, "lightPos");

		//texture = new OGLTexture(gl, "./textures/mosaic.jpg");
		
		cam.setPosition(new Vec3D(5, 5, 2.5));
		cam.setAzimuth(Math.PI * 1.25);
		cam.setZenith(Math.PI * -0.125);

		gl.glEnable(GL2.GL_DEPTH_TEST);
	}

	void createBuffers(GL2 gl) {
		float[] cube = {
				// dolni podstava
				0, 0, 0,	0, 0, -1,	0, 0, 
				1, 0, 0,	0, 0, -1,	1, 0, 
				1, 1, 0,	0, 0, -1,	1, 1, 
				0, 1, 0,	0, 0, -1,	0, 1,
				// horni podstava
				0, 0, 1,	0, 0, 1,	0, 0,
				1, 0, 1,	0, 0, 1,	1, 0,
				1, 1, 1,	0, 0, 1,	1, 1,
				0, 1, 1,	0, 0, 1,	0, 1,
				// stena x+
				1, 0, 0,	1, 0, 0,	0, 0,
				1, 1, 0,	1, 0, 0,	1, 0,
				1, 1, 1,	1, 0, 0,	1, 1,
				1, 0, 1,	1, 0, 0,	0, 1,
				// stena x-
				0, 0, 0,	-1, 0, 0,	0, 0,
				0, 1, 0,	-1, 0, 0,	1, 0,
				0, 1, 1,	-1, 0, 0,	1, 1,
				0, 0, 1,	-1, 0, 0,	0, 1,
				// stena y+
				0, 1, 0,	0, 1, 0,	0, 0,
				1, 1, 0,	0, 1, 0,	1, 0,
				1, 1, 1,	0, 1, 0,	1, 1,
				0, 1, 1,	0, 1, 0,	0, 1,
				// stena y-
				0, 0, 0,	0, -1, 0,	0, 0,
				1, 0, 0,	0, -1, 0,	1, 0,
				1, 0, 1,	0, -1, 0,	1, 1,
				0, 0, 1,	0, -1, 0, 	0, 1
		};

		OGLBuffers.Attrib[] attributes = {
				new OGLBuffers.Attrib("inPosition", 3),
				new OGLBuffers.Attrib("inNormal", 3),
				new OGLBuffers.Attrib("inTextureCoordinates", 2)
		};

		buffers = new OGLBuffers(gl, cube, attributes, null);
	}
	
	public void display(GLAutoDrawable glDrawable) {
		GL2 gl = glDrawable.getGL().getGL2();
		//glClearColor - barva pozadi
		gl.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
		//krychle
		//gl.glUseProgram(shaderProgram); 
		//gl.glUniformMatrix4fv(locMat, 1, false,
		//		ToFloatArray.convert(cam.getViewMatrix().mul(proj)), 0);
		
		//texture.bind(shaderProgram, "texture", 0);
		
		//buffers.draw(GL2.GL_QUADS, shaderProgram);

		//grid
		gl.glUseProgram(shaderGrid);
		gl.glUniformMatrix4fv(locMatGrid, 1, false,
				ToFloatArray.convert(cam.getViewMatrix().mul(proj)), 0);
		gl.glUniform3fv(locLightGrid, 1, ToFloatArray.convert(lighPos), 0);
		grid.draw(GL2.GL_TRIANGLES, shaderGrid);
	}

	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		this.width = width;
		this.height = height;
		proj = new Mat4PerspRH(Math.PI / 4, height / (double) width, 0.01, 1000.0);
	}

	public void displayChanged(GLAutoDrawable drawable, boolean modeChanged,
			boolean deviceChanged) {
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
		ox = e.getX();
		oy = e.getY();
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseDragged(MouseEvent e) {
		cam.addAzimuth((double) Math.PI * (ox - e.getX())
				/ width);
		cam.addZenith((double) Math.PI * (e.getY() - oy)
				/ width);
		ox = e.getX();
		oy = e.getY();
	}

	public void mouseMoved(MouseEvent e) {
	}

	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_W:
			cam.forward(1);
			break;
		case KeyEvent.VK_D:
			cam.right(1);
			break;
		case KeyEvent.VK_S:
			cam.backward(1);
			break;
		case KeyEvent.VK_A:
			cam.left(1);
			break;
		case KeyEvent.VK_SHIFT:
			cam.down(1);
			break;
		case KeyEvent.VK_CONTROL:
			cam.up(1);
			break;
		case KeyEvent.VK_SPACE:
			cam.setFirstPerson(!cam.getFirstPerson());
			break;
		case KeyEvent.VK_R:
			cam.mulRadius(0.9f);
			break;
		case KeyEvent.VK_F:
			cam.mulRadius(1.1f);
			break;
		}
	}

	public void keyReleased(KeyEvent e) {
	}

	public void keyTyped(KeyEvent e) {
	}

	public void dispose(GLAutoDrawable arg0) {
	}
}