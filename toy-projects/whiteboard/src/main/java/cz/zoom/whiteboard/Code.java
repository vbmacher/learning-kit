package cz.zoom.whiteboard;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.multi.qrcode.QRCodeMultiReader;
import com.google.zxing.oned.Code128Writer;
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

public class Code {
    public final static int DEFAULT_QR_WIDTH = 250;
    public final static int DEFAULT_QR_HEIGHT = 250;

    public static BufferedImage encode(Writer writer, String dumpedData, BarcodeFormat format, int width, int height) throws WriterException {
        BitMatrix bitMatrix = writer.encode(dumpedData, format, width, height);
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }

    public static BufferedImage encodeQR(String dumpedData) throws IOException, WriterException {
        return encodeQR(dumpedData, DEFAULT_QR_WIDTH, DEFAULT_QR_HEIGHT);
    }

    public static BufferedImage encodeQR(String dumpedData, int width, int height) throws IOException, WriterException {
        return encode(new QRCodeWriter(), dumpedData, BarcodeFormat.QR_CODE, width, height);
    }

    public static BufferedImage encodeCode128(String dumpedData) throws IOException, WriterException {
        return encodeCode128(dumpedData, DEFAULT_QR_WIDTH, DEFAULT_QR_WIDTH);
    }

    public static BufferedImage encodeCode128(String dumpedData, int width, int height) throws IOException, WriterException {
        return encode(new Code128Writer(), dumpedData, BarcodeFormat.CODE_128, width, height);
    }

    public static BufferedImage toRGB(BufferedImage i) {
        BufferedImage rgb = new BufferedImage(i.getWidth(null), i.getHeight(null), BufferedImage.TYPE_INT_RGB);
        rgb.createGraphics().drawImage(i, 0, 0, null);
        return rgb;
    }

    public static BufferedImage prepareForDecoding(BufferedImage image) {
        ColorConvertOp op = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
        image = toRGB(image);
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
        formats.add(BarcodeFormat.CODE_128);
        hints.put(DecodeHintType.POSSIBLE_FORMATS, formats);

        binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(prepareForDecoding(image))));
        return new QRCodeMultiReader().decodeMultiple(binaryBitmap, hints);
    }

}
