package storyteller

import javax.swing.JPanel
import java.awt.Graphics
import java.awt.event.MouseMotionListener
import java.awt.event.MouseListener
import java.awt.Point
import java.awt.Rectangle
import java.awt.event.MouseEvent
import java.awt.Graphics2D
import java.awt.AlphaComposite
import java.awt.RenderingHints
import java.awt.Color
import java.util.concurrent.CountDownLatch

class Board extends JPanel implements MouseListener, MouseMotionListener {
    private Room currentRoom; // Confined to Swing Event thread
    private volatile Room newRoom
    private float alpha = 0.0f; // Confined to Swing Event thread
    private volatile boolean shouldFadeOut = false;
    private volatile boolean shouldFadeIn = false

    private Board() {
        setDoubleBuffered(true)
        setBackground(Color.BLACK)
    }

    private Board(Room startRoom) {
        this()
        setCurrentRoom(startRoom)
    }

    public static Board newInstance() {
        def board = new Board()
        board.addMouseListener(board)
        board.addMouseMotionListener(board)
        return board
    }

    public static Board newInstance(Room startRoom) {
        def board = new Board(startRoom)
        board.addMouseListener(board)
        board.addMouseMotionListener(board)
        return board
    }

    private def updateSize() {
        updateSize(currentRoom)
    }

    private def updateSize(Room room) {
        resize(room.getSize());
        setPreferredSize(room.getSize());
        repaint()
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        Graphics2D g2d = (Graphics2D)graphics
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

        if (currentRoom != null) {
            currentRoom.paint(graphics);
        }
        if (shouldFadeOut) {
            fadeOut(g2d)
        } else if (shouldFadeIn) {
            fadeIn(g2d);
        }
    }

    private void fadeIn(Graphics2D g2d) {
        if (alpha < 1.0f) {
            alpha += 0.01f;
            if (alpha > 1.0f) {
                alpha = 1.0f
            }
            sleep10ms()
            repaint();
        } else {
            alpha = 1.0f
            shouldFadeIn = false
        }
    }

    private void fadeOut(Graphics2D g2d) {
        if (currentRoom != null && alpha > 0.0f) {
            alpha -= 0.01f;
            if (alpha < 0.0f) {
                alpha = 0.0f
            }
            sleep10ms()
            repaint();
        } else {
            alpha = 0.0f
            shouldFadeOut = false
            shouldFadeIn = true
            currentRoom = newRoom
            updateSize()
        }
    }

    private void sleep10ms() {
        try {
            Thread.sleep(6);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public final void setCurrentRoom(Room room) {
        newRoom = room;
        shouldFadeOut = true
        if (currentRoom == null) {
            updateSize(room)
        }
        repaint();
    }

    private GameObject getObjectAt(Point point) {
        def entry = currentRoom.objects.find {
            ((Rectangle)it.value.getRectangle()).contains(point)
        }
        return entry?.value
    }

    final def void mouseExited(MouseEvent e) {
    }

    final def void mouseDragged(MouseEvent e) {
    }

    final def void mouseMoved(MouseEvent e) {
        getObjectAt(e.getPoint())?.mouseMoved(e.getPoint())
        repaint()
    }

    final def void mouseEntered(MouseEvent e) {
    }

    final def void mouseReleased(MouseEvent e) {

    }

    final def void mousePressed(MouseEvent e) {

    }

    final def void mouseClicked(MouseEvent e) {
        getObjectAt(e.getPoint())?.clicked(e.getPoint())
        repaint()
    }

}

