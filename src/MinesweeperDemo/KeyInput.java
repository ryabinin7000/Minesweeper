//Класс, отвечающий за ввод с клавиатуры (горячие клавиши)

package MinesweeperDemo;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;

public class KeyInput implements KeyListener {

    //Метод, вызываемый при нажатии на клавишу (в данном случае пустой)
    public void keyPressed(KeyEvent e) {

    }

    //Метод, вызываемый при отпускании клавиши
    //Q или Esc - выход из приложения, F2 - рестарт
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_F2)
            Logic.start();
        else if(e.getKeyCode() == KeyEvent.VK_Q || e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            Renderer.getWindow().destroy();
            System.exit(0);
        }
    }
}
