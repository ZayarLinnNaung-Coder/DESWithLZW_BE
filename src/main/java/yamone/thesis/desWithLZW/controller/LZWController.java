package yamone.thesis.desWithLZW.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import yamone.thesis.desWithLZW.model.SampleResponse;
import yamone.thesis.desWithLZW.utils.DESUtils;
import yamone.thesis.desWithLZW.utils.LZWUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.zip.DataFormatException;

@RestController
@RequestMapping("/lzw")
@AllArgsConstructor
@CrossOrigin
public class LZWController {

    @PostMapping("/compress/message")
    ResponseEntity<SampleResponse> compress(@RequestParam(value = "message", required = false) String message
    ) {
        return ResponseEntity.ok(new SampleResponse(LZWUtils.compress(message)));
    }

    @PostMapping("/compress/file")
    public ResponseEntity<byte[]> compressFile(@RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(LZWUtils.compressFile(file.getBytes()));
    }

    @PostMapping("/decompress/message")
    ResponseEntity<SampleResponse> decompress(@RequestParam(value = "message", required = false) String message
    ) {
        return ResponseEntity.ok(new SampleResponse(LZWUtils.decompress(message)));
    }

    @PostMapping("/decompress/file")
    public ResponseEntity<byte[]> decompressFile(@RequestParam("file") MultipartFile file) throws IOException, DataFormatException {
        return ResponseEntity.ok(LZWUtils.decompressFile(file.getBytes()));
    }

}