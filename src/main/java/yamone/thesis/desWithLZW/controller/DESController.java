package yamone.thesis.desWithLZW.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import yamone.thesis.desWithLZW.model.SampleResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import yamone.thesis.desWithLZW.utils.DESUtils;

import java.io.IOException;
import java.util.Base64;

@RestController
@RequestMapping("/des")
@AllArgsConstructor
@CrossOrigin
public class DESController {

    @PostMapping("/encrypt/message")
    ResponseEntity<SampleResponse> encrypt(@RequestParam String key,
                                           @RequestParam(value = "message", required = false) String message
    ) throws Exception {
        String binResponse = DESUtils.encrypt(key, DESUtils.utfToBin(message));
        return ResponseEntity.ok(new SampleResponse(DESUtils.binToHex(binResponse)));
    }

    @PostMapping("/encrypt/file")
    public ResponseEntity<byte[]> encryptFile(@RequestParam("file") MultipartFile file, @RequestParam String key) throws Exception {
        byte[] fileBytes = file.getBytes();

        String s = Base64.getEncoder().encodeToString(fileBytes);

        String encrypted = DESUtils.encrypt(key, DESUtils.utfToBin(s));

        byte[] encryptedFileBytes = Base64.getDecoder().decode(DESUtils.binToHex(encrypted));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=encryptedFile.enc")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(encryptedFileBytes);
    }

    @PostMapping("/decrypt/message")
    ResponseEntity<SampleResponse> decrypt(@RequestParam String key,
                                           @RequestParam(value = "message", required = false) String message
    ) throws Exception {
        String binResponse = DESUtils.decrypt(key, DESUtils.hexToBin(message));
        return ResponseEntity.ok(new SampleResponse(DESUtils.binToUTF(binResponse)));
    }

    @PostMapping("/decrypt/file")
    public ResponseEntity<byte[]> decryptFile(@RequestParam("file") MultipartFile file, @RequestParam String key) throws Exception {
        byte[] fileBytes = file.getBytes();
        String s = Base64.getEncoder().encodeToString(fileBytes);
        String decryptResult = DESUtils.binToUTF(DESUtils.decrypt(key, DESUtils.hexToBin(s)));

        byte[] decryptedFileBytes = Base64.getDecoder().decode(decryptResult);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=encryptedFile.enc")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(decryptedFileBytes);
    }

}
