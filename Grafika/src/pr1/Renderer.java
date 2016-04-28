package pr1;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;

import com.jogamp.common.nio.Buffers;

import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class Renderer implements GLEventListener, MouseListener,
		MouseMotionListener, KeyListener {

	int width, height;

	int[] vertexBuffer = new int[1], indexBuffer = new int[1];

	int shaderProgram;

	public void init(GLAutoDrawable glDrawable) {
		GL2 gl = glDrawable.getGL().getGL2();

		// overeni podpory shaderu
		String extensions = gl.glGetString(GL2.GL_EXTENSIONS);
		if (extensions.indexOf("GL_ARB_vertex_shader") == -1
				|| extensions.indexOf("GL_ARB_fragment_shader") == -1) {
			throw new RuntimeException("Shaders not available.");
		}

		createBuffers(gl);
		createShaders(gl);
	}
	
	void createBuffers(GL2 gl) {
		// vytvoreni a naplneni vertex bufferu
		float[] vertexBufferData = {
			-1, -1, 
			1, 0, 
			0, 1 
		};
		FloatBuffer vertexBufferBuffer = Buffers
				.newDirectFloatBuffer(vertexBufferData); // kvuli predani dat do
															// nativni knihovny
		gl.glGenBuffers(1, vertexBuffer, 0);
		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, vertexBuffer[0]);
		gl.glBufferData(GL2.GL_ARRAY_BUFFER, vertexBufferData.length * 4,
				vertexBufferBuffer, GL2.GL_STATIC_DRAW);

		// vytvoreni a naplneni index bufferu (element buffer podle OpenGL)
		short[] indexBufferData = { 0, 1, 2 };
		ShortBuffer indexBufferBuffer = Buffers
				.newDirectShortBuffer(indexBufferData); // kvuli predani dat do
														// nativni knihovny
		gl.glGenBuffers(1, indexBuffer, 0);
		gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, indexBuffer[0]);
		gl.glBufferData(GL2.GL_ELEMENT_ARRAY_BUFFER,
				indexBufferData.length * 2, indexBufferBuffer,
				GL2.GL_STATIC_DRAW);
	}

	void createShaders(GL2 gl) {
		String shaderVertSrc[] = { 
			"#version 330\n",
			"in vec2 inPosition;", // vstup z vertex bufferu
			"void main() {", 
			"	vec2 position = inPosition;",
			"   position.x += 0.1;",
			" 	gl_Position = vec4(position, 0.0, 1.0);", 
			"}" 
		};
		// gl_Position - vestavena vystupni promenna pro pozici vrcholu
		// pred orezanim w a dehomogenizaci, musi byt naplnena

		String shaderFragSrc[] = { 
			"#version 330\n",
			"out vec4 outColor;", // vystup z fragment shaderu
			"void main() {",
			" 	outColor = vec4(0.5,0.1,0.8, 1.0);", 
			"}" 
		};

		// vertex shader
		int vs = gl.glCreateShader(GL2.GL_VERTEX_SHADER);
		gl.glShaderSource(vs, shaderVertSrc.length, shaderVertSrc,
				(int[]) null, 0);
		gl.glCompileShader(vs);
		System.out.println(checkLogInfo(gl, vs, GL2.GL_COMPILE_STATUS));

		// fragment shader
		int fs = gl.glCreateShader(GL2.GL_FRAGMENT_SHADER);
		gl.glShaderSource(fs, shaderFragSrc.length, shaderFragSrc,
				(int[]) null, 0);
		gl.glCompileShader(fs);
		System.out.println(checkLogInfo(gl, fs, GL2.GL_COMPILE_STATUS));

		// sestaveni programu
		shaderProgram = gl.glCreateProgram();
		gl.glAttachShader(shaderProgram, vs);
		gl.glAttachShader(shaderProgram, fs);
		gl.glLinkProgram(shaderProgram);
		System.out.println(checkLogInfo(gl, shaderProgram, GL2.GL_LINK_STATUS));
	}

	void bindBuffers(GL2 gl) {
		int locPosition = gl.glGetAttribLocation(shaderProgram, "inPosition"); // interni
																				// identifikace
																				// shaderove
																				// promenne
		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, vertexBuffer[0]);
		gl.glVertexAttribPointer(locPosition, 2, GL2.GL_FLOAT, false, 8, 0);
		gl.glEnableVertexAttribArray(locPosition);
		gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, indexBuffer[0]);
	}

	public void display(GLAutoDrawable glDrawable) {
		GL2 gl = glDrawable.getGL().getGL2();
		gl.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

		// nastaveni aktualniho shaderu, v teto ukazce nadbytecne
		gl.glUseProgram(shaderProgram); // pro pouziti vychoziho shaderu
										// "fixni pajplajny" slouzi
										// gl.glUseProgram(0);

		// nastaveni bufferu k vykresleni, v teto ukazce by stacilo jednou
		bindBuffers(gl);
		// vykresleni
		gl.glDrawElements(GL2.GL_TRIANGLES, 3, GL2.GL_UNSIGNED_SHORT, 0);

	}

	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		this.width = width;
		this.height = height;
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
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseDragged(MouseEvent e) {
	}

	public void mouseMoved(MouseEvent e) {
	}

	public void keyPressed(KeyEvent e) {
	}

	public void keyReleased(KeyEvent e) {
	}

	public void keyTyped(KeyEvent e) {
	}

	public void dispose(GLAutoDrawable arg0) {
	}

	static private String checkLogInfo(GL2 gl, int programObject, int mode) {
		switch (mode) {
		case GL2.GL_COMPILE_STATUS:
			return checkLogInfoShader(gl, programObject, mode);
		case GL2.GL_LINK_STATUS:
		case GL2.GL_VALIDATE_STATUS:
			return checkLogInfoProgram(gl, programObject, mode);
		default:
			return "Unsupported mode.";
		}
	}

	static private String checkLogInfoShader(GL2 gl, int programObject, int mode) {
		int[] error = new int[] { -1 };
		gl.glGetShaderiv(programObject, mode, error, 0);
		if (error[0] != GL2.GL_TRUE) {
			int[] len = new int[1];
			gl.glGetShaderiv(programObject, GL2.GL_INFO_LOG_LENGTH, len, 0);
			if (len[0] == 0) {
				return null;
			}
			byte[] errorMessage = new byte[len[0]];
			gl.glGetShaderInfoLog(programObject, len[0], len, 0, errorMessage,
					0);
			return new String(errorMessage, 0, len[0]);
		}
		return null;
	}

	static private String checkLogInfoProgram(GL2 gl, int programObject, int mode) {
		int[] error = new int[] { -1 };
		gl.glGetProgramiv(programObject, mode, error, 0);
		if (error[0] != GL2.GL_TRUE) {
			int[] len = new int[1];
			gl.glGetProgramiv(programObject, GL2.GL_INFO_LOG_LENGTH, len, 0);
			if (len[0] == 0) {
				return null;
			}
			byte[] errorMessage = new byte[len[0]];
			gl.glGetProgramInfoLog(programObject, len[0], len, 0, errorMessage,
					0);
			return new String(errorMessage, 0, len[0]);
		}
		return null;
	}
}