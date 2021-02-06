#version 140

#define PI 3.1415926535897932384626433832795

in vec3 wc_frag_normal;        	// fragment normal in world coordinates (wc_)
in vec2 frag_texcoord;			// texture UV coordinates
in vec3 wc_frag_pos;			// fragment position in world coordinates

out vec3 color;			        // pixel colour

uniform sampler2D tex;  		  // 2D texture sampler
uniform samplerCube skybox;		  // Cubemap texture used for reflections
uniform vec3 wc_camera_position;  // Position of the camera in world coordinates

// Tone mapping and display encoding combined
vec3 tonemap(vec3 linearRGB)
{
    float L_white = 0.7; // Controls the brightness of the image

    float inverseGamma = 1./2.2;
    return pow(linearRGB/L_white, vec3(inverseGamma)); // Display encoding - a gamma
}



void main()
{
	vec3 linear_color = vec3(0, 0, 0);
    vec3 point_light_1_location = vec3(2, 2, 3);
    vec3 point_light_1_colour = vec3(1, 1, 1);
    float point_light_1_intensity = 10;
    vec3 incident_direction = normalize(wc_frag_pos - point_light_1_location);
    vec3 viewing_direction = normalize(wc_frag_normal-wc_camera_position);
    vec3 inverted_incident = normalize(wc_frag_pos - wc_camera_position);
    vec3 reflected_ray = normalize(reflect(inverted_incident, normalize(wc_frag_normal)));


    vec3 I_a = vec3(0.3, 0.2, 0.1);
    vec3 C_diff = vec3(texture(tex, frag_texcoord));

    vec3 ambient = I_a * C_diff;

    float k_d = 0.3;
    float k_s = 0.6;
    float alpha = 0.5;

    float distanceToLight = length(point_light_1_location - wc_frag_pos);
    vec3 C_spec = point_light_1_colour;
    vec3 I = point_light_1_colour * (point_light_1_intensity / (PI * 4 * pow(distanceToLight, 2)));

    vec3 unitL_i = normalize(point_light_1_location - wc_frag_pos);
    vec3 unitN = normalize(wc_frag_normal);
    float NdotL = dot(unitL_i, unitN);
    vec3 diffuse = C_diff * k_d * I * max(0, NdotL);

    vec3 unitR = reflect(unitL_i, unitN);
    vec3 unitV = normalize(wc_camera_position - wc_frag_pos);
    float RdotV = dot(unitR, unitV);
    vec3 specular = C_spec * k_s * I * pow((max(0, RdotV)), alpha);
    linear_color = ambient + diffuse + specular + texture(skybox, reflected_ray).rgb;

	color = tonemap(linear_color);
}

