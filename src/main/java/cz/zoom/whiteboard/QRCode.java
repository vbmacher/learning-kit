package cz.zoom.whiteboard;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.multi.qrcode.QRCodeMultiReader;
import com.google.zxing.qrcode.QRCodeWriter;
import cz.zoom.whiteboard.decoder.ImageFilters;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

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

    public static BufferedImage prepareForDecoding(BufferedImage image) {
        ColorConvertOp op = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
        op.filter(image, image);
        
        image = ImageFilters.setContrast(image, 150);
        image = ImageFilters.gammaCorrection(image, 0.1);
        image = ImageFilters.blackAndWhite(image);

        return image;
    }

    public static Result[] decode(BufferedImage image) throws NotFoundException {
        if (image == null) {
            return null;
        }
        
        BinaryBitmap binaryBitmap;
        Map<DecodeHintType, Object> hints = new EnumMap<DecodeHintType, Object>(DecodeHintType.class);
        hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
        
        List<BarcodeFormat> formats = new ArrayList<BarcodeFormat>();
        formats.add(BarcodeFormat.QR_CODE);
        hints.put(DecodeHintType.POSSIBLE_FORMATS, formats);
        
        binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(prepareForDecoding(image))));
        return new QRCodeMultiReader().decodeMultiple(binaryBitmap, hints);
    }
    
}
