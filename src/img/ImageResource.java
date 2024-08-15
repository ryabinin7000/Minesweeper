//Класс, отвечающий за работу с изображениями

package img;

import MinesweeperDemo.Renderer;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class ImageResource {

    private BufferedImage image = null; //Изображение
    private Texture texture = null; //Текстура изображения

    //Конструктор класса, отвечающий за чтение изображения по заданному пути
    public ImageResource(String path){
        URL url = ImageResource.class.getResource(path);
        try {
            assert url != null;
            image = ImageIO.read(url);
        }
        catch (IOException e){
            e.printStackTrace();
        }
        if(image != null){
            image.flush();
        }
    }

    //Получение текстуры изображения
    public Texture getTexture() {
        if (image == null) {
            return null;
        }
        if (texture == null) {
            texture = AWTTextureIO.newTexture(Renderer.getProfile(), image, true);
        }
        return texture;
    }
}