package scanly.io.scanly_back.common.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    private final String AWS_S3_URL = "https://%s.s3.%s.amazonaws.com/";

    /**
     * 바이트 배열 S3 업로드
     * @param bytes 업로드할 바이트 배열
     * @param directory 저장할 디렉토리 경로
     * @param extension 파일 확장자
     * @param contentType 파일 Content-Type
     * @return 업로드된 파일의 S3 URL
     */
    public String uploadBytes(byte[] bytes, String directory, String extension, String contentType) {
        String fileName = directory + "/" + UUID.randomUUID() + "." + extension;

        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(fileName)
                    .contentType(contentType)
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(bytes));

            return getFileUrl(fileName);
        } catch (S3Exception e) {
            log.error("S3 업로드 중 오류 발생: {}", e.getMessage());
            throw new RuntimeException("S3 업로드에 실패했습니다.", e);
        }
    }

    /**
     * S3 파일 URL 생성
     * @param fileName S3 파일 키
     * @return S3 파일 URL
     */
    private String getFileUrl(String fileName) {
        return String.format(AWS_S3_URL+"%s", bucket, region, fileName);
    }

    /**
     * S3 파일 삭제
     * @param fileUrl 삭제할 파일의 S3 URL
     */
    public void delete(String fileUrl) {
        String fileName = extractFileNameFromUrl(fileUrl);

        try {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(fileName)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);
            log.info("파일 삭제 완료: {}", fileName);
        } catch (S3Exception e) {
            log.error("S3 파일 삭제 중 오류 발생: {}", e.getMessage());
            throw new RuntimeException("S3 파일 삭제에 실패했습니다.", e);
        }
    }

    /**
     * S3 URL에서 파일 키 추출
     * @param fileUrl S3 파일 URL
     * @return S3 파일 키
     */
    private String extractFileNameFromUrl(String fileUrl) {
        String prefix = String.format(AWS_S3_URL, bucket, region);
        return fileUrl.replace(prefix, "");
    }
}
