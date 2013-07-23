package storyteller

import javax.swing.JPanel
import java.awt.Graphics

class RoomCanvas extends JPanel {
    private volatile Room currentRoom;

    RoomCanvas(Room startRoom) {
        this.currentRoom = startRoom;
        updateSize()
    }

    private def updateSize() {
        resize(currentRoom.getSize());
        setPreferredSize(currentRoom.getSize());
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Room room = currentRoom;
        if (room != null) {
            room.paint(g);
        }
    }

    public void setCurrentRoom(Room room) {
        currentRoom = room;
        updateSize()
        javax.swing.SwingUtilities.getAncestorClass().pack()
    }

}

