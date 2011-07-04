package cz.dhl.term;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

class AMapper extends KeyAdapter {
    AEmulator emu;

    AMapper() {
    }

    public void keyTyped(KeyEvent e) {
        char ch = e.getKeyChar();
        if (ch == '\n')
            ch = '\r';
        send(ch);
    }

    void send(char ch) {
        emu.send(ch);
    }

    public synchronized void send(String s) {
        for (int i = 0; i < s.length(); i++)
            send(s.charAt(i));
    }
}
