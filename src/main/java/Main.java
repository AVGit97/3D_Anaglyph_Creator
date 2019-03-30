import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        File file = new File("images\\left.jpg");
        BufferedImage left = ImageIO.read(file);

        file = new File("images\\right.jpg");
        BufferedImage right = ImageIO.read(file);

        file = new File("images\\stereo.jpg");
        BufferedImage stereo = Stereo3D.imageToStereo3D(left, right);

        if (stereo != null) ImageIO.write(stereo, "jpg", file);

    }

}
