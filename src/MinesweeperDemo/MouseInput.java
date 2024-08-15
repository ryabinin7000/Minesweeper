//Класс, ответственный за ввод с мышки

package MinesweeperDemo;

import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;

public class MouseInput implements MouseListener {

    //Куча методов для разных действий мыши. Нам нужен только один

    public void mouseClicked(MouseEvent e) {

    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }

    public void mousePressed(MouseEvent e) {

    }

    //Метод при отпускании кнопки мыши
    public void mouseReleased(MouseEvent e) {
        if(e.getButton()==MouseEvent.BUTTON1) {
            if (Logic.isInRestart(e.getX(), e.getY())) {
                Logic.start();
            }
            else if (Logic.isIsRunning() && Logic.isFirstClick()) {
                Logic.generateGrid(e.getX(), e.getY());
            }
            else if (Logic.isIsRunning()){
                Logic.openCell(e.getX(), e.getY());
            }
        }
        if(e.getButton()==MouseEvent.BUTTON3){
            Logic.flag(e.getX(), e.getY());
        }
    }

    public void mouseMoved(MouseEvent e) {

    }

    public void mouseDragged(MouseEvent e) {

    }

    public void mouseWheelMoved(MouseEvent e) {

    }
}