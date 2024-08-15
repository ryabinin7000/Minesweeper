//Класс, отвечающий за внешность приложения

package MinesweeperDemo;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;

public class Display implements GLEventListener {

    public static GL2 gl = null; //Основная переменная для работы с окном

    //Базовая настройка окна. Вызывается один раз при запуске приложения
    public void init(GLAutoDrawable glAutoDrawable) {
        gl = glAutoDrawable.getGL().getGL2();
        gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
        gl.glEnable(GL2.GL_TEXTURE_2D);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(-Renderer.getCellSide(),
                Renderer.getScreenWidth() - Renderer.getCellSide(),
                Renderer.getCellSide()*(Logic.getGridHeight()+1),
                -Renderer.getCellSide()*4,-1,1);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        Renderer.render();
        Renderer.drawRestart();
    }

    //Вызывается при закрытии окна
    public void dispose(GLAutoDrawable glAutoDrawable) {

    }

    //Вызывается на каждый кадр
    public void display(GLAutoDrawable glAutoDrawable) {
        gl = glAutoDrawable.getGL().getGL2();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
        Renderer.render();
        Renderer.drawRestart();
    }

    //Вызывается при изменении размеров окна (в данном проекте эта опция запрещена)
    public void reshape(GLAutoDrawable glAutoDrawable, int x, int y, int width, int height) {

    }
}