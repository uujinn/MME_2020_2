#include <stdio.h>
#include <gl/freeglut.h>
#include <math.h>

int Width = 600;
int Height = 600;


void DrawEllipse(float cx, float cy, float a, float b);
void DrawCircle(float cx, float cy, float r);
void DrawsemiCircle(float startangle, float finishangle, float cx, float cy, float r);

void Render() {

	glClearColor(0.529412, 0.807843, 0.98039, 1);

	glClear(GL_COLOR_BUFFER_BIT);

	glMatrixMode(GL_PROJECTION); 
	glLoadIdentity();		
	gluOrtho2D(-10, 10, -10, 10);	

	glMatrixMode(GL_MODELVIEW); 

	// 물고기 꼬리
	glBegin(GL_TRIANGLES);
	{
		glColor3d(0.933333, 0.909804, 0.666667);
		glVertex2d(-5.5, 2.2);
		glColor3d(0.0, 1.0, 0.0);
		glVertex2d(-7, 2.7);
		glColor3d(0.0, 0.0, 1.0);
		glVertex2d(-7, 1.7);

	}
	glEnd();

	// 물고기 얼굴
	glColor3d(0.933333, 0.909804, 0.666667);
	DrawEllipse(-3.5, 2.2, 2.3, 1.3);

	// 물고기 눈
	glColor3d(0, 0, 0);
	DrawCircle(-1.8, 2.5, 0.13);



	//물고기 지느러미
	glBegin(GL_TRIANGLES);
	{
		glColor3d(0.933333, 0.909804, 0.666667);
		glVertex2d(-3, 2.2);
		glColor3d(0.0, 1.0, 0.0);
		glVertex2d(-3.5, 2.4);
		glColor3d(0.0, 0.0, 1.0);
		glVertex2d(-3.5, 2.0);

	}
	glEnd();


	// 물고기 물거품
	glColor3d(0.78431, 1, 1);
	DrawCircle(-0.6, 2.3, 0.2);
	glColor3d(0.78431, 1, 1);
	DrawCircle(-0.6, 3.05, 0.27);
	glColor3d(0.78431, 1, 1);
	DrawCircle(-0.6, 3.9, 0.34);

	// 오징어 머리 1
	glBegin(GL_TRIANGLES);
	{
		glColor3d(0.815686, 0.12549, 0.564706);
		glVertex2d(5.5, 7);
		glColor3d(0.980392, 0.501961, 0.447059);
		glVertex2d(2.9, 3);
		glColor3d(0.980392, 0.501961, 0.447059);
		glVertex2d(8.1, 3);

	}
	glEnd();

	// 오징어 머리 2
	glBegin(GL_TRIANGLES);
	{
		glColor3d(0.815686, 0.12549, 0.564706);
		glVertex2d(5.5, 7.5);
		glColor3d(0.980392, 0.501961, 0.447059);
		glVertex2d(3.5, 5);
		glColor3d(0.980392, 0.501961, 0.447059);
		glVertex2d(7.5, 5);

	}
	glEnd();

	// 오징어 다리1
	glBegin(GL_QUADS);
	{
		glColor3d(0.815686, 0.12549, 0.564706);
		glVertex2d(5.3, 3);
		glColor3d(0.980392, 0.501961, 0.447059);
		glVertex2d(5.3, 1.3);
		glColor3d(0.980392, 0.501961, 0.447059);
		glVertex2d(5.8, 1.3);
		glColor3d(0.815686, 0.12549, 0.564706);
		glVertex2d(5.8, 3);
	}
	glEnd();

	// 오징어 다리2
	glBegin(GL_POLYGON);
	{
		glColor3d(0.815686, 0.12549, 0.564706);
		glVertex2d(3.9, 3);
		glColor3d(0.980392, 0.501961, 0.447059);
		glVertex2d(3.4, 1.3);
		glColor3d(0.980392, 0.501961, 0.447059);
		glVertex2d(3.9, 1.3);
		glColor3d(0.815686, 0.12549, 0.564706);
		glVertex2d(4.4, 3);
	}
	glEnd();

	// 오징어 다리3
	glBegin(GL_POLYGON);
	{
		glColor3d(0.815686, 0.12549, 0.564706);
		glVertex2d(6.7, 3);
		glColor3d(0.980392, 0.501961, 0.447059);
		glVertex2d(7.2, 1.3);
		glColor3d(0.980392, 0.501961, 0.447059);
		glVertex2d(7.7, 1.3);
		glColor3d(0.815686, 0.12549, 0.564706);
		glVertex2d(7.2, 3);
	}
	glEnd();

	// 오징어 왼눈
	glColor3d(0, 0, 0);
	DrawCircle(4.8, 4, 0.2);
	// 오징어 오른눈
	glColor3d(0, 0, 0);
	DrawCircle(6.2, 4, 0.2);

	// 오징어 입
	glBegin(GL_TRIANGLES);
	{
		glColor3d(1, 0, 0);
		glVertex2d(5.5, 3.3);
		glColor3d(1, 0, 0);
		glVertex2d(5.25, 3.6);
		glColor3d(1, 0, 0);
		glVertex2d(5.75, 3.6);

	}
	glEnd();

	// 땅
	glBegin(GL_QUADS);
	{
		glColor3d(0.956863, 0.643137, 0.376471);
		glVertex2d(-10, -7);
		glColor3d(0.545098, 0.270588, 0.0745);
		glVertex2d(-10, -10);
		glColor3d(0.545098, 0.270588, 0.0745);
		glVertex2d(10, -10);
		glColor3d(0.956863, 0.643137, 0.376471);
		glVertex2d(10, -7);
	}
	glEnd();

	// 불가사리
	glBegin(GL_TRIANGLES);
	{
		glColor3d(1, 0.713725, 0.756863);


		glVertex2d(-6.7, -5.2);
		glColor3d(0.576471, 0.439216, 0.85882);

		glVertex2d(-2.3, -7.5);
		glColor3d(1, 0.713725, 0.756863);
		glVertex2d(-3, -5.2);
	}
	glEnd();
	glBegin(GL_TRIANGLES);
	{
		glColor3d(1, 0.713725, 0.756863);
		glVertex2d(-5, -5.2);
		glColor3d(0.576471, 0.439216, 0.85882);
		glVertex2d(-5.7, -7.5);
		glColor3d(1, 0.713725, 0.756863);
		glVertex2d(-1.3, -5.2);

	}
	glEnd();
	glBegin(GL_TRIANGLES);
	{
		glColor3d(0.576471, 0.439216, 0.85882);
		glVertex2d(-4, -3.8);
		glColor3d(1, 0.713725, 0.756863);
		glVertex2d(-4.8, -5.2);
		glColor3d(1, 0.713725, 0.756863);
		glVertex2d(-3.2, -5.2);

	}
	glEnd();

	// 불가사리 왼눈
	glColor3d(0, 0, 0);
	DrawCircle(-3.5, -5.7, 0.15);
	// 불가사리 오른눈
	glColor3d(0, 0, 0);
	DrawCircle(-4.5, -5.7, 0.15);

	// 불가사리 입
	glBegin(GL_TRIANGLES);
	{
		glColor3d(1, 0, 0);
		glVertex2d(-4, -6.5);
		glColor3d(1, 0, 0);
		glVertex2d(-4.25, -6.05);
		glColor3d(1, 0, 0);
		glVertex2d(-3.75, -6.05);

	}
	glEnd();

	// 돌 
	glColor3d(0.411765, 0.411765, 0.411765);
	DrawsemiCircle(0, 180, 5, -7.2, 1.5);
	glColor3d(0.745098, 0.745098, 0.745098);
	DrawsemiCircle(0, 180, 7, -7.4, 1.5);
	glFinish();

}

// 타원 그리기
void DrawEllipse(float cx, float cy, float a, float b)
{
	int i;

	glBegin(GL_TRIANGLE_FAN);

	for (i = 0; i < 360; i++)
	{
		glVertex2f(cx + a * cos(i), cy + b * sin(i));

	}
	glEnd();
}


// 원 그리기
void DrawCircle(float cx, float cy, float r) {
	glBegin(GL_POLYGON);
	int i;
	for (i = 0; i < 360; i++) {
		float theta = 2 * 3.141592 * i / 360;
		float x = r * cosf(theta);
		float y = r * sinf(theta);
		glVertex2f(x + cx, y + cy);
	}
	glEnd();
}

// 반원 그리기
void DrawsemiCircle(float startangle, float finishangle, float cx, float cy, float r) {
	glBegin(GL_POLYGON);
	int i;
	for (i = startangle; i < finishangle; i++) {
		float theta = 2 * 3.141592 * i / 360;
		float x = r * cosf(theta);
		float y = r * sinf(theta);
		glVertex2f(x + cx, y + cy);
	}
	glEnd();
}

void Reshape(int width, int height) { // width = 800, height = 600

	glViewport(0, 0, width, height);

}



int main(int argc, char **argv) {

	glutInit(&argc, argv);

	glutInitWindowSize(Width, Height);

	glutInitDisplayMode(GLUT_RGB);

	glutCreateWindow("Assignment1_2019112541");

	glutDisplayFunc(Render);

	glutReshapeFunc(Reshape);

	glutMainLoop();

	return 0;
}