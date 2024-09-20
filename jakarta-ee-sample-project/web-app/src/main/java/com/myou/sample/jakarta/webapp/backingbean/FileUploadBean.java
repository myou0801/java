package com.myou.sample.jakarta.webapp.backingbean;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import jakarta.servlet.http.Part;
import org.primefaces.model.file.UploadedFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Named
@RequestScoped
public class FileUploadBean {

    private Part uploadedFile2;

    public Part getUploadedFile2() {
        return uploadedFile2;
    }

    public void setUploadedFile2(Part uploadedFile2) {
        this.uploadedFile2 = uploadedFile2;
    }

    private UploadedFile uploadedFile;

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public String upload() {
            if (uploadedFile != null) {
            try {
                Path destination = Paths.get("/path/to/destination/", uploadedFile2.getSubmittedFileName());
                Files.copy(uploadedFile2.getInputStream(), destination);
                return "成功: ファイルがアップロードされました!";
            } catch (Exception e) {
                e.printStackTrace();
                return "エラー: アップロードに失敗しました";
            }
        } else {
            return "エラー: ファイルが選択されていません";
        }
    }
}
