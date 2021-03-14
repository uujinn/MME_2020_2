#include <stdio.h>
#include <gl/freeglut.h>


int Width = 500;
int Height = 500;
int startX[100], startY[100];
int finishX[100], finishY[100];
float r[100], g[100], b[100];
float linewidth[100];
int linerepeat[100];
long linepattern [100];
int i = 0, n = 0;
float xx =0 , yy = 0;


void Display();
void LineDisplay();
void MouseClick(int button, int state, int x, int y);
void MouseMotion(int x, int y);
void ColorMenu(int id);
void WidthMenu(int id);
void PatternMenu(int id);
void CreateMenu(int id);


int main(int argc, char **argv) {

	glutInit(&argc, argv);

	glutInitWindowSize(Width, Height);

	glutInitDisplayMode(GLUT_RGB);

	glutCreateWindow("선을 긋기 전에 모든 옵션을 선택해주세요.");

	Display();

	glutDisplayFunc(LineDisplay);
	glutMouseFunc(MouseClick);
	glutMotionFunc(MouseMotion);

	// 메뉴 생성
	GLint ColorMenuId = glutCreateMenu(ColorMenu);
	glutAddMenuEntry("Red", 0);
	glutAddMenuEntry("Green", 1);
	glutAddMenuEntry("Blue", 2);

	GLint WidthMenuId = glutCreateMenu(WidthMenu);
	glutAddMenuEntry("1.0", 0);
	glutAddMenuEntry("3.0", 1);
	glutAddMenuEntry("5.0", 2);

	GLint PatternMenuId = glutCreateMenu(PatternMenu);
	glutAddMenuEntry("Solid", 0);
	glutAddMenuEntry("Dotted", 1);


	GLint CreateMenuId = glutCreateMenu(CreateMenu);
	glutAddSubMenu("Color", ColorMenuId);
	glutAddSubMenu("Width", WidthMenuId);
	glutAddSubMenu("Pattern", PatternMenuId);
	glutAddMenuEntry("Exit", 3);

	glutAttachMenu(GLUT_RIGHT_BUTTON);

	glutMainLoop();

	return 0;
}

void Display() {
	// 색상 버퍼를 지울 색을 정한다.
	glClearColor(1.0f, 1.0f, 1.0f, 1.0f); 
	// 색상 버퍼를 지운다.
	glClear(GL_COLOR_BUFFER_BIT);

	glMatrixMode(GL_PROJECTION); 
	glLoadIdentity();	
	gluOrtho2D(0.0, Width, 0.0, Height);	

	glMatrixMode(GL_MODELVIEW); 
	glLoadIdentity();
}


void LineDisplay() {
	glClear(GL_COLOR_BUFFER_BIT);
	
	for (i = 0; i < n+1; i++) {
		glColor3f(r[i], g[i], b[i]);
		glLineWidth(linewidth[i]);
		glEnable(GL_LINE_STIPPLE);
		glLineStipple(linerepeat[i], linepattern[i]);
		glBegin(GL_LINES);
		{
			glVertex3f(startX[i], (Height - startY[i]), 0.0);
			glVertex3f(finishX[i], (Height - finishY[i]), 0.0);
		}
		glEnd();
		glDisable(GL_LINE_STIPPLE);
	}

	glColor3f(r[i], g[i], b[i]);
	glLineWidth(linewidth[i]);
	glEnable(GL_LINE_STIPPLE);
	glLineStipple(linerepeat[i], linepattern[i]);
	glBegin(GL_LINES);
	{
		glVertex3f(startX[i], (Height - startY[i]), 0.0);
		glVertex3f(xx, (Height - yy), 0.0);
	}
	glEnd();
	glDisable(GL_LINE_STIPPLE);

	glFinish();

}


void MouseClick(int button, int state, int x, int y) {

	if ((button == GLUT_LEFT_BUTTON) && (state == GLUT_DOWN)) {

		startX[i] = x;
		startY[i] = y;

	}



	if ((button == GLUT_LEFT_BUTTON) && (state == GLUT_UP)) {
		finishX[i] = x;
		finishY[i] = y;
		i++;
		n = i;
	}

}


void MouseMotion(int x, int y) {

	xx = x;
	yy = y;
	glutPostRedisplay();
}


void ColorMenu(int id) {

	if (id == 0)
		r[i] = 1.0, g[i] = 0.0, b[i] = 0.0;

	else if (id == 1)
		r[i] = 0.0, g[i] = 1.0, b[i] = 0.0;

	else if (id == 2)
		r[i] = 0.0, g[i] = 0.0, b[i] = 1.0;

}

void WidthMenu(int id) {

	if (id == 0) {
		linewidth[i] = 1.0;
	}

	else if (id == 1) {
		linewidth[i] = 3.0;

	}

	else if (id == 2) {
		linewidth[i] = 5.0;

	}
}

void PatternMenu(int id) {
	if (id == 0) {
		linerepeat[i] = 1;
		linepattern[i] = 0xFFFF;
	}
	else if (id == 1) {
		linerepeat[i] = 1;
		linepattern[i] = 0xF0F0;
	}
}

void CreateMenu(int id) {
	if (id == 3) {
		exit(0);
	}
}