package com.example.demo.controller;

import com.example.demo.storage.StorageFileNotFoundException;
import com.example.demo.storage.StorageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class FileUploadRestController {
    private final StorageService storageService;
    
    @Autowired
    public FileUploadRestController(StorageService storageService) {
        this.storageService = storageService;
    }
    
    @GetMapping("/upload-file")
    public String listUploadedFiles(Model model) throws IOException {
        model.addAttribute("files", storageService.loadAll().map(path -> MvcUriComponentsBuilder.fromMethodName(
            FileUploadRestController.class,
            "serveFile",
            path.getFileName().toString()).build().toUri().toString())
            .collect(Collectors.toList()));
        
        return "uploadForm";
    }
    
    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        Resource file = storageService.loadAsResource(filename);
        
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
    
    @PostMapping("/upload-file")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        storageService.store(file);
        redirectAttributes.addFlashAttribute("message",
            "You successfully uploaded " + file.getOriginalFilename() + "!");
        
        return "redirect:/";
    }
    
    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }
    
    /*public void readCSVFile(String pathToCsv) throws FileNotFoundException {
        try (BufferedReader csvReader = new BufferedReader(new FileReader(pathToCsv))) {
            
            while ((row = csvReader.readLine()) != null) {
                String[] data = row.split(",");
                // do something with the data
            }
            csvReader.close();
            
        } catch (Exception e) {
        
        }
    }*/
    
    public void writeCSVFile() throws IOException {
        // Our example data
        List<List<String>> rows = Arrays.asList(
            Arrays.asList("Jean", "author", "Java"),
            Arrays.asList("David", "editor", "Python"),
            Arrays.asList("Scott", "editor", "Node.js")
        );
        
        try (FileWriter csvWriter = new FileWriter("new.csv")) {
            csvWriter.append("Name");
            csvWriter.append(",");
            csvWriter.append("Role");
            csvWriter.append(",");
            csvWriter.append("Topic");
            csvWriter.append("\n");
            
            for (List<String> rowData : rows) {
                csvWriter.append(String.join(",", rowData));
                csvWriter.append("\n");
            }
            
            csvWriter.flush();
            csvWriter.close();
            
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
