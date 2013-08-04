package storyteller

import javax.swing.JPanel
import java.awt.Graphics
import java.awt.event.MouseMotionListener
import java.awt.event.MouseListener
import java.awt.Point
import java.awt.Rectangle
import java.awt.event.MouseEvent

class Board extends JPanel implements MouseListener, MouseMotionListener {
    private volatile Room currentRoom;

    private Board() {
        setDoubleBuffered(true)
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
        resize(currentRoom.getSize());
        setPreferredSize(currentRoom.getSize());
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        Room room = currentRoom;
        if (room != null) {
            room.paint(graphics);
        }
    }

    public final void setCurrentRoom(Room room) {
        currentRoom = room;
        updateSize()
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

