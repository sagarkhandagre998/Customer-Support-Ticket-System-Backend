// package com.application.Controllers;


// import com.application.Entities.Ticket;
// import com.application.Repository.TicketRepository;
// import com.application.Services.FileStorageService;
// import lombok.RequiredArgsConstructor;
// import org.springframework.core.io.FileSystemResource;
// import org.springframework.http.*;
// import org.springframework.web.bind.annotation.*;
// import org.springframework.web.multipart.MultipartFile;

// import java.io.File;
// import java.io.IOException;

// @RestController
// @RequestMapping("/tickets/{ticketId}/attachments")
// @RequiredArgsConstructor
// public class TicketAttachmentController {

//     private final TicketRepository ticketRepository;
//     private final FileStorageService fileStorageService;

//     // 📤 Upload file
//     @PostMapping
//     public ResponseEntity<String> uploadFile(@PathVariable Long ticketId,
//                                              @RequestParam("file") MultipartFile file) throws IOException {
//         Ticket ticket = ticketRepository.findById(ticketId)
//                 .orElseThrow(() -> new RuntimeException("Ticket not found"));

//         String path = fileStorageService.saveFile(file);
//         ticket.getAttachments().add(path);
//         ticketRepository.save(ticket);

//         return ResponseEntity.ok("File uploaded successfully: " + path);
//     }

//     // 📥 Download file
//     @GetMapping
//     public ResponseEntity<FileSystemResource> downloadFile(@PathVariable Long ticketId,
//                                                            @RequestParam String path) {
//         File file = fileStorageService.getFile(path);

//         if (!file.exists()) {
//             return ResponseEntity.notFound().build();
//         }

//         FileSystemResource resource = new FileSystemResource(file);
//         return ResponseEntity.ok()
//                 .contentType(MediaType.APPLICATION_OCTET_STREAM)
//                 .body(resource);
//     }
// }
