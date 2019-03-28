import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {

    private static final String LEFT_IMAGE = "left";
    private static final String RIGHT_IMAGE = "right";
    private static final String STEREO_IMAGE = "stereo";

    public static void main(String[] args) throws IOException {

        File file = new File("images\\" + LEFT_IMAGE + ".jpg");
        BufferedImage left = ImageIO.read(file);

        file = new File("images\\" + RIGHT_IMAGE + ".jpg");
        BufferedImage right = ImageIO.read(file);

        if (left.getWidth() != right.getWidth()) return;
        if (left.getHeight() != right.getHeight()) return;

        int width = left.getWidth(), height = left.getHeight();

        BufferedImage stereo = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        final Color red = new Color(255, 0, 0);
        final Color cyan = new Color(0, 255, 255);

        Color left_color, right_color;
        for (int w = 0; w < width; w++) {
            for (int h = 0; h < height; h++) {
                left_color = new Color(subtract(new Color(left.getRGB(w, h)), cyan));

                right_color = new Color(subtract(new Color(right.getRGB(w, h)), red));

                stereo.setRGB(w, h, add(left_color, right_color));
            }
        }

        file = new File("images\\" + STEREO_IMAGE + ".jpg");
        ImageIO.write(stereo, "jpg", file);

        System.out.println("3D stereo image created.");

    }

    private static int subtract(Color c1, Color c2) {

        int r = c1.getRed() - c2.getRed() < 0 ? 0 : c1.getRed() - c2.getRed();
        int g = c1.getGreen() - c2.getGreen() < 0 ? 0 : c1.getGreen() - c2.getGreen();
        int b = c1.getBlue() - c2.getBlue() < 0 ? 0 : c1.getBlue() - c2.getBlue();

        return new Color(r, g, b).getRGB();

    }

    private static int add(Color c1, Color c2) {

        int r = c1.getRed() + c2.getRed() > 255 ? 255 : c1.getRed() + c2.getRed();
        int g = c1.getGreen() + c2.getGreen() > 255 ? 255 : c1.getGreen() + c2.getGreen();
        int b = c1.getBlue() + c2.getBlue() > 255 ? 255 : c1.getBlue() + c2.getBlue();

        return new Color(r, g, b).getRGB();

    }

}
