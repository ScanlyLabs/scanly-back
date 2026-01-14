package scanly.io.scanly_back.common.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Map;

@Component
public class QrCodeGenerator {

    private static final int DEFAULT_WIDTH = 300;
    private static final int DEFAULT_HEIGHT = 300;

    @Value("${app.base-url}")
    private String baseUrl;

    /**
     * 명함 ID로 QR 코드 생성
     * @param cardId 명함 ID
     * @return Base64 인코딩된 QR 코드 이미지
     */
    public String generateQrCodeForCard(String cardId) {
        String url = baseUrl + "/api/cards/v1/" + cardId;
        return generateQrCode(url);
    }

    /**
     * URL로 QR 코드 생성
     * @param content QR 코드에 담을 내용
     * @return Base64 인코딩된 QR 코드 이미지
     */
    public String generateQrCode(String content) {
        return generateQrCode(content, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    /**
     * URL로 QR 코드 생성 (크기 지정)
     * @param content QR 코드에 담을 내용
     * @param width 너비
     * @param height 높이
     * @return Base64 인코딩된 QR 코드 이미지
     */
    public String generateQrCode(String content, int width, int height) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            Map<EncodeHintType, Object> hints = Map.of(
                    EncodeHintType.CHARACTER_SET, "UTF-8",
                    EncodeHintType.MARGIN, 1            // QR 주변 흰 여백
            );

            // 픽셀 정보 만들기
            BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);

            // 픽셀 정보 -> png 이미지 변환
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);

            return Base64.getEncoder().encodeToString(outputStream.toByteArray());
        } catch (WriterException | IOException e) {
            throw new RuntimeException("QR 코드 생성에 실패했습니다.", e); // 비즈니스 규칙 위반 X(시스템 에러)
        }
    }
}
