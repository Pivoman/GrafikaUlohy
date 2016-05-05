package pr1;

import java.util.ArrayList;
import java.util.List;

import com.jogamp.opengl.GL2;

import transforms.Vec2D;
import utils.OGLBuffers;
import utils.ToFloatArray;
import utils.ToIntArray;

public class GeometryGenerator {
	public static OGLBuffers generateGrid(GL2 gl, String variable, int m, int n) {
		List<Vec2D> vertices = new ArrayList<>();
		for (int i = 0; i < m; i++)
			for (int j = 0; j < n; j++)
				vertices.add(new Vec2D((double) j / (n - 1), (double) i / (m - 1)));
		
		List<Integer> indices = new ArrayList<>();
		for (int i = 0; i < m - 1; i++)
			for (int j = 0; j < n - 1; j++) {
				indices.add(i * n + j);
				indices.add((i + 1) * n + j);
				indices.add(i * n + j + 1);
				indices.add((i + 1) * n + j);
				indices.add(i * n + j + 1);
				indices.add((i + 1) * n + j + 1);
			}

		OGLBuffers.Attrib[] attributes = { 
				new OGLBuffers.Attrib(variable, 2), 
		};
		return new OGLBuffers(gl, ToFloatArray.convert(vertices), attributes, ToIntArray.convert(indices));
	}

}
