/*
 * #%L
 * A collection of simple Java utilities.
 * %%
 * Copyright (C) 2006 - 2014 Board of Regents of the University of
 * Wisconsin-Madison.
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

/**
 * A little GUI for testing Java memory behavior.
 *
 * @author Curtis Rueden
 */
@SuppressWarnings("serial")
public class TestMem extends JFrame implements ActionListener {
  private static final int MEM = 32 * 1024 * 1024; // 32 MB

  @SuppressWarnings("unused")
  private byte[] data;
  private JLabel memTotal, memMax, memFree, memUsed;

  public TestMem() {
    super("TestMem");
    JPanel pane = new JPanel();
    setContentPane(pane);
    pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
    pane.setBorder(new EmptyBorder(10, 10, 10, 10));

    JPanel buttons = new JPanel();
    buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));

    JButton malloc = new JButton("malloc");
    JButton free = new JButton("free");

    malloc.setToolTipText("Allocates 32 MB onto the heap.");
    free.setToolTipText("Frees the allocated memory and garbage collects.");

    malloc.setActionCommand("malloc");
    free.setActionCommand("free");
    malloc.addActionListener(this);
    free.addActionListener(this);

    buttons.add(malloc);
    buttons.add(free);

    memTotal = new JLabel();
    memMax = new JLabel();
    memFree = new JLabel();
    memUsed = new JLabel();

    memTotal.setAlignmentX(Component.CENTER_ALIGNMENT);
    memMax.setAlignmentX(Component.CENTER_ALIGNMENT);
    memFree.setAlignmentX(Component.CENTER_ALIGNMENT);
    memUsed.setAlignmentX(Component.CENTER_ALIGNMENT);

    refreshLabels();

    add(buttons);
    add(Box.createVerticalStrut(15));
    add(memTotal);
    add(memMax);
    add(memFree);
    add(memUsed);

    new Timer(50, this).start();

    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    pack();
    setVisible(true);
  }

  @Override
	public void actionPerformed(ActionEvent e) {
    String cmd = e.getActionCommand();
    if ("malloc".equals(cmd)) {
      // allocate lots of memory
      data = new byte[MEM];
    }
    else if ("free".equals(cmd)) {
      // free reference & garbage collect
      data = null;
      System.gc();
    }
    else {
      // timer event -- update memory usage display
      refreshLabels();
    }
  }

  private void refreshLabels() {
    Runtime r = Runtime.getRuntime();
    long total = r.totalMemory();
    long max = r.maxMemory();
    long free = r.freeMemory();
    long used = total - free;
    memTotal.setText("Total: " + (total / 1024) + " KB");
    memMax.setText("Max: " + (max / 1024) + " KB");
    memFree.setText("Free: " + (free / 1024) + " KB");
    memUsed.setText("Used: " + (used / 1024) + " KB");
  }

  public static void main(String[] args) {
    new TestMem();
  }

}
