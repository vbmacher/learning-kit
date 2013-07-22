package storyteller

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

// Thread-safe only when using methods inside this class.
class Room extends Expando {
    def volatile image
    def final objectName
    def final ConcurrentMap<String,GameObject> objects = new ConcurrentHashMap<String, GameObject>()

    Room(objectName, Map attributes, Map allObjects) {
        this.objectName = objectName
        updateImage(attributes.remove('image'))

        attributes.remove('objects')?.each {
            object = allObjects[it]
            if (object != null) {
                objects.put(it, object)
            }
        }
        setProperty('objects', objects)

        attributes.each { key, value ->
            this.setProperty(key, value)
        }
    }

    def put(object) {
        objects.put(object.objectName, object)
    }

    def take(objectName) {
        objects.remove(objectName)
    }

    def clear() {
        objects.clear()
    }

    private void updateImage(imageFile) {
        try {
            this.image = image(file: imageFile)
        } catch (Exception e) {
            println(Arrays.toString(e.getStackTrace()))
        }
    }


    def leftShift(object) {
        insertObject(object)
    }

    def String toString() {
        "{Room=$name}"
    }
}

