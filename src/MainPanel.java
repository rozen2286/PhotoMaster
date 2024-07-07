import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;

public class MainPanel extends JPanel {

    private static final int PANEL_WIDTH = 800;
    private static final int PANEL_HEIGHT = 600;
    public static final int IMAGE_X = 25;
    public static final int IMAGE_Y = 25;
    public static final int IMAGE_WIDTH = 600;
    public static final int IMAGE_HEIGHT = 500;
    private static final Color BACKGROUND_COLOR = new Color(154, 189, 169, 255);
    private static final Color BORDER_COLOR = Color.BLACK;
    private static final Color FILL_COLOR = Color.WHITE;
    private static final String[] FILTER_OPTIONS = {
            "Add Noise Filter",
            "Blur Filter",
            "Color Shift Left Filter",
            "Color Shift Right Filter",
            "Contrast Filter",
            "Darker Filter",
            "Eliminate Blue Filter",
            "Eliminate Green Filter",
            "Eliminate Red Filter",
            "Grayscale Filter",
            "Lighter Filter",
            "Mirror Filter",
            "Negative Filter",
            "Pixelate Filter",
            "Posterize Filter",
            "Sepia Filter",
            "Show Borders Filter",
            "Solarize Filter",
            "Threshold Filter",
            "Tint Filter",
            "Vintage Filter",
            "Vignette Filter"
    };

    private BufferedImage selectedImage;
    private BufferedImage filteredImage;
    private JComboBox<String> filterComboBox;

    private int imageWidth;
    private int imageHeight;

    private Control control;
    public static List<Point> points = new ArrayList<>();

    public MainPanel() {
        this.setBounds(0, 0, PANEL_WIDTH, PANEL_HEIGHT);
        this.setLayout(null);
        this.setBackground(BACKGROUND_COLOR);

        initializeComponents();

        this.control = new Control(this);
        this.addMouseListener(control);
    }

    private void initializeComponents() {
        int x = 750;
        int y = 175;
        int width = 120;
        int height = 30;

        JButton selectImageButton = createButton("Select Image", x, y);
        selectImageButton.addActionListener(e -> selectImage());
        this.add(selectImageButton);

        filterComboBox = new JComboBox<>(FILTER_OPTIONS);
        filterComboBox.setBounds(x, y += 50, width, height);
        this.add(filterComboBox);

        JButton applyFilterButton = createButton("Apply Filter", x, y += 50);
        applyFilterButton.addActionListener(e -> applySelectedFilter());
        this.add(applyFilterButton);

        JButton saveImageButton = createButton("Save Image", x, y += 50);
        saveImageButton.addActionListener(e -> saveImage());
        this.add(saveImageButton);

        JButton backButton = createButton("back", x, y += 100);
        backButton.addActionListener(e -> getOriginalImage());
        this.add(backButton);
    }

    private JButton createButton(String text, int x, int y) {
        JButton button = new JButton(text);
        button.setBounds(x, y, 120, 30);
        return button;
    }

    private void selectImage() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                selectedImage = ImageIO.read(selectedFile);

                this.imageWidth = selectedImage.getWidth();
                this.imageHeight = selectedImage.getHeight();

                selectedImage = resizeImage(selectedImage, IMAGE_WIDTH, IMAGE_HEIGHT);
                filteredImage = selectedImage;
                repaint();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    private BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        Image resultingImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
        BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(resultingImage, 0, 0, null);
        g2d.dispose();
        return outputImage;
    }

    private void getOriginalImage() {
        filteredImage = selectedImage;
        repaint();
    }

    private void applySelectedFilter() {
        if (selectedImage != null) {
            String selectedFilter = (String) filterComboBox.getSelectedItem();
            applyFilter(selectedFilter);
            repaint();
        }
    }

    private void applyFilter(String filter) {
        if (points.size() != 4 && !points.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Error: You must have exactly 4 points.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        switch (filter) {
            case "Add Noise Filter":
                filteredImage = ImageFilterProcessor.applyAddNoiseFilter(selectedImage);
                break;
            case "Blur Filter":
                filteredImage = ImageFilterProcessor.applyBlurFilter(selectedImage);
                break;
            case "Color Shift Left Filter":
                filteredImage = ImageFilterProcessor.applyColorShiftLeftFilter(selectedImage);
                break;
            case "Color Shift Right Filter":
                filteredImage = ImageFilterProcessor.applyColorShiftRightFilter(selectedImage);
                break;
            case "Contrast Filter":
                filteredImage = ImageFilterProcessor.applyContrastFilter(selectedImage);
                break;
            case "Darker Filter":
                filteredImage = ImageFilterProcessor.applyDarkerFilter(selectedImage);
                break;
            case "Eliminate Blue Filter":
                filteredImage = ImageFilterProcessor.applyEliminateBlueFilter(selectedImage);
                break;
            case "Eliminate Green Filter":
                filteredImage = ImageFilterProcessor.applyEliminateGreenFilter(selectedImage);
                break;
            case "Eliminate Red Filter":
                filteredImage = ImageFilterProcessor.applyEliminateRedFilter(selectedImage);
                break;
            case "Grayscale Filter":
                filteredImage = ImageFilterProcessor.applyGrayscaleFilter(selectedImage);
                break;
            case "Lighter Filter":
                filteredImage = ImageFilterProcessor.applyLighterFilter(selectedImage);
                break;
            case "Mirror Filter":
                filteredImage = ImageFilterProcessor.applyMirrorFilter(selectedImage);
                break;
            case "Negative Filter":
                filteredImage = ImageFilterProcessor.applyNegativeFilter(selectedImage);
                break;
            case "Pixelate Filter":
                filteredImage = ImageFilterProcessor.applyPixelateFilter(selectedImage, 10); // Add pixel size as required
                break;
            case "Posterize Filter":
                filteredImage = ImageFilterProcessor.applyPosterizeFilter(selectedImage);
                break;
            case "Sepia Filter":
                filteredImage = ImageFilterProcessor.applySepiaFilter(selectedImage);
                break;
            case "Show Borders Filter":
                filteredImage = ImageFilterProcessor.applyShowBordersFilter(selectedImage);
                break;
            case "Solarize Filter":
                filteredImage = ImageFilterProcessor.applySolarizeFilter(selectedImage);
                break;
            case "Threshold Filter":
                filteredImage = ImageFilterProcessor.applyThresholdFilter(selectedImage);
                break;
            case "Tint Filter":
                filteredImage = ImageFilterProcessor.applyTintFilter(selectedImage, new Color(255, 200, 200)); // Add tint color as required
                break;
            case "Vintage Filter":
                filteredImage = ImageFilterProcessor.applyVintageFilter(selectedImage);
                break;
            case "Vignette Filter":
                filteredImage = ImageFilterProcessor.applyVignetteFilter(selectedImage);
                break;
            default:
                throw new IllegalArgumentException("Invalid filter: " + filter);
        }
    }

    private void saveImage() {
        if (filteredImage != null) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Specify a file to save");
            int userSelection = fileChooser.showSaveDialog(this);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                if (!fileToSave.getAbsolutePath().endsWith(".png")) {
                    fileToSave = new File(fileToSave + ".png");
                }
                try {
                    BufferedImage resizedImage = resizeImage(filteredImage, imageWidth, imageHeight);
                    ImageIO.write(resizedImage, "png", fileToSave);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public void addPoint(int x, int y) {
        if (points.size() >= 4 || selectedImage == null) {
            return;
        }
        points.add(new Point(x, y));

        if (points.size() == 4) {
            alignPoints();
        }

        repaint();
    }

    private void alignPoints() {
        if (points.size() == 4) {
            int minX = points.stream().mapToInt(p -> p.x).min().orElse(0);
            int minY = points.stream().mapToInt(p -> p.y).min().orElse(0);
            int maxX = points.stream().mapToInt(p -> p.x).max().orElse(0);
            int maxY = points.stream().mapToInt(p -> p.y).max().orElse(0);

            points.set(0, new Point(minX, minY));
            points.set(1, new Point(maxX, minY));
            points.set(2, new Point(minX, maxY));
            points.set(3, new Point(maxX, maxY));

            points = points.stream()
                    .sorted(Comparator.comparingInt(p -> p.x))
                    .collect(Collectors.toList());
        }
    }

    public void removePoint(int x, int y) {
        int radius = 15;
        points.removeIf(point -> point.distance(x, y) < radius);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(BORDER_COLOR);
        g.drawRect(IMAGE_X, IMAGE_Y, IMAGE_WIDTH, IMAGE_HEIGHT);
        g.setColor(FILL_COLOR);
        g.fillRect(IMAGE_X, IMAGE_Y, IMAGE_WIDTH, IMAGE_HEIGHT);

        if (filteredImage != null) {
            g.drawImage(filteredImage, IMAGE_X, IMAGE_Y, IMAGE_WIDTH, IMAGE_HEIGHT, this);
        }

        g.setColor(Color.RED.brighter());
        for (Point point : points) {
            g.fillOval(point.x, point.y, 20, 20);
        }
    }
}