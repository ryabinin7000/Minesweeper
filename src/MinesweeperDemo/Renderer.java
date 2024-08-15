//Класс, отвечающий за инициализацию окна и отрисовку на нём

package MinesweeperDemo;

import com.jogamp.nativewindow.WindowClosingProtocol;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.texture.Texture;
import img.ImageResource;
import java.util.HashMap;

public class Renderer {

    private static GLProfile profile = null; //Базовый объект JOGL, наследуется от класса Object
    private static GLWindow window = null; //Окно

    private static final int cellSide = 50; //Длина стороны ячейки в пикселях
    private static final int screenWidth = cellSide*(Logic.getGridWidth()+2); //Ширина экрана
    private static final int screenHeight = cellSide*(Logic.getGridHeight()+5); //Высота экрана

    //Словарь для сопоставления состояний ячейки и путей к соотв. изображениям
    private static final HashMap<Byte, String> conditionCell;
    static {
        conditionCell = new HashMap<>();
        conditionCell.put((byte) 0, "opened.bmp");
        conditionCell.put((byte) 1, "1.bmp");
        conditionCell.put((byte) 2, "2.bmp");
        conditionCell.put((byte) 3, "3.bmp");
        conditionCell.put((byte) 4, "4.bmp");
        conditionCell.put((byte) 5, "5.bmp");
        conditionCell.put((byte) 6, "6.bmp");
        conditionCell.put((byte) 7, "7.bmp");
        conditionCell.put((byte) 8, "8.bmp");
        conditionCell.put((byte) 9, "cell.bmp");
        conditionCell.put((byte) 10, "flagged.bmp");
        conditionCell.put((byte) 11, "bombed.bmp");
        conditionCell.put((byte) 12, "bomb.bmp");
        conditionCell.put((byte) 13, "wrongflagged.bmp");
    }

    //Подобный словарь, но для иконки сапера
    private static final HashMap<Byte, String> conditionGame;
    static {
        conditionGame = new HashMap<>();
        conditionGame.put((byte) 0, "minesweeper.bmp");
        conditionGame.put((byte) 1, "minesweeperWin.bmp");
        conditionGame.put((byte) 2, "minesweeperLose.bmp");
    }

    //Геттеры
    public static GLProfile getProfile() {
        return profile;
    }
    public static GLWindow getWindow(){
        return window;
    }
    public static int getCellSide(){
        return cellSide;
    }
    public static int getScreenWidth(){
        return screenWidth;
    }

    //Метод, отвечающий за инициализацию проекта. Выполняется в самом начале
    public static void init() {
        GLProfile.initSingleton();
        profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);
        window = GLWindow.create(capabilities);
        window.setSize(screenWidth, screenHeight);
        window.setTitle("Minesweeper");
        window.setResizable(false);
        window.setDefaultCloseOperation(WindowClosingProtocol.WindowClosingMode.DISPOSE_ON_CLOSE);
        window.addGLEventListener(new Display());
        window.addMouseListener(new MouseInput());
        window.addKeyListener(new KeyInput());
        Animator animator = new Animator(window);
        animator.setRunAsFastAsPossible(true);
        animator.start();
        window.setVisible(true);
        Logic.start();
    }

    //Метод отрисовки ячейки
    private static void drawCell(ImageResource img, int x, int y) {
        GL2 gl = Display.gl;

        Texture tex = img.getTexture();
        if(tex != null){
            gl.glBindTexture(GL2.GL_TEXTURE_2D, tex.getTextureObject());
        }

        gl.glColor3f(1,1,1);
        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0,0);
        gl.glVertex2f(x,y);
        gl.glTexCoord2f(1,0);
        gl.glVertex2f(x+cellSide,y);
        gl.glTexCoord2f(1,1);
        gl.glVertex2f(x+cellSide,y+cellSide);
        gl.glTexCoord2f(0,1);
        gl.glVertex2f(x,y+cellSide);
        gl.glEnd();
        gl.glFlush();

        gl.glBindTexture(GL2.GL_TEXTURE_2D, 0);
        assert tex != null;
        tex.destroy(gl);
    }

    //Метод отрисовки иконки сапера
    public static void drawRestart(){
        GL2 gl = Display.gl;

        String path = conditionGame.get(Logic.getGameCondition());
        ImageResource image = new ImageResource(path);
        Texture tex = image.getTexture();
        if(tex != null){
            gl.glBindTexture(GL2.GL_TEXTURE_2D, tex.getTextureObject());
        }

        gl.glColor3f(1,1,1);
        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0,1);
        gl.glVertex2f(3*cellSide,-cellSide);
        gl.glTexCoord2f(1,1);
        gl.glVertex2f(5*cellSide,-cellSide);
        gl.glTexCoord2f(1,0);
        gl.glVertex2f(5*cellSide,-cellSide*3);
        gl.glTexCoord2f(0,0);
        gl.glVertex2f(3*cellSide,-cellSide*3);
        gl.glEnd();
        gl.glFlush();

        gl.glBindTexture(GL2.GL_TEXTURE_2D, 0);
        assert tex != null;
        tex.destroy(gl);
    }

    //Рендер игровой сетки
    public static void render() {
        for(byte i = 0; i<Logic.getGridHeight(); i++)
            for(byte j = 0; j<Logic.getGridWidth(); j++){
                ImageResource image = new ImageResource(conditionCell.get(Logic.getCondition()[i][j]));
                drawCell(image,j*cellSide,i*cellSide);
            }
    }
}