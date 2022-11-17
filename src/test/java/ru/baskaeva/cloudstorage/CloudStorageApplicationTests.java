package ru.baskaeva.cloudstorage;

import org.junit.ClassRule;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import ru.baskaeva.cloudstorage.dto.CredentialDTO;
import ru.baskaeva.cloudstorage.dto.FilenameDTO;
import ru.baskaeva.cloudstorage.dto.TokenDTO;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CloudStorageApplicationTests {

    TestRestTemplate restTemplate = new TestRestTemplate();
    String authToken;
    static URI url;

    @ClassRule
    public static DockerComposeContainer<?> compose =
            new DockerComposeContainer<>(
                    new File("C:\\CloudStorage\\docker-compose.yml"))
                    .withExposedService("mysqldb", 3306, Wait.forListeningPort())
                    .withExposedService("app", 8081, Wait.forListeningPort());


    @BeforeAll
    public static void setUp() throws URISyntaxException {
        compose.start();
        url = new URI("http://localhost:" + compose.getServicePort("app", 8081));
    }

    @Test
    void contextLoads() {

    }

    @Test
    @BeforeEach
    void authorizationTest() {
        CredentialDTO credentialDTO = new CredentialDTO("Mari", "1234");
        ResponseEntity<TokenDTO> response = restTemplate.postForEntity(url + "/login", credentialDTO, TokenDTO.class);
        authToken = response.getBody().getToken();

        Assertions.assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    @Order(1)
    void uploadTest() {

        HttpHeaders headers = new HttpHeaders();
        headers.add("auth-token", "Bearer " + authToken);
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, String> fileMap = new LinkedMultiValueMap<>();
        ContentDisposition contentDisposition = ContentDisposition
                .builder("form-data")
                .name("file")
                .filename("file.txt")
                .build();
        fileMap.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());
        HttpEntity<byte[]> fileEntity = new HttpEntity<>("Hello World!".getBytes(), fileMap);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", fileEntity);

        HttpEntity<MultiValueMap<String, Object>> requestEntity =
                new HttpEntity<>(body, headers);

        ResponseEntity<Void> responseFile = restTemplate.exchange(url + "/file?filename=file.txt", HttpMethod.POST, requestEntity, Void.class);

        Assertions.assertEquals(200, responseFile.getStatusCodeValue());
    }

    @Test
    @Order(2)
    void getListTestSuccess() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("auth-token", "Bearer " + authToken);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<? extends List> responseFile = restTemplate.exchange(url + "/list?limit=3", HttpMethod.GET, requestEntity, List.class);

        System.out.println(responseFile.getBody());

        Assertions.assertEquals(200, responseFile.getStatusCodeValue());
    }

    @Test
    void getListTestUnauthorized() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("auth-token", "Bearer " + "1234");
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<? extends List> responseFile = restTemplate.exchange(url + "/list?limit=3", HttpMethod.GET, requestEntity, List.class);

        Assertions.assertEquals(401, responseFile.getStatusCodeValue());
    }

    @Test
    @Order(4)
    void renameTestSuccess() {
        HttpHeaders headers = new HttpHeaders();
        FilenameDTO filenameDTO = new FilenameDTO();
        filenameDTO.setFilename("newfile.txt");
        headers.add("auth-token", "Bearer " + authToken);
        HttpEntity<FilenameDTO> requestEntity = new HttpEntity<>(filenameDTO, headers);

        ResponseEntity<Void> responseFile = restTemplate.exchange(url + "/file?filename=file.txt", HttpMethod.PUT, requestEntity, Void.class);

        System.out.println(responseFile.getStatusCode().getReasonPhrase());

        Assertions.assertEquals(200, responseFile.getStatusCodeValue());
    }

    @Test
    @Order(3)
    void downloadTestSuccess() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("auth-token", "Bearer " + authToken);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<byte[]> responseFile = restTemplate.exchange(url + "/file?filename=file.txt", HttpMethod.GET, requestEntity, byte[].class);

        System.out.println(responseFile.getStatusCode().getReasonPhrase());

        Assertions.assertEquals(200, responseFile.getStatusCodeValue());
    }

    @Test
    @Order(5)
    void deleteTestSuccess() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("auth-token", "Bearer " + authToken);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<Void> responseFile = restTemplate.exchange(url + "/file?filename=newfile.txt", HttpMethod.DELETE, requestEntity, Void.class);

        Assertions.assertEquals(200, responseFile.getStatusCodeValue());
    }
}

class MultipartInputStreamFileResource extends InputStreamResource {

    private final String filename;

    MultipartInputStreamFileResource(InputStream inputStream, String filename) {
        super(inputStream);
        this.filename = filename;
    }

    @Override
    public String getFilename() {
        return this.filename;
    }

    @Override
    public long contentLength() throws IOException {
        return -1;
    }
}