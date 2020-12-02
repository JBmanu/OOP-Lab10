package it.unibo.oop.lab.reactivegui02;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public final class ConcurrentGUI extends JFrame {
    private static final long serialVersionUID = 6240276529679155257L;

    private JPanel panel = new JPanel();
    private JPanel panelButt = new JPanel(new FlowLayout());
    private JLabel display = new JLabel();
    
    final private JButton stop = new JButton("stop");
    final private JButton up = new JButton("up");
    final private JButton down = new JButton("down");
    
    public ConcurrentGUI() {
        super(); 
        this.panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        this.panelButt.add(up);
        this.panelButt.add(down);
        this.panelButt.add(stop);
        
        this.panel.add(this.display);
        
        this.panel.add(this.panelButt);
        
        final Agent agent = new Agent();
        new Thread(agent).start();
        
        up.addActionListener(e -> agent.countUp());
        down.addActionListener(e -> agent.countDown());
        stop.addActionListener(e -> {
            agent.stopCounting();
            stop.setEnabled(false);
            up.setEnabled(false);
            down.setEnabled(false);
        });
        
        
        this.pack();
        this.getContentPane().add(panel);
        this.setVisible(true);
    }

    private class Agent implements Runnable {
        private volatile boolean stop;
        private volatile boolean up = true;
        private int counter;

        @Override
        public void run() {
            while (!stop) {
                try {
                    counter += up ? 1 : -1;
                    SwingUtilities.invokeLater(() -> display.setText(Integer.toString(counter)));
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
