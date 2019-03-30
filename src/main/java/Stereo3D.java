import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

class Stereo3D {

    static BufferedImage imageToStereo3D(BufferedImage left, BufferedImage right) {

        if (left.getWidth() != right.getWidth() || left.getHeight() != right.getHeight()) {
            System.out.println("Left and right image dimensions does not match.");
            return null;
        }

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

        System.out.println("3D stereo image created.");

        return stereo;

    }

    static BufferedImage[] videoToStereo3D(String leftMP4Path, String rightMP4Path) {

        FFmpegFrameGrabber leftFrameGrabber = new FFmpegFrameGrabber(leftMP4Path);
        FFmpegFrameGrabber rightFrameGrabber = new FFmpegFrameGrabber(rightMP4Path);

        BufferedImage[] leftFrames = convertMovieToJPG(leftFrameGrabber);
        BufferedImage[] rightFrames = convertMovieToJPG(rightFrameGrabber);

        if (leftFrames.length != rightFrames.length) {
            System.out.println("Left and right video frames does not match.");
            return null;
        }

        ArrayList<BufferedImage> stereoImages = new ArrayList<BufferedImage>();
        for (int i = 0; i < leftFrames.length; i++) {
            stereoImages.add(imageToStereo3D(leftFrames[i], rightFrames[i]));
        }

        return (BufferedImage[]) stereoImages.toArray();

    }

    private static BufferedImage[] convertMovieToJPG(FFmpegFrameGrabber frameGrabber) {

        Java2DFrameConverter converter = new Java2DFrameConverter();

        double frameRate = frameGrabber.getFrameRate();

        System.out.println("Video has " + frameGrabber.getLengthInFrames() + " frames and has frame rate of " + frameRate);

        ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();

        try {

            frameGrabber.start();
            Frame frame;

            for (int i = 1; i <= frameGrabber.getLengthInFrames(); i++) {

                frameGrabber.setFrameNumber(i);
                frame = frameGrabber.grab();
                images.add(converter.convert(frame));

            }

            frameGrabber.stop();

        } catch (Exception e) {

            e.printStackTrace();

        }

        return (BufferedImage[]) images.toArray();

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
