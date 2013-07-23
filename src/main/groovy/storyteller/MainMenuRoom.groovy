package storyteller

class MainMenuRoom extends Room {
    private static final String IMAGE_FILE_NAME = "/storyteller.png"

    MainMenuRoom() {
        super('MainMenu', [:], [:])
        updateImage(MainMenuRoom.class.getResource(IMAGE_FILE_NAME))
    }



}

