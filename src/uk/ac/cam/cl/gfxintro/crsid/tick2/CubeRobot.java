package uk.ac.cam.cl.gfxintro.crsid.tick2;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.GL_NO_ERROR;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL11.glGetError;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUniform3f;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import java.lang.Math;
import java.nio.FloatBuffer;

public class CubeRobot {
	
    // Filenames for vertex and fragment shader source code
    private final static String VSHADER_FN = "resources/cube_vertex_shader.glsl";
    private final static String FSHADER_FN = "resources/cube_fragment_shader.glsl";
    
    // Reference to skybox of the scene
    public SkyBox skybox;
    
    // Components of this CubeRobot
    
    // Component 1 : Body
	private final Mesh body_mesh;
	private final ShaderProgram body_shader;
	private final Texture body_texture;
	private Matrix4f body_transform_T;
	private Matrix4f body_transform_M;

	// Component 2 : Right Arm
	private final Mesh rightArm_mesh;
	private final ShaderProgram rightArm_shader;
	private final Texture rightArm_texture;
	private Matrix4f rightArm_transform_T;
	private Matrix4f rightArm_transform_M;

	// Component 3 : Left Arm
	private final Mesh leftArm_mesh;
	private final ShaderProgram leftArm_shader;
	private final Texture leftArm_texture;
	private Matrix4f leftArm_transform_T;
	private Matrix4f leftArm_transform_M;

	// Component 4 : Right Leg
	private final Mesh rightLeg_mesh;
	private final ShaderProgram rightLeg_shader;
	private final Texture rightLeg_texture;
	private Matrix4f rightLeg_transform_T;
	private Matrix4f rightLeg_transform_M;

	// Component 5 : Left Leg
	private final Mesh leftLeg_mesh;
	private final ShaderProgram leftLeg_shader;
	private final Texture leftLeg_texture;
	private Matrix4f leftLeg_transform_T;
	private Matrix4f leftLeg_transform_M;

	// Component 6 : Head
	private final Mesh head_mesh;
	private final ShaderProgram head_shader;
	private final Texture head_texture;
	private Matrix4f head_transform_T;
	private Matrix4f head_transform_M;


	
/**
 *  Constructor
 *  Initialize all the CubeRobot components
 */
	public CubeRobot() {
		// Create Body
		body_mesh = new CubeMesh();
		body_shader = new ShaderProgram(new Shader(GL_VERTEX_SHADER, VSHADER_FN), new Shader(GL_FRAGMENT_SHADER, FSHADER_FN), "colour");
		body_shader.bindDataToShader("oc_position", body_mesh.vertex_handle, 3);
		body_shader.bindDataToShader("oc_normal", body_mesh.normal_handle, 3);
		body_shader.bindDataToShader("texcoord", body_mesh.tex_handle, 2);

		// Create Right Arm
		rightArm_mesh = new CubeMesh();
		rightArm_shader = new ShaderProgram(new Shader(GL_VERTEX_SHADER, VSHADER_FN), new Shader(GL_FRAGMENT_SHADER, FSHADER_FN), "colour");
		rightArm_shader.bindDataToShader("oc_position", rightArm_mesh.vertex_handle, 3);
		rightArm_shader.bindDataToShader("oc_normal", rightArm_mesh.normal_handle, 3);
		rightArm_shader.bindDataToShader("texcoord", rightArm_mesh.tex_handle, 2);

		// Create Left Arm
		leftArm_mesh = new CubeMesh();
		leftArm_shader = new ShaderProgram(new Shader(GL_VERTEX_SHADER, VSHADER_FN), new Shader(GL_FRAGMENT_SHADER, FSHADER_FN), "colour");
		leftArm_shader.bindDataToShader("oc_position", leftArm_mesh.vertex_handle, 3);
		leftArm_shader.bindDataToShader("oc_normal", leftArm_mesh.normal_handle, 3);
		leftArm_shader.bindDataToShader("texcoord", leftArm_mesh.tex_handle, 2);

		// Create Right Leg
		rightLeg_mesh = new CubeMesh();
		rightLeg_shader = new ShaderProgram(new Shader(GL_VERTEX_SHADER, VSHADER_FN), new Shader(GL_FRAGMENT_SHADER, FSHADER_FN), "colour");
		rightLeg_shader.bindDataToShader("oc_position", rightLeg_mesh.vertex_handle, 3);
		rightLeg_shader.bindDataToShader("oc_normal", rightLeg_mesh.normal_handle, 3);
		rightLeg_shader.bindDataToShader("texcoord", rightLeg_mesh.tex_handle, 2);

		// Create Left Leg
		leftLeg_mesh = new CubeMesh();
		leftLeg_shader = new ShaderProgram(new Shader(GL_VERTEX_SHADER, VSHADER_FN), new Shader(GL_FRAGMENT_SHADER, FSHADER_FN), "colour");
		leftLeg_shader.bindDataToShader("oc_position", leftLeg_mesh.vertex_handle, 3);
		leftLeg_shader.bindDataToShader("oc_normal", leftLeg_mesh.normal_handle, 3);
		leftLeg_shader.bindDataToShader("texcoord", leftLeg_mesh.tex_handle, 2);

		// Create Head
		head_mesh = new CubeMesh();
		head_shader = new ShaderProgram(new Shader(GL_VERTEX_SHADER, VSHADER_FN), new Shader(GL_FRAGMENT_SHADER, FSHADER_FN), "colour");
		head_shader.bindDataToShader("oc_position", head_mesh.vertex_handle, 3);
		head_shader.bindDataToShader("oc_normal", head_mesh.normal_handle, 3);
		head_shader.bindDataToShader("texcoord", head_mesh.tex_handle, 2);


		// Initialise Body Texturing
		body_texture = new Texture();
		body_texture.load("resources/cubemap.png");

		// Initialise Right Arm Texturing
		rightArm_texture = new Texture();
		rightArm_texture.load("resources/cubemap.png");

		// Initialise Left Arm Texturing
		leftArm_texture = new Texture();
		leftArm_texture.load("resources/cubemap.png");

		// Initialise Right Leg Texturing
		rightLeg_texture = new Texture();
		rightLeg_texture.load("resources/cubemap.png");

		// Initialise Left Leg Texturing
		leftLeg_texture = new Texture();
		leftLeg_texture.load("resources/cubemap.png");

		// Initialise Head Texturing
		head_texture = new Texture();
		head_texture.load("resources/cubemap_head.png");


		// Build Body Transformation Matrices
		Matrix4f body_transform_E = new Matrix4f();
		body_transform_E.scale(1f,2f,1f);
		body_transform_T = new Matrix4f();
		body_transform_M = body_transform_E.mul(body_transform_T);

		// Build Right Arm Transformation Matrices
		Matrix4f rightArm_transform_E = new Matrix4f();
		rightArm_transform_E.scale(0.2f,2,0.2f);
		rightArm_transform_T = new Matrix4f();
		rightArm_transform_T.translate(-1f, 1f, 0)
				.scale(5, 0.5f, 5)
				.rotateAffineXYZ(0, 0, -0.125f)
				.scale(0.2f, 2, 0.2f)
				.translate(1f, -1f, 0);
		rightArm_transform_T.translate(-6f, -0.05f, 0);
		rightArm_transform_M = rightArm_transform_E.mul(rightArm_transform_T);

		// Build Left Arm Transformation Matrices
		Matrix4f leftArm_transform_E = new Matrix4f();
		leftArm_transform_E.scale(0.2f,2,0.2f);
		leftArm_transform_T = new Matrix4f();
		leftArm_transform_T.translate(1f, 1f, 0)
				.scale(5, 0.5f, 5)
				.rotateAffineXYZ(0, 0, 0.125f)
				.scale(0.2f, 2, 0.2f)
				.translate(-1f, -1f, 0);
		leftArm_transform_T.translate(6f, -0.05f, 0);
		leftArm_transform_M = leftArm_transform_E.mul(leftArm_transform_T);

		// Build Right Leg Transformation Matrices
		Matrix4f rightLeg_transform_E = new Matrix4f();
		rightLeg_transform_E.scale(0.2f,2,0.2f);
		rightLeg_transform_T = new Matrix4f();
		rightLeg_transform_T.translate(-1f, 1f, 0)
				.scale(5, 0.5f, 5)
				.rotateAffineXYZ(0, 0, 0)
				.scale(0.2f, 2, 0.2f)
				.translate(1f, -1f, 0);
		rightLeg_transform_T.translate(2f, -2f, 0);
		rightLeg_transform_M = rightLeg_transform_E.mul(rightLeg_transform_T);

		// Build Left Leg Transformation Matrices
		Matrix4f leftLeg_transform_E = new Matrix4f();
		leftLeg_transform_E.scale(0.2f,2,0.2f);
		leftLeg_transform_T = new Matrix4f();
		leftLeg_transform_T.translate(-1f, 1f, 0)
				.scale(5, 0.5f, 5)
				.rotateAffineXYZ(0, 0, 0)
				.scale(0.2f, 2, 0.2f)
				.translate(1f, -1f, 0);
		leftLeg_transform_T.translate(-2f, -2f, 0);
		leftLeg_transform_M = leftLeg_transform_E.mul(leftLeg_transform_T);

		// Build Head Transformation Matrices
		Matrix4f head_transform_E = new Matrix4f();
		head_transform_E.scale(0.5f,0.5f,0.5f);
		head_transform_T = new Matrix4f();
		head_transform_T.translate(-1f, 1f, 0)
				.scale(5, 0.5f, 5)
				.rotateAffineXYZ(0, 0, 0)
				.scale(0.2f, 2, 0.2f)
				.translate(1f, -1f, 0);
		head_transform_T.translate(0, 5f, 0);
		head_transform_M = head_transform_E.mul(head_transform_T);

	}
	
	public void render(Camera camera, float deltaTime, long elapsedTime) {
		float angle = 0f;

		// Animate Body
		body_transform_T = new Matrix4f();
		body_transform_T.rotateAffineXYZ(0, 0.3f * deltaTime, 0);
		body_transform_M = body_transform_M.mul(body_transform_T);



		// Animate Right Arm
		rightArm_transform_T = new Matrix4f();
		rightArm_transform_T.translate(1f, 1f, 0).scale(5, 0.5f, 5).rotateAffineXYZ(0, 0, 0).scale(0.2f, 2, 0.2f).translate(-1f, -1f, 0);
		rightArm_transform_M = rightArm_transform_M.mulLocal(body_transform_T).mul(rightArm_transform_T);

		// Animate Left Arm
		leftArm_transform_T = new Matrix4f();
		//leftArm_transform_T.translate(1f, 1f, 0).scale(5, 0.5f, 5).rotateAffineXYZ(-0.02f, 0, 0).scale(0.2f, 2, 0.2f).translate(-1f, -1f, 0);
		leftArm_transform_M = leftArm_transform_M.mulLocal(body_transform_T).mul(leftArm_transform_T);

		// Animate Right Leg
		rightLeg_transform_T = new Matrix4f();
		rightLeg_transform_M = rightLeg_transform_M.mulLocal(body_transform_T).mul(rightLeg_transform_T);

		// Animate Left Leg
		leftLeg_transform_T = new Matrix4f();
		leftLeg_transform_M = leftLeg_transform_M.mulLocal(body_transform_T).mul(leftLeg_transform_T);

		// Animate Head
		head_transform_T = new Matrix4f();
		head_transform_M = head_transform_M.mulLocal(body_transform_T).mul(head_transform_T);


		// Render Body
		renderMesh(camera, body_mesh, body_transform_M, body_shader, body_texture);

		// Render Right Arm
		renderMesh(camera, rightArm_mesh, rightArm_transform_M, rightArm_shader, rightArm_texture);

		// Render Left Arm
		renderMesh(camera, leftArm_mesh, leftArm_transform_M, leftArm_shader, leftArm_texture);

		// Render Right Leg
		renderMesh(camera, rightLeg_mesh, rightLeg_transform_M, rightLeg_shader, rightLeg_texture);

		// Render Left Leg
		renderMesh(camera, leftLeg_mesh, leftLeg_transform_M, leftLeg_shader, leftLeg_texture);

		// Render Head
		renderMesh(camera, head_mesh, head_transform_M, head_shader, head_texture);
		
	}


	public void renderMesh(Camera camera, Mesh mesh , Matrix4f modelMatrix, ShaderProgram shader, Texture texture) {
		// If shaders modified on disk, reload them
		shader.reloadIfNeeded(); 
		shader.useProgram();

		// Step 2: Pass relevant data to the vertex shader
		
		// compute and upload MVP
		Matrix4f mvp_matrix = new Matrix4f(camera.getProjectionMatrix()).mul(camera.getViewMatrix()).mul(modelMatrix);
		shader.uploadMatrix4f(mvp_matrix, "mvp_matrix");
		
		// Upload Model Matrix and Camera Location to the shader for Phong Illumination
		shader.uploadMatrix4f(modelMatrix, "m_matrix");
		shader.uploadVector3f(camera.getCameraPosition(), "wc_camera_position");
		
		// Transformation by a nonorthogonal matrix does not preserve angles
		// Thus we need a separate transformation matrix for normals
		Matrix3f normal_matrix = new Matrix3f();
		normal_matrix = (modelMatrix.get3x3(normal_matrix).invert()).transpose();
		
		shader.uploadMatrix3f(normal_matrix, "normal_matrix");
		
		// Step 3: Draw our VertexArray as triangles
		// Bind Textures
		texture.bindTexture();
		shader.bindTextureToShader("tex", 0);
		skybox.bindCubemap();
		shader.bindTextureToShader("skybox", 1);
		// draw
		glBindVertexArray(mesh.vertexArrayObj); // Bind the existing VertexArray object
		glDrawElements(GL_TRIANGLES, mesh.no_of_triangles, GL_UNSIGNED_INT, 0); // Draw it as triangles
		glBindVertexArray(0);             // Remove the binding
		
        // Unbind texture
		texture.unBindTexture();
		skybox.unBindCubemap();
	}
}
