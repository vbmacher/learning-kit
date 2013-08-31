package storyteller.gamemodel

class RoomBuilder extends BuilderSupport {
    private final static String MISSING_ERROR = "Missing object attributes";
    private final Map<String,Room> rooms
    private final Map<String,GameObject> objects

    public RoomBuilder(Map<String,Room> rooms, Map<String,GameObject> objects) {
        this.rooms = rooms
        this.objects = objects
    }

    @Override
    protected void setParent(Object parent, Object child) {
        throw new UnsupportedOperationException("Rooms cannot have children")
    }

    public Iterator iterator() {
        return rooms.values().iterator();
    }

    @Override
    protected Object createNode(Object name) {
        throw new UnsupportedOperationException(MISSING_ERROR);
    }

    @Override
    protected Object createNode(Object name, Object value) {
        throw new UnsupportedOperationException(MISSING_ERROR);
    }

    @Override
    protected Object createNode(Object name, Map attributes) {
        Room room = new Room(name, attributes, objects);
        rooms.put(name, room)
        return room;
    }

    @Override
    protected Object createNode(Object name, Map attributes, Object value) {
        throw new UnsupportedOperationException(MISSING_ERROR);
    }

}

