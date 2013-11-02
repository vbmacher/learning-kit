package cz.zoom.whiteboard;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.multi.qrcode.QRCodeMultiReader;
import com.google.zxing.qrcode.QRCodeWriter;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class QRCode {
    public final static int DEFAULT_QR_WIDTH = 250;
    public final static int DEFAULT_QR_HEIGHT = 250;

    public static BufferedImage encode(String dumpedData) throws IOException, WriterException {
        return encode(dumpedData, DEFAULT_QR_WIDTH, DEFAULT_QR_HEIGHT);
    }

    public static BufferedImage encode(String dumpedData, int width, int height) throws IOException, WriterException {
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix bitMatrix = writer.encode(dumpedData, BarcodeFormat.QR_CODE, width, height);
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }
    
    public static Result[] decode(BufferedImage image) throws NotFoundException {
        if (image == null) {
            return null;
        }
        
        BinaryBitmap binaryBitmap;

        binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(image)));
        return new QRCodeMultiReader().decodeMultiple(binaryBitmap);
    }
    
}
