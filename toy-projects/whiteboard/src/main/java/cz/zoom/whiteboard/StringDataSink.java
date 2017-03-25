package cz.zoom.whiteboard;

import java.io.IOException;
import java.io.OutputStream;
import net.jcip.annotations.NotThreadSafe;

@NotThreadSafe
public class StringDataSink extends OutputStream {
    String data = "";

    @Override
    public void write(int i) throws IOException {
        data += (char) i;
    }

    public String getStringData() {
        return data;
    }
    
}
