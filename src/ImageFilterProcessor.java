import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class ImageFilterProcessor {

    public static BufferedImage applyGrayscaleFilter(BufferedImage image) {
        return applyFilter(image, (color) -> {
            int colorAverage = (color.getRed() + color.getGreen() + color.getBlue()) / 3;
            return new Color(colorAverage, colorAverage, colorAverage);
        });
    }

    public static BufferedImage applyThresholdFilter(BufferedImage image) {
        return applyFilter(image, (color) -> {
            int grayValue = (color.getRed() + color.getGreen() + color.getBlue()) / 3;
            return grayValue > 128 ? Color.WHITE : Color.BLACK;
        });
    }

    public static BufferedImage applyPosterizeFilter(BufferedImage image) {
        return applyFilter(image, (color) -> {
            int red = (color.getRed() / 64) * 64;
            int green = (color.getGreen() / 64) * 64;
            int blue = (color.getBlue() / 64) * 64;
            return new Color(red, green, blue);
        });
    }

    public static BufferedImage applyTintFilter(BufferedImage image, Color tint) {
        return applyFilter(image, (color) -> {
            int red = (color.getRed() + tint.getRed()) / 2;
            int green = (color.getGreen() + tint.getGreen()) / 2;
            int blue = (color.getBlue() + tint.getBlue()) / 2;
            return new Color(red, green, blue);
        });
    }

    public static BufferedImage applyColorShiftRightFilter(BufferedImage image) {
        return applyFilter(image, (color) -> new Color(color.getBlue(), color.getRed(), color.getGreen()));
    }

    public static BufferedImage applyColorShiftLeftFilter(BufferedImage image) {
        return applyFilter(image, (color) -> new Color(color.getGreen(), color.getBlue(), color.getRed()));
    }

    public static BufferedImage applyMirrorFilter(BufferedImage image) {
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                newImage.setRGB(image.getWidth() - x - 1, y, image.getRGB(x, y));
            }
        }
        return newImage;
    }

    public static BufferedImage applyPixelateFilter(BufferedImage image, int pixelSize) {
        return applyPositionFilter(image, (color, x, y) -> {
            int avgRed = 0, avgGreen = 0, avgBlue = 0, count = 0;
            for (int i = 0; i < pixelSize; i++) {
                for (int j = 0; j < pixelSize; j++) {
                    int newX = x + i, newY = y + j;
                    if (newX < image.getWidth() && newY < image.getHeight()) {
                        Color neighborColor = new Color(image.getRGB(newX, newY));
                        avgRed += neighborColor.getRed();
                        avgGreen += neighborColor.getGreen();
                        avgBlue += neighborColor.getBlue();
                        count++;
                    }
                }
            }
            avgRed /= count;
            avgGreen /= count;
            avgBlue /= count;
            return new Color(avgRed, avgGreen, avgBlue);
        });
    }

    public static BufferedImage applyShowBordersFilter(BufferedImage image) {
        int[][] sobelX = {
                { -1, 0, 1 },
                { -2, 0, 2 },
                { -1, 0, 1 }
        };
        int[][] sobelY = {
                { -1, -2, -1 },
                { 0, 0, 0 },
                { 1, 2, 1 }
        };

        return applyPositionFilter(image, (color, x, y) -> {
            int gxRed = 0, gyRed = 0, gxGreen = 0, gyGreen = 0, gxBlue = 0, gyBlue = 0;
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    int newX = x + i, newY = y + j;
                    if (newX >= 0 && newX < image.getWidth() && newY >= 0 && newY < image.getHeight()) {
                        Color neighborColor = new Color(image.getRGB(newX, newY));
                        gxRed += sobelX[i + 1][j + 1] * neighborColor.getRed();
                        gyRed += sobelY[i + 1][j + 1] * neighborColor.getRed();
                        gxGreen += sobelX[i + 1][j + 1] * neighborColor.getGreen();
                        gyGreen += sobelY[i + 1][j + 1] * neighborColor.getGreen();
                        gxBlue += sobelX[i + 1][j + 1] * neighborColor.getBlue();
                        gyBlue += sobelY[i + 1][j + 1] * neighborColor.getBlue();
                    }
                }
            }
            int gradientRed = Math.min(255, (int) Math.sqrt(gxRed * gxRed + gyRed * gyRed));
            int gradientGreen = Math.min(255, (int) Math.sqrt(gxGreen * gxGreen + gyGreen * gyGreen));
            int gradientBlue = Math.min(255, (int) Math.sqrt(gxBlue * gxBlue + gyBlue * gyBlue));
            return new Color(gradientRed, gradientGreen, gradientBlue);
        });
    }

    public static BufferedImage applyEliminateRedFilter(BufferedImage image) {
        return applyFilter(image, (color) -> new Color(0, color.getGreen(), color.getBlue()));
    }

    public static BufferedImage applyEliminateGreenFilter(BufferedImage image) {
        return applyFilter(image, (color) -> new Color(color.getRed(), 0, color.getBlue()));
    }

    public static BufferedImage applyEliminateBlueFilter(BufferedImage image) {
        return applyFilter(image, (color) -> new Color(color.getRed(), color.getGreen(), 0));
    }

    public static BufferedImage applySepiaFilter(BufferedImage image) {
        return applyFilter(image, (color) -> {
            int tr = (int)(0.393 * color.getRed() + 0.769 * color.getGreen() + 0.189 * color.getBlue());
            int tg = (int)(0.349 * color.getRed() + 0.686 * color.getGreen() + 0.168 * color.getBlue());
            int tb = (int)(0.272 * color.getRed() + 0.534 * color.getGreen() + 0.131 * color.getBlue());

            tr = Math.min(255, tr);
            tg = Math.min(255, tg);
            tb = Math.min(255, tb);

            return new Color(tr, tg, tb);
        });
    }

    public static BufferedImage applyLighterFilter(BufferedImage image) {
        return applyFilter(image, (color) -> {
            int red = Math.min(255, color.getRed() + 30);
            int green = Math.min(255, color.getGreen() + 30);
            int blue = Math.min(255, color.getBlue() + 30);
            return new Color(red, green, blue);
        });
    }

    public static BufferedImage applyDarkerFilter(BufferedImage image) {
        return applyFilter(image, (color) -> {
            int red = Math.max(0, color.getRed() - 30);
            int green = Math.max(0, color.getGreen() - 30);
            int blue = Math.max(0, color.getBlue() - 30);
            return new Color(red, green, blue);
        });
    }

    public static BufferedImage applyVignetteFilter(BufferedImage image) {
        int centerX = image.getWidth() / 2;
        int centerY = image.getHeight() / 2;
        double maxDistance = Math.sqrt(centerX * centerX + centerY * centerY);

        return applyPositionFilter(image, (color, x, y) -> {
            double distance = Math.sqrt(Math.pow(centerX - x, 2) + Math.pow(centerY - y, 2));
            double vignetteFactor = distance / maxDistance;
            int red = (int) (color.getRed() * (1 - vignetteFactor));
            int green = (int) (color.getGreen() * (1 - vignetteFactor));
            int blue = (int) (color.getBlue() * (1 - vignetteFactor));
            return new Color(red, green, blue);
        });
    }

    public static BufferedImage applyAddNoiseFilter(BufferedImage image) {
        return applyFilter(image, (color) -> {
            int noise = (int) (Math.random() * 50) - 25;
            int red = Math.min(255, Math.max(0, color.getRed() + noise));
            int green = Math.min(255, Math.max(0, color.getGreen() + noise));
            int blue = Math.min(255, Math.max(0, color.getBlue() + noise));
            return new Color(red, green, blue);
        });
    }

    public static BufferedImage applySolarizeFilter(BufferedImage image) {
        return applyFilter(image, (color) -> {
            int threshold = 128;
            int red = color.getRed() > threshold ? 255 - color.getRed() : color.getRed();
            int green = color.getGreen() > threshold ? 255 - color.getGreen() : color.getGreen();
            int blue = color.getBlue() > threshold ? 255 - color.getBlue() : color.getBlue();
            return new Color(red, green, blue);
        });
    }

    public static BufferedImage applyVintageFilter(BufferedImage image) {
        BufferedImage sepiaImage = applySepiaFilter(image);
        return applyAddNoiseFilter(sepiaImage);
    }

    public static BufferedImage applyBlurFilter(BufferedImage image) {
        int offset = 1;
        return applyPositionFilter(image, (color, x, y) -> {
            int sumRed = 0;
            int sumGreen = 0;
            int sumBlue = 0;
            int count = 0;

            for (int i = -offset; i <= offset; i++) {
                for (int j = -offset; j <= offset; j++) {
                    int neighborX = x + i;
                    int neighborY = y + j;

                    if (neighborX >= 0 && neighborX < image.getWidth() && neighborY >= 0 && neighborY < image.getHeight()) {
                        Color neighborColor = new Color(image.getRGB(neighborX, neighborY));
                        sumRed += neighborColor.getRed();
                        sumGreen += neighborColor.getGreen();
                        sumBlue += neighborColor.getBlue();
                        count++;
                    }
                }
            }

            int avgRed = sumRed / count;
            int avgGreen = sumGreen / count;
            int avgBlue = sumBlue / count;

            return new Color(avgRed, avgGreen, avgBlue);
        });
    }

    public static BufferedImage applyContrastFilter(BufferedImage image) {
        return applyFilter(image, (color) -> {
            int red = adjustContrast(color.getRed(), 1.8);
            int green = adjustContrast(color.getGreen(), 1.8);
            int blue = adjustContrast(color.getBlue(), 1.8);

            return new Color(red, green, blue);
        });
    }

    public static BufferedImage applyNegativeFilter(BufferedImage image) {
        return applyFilter(image, (color) -> {
            int red = 255 - color.getRed();
            int green = 255 - color.getGreen();
            int blue = 255 - color.getBlue();
            return new Color(red, green, blue);
        });
    }

    private static int adjustContrast(int value, double contrastFactor) {
        double normalizedValue = (value - 128.0) / 128.0;
        double adjustedValue = normalizedValue * contrastFactor;
        int newValue = (int) (adjustedValue * 128.0 + 128.0);
        return Math.max(0, Math.min(255, newValue));
    }

    private static BufferedImage applyFilter(BufferedImage image, ColorTransformer transformer) {
        return applyPositionFilter(image, (color, x, y) -> transformer.apply(color));
    }

    private static BufferedImage applyPositionFilter(BufferedImage image, ColorPositionTransformer transformer) {
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

        List<Point> points = MainPanel.points;
        int startX = 0, endX = MainPanel.IMAGE_WIDTH, startY = 0, endY = MainPanel.IMAGE_HEIGHT;

        if (points.size() == 4) {
            startX = points.get(0).x;
            endX = points.get(2).x;
            startY = points.get(0).y;
            endY = points.get(1).y;
        }

        for (int x = 0; x < MainPanel.IMAGE_WIDTH; x++) {
            for (int y = 0; y < MainPanel.IMAGE_HEIGHT; y++) {
                Color color = new Color(image.getRGB(x, y));
                if (x >= startX && x <= endX && y >= startY && y <= endY) {
                    newImage.setRGB(x, y, transformer.apply(color, x, y).getRGB());
                } else {
                    newImage.setRGB(x, y, color.getRGB());
                }
            }
        }

        points.clear();
        return newImage;
    }

    @FunctionalInterface
    private interface ColorTransformer {
        Color apply(Color color);
    }

    @FunctionalInterface
    private interface ColorPositionTransformer {
        Color apply(Color color, int x, int y);
    }
}
