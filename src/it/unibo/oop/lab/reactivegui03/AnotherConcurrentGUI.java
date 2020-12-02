package it.unibo.oop.lab.reactivegui03;

import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class AnotherConcurrentGUI extends JFrame {
    private static final long serialVersionUID = -8710276539980695794L;
    
    private JPanel containerAll;
    private JPanel containerButton;
    
    private JButton up;
    private JButton down;
    private JButton stop;
    
    private JLabel num;

    public AnotherConcurrentGUI() {
        super("AnotherConcurrentGUI");
        this.containerAll = new JPanel();
        this.containerAll.setLayout(new BoxLayout(this.containerAll, BoxLayout.Y_AXIS));
        
        this.containerButton = new JPanel();
        this.containerButton.setLayout(new FlowLayout());
        
        this.up = new JButton("Up");
        this.down = new JButton("Down");
        this.stop = new JButton("Stop");
        
        this.num = new JLabel("0");
        
        this.containerButton.add(this.up);
        this.containerButton.add(this.down);
        this.containerButton.add(this.stop);
        this.containerAll.add(this.num);
        this.containerAll.add(this.containerButton);
        this.getContentPane().add(containerAll);
        
        final Agent agent = new Agent();
        new Thread(agent).start();
        new Thread(() -> {
            try {
                Thread.sleep(100000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            agent.stopCounting();
        }).start();
        
        up.addActionListener(e -> agent.countUp());
        down.addActionListener(e -> agent.countDown());
        stop.addActionListener(e -> {
            agent.stopCounting();
            stop.setEnabled(false);
            up.setEnabled(false);
            down.setEnabled(false);
        });
        
        
        this.pack();
        this.setVisible(true);
    }
    
    private class Agent implements Runnable {
        private volatile boolean stop;
        private volatile boolean up = true;
        private volatile int counter;

        @Override
        public void run() {
            while (!stop) {
                try {
                    counter += up ? 1 : -1;
                    SwingUtilities.invokeLater(() -> num.setText(Integer.toString(counter)));
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }

        public void stopCounting() {
            this.stop = true;
        }

        public void countUp() {
            this.up = true;
        }

        public void countDown() {
            this.up = false;
        }
    }

}
