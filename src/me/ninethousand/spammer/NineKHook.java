package me.ninethousand.spammer;

import org.json.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Sorry for spaghetti code!
 * @author 9k
 * @since 28/04/2021
 */

public class NineKHook {
    public static GUI gui;
    public static String VERSION = "9kHook B1.0";
    public static void main(String[]args) throws IOException{
         gui = new GUI();
    }
}

class GUI extends JFrame implements ActionListener {
    public JLabel status;

    public GUI() throws IOException {
        // background image
        JLabel label1 = new JLabel();
        label1.setOpaque(true);
        label1.setBackground(Color.RED);
        label1.setBounds(0,0,300,300);
        URL url = new URL("https://i.imgur.com/KWUHs7E.jpeg");
        Image image1 = ImageIO.read(url);
        Image scaled = image1.getScaledInstance(300, 300, Image.SCALE_SMOOTH);
        ImageIcon scal = new ImageIcon(scaled);
        label1.setIcon(scal);

        // title
        JLabel title = new JLabel("Discord Webhook Spammer");
        title.setFont(new Font("Calibri", Font.BOLD, 20));
        title.setOpaque(false);
        title.setForeground(Color.WHITE);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBounds(0,0,300,35);

        // enter url
        JTextField webhook = new JTextField();
        webhook.setText("webhook url");
        webhook.setFont(new Font("Calibri", NORMAL, 11));
        webhook.setHorizontalAlignment(SwingConstants.CENTER);
        webhook.setBounds(50, 50, 200, 20);

        // enter message
        JTextField message = new JTextField();
        message.setText("This Webhook Is Owned By 9k Now.");
        message.setFont(new Font("Calibri", NORMAL, 11));
        message.setHorizontalAlignment(SwingConstants.CENTER);
        message.setBounds(50, 80, 200, 20);

        // amount
        JSlider slider = new JSlider(0, 100, 10);
        slider.setBounds(50, 110, 200, 50);
        slider.setMajorTickSpacing(10);
        slider.setMinorTickSpacing(1);
        //slider.setPaintTicks(true);
        slider.setPaintLabels(true);

        // button
        JButton attack = new JButton("Attack");
        attack.setBounds(50, 180, 200, 20);
        attack.addActionListener(e -> {
            Spammer spammer = new Spammer(webhook.getText(), message.getText(), 100, slider.getValue(), "CurryGod.CC Developer", "https://imagevars.gulfnews.com/2017/7/24/1_16a08412ffc.2063789_4172633237_16a08412ffc_large.jpg");
            spammer.current = 0;
            for (int i = 1; i <= spammer.amount; i++) {
                spammer.start();
            }
            status.setBackground(new Color(0xFFA29F9F, true));
        });

        // status
        status = new JLabel("Status: Ready");
        status.setFont(new Font("Calibri", Font.BOLD, 17));
        status.setOpaque(true);
        status.setBackground(new Color(0xFFA29F9F, true));
        status.setForeground(Color.WHITE);
        status.setHorizontalAlignment(SwingConstants.CENTER);
        status.setBounds(0,210,300,50);

        // layered pane
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, 300, 300);
        layeredPane.add(title, JLayeredPane.DRAG_LAYER);
        layeredPane.add(webhook, JLayeredPane.DRAG_LAYER);
        layeredPane.add(message, JLayeredPane.DRAG_LAYER);
        layeredPane.add(slider, JLayeredPane.DRAG_LAYER);
        layeredPane.add(attack, JLayeredPane.DRAG_LAYER);
        layeredPane.add(status, JLayeredPane.DRAG_LAYER);
        layeredPane.add(label1, JLayeredPane.DEFAULT_LAYER);

        // frame
        setTitle(NineKHook.VERSION);
        URL url1 = new URL("https://i.imgur.com/LY3hRWW.png");
        Image image = ImageIO.read(url1);
        setIconImage(image);
        add(layeredPane);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(300, 300);
        setResizable(false);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
class Spammer {

    private final ExecutorService tPool = Executors.newSingleThreadExecutor();

    private final String webhookURL;
    private final String message;
    private final long delayMS;
    private final String name;
    private final String avatar;
    public int amount;
    public int current;
    public boolean attacking;

    private Runnable doWork = new Runnable() {
        public void run() {
            JSONObject result = new JSONObject();

            if (name != null && name.length() > 0) {
                result.put("username", name);
            }
            if (avatar != null && avatar.length() > 0) {
                result.put("avatar_url", avatar);
            }
            result.put("content", message);
            String payload = result.toString();

            try {
                HttpURLConnection connection = (HttpURLConnection) (new URL(webhookURL)).openConnection();

                connection.setRequestMethod("POST");
                connection.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64; rv:49.0) Gecko/20100101 Firefox/49.0");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Content-Length", String.valueOf(payload.length()));
                connection.setDoOutput(true);
                connection.setDoInput(true);

                try (DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream())) {
                    outputStream.write(payload.getBytes(StandardCharsets.UTF_8));
                    outputStream.flush();
                }
                int code = connection.getResponseCode();
                if (code >= 200 && code < 300) {
                    current = current + 1;
                    NineKHook.gui.status.setText("Status: Attacking [" + current + "/" + amount + "] - Success");
                    NineKHook.gui.status.setBackground(Color.GREEN);
                } else {
                    NineKHook.gui.status.setText("Status: Attacking [" + current + "/" + amount + "] - Fail");
                    NineKHook.gui.status.setBackground(Color.RED);

                }

                try {
                    /*TimeUnit.SECONDS.sleep(1);*/
                    Thread.sleep(200);
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            } catch (IOException ex) {
                Logger.getLogger(Spammer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    };

    Spammer(String webhookURL, String message, long delayMS, int amount, String name, String avatar) {
        this.webhookURL = webhookURL;
        this.message = message;
        this.delayMS = delayMS;
        this.amount = amount;
        this.name = name;
        this.avatar = avatar;
    }

    void start() {
        tPool.submit(doWork);
    }
}
